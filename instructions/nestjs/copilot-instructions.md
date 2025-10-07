# Instruções Gerais do Projeto - NestJS/TypeScript

## Tecnologias Base
- **TypeScript 5+**: Strict mode, últimas features (satisfies, const assertions)
- **NestJS 10+**: Framework principal, injeção de dependência nativa
- **Node.js 20+**: LTS atual, features modernas
- **npm/pnpm**: Gerenciador de pacotes (preferir pnpm)

## Princípios Fundamentais

### Object Calisthenics - SEMPRE aplicar (adaptado para TS):
1. **Um nível de indentação por método** - máximo 1 if/for aninhado
2. **Não use ELSE** - prefira early returns e guard clauses
3. **Encapsule primitivos** - crie Value Objects para conceitos de domínio
4. **Coleções de primeira classe** - wrapping em classes específicas
5. **Um ponto por linha** - evite method chaining excessivo
6. **Não abrevie nomes** - clareza > brevidade
7. **Mantenha entidades pequenas** - máximo 50 linhas por classe
8. **Máximo 2 propriedades por classe**
9. **Sem getters/setters** - expor comportamento, não dados

### Clean Architecture (NestJS)
- **Domain**: Entities + Value Objects + Domain Services
- **Application**: Use Cases + Application Services + DTOs
- **Infrastructure**: Repositories + External Services + Database
- **Presentation**: Controllers + Guards + Interceptors + Pipes

### Estrutura de Módulos NestJS
```
src/
├── domain/           # Entities + Value Objects + Interfaces
├── application/      # Use Cases + Application Services
├── infrastructure/   # Repositories + External + Database
├── presentation/     # Controllers + Guards + Pipes
├── shared/           # Common utilities + Types
└── main.ts          # Bootstrap application
```

## Regras de Codificação TypeScript

### Nomenclatura
- **Classes/Interfaces**: PascalCase (`UserService`, `IUserRepository`)
- **Métodos/Variáveis**: camelCase (`createUser`, `totalAmount`)
- **Constantes**: UPPER_SNAKE_CASE (`MAX_RETRY_ATTEMPTS`)
- **Tipos**: PascalCase com sufixo (`UserDto`, `CreateUserCommand`)
- **Arquivos**: kebab-case (`user.service.ts`, `create-user.use-case.ts`)

### Tipos TypeScript Obrigatórios
- **Strict mode**: `"strict": true` no tsconfig.json
- **No any**: Usar `unknown` ou tipos específicos
- **Interfaces explícitas**: Para todos os contratos
- **Utility Types**: `Readonly`, `Partial`, `Pick`, `Omit`
- **Type Guards**: Para validação de tipos em runtime

### NestJS Patterns
- **Dependency Injection**: Constructor injection sempre
- **Decorators**: `@Injectable()`, `@Controller()`, `@Module()`
- **Pipes**: Para validação e transformação
- **Guards**: Para autenticação e autorização
- **Interceptors**: Para logging, cache, transformação de resposta
- **Exception Filters**: Para tratamento global de erros

## Tratamento de Erros

### Hierarquia de Exceptions
```typescript
// Domain Exceptions
export class DomainException extends Error {
  constructor(message: string) {
    super(message);
    this.name = this.constructor.name;
  }
}

// Application Exceptions  
export class ValidationException extends DomainException {}
export class BusinessRuleException extends DomainException {}

// Infrastructure Exceptions
export class InfrastructureException extends Error {}
```

### NestJS Exception Handling
- **Built-in exceptions**: `BadRequestException`, `NotFoundException`, etc.
- **Custom exceptions**: Herdar de `HttpException`
- **Global filters**: Para tratamento centralizado
- **Proper HTTP status codes**: 200, 201, 400, 401, 404, 422, 500

## Injeção de Dependência (NestJS)

### Padrões Recomendados
```typescript
// ✅ Constructor injection
@Injectable()
export class UserService {
  constructor(
    private readonly userRepository: IUserRepository,
    private readonly emailService: IEmailService,
  ) {}
}

// ✅ Interface segregation
export interface IUserRepository {
  save(user: User): Promise<User>;
  findById(id: UserId): Promise<User | null>;
}
```

## Testes
- **Cobertura mínima**: 85% para domain, 80% para application
- **Estrutura**: Arrange/Act/Assert ou Given/When/Then
- **Nomes descritivos**: `should_ThrowException_When_UserNotFound`
- **Mocking**: Jest com spy/mock para dependências
- **Test Containers**: Para testes de integração com banco

## Validação e DTOs
- **class-validator**: Para validação de DTOs
- **class-transformer**: Para transformação de objetos
- **Pipes de validação**: `ValidationPipe` global
- **DTOs explícitos**: Input e Output DTOs separados

## Configuração e Environment
- **@nestjs/config**: Para configuração
- **Validation schema**: Joi ou Zod para env vars
- **Diferentes ambientes**: dev, test, prod
- **Secrets management**: Nunca commitar secrets

## Performance e Monitoring
- **Logging estruturado**: Winston ou Pino
- **Health checks**: `@nestjs/terminus`
- **Metrics**: Prometheus/OpenTelemetry quando necessário
- **Caching**: Redis para cache distribuído

## Segurança
- **Authentication**: JWT com `@nestjs/jwt`
- **Authorization**: Guards baseados em roles/permissions
- **Validation**: Sempre validar inputs
- **CORS**: Configurar adequadamente
- **Rate limiting**: `@nestjs/throttler`
- **Helmet**: Headers de segurança

## Documentação
- **Swagger/OpenAPI**: Auto-gerado com decorators
- **JSDoc**: Para código complexo
- **README**: Instruções de setup e uso
- **Architecture Decision Records**: Para decisões importantes