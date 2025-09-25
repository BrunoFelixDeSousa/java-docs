---
applyTo: "**/*.spec.ts,**/*.test.ts,**/test/**/*.ts"
description: "Padrões para testes unitários e integração com Jest e NestJS"
---

# Instruções de Testes - NestJS/TypeScript

## Objetivo
Estabelecer padrões consistentes para diferentes tipos de testes em aplicações NestJS, garantindo cobertura adequada, qualidade e manutenibilidade dos testes.

## Escopo
- Testes unitários (domain, application)
- Testes de integração (infrastructure, modules)
- Testes end-to-end (API completa)
- Testes de contrato (APIs externas)

## Pessoal vs Enterprise

### Projetos Pessoais (Foco: Rapidez)
- Coverage mínimo: 70%
- Focar em testes unitários críticos
- E2E apenas para fluxos principais
- Configuração básica do Jest

### Projetos Enterprise (Foco: Qualidade)
- Coverage mínimo: 85% (domain), 80% (application)
- Suite completa: unit + integration + e2e + contract
- Relatórios de coverage obrigatórios
- Testes de performance e carga
- Test containers para dependências externas

## Ferramentas e Configuração

### Stack Principal
```typescript
// package.json dependencies
{
  "devDependencies": {
    "@nestjs/testing": "^10.0.0",
    "jest": "^29.7.0",
    "@types/jest": "^29.5.5",
    "ts-jest": "^29.1.1",
    "supertest": "^6.3.3",
    "@types/supertest": "^2.0.12",
    "testcontainers": "^10.2.1"
  }
}
```

### Jest Configuration
```javascript
// jest.config.js
module.exports = {
  preset: 'ts-jest',
  testEnvironment: 'node',
  rootDir: 'src',
  testMatch: ['**/*.spec.ts', '**/*.test.ts'],
  collectCoverageFrom: [
    '**/*.ts',
    '!**/*.d.ts',
    '!**/node_modules/**',
    '!**/main.ts',
    '!**/*.module.ts'
  ],
  coverageThreshold: {
    global: {
      branches: 80,
      functions: 85,
      lines: 85,
      statements: 85
    },
    './domain/': {
      branches: 85,
      functions: 90,
      lines: 90,
      statements: 90
    }
  }
};
```

## Pirâmide de Testes

### 1. Testes Unitários (70% dos testes)
**Objetivo**: Testar unidades isoladas (classes, métodos) sem dependências externas

```typescript
// domain/user.entity.spec.ts
describe('User Entity', () => {
  describe('changeEmail', () => {
    it('should_UpdateEmail_When_ValidEmailProvided', () => {
      // Arrange
      const user = User.create({
        name: 'John Doe',
        email: 'john@example.com'
      });
      const newEmail = 'newemail@example.com';

      // Act
      user.changeEmail(newEmail);

      // Assert
      expect(user.email.value).toBe(newEmail);
    });

    it('should_ThrowDomainException_When_InvalidEmailProvided', () => {
      // Arrange
      const user = User.create({
        name: 'John Doe', 
        email: 'john@example.com'
      });

      // Act & Assert
      expect(() => user.changeEmail('invalid-email'))
        .toThrow(ValidationException);
    });
  });
});
```

### 2. Testes de Integração (20% dos testes)
**Objetivo**: Testar integração entre módulos, repositórios e serviços

```typescript
// application/create-user.use-case.integration.spec.ts
describe('CreateUserUseCase Integration', () => {
  let app: TestingModule;
  let useCase: CreateUserUseCase;
  let userRepository: IUserRepository;

  beforeEach(async () => {
    app = await Test.createTestingModule({
      imports: [UserModule, DatabaseModule],
      providers: [CreateUserUseCase]
    }).compile();

    useCase = app.get<CreateUserUseCase>(CreateUserUseCase);
    userRepository = app.get<IUserRepository>('IUserRepository');
  });

  afterEach(async () => {
    await app.close();
  });

  it('should_CreateUser_When_ValidDataProvided', async () => {
    // Arrange
    const command = new CreateUserCommand({
      name: 'John Doe',
      email: 'john@example.com'
    });

    // Act
    const result = await useCase.execute(command);

    // Assert
    expect(result.isSuccess).toBe(true);
    const savedUser = await userRepository.findById(result.value.id);
    expect(savedUser).toBeDefined();
    expect(savedUser!.name).toBe(command.name);
  });
});
```

### 3. Testes End-to-End (10% dos testes)
**Objetivo**: Testar fluxos completos da API

```typescript
// test/users.e2e-spec.ts
describe('Users (e2e)', () => {
  let app: INestApplication;
  let httpServer: any;

  beforeAll(async () => {
    const moduleFixture = await Test.createTestingModule({
      imports: [AppModule],
    }).compile();

    app = moduleFixture.createNestApplication();
    await app.init();
    httpServer = app.getHttpServer();
  });

  afterAll(async () => {
    await app.close();
  });

  describe('POST /users', () => {
    it('should_CreateUser_When_ValidPayload', async () => {
      // Arrange
      const createUserDto = {
        name: 'John Doe',
        email: 'john@example.com'
      };

      // Act & Assert
      const response = await request(httpServer)
        .post('/users')
        .send(createUserDto)
        .expect(201);

      expect(response.body).toHaveProperty('id');
      expect(response.body.name).toBe(createUserDto.name);
      expect(response.body.email).toBe(createUserDto.email);
    });

    it('should_ReturnBadRequest_When_InvalidPayload', async () => {
      // Arrange
      const invalidDto = {
        name: '', // invalid
        email: 'invalid-email'
      };

      // Act & Assert
      await request(httpServer)
        .post('/users')
        .send(invalidDto)
        .expect(400);
    });
  });
});
```

## Convenções e Padrões

### Nomenclatura de Testes
```typescript
// Padrão: should_[ExpectedResult]_When_[Condition]
describe('UserService', () => {
  it('should_ReturnUser_When_ValidIdProvided', () => {});
  it('should_ThrowNotFoundException_When_UserNotFound', () => {});
  it('should_UpdateUserEmail_When_ValidEmailProvided', () => {});
});

// Para testes com múltiplas condições
describe('calculateDiscount', () => {
  describe('when user is premium', () => {
    it('should_Apply20PercentDiscount_When_OrderAbove100', () => {});
    it('should_Apply10PercentDiscount_When_OrderBelow100', () => {});
  });
});
```

### Estrutura AAA (Arrange-Act-Assert)
```typescript
it('should_ProcessPayment_When_ValidCard', async () => {
  // Arrange - Preparar dados e mocks
  const paymentData = { 
    amount: 100.00, 
    cardToken: 'valid_token' 
  };
  const mockPaymentGateway = jest.fn().mockResolvedValue({ success: true });

  // Act - Executar a ação
  const result = await paymentService.process(paymentData);

  // Assert - Verificar resultados
  expect(result.isSuccess).toBe(true);
  expect(mockPaymentGateway).toHaveBeenCalledWith(paymentData);
});
```

### Mocking e Test Doubles

#### 1. Mocks para Dependências Externas
```typescript
// Mock de repositório
const mockUserRepository = {
  save: jest.fn(),
  findById: jest.fn(),
  findByEmail: jest.fn(),
};

beforeEach(() => {
  jest.clearAllMocks();
});
```

#### 2. Test Containers para Testes de Integração
```typescript
// infrastructure/database.integration.spec.ts
import { GenericContainer } from 'testcontainers';

describe('UserRepository Integration', () => {
  let container: StartedTestContainer;
  let repository: UserRepository;

  beforeAll(async () => {
    container = await new GenericContainer('postgres:15')
      .withExposedPorts(5432)
      .withEnvironment({
        POSTGRES_DB: 'testdb',
        POSTGRES_USER: 'test',
        POSTGRES_PASSWORD: 'test'
      })
      .start();

    // Setup database connection with container
  });

  afterAll(async () => {
    await container.stop();
  });
});
```

### Testes de Contrato (Contract Testing)

```typescript
// test/contracts/user-api.contract.spec.ts
describe('User API Contract', () => {
  it('should_MatchExpectedSchema_When_GetUserById', async () => {
    const response = await request(app.getHttpServer())
      .get('/users/123')
      .expect(200);

    const expectedSchema = {
      type: 'object',
      properties: {
        id: { type: 'string' },
        name: { type: 'string' },
        email: { type: 'string', format: 'email' },
        createdAt: { type: 'string', format: 'date-time' }
      },
      required: ['id', 'name', 'email', 'createdAt']
    };

    expect(response.body).toMatchSchema(expectedSchema);
  });
});
```

## Testing Utilities e Helpers

### Factory Pattern para Test Data
```typescript
// test/factories/user.factory.ts
export class UserTestFactory {
  static create(overrides: Partial<UserProps> = {}): User {
    const defaults: UserProps = {
      name: 'John Doe',
      email: 'john@example.com',
      createdAt: new Date(),
      ...overrides
    };
    
    return User.create(defaults);
  }

  static createMany(count: number, overrides: Partial<UserProps> = {}): User[] {
    return Array.from({ length: count }, (_, i) => 
      this.create({ ...overrides, email: `user${i}@example.com` })
    );
  }
}
```

### Custom Matchers
```typescript
// test/matchers/custom-matchers.ts
expect.extend({
  toBeValidUser(received: any) {
    const pass = received && 
                 typeof received.id === 'string' &&
                 typeof received.email === 'string' &&
                 received.email.includes('@');

    return {
      message: () => `expected ${received} to be a valid user`,
      pass,
    };
  },
});
```

## Performance Testing

### Benchmarking Critical Paths
```typescript
// test/performance/user-service.perf.spec.ts
describe('UserService Performance', () => {
  it('should_ProcessUsers_Within_AcceptableTime', async () => {
    const users = UserTestFactory.createMany(1000);
    
    const startTime = performance.now();
    await userService.processBatch(users);
    const endTime = performance.now();
    
    const executionTime = endTime - startTime;
    expect(executionTime).toBeLessThan(5000); // 5 seconds max
  });
});
```

## CI/CD Integration

### Scripts no package.json
```json
{
  "scripts": {
    "test": "jest",
    "test:watch": "jest --watch",
    "test:coverage": "jest --coverage",
    "test:debug": "node --inspect-brk -r tsconfig-paths/register -r ts-node/register node_modules/.bin/jest --runInBand",
    "test:e2e": "jest --config ./test/jest-e2e.json",
    "test:integration": "jest --testPathPattern=integration"
  }
}
```

### Coverage Reports
- **Formato**: HTML + LCOV para integração
- **Threshold**: Fail build se coverage abaixo do mínimo
- **Exclusions**: Arquivos de configuração, migrations, DTOs simples

## Checklist de Qualidade

### ✅ Antes de Commitar
- [ ] Todos os testes passando
- [ ] Coverage acima do threshold
- [ ] Nomes de testes descritivos
- [ ] Mocks limpos após cada teste
- [ ] Sem testes flaky ou dependentes de ordem

### ✅ Code Review
- [ ] Testes cobrem casos edge
- [ ] Assertions específicas e claras
- [ ] Setup/teardown adequados
- [ ] Não há lógica complexa nos testes
- [ ] Testes são independentes e isolados

### ✅ Deploy para Produção
- [ ] E2E tests passando
- [ ] Performance tests dentro dos limites
- [ ] Contract tests validados
- [ ] Health checks funcionando

## Métricas e Monitoring

### KPIs de Teste
- **Test Coverage**: >85% domain, >80% application
- **Test Execution Time**: <30s unit, <5min integration, <10min e2e
- **Test Stability**: <1% flaky tests
- **MTTR (Mean Time To Repair)**: <2h quando tests falham

### Dashboards Recomendados
- Coverage trends over time
- Test execution performance
- Flaky test identification
- CI/CD pipeline success rate

## Referências Técnicas
- [NestJS Testing Documentation](https://docs.nestjs.com/fundamentals/testing)
- [Jest Best Practices](https://github.com/goldbergyoni/javascript-testing-best-practices)
- [Test Pyramid by Martin Fowler](https://martinfowler.com/articles/practical-test-pyramid.html)
- [Contract Testing with Pact](https://docs.pact.io/)
