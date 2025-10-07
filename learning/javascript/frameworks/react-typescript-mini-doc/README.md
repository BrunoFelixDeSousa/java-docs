# ⚛️ React + TypeScript - Guia Completo Moderno# ⚛️ React + TypeScript - Guia Completo Moderno



**React + TypeScript** é a combinação perfeita para construir aplicações web **type-safe**, escaláveis e mantíveis.**React + TypeScript** é a combinação perfeita para construir aplicações web **type-safe**, escaláveis e mantíveis.



> "Build type-safe React applications with confidence"> "Build type-safe React applications with confidence"



[![React](https://img.shields.io/badge/React-18+-61DAFB?style=flat&logo=react&logoColor=white)](https://react.dev)[![React](https://img.shields.io/badge/React-18+-61DAFB?style=flat&logo=react&logoColor=white)](https://react.dev)

[![TypeScript](https://img.shields.io/badge/TypeScript-5+-3178C6?style=flat&logo=typescript&logoColor=white)](https://www.typescriptlang.org/)[![TypeScript](https://img.shields.io/badge/TypeScript-5+-3178C6?style=flat&logo=typescript&logoColor=white)](https://www.typescriptlang.org/)



------



## 📋 Índice## 📋 Índice



- [Por que React + TypeScript?](#-por-que-react--typescript)- [Por que React + TypeScript?](#-por-que-react--typescript)

- [Setup & Instalação](#-setup--instalação)- [Setup & Instalação](#-setup--instalação)

- [Componentes](#-componentes)- [Componentes](#-componentes)

- [Props & Children](#-props--children)- [Props & Children](#-props--children)

- [Hooks Básicos](#-hooks-básicos)- [Hooks Básicos](#-hooks-básicos)

- [Hooks Avançados](#-hooks-avançados)- [Hooks Avançados](#-hooks-avançados)

- [Custom Hooks](#-custom-hooks)- [Custom Hooks](#-custom-hooks)

- [Context API](#-context-api)- [Context API](#-context-api)

- [Events & Forms](#-events--forms)- [Events & Forms](#-events--forms)

- [Styling](#-styling)- [Styling](#-styling)

- [React Router](#-react-router)- [React Router](#-react-router)

- [Performance](#-performance)- [Performance](#-performance)

- [Testing](#-testing)- [Testing](#-testing)

- [Best Practices](#-best-practices)- [Best Practices](#-best-practices)



------



## 🎯 Por que React + TypeScript?## 🎯 Por que React + TypeScript?



### ✅ Comparação: JavaScript vs TypeScript### ✅ Comparação: JavaScript vs TypeScript



```typescript```typescript

// ❌ JavaScript: Props não tipadas// ❌ JavaScript: Props não tipadas

function Button({ label, onClick }) {function Button({ label, onClick }) {

  return <button onClick={onClick}>{label}</button>;  return <button onClick={onClick}>{label}</button>;

}}



// Problemas:// Problemas:

<Button label={123} onClick="não é função" /> // Sem erro em dev ⚠️<Button label={123} onClick="não é função" /> // Sem erro em dev ⚠️

<Button /> // label e onClick undefined ⚠️<Button /> // label e onClick undefined ⚠️



// ✅ TypeScript: Props tipadas e seguras// ✅ TypeScript: Props tipadas e seguras

interface ButtonProps {interface ButtonProps {

  label: string;  label: string;

  onClick: () => void;  onClick: () => void;

}}



function Button({ label, onClick }: ButtonProps) {function Button({ label, onClick }: ButtonProps) {

  return <button onClick={onClick}>{label}</button>;  return <button onClick={onClick}>{label}</button>;

}}



// Erros detectados ANTES de executar! ✨// Erros detectados ANTES de executar! ✨

<Button label={123} onClick="erro" /> // ❌ Type error<Button label={123} onClick="erro" /> // ❌ Type error

<Button /> // ❌ Missing required props<Button /> // ❌ Missing required props

<Button label="OK" onClick={() => {}} /> // ✅ Correto<Button label="OK" onClick={() => {}} /> // ✅ Correto

``````



### 🚀 Vantagens do TypeScript no React### 🚀 Vantagens do TypeScript no React



| Vantagem | Descrição || Vantagem | Descrição |

|----------|-----------||----------|-----------|

| **IntelliSense** | Autocomplete de props, states, eventos || **IntelliSense** | Autocomplete de props, states, eventos |

| **Type Safety** | Erros detectados em compile-time || **Type Safety** | Erros detectados em compile-time |

| **Refactoring** | Rename/move com segurança || **Refactoring** | Rename/move com segurança |

| **Documentation** | Types servem como documentação || **Documentation** | Types servem como documentação |

| **Less Bugs** | Menos erros em produção || **Less Bugs** | Menos erros em produção |

| **Better DX** | Developer Experience superior || **Better DX** | Developer Experience superior |



------



## 📦 Setup & Instalação## 📦 Setup & Instalação



### Vite (Recomendado - Rápido)### Vite (Recomendado - Rápido)



```bash```bash

# Criar projeto# Criar projeto

npm create vite@latest my-app -- --template react-tsnpm create vite@latest my-app -- --template react-ts



cd my-appcd my-app

npm installnpm install

npm run devnpm run dev

``````



### Next.js 14+ (Full-stack)### Next.js 14+ (Full-stack)



```bash```bash

npx create-next-app@latest my-app --typescript --tailwind --appnpx create-next-app@latest my-app --typescript --tailwind --app



cd my-appcd my-app

npm run devnpm run dev

``````



### Create React App (Legacy)### Create React App (Legacy)



```bash```bash

npx create-react-app my-app --template typescriptnpx create-react-app my-app --template typescript

``````



------



### tsconfig.json (Recomendado)### tsconfig.json (Recomendado)



```json```json

{{

  "compilerOptions": {  "compilerOptions": {

    "target": "ES2020",    "target": "ES2020",

    "lib": ["ES2020", "DOM", "DOM.Iterable"],    "lib": ["ES2020", "DOM", "DOM.Iterable"],

    "module": "ESNext",    "module": "ESNext",

    "moduleResolution": "bundler",    "moduleResolution": "bundler",

        

    // React específico    // React específico

    "jsx": "react-jsx",              // React 17+ (sem import React)    "jsx": "react-jsx",              // React 17+ (sem import React)

        

    // Type checking rigoroso    // Type checking rigoroso

    "strict": true,    "strict": true,

    "noImplicitAny": true,    "noImplicitAny": true,

    "strictNullChecks": true,    "strictNullChecks": true,

    "strictFunctionTypes": true,    "strictFunctionTypes": true,

        

    // Code quality    // Code quality

    "noUnusedLocals": true,    "noUnusedLocals": true,

    "noUnusedParameters": true,    "noUnusedParameters": true,

    "noFallthroughCasesInSwitch": true,    "noFallthroughCasesInSwitch": true,

        

    // Paths    // Paths

    "baseUrl": ".",    "baseUrl": ".",

    "paths": {    "paths": {

      "@/*": ["./src/*"],      "@/*": ["./src/*"],

      "@components/*": ["./src/components/*"],      "@components/*": ["./src/components/*"],

      "@hooks/*": ["./src/hooks/*"]      "@hooks/*": ["./src/hooks/*"]

    },    },

        

    // Outros    // Outros

    "esModuleInterop": true,    "esModuleInterop": true,

    "skipLibCheck": true,    "skipLibCheck": true,

    "resolveJsonModule": true,    "resolveJsonModule": true,

    "isolatedModules": true    "isolatedModules": true

  },  },

  "include": ["src"],  "include": ["src"],

  "exclude": ["node_modules"]  "exclude": ["node_modules"]

}}

``````



------



### Estrutura de Projeto (Recomendada)### Estrutura de Projeto (Recomendada)



``````

src/src/

├── components/          # Componentes React├── components/          # Componentes React

│   ├── ui/             # Componentes de UI (Button, Input)│   ├── ui/             # Componentes de UI (Button, Input)

│   ├── layout/         # Layout (Header, Footer, Sidebar)│   ├── layout/         # Layout (Header, Footer, Sidebar)

│   └── features/       # Componentes de features│   └── features/       # Componentes de features

││

├── hooks/              # Custom hooks├── hooks/              # Custom hooks

│   ├── useAuth.ts│   ├── useAuth.ts

│   └── useFetch.ts│   └── useFetch.ts

││

├── contexts/           # Context API├── contexts/           # Context API

│   └── AuthContext.tsx│   └── AuthContext.tsx

││

├── types/              # TypeScript types├── types/              # TypeScript types

│   ├── api.ts│   ├── api.ts

│   └── models.ts│   └── models.ts

││

├── utils/              # Funções utilitárias├── utils/              # Funções utilitárias

│   └── helpers.ts│   └── helpers.ts

││

├── services/           # API calls├── services/           # API calls

│   └── api.ts│   └── api.ts

││

├── App.tsx├── App.tsx

└── main.tsx└── main.tsx

``````



------



## 🧩 Componentes## 🧩 Componentes



### Componente Funcional Básico### Componente Funcional Básico



```tsx```tsx

// Componente sem props// Componente sem props

function Welcome() {function Welcome() {

  return <h1>Hello, World!</h1>;  return <h1>Hello, World!</h1>;

}}



// Export padrão// Export padrão

export default Welcome;export default Welcome;



// Named export (melhor para treeshaking)// Named export (melhor para treeshaking)

export function Welcome() {export function Welcome() {

  return <h1>Hello, World!</h1>;  return <h1>Hello, World!</h1>;

}}



// Arrow function// Arrow function

const Welcome = () => {const Welcome = () => {

  return <h1>Hello, World!</h1>;  return <h1>Hello, World!</h1>;

};};



// Arrow function com return implícito// Arrow function com return implícito

const Welcome = () => <h1>Hello, World!</h1>;const Welcome = () => <h1>Hello, World!</h1>;

``````



### Componente com State### Componente com State



```tsx```tsx

import { useState } from 'react';import { useState } from 'react';



function Counter() {function Counter() {

  // useState com tipo inferido  // useState com tipo inferido

  const [count, setCount] = useState(0); // count: number  const [count, setCount] = useState(0); // count: number



  return (  return (

    <div>    <div>

      <p>Count: {count}</p>      <p>Count: {count}</p>

      <button onClick={() => setCount(count + 1)}>Increment</button>      <button onClick={() => setCount(count + 1)}>Increment</button>

    </div>    </div>

  );  );

}}

``````



### Componente com State Complexo### Componente com State Complexo



```tsx```tsx

import { useState } from 'react';import { useState } from 'react';



// Definir interface para o state// Definir interface para o state

interface User {interface User {

  id: number;  id: number;

  name: string;  name: string;

  email: string;  email: string;

}}



function UserProfile() {function UserProfile() {

  // State tipado explicitamente  // State tipado explicitamente

  const [user, setUser] = useState<User | null>(null);  const [user, setUser] = useState<User | null>(null);

    

  // State com valor inicial  // State com valor inicial

  const [users, setUsers] = useState<User[]>([]);  const [users, setUsers] = useState<User[]>([]);

    

  const loadUser = () => {  const loadUser = () => {

    setUser({    setUser({

      id: 1,      id: 1,

      name: 'João Silva',      name: 'João Silva',

      email: 'joao@example.com'      email: 'joao@example.com'

    });    });

  };  };



  return (  return (

    <div>    <div>

      {user ? (      {user ? (

        <div>        <div>

          <h2>{user.name}</h2>          <h2>{user.name}</h2>

          <p>{user.email}</p>          <p>{user.email}</p>

        </div>        </div>

      ) : (      ) : (

        <button onClick={loadUser}>Load User</button>        <button onClick={loadUser}>Load User</button>

      )}      )}

    </div>    </div>

  );  );

}}

``````



------



## 📤 Props & Children## 📤 Props & Children



### Props Interface### Props Interface



```tsx```tsx

// Interface para props// Interface para props

interface ButtonProps {interface ButtonProps {

  label: string;  label: string;

  onClick: () => void;  onClick: () => void;

  disabled?: boolean;        // Opcional  disabled?: boolean;        // Opcional

  variant?: 'primary' | 'secondary'; // Union type  variant?: 'primary' | 'secondary'; // Union type

}}



function Button({ label, onClick, disabled = false, variant = 'primary' }: ButtonProps) {function Button({ label, onClick, disabled = false, variant = 'primary' }: ButtonProps) {

  return (  return (

    <button     <button 

      onClick={onClick}       onClick={onClick} 

      disabled={disabled}      disabled={disabled}

      className={variant === 'primary' ? 'btn-primary' : 'btn-secondary'}      className={variant === 'primary' ? 'btn-primary' : 'btn-secondary'}

    >    >

      {label}      {label}

    </button>    </button>

  );  );

}}



// Uso// Uso

<Button label="Click me" onClick={() => console.log('Clicked')} /><Button label="Click me" onClick={() => console.log('Clicked')} />

<Button label="Submit" onClick={handleSubmit} variant="primary" disabled /><Button label="Submit" onClick={handleSubmit} variant="primary" disabled />

``````



### Props com Children### Props com Children



```tsx```tsx

// Children como ReactNode (aceita qualquer JSX)// Children como ReactNode (aceita qualquer JSX)

interface CardProps {interface CardProps {

  title: string;  title: string;

  children: React.ReactNode;  children: React.ReactNode;

}}



function Card({ title, children }: CardProps) {function Card({ title, children }: CardProps) {

  return (  return (

    <div className="card">    <div className="card">

      <h2>{title}</h2>      <h2>{title}</h2>

      <div className="card-content">      <div className="card-content">

        {children}        {children}

      </div>      </div>

    </div>    </div>

  );  );

}}



// Uso// Uso

<Card title="My Card"><Card title="My Card">

  <p>Card content</p>  <p>Card content</p>

  <button>Action</button>  <button>Action</button>

</Card></Card>

``````



### Props com Type Alias### Props com Type Alias



```tsx```tsx

// Usar type ao invés de interface (preferência pessoal)// Usar type ao invés de interface (preferência pessoal)

type ButtonProps = {type ButtonProps = {

  label: string;  label: string;

  onClick: () => void;  onClick: () => void;

  disabled?: boolean;  disabled?: boolean;

};};



// Equivalente ao exemplo anterior// Equivalente ao exemplo anterior

function Button({ label, onClick, disabled }: ButtonProps) {function Button({ label, onClick, disabled }: ButtonProps) {

  return <button onClick={onClick} disabled={disabled}>{label}</button>;  return <button onClick={onClick} disabled={disabled}>{label}</button>;

}}

``````



### Props com Spread### Props com Spread



```tsx```tsx

// Extender props nativas de HTML// Extender props nativas de HTML

interface InputProps extends React.InputHTMLAttributes<HTMLInputElement> {interface InputProps extends React.InputHTMLAttributes<HTMLInputElement> {

  label: string;  label: string;

  error?: string;  error?: string;

}}



function Input({ label, error, ...rest }: InputProps) {function Input({ label, error, ...rest }: InputProps) {

  return (  return (

    <div>    <div>

      <label>{label}</label>      <label>{label}</label>

      <input {...rest} /> {/* Todas props HTML nativas funcionam */}      <input {...rest} /> {/* Todas props HTML nativas funcionam */}

      {error && <span className="error">{error}</span>}      {error && <span className="error">{error}</span>}

    </div>    </div>

  );  );

}}



// Uso - todas props de <input> funcionam!// Uso - todas props de <input> funcionam!

<Input <Input 

  label="Email"   label="Email" 

  type="email"   type="email" 

  placeholder="you@example.com"  placeholder="you@example.com"

  required  required

  error="Email inválido"  error="Email inválido"

/>/>

``````



### Props com Generics### Props com Generics



```tsx```tsx

// Componente genérico (funciona com qualquer tipo de item)// Componente genérico (funciona com qualquer tipo de item)

interface ListProps<T> {interface ListProps<T> {

  items: T[];  items: T[];

  renderItem: (item: T) => React.ReactNode;  renderItem: (item: T) => React.ReactNode;

}}



function List<T>({ items, renderItem }: ListProps<T>) {function List<T>({ items, renderItem }: ListProps<T>) {

  return (  return (

    <ul>    <ul>

      {items.map((item, index) => (      {items.map((item, index) => (

        <li key={index}>{renderItem(item)}</li>        <li key={index}>{renderItem(item)}</li>

      ))}      ))}

    </ul>    </ul>

  );  );

}}



// Uso com diferentes tipos// Uso com diferentes tipos

interface User {interface User {

  id: number;  id: number;

  name: string;  name: string;

}}



<List <List 

  items={users}                    // User[]  items={users}                    // User[]

  renderItem={(user) => user.name} // Type inference automático! ✨  renderItem={(user) => user.name} // Type inference automático! ✨

/>/>



<List <List 

  items={[1, 2, 3]}               // number[]  items={[1, 2, 3]}               // number[]

  renderItem={(num) => num * 2}   // Type inference funciona!  renderItem={(num) => num * 2}   // Type inference funciona!

/>/>

``````



------



## 🪝 Hooks Básicos## 🪝 Hooks Básicos



### useState### useState



```tsx```tsx

import { useState } from 'react';import { useState } from 'react';



function Examples() {function Examples() {

  // Type inference automático  // Type inference automático

  const [count, setCount] = useState(0);        // number  const [count, setCount] = useState(0);        // number

  const [name, setName] = useState('');         // string  const [name, setName] = useState('');         // string

  const [isOpen, setIsOpen] = useState(false);  // boolean  const [isOpen, setIsOpen] = useState(false);  // boolean

    

  // Type explícito quando necessário  // Type explícito quando necessário

  const [user, setUser] = useState<User | null>(null);  const [user, setUser] = useState<User | null>(null);

    

  // State com array  // State com array

  const [items, setItems] = useState<string[]>([]);  const [items, setItems] = useState<string[]>([]);

    

  // State com objeto  // State com objeto

  interface FormData {  interface FormData {

    name: string;    name: string;

    email: string;    email: string;

  }  }

    

  const [formData, setFormData] = useState<FormData>({  const [formData, setFormData] = useState<FormData>({

    name: '',    name: '',

    email: ''    email: ''

  });  });

    

  // Atualizar state objeto  // Atualizar state objeto

  const updateName = (name: string) => {  const updateName = (name: string) => {

    setFormData(prev => ({ ...prev, name }));    setFormData(prev => ({ ...prev, name }));

  };  };

    

  // Atualizar state array  // Atualizar state array

  const addItem = (item: string) => {  const addItem = (item: string) => {

    setItems(prev => [...prev, item]);    setItems(prev => [...prev, item]);

  };  };



  return <div>Examples</div>;  return <div>Examples</div>;

}}

``````



### useEffect### useEffect



```tsx```tsx

import { useState, useEffect } from 'react';import { useState, useEffect } from 'react';



function UserProfile({ userId }: { userId: number }) {function UserProfile({ userId }: { userId: number }) {

  const [user, setUser] = useState<User | null>(null);  const [user, setUser] = useState<User | null>(null);

  const [loading, setLoading] = useState(true);  const [loading, setLoading] = useState(true);



  // Effect básico  // Effect básico

  useEffect(() => {  useEffect(() => {

    console.log('Component mounted');    console.log('Component mounted');

        

    // Cleanup (executado quando componente desmonta)    // Cleanup (executado quando componente desmonta)

    return () => {    return () => {

      console.log('Component unmounted');      console.log('Component unmounted');

    };    };

  }, []); // [] = executa apenas uma vez  }, []); // [] = executa apenas uma vez



  // Effect com dependências  // Effect com dependências

  useEffect(() => {  useEffect(() => {

    const fetchUser = async () => {    const fetchUser = async () => {

      setLoading(true);      setLoading(true);

      try {      try {

        const response = await fetch(`/api/users/${userId}`);        const response = await fetch(`/api/users/${userId}`);

        const data = await response.json();        const data = await response.json();

        setUser(data);        setUser(data);

      } catch (error) {      } catch (error) {

        console.error('Error fetching user:', error);        console.error('Error fetching user:', error);

      } finally {      } finally {

        setLoading(false);        setLoading(false);

      }      }

    };    };



    fetchUser();    fetchUser();

  }, [userId]); // Re-executa quando userId mudar  }, [userId]); // Re-executa quando userId mudar



  // Effect com cleanup (WebSocket, timers, etc)  // Effect com cleanup (WebSocket, timers, etc)

  useEffect(() => {  useEffect(() => {

    const interval = setInterval(() => {    const interval = setInterval(() => {

      console.log('Tick');      console.log('Tick');

    }, 1000);    }, 1000);



    // Cleanup: limpar interval ao desmontar    // Cleanup: limpar interval ao desmontar

    return () => clearInterval(interval);    return () => clearInterval(interval);

  }, []);  }, []);



  if (loading) return <div>Loading...</div>;  if (loading) return <div>Loading...</div>;

  if (!user) return <div>User not found</div>;  if (!user) return <div>User not found</div>;



  return <div>{user.name}</div>;  return <div>{user.name}</div>;

}}

``````



### useRef### useRef



```tsx```tsx

import { useRef, useEffect } from 'react';import { useRef, useEffect } from 'react';



function Examples() {function Examples() {

  // Ref para elemento DOM  // Ref para elemento DOM

  const inputRef = useRef<HTMLInputElement>(null);  const inputRef = useRef<HTMLInputElement>(null);

    

  // Ref para valor mutável (não causa re-render)  // Ref para valor mutável (não causa re-render)

  const countRef = useRef(0);  const countRef = useRef(0);

    

  // Ref para armazenar timeout  // Ref para armazenar timeout

  const timeoutRef = useRef<number>();  const timeoutRef = useRef<number>();



  useEffect(() => {  useEffect(() => {

    // Focar input ao montar    // Focar input ao montar

    inputRef.current?.focus();    inputRef.current?.focus();

  }, []);  }, []);



  const startTimer = () => {  const startTimer = () => {

    timeoutRef.current = window.setTimeout(() => {    timeoutRef.current = window.setTimeout(() => {

      console.log('Timer finished');      console.log('Timer finished');

    }, 1000);    }, 1000);

  };  };



  const stopTimer = () => {  const stopTimer = () => {

    if (timeoutRef.current) {    if (timeoutRef.current) {

      clearTimeout(timeoutRef.current);      clearTimeout(timeoutRef.current);

    }    }

  };  };



  return (  return (

    <div>    <div>

      <input ref={inputRef} type="text" />      <input ref={inputRef} type="text" />

            

      <button onClick={() => {      <button onClick={() => {

        // Incrementar sem re-render        // Incrementar sem re-render

        countRef.current += 1;        countRef.current += 1;

        console.log(countRef.current);        console.log(countRef.current);

      }}>      }}>

        Increment (no re-render)        Increment (no re-render)

      </button>      </button>

            

      <button onClick={startTimer}>Start Timer</button>      <button onClick={startTimer}>Start Timer</button>

      <button onClick={stopTimer}>Stop Timer</button>      <button onClick={stopTimer}>Stop Timer</button>

    </div>    </div>

  );  );

}}

``````



### useContext### useContext



```tsx```tsx

import { createContext, useContext, useState, ReactNode } from 'react';import { createContext, useContext, useState, ReactNode } from 'react';



// 1. Definir tipo do contexto// 1. Definir tipo do contexto

interface AuthContextType {interface AuthContextType {

  user: User | null;  user: User | null;

  login: (email: string, password: string) => Promise<void>;  login: (email: string, password: string) => Promise<void>;

  logout: () => void;  logout: () => void;

  isAuthenticated: boolean;  isAuthenticated: boolean;

}}



// 2. Criar contexto com undefined inicial// 2. Criar contexto com undefined inicial

const AuthContext = createContext<AuthContextType | undefined>(undefined);const AuthContext = createContext<AuthContextType | undefined>(undefined);



// 3. Provider component// 3. Provider component

interface AuthProviderProps {interface AuthProviderProps {

  children: ReactNode;  children: ReactNode;

}}



export function AuthProvider({ children }: AuthProviderProps) {export function AuthProvider({ children }: AuthProviderProps) {

  const [user, setUser] = useState<User | null>(null);  const [user, setUser] = useState<User | null>(null);



  const login = async (email: string, password: string) => {  const login = async (email: string, password: string) => {

    // Simular login    // Simular login

    const response = await fetch('/api/login', {    const response = await fetch('/api/login', {

      method: 'POST',      method: 'POST',

      body: JSON.stringify({ email, password }),      body: JSON.stringify({ email, password }),

    });    });

    const userData = await response.json();    const userData = await response.json();

    setUser(userData);    setUser(userData);

  };  };



  const logout = () => {  const logout = () => {

    setUser(null);    setUser(null);

  };  };



  const value: AuthContextType = {  const value: AuthContextType = {

    user,    user,

    login,    login,

    logout,    logout,

    isAuthenticated: !!user,    isAuthenticated: !!user,

  };  };



  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;

}}



// 4. Custom hook para usar o contexto// 4. Custom hook para usar o contexto

export function useAuth() {export function useAuth() {

  const context = useContext(AuthContext);  const context = useContext(AuthContext);

    

  if (context === undefined) {  if (context === undefined) {

    throw new Error('useAuth must be used within AuthProvider');    throw new Error('useAuth must be used within AuthProvider');

  }  }

    

  return context;  return context;

}}



// 5. Uso em componentes// 5. Uso em componentes

function LoginForm() {function LoginForm() {

  const { login, isAuthenticated } = useAuth(); // Type-safe! ✨  const { login, isAuthenticated } = useAuth(); // Type-safe! ✨

  const [email, setEmail] = useState('');  const [email, setEmail] = useState('');

  const [password, setPassword] = useState('');  const [password, setPassword] = useState('');



  const handleSubmit = async (e: React.FormEvent) => {  const handleSubmit = async (e: React.FormEvent) => {

    e.preventDefault();    e.preventDefault();

    await login(email, password);    await login(email, password);

  };  };



  if (isAuthenticated) {  if (isAuthenticated) {

    return <div>Already logged in!</div>;    return <div>Already logged in!</div>;

  }  }



  return (  return (

    <form onSubmit={handleSubmit}>    <form onSubmit={handleSubmit}>

      <input       <input 

        type="email"         type="email" 

        value={email}         value={email} 

        onChange={(e) => setEmail(e.target.value)}         onChange={(e) => setEmail(e.target.value)} 

      />      />

      <input       <input 

        type="password"         type="password" 

        value={password}         value={password} 

        onChange={(e) => setPassword(e.target.value)}         onChange={(e) => setPassword(e.target.value)} 

      />      />

      <button type="submit">Login</button>      <button type="submit">Login</button>

    </form>    </form>

  );  );

}}



// 6. Uso no App// 6. Uso no App

function App() {function App() {

  return (  return (

    <AuthProvider>    <AuthProvider>

      <LoginForm />      <LoginForm />

    </AuthProvider>    </AuthProvider>

  );  );

}}

``````



------



## 🔥 Hooks Avançados## 🔥 Hooks Avançados



### useReducer### useReducer



```tsx```tsx

import { useReducer } from 'react';import { useReducer } from 'react';



// 1. Definir State type// 1. Definir State type

interface CounterState {interface CounterState {

  count: number;  count: number;

  step: number;  step: number;

}}



// 2. Definir Action types// 2. Definir Action types

type CounterAction =type CounterAction =

  | { type: 'INCREMENT' }  | { type: 'INCREMENT' }

  | { type: 'DECREMENT' }  | { type: 'DECREMENT' }

  | { type: 'SET_STEP'; payload: number }  | { type: 'SET_STEP'; payload: number }

  | { type: 'RESET' };  | { type: 'RESET' };



// 3. Reducer function// 3. Reducer function

function counterReducer(state: CounterState, action: CounterAction): CounterState {function counterReducer(state: CounterState, action: CounterAction): CounterState {

  switch (action.type) {  switch (action.type) {

    case 'INCREMENT':    case 'INCREMENT':

      return { ...state, count: state.count + state.step };      return { ...state, count: state.count + state.step };

        

    case 'DECREMENT':    case 'DECREMENT':

      return { ...state, count: state.count - state.step };      return { ...state, count: state.count - state.step };

        

    case 'SET_STEP':    case 'SET_STEP':

      return { ...state, step: action.payload };      return { ...state, step: action.payload };

        

    case 'RESET':    case 'RESET':

      return { count: 0, step: 1 };      return { count: 0, step: 1 };

        

    default:    default:

      return state;      return state;

  }  }

}}



// 4. Componente// 4. Componente

function Counter() {function Counter() {

  const [state, dispatch] = useReducer(counterReducer, {  const [state, dispatch] = useReducer(counterReducer, {

    count: 0,    count: 0,

    step: 1,    step: 1,

  });  });



  return (  return (

    <div>    <div>

      <p>Count: {state.count}</p>      <p>Count: {state.count}</p>

      <p>Step: {state.step}</p>      <p>Step: {state.step}</p>

            

      <button onClick={() => dispatch({ type: 'INCREMENT' })}>      <button onClick={() => dispatch({ type: 'INCREMENT' })}>

        + {state.step}        + {state.step}

      </button>      </button>

            

      <button onClick={() => dispatch({ type: 'DECREMENT' })}>      <button onClick={() => dispatch({ type: 'DECREMENT' })}>

        - {state.step}        - {state.step}

      </button>      </button>

            

      <button onClick={() => dispatch({ type: 'SET_STEP', payload: 5 })}>      <button onClick={() => dispatch({ type: 'SET_STEP', payload: 5 })}>

        Set step to 5        Set step to 5

      </button>      </button>

            

      <button onClick={() => dispatch({ type: 'RESET' })}>      <button onClick={() => dispatch({ type: 'RESET' })}>

        Reset        Reset

      </button>      </button>

    </div>    </div>

  );  );

}}

``````



### useMemo### useMemo



```tsx```tsx

import { useState, useMemo } from 'react';import { useState, useMemo } from 'react';



function ExpensiveComponent() {function ExpensiveComponent() {

  const [count, setCount] = useState(0);  const [count, setCount] = useState(0);

  const [items, setItems] = useState<number[]>([]);  const [items, setItems] = useState<number[]>([]);



  // Computação cara que só executa quando items mudar  // Computação cara que só executa quando items mudar

  const total = useMemo(() => {  const total = useMemo(() => {

    console.log('Calculating total...');    console.log('Calculating total...');

    return items.reduce((sum, item) => sum + item, 0);    return items.reduce((sum, item) => sum + item, 0);

  }, [items]); // Só recalcula se items mudar  }, [items]); // Só recalcula se items mudar



  // Filtro caro  // Filtro caro

  const expensiveItems = useMemo(() => {  const expensiveItems = useMemo(() => {

    console.log('Filtering expensive items...');    console.log('Filtering expensive items...');

    return items.filter(item => item > 100);    return items.filter(item => item > 100);

  }, [items]);  }, [items]);



  return (  return (

    <div>    <div>

      <p>Count: {count}</p>      <p>Count: {count}</p>

      <p>Total: {total}</p>      <p>Total: {total}</p>

      <p>Expensive items: {expensiveItems.length}</p>      <p>Expensive items: {expensiveItems.length}</p>

            

      {/* count muda mas total NÃO recalcula (useMemo) */}      {/* count muda mas total NÃO recalcula (useMemo) */}

      <button onClick={() => setCount(c => c + 1)}>      <button onClick={() => setCount(c => c + 1)}>

        Increment count        Increment count

      </button>      </button>

            

      <button onClick={() => setItems([...items, Math.random() * 200])}>      <button onClick={() => setItems([...items, Math.random() * 200])}>

        Add item        Add item

      </button>      </button>

    </div>    </div>

  );  );

}}

``````



### useCallback### useCallback



```tsx```tsx

import { useState, useCallback } from 'react';import { useState, useCallback } from 'react';



// Componente filho// Componente filho

interface ChildProps {interface ChildProps {

  onSave: (data: string) => void;  onSave: (data: string) => void;

}}



const Child = ({ onSave }: ChildProps) => {const Child = ({ onSave }: ChildProps) => {

  console.log('Child rendered');  console.log('Child rendered');

  return <button onClick={() => onSave('data')}>Save</button>;  return <button onClick={() => onSave('data')}>Save</button>;

};};



// Componente pai// Componente pai

function Parent() {function Parent() {

  const [count, setCount] = useState(0);  const [count, setCount] = useState(0);



  // ❌ Sem useCallback: nova função a cada render  // ❌ Sem useCallback: nova função a cada render

  const handleSave = (data: string) => {  const handleSave = (data: string) => {

    console.log('Saving:', data);    console.log('Saving:', data);

  };  };



  // ✅ Com useCallback: mesma função (não re-renderiza Child)  // ✅ Com useCallback: mesma função (não re-renderiza Child)

  const handleSaveOptimized = useCallback((data: string) => {  const handleSaveOptimized = useCallback((data: string) => {

    console.log('Saving:', data);    console.log('Saving:', data);

  }, []); // [] = função nunca muda  }, []); // [] = função nunca muda



  // useCallback com dependências  // useCallback com dependências

  const handleSaveWithCount = useCallback((data: string) => {  const handleSaveWithCount = useCallback((data: string) => {

    console.log('Saving:', data, 'count:', count);    console.log('Saving:', data, 'count:', count);

  }, [count]); // Nova função quando count mudar  }, [count]); // Nova função quando count mudar



  return (  return (

    <div>    <div>

      <p>Count: {count}</p>      <p>Count: {count}</p>

      <button onClick={() => setCount(c => c + 1)}>Increment</button>      <button onClick={() => setCount(c => c + 1)}>Increment</button>

            

      {/* Child re-renderiza a cada vez (handleSave muda) */}      {/* Child re-renderiza a cada vez (handleSave muda) */}

      <Child onSave={handleSave} />      <Child onSave={handleSave} />

            

      {/* Child NÃO re-renderiza (handleSaveOptimized é estável) */}      {/* Child NÃO re-renderiza (handleSaveOptimized é estável) */}

      <Child onSave={handleSaveOptimized} />      <Child onSave={handleSaveOptimized} />

    </div>    </div>

  );  );

}}

``````



### useTransition (React 18+)### useTransition (React 18+)



```tsx```tsx

import { useState, useTransition } from 'react';import { useState, useTransition } from 'react';



function SearchComponent() {function SearchComponent() {

  const [query, setQuery] = useState('');  const [query, setQuery] = useState('');

  const [results, setResults] = useState<string[]>([]);  const [results, setResults] = useState<string[]>([]);

    

  // isPending: indica se transição está em andamento  // isPending: indica se transição está em andamento

  // startTransition: marca atualização como não-urgente  // startTransition: marca atualização como não-urgente

  const [isPending, startTransition] = useTransition();  const [isPending, startTransition] = useTransition();



  const handleSearch = (value: string) => {  const handleSearch = (value: string) => {

    setQuery(value); // Atualização urgente (input)    setQuery(value); // Atualização urgente (input)

        

    // Atualização não-urgente (busca)    // Atualização não-urgente (busca)

    startTransition(() => {    startTransition(() => {

      // Simular busca cara      // Simular busca cara

      const filtered = expensiveSearch(value);      const filtered = expensiveSearch(value);

      setResults(filtered);      setResults(filtered);

    });    });

  };  };



  return (  return (

    <div>    <div>

      <input       <input 

        type="text"         type="text" 

        value={query}         value={query} 

        onChange={(e) => handleSearch(e.target.value)}        onChange={(e) => handleSearch(e.target.value)}

        placeholder="Search..."        placeholder="Search..."

      />      />

            

      {isPending && <span>Searching...</span>}      {isPending && <span>Searching...</span>}

            

      <ul>      <ul>

        {results.map((result, i) => (        {results.map((result, i) => (

          <li key={i}>{result}</li>          <li key={i}>{result}</li>

        ))}        ))}

      </ul>      </ul>

    </div>    </div>

  );  );

}}



function expensiveSearch(query: string): string[] {function expensiveSearch(query: string): string[] {

  // Simular busca cara  // Simular busca cara

  const items = Array.from({ length: 10000 }, (_, i) => `Item ${i}`);  const items = Array.from({ length: 10000 }, (_, i) => `Item ${i}`);

  return items.filter(item => item.includes(query));  return items.filter(item => item.includes(query));

}}

``````



------



## 🎣 Custom Hooks## 🎣 Custom Hooks



### useFetch Hook### useFetch Hook



```tsx```tsx

import { useState, useEffect } from 'react';import { useState, useEffect } from 'react';



interface UseFetchResult<T> {interface UseFetchResult<T> {

  data: T | null;  data: T | null;

  loading: boolean;  loading: boolean;

  error: Error | null;  error: Error | null;

  refetch: () => void;  refetch: () => void;

}}



function useFetch<T>(url: string): UseFetchResult<T> {function useFetch<T>(url: string): UseFetchResult<T> {

  const [data, setData] = useState<T | null>(null);  const [data, setData] = useState<T | null>(null);

  const [loading, setLoading] = useState(true);  const [loading, setLoading] = useState(true);

  const [error, setError] = useState<Error | null>(null);  const [error, setError] = useState<Error | null>(null);



  const fetchData = async () => {  const fetchData = async () => {

    setLoading(true);    setLoading(true);

    setError(null);    setError(null);

        

    try {    try {

      const response = await fetch(url);      const response = await fetch(url);

      if (!response.ok) throw new Error('Failed to fetch');      if (!response.ok) throw new Error('Failed to fetch');

            

      const json = await response.json();      const json = await response.json();

      setData(json);      setData(json);

    } catch (err) {    } catch (err) {

      setError(err as Error);      setError(err as Error);

    } finally {    } finally {

      setLoading(false);      setLoading(false);

    }    }

  };  };



  useEffect(() => {  useEffect(() => {

    fetchData();    fetchData();

  }, [url]);  }, [url]);



  return { data, loading, error, refetch: fetchData };  return { data, loading, error, refetch: fetchData };

}}



// Uso// Uso

interface User {interface User {

  id: number;  id: number;

  name: string;  name: string;

}}



function UserList() {function UserList() {

  const { data: users, loading, error, refetch } = useFetch<User[]>('/api/users');  const { data: users, loading, error, refetch } = useFetch<User[]>('/api/users');



  if (loading) return <div>Loading...</div>;  if (loading) return <div>Loading...</div>;

  if (error) return <div>Error: {error.message}</div>;  if (error) return <div>Error: {error.message}</div>;

  if (!users) return null;  if (!users) return null;



  return (  return (

    <div>    <div>

      <button onClick={refetch}>Refresh</button>      <button onClick={refetch}>Refresh</button>

      <ul>      <ul>

        {users.map(user => (        {users.map(user => (

          <li key={user.id}>{user.name}</li>          <li key={user.id}>{user.name}</li>

        ))}        ))}

      </ul>      </ul>

    </div>    </div>

  );  );

}}

``````



### useLocalStorage Hook### useLocalStorage Hook



```tsx```tsx

import { useState, useEffect } from 'react';import { useState, useEffect } from 'react';



function useLocalStorage<T>(key: string, initialValue: T) {function useLocalStorage<T>(key: string, initialValue: T) {

  // State com lazy initialization  // State com lazy initialization

  const [value, setValue] = useState<T>(() => {  const [value, setValue] = useState<T>(() => {

    try {    try {

      const item = window.localStorage.getItem(key);      const item = window.localStorage.getItem(key);

      return item ? JSON.parse(item) : initialValue;      return item ? JSON.parse(item) : initialValue;

    } catch (error) {    } catch (error) {

      console.error(error);      console.error(error);

      return initialValue;      return initialValue;

    }    }

  });  });



  // Atualizar localStorage quando value mudar  // Atualizar localStorage quando value mudar

  useEffect(() => {  useEffect(() => {

    try {    try {

      window.localStorage.setItem(key, JSON.stringify(value));      window.localStorage.setItem(key, JSON.stringify(value));

    } catch (error) {    } catch (error) {

      console.error(error);      console.error(error);

    }    }

  }, [key, value]);  }, [key, value]);



  return [value, setValue] as const;  return [value, setValue] as const;

}}



// Uso// Uso

function App() {function App() {

  const [name, setName] = useLocalStorage('name', '');  const [name, setName] = useLocalStorage('name', '');

  const [count, setCount] = useLocalStorage('count', 0);  const [count, setCount] = useLocalStorage('count', 0);



  return (  return (

    <div>    <div>

      <input       <input 

        value={name}         value={name} 

        onChange={(e) => setName(e.target.value)}         onChange={(e) => setName(e.target.value)} 

      />      />

      <button onClick={() => setCount(count + 1)}>      <button onClick={() => setCount(count + 1)}>

        Count: {count}        Count: {count}

      </button>      </button>

    </div>    </div>

  );  );

}}

``````



### useDebounce Hook### useDebounce Hook



```tsx```tsx

import { useState, useEffect } from 'react';import { useState, useEffect } from 'react';



function useDebounce<T>(value: T, delay: number): T {function useDebounce<T>(value: T, delay: number): T {

  const [debouncedValue, setDebouncedValue] = useState<T>(value);  const [debouncedValue, setDebouncedValue] = useState<T>(value);



  useEffect(() => {  useEffect(() => {

    const handler = setTimeout(() => {    const handler = setTimeout(() => {

      setDebouncedValue(value);      setDebouncedValue(value);

    }, delay);    }, delay);



    return () => {    return () => {

      clearTimeout(handler);      clearTimeout(handler);

    };    };

  }, [value, delay]);  }, [value, delay]);



  return debouncedValue;  return debouncedValue;

}}



// Uso: Search com debounce// Uso: Search com debounce

function SearchComponent() {function SearchComponent() {

  const [searchTerm, setSearchTerm] = useState('');  const [searchTerm, setSearchTerm] = useState('');

  const debouncedSearchTerm = useDebounce(searchTerm, 500);  const debouncedSearchTerm = useDebounce(searchTerm, 500);



  useEffect(() => {  useEffect(() => {

    if (debouncedSearchTerm) {    if (debouncedSearchTerm) {

      // Fazer busca (só depois de 500ms sem digitar)      // Fazer busca (só depois de 500ms sem digitar)

      console.log('Searching for:', debouncedSearchTerm);      console.log('Searching for:', debouncedSearchTerm);

    }    }

  }, [debouncedSearchTerm]);  }, [debouncedSearchTerm]);



  return (  return (

    <input    <input

      type="text"      type="text"

      value={searchTerm}      value={searchTerm}

      onChange={(e) => setSearchTerm(e.target.value)}      onChange={(e) => setSearchTerm(e.target.value)}

      placeholder="Search..."      placeholder="Search..."

    />    />

  );  );

}}

``````



------



## 🌐 Context API## 🌐 Context API



### Theme Context (Exemplo Completo)### Theme Context (Exemplo Completo)



```tsx```tsx

import { createContext, useContext, useState, ReactNode } from 'react';import { createContext, useContext, useState, ReactNode } from 'react';



// 1. Types// 1. Types

type Theme = 'light' | 'dark';type Theme = 'light' | 'dark';



interface ThemeContextType {interface ThemeContextType {

  theme: Theme;  theme: Theme;

  toggleTheme: () => void;  toggleTheme: () => void;

}}



// 2. Context// 2. Context

const ThemeContext = createContext<ThemeContextType | undefined>(undefined);const ThemeContext = createContext<ThemeContextType | undefined>(undefined);



// 3. Provider// 3. Provider

interface ThemeProviderProps {interface ThemeProviderProps {

  children: ReactNode;  children: ReactNode;

}}



export function ThemeProvider({ children }: ThemeProviderProps) {export function ThemeProvider({ children }: ThemeProviderProps) {

  const [theme, setTheme] = useState<Theme>('light');  const [theme, setTheme] = useState<Theme>('light');



  const toggleTheme = () => {  const toggleTheme = () => {

    setTheme(prev => prev === 'light' ? 'dark' : 'light');    setTheme(prev => prev === 'light' ? 'dark' : 'light');

  };  };



  return (  return (

    <ThemeContext.Provider value={{ theme, toggleTheme }}>    <ThemeContext.Provider value={{ theme, toggleTheme }}>

      {children}      {children}

    </ThemeContext.Provider>    </ThemeContext.Provider>

  );  );

}}



// 4. Custom Hook// 4. Custom Hook

export function useTheme() {export function useTheme() {

  const context = useContext(ThemeContext);  const context = useContext(ThemeContext);

    

  if (!context) {  if (!context) {

    throw new Error('useTheme must be used within ThemeProvider');    throw new Error('useTheme must be used within ThemeProvider');

  }  }

    

  return context;  return context;

}}



// 5. Uso// 5. Uso

function App() {function App() {

  return (  return (

    <ThemeProvider>    <ThemeProvider>

      <ThemedComponent />      <ThemedComponent />

    </ThemeProvider>    </ThemeProvider>

  );  );

}}



function ThemedComponent() {function ThemedComponent() {

  const { theme, toggleTheme } = useTheme();  const { theme, toggleTheme } = useTheme();



  return (  return (

    <div className={theme}>    <div className={theme}>

      <p>Current theme: {theme}</p>      <p>Current theme: {theme}</p>

      <button onClick={toggleTheme}>      <button onClick={toggleTheme}>

        Toggle Theme        Toggle Theme

      </button>      </button>

    </div>    </div>

  );  );

}}

``````



------



## 📝 Events & Forms## 📝 Events & Forms



### Event Handlers### Event Handlers



```tsx```tsx

function EventExamples() {function EventExamples() {

  // Click event  // Click event

  const handleClick = (e: React.MouseEvent<HTMLButtonElement>) => {  const handleClick = (e: React.MouseEvent<HTMLButtonElement>) => {

    console.log('Button clicked');    console.log('Button clicked');

    console.log('Coordinates:', e.clientX, e.clientY);    console.log('Coordinates:', e.clientX, e.clientY);

  };  };



  // Change event (input)  // Change event (input)

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {

    console.log('Value:', e.target.value);    console.log('Value:', e.target.value);

  };  };



  // Change event (select)  // Change event (select)

  const handleSelectChange = (e: React.ChangeEvent<HTMLSelectElement>) => {  const handleSelectChange = (e: React.ChangeEvent<HTMLSelectElement>) => {

    console.log('Selected:', e.target.value);    console.log('Selected:', e.target.value);

  };  };



  // Submit event  // Submit event

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {

    e.preventDefault();    e.preventDefault();

    console.log('Form submitted');    console.log('Form submitted');

  };  };



  // Key event  // Key event

  const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {  const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {

    if (e.key === 'Enter') {    if (e.key === 'Enter') {

      console.log('Enter pressed');      console.log('Enter pressed');

    }    }

  };  };



  // Focus event  // Focus event

  const handleFocus = (e: React.FocusEvent<HTMLInputElement>) => {  const handleFocus = (e: React.FocusEvent<HTMLInputElement>) => {

    console.log('Input focused');    console.log('Input focused');

  };  };



  return (  return (

    <form onSubmit={handleSubmit}>    <form onSubmit={handleSubmit}>

      <button onClick={handleClick}>Click me</button>      <button onClick={handleClick}>Click me</button>

            

      <input       <input 

        type="text"         type="text" 

        onChange={handleChange}        onChange={handleChange}

        onKeyDown={handleKeyDown}        onKeyDown={handleKeyDown}

        onFocus={handleFocus}        onFocus={handleFocus}

      />      />

            

      <select onChange={handleSelectChange}>      <select onChange={handleSelectChange}>

        <option value="1">Option 1</option>        <option value="1">Option 1</option>

        <option value="2">Option 2</option>        <option value="2">Option 2</option>

      </select>      </select>

            

      <button type="submit">Submit</button>      <button type="submit">Submit</button>

    </form>    </form>

  );  );

}}

``````



### Controlled Form### Controlled Form



```tsx```tsx

import { useState } from 'react';import { useState } from 'react';



interface FormData {interface FormData {

  name: string;  name: string;

  email: string;  email: string;

  message: string;  message: string;

  subscribe: boolean;  subscribe: boolean;

  plan: string;  plan: string;

}}



function ContactForm() {function ContactForm() {

  const [formData, setFormData] = useState<FormData>({  const [formData, setFormData] = useState<FormData>({

    name: '',    name: '',

    email: '',    email: '',

    message: '',    message: '',

    subscribe: false,    subscribe: false,

    plan: 'basic',    plan: 'basic',

  });  });



  const [errors, setErrors] = useState<Partial<FormData>>({});  const [errors, setErrors] = useState<Partial<FormData>>({});



  // Handle input change  // Handle input change

  const handleChange = (  const handleChange = (

    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>

  ) => {  ) => {

    const { name, value, type } = e.target;    const { name, value, type } = e.target;

        

    setFormData(prev => ({    setFormData(prev => ({

      ...prev,      ...prev,

      [name]: type === 'checkbox'       [name]: type === 'checkbox' 

        ? (e.target as HTMLInputElement).checked         ? (e.target as HTMLInputElement).checked 

        : value        : value

    }));    }));

  };  };



  // Validation  // Validation

  const validate = (): boolean => {  const validate = (): boolean => {

    const newErrors: Partial<FormData> = {};    const newErrors: Partial<FormData> = {};



    if (!formData.name) newErrors.name = 'Name is required';    if (!formData.name) newErrors.name = 'Name is required';

    if (!formData.email) newErrors.email = 'Email is required';    if (!formData.email) newErrors.email = 'Email is required';

    if (!formData.message) newErrors.message = 'Message is required';    if (!formData.message) newErrors.message = 'Message is required';



    setErrors(newErrors);    setErrors(newErrors);

    return Object.keys(newErrors).length === 0;    return Object.keys(newErrors).length === 0;

  };  };



  // Handle submit  // Handle submit

  const handleSubmit = (e: React.FormEvent) => {  const handleSubmit = (e: React.FormEvent) => {

    e.preventDefault();    e.preventDefault();



    if (validate()) {    if (validate()) {

      console.log('Form data:', formData);      console.log('Form data:', formData);

      // Send to API      // Send to API

    }    }

  };  };



  return (  return (

    <form onSubmit={handleSubmit}>    <form onSubmit={handleSubmit}>

      <div>      <div>

        <label htmlFor="name">Name</label>        <label htmlFor="name">Name</label>

        <input        <input

          id="name"          id="name"

          name="name"          name="name"

          type="text"          type="text"

          value={formData.name}          value={formData.name}

          onChange={handleChange}          onChange={handleChange}

        />        />

        {errors.name && <span className="error">{errors.name}</span>}        {errors.name && <span className="error">{errors.name}</span>}

      </div>      </div>



      <div>      <div>

        <label htmlFor="email">Email</label>        <label htmlFor="email">Email</label>

        <input        <input

          id="email"          id="email"

          name="email"          name="email"

          type="email"          type="email"

          value={formData.email}          value={formData.email}

          onChange={handleChange}          onChange={handleChange}

        />        />

        {errors.email && <span className="error">{errors.email}</span>}        {errors.email && <span className="error">{errors.email}</span>}

      </div>      </div>



      <div>      <div>

        <label htmlFor="message">Message</label>        <label htmlFor="message">Message</label>

        <textarea        <textarea

          id="message"          id="message"

          name="message"          name="message"

          value={formData.message}          value={formData.message}

          onChange={handleChange}          onChange={handleChange}

        />        />

        {errors.message && <span className="error">{errors.message}</span>}        {errors.message && <span className="error">{errors.message}</span>}

      </div>      </div>



      <div>      <div>

        <label>        <label>

          <input          <input

            name="subscribe"            name="subscribe"

            type="checkbox"            type="checkbox"

            checked={formData.subscribe}            checked={formData.subscribe}

            onChange={handleChange}            onChange={handleChange}

          />          />

          Subscribe to newsletter          Subscribe to newsletter

        </label>        </label>

      </div>      </div>



      <div>      <div>

        <label htmlFor="plan">Plan</label>        <label htmlFor="plan">Plan</label>

        <select        <select

          id="plan"          id="plan"

          name="plan"          name="plan"

          value={formData.plan}          value={formData.plan}

          onChange={handleChange}          onChange={handleChange}

        >        >

          <option value="basic">Basic</option>          <option value="basic">Basic</option>

          <option value="pro">Pro</option>          <option value="pro">Pro</option>

          <option value="enterprise">Enterprise</option>          <option value="enterprise">Enterprise</option>

        </select>        </select>

      </div>      </div>



      <button type="submit">Submit</button>      <button type="submit">Submit</button>

    </form>    </form>

  );  );

}}

``````



### Form with React Hook Form + Zod### Form with React Hook Form + Zod



```tsx```tsx

import { useForm } from 'react-hook-form';import { useForm } from 'react-hook-form';

import { zodResolver } from '@hookform/resolvers/zod';import { zodResolver } from '@hookform/resolvers/zod';

import { z } from 'zod';import { z } from 'zod';



// Schema de validação// Schema de validação

const contactSchema = z.object({const contactSchema = z.object({

  name: z.string().min(3, 'Nome deve ter pelo menos 3 caracteres'),  name: z.string().min(3, 'Nome deve ter pelo menos 3 caracteres'),

  email: z.string().email('Email inválido'),  email: z.string().email('Email inválido'),

  message: z.string().min(10, 'Mensagem muito curta'),  message: z.string().min(10, 'Mensagem muito curta'),

  subscribe: z.boolean().default(false),  subscribe: z.boolean().default(false),

});});



type ContactFormData = z.infer<typeof contactSchema>;type ContactFormData = z.infer<typeof contactSchema>;



function ContactForm() {function ContactForm() {

  const {  const {

    register,    register,

    handleSubmit,    handleSubmit,

    formState: { errors, isSubmitting },    formState: { errors, isSubmitting },

  } = useForm<ContactFormData>({  } = useForm<ContactFormData>({

    resolver: zodResolver(contactSchema),    resolver: zodResolver(contactSchema),

  });  });



  const onSubmit = async (data: ContactFormData) => {  const onSubmit = async (data: ContactFormData) => {

    console.log('Form data:', data);    console.log('Form data:', data);

    // Send to API    // Send to API

    await new Promise(resolve => setTimeout(resolve, 1000));    await new Promise(resolve => setTimeout(resolve, 1000));

  };  };



  return (  return (

    <form onSubmit={handleSubmit(onSubmit)}>    <form onSubmit={handleSubmit(onSubmit)}>

      <div>      <div>

        <label htmlFor="name">Name</label>        <label htmlFor="name">Name</label>

        <input id="name" {...register('name')} />        <input id="name" {...register('name')} />

        {errors.name && <span className="error">{errors.name.message}</span>}        {errors.name && <span className="error">{errors.name.message}</span>}

      </div>      </div>



      <div>      <div>

        <label htmlFor="email">Email</label>        <label htmlFor="email">Email</label>

        <input id="email" type="email" {...register('email')} />        <input id="email" type="email" {...register('email')} />

        {errors.email && <span className="error">{errors.email.message}</span>}        {errors.email && <span className="error">{errors.email.message}</span>}

      </div>      </div>



      <div>      <div>

        <label htmlFor="message">Message</label>        <label htmlFor="message">Message</label>

        <textarea id="message" {...register('message')} />        <textarea id="message" {...register('message')} />

        {errors.message && <span className="error">{errors.message.message}</span>}        {errors.message && <span className="error">{errors.message.message}</span>}

      </div>      </div>



      <div>      <div>

        <label>        <label>

          <input type="checkbox" {...register('subscribe')} />          <input type="checkbox" {...register('subscribe')} />

          Subscribe          Subscribe

        </label>        </label>

      </div>      </div>



      <button type="submit" disabled={isSubmitting}>      <button type="submit" disabled={isSubmitting}>

        {isSubmitting ? 'Sending...' : 'Submit'}        {isSubmitting ? 'Sending...' : 'Submit'}

      </button>      </button>

    </form>    </form>

  );  );

}}

``````



------



## 🎨 Styling## 🎨 Styling



### CSS Modules### CSS Modules



```tsx```tsx

// Button.module.css// Button.module.css

.button {.button {

  padding: 0.5rem 1rem;  padding: 0.5rem 1rem;

  border-radius: 0.25rem;  border-radius: 0.25rem;

}}



.primary {.primary {

  background: blue;  background: blue;

  color: white;  color: white;

}}



.secondary {.secondary {

  background: gray;  background: gray;

  color: white;  color: white;

}}



// Button.tsx// Button.tsx

import styles from './Button.module.css';import styles from './Button.module.css';



interface ButtonProps {interface ButtonProps {

  variant: 'primary' | 'secondary';  variant: 'primary' | 'secondary';

  children: React.ReactNode;  children: React.ReactNode;

}}



function Button({ variant, children }: ButtonProps) {function Button({ variant, children }: ButtonProps) {

  return (  return (

    <button className={`${styles.button} ${styles[variant]}`}>    <button className={`${styles.button} ${styles[variant]}`}>

      {children}      {children}

    </button>    </button>

  );  );

}}

``````



### Styled Components### Styled Components



```tsx```tsx

import styled from 'styled-components';import styled from 'styled-components';



// Styled component com props// Styled component com props

interface ButtonProps {interface ButtonProps {

  $variant: 'primary' | 'secondary';  $variant: 'primary' | 'secondary';

  $size?: 'small' | 'medium' | 'large';  $size?: 'small' | 'medium' | 'large';

}}



const StyledButton = styled.button<ButtonProps>`const StyledButton = styled.button<ButtonProps>`

  padding: ${props => {  padding: ${props => {

    switch (props.$size) {    switch (props.$size) {

      case 'small': return '0.25rem 0.5rem';      case 'small': return '0.25rem 0.5rem';

      case 'large': return '1rem 2rem';      case 'large': return '1rem 2rem';

      default: return '0.5rem 1rem';      default: return '0.5rem 1rem';

    }    }

  }};  }};

    

  background: ${props =>   background: ${props => 

    props.$variant === 'primary' ? 'blue' : 'gray'    props.$variant === 'primary' ? 'blue' : 'gray'

  };  };

    

  color: white;  color: white;

  border: none;  border: none;

  border-radius: 0.25rem;  border-radius: 0.25rem;

  cursor: pointer;  cursor: pointer;

    

  &:hover {  &:hover {

    opacity: 0.8;    opacity: 0.8;

  }  }

`;`;



// Uso// Uso

function App() {function App() {

  return (  return (

    <>    <>

      <StyledButton $variant="primary" $size="small">      <StyledButton $variant="primary" $size="small">

        Small Primary        Small Primary

      </StyledButton>      </StyledButton>

            

      <StyledButton $variant="secondary" $size="large">      <StyledButton $variant="secondary" $size="large">

        Large Secondary        Large Secondary

      </StyledButton>      </StyledButton>

    </>    </>

  );  );

}}

``````



### Tailwind CSS (Recomendado)### Tailwind CSS (Recomendado)



```tsx```tsx

import { clsx } from 'clsx';import { clsx } from 'clsx';



interface ButtonProps {interface ButtonProps {

  variant: 'primary' | 'secondary' | 'danger';  variant: 'primary' | 'secondary' | 'danger';

  size?: 'sm' | 'md' | 'lg';  size?: 'sm' | 'md' | 'lg';

  children: React.ReactNode;  children: React.ReactNode;

  onClick?: () => void;  onClick?: () => void;

}}



function Button({ variant, size = 'md', children, onClick }: ButtonProps) {function Button({ variant, size = 'md', children, onClick }: ButtonProps) {

  return (  return (

    <button    <button

      onClick={onClick}      onClick={onClick}

      className={clsx(      className={clsx(

        // Base styles        // Base styles

        'rounded font-semibold transition',        'rounded font-semibold transition',

                

        // Size variants        // Size variants

        {        {

          'px-3 py-1 text-sm': size === 'sm',          'px-3 py-1 text-sm': size === 'sm',

          'px-4 py-2 text-base': size === 'md',          'px-4 py-2 text-base': size === 'md',

          'px-6 py-3 text-lg': size === 'lg',          'px-6 py-3 text-lg': size === 'lg',

        },        },

                

        // Color variants        // Color variants

        {        {

          'bg-blue-500 hover:bg-blue-700 text-white': variant === 'primary',          'bg-blue-500 hover:bg-blue-700 text-white': variant === 'primary',

          'bg-gray-500 hover:bg-gray-700 text-white': variant === 'secondary',          'bg-gray-500 hover:bg-gray-700 text-white': variant === 'secondary',

          'bg-red-500 hover:bg-red-700 text-white': variant === 'danger',          'bg-red-500 hover:bg-red-700 text-white': variant === 'danger',

        }        }

      )}      )}

    >    >

      {children}      {children}

    </button>    </button>

  );  );

}}



// Uso// Uso

<Button variant="primary" size="lg">Primary</Button><Button variant="primary" size="lg">Primary</Button>

<Button variant="danger" size="sm">Delete</Button><Button variant="danger" size="sm">Delete</Button>

``````



------



## 🛣️ React Router## 🛣️ React Router



### Setup & Basic Routes### Setup & Basic Routes



```bash```bash

npm install react-router-domnpm install react-router-dom

``````



```tsx```tsx

import { BrowserRouter, Routes, Route, Link } from 'react-router-dom';import { BrowserRouter, Routes, Route, Link } from 'react-router-dom';



function App() {function App() {

  return (  return (

    <BrowserRouter>    <BrowserRouter>

      <nav>      <nav>

        <Link to="/">Home</Link>        <Link to="/">Home</Link>

        <Link to="/about">About</Link>        <Link to="/about">About</Link>

        <Link to="/users">Users</Link>        <Link to="/users">Users</Link>

      </nav>      </nav>



      <Routes>      <Routes>

        <Route path="/" element={<HomePage />} />        <Route path="/" element={<HomePage />} />

        <Route path="/about" element={<AboutPage />} />        <Route path="/about" element={<AboutPage />} />

        <Route path="/users" element={<UsersPage />} />        <Route path="/users" element={<UsersPage />} />

        <Route path="/users/:id" element={<UserDetailPage />} />        <Route path="/users/:id" element={<UserDetailPage />} />

        <Route path="*" element={<NotFoundPage />} />        <Route path="*" element={<NotFoundPage />} />

      </Routes>      </Routes>

    </BrowserRouter>    </BrowserRouter>

  );  );

}}

``````



### Router Hooks### Router Hooks



```tsx```tsx

import { import { 

  useNavigate,   useNavigate, 

  useParams,   useParams, 

  useSearchParams,  useSearchParams,

  useLocation   useLocation 

} from 'react-router-dom';} from 'react-router-dom';



function UserDetailPage() {function UserDetailPage() {

  // URL params: /users/:id  // URL params: /users/:id

  const { id } = useParams<{ id: string }>();  const { id } = useParams<{ id: string }>();

    

  // Query params: /users?search=john&page=2  // Query params: /users?search=john&page=2

  const [searchParams, setSearchParams] = useSearchParams();  const [searchParams, setSearchParams] = useSearchParams();

  const search = searchParams.get('search');  const search = searchParams.get('search');

  const page = searchParams.get('page');  const page = searchParams.get('page');

    

  // Navigate programmatically  // Navigate programmatically

  const navigate = useNavigate();  const navigate = useNavigate();

    

  // Location info  // Location info

  const location = useLocation();  const location = useLocation();



  const goBack = () => {  const goBack = () => {

    navigate(-1); // Go back    navigate(-1); // Go back

  };  };



  const goToUser = (userId: string) => {  const goToUser = (userId: string) => {

    navigate(`/users/${userId}`);    navigate(`/users/${userId}`);

  };  };



  const updateSearch = (term: string) => {  const updateSearch = (term: string) => {

    setSearchParams({ search: term, page: '1' });    setSearchParams({ search: term, page: '1' });

  };  };



  return (  return (

    <div>    <div>

      <h1>User {id}</h1>      <h1>User {id}</h1>

      <p>Search: {search}</p>      <p>Search: {search}</p>

      <p>Page: {page}</p>      <p>Page: {page}</p>

      <p>Current path: {location.pathname}</p>      <p>Current path: {location.pathname}</p>

            

      <button onClick={goBack}>Back</button>      <button onClick={goBack}>Back</button>

      <button onClick={() => goToUser('123')}>Go to user 123</button>      <button onClick={() => goToUser('123')}>Go to user 123</button>

      <button onClick={() => updateSearch('john')}>Search John</button>      <button onClick={() => updateSearch('john')}>Search John</button>

    </div>    </div>

  );  );

}}

``````



### Protected Routes### Protected Routes



```tsx```tsx

import { Navigate, Outlet } from 'react-router-dom';import { Navigate, Outlet } from 'react-router-dom';

import { useAuth } from './contexts/AuthContext';import { useAuth } from './contexts/AuthContext';



function ProtectedRoute() {function ProtectedRoute() {

  const { isAuthenticated } = useAuth();  const { isAuthenticated } = useAuth();



  if (!isAuthenticated) {  if (!isAuthenticated) {

    return <Navigate to="/login" replace />;    return <Navigate to="/login" replace />;

  }  }



  return <Outlet />; // Renderiza rotas filhas  return <Outlet />; // Renderiza rotas filhas

}}



// Uso// Uso

function App() {function App() {

  return (  return (

    <Routes>    <Routes>

      <Route path="/login" element={<LoginPage />} />      <Route path="/login" element={<LoginPage />} />

            

      {/* Rotas protegidas */}      {/* Rotas protegidas */}

      <Route element={<ProtectedRoute />}>      <Route element={<ProtectedRoute />}>

        <Route path="/dashboard" element={<DashboardPage />} />        <Route path="/dashboard" element={<DashboardPage />} />

        <Route path="/profile" element={<ProfilePage />} />        <Route path="/profile" element={<ProfilePage />} />

        <Route path="/settings" element={<SettingsPage />} />        <Route path="/settings" element={<SettingsPage />} />

      </Route>      </Route>

    </Routes>    </Routes>

  );  );

}}

``````



------



## ⚡ Performance## ⚡ Performance



### React.memo### React.memo



```tsx```tsx

import { memo } from 'react';import { memo } from 'react';



interface ChildProps {interface ChildProps {

  name: string;  name: string;

  count: number;  count: number;

}}



// Componente que só re-renderiza se props mudarem// Componente que só re-renderiza se props mudarem

const Child = memo(({ name, count }: ChildProps) => {const Child = memo(({ name, count }: ChildProps) => {

  console.log('Child rendered');  console.log('Child rendered');

  return (  return (

    <div>    <div>

      {name}: {count}      {name}: {count}

    </div>    </div>

  );  );

});});



// Com comparação customizada// Com comparação customizada

const ChildWithCustomCompare = memo(const ChildWithCustomCompare = memo(

  ({ name, count }: ChildProps) => {  ({ name, count }: ChildProps) => {

    return <div>{name}: {count}</div>;    return <div>{name}: {count}</div>;

  },  },

  (prevProps, nextProps) => {  (prevProps, nextProps) => {

    // Retornar true = NÃO re-renderizar    // Retornar true = NÃO re-renderizar

    // Retornar false = re-renderizar    // Retornar false = re-renderizar

    return prevProps.count === nextProps.count;    return prevProps.count === nextProps.count;

  }  }

););

``````



### Code Splitting (Lazy Loading)### Code Splitting (Lazy Loading)



```tsx```tsx

import { lazy, Suspense } from 'react';import { lazy, Suspense } from 'react';



// Lazy load components// Lazy load components

const Dashboard = lazy(() => import('./pages/Dashboard'));const Dashboard = lazy(() => import('./pages/Dashboard'));

const Settings = lazy(() => import('./pages/Settings'));const Settings = lazy(() => import('./pages/Settings'));



function App() {function App() {

  return (  return (

    <Routes>    <Routes>

      <Route       <Route 

        path="/dashboard"         path="/dashboard" 

        element={        element={

          <Suspense fallback={<div>Loading Dashboard...</div>}>          <Suspense fallback={<div>Loading Dashboard...</div>}>

            <Dashboard />            <Dashboard />

          </Suspense>          </Suspense>

        }         } 

      />      />

            

      <Route       <Route 

        path="/settings"         path="/settings" 

        element={        element={

          <Suspense fallback={<div>Loading Settings...</div>}>          <Suspense fallback={<div>Loading Settings...</div>}>

            <Settings />            <Settings />

          </Suspense>          </Suspense>

        }         } 

      />      />

    </Routes>    </Routes>

  );  );

}}

``````



------



## 🧪 Testing## 🧪 Testing



### Vitest + React Testing Library### Vitest + React Testing Library



```bash```bash

npm install -D vitest @testing-library/react @testing-library/jest-domnpm install -D vitest @testing-library/react @testing-library/jest-dom

``````



```tsx```tsx

// Button.test.tsx// Button.test.tsx

import { render, screen, fireEvent } from '@testing-library/react';import { render, screen, fireEvent } from '@testing-library/react';

import { describe, it, expect, vi } from 'vitest';import { describe, it, expect, vi } from 'vitest';

import { Button } from './Button';import { Button } from './Button';



describe('Button', () => {describe('Button', () => {

  it('renders with label', () => {  it('renders with label', () => {

    render(<Button label="Click me" onClick={() => {}} />);    render(<Button label="Click me" onClick={() => {}} />);

        

    expect(screen.getByText('Click me')).toBeInTheDocument();    expect(screen.getByText('Click me')).toBeInTheDocument();

  });  });



  it('calls onClick when clicked', () => {  it('calls onClick when clicked', () => {

    const handleClick = vi.fn();    const handleClick = vi.fn();

    render(<Button label="Click me" onClick={handleClick} />);    render(<Button label="Click me" onClick={handleClick} />);

        

    fireEvent.click(screen.getByText('Click me'));    fireEvent.click(screen.getByText('Click me'));

        

    expect(handleClick).toHaveBeenCalledTimes(1);    expect(handleClick).toHaveBeenCalledTimes(1);

  });  });



  it('is disabled when disabled prop is true', () => {  it('is disabled when disabled prop is true', () => {

    render(<Button label="Click me" onClick={() => {}} disabled />);    render(<Button label="Click me" onClick={() => {}} disabled />);

        

    expect(screen.getByRole('button')).toBeDisabled();    expect(screen.getByRole('button')).toBeDisabled();

  });  });

});});

``````



------



## ✅ Best Practices## ✅ Best Practices



### 1. Sempre Tipar Props### 1. Sempre Tipar Props



```tsx```tsx

// ❌ Ruim: Props sem tipo// ❌ Ruim: Props sem tipo

function Button({ label, onClick }) {function Button({ label, onClick }) {

  return <button onClick={onClick}>{label}</button>;  return <button onClick={onClick}>{label}</button>;

}}



// ✅ Bom: Props tipadas// ✅ Bom: Props tipadas

interface ButtonProps {interface ButtonProps {

  label: string;  label: string;

  onClick: () => void;  onClick: () => void;

}}



function Button({ label, onClick }: ButtonProps) {function Button({ label, onClick }: ButtonProps) {

  return <button onClick={onClick}>{label}</button>;  return <button onClick={onClick}>{label}</button>;

}}

``````



### 2. Use Type Inference### 2. Use Type Inference



```tsx```tsx

// ❌ Redundante// ❌ Redundante

const [count, setCount] = useState<number>(0);const [count, setCount] = useState<number>(0);



// ✅ Type inference automático// ✅ Type inference automático

const [count, setCount] = useState(0); // numberconst [count, setCount] = useState(0); // number

``````



### 3. Evite `any`### 3. Evite `any`



```tsx```tsx

// ❌ Ruim// ❌ Ruim

function process(data: any) {function process(data: any) {

  return data.value; // Sem type safety  return data.value; // Sem type safety

}}



// ✅ Bom// ✅ Bom

interface Data {interface Data {

  value: string;  value: string;

}}



function process(data: Data) {function process(data: Data) {

  return data.value; // Type safe!  return data.value; // Type safe!

}}



// ✅ Ou use unknown + type guard// ✅ Ou use unknown + type guard

function process(data: unknown) {function process(data: unknown) {

  if (typeof data === 'object' && data !== null && 'value' in data) {  if (typeof data === 'object' && data !== null && 'value' in data) {

    return (data as { value: string }).value;    return (data as { value: string }).value;

  }  }

  throw new Error('Invalid data');  throw new Error('Invalid data');

}}

``````



### 4. Organize Imports### 4. Organize Imports



```tsx```tsx

// ✅ Ordem recomendada// ✅ Ordem recomendada

// 1. React// 1. React

import { useState, useEffect } from 'react';import { useState, useEffect } from 'react';



// 2. Bibliotecas externas// 2. Bibliotecas externas

import { useQuery } from '@tanstack/react-query';import { useQuery } from '@tanstack/react-query';

import clsx from 'clsx';import clsx from 'clsx';



// 3. Componentes internos// 3. Componentes internos

import { Button } from '@/components/ui/Button';import { Button } from '@/components/ui/Button';

import { Card } from '@/components/ui/Card';import { Card } from '@/components/ui/Card';



// 4. Hooks personalizados// 4. Hooks personalizados

import { useAuth } from '@/hooks/useAuth';import { useAuth } from '@/hooks/useAuth';



// 5. Types// 5. Types

import type { User } from '@/types/user';import type { User } from '@/types/user';



// 6. Utils// 6. Utils

import { formatDate } from '@/utils/date';import { formatDate } from '@/utils/date';



// 7. Estilos// 7. Estilos

import styles from './Component.module.css';import styles from './Component.module.css';

``````



### 5. Use FC (Function Component) com Moderação### 5. Use FC (Function Component) com Moderação



```tsx```tsx

// ❌ Não recomendado (FC é menos flexível)// ❌ Não recomendado (FC é menos flexível)

const Button: React.FC<ButtonProps> = ({ children }) => {const Button: React.FC<ButtonProps> = ({ children }) => {

  return <button>{children}</button>;  return <button>{children}</button>;

};};



// ✅ Recomendado (mais flexível, melhor inferência)// ✅ Recomendado (mais flexível, melhor inferência)

function Button({ children }: ButtonProps) {function Button({ children }: ButtonProps) {

  return <button>{children}</button>;  return <button>{children}</button>;

}}



// Ou arrow function// Ou arrow function

const Button = ({ children }: ButtonProps) => {const Button = ({ children }: ButtonProps) => {

  return <button>{children}</button>;  return <button>{children}</button>;

};};

``````



### 6. Props Spreading com Types### 6. Props Spreading com Types



```tsx```tsx

// ✅ Extender props HTML nativas// ✅ Extender props HTML nativas

interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {

  variant?: 'primary' | 'secondary';  variant?: 'primary' | 'secondary';

}}



function Button({ variant = 'primary', ...rest }: ButtonProps) {function Button({ variant = 'primary', ...rest }: ButtonProps) {

  return <button {...rest} className={variant} />;  return <button {...rest} className={variant} />;

}}



// Uso - todas props HTML funcionam!// Uso - todas props HTML funcionam!

<Button <Button 

  variant="primary"  variant="primary"

  onClick={() => {}}  onClick={() => {}}

  disabled  disabled

  type="submit"  type="submit"

  aria-label="Submit"  aria-label="Submit"

/>/>

``````



### 7. Evite Inline Functions em JSX### 7. Evite Inline Functions em JSX



```tsx```tsx

// ❌ Ruim (nova função a cada render)// ❌ Ruim (nova função a cada render)

<button onClick={() => handleClick(id)}>Click</button><button onClick={() => handleClick(id)}>Click</button>



// ✅ Bom (com useCallback)// ✅ Bom (com useCallback)

const handleClickWithId = useCallback(() => {const handleClickWithId = useCallback(() => {

  handleClick(id);  handleClick(id);

}, [id]);}, [id]);



<button onClick={handleClickWithId}>Click</button><button onClick={handleClickWithId}>Click</button>



// ✅ Ou extrair para função// ✅ Ou extrair para função

function handleButtonClick() {function handleButtonClick() {

  handleClick(id);  handleClick(id);

}}



<button onClick={handleButtonClick}>Click</button><button onClick={handleButtonClick}>Click</button>

``````



### 8. Use Const Assertions### 8. Use Const Assertions



```tsx```tsx

// ✅ Const assertion para arrays// ✅ Const assertion para arrays

const COLORS = ['red', 'green', 'blue'] as const;const COLORS = ['red', 'green', 'blue'] as const;

type Color = typeof COLORS[number]; // 'red' | 'green' | 'blue'type Color = typeof COLORS[number]; // 'red' | 'green' | 'blue'



// ✅ Const assertion para objetos// ✅ Const assertion para objetos

const CONFIG = {const CONFIG = {

  apiUrl: 'https://api.example.com',  apiUrl: 'https://api.example.com',

  timeout: 5000,  timeout: 5000,

} as const;} as const;



type Config = typeof CONFIG;type Config = typeof CONFIG;

// { readonly apiUrl: "https://api.example.com"; readonly timeout: 5000; }// { readonly apiUrl: "https://api.example.com"; readonly timeout: 5000; }

``````



------



## 🎯 Exemplo Completo: TODO App## 🎯 Exemplo Completo: TODO App



```tsx```tsx

// types/todo.ts// types/todo.ts

export interface Todo {export interface Todo {

  id: string;  id: string;

  title: string;  title: string;

  completed: boolean;  completed: boolean;

  createdAt: Date;  createdAt: Date;

}}



export type CreateTodoDto = Omit<Todo, 'id' | 'createdAt'>;export type CreateTodoDto = Omit<Todo, 'id' | 'createdAt'>;



// hooks/useTodos.ts// hooks/useTodos.ts

import { useState } from 'react';import { useState } from 'react';

import { Todo, CreateTodoDto } from '../types/todo';import { Todo, CreateTodoDto } from '../types/todo';



export function useTodos() {export function useTodos() {

  const [todos, setTodos] = useState<Todo[]>([]);  const [todos, setTodos] = useState<Todo[]>([]);



  const addTodo = (dto: CreateTodoDto) => {  const addTodo = (dto: CreateTodoDto) => {

    const newTodo: Todo = {    const newTodo: Todo = {

      id: crypto.randomUUID(),      id: crypto.randomUUID(),

      ...dto,      ...dto,

      createdAt: new Date(),      createdAt: new Date(),

    };    };

    setTodos(prev => [...prev, newTodo]);    setTodos(prev => [...prev, newTodo]);

  };  };



  const toggleTodo = (id: string) => {  const toggleTodo = (id: string) => {

    setTodos(prev =>    setTodos(prev =>

      prev.map(todo =>      prev.map(todo =>

        todo.id === id ? { ...todo, completed: !todo.completed } : todo        todo.id === id ? { ...todo, completed: !todo.completed } : todo

      )      )

    );    );

  };  };



  const deleteTodo = (id: string) => {  const deleteTodo = (id: string) => {

    setTodos(prev => prev.filter(todo => todo.id !== id));    setTodos(prev => prev.filter(todo => todo.id !== id));

  };  };



  return { todos, addTodo, toggleTodo, deleteTodo };  return { todos, addTodo, toggleTodo, deleteTodo };

}}



// components/TodoForm.tsx// components/TodoForm.tsx

import { useState } from 'react';import { useState } from 'react';

import { CreateTodoDto } from '../types/todo';import { CreateTodoDto } from '../types/todo';



interface TodoFormProps {interface TodoFormProps {

  onSubmit: (dto: CreateTodoDto) => void;  onSubmit: (dto: CreateTodoDto) => void;

}}



export function TodoForm({ onSubmit }: TodoFormProps) {export function TodoForm({ onSubmit }: TodoFormProps) {

  const [title, setTitle] = useState('');  const [title, setTitle] = useState('');



  const handleSubmit = (e: React.FormEvent) => {  const handleSubmit = (e: React.FormEvent) => {

    e.preventDefault();    e.preventDefault();

    if (!title.trim()) return;    if (!title.trim()) return;



    onSubmit({ title, completed: false });    onSubmit({ title, completed: false });

    setTitle('');    setTitle('');

  };  };



  return (  return (

    <form onSubmit={handleSubmit} className="flex gap-2">    <form onSubmit={handleSubmit} className="flex gap-2">

      <input      <input

        type="text"        type="text"

        value={title}        value={title}

        onChange={(e) => setTitle(e.target.value)}        onChange={(e) => setTitle(e.target.value)}

        placeholder="New todo..."        placeholder="New todo..."

        className="flex-1 px-3 py-2 border rounded"        className="flex-1 px-3 py-2 border rounded"

      />      />

      <button      <button

        type="submit"        type="submit"

        className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-700"        className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-700"

      >      >

        Add        Add

      </button>      </button>

    </form>    </form>

  );  );

}}



// components/TodoItem.tsx// components/TodoItem.tsx

import { Todo } from '../types/todo';import { Todo } from '../types/todo';



interface TodoItemProps {interface TodoItemProps {

  todo: Todo;  todo: Todo;

  onToggle: (id: string) => void;  onToggle: (id: string) => void;

  onDelete: (id: string) => void;  onDelete: (id: string) => void;

}}



export function TodoItem({ todo, onToggle, onDelete }: TodoItemProps) {export function TodoItem({ todo, onToggle, onDelete }: TodoItemProps) {

  return (  return (

    <div className="flex items-center gap-3 p-3 border rounded">    <div className="flex items-center gap-3 p-3 border rounded">

      <input      <input

        type="checkbox"        type="checkbox"

        checked={todo.completed}        checked={todo.completed}

        onChange={() => onToggle(todo.id)}        onChange={() => onToggle(todo.id)}

      />      />

            

      <span className={todo.completed ? 'line-through text-gray-500' : ''}>      <span className={todo.completed ? 'line-through text-gray-500' : ''}>

        {todo.title}        {todo.title}

      </span>      </span>

            

      <button      <button

        onClick={() => onDelete(todo.id)}        onClick={() => onDelete(todo.id)}

        className="ml-auto px-3 py-1 bg-red-500 text-white rounded hover:bg-red-700"        className="ml-auto px-3 py-1 bg-red-500 text-white rounded hover:bg-red-700"

      >      >

        Delete        Delete

      </button>      </button>

    </div>    </div>

  );  );

}}



// App.tsx// App.tsx

import { TodoForm } from './components/TodoForm';import { TodoForm } from './components/TodoForm';

import { TodoItem } from './components/TodoItem';import { TodoItem } from './components/TodoItem';

import { useTodos } from './hooks/useTodos';import { useTodos } from './hooks/useTodos';



export function App() {export function App() {

  const { todos, addTodo, toggleTodo, deleteTodo } = useTodos();  const { todos, addTodo, toggleTodo, deleteTodo } = useTodos();



  const activeTodos = todos.filter(todo => !todo.completed);  const activeTodos = todos.filter(todo => !todo.completed);

  const completedTodos = todos.filter(todo => todo.completed);  const completedTodos = todos.filter(todo => todo.completed);



  return (  return (

    <div className="max-w-2xl mx-auto p-8">    <div className="max-w-2xl mx-auto p-8">

      <h1 className="text-3xl font-bold mb-6">Todo App</h1>      <h1 className="text-3xl font-bold mb-6">Todo App</h1>

            

      <TodoForm onSubmit={addTodo} />      <TodoForm onSubmit={addTodo} />

            

      <div className="mt-8">      <div className="mt-8">

        <h2 className="text-xl font-semibold mb-4">        <h2 className="text-xl font-semibold mb-4">

          Active ({activeTodos.length})          Active ({activeTodos.length})

        </h2>        </h2>

        <div className="space-y-2">        <div className="space-y-2">

          {activeTodos.map(todo => (          {activeTodos.map(todo => (

            <TodoItem            <TodoItem

              key={todo.id}              key={todo.id}

              todo={todo}              todo={todo}

              onToggle={toggleTodo}              onToggle={toggleTodo}

              onDelete={deleteTodo}              onDelete={deleteTodo}

            />            />

          ))}          ))}

        </div>        </div>

      </div>      </div>

            

      {completedTodos.length > 0 && (      {completedTodos.length > 0 && (

        <div className="mt-8">        <div className="mt-8">

          <h2 className="text-xl font-semibold mb-4">          <h2 className="text-xl font-semibold mb-4">

            Completed ({completedTodos.length})            Completed ({completedTodos.length})

          </h2>          </h2>

          <div className="space-y-2">          <div className="space-y-2">

            {completedTodos.map(todo => (            {completedTodos.map(todo => (

              <TodoItem              <TodoItem

                key={todo.id}                key={todo.id}

                todo={todo}                todo={todo}

                onToggle={toggleTodo}                onToggle={toggleTodo}

                onDelete={deleteTodo}                onDelete={deleteTodo}

              />              />

            ))}            ))}

          </div>          </div>

        </div>        </div>

      )}      )}

    </div>    </div>

  );  );

}}

``````



------



## 📚 Recursos Adicionais## 📚 Recursos Adicionais



### Documentação### Documentação

- [React Docs](https://react.dev) - Documentação oficial React 18+- [React Docs](https://react.dev) - Documentação oficial React 18+

- [TypeScript Handbook](https://www.typescriptlang.org/docs/handbook/intro.html)- [TypeScript Handbook](https://www.typescriptlang.org/docs/handbook/intro.html)

- [React TypeScript Cheatsheet](https://react-typescript-cheatsheet.netlify.app/)- [React TypeScript Cheatsheet](https://react-typescript-cheatsheet.netlify.app/)



### Ferramentas### Ferramentas

- **ESLint** - Linting TypeScript + React- **ESLint** - Linting TypeScript + React

- **Prettier** - Formatação de código- **Prettier** - Formatação de código

- **Vite** - Build tool rápido- **Vite** - Build tool rápido

- **Vitest** - Testing framework- **Vitest** - Testing framework



### Bibliotecas Essenciais### Bibliotecas Essenciais

```bash```bash

# Forms# Forms

npm install react-hook-form zod @hookform/resolversnpm install react-hook-form zod @hookform/resolvers



# Routing# Routing

npm install react-router-domnpm install react-router-dom



# State Management# State Management

npm install @tanstack/react-query zustandnpm install @tanstack/react-query zustand



# Styling# Styling

npm install tailwindcss clsxnpm install tailwindcss clsx



# UI Components# UI Components

npm install @radix-ui/react-dialog @radix-ui/react-dropdown-menunpm install @radix-ui/react-dialog @radix-ui/react-dropdown-menu

``````



------



**Voltar para**: [📁 Frameworks](../../../README.md) | [📁 JavaScript](../../../../README.md) | [📁 Learning](../../../../../README.md) | [📁 Repositório Principal](../../../../../../README.md)**Voltar para**: [📁 Frameworks](../../../README.md) | [📁 JavaScript](../../../../README.md) | [📁 Learning](../../../../../README.md) | [📁 Repositório Principal](../../../../../../README.md)

