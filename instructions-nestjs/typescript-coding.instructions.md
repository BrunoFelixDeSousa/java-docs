---
applyTo: "**/*.ts,**/*.js"
description: "Padrões de codificação TypeScript para aplicações NestJS profissionais"
---

# Instruções de Codificação TypeScript

## Objetivo
Estabelecer padrões consistentes de codificação TypeScript para garantir legibilidade, manutenibilidade e qualidade do código em aplicações NestJS.

## Escopo
- Convenções de nomenclatura e estilo
- Estrutura de arquivos e organização
- Padrões de tipos e interfaces
- Error handling e logging
- Performance e otimizações
- Segurança do código

## Pessoal vs Enterprise

### Projetos Pessoais (Foco: Rapidez)
- ESLint + Prettier configuração básica
- Tipos básicos obrigatórios (sem any)
- Naming conventions essenciais
- Error handling simples

### Projetos Enterprise (Foco: Qualidade)
- ESLint strict + regras customizadas
- Tipos complexos e generics
- Documentação JSDoc obrigatória
- Error handling robusto com logging
- Code reviews obrigatórios
- Métricas de qualidade automatizadas

## Configuração de Ferramentas

### TypeScript Configuration
```json
// tsconfig.json
{
  "compilerOptions": {
    "target": "ES2022",
    "module": "commonjs",
    "lib": ["ES2022"],
    "declaration": true,
    "removeComments": true,
    "emitDecoratorMetadata": true,
    "experimentalDecorators": true,
    "allowSyntheticDefaultImports": true,
    "sourceMap": true,
    "outDir": "./dist",
    "baseUrl": "./",
    "paths": {
      "@/*": ["src/*"],
      "@domain/*": ["src/domain/*"],
      "@application/*": ["src/application/*"],
      "@infrastructure/*": ["src/infrastructure/*"]
    },
    "incremental": true,
    "skipLibCheck": true,
    "strictNullChecks": true,
    "noImplicitAny": true,
    "strictBindCallApply": true,
    "forceConsistentCasingInFileNames": true,
    "noFallthroughCasesInSwitch": true,
    "strictPropertyInitialization": true,
    "noImplicitReturns": true,
    "noUnusedLocals": true,
    "noUnusedParameters": true
  },
  "include": ["src/**/*"],
  "exclude": ["node_modules", "dist", "**/*.spec.ts", "**/*.test.ts"]
}
```

### ESLint Configuration
```javascript
// .eslintrc.js
module.exports = {
  parser: '@typescript-eslint/parser',
  parserOptions: {
    project: 'tsconfig.json',
    tsconfigRootDir: __dirname,
    sourceType: 'module',
  },
  plugins: ['@typescript-eslint/eslint-plugin'],
  extends: [
    '@typescript-eslint/recommended',
    '@typescript-eslint/recommended-requiring-type-checking',
    'prettier',
  ],
  root: true,
  env: {
    node: true,
    jest: true,
  },
  ignorePatterns: ['.eslintrc.js', 'dist/**'],
  rules: {
    '@typescript-eslint/no-explicit-any': 'error',
    '@typescript-eslint/no-unused-vars': 'error',
    '@typescript-eslint/explicit-function-return-type': 'warn',
    '@typescript-eslint/no-empty-function': 'error',
    '@typescript-eslint/prefer-readonly': 'error',
    '@typescript-eslint/prefer-nullish-coalescing': 'error',
    '@typescript-eslint/prefer-optional-chain': 'error',
    'prefer-const': 'error',
    'no-var': 'error',
    'object-shorthand': 'error',
    'prefer-template': 'error',
  },
};
```

### Prettier Configuration
```json
// .prettierrc
{
  "semi": true,
  "trailingComma": "all",
  "singleQuote": true,
  "printWidth": 80,
  "tabWidth": 2,
  "useTabs": false,
  "bracketSpacing": true,
  "arrowParens": "avoid"
}
```

## Convenções de Nomenclatura

### Classes e Interfaces
```typescript
// ✅ Classes - PascalCase
export class UserService {}
export class PaymentProcessor {}
export class EmailNotificationHandler {}

// ✅ Interfaces - PascalCase com prefixo I (opcional)
export interface IUserRepository {}
export interface UserRepository {} // Alternativa sem prefixo

// ✅ Abstract Classes - PascalCase
export abstract class BaseEntity {}

// ✅ Enums - PascalCase
export enum UserStatus {
  ACTIVE = 'active',
  INACTIVE = 'inactive',
  SUSPENDED = 'suspended',
}
```

### Métodos e Propriedades
```typescript
// ✅ Métodos - camelCase, verbos descritivos
class UserService {
  async createUser(userData: CreateUserDto): Promise<User> {}
  async findUserById(id: string): Promise<User | null> {}
  async updateUserEmail(id: string, email: string): Promise<void> {}
  async deleteUser(id: string): Promise<void> {}
  
  // ✅ Propriedades - camelCase, substantivos
  private readonly userRepository: IUserRepository;
  private readonly maxRetryAttempts: number = 3;
  private isInitialized: boolean = false;
}
```

### Constantes e Configurações
```typescript
// ✅ Constantes - UPPER_SNAKE_CASE
export const MAX_PASSWORD_ATTEMPTS = 5;
export const DEFAULT_PAGINATION_LIMIT = 20;
export const API_VERSION = 'v1';

// ✅ Configurações - camelCase em objetos
export const databaseConfig = {
  host: process.env.DB_HOST,
  port: parseInt(process.env.DB_PORT ?? '5432'),
  maxConnections: 10,
} as const;
```

### Arquivos e Diretórios
```typescript
// ✅ Arquivos - kebab-case
user.service.ts
create-user.dto.ts
user-created.event.ts
payment-processor.service.ts

// ✅ Diretórios - kebab-case
src/
├── domain/
├── application/
│   ├── use-cases/
│   ├── event-handlers/
│   └── query-handlers/
├── infrastructure/
│   ├── database/
│   ├── external-services/
│   └── message-queues/
└── presentation/
    ├── controllers/
    ├── middlewares/
    └── dto/
```

## Tipagem TypeScript

### Tipos Básicos e Utilitários
```typescript
// ✅ Usar tipos específicos, evitar any
type UserId = string;
type UserEmail = string;
type UserAge = number;

// ✅ Utility Types para manipulação
type CreateUserRequest = Omit<User, 'id' | 'createdAt' | 'updatedAt'>;
type UpdateUserRequest = Partial<Pick<User, 'name' | 'email'>>;
type UserSummary = Pick<User, 'id' | 'name' | 'email'>;

// ✅ Union Types para estados
type PaymentStatus = 'pending' | 'processing' | 'completed' | 'failed';
type HttpMethod = 'GET' | 'POST' | 'PUT' | 'DELETE' | 'PATCH';
```

### Interfaces e Contratos
```typescript
// ✅ Interfaces para contratos de serviço
export interface IEmailService {
  sendWelcomeEmail(email: string, name: string): Promise<void>;
  sendPasswordResetEmail(email: string, token: string): Promise<void>;
}

// ✅ Interfaces para DTOs
export interface CreateUserDto {
  readonly name: string;
  readonly email: string;
  readonly password: string;
}

// ✅ Interfaces genéricas
export interface Repository<TEntity, TId> {
  save(entity: TEntity): Promise<TEntity>;
  findById(id: TId): Promise<TEntity | null>;
  delete(id: TId): Promise<void>;
}
```

### Generics e Tipos Avançados
```typescript
// ✅ Generics para reutilização
export class Result<TValue, TError = Error> {
  private constructor(
    private readonly _value?: TValue,
    private readonly _error?: TError,
    private readonly _isSuccess: boolean = true,
  ) {}

  static success<T>(value: T): Result<T> {
    return new Result<T>(value, undefined, true);
  }

  static failure<T, E>(error: E): Result<T, E> {
    return new Result<T, E>(undefined, error, false);
  }

  get isSuccess(): boolean {
    return this._isSuccess;
  }

  get value(): TValue {
    if (!this._isSuccess) {
      throw new Error('Cannot access value of failed result');
    }
    return this._value!;
  }

  get error(): TError {
    if (this._isSuccess) {
      throw new Error('Cannot access error of successful result');
    }
    return this._error!;
  }
}
```

### Type Guards e Validação
```typescript
// ✅ Type Guards para runtime validation
export function isValidEmail(email: unknown): email is string {
  return typeof email === 'string' && /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
}

export function isUser(obj: unknown): obj is User {
  return (
    typeof obj === 'object' &&
    obj !== null &&
    'id' in obj &&
    'email' in obj &&
    'name' in obj
  );
}

// ✅ Uso dos Type Guards
function processUserData(data: unknown): Result<User, string> {
  if (!isUser(data)) {
    return Result.failure('Invalid user data');
  }

  return Result.success(data);
}
```

## Error Handling

### Hierarquia de Exceções
```typescript
// ✅ Base Exception Class
export abstract class AppException extends Error {
  abstract readonly code: string;
  abstract readonly statusCode: number;

  constructor(
    message: string,
    public readonly context?: Record<string, unknown>,
  ) {
    super(message);
    this.name = this.constructor.name;
    Error.captureStackTrace(this, this.constructor);
  }
}

// ✅ Exceções Específicas
export class ValidationException extends AppException {
  readonly code = 'VALIDATION_ERROR';
  readonly statusCode = 400;
}

export class NotFoundError extends AppException {
  readonly code = 'NOT_FOUND';
  readonly statusCode = 404;
}

export class BusinessRuleViolationError extends AppException {
  readonly code = 'BUSINESS_RULE_VIOLATION';
  readonly statusCode = 422;
}
```

### Error Handling Patterns
```typescript
// ✅ Result Pattern para operações que podem falhar
export class UserService {
  async createUser(userData: CreateUserDto): Promise<Result<User, string>> {
    try {
      // Validação
      if (!isValidEmail(userData.email)) {
        return Result.failure('Invalid email format');
      }

      // Verificar se usuário já existe
      const existingUser = await this.userRepository.findByEmail(userData.email);
      if (existingUser) {
        return Result.failure('User with this email already exists');
      }

      // Criar usuário
      const user = User.create(userData);
      const savedUser = await this.userRepository.save(user);

      return Result.success(savedUser);
    } catch (error) {
      this.logger.error('Failed to create user', {
        userData: { ...userData, password: '[REDACTED]' },
        error: error.message,
      });
      
      return Result.failure('Internal server error');
    }
  }
}
```

### Logging e Observabilidade
```typescript
// ✅ Structured Logging
import { Logger } from '@nestjs/common';

export class UserService {
  private readonly logger = new Logger(UserService.name);

  async createUser(userData: CreateUserDto): Promise<User> {
    const correlationId = generateCorrelationId();
    
    this.logger.log('Creating user started', {
      correlationId,
      email: userData.email,
      timestamp: new Date().toISOString(),
    });

    try {
      const user = await this.performUserCreation(userData);
      
      this.logger.log('User created successfully', {
        correlationId,
        userId: user.id,
        email: user.email,
      });
      
      return user;
    } catch (error) {
      this.logger.error('User creation failed', {
        correlationId,
        email: userData.email,
        error: error.message,
        stack: error.stack,
      });
      
      throw error;
    }
  }
}
```

## Performance e Otimizações

### Lazy Loading e Async Operations
```typescript
// ✅ Lazy Loading de dependências pesadas
export class ReportService {
  private _heavyProcessor?: HeavyProcessorService;

  private get heavyProcessor(): HeavyProcessorService {
    if (!this._heavyProcessor) {
      this._heavyProcessor = new HeavyProcessorService();
    }
    return this._heavyProcessor;
  }

  async generateReport(): Promise<Report> {
    return this.heavyProcessor.process();
  }
}

// ✅ Batch Operations
export class UserService {
  async createUsersInBatch(users: CreateUserDto[]): Promise<User[]> {
    const BATCH_SIZE = 100;
    const results: User[] = [];

    for (let i = 0; i < users.length; i += BATCH_SIZE) {
      const batch = users.slice(i, i + BATCH_SIZE);
      const batchResults = await Promise.all(
        batch.map(user => this.createUser(user))
      );
      results.push(...batchResults);
    }

    return results;
  }
}
```

### Caching e Memoização
```typescript
// ✅ Method Memoization
function memoize<T extends (...args: unknown[]) => unknown>(
  fn: T,
): T {
  const cache = new Map();
  
  return ((...args: Parameters<T>) => {
    const key = JSON.stringify(args);
    
    if (cache.has(key)) {
      return cache.get(key);
    }
    
    const result = fn(...args);
    cache.set(key, result);
    
    return result;
  }) as T;
}

// ✅ Usage
export class CalculationService {
  @memoize
  expensiveCalculation(input: number): number {
    // Cálculo complexo
    return input * Math.pow(input, 2);
  }
}
```

## Segurança do Código

### Validação de Input
```typescript
// ✅ Input Sanitization
import { IsEmail, IsString, MinLength, MaxLength } from 'class-validator';
import { Transform } from 'class-transformer';

export class CreateUserDto {
  @IsString()
  @MinLength(2)
  @MaxLength(50)
  @Transform(({ value }) => value?.trim())
  readonly name: string;

  @IsEmail()
  @Transform(({ value }) => value?.toLowerCase().trim())
  readonly email: string;

  @IsString()
  @MinLength(8)
  readonly password: string;
}
```

### Prevenção de Information Disclosure
```typescript
// ✅ Safe Error Response
export class UserController {
  @Post()
  async createUser(@Body() createUserDto: CreateUserDto): Promise<UserResponse> {
    try {
      const result = await this.userService.createUser(createUserDto);
      
      if (!result.isSuccess) {
        // Não expor detalhes internos
        throw new BadRequestException('Invalid user data');
      }

      return this.toUserResponse(result.value);
    } catch (error) {
      // Log detalhado internamente
      this.logger.error('User creation failed', {
        dto: { ...createUserDto, password: '[REDACTED]' },
        error: error.message,
      });

      // Resposta genérica para o cliente
      throw new InternalServerErrorException('User creation failed');
    }
  }

  private toUserResponse(user: User): UserResponse {
    return {
      id: user.id,
      name: user.name,
      email: user.email,
      createdAt: user.createdAt,
      // Nunca retornar password ou dados sensíveis
    };
  }
}
```

### Secrets e Configuração Segura
```typescript
// ✅ Configuration Management
export interface DatabaseConfig {
  readonly host: string;
  readonly port: number;
  readonly username: string;
  readonly password: string;
  readonly database: string;
  readonly ssl: boolean;
}

@Injectable()
export class ConfigService {
  private readonly config: DatabaseConfig;

  constructor() {
    this.config = {
      host: this.getRequiredEnvVar('DB_HOST'),
      port: parseInt(this.getRequiredEnvVar('DB_PORT')),
      username: this.getRequiredEnvVar('DB_USERNAME'),
      password: this.getRequiredEnvVar('DB_PASSWORD'),
      database: this.getRequiredEnvVar('DB_DATABASE'),
      ssl: this.getEnvVar('DB_SSL') === 'true',
    };
  }

  private getRequiredEnvVar(name: string): string {
    const value = process.env[name];
    if (!value) {
      throw new Error(`Required environment variable ${name} is not set`);
    }
    return value;
  }

  private getEnvVar(name: string, defaultValue: string = ''): string {
    return process.env[name] ?? defaultValue;
  }

  get database(): DatabaseConfig {
    return { ...this.config }; // Return copy to prevent mutation
  }
}
```

## Documentação e Comentários

### JSDoc para APIs Públicas
```typescript
/**
 * Service responsible for user management operations.
 * 
 * @example
 * ```typescript
 * const userService = new UserService(userRepository, emailService);
 * const result = await userService.createUser({
 *   name: 'John Doe',
 *   email: 'john@example.com',
 *   password: 'securePassword123'
 * });
 * ```
 */
@Injectable()
export class UserService {
  /**
   * Creates a new user in the system.
   * 
   * @param userData - The user data for creation
   * @returns Promise that resolves to the created user
   * @throws ValidationException When user data is invalid
   * @throws ConflictException When user already exists
   * 
   * @example
   * ```typescript
   * const user = await userService.createUser({
   *   name: 'John Doe',
   *   email: 'john@example.com',
   *   password: 'password123'
   * });
   * ```
   */
  async createUser(userData: CreateUserDto): Promise<User> {
    // Implementation
  }
}
```

### Comments para Lógica Complexa
```typescript
// ✅ Explain WHY, not WHAT
export class PaymentProcessor {
  async processPayment(payment: Payment): Promise<PaymentResult> {
    // We retry failed payments up to 3 times because payment gateways
    // can have temporary network issues that resolve quickly
    const maxRetries = 3;
    let attempt = 0;

    while (attempt < maxRetries) {
      try {
        return await this.attemptPayment(payment);
      } catch (error) {
        attempt++;
        
        // Exponential backoff to avoid overwhelming the payment gateway
        const delay = Math.pow(2, attempt) * 1000;
        await this.sleep(delay);
        
        if (attempt === maxRetries) {
          throw new PaymentProcessingError(
            'Payment failed after maximum retries',
            { paymentId: payment.id, attempts: attempt }
          );
        }
      }
    }
  }
}
```

## Checklist de Qualidade

### ✅ Antes de Commitar
- [ ] ESLint sem warnings/errors
- [ ] Prettier formatação aplicada
- [ ] Todos os tipos explícitos (sem `any`)
- [ ] Nomes descritivos e consistentes
- [ ] Error handling adequado
- [ ] Logs estruturados implementados
- [ ] Comentários JSDoc em APIs públicas
- [ ] Validação de inputs implementada

### ✅ Code Review
- [ ] Lógica clara e bem estruturada
- [ ] Não há code smells (long methods, god classes)
- [ ] Performance considerations addressed
- [ ] Security best practices seguidas
- [ ] Reusabilidade considerada
- [ ] Testes cobrem casos edge

### ✅ Deploy para Produção
- [ ] Configurações de produção validadas
- [ ] Secrets não expostos no código
- [ ] Monitoring e alertas configurados
- [ ] Performance benchmarks atingidos

## Métricas e Monitoring

### Code Quality Metrics
- **Cyclomatic Complexity**: <10 por método
- **Lines of Code**: <200 por classe, <50 por método
- **Test Coverage**: >85%
- **ESLint Violations**: 0
- **Technical Debt**: <8h por sprint

### Performance Metrics
- **Response Time**: <200ms para 95% das requests
- **Memory Usage**: <512MB por instância
- **CPU Usage**: <70% em média
- **Error Rate**: <0.1%

## Referências Técnicas
- [TypeScript Handbook](https://www.typescriptlang.org/docs/)
- [ESLint TypeScript Rules](https://typescript-eslint.io/rules/)
- [Node.js Best Practices](https://github.com/goldbergyoni/nodebestpractices)
- [Clean Code TypeScript](https://github.com/labs42io/clean-code-typescript)
