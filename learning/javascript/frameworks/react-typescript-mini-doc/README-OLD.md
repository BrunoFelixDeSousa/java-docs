# ⚛️ React + TypeScript - Guia Completo Moderno

**React + TypeScript** é a combinação perfeita para construir aplicações web **type-safe**, escaláveis e mantíveis.

> "Build type-safe React applications with confidence"

[![React](https://img.shields.io/badge/React-18+-61DAFB?style=flat&logo=react&logoColor=white)](https://react.dev)
[![TypeScript](https://img.shields.io/badge/TypeScript-5+-3178C6?style=flat&logo=typescript&logoColor=white)](https://www.typescriptlang.org/)

---

## 📋 Índice

- [Por que React + TypeScript?](#-por-que-react--typescript)
- [Setup & Instalação](#-setup--instalação)
- [Componentes](#-componentes)
- [Props & Children](#-props--children)
- [Hooks Básicos](#-hooks-básicos)
- [Hooks Avançados](#-hooks-avançados)
- [Custom Hooks](#-custom-hooks)
- [Context API](#-context-api)
- [Events & Forms](#-events--forms)
- [Styling](#-styling)
- [React Router](#-react-router)
- [Performance](#-performance)
- [Testing](#-testing)
- [Best Practices](#-best-practices)

---

## 🎯 Por que React + TypeScript?

### ✅ Comparação: JavaScript vs TypeScript

```typescript
// ❌ JavaScript: Props não tipadas
function Button({ label, onClick }) {
  return <button onClick={onClick}>{label}</button>;
}

// Problemas:
<Button label={123} onClick="não é função" /> // Sem erro em dev ⚠️
<Button /> // label e onClick undefined ⚠️

// ✅ TypeScript: Props tipadas e seguras
interface ButtonProps {
  label: string;
  onClick: () => void;
}

function Button({ label, onClick }: ButtonProps) {
  return <button onClick={onClick}>{label}</button>;
}

// Erros detectados ANTES de executar! ✨
<Button label={123} onClick="erro" /> // ❌ Type error
<Button /> // ❌ Missing required props
<Button label="OK" onClick={() => {}} /> // ✅ Correto
```

### 🚀 Vantagens do TypeScript no React

| Vantagem | Descrição |
|----------|-----------|
| **IntelliSense** | Autocomplete de props, states, eventos |
| **Type Safety** | Erros detectados em compile-time |
| **Refactoring** | Rename/move com segurança |
| **Documentation** | Types servem como documentação |
| **Less Bugs** | Menos erros em produção |
| **Better DX** | Developer Experience superior |

---

## 📦 Setup & Instalação

### Vite (Recomendado - Rápido)

```bash
# Criar projeto
npm create vite@latest my-app -- --template react-ts

cd my-app
npm install
npm run dev
```

### Next.js 14+ (Full-stack)

```bash
npx create-next-app@latest my-app --typescript --tailwind --app

cd my-app
npm run dev
```

### Create React App (Legacy)

```bash
npx create-react-app my-app --template typescript
```

---

### tsconfig.json (Recomendado)

```json
{
  "compilerOptions": {
    "target": "ES2020",
    "lib": ["ES2020", "DOM", "DOM.Iterable"],
    "module": "ESNext",
    "moduleResolution": "bundler",
    
    // React específico
    "jsx": "react-jsx",              // React 17+ (sem import React)
    
    // Type checking rigoroso
    "strict": true,
    "noImplicitAny": true,
    "strictNullChecks": true,
    "strictFunctionTypes": true,
    
    // Code quality
    "noUnusedLocals": true,
    "noUnusedParameters": true,
    "noFallthroughCasesInSwitch": true,
    
    // Paths
    "baseUrl": ".",
    "paths": {
      "@/*": ["./src/*"],
      "@components/*": ["./src/components/*"],
      "@hooks/*": ["./src/hooks/*"]
    },
    
    // Outros
    "esModuleInterop": true,
    "skipLibCheck": true,
    "resolveJsonModule": true,
    "isolatedModules": true
  },
  "include": ["src"],
  "exclude": ["node_modules"]
}
```

---

### Estrutura de Projeto (Recomendada)

```
src/
├── components/          # Componentes React
│   ├── ui/             # Componentes de UI (Button, Input)
│   ├── layout/         # Layout (Header, Footer, Sidebar)
│   └── features/       # Componentes de features
│
├── hooks/              # Custom hooks
│   ├── useAuth.ts
│   └── useFetch.ts
│
├── contexts/           # Context API
│   └── AuthContext.tsx
│
├── types/              # TypeScript types
│   ├── api.ts
│   └── models.ts
│
├── utils/              # Funções utilitárias
│   └── helpers.ts
│
├── services/           # API calls
│   └── api.ts
│
├── App.tsx
└── main.tsx
```

---

## 🧩 Componentes

### Componente Funcional Básico

```tsx
// Componente sem props
function Welcome() {
  return <h1>Hello, World!</h1>;
}

// Export padrão
export default Welcome;

// Named export (melhor para treeshaking)
export function Welcome() {
  return <h1>Hello, World!</h1>;
}

// Arrow function
const Welcome = () => {
  return <h1>Hello, World!</h1>;
};

// Arrow function com return implícito
const Welcome = () => <h1>Hello, World!</h1>;
```

### Componente com State

```tsx
import { useState } from 'react';

function Counter() {
  // useState com tipo inferido
  const [count, setCount] = useState(0); // count: number

  return (
    <div>
      <p>Count: {count}</p>
      <button onClick={() => setCount(count + 1)}>Increment</button>
    </div>
  );
}
```

### Componente com State Complexo

```tsx
import { useState } from 'react';

// Definir interface para o state
interface User {
  id: number;
  name: string;
  email: string;
}

function UserProfile() {
  // State tipado explicitamente
  const [user, setUser] = useState<User | null>(null);
  
  // State com valor inicial
  const [users, setUsers] = useState<User[]>([]);
  
  const loadUser = () => {
    setUser({
      id: 1,
      name: 'João Silva',
      email: 'joao@example.com'
    });
  };

  return (
    <div>
      {user ? (
        <div>
          <h2>{user.name}</h2>
          <p>{user.email}</p>
        </div>
      ) : (
        <button onClick={loadUser}>Load User</button>
      )}
    </div>
  );
}
```

---

## 📤 Props & Children

### Props Interface

```tsx
// Interface para props
interface ButtonProps {
  label: string;
  onClick: () => void;
  disabled?: boolean;        // Opcional
  variant?: 'primary' | 'secondary'; // Union type
}

function Button({ label, onClick, disabled = false, variant = 'primary' }: ButtonProps) {
  return (
    <button 
      onClick={onClick} 
      disabled={disabled}
      className={variant === 'primary' ? 'btn-primary' : 'btn-secondary'}
    >
      {label}
    </button>
  );
}

// Uso
<Button label="Click me" onClick={() => console.log('Clicked')} />
<Button label="Submit" onClick={handleSubmit} variant="primary" disabled />
```

### Props com Children

```tsx
// Children como ReactNode (aceita qualquer JSX)
interface CardProps {
  title: string;
  children: React.ReactNode;
}

function Card({ title, children }: CardProps) {
  return (
    <div className="card">
      <h2>{title}</h2>
      <div className="card-content">
        {children}
      </div>
    </div>
  );
}

// Uso
<Card title="My Card">
  <p>Card content</p>
  <button>Action</button>
</Card>
```

### Props com Type Alias

```tsx
// Usar type ao invés de interface (preferência pessoal)
type ButtonProps = {
  label: string;
  onClick: () => void;
  disabled?: boolean;
};

// Equivalente ao exemplo anterior
function Button({ label, onClick, disabled }: ButtonProps) {
  return <button onClick={onClick} disabled={disabled}>{label}</button>;
}
```

### Props com Spread

```tsx
// Extender props nativas de HTML
interface InputProps extends React.InputHTMLAttributes<HTMLInputElement> {
  label: string;
  error?: string;
}

function Input({ label, error, ...rest }: InputProps) {
  return (
    <div>
      <label>{label}</label>
      <input {...rest} /> {/* Todas props HTML nativas funcionam */}
      {error && <span className="error">{error}</span>}
    </div>
  );
}

// Uso - todas props de <input> funcionam!
<Input 
  label="Email" 
  type="email" 
  placeholder="you@example.com"
  required
  error="Email inválido"
/>
```

### Props com Generics

```tsx
// Componente genérico (funciona com qualquer tipo de item)
interface ListProps<T> {
  items: T[];
  renderItem: (item: T) => React.ReactNode;
}

function List<T>({ items, renderItem }: ListProps<T>) {
  return (
    <ul>
      {items.map((item, index) => (
        <li key={index}>{renderItem(item)}</li>
      ))}
    </ul>
  );
}

// Uso com diferentes tipos
interface User {
  id: number;
  name: string;
}

<List 
  items={users}                    // User[]
  renderItem={(user) => user.name} // Type inference automático! ✨
/>

<List 
  items={[1, 2, 3]}               // number[]
  renderItem={(num) => num * 2}   // Type inference funciona!
/>
```

---

## 🪝 Hooks Básicos

### useState

```tsx
import { useState } from 'react';

function Examples() {
  // Type inference automático
  const [count, setCount] = useState(0);        // number
  const [name, setName] = useState('');         // string
  const [isOpen, setIsOpen] = useState(false);  // boolean
  
  // Type explícito quando necessário
  const [user, setUser] = useState<User | null>(null);
  
  // State com array
  const [items, setItems] = useState<string[]>([]);
  
  // State com objeto
  interface FormData {
    name: string;
    email: string;
  }
  
  const [formData, setFormData] = useState<FormData>({
    name: '',
    email: ''
  });
  
  // Atualizar state objeto
  const updateName = (name: string) => {
    setFormData(prev => ({ ...prev, name }));
  };
  
  // Atualizar state array
  const addItem = (item: string) => {
    setItems(prev => [...prev, item]);
  };

  return <div>Examples</div>;
}
```

### useEffect

```tsx
import { useState, useEffect } from 'react';

function UserProfile({ userId }: { userId: number }) {
  const [user, setUser] = useState<User | null>(null);
  const [loading, setLoading] = useState(true);

  // Effect básico
  useEffect(() => {
    console.log('Component mounted');
    
    // Cleanup (executado quando componente desmonta)
    return () => {
      console.log('Component unmounted');
    };
  }, []); // [] = executa apenas uma vez

  // Effect com dependências
  useEffect(() => {
    const fetchUser = async () => {
      setLoading(true);
      try {
        const response = await fetch(`/api/users/${userId}`);
        const data = await response.json();
        setUser(data);
      } catch (error) {
        console.error('Error fetching user:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchUser();
  }, [userId]); // Re-executa quando userId mudar

  // Effect com cleanup (WebSocket, timers, etc)
  useEffect(() => {
    const interval = setInterval(() => {
      console.log('Tick');
    }, 1000);

    // Cleanup: limpar interval ao desmontar
    return () => clearInterval(interval);
  }, []);

  if (loading) return <div>Loading...</div>;
  if (!user) return <div>User not found</div>;

  return <div>{user.name}</div>;
}
```

### useRef

```tsx
import { useRef, useEffect } from 'react';

function Examples() {
  // Ref para elemento DOM
  const inputRef = useRef<HTMLInputElement>(null);
  
  // Ref para valor mutável (não causa re-render)
  const countRef = useRef(0);
  
  // Ref para armazenar timeout
  const timeoutRef = useRef<number>();

  useEffect(() => {
    // Focar input ao montar
    inputRef.current?.focus();
  }, []);

  const startTimer = () => {
    timeoutRef.current = window.setTimeout(() => {
      console.log('Timer finished');
    }, 1000);
  };

  const stopTimer = () => {
    if (timeoutRef.current) {
      clearTimeout(timeoutRef.current);
    }
  };

  return (
    <div>
      <input ref={inputRef} type="text" />
      
      <button onClick={() => {
        // Incrementar sem re-render
        countRef.current += 1;
        console.log(countRef.current);
      }}>
        Increment (no re-render)
      </button>
      
      <button onClick={startTimer}>Start Timer</button>
      <button onClick={stopTimer}>Stop Timer</button>
    </div>
  );
}
```

### useContext

```tsx
import { createContext, useContext, useState, ReactNode } from 'react';

// 1. Definir tipo do contexto
interface AuthContextType {
  user: User | null;
  login: (email: string, password: string) => Promise<void>;
  logout: () => void;
  isAuthenticated: boolean;
}

// 2. Criar contexto com undefined inicial
const AuthContext = createContext<AuthContextType | undefined>(undefined);

// 3. Provider component
interface AuthProviderProps {
  children: ReactNode;
}

export function AuthProvider({ children }: AuthProviderProps) {
  const [user, setUser] = useState<User | null>(null);

  const login = async (email: string, password: string) => {
    // Simular login
    const response = await fetch('/api/login', {
      method: 'POST',
      body: JSON.stringify({ email, password }),
    });
    const userData = await response.json();
    setUser(userData);
  };

  const logout = () => {
    setUser(null);
  };

  const value: AuthContextType = {
    user,
    login,
    logout,
    isAuthenticated: !!user,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

// 4. Custom hook para usar o contexto
export function useAuth() {
  const context = useContext(AuthContext);
  
  if (context === undefined) {
    throw new Error('useAuth must be used within AuthProvider');
  }
  
  return context;
}

// 5. Uso em componentes
function LoginForm() {
  const { login, isAuthenticated } = useAuth(); // Type-safe! ✨
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    await login(email, password);
  };

  if (isAuthenticated) {
    return <div>Already logged in!</div>;
  }

  return (
    <form onSubmit={handleSubmit}>
      <input 
        type="email" 
        value={email} 
        onChange={(e) => setEmail(e.target.value)} 
      />
      <input 
        type="password" 
        value={password} 
        onChange={(e) => setPassword(e.target.value)} 
      />
      <button type="submit">Login</button>
    </form>
  );
}

// 6. Uso no App
function App() {
  return (
    <AuthProvider>
      <LoginForm />
    </AuthProvider>
  );
}
```

---

## 🔥 Hooks Avançados

### useReducer

```tsx
import { useReducer } from 'react';

// 1. Definir State type
interface CounterState {
  count: number;
  step: number;
}

// 2. Definir Action types
type CounterAction =
  | { type: 'INCREMENT' }
  | { type: 'DECREMENT' }
  | { type: 'SET_STEP'; payload: number }
  | { type: 'RESET' };

// 3. Reducer function
function counterReducer(state: CounterState, action: CounterAction): CounterState {
  switch (action.type) {
    case 'INCREMENT':
      return { ...state, count: state.count + state.step };
    
    case 'DECREMENT':
      return { ...state, count: state.count - state.step };
    
    case 'SET_STEP':
      return { ...state, step: action.payload };
    
    case 'RESET':
      return { count: 0, step: 1 };
    
    default:
      return state;
  }
}

// 4. Componente
function Counter() {
  const [state, dispatch] = useReducer(counterReducer, {
    count: 0,
    step: 1,
  });

  return (
    <div>
      <p>Count: {state.count}</p>
      <p>Step: {state.step}</p>
      
      <button onClick={() => dispatch({ type: 'INCREMENT' })}>
        + {state.step}
      </button>
      
      <button onClick={() => dispatch({ type: 'DECREMENT' })}>
        - {state.step}
      </button>
      
      <button onClick={() => dispatch({ type: 'SET_STEP', payload: 5 })}>
        Set step to 5
      </button>
      
      <button onClick={() => dispatch({ type: 'RESET' })}>
        Reset
      </button>
    </div>
  );
}
```

### useMemo

```tsx
import { useState, useMemo } from 'react';

function ExpensiveComponent() {
  const [count, setCount] = useState(0);
  const [items, setItems] = useState<number[]>([]);

  // Computação cara que só executa quando items mudar
  const total = useMemo(() => {
    console.log('Calculating total...');
    return items.reduce((sum, item) => sum + item, 0);
  }, [items]); // Só recalcula se items mudar

  // Filtro caro
  const expensiveItems = useMemo(() => {
    console.log('Filtering expensive items...');
    return items.filter(item => item > 100);
  }, [items]);

  return (
    <div>
      <p>Count: {count}</p>
      <p>Total: {total}</p>
      <p>Expensive items: {expensiveItems.length}</p>
      
      {/* count muda mas total NÃO recalcula (useMemo) */}
      <button onClick={() => setCount(c => c + 1)}>
        Increment count
      </button>
      
      <button onClick={() => setItems([...items, Math.random() * 200])}>
        Add item
      </button>
    </div>
  );
}
```

### useCallback

```tsx
import { useState, useCallback } from 'react';

// Componente filho
interface ChildProps {
  onSave: (data: string) => void;
}

const Child = ({ onSave }: ChildProps) => {
  console.log('Child rendered');
  return <button onClick={() => onSave('data')}>Save</button>;
};

// Componente pai
function Parent() {
  const [count, setCount] = useState(0);

  // ❌ Sem useCallback: nova função a cada render
  const handleSave = (data: string) => {
    console.log('Saving:', data);
  };

  // ✅ Com useCallback: mesma função (não re-renderiza Child)
  const handleSaveOptimized = useCallback((data: string) => {
    console.log('Saving:', data);
  }, []); // [] = função nunca muda

  // useCallback com dependências
  const handleSaveWithCount = useCallback((data: string) => {
    console.log('Saving:', data, 'count:', count);
  }, [count]); // Nova função quando count mudar

  return (
    <div>
      <p>Count: {count}</p>
      <button onClick={() => setCount(c => c + 1)}>Increment</button>
      
      {/* Child re-renderiza a cada vez (handleSave muda) */}
      <Child onSave={handleSave} />
      
      {/* Child NÃO re-renderiza (handleSaveOptimized é estável) */}
      <Child onSave={handleSaveOptimized} />
    </div>
  );
}
```

### useTransition (React 18+)

```tsx
import { useState, useTransition } from 'react';

function SearchComponent() {
  const [query, setQuery] = useState('');
  const [results, setResults] = useState<string[]>([]);
  
  // isPending: indica se transição está em andamento
  // startTransition: marca atualização como não-urgente
  const [isPending, startTransition] = useTransition();

  const handleSearch = (value: string) => {
    setQuery(value); // Atualização urgente (input)
    
    // Atualização não-urgente (busca)
    startTransition(() => {
      // Simular busca cara
      const filtered = expensiveSearch(value);
      setResults(filtered);
    });
  };

  return (
    <div>
      <input 
        type="text" 
        value={query} 
        onChange={(e) => handleSearch(e.target.value)}
        placeholder="Search..."
      />
      
      {isPending && <span>Searching...</span>}
      
      <ul>
        {results.map((result, i) => (
          <li key={i}>{result}</li>
        ))}
      </ul>
    </div>
  );
}

function expensiveSearch(query: string): string[] {
  // Simular busca cara
  const items = Array.from({ length: 10000 }, (_, i) => `Item ${i}`);
  return items.filter(item => item.includes(query));
}
```

---

## 🎣 Custom Hooks

### useFetch Hook

```tsx
import { useState, useEffect } from 'react';

interface UseFetchResult<T> {
  data: T | null;
  loading: boolean;
  error: Error | null;
  refetch: () => void;
}

function useFetch<T>(url: string): UseFetchResult<T> {
  const [data, setData] = useState<T | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<Error | null>(null);

  const fetchData = async () => {
    setLoading(true);
    setError(null);
    
    try {
      const response = await fetch(url);
      if (!response.ok) throw new Error('Failed to fetch');
      
      const json = await response.json();
      setData(json);
    } catch (err) {
      setError(err as Error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, [url]);

  return { data, loading, error, refetch: fetchData };
}

// Uso
interface User {
  id: number;
  name: string;
}

function UserList() {
  const { data: users, loading, error, refetch } = useFetch<User[]>('/api/users');

  if (loading) return <div>Loading...</div>;
  if (error) return <div>Error: {error.message}</div>;
  if (!users) return null;

  return (
    <div>
      <button onClick={refetch}>Refresh</button>
      <ul>
        {users.map(user => (
          <li key={user.id}>{user.name}</li>
        ))}
      </ul>
    </div>
  );
}
```

### useLocalStorage Hook

```tsx
import { useState, useEffect } from 'react';

function useLocalStorage<T>(key: string, initialValue: T) {
  // State com lazy initialization
  const [value, setValue] = useState<T>(() => {
    try {
      const item = window.localStorage.getItem(key);
      return item ? JSON.parse(item) : initialValue;
    } catch (error) {
      console.error(error);
      return initialValue;
    }
  });

  // Atualizar localStorage quando value mudar
  useEffect(() => {
    try {
      window.localStorage.setItem(key, JSON.stringify(value));
    } catch (error) {
      console.error(error);
    }
  }, [key, value]);

  return [value, setValue] as const;
}

// Uso
function App() {
  const [name, setName] = useLocalStorage('name', '');
  const [count, setCount] = useLocalStorage('count', 0);

  return (
    <div>
      <input 
        value={name} 
        onChange={(e) => setName(e.target.value)} 
      />
      <button onClick={() => setCount(count + 1)}>
        Count: {count}
      </button>
    </div>
  );
}
```

### useDebounce Hook

```tsx
import { useState, useEffect } from 'react';

function useDebounce<T>(value: T, delay: number): T {
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

// Uso: Search com debounce
function SearchComponent() {
  const [searchTerm, setSearchTerm] = useState('');
  const debouncedSearchTerm = useDebounce(searchTerm, 500);

  useEffect(() => {
    if (debouncedSearchTerm) {
      // Fazer busca (só depois de 500ms sem digitar)
      console.log('Searching for:', debouncedSearchTerm);
    }
  }, [debouncedSearchTerm]);

  return (
    <input
      type="text"
      value={searchTerm}
      onChange={(e) => setSearchTerm(e.target.value)}
      placeholder="Search..."
    />
  );
}
```

---

## 🌐 Context API

### Theme Context (Exemplo Completo)

```tsx
import { createContext, useContext, useState, ReactNode } from 'react';

// 1. Types
type Theme = 'light' | 'dark';

interface ThemeContextType {
  theme: Theme;
  toggleTheme: () => void;
}

// 2. Context
const ThemeContext = createContext<ThemeContextType | undefined>(undefined);

// 3. Provider
interface ThemeProviderProps {
  children: ReactNode;
}

export function ThemeProvider({ children }: ThemeProviderProps) {
  const [theme, setTheme] = useState<Theme>('light');

  const toggleTheme = () => {
    setTheme(prev => prev === 'light' ? 'dark' : 'light');
  };

  return (
    <ThemeContext.Provider value={{ theme, toggleTheme }}>
      {children}
    </ThemeContext.Provider>
  );
}

// 4. Custom Hook
export function useTheme() {
  const context = useContext(ThemeContext);
  
  if (!context) {
    throw new Error('useTheme must be used within ThemeProvider');
  }
  
  return context;
}

// 5. Uso
function App() {
  return (
    <ThemeProvider>
      <ThemedComponent />
    </ThemeProvider>
  );
}

function ThemedComponent() {
  const { theme, toggleTheme } = useTheme();

  return (
    <div className={theme}>
      <p>Current theme: {theme}</p>
      <button onClick={toggleTheme}>
        Toggle Theme
      </button>
    </div>
  );
}
```

---

## 📝 Events & Forms

### Event Handlers

```tsx
function EventExamples() {
  // Click event
  const handleClick = (e: React.MouseEvent<HTMLButtonElement>) => {
    console.log('Button clicked');
    console.log('Coordinates:', e.clientX, e.clientY);
  };

  // Change event (input)
  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    console.log('Value:', e.target.value);
  };

  // Change event (select)
  const handleSelectChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    console.log('Selected:', e.target.value);
  };

  // Submit event
  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    console.log('Form submitted');
  };

  // Key event
  const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Enter') {
      console.log('Enter pressed');
    }
  };

  // Focus event
  const handleFocus = (e: React.FocusEvent<HTMLInputElement>) => {
    console.log('Input focused');
  };

  return (
    <form onSubmit={handleSubmit}>
      <button onClick={handleClick}>Click me</button>
      
      <input 
        type="text" 
        onChange={handleChange}
        onKeyDown={handleKeyDown}
        onFocus={handleFocus}
      />
      
      <select onChange={handleSelectChange}>
        <option value="1">Option 1</option>
        <option value="2">Option 2</option>
      </select>
      
      <button type="submit">Submit</button>
    </form>
  );
}
```

### Controlled Form

```tsx
import { useState } from 'react';

interface FormData {
  name: string;
  email: string;
  message: string;
  subscribe: boolean;
  plan: string;
}

function ContactForm() {
  const [formData, setFormData] = useState<FormData>({
    name: '',
    email: '',
    message: '',
    subscribe: false,
    plan: 'basic',
  });

  const [errors, setErrors] = useState<Partial<FormData>>({});

  // Handle input change
  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>
  ) => {
    const { name, value, type } = e.target;
    
    setFormData(prev => ({
      ...prev,
      [name]: type === 'checkbox' 
        ? (e.target as HTMLInputElement).checked 
        : value
    }));
  };

  // Validation
  const validate = (): boolean => {
    const newErrors: Partial<FormData> = {};

    if (!formData.name) newErrors.name = 'Name is required';
    if (!formData.email) newErrors.email = 'Email is required';
    if (!formData.message) newErrors.message = 'Message is required';

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  // Handle submit
  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    if (validate()) {
      console.log('Form data:', formData);
      // Send to API
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <div>
        <label htmlFor="name">Name</label>
        <input
          id="name"
          name="name"
          type="text"
          value={formData.name}
          onChange={handleChange}
        />
        {errors.name && <span className="error">{errors.name}</span>}
      </div>

      <div>
        <label htmlFor="email">Email</label>
        <input
          id="email"
          name="email"
          type="email"
          value={formData.email}
          onChange={handleChange}
        />
        {errors.email && <span className="error">{errors.email}</span>}
      </div>

      <div>
        <label htmlFor="message">Message</label>
        <textarea
          id="message"
          name="message"
          value={formData.message}
          onChange={handleChange}
        />
        {errors.message && <span className="error">{errors.message}</span>}
      </div>

      <div>
        <label>
          <input
            name="subscribe"
            type="checkbox"
            checked={formData.subscribe}
            onChange={handleChange}
          />
          Subscribe to newsletter
        </label>
      </div>

      <div>
        <label htmlFor="plan">Plan</label>
        <select
          id="plan"
          name="plan"
          value={formData.plan}
          onChange={handleChange}
        >
          <option value="basic">Basic</option>
          <option value="pro">Pro</option>
          <option value="enterprise">Enterprise</option>
        </select>
      </div>

      <button type="submit">Submit</button>
    </form>
  );
}
```

### Form with React Hook Form + Zod

```tsx
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';

// Schema de validação
const contactSchema = z.object({
  name: z.string().min(3, 'Nome deve ter pelo menos 3 caracteres'),
  email: z.string().email('Email inválido'),
  message: z.string().min(10, 'Mensagem muito curta'),
  subscribe: z.boolean().default(false),
});

type ContactFormData = z.infer<typeof contactSchema>;

function ContactForm() {
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<ContactFormData>({
    resolver: zodResolver(contactSchema),
  });

  const onSubmit = async (data: ContactFormData) => {
    console.log('Form data:', data);
    // Send to API
    await new Promise(resolve => setTimeout(resolve, 1000));
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <div>
        <label htmlFor="name">Name</label>
        <input id="name" {...register('name')} />
        {errors.name && <span className="error">{errors.name.message}</span>}
      </div>

      <div>
        <label htmlFor="email">Email</label>
        <input id="email" type="email" {...register('email')} />
        {errors.email && <span className="error">{errors.email.message}</span>}
      </div>

      <div>
        <label htmlFor="message">Message</label>
        <textarea id="message" {...register('message')} />
        {errors.message && <span className="error">{errors.message.message}</span>}
      </div>

      <div>
        <label>
          <input type="checkbox" {...register('subscribe')} />
          Subscribe
        </label>
      </div>

      <button type="submit" disabled={isSubmitting}>
        {isSubmitting ? 'Sending...' : 'Submit'}
      </button>
    </form>
  );
}
```

---

## 🎨 Styling

### CSS Modules

```tsx
// Button.module.css
.button {
  padding: 0.5rem 1rem;
  border-radius: 0.25rem;
}

.primary {
  background: blue;
  color: white;
}

.secondary {
  background: gray;
  color: white;
}

// Button.tsx
import styles from './Button.module.css';

interface ButtonProps {
  variant: 'primary' | 'secondary';
  children: React.ReactNode;
}

function Button({ variant, children }: ButtonProps) {
  return (
    <button className={`${styles.button} ${styles[variant]}`}>
      {children}
    </button>
  );
}
```

### Styled Components

```tsx
import styled from 'styled-components';

// Styled component com props
interface ButtonProps {
  $variant: 'primary' | 'secondary';
  $size?: 'small' | 'medium' | 'large';
}

const StyledButton = styled.button<ButtonProps>`
  padding: ${props => {
    switch (props.$size) {
      case 'small': return '0.25rem 0.5rem';
      case 'large': return '1rem 2rem';
      default: return '0.5rem 1rem';
    }
  }};
  
  background: ${props => 
    props.$variant === 'primary' ? 'blue' : 'gray'
  };
  
  color: white;
  border: none;
  border-radius: 0.25rem;
  cursor: pointer;
  
  &:hover {
    opacity: 0.8;
  }
`;

// Uso
function App() {
  return (
    <>
      <StyledButton $variant="primary" $size="small">
        Small Primary
      </StyledButton>
      
      <StyledButton $variant="secondary" $size="large">
        Large Secondary
      </StyledButton>
    </>
  );
}
```

### Tailwind CSS (Recomendado)

```tsx
import { clsx } from 'clsx';

interface ButtonProps {
  variant: 'primary' | 'secondary' | 'danger';
  size?: 'sm' | 'md' | 'lg';
  children: React.ReactNode;
  onClick?: () => void;
}

function Button({ variant, size = 'md', children, onClick }: ButtonProps) {
  return (
    <button
      onClick={onClick}
      className={clsx(
        // Base styles
        'rounded font-semibold transition',
        
        // Size variants
        {
          'px-3 py-1 text-sm': size === 'sm',
          'px-4 py-2 text-base': size === 'md',
          'px-6 py-3 text-lg': size === 'lg',
        },
        
        // Color variants
        {
          'bg-blue-500 hover:bg-blue-700 text-white': variant === 'primary',
          'bg-gray-500 hover:bg-gray-700 text-white': variant === 'secondary',
          'bg-red-500 hover:bg-red-700 text-white': variant === 'danger',
        }
      )}
    >
      {children}
    </button>
  );
}

// Uso
<Button variant="primary" size="lg">Primary</Button>
<Button variant="danger" size="sm">Delete</Button>
```

---

## 🛣️ React Router

### Setup & Basic Routes

```bash
npm install react-router-dom
```

```tsx
import { BrowserRouter, Routes, Route, Link } from 'react-router-dom';

function App() {
  return (
    <BrowserRouter>
      <nav>
        <Link to="/">Home</Link>
        <Link to="/about">About</Link>
        <Link to="/users">Users</Link>
      </nav>

      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/about" element={<AboutPage />} />
        <Route path="/users" element={<UsersPage />} />
        <Route path="/users/:id" element={<UserDetailPage />} />
        <Route path="*" element={<NotFoundPage />} />
      </Routes>
    </BrowserRouter>
  );
}
```

### Router Hooks

```tsx
import { 
  useNavigate, 
  useParams, 
  useSearchParams,
  useLocation 
} from 'react-router-dom';

function UserDetailPage() {
  // URL params: /users/:id
  const { id } = useParams<{ id: string }>();
  
  // Query params: /users?search=john&page=2
  const [searchParams, setSearchParams] = useSearchParams();
  const search = searchParams.get('search');
  const page = searchParams.get('page');
  
  // Navigate programmatically
  const navigate = useNavigate();
  
  // Location info
  const location = useLocation();

  const goBack = () => {
    navigate(-1); // Go back
  };

  const goToUser = (userId: string) => {
    navigate(`/users/${userId}`);
  };

  const updateSearch = (term: string) => {
    setSearchParams({ search: term, page: '1' });
  };

  return (
    <div>
      <h1>User {id}</h1>
      <p>Search: {search}</p>
      <p>Page: {page}</p>
      <p>Current path: {location.pathname}</p>
      
      <button onClick={goBack}>Back</button>
      <button onClick={() => goToUser('123')}>Go to user 123</button>
      <button onClick={() => updateSearch('john')}>Search John</button>
    </div>
  );
}
```

### Protected Routes

```tsx
import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from './contexts/AuthContext';

function ProtectedRoute() {
  const { isAuthenticated } = useAuth();

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  return <Outlet />; // Renderiza rotas filhas
}

// Uso
function App() {
  return (
    <Routes>
      <Route path="/login" element={<LoginPage />} />
      
      {/* Rotas protegidas */}
      <Route element={<ProtectedRoute />}>
        <Route path="/dashboard" element={<DashboardPage />} />
        <Route path="/profile" element={<ProfilePage />} />
        <Route path="/settings" element={<SettingsPage />} />
      </Route>
    </Routes>
  );
}
```

---

## ⚡ Performance

### React.memo

```tsx
import { memo } from 'react';

interface ChildProps {
  name: string;
  count: number;
}

// Componente que só re-renderiza se props mudarem
const Child = memo(({ name, count }: ChildProps) => {
  console.log('Child rendered');
  return (
    <div>
      {name}: {count}
    </div>
  );
});

// Com comparação customizada
const ChildWithCustomCompare = memo(
  ({ name, count }: ChildProps) => {
    return <div>{name}: {count}</div>;
  },
  (prevProps, nextProps) => {
    // Retornar true = NÃO re-renderizar
    // Retornar false = re-renderizar
    return prevProps.count === nextProps.count;
  }
);
```

### Code Splitting (Lazy Loading)

```tsx
import { lazy, Suspense } from 'react';

// Lazy load components
const Dashboard = lazy(() => import('./pages/Dashboard'));
const Settings = lazy(() => import('./pages/Settings'));

function App() {
  return (
    <Routes>
      <Route 
        path="/dashboard" 
        element={
          <Suspense fallback={<div>Loading Dashboard...</div>}>
            <Dashboard />
          </Suspense>
        } 
      />
      
      <Route 
        path="/settings" 
        element={
          <Suspense fallback={<div>Loading Settings...</div>}>
            <Settings />
          </Suspense>
        } 
      />
    </Routes>
  );
}
```

---

## 🧪 Testing

### Vitest + React Testing Library

```bash
npm install -D vitest @testing-library/react @testing-library/jest-dom
```

```tsx
// Button.test.tsx
import { render, screen, fireEvent } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import { Button } from './Button';

describe('Button', () => {
  it('renders with label', () => {
    render(<Button label="Click me" onClick={() => {}} />);
    
    expect(screen.getByText('Click me')).toBeInTheDocument();
  });

  it('calls onClick when clicked', () => {
    const handleClick = vi.fn();
    render(<Button label="Click me" onClick={handleClick} />);
    
    fireEvent.click(screen.getByText('Click me'));
    
    expect(handleClick).toHaveBeenCalledTimes(1);
  });

  it('is disabled when disabled prop is true', () => {
    render(<Button label="Click me" onClick={() => {}} disabled />);
    
    expect(screen.getByRole('button')).toBeDisabled();
  });
});
```

---

## ✅ Best Practices

### 1. Sempre Tipar Props

```tsx
// ❌ Ruim: Props sem tipo
function Button({ label, onClick }) {
  return <button onClick={onClick}>{label}</button>;
}

// ✅ Bom: Props tipadas
interface ButtonProps {
  label: string;
  onClick: () => void;
}

function Button({ label, onClick }: ButtonProps) {
  return <button onClick={onClick}>{label}</button>;
}
```

### 2. Use Type Inference

```tsx
// ❌ Redundante
const [count, setCount] = useState<number>(0);

// ✅ Type inference automático
const [count, setCount] = useState(0); // number
```

### 3. Evite `any`

```tsx
// ❌ Ruim
function process(data: any) {
  return data.value; // Sem type safety
}

// ✅ Bom
interface Data {
  value: string;
}

function process(data: Data) {
  return data.value; // Type safe!
}

// ✅ Ou use unknown + type guard
function process(data: unknown) {
  if (typeof data === 'object' && data !== null && 'value' in data) {
    return (data as { value: string }).value;
  }
  throw new Error('Invalid data');
}
```

### 4. Organize Imports

```tsx
// ✅ Ordem recomendada
// 1. React
import { useState, useEffect } from 'react';

// 2. Bibliotecas externas
import { useQuery } from '@tanstack/react-query';
import clsx from 'clsx';

// 3. Componentes internos
import { Button } from '@/components/ui/Button';
import { Card } from '@/components/ui/Card';

// 4. Hooks personalizados
import { useAuth } from '@/hooks/useAuth';

// 5. Types
import type { User } from '@/types/user';

// 6. Utils
import { formatDate } from '@/utils/date';

// 7. Estilos
import styles from './Component.module.css';
```

### 5. Use FC (Function Component) com Moderação

```tsx
// ❌ Não recomendado (FC é menos flexível)
const Button: React.FC<ButtonProps> = ({ children }) => {
  return <button>{children}</button>;
};

// ✅ Recomendado (mais flexível, melhor inferência)
function Button({ children }: ButtonProps) {
  return <button>{children}</button>;
}

// Ou arrow function
const Button = ({ children }: ButtonProps) => {
  return <button>{children}</button>;
};
```

### 6. Props Spreading com Types

```tsx
// ✅ Extender props HTML nativas
interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: 'primary' | 'secondary';
}

function Button({ variant = 'primary', ...rest }: ButtonProps) {
  return <button {...rest} className={variant} />;
}

// Uso - todas props HTML funcionam!
<Button 
  variant="primary"
  onClick={() => {}}
  disabled
  type="submit"
  aria-label="Submit"
/>
```

### 7. Evite Inline Functions em JSX

```tsx
// ❌ Ruim (nova função a cada render)
<button onClick={() => handleClick(id)}>Click</button>

// ✅ Bom (com useCallback)
const handleClickWithId = useCallback(() => {
  handleClick(id);
}, [id]);

<button onClick={handleClickWithId}>Click</button>

// ✅ Ou extrair para função
function handleButtonClick() {
  handleClick(id);
}

<button onClick={handleButtonClick}>Click</button>
```

### 8. Use Const Assertions

```tsx
// ✅ Const assertion para arrays
const COLORS = ['red', 'green', 'blue'] as const;
type Color = typeof COLORS[number]; // 'red' | 'green' | 'blue'

// ✅ Const assertion para objetos
const CONFIG = {
  apiUrl: 'https://api.example.com',
  timeout: 5000,
} as const;

type Config = typeof CONFIG;
// { readonly apiUrl: "https://api.example.com"; readonly timeout: 5000; }
```

---

## 🎯 Exemplo Completo: TODO App

```tsx
// types/todo.ts
export interface Todo {
  id: string;
  title: string;
  completed: boolean;
  createdAt: Date;
}

export type CreateTodoDto = Omit<Todo, 'id' | 'createdAt'>;

// hooks/useTodos.ts
import { useState } from 'react';
import { Todo, CreateTodoDto } from '../types/todo';

export function useTodos() {
  const [todos, setTodos] = useState<Todo[]>([]);

  const addTodo = (dto: CreateTodoDto) => {
    const newTodo: Todo = {
      id: crypto.randomUUID(),
      ...dto,
      createdAt: new Date(),
    };
    setTodos(prev => [...prev, newTodo]);
  };

  const toggleTodo = (id: string) => {
    setTodos(prev =>
      prev.map(todo =>
        todo.id === id ? { ...todo, completed: !todo.completed } : todo
      )
    );
  };

  const deleteTodo = (id: string) => {
    setTodos(prev => prev.filter(todo => todo.id !== id));
  };

  return { todos, addTodo, toggleTodo, deleteTodo };
}

// components/TodoForm.tsx
import { useState } from 'react';
import { CreateTodoDto } from '../types/todo';

interface TodoFormProps {
  onSubmit: (dto: CreateTodoDto) => void;
}

export function TodoForm({ onSubmit }: TodoFormProps) {
  const [title, setTitle] = useState('');

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!title.trim()) return;

    onSubmit({ title, completed: false });
    setTitle('');
  };

  return (
    <form onSubmit={handleSubmit} className="flex gap-2">
      <input
        type="text"
        value={title}
        onChange={(e) => setTitle(e.target.value)}
        placeholder="New todo..."
        className="flex-1 px-3 py-2 border rounded"
      />
      <button
        type="submit"
        className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-700"
      >
        Add
      </button>
    </form>
  );
}

// components/TodoItem.tsx
import { Todo } from '../types/todo';

interface TodoItemProps {
  todo: Todo;
  onToggle: (id: string) => void;
  onDelete: (id: string) => void;
}

export function TodoItem({ todo, onToggle, onDelete }: TodoItemProps) {
  return (
    <div className="flex items-center gap-3 p-3 border rounded">
      <input
        type="checkbox"
        checked={todo.completed}
        onChange={() => onToggle(todo.id)}
      />
      
      <span className={todo.completed ? 'line-through text-gray-500' : ''}>
        {todo.title}
      </span>
      
      <button
        onClick={() => onDelete(todo.id)}
        className="ml-auto px-3 py-1 bg-red-500 text-white rounded hover:bg-red-700"
      >
        Delete
      </button>
    </div>
  );
}

// App.tsx
import { TodoForm } from './components/TodoForm';
import { TodoItem } from './components/TodoItem';
import { useTodos } from './hooks/useTodos';

export function App() {
  const { todos, addTodo, toggleTodo, deleteTodo } = useTodos();

  const activeTodos = todos.filter(todo => !todo.completed);
  const completedTodos = todos.filter(todo => todo.completed);

  return (
    <div className="max-w-2xl mx-auto p-8">
      <h1 className="text-3xl font-bold mb-6">Todo App</h1>
      
      <TodoForm onSubmit={addTodo} />
      
      <div className="mt-8">
        <h2 className="text-xl font-semibold mb-4">
          Active ({activeTodos.length})
        </h2>
        <div className="space-y-2">
          {activeTodos.map(todo => (
            <TodoItem
              key={todo.id}
              todo={todo}
              onToggle={toggleTodo}
              onDelete={deleteTodo}
            />
          ))}
        </div>
      </div>
      
      {completedTodos.length > 0 && (
        <div className="mt-8">
          <h2 className="text-xl font-semibold mb-4">
            Completed ({completedTodos.length})
          </h2>
          <div className="space-y-2">
            {completedTodos.map(todo => (
              <TodoItem
                key={todo.id}
                todo={todo}
                onToggle={toggleTodo}
                onDelete={deleteTodo}
              />
            ))}
          </div>
        </div>
      )}
    </div>
  );
}
```

---

## 📚 Recursos Adicionais

### Documentação
- [React Docs](https://react.dev) - Documentação oficial React 18+
- [TypeScript Handbook](https://www.typescriptlang.org/docs/handbook/intro.html)
- [React TypeScript Cheatsheet](https://react-typescript-cheatsheet.netlify.app/)

### Ferramentas
- **ESLint** - Linting TypeScript + React
- **Prettier** - Formatação de código
- **Vite** - Build tool rápido
- **Vitest** - Testing framework

### Bibliotecas Essenciais
```bash
# Forms
npm install react-hook-form zod @hookform/resolvers

# Routing
npm install react-router-dom

# State Management
npm install @tanstack/react-query zustand

# Styling
npm install tailwindcss clsx

# UI Components
npm install @radix-ui/react-dialog @radix-ui/react-dropdown-menu
```

---

**Voltar para**: [📁 Frameworks](../../../README.md) | [📁 JavaScript](../../../../README.md) | [📁 Learning](../../../../../README.md) | [📁 Repositório Principal](../../../../../../README.md)
