# 🏛️ **Arquitetura React/Next.js - Guia Definitivo**

> **Padrões arquiteturais modernos para aplicações escaláveis e manuteníveis**

---

## 📋 **Índice**

1. [Visão Geral](#visão-geral)
2. [Camadas da Aplicação](#camadas-da-aplicação)
3. [Estrutura de Pastas](#estrutura-de-pastas)
4. [Fluxo de Dados](#fluxo-de-dados)
5. [Princípios SOLID](#princípios-solid)
6. [Padrões de Design](#padrões-de-design)
7. [Feature-Based Organization](#feature-based-organization)
8. [State Management](#state-management)
9. [Error Handling](#error-handling)
10. [Performance Optimization](#performance-optimization)
11. [Testing Strategy](#testing-strategy)
12. [Security Best Practices](#security-best-practices)
13. [Code Quality](#code-quality)
14. [Deployment Strategy](#deployment-strategy)

---

## 1. 🎯 **Visão Geral**

### Padrões Arquiteturais Utilizados

Esta arquitetura combina o melhor de múltiplos padrões:

#### **1. Clean Architecture** (Hexagonal/Onion)
- ✅ Independência de frameworks
- ✅ Testabilidade completa
- ✅ Separação de concerns
- ✅ Regras de negócio isoladas

#### **2. Feature-Slice Design**
- ✅ Organização por features/módulos
- ✅ Baixo acoplamento entre features
- ✅ Alta coesão dentro de features
- ✅ Escalabilidade horizontal

#### **3. MVC Adaptado (Next.js)**
- ✅ **Model**: Types, Schemas, Domain Logic
- ✅ **View**: Components, Pages
- ✅ **Controller**: Hooks, Actions, Services

#### **4. Domain-Driven Design (DDD)**
- ✅ Bounded Contexts por feature
- ✅ Ubiquitous Language
- ✅ Aggregates e Entities
- ✅ Value Objects

### Princípios Fundamentais

```
┌─────────────────────────────────────────────────────────┐
│                  CORE PRINCIPLES                         │
├─────────────────────────────────────────────────────────┤
│  1. Separation of Concerns (SoC)                         │
│  2. Don't Repeat Yourself (DRY)                          │
│  3. Keep It Simple, Stupid (KISS)                        │
│  4. You Aren't Gonna Need It (YAGNI)                     │
│  5. Single Source of Truth (SSoT)                        │
│  6. Composition over Inheritance                         │
│  7. Dependency Inversion                                 │
└─────────────────────────────────────────────────────────┘
```

---

## 2. 🏗️ **Camadas da Aplicação**

### Arquitetura em Camadas (Clean Architecture)

```
┌─────────────────────────────────────────────────────────┐
│                     PRESENTATION                         │
│  ┌─────────────────────────────────────────────────┐    │
│  │  UI Components, Pages, Layouts                   │    │
│  │  • app/**/page.tsx                               │    │
│  │  • components/**/*.tsx                           │    │
│  │  • Não conhece lógica de negócio                 │    │
│  └─────────────────────────────────────────────────┘    │
└────────────────────────┬────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────┐
│                    CONTROLLERS                           │
│  ┌─────────────────────────────────────────────────┐    │
│  │  Orquestração, Composição                        │    │
│  │  • app/**/*Controller.tsx                        │    │
│  │  • Conecta UI com Business Logic                 │    │
│  └─────────────────────────────────────────────────┘    │
└────────────────────────┬────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────┐
│                 BUSINESS LOGIC (Hooks)                   │
│  ┌─────────────────────────────────────────────────┐    │
│  │  Estado, Efeitos, Regras de Negócio              │    │
│  │  • app/**/use*.ts                                │    │
│  │  • hooks/**/*.ts                                 │    │
│  │  • Reutilizável e testável                       │    │
│  └─────────────────────────────────────────────────┘    │
└────────────────────────┬────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────┐
│                  APPLICATION LAYER                       │
│  ┌─────────────────────────────────────────────────┐    │
│  │  Use Cases, Services, Actions                    │    │
│  │  • app/**/actions.ts (Server Actions)            │    │
│  │  • lib/services/**/*.ts                          │    │
│  │  • lib/use-cases/**/*.ts                         │    │
│  └─────────────────────────────────────────────────┘    │
└────────────────────────┬────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────┐
│                    DOMAIN LAYER                          │
│  ┌─────────────────────────────────────────────────┐    │
│  │  Entities, Value Objects, Domain Services        │    │
│  │  • lib/domain/**/*.ts                            │    │
│  │  • app/**/types.ts                               │    │
│  │  • lib/types/**/*.ts                             │    │
│  └─────────────────────────────────────────────────┘    │
└────────────────────────┬────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────┐
│              INFRASTRUCTURE LAYER                        │
│  ┌─────────────────────────────────────────────────┐    │
│  │  Database, APIs, External Services               │    │
│  │  • lib/db.ts (Prisma)                            │    │
│  │  • lib/api/**/*.ts                               │    │
│  │  • lib/integrations/**/*.ts                      │    │
│  └─────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────┘
```

### 1. **Presentation Layer** (UI)

**Localização**: `app/**/page.tsx`, `components/**/*.tsx`

**Responsabilidades**:
- ✅ Renderização de UI
- ✅ Captura de eventos do usuário
- ✅ Exibição de dados formatados
- ❌ Sem lógica de negócio
- ❌ Sem chamadas diretas a API/DB

**Exemplo**:
```tsx
// ═══════════════════════════════════════════════════════════
// app/produtos/page.tsx (Presentation)
// ═══════════════════════════════════════════════════════════

import { ProdutosController } from './ProdutosController';

export default function ProdutosPage() {
  return <ProdutosController />;
}

// ✅ Responsabilidade única: Definir a rota e renderizar o controller
```

**Regras**:
1. Server Components por padrão (Next.js 14+)
2. `'use client'` apenas quando necessário (interatividade)
3. Props tipadas com TypeScript
4. Componentes pequenos e focados (<200 linhas)
5. Sem lógica complexa (extrair para hooks)

---

### 2. **Controller Layer**

**Localização**: `app/**/*Controller.tsx`

**Responsabilidades**:
- ✅ Orquestrar fluxo de dados
- ✅ Conectar View com Hooks/Actions
- ✅ Gerenciar estado da página
- ✅ Coordenar múltiplos hooks
- ✅ Error boundaries e loading states

**Exemplo**:
```tsx
// ═══════════════════════════════════════════════════════════
// app/produtos/ProdutosController.tsx (Controller)
// ═══════════════════════════════════════════════════════════

'use client';

import { useProdutos } from './useProdutos';
import { useCarrinho } from '@/hooks/useCarrinho';
import { useFiltros } from './useFiltros';
import { ProdutosView } from './ProdutosView';
import { ErrorBoundary } from '@/components/ErrorBoundary';
import { Suspense } from 'react';

export function ProdutosController() {
  // ✅ Composição de múltiplos hooks
  const { produtos, loading, error, refetch } = useProdutos();
  const { carrinho, adicionarAoCarrinho } = useCarrinho();
  const { filtros, setFiltro, produtosFiltrados } = useFiltros(produtos);

  // ✅ Orquestração de lógica
  const handleAddToCart = async (produtoId: string) => {
    try {
      await adicionarAoCarrinho(produtoId);
      // Analytics, toast, etc
    } catch (error) {
      console.error('Erro ao adicionar ao carrinho:', error);
    }
  };

  // ✅ Error handling
  if (error) {
    return <ErrorBoundary error={error} retry={refetch} />;
  }

  // ✅ Passar dados e callbacks para View
  return (
    <Suspense fallback={<ProdutosViewSkeleton />}>
      <ProdutosView
        produtos={produtosFiltrados}
        loading={loading}
        carrinho={carrinho}
        filtros={filtros}
        onAddToCart={handleAddToCart}
        onFiltroChange={setFiltro}
      />
    </Suspense>
  );
}

// ✅ Responsabilidade: Configuração de infraestrutura
```

```tsx
// ═══════════════════════════════════════════════════════════
// lib/api/payment.api.ts (Infrastructure)
// ═══════════════════════════════════════════════════════════

import Stripe from 'stripe';

const stripe = new Stripe(process.env.STRIPE_SECRET_KEY!, {
  apiVersion: '2023-10-16',
});

export class PaymentAPI {
  static async createPaymentIntent(amount: number, currency: string = 'brl') {
    try {
      const paymentIntent = await stripe.paymentIntents.create({
        amount: amount * 100, // Stripe usa centavos
        currency,
      });

      return { success: true, data: paymentIntent };
    } catch (error) {
      console.error('Erro no pagamento:', error);
      return { success: false, error };
    }
  }
}

// ✅ Responsabilidade: Abstrair integrações com serviços externos
```

### **Regras de Dependência entre Camadas**

```
┌─────────────────────────────────────────────────────────┐
│  REGRA DE DEPENDÊNCIA (Dependency Inversion Principle)  │
└─────────────────────────────────────────────────────────┘

✅ PERMITIDO (Inward Dependencies):
   Presentation → Controllers → Business Logic → Application → Domain → Infrastructure
   
❌ PROIBIDO (Outward Dependencies):
   Infrastructure ↛ Domain
   Domain ↛ Application
   Application ↛ Business Logic
   
✅ EXCEÇÃO:
   Domain ← Todas as camadas (todos dependem do Domain)
```

**Exemplo de Violação**:
```tsx
// ❌ ERRADO: Domain depende de Infrastructure
// lib/domain/produto.ts
import { db } from '@/lib/db'; // ❌ Domain não pode importar Infrastructure

export class Produto {
  static async buscar() {
    return await db.produto.findMany(); // ❌ Violação!
  }
}
```

**Exemplo Correto**:
```tsx
// ✅ CORRETO: Domain define interface, Infrastructure implementa
// lib/domain/produto.repository.interface.ts
export interface ProdutoRepository {
  findMany(): Promise<Produto[]>;
  findById(id: string): Promise<Produto | null>;
  create(data: CreateProdutoDTO): Promise<Produto>;
}

// lib/infrastructure/produto.repository.ts
import { ProdutoRepository } from '@/lib/domain/produto.repository.interface';
import { db } from '@/lib/db';

export class PrismaProdutoRepository implements ProdutoRepository {
  async findMany() {
    return await db.produto.findMany();
  }
  
  async findById(id: string) {
    return await db.produto.findUnique({ where: { id } });
  }
  
  async create(data: CreateProdutoDTO) {
    return await db.produto.create({ data });
  }
}

// ✅ Domain define o contrato, Infrastructure implementa
```

---

## 3. 📁 **Estrutura de Pastas**

### **Estrutura Feature-Based (Recomendada)**

```
app/
├── (auth)/                          # Route Group (não afeta URL)
│   ├── login/
│   │   ├── page.tsx                 # /login
│   │   ├── LoginController.tsx      # Controller
│   │   ├── LoginForm.tsx            # View Component
│   │   ├── useLogin.ts              # Business Logic Hook
│   │   ├── actions.ts               # Server Actions
│   │   ├── types.ts                 # Domain Types
│   │   └── schema.ts                # Zod Validation
│   │
│   ├── register/
│   │   ├── page.tsx                 # /register
│   │   ├── RegisterController.tsx
│   │   ├── useRegister.ts
│   │   ├── actions.ts
│   │   └── schema.ts
│   │
│   └── layout.tsx                   # Shared Auth Layout
│
├── (dashboard)/                     # Route Group
│   ├── produtos/
│   │   ├── page.tsx                 # /produtos (Lista)
│   │   ├── [id]/
│   │   │   ├── page.tsx             # /produtos/[id] (Detalhes)
│   │   │   └── edit/
│   │   │       └── page.tsx         # /produtos/[id]/edit
│   │   │
│   │   ├── _components/             # Feature-specific Components
│   │   │   ├── ProdutoCard.tsx
│   │   │   ├── ProdutoForm.tsx
│   │   │   └── ProdutoFilters.tsx
│   │   │
│   │   ├── ProdutosController.tsx   # Controller
│   │   ├── useProdutos.ts           # Business Logic
│   │   ├── useProdutoFilters.ts     # Filter Logic
│   │   ├── actions.ts               # CRUD Actions
│   │   ├── types.ts                 # Product Types
│   │   ├── schema.ts                # Validation
│   │   └── constants.ts             # Feature Constants
│   │
│   ├── carrinho/
│   │   ├── page.tsx                 # /carrinho
│   │   ├── CarrinhoController.tsx
│   │   ├── _components/
│   │   │   ├── CarrinhoItem.tsx
│   │   │   └── CarrinhoResumo.tsx
│   │   ├── useCarrinho.ts
│   │   ├── actions.ts
│   │   └── types.ts
│   │
│   └── layout.tsx                   # Dashboard Layout (Sidebar, Header)
│
├── api/                             # API Routes (REST/GraphQL)
│   ├── produtos/
│   │   └── route.ts                 # GET/POST /api/produtos
│   ├── webhooks/
│   │   └── stripe/
│   │       └── route.ts             # POST /api/webhooks/stripe
│   └── trpc/
│       └── [trpc]/
│           └── route.ts             # tRPC Handler
│
├── layout.tsx                       # Root Layout
├── page.tsx                         # Home Page (/)
├── not-found.tsx                    # 404 Page
├── error.tsx                        # Error Boundary
└── global-error.tsx                 # Global Error Handler

components/                          # Shared Components
├── ui/                              # shadcn/ui Components
│   ├── button.tsx
│   ├── input.tsx
│   ├── dialog.tsx
│   ├── dropdown-menu.tsx
│   └── ...
│
├── layout/                          # Layout Components
│   ├── Header.tsx
│   ├── Footer.tsx
│   ├── Sidebar.tsx
│   └── Container.tsx
│
├── forms/                           # Form Components
│   ├── FormField.tsx                # Reusable Field
│   ├── FormError.tsx
│   └── FormLabel.tsx
│
└── shared/                          # Shared Business Components
    ├── DataTable.tsx
    ├── SearchBar.tsx
    ├── Pagination.tsx
    └── ErrorBoundary.tsx

hooks/                               # Global/Shared Hooks
├── useAuth.ts                       # Authentication Hook
├── useToast.ts                      # Toast Notifications
├── useMediaQuery.ts                 # Responsive Hook
├── useDebounce.ts                   # Debounce Hook
├── useLocalStorage.ts               # LocalStorage Hook
└── useFetch.ts                      # Generic Fetch Hook

lib/                                 # Utilities & Infrastructure
├── db.ts                            # Prisma Client
├── auth.ts                          # NextAuth Config
├── utils.ts                         # Utility Functions (cn, etc)
│
├── validations/                     # Shared Validation Schemas
│   ├── auth.schema.ts
│   ├── common.schema.ts
│   └── produto.schema.ts
│
├── domain/                          # Domain Layer
│   ├── entities/
│   │   ├── Produto.ts
│   │   ├── Usuario.ts
│   │   └── Pedido.ts
│   │
│   ├── value-objects/
│   │   ├── Email.ts
│   │   ├── CPF.ts
│   │   └── Preco.ts
│   │
│   ├── services/                    # Domain Services
│   │   ├── ProdutoService.ts
│   │   └── PedidoService.ts
│   │
│   └── repositories/                # Repository Interfaces
│       ├── IProdutoRepository.ts
│       └── IUsuarioRepository.ts
│
├── infrastructure/                  # Infrastructure Layer
│   ├── repositories/                # Implementations
│   │   ├── PrismaProdutoRepository.ts
│   │   └── PrismaUsuarioRepository.ts
│   │
│   ├── api/                         # External APIs
│   │   ├── payment.api.ts           # Stripe
│   │   ├── shipping.api.ts          # Correios
│   │   └── email.api.ts             # Resend
│   │
│   └── cache/                       # Cache Providers
│       ├── redis.ts
│       └── memory.ts
│
├── services/                        # Application Services
│   ├── auth.service.ts
│   ├── email.service.ts
│   ├── payment.service.ts
│   └── analytics.service.ts
│
├── use-cases/                       # Use Cases (Application Layer)
│   ├── produto/
│   │   ├── CreateProduto.ts
│   │   ├── UpdateProduto.ts
│   │   ├── DeleteProduto.ts
│   │   └── GetProdutos.ts
│   │
│   └── pedido/
│       ├── CreatePedido.ts
│       └── CancelPedido.ts
│
└── constants/                       # Global Constants
    ├── routes.ts                    # Route Paths
    ├── api-urls.ts                  # API Endpoints
    └── config.ts                    # App Configuration

types/                               # Global TypeScript Types
├── index.ts                         # Re-exports
├── api.types.ts                     # API Types
├── database.types.ts                # Database Types
└── global.d.ts                      # Global Declarations

public/                              # Static Assets
├── images/
│   ├── logo.svg
│   └── placeholder.png
├── fonts/
│   └── custom-font.woff2
└── icons/
    └── favicon.ico

prisma/                              # Database Schema
├── schema.prisma                    # Prisma Schema
├── migrations/                      # Migration History
└── seed.ts                          # Database Seeding

tests/                               # Tests
├── e2e/                             # E2E Tests (Playwright)
│   ├── auth.spec.ts
│   └── produtos.spec.ts
│
├── integration/                     # Integration Tests
│   └── api/
│       └── produtos.test.ts
│
└── unit/                            # Unit Tests
    ├── hooks/
    │   └── useProdutos.test.ts
    └── services/
        └── ProdutoService.test.ts

config/                              # Configuration Files
├── next.config.mjs                  # Next.js Config
├── tailwind.config.ts               # Tailwind Config
├── tsconfig.json                    # TypeScript Config
├── .eslintrc.json                   # ESLint Config
├── .prettierrc                      # Prettier Config
├── vitest.config.ts                 # Vitest Config
└── playwright.config.ts             # Playwright Config
```

### **Convenções de Nomenclatura**

#### **1. Arquivos de Componentes**
```
✅ PascalCase para componentes:
   - Button.tsx
   - UserProfile.tsx
   - ProductCard.tsx

✅ kebab-case para rotas (Next.js):
   - page.tsx
   - layout.tsx
   - not-found.tsx
   - error.tsx
```

#### **2. Hooks**
```
✅ camelCase com prefixo 'use':
   - useAuth.ts
   - useProdutos.ts
   - useCarrinho.ts
```

#### **3. Server Actions**
```
✅ actions.ts (dentro de cada feature):
   - app/produtos/actions.ts
   - app/auth/actions.ts
```

#### **4. Types e Schemas**
```
✅ types.ts para tipos de domínio
✅ schema.ts para validação Zod
✅ PascalCase para interfaces/types
```

#### **5. Utilities**
```
✅ camelCase para funções utilitárias:
   - formatCurrency.ts
   - calculateDiscount.ts
   - generateSlug.ts
```

### **Regras de Organização**

#### **1. Colocation** (Co-localização)
```
✅ Manter código relacionado junto:
app/produtos/
  ├── page.tsx
  ├── ProdutosController.tsx    # ✅ Mesmo diretório da página
  ├── useProdutos.ts             # ✅ Hook específico da feature
  └── _components/               # ✅ Componentes privados (prefixo _)
      └── ProdutoCard.tsx
```

#### **2. Prefixo `_` para Privacidade**
```
✅ _components/   → Componentes privados da feature (não geram rotas)
✅ _lib/          → Utilitários privados
✅ _hooks/        → Hooks privados

❌ Sem prefixo  → Público/compartilhável
```

#### **3. Route Groups** (Grupos de Rotas)
```
✅ (auth)/     → Agrupa rotas sem afetar URL
✅ (dashboard)/ → Layout compartilhado
✅ (marketing)/ → Seção de marketing

Exemplo:
app/(auth)/login/page.tsx        → URL: /login
app/(auth)/register/page.tsx     → URL: /register
app/(dashboard)/produtos/page.tsx → URL: /produtos
```

#### **4. Parallel Routes** (Rotas Paralelas)
```
app/
  ├── @modal/           # Slot para modal
  │   └── produto/
  │       └── [id]/
  │           └── page.tsx
  ├── @sidebar/         # Slot para sidebar
  │   └── default.tsx
  └── layout.tsx        # Renderiza ambos os slots
```

#### **5. Intercepting Routes** (Rotas Interceptadas)
```
app/
  ├── produtos/
  │   ├── [id]/
  │   │   └── page.tsx          # Página completa
  │   └── (..)produtos/
  │       └── [id]/
  │           └── page.tsx      # Modal interceptado
```

---

## 4. 🔄 **Fluxo de Dados**

### **Fluxo Unidirecional (Unidirectional Data Flow)**

```
┌─────────────────────────────────────────────────────────────────┐
│                     FLUXO DE DADOS COMPLETO                      │
└─────────────────────────────────────────────────────────────────┘

1. USER INTERACTION (Cliente)
   │
   ├─► Click em botão
   ├─► Submit de formulário
   └─► Input de texto
        │
        ▼
2. EVENT HANDLER (Controller/Component)
   │
   ├─► onClick={handleClick}
   ├─► onSubmit={handleSubmit}
   └─► onChange={handleChange}
        │
        ▼
3. ACTION/HOOK (Business Logic)
   │
   ├─► Hook atualiza estado local (useState)
   ├─► Hook dispara efeito (useEffect)
   └─► Hook chama Server Action
        │
        ▼
4. SERVER ACTION (Application Layer)
   │
   ├─► Valida dados (Zod)
   ├─► Aplica regras de negócio (Domain)
   └─► Persiste no banco (Infrastructure)
        │
        ▼
5. DATABASE (Infrastructure)
   │
   ├─► INSERT/UPDATE/DELETE
   └─► Retorna resultado
        │
        ▼
6. REVALIDATION (Next.js Cache)
   │
   ├─► revalidatePath('/produtos')
   ├─► revalidateTag('produtos')
   └─► Cache invalidado
        │
        ▼
7. RE-RENDER (React)
   │
   ├─► Componente re-renderiza
   ├─► UI atualizada
   └─► Usuário vê mudança
```

### **Exemplo Completo de Fluxo**

```tsx
// ═══════════════════════════════════════════════════════════
// PASSO 1: User Interaction (View)
// ═══════════════════════════════════════════════════════════

// components/ProdutoCard.tsx
'use client';

interface ProdutoCardProps {
  produto: Produto;
  onAddToCart: (id: string) => void; // Callback do Controller
}

export function ProdutoCard({ produto, onAddToCart }: ProdutoCardProps) {
  return (
    <div className="card">
      <h3>{produto.nome}</h3>
      <p>R$ {produto.preco}</p>
      
      {/* ✅ 1. Usuário clica no botão */}
      <button onClick={() => onAddToCart(produto.id)}>
        Adicionar ao Carrinho
      </button>
    </div>
  );
}

// ═══════════════════════════════════════════════════════════
// PASSO 2: Event Handler (Controller)
// ═══════════════════════════════════════════════════════════

// app/produtos/ProdutosController.tsx
'use client';

import { useProdutos } from './useProdutos';
import { useCarrinho } from '@/hooks/useCarrinho';
import { toast } from 'sonner';

export function ProdutosController() {
  const { produtos } = useProdutos();
  const { adicionarAoCarrinho } = useCarrinho();

  // ✅ 2. Handler orquestra lógica
  const handleAddToCart = async (produtoId: string) => {
    try {
      // ✅ 3. Chama hook de negócio
      await adicionarAoCarrinho(produtoId);
      
      toast.success('Produto adicionado ao carrinho!');
    } catch (error) {
      toast.error('Erro ao adicionar produto');
      console.error(error);
    }
  };

  return (
    <div className="grid">
      {produtos.map((produto) => (
        <ProdutoCard
          key={produto.id}
          produto={produto}
          onAddToCart={handleAddToCart}
        />
      ))}
    </div>
  );
}

// ═══════════════════════════════════════════════════════════
// PASSO 3: Action/Hook (Business Logic)
// ═══════════════════════════════════════════════════════════

// hooks/useCarrinho.ts
'use client';

import { useState, useCallback } from 'react';
import { addItemToCart } from '@/app/carrinho/actions';

export function useCarrinho() {
  const [loading, setLoading] = useState(false);

  // ✅ 3. Hook encapsula lógica de negócio
  const adicionarAoCarrinho = useCallback(async (produtoId: string) => {
    setLoading(true);
    
    try {
      // ✅ 4. Chama Server Action
      const result = await addItemToCart(produtoId, 1);
      
      if (!result.success) {
        throw new Error(result.error);
      }
      
      return result.data;
    } finally {
      setLoading(false);
    }
  }, []);

  return {
    adicionarAoCarrinho,
    loading
  };
}

// ═══════════════════════════════════════════════════════════
// PASSO 4: Server Action (Application Layer)
// ═══════════════════════════════════════════════════════════

// app/carrinho/actions.ts
'use server';

import { revalidatePath } from 'next/cache';
import { db } from '@/lib/db';
import { auth } from '@/lib/auth';
import { z } from 'zod';

const addToCartSchema = z.object({
  produtoId: z.string().uuid(),
  quantidade: z.number().int().positive()
});

export async function addItemToCart(produtoId: string, quantidade: number) {
  try {
    // ✅ Autenticação
    const session = await auth();
    if (!session?.user?.id) {
      return { success: false, error: 'Não autenticado' };
    }

    // ✅ Validação
    const result = addToCartSchema.safeParse({ produtoId, quantidade });
    if (!result.success) {
      return { 
        success: false, 
        error: 'Dados inválidos',
        errors: result.error.flatten().fieldErrors
      };
    }

    // ✅ Verificar estoque (Domain Logic)
    const produto = await db.produto.findUnique({
      where: { id: produtoId }
    });

    if (!produto || produto.estoque < quantidade) {
      return { success: false, error: 'Produto indisponível' };
    }

    // ✅ 5. Persistir no banco (Infrastructure)
    const carrinhoItem = await db.carrinhoItem.create({
      data: {
        userId: session.user.id,
        produtoId,
        quantidade
      },
      include: {
        produto: true
      }
    });

    // ✅ 6. Revalidar cache
    revalidatePath('/carrinho');
    revalidatePath('/produtos');

    return {
      success: true,
      data: carrinhoItem
    };

  } catch (error) {
    console.error('Erro ao adicionar ao carrinho:', error);
    return {
      success: false,
      error: 'Erro interno do servidor'
    };
  }
}

// ✅ 7. React re-renderiza componentes com novos dados
```

### **Fluxo de Dados com React Query**

```tsx
// ═══════════════════════════════════════════════════════════
// React Query para Cache e Sincronização
// ═══════════════════════════════════════════════════════════

// hooks/useProdutos.ts
'use client';

import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { getProdutos, createProduto } from '@/app/produtos/actions';

export function useProdutos() {
  const queryClient = useQueryClient();

  // ✅ Query para leitura (GET)
  const { data: produtos, isLoading, error } = useQuery({
    queryKey: ['produtos'],
    queryFn: getProdutos,
    staleTime: 1000 * 60 * 5, // 5 minutos
  });

  // ✅ Mutation para escrita (POST/PUT/DELETE)
  const createMutation = useMutation({
    mutationFn: createProduto,
    onSuccess: () => {
      // ✅ Invalidar cache após sucesso
      queryClient.invalidateQueries({ queryKey: ['produtos'] });
    },
  });

  return {
    produtos: produtos || [],
    isLoading,
    error,
    createProduto: createMutation.mutate
  };
}
```

### **Fluxo com Optimistic Updates**

```tsx
// ═══════════════════════════════════════════════════════════
// Optimistic Update para UX instantânea
// ═══════════════════════════════════════════════════════════

// hooks/useCarrinho.ts
'use client';

import { useMutation, useQueryClient } from '@tanstack/react-query';
import { addItemToCart } from '@/app/carrinho/actions';

export function useCarrinho() {
  const queryClient = useQueryClient();

  const addToCartMutation = useMutation({
    mutationFn: addItemToCart,
    
    // ✅ Optimistic Update (UI atualiza ANTES da resposta do servidor)
    onMutate: async ({ produtoId, quantidade }) => {
      // Cancelar queries em andamento
      await queryClient.cancelQueries({ queryKey: ['carrinho'] });

      // Snapshot do estado anterior (para rollback)
      const previousCarrinho = queryClient.getQueryData(['carrinho']);

      // Atualizar cache otimisticamente
      queryClient.setQueryData(['carrinho'], (old: any) => {
        return {
          ...old,
          items: [
            ...old.items,
            { produtoId, quantidade, id: 'temp-id' }
          ]
        };
      });

      // Retornar contexto para rollback
      return { previousCarrinho };
    },

    // ✅ Rollback em caso de erro
    onError: (err, variables, context) => {
      if (context?.previousCarrinho) {
        queryClient.setQueryData(['carrinho'], context.previousCarrinho);
      }
    },

    // ✅ Refetch após sucesso
    onSettled: () => {
      queryClient.invalidateQueries({ queryKey: ['carrinho'] });
    },
  });

  return {
    addToCart: addToCartMutation.mutate
  };
}
```

### **Fluxo de Dados com Zustand (Global State)**

```tsx
// ═══════════════════════════════════════════════════════════
// State Management Global com Zustand
// ═══════════════════════════════════════════════════════════

// lib/stores/carrinho.store.ts
import { create } from 'zustand';
import { persist } from 'zustand/middleware';

interface CarrinhoItem {
  produtoId: string;
  nome: string;
  preco: number;
  quantidade: number;
}

interface CarrinhoStore {
  items: CarrinhoItem[];
  total: number;
  
  // Actions
  addItem: (item: CarrinhoItem) => void;
  removeItem: (produtoId: string) => void;
  updateQuantidade: (produtoId: string, quantidade: number) => void;
  clear: () => void;
  
  // Computed
  calcularTotal: () => number;
}

export const useCarrinhoStore = create<CarrinhoStore>()(
  persist(
    (set, get) => ({
      items: [],
      total: 0,

      addItem: (item) => set((state) => {
        const existingItem = state.items.find(i => i.produtoId === item.produtoId);
        
        if (existingItem) {
          return {
            items: state.items.map(i =>
              i.produtoId === item.produtoId
                ? { ...i, quantidade: i.quantidade + item.quantidade }
                : i
            ),
            total: get().calcularTotal()
          };
        }
        
        return {
          items: [...state.items, item],
          total: get().calcularTotal()
        };
      }),

      removeItem: (produtoId) => set((state) => ({
        items: state.items.filter(i => i.produtoId !== produtoId),
        total: get().calcularTotal()
      })),

      updateQuantidade: (produtoId, quantidade) => set((state) => ({
        items: state.items.map(i =>
          i.produtoId === produtoId ? { ...i, quantidade } : i
        ),
        total: get().calcularTotal()
      })),

      clear: () => set({ items: [], total: 0 }),

      calcularTotal: () => {
        const { items } = get();
        return items.reduce((acc, item) => acc + (item.preco * item.quantidade), 0);
      }
    }),
    {
      name: 'carrinho-storage', // LocalStorage key
      partialize: (state) => ({ items: state.items }), // Salvar apenas items
    }
  )
);

// ═══════════════════════════════════════════════════════════
// Uso em Componentes
// ═══════════════════════════════════════════════════════════

'use client';

import { useCarrinhoStore } from '@/lib/stores/carrinho.store';

export function CarrinhoButton() {
  // ✅ Selecionar apenas o que precisa (otimização)
  const items = useCarrinhoStore((state) => state.items);
  const total = useCarrinhoStore((state) => state.total);

  return (
    <button>
      Carrinho ({items.length}) - R$ {total.toFixed(2)}
    </button>
  );
}

export function ProdutoCard({ produto }: { produto: Produto }) {
  const addItem = useCarrinhoStore((state) => state.addItem);

  const handleAddToCart = () => {
    addItem({
      produtoId: produto.id,
      nome: produto.nome,
      preco: produto.preco,
      quantidade: 1
    });
  };

  return (
    <button onClick={handleAddToCart}>
      Adicionar ao Carrinho
    </button>
  );
}
```

### **Fluxo de Dados Server-Side (Server Components)**

```tsx
// ═══════════════════════════════════════════════════════════
// Server Component com Data Fetching
// ═══════════════════════════════════════════════════════════

// app/produtos/page.tsx (Server Component)
import { db } from '@/lib/db';
import { ProdutosList } from './ProdutosList';

// ✅ Fetch diretamente no Server Component
async function getProdutos() {
  const produtos = await db.produto.findMany({
    where: { ativo: true },
    orderBy: { createdAt: 'desc' },
    take: 20
  });

  return produtos;
}

export default async function ProdutosPage() {
  // ✅ Await direto no componente (React 19+)
  const produtos = await getProdutos();

  return (
    <div>
      <h1>Produtos</h1>
      
      {/* ✅ Passar dados para Client Component */}
      <ProdutosList produtos={produtos} />
    </div>
  );
}

// ═══════════════════════════════════════════════════════════
// Client Component recebe dados do Server Component
// ═══════════════════════════════════════════════════════════

// app/produtos/ProdutosList.tsx
'use client';

import { Produto } from './types';
import { ProdutoCard } from './ProdutoCard';

interface ProdutosListProps {
  produtos: Produto[];
}

export function ProdutosList({ produtos }: ProdutosListProps) {
  // ✅ Lógica de interação no cliente
  const [filteredProdutos, setFilteredProdutos] = useState(produtos);

  const handleSearch = (query: string) => {
    const filtered = produtos.filter(p =>
      p.nome.toLowerCase().includes(query.toLowerCase())
    );
    setFilteredProdutos(filtered);
  };

  return (
    <div>
      <SearchBar onSearch={handleSearch} />
      
      <div className="grid">
        {filteredProdutos.map((produto) => (
          <ProdutoCard key={produto.id} produto={produto} />
        ))}
      </div>
    </div>
  );
}
```

### **Regras de Fluxo de Dados**

#### **1. Single Source of Truth (SSoT)**
```tsx
// ✅ CORRETO: Um único estado autoritativo
const [produtos, setProdutos] = useState([]);

// ❌ ERRADO: Estado duplicado/sincronizado
const [produtos, setProdutos] = useState([]);
const [produtosFiltered, setProdutosFiltered] = useState([]);

// ✅ CORRETO: Derivar estado
const produtosFiltered = useMemo(() =>
  produtos.filter(p => p.ativo),
  [produtos]
);
```

#### **2. Props Down, Events Up**
```tsx
// ✅ CORRETO: Dados descem, eventos sobem
<ProdutoCard
  produto={produto}           // ✅ Props descem
  onAddToCart={handleAddToCart} // ✅ Eventos sobem
/>

// ❌ ERRADO: Passar setState como prop
<ProdutoCard
  produto={produto}
  setProdutos={setProdutos}  // ❌ Não passar setState
/>
```

#### **3. Imutabilidade**
```tsx
// ✅ CORRETO: Criar novo objeto/array
setProdutos(prev => [...prev, novoProduto]);
setProdutos(prev => prev.map(p => p.id === id ? { ...p, ...updates } : p));

// ❌ ERRADO: Mutar estado diretamente
produtos.push(novoProduto);     // ❌ Mutação
setProdutos(produtos);          // ❌ Mesma referência
```

#### **4. Colocation (Co-localização de Estado)**
```tsx
// ✅ CORRETO: Estado próximo de onde é usado
function ProdutoCard() {
  const [expanded, setExpanded] = useState(false); // ✅ Local ao componente
  
  return (
    <div>
      <button onClick={() => setExpanded(!expanded)}>Toggle</button>
      {expanded && <ProdutoDetalhes />}
    </div>
  );
}

// ❌ ERRADO: Estado desnecessariamente global
// Não usar Zustand/Context para estado UI local!
```

---

## 5. 🎯 **Princípios SOLID**

### **Visão Geral dos Princípios SOLID em React**

```
┌─────────────────────────────────────────────────────────────┐
│                    SOLID PRINCIPLES                          │
├─────────────────────────────────────────────────────────────┤
│  S - Single Responsibility Principle (SRP)                   │
│      "Um componente deve ter apenas uma razão para mudar"   │
│                                                              │
│  O - Open/Closed Principle (OCP)                             │
│      "Aberto para extensão, fechado para modificação"       │
│                                                              │
│  L - Liskov Substitution Principle (LSP)                     │
│      "Subtipos devem ser substituíveis por seus tipos base" │
│                                                              │
│  I - Interface Segregation Principle (ISP)                   │
│      "Clientes não devem depender de interfaces não usadas" │
│                                                              │
│  D - Dependency Inversion Principle (DIP)                    │
│      "Dependa de abstrações, não de implementações"         │
└─────────────────────────────────────────────────────────────┘
```

---

### **1. Single Responsibility Principle (SRP)**

**Definição**: Cada módulo/componente/função deve ter apenas **uma responsabilidade** e **uma razão para mudar**.

#### **❌ Violação do SRP**

```tsx
// ═══════════════════════════════════════════════════════════
// ❌ RUIM: Componente com múltiplas responsabilidades
// ═══════════════════════════════════════════════════════════

'use client';

export function UserProfile({ userId }: { userId: string }) {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(false);
  
  // ❌ Responsabilidade 1: Buscar dados
  useEffect(() => {
    async function loadUser() {
      setLoading(true);
      const response = await fetch(`/api/users/${userId}`);
      const data = await response.json();
      setUser(data);
      setLoading(false);
    }
    loadUser();
  }, [userId]);
  
  // ❌ Responsabilidade 2: Validação
  const validateEmail = (email: string) => {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
  };
  
  // ❌ Responsabilidade 3: Formatação
  const formatPhone = (phone: string) => {
    return phone.replace(/(\d{2})(\d{5})(\d{4})/, '($1) $2-$3');
  };
  
  // ❌ Responsabilidade 4: Lógica de negócio
  const calculateUserAge = (birthDate: Date) => {
    const today = new Date();
    const age = today.getFullYear() - birthDate.getFullYear();
    return age;
  };
  
  // ❌ Responsabilidade 5: Renderização complexa
  if (loading) return <Spinner />;
  if (!user) return <Error />;
  
  return (
    <div>
      <h1>{user.name}</h1>
      <p>{validateEmail(user.email) ? user.email : 'Email inválido'}</p>
      <p>{formatPhone(user.phone)}</p>
      <p>Idade: {calculateUserAge(new Date(user.birthDate))}</p>
    </div>
  );
}

// ❌ Problema: 5 razões para mudar este componente!
```

#### **✅ Aplicando SRP**

```tsx
// ═══════════════════════════════════════════════════════════
// ✅ BOM: Separação de Responsabilidades
// ═══════════════════════════════════════════════════════════

// 1️⃣ RESPONSABILIDADE: Data Fetching (Hook)
// hooks/useUser.ts
export function useUser(userId: string) {
  const [user, setUser] = useState<User | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<Error | null>(null);
  
  useEffect(() => {
    async function loadUser() {
      setLoading(true);
      try {
        const response = await fetch(`/api/users/${userId}`);
        const data = await response.json();
        setUser(data);
      } catch (err) {
        setError(err instanceof Error ? err : new Error('Failed to load user'));
      } finally {
        setLoading(false);
      }
    }
    loadUser();
  }, [userId]);
  
  return { user, loading, error };
}

// 2️⃣ RESPONSABILIDADE: Validação (Utility)
// lib/utils/validation.ts
export const emailValidator = {
  isValid: (email: string): boolean => {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
  },
  
  validate: (email: string): { valid: boolean; error?: string } => {
    if (!email) {
      return { valid: false, error: 'Email é obrigatório' };
    }
    if (!emailValidator.isValid(email)) {
      return { valid: false, error: 'Email inválido' };
    }
    return { valid: true };
  }
};

// 3️⃣ RESPONSABILIDADE: Formatação (Utility)
// lib/utils/formatters.ts
export const phoneFormatter = {
  format: (phone: string): string => {
    const cleaned = phone.replace(/\D/g, '');
    return cleaned.replace(/(\d{2})(\d{5})(\d{4})/, '($1) $2-$3');
  },
  
  unformat: (phone: string): string => {
    return phone.replace(/\D/g, '');
  }
};

// 4️⃣ RESPONSABILIDADE: Lógica de Negócio (Domain Service)
// lib/domain/services/UserService.ts
export class UserService {
  static calculateAge(birthDate: Date): number {
    const today = new Date();
    const age = today.getFullYear() - birthDate.getFullYear();
    const monthDiff = today.getMonth() - birthDate.getMonth();
    
    if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())) {
      return age - 1;
    }
    
    return age;
  }
  
  static isAdult(birthDate: Date): boolean {
    return UserService.calculateAge(birthDate) >= 18;
  }
}

// 5️⃣ RESPONSABILIDADE: Renderização (View Component)
// components/UserProfile.tsx
import { useUser } from '@/hooks/useUser';
import { emailValidator } from '@/lib/utils/validation';
import { phoneFormatter } from '@/lib/utils/formatters';
import { UserService } from '@/lib/domain/services/UserService';

export function UserProfile({ userId }: { userId: string }) {
  const { user, loading, error } = useUser(userId);
  
  if (loading) return <UserProfileSkeleton />;
  if (error) return <UserProfileError error={error} />;
  if (!user) return null;
  
  return (
    <div className="user-profile">
      <UserAvatar src={user.avatar} alt={user.name} />
      <UserName>{user.name}</UserName>
      <UserEmail email={user.email} isValid={emailValidator.isValid(user.email)} />
      <UserPhone phone={phoneFormatter.format(user.phone)} />
      <UserAge age={UserService.calculateAge(new Date(user.birthDate))} />
    </div>
  );
}

// ✅ Cada módulo tem UMA responsabilidade clara!
```

---

### **2. Open/Closed Principle (OCP)**

**Definição**: Componentes devem ser **abertos para extensão**, mas **fechados para modificação**.

#### **❌ Violação do OCP**

```tsx
// ═══════════════════════════════════════════════════════════
// ❌ RUIM: Modificar componente para cada novo tipo
// ═══════════════════════════════════════════════════════════

export function Button({ type, children }: { type: string; children: React.ReactNode }) {
  // ❌ Precisa modificar o componente para adicionar novos tipos
  if (type === 'primary') {
    return <button className="bg-blue-500 text-white">{children}</button>;
  }
  
  if (type === 'secondary') {
    return <button className="bg-gray-500 text-white">{children}</button>;
  }
  
  if (type === 'danger') {
    return <button className="bg-red-500 text-white">{children}</button>;
  }
  
  // ❌ Adicionar 'success' exige modificar o código existente!
  if (type === 'success') {
    return <button className="bg-green-500 text-white">{children}</button>;
  }
  
  return <button>{children}</button>;
}
```

#### **✅ Aplicando OCP (Composition Pattern)**

```tsx
// ═══════════════════════════════════════════════════════════
// ✅ BOM: Extensível sem modificação
// ═══════════════════════════════════════════════════════════

// 1️⃣ Base Component (Fechado para modificação)
// components/ui/Button.tsx
import { cva, type VariantProps } from 'class-variance-authority';

const buttonVariants = cva(
  // Base styles
  'inline-flex items-center justify-center rounded-md font-medium transition-colors',
  {
    variants: {
      variant: {
        primary: 'bg-blue-500 text-white hover:bg-blue-600',
        secondary: 'bg-gray-500 text-white hover:bg-gray-600',
        danger: 'bg-red-500 text-white hover:bg-red-600',
        ghost: 'hover:bg-gray-100',
        link: 'underline-offset-4 hover:underline',
      },
      size: {
        sm: 'h-9 px-3 text-sm',
        md: 'h-10 px-4',
        lg: 'h-11 px-8',
      },
    },
    defaultVariants: {
      variant: 'primary',
      size: 'md',
    },
  }
);

interface ButtonProps
  extends React.ButtonHTMLAttributes<HTMLButtonElement>,
    VariantProps<typeof buttonVariants> {}

export function Button({ variant, size, className, ...props }: ButtonProps) {
  return (
    <button
      className={buttonVariants({ variant, size, className })}
      {...props}
    />
  );
}

// 2️⃣ Extensão SEM modificação (Composition)
// components/SubmitButton.tsx
export function SubmitButton({ loading, children, ...props }: SubmitButtonProps) {
  return (
    <Button variant="primary" disabled={loading} {...props}>
      {loading ? <Spinner /> : children}
    </Button>
  );
}

// 3️⃣ Nova variante (config, não código)
// tailwind.config.ts - Adicionar nova cor
export default {
  theme: {
    extend: {
      colors: {
        success: '#10b981', // ✅ Extensão via config!
      }
    }
  }
}

// components/ui/Button.tsx (atualizar variants)
const buttonVariants = cva('...', {
  variants: {
    variant: {
      // ... variantes existentes
      success: 'bg-success text-white hover:bg-success/90', // ✅ Adicionar via config
    }
  }
});

// ✅ Componente base não foi modificado, apenas estendido!
```

#### **✅ OCP com Plugin Pattern**

```tsx
// ═══════════════════════════════════════════════════════════
// ✅ Plugin Pattern para extensibilidade
// ═══════════════════════════════════════════════════════════

// lib/notification/types.ts
export interface NotificationPlugin {
  send(message: string, options?: any): Promise<void>;
}

// lib/notification/NotificationService.ts
export class NotificationService {
  private plugins: NotificationPlugin[] = [];
  
  // ✅ Extensível: adicionar plugins sem modificar a classe
  register(plugin: NotificationPlugin) {
    this.plugins.push(plugin);
  }
  
  async notify(message: string, options?: any) {
    await Promise.all(
      this.plugins.map(plugin => plugin.send(message, options))
    );
  }
}

// plugins/EmailNotificationPlugin.ts
export class EmailNotificationPlugin implements NotificationPlugin {
  async send(message: string) {
    console.log('Sending email:', message);
    // Implementação de email
  }
}

// plugins/SMSNotificationPlugin.ts
export class SMSNotificationPlugin implements NotificationPlugin {
  async send(message: string) {
    console.log('Sending SMS:', message);
    // Implementação de SMS
  }
}

// plugins/PushNotificationPlugin.ts (✅ Nova extensão!)
export class PushNotificationPlugin implements NotificationPlugin {
  async send(message: string) {
    console.log('Sending Push:', message);
    // Implementação de Push
  }
}

// Uso
const notificationService = new NotificationService();
notificationService.register(new EmailNotificationPlugin());
notificationService.register(new SMSNotificationPlugin());
notificationService.register(new PushNotificationPlugin()); // ✅ Extensão!

await notificationService.notify('Novo pedido!');
// ✅ NotificationService não foi modificado!
```

---

### **3. Liskov Substitution Principle (LSP)**

**Definição**: Objetos de subtipos devem ser **substituíveis** por objetos de seus tipos base sem quebrar a aplicação.

#### **❌ Violação do LSP**

```tsx
// ═══════════════════════════════════════════════════════════
// ❌ RUIM: Subtipo quebra o contrato do tipo base
// ═══════════════════════════════════════════════════════════

// Base Input Component
interface InputProps {
  value: string;
  onChange: (value: string) => void;
  placeholder?: string;
}

function TextInput({ value, onChange, placeholder }: InputProps) {
  return (
    <input
      type="text"
      value={value}
      onChange={(e) => onChange(e.target.value)}
      placeholder={placeholder}
    />
  );
}

// ❌ Subtipo que quebra o contrato
function ReadOnlyInput({ value, placeholder }: InputProps) {
  // ❌ onChange é obrigatório na interface, mas não faz sentido aqui
  // ❌ Passar ReadOnlyInput onde espera-se InputProps vai quebrar!
  return (
    <input
      type="text"
      value={value}
      placeholder={placeholder}
      readOnly
    />
  );
}

// ❌ Uso quebra quando substitui TextInput por ReadOnlyInput
function Form() {
  const [name, setName] = useState('');
  
  // ✅ Funciona com TextInput
  <TextInput value={name} onChange={setName} />
  
  // ❌ Quebra com ReadOnlyInput (onChange não tem efeito!)
  <ReadOnlyInput value={name} onChange={setName} />
}
```

#### **✅ Aplicando LSP**

```tsx
// ═══════════════════════════════════════════════════════════
// ✅ BOM: Interfaces segregadas e substituíveis
// ═══════════════════════════════════════════════════════════

// Base Props (comum a todos)
interface BaseInputProps {
  value: string;
  placeholder?: string;
  className?: string;
}

// Editable Input Props (adiciona onChange)
interface EditableInputProps extends BaseInputProps {
  onChange: (value: string) => void;
}

// ReadOnly Input Props (sem onChange)
interface ReadOnlyInputProps extends BaseInputProps {
  label?: string;
}

// ✅ TextInput com onChange obrigatório
export function TextInput({ value, onChange, placeholder, className }: EditableInputProps) {
  return (
    <input
      type="text"
      value={value}
      onChange={(e) => onChange(e.target.value)}
      placeholder={placeholder}
      className={className}
    />
  );
}

// ✅ ReadOnlyInput SEM onChange
export function ReadOnlyInput({ value, placeholder, label, className }: ReadOnlyInputProps) {
  return (
    <div className={className}>
      {label && <label>{label}</label>}
      <input
        type="text"
        value={value}
        placeholder={placeholder}
        readOnly
        className="bg-gray-100"
      />
    </div>
  );
}

// ✅ Uso correto: tipos específicos para cada caso
function Form() {
  const [name, setName] = useState('');
  const userId = '12345';
  
  {/* ✅ Editable */}
  <TextInput value={name} onChange={setName} placeholder="Digite seu nome" />
  
  {/* ✅ ReadOnly */}
  <ReadOnlyInput value={userId} label="ID do Usuário" />
}

// ✅ Cada componente tem seu próprio contrato claro!
```

#### **✅ LSP com Polimorfismo de Componentes**

```tsx
// ═══════════════════════════════════════════════════════════
// ✅ Componentes polimórficos substituíveis
// ═══════════════════════════════════════════════════════════

// Interface base
interface Renderable {
  render(): React.ReactNode;
}

// Implementações substituíveis
class UserCard implements Renderable {
  constructor(private user: User) {}
  
  render() {
    return (
      <div className="card">
        <h3>{this.user.name}</h3>
        <p>{this.user.email}</p>
      </div>
    );
  }
}

class ProductCard implements Renderable {
  constructor(private product: Product) {}
  
  render() {
    return (
      <div className="card">
        <h3>{this.product.nome}</h3>
        <p>R$ {this.product.preco}</p>
      </div>
    );
  }
}

// ✅ Componente que aceita qualquer Renderable
function CardList({ items }: { items: Renderable[] }) {
  return (
    <div className="grid">
      {items.map((item, index) => (
        <div key={index}>{item.render()}</div>
      ))}
    </div>
  );
}

// ✅ Uso: substituição transparente
const users = [new UserCard(user1), new UserCard(user2)];
const products = [new ProductCard(prod1), new ProductCard(prod2)];

<CardList items={users} />    {/* ✅ Funciona */}
<CardList items={products} /> {/* ✅ Funciona */}
<CardList items={[...users, ...products]} /> {/* ✅ Funciona! */}
```

---

### **4. Interface Segregation Principle (ISP)**

**Definição**: Clientes não devem ser forçados a depender de interfaces que não usam.

#### **❌ Violação do ISP**

```tsx
// ═══════════════════════════════════════════════════════════
// ❌ RUIM: Interface muito grande (Fat Interface)
// ═══════════════════════════════════════════════════════════

interface DataTableProps {
  data: any[];
  columns: Column[];
  
  // Features que nem todos os componentes precisam
  onSort?: (column: string) => void;
  onFilter?: (filters: Filter[]) => void;
  onPaginate?: (page: number) => void;
  onExport?: (format: 'csv' | 'pdf') => void;
  onRowClick?: (row: any) => void;
  onRowSelect?: (rows: any[]) => void;
  onRowDelete?: (row: any) => void;
  onRowEdit?: (row: any) => void;
  onSearch?: (query: string) => void;
  onRefresh?: () => void;
  
  // Props de configuração
  sortable?: boolean;
  filterable?: boolean;
  paginated?: boolean;
  exportable?: boolean;
  selectable?: boolean;
  editable?: boolean;
  searchable?: boolean;
  refreshable?: boolean;
}

// ❌ Componente simples forçado a lidar com props desnecessárias
function SimpleTable({ data, columns }: DataTableProps) {
  // ❌ Recebe 16+ props, mas usa apenas 2!
  return <table>...</table>;
}
```

#### **✅ Aplicando ISP (Interfaces Segregadas)**

```tsx
// ═══════════════════════════════════════════════════════════
// ✅ BOM: Interfaces pequenas e focadas
// ═══════════════════════════════════════════════════════════

// 1️⃣ Interface Base (mínima)
interface BaseTableProps {
  data: any[];
  columns: Column[];
}

// 2️⃣ Interfaces específicas para cada feature
interface SortableTableProps {
  onSort: (column: string) => void;
  sortColumn?: string;
  sortDirection?: 'asc' | 'desc';
}

interface FilterableTableProps {
  onFilter: (filters: Filter[]) => void;
  filters?: Filter[];
}

interface PaginatedTableProps {
  onPaginate: (page: number) => void;
  currentPage: number;
  totalPages: number;
}

interface SelectableTableProps {
  onRowSelect: (rows: any[]) => void;
  selectedRows: any[];
}

interface ExportableTableProps {
  onExport: (format: 'csv' | 'pdf') => void;
}

// 3️⃣ Componentes específicos usando apenas o que precisam

// ✅ Tabela Simples (apenas base)
export function SimpleTable({ data, columns }: BaseTableProps) {
  return (
    <table>
      <thead>
        <tr>
          {columns.map(col => <th key={col.key}>{col.label}</th>)}
        </tr>
      </thead>
      <tbody>
        {data.map((row, i) => (
          <tr key={i}>
            {columns.map(col => <td key={col.key}>{row[col.key]}</td>)}
          </tr>
        ))}
      </tbody>
    </table>
  );
}

// ✅ Tabela Ordenável (base + sortable)
export function SortableTable({
  data,
  columns,
  onSort,
  sortColumn,
  sortDirection
}: BaseTableProps & SortableTableProps) {
  return (
    <table>
      <thead>
        <tr>
          {columns.map(col => (
            <th key={col.key} onClick={() => onSort(col.key)}>
              {col.label}
              {sortColumn === col.key && (sortDirection === 'asc' ? '↑' : '↓')}
            </th>
          ))}
        </tr>
      </thead>
      {/* ... */}
    </table>
  );
}

// ✅ Tabela Completa (composition de features)
export function AdvancedTable({
  data,
  columns,
  ...props
}: BaseTableProps & 
   SortableTableProps & 
   FilterableTableProps & 
   PaginatedTableProps & 
   SelectableTableProps) {
  return (
    <div>
      <TableFilters filters={props.filters} onFilter={props.onFilter} />
      <SortableTable
        data={data}
        columns={columns}
        onSort={props.onSort}
        sortColumn={props.sortColumn}
        sortDirection={props.sortDirection}
      />
      <TablePagination
        currentPage={props.currentPage}
        totalPages={props.totalPages}
        onPaginate={props.onPaginate}
      />
    </div>
  );
}

// ✅ Uso: Cada componente depende apenas do que precisa!
<SimpleTable data={users} columns={userColumns} />

<SortableTable
  data={products}
  columns={productColumns}
  onSort={handleSort}
  sortColumn="name"
  sortDirection="asc"
/>
```

#### **✅ ISP com Custom Hooks**

```tsx
// ═══════════════════════════════════════════════════════════
// ✅ Hooks segregados por responsabilidade
// ═══════════════════════════════════════════════════════════

// ❌ RUIM: Hook monolítico
function useDataTable() {
  // Retorna 20+ propriedades/métodos que nem todos precisam
  return {
    data, loading, error,
    sort, filter, paginate, export, select, refresh, search,
    // ... muitos mais
  };
}

// ✅ BOM: Hooks específicos
function useTableData() {
  return { data, loading, error };
}

function useTableSort() {
  return { sort, sortColumn, sortDirection };
}

function useTableFilter() {
  return { filter, filters, clearFilters };
}

function useTablePagination() {
  return { currentPage, totalPages, paginate, goToPage };
}

// ✅ Composição quando necessário
function useAdvancedTable() {
  const tableData = useTableData();
  const tableSort = useTableSort();
  const tableFilter = useTableFilter();
  
  return {
    ...tableData,
    ...tableSort,
    ...tableFilter
  };
}

// ✅ Uso: Importar apenas o necessário
function SimpleTableComponent() {
  const { data, loading } = useTableData(); // ✅ Apenas 2 props
  // ...
}

function SortableTableComponent() {
  const { data } = useTableData();
  const { sort, sortColumn } = useTableSort(); // ✅ Apenas sorting
  // ...
}
```

---

### **5. Dependency Inversion Principle (DIP)**

**Definição**: Módulos de alto nível não devem depender de módulos de baixo nível. Ambos devem depender de **abstrações**.

#### **❌ Violação do DIP**

```tsx
// ═══════════════════════════════════════════════════════════
// ❌ RUIM: Dependência direta de implementações concretas
// ═══════════════════════════════════════════════════════════

// Low-level module (implementação concreta)
class LocalStorageService {
  save(key: string, value: any) {
    localStorage.setItem(key, JSON.stringify(value));
  }
  
  load(key: string) {
    const item = localStorage.getItem(key);
    return item ? JSON.parse(item) : null;
  }
}

// ❌ High-level module depende de implementação concreta
class UserPreferences {
  private storage = new LocalStorageService(); // ❌ Acoplamento direto!
  
  saveTheme(theme: string) {
    this.storage.save('theme', theme);
  }
  
  loadTheme() {
    return this.storage.load('theme');
  }
}

// ❌ Problema: Impossível trocar LocalStorage por outro storage sem modificar UserPreferences!
```

#### **✅ Aplicando DIP**

```tsx
// ═══════════════════════════════════════════════════════════
// ✅ BOM: Dependência de abstrações
// ═══════════════════════════════════════════════════════════

// 1️⃣ ABSTRAÇÃO (Interface)
interface StorageService {
  save(key: string, value: any): void;
  load<T>(key: string): T | null;
  remove(key: string): void;
}

// 2️⃣ IMPLEMENTAÇÕES CONCRETAS (Low-level modules)

// LocalStorage Implementation
export class LocalStorageService implements StorageService {
  save(key: string, value: any) {
    localStorage.setItem(key, JSON.stringify(value));
  }
  
  load<T>(key: string): T | null {
    const item = localStorage.getItem(key);
    return item ? JSON.parse(item) : null;
  }
  
  remove(key: string) {
    localStorage.removeItem(key);
  }
}

// SessionStorage Implementation
export class SessionStorageService implements StorageService {
  save(key: string, value: any) {
    sessionStorage.setItem(key, JSON.stringify(value));
  }
  
  load<T>(key: string): T | null {
    const item = sessionStorage.getItem(key);
    return item ? JSON.parse(item) : null;
  }
  
  remove(key: string) {
    sessionStorage.removeItem(key);
  }
}

// IndexedDB Implementation
export class IndexedDBStorageService implements StorageService {
  async save(key: string, value: any) {
    // Implementação com IndexedDB
  }
  
  async load<T>(key: string): Promise<T | null> {
    // Implementação com IndexedDB
    return null;
  }
  
  async remove(key: string) {
    // Implementação com IndexedDB
  }
}

// 3️⃣ HIGH-LEVEL MODULE (depende de abstração)
export class UserPreferences {
  // ✅ Depende da interface, não da implementação!
  constructor(private storage: StorageService) {}
  
  saveTheme(theme: string) {
    this.storage.save('theme', theme);
  }
  
  loadTheme(): string | null {
    return this.storage.load<string>('theme');
  }
  
  clearTheme() {
    this.storage.remove('theme');
  }
}

// 4️⃣ USO: Injeção de Dependência
const localStorage = new LocalStorageService();
const sessionStorage = new SessionStorageService();
const indexedDB = new IndexedDBStorageService();

// ✅ Escolher implementação em tempo de execução!
const userPrefs1 = new UserPreferences(localStorage);
const userPrefs2 = new UserPreferences(sessionStorage);
const userPrefs3 = new UserPreferences(indexedDB);

// ✅ Fácil trocar implementação sem modificar UserPreferences!
```

#### **✅ DIP com React Context (Dependency Injection)**

```tsx
// ═══════════════════════════════════════════════════════════
// ✅ Dependency Injection com Context API
// ═══════════════════════════════════════════════════════════

// 1️⃣ Abstração
interface IAuthService {
  login(email: string, password: string): Promise<User>;
  logout(): Promise<void>;
  getCurrentUser(): User | null;
}

// 2️⃣ Implementações
class NextAuthService implements IAuthService {
  async login(email: string, password: string) {
    // Implementação com NextAuth
    return user;
  }
  
  async logout() {
    // Implementação com NextAuth
  }
  
  getCurrentUser() {
    // Implementação com NextAuth
    return user;
  }
}

class SupabaseAuthService implements IAuthService {
  async login(email: string, password: string) {
    // Implementação com Supabase
    return user;
  }
  
  async logout() {
    // Implementação com Supabase
  }
  
  getCurrentUser() {
    // Implementação com Supabase
    return user;
  }
}

// 3️⃣ Context para Dependency Injection
const AuthServiceContext = createContext<IAuthService | null>(null);

export function AuthServiceProvider({ 
  children,
  service 
}: { 
  children: React.ReactNode;
  service: IAuthService;
}) {
  return (
    <AuthServiceContext.Provider value={service}>
      {children}
    </AuthServiceContext.Provider>
  );
}

export function useAuthService() {
  const context = useContext(AuthServiceContext);
  if (!context) {
    throw new Error('useAuthService must be used within AuthServiceProvider');
  }
  return context;
}

// 4️⃣ Componente depende da abstração
function LoginForm() {
  const authService = useAuthService(); // ✅ Abstração!
  
  const handleLogin = async (email: string, password: string) => {
    await authService.login(email, password);
  };
  
  return <form onSubmit={handleLogin}>...</form>;
}

// 5️⃣ Configuração (escolher implementação)
// app/layout.tsx
const authService = new NextAuthService(); // ou new SupabaseAuthService()

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html>
      <body>
        <AuthServiceProvider service={authService}>
          {children}
        </AuthServiceProvider>
      </body>
    </html>
  );
}

// ✅ LoginForm não sabe qual implementação está usando!
// ✅ Fácil trocar de NextAuth para Supabase sem modificar LoginForm!
```

---

## 6. 🎨 **Padrões de Design**

### **Padrões de Design Aplicados em React/Next.js**

```
┌─────────────────────────────────────────────────────────────┐
│                   DESIGN PATTERNS                            │
├─────────────────────────────────────────────────────────────┤
│  CREATIONAL (Criação)                                        │
│  • Factory Pattern                                           │
│  • Builder Pattern                                           │
│  • Singleton Pattern                                         │
│                                                              │
│  STRUCTURAL (Estrutura)                                      │
│  • Adapter Pattern                                           │
│  • Facade Pattern                                            │
│  • Proxy Pattern                                             │
│  • Composite Pattern                                         │
│                                                              │
│  BEHAVIORAL (Comportamento)                                  │
│  • Observer Pattern                                          │
│  • Strategy Pattern                                          │
│  • Command Pattern                                           │
│  • State Pattern                                             │
│                                                              │
│  REACT-SPECIFIC                                              │
│  • Container/Presenter Pattern                               │
│  • Compound Components                                       │
│  • Render Props                                              │
│  • Higher-Order Components (HOC)                             │
└─────────────────────────────────────────────────────────────┘
```

---

### **1. Factory Pattern**

**Definição**: Criar objetos sem especificar a classe exata do objeto que será criado.

```tsx
// ═══════════════════════════════════════════════════════════
// Factory Pattern - Component Factory
// ═══════════════════════════════════════════════════════════

// 1️⃣ Tipos de notificações
type NotificationType = 'success' | 'error' | 'warning' | 'info';

interface Notification {
  id: string;
  type: NotificationType;
  message: string;
  timestamp: Date;
}

// 2️⃣ Factory para criar componentes de notificação
class NotificationComponentFactory {
  static create(notification: Notification) {
    switch (notification.type) {
      case 'success':
        return <SuccessNotification {...notification} />;
      
      case 'error':
        return <ErrorNotification {...notification} />;
      
      case 'warning':
        return <WarningNotification {...notification} />;
      
      case 'info':
        return <InfoNotification {...notification} />;
      
      default:
        return <DefaultNotification {...notification} />;
    }
  }
}

// 3️⃣ Componentes específicos
function SuccessNotification({ message }: Notification) {
  return (
    <div className="bg-green-100 border-green-500">
      <CheckCircle className="text-green-600" />
      <p>{message}</p>
    </div>
  );
}

function ErrorNotification({ message }: Notification) {
  return (
    <div className="bg-red-100 border-red-500">
      <XCircle className="text-red-600" />
      <p>{message}</p>
    </div>
  );
}

// ... outras implementações

// 4️⃣ Uso
function NotificationList({ notifications }: { notifications: Notification[] }) {
  return (
    <div className="space-y-2">
      {notifications.map((notification) => (
        <div key={notification.id}>
          {NotificationComponentFactory.create(notification)}
        </div>
      ))}
    </div>
  );
}
```

**Factory com Server Actions**:

```tsx
// ═══════════════════════════════════════════════════════════
// Factory Pattern - Action Handler Factory
// ═══════════════════════════════════════════════════════════

// app/actions/factory.ts
'use server';

type ActionType = 'create' | 'update' | 'delete' | 'archive';

interface ActionHandler<T> {
  execute(data: T): Promise<{ success: boolean; data?: any; error?: string }>;
}

// Handlers específicos
class CreateProductHandler implements ActionHandler<CreateProductDTO> {
  async execute(data: CreateProductDTO) {
    const produto = await db.produto.create({ data });
    revalidatePath('/produtos');
    return { success: true, data: produto };
  }
}

class UpdateProductHandler implements ActionHandler<UpdateProductDTO> {
  async execute(data: UpdateProductDTO) {
    const produto = await db.produto.update({
      where: { id: data.id },
      data
    });
    revalidatePath('/produtos');
    return { success: true, data: produto };
  }
}

class DeleteProductHandler implements ActionHandler<{ id: string }> {
  async execute(data: { id: string }) {
    await db.produto.delete({ where: { id: data.id } });
    revalidatePath('/produtos');
    return { success: true };
  }
}

// Factory
export class ProductActionFactory {
  static create(actionType: ActionType): ActionHandler<any> {
    switch (actionType) {
      case 'create':
        return new CreateProductHandler();
      case 'update':
        return new UpdateProductHandler();
      case 'delete':
        return new DeleteProductHandler();
      default:
        throw new Error(`Unknown action type: ${actionType}`);
    }
  }
}

// Uso
export async function handleProductAction(
  actionType: ActionType,
  data: any
) {
  const handler = ProductActionFactory.create(actionType);
  return await handler.execute(data);
}
```

---

### **2. Builder Pattern**

**Definição**: Construir objetos complexos passo a passo.

```tsx
// ═══════════════════════════════════════════════════════════
// Builder Pattern - Query Builder
// ═══════════════════════════════════════════════════════════

// lib/query/QueryBuilder.ts
interface QueryOptions {
  where?: Record<string, any>;
  include?: Record<string, boolean>;
  orderBy?: Record<string, 'asc' | 'desc'>;
  take?: number;
  skip?: number;
  select?: Record<string, boolean>;
}

export class ProductQueryBuilder {
  private options: QueryOptions = {};

  where(conditions: Record<string, any>) {
    this.options.where = { ...this.options.where, ...conditions };
    return this;
  }

  include(relations: Record<string, boolean>) {
    this.options.include = { ...this.options.include, ...relations };
    return this;
  }

  orderBy(field: string, direction: 'asc' | 'desc' = 'asc') {
    this.options.orderBy = { [field]: direction };
    return this;
  }

  limit(count: number) {
    this.options.take = count;
    return this;
  }

  offset(count: number) {
    this.options.skip = count;
    return this;
  }

  select(fields: string[]) {
    this.options.select = fields.reduce((acc, field) => ({
      ...acc,
      [field]: true
    }), {});
    return this;
  }

  onlyActive() {
    return this.where({ ativo: true });
  }

  inStock() {
    return this.where({ estoque: { gt: 0 } });
  }

  async execute() {
    return await db.produto.findMany(this.options);
  }

  build() {
    return this.options;
  }
}

// Uso
const produtos = await new ProductQueryBuilder()
  .where({ categoriaId: '123' })
  .onlyActive()
  .inStock()
  .include({ categoria: true, avaliacoes: true })
  .orderBy('createdAt', 'desc')
  .limit(20)
  .execute();

// ✅ API fluente e legível!
```

**Builder para Componentes Complexos**:

```tsx
// ═══════════════════════════════════════════════════════════
// Builder Pattern - Form Builder
// ═══════════════════════════════════════════════════════════

// lib/forms/FormBuilder.tsx
interface FormField {
  name: string;
  type: 'text' | 'email' | 'number' | 'select' | 'checkbox';
  label: string;
  placeholder?: string;
  validation?: any;
  options?: Array<{ label: string; value: string }>;
}

export class FormBuilder {
  private fields: FormField[] = [];
  private submitLabel: string = 'Enviar';
  private onSubmitHandler?: (data: any) => void;

  addTextField(name: string, label: string, validation?: any) {
    this.fields.push({ name, type: 'text', label, validation });
    return this;
  }

  addEmailField(name: string, label: string, validation?: any) {
    this.fields.push({ name, type: 'email', label, validation });
    return this;
  }

  addSelectField(
    name: string,
    label: string,
    options: Array<{ label: string; value: string }>,
    validation?: any
  ) {
    this.fields.push({ name, type: 'select', label, options, validation });
    return this;
  }

  addCheckboxField(name: string, label: string) {
    this.fields.push({ name, type: 'checkbox', label });
    return this;
  }

  setSubmitLabel(label: string) {
    this.submitLabel = label;
    return this;
  }

  onSubmit(handler: (data: any) => void) {
    this.onSubmitHandler = handler;
    return this;
  }

  build() {
    return (
      <DynamicForm
        fields={this.fields}
        submitLabel={this.submitLabel}
        onSubmit={this.onSubmitHandler}
      />
    );
  }
}

// Uso
const loginForm = new FormBuilder()
  .addEmailField('email', 'E-mail', z.string().email())
  .addTextField('password', 'Senha', z.string().min(6))
  .addCheckboxField('remember', 'Lembrar-me')
  .setSubmitLabel('Entrar')
  .onSubmit((data) => console.log(data))
  .build();

export default function LoginPage() {
  return <div>{loginForm}</div>;
}
```

---

### **3. Singleton Pattern**

**Definição**: Garantir que uma classe tenha apenas uma instância.

```tsx
// ═══════════════════════════════════════════════════════════
// Singleton Pattern - Database Connection
// ═══════════════════════════════════════════════════════════

// lib/db.ts
import { PrismaClient } from '@prisma/client';

class DatabaseConnection {
  private static instance: PrismaClient;

  private constructor() {
    // Construtor privado impede new DatabaseConnection()
  }

  public static getInstance(): PrismaClient {
    if (!DatabaseConnection.instance) {
      DatabaseConnection.instance = new PrismaClient({
        log: process.env.NODE_ENV === 'development' 
          ? ['query', 'error', 'warn'] 
          : ['error'],
      });
    }

    return DatabaseConnection.instance;
  }
}

export const db = DatabaseConnection.getInstance();

// ✅ Sempre a mesma instância em toda aplicação
```

**Singleton com Cache Manager**:

```tsx
// ═══════════════════════════════════════════════════════════
// Singleton Pattern - Cache Manager
// ═══════════════════════════════════════════════════════════

// lib/cache/CacheManager.ts
export class CacheManager {
  private static instance: CacheManager;
  private cache: Map<string, { value: any; expiry: number }>;

  private constructor() {
    this.cache = new Map();
  }

  static getInstance(): CacheManager {
    if (!CacheManager.instance) {
      CacheManager.instance = new CacheManager();
    }
    return CacheManager.instance;
  }

  set(key: string, value: any, ttl: number = 60000) {
    const expiry = Date.now() + ttl;
    this.cache.set(key, { value, expiry });
  }

  get<T>(key: string): T | null {
    const item = this.cache.get(key);
    
    if (!item) return null;
    
    if (Date.now() > item.expiry) {
      this.cache.delete(key);
      return null;
    }
    
    return item.value as T;
  }

  has(key: string): boolean {
    const item = this.cache.get(key);
    
    if (!item) return false;
    
    if (Date.now() > item.expiry) {
      this.cache.delete(key);
      return false;
    }
    
    return true;
  }

  clear() {
    this.cache.clear();
  }

  remove(key: string) {
    this.cache.delete(key);
  }
}

// Uso
const cache = CacheManager.getInstance();

// Set
cache.set('user:123', userData, 5 * 60 * 1000); // 5 minutos

// Get
const user = cache.get<User>('user:123');

// ✅ Mesma instância de cache em toda aplicação
```

---

### **4. Adapter Pattern**

**Definição**: Converter a interface de uma classe em outra interface esperada pelos clientes.

```tsx
// ═══════════════════════════════════════════════════════════
// Adapter Pattern - API Adapter
// ═══════════════════════════════════════════════════════════

// Interfaces padronizadas
interface User {
  id: string;
  name: string;
  email: string;
  avatar?: string;
}

// API externa 1 (formato diferente)
interface SupabaseUser {
  uid: string;
  displayName: string;
  emailAddress: string;
  photoURL?: string;
}

// API externa 2 (formato diferente)
interface Auth0User {
  user_id: string;
  full_name: string;
  email_verified: string;
  picture?: string;
}

// Adapters
export class SupabaseUserAdapter {
  static adapt(supabaseUser: SupabaseUser): User {
    return {
      id: supabaseUser.uid,
      name: supabaseUser.displayName,
      email: supabaseUser.emailAddress,
      avatar: supabaseUser.photoURL
    };
  }
}

export class Auth0UserAdapter {
  static adapt(auth0User: Auth0User): User {
    return {
      id: auth0User.user_id,
      name: auth0User.full_name,
      email: auth0User.email_verified,
      avatar: auth0User.picture
    };
  }
}

// Uso
async function getUser(provider: 'supabase' | 'auth0'): Promise<User> {
  if (provider === 'supabase') {
    const supabaseUser = await supabaseClient.auth.getUser();
    return SupabaseUserAdapter.adapt(supabaseUser);
  } else {
    const auth0User = await auth0Client.getUser();
    return Auth0UserAdapter.adapt(auth0User);
  }
}

// ✅ Componentes usam interface padronizada User
function UserProfile({ user }: { user: User }) {
  return (
    <div>
      <img src={user.avatar} alt={user.name} />
      <h1>{user.name}</h1>
      <p>{user.email}</p>
    </div>
  );
}
```

**Adapter para Payment Providers**:

```tsx
// ═══════════════════════════════════════════════════════════
// Adapter Pattern - Payment Provider Adapter
// ═══════════════════════════════════════════════════════════

// Interface padrão
interface PaymentProvider {
  createPayment(amount: number, currency: string): Promise<PaymentResult>;
  capturePayment(paymentId: string): Promise<boolean>;
  refundPayment(paymentId: string, amount?: number): Promise<boolean>;
}

interface PaymentResult {
  id: string;
  status: 'pending' | 'completed' | 'failed';
  amount: number;
  currency: string;
}

// Adapter para Stripe
export class StripeAdapter implements PaymentProvider {
  private stripe: Stripe;

  constructor(apiKey: string) {
    this.stripe = new Stripe(apiKey, { apiVersion: '2023-10-16' });
  }

  async createPayment(amount: number, currency: string): Promise<PaymentResult> {
    const paymentIntent = await this.stripe.paymentIntents.create({
      amount: amount * 100, // Stripe usa centavos
      currency,
    });

    return {
      id: paymentIntent.id,
      status: paymentIntent.status === 'succeeded' ? 'completed' : 'pending',
      amount,
      currency
    };
  }

  async capturePayment(paymentId: string): Promise<boolean> {
    const result = await this.stripe.paymentIntents.capture(paymentId);
    return result.status === 'succeeded';
  }

  async refundPayment(paymentId: string, amount?: number): Promise<boolean> {
    const refund = await this.stripe.refunds.create({
      payment_intent: paymentId,
      amount: amount ? amount * 100 : undefined
    });
    return refund.status === 'succeeded';
  }
}

// Adapter para PayPal
export class PayPalAdapter implements PaymentProvider {
  private paypal: any;

  constructor(clientId: string, secret: string) {
    // Inicializar PayPal SDK
  }

  async createPayment(amount: number, currency: string): Promise<PaymentResult> {
    const order = await this.paypal.orders.create({
      purchase_units: [{
        amount: { value: amount.toString(), currency_code: currency }
      }]
    });

    return {
      id: order.id,
      status: order.status === 'APPROVED' ? 'completed' : 'pending',
      amount,
      currency
    };
  }

  async capturePayment(paymentId: string): Promise<boolean> {
    const result = await this.paypal.orders.capture(paymentId);
    return result.status === 'COMPLETED';
  }

  async refundPayment(paymentId: string, amount?: number): Promise<boolean> {
    const refund = await this.paypal.payments.refund(paymentId, { amount });
    return refund.state === 'completed';
  }
}

// Service que usa qualquer provider
export class PaymentService {
  constructor(private provider: PaymentProvider) {}

  async processPayment(amount: number, currency: string = 'brl') {
    try {
      const result = await this.provider.createPayment(amount, currency);
      
      if (result.status === 'pending') {
        await this.provider.capturePayment(result.id);
      }
      
      return result;
    } catch (error) {
      console.error('Payment failed:', error);
      throw error;
    }
  }
}

// Uso
const stripeProvider = new StripeAdapter(process.env.STRIPE_KEY!);
const paypalProvider = new PayPalAdapter(
  process.env.PAYPAL_CLIENT_ID!,
  process.env.PAYPAL_SECRET!
);

const paymentService = new PaymentService(stripeProvider);
await paymentService.processPayment(100, 'brl');

// ✅ Trocar provider não afeta PaymentService!
```

---

### **5. Facade Pattern**

**Definição**: Fornecer uma interface simplificada para um sistema complexo.

```tsx
// ═══════════════════════════════════════════════════════════
// Facade Pattern - Order Processing Facade
// ═══════════════════════════════════════════════════════════

// Subsistemas complexos
class InventoryService {
  async checkStock(produtoId: string, quantidade: number): Promise<boolean> {
    // Lógica complexa de verificação de estoque
    return true;
  }

  async reserveStock(produtoId: string, quantidade: number): Promise<void> {
    // Lógica de reserva
  }
}

class PaymentService {
  async processPayment(amount: number, method: string): Promise<string> {
    // Lógica complexa de pagamento
    return 'payment-id-123';
  }
}

class ShippingService {
  async calculateShipping(address: Address): Promise<number> {
    // Cálculo de frete
    return 15.99;
  }

  async createShippingLabel(orderId: string): Promise<string> {
    // Gerar etiqueta
    return 'label-url';
  }
}

class NotificationService {
  async sendOrderConfirmation(userId: string, orderId: string): Promise<void> {
    // Enviar email/SMS
  }
}

// ✅ FACADE: Interface simplificada
export class OrderFacade {
  private inventory = new InventoryService();
  private payment = new PaymentService();
  private shipping = new ShippingService();
  private notification = new NotificationService();

  async createOrder(orderData: CreateOrderDTO): Promise<OrderResult> {
    try {
      // 1. Verificar estoque
      const hasStock = await this.inventory.checkStock(
        orderData.produtoId,
        orderData.quantidade
      );

      if (!hasStock) {
        throw new Error('Produto fora de estoque');
      }

      // 2. Calcular frete
      const shippingCost = await this.shipping.calculateShipping(orderData.address);

      // 3. Processar pagamento
      const totalAmount = orderData.amount + shippingCost;
      const paymentId = await this.payment.processPayment(
        totalAmount,
        orderData.paymentMethod
      );

      // 4. Reservar estoque
      await this.inventory.reserveStock(orderData.produtoId, orderData.quantidade);

      // 5. Criar pedido no banco
      const order = await db.pedido.create({
        data: {
          userId: orderData.userId,
          produtoId: orderData.produtoId,
          quantidade: orderData.quantidade,
          totalAmount,
          paymentId,
          status: 'CONFIRMED'
        }
      });

      // 6. Gerar etiqueta de envio
      const shippingLabel = await this.shipping.createShippingLabel(order.id);

      // 7. Enviar notificação
      await this.notification.sendOrderConfirmation(orderData.userId, order.id);

      return {
        success: true,
        orderId: order.id,
        shippingLabel,
        totalAmount
      };

    } catch (error) {
      console.error('Order creation failed:', error);
      // Rollback logic aqui
      return {
        success: false,
        error: error instanceof Error ? error.message : 'Unknown error'
      };
    }
  }
}

// Uso simples (1 linha vs 7 chamadas)
const orderFacade = new OrderFacade();
const result = await orderFacade.createOrder({
  userId: '123',
  produtoId: '456',
  quantidade: 2,
  amount: 99.99,
  paymentMethod: 'credit_card',
  address: userAddress
});

// ✅ Facade esconde complexidade de 5 subsistemas!
```

---

### **6. Observer Pattern (Pub/Sub)**

**Definição**: Definir uma dependência um-para-muitos entre objetos, onde mudanças em um objeto notificam seus dependentes.

```tsx
// ═══════════════════════════════════════════════════════════
// Observer Pattern - Event Bus
// ═══════════════════════════════════════════════════════════

// lib/events/EventBus.ts
type EventHandler<T = any> = (data: T) => void;

export class EventBus {
  private static instance: EventBus;
  private events: Map<string, EventHandler[]>;

  private constructor() {
    this.events = new Map();
  }

  static getInstance(): EventBus {
    if (!EventBus.instance) {
      EventBus.instance = new EventBus();
    }
    return EventBus.instance;
  }

  // Assinar evento
  on<T>(event: string, handler: EventHandler<T>): () => void {
    if (!this.events.has(event)) {
      this.events.set(event, []);
    }

    this.events.get(event)!.push(handler);

    // Retornar função de cleanup (unsubscribe)
    return () => this.off(event, handler);
  }

  // Desinscrever
  off<T>(event: string, handler: EventHandler<T>): void {
    const handlers = this.events.get(event);
    if (handlers) {
      const index = handlers.indexOf(handler);
      if (index !== -1) {
        handlers.splice(index, 1);
      }
    }
  }

  // Emitir evento
  emit<T>(event: string, data: T): void {
    const handlers = this.events.get(event);
    if (handlers) {
      handlers.forEach(handler => handler(data));
    }
  }

  // Limpar todos os listeners
  clear(): void {
    this.events.clear();
  }
}

// Singleton
export const eventBus = EventBus.getInstance();

// ═══════════════════════════════════════════════════════════
// Uso com React
// ═══════════════════════════════════════════════════════════

// hooks/useEventBus.ts
export function useEventBus<T>(
  event: string,
  handler: EventHandler<T>
) {
  useEffect(() => {
    const unsubscribe = eventBus.on<T>(event, handler);
    return unsubscribe; // Cleanup automático
  }, [event, handler]);
}

// Componente Publisher (emite eventos)
function AddToCartButton({ produto }: { produto: Produto }) {
  const handleClick = () => {
    // Adicionar ao carrinho
    addToCart(produto);
    
    // ✅ Emitir evento
    eventBus.emit('cart:item-added', {
      produtoId: produto.id,
      nome: produto.nome,
      preco: produto.preco
    });
  };

  return <button onClick={handleClick}>Adicionar</button>;
}

// Componente Observer 1 (escuta eventos)
function CartBadge() {
  const [count, setCount] = useState(0);

  useEventBus('cart:item-added', () => {
    setCount(prev => prev + 1);
  });

  return <span className="badge">{count}</span>;
}

// Componente Observer 2 (escuta eventos)
function NotificationToast() {
  const { toast } = useToast();

  useEventBus<{ nome: string; preco: number }>('cart:item-added', (data) => {
    toast({
      title: 'Produto adicionado!',
      description: `${data.nome} - R$ ${data.preco}`
    });
  });

  return null;
}

// Componente Observer 3 (analytics)
function AnalyticsTracker() {
  useEventBus('cart:item-added', (data) => {
    analytics.track('add_to_cart', data);
  });

  return null;
}

// ✅ 1 evento → Múltiplos observadores reagindo!
```

---

### **7. Strategy Pattern**

**Definição**: Definir uma família de algoritmos, encapsular cada um e torná-los intercambiáveis.

```tsx
// ═══════════════════════════════════════════════════════════
// Strategy Pattern - Sorting Strategy
// ═══════════════════════════════════════════════════════════

// Interface Strategy
interface SortStrategy<T> {
  sort(items: T[]): T[];
}

// Strategies concretas
class PriceAscStrategy implements SortStrategy<Produto> {
  sort(items: Produto[]): Produto[] {
    return [...items].sort((a, b) => a.preco - b.preco);
  }
}

class PriceDescStrategy implements SortStrategy<Produto> {
  sort(items: Produto[]): Produto[] {
    return [...items].sort((a, b) => b.preco - a.preco);
  }
}

class NameAscStrategy implements SortStrategy<Produto> {
  sort(items: Produto[]): Produto[] {
    return [...items].sort((a, b) => a.nome.localeCompare(b.nome));
  }
}

class PopularityStrategy implements SortStrategy<Produto> {
  sort(items: Produto[]): Produto[] {
    return [...items].sort((a, b) => b.vendas - a.vendas);
  }
}

// Context
class ProductSorter {
  constructor(private strategy: SortStrategy<Produto>) {}

  setStrategy(strategy: SortStrategy<Produto>) {
    this.strategy = strategy;
  }

  sort(products: Produto[]): Produto[] {
    return this.strategy.sort(products);
  }
}

// ═══════════════════════════════════════════════════════════
// Uso em React
// ═══════════════════════════════════════════════════════════

type SortOption = 'price-asc' | 'price-desc' | 'name' | 'popularity';

function ProductList({ produtos }: { produtos: Produto[] }) {
  const [sortOption, setSortOption] = useState<SortOption>('popularity');

  const getSortedProducts = () => {
    const sorter = new ProductSorter(new PopularityStrategy());

    // ✅ Trocar strategy dinamicamente
    switch (sortOption) {
      case 'price-asc':
        sorter.setStrategy(new PriceAscStrategy());
        break;
      case 'price-desc':
        sorter.setStrategy(new PriceDescStrategy());
        break;
      case 'name':
        sorter.setStrategy(new NameAscStrategy());
        break;
      case 'popularity':
        sorter.setStrategy(new PopularityStrategy());
        break;
    }

    return sorter.sort(produtos);
  };

  const sortedProducts = getSortedProducts();

  return (
    <div>
      <select value={sortOption} onChange={(e) => setSortOption(e.target.value as SortOption)}>
        <option value="popularity">Mais Populares</option>
        <option value="price-asc">Menor Preço</option>
        <option value="price-desc">Maior Preço</option>
        <option value="name">Nome (A-Z)</option>
      </select>

      <div className="grid">
        {sortedProducts.map(produto => (
          <ProductCard key={produto.id} produto={produto} />
        ))}
      </div>
    </div>
  );
}
```

**Strategy para Validação**:

```tsx
// ═══════════════════════════════════════════════════════════
// Strategy Pattern - Validation Strategy
// ═══════════════════════════════════════════════════════════

interface ValidationStrategy {
  validate(value: string): { valid: boolean; error?: string };
}

class EmailValidationStrategy implements ValidationStrategy {
  validate(value: string) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(value)
      ? { valid: true }
      : { valid: false, error: 'Email inválido' };
  }
}

class CPFValidationStrategy implements ValidationStrategy {
  validate(value: string) {
    const cpf = value.replace(/\D/g, '');
    
    if (cpf.length !== 11) {
      return { valid: false, error: 'CPF deve ter 11 dígitos' };
    }
    
    // Lógica de validação de CPF
    return { valid: true };
  }
}

class PhoneValidationStrategy implements ValidationStrategy {
  validate(value: string) {
    const phone = value.replace(/\D/g, '');
    
    if (phone.length < 10 || phone.length > 11) {
      return { valid: false, error: 'Telefone inválido' };
    }
    
    return { valid: true };
  }
}

// Hook para usar strategies
function useValidator(strategy: ValidationStrategy) {
  const validate = useCallback((value: string) => {
    return strategy.validate(value);
  }, [strategy]);

  return { validate };
}

// Uso
function FormField({ type }: { type: 'email' | 'cpf' | 'phone' }) {
  const [value, setValue] = useState('');
  const [error, setError] = useState('');

  const getStrategy = () => {
    switch (type) {
      case 'email':
        return new EmailValidationStrategy();
      case 'cpf':
        return new CPFValidationStrategy();
      case 'phone':
        return new PhoneValidationStrategy();
    }
  };

  const { validate } = useValidator(getStrategy());

  const handleBlur = () => {
    const result = validate(value);
    setError(result.valid ? '' : result.error!);
  };

  return (
    <div>
      <input
        value={value}
        onChange={(e) => setValue(e.target.value)}
        onBlur={handleBlur}
      />
      {error && <span className="error">{error}</span>}
    </div>
  );
}
```

---

## 7. 🗂️ **Feature-Based Organization**

### **Arquitetura por Features (Feature-Slice Design)**

```
┌─────────────────────────────────────────────────────────────┐
│              FEATURE-BASED ORGANIZATION                      │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  ✅ VANTAGENS                                                │
│  • Alta coesão dentro de cada feature                       │
│  • Baixo acoplamento entre features                         │
│  • Escalabilidade horizontal (adicionar features)           │
│  • Desenvolvimento em equipe facilitado                     │
│  • Manutenção isolada por feature                           │
│  • Código relacionado agrupado                              │
│                                                              │
│  📦 ESTRUTURA                                                │
│  features/                                                   │
│    ├── auth/         (Feature 1)                            │
│    ├── products/     (Feature 2)                            │
│    ├── cart/         (Feature 3)                            │
│    └── checkout/     (Feature 4)                            │
│                                                              │
│  🔒 REGRAS                                                   │
│  • Features não importam umas das outras diretamente        │
│  • Shared code em lib/ ou shared/                           │
│  • Cada feature é auto-contida (UI, lógica, tipos)          │
│  • Comunicação via eventos ou shared state                  │
└─────────────────────────────────────────────────────────────┘
```

---

### **Estrutura de uma Feature Completa**

```
features/produtos/
├── components/              # UI Components (Feature-specific)
│   ├── ProdutoCard.tsx
│   ├── ProdutoForm.tsx
│   ├── ProdutoFilters.tsx
│   └── ProdutoDetails.tsx
│
├── hooks/                   # Custom Hooks
│   ├── useProdutos.ts
│   ├── useProdutoFilters.ts
│   ├── useProdutoForm.ts
│   └── useProdutoSearch.ts
│
├── api/                     # API Layer
│   ├── getProdutos.ts
│   ├── createProduto.ts
│   ├── updateProduto.ts
│   └── deleteProduto.ts
│
├── actions/                 # Server Actions (Next.js)
│   └── produto.actions.ts
│
├── types/                   # TypeScript Types
│   ├── produto.types.ts
│   └── produto-form.types.ts
│
├── schemas/                 # Validation Schemas
│   └── produto.schema.ts
│
├── services/                # Business Logic
│   └── ProdutoService.ts
│
├── constants/               # Feature Constants
│   └── produto.constants.ts
│
├── utils/                   # Feature Utilities
│   ├── formatProduto.ts
│   └── calculateDiscount.ts
│
├── contexts/                # Feature-specific Contexts
│   └── ProdutoContext.tsx
│
└── index.ts                 # Public API (exports)
```

---

### **Exemplo Completo: Feature "Produtos"**

#### **1. Types & Schemas**

```tsx
// ═══════════════════════════════════════════════════════════
// features/produtos/types/produto.types.ts
// ═══════════════════════════════════════════════════════════

export interface Produto {
  id: string;
  nome: string;
  descricao: string;
  preco: number;
  estoque: number;
  categoriaId: string;
  categoria?: Categoria;
  imagemUrl?: string;
  ativo: boolean;
  createdAt: Date;
  updatedAt: Date;
}

export interface CreateProdutoDTO {
  nome: string;
  descricao: string;
  preco: number;
  estoque: number;
  categoriaId: string;
  imagemUrl?: string;
}

export interface UpdateProdutoDTO extends Partial<CreateProdutoDTO> {
  id: string;
}

export interface ProdutoFilters {
  categoria?: string;
  precoMin?: number;
  precoMax?: number;
  search?: string;
  emEstoque?: boolean;
}

// ═══════════════════════════════════════════════════════════
// features/produtos/schemas/produto.schema.ts
// ═══════════════════════════════════════════════════════════

import { z } from 'zod';

export const produtoSchema = z.object({
  nome: z.string()
    .min(3, 'Nome deve ter no mínimo 3 caracteres')
    .max(100, 'Nome deve ter no máximo 100 caracteres'),
  
  descricao: z.string()
    .min(10, 'Descrição deve ter no mínimo 10 caracteres')
    .max(500, 'Descrição deve ter no máximo 500 caracteres'),
  
  preco: z.number()
    .positive('Preço deve ser positivo')
    .max(999999, 'Preço muito alto'),
  
  estoque: z.number()
    .int('Estoque deve ser inteiro')
    .nonnegative('Estoque não pode ser negativo'),
  
  categoriaId: z.string().uuid('ID de categoria inválido'),
  
  imagemUrl: z.string().url('URL inválida').optional()
});

export type ProdutoFormData = z.infer<typeof produtoSchema>;
```

#### **2. Server Actions**

```tsx
// ═══════════════════════════════════════════════════════════
// features/produtos/actions/produto.actions.ts
// ═══════════════════════════════════════════════════════════

'use server';

import { revalidatePath, revalidateTag } from 'next/cache';
import { db } from '@/lib/db';
import { produtoSchema } from '../schemas/produto.schema';
import type { CreateProdutoDTO, UpdateProdutoDTO, Produto } from '../types/produto.types';

export async function getProdutos(): Promise<Produto[]> {
  try {
    const produtos = await db.produto.findMany({
      where: { ativo: true },
      include: { categoria: true },
      orderBy: { createdAt: 'desc' }
    });

    return produtos;
  } catch (error) {
    console.error('Erro ao buscar produtos:', error);
    throw new Error('Falha ao carregar produtos');
  }
}

export async function getProdutoById(id: string): Promise<Produto | null> {
  try {
    const produto = await db.produto.findUnique({
      where: { id },
      include: { categoria: true }
    });

    return produto;
  } catch (error) {
    console.error('Erro ao buscar produto:', error);
    return null;
  }
}

export async function createProduto(data: CreateProdutoDTO) {
  const result = produtoSchema.safeParse(data);

  if (!result.success) {
    return {
      success: false,
      errors: result.error.flatten().fieldErrors
    };
  }

  try {
    const produto = await db.produto.create({
      data: {
        ...result.data,
        ativo: true
      }
    });

    revalidatePath('/produtos');
    revalidateTag('produtos');

    return {
      success: true,
      data: produto
    };
  } catch (error) {
    console.error('Erro ao criar produto:', error);
    return {
      success: false,
      error: 'Falha ao criar produto'
    };
  }
}

export async function updateProduto(data: UpdateProdutoDTO) {
  const { id, ...updates } = data;

  const result = produtoSchema.partial().safeParse(updates);

  if (!result.success) {
    return {
      success: false,
      errors: result.error.flatten().fieldErrors
    };
  }

  try {
    const produto = await db.produto.update({
      where: { id },
      data: result.data
    });

    revalidatePath('/produtos');
    revalidatePath(`/produtos/${id}`);
    revalidateTag('produtos');

    return {
      success: true,
      data: produto
    };
  } catch (error) {
    console.error('Erro ao atualizar produto:', error);
    return {
      success: false,
      error: 'Falha ao atualizar produto'
    };
  }
}

export async function deleteProduto(id: string) {
  try {
    await db.produto.delete({ where: { id } });

    revalidatePath('/produtos');
    revalidateTag('produtos');

    return { success: true };
  } catch (error) {
    console.error('Erro ao deletar produto:', error);
    return {
      success: false,
      error: 'Falha ao deletar produto'
    };
  }
}
```

#### **3. Custom Hooks**

```tsx
// ═══════════════════════════════════════════════════════════
// features/produtos/hooks/useProdutos.ts
// ═══════════════════════════════════════════════════════════

'use client';

import { useState, useEffect, useCallback } from 'react';
import { getProdutos } from '../actions/produto.actions';
import type { Produto } from '../types/produto.types';

interface UseProdutosReturn {
  produtos: Produto[];
  loading: boolean;
  error: Error | null;
  refetch: () => Promise<void>;
}

export function useProdutos(): UseProdutosReturn {
  const [produtos, setProdutos] = useState<Produto[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<Error | null>(null);

  const loadProdutos = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);
      
      const data = await getProdutos();
      setProdutos(data);
    } catch (err) {
      setError(err instanceof Error ? err : new Error('Erro desconhecido'));
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    loadProdutos();
  }, [loadProdutos]);

  return {
    produtos,
    loading,
    error,
    refetch: loadProdutos
  };
}

// ═══════════════════════════════════════════════════════════
// features/produtos/hooks/useProdutoFilters.ts
// ═══════════════════════════════════════════════════════════

'use client';

import { useState, useMemo } from 'react';
import type { Produto, ProdutoFilters } from '../types/produto.types';

interface UseProdutoFiltersReturn {
  filtros: ProdutoFilters;
  produtosFiltrados: Produto[];
  setFiltro: (key: keyof ProdutoFilters, value: any) => void;
  limparFiltros: () => void;
}

export function useProdutoFilters(produtos: Produto[]): UseProdutoFiltersReturn {
  const [filtros, setFiltros] = useState<ProdutoFilters>({});

  const setFiltro = (key: keyof ProdutoFilters, value: any) => {
    setFiltros(prev => ({ ...prev, [key]: value }));
  };

  const limparFiltros = () => {
    setFiltros({});
  };

  const produtosFiltrados = useMemo(() => {
    return produtos.filter(produto => {
      // Filtrar por categoria
      if (filtros.categoria && produto.categoriaId !== filtros.categoria) {
        return false;
      }

      // Filtrar por preço mínimo
      if (filtros.precoMin && produto.preco < filtros.precoMin) {
        return false;
      }

      // Filtrar por preço máximo
      if (filtros.precoMax && produto.preco > filtros.precoMax) {
        return false;
      }

      // Filtrar por busca
      if (filtros.search) {
        const searchLower = filtros.search.toLowerCase();
        const matchNome = produto.nome.toLowerCase().includes(searchLower);
        const matchDescricao = produto.descricao.toLowerCase().includes(searchLower);
        
        if (!matchNome && !matchDescricao) {
          return false;
        }
      }

      // Filtrar por estoque
      if (filtros.emEstoque && produto.estoque === 0) {
        return false;
      }

      return true;
    });
  }, [produtos, filtros]);

  return {
    filtros,
    produtosFiltrados,
    setFiltro,
    limparFiltros
  };
}

// ═══════════════════════════════════════════════════════════
// features/produtos/hooks/useProdutoForm.ts
// ═══════════════════════════════════════════════════════════

'use client';

import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { produtoSchema } from '../schemas/produto.schema';
import { createProduto, updateProduto } from '../actions/produto.actions';
import type { ProdutoFormData } from '../schemas/produto.schema';
import type { Produto } from '../types/produto.types';

interface UseProdutoFormProps {
  produto?: Produto;
  onSuccess?: () => void;
}

export function useProdutoForm({ produto, onSuccess }: UseProdutoFormProps = {}) {
  const form = useForm<ProdutoFormData>({
    resolver: zodResolver(produtoSchema),
    defaultValues: produto ? {
      nome: produto.nome,
      descricao: produto.descricao,
      preco: produto.preco,
      estoque: produto.estoque,
      categoriaId: produto.categoriaId,
      imagemUrl: produto.imagemUrl
    } : undefined
  });

  const onSubmit = async (data: ProdutoFormData) => {
    try {
      const result = produto
        ? await updateProduto({ id: produto.id, ...data })
        : await createProduto(data);

      if (result.success) {
        form.reset();
        onSuccess?.();
      } else if (result.errors) {
        // Set form errors
        Object.entries(result.errors).forEach(([key, messages]) => {
          form.setError(key as keyof ProdutoFormData, {
            message: messages?.[0]
          });
        });
      }

      return result;
    } catch (error) {
      console.error('Erro ao salvar produto:', error);
      return { success: false, error: 'Erro ao salvar produto' };
    }
  };

  return {
    form,
    onSubmit: form.handleSubmit(onSubmit)
  };
}
```

#### **4. Components**

```tsx
// ═══════════════════════════════════════════════════════════
// features/produtos/components/ProdutoCard.tsx
// ═══════════════════════════════════════════════════════════

'use client';

import Image from 'next/image';
import { formatCurrency } from '@/lib/utils/formatters';
import type { Produto } from '../types/produto.types';

interface ProdutoCardProps {
  produto: Produto;
  onAddToCart?: (produto: Produto) => void;
}

export function ProdutoCard({ produto, onAddToCart }: ProdutoCardProps) {
  const inStock = produto.estoque > 0;

  return (
    <div className="border rounded-lg overflow-hidden hover:shadow-lg transition-shadow">
      {/* Imagem */}
      <div className="relative h-48 bg-gray-200">
        {produto.imagemUrl ? (
          <Image
            src={produto.imagemUrl}
            alt={produto.nome}
            fill
            className="object-cover"
          />
        ) : (
          <div className="flex items-center justify-center h-full text-gray-400">
            Sem imagem
          </div>
        )}
      </div>

      {/* Conteúdo */}
      <div className="p-4">
        <h3 className="font-semibold text-lg mb-2">{produto.nome}</h3>
        <p className="text-gray-600 text-sm mb-4 line-clamp-2">
          {produto.descricao}
        </p>

        {/* Preço e Estoque */}
        <div className="flex items-center justify-between mb-4">
          <span className="text-2xl font-bold text-blue-600">
            {formatCurrency(produto.preco)}
          </span>
          
          <span className={`text-sm ${inStock ? 'text-green-600' : 'text-red-600'}`}>
            {inStock ? `${produto.estoque} em estoque` : 'Fora de estoque'}
          </span>
        </div>

        {/* Ações */}
        <button
          onClick={() => onAddToCart?.(produto)}
          disabled={!inStock}
          className="w-full bg-blue-600 text-white py-2 rounded-md hover:bg-blue-700 disabled:bg-gray-300 disabled:cursor-not-allowed transition-colors"
        >
          {inStock ? 'Adicionar ao Carrinho' : 'Indisponível'}
        </button>
      </div>
    </div>
  );
}

// ═══════════════════════════════════════════════════════════
// features/produtos/components/ProdutoForm.tsx
// ═══════════════════════════════════════════════════════════

'use client';

import { FormProvider } from 'react-hook-form';
import { useProdutoForm } from '../hooks/useProdutoForm';
import { FormField } from '@/components/forms/FormField';
import { Button } from '@/components/ui/button';
import type { Produto } from '../types/produto.types';

interface ProdutoFormProps {
  produto?: Produto;
  onSuccess?: () => void;
}

export function ProdutoForm({ produto, onSuccess }: ProdutoFormProps) {
  const { form, onSubmit } = useProdutoForm({ produto, onSuccess });

  return (
    <FormProvider {...form}>
      <form onSubmit={onSubmit} className="space-y-4">
        <FormField
          name="nome"
          label="Nome do Produto"
          placeholder="Digite o nome"
        />

        <FormField
          name="descricao"
          label="Descrição"
          placeholder="Digite a descrição"
          type="textarea"
        />

        <div className="grid grid-cols-2 gap-4">
          <FormField
            name="preco"
            label="Preço"
            type="number"
            step="0.01"
            placeholder="0.00"
          />

          <FormField
            name="estoque"
            label="Estoque"
            type="number"
            placeholder="0"
          />
        </div>

        <FormField
          name="categoriaId"
          label="Categoria"
          type="select"
          options={[
            { label: 'Eletrônicos', value: 'cat-1' },
            { label: 'Livros', value: 'cat-2' },
            { label: 'Roupas', value: 'cat-3' }
          ]}
        />

        <FormField
          name="imagemUrl"
          label="URL da Imagem"
          placeholder="https://exemplo.com/imagem.jpg"
        />

        <div className="flex gap-4">
          <Button type="submit" disabled={form.formState.isSubmitting}>
            {form.formState.isSubmitting ? 'Salvando...' : 'Salvar'}
          </Button>
          
          <Button
            type="button"
            variant="secondary"
            onClick={() => form.reset()}
          >
            Limpar
          </Button>
        </div>
      </form>
    </FormProvider>
  );
}

// ═══════════════════════════════════════════════════════════
// features/produtos/components/ProdutoFilters.tsx
// ═══════════════════════════════════════════════════════════

'use client';

import type { ProdutoFilters } from '../types/produto.types';

interface ProdutoFiltersProps {
  filtros: ProdutoFilters;
  onFiltroChange: (key: keyof ProdutoFilters, value: any) => void;
  onLimparFiltros: () => void;
}

export function ProdutoFilters({
  filtros,
  onFiltroChange,
  onLimparFiltros
}: ProdutoFiltersProps) {
  return (
    <div className="bg-white p-4 rounded-lg shadow-md space-y-4">
      {/* Busca */}
      <div>
        <label className="block text-sm font-medium mb-2">Buscar</label>
        <input
          type="text"
          value={filtros.search || ''}
          onChange={(e) => onFiltroChange('search', e.target.value)}
          placeholder="Nome ou descrição..."
          className="w-full px-3 py-2 border rounded-md"
        />
      </div>

      {/* Categoria */}
      <div>
        <label className="block text-sm font-medium mb-2">Categoria</label>
        <select
          value={filtros.categoria || ''}
          onChange={(e) => onFiltroChange('categoria', e.target.value || undefined)}
          className="w-full px-3 py-2 border rounded-md"
        >
          <option value="">Todas</option>
          <option value="cat-1">Eletrônicos</option>
          <option value="cat-2">Livros</option>
          <option value="cat-3">Roupas</option>
        </select>
      </div>

      {/* Faixa de Preço */}
      <div>
        <label className="block text-sm font-medium mb-2">Preço</label>
        <div className="grid grid-cols-2 gap-2">
          <input
            type="number"
            value={filtros.precoMin || ''}
            onChange={(e) => onFiltroChange('precoMin', Number(e.target.value) || undefined)}
            placeholder="Min"
            className="px-3 py-2 border rounded-md"
          />
          <input
            type="number"
            value={filtros.precoMax || ''}
            onChange={(e) => onFiltroChange('precoMax', Number(e.target.value) || undefined)}
            placeholder="Max"
            className="px-3 py-2 border rounded-md"
          />
        </div>
      </div>

      {/* Checkbox - Em Estoque */}
      <div className="flex items-center gap-2">
        <input
          type="checkbox"
          id="emEstoque"
          checked={filtros.emEstoque || false}
          onChange={(e) => onFiltroChange('emEstoque', e.target.checked || undefined)}
          className="rounded"
        />
        <label htmlFor="emEstoque" className="text-sm">
          Apenas em estoque
        </label>
      </div>

      {/* Botão Limpar */}
      <button
        onClick={onLimparFiltros}
        className="w-full bg-gray-200 text-gray-700 py-2 rounded-md hover:bg-gray-300 transition-colors"
      >
        Limpar Filtros
      </button>
    </div>
  );
}
```

#### **5. Public API (Index)**

```tsx
// ═══════════════════════════════════════════════════════════
// features/produtos/index.ts
// ═══════════════════════════════════════════════════════════

// Components
export { ProdutoCard } from './components/ProdutoCard';
export { ProdutoForm } from './components/ProdutoForm';
export { ProdutoFilters } from './components/ProdutoFilters';

// Hooks
export { useProdutos } from './hooks/useProdutos';
export { useProdutoFilters } from './hooks/useProdutoFilters';
export { useProdutoForm } from './hooks/useProdutoForm';

// Actions
export {
  getProdutos,
  getProdutoById,
  createProduto,
  updateProduto,
  deleteProduto
} from './actions/produto.actions';

// Types
export type {
  Produto,
  CreateProdutoDTO,
  UpdateProdutoDTO,
  ProdutoFilters
} from './types/produto.types';

// Schemas
export { produtoSchema } from './schemas/produto.schema';
export type { ProdutoFormData } from './schemas/produto.schema';
```

---

### **Comunicação Entre Features**

#### **1. Via Shared State (Zustand)**

```tsx
// ═══════════════════════════════════════════════════════════
// lib/stores/app.store.ts (Shared Store)
// ═══════════════════════════════════════════════════════════

import { create } from 'zustand';

interface AppStore {
  // Feature: Carrinho
  carrinhoCount: number;
  incrementCarrinho: () => void;
  
  // Feature: Notificações
  notifications: Notification[];
  addNotification: (notification: Notification) => void;
  
  // Feature: User
  user: User | null;
  setUser: (user: User | null) => void;
}

export const useAppStore = create<AppStore>((set) => ({
  carrinhoCount: 0,
  incrementCarrinho: () => set((state) => ({ carrinhoCount: state.carrinhoCount + 1 })),
  
  notifications: [],
  addNotification: (notification) => set((state) => ({
    notifications: [...state.notifications, notification]
  })),
  
  user: null,
  setUser: (user) => set({ user })
}));

// Feature Produtos usa o store
// features/produtos/components/ProdutoCard.tsx
import { useAppStore } from '@/lib/stores/app.store';

export function ProdutoCard({ produto }: { produto: Produto }) {
  const incrementCarrinho = useAppStore((state) => state.incrementCarrinho);
  
  const handleAddToCart = () => {
    // Adicionar ao carrinho
    incrementCarrinho(); // ✅ Atualiza contador global
  };
  
  return <button onClick={handleAddToCart}>Adicionar</button>;
}
```

#### **2. Via Event Bus (Observer Pattern)**

```tsx
// ═══════════════════════════════════════════════════════════
// Comunicação via eventos
// ═══════════════════════════════════════════════════════════

// Feature Produtos emite evento
// features/produtos/components/ProdutoCard.tsx
import { eventBus } from '@/lib/events/EventBus';

export function ProdutoCard({ produto }: { produto: Produto }) {
  const handleAddToCart = () => {
    // Adicionar ao carrinho
    
    // ✅ Emitir evento para outras features
    eventBus.emit('produto:adicionado-ao-carrinho', {
      produtoId: produto.id,
      nome: produto.nome,
      preco: produto.preco
    });
  };
  
  return <button onClick={handleAddToCart}>Adicionar</button>;
}

// Feature Notificações escuta evento
// features/notifications/components/NotificationListener.tsx
import { useEventBus } from '@/lib/events/useEventBus';

export function NotificationListener() {
  useEventBus('produto:adicionado-ao-carrinho', (data) => {
    toast.success(`${data.nome} adicionado ao carrinho!`);
  });
  
  return null;
}

// Feature Analytics escuta evento
// features/analytics/components/AnalyticsTracker.tsx
import { useEventBus } from '@/lib/events/useEventBus';

export function AnalyticsTracker() {
  useEventBus('produto:adicionado-ao-carrinho', (data) => {
    analytics.track('add_to_cart', {
      product_id: data.produtoId,
      product_name: data.nome,
      price: data.preco
    });
  });
  
  return null;
}

// ✅ Features desacopladas, comunicam via eventos!
```

#### **3. Via Shared Utilities**

```tsx
// ═══════════════════════════════════════════════════════════
// lib/shared/formatters.ts (Shared Utilities)
// ═══════════════════════════════════════════════════════════

export const formatCurrency = (value: number): string => {
  return new Intl.NumberFormat('pt-BR', {
    style: 'currency',
    currency: 'BRL'
  }).format(value);
};

export const formatDate = (date: Date): string => {
  return new Intl.DateTimeFormat('pt-BR').format(date);
};

export const formatPhone = (phone: string): string => {
  return phone.replace(/(\d{2})(\d{5})(\d{4})/, '($1) $2-$3');
};

// ✅ Todas as features podem importar de lib/shared
// features/produtos/components/ProdutoCard.tsx
import { formatCurrency } from '@/lib/shared/formatters';

// features/pedidos/components/PedidoCard.tsx
import { formatCurrency, formatDate } from '@/lib/shared/formatters';
```

---

### **Regras de Dependência entre Features**

```
┌─────────────────────────────────────────────────────────────┐
│             REGRAS DE IMPORTAÇÃO ENTRE FEATURES              │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  ❌ PROIBIDO:                                                │
│  • Feature A importar diretamente de Feature B              │
│                                                              │
│    features/produtos/...                                     │
│      ❌ import { ... } from '@/features/carrinho/...'       │
│                                                              │
│  ✅ PERMITIDO:                                               │
│  • Importar de lib/shared                                   │
│  • Importar de lib/utils                                    │
│  • Importar de components/ui (design system)                │
│  • Comunicação via EventBus                                 │
│  • Comunicação via Shared State (Zustand/Context)           │
│                                                              │
│    features/produtos/...                                     │
│      ✅ import { formatCurrency } from '@/lib/shared/...'   │
│      ✅ import { Button } from '@/components/ui/button'     │
│      ✅ import { eventBus } from '@/lib/events/EventBus'    │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

---

## 8. 🌐 **State Management**

### **Estratégias de Gerenciamento de Estado**

```
┌─────────────────────────────────────────────────────────────┐
│                    STATE MANAGEMENT                          │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  1️⃣ LOCAL STATE (Component State)                           │
│     • useState, useReducer                                   │
│     • Escopo: Um único componente                           │
│     • Exemplo: Form inputs, toggle states                   │
│                                                              │
│  2️⃣ SHARED STATE (Component Tree)                           │
│     • Context API, Prop Drilling                            │
│     • Escopo: Árvore de componentes                         │
│     • Exemplo: Theme, Auth user                             │
│                                                              │
│  3️⃣ GLOBAL STATE (Application-wide)                         │
│     • Zustand, Jotai, Redux Toolkit                         │
│     • Escopo: Toda aplicação                                │
│     • Exemplo: Carrinho, Notificações                       │
│                                                              │
│  4️⃣ SERVER STATE (Remote Data)                              │
│     • React Query, SWR, RTK Query                           │
│     • Escopo: Dados do servidor                             │
│     • Exemplo: Produtos, Usuários, Posts                    │
│                                                              │
│  5️⃣ URL STATE (Navigation)                                  │
│     • useSearchParams, useRouter                            │
│     • Escopo: URL e navegação                               │
│     • Exemplo: Filtros, Paginação, Tabs                     │
│                                                              │
│  6️⃣ FORM STATE (Form Data)                                  │
│     • React Hook Form, Formik                               │
│     • Escopo: Formulários                                   │
│     • Exemplo: Login, Cadastro, Checkout                    │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

---

### **1. Local State (useState, useReducer)**

#### **useState - Estado Simples**

```tsx
// ═══════════════════════════════════════════════════════════
// Local State - useState
// ═══════════════════════════════════════════════════════════

'use client';

import { useState } from 'react';

export function Counter() {
  // ✅ Estado local ao componente
  const [count, setCount] = useState(0);

  return (
    <div>
      <p>Count: {count}</p>
      <button onClick={() => setCount(count + 1)}>Increment</button>
      <button onClick={() => setCount(count - 1)}>Decrement</button>
      <button onClick={() => setCount(0)}>Reset</button>
    </div>
  );
}

// ═══════════════════════════════════════════════════════════
// Local State - Multiple States
// ═══════════════════════════════════════════════════════════

export function LoginForm() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError(null);

    try {
      await login(email, password);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Erro desconhecido');
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <input
        type="email"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
        placeholder="Email"
      />
      <input
        type="password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
        placeholder="Senha"
      />
      
      {error && <p className="error">{error}</p>}
      
      <button type="submit" disabled={loading}>
        {loading ? 'Entrando...' : 'Entrar'}
      </button>
    </form>
  );
}
```

#### **useReducer - Estado Complexo**

```tsx
// ═══════════════════════════════════════════════════════════
// useReducer - Complex State Logic
// ═══════════════════════════════════════════════════════════

import { useReducer } from 'react';

// State Type
interface TodoState {
  todos: Todo[];
  filter: 'all' | 'active' | 'completed';
}

// Action Types
type TodoAction =
  | { type: 'ADD_TODO'; payload: string }
  | { type: 'TOGGLE_TODO'; payload: string }
  | { type: 'DELETE_TODO'; payload: string }
  | { type: 'SET_FILTER'; payload: 'all' | 'active' | 'completed' }
  | { type: 'CLEAR_COMPLETED' };

// Reducer
function todoReducer(state: TodoState, action: TodoAction): TodoState {
  switch (action.type) {
    case 'ADD_TODO':
      return {
        ...state,
        todos: [
          ...state.todos,
          {
            id: crypto.randomUUID(),
            text: action.payload,
            completed: false
          }
        ]
      };

    case 'TOGGLE_TODO':
      return {
        ...state,
        todos: state.todos.map(todo =>
          todo.id === action.payload
            ? { ...todo, completed: !todo.completed }
            : todo
        )
      };

    case 'DELETE_TODO':
      return {
        ...state,
        todos: state.todos.filter(todo => todo.id !== action.payload)
      };

    case 'SET_FILTER':
      return {
        ...state,
        filter: action.payload
      };

    case 'CLEAR_COMPLETED':
      return {
        ...state,
        todos: state.todos.filter(todo => !todo.completed)
      };

    default:
      return state;
  }
}

// Component
export function TodoList() {
  const [state, dispatch] = useReducer(todoReducer, {
    todos: [],
    filter: 'all'
  });

  const filteredTodos = state.todos.filter(todo => {
    if (state.filter === 'active') return !todo.completed;
    if (state.filter === 'completed') return todo.completed;
    return true;
  });

  return (
    <div>
      <input
        type="text"
        onKeyDown={(e) => {
          if (e.key === 'Enter' && e.currentTarget.value) {
            dispatch({ type: 'ADD_TODO', payload: e.currentTarget.value });
            e.currentTarget.value = '';
          }
        }}
      />

      <div className="filters">
        <button onClick={() => dispatch({ type: 'SET_FILTER', payload: 'all' })}>
          All
        </button>
        <button onClick={() => dispatch({ type: 'SET_FILTER', payload: 'active' })}>
          Active
        </button>
        <button onClick={() => dispatch({ type: 'SET_FILTER', payload: 'completed' })}>
          Completed
        </button>
      </div>

      <ul>
        {filteredTodos.map(todo => (
          <li key={todo.id}>
            <input
              type="checkbox"
              checked={todo.completed}
              onChange={() => dispatch({ type: 'TOGGLE_TODO', payload: todo.id })}
            />
            <span className={todo.completed ? 'line-through' : ''}>{todo.text}</span>
            <button onClick={() => dispatch({ type: 'DELETE_TODO', payload: todo.id })}>
              Delete
            </button>
          </li>
        ))}
      </ul>

      <button onClick={() => dispatch({ type: 'CLEAR_COMPLETED' })}>
        Clear Completed
      </button>
    </div>
  );
}
```

---

### **2. Shared State (Context API)**

```tsx
// ═══════════════════════════════════════════════════════════
// Context API - Theme Context
// ═══════════════════════════════════════════════════════════

// contexts/ThemeContext.tsx
import { createContext, useContext, useState, useEffect } from 'react';

type Theme = 'light' | 'dark';

interface ThemeContextValue {
  theme: Theme;
  toggleTheme: () => void;
  setTheme: (theme: Theme) => void;
}

const ThemeContext = createContext<ThemeContextValue | undefined>(undefined);

export function ThemeProvider({ children }: { children: React.ReactNode }) {
  const [theme, setTheme] = useState<Theme>('light');

  // Carregar tema do localStorage
  useEffect(() => {
    const savedTheme = localStorage.getItem('theme') as Theme;
    if (savedTheme) {
      setTheme(savedTheme);
    }
  }, []);

  // Salvar tema no localStorage
  useEffect(() => {
    localStorage.setItem('theme', theme);
    document.documentElement.classList.toggle('dark', theme === 'dark');
  }, [theme]);

  const toggleTheme = () => {
    setTheme(prev => prev === 'light' ? 'dark' : 'light');
  };

  return (
    <ThemeContext.Provider value={{ theme, toggleTheme, setTheme }}>
      {children}
    </ThemeContext.Provider>
  );
}

export function useTheme() {
  const context = useContext(ThemeContext);
  if (!context) {
    throw new Error('useTheme must be used within ThemeProvider');
  }
  return context;
}

// ═══════════════════════════════════════════════════════════
// Uso em Componentes
// ═══════════════════════════════════════════════════════════

// app/layout.tsx
export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html>
      <body>
        <ThemeProvider>
          {children}
        </ThemeProvider>
      </body>
    </html>
  );
}

// components/ThemeToggle.tsx
import { useTheme } from '@/contexts/ThemeContext';

export function ThemeToggle() {
  const { theme, toggleTheme } = useTheme();

  return (
    <button onClick={toggleTheme}>
      {theme === 'light' ? '🌙 Dark Mode' : '☀️ Light Mode'}
    </button>
  );
}
```

**Context com useReducer (Advanced)**:

```tsx
// ═══════════════════════════════════════════════════════════
// Context + useReducer - Auth Context
// ═══════════════════════════════════════════════════════════

// contexts/AuthContext.tsx
import { createContext, useContext, useReducer, useEffect } from 'react';

interface User {
  id: string;
  name: string;
  email: string;
}

interface AuthState {
  user: User | null;
  loading: boolean;
  error: string | null;
}

type AuthAction =
  | { type: 'LOGIN_START' }
  | { type: 'LOGIN_SUCCESS'; payload: User }
  | { type: 'LOGIN_FAILURE'; payload: string }
  | { type: 'LOGOUT' }
  | { type: 'SET_USER'; payload: User };

interface AuthContextValue extends AuthState {
  login: (email: string, password: string) => Promise<void>;
  logout: () => void;
}

const AuthContext = createContext<AuthContextValue | undefined>(undefined);

function authReducer(state: AuthState, action: AuthAction): AuthState {
  switch (action.type) {
    case 'LOGIN_START':
      return { ...state, loading: true, error: null };
    
    case 'LOGIN_SUCCESS':
      return { user: action.payload, loading: false, error: null };
    
    case 'LOGIN_FAILURE':
      return { user: null, loading: false, error: action.payload };
    
    case 'LOGOUT':
      return { user: null, loading: false, error: null };
    
    case 'SET_USER':
      return { ...state, user: action.payload };
    
    default:
      return state;
  }
}

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [state, dispatch] = useReducer(authReducer, {
    user: null,
    loading: false,
    error: null
  });

  const login = async (email: string, password: string) => {
    dispatch({ type: 'LOGIN_START' });
    
    try {
      const response = await fetch('/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, password })
      });

      if (!response.ok) throw new Error('Login failed');

      const user = await response.json();
      dispatch({ type: 'LOGIN_SUCCESS', payload: user });
    } catch (error) {
      dispatch({
        type: 'LOGIN_FAILURE',
        payload: error instanceof Error ? error.message : 'Login failed'
      });
    }
  };

  const logout = () => {
    dispatch({ type: 'LOGOUT' });
  };

  return (
    <AuthContext.Provider value={{ ...state, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within AuthProvider');
  }
  return context;
}
```

---

### **3. Global State (Zustand)**

```tsx
// ═══════════════════════════════════════════════════════════
// Zustand - Global State Management
// ═══════════════════════════════════════════════════════════

// lib/stores/cart.store.ts
import { create } from 'zustand';
import { persist } from 'zustand/middleware';

interface CartItem {
  id: string;
  nome: string;
  preco: number;
  quantidade: number;
  imagemUrl?: string;
}

interface CartStore {
  items: CartItem[];
  total: number;
  
  // Actions
  addItem: (item: Omit<CartItem, 'quantidade'>) => void;
  removeItem: (id: string) => void;
  updateQuantity: (id: string, quantidade: number) => void;
  clearCart: () => void;
  
  // Computed
  getItemCount: () => number;
  calculateTotal: () => number;
}

export const useCartStore = create<CartStore>()(
  persist(
    (set, get) => ({
      items: [],
      total: 0,

      addItem: (item) => set((state) => {
        const existingItem = state.items.find(i => i.id === item.id);

        if (existingItem) {
          return {
            items: state.items.map(i =>
              i.id === item.id
                ? { ...i, quantidade: i.quantidade + 1 }
                : i
            )
          };
        }

        return {
          items: [...state.items, { ...item, quantidade: 1 }]
        };
      }),

      removeItem: (id) => set((state) => ({
        items: state.items.filter(i => i.id !== id)
      })),

      updateQuantity: (id, quantidade) => set((state) => {
        if (quantidade <= 0) {
          return { items: state.items.filter(i => i.id !== id) };
        }

        return {
          items: state.items.map(i =>
            i.id === id ? { ...i, quantidade } : i
          )
        };
      }),

      clearCart: () => set({ items: [], total: 0 }),

      getItemCount: () => {
        const { items } = get();
        return items.reduce((sum, item) => sum + item.quantidade, 0);
      },

      calculateTotal: () => {
        const { items } = get();
        const total = items.reduce((sum, item) => sum + (item.preco * item.quantidade), 0);
        set({ total });
        return total;
      }
    }),
    {
      name: 'cart-storage',
      partialize: (state) => ({ items: state.items })
    }
  )
);

// ═══════════════════════════════════════════════════════════
// Uso em Componentes
// ═══════════════════════════════════════════════════════════

// components/CartBadge.tsx
'use client';

import { useCartStore } from '@/lib/stores/cart.store';

export function CartBadge() {
  // ✅ Selecionar apenas o necessário (otimização)
  const itemCount = useCartStore((state) => state.getItemCount());

  return (
    <div className="relative">
      <ShoppingCartIcon />
      {itemCount > 0 && (
        <span className="badge">{itemCount}</span>
      )}
    </div>
  );
}

// components/ProductCard.tsx
'use client';

import { useCartStore } from '@/lib/stores/cart.store';

export function ProductCard({ product }: { product: Product }) {
  const addItem = useCartStore((state) => state.addItem);

  const handleAddToCart = () => {
    addItem({
      id: product.id,
      nome: product.nome,
      preco: product.preco,
      imagemUrl: product.imagemUrl
    });
  };

  return (
    <div>
      <h3>{product.nome}</h3>
      <p>R$ {product.preco}</p>
      <button onClick={handleAddToCart}>Adicionar ao Carrinho</button>
    </div>
  );
}

// components/Cart.tsx
'use client';

import { useCartStore } from '@/lib/stores/cart.store';
import { useEffect } from 'react';

export function Cart() {
  const items = useCartStore((state) => state.items);
  const total = useCartStore((state) => state.total);
  const updateQuantity = useCartStore((state) => state.updateQuantity);
  const removeItem = useCartStore((state) => state.removeItem);
  const clearCart = useCartStore((state) => state.clearCart);
  const calculateTotal = useCartStore((state) => state.calculateTotal);

  // Recalcular total quando items mudar
  useEffect(() => {
    calculateTotal();
  }, [items, calculateTotal]);

  if (items.length === 0) {
    return <p>Carrinho vazio</p>;
  }

  return (
    <div>
      <h2>Carrinho</h2>
      
      {items.map((item) => (
        <div key={item.id} className="cart-item">
          <img src={item.imagemUrl} alt={item.nome} />
          <h3>{item.nome}</h3>
          <p>R$ {item.preco}</p>
          
          <div className="quantity-controls">
            <button onClick={() => updateQuantity(item.id, item.quantidade - 1)}>
              -
            </button>
            <span>{item.quantidade}</span>
            <button onClick={() => updateQuantity(item.id, item.quantidade + 1)}>
              +
            </button>
          </div>

          <button onClick={() => removeItem(item.id)}>Remover</button>
        </div>
      ))}

      <div className="cart-total">
        <h3>Total: R$ {total.toFixed(2)}</h3>
      </div>

      <button onClick={clearCart}>Limpar Carrinho</button>
      <button>Finalizar Compra</button>
    </div>
  );
}
```

**Zustand com Slices (Modular)**:

```tsx
// ═══════════════════════════════════════════════════════════
// Zustand Slices - Modular Store
// ═══════════════════════════════════════════════════════════

// lib/stores/slices/cartSlice.ts
export interface CartSlice {
  items: CartItem[];
  addItem: (item: CartItem) => void;
  removeItem: (id: string) => void;
}

export const createCartSlice = (set: any): CartSlice => ({
  items: [],
  
  addItem: (item) => set((state: any) => ({
    items: [...state.items, item]
  })),
  
  removeItem: (id) => set((state: any) => ({
    items: state.items.filter((i: CartItem) => i.id !== id)
  }))
});

// lib/stores/slices/userSlice.ts
export interface UserSlice {
  user: User | null;
  setUser: (user: User | null) => void;
}

export const createUserSlice = (set: any): UserSlice => ({
  user: null,
  setUser: (user) => set({ user })
});

// lib/stores/appStore.ts
import { create } from 'zustand';
import { createCartSlice, CartSlice } from './slices/cartSlice';
import { createUserSlice, UserSlice } from './slices/userSlice';

type AppStore = CartSlice & UserSlice;

export const useAppStore = create<AppStore>()((...a) => ({
  ...createCartSlice(...a),
  ...createUserSlice(...a)
}));

// ✅ Store modular com slices separados!
```

---

### **4. Server State (React Query)**

```tsx
// ═══════════════════════════════════════════════════════════
// React Query - Server State Management
// ═══════════════════════════════════════════════════════════

// app/providers.tsx
'use client';

import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { ReactQueryDevtools } from '@tanstack/react-query-devtools';
import { useState } from 'react';

export function Providers({ children }: { children: React.ReactNode }) {
  const [queryClient] = useState(() => new QueryClient({
    defaultOptions: {
      queries: {
        staleTime: 60 * 1000, // 1 minuto
        gcTime: 5 * 60 * 1000, // 5 minutos (cacheTime renomeado para gcTime)
        refetchOnWindowFocus: false,
      },
    },
  }));

  return (
    <QueryClientProvider client={queryClient}>
      {children}
      <ReactQueryDevtools initialIsOpen={false} />
    </QueryClientProvider>
  );
}

// ═══════════════════════════════════════════════════════════
// Queries (GET)
// ═══════════════════════════════════════════════════════════

// hooks/useProdutos.ts
import { useQuery } from '@tanstack/react-query';
import { getProdutos } from '@/features/produtos/actions/produto.actions';

export function useProdutos() {
  return useQuery({
    queryKey: ['produtos'],
    queryFn: getProdutos,
    staleTime: 5 * 60 * 1000, // 5 minutos
  });
}

// hooks/useProduto.ts
import { useQuery } from '@tanstack/react-query';
import { getProdutoById } from '@/features/produtos/actions/produto.actions';

export function useProduto(id: string) {
  return useQuery({
    queryKey: ['produto', id],
    queryFn: () => getProdutoById(id),
    enabled: !!id, // Só executar se ID existir
  });
}

// ═══════════════════════════════════════════════════════════
// Mutations (POST/PUT/DELETE)
// ═══════════════════════════════════════════════════════════

// hooks/useProdutoMutations.ts
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { createProduto, updateProduto, deleteProduto } from '@/features/produtos/actions/produto.actions';
import { toast } from 'sonner';

export function useCreateProduto() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: createProduto,
    onSuccess: () => {
      // Invalidar cache
      queryClient.invalidateQueries({ queryKey: ['produtos'] });
      toast.success('Produto criado com sucesso!');
    },
    onError: (error) => {
      toast.error('Erro ao criar produto');
      console.error(error);
    },
  });
}

export function useUpdateProduto() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: updateProduto,
    onSuccess: (data, variables) => {
      // Invalidar lista e detalhe
      queryClient.invalidateQueries({ queryKey: ['produtos'] });
      queryClient.invalidateQueries({ queryKey: ['produto', variables.id] });
      toast.success('Produto atualizado!');
    },
  });
}

export function useDeleteProduto() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: deleteProduto,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['produtos'] });
      toast.success('Produto deletado!');
    },
  });
}

// ═══════════════════════════════════════════════════════════
// Uso em Componentes
// ═══════════════════════════════════════════════════════════

'use client';

import { useProdutos } from '@/hooks/useProdutos';
import { useCreateProduto } from '@/hooks/useProdutoMutations';

export function ProdutosPage() {
  const { data: produtos, isLoading, error } = useProdutos();
  const createMutation = useCreateProduto();

  if (isLoading) return <LoadingSpinner />;
  if (error) return <ErrorMessage error={error} />;

  const handleCreate = async (data: CreateProdutoDTO) => {
    await createMutation.mutateAsync(data);
  };

  return (
    <div>
      <ProdutoForm onSubmit={handleCreate} />
      
      <div className="grid">
        {produtos?.map((produto) => (
          <ProdutoCard key={produto.id} produto={produto} />
        ))}
      </div>
    </div>
  );
}
```

**React Query - Optimistic Updates**:

```tsx
// ═══════════════════════════════════════════════════════════
// Optimistic Updates
// ═══════════════════════════════════════════════════════════

import { useMutation, useQueryClient } from '@tanstack/react-query';

export function useToggleLike(postId: string) {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (liked: boolean) => toggleLike(postId, liked),
    
    // ✅ Optimistic Update (UI atualiza ANTES da resposta)
    onMutate: async (liked) => {
      // Cancelar queries em andamento
      await queryClient.cancelQueries({ queryKey: ['post', postId] });

      // Snapshot do estado anterior (para rollback)
      const previousPost = queryClient.getQueryData(['post', postId]);

      // Atualizar cache otimisticamente
      queryClient.setQueryData(['post', postId], (old: any) => ({
        ...old,
        liked,
        likes: liked ? old.likes + 1 : old.likes - 1
      }));

      // Retornar contexto para rollback
      return { previousPost };
    },

    // ✅ Rollback em caso de erro
    onError: (err, variables, context) => {
      if (context?.previousPost) {
        queryClient.setQueryData(['post', postId], context.previousPost);
      }
      toast.error('Erro ao curtir post');
    },

    // ✅ Refetch após conclusão
    onSettled: () => {
      queryClient.invalidateQueries({ queryKey: ['post', postId] });
    },
  });
}
```

---

## 9. ⚠️ **Error Handling**

### **Estratégias de Tratamento de Erros**

```
┌─────────────────────────────────────────────────────────────┐
│                    ERROR HANDLING STRATEGY                   │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  1️⃣ CLIENT-SIDE ERRORS                                      │
│     • Error Boundaries (React)                              │
│     • Try-Catch blocks                                      │
│     • Promise rejections                                    │
│                                                              │
│  2️⃣ SERVER-SIDE ERRORS                                      │
│     • API Route errors                                      │
│     • Server Action errors                                  │
│     • Database errors                                       │
│                                                              │
│  3️⃣ VALIDATION ERRORS                                       │
│     • Form validation                                       │
│     • Schema validation (Zod)                               │
│     • Type errors                                           │
│                                                              │
│  4️⃣ NETWORK ERRORS                                          │
│     • Fetch failures                                        │
│     • Timeout errors                                        │
│     • Connection errors                                     │
│                                                              │
│  5️⃣ USER FEEDBACK                                           │
│     • Toast notifications                                   │
│     • Error messages                                        │
│     • Retry mechanisms                                      │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

---

### **1. Error Boundaries (React)**

```tsx
// ═══════════════════════════════════════════════════════════
// Error Boundary Component
// ═══════════════════════════════════════════════════════════

// components/ErrorBoundary.tsx
'use client';

import { Component, ReactNode } from 'react';

interface ErrorBoundaryProps {
  children: ReactNode;
  fallback?: (error: Error, retry: () => void) => ReactNode;
}

interface ErrorBoundaryState {
  hasError: boolean;
  error: Error | null;
}

export class ErrorBoundary extends Component<ErrorBoundaryProps, ErrorBoundaryState> {
  constructor(props: ErrorBoundaryProps) {
    super(props);
    this.state = {
      hasError: false,
      error: null
    };
  }

  static getDerivedStateFromError(error: Error): ErrorBoundaryState {
    return {
      hasError: true,
      error
    };
  }

  componentDidCatch(error: Error, errorInfo: React.ErrorInfo) {
    console.error('ErrorBoundary caught an error:', error, errorInfo);
    
    // ✅ Enviar para serviço de logging (Sentry, LogRocket, etc)
    // logErrorToService(error, errorInfo);
  }

  retry = () => {
    this.setState({ hasError: false, error: null });
  };

  render() {
    if (this.state.hasError) {
      if (this.props.fallback) {
        return this.props.fallback(this.state.error!, this.retry);
      }

      return (
        <div className="error-boundary">
          <h2>Algo deu errado</h2>
          <p>{this.state.error?.message}</p>
          <button onClick={this.retry}>Tentar novamente</button>
        </div>
      );
    }

    return this.props.children;
  }
}

// ═══════════════════════════════════════════════════════════
// Uso em Aplicação
// ═══════════════════════════════════════════════════════════

// app/layout.tsx
export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html>
      <body>
        <ErrorBoundary>
          {children}
        </ErrorBoundary>
      </body>
    </html>
  );
}

// app/produtos/page.tsx
export default function ProdutosPage() {
  return (
    <ErrorBoundary
      fallback={(error, retry) => (
        <div className="error-container">
          <h3>Erro ao carregar produtos</h3>
          <p>{error.message}</p>
          <button onClick={retry}>Recarregar</button>
        </div>
      )}
    >
      <ProdutosList />
    </ErrorBoundary>
  );
}
```

**Error Boundary Avançado com Recovery**:

```tsx
// ═══════════════════════════════════════════════════════════
// Advanced Error Boundary com Recovery State
// ═══════════════════════════════════════════════════════════

// components/AdvancedErrorBoundary.tsx
'use client';

import { Component, ReactNode } from 'react';

interface Props {
  children: ReactNode;
  onError?: (error: Error, errorInfo: React.ErrorInfo) => void;
  maxRetries?: number;
}

interface State {
  hasError: boolean;
  error: Error | null;
  retryCount: number;
}

export class AdvancedErrorBoundary extends Component<Props, State> {
  constructor(props: Props) {
    super(props);
    this.state = {
      hasError: false,
      error: null,
      retryCount: 0
    };
  }

  static getDerivedStateFromError(error: Error): Partial<State> {
    return { hasError: true, error };
  }

  componentDidCatch(error: Error, errorInfo: React.ErrorInfo) {
    console.error('Error caught:', error, errorInfo);
    this.props.onError?.(error, errorInfo);
  }

  retry = () => {
    const { maxRetries = 3 } = this.props;
    const { retryCount } = this.state;

    if (retryCount < maxRetries) {
      this.setState({
        hasError: false,
        error: null,
        retryCount: retryCount + 1
      });
    } else {
      console.error('Max retry attempts reached');
    }
  };

  render() {
    const { hasError, error, retryCount } = this.state;
    const { maxRetries = 3 } = this.props;

    if (hasError) {
      return (
        <div className="error-boundary">
          <h2>Erro na aplicação</h2>
          <p>{error?.message}</p>
          
          {retryCount < maxRetries ? (
            <button onClick={this.retry}>
              Tentar novamente ({retryCount + 1}/{maxRetries})
            </button>
          ) : (
            <p>Número máximo de tentativas atingido. Por favor, recarregue a página.</p>
          )}
        </div>
      );
    }

    return this.props.children;
  }
}
```

---

### **2. Server-Side Error Handling**

#### **Server Actions Error Handling**

```tsx
// ═══════════════════════════════════════════════════════════
// Server Action com Error Handling
// ═══════════════════════════════════════════════════════════

// app/produtos/actions.ts
'use server';

import { revalidatePath } from 'next/cache';
import { db } from '@/lib/db';
import { produtoSchema } from './schema';
import { logger } from '@/lib/logger';

// ✅ Type-safe error response
type ActionResult<T> = 
  | { success: true; data: T }
  | { success: false; error: string; code?: string };

export async function createProduto(formData: FormData): Promise<ActionResult<Produto>> {
  try {
    // 1️⃣ Validação
    const rawData = {
      nome: formData.get('nome'),
      preco: Number(formData.get('preco')),
      estoque: Number(formData.get('estoque'))
    };

    const result = produtoSchema.safeParse(rawData);

    if (!result.success) {
      return {
        success: false,
        error: 'Dados inválidos',
        code: 'VALIDATION_ERROR'
      };
    }

    // 2️⃣ Business Logic
    const produto = await db.produto.create({
      data: result.data
    });

    // 3️⃣ Revalidation
    revalidatePath('/produtos');

    return {
      success: true,
      data: produto
    };

  } catch (error) {
    // ✅ Log error
    logger.error('Error creating produto:', error);

    // ✅ Categorizar erro
    if (error instanceof Prisma.PrismaClientKnownRequestError) {
      if (error.code === 'P2002') {
        return {
          success: false,
          error: 'Produto já existe',
          code: 'DUPLICATE_ERROR'
        };
      }
    }

    // ✅ Generic error
    return {
      success: false,
      error: 'Erro ao criar produto',
      code: 'INTERNAL_ERROR'
    };
  }
}

// ═══════════════════════════════════════════════════════════
// Uso no Cliente
// ═══════════════════════════════════════════════════════════

'use client';

import { createProduto } from './actions';
import { toast } from 'sonner';

export function ProdutoForm() {
  const handleSubmit = async (formData: FormData) => {
    const result = await createProduto(formData);

    if (result.success) {
      toast.success('Produto criado com sucesso!');
    } else {
      // ✅ Tratamento específico por código de erro
      switch (result.code) {
        case 'VALIDATION_ERROR':
          toast.error('Dados inválidos. Verifique os campos.');
          break;
        case 'DUPLICATE_ERROR':
          toast.error('Produto já existe no sistema.');
          break;
        default:
          toast.error(result.error);
      }
    }
  };

  return <form action={handleSubmit}>...</form>;
}
```

#### **API Route Error Handling**

```tsx
// ═══════════════════════════════════════════════════════════
// API Route com Error Handling
// ═══════════════════════════════════════════════════════════

// app/api/produtos/route.ts
import { NextRequest, NextResponse } from 'next/server';
import { db } from '@/lib/db';
import { produtoSchema } from '@/app/produtos/schema';

// ✅ Error Handler Utility
class APIError extends Error {
  constructor(
    public statusCode: number,
    public code: string,
    message: string
  ) {
    super(message);
    this.name = 'APIError';
  }
}

export async function GET(request: NextRequest) {
  try {
    const produtos = await db.produto.findMany();

    return NextResponse.json({
      success: true,
      data: produtos
    });

  } catch (error) {
    console.error('GET /api/produtos error:', error);

    return NextResponse.json(
      {
        success: false,
        error: 'Erro ao buscar produtos'
      },
      { status: 500 }
    );
  }
}

export async function POST(request: NextRequest) {
  try {
    // 1️⃣ Parse body
    const body = await request.json();

    // 2️⃣ Validação
    const result = produtoSchema.safeParse(body);

    if (!result.success) {
      throw new APIError(
        400,
        'VALIDATION_ERROR',
        'Dados inválidos'
      );
    }

    // 3️⃣ Create produto
    const produto = await db.produto.create({
      data: result.data
    });

    return NextResponse.json(
      {
        success: true,
        data: produto
      },
      { status: 201 }
    );

  } catch (error) {
    console.error('POST /api/produtos error:', error);

    // ✅ Tratamento de erro específico
    if (error instanceof APIError) {
      return NextResponse.json(
        {
          success: false,
          error: error.message,
          code: error.code
        },
        { status: error.statusCode }
      );
    }

    // ✅ Erro genérico
    return NextResponse.json(
      {
        success: false,
        error: 'Erro interno do servidor'
      },
      { status: 500 }
    );
  }
}
```

---

### **3. Validation Errors (Zod)**

```tsx
// ═══════════════════════════════════════════════════════════
// Validation Error Handling com Zod
// ═══════════════════════════════════════════════════════════

// lib/validation/handleZodError.ts
import { ZodError } from 'zod';

export function handleZodError(error: ZodError) {
  const fieldErrors: Record<string, string[]> = {};

  error.errors.forEach((err) => {
    const path = err.path.join('.');
    if (!fieldErrors[path]) {
      fieldErrors[path] = [];
    }
    fieldErrors[path].push(err.message);
  });

  return {
    success: false,
    errors: fieldErrors
  };
}

// ═══════════════════════════════════════════════════════════
// Uso em Server Action
// ═══════════════════════════════════════════════════════════

'use server';

import { produtoSchema } from './schema';
import { handleZodError } from '@/lib/validation/handleZodError';

export async function createProduto(data: unknown) {
  try {
    const validData = produtoSchema.parse(data);
    
    const produto = await db.produto.create({
      data: validData
    });

    return {
      success: true,
      data: produto
    };

  } catch (error) {
    if (error instanceof ZodError) {
      return handleZodError(error);
    }

    return {
      success: false,
      error: 'Erro ao criar produto'
    };
  }
}

// ═══════════════════════════════════════════════════════════
// Display Validation Errors in Form
// ═══════════════════════════════════════════════════════════

'use client';

import { useActionState } from 'react';
import { createProduto } from './actions';

export function ProdutoForm() {
  const [state, formAction] = useActionState(createProduto, null);

  return (
    <form action={formAction}>
      <div>
        <label>Nome</label>
        <input name="nome" />
        {state?.errors?.nome && (
          <span className="error">{state.errors.nome[0]}</span>
        )}
      </div>

      <div>
        <label>Preço</label>
        <input name="preco" type="number" />
        {state?.errors?.preco && (
          <span className="error">{state.errors.preco[0]}</span>
        )}
      </div>

      <button type="submit">Criar Produto</button>
    </form>
  );
}
```

---

### **4. Network Error Handling**

```tsx
// ═══════════════════════════════════════════════════════════
// Network Error Handling com Retry
// ═══════════════════════════════════════════════════════════

// lib/api/fetchWithRetry.ts
interface FetchOptions extends RequestInit {
  maxRetries?: number;
  retryDelay?: number;
}

export async function fetchWithRetry(
  url: string,
  options: FetchOptions = {}
): Promise<Response> {
  const { maxRetries = 3, retryDelay = 1000, ...fetchOptions } = options;

  for (let attempt = 0; attempt <= maxRetries; attempt++) {
    try {
      const response = await fetch(url, fetchOptions);

      // ✅ Retry on 5xx errors
      if (response.status >= 500 && attempt < maxRetries) {
        await new Promise(resolve => setTimeout(resolve, retryDelay * (attempt + 1)));
        continue;
      }

      return response;

    } catch (error) {
      // ✅ Network error - retry
      if (attempt < maxRetries) {
        console.log(`Retry attempt ${attempt + 1} for ${url}`);
        await new Promise(resolve => setTimeout(resolve, retryDelay * (attempt + 1)));
        continue;
      }

      throw new Error(`Network error after ${maxRetries} retries: ${error}`);
    }
  }

  throw new Error('Max retries reached');
}

// ═══════════════════════════════════════════════════════════
// Hook com Error Handling
// ═══════════════════════════════════════════════════════════

'use client';

import { useState, useCallback } from 'react';
import { fetchWithRetry } from '@/lib/api/fetchWithRetry';

interface FetchState<T> {
  data: T | null;
  loading: boolean;
  error: Error | null;
}

export function useFetch<T>(url: string) {
  const [state, setState] = useState<FetchState<T>>({
    data: null,
    loading: false,
    error: null
  });

  const execute = useCallback(async () => {
    setState({ data: null, loading: true, error: null });

    try {
      const response = await fetchWithRetry(url, {
        maxRetries: 3,
        retryDelay: 1000
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const data = await response.json();
      setState({ data, loading: false, error: null });

    } catch (error) {
      setState({
        data: null,
        loading: false,
        error: error instanceof Error ? error : new Error('Unknown error')
      });
    }
  }, [url]);

  return { ...state, execute };
}

// ═══════════════════════════════════════════════════════════
// Uso em Componente
// ═══════════════════════════════════════════════════════════

'use client';

export function ProdutosList() {
  const { data, loading, error, execute } = useFetch<Produto[]>('/api/produtos');

  useEffect(() => {
    execute();
  }, [execute]);

  if (loading) return <LoadingSpinner />;
  
  if (error) {
    return (
      <div className="error">
        <p>Erro ao carregar produtos: {error.message}</p>
        <button onClick={execute}>Tentar novamente</button>
      </div>
    );
  }

  return (
    <div className="grid">
      {data?.map(produto => (
        <ProdutoCard key={produto.id} produto={produto} />
      ))}
    </div>
  );
}
```

---

### **5. User Feedback (Toast Notifications)**

```tsx
// ═══════════════════════════════════════════════════════════
// Centralized Error Handler com Toast
// ═══════════════════════════════════════════════════════════

// lib/error/ErrorHandler.ts
import { toast } from 'sonner';

export class ErrorHandler {
  static handle(error: unknown, context?: string) {
    console.error(`Error in ${context}:`, error);

    if (error instanceof Error) {
      // ✅ Network errors
      if (error.message.includes('fetch') || error.message.includes('Network')) {
        toast.error('Erro de conexão. Verifique sua internet.');
        return;
      }

      // ✅ Validation errors
      if (error.message.includes('validation')) {
        toast.error('Dados inválidos. Verifique os campos.');
        return;
      }

      // ✅ Generic error
      toast.error(error.message);
    } else {
      toast.error('Erro desconhecido');
    }
  }

  static handleAsync<T>(
    promise: Promise<T>,
    context?: string
  ): Promise<T | null> {
    return promise.catch((error) => {
      this.handle(error, context);
      return null;
    });
  }
}

// ═══════════════════════════════════════════════════════════
// Uso em Componentes
// ═══════════════════════════════════════════════════════════

'use client';

import { ErrorHandler } from '@/lib/error/ErrorHandler';
import { createProduto } from './actions';

export function ProdutoForm() {
  const handleSubmit = async (formData: FormData) => {
    const result = await ErrorHandler.handleAsync(
      createProduto(formData),
      'createProduto'
    );

    if (result?.success) {
      toast.success('Produto criado com sucesso!');
    }
  };

  return <form action={handleSubmit}>...</form>;
}
```

**Custom Error Hook**:

```tsx
// ═══════════════════════════════════════════════════════════
// Custom Hook para Error Handling
// ═══════════════════════════════════════════════════════════

// hooks/useErrorHandler.ts
import { useCallback } from 'react';
import { toast } from 'sonner';

interface ErrorHandlerOptions {
  onError?: (error: Error) => void;
  showToast?: boolean;
  retryable?: boolean;
}

export function useErrorHandler(options: ErrorHandlerOptions = {}) {
  const { onError, showToast = true, retryable = false } = options;

  const handleError = useCallback((error: unknown, retry?: () => void) => {
    const errorObj = error instanceof Error ? error : new Error(String(error));

    console.error('Error:', errorObj);

    if (showToast) {
      if (retryable && retry) {
        toast.error(errorObj.message, {
          action: {
            label: 'Tentar novamente',
            onClick: retry
          }
        });
      } else {
        toast.error(errorObj.message);
      }
    }

    onError?.(errorObj);
  }, [onError, showToast, retryable]);

  return { handleError };
}

// ═══════════════════════════════════════════════════════════
// Uso
// ═══════════════════════════════════════════════════════════

'use client';

export function ProdutosList() {
  const { handleError } = useErrorHandler({ retryable: true });
  const [produtos, setProdutos] = useState<Produto[]>([]);

  const loadProdutos = async () => {
    try {
      const data = await getProdutos();
      setProdutos(data);
    } catch (error) {
      handleError(error, loadProdutos);
    }
  };

  useEffect(() => {
    loadProdutos();
  }, []);

  return <div>...</div>;
}
```

---

### **6. Global Error Handling**

```tsx
// ═══════════════════════════════════════════════════════════
// Global Error Handler (Next.js)
// ═══════════════════════════════════════════════════════════

// app/global-error.tsx
'use client';

export default function GlobalError({
  error,
  reset,
}: {
  error: Error & { digest?: string };
  reset: () => void;
}) {
  return (
    <html>
      <body>
        <div className="global-error">
          <h2>Algo deu muito errado!</h2>
          <p>{error.message}</p>
          {error.digest && <p>Error ID: {error.digest}</p>}
          <button onClick={reset}>Tentar novamente</button>
        </div>
      </body>
    </html>
  );
}

// app/error.tsx (Page-level error)
'use client';

export default function Error({
  error,
  reset,
}: {
  error: Error & { digest?: string };
  reset: () => void;
}) {
  useEffect(() => {
    // Log error to error reporting service
    console.error('Page error:', error);
  }, [error]);

  return (
    <div className="error-page">
      <h2>Erro na página</h2>
      <p>{error.message}</p>
      <button onClick={reset}>Tentar novamente</button>
    </div>
  );
}
```

---

## 10. ⚡ **Performance Optimization**

### **Estratégias de Otimização de Performance**

```
┌─────────────────────────────────────────────────────────────┐
│                PERFORMANCE OPTIMIZATION                      │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  1️⃣ CODE SPLITTING                                          │
│     • Dynamic Imports                                       │
│     • React.lazy()                                          │
│     • Route-based splitting                                 │
│                                                              │
│  2️⃣ LAZY LOADING                                            │
│     • Components                                            │
│     • Images                                                │
│     • Third-party libraries                                 │
│                                                              │
│  3️⃣ MEMOIZATION                                             │
│     • React.memo()                                          │
│     • useMemo()                                             │
│     • useCallback()                                         │
│                                                              │
│  4️⃣ VIRTUALIZATION                                          │
│     • Virtual scrolling                                     │
│     • Windowing                                             │
│     • Infinite scroll                                       │
│                                                              │
│  5️⃣ IMAGE OPTIMIZATION                                      │
│     • Next.js Image                                         │
│     • Responsive images                                     │
│     • Format optimization                                   │
│                                                              │
│  6️⃣ BUNDLE OPTIMIZATION                                     │
│     • Tree shaking                                          │
│     • Dead code elimination                                 │
│     • Bundle analysis                                       │
│                                                              │
│  7️⃣ CORE WEB VITALS                                         │
│     • LCP (Largest Contentful Paint)                        │
│     • FID (First Input Delay)                               │
│     • CLS (Cumulative Layout Shift)                         │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

---

### **1. Code Splitting**

#### **Dynamic Imports**

```tsx
// ═══════════════════════════════════════════════════════════
// Dynamic Imports para Code Splitting
// ═══════════════════════════════════════════════════════════

// ❌ RUIM: Import estático (bundle único grande)
import HeavyChart from '@/components/HeavyChart';
import HeavyEditor from '@/components/HeavyEditor';
import HeavyMap from '@/components/HeavyMap';

export function Dashboard() {
  return (
    <div>
      <HeavyChart />
      <HeavyEditor />
      <HeavyMap />
    </div>
  );
}

// ✅ BOM: Dynamic Import (code splitting automático)
import dynamic from 'next/dynamic';

const HeavyChart = dynamic(() => import('@/components/HeavyChart'), {
  loading: () => <ChartSkeleton />,
  ssr: false // Desabilitar SSR se necessário
});

const HeavyEditor = dynamic(() => import('@/components/HeavyEditor'), {
  loading: () => <EditorSkeleton />
});

const HeavyMap = dynamic(() => import('@/components/HeavyMap'), {
  loading: () => <MapSkeleton />,
  ssr: false
});

export function Dashboard() {
  return (
    <div>
      <HeavyChart />
      <HeavyEditor />
      <HeavyMap />
    </div>
  );
}
```

#### **React.lazy() com Suspense**

```tsx
// ═══════════════════════════════════════════════════════════
// React.lazy() para Code Splitting
// ═══════════════════════════════════════════════════════════

'use client';

import { lazy, Suspense } from 'react';

// ✅ Lazy loading de componentes
const AdminPanel = lazy(() => import('@/components/AdminPanel'));
const Analytics = lazy(() => import('@/components/Analytics'));
const Reports = lazy(() => import('@/components/Reports'));

export function DashboardPage() {
  const [activeTab, setActiveTab] = useState<'admin' | 'analytics' | 'reports'>('admin');

  return (
    <div>
      <Tabs value={activeTab} onValueChange={setActiveTab}>
        <TabsList>
          <TabsTrigger value="admin">Admin</TabsTrigger>
          <TabsTrigger value="analytics">Analytics</TabsTrigger>
          <TabsTrigger value="reports">Reports</TabsTrigger>
        </TabsList>

        <TabsContent value="admin">
          <Suspense fallback={<AdminPanelSkeleton />}>
            <AdminPanel />
          </Suspense>
        </TabsContent>

        <TabsContent value="analytics">
          <Suspense fallback={<AnalyticsSkeleton />}>
            <Analytics />
          </Suspense>
        </TabsContent>

        <TabsContent value="reports">
          <Suspense fallback={<ReportsSkeleton />}>
            <Reports />
          </Suspense>
        </TabsContent>
      </Tabs>
    </div>
  );
}
```

#### **Route-Based Code Splitting**

```tsx
// ═══════════════════════════════════════════════════════════
// Route-Based Code Splitting (Next.js automático)
// ═══════════════════════════════════════════════════════════

// app/produtos/page.tsx
export default function ProdutosPage() {
  // ✅ Código isolado nesta rota (chunk separado)
  return <ProdutosList />;
}

// app/carrinho/page.tsx
export default function CarrinhoPage() {
  // ✅ Código isolado nesta rota (chunk separado)
  return <Carrinho />;
}

// ✅ Next.js cria bundles separados automaticamente:
// - _app-pages-produtos-page.js
// - _app-pages-carrinho-page.js
```

---

### **2. Lazy Loading**

#### **Lazy Loading de Componentes**

```tsx
// ═══════════════════════════════════════════════════════════
// Lazy Loading Condicional
// ═══════════════════════════════════════════════════════════

'use client';

import dynamic from 'next/dynamic';
import { useState } from 'react';

// ✅ Carregar apenas quando modal for aberto
const ProductModal = dynamic(() => import('@/components/ProductModal'));

export function ProductCard({ product }: { product: Product }) {
  const [isModalOpen, setIsModalOpen] = useState(false);

  return (
    <div className="product-card">
      <h3>{product.nome}</h3>
      <button onClick={() => setIsModalOpen(true)}>
        Ver Detalhes
      </button>

      {/* ✅ Modal só carrega quando isModalOpen = true */}
      {isModalOpen && (
        <ProductModal
          product={product}
          onClose={() => setIsModalOpen(false)}
        />
      )}
    </div>
  );
}
```

#### **Lazy Loading de Imagens**

```tsx
// ═══════════════════════════════════════════════════════════
// Next.js Image com Lazy Loading
// ═══════════════════════════════════════════════════════════

import Image from 'next/image';

export function ProductCard({ product }: { product: Product }) {
  return (
    <div className="product-card">
      {/* ✅ Lazy loading automático + otimização */}
      <Image
        src={product.imagemUrl}
        alt={product.nome}
        width={300}
        height={300}
        loading="lazy" // ✅ Lazy loading nativo
        placeholder="blur" // ✅ Blur placeholder
        blurDataURL={product.blurDataURL}
        quality={75} // ✅ Qualidade otimizada
      />

      <h3>{product.nome}</h3>
      <p>R$ {product.preco}</p>
    </div>
  );
}

// ✅ Responsive Images
export function HeroImage() {
  return (
    <Image
      src="/hero.jpg"
      alt="Hero"
      fill // ✅ Preenche container
      sizes="(max-width: 768px) 100vw, (max-width: 1200px) 50vw, 33vw"
      priority // ✅ Não lazy load (above the fold)
    />
  );
}
```

#### **Lazy Loading de Bibliotecas**

```tsx
// ═══════════════════════════════════════════════════════════
// Lazy Loading de Third-Party Libraries
// ═══════════════════════════════════════════════════════════

'use client';

import { useState } from 'react';

export function PdfViewer({ url }: { url: string }) {
  const [showPdf, setShowPdf] = useState(false);

  const loadPdf = async () => {
    // ✅ Carregar react-pdf apenas quando necessário
    const { Document, Page } = await import('react-pdf');
    setShowPdf(true);
  };

  return (
    <div>
      {!showPdf ? (
        <button onClick={loadPdf}>Visualizar PDF</button>
      ) : (
        // Renderizar PDF
        <div>PDF content</div>
      )}
    </div>
  );
}

// ✅ Exemplo com Chart.js
export function ChartComponent({ data }: { data: any[] }) {
  const [Chart, setChart] = useState<any>(null);

  useEffect(() => {
    // ✅ Carregar Chart.js apenas no cliente
    import('chart.js').then((module) => {
      setChart(() => module.Chart);
    });
  }, []);

  if (!Chart) return <ChartSkeleton />;

  return <canvas ref={chartRef} />;
}
```

---

### **3. Memoization**

#### **React.memo() - Componentes**

```tsx
// ═══════════════════════════════════════════════════════════
// React.memo() para evitar re-renders desnecessários
// ═══════════════════════════════════════════════════════════

'use client';

// ❌ RUIM: Re-renderiza sempre que pai re-renderizar
export function ProductCard({ product }: { product: Product }) {
  console.log('ProductCard render');
  return (
    <div>
      <h3>{product.nome}</h3>
      <p>R$ {product.preco}</p>
    </div>
  );
}

// ✅ BOM: Só re-renderiza se props mudarem
export const ProductCard = memo(function ProductCard({ product }: { product: Product }) {
  console.log('ProductCard render');
  return (
    <div>
      <h3>{product.nome}</h3>
      <p>R$ {product.preco}</p>
    </div>
  );
});

// ✅ Com comparação customizada
export const ProductCard = memo(
  function ProductCard({ product }: { product: Product }) {
    return (
      <div>
        <h3>{product.nome}</h3>
        <p>R$ {product.preco}</p>
      </div>
    );
  },
  (prevProps, nextProps) => {
    // ✅ Retornar true para EVITAR re-render
    return prevProps.product.id === nextProps.product.id &&
           prevProps.product.preco === nextProps.product.preco;
  }
);
```

#### **useMemo() - Valores Computados**

```tsx
// ═══════════════════════════════════════════════════════════
// useMemo() para cálculos custosos
// ═══════════════════════════════════════════════════════════

'use client';

import { useMemo } from 'react';

export function ProductsList({ produtos }: { produtos: Product[] }) {
  const [searchQuery, setSearchQuery] = useState('');
  const [priceRange, setPriceRange] = useState({ min: 0, max: 1000 });

  // ❌ RUIM: Recalcula a cada render
  const filteredProdutos = produtos
    .filter(p => p.nome.toLowerCase().includes(searchQuery.toLowerCase()))
    .filter(p => p.preco >= priceRange.min && p.preco <= priceRange.max)
    .sort((a, b) => a.preco - b.preco);

  // ✅ BOM: Só recalcula quando dependências mudarem
  const filteredProdutos = useMemo(() => {
    console.log('Recalculando produtos filtrados');
    return produtos
      .filter(p => p.nome.toLowerCase().includes(searchQuery.toLowerCase()))
      .filter(p => p.preco >= priceRange.min && p.preco <= priceRange.max)
      .sort((a, b) => a.preco - b.preco);
  }, [produtos, searchQuery, priceRange]);

  return (
    <div>
      <SearchBar value={searchQuery} onChange={setSearchQuery} />
      <PriceRangeFilter value={priceRange} onChange={setPriceRange} />

      <div className="grid">
        {filteredProdutos.map(produto => (
          <ProductCard key={produto.id} produto={produto} />
        ))}
      </div>
    </div>
  );
}

// ✅ useMemo para objetos/arrays (evitar nova referência)
export function UserProfile({ userId }: { userId: string }) {
  const { data: user } = useUser(userId);

  // ✅ Evitar criar novo objeto a cada render
  const userStats = useMemo(() => ({
    totalPedidos: user?.pedidos?.length || 0,
    totalGasto: user?.pedidos?.reduce((acc, p) => acc + p.total, 0) || 0,
    produtosFavoritos: user?.favoritos?.length || 0
  }), [user]);

  return <StatsDisplay stats={userStats} />;
}
```

#### **useCallback() - Funções**

```tsx
// ═══════════════════════════════════════════════════════════
// useCallback() para funções estáveis
// ═══════════════════════════════════════════════════════════

'use client';

import { useCallback, memo } from 'react';

// ❌ RUIM: Nova função a cada render
export function ProductsList() {
  const [produtos, setProdutos] = useState<Product[]>([]);

  // ❌ handleDelete é uma nova função a cada render
  const handleDelete = (id: string) => {
    setProdutos(prev => prev.filter(p => p.id !== id));
  };

  return (
    <div>
      {produtos.map(produto => (
        <ProductCard
          key={produto.id}
          produto={produto}
          onDelete={handleDelete} // ❌ Nova referência a cada render
        />
      ))}
    </div>
  );
}

// ✅ BOM: Função estável com useCallback
export function ProductsList() {
  const [produtos, setProdutos] = useState<Product[]>([]);

  // ✅ Função estável (mesma referência)
  const handleDelete = useCallback((id: string) => {
    setProdutos(prev => prev.filter(p => p.id !== id));
  }, []); // ✅ Sem dependências (usa forma funcional de setState)

  return (
    <div>
      {produtos.map(produto => (
        <ProductCard
          key={produto.id}
          produto={produto}
          onDelete={handleDelete} // ✅ Mesma referência
        />
      ))}
    </div>
  );
}

// ✅ ProductCard memo não re-renderiza desnecessariamente
const ProductCard = memo(function ProductCard({
  produto,
  onDelete
}: {
  produto: Product;
  onDelete: (id: string) => void;
}) {
  return (
    <div>
      <h3>{produto.nome}</h3>
      <button onClick={() => onDelete(produto.id)}>Deletar</button>
    </div>
  );
});
```

---

### **4. Virtualization (Virtual Scrolling)**

```tsx
// ═══════════════════════════════════════════════════════════
// Virtualization com react-window
// ═══════════════════════════════════════════════════════════

'use client';

import { FixedSizeList as List } from 'react-window';

// ❌ RUIM: Renderizar 10.000 itens (DOM gigante)
export function ProductsList({ produtos }: { produtos: Product[] }) {
  return (
    <div className="produtos-list">
      {produtos.map(produto => (
        <ProductCard key={produto.id} produto={produto} />
      ))}
    </div>
  );
}

// ✅ BOM: Virtualização (apenas itens visíveis no DOM)
export function VirtualizedProductsList({ produtos }: { produtos: Product[] }) {
  const Row = ({ index, style }: { index: number; style: React.CSSProperties }) => (
    <div style={style}>
      <ProductCard produto={produtos[index]} />
    </div>
  );

  return (
    <List
      height={600} // Altura do container
      itemCount={produtos.length}
      itemSize={150} // Altura de cada item
      width="100%"
    >
      {Row}
    </List>
  );
}

// ✅ Variable Size List (itens com alturas diferentes)
import { VariableSizeList } from 'react-window';

export function VariableSizeProductsList({ produtos }: { produtos: Product[] }) {
  const getItemSize = (index: number) => {
    // Altura dinâmica baseada no conteúdo
    return produtos[index].descricao.length > 100 ? 200 : 150;
  };

  const Row = ({ index, style }: { index: number; style: React.CSSProperties }) => (
    <div style={style}>
      <ProductCard produto={produtos[index]} />
    </div>
  );

  return (
    <VariableSizeList
      height={600}
      itemCount={produtos.length}
      itemSize={getItemSize}
      width="100%"
    >
      {Row}
    </VariableSizeList>
  );
}
```

#### **Infinite Scroll com Intersection Observer**

```tsx
// ═══════════════════════════════════════════════════════════
// Infinite Scroll Otimizado
// ═══════════════════════════════════════════════════════════

'use client';

import { useRef, useEffect, useState } from 'react';

export function InfiniteProductsList() {
  const [produtos, setProdutos] = useState<Product[]>([]);
  const [page, setPage] = useState(1);
  const [hasMore, setHasMore] = useState(true);
  const [loading, setLoading] = useState(false);
  const observerTarget = useRef<HTMLDivElement>(null);

  const loadMore = async () => {
    if (loading || !hasMore) return;

    setLoading(true);
    const newProdutos = await fetchProdutos(page);

    if (newProdutos.length === 0) {
      setHasMore(false);
    } else {
      setProdutos(prev => [...prev, ...newProdutos]);
      setPage(prev => prev + 1);
    }

    setLoading(false);
  };

  // ✅ Intersection Observer para detectar scroll
  useEffect(() => {
    const observer = new IntersectionObserver(
      (entries) => {
        if (entries[0].isIntersecting) {
          loadMore();
        }
      },
      { threshold: 1.0 }
    );

    if (observerTarget.current) {
      observer.observe(observerTarget.current);
    }

    return () => {
      if (observerTarget.current) {
        observer.unobserve(observerTarget.current);
      }
    };
  }, [page, hasMore, loading]);

  return (
    <div>
      <div className="grid">
        {produtos.map(produto => (
          <ProductCard key={produto.id} produto={produto} />
        ))}
      </div>

      {/* ✅ Observer target */}
      <div ref={observerTarget} className="h-10" />

      {loading && <LoadingSpinner />}
      {!hasMore && <p>Não há mais produtos</p>}
    </div>
  );
}
```

---

### **5. Image Optimization**

```tsx
// ═══════════════════════════════════════════════════════════
// Next.js Image Optimization
// ═══════════════════════════════════════════════════════════

import Image from 'next/image';

// ✅ Otimização automática de imagens
export function ProductCard({ product }: { product: Product }) {
  return (
    <div className="product-card">
      <Image
        src={product.imagemUrl}
        alt={product.nome}
        width={300}
        height={300}
        quality={75} // ✅ 75% qualidade (bom equilíbrio)
        loading="lazy" // ✅ Lazy loading
        placeholder="blur" // ✅ Blur placeholder
        blurDataURL={product.blurDataURL}
      />
    </div>
  );
}

// ✅ Responsive Images com sizes
export function HeroBanner() {
  return (
    <div className="hero">
      <Image
        src="/hero-image.jpg"
        alt="Hero"
        fill
        sizes="(max-width: 768px) 100vw, (max-width: 1200px) 80vw, 60vw"
        priority // ✅ Carregar imediatamente (acima da dobra)
      />
    </div>
  );
}

// ✅ Next.js Config para otimização de imagens
// next.config.mjs
export default {
  images: {
    formats: ['image/avif', 'image/webp'], // ✅ Formatos modernos
    deviceSizes: [640, 750, 828, 1080, 1200, 1920, 2048, 3840],
    imageSizes: [16, 32, 48, 64, 96, 128, 256, 384],
    remotePatterns: [
      {
        protocol: 'https',
        hostname: 'cdn.example.com',
        pathname: '/images/**',
      },
    ],
  },
};
```

---

### **6. Bundle Optimization**

#### **Tree Shaking**

```tsx
// ═══════════════════════════════════════════════════════════
// Tree Shaking - Import apenas o necessário
// ═══════════════════════════════════════════════════════════

// ❌ RUIM: Import toda biblioteca
import _ from 'lodash';
const result = _.debounce(fn, 300);

// ✅ BOM: Import específico (tree-shakeable)
import { debounce } from 'lodash-es';
const result = debounce(fn, 300);

// ❌ RUIM: Import tudo do date-fns
import * as dateFns from 'date-fns';
const formatted = dateFns.format(new Date(), 'yyyy-MM-dd');

// ✅ BOM: Import apenas format
import { format } from 'date-fns';
const formatted = format(new Date(), 'yyyy-MM-dd');

// ❌ RUIM: Importar ícones inteiros
import * as Icons from 'react-icons/fa';
<Icons.FaUser />

// ✅ BOM: Import específico
import { FaUser } from 'react-icons/fa';
<FaUser />
```

#### **Bundle Analysis**

```bash
# ═══════════════════════════════════════════════════════════
# Analisar tamanho do bundle
# ═══════════════════════════════════════════════════════════

# Instalar @next/bundle-analyzer
npm install --save-dev @next/bundle-analyzer

# next.config.mjs
import withBundleAnalyzer from '@next/bundle-analyzer';

const bundleAnalyzer = withBundleAnalyzer({
  enabled: process.env.ANALYZE === 'true',
});

export default bundleAnalyzer({
  // ... resto da config
});

# Executar análise
ANALYZE=true npm run build
```

---

### **7. Core Web Vitals**

#### **LCP (Largest Contentful Paint)**

```tsx
// ═══════════════════════════════════════════════════════════
// Otimizar LCP
// ═══════════════════════════════════════════════════════════

// ✅ 1. Priorizar hero image
export function HeroSection() {
  return (
    <div className="hero">
      <Image
        src="/hero.jpg"
        alt="Hero"
        fill
        priority // ✅ Carregar imediatamente
        quality={90} // ✅ Alta qualidade para hero
      />
    </div>
  );
}

// ✅ 2. Preload critical resources
// app/layout.tsx
export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html>
      <head>
        {/* ✅ Preload hero image */}
        <link
          rel="preload"
          as="image"
          href="/hero.jpg"
          imageSrcSet="/hero-640.jpg 640w, /hero-1280.jpg 1280w"
        />
        
        {/* ✅ Preload critical fonts */}
        <link
          rel="preload"
          href="/fonts/inter-var.woff2"
          as="font"
          type="font/woff2"
          crossOrigin="anonymous"
        />
      </head>
      <body>{children}</body>
    </html>
  );
}

// ✅ 3. Server Components para reduzir JS
// app/page.tsx (Server Component)
export default async function HomePage() {
  const produtos = await getProdutos();

  return (
    <div>
      <HeroSection />
      <ProdutosGrid produtos={produtos} />
    </div>
  );
}
```

#### **FID (First Input Delay) / INP (Interaction to Next Paint)**

```tsx
// ═══════════════════════════════════════════════════════════
// Otimizar FID/INP
// ═══════════════════════════════════════════════════════════

// ✅ 1. Debounce expensive operations
'use client';

import { useDeferredValue } from 'react';

export function SearchComponent() {
  const [searchQuery, setSearchQuery] = useState('');
  
  // ✅ Defer non-urgent updates
  const deferredQuery = useDeferredValue(searchQuery);

  const searchResults = useMemo(() => {
    return expensiveSearch(deferredQuery);
  }, [deferredQuery]);

  return (
    <div>
      <input
        value={searchQuery}
        onChange={(e) => setSearchQuery(e.target.value)}
        placeholder="Buscar..."
      />
      <SearchResults results={searchResults} />
    </div>
  );
}

// ✅ 2. useTransition para atualizações não urgentes
'use client';

import { useTransition } from 'react';

export function TabsComponent() {
  const [activeTab, setActiveTab] = useState('tab1');
  const [isPending, startTransition] = useTransition();

  const handleTabChange = (tab: string) => {
    // ✅ Marcar atualização como não urgente
    startTransition(() => {
      setActiveTab(tab);
    });
  };

  return (
    <div>
      <Tabs value={activeTab} onValueChange={handleTabChange}>
        {/* Tabs content */}
      </Tabs>
      {isPending && <LoadingSpinner />}
    </div>
  );
}
```

#### **CLS (Cumulative Layout Shift)**

```tsx
// ═══════════════════════════════════════════════════════════
// Otimizar CLS
// ═══════════════════════════════════════════════════════════

// ✅ 1. Reservar espaço para imagens
export function ProductCard({ product }: { product: Product }) {
  return (
    <div className="product-card">
      {/* ✅ width/height para evitar layout shift */}
      <Image
        src={product.imagemUrl}
        alt={product.nome}
        width={300}
        height={300}
        className="w-full h-auto"
      />
      <h3>{product.nome}</h3>
    </div>
  );
}

// ✅ 2. Skeleton loaders
export function ProductCardSkeleton() {
  return (
    <div className="product-card">
      {/* ✅ Mesmas dimensões do conteúdo real */}
      <div className="w-full h-[300px] bg-gray-200 animate-pulse" />
      <div className="h-6 bg-gray-200 animate-pulse mt-2" />
      <div className="h-4 bg-gray-200 animate-pulse mt-1 w-1/2" />
    </div>
  );
}

// ✅ 3. Font loading sem FOUT/FOIT
// app/layout.tsx
import { Inter } from 'next/font/google';

const inter = Inter({
  subsets: ['latin'],
  display: 'swap', // ✅ Evitar invisible text
  preload: true,
});

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html className={inter.className}>
      <body>{children}</body>
    </html>
  );
}

// ✅ 4. Reservar espaço para ads/banners
export function AdBanner() {
  return (
    <div className="ad-container">
      {/* ✅ Container com altura fixa */}
      <div className="w-full h-[250px]">
        <AdComponent />
      </div>
    </div>
  );
}
```

---

### **Performance Monitoring**

```tsx
// ═══════════════════════════════════════════════════════════
// Monitorar Core Web Vitals
// ═══════════════════════════════════════════════════════════

// app/layout.tsx
import { SpeedInsights } from '@vercel/speed-insights/next';
import { Analytics } from '@vercel/analytics/react';

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html>
      <body>
        {children}
        <SpeedInsights /> {/* ✅ Core Web Vitals tracking */}
        <Analytics /> {/* ✅ Analytics */}
      </body>
    </html>
  );
}

// ✅ Custom Web Vitals reporting
// app/web-vitals.ts
export function reportWebVitals(metric: any) {
  console.log(metric);

  // Enviar para serviço de analytics
  if (metric.label === 'web-vital') {
    // Google Analytics
    window.gtag?.('event', metric.name, {
      value: Math.round(metric.name === 'CLS' ? metric.value * 1000 : metric.value),
      event_label: metric.id,
      non_interaction: true,
    });
  }
}
```

---

## 11. 🧪 **Testing Strategy**
```

**Padrões**:
- Um controller por página/feature complexa
- Nome: `*Controller.tsx`
- Sempre Client Component (`'use client'`)
- Mínima lógica de negócio (delegar para hooks)

---

### 3. **Business Logic Layer** (Hooks)

**Localização**: `app/**/use*.ts`, `hooks/**/*.ts`

**Responsabilidades**:
- ✅ Lógica de negócio do cliente
- ✅ Gerenciamento de estado
- ✅ Side effects (useEffect)
- ✅ Transformação de dados
- ✅ Reutilização entre controllers

**Exemplo**:
```tsx
// ═══════════════════════════════════════════════════════════
// app/produtos/useProdutos.ts (Business Logic)
// ═══════════════════════════════════════════════════════════

import { useState, useEffect, useCallback } from 'react';
import { getProdutos } from './actions';
import { Produto } from './types';

interface UseProdutosReturn {
  produtos: Produto[];
  loading: boolean;
  error: Error | null;
  refetch: () => Promise<void>;
}

export function useProdutos(): UseProdutosReturn {
  const [produtos, setProdutos] = useState<Produto[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<Error | null>(null);

  // ✅ Lógica de carregamento
  const loadProdutos = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);
      
      const data = await getProdutos();
      setProdutos(data);
    } catch (err) {
      setError(err instanceof Error ? err : new Error('Erro ao carregar produtos'));
    } finally {
      setLoading(false);
    }
  }, []);

  // ✅ Auto-load on mount
  useEffect(() => {
    loadProdutos();
  }, [loadProdutos]);

  return {
    produtos,
    loading,
    error,
    refetch: loadProdutos
  };
}

// ✅ Responsabilidade: Encapsular lógica de negócio reutilizável
```

**Regras para Hooks**:
1. Prefixo `use*`
2. Retornar objeto com valores e funções
3. TypeScript tipado (interface de retorno)
4. Documentar parâmetros e retorno
5. Testável isoladamente

---

### 4. **Application Layer** (Server Actions & Services)

**Localização**: `app/**/actions.ts`, `lib/services/**/*.ts`

**Responsabilidades**:
- ✅ Operações do servidor (Server Actions)
- ✅ Chamadas a APIs externas
- ✅ Acesso ao banco de dados
- ✅ Validações server-side
- ✅ Revalidação de cache (Next.js)

**Exemplo (Server Actions)**:
```tsx
// ═══════════════════════════════════════════════════════════
// app/produtos/actions.ts (Application Layer)
// ═══════════════════════════════════════════════════════════

'use server';

import { revalidatePath } from 'next/cache';
import { db } from '@/lib/db';
import { produtoSchema } from './schema';
import { Produto } from './types';

// ✅ Read Operation
export async function getProdutos(): Promise<Produto[]> {
  try {
    const produtos = await db.produto.findMany({
      where: { ativo: true },
      orderBy: { createdAt: 'desc' }
    });

    return produtos;
  } catch (error) {
    console.error('Erro ao buscar produtos:', error);
    throw new Error('Falha ao carregar produtos');
  }
}

// ✅ Write Operation with Validation
export async function createProduto(formData: FormData) {
  // Server-side validation
  const rawData = {
    nome: formData.get('nome'),
    preco: Number(formData.get('preco')),
    estoque: Number(formData.get('estoque'))
  };

  const result = produtoSchema.safeParse(rawData);

  if (!result.success) {
    return {
      success: false,
      errors: result.error.flatten().fieldErrors
    };
  }

  try {
    const produto = await db.produto.create({
      data: result.data
    });

    // ✅ Revalidate cache
    revalidatePath('/produtos');

    return {
      success: true,
      data: produto
    };
  } catch (error) {
    console.error('Erro ao criar produto:', error);
    return {
      success: false,
      error: 'Falha ao criar produto'
    };
  }
}

// ✅ Responsabilidade: Operações server-side com validação
```

**Exemplo (Services)**:
```tsx
// ═══════════════════════════════════════════════════════════
// lib/services/email.service.ts
// ═══════════════════════════════════════════════════════════

import { Resend } from 'resend';

const resend = new Resend(process.env.RESEND_API_KEY);

export class EmailService {
  static async sendWelcomeEmail(email: string, name: string) {
    try {
      await resend.emails.send({
        from: 'noreply@example.com',
        to: email,
        subject: 'Bem-vindo!',
        html: `<h1>Olá, ${name}!</h1>`
      });

      return { success: true };
    } catch (error) {
      console.error('Erro ao enviar email:', error);
      return { success: false, error };
    }
  }
}

// ✅ Responsabilidade: Integração com serviços externos
```

---

### 5. **Domain Layer** (Types & Business Rules)

**Localização**: `app/**/types.ts`, `lib/domain/**/*.ts`, `lib/types/**/*.ts`

**Responsabilidades**:
- ✅ Definições de tipos/interfaces
- ✅ Entidades de domínio
- ✅ Value Objects
- ✅ Regras de negócio puras
- ✅ Validações com Zod

**Exemplo**:
```tsx
// ═══════════════════════════════════════════════════════════
// app/produtos/types.ts (Domain)
// ═══════════════════════════════════════════════════════════

// ✅ Entity
export interface Produto {
  id: string;
  nome: string;
  descricao: string;
  preco: number;
  estoque: number;
  categoria: Categoria;
  ativo: boolean;
  createdAt: Date;
  updatedAt: Date;
}

// ✅ Value Object
export interface Preco {
  valor: number;
  moeda: 'BRL' | 'USD';
  formatado: string;
}

// ✅ Enum
export enum Categoria {
  ELETRONICOS = 'ELETRONICOS',
  LIVROS = 'LIVROS',
  ROUPAS = 'ROUPAS'
}

// ✅ DTO (Data Transfer Object)
export interface CreateProdutoDTO {
  nome: string;
  descricao: string;
  preco: number;
  estoque: number;
  categoriaId: string;
}

// ✅ Domain Service (Pure Function)
export class ProdutoService {
  static calcularPrecoComDesconto(produto: Produto, percentual: number): number {
    if (percentual < 0 || percentual > 100) {
      throw new Error('Percentual inválido');
    }
    
    return produto.preco * (1 - percentual / 100);
  }

  static estaDisponivel(produto: Produto): boolean {
    return produto.ativo && produto.estoque > 0;
  }

  static formatarPreco(valor: number): string {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL'
    }).format(valor);
  }
}

// ✅ Responsabilidade: Definir contratos e regras de negócio puras
```

**Exemplo (Validation Schema)**:
```tsx
// ═══════════════════════════════════════════════════════════
// app/produtos/schema.ts
// ═══════════════════════════════════════════════════════════

import { z } from 'zod';

export const produtoSchema = z.object({
  nome: z.string()
    .min(3, 'Nome deve ter no mínimo 3 caracteres')
    .max(100, 'Nome deve ter no máximo 100 caracteres'),
  
  descricao: z.string()
    .min(10, 'Descrição deve ter no mínimo 10 caracteres')
    .max(500, 'Descrição deve ter no máximo 500 caracteres'),
  
  preco: z.number()
    .positive('Preço deve ser positivo')
    .max(999999, 'Preço muito alto'),
  
  estoque: z.number()
    .int('Estoque deve ser um número inteiro')
    .nonnegative('Estoque não pode ser negativo'),
  
  categoriaId: z.string().uuid('ID de categoria inválido')
});

export type ProdutoFormData = z.infer<typeof produtoSchema>;

// ✅ Responsabilidade: Validação e contratos de dados
```

---

### 6. **Infrastructure Layer**

**Localização**: `lib/db.ts`, `lib/api/**/*.ts`, `lib/integrations/**/*.ts`

**Responsabilidades**:
- ✅ Conexões com banco de dados
- ✅ APIs externas
- ✅ Configurações de terceiros
- ✅ Cache providers
- ✅ File storage

**Exemplo**:
```tsx
// ═══════════════════════════════════════════════════════════
// lib/db.ts (Infrastructure)
// ═══════════════════════════════════════════════════════════

import { PrismaClient } from '@prisma/client';

const globalForPrisma = global as unknown as { prisma: PrismaClient };

export const db = globalForPrisma.prisma || new PrismaClient({
  log: process.env.NODE_ENV === 'development' ? ['query', 'error', 'warn'] : ['error'],
});

if (process.env.NODE_ENV !== 'production') {
  globalForPrisma.prisma = db;
}

// ✅ Responsabilidade: Configuração de infraestrutura
```

## Fluxo de Dados

```
┌─────────────────────────────────────────────────────────┐
│                    User Interaction                      │
└────────────────────────┬────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────┐
│                  View (page.tsx)                         │
│  - Renderiza UI                                          │
│  - Captura eventos                                       │
└────────────────────────┬────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────┐
│            Controller (*Controller.tsx)                  │
│  - Orquestra lógica                                      │
│  - Conecta camadas                                       │
└────────────────────────┬────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────┐
│              Hook (use*.ts)                              │
│  - Gerencia estado                                       │
│  - Lógica de negócio                                     │
└────────────────────────┬────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────┐
│          Server Action (actions.ts)                      │
│  - Operações servidor                                    │
│  - Acesso a dados                                        │
└────────────────────────┬────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────┐
│            Service (lib/services/)                       │
│  - Lógica de negócio complexa                            │
│  - Integrações externas                                  │
└────────────────────────┬────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────┐
│                  Database / API                          │
└─────────────────────────────────────────────────────────┘
```

## Princípios SOLID

### Single Responsibility Principle (SRP)
Cada arquivo tem uma única responsabilidade:
- `page.tsx`: Apenas renderização
- `Controller.tsx`: Apenas orquestração
- `useHook.ts`: Apenas lógica de estado
- `actions.ts`: Apenas operações servidor

### Open/Closed Principle (OCP)
Componentes abertos para extensão, fechados para modificação:
```tsx
// Componente base
export function Card({ children }) {
  return <Box>{children}</Box>
}

// Extensão sem modificar o original
export function ProductCard({ product }) {
  return (
    <Card>
      <ProductInfo product={product} />
    </Card>
  )
}
```

### Liskov Substitution Principle (LSP)
Componentes podem ser substituídos por suas variantes:
```tsx
interface ButtonProps {
  onClick: () => void
  children: ReactNode
}

// Qualquer botão pode substituir outro
<PrimaryButton onClick={handleClick}>Salvar</PrimaryButton>
<SecondaryButton onClick={handleClick}>Salvar</SecondaryButton>
```

### Interface Segregation Principle (ISP)
Interfaces específicas e focadas:
```tsx
// ❌ Interface muito grande
interface UserActions {
  login: () => void
  logout: () => void
  updateProfile: () => void
  deleteAccount: () => void
}

// ✅ Interfaces segregadas
interface AuthActions {
  login: () => void
  logout: () => void
}

interface ProfileActions {
  updateProfile: () => void
  deleteAccount: () => void
}
```

### Dependency Inversion Principle (DIP)
Depender de abstrações, não de implementações:
```tsx
// ❌ Dependência direta
function UserList() {
  const users = await db.user.findMany() // Dependência direta do DB
}

// ✅ Dependência de abstração
function UserList() {
  const users = await getUsers() // Abstração via action
}
```

## Padrões de Design

### Repository Pattern
```tsx
// lib/repositories/user.repository.ts
export class UserRepository {
  async findAll() {
    return await db.user.findMany()
  }
  
  async findById(id: string) {
    return await db.user.findUnique({ where: { id } })
  }
}
```

### Service Pattern
```tsx
// lib/services/auth.service.ts
export class AuthService {
  constructor(private userRepo: UserRepository) {}
  
  async login(email: string, password: string) {
    const user = await this.userRepo.findByEmail(email)
    // Lógica de autenticação
  }
}
```

### Factory Pattern
```tsx
// lib/factories/notification.factory.ts
export function createNotification(type: 'success' | 'error') {
  switch (type) {
    case 'success':
      return new SuccessNotification()
    case 'error':
      return new ErrorNotification()
  }
}
```

## Boas Práticas

### 1. Separação de Concerns
- Cada arquivo tem uma responsabilidade clara
- Não misture lógica de UI com lógica de negócio
- Mantenha componentes pequenos e focados

### 2. Composição sobre Herança
```tsx
// ✅ Composição
function UserProfile() {
  return (
    <Card>
      <Avatar />
      <UserInfo />
      <UserActions />
    </Card>
  )
}

// ❌ Herança
class UserProfile extends BaseProfile {
  // Difícil de manter e estender
}
```

### 3. Imutabilidade
```tsx
// ✅ Imutável
const newState = { ...state, loading: true }

// ❌ Mutável
state.loading = true
```

### 4. Tipagem Forte
```tsx
// ✅ Tipado
interface User {
  id: string
  name: string
}

function getUser(): User {
  // ...
}

// ❌ Sem tipo
function getUser() {
  return { id: '1', name: 'João' }
}
```

### 5. Error Handling
```tsx
// ✅ Tratamento adequado
try {
  const data = await fetchData()
  return { data, error: null }
} catch (error) {
  console.error('Error:', error)
  return { data: null, error: 'Falha ao carregar dados' }
}
```

## Testing Strategy

### Unit Tests
Testar funções e hooks isoladamente:
```tsx
// useProdutos.test.ts
describe('useProdutos', () => {
  it('should load products', async () => {
    const { result } = renderHook(() => useProdutos())
    await waitFor(() => {
      expect(result.current.produtos).toHaveLength(3)
    })
  })
})
```

### Integration Tests
Testar fluxos completos:
```tsx
// produtos.test.tsx
describe('Produtos Page', () => {
  it('should display products and add to cart', async () => {
    render(<ProdutosPage />)
    const addButton = screen.getByText('Adicionar ao Carrinho')
    fireEvent.click(addButton)
    expect(screen.getByText('Produto adicionado')).toBeInTheDocument()
  })
})
```

### E2E Tests
Testar cenários de usuário:
```tsx
// produtos.e2e.ts
test('user can purchase a product', async ({ page }) => {
  await page.goto('/produtos')
  await page.click('text=Adicionar ao Carrinho')
  await page.click('text=Finalizar Compra')
  await expect(page).toHaveURL('/checkout')
})
```

## Performance

### Code Splitting
```tsx
// Lazy loading de componentes
const HeavyComponent = dynamic(() => import('./HeavyComponent'), {
  loading: () => <Spinner />,
})
```

### Memoization
```tsx
// Memoizar componentes pesados
const ProductCard = memo(({ product }) => {
  return <Card>{product.name}</Card>
})

// Memoizar valores calculados
const total = useMemo(() => {
  return items.reduce((sum, item) => sum + item.price, 0)
}, [items])
```

### Debouncing
```tsx
// Debounce em buscas
const debouncedSearch = useDebounce(searchTerm, 500)

useEffect(() => {
  if (debouncedSearch) {
    searchProducts(debouncedSearch)
  }
}, [debouncedSearch])
```

---

## 11. Testing Strategy (Estratégia de Testes)

Uma estratégia de testes abrangente garante qualidade, previne regressões e facilita refatorações.

### **Pirâmide de Testes**

```
         /\
        /  \       E2E Tests
       /    \      (Poucos, Lentos, Caros)
      /------\
     /        \    Integration Tests
    /          \   (Médios, Médios, Médios)
   /------------\
  /              \ Unit Tests
 /________________\ (Muitos, Rápidos, Baratos)

Proporção Recomendada:
- Unit Tests: 70%
- Integration Tests: 20%
- E2E Tests: 10%
```

---

### **11.1. Unit Testing (Testes Unitários)**

Testam funções, hooks e componentes isoladamente.

#### **Setup com Vitest**

```typescript
// ═══════════════════════════════════════════════════════════
// vitest.config.ts
// ═══════════════════════════════════════════════════════════
import { defineConfig } from 'vitest/config';
import react from '@vitejs/plugin-react';
import path from 'path';

export default defineConfig({
  plugins: [react()],
  test: {
    globals: true,
    environment: 'jsdom',
    setupFiles: ['./tests/setup.ts'],
    coverage: {
      provider: 'v8',
      reporter: ['text', 'json', 'html'],
      exclude: [
        'node_modules/',
        'tests/',
        '**/*.d.ts',
        '**/*.config.*',
        '**/mockData',
      ],
    },
  },
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './'),
    },
  },
});

// ═══════════════════════════════════════════════════════════
// tests/setup.ts
// ═══════════════════════════════════════════════════════════
import '@testing-library/jest-dom';
import { cleanup } from '@testing-library/react';
import { afterEach, vi } from 'vitest';

// Cleanup após cada teste
afterEach(() => {
  cleanup();
});

// Mock window.matchMedia
Object.defineProperty(window, 'matchMedia', {
  writable: true,
  value: vi.fn().mockImplementation(query => ({
    matches: false,
    media: query,
    onchange: null,
    addListener: vi.fn(),
    removeListener: vi.fn(),
    addEventListener: vi.fn(),
    removeEventListener: vi.fn(),
    dispatchEvent: vi.fn(),
  })),
});

// Mock IntersectionObserver
global.IntersectionObserver = class IntersectionObserver {
  constructor() {}
  disconnect() {}
  observe() {}
  takeRecords() { return []; }
  unobserve() {}
} as any;
```

#### **Testar Utility Functions**

```typescript
// ═══════════════════════════════════════════════════════════
// lib/utils/format.ts
// ═══════════════════════════════════════════════════════════
export function formatCurrency(value: number): string {
  return new Intl.NumberFormat('pt-BR', {
    style: 'currency',
    currency: 'BRL',
  }).format(value);
}

export function formatDate(date: Date): string {
  return new Intl.DateTimeFormat('pt-BR').format(date);
}

export function truncateText(text: string, maxLength: number): string {
  if (text.length <= maxLength) return text;
  return text.slice(0, maxLength) + '...';
}

// ═══════════════════════════════════════════════════════════
// lib/utils/__tests__/format.test.ts
// ═══════════════════════════════════════════════════════════
import { describe, it, expect } from 'vitest';
import { formatCurrency, formatDate, truncateText } from '../format';

describe('formatCurrency', () => {
  it('formata números positivos corretamente', () => {
    expect(formatCurrency(1234.56)).toBe('R$ 1.234,56');
  });

  it('formata zero corretamente', () => {
    expect(formatCurrency(0)).toBe('R$ 0,00');
  });

  it('formata números negativos corretamente', () => {
    expect(formatCurrency(-99.99)).toBe('-R$ 99,99');
  });

  it('arredonda casas decimais', () => {
    expect(formatCurrency(10.999)).toBe('R$ 11,00');
  });
});

describe('formatDate', () => {
  it('formata data corretamente', () => {
    const date = new Date('2024-01-15');
    expect(formatDate(date)).toBe('15/01/2024');
  });

  it('lida com ano bissexto', () => {
    const date = new Date('2024-02-29');
    expect(formatDate(date)).toBe('29/02/2024');
  });
});

describe('truncateText', () => {
  it('não trunca texto menor que maxLength', () => {
    expect(truncateText('Hello', 10)).toBe('Hello');
  });

  it('trunca texto maior que maxLength', () => {
    expect(truncateText('Hello World', 5)).toBe('Hello...');
  });

  it('lida com maxLength zero', () => {
    expect(truncateText('Hello', 0)).toBe('...');
  });

  it('lida com string vazia', () => {
    expect(truncateText('', 10)).toBe('');
  });
});
```

#### **Testar Custom Hooks**

```typescript
// ═══════════════════════════════════════════════════════════
// hooks/useDebounce.ts
// ═══════════════════════════════════════════════════════════
import { useState, useEffect } from 'react';

export function useDebounce<T>(value: T, delay: number): T {
  const [debouncedValue, setDebouncedValue] = useState<T>(value);

  useEffect(() => {
    const handler = setTimeout(() => {
      setDebouncedValue(value);
    }, delay);

    return () => {
      clearTimeout(handler);
    };
  }, [value, delay]);

  return debouncedValue;
}

// ═══════════════════════════════════════════════════════════
// hooks/__tests__/useDebounce.test.ts
// ═══════════════════════════════════════════════════════════
import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest';
import { renderHook, waitFor } from '@testing-library/react';
import { useDebounce } from '../useDebounce';

describe('useDebounce', () => {
  beforeEach(() => {
    vi.useFakeTimers();
  });

  afterEach(() => {
    vi.restoreAllMocks();
  });

  it('retorna valor inicial imediatamente', () => {
    const { result } = renderHook(() => useDebounce('initial', 500));
    expect(result.current).toBe('initial');
  });

  it('atualiza valor após delay', async () => {
    const { result, rerender } = renderHook(
      ({ value, delay }) => useDebounce(value, delay),
      {
        initialProps: { value: 'initial', delay: 500 },
      }
    );

    expect(result.current).toBe('initial');

    // Atualiza valor
    rerender({ value: 'updated', delay: 500 });

    // Não deve atualizar imediatamente
    expect(result.current).toBe('initial');

    // Avança timers
    vi.advanceTimersByTime(500);

    // Deve atualizar após delay
    await waitFor(() => {
      expect(result.current).toBe('updated');
    });
  });

  it('cancela debounce anterior quando valor muda rapidamente', async () => {
    const { result, rerender } = renderHook(
      ({ value }) => useDebounce(value, 500),
      {
        initialProps: { value: 'first' },
      }
    );

    rerender({ value: 'second' });
    vi.advanceTimersByTime(200);

    rerender({ value: 'third' });
    vi.advanceTimersByTime(200);

    // Não deve atualizar ainda
    expect(result.current).toBe('first');

    // Completa o delay do último valor
    vi.advanceTimersByTime(300);

    await waitFor(() => {
      expect(result.current).toBe('third');
    });
  });
});
```

#### **Testar Context Providers**

```typescript
// ═══════════════════════════════════════════════════════════
// contexts/CartContext.tsx
// ═══════════════════════════════════════════════════════════
import { createContext, useContext, useState, ReactNode } from 'react';

interface CartItem {
  id: string;
  name: string;
  price: number;
  quantity: number;
}

interface CartContextData {
  items: CartItem[];
  addItem: (item: Omit<CartItem, 'quantity'>) => void;
  removeItem: (id: string) => void;
  clearCart: () => void;
  total: number;
}

const CartContext = createContext<CartContextData>({} as CartContextData);

export function CartProvider({ children }: { children: ReactNode }) {
  const [items, setItems] = useState<CartItem[]>([]);

  const addItem = (item: Omit<CartItem, 'quantity'>) => {
    setItems(prev => {
      const existing = prev.find(i => i.id === item.id);
      if (existing) {
        return prev.map(i =>
          i.id === item.id ? { ...i, quantity: i.quantity + 1 } : i
        );
      }
      return [...prev, { ...item, quantity: 1 }];
    });
  };

  const removeItem = (id: string) => {
    setItems(prev => prev.filter(item => item.id !== id));
  };

  const clearCart = () => {
    setItems([]);
  };

  const total = items.reduce((sum, item) => sum + item.price * item.quantity, 0);

  return (
    <CartContext.Provider value={{ items, addItem, removeItem, clearCart, total }}>
      {children}
    </CartContext.Provider>
  );
}

export const useCart = () => useContext(CartContext);

// ═══════════════════════════════════════════════════════════
// contexts/__tests__/CartContext.test.tsx
// ═══════════════════════════════════════════════════════════
import { describe, it, expect } from 'vitest';
import { renderHook, act } from '@testing-library/react';
import { CartProvider, useCart } from '../CartContext';
import { ReactNode } from 'react';

const wrapper = ({ children }: { children: ReactNode }) => (
  <CartProvider>{children}</CartProvider>
);

describe('CartContext', () => {
  it('inicia com carrinho vazio', () => {
    const { result } = renderHook(() => useCart(), { wrapper });
    expect(result.current.items).toEqual([]);
    expect(result.current.total).toBe(0);
  });

  it('adiciona item ao carrinho', () => {
    const { result } = renderHook(() => useCart(), { wrapper });

    act(() => {
      result.current.addItem({
        id: '1',
        name: 'Produto 1',
        price: 100,
      });
    });

    expect(result.current.items).toHaveLength(1);
    expect(result.current.items[0]).toEqual({
      id: '1',
      name: 'Produto 1',
      price: 100,
      quantity: 1,
    });
    expect(result.current.total).toBe(100);
  });

  it('incrementa quantidade ao adicionar item existente', () => {
    const { result } = renderHook(() => useCart(), { wrapper });

    act(() => {
      result.current.addItem({ id: '1', name: 'Produto 1', price: 100 });
      result.current.addItem({ id: '1', name: 'Produto 1', price: 100 });
    });

    expect(result.current.items).toHaveLength(1);
    expect(result.current.items[0].quantity).toBe(2);
    expect(result.current.total).toBe(200);
  });

  it('remove item do carrinho', () => {
    const { result } = renderHook(() => useCart(), { wrapper });

    act(() => {
      result.current.addItem({ id: '1', name: 'Produto 1', price: 100 });
      result.current.addItem({ id: '2', name: 'Produto 2', price: 50 });
    });

    expect(result.current.items).toHaveLength(2);

    act(() => {
      result.current.removeItem('1');
    });

    expect(result.current.items).toHaveLength(1);
    expect(result.current.items[0].id).toBe('2');
    expect(result.current.total).toBe(50);
  });

  it('limpa carrinho', () => {
    const { result } = renderHook(() => useCart(), { wrapper });

    act(() => {
      result.current.addItem({ id: '1', name: 'Produto 1', price: 100 });
      result.current.addItem({ id: '2', name: 'Produto 2', price: 50 });
    });

    expect(result.current.items).toHaveLength(2);

    act(() => {
      result.current.clearCart();
    });

    expect(result.current.items).toEqual([]);
    expect(result.current.total).toBe(0);
  });

  it('calcula total corretamente', () => {
    const { result } = renderHook(() => useCart(), { wrapper });

    act(() => {
      result.current.addItem({ id: '1', name: 'Produto 1', price: 100 });
      result.current.addItem({ id: '1', name: 'Produto 1', price: 100 }); // quantity = 2
      result.current.addItem({ id: '2', name: 'Produto 2', price: 50 });
    });

    // (100 * 2) + (50 * 1) = 250
    expect(result.current.total).toBe(250);
  });
});
```

---

### **11.2. Component Testing (Testes de Componentes)**

Testam componentes React com React Testing Library.

#### **Testar Componentes de UI**

```typescript
// ═══════════════════════════════════════════════════════════
// components/ui/Button.tsx
// ═══════════════════════════════════════════════════════════
import { ButtonHTMLAttributes, forwardRef } from 'react';

export interface ButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: 'primary' | 'secondary' | 'danger';
  loading?: boolean;
}

export const Button = forwardRef<HTMLButtonElement, ButtonProps>(
  ({ variant = 'primary', loading, children, disabled, ...props }, ref) => {
    const baseClass = 'px-4 py-2 rounded font-medium transition-colors';
    
    const variantClasses = {
      primary: 'bg-blue-600 text-white hover:bg-blue-700',
      secondary: 'bg-gray-200 text-gray-900 hover:bg-gray-300',
      danger: 'bg-red-600 text-white hover:bg-red-700',
    };

    return (
      <button
        ref={ref}
        className={`${baseClass} ${variantClasses[variant]}`}
        disabled={disabled || loading}
        {...props}
      >
        {loading ? 'Carregando...' : children}
      </button>
    );
  }
);

Button.displayName = 'Button';

// ═══════════════════════════════════════════════════════════
// components/ui/__tests__/Button.test.tsx
// ═══════════════════════════════════════════════════════════
import { describe, it, expect, vi } from 'vitest';
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { Button } from '../Button';

describe('Button', () => {
  it('renderiza children corretamente', () => {
    render(<Button>Click me</Button>);
    expect(screen.getByText('Click me')).toBeInTheDocument();
  });

  it('aplica variant primary por padrão', () => {
    render(<Button>Button</Button>);
    const button = screen.getByRole('button');
    expect(button).toHaveClass('bg-blue-600');
  });

  it('aplica variant secundária', () => {
    render(<Button variant="secondary">Button</Button>);
    const button = screen.getByRole('button');
    expect(button).toHaveClass('bg-gray-200');
  });

  it('aplica variant danger', () => {
    render(<Button variant="danger">Button</Button>);
    const button = screen.getByRole('button');
    expect(button).toHaveClass('bg-red-600');
  });

  it('chama onClick quando clicado', async () => {
    const handleClick = vi.fn();
    const user = userEvent.setup();

    render(<Button onClick={handleClick}>Click me</Button>);
    
    await user.click(screen.getByText('Click me'));
    
    expect(handleClick).toHaveBeenCalledTimes(1);
  });

  it('não chama onClick quando disabled', async () => {
    const handleClick = vi.fn();
    const user = userEvent.setup();

    render(<Button onClick={handleClick} disabled>Click me</Button>);
    
    await user.click(screen.getByText('Click me'));
    
    expect(handleClick).not.toHaveBeenCalled();
  });

  it('mostra estado de loading', () => {
    render(<Button loading>Submit</Button>);
    expect(screen.getByText('Carregando...')).toBeInTheDocument();
    expect(screen.queryByText('Submit')).not.toBeInTheDocument();
  });

  it('desabilita botão quando loading', () => {
    render(<Button loading>Submit</Button>);
    expect(screen.getByRole('button')).toBeDisabled();
  });

  it('aceita ref', () => {
    const ref = { current: null };
    render(<Button ref={ref}>Button</Button>);
    expect(ref.current).toBeInstanceOf(HTMLButtonElement);
  });
});
```

#### **Testar Componentes com Formulários**

```typescript
// ═══════════════════════════════════════════════════════════
// components/forms/LoginForm.tsx
// ═══════════════════════════════════════════════════════════
'use client';

import { useState } from 'react';
import { Button } from '@/components/ui/Button';

interface LoginFormProps {
  onSubmit: (data: { email: string; password: string }) => Promise<void>;
}

export function LoginForm({ onSubmit }: LoginFormProps) {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');

    if (!email || !password) {
      setError('Preencha todos os campos');
      return;
    }

    setLoading(true);
    try {
      await onSubmit({ email, password });
    } catch (err) {
      setError('Erro ao fazer login');
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <div>
        <label htmlFor="email">Email</label>
        <input
          id="email"
          type="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          placeholder="seu@email.com"
        />
      </div>

      <div>
        <label htmlFor="password">Senha</label>
        <input
          id="password"
          type="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          placeholder="********"
        />
      </div>

      {error && <div role="alert">{error}</div>}

      <Button type="submit" loading={loading}>
        Entrar
      </Button>
    </form>
  );
}

// ═══════════════════════════════════════════════════════════
// components/forms/__tests__/LoginForm.test.tsx
// ═══════════════════════════════════════════════════════════
import { describe, it, expect, vi } from 'vitest';
import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { LoginForm } from '../LoginForm';

describe('LoginForm', () => {
  it('renderiza campos de formulário', () => {
    render(<LoginForm onSubmit={vi.fn()} />);
    
    expect(screen.getByLabelText(/email/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/senha/i)).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /entrar/i })).toBeInTheDocument();
  });

  it('atualiza valores dos inputs', async () => {
    const user = userEvent.setup();
    render(<LoginForm onSubmit={vi.fn()} />);

    const emailInput = screen.getByLabelText(/email/i);
    const passwordInput = screen.getByLabelText(/senha/i);

    await user.type(emailInput, 'test@example.com');
    await user.type(passwordInput, 'password123');

    expect(emailInput).toHaveValue('test@example.com');
    expect(passwordInput).toHaveValue('password123');
  });

  it('mostra erro quando campos estão vazios', async () => {
    const user = userEvent.setup();
    render(<LoginForm onSubmit={vi.fn()} />);

    await user.click(screen.getByRole('button', { name: /entrar/i }));

    expect(await screen.findByRole('alert')).toHaveTextContent(
      'Preencha todos os campos'
    );
  });

  it('chama onSubmit com dados corretos', async () => {
    const handleSubmit = vi.fn().mockResolvedValue(undefined);
    const user = userEvent.setup();
    
    render(<LoginForm onSubmit={handleSubmit} />);

    await user.type(screen.getByLabelText(/email/i), 'test@example.com');
    await user.type(screen.getByLabelText(/senha/i), 'password123');
    await user.click(screen.getByRole('button', { name: /entrar/i }));

    await waitFor(() => {
      expect(handleSubmit).toHaveBeenCalledWith({
        email: 'test@example.com',
        password: 'password123',
      });
    });
  });

  it('mostra estado de loading durante submit', async () => {
    const handleSubmit = vi.fn(
      () => new Promise(resolve => setTimeout(resolve, 100))
    );
    const user = userEvent.setup();

    render(<LoginForm onSubmit={handleSubmit} />);

    await user.type(screen.getByLabelText(/email/i), 'test@example.com');
    await user.type(screen.getByLabelText(/senha/i), 'password123');
    await user.click(screen.getByRole('button', { name: /entrar/i }));

    expect(screen.getByText('Carregando...')).toBeInTheDocument();

    await waitFor(() => {
      expect(screen.queryByText('Carregando...')).not.toBeInTheDocument();
    });
  });

  it('mostra erro quando onSubmit falha', async () => {
    const handleSubmit = vi.fn().mockRejectedValue(new Error('Network error'));
    const user = userEvent.setup();

    render(<LoginForm onSubmit={handleSubmit} />);

    await user.type(screen.getByLabelText(/email/i), 'test@example.com');
    await user.type(screen.getByLabelText(/senha/i), 'password123');
    await user.click(screen.getByRole('button', { name: /entrar/i }));

    expect(await screen.findByRole('alert')).toHaveTextContent(
      'Erro ao fazer login'
    );
  });
});
```

---

### **11.3. Integration Testing (Testes de Integração)**

Testam múltiplos componentes trabalhando juntos.

```typescript
// ═══════════════════════════════════════════════════════════
// app/produtos/__tests__/produtos.integration.test.tsx
// ═══════════════════════════════════════════════════════════
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { ProdutosController } from '../ProdutosController';
import * as actions from '../actions';

// Mock das actions
vi.mock('../actions', () => ({
  getProdutos: vi.fn(),
  deleteProduto: vi.fn(),
}));

const mockProdutos = [
  { id: '1', nome: 'Produto 1', preco: 100, estoque: 10 },
  { id: '2', nome: 'Produto 2', preco: 200, estoque: 5 },
];

describe('Produtos Integration', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('carrega e exibe lista de produtos', async () => {
    vi.mocked(actions.getProdutos).mockResolvedValue(mockProdutos);

    render(<ProdutosController />);

    // Mostra loading inicialmente
    expect(screen.getByText(/carregando/i)).toBeInTheDocument();

    // Aguarda produtos carregarem
    await waitFor(() => {
      expect(screen.getByText('Produto 1')).toBeInTheDocument();
      expect(screen.getByText('Produto 2')).toBeInTheDocument();
    });

    expect(actions.getProdutos).toHaveBeenCalledTimes(1);
  });

  it('deleta produto quando botão é clicado', async () => {
    vi.mocked(actions.getProdutos).mockResolvedValue(mockProdutos);
    vi.mocked(actions.deleteProduto).mockResolvedValue(undefined);

    const user = userEvent.setup();
    render(<ProdutosController />);

    // Aguarda produtos carregarem
    await waitFor(() => {
      expect(screen.getByText('Produto 1')).toBeInTheDocument();
    });

    // Clica no botão deletar do primeiro produto
    const deleteButtons = screen.getAllByRole('button', { name: /deletar/i });
    await user.click(deleteButtons[0]);

    // Confirma deleção (se houver modal de confirmação)
    const confirmButton = screen.getByRole('button', { name: /confirmar/i });
    await user.click(confirmButton);

    // Verifica que action foi chamada
    await waitFor(() => {
      expect(actions.deleteProduto).toHaveBeenCalledWith('1');
    });

    // Verifica que produto foi removido da lista
    await waitFor(() => {
      expect(screen.queryByText('Produto 1')).not.toBeInTheDocument();
    });
  });

  it('mostra mensagem de erro quando falha ao carregar', async () => {
    vi.mocked(actions.getProdutos).mockRejectedValue(
      new Error('Network error')
    );

    render(<ProdutosController />);

    await waitFor(() => {
      expect(screen.getByText(/erro/i)).toBeInTheDocument();
    });
  });

  it('filtra produtos por busca', async () => {
    vi.mocked(actions.getProdutos).mockResolvedValue(mockProdutos);

    const user = userEvent.setup();
    render(<ProdutosController />);

    await waitFor(() => {
      expect(screen.getByText('Produto 1')).toBeInTheDocument();
    });

    // Digita no campo de busca
    const searchInput = screen.getByPlaceholderText(/buscar/i);
    await user.type(searchInput, 'Produto 1');

    // Verifica que apenas Produto 1 é exibido
    await waitFor(() => {
      expect(screen.getByText('Produto 1')).toBeInTheDocument();
      expect(screen.queryByText('Produto 2')).not.toBeInTheDocument();
    });
  });
});
```

---

### **11.4. E2E Testing (Testes End-to-End)**

Testam fluxos completos da aplicação com Playwright.

#### **Setup do Playwright**

```typescript
// ═══════════════════════════════════════════════════════════
// playwright.config.ts
// ═══════════════════════════════════════════════════════════
import { defineConfig, devices } from '@playwright/test';

export default defineConfig({
  testDir: './tests/e2e',
  fullyParallel: true,
  forbidOnly: !!process.env.CI,
  retries: process.env.CI ? 2 : 0,
  workers: process.env.CI ? 1 : undefined,
  reporter: 'html',
  
  use: {
    baseURL: 'http://localhost:3000',
    trace: 'on-first-retry',
    screenshot: 'only-on-failure',
  },

  projects: [
    {
      name: 'chromium',
      use: { ...devices['Desktop Chrome'] },
    },
    {
      name: 'firefox',
      use: { ...devices['Desktop Firefox'] },
    },
    {
      name: 'webkit',
      use: { ...devices['Desktop Safari'] },
    },
    {
      name: 'Mobile Chrome',
      use: { ...devices['Pixel 5'] },
    },
  ],

  webServer: {
    command: 'npm run dev',
    url: 'http://localhost:3000',
    reuseExistingServer: !process.env.CI,
  },
});

// ═══════════════════════════════════════════════════════════
// tests/e2e/auth.spec.ts
// ═══════════════════════════════════════════════════════════
import { test, expect } from '@playwright/test';

test.describe('Autenticação', () => {
  test('deve fazer login com credenciais válidas', async ({ page }) => {
    await page.goto('/login');

    // Preenche formulário
    await page.fill('input[name="email"]', 'test@example.com');
    await page.fill('input[name="password"]', 'password123');

    // Clica no botão
    await page.click('button[type="submit"]');

    // Verifica redirecionamento
    await expect(page).toHaveURL('/dashboard');

    // Verifica elemento da página autenticada
    await expect(page.getByText(/bem-vindo/i)).toBeVisible();
  });

  test('deve mostrar erro com credenciais inválidas', async ({ page }) => {
    await page.goto('/login');

    await page.fill('input[name="email"]', 'wrong@example.com');
    await page.fill('input[name="password"]', 'wrongpassword');
    await page.click('button[type="submit"]');

    // Verifica mensagem de erro
    await expect(page.getByText(/credenciais inválidas/i)).toBeVisible();

    // Ainda está na página de login
    await expect(page).toHaveURL('/login');
  });

  test('deve fazer logout', async ({ page }) => {
    // Faz login primeiro
    await page.goto('/login');
    await page.fill('input[name="email"]', 'test@example.com');
    await page.fill('input[name="password"]', 'password123');
    await page.click('button[type="submit"]');

    await expect(page).toHaveURL('/dashboard');

    // Faz logout
    await page.click('button:has-text("Sair")');

    // Verifica redirecionamento
    await expect(page).toHaveURL('/login');
  });
});

// ═══════════════════════════════════════════════════════════
// tests/e2e/produtos.spec.ts
// ═══════════════════════════════════════════════════════════
import { test, expect } from '@playwright/test';

test.describe('Gestão de Produtos', () => {
  test.beforeEach(async ({ page }) => {
    // Login antes de cada teste
    await page.goto('/login');
    await page.fill('input[name="email"]', 'admin@example.com');
    await page.fill('input[name="password"]', 'admin123');
    await page.click('button[type="submit"]');
    await expect(page).toHaveURL('/dashboard');
  });

  test('deve criar novo produto', async ({ page }) => {
    await page.goto('/produtos');
    await page.click('button:has-text("Novo Produto")');

    // Preenche formulário
    await page.fill('input[name="nome"]', 'Notebook Dell');
    await page.fill('textarea[name="descricao"]', 'Notebook de alta performance');
    await page.fill('input[name="preco"]', '3500');
    await page.fill('input[name="estoque"]', '10');

    // Submit
    await page.click('button[type="submit"]');

    // Verifica sucesso
    await expect(page.getByText(/produto criado/i)).toBeVisible();
    await expect(page.getByText('Notebook Dell')).toBeVisible();
  });

  test('deve editar produto existente', async ({ page }) => {
    await page.goto('/produtos');

    // Clica em editar no primeiro produto
    await page.click('button[aria-label="Editar"]:first-of-type');

    // Atualiza nome
    await page.fill('input[name="nome"]', 'Notebook Dell XPS Atualizado');
    await page.click('button[type="submit"]');

    // Verifica atualização
    await expect(page.getByText(/produto atualizado/i)).toBeVisible();
    await expect(page.getByText('Notebook Dell XPS Atualizado')).toBeVisible();
  });

  test('deve deletar produto', async ({ page }) => {
    await page.goto('/produtos');

    // Conta produtos antes
    const produtosAntes = await page.locator('[data-testid="produto-card"]').count();

    // Deleta primeiro produto
    await page.click('button[aria-label="Deletar"]:first-of-type');

    // Confirma modal
    await page.click('button:has-text("Confirmar")');

    // Verifica que foi deletado
    await expect(page.getByText(/produto deletado/i)).toBeVisible();

    const produtosDepois = await page.locator('[data-testid="produto-card"]').count();
    expect(produtosDepois).toBe(produtosAntes - 1);
  });

  test('deve buscar produtos', async ({ page }) => {
    await page.goto('/produtos');

    // Digita na busca
    await page.fill('input[placeholder*="Buscar"]', 'Notebook');

    // Aguarda resultados
    await page.waitForTimeout(500);

    // Verifica que apenas notebooks são exibidos
    const produtos = page.locator('[data-testid="produto-card"]');
    const count = await produtos.count();

    for (let i = 0; i < count; i++) {
      const text = await produtos.nth(i).textContent();
      expect(text?.toLowerCase()).toContain('notebook');
    }
  });

  test('deve paginar produtos', async ({ page }) => {
    await page.goto('/produtos');

    // Clica na página 2
    await page.click('button:has-text("2")');

    // Verifica URL atualizada
    await expect(page).toHaveURL(/page=2/);

    // Verifica que produtos diferentes são exibidos
    await expect(page.locator('[data-testid="produto-card"]')).toHaveCount(10);
  });
});

// ═══════════════════════════════════════════════════════════
// tests/e2e/checkout.spec.ts
// ═══════════════════════════════════════════════════════════
import { test, expect } from '@playwright/test';

test.describe('Fluxo de Checkout', () => {
  test('deve completar compra com sucesso', async ({ page }) => {
    // 1. Navega para catálogo
    await page.goto('/catalogo');

    // 2. Adiciona produto ao carrinho
    await page.click('[data-testid="produto-1"] button:has-text("Adicionar")');
    await expect(page.getByText(/adicionado ao carrinho/i)).toBeVisible();

    // 3. Vai para carrinho
    await page.click('a[href="/carrinho"]');
    await expect(page).toHaveURL('/carrinho');

    // 4. Verifica produto no carrinho
    await expect(page.getByText('1 item')).toBeVisible();

    // 5. Procede para checkout
    await page.click('button:has-text("Finalizar Compra")');
    await expect(page).toHaveURL('/checkout');

    // 6. Preenche dados de entrega
    await page.fill('input[name="nome"]', 'João Silva');
    await page.fill('input[name="email"]', 'joao@example.com');
    await page.fill('input[name="telefone"]', '11999999999');
    await page.fill('input[name="cep"]', '01234567');
    await page.fill('input[name="rua"]', 'Rua Teste');
    await page.fill('input[name="numero"]', '123');
    await page.fill('input[name="cidade"]', 'São Paulo');
    await page.fill('input[name="estado"]', 'SP');

    // 7. Seleciona pagamento
    await page.click('input[value="cartao"]');

    // 8. Preenche dados do cartão
    await page.fill('input[name="numero-cartao"]', '4111111111111111');
    await page.fill('input[name="nome-cartao"]', 'JOAO SILVA');
    await page.fill('input[name="validade"]', '12/25');
    await page.fill('input[name="cvv"]', '123');

    // 9. Finaliza pedido
    await page.click('button:has-text("Confirmar Pedido")');

    // 10. Verifica página de sucesso
    await expect(page).toHaveURL(/\/pedido\/\d+\/sucesso/);
    await expect(page.getByText(/pedido confirmado/i)).toBeVisible();

    // 11. Verifica número do pedido
    const numeroPedido = await page.locator('[data-testid="numero-pedido"]').textContent();
    expect(numeroPedido).toMatch(/^#\d+$/);
  });

  test('deve validar campos obrigatórios', async ({ page }) => {
    await page.goto('/carrinho');
    
    // Adiciona produto mock
    await page.evaluate(() => {
      localStorage.setItem('cart', JSON.stringify([
        { id: '1', nome: 'Produto Teste', preco: 100, quantidade: 1 }
      ]));
    });
    await page.reload();

    await page.click('button:has-text("Finalizar Compra")');

    // Tenta submeter sem preencher
    await page.click('button:has-text("Continuar")');

    // Verifica erros de validação
    await expect(page.getByText(/campo obrigatório/i)).toHaveCount(8);
  });
});
```

---

### **11.5. Testing Best Practices**

```typescript
// ✅ BOM
describe('UserProfile', () => {
  it('exibe nome do usuário', () => {
    render(<UserProfile user={{ name: 'João' }} />);
    expect(screen.getByText('João')).toBeInTheDocument();
  });

  it('mostra avatar padrão quando não há imagem', () => {
    render(<UserProfile user={{ name: 'João' }} />);
    expect(screen.getByAltText('Avatar padrão')).toBeInTheDocument();
  });
});

// ❌ EVITAR
describe('UserProfile', () => {
  it('funciona', () => { // ❌ Descrição vaga
    const { container } = render(<UserProfile />); // ❌ Sem props
    expect(container).toBeTruthy(); // ❌ Asserção fraca
  });
});

// ✅ BOM - Teste AAA (Arrange, Act, Assert)
it('adiciona produto ao carrinho', async () => {
  // Arrange
  const user = userEvent.setup();
  const handleAdd = vi.fn();
  render(<ProductCard product={mockProduct} onAdd={handleAdd} />);
  
  // Act
  await user.click(screen.getByRole('button', { name: /adicionar/i }));
  
  // Assert
  expect(handleAdd).toHaveBeenCalledWith(mockProduct.id);
  expect(screen.getByText(/adicionado/i)).toBeInTheDocument();
});

// ✅ BOM - Usar queries semânticas
screen.getByRole('button', { name: /enviar/i }); // ✅
screen.getByLabelText(/email/i); // ✅
screen.getByText(/bem-vindo/i); // ✅

// ❌ EVITAR
screen.getByClassName('submit-btn'); // ❌
screen.getByTestId('email-input'); // ❌ (usar apenas quando necessário)
```

---

## Conclusão

Esta arquitetura fornece:
- ✅ Separação clara de responsabilidades
- ✅ Código testável e manutenível
- ✅ Escalabilidade para projetos grandes
- ✅ Reutilização de código
- ✅ Facilidade de onboarding de novos desenvolvedores

---

**Próxima Seção**: Security Best Practices (Melhores Práticas de Segurança)

**Progresso**: 11 de 14 seções concluídas ✅

---

## 12. Security Best Practices (Melhores Práticas de Segurança)

Segurança é fundamental em aplicações web modernas. Esta seção cobre as principais práticas de segurança.

### **12.1. Authentication (Autenticação)**

#### **NextAuth.js (Auth.js) - Setup Completo**

```typescript
// ═══════════════════════════════════════════════════════════
// lib/auth.ts
// ═══════════════════════════════════════════════════════════
import NextAuth, { NextAuthOptions } from 'next-auth';
import CredentialsProvider from 'next-auth/providers/credentials';
import GoogleProvider from 'next-auth/providers/google';
import { PrismaAdapter } from '@next-auth/prisma-adapter';
import { db } from '@/lib/db';
import bcrypt from 'bcryptjs';

export const authOptions: NextAuthOptions = {
  adapter: PrismaAdapter(db),
  
  providers: [
    // ✅ Credentials Provider
    CredentialsProvider({
      name: 'credentials',
      credentials: {
        email: { label: 'Email', type: 'email' },
        password: { label: 'Senha', type: 'password' },
      },
      async authorize(credentials) {
        if (!credentials?.email || !credentials?.password) {
          throw new Error('Credenciais inválidas');
        }

        const user = await db.user.findUnique({
          where: { email: credentials.email },
        });

        if (!user || !user.hashedPassword) {
          throw new Error('Usuário não encontrado');
        }

        const isCorrectPassword = await bcrypt.compare(
          credentials.password,
          user.hashedPassword
        );

        if (!isCorrectPassword) {
          throw new Error('Senha incorreta');
        }

        return {
          id: user.id,
          email: user.email,
          name: user.name,
          role: user.role,
        };
      },
    }),

    // ✅ OAuth Provider
    GoogleProvider({
      clientId: process.env.GOOGLE_CLIENT_ID!,
      clientSecret: process.env.GOOGLE_CLIENT_SECRET!,
    }),
  ],

  // ✅ Session Strategy
  session: {
    strategy: 'jwt',
    maxAge: 30 * 24 * 60 * 60, // 30 dias
  },

  // ✅ JWT Configuration
  jwt: {
    secret: process.env.NEXTAUTH_SECRET,
    maxAge: 30 * 24 * 60 * 60,
  },

  // ✅ Callbacks
  callbacks: {
    async jwt({ token, user, account }) {
      if (user) {
        token.id = user.id;
        token.role = user.role;
      }
      return token;
    },

    async session({ session, token }) {
      if (session.user) {
        session.user.id = token.id as string;
        session.user.role = token.role as string;
      }
      return session;
    },

    async redirect({ url, baseUrl }) {
      // Apenas permite redirects para o mesmo domínio
      if (url.startsWith('/')) return `${baseUrl}${url}`;
      if (new URL(url).origin === baseUrl) return url;
      return baseUrl;
    },
  },

  // ✅ Custom Pages
  pages: {
    signIn: '/login',
    signOut: '/logout',
    error: '/error',
    verifyRequest: '/verify-request',
  },

  // ✅ Events
  events: {
    async signIn({ user, account }) {
      console.log('User signed in:', user.email);
      
      // Log de auditoria
      await db.auditLog.create({
        data: {
          userId: user.id,
          action: 'SIGN_IN',
          provider: account?.provider,
          timestamp: new Date(),
        },
      });
    },

    async signOut({ token }) {
      console.log('User signed out:', token.email);
    },
  },

  // ✅ Debug (apenas em desenvolvimento)
  debug: process.env.NODE_ENV === 'development',
};

export const handler = NextAuth(authOptions);

// ═══════════════════════════════════════════════════════════
// app/api/auth/[...nextauth]/route.ts
// ═══════════════════════════════════════════════════════════
export { handler as GET, handler as POST };

// ═══════════════════════════════════════════════════════════
// lib/auth-utils.ts - Utilities
// ═══════════════════════════════════════════════════════════
import { getServerSession } from 'next-auth';
import { authOptions } from './auth';
import bcrypt from 'bcryptjs';

// ✅ Get Session no Servidor
export async function getSession() {
  return await getServerSession(authOptions);
}

// ✅ Get User Autenticado
export async function getCurrentUser() {
  const session = await getSession();
  return session?.user;
}

// ✅ Verificar se está autenticado
export async function requireAuth() {
  const session = await getSession();
  
  if (!session?.user) {
    throw new Error('Não autenticado');
  }
  
  return session.user;
}

// ✅ Verificar Role
export async function requireRole(role: string | string[]) {
  const user = await requireAuth();
  const roles = Array.isArray(role) ? role : [role];
  
  if (!roles.includes(user.role)) {
    throw new Error('Sem permissão');
  }
  
  return user;
}

// ✅ Hash de Senha
export async function hashPassword(password: string): Promise<string> {
  return await bcrypt.hash(password, 12);
}

// ✅ Validar Senha
export async function validatePassword(
  password: string,
  hashedPassword: string
): Promise<boolean> {
  return await bcrypt.compare(password, hashedPassword);
}

// ═══════════════════════════════════════════════════════════
// Uso em Server Components
// ═══════════════════════════════════════════════════════════
import { requireAuth } from '@/lib/auth-utils';

export default async function DashboardPage() {
  const user = await requireAuth();
  
  return (
    <div>
      <h1>Bem-vindo, {user.name}!</h1>
    </div>
  );
}

// ═══════════════════════════════════════════════════════════
// Uso em Server Actions
// ═══════════════════════════════════════════════════════════
'use server';

import { requireRole } from '@/lib/auth-utils';
import { revalidatePath } from 'next/cache';

export async function deleteProduto(id: string) {
  // Apenas ADMIN pode deletar
  await requireRole('ADMIN');
  
  await db.produto.delete({ where: { id } });
  revalidatePath('/produtos');
}

// ═══════════════════════════════════════════════════════════
// Uso em Client Components
// ═══════════════════════════════════════════════════════════
'use client';

import { useSession, signIn, signOut } from 'next-auth/react';

export function UserMenu() {
  const { data: session, status } = useSession();
  
  if (status === 'loading') {
    return <div>Carregando...</div>;
  }
  
  if (!session) {
    return <button onClick={() => signIn()}>Entrar</button>;
  }
  
  return (
    <div>
      <p>Olá, {session.user.name}</p>
      <button onClick={() => signOut()}>Sair</button>
    </div>
  );
}
```

---

### **12.2. Authorization (Autorização)**

#### **RBAC (Role-Based Access Control)**

```typescript
// ═══════════════════════════════════════════════════════════
// lib/permissions.ts
// ═══════════════════════════════════════════════════════════
export enum Role {
  USER = 'USER',
  ADMIN = 'ADMIN',
  MODERATOR = 'MODERATOR',
}

export enum Permission {
  // Produtos
  PRODUTOS_READ = 'produtos:read',
  PRODUTOS_CREATE = 'produtos:create',
  PRODUTOS_UPDATE = 'produtos:update',
  PRODUTOS_DELETE = 'produtos:delete',
  
  // Usuários
  USERS_READ = 'users:read',
  USERS_CREATE = 'users:create',
  USERS_UPDATE = 'users:update',
  USERS_DELETE = 'users:delete',
  
  // Configurações
  SETTINGS_READ = 'settings:read',
  SETTINGS_UPDATE = 'settings:update',
}

// ✅ Mapa de Permissões por Role
export const rolePermissions: Record<Role, Permission[]> = {
  [Role.USER]: [
    Permission.PRODUTOS_READ,
  ],
  
  [Role.MODERATOR]: [
    Permission.PRODUTOS_READ,
    Permission.PRODUTOS_CREATE,
    Permission.PRODUTOS_UPDATE,
    Permission.USERS_READ,
  ],
  
  [Role.ADMIN]: Object.values(Permission), // Todas as permissões
};

// ✅ Verificar se Role tem Permissão
export function hasPermission(role: Role, permission: Permission): boolean {
  return rolePermissions[role]?.includes(permission) ?? false;
}

// ✅ Verificar múltiplas permissões
export function hasAnyPermission(
  role: Role,
  permissions: Permission[]
): boolean {
  return permissions.some(p => hasPermission(role, p));
}

export function hasAllPermissions(
  role: Role,
  permissions: Permission[]
): boolean {
  return permissions.every(p => hasPermission(role, p));
}

// ═══════════════════════════════════════════════════════════
// middleware.ts - Route Protection
// ═══════════════════════════════════════════════════════════
import { NextResponse } from 'next/server';
import type { NextRequest } from 'next/server';
import { getToken } from 'next-auth/jwt';

export async function middleware(request: NextRequest) {
  const token = await getToken({ req: request });
  const { pathname } = request.nextUrl;

  // ✅ Rotas públicas
  const publicRoutes = ['/', '/login', '/register', '/forgot-password'];
  if (publicRoutes.includes(pathname)) {
    return NextResponse.next();
  }

  // ✅ Verificar autenticação
  if (!token) {
    const loginUrl = new URL('/login', request.url);
    loginUrl.searchParams.set('callbackUrl', pathname);
    return NextResponse.redirect(loginUrl);
  }

  // ✅ Verificar permissões por rota
  const adminRoutes = ['/admin', '/usuarios', '/configuracoes'];
  const isAdminRoute = adminRoutes.some(route => pathname.startsWith(route));
  
  if (isAdminRoute && token.role !== 'ADMIN') {
    return NextResponse.redirect(new URL('/unauthorized', request.url));
  }

  // ✅ Rate Limiting por IP
  const ip = request.ip ?? 'unknown';
  const rateLimit = await checkRateLimit(ip);
  
  if (!rateLimit.success) {
    return NextResponse.json(
      { error: 'Too many requests' },
      { status: 429 }
    );
  }

  return NextResponse.next();
}

export const config = {
  matcher: [
    '/((?!api|_next/static|_next/image|favicon.ico).*)',
  ],
};

// ═══════════════════════════════════════════════════════════
// components/PermissionGate.tsx - Component-Level Protection
// ═══════════════════════════════════════════════════════════
'use client';

import { useSession } from 'next-auth/react';
import { hasPermission, Permission, Role } from '@/lib/permissions';

interface PermissionGateProps {
  permission: Permission;
  children: React.ReactNode;
  fallback?: React.ReactNode;
}

export function PermissionGate({
  permission,
  children,
  fallback = null,
}: PermissionGateProps) {
  const { data: session } = useSession();
  
  if (!session?.user) {
    return <>{fallback}</>;
  }
  
  const userRole = session.user.role as Role;
  const hasAccess = hasPermission(userRole, permission);
  
  if (!hasAccess) {
    return <>{fallback}</>;
  }
  
  return <>{children}</>;
}

// ═══════════════════════════════════════════════════════════
// Uso do PermissionGate
// ═══════════════════════════════════════════════════════════
import { PermissionGate } from '@/components/PermissionGate';
import { Permission } from '@/lib/permissions';

export function ProdutoActions() {
  return (
    <div>
      <PermissionGate permission={Permission.PRODUTOS_UPDATE}>
        <button>Editar</button>
      </PermissionGate>
      
      <PermissionGate permission={Permission.PRODUTOS_DELETE}>
        <button>Deletar</button>
      </PermissionGate>
    </div>
  );
}
```

---

### **12.3. CSRF Protection**

```typescript
// ═══════════════════════════════════════════════════════════
// lib/csrf.ts - CSRF Token Generation
// ═══════════════════════════════════════════════════════════
import { randomBytes, createHmac } from 'crypto';

const CSRF_SECRET = process.env.CSRF_SECRET!;

// ✅ Gerar CSRF Token
export function generateCsrfToken(): string {
  const token = randomBytes(32).toString('hex');
  const timestamp = Date.now().toString();
  
  const hmac = createHmac('sha256', CSRF_SECRET);
  hmac.update(`${token}.${timestamp}`);
  const signature = hmac.digest('hex');
  
  return `${token}.${timestamp}.${signature}`;
}

// ✅ Validar CSRF Token
export function validateCsrfToken(token: string): boolean {
  try {
    const [tokenValue, timestamp, signature] = token.split('.');
    
    // Verificar expiração (1 hora)
    const tokenAge = Date.now() - parseInt(timestamp);
    if (tokenAge > 3600000) {
      return false;
    }
    
    // Verificar assinatura
    const hmac = createHmac('sha256', CSRF_SECRET);
    hmac.update(`${tokenValue}.${timestamp}`);
    const expectedSignature = hmac.digest('hex');
    
    return signature === expectedSignature;
  } catch {
    return false;
  }
}

// ═══════════════════════════════════════════════════════════
// app/api/csrf/route.ts - CSRF Token Endpoint
// ═══════════════════════════════════════════════════════════
import { NextResponse } from 'next/server';
import { generateCsrfToken } from '@/lib/csrf';

export async function GET() {
  const csrfToken = generateCsrfToken();
  
  return NextResponse.json({ csrfToken });
}

// ═══════════════════════════════════════════════════════════
// hooks/useCsrfToken.ts
// ═══════════════════════════════════════════════════════════
'use client';

import { useState, useEffect } from 'react';

export function useCsrfToken() {
  const [csrfToken, setCsrfToken] = useState<string | null>(null);
  
  useEffect(() => {
    fetch('/api/csrf')
      .then(res => res.json())
      .then(data => setCsrfToken(data.csrfToken));
  }, []);
  
  return csrfToken;
}

// ═══════════════════════════════════════════════════════════
// Uso em Formulários
// ═══════════════════════════════════════════════════════════
'use client';

import { useCsrfToken } from '@/hooks/useCsrfToken';

export function CreateProductForm() {
  const csrfToken = useCsrfToken();
  
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    const formData = new FormData(e.target as HTMLFormElement);
    
    // ✅ Incluir CSRF Token
    const response = await fetch('/api/produtos', {
      method: 'POST',
      headers: {
        'X-CSRF-Token': csrfToken!,
      },
      body: formData,
    });
  };
  
  if (!csrfToken) {
    return <div>Carregando...</div>;
  }
  
  return (
    <form onSubmit={handleSubmit}>
      <input type="hidden" name="csrfToken" value={csrfToken} />
      {/* Campos do formulário */}
    </form>
  );
}

// ═══════════════════════════════════════════════════════════
// Middleware para validar CSRF
// ═══════════════════════════════════════════════════════════
import { NextRequest, NextResponse } from 'next/server';
import { validateCsrfToken } from '@/lib/csrf';

export async function POST(request: NextRequest) {
  const csrfToken = request.headers.get('X-CSRF-Token');
  
  if (!csrfToken || !validateCsrfToken(csrfToken)) {
    return NextResponse.json(
      { error: 'Invalid CSRF token' },
      { status: 403 }
    );
  }
  
  // Processar request
  // ...
}
```

---

### **12.4. XSS Prevention (Cross-Site Scripting)**

```typescript
// ═══════════════════════════════════════════════════════════
// lib/sanitize.ts - Sanitização de Input
// ═══════════════════════════════════════════════════════════
import DOMPurify from 'isomorphic-dompurify';

// ✅ Sanitizar HTML
export function sanitizeHtml(dirty: string): string {
  return DOMPurify.sanitize(dirty, {
    ALLOWED_TAGS: ['b', 'i', 'em', 'strong', 'a', 'p', 'br'],
    ALLOWED_ATTR: ['href', 'title'],
  });
}

// ✅ Escapar HTML
export function escapeHtml(text: string): string {
  const map: Record<string, string> = {
    '&': '&amp;',
    '<': '&lt;',
    '>': '&gt;',
    '"': '&quot;',
    "'": '&#x27;',
    '/': '&#x2F;',
  };
  
  return text.replace(/[&<>"'/]/g, char => map[char]);
}

// ✅ Validar URL (prevenir javascript:)
export function isSafeUrl(url: string): boolean {
  const dangerousProtocols = ['javascript:', 'data:', 'vbscript:'];
  const lowerUrl = url.toLowerCase().trim();
  
  return !dangerousProtocols.some(protocol => lowerUrl.startsWith(protocol));
}

// ═══════════════════════════════════════════════════════════
// components/SafeHtml.tsx - Renderizar HTML Sanitizado
// ═══════════════════════════════════════════════════════════
interface SafeHtmlProps {
  html: string;
  className?: string;
}

export function SafeHtml({ html, className }: SafeHtmlProps) {
  const sanitized = sanitizeHtml(html);
  
  return (
    <div
      className={className}
      dangerouslySetInnerHTML={{ __html: sanitized }}
    />
  );
}

// ═══════════════════════════════════════════════════════════
// Uso
// ═══════════════════════════════════════════════════════════
import { SafeHtml } from '@/components/SafeHtml';

export function BlogPost({ content }: { content: string }) {
  return <SafeHtml html={content} className="prose" />;
}

// ✅ BOM - React escapa automaticamente
export function UserComment({ text }: { text: string }) {
  return <p>{text}</p>; // Seguro - React escapa
}

// ❌ EVITAR - dangerouslySetInnerHTML sem sanitização
export function UnsafeComment({ html }: { html: string }) {
  return <div dangerouslySetInnerHTML={{ __html: html }} />; // ❌ Perigoso!
}

// ═══════════════════════════════════════════════════════════
// Validação de Input
// ═══════════════════════════════════════════════════════════
import { z } from 'zod';

const commentSchema = z.object({
  text: z.string()
    .min(1, 'Comentário não pode estar vazio')
    .max(500, 'Comentário muito longo')
    .refine(
      (text) => !/<script|javascript:/i.test(text),
      'Conteúdo inválido detectado'
    ),
});
```

---

### **12.5. SQL Injection Prevention**

```typescript
// ═══════════════════════════════════════════════════════════
// ✅ BOM - Usar Prisma (Previne SQL Injection)
// ═══════════════════════════════════════════════════════════
import { db } from '@/lib/db';

// ✅ Seguro - Prisma usa parameterized queries
export async function getProdutosByName(name: string) {
  return await db.produto.findMany({
    where: {
      nome: {
        contains: name, // Seguro - Prisma sanitiza
      },
    },
  });
}

// ✅ Seguro - Raw query com parâmetros
export async function getUsers(email: string) {
  return await db.$queryRaw`
    SELECT * FROM users WHERE email = ${email}
  `; // Seguro - Tagged template
}

// ❌ EVITAR - String concatenation
export async function unsafeQuery(email: string) {
  // ❌ NUNCA FAZER ISSO!
  return await db.$queryRawUnsafe(
    `SELECT * FROM users WHERE email = '${email}'`
  );
}

// ═══════════════════════════════════════════════════════════
// Validação de Input antes do DB
// ═══════════════════════════════════════════════════════════
import { z } from 'zod';

const searchSchema = z.object({
  query: z.string()
    .min(1)
    .max(100)
    .regex(/^[a-zA-Z0-9\s]+$/, 'Apenas letras e números'),
});

export async function searchProdutos(query: string) {
  // ✅ Validar primeiro
  const validated = searchSchema.parse({ query });
  
  // ✅ Usar Prisma
  return await db.produto.findMany({
    where: {
      nome: {
        contains: validated.query,
      },
    },
  });
}
```

---

### **12.6. Security Headers**

```typescript
// ═══════════════════════════════════════════════════════════
// next.config.js - Security Headers
// ═══════════════════════════════════════════════════════════
/** @type {import('next').NextConfig} */
const nextConfig = {
  async headers() {
    return [
      {
        source: '/:path*',
        headers: [
          // ✅ Content Security Policy
          {
            key: 'Content-Security-Policy',
            value: [
              "default-src 'self'",
              "script-src 'self' 'unsafe-eval' 'unsafe-inline' https://vercel.live",
              "style-src 'self' 'unsafe-inline'",
              "img-src 'self' data: blob: https:",
              "font-src 'self' data:",
              "connect-src 'self' https:",
              "frame-ancestors 'none'",
            ].join('; '),
          },
          // ✅ X-Frame-Options (Clickjacking Protection)
          {
            key: 'X-Frame-Options',
            value: 'DENY',
          },
          // ✅ X-Content-Type-Options
          {
            key: 'X-Content-Type-Options',
            value: 'nosniff',
          },
          // ✅ Referrer-Policy
          {
            key: 'Referrer-Policy',
            value: 'strict-origin-when-cross-origin',
          },
          // ✅ Permissions-Policy
          {
            key: 'Permissions-Policy',
            value: 'camera=(), microphone=(), geolocation=()',
          },
          // ✅ Strict-Transport-Security (HSTS)
          {
            key: 'Strict-Transport-Security',
            value: 'max-age=63072000; includeSubDomains; preload',
          },
          // ✅ X-XSS-Protection
          {
            key: 'X-XSS-Protection',
            value: '1; mode=block',
          },
        ],
      },
    ];
  },
};

module.exports = nextConfig;
```

---

### **12.7. Environment Variables Security**

```typescript
// ═══════════════════════════════════════════════════════════
// .env.example - Template
// ═══════════════════════════════════════════════════════════
# Database
DATABASE_URL="postgresql://..."

# Auth
NEXTAUTH_URL="http://localhost:3000"
NEXTAUTH_SECRET="generate-with-openssl-rand-base64-32"

# OAuth
GOOGLE_CLIENT_ID=""
GOOGLE_CLIENT_SECRET=""

# API Keys
STRIPE_SECRET_KEY=""
RESEND_API_KEY=""

# ═══════════════════════════════════════════════════════════
# .gitignore - NUNCA COMMITAR
# ═══════════════════════════════════════════════════════════
.env
.env.local
.env.production

# ═══════════════════════════════════════════════════════════
// lib/env.ts - Validação de Env Vars
// ═══════════════════════════════════════════════════════════
import { z } from 'zod';

const envSchema = z.object({
  // Database
  DATABASE_URL: z.string().url(),
  
  // Auth
  NEXTAUTH_URL: z.string().url(),
  NEXTAUTH_SECRET: z.string().min(32),
  
  // OAuth
  GOOGLE_CLIENT_ID: z.string().min(1),
  GOOGLE_CLIENT_SECRET: z.string().min(1),
  
  // API Keys
  STRIPE_SECRET_KEY: z.string().startsWith('sk_'),
  RESEND_API_KEY: z.string().startsWith('re_'),
  
  // Node Env
  NODE_ENV: z.enum(['development', 'production', 'test']),
});

// ✅ Validar na inicialização
export const env = envSchema.parse(process.env);

// ═══════════════════════════════════════════════════════════
// Uso Seguro
// ═══════════════════════════════════════════════════════════
import { env } from '@/lib/env';

// ✅ BOM
const apiKey = env.STRIPE_SECRET_KEY;

// ❌ EVITAR - Acessar process.env diretamente
const unsafeKey = process.env.STRIPE_SECRET_KEY; // Pode ser undefined

// ✅ BOM - Variáveis públicas (NEXT_PUBLIC_)
const publicApiUrl = process.env.NEXT_PUBLIC_API_URL;

// ❌ NUNCA expor secrets no cliente
const secret = process.env.NEXTAUTH_SECRET; // ❌ NUNCA em Client Component!
```

---

### **12.8. Rate Limiting**

```typescript
// ═══════════════════════════════════════════════════════════
// lib/rate-limit.ts - In-Memory Rate Limiter
// ═══════════════════════════════════════════════════════════
interface RateLimitConfig {
  interval: number; // ms
  uniqueTokenPerInterval: number;
}

class RateLimiter {
  private tokens: Map<string, number[]> = new Map();
  
  constructor(private config: RateLimitConfig) {}
  
  check(identifier: string, limit: number): { success: boolean; remaining: number } {
    const now = Date.now();
    const windowStart = now - this.config.interval;
    
    // Obter tokens do identificador
    let tokens = this.tokens.get(identifier) || [];
    
    // Remover tokens expirados
    tokens = tokens.filter(token => token > windowStart);
    
    // Verificar limite
    if (tokens.length >= limit) {
      this.tokens.set(identifier, tokens);
      return { success: false, remaining: 0 };
    }
    
    // Adicionar novo token
    tokens.push(now);
    this.tokens.set(identifier, tokens);
    
    // Limpar tokens antigos periodicamente
    this.cleanup();
    
    return { success: true, remaining: limit - tokens.length };
  }
  
  private cleanup() {
    if (this.tokens.size > this.config.uniqueTokenPerInterval) {
      const now = Date.now();
      const windowStart = now - this.config.interval;
      
      for (const [key, tokens] of this.tokens.entries()) {
        const validTokens = tokens.filter(token => token > windowStart);
        
        if (validTokens.length === 0) {
          this.tokens.delete(key);
        } else {
          this.tokens.set(key, validTokens);
        }
      }
    }
  }
}

// ✅ Criar instância global
export const rateLimiter = new RateLimiter({
  interval: 60 * 1000, // 1 minuto
  uniqueTokenPerInterval: 500,
});

// ═══════════════════════════════════════════════════════════
// Uso em API Route
// ═══════════════════════════════════════════════════════════
import { NextRequest, NextResponse } from 'next/server';
import { rateLimiter } from '@/lib/rate-limit';

export async function POST(request: NextRequest) {
  const ip = request.ip ?? 'unknown';
  
  // ✅ Verificar rate limit (10 requests por minuto)
  const { success, remaining } = rateLimiter.check(ip, 10);
  
  if (!success) {
    return NextResponse.json(
      { error: 'Too many requests' },
      { 
        status: 429,
        headers: {
          'X-RateLimit-Limit': '10',
          'X-RateLimit-Remaining': '0',
          'Retry-After': '60',
        },
      }
    );
  }
  
  // Processar request
  const data = await request.json();
  
  return NextResponse.json(
    { success: true },
    {
      headers: {
        'X-RateLimit-Limit': '10',
        'X-RateLimit-Remaining': remaining.toString(),
      },
    }
  );
}

// ═══════════════════════════════════════════════════════════
// lib/redis-rate-limit.ts - Rate Limit com Redis (Produção)
// ═══════════════════════════════════════════════════════════
import { Redis } from '@upstash/redis';

const redis = new Redis({
  url: process.env.UPSTASH_REDIS_URL!,
  token: process.env.UPSTASH_REDIS_TOKEN!,
});

export async function checkRateLimit(
  identifier: string,
  limit: number = 10,
  window: number = 60
): Promise<{ success: boolean; remaining: number }> {
  const key = `rate_limit:${identifier}`;
  
  // Usar Redis pipeline para atomicidade
  const pipeline = redis.pipeline();
  pipeline.incr(key);
  pipeline.expire(key, window);
  
  const [count] = await pipeline.exec() as [number, string];
  
  if (count > limit) {
    return { success: false, remaining: 0 };
  }
  
  return { success: true, remaining: limit - count };
}
```

---

### **12.9. Input Validation**

```typescript
// ═══════════════════════════════════════════════════════════
// lib/validations/common.ts - Validações Comuns
// ═══════════════════════════════════════════════════════════
import { z } from 'zod';

// ✅ Email
export const emailSchema = z.string()
  .email('Email inválido')
  .toLowerCase()
  .trim();

// ✅ Senha Forte
export const passwordSchema = z.string()
  .min(8, 'Senha deve ter no mínimo 8 caracteres')
  .regex(/[A-Z]/, 'Senha deve conter maiúscula')
  .regex(/[a-z]/, 'Senha deve conter minúscula')
  .regex(/[0-9]/, 'Senha deve conter número')
  .regex(/[^A-Za-z0-9]/, 'Senha deve conter caractere especial');

// ✅ CPF
export const cpfSchema = z.string()
  .regex(/^\d{3}\.\d{3}\.\d{3}-\d{2}$/, 'CPF inválido')
  .refine(validateCPF, 'CPF inválido');

function validateCPF(cpf: string): boolean {
  const numbers = cpf.replace(/\D/g, '');
  
  if (numbers.length !== 11) return false;
  if (/^(\d)\1+$/.test(numbers)) return false;
  
  // Validar dígitos verificadores
  let sum = 0;
  for (let i = 0; i < 9; i++) {
    sum += parseInt(numbers[i]) * (10 - i);
  }
  let digit = 11 - (sum % 11);
  if (digit > 9) digit = 0;
  if (parseInt(numbers[9]) !== digit) return false;
  
  sum = 0;
  for (let i = 0; i < 10; i++) {
    sum += parseInt(numbers[i]) * (11 - i);
  }
  digit = 11 - (sum % 11);
  if (digit > 9) digit = 0;
  if (parseInt(numbers[10]) !== digit) return false;
  
  return true;
}

// ✅ URL
export const urlSchema = z.string()
  .url('URL inválida')
  .refine(
    (url) => !url.toLowerCase().startsWith('javascript:'),
    'URL não permitida'
  );

// ✅ Telefone
export const phoneSchema = z.string()
  .regex(/^\(\d{2}\) \d{4,5}-\d{4}$/, 'Telefone inválido');

// ✅ CEP
export const cepSchema = z.string()
  .regex(/^\d{5}-\d{3}$/, 'CEP inválido');

// ═══════════════════════════════════════════════════════════
// Uso em Server Actions
// ═══════════════════════════════════════════════════════════
'use server';

import { z } from 'zod';
import { emailSchema, passwordSchema } from '@/lib/validations/common';

const registerSchema = z.object({
  name: z.string().min(3).max(100),
  email: emailSchema,
  password: passwordSchema,
  confirmPassword: z.string(),
}).refine(
  (data) => data.password === data.confirmPassword,
  {
    message: 'Senhas não conferem',
    path: ['confirmPassword'],
  }
);

export async function register(data: FormData) {
  // ✅ Validar input
  const validated = registerSchema.parse(Object.fromEntries(data));
  
  // Processar registro
  // ...
}
```

---

### **12.10. Security Checklist**

```typescript
// ═══════════════════════════════════════════════════════════
// CHECKLIST DE SEGURANÇA
// ═══════════════════════════════════════════════════════════

/**
 * ✅ AUTENTICAÇÃO & AUTORIZAÇÃO
 * □ NextAuth.js configurado
 * □ JWT com secret forte
 * □ Session com expiração
 * □ RBAC implementado
 * □ Middleware de proteção de rotas
 * □ Logout em todos os dispositivos
 */

/**
 * ✅ PROTEÇÃO CONTRA ATAQUES
 * □ CSRF tokens em formulários
 * □ XSS prevention (sanitização)
 * □ SQL Injection prevention (Prisma)
 * □ Rate limiting implementado
 * □ Input validation (Zod)
 * □ Security headers configurados
 */

/**
 * ✅ DADOS SENSÍVEIS
 * □ .env não commitado
 * □ Secrets em variáveis de ambiente
 * □ Validação de env vars
 * □ Senhas hasheadas (bcrypt)
 * □ Dados PII criptografados
 * □ HTTPS em produção
 */

/**
 * ✅ API SECURITY
 * □ Rate limiting por IP
 * □ CORS configurado
 * □ API keys rotacionadas
 * □ Request size limits
 * □ Timeout configurado
 * □ Error messages sanitizados
 */

/**
 * ✅ FRONTEND SECURITY
 * □ React escapa output automaticamente
 * □ dangerouslySetInnerHTML com DOMPurify
 * □ URLs validadas antes de usar
 * □ File uploads validados
 * □ Tokens armazenados com segurança
 * □ LocalStorage usado apropriadamente
 */

/**
 * ✅ MONITORAMENTO
 * □ Logs de autenticação
 * □ Logs de ações críticas
 * □ Alertas de tentativas suspeitas
 * □ Auditoria de acessos
 * □ Error tracking (Sentry)
 * □ Revisão regular de dependências
 */
```

---

**Próxima Seção**: Code Quality (Qualidade de Código)

**Progresso**: 12 de 14 seções concluídas ✅
