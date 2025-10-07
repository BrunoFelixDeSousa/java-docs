# 🔷 **React + TypeScript - Guia Completo**

> **TypeScript com React, typing patterns, generics, utility types e best practices**

---

## 📑 **Índice**

1. [Introdução ao TypeScript com React](#1-introdução-ao-typescript-com-react)
2. [Setup e Configuração](#2-setup-e-configuração)
3. [Typing Components](#3-typing-components)
4. [Props e Children](#4-props-e-children)
5. [Events e Handlers](#5-events-e-handlers)
6. [Hooks com TypeScript](#6-hooks-com-typescript)
7. [Context API com TypeScript](#7-context-api-com-typescript)
8. [Forms e Inputs](#8-forms-e-inputs)
9. [Refs e DOM](#9-refs-e-dom)
10. [Higher-Order Components (HOC)](#10-higher-order-components-hoc)
11. [Render Props](#11-render-props)
12. [Generics em React](#12-generics-em-react)
13. [Utility Types](#13-utility-types)
14. [Advanced Patterns](#14-advanced-patterns)
15. [Recursos e Referências](#15-recursos-e-referências)

---

## 1. 🎯 **Introdução ao TypeScript com React**

### 1.1. Por que TypeScript com React?

**Benefícios:**

```
✅ TYPE SAFETY
   - Erros em tempo de desenvolvimento
   - Autocomplete inteligente
   - Refactoring seguro

✅ DOCUMENTAÇÃO VIVA
   - Props autodocumentadas
   - Contratos claros
   - Menos PropTypes

✅ PRODUTIVIDADE
   - IntelliSense melhor
   - Menos bugs em produção
   - Confiança ao refatorar

✅ ESCALABILIDADE
   - Codebase grande mais mantível
   - Onboarding mais fácil
   - Padrões consistentes
```

### 1.2. React vs React + TypeScript

```tsx
// ═══════════════════════════════════════════════════════════
// JAVASCRIPT (sem tipos)
// ═══════════════════════════════════════════════════════════

function Button({ label, onClick, disabled }) {
  return (
    <button onClick={onClick} disabled={disabled}>
      {label}
    </button>
  );
}

// ❌ PROBLEMAS:
// - Quais props são obrigatórias?
// - onClick recebe quais parâmetros?
// - disabled pode ser string?
// - Sem autocomplete

// ═══════════════════════════════════════════════════════════
// TYPESCRIPT (com tipos)
// ═══════════════════════════════════════════════════════════

interface ButtonProps {
  label: string;
  onClick: (event: React.MouseEvent<HTMLButtonElement>) => void;
  disabled?: boolean;
}

function Button({ label, onClick, disabled }: ButtonProps) {
  return (
    <button onClick={onClick} disabled={disabled}>
      {label}
    </button>
  );
}

// ✅ BENEFÍCIOS:
// - Props claramente definidas
// - onClick tipado corretamente
// - disabled só aceita boolean
// - Autocomplete completo
// - Erro se passar prop errada
```

### 1.3. Convenções de Nomenclatura

```tsx
// ═══════════════════════════════════════════════════════════
// NAMING CONVENTIONS
// ═══════════════════════════════════════════════════════════

// ✅ Props interface: ComponentNameProps
interface ButtonProps {
  label: string;
}

// ✅ State type: ComponentNameState
interface FormState {
  email: string;
  password: string;
}

// ✅ Event handlers: handleEventName
const handleClick = (event: React.MouseEvent) => {};
const handleSubmit = (event: React.FormEvent) => {};

// ✅ Generic components: PascalCase
interface ListProps<T> {
  items: T[];
}

// ✅ Utility types: descriptive names
type User = {
  id: number;
  name: string;
};

type PartialUser = Partial<User>;
type ReadonlyUser = Readonly<User>;
```

---

## 2. ⚙️ **Setup e Configuração**

### 2.1. Criar Projeto com TypeScript

```bash
# ═══════════════════════════════════════════════════════════
# VITE (recomendado)
# ═══════════════════════════════════════════════════════════

npm create vite@latest my-app -- --template react-ts
cd my-app
npm install

# ═══════════════════════════════════════════════════════════
# CREATE REACT APP (legacy)
# ═══════════════════════════════════════════════════════════

npx create-react-app my-app --template typescript

# ═══════════════════════════════════════════════════════════
# NEXT.JS
# ═══════════════════════════════════════════════════════════

npx create-next-app@latest my-app --typescript
```

### 2.2. Adicionar TypeScript a Projeto Existente

```bash
# Instalar dependências
npm install --save-dev typescript @types/react @types/react-dom

# Criar tsconfig.json
npx tsc --init
```

### 2.3. tsconfig.json (React)

```json
{
  "compilerOptions": {
    // ═══════════════════════════════════════════════════════
    // ESSENCIAL PARA REACT
    // ═══════════════════════════════════════════════════════
    
    "target": "ES2020",                    // ES6+ features
    "lib": ["ES2020", "DOM", "DOM.Iterable"],
    "jsx": "react-jsx",                    // React 17+ JSX transform
    "module": "ESNext",
    "moduleResolution": "bundler",         // Vite/Webpack
    
    // ═══════════════════════════════════════════════════════
    // STRICT MODE (recomendado)
    // ═══════════════════════════════════════════════════════
    
    "strict": true,                        // Habilita todos strict checks
    "noUnusedLocals": true,                // Erro em variáveis não usadas
    "noUnusedParameters": true,            // Erro em parâmetros não usados
    "noImplicitReturns": true,             // Erro se função não retorna
    "noFallthroughCasesInSwitch": true,    // Erro em switch sem break
    
    // ═══════════════════════════════════════════════════════
    // MODULE RESOLUTION
    // ═══════════════════════════════════════════════════════
    
    "baseUrl": ".",
    "paths": {
      "@/*": ["./src/*"],                  // Alias para imports
      "@components/*": ["./src/components/*"],
      "@hooks/*": ["./src/hooks/*"],
      "@utils/*": ["./src/utils/*"]
    },
    
    // ═══════════════════════════════════════════════════════
    // OUTROS
    // ═══════════════════════════════════════════════════════
    
    "esModuleInterop": true,
    "skipLibCheck": true,                  // Skip type checking de .d.ts
    "allowSyntheticDefaultImports": true,
    "forceConsistentCasingInFileNames": true,
    "resolveJsonModule": true,
    "isolatedModules": true
  },
  "include": ["src"],
  "exclude": ["node_modules"]
}
```

### 2.4. Estrutura de Pastas

```
src/
├── components/
│   ├── Button/
│   │   ├── Button.tsx
│   │   ├── Button.types.ts      # Types separados
│   │   └── index.ts
│   └── Card/
│       ├── Card.tsx
│       └── index.ts
├── hooks/
│   ├── useAuth.ts
│   └── useFetch.ts
├── types/
│   ├── api.ts                   # API response types
│   ├── models.ts                # Domain models
│   └── global.d.ts              # Global type declarations
├── utils/
│   └── helpers.ts
└── App.tsx
```

### 2.5. Ambiente de Desenvolvimento

```json
// ═══════════════════════════════════════════════════════════
// package.json: Scripts úteis
// ═══════════════════════════════════════════════════════════

{
  "scripts": {
    "dev": "vite",
    "build": "tsc && vite build",         // Type check + build
    "typecheck": "tsc --noEmit",          // Apenas type check
    "lint": "eslint . --ext ts,tsx"
  },
  "devDependencies": {
    "@types/react": "^18.2.0",
    "@types/react-dom": "^18.2.0",
    "@typescript-eslint/eslint-plugin": "^6.0.0",
    "@typescript-eslint/parser": "^6.0.0",
    "typescript": "^5.3.0"
  }
}
```

---

## 3. 🧩 **Typing Components**

### 3.1. Function Components

```tsx
// ═══════════════════════════════════════════════════════════
// FUNCTION COMPONENT: Básico
// ═══════════════════════════════════════════════════════════

// ✅ RECOMENDADO: Interface para props
interface GreetingProps {
  name: string;
  age?: number;  // Opcional
}

function Greeting({ name, age }: GreetingProps) {
  return (
    <div>
      Hello, {name}! {age && `You are ${age} years old.`}
    </div>
  );
}

// ✅ ALTERNATIVA: Type alias
type GreetingProps = {
  name: string;
  age?: number;
};

// ❌ NÃO recomendado: React.FC (deprecated pattern)
const Greeting: React.FC<GreetingProps> = ({ name, age }) => {
  return <div>Hello, {name}!</div>;
};

// Por quê? React.FC:
// - Adiciona children automaticamente (nem sempre desejado)
// - Mais verboso
// - Menos flexível
```

### 3.2. Props com Valores Padrão

```tsx
// ═══════════════════════════════════════════════════════════
// DEFAULT PROPS
// ═══════════════════════════════════════════════════════════

interface ButtonProps {
  variant?: 'primary' | 'secondary' | 'danger';
  size?: 'small' | 'medium' | 'large';
  disabled?: boolean;
  children: React.ReactNode;
}

// ✅ OPÇÃO 1: Destructuring com defaults
function Button({
  variant = 'primary',
  size = 'medium',
  disabled = false,
  children
}: ButtonProps) {
  return (
    <button className={`btn btn-${variant} btn-${size}`} disabled={disabled}>
      {children}
    </button>
  );
}

// ✅ OPÇÃO 2: Default props separado (legacy, mas válido)
const defaultProps = {
  variant: 'primary' as const,
  size: 'medium' as const,
  disabled: false
};

function Button(props: ButtonProps) {
  const { variant, size, disabled, children } = { ...defaultProps, ...props };
  
  return (
    <button className={`btn btn-${variant} btn-${size}`} disabled={disabled}>
      {children}
    </button>
  );
}
```

### 3.3. Component com Generics

```tsx
// ═══════════════════════════════════════════════════════════
// GENERIC COMPONENT
// ═══════════════════════════════════════════════════════════

interface ListProps<T> {
  items: T[];
  renderItem: (item: T) => React.ReactNode;
  keyExtractor: (item: T) => string | number;
}

function List<T>({ items, renderItem, keyExtractor }: ListProps<T>) {
  return (
    <ul>
      {items.map((item) => (
        <li key={keyExtractor(item)}>
          {renderItem(item)}
        </li>
      ))}
    </ul>
  );
}

// Uso
interface User {
  id: number;
  name: string;
}

function UserList() {
  const users: User[] = [
    { id: 1, name: 'Alice' },
    { id: 2, name: 'Bob' }
  ];

  return (
    <List
      items={users}
      renderItem={(user) => <span>{user.name}</span>}
      keyExtractor={(user) => user.id}
    />
  );
}

// ✅ TypeScript infere T = User automaticamente!
```

### 3.4. Component com Multiple Types

```tsx
// ═══════════════════════════════════════════════════════════
// UNION TYPES em componentes
// ═══════════════════════════════════════════════════════════

// Tipo base
interface BaseCardProps {
  title: string;
  className?: string;
}

// Variante 1: Card com imagem
interface ImageCardProps extends BaseCardProps {
  variant: 'image';
  imageUrl: string;
  imageAlt: string;
}

// Variante 2: Card com ícone
interface IconCardProps extends BaseCardProps {
  variant: 'icon';
  icon: React.ReactNode;
}

// Union type
type CardProps = ImageCardProps | IconCardProps;

function Card(props: CardProps) {
  const { title, className } = props;

  return (
    <div className={className}>
      <h2>{title}</h2>
      
      {/* ✅ Type narrowing com discriminated union */}
      {props.variant === 'image' && (
        <img src={props.imageUrl} alt={props.imageAlt} />
      )}
      
      {props.variant === 'icon' && (
        <div className="icon">{props.icon}</div>
      )}
    </div>
  );
}

// Uso
<Card variant="image" title="Photo" imageUrl="/photo.jpg" imageAlt="Photo" />
<Card variant="icon" title="Settings" icon={<SettingsIcon />} />

// ❌ ERRO: imageUrl sem variant="image"
<Card title="Invalid" imageUrl="/photo.jpg" />
```

---

## 4. 👶 **Props e Children**

### 4.1. Children Types

```tsx
// ═══════════════════════════════════════════════════════════
// TYPING CHILDREN
// ═══════════════════════════════════════════════════════════

// ✅ React.ReactNode: Aceita qualquer coisa renderizável
interface ContainerProps {
  children: React.ReactNode;
}

function Container({ children }: ContainerProps) {
  return <div className="container">{children}</div>;
}

// Uso: Aceita string, number, JSX, array, null, undefined
<Container>Hello</Container>
<Container>{123}</Container>
<Container><span>JSX</span></Container>
<Container>{[1, 2, 3]}</Container>

// ✅ React.ReactElement: Apenas JSX elements
interface WrapperProps {
  children: React.ReactElement;
}

function Wrapper({ children }: WrapperProps) {
  return <div>{children}</div>;
}

// Uso
<Wrapper><span>Valid</span></Wrapper>

// ❌ ERRO: Não aceita string/number
<Wrapper>Invalid</Wrapper>

// ✅ JSX.Element: Alias para React.ReactElement
interface BoxProps {
  children: JSX.Element;
}

// ✅ Array de elementos
interface ListProps {
  children: React.ReactElement[];
}

// ✅ Específico: Apenas certos componentes
interface TabsProps {
  children: React.ReactElement<TabProps> | React.ReactElement<TabProps>[];
}
```

### 4.2. Children como Function (Render Props)

```tsx
// ═══════════════════════════════════════════════════════════
// CHILDREN AS FUNCTION
// ═══════════════════════════════════════════════════════════

interface MouseTrackerProps {
  children: (position: { x: number; y: number }) => React.ReactNode;
}

function MouseTracker({ children }: MouseTrackerProps) {
  const [position, setPosition] = React.useState({ x: 0, y: 0 });

  React.useEffect(() => {
    const handleMouseMove = (e: MouseEvent) => {
      setPosition({ x: e.clientX, y: e.clientY });
    };

    window.addEventListener('mousemove', handleMouseMove);
    return () => window.removeEventListener('mousemove', handleMouseMove);
  }, []);

  return <>{children(position)}</>;
}

// Uso
<MouseTracker>
  {({ x, y }) => (
    <div>Mouse at ({x}, {y})</div>
  )}
</MouseTracker>
```

### 4.3. Props Spreading

```tsx
// ═══════════════════════════════════════════════════════════
// PROPS SPREADING com TypeScript
// ═══════════════════════════════════════════════════════════

interface CustomButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: 'primary' | 'secondary';
  loading?: boolean;
}

function CustomButton({
  variant = 'primary',
  loading = false,
  children,
  disabled,
  ...restProps  // ✅ Pega todas outras props de button
}: CustomButtonProps) {
  return (
    <button
      className={`btn btn-${variant}`}
      disabled={disabled || loading}
      {...restProps}  // ✅ Spread: onClick, type, etc
    >
      {loading ? 'Loading...' : children}
    </button>
  );
}

// Uso: Aceita todas props nativas de button
<CustomButton
  variant="primary"
  onClick={() => console.log('Clicked')}
  type="submit"
  aria-label="Submit form"
/>
```

### 4.4. Polymorphic Components

```tsx
// ═══════════════════════════════════════════════════════════
// POLYMORPHIC COMPONENT: Mudar elemento base
// ═══════════════════════════════════════════════════════════

type AsProp<C extends React.ElementType> = {
  as?: C;
};

type PropsToOmit<C extends React.ElementType, P> = keyof (AsProp<C> & P);

type PolymorphicComponentProp<
  C extends React.ElementType,
  Props = {}
> = React.PropsWithChildren<Props & AsProp<C>> &
  Omit<React.ComponentPropsWithoutRef<C>, PropsToOmit<C, Props>>;

interface TextProps {
  color?: 'primary' | 'secondary' | 'error';
  size?: 'small' | 'medium' | 'large';
}

type TextComponent = <C extends React.ElementType = 'span'>(
  props: PolymorphicComponentProp<C, TextProps>
) => React.ReactElement | null;

const Text: TextComponent = ({
  as,
  color = 'primary',
  size = 'medium',
  children,
  ...restProps
}) => {
  const Component = as || 'span';

  return (
    <Component
      className={`text text-${color} text-${size}`}
      {...restProps}
    >
      {children}
    </Component>
  );
};

// Uso: Pode ser qualquer elemento HTML
<Text>Default span</Text>
<Text as="p">Paragraph</Text>
<Text as="h1" color="primary" size="large">Heading</Text>
<Text as="a" href="/link">Link</Text>  {/* ✅ href tipado! */}
```

---

## 5. 🎯 **Events e Handlers**

### 5.1. Event Types

```tsx
// ═══════════════════════════════════════════════════════════
// EVENT TYPES COMUNS
// ═══════════════════════════════════════════════════════════

function EventExamples() {
  // ✅ Mouse events
  const handleClick = (event: React.MouseEvent<HTMLButtonElement>) => {
    console.log(event.clientX, event.clientY);
  };

  const handleDivClick = (event: React.MouseEvent<HTMLDivElement>) => {
    console.log('Div clicked');
  };

  // ✅ Keyboard events
  const handleKeyDown = (event: React.KeyboardEvent<HTMLInputElement>) => {
    if (event.key === 'Enter') {
      console.log('Enter pressed');
    }
  };

  // ✅ Form events
  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    console.log('Form submitted');
  };

  // ✅ Change events
  const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    console.log(event.target.value);
  };

  const handleSelectChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
    console.log(event.target.value);
  };

  const handleTextareaChange = (event: React.ChangeEvent<HTMLTextAreaElement>) => {
    console.log(event.target.value);
  };

  // ✅ Focus events
  const handleFocus = (event: React.FocusEvent<HTMLInputElement>) => {
    console.log('Input focused');
  };

  // ✅ Drag events
  const handleDrop = (event: React.DragEvent<HTMLDivElement>) => {
    event.preventDefault();
    console.log('Dropped');
  };

  return (
    <div>
      <button onClick={handleClick}>Click me</button>
      <input onKeyDown={handleKeyDown} onChange={handleChange} />
      <form onSubmit={handleSubmit}>
        <button type="submit">Submit</button>
      </form>
    </div>
  );
}
```

### 5.2. Event Handler Props

```tsx
// ═══════════════════════════════════════════════════════════
// TYPING EVENT HANDLERS em props
// ═══════════════════════════════════════════════════════════

interface InputProps {
  value: string;
  onChange: (value: string) => void;  // ✅ Recebe apenas value
  onKeyDown?: (event: React.KeyboardEvent<HTMLInputElement>) => void;
}

function Input({ value, onChange, onKeyDown }: InputProps) {
  const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    onChange(event.target.value);  // ✅ Simplifica para componente pai
  };

  return (
    <input
      type="text"
      value={value}
      onChange={handleChange}
      onKeyDown={onKeyDown}
    />
  );
}

// Uso
function Form() {
  const [text, setText] = useState('');

  return (
    <Input
      value={text}
      onChange={(value) => setText(value)}  // ✅ Recebe apenas string
      onKeyDown={(e) => {
        if (e.key === 'Enter') console.log('Enter');
      }}
    />
  );
}
```

### 5.3. Synthetic Events

```tsx
// ═══════════════════════════════════════════════════════════
// SYNTHETIC vs NATIVE EVENTS
// ═══════════════════════════════════════════════════════════

function EventComparison() {
  // ✅ React Synthetic Event
  const handleClick = (event: React.MouseEvent<HTMLButtonElement>) => {
    console.log(event.currentTarget);  // ✅ Tipado como HTMLButtonElement
  };

  // ✅ Native DOM Event (useEffect, addEventListener)
  useEffect(() => {
    const handleNativeClick = (event: MouseEvent) => {
      console.log(event.target);
    };

    window.addEventListener('click', handleNativeClick);
    return () => window.removeEventListener('click', handleNativeClick);
  }, []);

  return <button onClick={handleClick}>Click</button>;
}
```

### 5.4. Custom Event Handlers

```tsx
// ═══════════════════════════════════════════════════════════
// CUSTOM EVENT HANDLER TYPES
// ═══════════════════════════════════════════════════════════

// Tipo para callback customizado
type SelectHandler = (selectedId: number, selectedItem: Item) => void;

interface SelectProps {
  items: Item[];
  onSelect: SelectHandler;
}

function Select({ items, onSelect }: SelectProps) {
  const handleClick = (item: Item) => {
    onSelect(item.id, item);
  };

  return (
    <ul>
      {items.map((item) => (
        <li key={item.id} onClick={() => handleClick(item)}>
          {item.name}
        </li>
      ))}
    </ul>
  );
}

// Uso
<Select
  items={items}
  onSelect={(id, item) => {
    console.log('Selected:', id, item);
  }}
/>
```

---

## 6. 🎣 **Hooks com TypeScript**

### 6.1. useState

```tsx
// ═══════════════════════════════════════════════════════════
// useState: Type Inference
// ═══════════════════════════════════════════════════════════

function StateExamples() {
  // ✅ Inferência automática (primitivos)
  const [count, setCount] = useState(0);  // number
  const [name, setName] = useState('');   // string
  const [isOpen, setIsOpen] = useState(false);  // boolean

  // ✅ Type explícito (recomendado para objetos/arrays)
  const [user, setUser] = useState<User | null>(null);

  interface User {
    id: number;
    name: string;
    email: string;
  }

  // ✅ Type com valor inicial
  const [users, setUsers] = useState<User[]>([]);

  // ✅ Union types
  const [status, setStatus] = useState<'idle' | 'loading' | 'success' | 'error'>('idle');

  // ✅ Optional type
  const [data, setData] = useState<string | undefined>(undefined);

  return (
    <div>
      <p>{count}</p>
      <button onClick={() => setCount(count + 1)}>Increment</button>
    </div>
  );
}
```

### 6.2. useReducer

```tsx
// ═══════════════════════════════════════════════════════════
// useReducer: Typing State e Actions
// ═══════════════════════════════════════════════════════════

// State type
interface State {
  count: number;
  user: User | null;
  loading: boolean;
}

// Action types (Discriminated Union)
type Action =
  | { type: 'increment' }
  | { type: 'decrement' }
  | { type: 'reset'; payload: number }
  | { type: 'setUser'; payload: User }
  | { type: 'setLoading'; payload: boolean };

// Reducer
function reducer(state: State, action: Action): State {
  switch (action.type) {
    case 'increment':
      return { ...state, count: state.count + 1 };
    
    case 'decrement':
      return { ...state, count: state.count - 1 };
    
    case 'reset':
      return { ...state, count: action.payload };  // ✅ payload tipado
    
    case 'setUser':
      return { ...state, user: action.payload };   // ✅ payload tipado
    
    case 'setLoading':
      return { ...state, loading: action.payload };
    
    default:
      return state;  // ✅ Exhaustive check
  }
}

// Component
function Counter() {
  const [state, dispatch] = useReducer(reducer, {
    count: 0,
    user: null,
    loading: false
  });

  return (
    <div>
      <p>Count: {state.count}</p>
      <button onClick={() => dispatch({ type: 'increment' })}>+</button>
      <button onClick={() => dispatch({ type: 'reset', payload: 0 })}>Reset</button>
      
      {/* ❌ ERRO: payload obrigatório para 'reset' */}
      {/* <button onClick={() => dispatch({ type: 'reset' })}>Invalid</button> */}
    </div>
  );
}
```

### 6.3. useRef

```tsx
// ═══════════════════════════════════════════════════════════
// useRef: DOM refs e mutable values
// ═══════════════════════════════════════════════════════════

function RefExamples() {
  // ✅ DOM ref
  const inputRef = useRef<HTMLInputElement>(null);

  useEffect(() => {
    // ✅ TypeScript sabe que é HTMLInputElement | null
    inputRef.current?.focus();
  }, []);

  // ✅ Mutable value
  const countRef = useRef<number>(0);

  useEffect(() => {
    countRef.current += 1;
    console.log('Render count:', countRef.current);
  });

  // ✅ Timer ref
  const timerRef = useRef<NodeJS.Timeout | null>(null);

  const startTimer = () => {
    timerRef.current = setInterval(() => {
      console.log('Tick');
    }, 1000);
  };

  const stopTimer = () => {
    if (timerRef.current) {
      clearInterval(timerRef.current);
    }
  };

  return (
    <div>
      <input ref={inputRef} type="text" />
      <button onClick={startTimer}>Start</button>
      <button onClick={stopTimer}>Stop</button>
    </div>
  );
}
```

### 6.4. useEffect e useLayoutEffect

```tsx
// ═══════════════════════════════════════════════════════════
// useEffect: Typing dependencies
// ═══════════════════════════════════════════════════════════

function EffectExamples() {
  const [count, setCount] = useState(0);

  // ✅ Cleanup function tipada
  useEffect(() => {
    const timer = setInterval(() => {
      setCount(c => c + 1);
    }, 1000);

    // ✅ Cleanup: () => void
    return () => clearInterval(timer);
  }, []);

  // ✅ Async em useEffect
  useEffect(() => {
    let cancelled = false;

    const fetchData = async () => {
      const response = await fetch('/api/data');
      const data: ApiResponse = await response.json();

      if (!cancelled) {
        console.log(data);
      }
    };

    fetchData();

    return () => {
      cancelled = true;
    };
  }, []);

  return <div>{count}</div>;
}
```

### 6.5. Custom Hooks

```tsx
// ═══════════════════════════════════════════════════════════
// CUSTOM HOOKS com TypeScript
// ═══════════════════════════════════════════════════════════

// Hook simples com retorno tipado
function useToggle(initialValue = false): [boolean, () => void] {
  const [value, setValue] = useState(initialValue);

  const toggle = useCallback(() => {
    setValue(v => !v);
  }, []);

  return [value, toggle];
}

// Hook com objeto de retorno
interface UseCounterReturn {
  count: number;
  increment: () => void;
  decrement: () => void;
  reset: () => void;
}

function useCounter(initialValue = 0): UseCounterReturn {
  const [count, setCount] = useState(initialValue);

  const increment = useCallback(() => setCount(c => c + 1), []);
  const decrement = useCallback(() => setCount(c => c - 1), []);
  const reset = useCallback(() => setCount(initialValue), [initialValue]);

  return { count, increment, decrement, reset };
}

// Hook genérico
interface UseFetchReturn<T> {
  data: T | null;
  loading: boolean;
  error: Error | null;
  refetch: () => void;
}

function useFetch<T>(url: string): UseFetchReturn<T> {
  const [data, setData] = useState<T | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<Error | null>(null);

  const fetchData = useCallback(async () => {
    try {
      setLoading(true);
      const response = await fetch(url);
      const json = await response.json();
      setData(json);
    } catch (err) {
      setError(err instanceof Error ? err : new Error('Unknown error'));
    } finally {
      setLoading(false);
    }
  }, [url]);

  useEffect(() => {
    fetchData();
  }, [fetchData]);

  return { data, loading, error, refetch: fetchData };
}

// Uso
interface User {
  id: number;
  name: string;
}

function UserProfile() {
  const { data: user, loading, error } = useFetch<User>('/api/user/1');

  if (loading) return <div>Loading...</div>;
  if (error) return <div>Error: {error.message}</div>;
  if (!user) return null;

  return <div>{user.name}</div>;
}
```

---

## 7. 🌐 **Context API com TypeScript**

### 7.1. Context Básico

```tsx
// ═══════════════════════════════════════════════════════════
// CONTEXT com TypeScript
// ═══════════════════════════════════════════════════════════

// Tipo do Context
interface ThemeContextType {
  theme: 'light' | 'dark';
  toggleTheme: () => void;
}

// ✅ Context com valor default
const ThemeContext = createContext<ThemeContextType>({
  theme: 'light',
  toggleTheme: () => {}
});

// ❌ Alternativa: Context pode ser undefined
const ThemeContext = createContext<ThemeContextType | undefined>(undefined);

// Provider
interface ThemeProviderProps {
  children: React.ReactNode;
}

function ThemeProvider({ children }: ThemeProviderProps) {
  const [theme, setTheme] = useState<'light' | 'dark'>('light');

  const toggleTheme = useCallback(() => {
    setTheme(t => t === 'light' ? 'dark' : 'light');
  }, []);

  const value: ThemeContextType = {
    theme,
    toggleTheme
  };

  return (
    <ThemeContext.Provider value={value}>
      {children}
    </ThemeContext.Provider>
  );
}

// ✅ Custom hook com type guard
function useTheme(): ThemeContextType {
  const context = useContext(ThemeContext);

  if (context === undefined) {
    throw new Error('useTheme must be used within ThemeProvider');
  }

  return context;
}

// Uso
function App() {
  return (
    <ThemeProvider>
      <Header />
    </ThemeProvider>
  );
}

function Header() {
  const { theme, toggleTheme } = useTheme();

  return (
    <header className={theme}>
      <button onClick={toggleTheme}>Toggle Theme</button>
    </header>
  );
}
```

### 7.2. Context com Reducer

```tsx
// ═══════════════════════════════════════════════════════════
// CONTEXT + useReducer
// ═══════════════════════════════════════════════════════════

// State type
interface AuthState {
  user: User | null;
  isAuthenticated: boolean;
  loading: boolean;
}

interface User {
  id: number;
  name: string;
  email: string;
}

// Actions
type AuthAction =
  | { type: 'LOGIN_START' }
  | { type: 'LOGIN_SUCCESS'; payload: User }
  | { type: 'LOGIN_FAILURE' }
  | { type: 'LOGOUT' };

// Reducer
function authReducer(state: AuthState, action: AuthAction): AuthState {
  switch (action.type) {
    case 'LOGIN_START':
      return { ...state, loading: true };
    
    case 'LOGIN_SUCCESS':
      return {
        user: action.payload,
        isAuthenticated: true,
        loading: false
      };
    
    case 'LOGIN_FAILURE':
      return { ...state, loading: false };
    
    case 'LOGOUT':
      return {
        user: null,
        isAuthenticated: false,
        loading: false
      };
    
    default:
      return state;
  }
}

// Context type
interface AuthContextType {
  state: AuthState;
  login: (email: string, password: string) => Promise<void>;
  logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

// Provider
function AuthProvider({ children }: { children: React.ReactNode }) {
  const [state, dispatch] = useReducer(authReducer, {
    user: null,
    isAuthenticated: false,
    loading: false
  });

  const login = useCallback(async (email: string, password: string) => {
    dispatch({ type: 'LOGIN_START' });

    try {
      const response = await fetch('/api/login', {
        method: 'POST',
        body: JSON.stringify({ email, password })
      });

      const user: User = await response.json();
      dispatch({ type: 'LOGIN_SUCCESS', payload: user });
    } catch (error) {
      dispatch({ type: 'LOGIN_FAILURE' });
    }
  }, []);

  const logout = useCallback(() => {
    dispatch({ type: 'LOGOUT' });
  }, []);

  const value: AuthContextType = {
    state,
    login,
    logout
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

// Hook
function useAuth(): AuthContextType {
  const context = useContext(AuthContext);

  if (!context) {
    throw new Error('useAuth must be used within AuthProvider');
  }

  return context;
}

// Uso
function LoginForm() {
  const { login, state } = useAuth();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    login(email, password);
  };

  return (
    <form onSubmit={handleSubmit}>
      <input value={email} onChange={(e) => setEmail(e.target.value)} />
      <input value={password} onChange={(e) => setPassword(e.target.value)} type="password" />
      <button type="submit" disabled={state.loading}>
        {state.loading ? 'Logging in...' : 'Login'}
      </button>
    </form>
  );
}
```

### 7.3. Multiple Contexts

```tsx
// ═══════════════════════════════════════════════════════════
// COMPOSING MULTIPLE CONTEXTS
// ═══════════════════════════════════════════════════════════

// Helper para compor providers
type ProviderProps = { children: React.ReactNode };

function composeProviders(...providers: React.FC<ProviderProps>[]) {
  return ({ children }: ProviderProps) =>
    providers.reduceRight(
      (acc, Provider) => <Provider>{acc}</Provider>,
      children
    );
}

// Uso
const AppProviders = composeProviders(
  ThemeProvider,
  AuthProvider,
  NotificationProvider
);

function App() {
  return (
    <AppProviders>
      <Main />
    </AppProviders>
  );
}
```

---

## 8. 📝 **Forms e Inputs**

### 8.1. Controlled Inputs

```tsx
// ═══════════════════════════════════════════════════════════
// CONTROLLED INPUTS
// ═══════════════════════════════════════════════════════════

interface FormData {
  name: string;
  email: string;
  age: number;
  country: string;
  bio: string;
  agreedToTerms: boolean;
}

function ControlledForm() {
  const [formData, setFormData] = useState<FormData>({
    name: '',
    email: '',
    age: 0,
    country: '',
    bio: '',
    agreedToTerms: false
  });

  // ✅ Generic handler para inputs
  const handleInputChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>
  ) => {
    const { name, value, type } = e.target;

    setFormData(prev => ({
      ...prev,
      [name]: type === 'number' ? Number(value) : value
    }));
  };

  // ✅ Handler para checkbox
  const handleCheckboxChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData(prev => ({
      ...prev,
      [e.target.name]: e.target.checked
    }));
  };

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    console.log('Form data:', formData);
  };

  return (
    <form onSubmit={handleSubmit}>
      <input
        type="text"
        name="name"
        value={formData.name}
        onChange={handleInputChange}
      />

      <input
        type="email"
        name="email"
        value={formData.email}
        onChange={handleInputChange}
      />

      <input
        type="number"
        name="age"
        value={formData.age}
        onChange={handleInputChange}
      />

      <select name="country" value={formData.country} onChange={handleInputChange}>
        <option value="">Select...</option>
        <option value="us">USA</option>
        <option value="br">Brazil</option>
      </select>

      <textarea
        name="bio"
        value={formData.bio}
        onChange={handleInputChange}
      />

      <input
        type="checkbox"
        name="agreedToTerms"
        checked={formData.agreedToTerms}
        onChange={handleCheckboxChange}
      />

      <button type="submit">Submit</button>
    </form>
  );
}
```

### 8.2. React Hook Form com TypeScript

```bash
npm install react-hook-form
```

```tsx
// ═══════════════════════════════════════════════════════════
// REACT HOOK FORM
// ═══════════════════════════════════════════════════════════

import { useForm, SubmitHandler } from 'react-hook-form';

interface FormInputs {
  email: string;
  password: string;
  age: number;
}

function LoginForm() {
  const {
    register,
    handleSubmit,
    formState: { errors }
  } = useForm<FormInputs>();

  const onSubmit: SubmitHandler<FormInputs> = (data) => {
    console.log(data);  // ✅ data tipado como FormInputs
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <input
        {...register('email', {
          required: 'Email is required',
          pattern: {
            value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
            message: 'Invalid email'
          }
        })}
        type="email"
      />
      {errors.email && <span>{errors.email.message}</span>}

      <input
        {...register('password', {
          required: 'Password is required',
          minLength: {
            value: 6,
            message: 'Password must be at least 6 characters'
          }
        })}
        type="password"
      />
      {errors.password && <span>{errors.password.message}</span>}

      <input
        {...register('age', {
          required: 'Age is required',
          min: { value: 18, message: 'Must be 18+' }
        })}
        type="number"
      />
      {errors.age && <span>{errors.age.message}</span>}

      <button type="submit">Submit</button>
    </form>
  );
}
```

### 8.3. React Hook Form com Zod

```bash
npm install @hookform/resolvers zod
```

```tsx
// ═══════════════════════════════════════════════════════════
// REACT HOOK FORM + ZOD
// ═══════════════════════════════════════════════════════════

import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';

// ✅ Schema Zod
const schema = z.object({
  email: z.string().email('Invalid email'),
  password: z.string().min(6, 'Password must be at least 6 characters'),
  age: z.number().min(18, 'Must be 18+'),
  confirmPassword: z.string()
}).refine(data => data.password === data.confirmPassword, {
  message: 'Passwords must match',
  path: ['confirmPassword']
});

// ✅ Infer type do schema
type FormInputs = z.infer<typeof schema>;

function RegisterForm() {
  const {
    register,
    handleSubmit,
    formState: { errors }
  } = useForm<FormInputs>({
    resolver: zodResolver(schema)
  });

  const onSubmit = (data: FormInputs) => {
    console.log(data);  // ✅ Validado e tipado!
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <input {...register('email')} type="email" />
      {errors.email && <span>{errors.email.message}</span>}

      <input {...register('password')} type="password" />
      {errors.password && <span>{errors.password.message}</span>}

      <input {...register('confirmPassword')} type="password" />
      {errors.confirmPassword && <span>{errors.confirmPassword.message}</span>}

      <input {...register('age', { valueAsNumber: true })} type="number" />
      {errors.age && <span>{errors.age.message}</span>}

      <button type="submit">Register</button>
    </form>
  );
}
```

### 8.4. Form Component Genérico

```tsx
// ═══════════════════════════════════════════════════════════
// GENERIC FORM COMPONENT
// ═══════════════════════════════════════════════════════════

import { useForm, FieldValues, UseFormReturn } from 'react-hook-form';

interface FormProps<T extends FieldValues> {
  defaultValues: T;
  onSubmit: (data: T) => void | Promise<void>;
  children: (methods: UseFormReturn<T>) => React.ReactNode;
}

function Form<T extends FieldValues>({
  defaultValues,
  onSubmit,
  children
}: FormProps<T>) {
  const methods = useForm<T>({ defaultValues });

  return (
    <form onSubmit={methods.handleSubmit(onSubmit)}>
      {children(methods)}
    </form>
  );
}

// Uso
interface LoginData {
  email: string;
  password: string;
}

function LoginPage() {
  const handleLogin = async (data: LoginData) => {
    console.log('Login:', data);
  };

  return (
    <Form
      defaultValues={{ email: '', password: '' }}
      onSubmit={handleLogin}
    >
      {({ register, formState: { errors } }) => (
        <>
          <input {...register('email')} type="email" />
          {errors.email && <span>{errors.email.message}</span>}

          <input {...register('password')} type="password" />
          {errors.password && <span>{errors.password.message}</span>}

          <button type="submit">Login</button>
        </>
      )}
    </Form>
  );
}
```

---

## 9. 🎯 **Refs e DOM**

### 9.1. useRef para DOM Elements

```tsx
// ═══════════════════════════════════════════════════════════
// useRef: DOM ELEMENTS
// ═══════════════════════════════════════════════════════════

function RefExamples() {
  // ✅ Input ref
  const inputRef = useRef<HTMLInputElement>(null);

  // ✅ Button ref
  const buttonRef = useRef<HTMLButtonElement>(null);

  // ✅ Div ref
  const divRef = useRef<HTMLDivElement>(null);

  // ✅ Canvas ref
  const canvasRef = useRef<HTMLCanvasElement>(null);

  // ✅ Video ref
  const videoRef = useRef<HTMLVideoElement>(null);

  useEffect(() => {
    // ✅ TypeScript sabe que current pode ser null
    inputRef.current?.focus();

    // ✅ Métodos específicos do elemento
    if (canvasRef.current) {
      const ctx = canvasRef.current.getContext('2d');
      ctx?.fillRect(0, 0, 100, 100);
    }

    if (videoRef.current) {
      videoRef.current.play();
    }
  }, []);

  return (
    <div ref={divRef}>
      <input ref={inputRef} />
      <button ref={buttonRef}>Click</button>
      <canvas ref={canvasRef} />
      <video ref={videoRef} />
    </div>
  );
}
```

### 9.2. forwardRef

```tsx
// ═══════════════════════════════════════════════════════════
// forwardRef com TypeScript
// ═══════════════════════════════════════════════════════════

interface InputProps {
  label: string;
  error?: string;
}

// ✅ forwardRef tipado
const Input = forwardRef<HTMLInputElement, InputProps>(
  ({ label, error, ...restProps }, ref) => {
    return (
      <div>
        <label>{label}</label>
        <input ref={ref} {...restProps} />
        {error && <span>{error}</span>}
      </div>
    );
  }
);

Input.displayName = 'Input';

// Uso
function Form() {
  const emailRef = useRef<HTMLInputElement>(null);

  useEffect(() => {
    emailRef.current?.focus();
  }, []);

  return <Input ref={emailRef} label="Email" type="email" />;
}
```

### 9.3. useImperativeHandle

```tsx
// ═══════════════════════════════════════════════════════════
// useImperativeHandle: Expor métodos customizados
// ═══════════════════════════════════════════════════════════

// Tipo das funções expostas
interface VideoPlayerRef {
  play: () => void;
  pause: () => void;
  reset: () => void;
}

interface VideoPlayerProps {
  src: string;
}

const VideoPlayer = forwardRef<VideoPlayerRef, VideoPlayerProps>(
  ({ src }, ref) => {
    const videoRef = useRef<HTMLVideoElement>(null);

    // ✅ Expor métodos customizados
    useImperativeHandle(ref, () => ({
      play: () => {
        videoRef.current?.play();
      },
      pause: () => {
        videoRef.current?.pause();
      },
      reset: () => {
        if (videoRef.current) {
          videoRef.current.currentTime = 0;
          videoRef.current.pause();
        }
      }
    }));

    return <video ref={videoRef} src={src} />;
  }
);

// Uso
function App() {
  const playerRef = useRef<VideoPlayerRef>(null);

  const handlePlay = () => {
    playerRef.current?.play();  // ✅ Tipado!
  };

  const handleReset = () => {
    playerRef.current?.reset();
  };

  return (
    <div>
      <VideoPlayer ref={playerRef} src="/video.mp4" />
      <button onClick={handlePlay}>Play</button>
      <button onClick={handleReset}>Reset</button>
    </div>
  );
}
```

### 9.4. Callback Refs

```tsx
// ═══════════════════════════════════════════════════════════
// CALLBACK REF
// ═══════════════════════════════════════════════════════════

function CallbackRefExample() {
  const [height, setHeight] = useState(0);

  // ✅ Callback ref tipado
  const measureRef = useCallback((node: HTMLDivElement | null) => {
    if (node !== null) {
      setHeight(node.getBoundingClientRect().height);
    }
  }, []);

  return (
    <div>
      <div ref={measureRef}>
        <p>Content</p>
      </div>
      <p>Height: {height}px</p>
    </div>
  );
}
```

---

## 10. 🔄 **Higher-Order Components (HOC)**

### 10.1. HOC Básico

```tsx
// ═══════════════════════════════════════════════════════════
// HOC com TypeScript
// ═══════════════════════════════════════════════════════════

// Props que o HOC adiciona
interface WithLoadingProps {
  loading: boolean;
}

// ✅ HOC genérico
function withLoading<P extends object>(
  Component: React.ComponentType<P>
) {
  return function WithLoadingComponent(
    props: P & WithLoadingProps
  ) {
    const { loading, ...restProps } = props;

    if (loading) {
      return <div>Loading...</div>;
    }

    return <Component {...(restProps as P)} />;
  };
}

// Component original
interface UserListProps {
  users: User[];
}

function UserList({ users }: UserListProps) {
  return (
    <ul>
      {users.map(user => (
        <li key={user.id}>{user.name}</li>
      ))}
    </ul>
  );
}

// Component com HOC
const UserListWithLoading = withLoading(UserList);

// Uso
<UserListWithLoading users={users} loading={isLoading} />
```

### 10.2. HOC com Props Injetadas

```tsx
// ═══════════════════════════════════════════════════════════
// HOC que injeta props
// ═══════════════════════════════════════════════════════════

interface InjectedAuthProps {
  user: User | null;
  isAuthenticated: boolean;
  logout: () => void;
}

function withAuth<P extends InjectedAuthProps>(
  Component: React.ComponentType<P>
) {
  return function WithAuthComponent(
    props: Omit<P, keyof InjectedAuthProps>
  ) {
    const { user, isAuthenticated, logout } = useAuth();

    return (
      <Component
        {...(props as P)}
        user={user}
        isAuthenticated={isAuthenticated}
        logout={logout}
      />
    );
  };
}

// Component que recebe props injetadas
interface ProfileProps extends InjectedAuthProps {
  title: string;
}

function Profile({ user, isAuthenticated, logout, title }: ProfileProps) {
  if (!isAuthenticated || !user) {
    return <div>Not authenticated</div>;
  }

  return (
    <div>
      <h1>{title}</h1>
      <p>{user.name}</p>
      <button onClick={logout}>Logout</button>
    </div>
  );
}

const ProfileWithAuth = withAuth(Profile);

// Uso: Não precisa passar user, isAuthenticated, logout
<ProfileWithAuth title="My Profile" />
```

### 10.3. Composing HOCs

```tsx
// ═══════════════════════════════════════════════════════════
// COMPOR MÚLTIPLOS HOCs
// ═══════════════════════════════════════════════════════════

function compose<P>(...hocs: Array<(c: React.ComponentType<any>) => React.ComponentType<any>>) {
  return (Component: React.ComponentType<P>) =>
    hocs.reduceRight((acc, hoc) => hoc(acc), Component);
}

// Uso
const EnhancedComponent = compose(
  withAuth,
  withLoading,
  withTheme
)(MyComponent);
```

---

## 11. 🎭 **Render Props**

### 11.1. Render Props Básico

```tsx
// ═══════════════════════════════════════════════════════════
// RENDER PROPS
// ═══════════════════════════════════════════════════════════

interface MousePosition {
  x: number;
  y: number;
}

interface MouseTrackerProps {
  render: (position: MousePosition) => React.ReactNode;
}

function MouseTracker({ render }: MouseTrackerProps) {
  const [position, setPosition] = useState<MousePosition>({ x: 0, y: 0 });

  useEffect(() => {
    const handleMouseMove = (e: MouseEvent) => {
      setPosition({ x: e.clientX, y: e.clientY });
    };

    window.addEventListener('mousemove', handleMouseMove);
    return () => window.removeEventListener('mousemove', handleMouseMove);
  }, []);

  return <>{render(position)}</>;
}

// Uso
<MouseTracker
  render={({ x, y }) => (
    <div>Mouse at ({x}, {y})</div>
  )}
/>
```

### 11.2. Children as Function

```tsx
// ═══════════════════════════════════════════════════════════
// CHILDREN AS FUNCTION (render props pattern)
// ═══════════════════════════════════════════════════════════

interface DataFetcherProps<T> {
  url: string;
  children: (data: {
    data: T | null;
    loading: boolean;
    error: Error | null;
  }) => React.ReactNode;
}

function DataFetcher<T>({ url, children }: DataFetcherProps<T>) {
  const [data, setData] = useState<T | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<Error | null>(null);

  useEffect(() => {
    fetch(url)
      .then(res => res.json())
      .then(setData)
      .catch(setError)
      .finally(() => setLoading(false));
  }, [url]);

  return <>{children({ data, loading, error })}</>;
}

// Uso
interface User {
  id: number;
  name: string;
}

<DataFetcher<User> url="/api/user/1">
  {({ data, loading, error }) => {
    if (loading) return <div>Loading...</div>;
    if (error) return <div>Error: {error.message}</div>;
    if (!data) return null;

    return <div>{data.name}</div>;
  }}
</DataFetcher>
```

---

## 12. 🔷 **Generics em React**

### 12.1. Generic Components

```tsx
// ═══════════════════════════════════════════════════════════
// GENERIC COMPONENTS
// ═══════════════════════════════════════════════════════════

// Lista genérica
interface ListProps<T> {
  items: T[];
  renderItem: (item: T, index: number) => React.ReactNode;
  keyExtractor: (item: T, index: number) => string | number;
}

function List<T>({ items, renderItem, keyExtractor }: ListProps<T>) {
  return (
    <ul>
      {items.map((item, index) => (
        <li key={keyExtractor(item, index)}>
          {renderItem(item, index)}
        </li>
      ))}
    </ul>
  );
}

// Uso com diferentes tipos
interface Product {
  id: number;
  name: string;
  price: number;
}

<List
  items={products}
  renderItem={(product) => (
    <div>
      {product.name} - ${product.price}
    </div>
  )}
  keyExtractor={(product) => product.id}
/>

interface Todo {
  id: string;
  text: string;
  completed: boolean;
}

<List
  items={todos}
  renderItem={(todo) => (
    <span style={{ textDecoration: todo.completed ? 'line-through' : 'none' }}>
      {todo.text}
    </span>
  )}
  keyExtractor={(todo) => todo.id}
/>
```

### 12.2. Generic Hooks

```tsx
// ═══════════════════════════════════════════════════════════
// GENERIC HOOKS
// ═══════════════════════════════════════════════════════════

// Hook genérico de paginação
interface UsePaginationReturn<T> {
  currentPage: number;
  totalPages: number;
  pageData: T[];
  nextPage: () => void;
  prevPage: () => void;
  goToPage: (page: number) => void;
}

function usePagination<T>(
  data: T[],
  itemsPerPage: number
): UsePaginationReturn<T> {
  const [currentPage, setCurrentPage] = useState(1);

  const totalPages = Math.ceil(data.length / itemsPerPage);

  const pageData = useMemo(() => {
    const start = (currentPage - 1) * itemsPerPage;
    const end = start + itemsPerPage;
    return data.slice(start, end);
  }, [data, currentPage, itemsPerPage]);

  const nextPage = useCallback(() => {
    setCurrentPage(p => Math.min(p + 1, totalPages));
  }, [totalPages]);

  const prevPage = useCallback(() => {
    setCurrentPage(p => Math.max(p - 1, 1));
  }, []);

  const goToPage = useCallback((page: number) => {
    setCurrentPage(Math.max(1, Math.min(page, totalPages)));
  }, [totalPages]);

  return {
    currentPage,
    totalPages,
    pageData,
    nextPage,
    prevPage,
    goToPage
  };
}

// Uso
function ProductList() {
  const products: Product[] = [...];
  
  const {
    currentPage,
    totalPages,
    pageData,
    nextPage,
    prevPage
  } = usePagination(products, 20);

  return (
    <div>
      {pageData.map(product => (
        <div key={product.id}>{product.name}</div>
      ))}
      
      <button onClick={prevPage} disabled={currentPage === 1}>
        Previous
      </button>
      <span>Page {currentPage} of {totalPages}</span>
      <button onClick={nextPage} disabled={currentPage === totalPages}>
        Next
      </button>
    </div>
  );
}
```

### 12.3. Generic Form Fields

```tsx
// ═══════════════════════════════════════════════════════════
// GENERIC FORM FIELD
// ═══════════════════════════════════════════════════════════

interface FieldProps<T> {
  name: keyof T;
  value: T[keyof T];
  onChange: (name: keyof T, value: T[keyof T]) => void;
  label: string;
  type?: string;
}

function Field<T>({ name, value, onChange, label, type = 'text' }: FieldProps<T>) {
  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    onChange(name, e.target.value as T[keyof T]);
  };

  return (
    <div>
      <label>{label}</label>
      <input
        type={type}
        value={value as string}
        onChange={handleChange}
      />
    </div>
  );
}

// Uso
interface UserForm {
  name: string;
  email: string;
  age: number;
}

function UserFormComponent() {
  const [formData, setFormData] = useState<UserForm>({
    name: '',
    email: '',
    age: 0
  });

  const handleFieldChange = <K extends keyof UserForm>(
    name: K,
    value: UserForm[K]
  ) => {
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  return (
    <form>
      <Field
        name="name"
        value={formData.name}
        onChange={handleFieldChange}
        label="Name"
      />
      
      <Field
        name="email"
        value={formData.email}
        onChange={handleFieldChange}
        label="Email"
        type="email"
      />
      
      <Field
        name="age"
        value={formData.age}
        onChange={handleFieldChange}
        label="Age"
        type="number"
      />
    </form>
  );
}
```

---

## 13. 🛠️ **Utility Types**

### 13.1. Built-in Utility Types

```tsx
// ═══════════════════════════════════════════════════════════
// UTILITY TYPES DO TYPESCRIPT
// ═══════════════════════════════════════════════════════════

interface User {
  id: number;
  name: string;
  email: string;
  age: number;
  address: {
    street: string;
    city: string;
  };
}

// ✅ Partial<T>: Todas propriedades opcionais
type PartialUser = Partial<User>;
// { id?: number; name?: string; email?: string; ... }

function updateUser(id: number, updates: Partial<User>) {
  // ✅ Pode atualizar apenas algumas propriedades
}

updateUser(1, { name: 'John' });  // ✅ OK
updateUser(1, { email: 'john@example.com', age: 30 });  // ✅ OK

// ✅ Required<T>: Todas propriedades obrigatórias
type RequiredUser = Required<User>;
// Remove todos os ? (opcionais)

// ✅ Readonly<T>: Todas propriedades readonly
type ReadonlyUser = Readonly<User>;

const user: ReadonlyUser = { id: 1, name: 'John', ... };
// user.name = 'Jane';  // ❌ ERRO: readonly

// ✅ Pick<T, K>: Seleciona apenas certas propriedades
type UserPreview = Pick<User, 'id' | 'name'>;
// { id: number; name: string; }

// ✅ Omit<T, K>: Remove certas propriedades
type UserWithoutId = Omit<User, 'id'>;
// { name: string; email: string; age: number; address: {...} }

// ✅ Record<K, T>: Objeto com chaves K e valores T
type UserRoles = Record<number, 'admin' | 'user' | 'guest'>;
// { [id: number]: 'admin' | 'user' | 'guest' }

const roles: UserRoles = {
  1: 'admin',
  2: 'user',
  3: 'guest'
};

// ✅ Exclude<T, U>: Remove tipos de union
type AllRoles = 'admin' | 'user' | 'guest' | 'superadmin';
type BasicRoles = Exclude<AllRoles, 'superadmin'>;
// 'admin' | 'user' | 'guest'

// ✅ Extract<T, U>: Extrai tipos de union
type AdminRoles = Extract<AllRoles, 'admin' | 'superadmin'>;
// 'admin' | 'superadmin'

// ✅ NonNullable<T>: Remove null e undefined
type MaybeString = string | null | undefined;
type DefiniteString = NonNullable<MaybeString>;
// string

// ✅ ReturnType<T>: Tipo de retorno de função
function getUser() {
  return { id: 1, name: 'John' };
}

type UserReturn = ReturnType<typeof getUser>;
// { id: number; name: string; }

// ✅ Parameters<T>: Tipos dos parâmetros de função
function createUser(name: string, age: number) {
  return { name, age };
}

type CreateUserParams = Parameters<typeof createUser>;
// [name: string, age: number]
```

### 13.2. React Utility Types

```tsx
// ═══════════════════════════════════════════════════════════
// REACT UTILITY TYPES
// ═══════════════════════════════════════════════════════════

// ✅ React.ComponentProps<T>: Props de um componente
const Button = (props: { label: string; onClick: () => void }) => {
  return <button onClick={props.onClick}>{props.label}</button>;
};

type ButtonProps = React.ComponentProps<typeof Button>;
// { label: string; onClick: () => void; }

// ✅ React.ComponentPropsWithRef<T>: Props + ref
type ButtonPropsWithRef = React.ComponentPropsWithRef<'button'>;
// Todas props de <button> + ref

// ✅ React.ComponentPropsWithoutRef<T>: Props sem ref
type ButtonPropsWithoutRef = React.ComponentPropsWithoutRef<'button'>;

// ✅ React.HTMLAttributes<T>: Atributos HTML de elemento
type DivAttributes = React.HTMLAttributes<HTMLDivElement>;
// onClick, className, style, etc.

// ✅ React.CSSProperties: Props de style
const styles: React.CSSProperties = {
  color: 'red',
  fontSize: '16px',
  backgroundColor: '#f0f0f0'
};

// ✅ React.ReactElement vs React.ReactNode
type ElementOnly = React.ReactElement;  // Apenas JSX elements
type NodeAny = React.ReactNode;  // Qualquer coisa renderizável

// ✅ React.FC (não recomendado, mas útil saber)
const Component: React.FC<{ title: string }> = ({ title, children }) => {
  return <div>{title}{children}</div>;
};

// ✅ React.PropsWithChildren<P>: Adiciona children a props
interface ButtonProps {
  variant: 'primary' | 'secondary';
}

type ButtonPropsWithChildren = React.PropsWithChildren<ButtonProps>;
// { variant: ...; children?: ReactNode; }
```

### 13.3. Custom Utility Types

```tsx
// ═══════════════════════════════════════════════════════════
// CUSTOM UTILITY TYPES
// ═══════════════════════════════════════════════════════════

// ✅ DeepPartial: Partial recursivo
type DeepPartial<T> = {
  [P in keyof T]?: T[P] extends object ? DeepPartial<T[P]> : T[P];
};

interface Config {
  api: {
    url: string;
    timeout: number;
    headers: {
      authorization: string;
    };
  };
}

const config: DeepPartial<Config> = {
  api: {
    url: 'https://api.example.com'
    // ✅ timeout e headers opcionais
  }
};

// ✅ DeepReadonly: Readonly recursivo
type DeepReadonly<T> = {
  readonly [P in keyof T]: T[P] extends object ? DeepReadonly<T[P]> : T[P];
};

// ✅ Nullable<T>: Adiciona null
type Nullable<T> = T | null;

type NullableUser = Nullable<User>;
// User | null

// ✅ Optional<T, K>: Torna certas props opcionais
type Optional<T, K extends keyof T> = Omit<T, K> & Partial<Pick<T, K>>;

type UserWithOptionalEmail = Optional<User, 'email'>;
// { id: number; name: string; email?: string; ... }

// ✅ RequireAtLeastOne<T>: Pelo menos uma prop obrigatória
type RequireAtLeastOne<T, Keys extends keyof T = keyof T> = Pick<T, Exclude<keyof T, Keys>> &
  {
    [K in Keys]-?: Required<Pick<T, K>> & Partial<Pick<T, Exclude<Keys, K>>>;
  }[Keys];

interface SearchFilters {
  name?: string;
  email?: string;
  age?: number;
}

type SearchFiltersRequired = RequireAtLeastOne<SearchFilters>;
// Pelo menos name, email ou age deve ser fornecido

// ✅ ValueOf<T>: Union de valores
type ValueOf<T> = T[keyof T];

interface Colors {
  red: '#ff0000';
  green: '#00ff00';
  blue: '#0000ff';
}

type ColorValues = ValueOf<Colors>;
// '#ff0000' | '#00ff00' | '#0000ff'

// ✅ Awaited<T>: Tipo de Promise resolvida (TypeScript 4.5+)
type AsyncUserReturn = Awaited<Promise<User>>;
// User
```

### 13.4. Discriminated Unions

```tsx
// ═══════════════════════════════════════════════════════════
// DISCRIMINATED UNIONS (Tagged Unions)
// ═══════════════════════════════════════════════════════════

// API Response types
interface SuccessResponse {
  status: 'success';
  data: User;
}

interface ErrorResponse {
  status: 'error';
  error: {
    code: string;
    message: string;
  };
}

interface LoadingResponse {
  status: 'loading';
}

type ApiResponse = SuccessResponse | ErrorResponse | LoadingResponse;

function handleResponse(response: ApiResponse) {
  // ✅ Type narrowing com discriminant 'status'
  switch (response.status) {
    case 'success':
      console.log(response.data);  // ✅ TS sabe que é SuccessResponse
      break;
    
    case 'error':
      console.log(response.error.message);  // ✅ TS sabe que é ErrorResponse
      break;
    
    case 'loading':
      console.log('Loading...');  // ✅ TS sabe que é LoadingResponse
      break;
  }
}

// Component com discriminated union
interface ButtonLoadingState {
  state: 'loading';
}

interface ButtonSuccessState {
  state: 'success';
  message: string;
}

interface ButtonErrorState {
  state: 'error';
  error: string;
}

type ButtonState = ButtonLoadingState | ButtonSuccessState | ButtonErrorState;

interface AsyncButtonProps {
  buttonState: ButtonState;
  onClick: () => void;
}

function AsyncButton({ buttonState, onClick }: AsyncButtonProps) {
  return (
    <button onClick={onClick} disabled={buttonState.state === 'loading'}>
      {buttonState.state === 'loading' && 'Loading...'}
      {buttonState.state === 'success' && buttonState.message}
      {buttonState.state === 'error' && `Error: ${buttonState.error}`}
    </button>
  );
}
```

---

## 14. 🚀 **Advanced Patterns**

### 14.1. Compound Components com TypeScript

```tsx
// ═══════════════════════════════════════════════════════════
// COMPOUND COMPONENTS
// ═══════════════════════════════════════════════════════════

interface TabsContextType {
  activeTab: string;
  setActiveTab: (id: string) => void;
}

const TabsContext = createContext<TabsContextType | undefined>(undefined);

function useTabs() {
  const context = useContext(TabsContext);
  if (!context) {
    throw new Error('Tabs components must be used within Tabs');
  }
  return context;
}

// Root component
interface TabsProps {
  defaultTab: string;
  children: React.ReactNode;
}

function Tabs({ defaultTab, children }: TabsProps) {
  const [activeTab, setActiveTab] = useState(defaultTab);

  return (
    <TabsContext.Provider value={{ activeTab, setActiveTab }}>
      <div className="tabs">{children}</div>
    </TabsContext.Provider>
  );
}

// Tab List
interface TabListProps {
  children: React.ReactNode;
}

function TabList({ children }: TabListProps) {
  return <div className="tab-list">{children}</div>;
}

// Tab
interface TabProps {
  id: string;
  children: React.ReactNode;
}

function Tab({ id, children }: TabProps) {
  const { activeTab, setActiveTab } = useTabs();

  return (
    <button
      className={activeTab === id ? 'active' : ''}
      onClick={() => setActiveTab(id)}
    >
      {children}
    </button>
  );
}

// Tab Panel
interface TabPanelProps {
  id: string;
  children: React.ReactNode;
}

function TabPanel({ id, children }: TabPanelProps) {
  const { activeTab } = useTabs();

  if (activeTab !== id) return null;

  return <div className="tab-panel">{children}</div>;
}

// Compor em namespace
const TabsCompound = Object.assign(Tabs, {
  List: TabList,
  Tab,
  Panel: TabPanel
});

// Uso
function App() {
  return (
    <TabsCompound defaultTab="home">
      <TabsCompound.List>
        <TabsCompound.Tab id="home">Home</TabsCompound.Tab>
        <TabsCompound.Tab id="profile">Profile</TabsCompound.Tab>
        <TabsCompound.Tab id="settings">Settings</TabsCompound.Tab>
      </TabsCompound.List>

      <TabsCompound.Panel id="home">Home content</TabsCompound.Panel>
      <TabsCompound.Panel id="profile">Profile content</TabsCompound.Panel>
      <TabsCompound.Panel id="settings">Settings content</TabsCompound.Panel>
    </TabsCompound>
  );
}
```

### 14.2. Render Props with Inversion of Control

```tsx
// ═══════════════════════════════════════════════════════════
// RENDER PROPS com controle total
// ═══════════════════════════════════════════════════════════

interface ToggleRenderProps {
  on: boolean;
  toggle: () => void;
  setOn: (value: boolean) => void;
  setOff: () => void;
}

interface ToggleProps {
  defaultOn?: boolean;
  onToggle?: (on: boolean) => void;
  children: (props: ToggleRenderProps) => React.ReactNode;
}

function Toggle({ defaultOn = false, onToggle, children }: ToggleProps) {
  const [on, setOn] = useState(defaultOn);

  const toggle = useCallback(() => {
    const newValue = !on;
    setOn(newValue);
    onToggle?.(newValue);
  }, [on, onToggle]);

  const setOnCallback = useCallback((value: boolean) => {
    setOn(value);
    onToggle?.(value);
  }, [onToggle]);

  const setOff = useCallback(() => {
    setOn(false);
    onToggle?.(false);
  }, [onToggle]);

  return <>{children({ on, toggle, setOn: setOnCallback, setOff })}</>;
}

// Uso
<Toggle defaultOn={false}>
  {({ on, toggle, setOff }) => (
    <div>
      <button onClick={toggle}>{on ? 'ON' : 'OFF'}</button>
      <button onClick={setOff}>Force OFF</button>
      {on && <p>Content visible</p>}
    </div>
  )}
</Toggle>
```

### 14.3. State Reducer Pattern

```tsx
// ═══════════════════════════════════════════════════════════
// STATE REDUCER PATTERN
// ═══════════════════════════════════════════════════════════

interface CounterState {
  count: number;
}

type CounterAction =
  | { type: 'INCREMENT' }
  | { type: 'DECREMENT' }
  | { type: 'RESET'; payload?: number };

type CounterReducer = (
  state: CounterState,
  action: CounterAction
) => CounterState;

const defaultReducer: CounterReducer = (state, action) => {
  switch (action.type) {
    case 'INCREMENT':
      return { count: state.count + 1 };
    case 'DECREMENT':
      return { count: state.count - 1 };
    case 'RESET':
      return { count: action.payload ?? 0 };
    default:
      return state;
  }
};

interface UseCounterProps {
  initialCount?: number;
  reducer?: CounterReducer;  // ✅ Usuário pode customizar reducer
}

function useCounter({ initialCount = 0, reducer = defaultReducer }: UseCounterProps = {}) {
  const [state, dispatch] = useReducer(reducer, { count: initialCount });

  const increment = useCallback(() => dispatch({ type: 'INCREMENT' }), []);
  const decrement = useCallback(() => dispatch({ type: 'DECREMENT' }), []);
  const reset = useCallback((value?: number) => dispatch({ type: 'RESET', payload: value }), []);

  return {
    count: state.count,
    increment,
    decrement,
    reset
  };
}

// Uso: Customizar comportamento
function CustomCounter() {
  // ✅ Usuário pode limitar counter a max 10
  const customReducer: CounterReducer = (state, action) => {
    const newState = defaultReducer(state, action);
    
    if (newState.count > 10) {
      return { count: 10 };
    }
    
    return newState;
  };

  const { count, increment } = useCounter({
    initialCount: 0,
    reducer: customReducer
  });

  return (
    <div>
      <p>Count: {count}</p>
      <button onClick={increment}>Increment</button>
    </div>
  );
}
```

### 14.4. Controlled Props Pattern

```tsx
// ═══════════════════════════════════════════════════════════
// CONTROLLED PROPS (Controlled vs Uncontrolled)
// ═══════════════════════════════════════════════════════════

interface ControlledInputProps {
  // Controlled mode
  value?: string;
  onChange?: (value: string) => void;
  
  // Uncontrolled mode
  defaultValue?: string;
}

function ControlledInput({ value, onChange, defaultValue }: ControlledInputProps) {
  const [internalValue, setInternalValue] = useState(defaultValue ?? '');

  // ✅ Controlled se value está definido
  const isControlled = value !== undefined;
  const currentValue = isControlled ? value : internalValue;

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const newValue = e.target.value;

    if (!isControlled) {
      setInternalValue(newValue);
    }

    onChange?.(newValue);
  };

  return (
    <input
      type="text"
      value={currentValue}
      onChange={handleChange}
    />
  );
}

// Uso: Controlled
function ControlledExample() {
  const [value, setValue] = useState('');

  return <ControlledInput value={value} onChange={setValue} />;
}

// Uso: Uncontrolled
function UncontrolledExample() {
  return <ControlledInput defaultValue="initial" />;
}
```

### 14.5. Component Injection Pattern

```tsx
// ═══════════════════════════════════════════════════════════
// COMPONENT INJECTION
// ═══════════════════════════════════════════════════════════

interface ListProps<T> {
  items: T[];
  ItemComponent: React.ComponentType<{ item: T }>;  // ✅ Injeta componente
  EmptyComponent?: React.ComponentType;
  LoadingComponent?: React.ComponentType;
  loading?: boolean;
}

function List<T>({
  items,
  ItemComponent,
  EmptyComponent,
  LoadingComponent,
  loading = false
}: ListProps<T>) {
  if (loading && LoadingComponent) {
    return <LoadingComponent />;
  }

  if (items.length === 0 && EmptyComponent) {
    return <EmptyComponent />;
  }

  return (
    <ul>
      {items.map((item, index) => (
        <li key={index}>
          <ItemComponent item={item} />
        </li>
      ))}
    </ul>
  );
}

// Components injetados
interface Product {
  id: number;
  name: string;
  price: number;
}

const ProductItem: React.FC<{ item: Product }> = ({ item }) => (
  <div>
    {item.name} - ${item.price}
  </div>
);

const EmptyState = () => <div>No products found</div>;
const LoadingState = () => <div>Loading products...</div>;

// Uso
<List
  items={products}
  ItemComponent={ProductItem}
  EmptyComponent={EmptyState}
  LoadingComponent={LoadingState}
  loading={isLoading}
/>
```

### 14.6. Provider Pattern with Factory

```tsx
// ═══════════════════════════════════════════════════════════
// PROVIDER FACTORY
// ═══════════════════════════════════════════════════════════

function createContextProvider<T>() {
  const Context = createContext<T | undefined>(undefined);

  function Provider({
    value,
    children
  }: {
    value: T;
    children: React.ReactNode;
  }) {
    return <Context.Provider value={value}>{children}</Context.Provider>;
  }

  function useContextValue(): T {
    const context = useContext(Context);

    if (context === undefined) {
      throw new Error('useContextValue must be used within Provider');
    }

    return context;
  }

  return [Provider, useContextValue] as const;
}

// Uso: Criar múltiplos contexts facilmente
interface UserContextType {
  user: User | null;
  login: (user: User) => void;
  logout: () => void;
}

const [UserProvider, useUser] = createContextProvider<UserContextType>();

interface ThemeContextType {
  theme: 'light' | 'dark';
  toggleTheme: () => void;
}

const [ThemeProvider, useTheme] = createContextProvider<ThemeContextType>();

// Implementação
function App() {
  const [user, setUser] = useState<User | null>(null);

  const userValue: UserContextType = {
    user,
    login: setUser,
    logout: () => setUser(null)
  };

  return (
    <UserProvider value={userValue}>
      <Main />
    </UserProvider>
  );
}
```

---

## 15. 📚 **Recursos e Referências**

### 15.1. Documentação Oficial

**TypeScript:**
- [TypeScript Handbook](https://www.typescriptlang.org/docs/handbook/intro.html)
- [TypeScript Cheat Sheet](https://www.typescriptlang.org/cheatsheets)
- [Utility Types](https://www.typescriptlang.org/docs/handbook/utility-types.html)

**React + TypeScript:**
- [React TypeScript Cheatsheet](https://react-typescript-cheatsheet.netlify.app/)
- [React Docs - TypeScript](https://react.dev/learn/typescript)
- [DefinitelyTyped - @types/react](https://github.com/DefinitelyTyped/DefinitelyTyped/tree/master/types/react)

### 15.2. Ferramentas

**Type Checking:**
- [TypeScript Compiler (tsc)](https://www.typescriptlang.org/docs/handbook/compiler-options.html)
- [ts-node](https://github.com/TypeStrong/ts-node) - Execute TypeScript diretamente

**Linting:**
- [@typescript-eslint](https://typescript-eslint.io/) - ESLint para TypeScript
- [eslint-plugin-react](https://github.com/jsx-eslint/eslint-plugin-react)

**Build Tools:**
- [Vite](https://vitejs.dev/) - Build tool com TypeScript nativo
- [esbuild](https://esbuild.github.io/) - Bundler ultrarrápido
- [swc](https://swc.rs/) - TypeScript compiler rápido

**Type Generation:**
- [ts-to-zod](https://github.com/fabien0102/ts-to-zod) - TypeScript → Zod schemas
- [json-to-ts](https://github.com/MariusAlch/json-to-ts) - JSON → TypeScript interfaces
- [quicktype](https://quicktype.io/) - JSON → TypeScript types

### 15.3. Bibliotecas Populares com TypeScript

**State Management:**
- [Zustand](https://github.com/pmndrs/zustand) - TypeScript-first state
- [Redux Toolkit](https://redux-toolkit.js.org/) - Excelente suporte TS
- [Jotai](https://jotai.org/) - Atomic state com TypeScript

**Forms:**
- [React Hook Form](https://react-hook-form.com/) - TypeScript-first
- [Zod](https://zod.dev/) - Schema validation
- [Yup](https://github.com/jquense/yup) - Validation

**Data Fetching:**
- [TanStack Query](https://tanstack.com/query) - TypeScript-first
- [SWR](https://swr.vercel.app/) - Bom suporte TS
- [tRPC](https://trpc.io/) - End-to-end type safety

**UI Libraries:**
- [Radix UI](https://www.radix-ui.com/) - Headless UI com TypeScript
- [Headless UI](https://headlessui.com/) - TypeScript-first
- [Chakra UI](https://chakra-ui.com/) - Excelente suporte TS

**Routing:**
- [TanStack Router](https://tanstack.com/router) - Type-safe routing
- [React Router](https://reactrouter.com/) - Suporte TypeScript

### 15.4. Artigos e Tutoriais

**Guides:**
- [React TypeScript Cheatsheet](https://react-typescript-cheatsheet.netlify.app/)
- [TypeScript Deep Dive](https://basarat.gitbook.io/typescript/)
- [Total TypeScript](https://www.totaltypescript.com/)

**Blog Posts:**
- [Using TypeScript with React](https://www.typescriptlang.org/docs/handbook/react.html)
- [Advanced TypeScript Patterns](https://kentcdodds.com/blog/advanced-typescript)
- [Type-safe React](https://blog.logrocket.com/type-safe-react/)

### 15.5. Checklist de Best Practices

**Setup:**
- ✅ `strict: true` no tsconfig.json
- ✅ `noUnusedLocals` e `noUnusedParameters`
- ✅ `noImplicitReturns`
- ✅ ESLint com @typescript-eslint
- ✅ Path aliases configurados

**Componentes:**
- ✅ Usar interface para props (não React.FC)
- ✅ Props opcionais com `?`
- ✅ Valores padrão no destructuring
- ✅ Evitar `any` (usar `unknown` se necessário)

**Types:**
- ✅ Interface para objetos (extends)
- ✅ Type para unions/intersections
- ✅ Tipos separados em arquivos `.types.ts`
- ✅ Utility types quando apropriado

**Hooks:**
- ✅ Tipar useState quando não primitivo
- ✅ useReducer com discriminated unions
- ✅ useRef com tipo específico do elemento
- ✅ Custom hooks sempre tipados

**Events:**
- ✅ Usar tipos específicos (React.MouseEvent, etc)
- ✅ Generic no tipo do elemento (<HTMLButtonElement>)
- ✅ Handlers tipados nas props

**Context:**
- ✅ Context com undefined + custom hook
- ✅ Error se usado fora do Provider
- ✅ Tipos separados para State e Actions

### 15.6. Exemplo Completo - Todo App com TypeScript

```tsx
// ═══════════════════════════════════════════════════════════
// TODO APP COMPLETO COM TYPESCRIPT
// ═══════════════════════════════════════════════════════════

// types/todo.types.ts
export interface Todo {
  id: string;
  text: string;
  completed: boolean;
  createdAt: Date;
}

export type TodoFilter = 'all' | 'active' | 'completed';

export interface TodoState {
  todos: Todo[];
  filter: TodoFilter;
}

export type TodoAction =
  | { type: 'ADD_TODO'; payload: string }
  | { type: 'TOGGLE_TODO'; payload: string }
  | { type: 'DELETE_TODO'; payload: string }
  | { type: 'SET_FILTER'; payload: TodoFilter }
  | { type: 'CLEAR_COMPLETED' };

// hooks/useTodos.ts
export function useTodos() {
  const reducer = (state: TodoState, action: TodoAction): TodoState => {
    switch (action.type) {
      case 'ADD_TODO':
        return {
          ...state,
          todos: [
            ...state.todos,
            {
              id: crypto.randomUUID(),
              text: action.payload,
              completed: false,
              createdAt: new Date()
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
        return { ...state, filter: action.payload };

      case 'CLEAR_COMPLETED':
        return {
          ...state,
          todos: state.todos.filter(todo => !todo.completed)
        };

      default:
        return state;
    }
  };

  const [state, dispatch] = useReducer(reducer, {
    todos: [],
    filter: 'all'
  });

  const filteredTodos = useMemo(() => {
    switch (state.filter) {
      case 'active':
        return state.todos.filter(todo => !todo.completed);
      case 'completed':
        return state.todos.filter(todo => todo.completed);
      default:
        return state.todos;
    }
  }, [state.todos, state.filter]);

  const addTodo = useCallback((text: string) => {
    dispatch({ type: 'ADD_TODO', payload: text });
  }, []);

  const toggleTodo = useCallback((id: string) => {
    dispatch({ type: 'TOGGLE_TODO', payload: id });
  }, []);

  const deleteTodo = useCallback((id: string) => {
    dispatch({ type: 'DELETE_TODO', payload: id });
  }, []);

  const setFilter = useCallback((filter: TodoFilter) => {
    dispatch({ type: 'SET_FILTER', payload: filter });
  }, []);

  const clearCompleted = useCallback(() => {
    dispatch({ type: 'CLEAR_COMPLETED' });
  }, []);

  return {
    todos: filteredTodos,
    filter: state.filter,
    addTodo,
    toggleTodo,
    deleteTodo,
    setFilter,
    clearCompleted,
    stats: {
      total: state.todos.length,
      active: state.todos.filter(t => !t.completed).length,
      completed: state.todos.filter(t => t.completed).length
    }
  };
}

// components/TodoItem.tsx
interface TodoItemProps {
  todo: Todo;
  onToggle: (id: string) => void;
  onDelete: (id: string) => void;
}

const TodoItem = memo(({ todo, onToggle, onDelete }: TodoItemProps) => {
  return (
    <li>
      <input
        type="checkbox"
        checked={todo.completed}
        onChange={() => onToggle(todo.id)}
      />
      <span style={{ textDecoration: todo.completed ? 'line-through' : 'none' }}>
        {todo.text}
      </span>
      <button onClick={() => onDelete(todo.id)}>Delete</button>
    </li>
  );
});

TodoItem.displayName = 'TodoItem';

// components/TodoForm.tsx
interface TodoFormProps {
  onSubmit: (text: string) => void;
}

function TodoForm({ onSubmit }: TodoFormProps) {
  const [text, setText] = useState('');

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    if (text.trim()) {
      onSubmit(text);
      setText('');
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <input
        type="text"
        value={text}
        onChange={(e) => setText(e.target.value)}
        placeholder="What needs to be done?"
      />
      <button type="submit">Add</button>
    </form>
  );
}

// components/TodoFilters.tsx
interface TodoFiltersProps {
  current: TodoFilter;
  onChange: (filter: TodoFilter) => void;
}

function TodoFilters({ current, onChange }: TodoFiltersProps) {
  const filters: TodoFilter[] = ['all', 'active', 'completed'];

  return (
    <div>
      {filters.map(filter => (
        <button
          key={filter}
          onClick={() => onChange(filter)}
          className={current === filter ? 'active' : ''}
        >
          {filter}
        </button>
      ))}
    </div>
  );
}

// App.tsx
function App() {
  const {
    todos,
    filter,
    addTodo,
    toggleTodo,
    deleteTodo,
    setFilter,
    clearCompleted,
    stats
  } = useTodos();

  return (
    <div>
      <h1>Todo App</h1>

      <TodoForm onSubmit={addTodo} />

      <TodoFilters current={filter} onChange={setFilter} />

      <ul>
        {todos.map(todo => (
          <TodoItem
            key={todo.id}
            todo={todo}
            onToggle={toggleTodo}
            onDelete={deleteTodo}
          />
        ))}
      </ul>

      <div>
        <p>
          {stats.active} active / {stats.total} total
        </p>
        {stats.completed > 0 && (
          <button onClick={clearCompleted}>Clear completed</button>
        )}
      </div>
    </div>
  );
}

export default App;
```

**Benefícios do código acima:**

```
✅ TYPE SAFETY COMPLETO
   - Todos os tipos definidos
   - Zero uso de any
   - Discriminated unions para actions

✅ MANUTENIBILIDADE
   - Tipos separados
   - Custom hook reutilizável
   - Componentes pequenos e focados

✅ PERFORMANCE
   - memo nos componentes
   - useCallback nos handlers
   - useMemo para filtros

✅ DEVELOPER EXPERIENCE
   - Autocomplete em tudo
   - Erros em tempo de dev
   - Refactoring seguro
```

---

## 🎯 **Conclusão**

### Por que TypeScript com React?

```
1️⃣ TYPE SAFETY
   ✅ Erros em tempo de desenvolvimento
   ✅ Menos bugs em produção
   ✅ Refactoring confiante

2️⃣ DEVELOPER EXPERIENCE
   ✅ Autocomplete inteligente
   ✅ IntelliSense completo
   ✅ Documentação inline

3️⃣ ESCALABILIDADE
   ✅ Codebase grande mantível
   ✅ Onboarding mais fácil
   ✅ Contratos claros entre componentes

4️⃣ PRODUTIVIDADE
   ✅ Menos tempo debugando
   ✅ Mais tempo desenvolvendo features
   ✅ Confiança ao mudar código
```

### Curva de Aprendizado

```
📈 PROGRESSÃO RECOMENDADA:

1. BÁSICO (1-2 semanas)
   - Setup e tsconfig
   - Typing components e props
   - useState e useEffect
   - Event handlers

2. INTERMEDIÁRIO (2-4 semanas)
   - Generics em components
   - Context API
   - Custom hooks
   - Forms com TypeScript

3. AVANÇADO (1-2 meses)
   - Utility types
   - Advanced patterns
   - HOCs e Render Props
   - Type-safe APIs
```

### Regras de Ouro

> **"Começe estrito, relaxe quando necessário"**

**Práticas essenciais:**

1. ✅ `strict: true` sempre
2. ✅ Evite `any` (use `unknown`)
3. ✅ Prefira `interface` para objetos
4. ✅ Use utility types (Partial, Pick, Omit)
5. ✅ Discriminated unions para estados
6. ✅ Custom hooks sempre tipados
7. ✅ Type guards quando necessário
8. ✅ Generics para componentes reutilizáveis

**Não faça:**

1. ❌ Não use `any` (use `unknown`)
2. ❌ Não ignore erros do TS (`@ts-ignore`)
3. ❌ Não desabilite strict mode
4. ❌ Não use `React.FC` (deprecated pattern)
5. ❌ Não deixe tipos implícitos em objetos complexos

---

**🎉 Fim do Guia TypeScript + React!**

**Próximos passos:**
1. Configure projeto com TypeScript
2. Migre um componente por vez
3. Ative strict mode
4. Pratique com utility types
5. Construa app completo type-safe! 🚀

**Recursos adicionais:**
- [React TypeScript Cheatsheet](https://react-typescript-cheatsheet.netlify.app/)
- [TypeScript Playground](https://www.typescriptlang.org/play)
- [Total TypeScript](https://www.totaltypescript.com/)

