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

- [Documentação Fundamentos](#-documentação-fundamentos)
- [Documentação Avançada](#-documentação-avançada)
- [Arquitetura](#️-arquitetura)
- [Guia de Estudo](#-guia-de-estudo)
- [Recursos Adicionais](#-recursos-adicionais)

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

## 📚 Guia de Estudo

### Iniciantes (2-3 meses)
1. **Semana 1-2**: [react-fundamentos.md](./react-fundamentos.md)
2. **Semana 3-4**: [react-hooks.md](./react-hooks.md)
3. **Semana 5-6**: [react-typescript.md](./react-typescript.md)
4. **Semana 7-8**: [react-forms.md](./react-forms.md)
5. **Semana 9-10**: Projeto prático
6. **Semana 11-12**: [react-router.md](./react-router.md)

### Intermediários (2-3 meses)
1. **Semana 1-2**: [react-state-management.md](./react-state-management.md)
2. **Semana 3-4**: [react-patterns.md](./react-patterns.md)
3. **Semana 5-6**: [react-performance.md](./react-performance.md)
4. **Semana 7-8**: [react-suspense.md](./react-suspense.md)
5. **Semana 9-12**: [next-js.md](./next-js.md)

### Avançados (1-2 meses)
1. **Semana 1-2**: [ARCHITECTURE.md](./ARCHITECTURE.md)
2. **Semana 3-4**: [FOLDER_STRUCTURE.md](./FOLDER_STRUCTURE.md)
3. **Semana 5-8**: Projeto complexo com arquitetura completa

---

## 🔧 Recursos Adicionais

### Documentação Oficial
- [React Documentation](https://react.dev)
- [Next.js Documentation](https://nextjs.org/docs)
- [TypeScript Documentation](https://www.typescriptlang.org/docs)

### Ferramentas
- **Vite** - Build tool rápido
- **React DevTools** - Chrome extension
- **TypeScript** - Type safety
- **ESLint** - Linting
- **Prettier** - Code formatting

### Bibliotecas Essenciais
- **React Hook Form** - Form management
- **Zod** - Schema validation
- **TanStack Query** - Data fetching
- **Zustand** - State management
- **Tailwind CSS** - Styling

---

## 🎯 Próximos Passos

1. **Clone o repositório**
2. **Escolha seu nível** (Iniciante/Intermediário/Avançado)
3. **Siga o guia de estudo**
4. **Pratique com projetos**
5. **Contribua com o repositório**

---

**Voltar para**: [📁 Repositório Principal](../README.md)
