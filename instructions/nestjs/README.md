# 📚 Instruções NestJS - Guia Completo de Desenvolvimento

Conjunto de instruções e boas práticas para desenvolvimento NestJS enterprise com TypeScript, seguindo padrões de arquitetura limpa e DDD.

---

## 📋 Índice

- [Visão Geral](#-visão-geral)
- [TypeScript Coding](#-typescript-coding)
- [NestJS Architecture](#️-nestjs-architecture)
- [Domain Modeling](#-domain-modeling)
- [Testing](#-testing)
- [Documentation](#-documentation)
- [Copilot Instructions](#-copilot-instructions)

---

## 🎯 Visão Geral

Este repositório contém **instruções padronizadas** para desenvolvimento NestJS enterprise, cobrindo:

- ✅ TypeScript moderno (5.x+)
- ✅ Arquitetura modular e limpa
- ✅ Domain-Driven Design (DDD)
- ✅ Testes automatizados (Unit, E2E)
- ✅ Documentação técnica
- ✅ Boas práticas de código

---

## 💻 TypeScript Coding

📄 **[typescript-coding.instructions.md](./typescript-coding.instructions.md)**

Padrões de código TypeScript seguindo boas práticas.

### Principais Tópicos

#### 1. **Type Safety**
```typescript
// ✅ Tipos explícitos
interface Usuario {
  id: string;
  nome: string;
  email: string;
  criadoEm: Date;
}

function buscarUsuario(id: string): Promise<Usuario> {
  // ...
}

// ✅ Generics
class Repository<T> {
  async findById(id: string): Promise<T | null> {
    // ...
  }
  
  async findAll(): Promise<T[]> {
    // ...
  }
}

// ✅ Type Guards
function isUsuario(obj: unknown): obj is Usuario {
  return (
    typeof obj === 'object' &&
    obj !== null &&
    'id' in obj &&
    'email' in obj
  );
}
```

---

#### 2. **Modern TypeScript Features**
```typescript
// Utility Types
type UsuarioSemId = Omit<Usuario, 'id'>;
type UsuarioOpcional = Partial<Usuario>;
type UsuarioReadonly = Readonly<Usuario>;

// Template Literal Types
type EventName = 'user.created' | 'user.updated' | 'user.deleted';
type EventHandler = `on${Capitalize<EventName>}`;
// 'onUser.created' | 'onUser.updated' | 'onUser.deleted'

// Conditional Types
type NonNullable<T> = T extends null | undefined ? never : T;

// Mapped Types
type Errors<T> = {
  [K in keyof T]?: string;
};

const errors: Errors<Usuario> = {
  email: 'Email inválido',
  nome: 'Nome muito curto',
};
```

---

#### 3. **Decorators**
```typescript
// Class Decorator
@Module({
  controllers: [UsuarioController],
  providers: [UsuarioService],
})
export class UsuarioModule {}

// Method Decorator
@Get(':id')
async buscarPorId(@Param('id') id: string) {
  return this.service.buscarPorId(id);
}

// Property Decorator
@Injectable()
export class UsuarioService {
  @InjectRepository(Usuario)
  private readonly repository: Repository<Usuario>;
}

// Parameter Decorator
@Post()
async criar(@Body() dto: CriarUsuarioDto) {
  return this.service.criar(dto);
}
```

---

## 🏗️ NestJS Architecture

📄 **[nestjs-architecture.instructions.md](./nestjs-architecture.instructions.md)**

Padrões arquiteturais para aplicações NestJS enterprise.

### Estrutura de Módulos

```
src/
├── modules/
│   ├── usuario/
│   │   ├── domain/              # Entidades, Value Objects
│   │   │   ├── entities/
│   │   │   │   └── usuario.entity.ts
│   │   │   └── value-objects/
│   │   │       └── email.vo.ts
│   │   ├── application/         # DTOs, Use Cases
│   │   │   ├── dto/
│   │   │   │   ├── criar-usuario.dto.ts
│   │   │   │   └── atualizar-usuario.dto.ts
│   │   │   └── use-cases/
│   │   │       └── criar-usuario.use-case.ts
│   │   ├── infrastructure/      # Repositories, Adapters
│   │   │   ├── repositories/
│   │   │   │   └── usuario.repository.ts
│   │   │   └── adapters/
│   │   ├── presentation/        # Controllers
│   │   │   └── usuario.controller.ts
│   │   └── usuario.module.ts
│   └── ...
├── shared/                       # Código compartilhado
│   ├── database/
│   ├── decorators/
│   ├── filters/
│   ├── guards/
│   ├── interceptors/
│   └── pipes/
└── main.ts
```

---

### Exemplo Completo: Módulo de Usuário

#### 1. **Entity (Domain)**
```typescript
// usuario.entity.ts
import { Entity, Column, PrimaryGeneratedColumn } from 'typeorm';
import { Email } from '../value-objects/email.vo';

@Entity('usuarios')
export class Usuario {
  @PrimaryGeneratedColumn('uuid')
  id: string;

  @Column()
  nome: string;

  @Column({ unique: true })
  private _email: string;

  @Column({ select: false })
  senha: string;

  @Column({ type: 'timestamp', default: () => 'CURRENT_TIMESTAMP' })
  criadoEm: Date;

  get email(): Email {
    return new Email(this._email);
  }

  set email(email: Email) {
    this._email = email.getValue();
  }
}
```

---

#### 2. **Value Object**
```typescript
// email.vo.ts
export class Email {
  private readonly value: string;

  constructor(email: string) {
    if (!this.isValid(email)) {
      throw new Error('Email inválido');
    }
    this.value = email;
  }

  private isValid(email: string): boolean {
    const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return regex.test(email);
  }

  getValue(): string {
    return this.value;
  }

  equals(other: Email): boolean {
    return this.value === other.value;
  }
}
```

---

#### 3. **DTO (Application)**
```typescript
// criar-usuario.dto.ts
import { IsEmail, IsNotEmpty, MinLength } from 'class-validator';

export class CriarUsuarioDto {
  @IsNotEmpty({ message: 'Nome é obrigatório' })
  @MinLength(3, { message: 'Nome deve ter no mínimo 3 caracteres' })
  nome: string;

  @IsEmail({}, { message: 'Email inválido' })
  email: string;

  @MinLength(6, { message: 'Senha deve ter no mínimo 6 caracteres' })
  senha: string;
}
```

---

#### 4. **Use Case**
```typescript
// criar-usuario.use-case.ts
import { Injectable, ConflictException } from '@nestjs/common';
import { UsuarioRepository } from '../../infrastructure/repositories/usuario.repository';
import { CriarUsuarioDto } from '../dto/criar-usuario.dto';
import { Usuario } from '../../domain/entities/usuario.entity';
import { Email } from '../../domain/value-objects/email.vo';
import * as bcrypt from 'bcrypt';

@Injectable()
export class CriarUsuarioUseCase {
  constructor(private readonly repository: UsuarioRepository) {}

  async executar(dto: CriarUsuarioDto): Promise<Usuario> {
    // Verificar se email já existe
    const existente = await this.repository.findByEmail(dto.email);
    if (existente) {
      throw new ConflictException('Email já cadastrado');
    }

    // Criar usuário
    const usuario = new Usuario();
    usuario.nome = dto.nome;
    usuario.email = new Email(dto.email);
    usuario.senha = await bcrypt.hash(dto.senha, 10);

    return this.repository.save(usuario);
  }
}
```

---

#### 5. **Repository (Infrastructure)**
```typescript
// usuario.repository.ts
import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { Usuario } from '../../domain/entities/usuario.entity';

@Injectable()
export class UsuarioRepository {
  constructor(
    @InjectRepository(Usuario)
    private readonly repository: Repository<Usuario>,
  ) {}

  async findById(id: string): Promise<Usuario | null> {
    return this.repository.findOne({ where: { id } });
  }

  async findByEmail(email: string): Promise<Usuario | null> {
    return this.repository.findOne({ where: { _email: email } });
  }

  async findAll(): Promise<Usuario[]> {
    return this.repository.find();
  }

  async save(usuario: Usuario): Promise<Usuario> {
    return this.repository.save(usuario);
  }

  async delete(id: string): Promise<void> {
    await this.repository.delete(id);
  }
}
```

---

#### 6. **Controller (Presentation)**
```typescript
// usuario.controller.ts
import { Controller, Get, Post, Body, Param, UseGuards } from '@nestjs/common';
import { CriarUsuarioUseCase } from '../application/use-cases/criar-usuario.use-case';
import { CriarUsuarioDto } from '../application/dto/criar-usuario.dto';
import { JwtAuthGuard } from '../../../shared/guards/jwt-auth.guard';
import { ApiTags, ApiOperation, ApiBearerAuth } from '@nestjs/swagger';

@ApiTags('usuarios')
@Controller('usuarios')
export class UsuarioController {
  constructor(private readonly criarUsuarioUseCase: CriarUsuarioUseCase) {}

  @Post()
  @ApiOperation({ summary: 'Criar novo usuário' })
  async criar(@Body() dto: CriarUsuarioDto) {
    return this.criarUsuarioUseCase.executar(dto);
  }

  @Get(':id')
  @UseGuards(JwtAuthGuard)
  @ApiBearerAuth()
  @ApiOperation({ summary: 'Buscar usuário por ID' })
  async buscarPorId(@Param('id') id: string) {
    return this.usuarioService.buscarPorId(id);
  }
}
```

---

#### 7. **Module**
```typescript
// usuario.module.ts
import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { Usuario } from './domain/entities/usuario.entity';
import { UsuarioController } from './presentation/usuario.controller';
import { UsuarioRepository } from './infrastructure/repositories/usuario.repository';
import { CriarUsuarioUseCase } from './application/use-cases/criar-usuario.use-case';

@Module({
  imports: [TypeOrmModule.forFeature([Usuario])],
  controllers: [UsuarioController],
  providers: [UsuarioRepository, CriarUsuarioUseCase],
  exports: [UsuarioRepository],
})
export class UsuarioModule {}
```

---

## 🎨 Domain Modeling

📄 **[domain-modeling.instructions.md](./domain-modeling.instructions.md)**

Domain-Driven Design (DDD) com NestJS.

### Conceitos Principais

#### 1. **Entities vs Value Objects**
```typescript
// Entity - tem identidade
class Pedido {
  id: string;
  numero: string;
  total: Money;
  
  // Dois pedidos com mesmo total são DIFERENTES
}

// Value Object - sem identidade
class Money {
  constructor(
    private readonly valor: number,
    private readonly moeda: string,
  ) {}
  
  // Dois Money com mesmo valor são IGUAIS
  equals(other: Money): boolean {
    return this.valor === other.valor && this.moeda === other.moeda;
  }
}
```

---

#### 2. **Aggregates**
```typescript
// Pedido é o Aggregate Root
class Pedido {
  private itens: PedidoItem[] = [];
  
  adicionarItem(produto: Produto, quantidade: number) {
    const item = new PedidoItem(produto, quantidade);
    this.itens.push(item);
    this.calcularTotal();
  }
  
  removerItem(itemId: string) {
    this.itens = this.itens.filter(item => item.id !== itemId);
    this.calcularTotal();
  }
  
  // Sempre consistente
  private calcularTotal() {
    this.total = this.itens.reduce(
      (sum, item) => sum + item.getSubtotal(),
      0
    );
  }
}
```

---

## 🧪 Testing

📄 **[nestjs-testing.instructions.md](./nestjs-testing.instructions.md)**

Estratégias de testes em NestJS.

### 1. **Unit Tests**
```typescript
describe('CriarUsuarioUseCase', () => {
  let useCase: CriarUsuarioUseCase;
  let repository: jest.Mocked<UsuarioRepository>;

  beforeEach(() => {
    repository = {
      findByEmail: jest.fn(),
      save: jest.fn(),
    } as any;

    useCase = new CriarUsuarioUseCase(repository);
  });

  it('deve criar usuário com sucesso', async () => {
    // Arrange
    const dto: CriarUsuarioDto = {
      nome: 'João Silva',
      email: 'joao@example.com',
      senha: '123456',
    };

    repository.findByEmail.mockResolvedValue(null);
    repository.save.mockResolvedValue({ id: '1', ...dto } as any);

    // Act
    const resultado = await useCase.executar(dto);

    // Assert
    expect(resultado).toBeDefined();
    expect(repository.save).toHaveBeenCalled();
  });

  it('deve lançar erro se email já existe', async () => {
    const dto: CriarUsuarioDto = {
      nome: 'João Silva',
      email: 'joao@example.com',
      senha: '123456',
    };

    repository.findByEmail.mockResolvedValue({} as any);

    await expect(useCase.executar(dto)).rejects.toThrow(ConflictException);
  });
});
```

---

### 2. **E2E Tests**
```typescript
describe('UsuarioController (e2e)', () => {
  let app: INestApplication;

  beforeAll(async () => {
    const moduleFixture = await Test.createTestingModule({
      imports: [AppModule],
    }).compile();

    app = moduleFixture.createNestApplication();
    await app.init();
  });

  afterAll(async () => {
    await app.close();
  });

  it('/usuarios (POST)', () => {
    return request(app.getHttpServer())
      .post('/usuarios')
      .send({
        nome: 'João Silva',
        email: 'joao@example.com',
        senha: '123456',
      })
      .expect(201)
      .expect((res) => {
        expect(res.body).toHaveProperty('id');
        expect(res.body.nome).toBe('João Silva');
      });
  });
});
```

---

## 📖 Documentation

📄 **[documentation.instructions.md](./documentation.instructions.md)**

Padrões de documentação técnica com Swagger/OpenAPI.

```typescript
@ApiTags('usuarios')
@Controller('usuarios')
export class UsuarioController {
  
  @Post()
  @ApiOperation({ summary: 'Criar novo usuário' })
  @ApiResponse({ status: 201, description: 'Usuário criado com sucesso' })
  @ApiResponse({ status: 400, description: 'Dados inválidos' })
  @ApiResponse({ status: 409, description: 'Email já cadastrado' })
  async criar(@Body() dto: CriarUsuarioDto) {
    return this.criarUsuarioUseCase.executar(dto);
  }
}
```

---

## 🤖 Copilot Instructions

📄 **[copilot-instructions.md](./copilot-instructions.md)**

Instruções específicas para GitHub Copilot ao trabalhar com NestJS.

---

**Voltar para**: [📁 Repositório Principal](../README.md)
