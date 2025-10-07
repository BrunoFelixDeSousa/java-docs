# 📁 **Estrutura de Pastas - Next.js 14+ (App Router)**

> **Guia completo de organização de projeto Next.js com Clean Architecture, Feature-Slice Design e TypeScript**

---

## 📋 **Índice**

1. [Visão Geral da Arquitetura](#visão-geral-da-arquitetura)
2. [Estrutura Completa](#estrutura-completa)
3. [Camadas da Aplicação](#camadas-da-aplicação)
4. [Convenções de Nomenclatura](#convenções-de-nomenclatura)
5. [Fluxo de Dados](#fluxo-de-dados)
6. [Exemplos Práticos](#exemplos-práticos)
7. [Boas Práticas](#boas-práticas)
8. [Configuração Inicial](#configuração-inicial)

---

## 🏗️ **Visão Geral da Arquitetura**

Esta estrutura combina os seguintes padrões arquiteturais:

```
┌─────────────────────────────────────────────────────────┐
│            PADRÕES ARQUITETURAIS APLICADOS               │
├─────────────────────────────────────────────────────────┤
│  ✅ Clean Architecture (Hexagonal/Onion)                │
│  ✅ Feature-Slice Design (Organização por Features)     │
│  ✅ MVC Adaptado (Model-View-Controller)                │
│  ✅ Domain-Driven Design (DDD)                          │
│  ✅ Component-Driven Development                        │
└─────────────────────────────────────────────────────────┘
```

### **Princípios Fundamentais**

- **Separation of Concerns**: Cada camada tem uma responsabilidade única
- **Dependency Inversion**: Dependências apontam para abstrações
- **Feature-Based Organization**: Código agrupado por funcionalidade
- **Server-First Architecture**: Aproveitar React Server Components
- **Type Safety**: TypeScript em toda aplicação


---

## 📂 **Estrutura Completa**

```
project-root/
├── app/                                    # ⚛️ Next.js App Router (Route-based)
│   │
│   ├── (auth)/                            # 🔐 Route Group - Autenticação
│   │   ├── login/
│   │   │   ├── page.tsx                   # View (Server Component)
│   │   │   ├── LoginController.tsx        # Controller (Client Component)
│   │   │   ├── LoginForm.tsx              # Form Component
│   │   │   ├── useLogin.ts                # Business Logic Hook
│   │   │   ├── actions.ts                 # Server Actions
│   │   │   ├── schema.ts                  # Zod Validation Schema
│   │   │   └── types.ts                   # TypeScript Types
│   │   │
│   │   ├── register/
│   │   │   ├── page.tsx
│   │   │   ├── RegisterController.tsx
│   │   │   ├── RegisterForm.tsx
│   │   │   ├── useRegister.ts
│   │   │   ├── actions.ts
│   │   │   ├── schema.ts
│   │   │   └── types.ts
│   │   │
│   │   ├── forgot-password/
│   │   │   ├── page.tsx
│   │   │   ├── ForgotPasswordController.tsx
│   │   │   ├── useResetPassword.ts
│   │   │   ├── actions.ts
│   │   │   └── types.ts
│   │   │
│   │   └── layout.tsx                     # Auth Layout (Logo, Background)
│   │
│   ├── (dashboard)/                       # 📊 Route Group - Área Administrativa
│   │   ├── dashboard/
│   │   │   ├── page.tsx
│   │   │   ├── DashboardController.tsx
│   │   │   ├── _components/               # Private components (prefix _)
│   │   │   │   ├── StatsCard.tsx
│   │   │   │   ├── RecentActivity.tsx
│   │   │   │   └── QuickActions.tsx
│   │   │   ├── useDashboard.ts
│   │   │   ├── actions.ts
│   │   │   └── types.ts
│   │   │
│   │   ├── produtos/
│   │   │   ├── page.tsx                   # Product List
│   │   │   ├── ProdutosController.tsx
│   │   │   ├── [id]/
│   │   │   │   ├── page.tsx               # Product Details
│   │   │   │   ├── ProdutoController.tsx
│   │   │   │   ├── useProduto.ts
│   │   │   │   ├── actions.ts
│   │   │   │   └── edit/
│   │   │   │       ├── page.tsx           # Edit Product
│   │   │   │       ├── EditProdutoController.tsx
│   │   │   │       ├── useEditProduto.ts
│   │   │   │       └── actions.ts
│   │   │   │
│   │   │   ├── novo/
│   │   │   │   ├── page.tsx               # Create Product
│   │   │   │   ├── NovoProdutoController.tsx
│   │   │   │   └── actions.ts
│   │   │   │
│   │   │   ├── _components/
│   │   │   │   ├── ProdutoCard.tsx
│   │   │   │   ├── ProdutoForm.tsx
│   │   │   │   ├── ProdutoFilters.tsx
│   │   │   │   └── ProdutoTable.tsx
│   │   │   │
│   │   │   ├── useProdutos.ts
│   │   │   ├── useProdutoFilters.ts
│   │   │   ├── actions.ts
│   │   │   ├── schema.ts
│   │   │   └── types.ts
│   │   │
│   │   ├── usuarios/
│   │   │   ├── page.tsx
│   │   │   ├── UsuariosController.tsx
│   │   │   ├── [id]/
│   │   │   │   ├── page.tsx
│   │   │   │   └── edit/
│   │   │   │       └── page.tsx
│   │   │   ├── _components/
│   │   │   │   ├── UserTable.tsx
│   │   │   │   └── UserForm.tsx
│   │   │   ├── useUsuarios.ts
│   │   │   ├── actions.ts
│   │   │   ├── schema.ts
│   │   │   └── types.ts
│   │   │
│   │   ├── configuracoes/
│   │   │   ├── page.tsx
│   │   │   ├── ConfiguracoesController.tsx
│   │   │   ├── geral/
│   │   │   │   └── page.tsx
│   │   │   ├── seguranca/
│   │   │   │   └── page.tsx
│   │   │   ├── notificacoes/
│   │   │   │   └── page.tsx
│   │   │   ├── actions.ts
│   │   │   └── types.ts
│   │   │
│   │   └── layout.tsx                     # Dashboard Layout (Sidebar, Header)
│   │
│   ├── (website)/                         # 🌐 Route Group - Website Público
│   │   ├── page.tsx                       # Homepage
│   │   ├── HomeController.tsx
│   │   ├── useHome.ts
│   │   ├── actions.ts
│   │   ├── types.ts
│   │   │
│   │   ├── sobre/
│   │   │   ├── page.tsx
│   │   │   ├── SobreController.tsx
│   │   │   ├── _components/
│   │   │   │   ├── TeamSection.tsx
│   │   │   │   ├── TimelineSection.tsx
│   │   │   │   └── ValuesSection.tsx
│   │   │   └── types.ts
│   │   │
│   │   ├── catalogo/
│   │   │   ├── page.tsx                   # Product Catalog
│   │   │   ├── CatalogoController.tsx
│   │   │   ├── [categoria]/
│   │   │   │   ├── page.tsx               # Category Page
│   │   │   │   ├── CategoriaController.tsx
│   │   │   │   └── [id]/
│   │   │   │       ├── page.tsx           # Product Details
│   │   │   │       ├── ProdutoController.tsx
│   │   │   │       └── useProduto.ts
│   │   │   │
│   │   │   ├── _components/
│   │   │   │   ├── ProductGrid.tsx
│   │   │   │   ├── CategoryFilter.tsx
│   │   │   │   ├── PriceFilter.tsx
│   │   │   │   └── SortOptions.tsx
│   │   │   │
│   │   │   ├── useCatalogo.ts
│   │   │   ├── useCatalogoFilters.ts
│   │   │   ├── actions.ts
│   │   │   └── types.ts
│   │   │
│   │   ├── carrinho/
│   │   │   ├── page.tsx
│   │   │   ├── CarrinhoController.tsx
│   │   │   ├── _components/
│   │   │   │   ├── CartItem.tsx
│   │   │   │   ├── CartSummary.tsx
│   │   │   │   └── CartEmpty.tsx
│   │   │   ├── useCarrinho.ts
│   │   │   ├── actions.ts
│   │   │   └── types.ts
│   │   │
│   │   ├── checkout/
│   │   │   ├── page.tsx
│   │   │   ├── CheckoutController.tsx
│   │   │   ├── _components/
│   │   │   │   ├── ShippingForm.tsx
│   │   │   │   ├── PaymentForm.tsx
│   │   │   │   └── OrderSummary.tsx
│   │   │   ├── useCheckout.ts
│   │   │   ├── actions.ts
│   │   │   ├── schema.ts
│   │   │   └── types.ts
│   │   │
│   │   ├── blog/
│   │   │   ├── page.tsx                   # Blog List
│   │   │   ├── BlogController.tsx
│   │   │   ├── [slug]/
│   │   │   │   ├── page.tsx               # Blog Post
│   │   │   │   └── PostController.tsx
│   │   │   ├── _components/
│   │   │   │   ├── BlogCard.tsx
│   │   │   │   └── BlogCategories.tsx
│   │   │   └── types.ts
│   │   │
│   │   ├── contato/
│   │   │   ├── page.tsx
│   │   │   ├── ContatoController.tsx
│   │   │   ├── ContactForm.tsx
│   │   │   ├── useContato.ts
│   │   │   ├── actions.ts
│   │   │   ├── schema.ts
│   │   │   └── types.ts
│   │   │
│   │   └── layout.tsx                     # Website Layout (Header, Footer)
│   │
│   ├── api/                               # 🔌 API Routes (REST/GraphQL)
│   │   ├── auth/
│   │   │   └── [...nextauth]/
│   │   │       └── route.ts               # NextAuth.js
│   │   │
│   │   ├── produtos/
│   │   │   ├── route.ts                   # GET, POST /api/produtos
│   │   │   └── [id]/
│   │   │       └── route.ts               # GET, PUT, DELETE /api/produtos/:id
│   │   │
│   │   ├── usuarios/
│   │   │   ├── route.ts
│   │   │   └── [id]/
│   │   │       └── route.ts
│   │   │
│   │   ├── upload/
│   │   │   └── route.ts                   # File upload endpoint
│   │   │
│   │   ├── search/
│   │   │   └── route.ts                   # Full-text search
│   │   │
│   │   ├── webhooks/
│   │   │   ├── stripe/
│   │   │   │   └── route.ts               # Stripe webhooks
│   │   │   └── mail/
│   │   │       └── route.ts               # Email webhooks
│   │   │
│   │   ├── revalidate/
│   │   │   └── route.ts                   # On-demand revalidation
│   │   │
│   │   └── trpc/
│   │       └── [trpc]/
│   │           └── route.ts               # tRPC endpoint (opcional)
│   │
│   ├── sitemap.ts                         # Sitemap generation
│   ├── robots.ts                          # Robots.txt
│   ├── manifest.ts                        # PWA Manifest
│   ├── layout.tsx                         # Root Layout
│   ├── providers.tsx                      # Client Providers (Context, Theme, etc)
│   ├── error.tsx                          # Root Error Boundary
│   ├── global-error.tsx                   # Global Error Handler
│   ├── loading.tsx                        # Root Loading UI
│   ├── not-found.tsx                      # 404 Page
│   └── globals.css                        # Global Styles
│
├── components/                            # ⚛️ Shared React Components
│   ├── ui/                                # 🎨 Base UI Components (shadcn/ui style)
│   │   ├── button.tsx
│   │   ├── input.tsx
│   │   ├── card.tsx
│   │   ├── dialog.tsx
│   │   ├── dropdown-menu.tsx
│   │   ├── select.tsx
│   │   ├── checkbox.tsx
│   │   ├── radio-group.tsx
│   │   ├── switch.tsx
│   │   ├── tabs.tsx
│   │   ├── toast.tsx
│   │   ├── tooltip.tsx
│   │   ├── accordion.tsx
│   │   ├── avatar.tsx
│   │   ├── badge.tsx
│   │   ├── calendar.tsx
│   │   ├── popover.tsx
│   │   ├── skeleton.tsx
│   │   └── ...
│   │
│   ├── layout/                            # 🏗️ Layout Components
│   │   ├── Header.tsx
│   │   ├── Footer.tsx
│   │   ├── Sidebar.tsx
│   │   ├── MobileMenu.tsx
│   │   ├── Container.tsx
│   │   ├── Section.tsx
│   │   └── Grid.tsx
│   │
│   ├── sections/                          # 📄 Page Sections (Marketing)
│   │   ├── HeroSection.tsx
│   │   ├── FeaturesSection.tsx
│   │   ├── TestimonialsSection.tsx
│   │   ├── PricingSection.tsx
│   │   ├── CTASection.tsx
│   │   ├── FAQSection.tsx
│   │   └── StatsSection.tsx
│   │
│   ├── forms/                             # 📝 Form Components
│   │   ├── FormField.tsx                  # Reusable form field wrapper
│   │   ├── FormError.tsx                  # Error message display
│   │   ├── FormLabel.tsx                  # Form label with required indicator
│   │   ├── SearchForm.tsx
│   │   ├── NewsletterForm.tsx
│   │   └── FilterForm.tsx
│   │
│   ├── cards/                             # 🃏 Card Components
│   │   ├── ProductCard.tsx
│   │   ├── BlogCard.tsx
│   │   ├── FeatureCard.tsx
│   │   ├── TestimonialCard.tsx
│   │   ├── PricingCard.tsx
│   │   └── StatCard.tsx
│   │
│   ├── shared/                            # 🔗 Shared Business Components
│   │   ├── LoadingSpinner.tsx
│   │   ├── ErrorMessage.tsx
│   │   ├── EmptyState.tsx
│   │   ├── Pagination.tsx
│   │   ├── Breadcrumb.tsx
│   │   ├── DataTable.tsx
│   │   ├── SearchBar.tsx
│   │   ├── SortDropdown.tsx
│   │   └── BackButton.tsx
│   │
│   ├── widgets/                           # 🔧 Special Widgets
│   │   ├── WhatsAppFloat.tsx
│   │   ├── CookieBanner.tsx
│   │   ├── NewsletterPopup.tsx
│   │   ├── LiveChat.tsx
│   │   └── BackToTop.tsx
│   │
│   └── providers/                         # 🌐 Client-side Providers
│       ├── ThemeProvider.tsx
│       ├── ToastProvider.tsx
│       └── ModalProvider.tsx
│
├── lib/                                   # 📚 Libraries & Utilities
│   ├── api/                               # 🔌 API Client Layer
│   │   ├── client.ts                      # Configured fetch/axios
│   │   ├── endpoints.ts                   # API endpoint URLs
│   │   ├── interceptors.ts                # Request/Response interceptors
│   │   └── types.ts                       # API response types
│   │
│   ├── db.ts                              # 🗄️ Database Client (Prisma)
│   ├── auth.ts                            # 🔐 Auth Configuration (NextAuth)
│   ├── utils.ts                           # 🛠️ Utility Functions (cn, etc)
│   │
│   ├── validations/                       # ✅ Validation Schemas (Zod)
│   │   ├── auth.schema.ts
│   │   ├── produto.schema.ts
│   │   ├── usuario.schema.ts
│   │   ├── contato.schema.ts
│   │   └── common.schema.ts               # Shared schemas
│   │
│   ├── services/                          # 🎯 Application Services
│   │   ├── auth.service.ts
│   │   ├── produto.service.ts
│   │   ├── usuario.service.ts
│   │   ├── email.service.ts
│   │   ├── payment.service.ts
│   │   ├── upload.service.ts
│   │   └── analytics.service.ts
│   │
│   ├── domain/                            # 🏛️ Domain Layer (DDD)
│   │   ├── entities/
│   │   │   ├── Produto.ts
│   │   │   ├── Usuario.ts
│   │   │   └── Pedido.ts
│   │   │
│   │   ├── value-objects/
│   │   │   ├── Email.ts
│   │   │   ├── CPF.ts
│   │   │   └── Preco.ts
│   │   │
│   │   ├── services/                      # Domain services
│   │   │   ├── ProdutoService.ts
│   │   │   └── PedidoService.ts
│   │   │
│   │   └── repositories/                  # Repository interfaces
│   │       ├── IProdutoRepository.ts
│   │       └── IUsuarioRepository.ts
│   │
│   ├── infrastructure/                    # 🔩 Infrastructure Layer
│   │   ├── repositories/                  # Repository implementations
│   │   │   ├── PrismaProdutoRepository.ts
│   │   │   └── PrismaUsuarioRepository.ts
│   │   │
│   │   ├── api/                           # External API integrations
│   │   │   ├── payment.api.ts             # Stripe
│   │   │   ├── shipping.api.ts            # Correios
│   │   │   └── email.api.ts               # Resend
│   │   │
│   │   └── cache/                         # Cache providers
│   │       ├── redis.ts
│   │       └── memory.ts
│   │
│   ├── use-cases/                         # 📋 Use Cases (Application Layer)
│   │   ├── produto/
│   │   │   ├── CreateProduto.ts
│   │   │   ├── UpdateProduto.ts
│   │   │   ├── DeleteProduto.ts
│   │   │   └── GetProdutos.ts
│   │   │
│   │   └── pedido/
│   │       ├── CreatePedido.ts
│   │       └── CancelPedido.ts
│   │
│   ├── utils/                             # 🛠️ Utility Functions
│   │   ├── format.ts                      # Date, currency, phone formatting
│   │   ├── validation.ts                  # Custom validators
│   │   ├── string.ts                      # String manipulation
│   │   ├── array.ts                       # Array helpers
│   │   ├── file.ts                        # File utilities
│   │   └── url.ts                         # URL utilities
│   │
│   ├── constants/                         # 📌 Application Constants
│   │   ├── routes.ts                      # Route paths
│   │   ├── api-urls.ts                    # API endpoints
│   │   ├── config.ts                      # App configuration
│   │   ├── messages.ts                    # System messages
│   │   └── regex.ts                       # Regex patterns
│   │
│   └── types/                             # 📝 Global TypeScript Types
│       ├── index.ts                       # Re-exports
│       ├── api.types.ts                   # API types
│       ├── database.types.ts              # Database types
│       ├── next-auth.d.ts                 # NextAuth type extensions
│       └── global.d.ts                    # Global declarations
│
├── hooks/                                 # 🪝 Global Custom Hooks
│   ├── useAuth.ts                         # Authentication hook
│   ├── useLocalStorage.ts                 # LocalStorage persistence
│   ├── useDebounce.ts                     # Debounce hook
│   ├── useMediaQuery.ts                   # Responsive breakpoints
│   ├── useIntersectionObserver.ts         # Intersection observer
│   ├── useFetch.ts                        # Generic fetch hook
│   ├── useToast.ts                        # Toast notifications
│   ├── useModal.ts                        # Modal state management
│   └── useScrollLock.ts                   # Scroll locking
│
├── contexts/                              # 🌐 Global React Contexts
│   ├── AuthContext.tsx
│   ├── CartContext.tsx
│   ├── ThemeContext.tsx
│   └── NotificationContext.tsx
│
├── middleware.ts                          # 🛡️ Next.js Middleware (Auth, i18n, etc)
│
├── public/                                # 📦 Static Assets
│   ├── images/
│   │   ├── logo.svg
│   │   ├── logo-white.svg
│   │   ├── hero/
│   │   ├── products/
│   │   ├── team/
│   │   └── icons/
│   │       ├── icon-192.png
│   │       └── icon-512.png
│   │
│   ├── fonts/                             # Custom fonts (if not using next/font)
│   │   └── custom-font.woff2
│   │
│   ├── locales/                           # i18n translation files
│   │   ├── pt-BR.json
│   │   └── en-US.json
│   │
│   ├── favicon.ico
│   ├── apple-touch-icon.png
│   └── robots.txt                         # (or use app/robots.ts)
│
├── styles/                                # 🎨 Styles
│   ├── globals.css                        # Global CSS (Tailwind imports)
│   ├── animations.css                     # Custom animations
│   └── prose.css                          # Typography styles
│
├── prisma/                                # 🗄️ Prisma ORM
│   ├── schema.prisma                      # Database schema
│   ├── migrations/                        # Migration history
│   └── seed.ts                            # Database seeding
│
├── tests/                                 # 🧪 Tests
│   ├── unit/                              # Unit tests (Vitest)
│   │   ├── components/
│   │   ├── hooks/
│   │   └── utils/
│   │
│   ├── integration/                       # Integration tests
│   │   ├── api/
│   │   └── database/
│   │
│   └── e2e/                               # E2E tests (Playwright)
│       ├── auth.spec.ts
│       ├── produtos.spec.ts
│       └── checkout.spec.ts
│
├── scripts/                               # 🔧 Utility Scripts
│   ├── seed.ts                            # Database seeding
│   ├── migrate.ts                         # Database migrations
│   └── generate-types.ts                  # Type generation
│
├── config/                                # ⚙️ Configuration Files
│   ├── next.config.mjs                    # Next.js configuration
│   ├── tailwind.config.ts                 # Tailwind CSS
│   ├── tsconfig.json                      # TypeScript
│   ├── .eslintrc.json                     # ESLint
│   ├── .prettierrc                        # Prettier
│   ├── vitest.config.ts                   # Vitest (unit tests)
│   └── playwright.config.ts               # Playwright (E2E tests)
│
├── .env.example                           # Environment variables template
├── .env.local                             # Local environment (gitignored)
├── .gitignore
├── package.json
├── README.md
└── FOLDER_STRUCTURE.md                    # This file
```

---

## 🏛️ **Camadas da Aplicação**

### **Diagrama de Arquitetura**

```
┌─────────────────────────────────────────────────────────┐
│                     PRESENTATION                         │
│  ┌─────────────────────────────────────────────────┐    │
│  │  UI Components, Pages, Layouts                   │    │
│  │  • app/**/page.tsx (Server Components)           │    │
│  │  • components/**/*.tsx                           │    │
│  └─────────────────────────────────────────────────┘    │
└────────────────────────┬────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────┐
│                    CONTROLLERS                           │
│  ┌─────────────────────────────────────────────────┐    │
│  │  Orquestração, Composição                        │    │
│  │  • app/**/*Controller.tsx (Client Components)    │    │
│  └─────────────────────────────────────────────────┘    │
└────────────────────────┬────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────┐
│                 BUSINESS LOGIC (Hooks)                   │
│  ┌─────────────────────────────────────────────────┐    │
│  │  Estado, Efeitos, Regras de Negócio              │    │
│  │  • hooks/**/*.ts                                 │    │
│  └─────────────────────────────────────────────────┘    │
└────────────────────────┬────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────┐
│                  APPLICATION LAYER                       │
│  ┌─────────────────────────────────────────────────┐    │
│  │  Use Cases, Services, Server Actions             │    │
│  │  • app/**/actions.ts                             │    │
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
│  └─────────────────────────────────────────────────┘    │
└────────────────────────┬────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────┐
│              INFRASTRUCTURE LAYER                        │
│  ┌─────────────────────────────────────────────────┐    │
│  │  Database, APIs, External Services               │    │
│  │  • lib/db.ts (Prisma)                            │    │
│  │  • lib/infrastructure/**/*.ts                    │    │
│  └─────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────┘
```

### **1. Presentation Layer** (UI)
**Localização**: `app/**/page.tsx`, `components/**/*.tsx`

**Responsabilidades**:
- ✅ Renderização de UI (Server Components por padrão)
- ✅ Captura de eventos do usuário
- ✅ Exibição de dados formatados
- ❌ **NÃO** contém lógica de negócio
- ❌ **NÃO** acessa diretamente API/Database

**Exemplo**:
```tsx
// app/produtos/page.tsx (Server Component)
import { ProdutosController } from './ProdutosController';

export default function ProdutosPage() {
  return <ProdutosController />;
}
```

### **2. Controller Layer**
**Localização**: `app/**/*Controller.tsx`

**Responsabilidades**:
- ✅ Orquestrar fluxo de dados
- ✅ Conectar View com Hooks/Actions
- ✅ Gerenciar estado da página
- ✅ Coordenar múltiplos hooks
- ✅ Error boundaries e loading states

**Exemplo**:
```tsx
// app/produtos/ProdutosController.tsx
'use client';

import { useProdutos } from './useProdutos';
import { ProdutosView } from './ProdutosView';

export function ProdutosController() {
  const { produtos, loading, error } = useProdutos();
  
  if (loading) return <LoadingSpinner />;
  if (error) return <ErrorMessage error={error} />;
  
  return <ProdutosView produtos={produtos} />;
}
```

### **3. Business Logic Layer** (Hooks)
**Localização**: `hooks/**/*.ts`, `app/**/use*.ts`

**Responsabilidades**:
- ✅ Gerenciamento de estado local
- ✅ Lógica de negócio do cliente
- ✅ Efeitos colaterais (useEffect)
- ✅ Reutilizável entre componentes

**Exemplo**:
```tsx
// app/produtos/useProdutos.ts
'use client';

import { useState, useEffect } from 'react';
import { getProdutos } from './actions';

export function useProdutos() {
  const [produtos, setProdutos] = useState([]);
  const [loading, setLoading] = useState(true);
  
  useEffect(() => {
    getProdutos().then(setProdutos).finally(() => setLoading(false));
  }, []);
  
  return { produtos, loading };
}
```

### **4. Application Layer**
**Localização**: `app/**/actions.ts`, `lib/services/**/*.ts`, `lib/use-cases/**/*.ts`

**Responsabilidades**:
- ✅ Server Actions (Next.js)
- ✅ Casos de uso da aplicação
- ✅ Orquestração de serviços
- ✅ Validação de dados (Zod)

**Exemplo**:
```tsx
// app/produtos/actions.ts
'use server';

import { revalidatePath } from 'next/cache';
import { db } from '@/lib/db';
import { produtoSchema } from './schema';

export async function createProduto(data: FormData) {
  const validated = produtoSchema.parse(Object.fromEntries(data));
  
  const produto = await db.produto.create({ data: validated });
  
  revalidatePath('/produtos');
  return produto;
}
```

### **5. Domain Layer**
**Localização**: `lib/domain/**/*.ts`

**Responsabilidades**:
- ✅ Entidades de negócio
- ✅ Value Objects
- ✅ Regras de domínio
- ✅ Interfaces de repositórios

**Exemplo**:
```tsx
// lib/domain/entities/Produto.ts
export class Produto {
  constructor(
    public id: string,
    public nome: string,
    public preco: number,
    public estoque: number
  ) {
    this.validar();
  }
  
  private validar() {
    if (this.preco < 0) throw new Error('Preço inválido');
    if (this.estoque < 0) throw new Error('Estoque inválido');
  }
  
  estaDisponivel(): boolean {
    return this.estoque > 0;
  }
}
```

### **6. Infrastructure Layer**
**Localização**: `lib/db.ts`, `lib/infrastructure/**/*.ts`

**Responsabilidades**:
- ✅ Acesso ao banco de dados (Prisma)
- ✅ Integrações com APIs externas
- ✅ Cache (Redis)
- ✅ Implementações de repositórios

**Exemplo**:
```tsx
// lib/db.ts
import { PrismaClient } from '@prisma/client';

const globalForPrisma = globalThis as unknown as {
  prisma: PrismaClient | undefined;
};

export const db = globalForPrisma.prisma ?? new PrismaClient();

if (process.env.NODE_ENV !== 'production') globalForPrisma.prisma = db;
```

---

## 📋 **Convenções de Nomenclatura**

### **Arquivos**

```
✅ Componentes React
   - PascalCase: ProductCard.tsx, UserProfile.tsx
   - Sufixo descritivo: LoginForm.tsx, ProductList.tsx

✅ Hooks Customizados
   - camelCase com prefixo 'use': useAuth.ts, useProdutos.ts
   - Específico da feature: app/produtos/useProdutos.ts

✅ Server Actions
   - actions.ts dentro de cada feature
   - Funções export: createProduto, updateProduto, deleteProduto

✅ Types e Schemas
   - PascalCase com sufixo: produto.types.ts, produto.schema.ts
   - Interfaces/Types: PascalCase (User, Product, OrderDTO)

✅ Utilities
   - camelCase: formatDate.ts, validateEmail.ts
   - Agrupados por função: format.ts, validation.ts

✅ Constants
   - camelCase para arquivo: routes.ts, config.ts
   - UPPER_SNAKE_CASE para valores: API_URL, MAX_RETRIES

✅ Pages (Next.js)
   - kebab-case para rotas: page.tsx, layout.tsx, loading.tsx
   - Rotas dinâmicas: [id]/page.tsx, [...slug]/page.tsx
```

### **Pastas**

```
✅ Route Groups (Next.js App Router)
   - Entre parênteses: (auth), (dashboard), (website)
   - Não afetam URL: app/(auth)/login → /login

✅ Dynamic Routes
   - Colchetes: [id], [slug], [categoria]
   - Catch-all: [...slug], [[...slug]]

✅ Private Components
   - Prefixo underscore: _components/, _lib/
   - Não geram rotas: app/produtos/_components/

✅ Pastas de Código
   - camelCase ou kebab-case consistente
   - Plural para coleções: components/, hooks/, services/
```

### **Variáveis e Funções**

```typescript
// ✅ BOM
const userName = 'João';
const productList = [];
function calculateTotal() {}
const handleSubmit = () => {};

// ❌ EVITAR
const user_name = 'João';  // snake_case
const ProductList = [];     // PascalCase para variáveis
function CalculateTotal() {} // PascalCase para funções
```

### **Componentes React**

```tsx
// ✅ BOM: PascalCase
export function ProductCard({ product }: ProductCardProps) {}
export default function LoginPage() {}

// ❌ EVITAR: camelCase
export function productCard() {}
export default function loginPage() {}
```

---

## 🔄 **Fluxo de Dados**

### **Fluxo Unidirecional (Top-Down)**

```
┌─────────────────────────────────────────────────────────┐
│                  FLUXO DE DADOS COMPLETO                 │
└─────────────────────────────────────────────────────────┘

1. USER INTERACTION
   │
   ├─► Click em botão
   ├─► Submit de formulário
   └─► Input de texto
        │
        ▼
2. VIEW (page.tsx ou Component.tsx)
   │
   ├─► Captura evento
   └─► Chama handler do Controller
        │
        ▼
3. CONTROLLER (Controller.tsx)
   │
   ├─► Orquestra lógica
   ├─► Chama hooks
   └─► Dispara ações
        │
        ▼
4. HOOK (useFeature.ts)
   │
   ├─► Gerencia estado
   ├─► Aplica lógica de negócio
   └─► Chama Server Action
        │
        ▼
5. SERVER ACTION (actions.ts)
   │
   ├─► Valida dados (Zod)
   ├─► Chama service/repository
   └─► Persiste no banco
        │
        ▼
6. SERVICE/REPOSITORY
   │
   ├─► Acessa Database (Prisma)
   └─► Retorna resultado
        │
        ▼
7. REVALIDATION (Next.js Cache)
   │
   ├─► revalidatePath('/produtos')
   └─► revalidateTag('produtos')
        │
        ▼
8. RE-RENDER
   │
   ├─► React atualiza UI
   └─► Usuário vê mudança
```

### **Exemplo Completo de Fluxo**

```tsx
// ═══════════════════════════════════════════════════════════
// 1. VIEW (page.tsx) - Server Component
// ═══════════════════════════════════════════════════════════
// app/produtos/page.tsx
import { ProdutosController } from './ProdutosController';

export default function ProdutosPage() {
  return <ProdutosController />;
}

// ═══════════════════════════════════════════════════════════
// 2. CONTROLLER - Client Component
// ═══════════════════════════════════════════════════════════
// app/produtos/ProdutosController.tsx
'use client';

import { useProdutos } from './useProdutos';
import { ProdutosList } from './_components/ProdutosList';

export function ProdutosController() {
  const { produtos, loading, error, handleDelete } = useProdutos();
  
  if (loading) return <LoadingSpinner />;
  if (error) return <ErrorMessage error={error} />;
  
  return (
    <ProdutosList 
      produtos={produtos} 
      onDelete={handleDelete} 
    />
  );
}

// ═══════════════════════════════════════════════════════════
// 3. HOOK - Business Logic
// ═══════════════════════════════════════════════════════════
// app/produtos/useProdutos.ts
'use client';

import { useState, useEffect, useCallback } from 'react';
import { getProdutos, deleteProduto } from './actions';
import { toast } from 'sonner';

export function useProdutos() {
  const [produtos, setProdutos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  
  useEffect(() => {
    getProdutos()
      .then(setProdutos)
      .catch(setError)
      .finally(() => setLoading(false));
  }, []);
  
  const handleDelete = useCallback(async (id: string) => {
    try {
      await deleteProduto(id);
      setProdutos(prev => prev.filter(p => p.id !== id));
      toast.success('Produto deletado!');
    } catch (error) {
      toast.error('Erro ao deletar produto');
    }
  }, []);
  
  return { produtos, loading, error, handleDelete };
}

// ═══════════════════════════════════════════════════════════
// 4. SERVER ACTION - Application Layer
// ═══════════════════════════════════════════════════════════
// app/produtos/actions.ts
'use server';

import { revalidatePath } from 'next/cache';
import { db } from '@/lib/db';
import { produtoSchema } from './schema';

export async function getProdutos() {
  const produtos = await db.produto.findMany({
    orderBy: { createdAt: 'desc' }
  });
  return produtos;
}

export async function createProduto(data: FormData) {
  const validated = produtoSchema.parse(Object.fromEntries(data));
  
  const produto = await db.produto.create({
    data: validated
  });
  
  revalidatePath('/produtos');
  return produto;
}

export async function deleteProduto(id: string) {
  await db.produto.delete({ where: { id } });
  revalidatePath('/produtos');
}

// ═══════════════════════════════════════════════════════════
// 5. SCHEMA - Validation
// ═══════════════════════════════════════════════════════════
// app/produtos/schema.ts
import { z } from 'zod';

export const produtoSchema = z.object({
  nome: z.string().min(3, 'Nome deve ter no mínimo 3 caracteres'),
  descricao: z.string().optional(),
  preco: z.number().positive('Preço deve ser positivo'),
  estoque: z.number().int().min(0, 'Estoque não pode ser negativo'),
  categoriaId: z.string().uuid()
});

// ═══════════════════════════════════════════════════════════
// 6. TYPES - TypeScript
// ═══════════════════════════════════════════════════════════
// app/produtos/types.ts
export interface Produto {
  id: string;
  nome: string;
  descricao?: string;
  preco: number;
  estoque: number;
  categoriaId: string;
  createdAt: Date;
  updatedAt: Date;
}

export interface CreateProdutoDTO {
  nome: string;
  descricao?: string;
  preco: number;
  estoque: number;
  categoriaId: string;
}
```

---

## 💡 **Exemplos Práticos**

### **1. Criar Nova Feature Completa**

```bash
# Estrutura mínima para uma nova feature
mkdir -p app/minha-feature/_components

# Criar arquivos essenciais
touch app/minha-feature/page.tsx
touch app/minha-feature/MinhaFeatureController.tsx
touch app/minha-feature/useMinhaFeature.ts
touch app/minha-feature/actions.ts
touch app/minha-feature/schema.ts
touch app/minha-feature/types.ts
```

**Implementação Base**:

```tsx
// ═══════════════════════════════════════════════════════════
// types.ts
// ═══════════════════════════════════════════════════════════
export interface MinhaFeatureData {
  id: string;
  nome: string;
  descricao: string;
  createdAt: Date;
}

export interface CreateMinhaFeatureDTO {
  nome: string;
  descricao: string;
}

// ═══════════════════════════════════════════════════════════
// schema.ts
// ═══════════════════════════════════════════════════════════
import { z } from 'zod';

export const minhaFeatureSchema = z.object({
  nome: z.string().min(3),
  descricao: z.string().min(10)
});

// ═══════════════════════════════════════════════════════════
// actions.ts
// ═══════════════════════════════════════════════════════════
'use server';

import { revalidatePath } from 'next/cache';
import { db } from '@/lib/db';
import { minhaFeatureSchema } from './schema';

export async function getItems() {
  return await db.minhaFeature.findMany();
}

export async function createItem(data: FormData) {
  const validated = minhaFeatureSchema.parse(Object.fromEntries(data));
  const item = await db.minhaFeature.create({ data: validated });
  revalidatePath('/minha-feature');
  return item;
}

// ═══════════════════════════════════════════════════════════
// useMinhaFeature.ts
// ═══════════════════════════════════════════════════════════
'use client';

import { useState, useEffect } from 'react';
import { getItems } from './actions';
import type { MinhaFeatureData } from './types';

export function useMinhaFeature() {
  const [data, setData] = useState<MinhaFeatureData[]>([]);
  const [loading, setLoading] = useState(true);
  
  useEffect(() => {
    getItems()
      .then(setData)
      .finally(() => setLoading(false));
  }, []);
  
  return { data, loading };
}

// ═══════════════════════════════════════════════════════════
// MinhaFeatureController.tsx
// ═══════════════════════════════════════════════════════════
'use client';

import { useMinhaFeature } from './useMinhaFeature';
import { MinhaFeatureView } from './_components/MinhaFeatureView';

export function MinhaFeatureController() {
  const { data, loading } = useMinhaFeature();
  
  if (loading) return <div>Carregando...</div>;
  
  return <MinhaFeatureView data={data} />;
}

// ═══════════════════════════════════════════════════════════
// page.tsx
// ═══════════════════════════════════════════════════════════
import { MinhaFeatureController } from './MinhaFeatureController';

export default function MinhaFeaturePage() {
  return (
    <div className="container mx-auto py-8">
      <h1 className="text-3xl font-bold mb-6">Minha Feature</h1>
      <MinhaFeatureController />
    </div>
  );
}
```

### **2. Componente Reutilizável com TypeScript**

```tsx
// ═══════════════════════════════════════════════════════════
// components/ui/Button.tsx
// ═══════════════════════════════════════════════════════════
import { ButtonHTMLAttributes, forwardRef } from 'react';
import { cva, type VariantProps } from 'class-variance-authority';
import { cn } from '@/lib/utils';

const buttonVariants = cva(
  'inline-flex items-center justify-center rounded-md font-medium transition-colors',
  {
    variants: {
      variant: {
        default: 'bg-primary text-white hover:bg-primary/90',
        destructive: 'bg-red-500 text-white hover:bg-red-600',
        outline: 'border border-input bg-background hover:bg-accent',
        ghost: 'hover:bg-accent hover:text-accent-foreground',
        link: 'text-primary underline-offset-4 hover:underline',
      },
      size: {
        default: 'h-10 px-4 py-2',
        sm: 'h-9 rounded-md px-3',
        lg: 'h-11 rounded-md px-8',
        icon: 'h-10 w-10',
      },
    },
    defaultVariants: {
      variant: 'default',
      size: 'default',
    },
  }
);

export interface ButtonProps
  extends ButtonHTMLAttributes<HTMLButtonElement>,
    VariantProps<typeof buttonVariants> {
  loading?: boolean;
}

export const Button = forwardRef<HTMLButtonElement, ButtonProps>(
  ({ className, variant, size, loading, children, ...props }, ref) => {
    return (
      <button
        className={cn(buttonVariants({ variant, size, className }))}
        ref={ref}
        disabled={loading || props.disabled}
        {...props}
      >
        {loading ? 'Carregando...' : children}
      </button>
    );
  }
);

Button.displayName = 'Button';
```

### **3. Custom Hook com TypeScript**

```tsx
// ═══════════════════════════════════════════════════════════
// hooks/useLocalStorage.ts
// ═══════════════════════════════════════════════════════════
import { useState, useEffect } from 'react';

export function useLocalStorage<T>(
  key: string,
  initialValue: T
): [T, (value: T | ((val: T) => T)) => void] {
  // State para armazenar o valor
  const [storedValue, setStoredValue] = useState<T>(() => {
    if (typeof window === 'undefined') {
      return initialValue;
    }
    
    try {
      const item = window.localStorage.getItem(key);
      return item ? JSON.parse(item) : initialValue;
    } catch (error) {
      console.error(error);
      return initialValue;
    }
  });

  // Retornar uma versão envolvida da função setState
  const setValue = (value: T | ((val: T) => T)) => {
    try {
      const valueToStore =
        value instanceof Function ? value(storedValue) : value;
      
      setStoredValue(valueToStore);
      
      if (typeof window !== 'undefined') {
        window.localStorage.setItem(key, JSON.stringify(valueToStore));
      }
    } catch (error) {
      console.error(error);
    }
  };

  return [storedValue, setValue];
}

// Uso:
// const [theme, setTheme] = useLocalStorage('theme', 'light');
```

---

## 🚀 **Boas Práticas**

### **1. Componentes**

```tsx
// ✅ BOM
// - Um componente por arquivo
// - Props tipadas
// - Componentes pequenos (<200 linhas)
// - Reutilizável

interface ProductCardProps {
  produto: Produto;
  onAddToCart: (id: string) => void;
}

export function ProductCard({ produto, onAddToCart }: ProductCardProps) {
  return (
    <div className="card">
      <h3>{produto.nome}</h3>
      <p>R$ {produto.preco.toFixed(2)}</p>
      <button onClick={() => onAddToCart(produto.id)}>
        Adicionar ao Carrinho
      </button>
    </div>
  );
}

// ❌ EVITAR
// - Múltiplos componentes no mesmo arquivo
// - Props sem tipos (any)
// - Componentes gigantes (>500 linhas)
// - Lógica complexa misturada com UI
```

### **2. Hooks**

```tsx
// ✅ BOM
// - Prefixo 'use'
// - Uma responsabilidade
// - Retornar objeto nomeado
// - Documentar dependências

export function useProdutos() {
  const [produtos, setProdutos] = useState<Produto[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<Error | null>(null);
  
  useEffect(() => {
    getProdutos()
      .then(setProdutos)
      .catch(setError)
      .finally(() => setLoading(false));
  }, []); // ✅ Dependências claras
  
  return { produtos, loading, error }; // ✅ Objeto nomeado
}

// ❌ EVITAR
export function produtosHook() { // ❌ Sem prefixo 'use'
  // ... código
  return [produtos, loading]; // ❌ Array não nomeado
}
```

### **3. Server Actions**

```tsx
// ✅ BOM
// - Sempre validar inputs
// - Usar revalidatePath/revalidateTag
// - Tratar erros adequadamente
// - Retornar tipos consistentes

'use server';

import { revalidatePath } from 'next/cache';
import { produtoSchema } from './schema';

export async function createProduto(data: FormData) {
  try {
    // ✅ Validação
    const validated = produtoSchema.parse(Object.fromEntries(data));
    
    // ✅ Operação
    const produto = await db.produto.create({ data: validated });
    
    // ✅ Revalidação
    revalidatePath('/produtos');
    
    // ✅ Retorno consistente
    return { success: true, data: produto };
  } catch (error) {
    // ✅ Tratamento de erro
    return { success: false, error: error.message };
  }
}

// ❌ EVITAR
export async function createProduto(data: any) { // ❌ Sem validação
  const produto = await db.produto.create({ data }); // ❌ Sem try-catch
  // ❌ Sem revalidation
  return produto; // ❌ Retorno inconsistente
}
```

### **4. Tipos**

```tsx
// ✅ BOM
// - Exportar todos os tipos
// - Usar interfaces para objetos
// - Usar types para unions/intersections
// - Evitar 'any'

export interface Produto {
  id: string;
  nome: string;
  preco: number;
}

export type ProdutoStatus = 'ativo' | 'inativo' | 'esgotado';

export type ProdutoComCategoria = Produto & {
  categoria: Categoria;
};

// ❌ EVITAR
interface Produto { // ❌ Não exportado
  data: any; // ❌ Usar 'any'
}
```

### **5. Organização**

```
✅ BOAS PRÁTICAS

1. Agrupar por feature, não por tipo
   ✅ app/produtos/
   ❌ app/pages/produtos/, app/hooks/produtos/

2. Manter arquivos pequenos (<300 linhas)
   ✅ Dividir em múltiplos arquivos
   ❌ Arquivo monolítico de 1000+ linhas

3. Colocar testes próximos ao código
   ✅ app/produtos/__tests__/
   ❌ tests/produtos/ (longe do código)

4. Documentar código complexo
   ✅ /** JSDoc para funções públicas */
   ❌ Código sem comentários

5. Prefixo _ para privado
   ✅ _components/ (não gera rota)
   ❌ components/ (pode gerar rota indesejada)
```

---

## ⚙️ **Configuração Inicial**

### **1. Clonar e Instalar**

```bash
# Clone o repositório
git clone <repo-url>
cd project-name

# Instale as dependências
npm install
# ou
pnpm install
# ou
yarn install
```

### **2. Configurar Variáveis de Ambiente**

```bash
# Copie o arquivo de exemplo
cp .env.example .env.local

# Edite com suas credenciais
nano .env.local
```

**Exemplo de `.env.local`**:
```env
# Database
DATABASE_URL="postgresql://user:password@localhost:5432/mydb"

# Auth
NEXTAUTH_URL="http://localhost:3000"
NEXTAUTH_SECRET="your-secret-key"

# External APIs
STRIPE_SECRET_KEY="sk_test_..."
STRIPE_WEBHOOK_SECRET="whsec_..."

# Email
RESEND_API_KEY="re_..."

# Analytics
NEXT_PUBLIC_GA_ID="G-..."
```

### **3. Configurar Database**

```bash
# Gerar cliente Prisma
npx prisma generate

# Executar migrações
npx prisma migrate dev

# Seed do banco (opcional)
npx prisma db seed
```

### **4. Executar em Desenvolvimento**

```bash
# Iniciar servidor de desenvolvimento
npm run dev

# Acesse http://localhost:3000
```

### **5. Build para Produção**

```bash
# Build otimizado
npm run build

# Rodar em produção
npm start
```

---

## 📚 **Recursos Adicionais**

### **Documentação Oficial**
- [Next.js 14+ Documentation](https://nextjs.org/docs)
- [React Documentation](https://react.dev)
- [TypeScript Documentation](https://www.typescriptlang.org/docs)
- [Prisma Documentation](https://www.prisma.io/docs)
- [Zod Documentation](https://zod.dev)

### **Padrões Arquiteturais**
- [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Feature-Slice Design](https://feature-sliced.design)
- [Domain-Driven Design](https://martinfowler.com/bliki/DomainDrivenDesign.html)
- [Component-Driven Development](https://www.componentdriven.org)

### **Ferramentas Recomendadas**
- **UI Components**: [shadcn/ui](https://ui.shadcn.com), [Radix UI](https://www.radix-ui.com)
- **Forms**: [React Hook Form](https://react-hook-form.com), [Zod](https://zod.dev)
- **State Management**: [Zustand](https://zustand-demo.pmnd.rs), [Jotai](https://jotai.org)
- **Data Fetching**: [TanStack Query](https://tanstack.com/query)
- **Styling**: [Tailwind CSS](https://tailwindcss.com)
- **Testing**: [Vitest](https://vitest.dev), [Testing Library](https://testing-library.com), [Playwright](https://playwright.dev)

---

## 🎯 **Checklist de Qualidade**

Antes de fazer deploy, verifique:

```
✅ TypeScript
   □ Sem erros de tipo
   □ Sem uso de 'any'
   □ Todos os tipos exportados

✅ Testes
   □ Unit tests para hooks
   □ Integration tests para actions
   □ E2E tests para fluxos críticos
   □ Coverage > 80%

✅ Performance
   □ Lighthouse score > 90
   □ Bundle size < 200KB (first load)
   □ Images otimizadas
   □ Code splitting aplicado

✅ SEO
   □ Metadata em todas as páginas
   □ Sitemap.xml gerado
   □ Robots.txt configurado
   □ Open Graph tags

✅ Acessibilidade
   □ Semantic HTML
   □ ARIA labels
   □ Keyboard navigation
   □ Screen reader friendly

✅ Segurança
   □ Env variables não commitadas
   □ HTTPS habilitado
   □ CSRF protection
   □ XSS prevention
   □ SQL injection prevention

✅ Code Quality
   □ ESLint sem erros
   □ Prettier formatado
   □ Sem console.logs
   □ Código documentado
```

---

## 🤝 **Contribuindo**

Para contribuir com melhorias nesta estrutura:

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

---

## 📄 **Licença**

Este template é open-source e disponível sob a licença MIT.

---

## 📞 **Suporte**

- **Documentação**: Este arquivo
- **Issues**: GitHub Issues
- **Discussões**: GitHub Discussions
- **Email**: seu-email@example.com

---

**Última atualização**: Dezembro 2024  
**Versão**: 2.0.0  
**Compatível com**: Next.js 14+, React 18+, TypeScript 5+
