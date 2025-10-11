# ⚛️ React & Next.js - Documentação Completa

Documentação abrangente de **React 18+** e **Next.js 14+** em português, cobrindo desde fundamentos até arquitetura avançada.

---

## 📊 Visão Geral

| Categoria | Arquivos | Linhas Totais | Status |
|-----------|----------|---------------|--------|
| **Fundamentos** | 3 arquivos | ~8,000 linhas | ✅ 100% |
| **Avançado** | 8 arquivos | ~25,000 linhas | ✅ 100% |
| **Arquitetura** | 2 arquivos | ~17,000 linhas | 🔄 86% |
| **TOTAL** | **13 arquivos** | **~50,000 linhas** | 🚀 |

---

## 📋 Índice

- [Visão Geral](#-visão-geral)
- [🚀 Início Rápido](#-início-rápido)
- [🗺️ Guia de Estudo - Ordem Recomendada](#-guia-de-estudo---ordem-recomendada)
  - [Fase 1: Fundamentos React](#-fase-1-fundamentos-react-semana-1-4)
  - [Fase 2: TypeScript e Ferramentas](#-fase-2-typescript-e-ferramentas-semana-5-8)
  - [Fase 3: State Management](#-fase-3-state-management-semana-9-12)
  - [Fase 4: Patterns e Performance](#-fase-4-patterns-e-performance-semana-13-16)
  - [Fase 5: Next.js](#-fase-5-nextjs-semana-17-20)
  - [Fase 6: Arquitetura Avançada](#-fase-6-arquitetura-avançada-semana-21-24)
- [🗺️ Trilhas Alternativas](#-trilhas-alternativas-de-estudo)
- [📊 Matriz de Pré-requisitos](#-matriz-de-pré-requisitos)
- [⏱️ Estimativa de Tempo](#️-estimativa-de-tempo-por-documento)
- [🎯 Checklist de Progresso](#-checklist-de-progresso)
- [💡 Dicas de Estudo](#-dicas-de-estudo)
- [Documentação por Categoria](#-documentação-por-categoria)
  - [Fundamentos](#-fundamentos)
  - [Avançado](#-avançado)
  - [Arquitetura](#️-arquitetura)
- [Recursos Adicionais](#-recursos-adicionais)

---

## 🚀 Início Rápido

**Novo no React?** Comece aqui:

1. 📖 Leia **[Por que React?](#-visão-geral)**
2. 🗺️ Escolha sua **[Trilha de Estudo](#-guia-de-estudo---ordem-recomendada)**
3. ✅ Marque seu progresso no **[Checklist](#-checklist-de-progresso)**
4. 🎯 Siga a **[Ordem Recomendada](#-fase-1-fundamentos-react-semana-1-4)**

---

## 🗺️ Mapa de Aprendizado

```
┌────────────────────────────────────────────────────────────────┐
│                   JORNADA REACT & NEXT.JS                      │
│                     (24+ semanas)                              │
└────────────────────────────────────────────────────────────────┘

    ⚛️ FASE 1: FUNDAMENTOS REACT (Semana 1-4)
    ┌──────────────────────────────────┐
    │  ├─ react-fundamentos.md         │
    │  └─ react-hooks.md ⭐            │
    └──────────────────────────────────┘
              │
              ▼
    📘 FASE 2: TYPESCRIPT (Semana 5-8)
    ┌──────────────────────────────────┐
    │  ├─ react-typescript.md ⭐       │
    │  └─ react-forms.md               │
    └──────────────────────────────────┘
              │
              ▼
    🔄 FASE 3: STATE MANAGEMENT (Semana 9-12)
    ┌──────────────────────────────────┐
    │  ├─ react-state-management.md ⭐ │
    │  └─ react-router.md              │
    └──────────────────────────────────┘
              │
              ▼
    ⚡ FASE 4: PATTERNS (Semana 13-16)
    ┌──────────────────────────────────┐
    │  ├─ react-patterns.md ⭐         │
    │  ├─ react-performance.md         │
    │  └─ react-suspense.md            │
    └──────────────────────────────────┘
              │
              ▼
    🚀 FASE 5: NEXT.JS (Semana 17-20)
    ┌──────────────────────────────────┐
    │  └─ next-js.md ⭐⭐              │
    │     (App Router, RSC, Actions)   │
    └──────────────────────────────────┘
              │
              ▼
    🏗️ FASE 6: ARQUITETURA (Semana 21-24)
    ┌──────────────────────────────────┐
    │  ├─ ARCHITECTURE.md ⭐⭐         │
    │  └─ FOLDER_STRUCTURE.md          │
    └──────────────────────────────────┘
              │
              ▼
    🏆 PROJETO FINAL: Aplicação Full-Stack
    ┌──────────────────────────────────────────────────────┐
    │  ✅ Next.js 14+ App Router                           │
    │  ✅ Server/Client Components + RSC                   │
    │  ✅ TypeScript + Zod Validation                      │
    │  ✅ TanStack Query + Zustand                         │
    │  ✅ React Hook Form + UI Components                  │
    │  ✅ Clean Architecture + Feature-Based               │
    │  ✅ Testing (Jest + Testing Library + Playwright)    │
    │  ✅ CI/CD + Vercel Deploy                            │
    └──────────────────────────────────────────────────────┘

⭐ = Documentos mais complexos (reserve mais tempo)
⭐⭐ = Documentos muito complexos (reserve muito mais tempo)
```

---

## 🗺️ Guia de Estudo - Ordem Recomendada

### ⚛️ **FASE 1: Fundamentos React** (Semana 1-4)

> **Objetivo**: Dominar os conceitos básicos do React e criar componentes funcionais

#### **Semana 1-2: React Core**
1. **[react-fundamentos.md](./fundamentals/react-fundamentos.md)** (~2,500 linhas)
   - JSX e Virtual DOM
   - Components (Function & Class)
   - Props e Children
   - State e Event Handling
   - Conditional Rendering
   - Lists & Keys
   
**Projeto Prático**: Todo List com add/remove/filter

#### **Semana 3-4: Hooks Essenciais**
2. **[react-hooks.md](./fundamentals/react-hooks.md)** (~3,000 linhas)
   - useState, useEffect
   - useContext, useReducer
   - useCallback, useMemo
   - useRef
   - Custom Hooks
   
**Projeto Prático**: Dashboard com fetch de API, filtros e cache local

---

### 📘 **FASE 2: TypeScript e Ferramentas** (Semana 5-8)

> **Objetivo**: Type safety e formulários profissionais

#### **Semana 5-6: TypeScript**
3. **[react-typescript.md](./fundamentals/react-typescript.md)** (~3,100 linhas)
   - Props typing
   - Hooks typing
   - Event handlers
   - Generic components
   - Utility types
   
**Projeto Prático**: Refatorar projetos anteriores para TypeScript

#### **Semana 7-8: Forms**
4. **[react-forms.md](./advanced/react-forms.md)** (~2,900 linhas)
   - React Hook Form
   - Zod validation
   - Field Arrays
   - File uploads
   
**Projeto Prático**: Multi-step form com validação complexa

---

### 🔄 **FASE 3: State Management** (Semana 9-12)

> **Objetivo**: Gerenciar estado global e navegação

#### **Semana 9-10: State Management**
5. **[react-state-management.md](./advanced/react-state-management.md)** (~2,800 linhas)
   - Context API
   - Zustand
   - Jotai
   - Redux Toolkit
   - TanStack Query
   
**Projeto Prático**: E-commerce com carrinho (Zustand + TanStack Query)

#### **Semana 11-12: Routing**
6. **[react-router.md](./advanced/react-router.md)** (~2,400 linhas)
   - React Router v6
   - Nested routes
   - Protected routes
   - Lazy loading
   
**Projeto Prático**: Multi-page app com auth e rotas protegidas

---

### ⚡ **FASE 4: Patterns e Performance** (Semana 13-16)

> **Objetivo**: Código escalável e otimizado

#### **Semana 13-14: Design Patterns**
7. **[react-patterns.md](./advanced/react-patterns.md)** (~3,200 linhas)
   - Compound Components
   - Render Props
   - HOCs
   - Custom Hooks Pattern
   - Provider Pattern
   
**Projeto Prático**: Component library (Tabs, Modal, Dropdown)

#### **Semana 15: Performance**
8. **[react-performance.md](./advanced/react-performance.md)** (~2,600 linhas)
   - React.memo
   - useMemo, useCallback
   - Code splitting
   - Virtualization
   - Web Workers
   
**Projeto Prático**: Otimizar app com 10k+ items

#### **Semana 16: Suspense**
9. **[react-suspense.md](./advanced/react-suspense.md)** (~2,200 linhas)
   - Suspense for data fetching
   - Error Boundaries
   - Streaming SSR
   - Concurrent rendering
   
**Projeto Prático**: App com Suspense e Error Boundaries

---

### 🚀 **FASE 5: Next.js** (Semana 17-20)

> **Objetivo**: Full-stack com Next.js 14+ App Router

10. **[next-js.md](./nextjs/next-js.md)** (~4,500 linhas)
    - App Router
    - Server Components (RSC)
    - Client Components
    - Server Actions
    - Data Fetching
    - Metadata & SEO
    - Streaming
    - Layouts
    
**Projeto Prático**: Blog full-stack com CMS, auth e deploy

---

### 🏗️ **FASE 6: Arquitetura Avançada** (Semana 21-24)

> **Objetivo**: Arquitetura enterprise e boas práticas

#### **Semana 21-22: Clean Architecture**
11. **[ARCHITECTURE.md](./architecture/ARCHITECTURE.md)** (~11,200 linhas)
    - Clean Architecture
    - SOLID principles
    - Design Patterns
    - State Management strategies
    - Error Handling
    - Testing Strategy
    - Security
    
#### **Semana 23-24: Folder Structure**
12. **[FOLDER_STRUCTURE.md](./architecture/FOLDER_STRUCTURE.md)** (~5,900 linhas)
    - Feature-based organization
    - Domain-Driven Design
    - Component organization
    - File naming conventions
    
**Projeto Final**: Aplicação enterprise completa

---

## 🗺️ Trilhas Alternativas de Estudo

### 🏃 **Trilha Rápida** (12 semanas - Desenvolvedor Experiente)

**Semana 1-2**: Fundamentos (1-2)  
**Semana 3-4**: TypeScript + Forms (3-4)  
**Semana 5-6**: State Management (5-6)  
**Semana 7-8**: Patterns + Performance (7-8)  
**Semana 9-10**: Next.js (10)  
**Semana 11-12**: Arquitetura (11-12)

### 🎯 **Trilha Frontend Web** (16 semanas)

Foco: SPAs modernas com React

1. Fundamentos (1-2)
2. TypeScript (3)
3. Forms (4)
4. State Management (5)
5. Router (6)
6. Patterns (7)
7. Performance (8)
8. Arquitetura (11-12)

### 🌐 **Trilha Full-Stack** (20 semanas)

Foco: Next.js e aplicações full-stack

1. Fundamentos (1-2)
2. TypeScript (3)
3. Forms (4)
4. State Management (5)
5. Next.js (10) - 4 semanas
6. Arquitetura (11-12)

### 📱 **Trilha React Native** (após React web)

Pré-requisito: Completar Fases 1-4

1. React Native fundamentos
2. Navigation
3. Native modules
4. Performance mobile
5. Deploy (App Store/Play Store)

---

## 📊 Matriz de Pré-requisitos

```
┌─────────────────────────────────────────────────────────┐
│ LEGENDA: ✅ Obrigatório  🔶 Recomendado  ⬜ Opcional    │
└─────────────────────────────────────────────────────────┘

Documento                        │ Pré-requisitos
─────────────────────────────────┼───────────────────────────────
react-fundamentos.md             │ (nenhum - JavaScript básico)
react-hooks.md                   │ react-fundamentos ✅
react-typescript.md              │ react-hooks ✅
react-forms.md                   │ react-typescript 🔶
react-state-management.md        │ react-hooks ✅, react-typescript 🔶
react-router.md                  │ react-fundamentos ✅
react-patterns.md                │ react-hooks ✅, react-typescript ✅
react-performance.md             │ react-hooks ✅, react-patterns 🔶
react-suspense.md                │ react-hooks ✅
next-js.md                       │ react-hooks ✅, react-typescript ✅
ARCHITECTURE.md                  │ react-patterns ✅, next-js 🔶
FOLDER_STRUCTURE.md              │ ARCHITECTURE ✅
```

---

## ⏱️ Estimativa de Tempo por Documento

| Documento | Linhas | Dificuldade | Tempo Leitura | Prática | Total |
|-----------|--------|-------------|---------------|---------|-------|
| react-fundamentos.md | ~2,500 | ⭐ | 4-5h | 6-8h | 10-13h |
| react-hooks.md | ~3,000 | ⭐⭐ | 6-8h | 10-12h | 16-20h |
| react-typescript.md | ~3,100 | ⭐⭐⭐ | 6-8h | 8-10h | 14-18h |
| react-forms.md | ~2,900 | ⭐⭐ | 5-6h | 6-8h | 11-14h |
| react-state-management.md | ~2,800 | ⭐⭐⭐ | 6-8h | 10-12h | 16-20h |
| react-router.md | ~2,400 | ⭐⭐ | 4-5h | 4-6h | 8-11h |
| react-patterns.md | ~3,200 | ⭐⭐⭐⭐ | 8-10h | 12-15h | 20-25h |
| react-performance.md | ~2,600 | ⭐⭐⭐ | 5-6h | 8-10h | 13-16h |
| react-suspense.md | ~2,200 | ⭐⭐⭐ | 4-5h | 6-8h | 10-13h |
| next-js.md | ~4,500 | ⭐⭐⭐⭐ | 10-12h | 16-20h | 26-32h |
| ARCHITECTURE.md | ~11,200 | ⭐⭐⭐⭐⭐ | 15-20h | 20-30h | 35-50h |
| FOLDER_STRUCTURE.md | ~5,900 | ⭐⭐⭐ | 8-10h | 10-15h | 18-25h |

**Total Estimado**: ~200-260 horas (teoria + prática)

---

## 🎯 Checklist de Progresso

Marque conforme for completando:

### **Fase 1: Fundamentos React**
- [ ] react-fundamentos.md
- [ ] react-hooks.md

### **Fase 2: TypeScript**
- [ ] react-typescript.md
- [ ] react-forms.md

### **Fase 3: State Management**
- [ ] react-state-management.md
- [ ] react-router.md

### **Fase 4: Patterns**
- [ ] react-patterns.md
- [ ] react-performance.md
- [ ] react-suspense.md

### **Fase 5: Next.js**
- [ ] next-js.md

### **Fase 6: Arquitetura**
- [ ] ARCHITECTURE.md
- [ ] FOLDER_STRUCTURE.md

---

## 💡 Dicas de Estudo

### ✅ **Boas Práticas**
1. **Não pule o TypeScript** - Essencial para produção
2. **Pratique sempre** - Cada conceito em um projeto
3. **Use React DevTools** - Debugging visual
4. **Leia código open-source** - GitHub, Vercel
5. **Teste desde cedo** - Jest + Testing Library

### ⚡ **Recursos Complementares**
- **React Docs**: https://react.dev
- **Next.js Docs**: https://nextjs.org/docs
- **TypeScript Handbook**: https://www.typescriptlang.org/docs
- **YouTube**: Theo (t3.gg), Jack Herrington, Web Dev Simplified

### 🚫 **Erros Comuns**
- ❌ Usar class components (use functions + hooks)
- ❌ Não tipar props corretamente
- ❌ useEffect sem cleanup
- ❌ Não usar key em listas
- ❌ Muitos re-renders desnecessários
- ❌ Não separar Server/Client Components (Next.js)

### 🎓 **Projetos Sugeridos por Fase**

**Fase 1**: Todo App, Weather App  
**Fase 2**: Form Builder, Quiz App  
**Fase 3**: E-commerce (cart), Dashboard  
**Fase 4**: Component Library, Data Table  
**Fase 5**: Blog CMS, SaaS Landing Page  
**Fase 6**: Full-stack App (Notion clone, Trello clone)

---

## 📚 Documentação por Categoria

> **Nota**: Esta seção organiza todas as documentações por categoria. Para ordem de estudo, veja [Guia de Estudo](#-guia-de-estudo---ordem-recomendada).

---

## 📘 Documentação Fundamentos

### 1. React Fundamentos
📄 **[react-fundamentos.md](./react-fundamentos.md)** (~2,500 linhas)

**Conteúdo**:
- ✅ JSX - JavaScript XML
- ✅ Components (Function & Class)
- ✅ Props & Children
- ✅ State Management
- ✅ Event Handling
- ✅ Conditional Rendering
- ✅ Lists & Keys
- ✅ Forms & Controlled Components
- ✅ Lifecycle Methods
- ✅ Composition vs Inheritance

**Exemplo**:
```tsx
function Welcome({ name }: { name: string }) {
  return <h1>Olá, {name}!</h1>;
}

function App() {
  return <Welcome name="Bruno" />;
}
```

---

### 2. React Hooks
📄 **[react-hooks.md](./react-hooks.md)** (~3,000 linhas)

**Todos os Hooks**:
- ✅ **useState** - State management
- ✅ **useEffect** - Side effects
- ✅ **useContext** - Context API
- ✅ **useReducer** - Complex state
- ✅ **useCallback** - Memoized callbacks
- ✅ **useMemo** - Memoized values
- ✅ **useRef** - DOM refs & mutable values
- ✅ **useImperativeHandle** - Customize ref
- ✅ **useLayoutEffect** - Synchronous effects
- ✅ **useDebugValue** - DevTools labels
- ✅ **useDeferredValue** - Defer updates
- ✅ **useTransition** - Non-blocking updates
- ✅ **useId** - Unique IDs
- ✅ **useSyncExternalStore** - External stores

**Custom Hooks**:
```tsx
function useLocalStorage<T>(key: string, initialValue: T) {
  const [value, setValue] = useState<T>(() => {
    const stored = localStorage.getItem(key);
    return stored ? JSON.parse(stored) : initialValue;
  });

  useEffect(() => {
    localStorage.setItem(key, JSON.stringify(value));
  }, [key, value]);

  return [value, setValue] as const;
}
```

---

### 3. React TypeScript
📄 **[react-typescript.md](./react-typescript.md)** (~3,100 linhas)

**Conteúdo**:
- ✅ Props typing
- ✅ State typing
- ✅ Event handlers
- ✅ Children typing
- ✅ Generic components
- ✅ Hooks typing
- ✅ Context typing
- ✅ Refs typing
- ✅ Utility types

**Exemplo**:
```tsx
interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: 'primary' | 'secondary';
  loading?: boolean;
}

export const Button = forwardRef<HTMLButtonElement, ButtonProps>(
  ({ variant = 'primary', loading, children, ...props }, ref) => {
    return (
      <button ref={ref} disabled={loading} {...props}>
        {loading ? 'Loading...' : children}
      </button>
    );
  }
);
```

---

## 🚀 Documentação Avançada

### 4. State Management
📄 **[react-state-management.md](./react-state-management.md)** (~2,800 linhas)

**6 Estratégias**:
1. ✅ **useState** - Local state
2. ✅ **Context API** - Global state
3. ✅ **Zustand** - Lightweight store
4. ✅ **Jotai** - Atomic state
5. ✅ **Redux Toolkit** - Predictable state
6. ✅ **TanStack Query** - Server state

**Zustand Example**:
```tsx
import { create } from 'zustand';

interface CartStore {
  items: CartItem[];
  addItem: (item: CartItem) => void;
  removeItem: (id: string) => void;
}

export const useCart = create<CartStore>((set) => ({
  items: [],
  addItem: (item) => set((state) => ({
    items: [...state.items, item]
  })),
  removeItem: (id) => set((state) => ({
    items: state.items.filter(item => item.id !== id)
  })),
}));
```

---

### 5. React Patterns
📄 **[react-patterns.md](./react-patterns.md)** (~3,200 linhas)

**Padrões Essenciais**:
- ✅ **Compound Components** - Componentes compostos
- ✅ **Render Props** - Compartilhar lógica
- ✅ **Higher-Order Components** (HOC)
- ✅ **Custom Hooks Pattern**
- ✅ **Controlled/Uncontrolled Components**
- ✅ **Provider Pattern**
- ✅ **Container/Presenter Pattern**

**Compound Components**:
```tsx
function Tabs({ children }: { children: React.ReactNode }) {
  const [activeTab, setActiveTab] = useState(0);
  
  return (
    <TabsContext.Provider value={{ activeTab, setActiveTab }}>
      {children}
    </TabsContext.Provider>
  );
}

Tabs.List = TabsList;
Tabs.Tab = Tab;
Tabs.Panel = TabPanel;

// Uso
<Tabs>
  <Tabs.List>
    <Tabs.Tab>Tab 1</Tabs.Tab>
    <Tabs.Tab>Tab 2</Tabs.Tab>
  </Tabs.List>
  <Tabs.Panel>Content 1</Tabs.Panel>
  <Tabs.Panel>Content 2</Tabs.Panel>
</Tabs>
```

---

### 6. Performance Optimization
📄 **[react-performance.md](./react-performance.md)** (~2,600 linhas)

**Técnicas**:
- ✅ **React.memo** - Prevent re-renders
- ✅ **useMemo** - Memoize calculations
- ✅ **useCallback** - Memoize functions
- ✅ **Code Splitting** - Lazy loading
- ✅ **Virtualization** - Large lists
- ✅ **Debouncing/Throttling**
- ✅ **Web Workers**

**React.memo Example**:
```tsx
const ProductCard = React.memo(({ product }: { product: Product }) => {
  return (
    <div>
      <h3>{product.name}</h3>
      <p>{product.price}</p>
    </div>
  );
}, (prevProps, nextProps) => {
  return prevProps.product.id === nextProps.product.id;
});
```

---

### 7. React Router
📄 **[react-router.md](./react-router.md)** (~2,400 linhas)

**React Router v6**:
- ✅ BrowserRouter, Routes, Route
- ✅ Nested Routes
- ✅ Dynamic Routes & Params
- ✅ Navigation (Link, NavLink, useNavigate)
- ✅ Search Params
- ✅ Protected Routes
- ✅ Lazy Loading
- ✅ Error Handling

**Example**:
```tsx
<BrowserRouter>
  <Routes>
    <Route path="/" element={<Layout />}>
      <Route index element={<Home />} />
      <Route path="about" element={<About />} />
      <Route path="produtos">
        <Route index element={<ProdutosList />} />
        <Route path=":id" element={<ProdutoDetail />} />
      </Route>
      <Route path="*" element={<NotFound />} />
    </Route>
  </Routes>
</BrowserRouter>
```

---

### 8. React Suspense
📄 **[react-suspense.md](./react-suspense.md)** (~2,200 linhas)

**Conteúdo**:
- ✅ Suspense for Data Fetching
- ✅ Error Boundaries
- ✅ Streaming SSR
- ✅ Lazy Loading Components
- ✅ Concurrent Rendering

**Example**:
```tsx
<Suspense fallback={<LoadingSpinner />}>
  <ProductList />
</Suspense>

// Error Boundary
<ErrorBoundary fallback={<ErrorUI />}>
  <Suspense fallback={<Loading />}>
    <AsyncComponent />
  </Suspense>
</ErrorBoundary>
```

---

### 9. Next.js 14+
📄 **[next-js.md](./next-js.md)** (~4,500 linhas)

**App Router (Next.js 14+)**:
- ✅ **Server Components** - RSC
- ✅ **Client Components** - Interactivity
- ✅ **Layouts** - Shared UI
- ✅ **Loading & Error UI**
- ✅ **Routing** - File-based
- ✅ **Data Fetching** - Server-side
- ✅ **Server Actions** - Mutations
- ✅ **Metadata** - SEO
- ✅ **Streaming** - Progressive rendering

**Server Component**:
```tsx
// app/produtos/page.tsx
export default async function ProdutosPage() {
  const produtos = await db.produto.findMany();
  
  return (
    <div>
      <h1>Produtos</h1>
      <ProdutosList produtos={produtos} />
    </div>
  );
}
```

**Server Action**:
```tsx
'use server';

export async function createProduto(formData: FormData) {
  const nome = formData.get('nome') as string;
  
  await db.produto.create({
    data: { nome }
  });
  
  revalidatePath('/produtos');
}
```

---

### 10. React Forms
📄 **[react-forms.md](./react-forms.md)** (~2,900 linhas)

**Conteúdo**:
- ✅ **React Hook Form** - Form library
- ✅ **Zod** - Schema validation
- ✅ **Controlled/Uncontrolled inputs**
- ✅ **Field Arrays**
- ✅ **Custom Validations**
- ✅ **File Uploads**

**React Hook Form + Zod**:
```tsx
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';

const schema = z.object({
  email: z.string().email(),
  password: z.string().min(8),
});

type FormData = z.infer<typeof schema>;

function LoginForm() {
  const { register, handleSubmit, formState: { errors } } = useForm<FormData>({
    resolver: zodResolver(schema),
  });

  const onSubmit = (data: FormData) => {
    console.log(data);
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <input {...register('email')} />
      {errors.email && <span>{errors.email.message}</span>}
      
      <input type="password" {...register('password')} />
      {errors.password && <span>{errors.password.message}</span>}
      
      <button type="submit">Login</button>
    </form>
  );
}
```

---

## 🏗️ Arquitetura

### 11. ARCHITECTURE.md
📄 **[ARCHITECTURE.md](./ARCHITECTURE.md)** (~11,200 linhas)

**12 Seções Completas** (86% concluído):

1. ✅ **Visão Geral** - 4 padrões arquiteturais
2. ✅ **Camadas da Aplicação** - 6 camadas
3. ✅ **Estrutura de Pastas** - Feature-based
4. ✅ **Fluxo de Dados** - Unidirectional
5. ✅ **Princípios SOLID** - Com exemplos
6. ✅ **Padrões de Design** - 7 padrões
7. ✅ **Feature-Based Organization**
8. ✅ **State Management** - 6 estratégias
9. ✅ **Error Handling** - Boundaries, Actions
10. ✅ **Performance Optimization** - Code splitting, memoization
11. ✅ **Testing Strategy** - Unit, Integration, E2E
12. ✅ **Security Best Practices** - Auth, CSRF, XSS

**Próximas Seções**:
- 🔄 Code Quality
- 🔄 Deployment Strategy

---

### 12. FOLDER_STRUCTURE.md
📄 **[FOLDER_STRUCTURE.md](./FOLDER_STRUCTURE.md)** (~5,900 linhas)

**Estrutura Next.js 14+ App Router**:

```
project-root/
├── app/                    # App Router
│   ├── (auth)/            # Route Group - Auth
│   ├── (dashboard)/       # Route Group - Dashboard
│   ├── api/               # API Routes
│   ├── layout.tsx         # Root Layout
│   └── page.tsx           # Home Page
├── components/            # Shared Components
│   ├── ui/               # Base UI components
│   ├── layout/           # Layout components
│   └── forms/            # Form components
├── lib/                  # Libraries & Utils
│   ├── db.ts            # Database
│   ├── auth.ts          # Authentication
│   └── utils.ts         # Utilities
├── hooks/               # Custom Hooks
├── contexts/            # React Contexts
└── middleware.ts        # Next.js Middleware
```

**Padrões**:
- ✅ Clean Architecture
- ✅ Feature-Slice Design
- ✅ Domain-Driven Design
- ✅ Component-Driven Development

---

**Próximas Seções**:
- � Code Quality
- 🔄 Deployment Strategy

---

## 🔗 Recursos Adicionais

### Documentação Oficial
- [React Documentation](https://react.dev) - Docs oficiais React 18+
- [Next.js Documentation](https://nextjs.org/docs) - Docs oficiais Next.js 14+
- [TypeScript Documentation](https://www.typescriptlang.org/docs) - TypeScript Handbook
- [React TypeScript Cheatsheet](https://react-typescript-cheatsheet.netlify.app/)

### Ferramentas Essenciais
- **[Vite](https://vitejs.dev)** - Build tool rápido para React
- **[React DevTools](https://react.dev/learn/react-developer-tools)** - Chrome/Firefox extension
- **[TypeScript](https://www.typescriptlang.org/)** - Type safety
- **[ESLint](https://eslint.org/)** - Linting
- **[Prettier](https://prettier.io/)** - Code formatting
- **[Vitest](https://vitest.dev/)** - Testing framework

### Bibliotecas Recomendadas

#### State Management
- **[Zustand](https://zustand-demo.pmnd.rs/)** - Lightweight state (recomendado)
- **[Jotai](https://jotai.org/)** - Atomic state
- **[TanStack Query](https://tanstack.com/query)** - Server state (essencial)
- **[Redux Toolkit](https://redux-toolkit.js.org/)** - Predictable state

#### Forms & Validation
- **[React Hook Form](https://react-hook-form.com/)** - Form management (recomendado)
- **[Zod](https://zod.dev/)** - Schema validation
- **[Yup](https://github.com/jquense/yup)** - Alternativa ao Zod

#### UI Components
- **[Radix UI](https://www.radix-ui.com/)** - Unstyled primitives
- **[shadcn/ui](https://ui.shadcn.com/)** - Re-usable components
- **[Headless UI](https://headlessui.com/)** - Tailwind components
- **[Mantine](https://mantine.dev/)** - Full UI library

#### Styling
- **[Tailwind CSS](https://tailwindcss.com/)** - Utility-first (recomendado)
- **[Styled Components](https://styled-components.com/)** - CSS-in-JS
- **[CSS Modules](https://github.com/css-modules/css-modules)** - Scoped CSS

#### Animation
- **[Framer Motion](https://www.framer.com/motion/)** - Animation library
- **[React Spring](https://www.react-spring.dev/)** - Spring physics

#### Testing
- **[Vitest](https://vitest.dev/)** - Unit testing (recomendado)
- **[Testing Library](https://testing-library.com/)** - Component testing
- **[Playwright](https://playwright.dev/)** - E2E testing
- **[MSW](https://mswjs.io/)** - API mocking

### Comunidade & Aprendizado

#### Blogs & Newsletters
- **[Kent C. Dodds Blog](https://kentcdodds.com/blog)** - Testing, React
- **[Josh Comeau Blog](https://www.joshwcomeau.com/)** - CSS, React
- **[Robin Wieruch Blog](https://www.robinwieruch.de/)** - React tutorials
- **[This Week in React](https://thisweekinreact.com/)** - Newsletter

#### YouTube Channels
- **[Theo - t3.gg](https://www.youtube.com/@t3dotgg)** - Next.js, TypeScript
- **[Jack Herrington](https://www.youtube.com/@jherr)** - React, TypeScript
- **[Web Dev Simplified](https://www.youtube.com/@WebDevSimplified)** - React basics
- **[Fireship](https://www.youtube.com/@Fireship)** - Quick tutorials

#### Comunidades
- **[React Discord](https://discord.gg/react)** - Oficial
- **[Next.js Discord](https://nextjs.org/discord)** - Oficial
- **[Reactiflux](https://www.reactiflux.com/)** - React community
- **[Stack Overflow](https://stackoverflow.com/questions/tagged/reactjs)** - Q&A

### Cursos Online (Opcionais)

- **[Epic React](https://epicreact.dev/)** - Kent C. Dodds
- **[React Query Course](https://ui.dev/c/react-query)** - ui.dev
- **[Testing JavaScript](https://testingjavascript.com/)** - Kent C. Dodds
- **[TypeScript Course](https://www.totaltypescript.com/)** - Matt Pocock

### Open Source para Estudar

#### Next.js Apps
- **[Taxonomy](https://github.com/shadcn-ui/taxonomy)** - Next.js 14 App Router
- **[Next.js Commerce](https://github.com/vercel/commerce)** - E-commerce
- **[T3 App](https://github.com/t3-oss/create-t3-app)** - Full-stack TypeScript

#### Component Libraries
- **[shadcn/ui Source](https://github.com/shadcn-ui/ui)** - Component patterns
- **[Radix UI Source](https://github.com/radix-ui/primitives)** - Headless components

### Templates & Starters

- **[create-next-app](https://nextjs.org/docs/api-reference/create-next-app)** - Next.js oficial
- **[Vite + React](https://vitejs.dev/guide/)** - Vite oficial
- **[T3 Stack](https://create.t3.gg/)** - Next.js + tRPC + Prisma
- **[Nextra](https://nextra.site/)** - Docs site generator

---

## 🎯 Próximos Passos

1. ✅ **Clone o repositório**
2. 📖 **Escolha seu nível** (Iniciante/Intermediário/Avançado)
3. 🗺️ **Siga o [Guia de Estudo](#-guia-de-estudo---ordem-recomendada)**
4. 💻 **Pratique com projetos** após cada fase
5. 🤝 **Contribua** com o repositório (PRs bem-vindos!)

### Dúvidas Frequentes

**Q: Preciso saber JavaScript antes?**  
A: Sim! Recomendo sólido conhecimento em ES6+: arrow functions, destructuring, spread operator, promises, async/await.

**Q: Posso pular o TypeScript?**  
A: Não recomendado. 90%+ das vagas React exigem TypeScript.

**Q: Next.js ou React puro?**  
A: Aprenda React primeiro (Fases 1-4), depois Next.js. Next.js É React + features extras.

**Q: Qual biblioteca de state escolher?**  
A: TanStack Query (server state) + Zustand (client state) é a combinação mais moderna.

**Q: Preciso aprender class components?**  
A: Não para novos projetos. Foque em function components + hooks.

---

**📍 Você está aqui**: frontend/react/README.md  
**⬆️ Voltar para**: [📁 Repositório Principal](../../README.md)
