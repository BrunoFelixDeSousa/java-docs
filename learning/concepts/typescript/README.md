# 📘 TypeScript - Guia Completo Moderno

**TypeScript** é um superset de JavaScript desenvolvido pela Microsoft que adiciona **tipagem estática opcional** à linguagem.

> "TypeScript = JavaScript + Types + Modern Features"

---

## 📋 Índice

- [Por que TypeScript?](#-por-que-typescript)
- [Setup](#-setup)
- [Tipos Básicos](#-tipos-básicos)
- [Interfaces & Types](#️-interfaces--types)
- [Classes](#-classes)
- [Funções](#-funções)
- [Generics](#-generics)
- [Utility Types](#️-utility-types)
- [Type Guards](#️-type-guards)
- [Decorators](#-decorators)
- [TypeScript Avançado](#-typescript-avançado)
- [Best Practices](#-best-practices)

---

## 🎯 Por que TypeScript?

### ✅ Vantagens
- **Type Safety** - Detecta erros em tempo de compilação
- **IntelliSense** - Autocompletar e navegação poderosos
- **Refactoring** - Refatoração segura e confiável
- **Self-documenting** - Tipos servem como documentação
- **Modern JS** - Acesso a recursos ES Next hoje
- **Interoperabilidade** - Funciona com bibliotecas JavaScript

### ⚠️ Trade-offs
- Curva de aprendizado inicial
- Passo adicional de compilação
- Setup e configuração necessários

---

## 🚀 Setup

### Instalação

```bash
# Global
npm install -g typescript

# Local (projeto)
npm install --save-dev typescript

# Verificar versão
tsc --version
```

### Inicializar Projeto

```bash
npm init -y
npm install --save-dev typescript
npx tsc --init  # Cria tsconfig.json
```

### tsconfig.json (Recomendado)

```json
{
  "compilerOptions": {
    "target": "ES2022",
    "module": "ESNext",
    "lib": ["ES2022", "DOM"],
    "moduleResolution": "bundler",
    "strict": true,
    "esModuleInterop": true,
    "skipLibCheck": true,
    "forceConsistentCasingInFileNames": true,
    "resolveJsonModule": true,
    "isolatedModules": true,
    "outDir": "./dist",
    "rootDir": "./src",
    "sourceMap": true,
    "declaration": true
  },
  "include": ["src/**/*"],
  "exclude": ["node_modules", "dist", "**/*.test.ts"]
}
```

### Compilar

```bash
npx tsc              # Compilar tudo
npx tsc --watch      # Watch mode
npx tsc file.ts      # Arquivo específico
```

---

## 📦 Tipos Básicos

### Primitivos

```typescript
// Tipos básicos
let isDone: boolean = false;
let age: number = 25;
let name: string = "João";
let nothing: null = null;
let notDefined: undefined = undefined;

// Números especiais
let decimal: number = 6;
let hex: number = 0xf00d;
let binary: number = 0b1010;
let octal: number = 0o744;
let big: bigint = 100n;

// Template literals
let greeting: string = `Hello, ${name}!`;
```

### Arrays

```typescript
// Duas sintaxes
let numbers: number[] = [1, 2, 3];
let strings: Array<string> = ["a", "b", "c"];

// Array misto
let mixed: (number | string)[] = [1, "two", 3];

// Array multidimensional
let matrix: number[][] = [
  [1, 2, 3],
  [4, 5, 6]
];

// ReadOnly
const readonlyArr: readonly number[] = [1, 2, 3];
// readonlyArr.push(4); // ❌ Erro
```

### Tuples

```typescript
// Tupla: array com tipos e tamanho fixos
let tuple: [string, number] = ["hello", 42];

// Destructuring
let [text, num] = tuple;

// Tupla com opcional
let optionalTuple: [string, number?] = ["hello"];

// Tupla com rest
let restTuple: [string, ...number[]] = ["hello", 1, 2, 3];

// Named tuples (TS 4.0+)
type Point = [x: number, y: number];
let point: Point = [10, 20];
```

### Enums

```typescript
// Enum numérico
enum Direction {
  Up = 1,
  Down,
  Left,
  Right
}

let dir: Direction = Direction.Up;
console.log(dir); // 1
console.log(Direction[1]); // "Up" (reverse mapping)

// Enum de strings (recomendado)
enum LogLevel {
  Error = "ERROR",
  Warning = "WARNING",
  Info = "INFO"
}

// Enum como constante (mais performático)
const enum Color {
  Red = "#FF0000",
  Green = "#00FF00",
  Blue = "#0000FF"
}

let color = Color.Red; // Compilado como "#FF0000"
```

### Any, Unknown, Never, Void

```typescript
// any: escape hatch (evitar!)
let anything: any = "hello";
anything = 42;
anything.whatever(); // ❌ Sem verificação de tipos

// unknown: any tipado seguro
let unknownValue: unknown = "hello";
// unknownValue.toUpperCase(); // ❌ Erro
if (typeof unknownValue === "string") {
  unknownValue.toUpperCase(); // ✅ OK após type guard
}

// never: valor que nunca ocorre
function error(message: string): never {
  throw new Error(message);
}

function infiniteLoop(): never {
  while (true) {}
}

// void: ausência de valor (funções que não retornam)
function log(message: string): void {
  console.log(message);
}
```

### Literal Types

```typescript
// String literal
let direction: "left" | "right" | "up" | "down";
direction = "left"; // ✅
// direction = "middle"; // ❌ Erro

// Number literal
let diceRoll: 1 | 2 | 3 | 4 | 5 | 6;

// Boolean literal (raro)
let success: true = true;

// Template literal types (TS 4.1+)
type EventName = `on${Capitalize<string>}`;
let event: EventName = "onClick"; // ✅
```

---

## 🏗️ Interfaces & Types

### Interfaces

```typescript
// Interface básica
interface User {
  id: number;
  name: string;
  email: string;
  age?: number; // Opcional
  readonly createdAt: Date; // ReadOnly
}

const user: User = {
  id: 1,
  name: "João",
  email: "joao@example.com",
  createdAt: new Date()
};

// user.id = 2; // ✅ OK
// user.createdAt = new Date(); // ❌ Erro (readonly)

// Extending interfaces
interface Admin extends User {
  role: "admin";
  permissions: string[];
}

// Merging interfaces (declaration merging)
interface Window {
  customProperty: string;
}

interface Window {
  anotherProperty: number;
}

// Window agora tem ambas propriedades
```

### Type Aliases

```typescript
// Type alias básico
type ID = string | number;
type UserID = ID;

// Object type
type Product = {
  id: ID;
  name: string;
  price: number;
  inStock: boolean;
};

// Union types
type Status = "pending" | "approved" | "rejected";

// Intersection types
type HasTimestamps = {
  createdAt: Date;
  updatedAt: Date;
};

type Post = Product & HasTimestamps;

// Function type
type Callback = (data: string) => void;
type MathOperation = (a: number, b: number) => number;
```

### Interface vs Type

```typescript
// ✅ Interface: objetos, classes, herança
interface Animal {
  name: string;
}

interface Dog extends Animal {
  breed: string;
}

// ✅ Type: unions, intersections, primitivos
type Pet = Dog | Cat;
type ID = string | number;

// Regra geral:
// - Use interface para objetos/classes
// - Use type para unions, intersections, utilities
```

### Index Signatures

```typescript
// Index signature
interface StringMap {
  [key: string]: string;
}

const colors: StringMap = {
  primary: "#FF0000",
  secondary: "#00FF00"
};

// Com tipos específicos + index signature
interface Config {
  apiUrl: string;
  timeout: number;
  [key: string]: string | number; // Outras propriedades
}
```

---

## 👥 Classes

### Classes Básicas

```typescript
class Person {
  // Propriedades
  name: string;
  age: number;
  private ssn: string; // Privado
  protected id: number; // Protegido

  // Constructor
  constructor(name: string, age: number, ssn: string, id: number) {
    this.name = name;
    this.age = age;
    this.ssn = ssn;
    this.id = id;
  }

  // Método
  greet(): string {
    return `Hello, I'm ${this.name}`;
  }

  // Getter
  get fullInfo(): string {
    return `${this.name} (${this.age})`;
  }

  // Setter
  set updateAge(newAge: number) {
    if (newAge > 0) this.age = newAge;
  }
}

const person = new Person("João", 25, "123-45-6789", 1);
console.log(person.greet());
// console.log(person.ssn); // ❌ Erro (private)
```

### Parameter Properties (Shorthand)

```typescript
// Shorthand para propriedades
class Product {
  constructor(
    public id: number,
    public name: string,
    private price: number,
    readonly category: string
  ) {}

  getPrice(): number {
    return this.price;
  }
}

const product = new Product(1, "Laptop", 1000, "Electronics");
console.log(product.id); // ✅
// console.log(product.price); // ❌ Erro (private)
```

### Herança

```typescript
class Animal {
  constructor(public name: string) {}

  move(distance: number = 0): void {
    console.log(`${this.name} moved ${distance}m`);
  }
}

class Dog extends Animal {
  bark(): void {
    console.log("Woof! Woof!");
  }

  // Override
  move(distance: number = 5): void {
    console.log("Dog running...");
    super.move(distance);
  }
}

const dog = new Dog("Rex");
dog.bark(); // "Woof! Woof!"
dog.move(10); // "Dog running..." + "Rex moved 10m"
```

### Abstract Classes

```typescript
abstract class Shape {
  abstract area(): number;
  abstract perimeter(): number;

  // Método concreto
  describe(): string {
    return `Area: ${this.area()}, Perimeter: ${this.perimeter()}`;
  }
}

class Circle extends Shape {
  constructor(private radius: number) {
    super();
  }

  area(): number {
    return Math.PI * this.radius ** 2;
  }

  perimeter(): number {
    return 2 * Math.PI * this.radius;
  }
}

const circle = new Circle(5);
console.log(circle.describe());
```

### Static Members

```typescript
class MathUtils {
  static PI: number = 3.14159;

  static calculateCircleArea(radius: number): number {
    return this.PI * radius ** 2;
  }

  static {
    // Static initialization block (TS 4.4+)
    console.log("MathUtils initialized");
  }
}

console.log(MathUtils.PI);
console.log(MathUtils.calculateCircleArea(5));
```

---

## 🔧 Funções

### Function Types

```typescript
// Função nomeada
function add(a: number, b: number): number {
  return a + b;
}

// Arrow function
const subtract = (a: number, b: number): number => a - b;

// Function type
type MathOp = (x: number, y: number) => number;

const multiply: MathOp = (x, y) => x * y;

// Parâmetros opcionais
function greet(name: string, greeting?: string): string {
  return `${greeting || "Hello"}, ${name}!`;
}

// Parâmetro com valor padrão
function createUser(name: string, role: string = "user") {
  return { name, role };
}

// Rest parameters
function sum(...numbers: number[]): number {
  return numbers.reduce((acc, n) => acc + n, 0);
}

console.log(sum(1, 2, 3, 4, 5)); // 15
```

### Overloads

```typescript
// Function overloads
function format(value: string): string;
function format(value: number): string;
function format(value: boolean): string;
function format(value: string | number | boolean): string {
  if (typeof value === "string") {
    return value.toUpperCase();
  } else if (typeof value === "number") {
    return value.toFixed(2);
  } else {
    return value ? "YES" : "NO";
  }
}

console.log(format("hello")); // "HELLO"
console.log(format(42.5)); // "42.50"
console.log(format(true)); // "YES"
```

### This Type

```typescript
class Calculator {
  constructor(private value: number = 0) {}

  add(n: number): this {
    this.value += n;
    return this;
  }

  multiply(n: number): this {
    this.value *= n;
    return this;
  }

  getValue(): number {
    return this.value;
  }
}

const result = new Calculator(5)
  .add(10)
  .multiply(2)
  .getValue();

console.log(result); // 30
```

---

## 🎁 Generics

### Generic Functions

```typescript
// Generic function
function identity<T>(arg: T): T {
  return arg;
}

let output1 = identity<string>("hello");
let output2 = identity<number>(42);
let output3 = identity("auto"); // Type inference

// Generic com constraint
function getProperty<T, K extends keyof T>(obj: T, key: K): T[K] {
  return obj[key];
}

const person = { name: "João", age: 25 };
const name = getProperty(person, "name"); // string
const age = getProperty(person, "age"); // number
```

### Generic Classes

```typescript
class Box<T> {
  constructor(private value: T) {}

  getValue(): T {
    return this.value;
  }

  setValue(value: T): void {
    this.value = value;
  }
}

const stringBox = new Box<string>("hello");
const numberBox = new Box<number>(42);

console.log(stringBox.getValue()); // "hello"
console.log(numberBox.getValue()); // 42
```

### Generic Interfaces

```typescript
interface Repository<T> {
  findById(id: number): Promise<T | null>;
  findAll(): Promise<T[]>;
  save(item: T): Promise<T>;
  delete(id: number): Promise<void>;
}

interface User {
  id: number;
  name: string;
}

class UserRepository implements Repository<User> {
  async findById(id: number): Promise<User | null> {
    // Implementation
    return null;
  }

  async findAll(): Promise<User[]> {
    return [];
  }

  async save(user: User): Promise<User> {
    return user;
  }

  async delete(id: number): Promise<void> {
    // Implementation
  }
}
```

### Generic Constraints

```typescript
// Constraint: deve ter propriedade length
interface HasLength {
  length: number;
}

function logLength<T extends HasLength>(arg: T): void {
  console.log(arg.length);
}

logLength("hello"); // 5
logLength([1, 2, 3]); // 3
logLength({ length: 10 }); // 10
// logLength(42); // ❌ Erro

// Multiple constraints
interface Timestamped {
  timestamp: Date;
}

interface Named {
  name: string;
}

function logItem<T extends Timestamped & Named>(item: T): void {
  console.log(`${item.name} at ${item.timestamp}`);
}
```

---

## 🛠️ Utility Types

TypeScript fornece vários **utility types** built-in:

### Partial & Required

```typescript
interface User {
  id: number;
  name: string;
  email: string;
  age: number;
}

// Partial: todas propriedades opcionais
type PartialUser = Partial<User>;
// { id?: number; name?: string; email?: string; age?: number; }

function updateUser(id: number, updates: Partial<User>) {
  // Atualiza apenas campos fornecidos
}

updateUser(1, { email: "new@example.com" });

// Required: todas propriedades obrigatórias
type RequiredUser = Required<PartialUser>;
```

### Pick & Omit

```typescript
// Pick: seleciona propriedades
type UserPreview = Pick<User, "id" | "name">;
// { id: number; name: string; }

// Omit: exclui propriedades
type UserWithoutId = Omit<User, "id">;
// { name: string; email: string; age: number; }

type CreateUserDto = Omit<User, "id">;
```

### Record

```typescript
// Record: objeto com chaves e valores tipados
type Role = "admin" | "user" | "guest";

type Permissions = Record<Role, string[]>;

const permissions: Permissions = {
  admin: ["read", "write", "delete"],
  user: ["read", "write"],
  guest: ["read"]
};

// Record genérico
type Dictionary<T> = Record<string, T>;

const scores: Dictionary<number> = {
  alice: 95,
  bob: 87,
  charlie: 92
};
```

### Readonly & ReadonlyArray

```typescript
// Readonly: propriedades readonly
type ReadonlyUser = Readonly<User>;

const user: ReadonlyUser = {
  id: 1,
  name: "João",
  email: "joao@example.com",
  age: 25
};

// user.name = "Maria"; // ❌ Erro

// ReadonlyArray
const numbers: ReadonlyArray<number> = [1, 2, 3];
// numbers.push(4); // ❌ Erro
// numbers[0] = 10; // ❌ Erro
```

### ReturnType & Parameters

```typescript
function createUser(name: string, age: number) {
  return { name, age, createdAt: new Date() };
}

// ReturnType: tipo de retorno de função
type User = ReturnType<typeof createUser>;
// { name: string; age: number; createdAt: Date; }

// Parameters: tupla com tipos dos parâmetros
type CreateUserParams = Parameters<typeof createUser>;
// [string, number]

function logParams(...args: CreateUserParams) {
  console.log(args);
}
```

### Exclude, Extract, NonNullable

```typescript
type T1 = Exclude<"a" | "b" | "c", "a">; // "b" | "c"
type T2 = Extract<"a" | "b" | "c", "a" | "f">; // "a"

type T3 = string | number | null | undefined;
type T4 = NonNullable<T3>; // string | number
```

---

## 🛡️ Type Guards

### typeof Guard

```typescript
function processValue(value: string | number) {
  if (typeof value === "string") {
    // TypeScript sabe que value é string aqui
    return value.toUpperCase();
  } else {
    // value é number aqui
    return value.toFixed(2);
  }
}
```

### instanceof Guard

```typescript
class Dog {
  bark() {
    console.log("Woof!");
  }
}

class Cat {
  meow() {
    console.log("Meow!");
  }
}

function makeSound(animal: Dog | Cat) {
  if (animal instanceof Dog) {
    animal.bark();
  } else {
    animal.meow();
  }
}
```

### in Operator

```typescript
interface Car {
  drive(): void;
}

interface Boat {
  sail(): void;
}

function move(vehicle: Car | Boat) {
  if ("drive" in vehicle) {
    vehicle.drive();
  } else {
    vehicle.sail();
  }
}
```

### Custom Type Guards

```typescript
interface Fish {
  swim(): void;
}

interface Bird {
  fly(): void;
}

// Type predicate: `pet is Fish`
function isFish(pet: Fish | Bird): pet is Fish {
  return (pet as Fish).swim !== undefined;
}

function move(pet: Fish | Bird) {
  if (isFish(pet)) {
    pet.swim();
  } else {
    pet.fly();
  }
}
```

### Discriminated Unions

```typescript
interface Success {
  status: "success";
  data: any;
}

interface Error {
  status: "error";
  message: string;
}

type Response = Success | Error;

function handleResponse(response: Response) {
  // Discriminant: status
  if (response.status === "success") {
    console.log(response.data);
  } else {
    console.log(response.message);
  }
}
```

---

## ✨ Decorators

> **Nota**: Decorators são experimentais. Habilite `"experimentalDecorators": true` no tsconfig.json

### Class Decorator

```typescript
function sealed(constructor: Function) {
  Object.seal(constructor);
  Object.seal(constructor.prototype);
}

@sealed
class Greeter {
  greeting: string;
  constructor(message: string) {
    this.greeting = message;
  }
}
```

### Method Decorator

```typescript
function log(target: any, propertyKey: string, descriptor: PropertyDescriptor) {
  const originalMethod = descriptor.value;

  descriptor.value = function (...args: any[]) {
    console.log(`Calling ${propertyKey} with`, args);
    const result = originalMethod.apply(this, args);
    console.log(`Result:`, result);
    return result;
  };

  return descriptor;
}

class Calculator {
  @log
  add(a: number, b: number): number {
    return a + b;
  }
}

const calc = new Calculator();
calc.add(2, 3);
// Logs: "Calling add with [2, 3]"
// Logs: "Result: 5"
```

### Property Decorator

```typescript
function format(formatString: string) {
  return function (target: any, propertyKey: string) {
    let value: string;

    const getter = function () {
      return value;
    };

    const setter = function (newVal: string) {
      value = formatString.replace("%s", newVal);
    };

    Object.defineProperty(target, propertyKey, {
      get: getter,
      set: setter,
      enumerable: true,
      configurable: true
    });
  };
}

class Person {
  @format("Hello, %s!")
  name: string;
}

const person = new Person();
person.name = "João";
console.log(person.name); // "Hello, João!"
```

---

## 🚀 TypeScript Avançado

### Mapped Types

```typescript
type Readonly<T> = {
  readonly [P in keyof T]: T[P];
};

type Optional<T> = {
  [P in keyof T]?: T[P];
};

type Nullable<T> = {
  [P in keyof T]: T[P] | null;
};

interface User {
  id: number;
  name: string;
}

type ReadonlyUser = Readonly<User>;
type OptionalUser = Optional<User>;
type NullableUser = Nullable<User>;
```

### Conditional Types

```typescript
type IsString<T> = T extends string ? true : false;

type A = IsString<string>; // true
type B = IsString<number>; // false

// Exemplo prático
type NonNullable<T> = T extends null | undefined ? never : T;

type C = NonNullable<string | null>; // string
```

### Template Literal Types

```typescript
type EventName = "click" | "scroll" | "mousemove";
type HandlerName = `on${Capitalize<EventName>}`;
// "onClick" | "onScroll" | "onMousemove"

type PropName = `${"top" | "bottom"}${"Left" | "Right"}`;
// "topLeft" | "topRight" | "bottomLeft" | "bottomRight"
```

### Infer Keyword

```typescript
type ReturnType<T> = T extends (...args: any[]) => infer R ? R : never;

function getUser() {
  return { id: 1, name: "João" };
}

type User = ReturnType<typeof getUser>;
// { id: number; name: string; }
```

---

## ✅ Best Practices

### 1. Enable Strict Mode

```json
{
  "compilerOptions": {
    "strict": true,
    "noImplicitAny": true,
    "strictNullChecks": true,
    "strictFunctionTypes": true,
    "strictBindCallApply": true,
    "strictPropertyInitialization": true,
    "noImplicitThis": true,
    "alwaysStrict": true
  }
}
```

### 2. Evite `any`

```typescript
// ❌ Ruim
function process(data: any) {
  return data.value;
}

// ✅ Bom
function process<T>(data: T): T {
  return data;
}

// ✅ Ou use unknown
function process(data: unknown) {
  if (typeof data === "object" && data !== null && "value" in data) {
    return (data as { value: any }).value;
  }
}
```

### 3. Use Type Inference

```typescript
// ❌ Redundante
const name: string = "João";
const age: number = 25;

// ✅ Type inference
const name = "João"; // string
const age = 25; // number

// ✅ Especifique apenas quando necessário
const numbers: number[] = []; // Necessário
```

### 4. Prefira Interfaces para Objetos

```typescript
// ✅ Interface para objetos
interface User {
  id: number;
  name: string;
}

// ✅ Type para unions/intersections
type ID = string | number;
type Status = "pending" | "approved";
```

### 5. Use Const Assertions

```typescript
// Sem const assertion
const config = {
  apiUrl: "https://api.example.com",
  timeout: 5000
};
// Type: { apiUrl: string; timeout: number; }

// Com const assertion
const config = {
  apiUrl: "https://api.example.com",
  timeout: 5000
} as const;
// Type: { readonly apiUrl: "https://api.example.com"; readonly timeout: 5000; }

// Arrays
const colors = ["red", "green", "blue"] as const;
// Type: readonly ["red", "green", "blue"]
```

### 6. Null Safety

```typescript
// ❌ Pode causar erro
function greet(name: string | null) {
  return `Hello, ${name.toUpperCase()}!`; // Erro se name é null
}

// ✅ Verificação explícita
function greet(name: string | null) {
  if (name === null) return "Hello, Guest!";
  return `Hello, ${name.toUpperCase()}!`;
}

// ✅ Optional chaining
function greet(user: { name?: string }) {
  return `Hello, ${user.name?.toUpperCase() ?? "Guest"}!`;
}
```

### 7. Discriminated Unions

```typescript
// ✅ Use discriminated unions para estados
type State =
  | { status: "loading" }
  | { status: "error"; message: string }
  | { status: "success"; data: any };

function handleState(state: State) {
  switch (state.status) {
    case "loading":
      return "Loading...";
    case "error":
      return `Error: ${state.message}`;
    case "success":
      return state.data;
  }
}
```

### 8. Organização de Arquivos

```
src/
├── types/              # Tipos compartilhados
│   ├── models.ts
│   └── api.ts
├── utils/              # Utilitários
│   └── helpers.ts
├── components/         # Componentes
│   └── Button.tsx
└── services/           # Serviços
    └── api.ts
```

---

## 📚 Recursos Adicionais

### Documentação Oficial
- [TypeScript Handbook](https://www.typescriptlang.org/docs/handbook/intro.html)
- [TypeScript Playground](https://www.typescriptlang.org/play)
- [TypeScript Deep Dive](https://basarat.gitbook.io/typescript/)

### Ferramentas
- **ts-node** - Executar TypeScript diretamente
- **tsc-watch** - Watch mode aprimorado
- **typescript-eslint** - ESLint para TypeScript
- **prettier** - Formatação de código

### Instalação de Ferramentas

```bash
npm install --save-dev ts-node tsc-watch
npm install --save-dev @typescript-eslint/parser @typescript-eslint/eslint-plugin
npm install --save-dev prettier
```

---

## 🎯 Exemplo Completo

```typescript
// types/user.ts
export interface User {
  id: number;
  name: string;
  email: string;
  role: "admin" | "user" | "guest";
}

export type CreateUserDto = Omit<User, "id">;

// services/userService.ts
import { User, CreateUserDto } from "../types/user";

class UserService {
  private users: User[] = [];
  private nextId = 1;

  async create(dto: CreateUserDto): Promise<User> {
    const user: User = {
      id: this.nextId++,
      ...dto
    };
    this.users.push(user);
    return user;
  }

  async findById(id: number): Promise<User | null> {
    return this.users.find(u => u.id === id) ?? null;
  }

  async findAll(): Promise<User[]> {
    return [...this.users];
  }

  async update(id: number, updates: Partial<User>): Promise<User | null> {
    const index = this.users.findIndex(u => u.id === id);
    if (index === -1) return null;

    this.users[index] = { ...this.users[index], ...updates };
    return this.users[index];
  }

  async delete(id: number): Promise<boolean> {
    const index = this.users.findIndex(u => u.id === id);
    if (index === -1) return false;

    this.users.splice(index, 1);
    return true;
  }
}

export const userService = new UserService();

// index.ts
import { userService } from "./services/userService";

async function main() {
  const user = await userService.create({
    name: "João Silva",
    email: "joao@example.com",
    role: "user"
  });

  console.log("Created:", user);

  const found = await userService.findById(user.id);
  console.log("Found:", found);

  const all = await userService.findAll();
  console.log("All users:", all);
}

main();
```

---

**Voltar para**: [📁 Conceitos](../../README.md) | [📁 Learning](../../../README.md) | [📁 Repositório Principal](../../../../README.md)
