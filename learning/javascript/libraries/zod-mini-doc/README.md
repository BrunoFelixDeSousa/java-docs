# ✅ Zod - Validação TypeScript-First

**Zod** é a biblioteca mais poderosa para **validação de schemas** com tipagem TypeScript automática.

> "Schema validation with static type inference"

[![Zod](https://img.shields.io/badge/Zod-v3+-3E67B1?style=flat&logo=zod&logoColor=white)](https://zod.dev)
[![TypeScript](https://img.shields.io/badge/TypeScript-Native-3178C6?style=flat&logo=typescript&logoColor=white)](https://www.typescriptlang.org/)

---

## 📋 Índice

- [Por que Zod?](#-por-que-zod)
- [Instalação](#-instalação)
- [Conceitos Básicos](#-conceitos-básicos)
- [Tipos Primitivos](#-tipos-primitivos)
- [Strings Avançadas](#-strings-avançadas)
- [Numbers & Booleans](#-numbers--booleans)
- [Objects](#-objects)
- [Arrays & Tuples](#-arrays--tuples)
- [Unions & Enums](#-unions--enums)
- [Transformações](#-transformações)
- [Validações Personalizadas](#-validações-personalizadas)
- [Integração com React Hook Form](#-integração-com-react-hook-form)
- [Mensagens de Erro](#-mensagens-de-erro)
- [Best Practices](#-best-practices)

---

## 🎯 Por que Zod?

### ✅ Comparação com Alternativas

```typescript
// ❌ Validação manual: Sem type safety
function validateUser(data: any) {
  if (typeof data.email !== 'string') throw new Error('Invalid email');
  if (typeof data.age !== 'number') throw new Error('Invalid age');
  // ... dezenas de validações manuais
  return data; // Type: any ❌
}

// ❌ class-validator: Decorators + classes
import { IsEmail, IsInt, Min } from 'class-validator';

class User {
  @IsEmail()
  email!: string;

  @IsInt()
  @Min(0)
  age!: number;
}
// Problema: Precisa de classes, decorators, reflection

// ✅ Zod: Schema + Type Inference automática
import { z } from 'zod';

const UserSchema = z.object({
  email: z.string().email(),
  age: z.number().int().min(0),
});

type User = z.infer<typeof UserSchema>; // Type automático! ✨
// { email: string; age: number }

const user = UserSchema.parse(data); // Validado + tipado
```

### 🚀 Vantagens do Zod

| Recurso | Descrição |
|---------|-----------|
| **Type Inference** | Tipos TypeScript automáticos do schema |
| **Zero Dependencies** | Sem dependências externas |
| **Runtime + Compile Time** | Validação em runtime + tipos em compile |
| **Composable** | Schemas reutilizáveis e combináveis |
| **Error Messages** | Mensagens customizáveis e localizadas |
| **Transform** | Transformar dados durante validação |
| **Async Validation** | Validações assíncronas (API calls) |

---

## 📦 Instalação

```bash
npm install zod
```

### TypeScript Config

```json
{
  "compilerOptions": {
    "strict": true,              // Obrigatório
    "strictNullChecks": true     // Recomendado
  }
}
```

---

## 💡 Conceitos Básicos

### Parse vs SafeParse

```typescript
import { z } from 'zod';

const schema = z.string();

// parse(): Lança exceção em caso de erro
try {
  const result = schema.parse('hello'); // ✅ "hello"
  const invalid = schema.parse(123);    // ❌ Lança ZodError
} catch (error) {
  console.error(error); // ZodError com detalhes
}

// safeParse(): Retorna objeto { success, data/error }
const result1 = schema.safeParse('hello');
if (result1.success) {
  console.log(result1.data); // "hello"
} else {
  console.log(result1.error); // ZodError
}

const result2 = schema.safeParse(123);
if (!result2.success) {
  console.log(result2.error.issues); // Array de erros
}

// ✅ Usar safeParse() em produção (não lança exceção)
// ✅ Usar parse() em desenvolvimento/testes (fail fast)
```

### Type Inference

```typescript
// Schema define validação
const UserSchema = z.object({
  id: z.number(),
  name: z.string(),
  email: z.string().email(),
});

// Type inferido AUTOMATICAMENTE do schema
type User = z.infer<typeof UserSchema>;
/* Type é:
{
  id: number;
  name: string;
  email: string;
}
*/

// Usar o type em funções
function createUser(data: User) {
  // data é tipado!
  console.log(data.email.toLowerCase());
}

// Validar + inferir em um passo
const user = UserSchema.parse({ 
  id: 1, 
  name: 'João', 
  email: 'joao@example.com' 
});
// user é do tipo User automaticamente ✨
```

---

## 🔤 Tipos Primitivos

### String

```typescript
// Básico
const schema = z.string();

schema.parse('hello');     // ✅ "hello"
schema.parse(123);         // ❌ ZodError: Expected string, received number
schema.parse('');          // ✅ "" (string vazia é válida)
schema.parse(null);        // ❌ ZodError
schema.parse(undefined);   // ❌ ZodError

// String não-vazia
z.string().min(1);          // Pelo menos 1 caractere
z.string().nonempty();      // Alias para min(1) - DEPRECATED no v3.23+

// Comprimento
z.string().length(8);       // Exatamente 8 caracteres
z.string().min(3);          // Mínimo 3
z.string().max(20);         // Máximo 20
z.string().min(3).max(20);  // Entre 3 e 20

// Opcional e nullable
z.string().optional();      // string | undefined
z.string().nullable();      // string | null
z.string().nullish();       // string | null | undefined

// Default value
z.string().default('N/A');  // Se undefined, retorna 'N/A'

// Exemplo completo
const UsernameSchema = z.string()
  .min(3, 'Username muito curto')
  .max(20, 'Username muito longo')
  .regex(/^[a-zA-Z0-9_]+$/, 'Apenas letras, números e _');
```

### Number

```typescript
// Básico
const schema = z.number();

schema.parse(42);          // ✅ 42
schema.parse(-10);         // ✅ -10
schema.parse(3.14);        // ✅ 3.14
schema.parse('42');        // ❌ ZodError (string, não number)
schema.parse(NaN);         // ❌ ZodError
schema.parse(Infinity);    // ❌ ZodError

// Validações numéricas
z.number().int();          // Inteiro
z.number().positive();     // > 0
z.number().nonnegative();  // >= 0
z.number().negative();     // < 0
z.number().nonpositive();  // <= 0

z.number().min(0);         // >= 0
z.number().max(100);       // <= 100
z.number().min(0).max(100); // Entre 0 e 100

// Múltiplo de N
z.number().multipleOf(5);  // Múltiplo de 5 (0, 5, 10, 15...)

// Precisão segura (JavaScript safe integers)
z.number().safe();         // Entre -(2^53 - 1) e (2^53 - 1)

// Finito (não Infinity ou -Infinity)
z.number().finite();

// Exemplo: Idade
const AgeSchema = z.number()
  .int('Idade deve ser inteiro')
  .min(0, 'Idade não pode ser negativa')
  .max(150, 'Idade muito alta');

// Exemplo: Porcentagem
const PercentageSchema = z.number()
  .min(0)
  .max(100)
  .multipleOf(0.01); // Até 2 casas decimais
```

### Boolean

```typescript
const schema = z.boolean();

schema.parse(true);        // ✅ true
schema.parse(false);       // ✅ false
schema.parse('true');      // ❌ ZodError
schema.parse(1);           // ❌ ZodError
schema.parse(0);           // ❌ ZodError
```

### Date

```typescript
const schema = z.date();

schema.parse(new Date());               // ✅
schema.parse('2024-01-01');             // ❌ ZodError (string)
schema.parse(Date.now());               // ❌ ZodError (number)

// Validações de data
z.date().min(new Date('2000-01-01'));   // Depois de 2000
z.date().max(new Date());               // Até hoje

// Exemplo: Data de nascimento
const BirthdateSchema = z.date()
  .max(new Date(), 'Data não pode ser no futuro')
  .min(new Date('1900-01-01'), 'Data muito antiga');
```

---

## 🔡 Strings Avançadas

### Validações Built-in

```typescript
// Email
z.string().email();
z.string().email('Email inválido');

// URL
z.string().url();
z.string().url('URL inválida');

// UUID
z.string().uuid();

// CUID
z.string().cuid();

// ULID
z.string().ulid();

// Regex
z.string().regex(/^\d{3}-\d{3}-\d{4}$/);
z.string().regex(/^[A-Z]{2}\d{4}$/, 'Formato: AA0000');

// Emoji
z.string().emoji();

// IP
z.string().ip();              // IPv4 ou IPv6
z.string().ip({ version: 'v4' }); // Apenas IPv4

// DateTime ISO
z.string().datetime();
z.string().datetime({ offset: true }); // Com timezone
```

### Transformações de String

```typescript
// Trim (remover espaços)
const TrimSchema = z.string().trim();
TrimSchema.parse('  hello  '); // "hello"

// Lowercase
const LowerSchema = z.string().toLowerCase();
LowerSchema.parse('HELLO'); // "hello"

// Uppercase
const UpperSchema = z.string().toUpperCase();
UpperSchema.parse('hello'); // "HELLO"

// Combinado
const EmailSchema = z.string()
  .trim()
  .toLowerCase()
  .email();

EmailSchema.parse('  John@EXAMPLE.com  '); // "john@example.com"
```

### String Enums (Literals)

```typescript
// Valores literais específicos
const RoleSchema = z.enum(['admin', 'user', 'guest']);

type Role = z.infer<typeof RoleSchema>; // 'admin' | 'user' | 'guest'

RoleSchema.parse('admin');    // ✅ "admin"
RoleSchema.parse('superuser'); // ❌ ZodError

// Acessar valores possíveis
RoleSchema.options; // ['admin', 'user', 'guest']

// Native Enum do TypeScript
enum Fruits {
  Apple = 'apple',
  Banana = 'banana',
}

const FruitSchema = z.nativeEnum(Fruits);
FruitSchema.parse('apple'); // ✅
FruitSchema.parse(Fruits.Apple); // ✅
```

---

## 🔢 Numbers & Booleans

### Coercion (Conversão Automática)

```typescript
// String para Number
const NumberCoerceSchema = z.coerce.number();

NumberCoerceSchema.parse('42');    // 42 (number)
NumberCoerceSchema.parse('3.14');  // 3.14
NumberCoerceSchema.parse('abc');   // NaN → ❌ ZodError

// Boolean coercion
const BooleanCoerceSchema = z.coerce.boolean();

BooleanCoerceSchema.parse('true');   // true
BooleanCoerceSchema.parse('false');  // false
BooleanCoerceSchema.parse('yes');    // true
BooleanCoerceSchema.parse('');       // false
BooleanCoerceSchema.parse(1);        // true
BooleanCoerceSchema.parse(0);        // false

// Date coercion
const DateCoerceSchema = z.coerce.date();

DateCoerceSchema.parse('2024-01-01'); // Date object
DateCoerceSchema.parse(1234567890);   // Date from timestamp
```

---

## 📦 Objects

### Object Básico

```typescript
const UserSchema = z.object({
  id: z.number(),
  name: z.string(),
  email: z.string().email(),
  age: z.number().int().positive(),
});

type User = z.infer<typeof UserSchema>;
/*
{
  id: number;
  name: string;
  email: string;
  age: number;
}
*/

const user = UserSchema.parse({
  id: 1,
  name: 'João',
  email: 'joao@example.com',
  age: 25,
});
```

### Campos Opcionais

```typescript
const UserSchema = z.object({
  id: z.number(),
  name: z.string(),
  email: z.string().email(),
  
  // Campos opcionais
  age: z.number().optional(),        // number | undefined
  bio: z.string().nullable(),        // string | null
  avatar: z.string().nullish(),      // string | null | undefined
  
  // Com default
  role: z.string().default('user'),  // string (não opcional)
});

type User = z.infer<typeof UserSchema>;
/*
{
  id: number;
  name: string;
  email: string;
  age?: number;
  bio: string | null;
  avatar?: string | null;
  role: string;
}
*/

// Válido sem campos opcionais
UserSchema.parse({
  id: 1,
  name: 'João',
  email: 'joao@example.com',
  bio: null,
}); // role = 'user' automaticamente
```

### Extends & Merge

```typescript
// Base schema
const PersonSchema = z.object({
  name: z.string(),
  age: z.number(),
});

// Extend (adicionar campos)
const UserSchema = PersonSchema.extend({
  email: z.string().email(),
  password: z.string().min(8),
});

type User = z.infer<typeof UserSchema>;
/*
{
  name: string;
  age: number;
  email: string;
  password: string;
}
*/

// Merge (combinar schemas)
const AddressSchema = z.object({
  street: z.string(),
  city: z.string(),
});

const UserWithAddressSchema = UserSchema.merge(AddressSchema);

type UserWithAddress = z.infer<typeof UserWithAddressSchema>;
/*
{
  name: string;
  age: number;
  email: string;
  password: string;
  street: string;
  city: string;
}
*/
```

### Pick & Omit

```typescript
const UserSchema = z.object({
  id: z.number(),
  name: z.string(),
  email: z.string().email(),
  password: z.string(),
  role: z.string(),
});

// Pick: Selecionar campos
const UserPreviewSchema = UserSchema.pick({
  id: true,
  name: true,
  email: true,
});

type UserPreview = z.infer<typeof UserPreviewSchema>;
// { id: number; name: string; email: string; }

// Omit: Excluir campos
const CreateUserSchema = UserSchema.omit({
  id: true, // ID gerado automaticamente
});

type CreateUserDto = z.infer<typeof CreateUserSchema>;
// { name: string; email: string; password: string; role: string; }

// Omit para retorno público (sem senha)
const PublicUserSchema = UserSchema.omit({
  password: true,
});
```

### Partial & Required

```typescript
const UserSchema = z.object({
  name: z.string(),
  email: z.string(),
  age: z.number(),
});

// Partial: Todos campos opcionais
const PartialUserSchema = UserSchema.partial();

type PartialUser = z.infer<typeof PartialUserSchema>;
// { name?: string; email?: string; age?: number; }

// Partial seletivo
const UpdateUserSchema = UserSchema.partial({
  name: true,
  age: true,
}).required({ email: true }); // Email obrigatório

type UpdateUser = z.infer<typeof UpdateUserSchema>;
// { name?: string; email: string; age?: number; }

// Required: Todos campos obrigatórios
const OptionalUserSchema = z.object({
  name: z.string().optional(),
  email: z.string().optional(),
});

const RequiredUserSchema = OptionalUserSchema.required();
// { name: string; email: string; }
```

### Nested Objects

```typescript
const AddressSchema = z.object({
  street: z.string(),
  city: z.string(),
  country: z.string(),
  zipCode: z.string().regex(/^\d{5}-\d{3}$/),
});

const UserSchema = z.object({
  id: z.number(),
  name: z.string(),
  
  // Objeto aninhado
  address: AddressSchema,
  
  // Objeto aninhado opcional
  company: z.object({
    name: z.string(),
    role: z.string(),
  }).optional(),
});

type User = z.infer<typeof UserSchema>;
/*
{
  id: number;
  name: string;
  address: {
    street: string;
    city: string;
    country: string;
    zipCode: string;
  };
  company?: {
    name: string;
    role: string;
  };
}
*/
```

### Strict & Passthrough

```typescript
const UserSchema = z.object({
  name: z.string(),
  email: z.string(),
});

// Padrão: Ignora campos extras
UserSchema.parse({
  name: 'João',
  email: 'joao@example.com',
  extra: 'ignored', // Ignorado silenciosamente
}); // { name: 'João', email: 'joao@example.com' }

// strict(): Erro se houver campos extras
const StrictUserSchema = UserSchema.strict();

StrictUserSchema.parse({
  name: 'João',
  email: 'joao@example.com',
  extra: 'error', // ❌ ZodError: Unrecognized key 'extra'
});

// passthrough(): Mantém campos extras
const PassthroughUserSchema = UserSchema.passthrough();

PassthroughUserSchema.parse({
  name: 'João',
  email: 'joao@example.com',
  extra: 'kept',
}); // { name: 'João', email: 'joao@example.com', extra: 'kept' }
```

---

## 📚 Arrays & Tuples

### Arrays

```typescript
// Array de strings
const StringArraySchema = z.array(z.string());

StringArraySchema.parse(['a', 'b', 'c']);  // ✅
StringArraySchema.parse([1, 2, 3]);        // ❌ ZodError
StringArraySchema.parse([]);               // ✅ []

// Array com validações
const ArraySchema = z.array(z.string())
  .min(1, 'Pelo menos 1 item')
  .max(10, 'Máximo 10 itens')
  .nonempty('Array não pode ser vazio'); // Alias para min(1)

// Array de objetos
const UserArraySchema = z.array(
  z.object({
    id: z.number(),
    name: z.string(),
  })
);

type UserArray = z.infer<typeof UserArraySchema>;
// { id: number; name: string; }[]

// Array não-vazio
const TagsSchema = z.string().array().nonempty();
```

### Tuples

```typescript
// Tupla: Array com tipos e tamanho fixos
const CoordinateSchema = z.tuple([
  z.number(), // latitude
  z.number(), // longitude
]);

type Coordinate = z.infer<typeof CoordinateSchema>;
// [number, number]

CoordinateSchema.parse([10.5, 20.3]);  // ✅
CoordinateSchema.parse([10.5]);        // ❌ ZodError (falta 1 elemento)
CoordinateSchema.parse([10.5, 20.3, 30]); // ❌ ZodError (excesso)

// Tupla com rest
const PersonTupleSchema = z.tuple([
  z.string(),  // name
  z.number(),  // age
]).rest(z.string()); // ...hobbies

type PersonTuple = z.infer<typeof PersonTupleSchema>;
// [string, number, ...string[]]

PersonTupleSchema.parse(['João', 25, 'reading', 'gaming']); // ✅
```

---

## 🔀 Unions & Enums

### Unions

```typescript
// Union: Aceita um dos tipos
const StringOrNumberSchema = z.union([
  z.string(),
  z.number(),
]);

type StringOrNumber = z.infer<typeof StringOrNumberSchema>;
// string | number

StringOrNumberSchema.parse('hello'); // ✅
StringOrNumberSchema.parse(42);      // ✅
StringOrNumberSchema.parse(true);    // ❌ ZodError

// Syntactic sugar
const StringOrNumberSchema2 = z.string().or(z.number());

// Discriminated Union (recomendado para objects)
const ResponseSchema = z.discriminatedUnion('status', [
  z.object({
    status: z.literal('success'),
    data: z.any(),
  }),
  z.object({
    status: z.literal('error'),
    message: z.string(),
  }),
]);

type Response = z.infer<typeof ResponseSchema>;
/*
| { status: 'success'; data: any }
| { status: 'error'; message: string }
*/

const response = ResponseSchema.parse({
  status: 'success',
  data: { id: 1 },
});

if (response.status === 'success') {
  console.log(response.data); // Type narrowing funciona! ✨
}
```

### Literals

```typescript
// Literal: Valor exato
const TrueSchema = z.literal(true);
const FortyTwoSchema = z.literal(42);
const HelloSchema = z.literal('hello');

TrueSchema.parse(true);   // ✅
TrueSchema.parse(false);  // ❌ ZodError

// Literal union (melhor que enum em muitos casos)
const StatusSchema = z.union([
  z.literal('pending'),
  z.literal('approved'),
  z.literal('rejected'),
]);

type Status = z.infer<typeof StatusSchema>;
// 'pending' | 'approved' | 'rejected'
```

---

## 🔄 Transformações

### Transform

```typescript
// Transformar dados durante validação
const StringToNumberSchema = z.string().transform((val) => parseInt(val));

const result = StringToNumberSchema.parse('42');
console.log(result); // 42 (number, não string)

type Result = z.infer<typeof StringToNumberSchema>; // number

// Transformação com validação
const UppercaseEmailSchema = z.string()
  .email()
  .transform((val) => val.toLowerCase());

UppercaseEmailSchema.parse('John@EXAMPLE.com'); // "john@example.com"

// Transformação complexa
const DateFromStringSchema = z.string()
  .regex(/^\d{4}-\d{2}-\d{2}$/)
  .transform((val) => new Date(val));

const date = DateFromStringSchema.parse('2024-01-15');
console.log(date); // Date object

// Encadear transformações
const ProcessedSchema = z.string()
  .trim()
  .toLowerCase()
  .transform((val) => val.split(' '))
  .transform((arr) => arr.filter(word => word.length > 0));

ProcessedSchema.parse('  Hello   World  '); // ['hello', 'world']
```

### Preprocess

```typescript
// Preprocessar ANTES da validação
const TrimmedStringSchema = z.preprocess(
  (val) => typeof val === 'string' ? val.trim() : val,
  z.string().min(1)
);

TrimmedStringSchema.parse('  hello  '); // "hello"

// Converter string para number
const NumberFromStringSchema = z.preprocess(
  (val) => {
    if (typeof val === 'string') {
      const parsed = parseFloat(val);
      return isNaN(parsed) ? val : parsed;
    }
    return val;
  },
  z.number()
);

NumberFromStringSchema.parse('42.5'); // 42.5 (number)
NumberFromStringSchema.parse(10);     // 10
```

---

## ✨ Validações Personalizadas

### Refine

```typescript
// Validação customizada com refine()
const PasswordSchema = z.string()
  .min(8)
  .refine(
    (val) => /[A-Z]/.test(val),
    { message: 'Senha deve conter pelo menos 1 maiúscula' }
  )
  .refine(
    (val) => /[0-9]/.test(val),
    { message: 'Senha deve conter pelo menos 1 número' }
  );

PasswordSchema.parse('password123'); // ❌ (sem maiúscula)
PasswordSchema.parse('Password123'); // ✅

// Validação entre campos
const PasswordConfirmSchema = z.object({
  password: z.string().min(8),
  confirmPassword: z.string(),
}).refine(
  (data) => data.password === data.confirmPassword,
  {
    message: 'Senhas não conferem',
    path: ['confirmPassword'], // Campo que receberá o erro
  }
);

// Validação assíncrona
const UsernameSchema = z.string().refine(
  async (username) => {
    // Simular check no banco
    const exists = await checkUsernameExists(username);
    return !exists;
  },
  { message: 'Username já existe' }
);

// Uso assíncrono
const result = await UsernameSchema.safeParseAsync('john_doe');
```

### SuperRefine

```typescript
// SuperRefine: Controle total sobre erros
const UserSchema = z.object({
  email: z.string(),
  age: z.number(),
}).superRefine((data, ctx) => {
  // Validar email
  if (!data.email.includes('@')) {
    ctx.addIssue({
      code: z.ZodIssueCode.custom,
      message: 'Email inválido',
      path: ['email'],
    });
  }

  // Validar idade
  if (data.age < 18) {
    ctx.addIssue({
      code: z.ZodIssueCode.custom,
      message: 'Deve ter 18+ anos',
      path: ['age'],
    });
  }

  // Validação entre campos
  if (data.age > 65 && !data.email.includes('.gov')) {
    ctx.addIssue({
      code: z.ZodIssueCode.custom,
      message: 'Idosos devem usar email governamental',
      path: ['email'],
    });
  }
});
```

---

## 🎨 Integração com React Hook Form

### Setup Básico

```bash
npm install react-hook-form @hookform/resolvers zod
```

```typescript
// schemas/userSchema.ts
import { z } from 'zod';

export const UserFormSchema = z.object({
  name: z.string()
    .min(3, 'Nome deve ter pelo menos 3 caracteres')
    .max(50, 'Nome muito longo'),
  
  email: z.string()
    .email('Email inválido'),
  
  age: z.number()
    .int('Idade deve ser inteiro')
    .min(18, 'Deve ter 18+ anos')
    .max(120, 'Idade inválida'),
  
  password: z.string()
    .min(8, 'Senha deve ter pelo menos 8 caracteres')
    .regex(/[A-Z]/, 'Deve conter pelo menos 1 maiúscula')
    .regex(/[0-9]/, 'Deve conter pelo menos 1 número'),
  
  confirmPassword: z.string(),
  
  terms: z.boolean()
    .refine((val) => val === true, {
      message: 'Você deve aceitar os termos',
    }),
}).refine(
  (data) => data.password === data.confirmPassword,
  {
    message: 'Senhas não conferem',
    path: ['confirmPassword'],
  }
);

export type UserFormData = z.infer<typeof UserFormSchema>;

// components/UserForm.tsx
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { UserFormSchema, UserFormData } from '../schemas/userSchema';

export function UserForm() {
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<UserFormData>({
    resolver: zodResolver(UserFormSchema),
    defaultValues: {
      name: '',
      email: '',
      age: 18,
      password: '',
      confirmPassword: '',
      terms: false,
    },
  });

  const onSubmit = async (data: UserFormData) => {
    console.log('Dados validados:', data);
    // Enviar para API
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <div>
        <label htmlFor="name">Nome</label>
        <input id="name" {...register('name')} />
        {errors.name && <span className="error">{errors.name.message}</span>}
      </div>

      <div>
        <label htmlFor="email">Email</label>
        <input id="email" type="email" {...register('email')} />
        {errors.email && <span className="error">{errors.email.message}</span>}
      </div>

      <div>
        <label htmlFor="age">Idade</label>
        <input 
          id="age" 
          type="number" 
          {...register('age', { valueAsNumber: true })} 
        />
        {errors.age && <span className="error">{errors.age.message}</span>}
      </div>

      <div>
        <label htmlFor="password">Senha</label>
        <input id="password" type="password" {...register('password')} />
        {errors.password && <span className="error">{errors.password.message}</span>}
      </div>

      <div>
        <label htmlFor="confirmPassword">Confirmar Senha</label>
        <input id="confirmPassword" type="password" {...register('confirmPassword')} />
        {errors.confirmPassword && (
          <span className="error">{errors.confirmPassword.message}</span>
        )}
      </div>

      <div>
        <label>
          <input type="checkbox" {...register('terms')} />
          Aceito os termos
        </label>
        {errors.terms && <span className="error">{errors.terms.message}</span>}
      </div>

      <button type="submit" disabled={isSubmitting}>
        {isSubmitting ? 'Enviando...' : 'Cadastrar'}
      </button>
    </form>
  );
}
```

### Validação Assíncrona com React Hook Form

```typescript
// Schema com validação assíncrona
const UsernameSchema = z.string()
  .min(3)
  .refine(
    async (username) => {
      const response = await fetch(`/api/check-username?username=${username}`);
      const { available } = await response.json();
      return available;
    },
    { message: 'Username já existe' }
  );

// No formulário, usar mode: 'onChange' ou 'onBlur'
const { register, handleSubmit } = useForm({
  resolver: zodResolver(UserFormSchema),
  mode: 'onBlur', // Valida ao sair do campo
});
```

---

## 📝 Mensagens de Erro

### Mensagens Customizadas

```typescript
// Por validação
const EmailSchema = z.string().email('Formato de email inválido');

// Objeto de mensagens
const PasswordSchema = z.string().min(8, {
  message: 'Senha deve ter pelo menos 8 caracteres',
});

// Mensagens em refine
const AgeSchema = z.number().refine(
  (age) => age >= 18,
  { message: 'Você deve ter 18 anos ou mais' }
);

// Mensagens dinâmicas
const MinLengthSchema = z.string().min(5, (val) => ({
  message: `String muito curta: ${val.actual} caracteres, mínimo ${val.minimum}`,
}));
```

### Error Map Global

```typescript
import { z } from 'zod';

// Definir mensagens globais em português
const customErrorMap: z.ZodErrorMap = (issue, ctx) => {
  if (issue.code === z.ZodIssueCode.invalid_type) {
    if (issue.expected === 'string') {
      return { message: 'Campo deve ser texto' };
    }
    if (issue.expected === 'number') {
      return { message: 'Campo deve ser número' };
    }
  }

  if (issue.code === z.ZodIssueCode.too_small) {
    if (issue.type === 'string') {
      return { message: `Mínimo ${issue.minimum} caracteres` };
    }
    if (issue.type === 'number') {
      return { message: `Valor mínimo: ${issue.minimum}` };
    }
  }

  if (issue.code === z.ZodIssueCode.too_big) {
    if (issue.type === 'string') {
      return { message: `Máximo ${issue.maximum} caracteres` };
    }
  }

  // Mensagem padrão
  return { message: ctx.defaultError };
};

// Aplicar globalmente
z.setErrorMap(customErrorMap);

// Ou por schema
const UserSchema = z.object({
  name: z.string(),
  age: z.number(),
}, customErrorMap);
```

### Formatando Erros

```typescript
const UserSchema = z.object({
  name: z.string().min(3),
  email: z.string().email(),
  age: z.number().min(18),
});

const result = UserSchema.safeParse({
  name: 'Jo',
  email: 'invalid',
  age: 15,
});

if (!result.success) {
  // Erros brutos
  console.log(result.error.issues);
  /*
  [
    {
      code: 'too_small',
      minimum: 3,
      type: 'string',
      inclusive: true,
      message: 'String must contain at least 3 character(s)',
      path: ['name']
    },
    {
      validation: 'email',
      code: 'invalid_string',
      message: 'Invalid email',
      path: ['email']
    },
    ...
  ]
  */

  // Formatar erros por campo
  const formattedErrors = result.error.format();
  /*
  {
    name: { _errors: ['String must contain at least 3 character(s)'] },
    email: { _errors: ['Invalid email'] },
    age: { _errors: ['Number must be greater than or equal to 18'] },
    _errors: []
  }
  */

  // Usar no React
  const nameError = formattedErrors.name?._errors[0];
  const emailError = formattedErrors.email?._errors[0];

  // Flatten (mais fácil para formulários)
  const flatErrors = result.error.flatten();
  /*
  {
    formErrors: [],
    fieldErrors: {
      name: ['String must contain at least 3 character(s)'],
      email: ['Invalid email'],
      age: ['Number must be greater than or equal to 18']
    }
  }
  */
}
```

---

## ✅ Best Practices

### 1. Organize Schemas em Arquivos Separados

```typescript
// ❌ Ruim: Schemas misturados com componentes
function UserForm() {
  const schema = z.object({ name: z.string() });
  // ...
}

// ✅ Bom: Schemas centralizados
// schemas/user.schema.ts
export const UserSchema = z.object({
  id: z.number(),
  name: z.string(),
  email: z.string().email(),
});

export const CreateUserSchema = UserSchema.omit({ id: true });
export const UpdateUserSchema = CreateUserSchema.partial();

export type User = z.infer<typeof UserSchema>;
export type CreateUserDto = z.infer<typeof CreateUserSchema>;
export type UpdateUserDto = z.infer<typeof UpdateUserSchema>;
```

### 2. Reutilize Schemas com Extend/Merge

```typescript
// Base schemas
const TimestampsSchema = z.object({
  createdAt: z.date(),
  updatedAt: z.date(),
});

const UserBaseSchema = z.object({
  name: z.string(),
  email: z.string().email(),
});

// Compor schemas
const UserSchema = UserBaseSchema.merge(TimestampsSchema).extend({
  id: z.number(),
  role: z.enum(['admin', 'user']),
});
```

### 3. Use safeParse() em Produção

```typescript
// ❌ Ruim: parse() pode crashar a aplicação
function processUser(data: unknown) {
  const user = UserSchema.parse(data); // Pode lançar exceção
  return user;
}

// ✅ Bom: safeParse() retorna objeto
function processUser(data: unknown) {
  const result = UserSchema.safeParse(data);
  
  if (!result.success) {
    console.error('Validação falhou:', result.error);
    return null;
  }
  
  return result.data; // Type: User
}
```

### 4. Validações Assíncronas: Use com Cuidado

```typescript
// ⚠️ Validações assíncronas podem ser lentas
const UsernameSchema = z.string().refine(
  async (username) => {
    // Chamada de API a cada validação!
    const exists = await checkUsername(username);
    return !exists;
  },
  'Username já existe'
);

// ✅ Melhor: Debounce no React Hook Form
const { register } = useForm({
  mode: 'onBlur', // Valida apenas ao sair do campo
});

// Ou valide no backend ao submeter
```

### 5. Type Inference > Definir Types Manualmente

```typescript
// ❌ Ruim: Tipos duplicados
interface User {
  id: number;
  name: string;
}

const UserSchema = z.object({
  id: z.number(),
  name: z.string(),
});

// ✅ Bom: Inferir type do schema (single source of truth)
const UserSchema = z.object({
  id: z.number(),
  name: z.string(),
});

type User = z.infer<typeof UserSchema>;
```

### 6. Coercion para Dados de Formulários

```typescript
// HTML inputs sempre retornam strings
// Use coerce para converter automaticamente
const FormSchema = z.object({
  name: z.string(),
  age: z.coerce.number(),        // "25" → 25
  agreed: z.coerce.boolean(),    // "on" → true
  birthdate: z.coerce.date(),    // "2000-01-01" → Date
});

// Ou no React Hook Form
<input 
  type="number" 
  {...register('age', { valueAsNumber: true })} 
/>
```

### 7. Discriminated Unions para Type Safety

```typescript
// ✅ Discriminated union permite type narrowing
const ResponseSchema = z.discriminatedUnion('status', [
  z.object({
    status: z.literal('success'),
    data: z.any(),
  }),
  z.object({
    status: z.literal('error'),
    error: z.string(),
  }),
]);

const response = ResponseSchema.parse(data);

if (response.status === 'success') {
  console.log(response.data); // TypeScript sabe que data existe ✨
} else {
  console.log(response.error); // TypeScript sabe que error existe ✨
}
```

### 8. Strict Mode em Produção

```typescript
// ❌ Padrão: Campos extras são ignorados
const UserSchema = z.object({
  name: z.string(),
  email: z.string(),
});

UserSchema.parse({
  name: 'João',
  email: 'joao@example.com',
  hacker: 'injected!', // Ignorado silenciosamente ⚠️
});

// ✅ Strict: Erro se houver campos extras
const StrictUserSchema = UserSchema.strict();

StrictUserSchema.parse({
  name: 'João',
  email: 'joao@example.com',
  hacker: 'error!', // ❌ ZodError
});
```

---

## 🎯 Exemplo Completo: API REST com Zod

```typescript
// schemas/product.schema.ts
import { z } from 'zod';

export const ProductSchema = z.object({
  id: z.string().uuid(),
  name: z.string().min(3).max(100),
  description: z.string().max(500).optional(),
  price: z.number().positive().multipleOf(0.01),
  stock: z.number().int().nonnegative(),
  category: z.enum(['electronics', 'clothing', 'food', 'other']),
  tags: z.array(z.string()).max(5).optional(),
  createdAt: z.date(),
  updatedAt: z.date(),
});

export const CreateProductSchema = ProductSchema.omit({
  id: true,
  createdAt: true,
  updatedAt: true,
});

export const UpdateProductSchema = CreateProductSchema.partial();

export const ProductQuerySchema = z.object({
  page: z.coerce.number().int().positive().default(1),
  limit: z.coerce.number().int().min(1).max(100).default(20),
  category: z.enum(['electronics', 'clothing', 'food', 'other']).optional(),
  minPrice: z.coerce.number().nonnegative().optional(),
  maxPrice: z.coerce.number().positive().optional(),
  search: z.string().max(100).optional(),
}).refine(
  (data) => {
    if (data.minPrice && data.maxPrice) {
      return data.minPrice <= data.maxPrice;
    }
    return true;
  },
  {
    message: 'minPrice deve ser menor que maxPrice',
    path: ['minPrice'],
  }
);

export type Product = z.infer<typeof ProductSchema>;
export type CreateProductDto = z.infer<typeof CreateProductSchema>;
export type UpdateProductDto = z.infer<typeof UpdateProductSchema>;
export type ProductQuery = z.infer<typeof ProductQuerySchema>;

// api/products.ts (Express example)
import express from 'express';
import { 
  CreateProductSchema, 
  UpdateProductSchema, 
  ProductQuerySchema 
} from '../schemas/product.schema';

const router = express.Router();

// GET /products
router.get('/products', (req, res) => {
  const queryResult = ProductQuerySchema.safeParse(req.query);

  if (!queryResult.success) {
    return res.status(400).json({
      error: 'Parâmetros inválidos',
      details: queryResult.error.format(),
    });
  }

  const query = queryResult.data;
  // Usar query.page, query.limit, etc (tipado!)
  
  res.json({ products: [], total: 0, page: query.page });
});

// POST /products
router.post('/products', (req, res) => {
  const bodyResult = CreateProductSchema.safeParse(req.body);

  if (!bodyResult.success) {
    return res.status(400).json({
      error: 'Dados inválidos',
      details: bodyResult.error.flatten(),
    });
  }

  const productData = bodyResult.data;
  // Criar produto (dados validados e tipados!)
  
  res.status(201).json({ message: 'Produto criado' });
});

// PATCH /products/:id
router.patch('/products/:id', (req, res) => {
  const bodyResult = UpdateProductSchema.safeParse(req.body);

  if (!bodyResult.success) {
    return res.status(400).json({
      error: 'Dados inválidos',
      details: bodyResult.error.flatten(),
    });
  }

  const updates = bodyResult.data;
  // Atualizar produto
  
  res.json({ message: 'Produto atualizado' });
});

export default router;
```

---

## 📚 Recursos Adicionais

### Documentação
- [Zod Official Docs](https://zod.dev)
- [Zod GitHub](https://github.com/colinhacks/zod)

### Ferramentas
- **@hookform/resolvers** - Integração com React Hook Form
- **zod-to-json-schema** - Converter Zod para JSON Schema
- **zod-to-ts** - Gerar TypeScript types de schemas Zod

### Comunidade
- [GitHub Discussions](https://github.com/colinhacks/zod/discussions)
- [Discord](https://discord.gg/RcG33DQJdf)

---

**Voltar para**: [📁 Libraries](../../../README.md) | [📁 JavaScript](../../../../README.md) | [📁 Learning](../../../../../README.md) | [📁 Repositório Principal](../../../../../../README.md)
