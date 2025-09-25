---
applyTo: "**/modules/**/*.ts,**/main.ts,**/*.module.ts"
description: "Padrões arquiteturais para aplicações NestJS escaláveis e maintíveis"
---

# Instruções de Arquitetura NestJS

## Objetivo
Estabelecer padrões arquiteturais consistentes para aplicações NestJS escaláveis, mantendo separação de responsabilidades e alta coesão.

## Escopo
- Estrutura de módulos e organização
- Clean Architecture com NestJS
- Padrões de injeção de dependência
- Configuração de aplicação
- Escalabilidade e performance
- Multi-tenant e ambiente distribuído

## Pessoal vs Enterprise

### Projetos Pessoais (Foco: Simplicidade)
- Estrutura modular básica
- Configuração centralizada simples
- Injeção de dependência direta
- Monolito modular

### Projetos Enterprise (Foco: Escalabilidade)
- Arquitetura hexagonal completa
- Módulos auto-contidos
- Feature flags e configuração por ambiente
- Microserviços quando apropriado
- Multi-tenant architecture
- Observabilidade completa

## Arquitetura Base - Clean Architecture

### Estrutura de Diretórios
```
src/
├── main.ts                          # Bootstrap da aplicação
├── app.module.ts                    # Módulo raiz
├── shared/                          # Código compartilhado
│   ├── config/                      # Configurações globais
│   ├── exceptions/                  # Exception filters globais
│   ├── guards/                      # Guards globais
│   ├── interceptors/                # Interceptors globais
│   ├── pipes/                       # Pipes globais
│   └── utils/                       # Utilitários
├── modules/                         # Módulos de funcionalidade
│   └── users/                       # Exemplo: módulo de usuários
│       ├── users.module.ts
│       ├── domain/                  # Domain layer
│       │   ├── entities/
│       │   ├── value-objects/
│       │   ├── repositories/        # Interfaces
│       │   └── services/
│       ├── application/             # Application layer  
│       │   ├── use-cases/
│       │   ├── dto/
│       │   ├── handlers/
│       │   └── services/
│       ├── infrastructure/          # Infrastructure layer
│       │   ├── repositories/        # Implementações
│       │   ├── database/
│       │   └── external/
│       └── presentation/            # Presentation layer
│           ├── controllers/
│           ├── guards/
│           ├── pipes/
│           └── dto/
└── database/                        # Database configurations
    ├── migrations/
    ├── seeds/
    └── config/
```

## Módulos e Organização

### Módulo Base Template
```typescript
// modules/users/users.module.ts
@Module({
  imports: [
    // External modules
    TypeOrmModule.forFeature([UserEntity]),
    
    // Internal modules
    SharedModule,
    EventsModule,
  ],
  controllers: [
    UsersController,
  ],
  providers: [
    // Use Cases
    CreateUserUseCase,
    UpdateUserUseCase,
    DeleteUserUseCase,
    GetUserUseCase,
    
    // Services
    UserDomainService,
    UserApplicationService,
    
    // Repositories
    {
      provide: 'IUserRepository',
      useClass: TypeOrmUserRepository,
    },
    
    // External Services
    {
      provide: 'IEmailService',
      useClass: SendGridEmailService,
    },
    
    // Event Handlers
    UserCreatedHandler,
    UserUpdatedHandler,
  ],
  exports: [
    'IUserRepository',
    UserApplicationService,
  ],
})
export class UsersModule {}
```

### Auto-registering Modules
```typescript
// shared/decorators/auto-register.decorator.ts
export const AUTO_REGISTER_METADATA = 'AUTO_REGISTER';

export function AutoRegister(config: {
  token?: string | symbol;
  scope?: Scope;
}): ClassDecorator {
  return (target: any) => {
    Reflect.defineMetadata(AUTO_REGISTER_METADATA, config, target);
  };
}

// shared/modules/auto-register.module.ts
@Module({})
export class AutoRegisterModule {
  static forRoot(): DynamicModule {
    const providers = this.discoverProviders();
    
    return {
      module: AutoRegisterModule,
      providers,
      exports: providers.map(p => p.provide),
      global: true,
    };
  }
  
  private static discoverProviders(): Provider[] {
    // Implementation to scan and auto-register providers
    // Based on decorators and file patterns
  }
}
```

## Injeção de Dependência Avançada

### Factory Providers com Configuração
```typescript
// modules/users/users.providers.ts
export const USER_PROVIDERS: Provider[] = [
  {
    provide: 'IUserRepository',
    useFactory: (
      dataSource: DataSource,
      config: ConfigService,
      logger: Logger
    ): IUserRepository => {
      const dbType = config.get<string>('database.type');
      
      switch (dbType) {
        case 'mongodb':
          return new MongoUserRepository(dataSource, logger);
        case 'postgresql':
          return new PostgreSQLUserRepository(dataSource, logger);
        default:
          return new InMemoryUserRepository(logger);
      }
    },
    inject: [DataSource, ConfigService, Logger],
  },
  
  {
    provide: 'IEmailService',
    useFactory: (config: ConfigService): IEmailService => {
      const provider = config.get<string>('email.provider');
      const apiKey = config.get<string>('email.apiKey');
      
      switch (provider) {
        case 'sendgrid':
          return new SendGridService(apiKey);
        case 'ses':
          return new SESService(config.get('aws'));
        default:
          return new ConsoleEmailService();
      }
    },
    inject: [ConfigService],
  },
];
```

### Conditional Providers
```typescript
// shared/providers/conditional.provider.ts
export function createConditionalProvider<T>(
  token: string | symbol,
  condition: (config: ConfigService) => boolean,
  trueProvider: Type<T>,
  falseProvider: Type<T>
): Provider {
  return {
    provide: token,
    useFactory: (config: ConfigService) => {
      const ProviderClass = condition(config) ? trueProvider : falseProvider;
      return new ProviderClass();
    },
    inject: [ConfigService],
  };
}

// Usage
const EMAIL_PROVIDER = createConditionalProvider(
  'IEmailService',
  (config) => config.get('NODE_ENV') === 'production',
  SendGridService,
  ConsoleEmailService
);
```

## Configuração de Aplicação

### Configuration Schema
```typescript
// shared/config/config.schema.ts
export interface AppConfig {
  app: {
    name: string;
    version: string;
    port: number;
    environment: 'development' | 'staging' | 'production';
  };
  database: {
    type: 'postgresql' | 'mongodb';
    host: string;
    port: number;
    username: string;
    password: string;
    database: string;
    ssl: boolean;
  };
  auth: {
    jwtSecret: string;
    jwtExpiresIn: string;
    refreshTokenExpiresIn: string;
  };
  external: {
    email: {
      provider: 'sendgrid' | 'ses';
      apiKey: string;
    };
    payment: {
      provider: 'stripe' | 'paypal';
      webhookSecret: string;
    };
  };
  features: {
    userRegistration: boolean;
    socialLogin: boolean;
    multiTenant: boolean;
  };
}
```

### Configuration Validation
```typescript
// shared/config/config.validation.ts
import Joi from 'joi';

export const configValidationSchema = Joi.object({
  NODE_ENV: Joi.string()
    .valid('development', 'staging', 'production')
    .default('development'),
    
  PORT: Joi.number().port().default(3000),
  
  // Database
  DB_TYPE: Joi.string().valid('postgresql', 'mongodb').required(),
  DB_HOST: Joi.string().required(),
  DB_PORT: Joi.number().port().required(),
  DB_USERNAME: Joi.string().required(),
  DB_PASSWORD: Joi.string().required(),
  DB_DATABASE: Joi.string().required(),
  
  // Auth
  JWT_SECRET: Joi.string().min(32).required(),
  JWT_EXPIRES_IN: Joi.string().default('15m'),
  
  // Feature Flags
  ENABLE_USER_REGISTRATION: Joi.boolean().default(true),
  ENABLE_SOCIAL_LOGIN: Joi.boolean().default(false),
  ENABLE_MULTI_TENANT: Joi.boolean().default(false),
});

// shared/config/config.service.ts
@Injectable()
export class TypedConfigService {
  constructor(private configService: ConfigService) {}
  
  get app(): AppConfig['app'] {
    return {
      name: this.configService.get<string>('APP_NAME', 'MyApp'),
      version: this.configService.get<string>('APP_VERSION', '1.0.0'),
      port: this.configService.get<number>('PORT', 3000),
      environment: this.configService.get<AppConfig['app']['environment']>('NODE_ENV', 'development'),
    };
  }
  
  get database(): AppConfig['database'] {
    return {
      type: this.configService.get<AppConfig['database']['type']>('DB_TYPE'),
      host: this.configService.get<string>('DB_HOST'),
      port: this.configService.get<number>('DB_PORT'),
      username: this.configService.get<string>('DB_USERNAME'),
      password: this.configService.get<string>('DB_PASSWORD'),
      database: this.configService.get<string>('DB_DATABASE'),
      ssl: this.configService.get<boolean>('DB_SSL', false),
    };
  }
  
  get features(): AppConfig['features'] {
    return {
      userRegistration: this.configService.get<boolean>('ENABLE_USER_REGISTRATION', true),
      socialLogin: this.configService.get<boolean>('ENABLE_SOCIAL_LOGIN', false),
      multiTenant: this.configService.get<boolean>('ENABLE_MULTI_TENANT', false),
    };
  }
}
```

### Bootstrap Configuration
```typescript
// main.ts
async function bootstrap() {
  const app = await NestFactory.create(AppModule, {
    logger: ['error', 'warn', 'log'],
  });
  
  const configService = app.get(TypedConfigService);
  const config = configService.app;
  
  // Global configuration
  app.setGlobalPrefix('api/v1');
  
  // Security
  app.use(helmet());
  app.enableCors({
    origin: config.environment === 'production' 
      ? process.env.ALLOWED_ORIGINS?.split(',') 
      : true,
    credentials: true,
  });
  
  // Validation
  app.useGlobalPipes(new ValidationPipe({
    transform: true,
    whitelist: true,
    forbidNonWhitelisted: true,
  }));
  
  // Exception handling
  app.useGlobalFilters(new GlobalExceptionFilter());
  
  // Interceptors
  app.useGlobalInterceptors(
    new LoggingInterceptor(),
    new TimeoutInterceptor(30000),
  );
  
  // Swagger
  if (config.environment !== 'production') {
    const document = SwaggerModule.createDocument(app, swaggerConfig);
    SwaggerModule.setup('api/docs', app, document);
  }
  
  // Health checks
  const healthController = app.get(HealthController);
  app.use('/health', healthController.check);
  
  await app.listen(config.port);
  
  Logger.log(`Application running on port ${config.port}`, 'Bootstrap');
  Logger.log(`Environment: ${config.environment}`, 'Bootstrap');
}
```

## Escalabilidade e Performance

### Caching Strategy
```typescript
// shared/cache/cache.module.ts
@Module({})
export class CacheModule {
  static forRoot(options: CacheModuleOptions = {}): DynamicModule {
    return {
      module: CacheModule,
      imports: [
        CacheModule.forFeature({
          store: redisStore,
          host: process.env.REDIS_HOST,
          port: parseInt(process.env.REDIS_PORT || '6379'),
          ttl: options.ttl || 60 * 60, // 1 hour
        }),
      ],
      providers: [
        CacheService,
        {
          provide: 'CACHE_OPTIONS',
          useValue: options,
        },
      ],
      exports: [CacheService],
      global: true,
    };
  }
}

// shared/cache/cache.service.ts
@Injectable()
export class CacheService {
  constructor(@Inject(CACHE_MANAGER) private cacheManager: Cache) {}
  
  async get<T>(key: string): Promise<T | undefined> {
    return this.cacheManager.get<T>(key);
  }
  
  async set<T>(key: string, value: T, ttl?: number): Promise<void> {
    await this.cacheManager.set(key, value, ttl);
  }
  
  async del(key: string): Promise<void> {
    await this.cacheManager.del(key);
  }
  
  async getOrSet<T>(
    key: string,
    factory: () => Promise<T>,
    ttl?: number
  ): Promise<T> {
    let value = await this.get<T>(key);
    
    if (value === undefined) {
      value = await factory();
      await this.set(key, value, ttl);
    }
    
    return value;
  }
}
```

### Database Optimization
```typescript
// shared/database/database.module.ts
@Module({})
export class DatabaseModule {
  static forRoot(): DynamicModule {
    return {
      module: DatabaseModule,
      imports: [
        TypeOrmModule.forRootAsync({
          useFactory: (config: TypedConfigService): TypeOrmModuleOptions => ({
            type: 'postgres',
            ...config.database,
            entities: [__dirname + '/../**/*.entity{.ts,.js}'],
            migrations: [__dirname + '/migrations/*{.ts,.js}'],
            synchronize: false,
            logging: config.app.environment === 'development' ? 'all' : ['error'],
            
            // Connection pooling
            poolSize: 20,
            maxQueryExecutionTime: 1000,
            
            // Performance
            cache: {
              duration: 30000, // 30 seconds
            },
            
            // Read replicas for scaling reads
            replication: config.app.environment === 'production' ? {
              master: config.database,
              slaves: [
                // Read replica configurations
              ],
            } : undefined,
          }),
          inject: [TypedConfigService],
        }),
      ],
      providers: [DatabaseHealthService],
      exports: [DatabaseHealthService],
    };
  }
}
```

## Multi-tenant Architecture

### Tenant Context
```typescript
// shared/multi-tenant/tenant.context.ts
export class TenantContext {
  private static tenantId: string | null = null;
  
  static setTenant(tenantId: string): void {
    this.tenantId = tenantId;
  }
  
  static getTenant(): string | null {
    return this.tenantId;
  }
  
  static clearTenant(): void {
    this.tenantId = null;
  }
}

// shared/multi-tenant/tenant.interceptor.ts
@Injectable()
export class TenantInterceptor implements NestInterceptor {
  intercept(context: ExecutionContext, next: CallHandler): Observable<any> {
    const request = context.switchToHttp().getRequest();
    const tenantId = this.extractTenantId(request);
    
    if (tenantId) {
      TenantContext.setTenant(tenantId);
    }
    
    return next.handle().pipe(
      finalize(() => TenantContext.clearTenant())
    );
  }
  
  private extractTenantId(request: any): string | null {
    // Extract from header, subdomain, or JWT token
    return request.headers['x-tenant-id'] || 
           this.extractFromSubdomain(request.headers.host) ||
           this.extractFromJWT(request.headers.authorization);
  }
}
```

### Tenant-aware Repository
```typescript
// infrastructure/repositories/tenant-aware.repository.ts
export abstract class TenantAwareRepository<T> {
  constructor(protected repository: Repository<T>) {}
  
  protected addTenantFilter(query: SelectQueryBuilder<T>): void {
    const tenantId = TenantContext.getTenant();
    if (tenantId) {
      query.andWhere('entity.tenantId = :tenantId', { tenantId });
    }
  }
  
  async find(options?: FindManyOptions<T>): Promise<T[]> {
    const query = this.repository.createQueryBuilder('entity');
    this.addTenantFilter(query);
    
    if (options?.where) {
      query.andWhere(options.where as any);
    }
    
    return query.getMany();
  }
}
```

## Observabilidade e Monitoring

### Structured Logging
```typescript
// shared/logging/logger.service.ts
@Injectable()
export class StructuredLogger {
  private readonly logger = new Logger(StructuredLogger.name);
  
  log(message: string, context?: Record<string, any>): void {
    this.logger.log(this.formatMessage(message, context));
  }
  
  error(message: string, error?: Error, context?: Record<string, any>): void {
    this.logger.error(this.formatMessage(message, {
      ...context,
      error: error?.message,
      stack: error?.stack,
    }));
  }
  
  private formatMessage(message: string, context?: Record<string, any>): string {
    const logEntry = {
      timestamp: new Date().toISOString(),
      message,
      tenantId: TenantContext.getTenant(),
      correlationId: this.getCorrelationId(),
      ...context,
    };
    
    return JSON.stringify(logEntry);
  }
  
  private getCorrelationId(): string | null {
    // Implementation to get correlation ID from context
  }
}
```

### Health Checks
```typescript
// shared/health/health.controller.ts
@Controller('health')
export class HealthController {
  constructor(
    private health: HealthCheckService,
    private db: TypeOrmHealthIndicator,
    private redis: RedisHealthIndicator,
  ) {}
  
  @Get()
  @HealthCheck()
  check() {
    return this.health.check([
      () => this.db.pingCheck('database'),
      () => this.redis.checkHealth('redis'),
      () => this.checkExternalServices(),
    ]);
  }
  
  private async checkExternalServices(): Promise<HealthIndicatorResult> {
    // Check external API dependencies
    return {
      external: {
        status: 'up',
        services: ['email-service', 'payment-gateway']
      }
    };
  }
}
```

### Metrics Collection
```typescript
// shared/metrics/metrics.service.ts
@Injectable()
export class MetricsService {
  private readonly httpRequestsTotal = new Counter({
    name: 'http_requests_total',
    help: 'Total number of HTTP requests',
    labelNames: ['method', 'route', 'status_code', 'tenant_id'],
  });
  
  private readonly httpRequestDuration = new Histogram({
    name: 'http_request_duration_seconds',
    help: 'Duration of HTTP requests in seconds',
    labelNames: ['method', 'route', 'tenant_id'],
    buckets: [0.001, 0.01, 0.1, 1, 5],
  });
  
  recordHttpRequest(
    method: string, 
    route: string, 
    statusCode: number, 
    duration: number
  ): void {
    const tenantId = TenantContext.getTenant() || 'unknown';
    
    this.httpRequestsTotal
      .labels(method, route, statusCode.toString(), tenantId)
      .inc();
      
    this.httpRequestDuration
      .labels(method, route, tenantId)
      .observe(duration);
  }
}
```

## Event-Driven Architecture

### Event Bus Configuration
```typescript
// shared/events/event-bus.module.ts
@Module({
  imports: [EventEmitterModule.forRoot()],
  providers: [
    {
      provide: 'IEventBus',
      useClass: NestJSEventBus,
    },
  ],
  exports: ['IEventBus'],
})
export class EventBusModule {}

// shared/events/event-bus.interface.ts
export interface IEventBus {
  publish<T extends DomainEvent>(event: T): Promise<void>;
  subscribe<T extends DomainEvent>(
    eventType: string,
    handler: EventHandler<T>
  ): void;
}
```

### CQRS Integration
```typescript
// shared/cqrs/cqrs.module.ts
@Module({
  imports: [CqrsModule],
  providers: [
    QueryBus,
    CommandBus,
    EventBus,
  ],
  exports: [QueryBus, CommandBus, EventBus],
})
export class SharedCqrsModule {}

// Example usage in controller
@Controller('users')
export class UsersController {
  constructor(
    private readonly commandBus: CommandBus,
    private readonly queryBus: QueryBus,
  ) {}
  
  @Post()
  async createUser(@Body() dto: CreateUserDto): Promise<UserResponseDto> {
    const command = new CreateUserCommand(dto);
    const result = await this.commandBus.execute(command);
    return result;
  }
  
  @Get(':id')
  async getUser(@Param('id') id: string): Promise<UserResponseDto> {
    const query = new GetUserQuery(id);
    const result = await this.queryBus.execute(query);
    return result;
  }
}
```

## Checklist Arquitetural

### ✅ Estrutura e Organização
- [ ] Módulos organizados por feature/domínio
- [ ] Clean Architecture layers respeitadas
- [ ] Separação clara de responsabilidades
- [ ] Injeção de dependência adequada
- [ ] Interfaces para abstrações

### ✅ Configuração e Environment
- [ ] Configuração tipada e validada
- [ ] Environment variables seguras
- [ ] Feature flags implementadas
- [ ] Configuração por ambiente

### ✅ Performance e Escalabilidade
- [ ] Cache strategy implementada
- [ ] Database optimization
- [ ] Connection pooling configurado
- [ ] Read/write split quando necessário

### ✅ Observabilidade
- [ ] Logging estruturado
- [ ] Health checks implementados
- [ ] Metrics collection
- [ ] Distributed tracing (quando aplicável)

### ✅ Multi-tenant (se aplicável)
- [ ] Tenant isolation
- [ ] Tenant-aware repositories
- [ ] Security boundaries

## Métricas Arquiteturais

### KPIs para Enterprise
- **Module Cohesion**: >80% das classes por módulo relacionadas
- **Coupling**: <20% de dependências cruzadas entre domínios
- **Response Time**: <200ms para 95% das requests
- **Availability**: >99.9% uptime
- **Scalability**: Linear scaling até 10x load

## Referências Técnicas
- [NestJS Official Documentation](https://docs.nestjs.com/)
- [Clean Architecture by Robert Martin](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Microservices Patterns by Chris Richardson](https://microservices.io/)
- [Building Microservices by Sam Newman](https://samnewman.io/books/building_microservices/)
