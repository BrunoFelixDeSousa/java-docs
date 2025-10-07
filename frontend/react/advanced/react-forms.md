# ⚡ **React Forms - Guia Completo**

> **React Hook Form, Zod, Validação, Multi-Step Forms, File Uploads**

---

## 📋 **Índice**

1. [Introdução a Formulários em React](#1-introdução-a-formulários-em-react)
2. [React Hook Form - Fundamentos](#2-react-hook-form---fundamentos)
3. [Zod - Schema Validation](#3-zod---schema-validation)
4. [Validação Avançada](#4-validação-avançada)
5. [Form State Management](#5-form-state-management)
6. [Multi-Step Forms](#6-multi-step-forms)
7. [File Uploads](#7-file-uploads)
8. [Dynamic Forms](#8-dynamic-forms)
9. [Formulários com UI Libraries](#9-formulários-com-ui-libraries)
10. [Server-Side Validation](#10-server-side-validation)
11. [Formulários Acessíveis](#11-formulários-acessíveis)
12. [Performance Optimization](#12-performance-optimization)
13. [Form Testing](#13-form-testing)
14. [Patterns & Best Practices](#14-patterns--best-practices)
15. [Recursos e Referências](#15-recursos-e-referências)

---

## 1. 📝 **Introdução a Formulários em React**

### 1.1. Controlled Components

```tsx
// ═══════════════════════════════════════════════════════════
// CONTROLLED COMPONENTS (React tradicional)
// ═══════════════════════════════════════════════════════════

import { useState } from 'react';

function LoginForm() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [errors, setErrors] = useState<Record<string, string>>({});

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    
    // ✅ Validation
    const newErrors: Record<string, string> = {};
    
    if (!email) {
      newErrors.email = 'Email is required';
    } else if (!/\S+@\S+\.\S+/.test(email)) {
      newErrors.email = 'Email is invalid';
    }
    
    if (!password) {
      newErrors.password = 'Password is required';
    } else if (password.length < 6) {
      newErrors.password = 'Password must be at least 6 characters';
    }
    
    setErrors(newErrors);
    
    if (Object.keys(newErrors).length === 0) {
      console.log('Submit:', { email, password });
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <div>
        <label htmlFor="email">Email:</label>
        <input
          id="email"
          type="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />
        {errors.email && <span className="error">{errors.email}</span>}
      </div>

      <div>
        <label htmlFor="password">Password:</label>
        <input
          id="password"
          type="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        {errors.password && <span className="error">{errors.password}</span>}
      </div>

      <button type="submit">Login</button>
    </form>
  );
}

/**
 * ❌ PROBLEMAS:
 * - Re-render a cada keystroke
 * - Código verboso
 * - Difícil de escalar
 * - Validação manual
 */
```

### 1.2. Uncontrolled Components

```tsx
// ═══════════════════════════════════════════════════════════
// UNCONTROLLED COMPONENTS (useRef)
// ═══════════════════════════════════════════════════════════

import { useRef, FormEvent } from 'react';

function LoginForm() {
  const emailRef = useRef<HTMLInputElement>(null);
  const passwordRef = useRef<HTMLInputElement>(null);

  const handleSubmit = (e: FormEvent) => {
    e.preventDefault();
    
    const email = emailRef.current?.value;
    const password = passwordRef.current?.value;
    
    console.log('Submit:', { email, password });
  };

  return (
    <form onSubmit={handleSubmit}>
      <div>
        <label htmlFor="email">Email:</label>
        <input
          id="email"
          type="email"
          ref={emailRef}
        />
      </div>

      <div>
        <label htmlFor="password">Password:</label>
        <input
          id="password"
          type="password"
          ref={passwordRef}
        />
      </div>

      <button type="submit">Login</button>
    </form>
  );
}

/**
 * ✅ BENEFÍCIOS:
 * - Menos re-renders
 * - Performance melhor
 * 
 * ❌ PROBLEMAS:
 * - Sem validação em tempo real
 * - Difícil controlar estado
 */
```

### 1.3. Por que React Hook Form?

```tsx
// ═══════════════════════════════════════════════════════════
// REACT HOOK FORM (Melhor dos dois mundos)
// ═══════════════════════════════════════════════════════════

import { useForm } from 'react-hook-form';

interface FormData {
  email: string;
  password: string;
}

function LoginForm() {
  const {
    register,
    handleSubmit,
    formState: { errors }
  } = useForm<FormData>();

  const onSubmit = (data: FormData) => {
    console.log('Submit:', data);
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <div>
        <label htmlFor="email">Email:</label>
        <input
          id="email"
          type="email"
          {...register('email', {
            required: 'Email is required',
            pattern: {
              value: /\S+@\S+\.\S+/,
              message: 'Email is invalid'
            }
          })}
        />
        {errors.email && <span className="error">{errors.email.message}</span>}
      </div>

      <div>
        <label htmlFor="password">Password:</label>
        <input
          id="password"
          type="password"
          {...register('password', {
            required: 'Password is required',
            minLength: {
              value: 6,
              message: 'Password must be at least 6 characters'
            }
          })}
        />
        {errors.password && <span className="error">{errors.password.message}</span>}
      </div>

      <button type="submit">Login</button>
    </form>
  );
}

/**
 * ✅ BENEFÍCIOS:
 * - Menos re-renders (uncontrolled internamente)
 * - Validação integrada
 * - TypeScript support
 * - API simples
 * - Pequeno bundle size (~9kb)
 */
```

### 1.4. Instalação

```bash
# ═══════════════════════════════════════════════════════════
# INSTALAÇÃO
# ═══════════════════════════════════════════════════════════

# React Hook Form
npm install react-hook-form

# Zod (validação)
npm install zod

# Integração RHF + Zod
npm install @hookform/resolvers

# UI (opcional)
npm install @radix-ui/react-label
npm install @radix-ui/react-select
npm install class-variance-authority clsx tailwind-merge
```

---

## 2. 🎣 **React Hook Form - Fundamentos**

### 2.1. Basic Usage

```tsx
// ═══════════════════════════════════════════════════════════
// BASIC FORM
// ═══════════════════════════════════════════════════════════

import { useForm, SubmitHandler } from 'react-hook-form';

interface FormData {
  firstName: string;
  lastName: string;
  email: string;
  age: number;
}

function BasicForm() {
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting }
  } = useForm<FormData>();

  const onSubmit: SubmitHandler<FormData> = async (data) => {
    // ✅ Simulate API call
    await new Promise(resolve => setTimeout(resolve, 1000));
    console.log('Form data:', data);
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
      {/* First Name */}
      <div>
        <label htmlFor="firstName">First Name</label>
        <input
          id="firstName"
          {...register('firstName', {
            required: 'First name is required',
            minLength: {
              value: 2,
              message: 'First name must be at least 2 characters'
            }
          })}
          className="border rounded px-3 py-2 w-full"
        />
        {errors.firstName && (
          <p className="text-red-500 text-sm mt-1">{errors.firstName.message}</p>
        )}
      </div>

      {/* Last Name */}
      <div>
        <label htmlFor="lastName">Last Name</label>
        <input
          id="lastName"
          {...register('lastName', { required: 'Last name is required' })}
          className="border rounded px-3 py-2 w-full"
        />
        {errors.lastName && (
          <p className="text-red-500 text-sm mt-1">{errors.lastName.message}</p>
        )}
      </div>

      {/* Email */}
      <div>
        <label htmlFor="email">Email</label>
        <input
          id="email"
          type="email"
          {...register('email', {
            required: 'Email is required',
            pattern: {
              value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
              message: 'Invalid email address'
            }
          })}
          className="border rounded px-3 py-2 w-full"
        />
        {errors.email && (
          <p className="text-red-500 text-sm mt-1">{errors.email.message}</p>
        )}
      </div>

      {/* Age */}
      <div>
        <label htmlFor="age">Age</label>
        <input
          id="age"
          type="number"
          {...register('age', {
            required: 'Age is required',
            min: {
              value: 18,
              message: 'You must be at least 18 years old'
            },
            max: {
              value: 120,
              message: 'Age must be less than 120'
            },
            valueAsNumber: true  // ✅ Convert to number
          })}
          className="border rounded px-3 py-2 w-full"
        />
        {errors.age && (
          <p className="text-red-500 text-sm mt-1">{errors.age.message}</p>
        )}
      </div>

      <button
        type="submit"
        disabled={isSubmitting}
        className="bg-blue-600 text-white px-6 py-2 rounded hover:bg-blue-700 disabled:opacity-50"
      >
        {isSubmitting ? 'Submitting...' : 'Submit'}
      </button>
    </form>
  );
}
```

### 2.2. Default Values

```tsx
// ═══════════════════════════════════════════════════════════
// DEFAULT VALUES
// ═══════════════════════════════════════════════════════════

interface FormData {
  username: string;
  email: string;
  notifications: boolean;
  role: 'user' | 'admin';
}

function FormWithDefaults() {
  const { register, handleSubmit } = useForm<FormData>({
    defaultValues: {
      username: 'john_doe',
      email: 'john@example.com',
      notifications: true,
      role: 'user'
    }
  });

  const onSubmit = (data: FormData) => {
    console.log(data);
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <input {...register('username')} />
      <input {...register('email')} />
      
      <label>
        <input type="checkbox" {...register('notifications')} />
        Enable notifications
      </label>

      <select {...register('role')}>
        <option value="user">User</option>
        <option value="admin">Admin</option>
      </select>

      <button type="submit">Submit</button>
    </form>
  );
}

// ✅ Async default values
function EditUserForm({ userId }: { userId: string }) {
  const { register, handleSubmit, reset } = useForm<FormData>();

  useEffect(() => {
    async function loadUser() {
      const user = await fetch(`/api/users/${userId}`).then(r => r.json());
      
      reset({
        username: user.username,
        email: user.email,
        notifications: user.notifications,
        role: user.role
      });
    }
    
    loadUser();
  }, [userId, reset]);

  return (
    <form onSubmit={handleSubmit(data => console.log(data))}>
      {/* ... */}
    </form>
  );
}
```

### 2.3. Watch & GetValues

```tsx
// ═══════════════════════════════════════════════════════════
// WATCH & GETVALUES
// ═══════════════════════════════════════════════════════════

function WatchExample() {
  const { register, watch, getValues } = useForm<{
    showEmail: boolean;
    email: string;
    password: string;
  }>();

  // ✅ Watch single field (re-renders on change)
  const showEmail = watch('showEmail');

  // ✅ Watch multiple fields
  const [email, password] = watch(['email', 'password']);

  // ✅ Watch all fields
  const formData = watch();

  // ✅ GetValues (não causa re-render)
  const handleClick = () => {
    const currentEmail = getValues('email');
    console.log('Current email:', currentEmail);
  };

  return (
    <form>
      <label>
        <input type="checkbox" {...register('showEmail')} />
        Show email field
      </label>

      {showEmail && (
        <input
          type="email"
          placeholder="Email"
          {...register('email')}
        />
      )}

      <input
        type="password"
        placeholder="Password"
        {...register('password')}
      />

      <button type="button" onClick={handleClick}>
        Log Current Email
      </button>

      {/* Debug */}
      <pre>{JSON.stringify(formData, null, 2)}</pre>
    </form>
  );
}
```

### 2.4. SetValue & Reset

```tsx
// ═══════════════════════════════════════════════════════════
// SETVALUE & RESET
// ═══════════════════════════════════════════════════════════

function FormControls() {
  const { register, setValue, reset, handleSubmit } = useForm<{
    username: string;
    email: string;
  }>({
    defaultValues: {
      username: '',
      email: ''
    }
  });

  // ✅ Set single value
  const fillUsername = () => {
    setValue('username', 'john_doe', {
      shouldValidate: true,  // Trigger validation
      shouldDirty: true,     // Mark as dirty
      shouldTouch: true      // Mark as touched
    });
  };

  // ✅ Reset to defaults
  const handleReset = () => {
    reset();
  };

  // ✅ Reset to new values
  const loadUserData = () => {
    reset({
      username: 'jane_doe',
      email: 'jane@example.com'
    });
  };

  return (
    <form onSubmit={handleSubmit(console.log)}>
      <input {...register('username')} />
      <input {...register('email')} />

      <button type="button" onClick={fillUsername}>
        Fill Username
      </button>
      
      <button type="button" onClick={handleReset}>
        Reset
      </button>
      
      <button type="button" onClick={loadUserData}>
        Load User Data
      </button>

      <button type="submit">Submit</button>
    </form>
  );
}
```

---

## 3. 🔒 **Zod - Schema Validation**

### 3.1. Basic Zod Schema

```tsx
// ═══════════════════════════════════════════════════════════
// ZOD SCHEMA
// ═══════════════════════════════════════════════════════════

import { z } from 'zod';

// ✅ Define schema
const userSchema = z.object({
  username: z.string()
    .min(3, 'Username must be at least 3 characters')
    .max(20, 'Username must be at most 20 characters'),
  
  email: z.string()
    .email('Invalid email address'),
  
  age: z.number()
    .min(18, 'You must be at least 18 years old')
    .max(120, 'Age must be less than 120'),
  
  password: z.string()
    .min(8, 'Password must be at least 8 characters')
    .regex(/[A-Z]/, 'Password must contain at least one uppercase letter')
    .regex(/[a-z]/, 'Password must contain at least one lowercase letter')
    .regex(/[0-9]/, 'Password must contain at least one number'),
  
  website: z.string()
    .url('Invalid URL')
    .optional(),
  
  terms: z.boolean()
    .refine(val => val === true, 'You must accept the terms')
});

// ✅ Infer TypeScript type
type UserFormData = z.infer<typeof userSchema>;

/**
 * UserFormData é equivalente a:
 * {
 *   username: string;
 *   email: string;
 *   age: number;
 *   password: string;
 *   website?: string | undefined;
 *   terms: boolean;
 * }
 */
```

### 3.2. React Hook Form + Zod

```tsx
// ═══════════════════════════════════════════════════════════
// REACT HOOK FORM + ZOD
// ═══════════════════════════════════════════════════════════

import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';

const formSchema = z.object({
  email: z.string().email('Invalid email'),
  password: z.string().min(8, 'Password must be at least 8 characters')
});

type FormData = z.infer<typeof formSchema>;

function LoginForm() {
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting }
  } = useForm<FormData>({
    resolver: zodResolver(formSchema)  // ✅ Zod resolver
  });

  const onSubmit = async (data: FormData) => {
    console.log('Valid data:', data);
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <div>
        <input
          type="email"
          placeholder="Email"
          {...register('email')}
        />
        {errors.email && <span>{errors.email.message}</span>}
      </div>

      <div>
        <input
          type="password"
          placeholder="Password"
          {...register('password')}
        />
        {errors.password && <span>{errors.password.message}</span>}
      </div>

      <button type="submit" disabled={isSubmitting}>
        {isSubmitting ? 'Logging in...' : 'Login'}
      </button>
    </form>
  );
}
```

### 3.3. Complex Validation

```tsx
// ═══════════════════════════════════════════════════════════
// COMPLEX ZOD VALIDATION
// ═══════════════════════════════════════════════════════════

const registerSchema = z.object({
  email: z.string().email(),
  
  password: z.string().min(8),
  
  confirmPassword: z.string(),
  
  birthDate: z.string()
    .refine(date => {
      const age = new Date().getFullYear() - new Date(date).getFullYear();
      return age >= 18;
    }, 'You must be at least 18 years old'),
  
  phoneNumber: z.string()
    .regex(/^\+?[1-9]\d{1,14}$/, 'Invalid phone number'),
  
  role: z.enum(['user', 'admin', 'moderator']),
  
  tags: z.array(z.string()).min(1, 'Select at least one tag'),
  
  address: z.object({
    street: z.string().min(1),
    city: z.string().min(1),
    zipCode: z.string().regex(/^\d{5}(-\d{4})?$/, 'Invalid ZIP code')
  })
}).refine(data => data.password === data.confirmPassword, {
  message: "Passwords don't match",
  path: ['confirmPassword']  // ✅ Error em confirmPassword
});

type RegisterFormData = z.infer<typeof registerSchema>;

function RegisterForm() {
  const {
    register,
    handleSubmit,
    formState: { errors }
  } = useForm<RegisterFormData>({
    resolver: zodResolver(registerSchema)
  });

  return (
    <form onSubmit={handleSubmit(console.log)}>
      <input {...register('email')} placeholder="Email" />
      {errors.email && <span>{errors.email.message}</span>}

      <input {...register('password')} type="password" placeholder="Password" />
      {errors.password && <span>{errors.password.message}</span>}

      <input {...register('confirmPassword')} type="password" placeholder="Confirm Password" />
      {errors.confirmPassword && <span>{errors.confirmPassword.message}</span>}

      <input {...register('birthDate')} type="date" />
      {errors.birthDate && <span>{errors.birthDate.message}</span>}

      <input {...register('phoneNumber')} placeholder="Phone" />
      {errors.phoneNumber && <span>{errors.phoneNumber.message}</span>}

      <select {...register('role')}>
        <option value="user">User</option>
        <option value="admin">Admin</option>
        <option value="moderator">Moderator</option>
      </select>
      {errors.role && <span>{errors.role.message}</span>}

      <input {...register('address.street')} placeholder="Street" />
      {errors.address?.street && <span>{errors.address.street.message}</span>}

      <input {...register('address.city')} placeholder="City" />
      {errors.address?.city && <span>{errors.address.city.message}</span>}

      <input {...register('address.zipCode')} placeholder="ZIP Code" />
      {errors.address?.zipCode && <span>{errors.address.zipCode.message}</span>}

      <button type="submit">Register</button>
    </form>
  );
}
```

---

## 4. 🔍 **Validação Avançada**

### 4.1. Async Validation

```tsx
// ═══════════════════════════════════════════════════════════
// ASYNC VALIDATION
// ═══════════════════════════════════════════════════════════

import { useForm } from 'react-hook-form';

interface FormData {
  username: string;
  email: string;
}

function SignupForm() {
  const {
    register,
    handleSubmit,
    formState: { errors, isValidating }
  } = useForm<FormData>();

  // ✅ Async validation: Check if username exists
  const validateUsername = async (username: string) => {
    await new Promise(resolve => setTimeout(resolve, 500));  // Simulate API call
    
    const taken = ['admin', 'root', 'user'].includes(username.toLowerCase());
    
    return !taken || 'Username is already taken';
  };

  // ✅ Async validation: Check if email exists
  const validateEmail = async (email: string) => {
    const response = await fetch('/api/check-email', {
      method: 'POST',
      body: JSON.stringify({ email })
    });
    
    const { exists } = await response.json();
    
    return !exists || 'Email is already registered';
  };

  return (
    <form onSubmit={handleSubmit(console.log)}>
      <div>
        <input
          placeholder="Username"
          {...register('username', {
            required: 'Username is required',
            validate: validateUsername  // ✅ Async validation
          })}
        />
        {errors.username && <span>{errors.username.message}</span>}
        {isValidating && <span>Checking username...</span>}
      </div>

      <div>
        <input
          type="email"
          placeholder="Email"
          {...register('email', {
            required: 'Email is required',
            validate: validateEmail  // ✅ Async validation
          })}
        />
        {errors.email && <span>{errors.email.message}</span>}
      </div>

      <button type="submit">Sign Up</button>
    </form>
  );
}
```

### 4.2. Custom Validation

```tsx
// ═══════════════════════════════════════════════════════════
// CUSTOM VALIDATION
// ═══════════════════════════════════════════════════════════

function CustomValidationForm() {
  const { register, handleSubmit, formState: { errors } } = useForm<{
    password: string;
    website: string;
    age: number;
  }>();

  return (
    <form onSubmit={handleSubmit(console.log)}>
      {/* Strong password */}
      <input
        type="password"
        placeholder="Password"
        {...register('password', {
          validate: {
            hasUpperCase: (value) =>
              /[A-Z]/.test(value) || 'Must contain uppercase letter',
            hasLowerCase: (value) =>
              /[a-z]/.test(value) || 'Must contain lowercase letter',
            hasNumber: (value) =>
              /[0-9]/.test(value) || 'Must contain number',
            hasSpecialChar: (value) =>
              /[!@#$%^&*]/.test(value) || 'Must contain special character',
            minLength: (value) =>
              value.length >= 8 || 'Must be at least 8 characters'
          }
        })}
      />
      {errors.password && <span>{errors.password.message}</span>}

      {/* URL validation */}
      <input
        placeholder="Website"
        {...register('website', {
          validate: (value) => {
            try {
              new URL(value);
              return true;
            } catch {
              return 'Invalid URL';
            }
          }
        })}
      />
      {errors.website && <span>{errors.website.message}</span>}

      {/* Custom age validation */}
      <input
        type="number"
        placeholder="Age"
        {...register('age', {
          validate: {
            positive: (value) => value > 0 || 'Age must be positive',
            adult: (value) => value >= 18 || 'Must be 18 or older',
            reasonable: (value) => value <= 120 || 'Invalid age'
          }
        })}
      />
      {errors.age && <span>{errors.age.message}</span>}

      <button type="submit">Submit</button>
    </form>
  );
}
```

### 4.3. Dependent Fields Validation

```tsx
// ═══════════════════════════════════════════════════════════
// DEPENDENT FIELDS (Password confirmation, date ranges, etc)
// ═══════════════════════════════════════════════════════════

import { useForm } from 'react-hook-form';

interface FormData {
  password: string;
  confirmPassword: string;
  startDate: string;
  endDate: string;
  minPrice: number;
  maxPrice: number;
}

function DependentFieldsForm() {
  const {
    register,
    handleSubmit,
    watch,
    formState: { errors }
  } = useForm<FormData>();

  const password = watch('password');
  const startDate = watch('startDate');
  const minPrice = watch('minPrice');

  return (
    <form onSubmit={handleSubmit(console.log)}>
      {/* Password confirmation */}
      <div>
        <input
          type="password"
          placeholder="Password"
          {...register('password', { required: 'Password is required' })}
        />
      </div>

      <div>
        <input
          type="password"
          placeholder="Confirm Password"
          {...register('confirmPassword', {
            required: 'Please confirm password',
            validate: (value) =>
              value === password || 'Passwords do not match'
          })}
        />
        {errors.confirmPassword && <span>{errors.confirmPassword.message}</span>}
      </div>

      {/* Date range */}
      <div>
        <input
          type="date"
          {...register('startDate', { required: 'Start date is required' })}
        />
      </div>

      <div>
        <input
          type="date"
          {...register('endDate', {
            required: 'End date is required',
            validate: (value) =>
              new Date(value) >= new Date(startDate) ||
              'End date must be after start date'
          })}
        />
        {errors.endDate && <span>{errors.endDate.message}</span>}
      </div>

      {/* Price range */}
      <div>
        <input
          type="number"
          placeholder="Min Price"
          {...register('minPrice', {
            required: 'Min price is required',
            valueAsNumber: true
          })}
        />
      </div>

      <div>
        <input
          type="number"
          placeholder="Max Price"
          {...register('maxPrice', {
            required: 'Max price is required',
            valueAsNumber: true,
            validate: (value) =>
              value >= minPrice || 'Max price must be greater than min price'
          })}
        />
        {errors.maxPrice && <span>{errors.maxPrice.message}</span>}
      </div>

      <button type="submit">Submit</button>
    </form>
  );
}
```

### 4.4. Zod Advanced Validation

```tsx
// ═══════════════════════════════════════════════════════════
// ZOD ADVANCED
// ═══════════════════════════════════════════════════════════

import { z } from 'zod';
import { zodResolver } from '@hookform/resolvers/zod';
import { useForm } from 'react-hook-form';

// ✅ 1. Transform
const transformSchema = z.object({
  email: z.string()
    .email()
    .transform(val => val.toLowerCase()),  // ✅ Normalize
  
  tags: z.string()
    .transform(val => val.split(',').map(t => t.trim()))  // ✅ "a, b, c" → ["a", "b", "c"]
});

// ✅ 2. Preprocess
const preprocessSchema = z.object({
  age: z.preprocess(
    (val) => Number(val),  // ✅ Convert string to number
    z.number().min(18)
  )
});

// ✅ 3. Superrefine (complex validation)
const signupSchema = z.object({
  password: z.string().min(8),
  confirmPassword: z.string(),
  username: z.string().min(3),
  email: z.string().email()
}).superRefine((data, ctx) => {
  // ✅ Password match
  if (data.password !== data.confirmPassword) {
    ctx.addIssue({
      code: z.ZodIssueCode.custom,
      message: 'Passwords do not match',
      path: ['confirmPassword']
    });
  }

  // ✅ Username cannot be in email
  if (data.email.includes(data.username)) {
    ctx.addIssue({
      code: z.ZodIssueCode.custom,
      message: 'Email should not contain username',
      path: ['email']
    });
  }
});

// ✅ 4. Discriminated Unions
const paymentSchema = z.discriminatedUnion('method', [
  z.object({
    method: z.literal('credit_card'),
    cardNumber: z.string().length(16),
    cvv: z.string().length(3)
  }),
  z.object({
    method: z.literal('paypal'),
    email: z.string().email()
  }),
  z.object({
    method: z.literal('bank_transfer'),
    accountNumber: z.string(),
    routingNumber: z.string()
  })
]);

type PaymentFormData = z.infer<typeof paymentSchema>;

function PaymentForm() {
  const {
    register,
    watch,
    handleSubmit,
    formState: { errors }
  } = useForm<PaymentFormData>({
    resolver: zodResolver(paymentSchema)
  });

  const method = watch('method');

  return (
    <form onSubmit={handleSubmit(console.log)}>
      <select {...register('method')}>
        <option value="credit_card">Credit Card</option>
        <option value="paypal">PayPal</option>
        <option value="bank_transfer">Bank Transfer</option>
      </select>

      {method === 'credit_card' && (
        <>
          <input {...register('cardNumber')} placeholder="Card Number" />
          {errors.cardNumber && <span>{errors.cardNumber.message}</span>}
          
          <input {...register('cvv')} placeholder="CVV" />
          {errors.cvv && <span>{errors.cvv.message}</span>}
        </>
      )}

      {method === 'paypal' && (
        <>
          <input {...register('email')} placeholder="PayPal Email" />
          {errors.email && <span>{errors.email.message}</span>}
        </>
      )}

      {method === 'bank_transfer' && (
        <>
          <input {...register('accountNumber')} placeholder="Account Number" />
          {errors.accountNumber && <span>{errors.accountNumber.message}</span>}
          
          <input {...register('routingNumber')} placeholder="Routing Number" />
          {errors.routingNumber && <span>{errors.routingNumber.message}</span>}
        </>
      )}

      <button type="submit">Pay</button>
    </form>
  );
}
```

### 4.5. Debounced Validation

```tsx
// ═══════════════════════════════════════════════════════════
// DEBOUNCED VALIDATION (Performance)
// ═══════════════════════════════════════════════════════════

import { useForm } from 'react-hook-form';
import { useCallback } from 'react';
import { debounce } from 'lodash';  // npm install lodash

interface FormData {
  username: string;
}

function DebouncedValidationForm() {
  const {
    register,
    handleSubmit,
    formState: { errors }
  } = useForm<FormData>({
    mode: 'onChange'  // ✅ Validate on change
  });

  // ✅ Debounced validation (500ms delay)
  const validateUsername = useCallback(
    debounce(async (username: string) => {
      console.log('Checking username:', username);
      
      const response = await fetch(`/api/check-username?username=${username}`);
      const { available } = await response.json();
      
      return available || 'Username is taken';
    }, 500),
    []
  );

  return (
    <form onSubmit={handleSubmit(console.log)}>
      <input
        placeholder="Username"
        {...register('username', {
          required: 'Username is required',
          validate: validateUsername
        })}
      />
      {errors.username && <span>{errors.username.message}</span>}

      <button type="submit">Submit</button>
    </form>
  );
}

/**
 * ✅ BENEFÍCIOS:
 * - Reduz chamadas API
 * - Melhor UX (não valida cada keystroke)
 * - Performance otimizada
 */
```

---

## 5. 📊 **Form State Management**

### 5.1. Form State

```tsx
// ═══════════════════════════════════════════════════════════
// FORM STATE
// ═══════════════════════════════════════════════════════════

import { useForm } from 'react-hook-form';

interface FormData {
  firstName: string;
  lastName: string;
  email: string;
}

function FormStateExample() {
  const {
    register,
    handleSubmit,
    formState: {
      errors,           // ✅ Validation errors
      isDirty,          // ✅ Form foi modificado
      dirtyFields,      // ✅ Quais campos foram modificados
      touchedFields,    // ✅ Quais campos foram tocados (blur)
      isSubmitted,      // ✅ Form foi submetido
      isSubmitting,     // ✅ Form está submetendo
      isSubmitSuccessful, // ✅ Submit teve sucesso
      submitCount,      // ✅ Quantas vezes foi submetido
      isValid,          // ✅ Form é válido
      isValidating      // ✅ Está validando
    }
  } = useForm<FormData>({
    mode: 'onChange',  // Validate on change
    defaultValues: {
      firstName: '',
      lastName: '',
      email: ''
    }
  });

  const onSubmit = async (data: FormData) => {
    await new Promise(resolve => setTimeout(resolve, 1000));
    console.log('Submit:', data);
  };

  return (
    <div>
      <form onSubmit={handleSubmit(onSubmit)}>
        <input {...register('firstName', { required: true })} placeholder="First Name" />
        <input {...register('lastName', { required: true })} placeholder="Last Name" />
        <input {...register('email', { required: true })} placeholder="Email" />

        <button type="submit" disabled={isSubmitting || !isDirty || !isValid}>
          {isSubmitting ? 'Submitting...' : 'Submit'}
        </button>
      </form>

      {/* Debug Panel */}
      <div className="debug-panel">
        <h3>Form State:</h3>
        <pre>{JSON.stringify({
          isDirty,
          dirtyFields,
          touchedFields,
          isSubmitted,
          isSubmitting,
          isSubmitSuccessful,
          submitCount,
          isValid,
          isValidating,
          errors
        }, null, 2)}</pre>
      </div>
    </div>
  );
}
```

### 5.2. Validation Modes

```tsx
// ═══════════════════════════════════════════════════════════
// VALIDATION MODES
// ═══════════════════════════════════════════════════════════

import { useForm } from 'react-hook-form';

interface FormData {
  username: string;
}

// ✅ 1. onSubmit (default) - Valida apenas no submit
function OnSubmitMode() {
  const { register, handleSubmit, formState: { errors } } = useForm<FormData>({
    mode: 'onSubmit'  // ✅ Validate on submit only
  });

  return (
    <form onSubmit={handleSubmit(console.log)}>
      <input {...register('username', { required: 'Required' })} />
      {errors.username && <span>{errors.username.message}</span>}
      <button type="submit">Submit</button>
    </form>
  );
}

// ✅ 2. onBlur - Valida quando campo perde foco
function OnBlurMode() {
  const { register, handleSubmit, formState: { errors } } = useForm<FormData>({
    mode: 'onBlur'  // ✅ Validate on blur
  });

  return (
    <form onSubmit={handleSubmit(console.log)}>
      <input {...register('username', { required: 'Required' })} />
      {errors.username && <span>{errors.username.message}</span>}
      <button type="submit">Submit</button>
    </form>
  );
}

// ✅ 3. onChange - Valida em cada mudança
function OnChangeMode() {
  const { register, handleSubmit, formState: { errors } } = useForm<FormData>({
    mode: 'onChange'  // ✅ Validate on every change
  });

  return (
    <form onSubmit={handleSubmit(console.log)}>
      <input {...register('username', { required: 'Required' })} />
      {errors.username && <span>{errors.username.message}</span>}
      <button type="submit">Submit</button>
    </form>
  );
}

// ✅ 4. onTouched - Valida após primeiro blur, depois onChange
function OnTouchedMode() {
  const { register, handleSubmit, formState: { errors } } = useForm<FormData>({
    mode: 'onTouched'  // ✅ Validate after blur, then onChange
  });

  return (
    <form onSubmit={handleSubmit(console.log)}>
      <input {...register('username', { required: 'Required' })} />
      {errors.username && <span>{errors.username.message}</span>}
      <button type="submit">Submit</button>
    </form>
  );
}

// ✅ 5. all - Valida em blur e change
function AllMode() {
  const { register, handleSubmit, formState: { errors } } = useForm<FormData>({
    mode: 'all'  // ✅ Validate on blur AND change
  });

  return (
    <form onSubmit={handleSubmit(console.log)}>
      <input {...register('username', { required: 'Required' })} />
      {errors.username && <span>{errors.username.message}</span>}
      <button type="submit">Submit</button>
    </form>
  );
}

/**
 * 📌 RECOMENDAÇÕES:
 * - onSubmit: Forms simples (menos distrações)
 * - onBlur: Melhor UX (não valida enquanto digita)
 * - onChange: Feedback imediato (bom para senhas)
 * - onTouched: Híbrido (melhor dos dois mundos) ✅
 */
```

### 5.3. Trigger Validation

```tsx
// ═══════════════════════════════════════════════════════════
// TRIGGER VALIDATION (Manual)
// ═══════════════════════════════════════════════════════════

import { useForm } from 'react-hook-form';

interface FormData {
  email: string;
  password: string;
  terms: boolean;
}

function ManualValidationForm() {
  const {
    register,
    handleSubmit,
    trigger,  // ✅ Manual trigger
    formState: { errors }
  } = useForm<FormData>();

  // ✅ Validate single field
  const validateEmail = async () => {
    const isValid = await trigger('email');
    console.log('Email is valid:', isValid);
  };

  // ✅ Validate multiple fields
  const validateCredentials = async () => {
    const isValid = await trigger(['email', 'password']);
    console.log('Credentials are valid:', isValid);
  };

  // ✅ Validate all fields
  const validateAll = async () => {
    const isValid = await trigger();
    console.log('Form is valid:', isValid);
  };

  return (
    <form onSubmit={handleSubmit(console.log)}>
      <input
        type="email"
        {...register('email', { required: 'Email is required' })}
        placeholder="Email"
      />
      {errors.email && <span>{errors.email.message}</span>}
      <button type="button" onClick={validateEmail}>
        Validate Email
      </button>

      <input
        type="password"
        {...register('password', { required: 'Password is required' })}
        placeholder="Password"
      />
      {errors.password && <span>{errors.password.message}</span>}

      <label>
        <input
          type="checkbox"
          {...register('terms', { required: 'You must accept terms' })}
        />
        Accept Terms
      </label>
      {errors.terms && <span>{errors.terms.message}</span>}

      <button type="button" onClick={validateCredentials}>
        Validate Credentials
      </button>

      <button type="button" onClick={validateAll}>
        Validate All
      </button>

      <button type="submit">Submit</button>
    </form>
  );
}
```

### 5.4. Clear Errors

```tsx
// ═══════════════════════════════════════════════════════════
// CLEAR ERRORS
// ═══════════════════════════════════════════════════════════

import { useForm } from 'react-hook-form';

interface FormData {
  username: string;
  email: string;
}

function ClearErrorsForm() {
  const {
    register,
    handleSubmit,
    setError,
    clearErrors,
    formState: { errors }
  } = useForm<FormData>();

  // ✅ Set custom error
  const setCustomError = () => {
    setError('username', {
      type: 'manual',
      message: 'This username is not allowed'
    });
  };

  // ✅ Clear single error
  const clearUsernameError = () => {
    clearErrors('username');
  };

  // ✅ Clear multiple errors
  const clearMultipleErrors = () => {
    clearErrors(['username', 'email']);
  };

  // ✅ Clear all errors
  const clearAllErrors = () => {
    clearErrors();
  };

  return (
    <form onSubmit={handleSubmit(console.log)}>
      <input
        {...register('username', { required: 'Username is required' })}
        placeholder="Username"
      />
      {errors.username && <span>{errors.username.message}</span>}

      <input
        {...register('email', { required: 'Email is required' })}
        placeholder="Email"
      />
      {errors.email && <span>{errors.email.message}</span>}

      <button type="button" onClick={setCustomError}>
        Set Custom Error
      </button>

      <button type="button" onClick={clearUsernameError}>
        Clear Username Error
      </button>

      <button type="button" onClick={clearMultipleErrors}>
        Clear Multiple Errors
      </button>

      <button type="button" onClick={clearAllErrors}>
        Clear All Errors
      </button>

      <button type="submit">Submit</button>
    </form>
  );
}
```

---

## 6. 🪜 **Multi-Step Forms**

### 6.1. Basic Multi-Step

```tsx
// ═══════════════════════════════════════════════════════════
// MULTI-STEP FORM (Basic)
// ═══════════════════════════════════════════════════════════

import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';

// ✅ Step 1 schema
const step1Schema = z.object({
  firstName: z.string().min(2, 'First name must be at least 2 characters'),
  lastName: z.string().min(2, 'Last name must be at least 2 characters'),
  email: z.string().email('Invalid email address')
});

// ✅ Step 2 schema
const step2Schema = z.object({
  address: z.string().min(5, 'Address is required'),
  city: z.string().min(2, 'City is required'),
  zipCode: z.string().regex(/^\d{5}$/, 'ZIP code must be 5 digits')
});

// ✅ Step 3 schema
const step3Schema = z.object({
  cardNumber: z.string().length(16, 'Card number must be 16 digits'),
  cvv: z.string().length(3, 'CVV must be 3 digits')
});

// ✅ Complete schema (all steps)
const completeSchema = step1Schema.merge(step2Schema).merge(step3Schema);

type FormData = z.infer<typeof completeSchema>;

function MultiStepForm() {
  const [step, setStep] = useState(1);
  
  const {
    register,
    handleSubmit,
    trigger,
    getValues,
    formState: { errors }
  } = useForm<FormData>({
    resolver: zodResolver(completeSchema),
    mode: 'onChange'
  });

  const nextStep = async () => {
    let isValid = false;

    if (step === 1) {
      isValid = await trigger(['firstName', 'lastName', 'email']);
    } else if (step === 2) {
      isValid = await trigger(['address', 'city', 'zipCode']);
    }

    if (isValid) {
      setStep(prev => prev + 1);
    }
  };

  const prevStep = () => {
    setStep(prev => prev - 1);
  };

  const onSubmit = (data: FormData) => {
    console.log('Final submission:', data);
  };

  return (
    <div>
      {/* Progress Bar */}
      <div className="progress">
        <div className="progress-bar" style={{ width: `${(step / 3) * 100}%` }} />
      </div>

      <form onSubmit={handleSubmit(onSubmit)}>
        {/* Step 1: Personal Info */}
        {step === 1 && (
          <div>
            <h2>Step 1: Personal Information</h2>
            
            <input
              {...register('firstName')}
              placeholder="First Name"
            />
            {errors.firstName && <span>{errors.firstName.message}</span>}

            <input
              {...register('lastName')}
              placeholder="Last Name"
            />
            {errors.lastName && <span>{errors.lastName.message}</span>}

            <input
              type="email"
              {...register('email')}
              placeholder="Email"
            />
            {errors.email && <span>{errors.email.message}</span>}

            <button type="button" onClick={nextStep}>
              Next
            </button>
          </div>
        )}

        {/* Step 2: Address */}
        {step === 2 && (
          <div>
            <h2>Step 2: Address</h2>
            
            <input
              {...register('address')}
              placeholder="Address"
            />
            {errors.address && <span>{errors.address.message}</span>}

            <input
              {...register('city')}
              placeholder="City"
            />
            {errors.city && <span>{errors.city.message}</span>}

            <input
              {...register('zipCode')}
              placeholder="ZIP Code"
            />
            {errors.zipCode && <span>{errors.zipCode.message}</span>}

            <button type="button" onClick={prevStep}>
              Previous
            </button>
            <button type="button" onClick={nextStep}>
              Next
            </button>
          </div>
        )}

        {/* Step 3: Payment */}
        {step === 3 && (
          <div>
            <h2>Step 3: Payment</h2>
            
            <input
              {...register('cardNumber')}
              placeholder="Card Number"
              maxLength={16}
            />
            {errors.cardNumber && <span>{errors.cardNumber.message}</span>}

            <input
              {...register('cvv')}
              placeholder="CVV"
              maxLength={3}
            />
            {errors.cvv && <span>{errors.cvv.message}</span>}

            <button type="button" onClick={prevStep}>
              Previous
            </button>
            <button type="submit">
              Submit
            </button>
          </div>
        )}
      </form>

      {/* Debug */}
      <pre>{JSON.stringify(getValues(), null, 2)}</pre>
    </div>
  );
}
```

### 6.2. Multi-Step with Context

```tsx
// ═══════════════════════════════════════════════════════════
// MULTI-STEP WITH CONTEXT (Shared state)
// ═══════════════════════════════════════════════════════════

import { createContext, useContext, useState, ReactNode } from 'react';
import { useForm, FormProvider, UseFormReturn } from 'react-hook-form';
import { z } from 'zod';
import { zodResolver } from '@hookform/resolvers/zod';

// Schema
const formSchema = z.object({
  // Step 1
  firstName: z.string().min(2),
  lastName: z.string().min(2),
  
  // Step 2
  email: z.string().email(),
  phone: z.string().min(10),
  
  // Step 3
  bio: z.string().min(10)
});

type FormData = z.infer<typeof formSchema>;

// Context
interface MultiStepContextType {
  step: number;
  nextStep: () => void;
  prevStep: () => void;
  goToStep: (step: number) => void;
}

const MultiStepContext = createContext<MultiStepContextType | null>(null);

function useMultiStep() {
  const context = useContext(MultiStepContext);
  if (!context) {
    throw new Error('useMultiStep must be used within MultiStepProvider');
  }
  return context;
}

// Provider
function MultiStepProvider({ children }: { children: ReactNode }) {
  const [step, setStep] = useState(1);

  const nextStep = () => setStep(prev => Math.min(prev + 1, 3));
  const prevStep = () => setStep(prev => Math.max(prev - 1, 1));
  const goToStep = (step: number) => setStep(step);

  return (
    <MultiStepContext.Provider value={{ step, nextStep, prevStep, goToStep }}>
      {children}
    </MultiStepContext.Provider>
  );
}

// Step 1 Component
function Step1() {
  const { nextStep } = useMultiStep();
  const { register, formState: { errors }, trigger } = useFormContext<FormData>();

  const handleNext = async () => {
    const isValid = await trigger(['firstName', 'lastName']);
    if (isValid) nextStep();
  };

  return (
    <div>
      <h2>Personal Info</h2>
      <input {...register('firstName')} placeholder="First Name" />
      {errors.firstName && <span>{errors.firstName.message}</span>}

      <input {...register('lastName')} placeholder="Last Name" />
      {errors.lastName && <span>{errors.lastName.message}</span>}

      <button onClick={handleNext}>Next</button>
    </div>
  );
}

// Step 2 Component
function Step2() {
  const { nextStep, prevStep } = useMultiStep();
  const { register, formState: { errors }, trigger } = useFormContext<FormData>();

  const handleNext = async () => {
    const isValid = await trigger(['email', 'phone']);
    if (isValid) nextStep();
  };

  return (
    <div>
      <h2>Contact Info</h2>
      <input {...register('email')} placeholder="Email" />
      {errors.email && <span>{errors.email.message}</span>}

      <input {...register('phone')} placeholder="Phone" />
      {errors.phone && <span>{errors.phone.message}</span>}

      <button onClick={prevStep}>Previous</button>
      <button onClick={handleNext}>Next</button>
    </div>
  );
}

// Step 3 Component
function Step3() {
  const { prevStep } = useMultiStep();
  const { register, formState: { errors } } = useFormContext<FormData>();

  return (
    <div>
      <h2>Bio</h2>
      <textarea {...register('bio')} placeholder="Tell us about yourself" />
      {errors.bio && <span>{errors.bio.message}</span>}

      <button onClick={prevStep}>Previous</button>
      <button type="submit">Submit</button>
    </div>
  );
}

// Main Form
function useFormContext<T>() {
  return useForm<T>() as unknown as UseFormReturn<T>;
}

function MultiStepFormWithContext() {
  const methods = useForm<FormData>({
    resolver: zodResolver(formSchema),
    mode: 'onChange'
  });

  const { step } = useMultiStep();

  const onSubmit = (data: FormData) => {
    console.log('Final data:', data);
  };

  return (
    <FormProvider {...methods}>
      <form onSubmit={methods.handleSubmit(onSubmit)}>
        {step === 1 && <Step1 />}
        {step === 2 && <Step2 />}
        {step === 3 && <Step3 />}
      </form>
    </FormProvider>
  );
}

// App
export default function App() {
  return (
    <MultiStepProvider>
      <MultiStepFormWithContext />
    </MultiStepProvider>
  );
}
```

### 6.3. Multi-Step with Review

```tsx
// ═══════════════════════════════════════════════════════════
// MULTI-STEP WITH REVIEW STEP
// ═══════════════════════════════════════════════════════════

import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { z } from 'zod';
import { zodResolver } from '@hookform/resolvers/zod';

const schema = z.object({
  firstName: z.string().min(2),
  lastName: z.string().min(2),
  email: z.string().email(),
  address: z.string().min(5),
  city: z.string().min(2),
  zipCode: z.string().regex(/^\d{5}$/)
});

type FormData = z.infer<typeof schema>;

function MultiStepFormWithReview() {
  const [step, setStep] = useState(1);
  const TOTAL_STEPS = 4;  // 3 steps + 1 review

  const {
    register,
    handleSubmit,
    trigger,
    getValues,
    formState: { errors }
  } = useForm<FormData>({
    resolver: zodResolver(schema)
  });

  const nextStep = async () => {
    let isValid = false;

    switch (step) {
      case 1:
        isValid = await trigger(['firstName', 'lastName', 'email']);
        break;
      case 2:
        isValid = await trigger(['address', 'city', 'zipCode']);
        break;
      case 3:
        isValid = true;  // Review step, no validation needed
        break;
    }

    if (isValid) {
      setStep(prev => prev + 1);
    }
  };

  const onSubmit = (data: FormData) => {
    console.log('Submit:', data);
  };

  const values = getValues();

  return (
    <div>
      {/* Progress */}
      <div className="steps">
        {Array.from({ length: TOTAL_STEPS }).map((_, i) => (
          <div
            key={i}
            className={`step ${i + 1 === step ? 'active' : ''} ${i + 1 < step ? 'completed' : ''}`}
          >
            {i + 1}
          </div>
        ))}
      </div>

      <form onSubmit={handleSubmit(onSubmit)}>
        {/* Step 1 */}
        {step === 1 && (
          <div>
            <h2>Personal Information</h2>
            <input {...register('firstName')} placeholder="First Name" />
            {errors.firstName && <span>{errors.firstName.message}</span>}

            <input {...register('lastName')} placeholder="Last Name" />
            {errors.lastName && <span>{errors.lastName.message}</span>}

            <input {...register('email')} placeholder="Email" />
            {errors.email && <span>{errors.email.message}</span>}

            <button type="button" onClick={nextStep}>Next</button>
          </div>
        )}

        {/* Step 2 */}
        {step === 2 && (
          <div>
            <h2>Address</h2>
            <input {...register('address')} placeholder="Address" />
            {errors.address && <span>{errors.address.message}</span>}

            <input {...register('city')} placeholder="City" />
            {errors.city && <span>{errors.city.message}</span>}

            <input {...register('zipCode')} placeholder="ZIP Code" />
            {errors.zipCode && <span>{errors.zipCode.message}</span>}

            <button type="button" onClick={() => setStep(1)}>Previous</button>
            <button type="button" onClick={nextStep}>Next</button>
          </div>
        )}

        {/* Step 3: Review */}
        {step === 3 && (
          <div>
            <h2>Review Your Information</h2>
            
            <div className="review-section">
              <h3>Personal Information</h3>
              <p><strong>Name:</strong> {values.firstName} {values.lastName}</p>
              <p><strong>Email:</strong> {values.email}</p>
              <button type="button" onClick={() => setStep(1)}>Edit</button>
            </div>

            <div className="review-section">
              <h3>Address</h3>
              <p><strong>Address:</strong> {values.address}</p>
              <p><strong>City:</strong> {values.city}</p>
              <p><strong>ZIP:</strong> {values.zipCode}</p>
              <button type="button" onClick={() => setStep(2)}>Edit</button>
            </div>

            <button type="button" onClick={() => setStep(2)}>Previous</button>
            <button type="submit">Submit</button>
          </div>
        )}
      </form>
    </div>
  );
}
```

---

## 7. 📁 **File Uploads**

### 7.1. Single File Upload

```tsx
// ═══════════════════════════════════════════════════════════
// SINGLE FILE UPLOAD
// ═══════════════════════════════════════════════════════════

import { useForm } from 'react-hook-form';
import { z } from 'zod';
import { zodResolver } from '@hookform/resolvers/zod';
import { useState } from 'react';

const MAX_FILE_SIZE = 5 * 1024 * 1024;  // 5MB
const ACCEPTED_IMAGE_TYPES = ['image/jpeg', 'image/jpg', 'image/png', 'image/webp'];

const schema = z.object({
  name: z.string().min(2, 'Name is required'),
  avatar: z
    .instanceof(FileList)
    .refine(files => files.length > 0, 'Avatar is required')
    .refine(files => files[0]?.size <= MAX_FILE_SIZE, 'Max file size is 5MB')
    .refine(
      files => ACCEPTED_IMAGE_TYPES.includes(files[0]?.type),
      'Only .jpg, .jpeg, .png and .webp formats are supported'
    )
});

type FormData = z.infer<typeof schema>;

function SingleFileUpload() {
  const [preview, setPreview] = useState<string | null>(null);

  const {
    register,
    handleSubmit,
    formState: { errors },
    watch
  } = useForm<FormData>({
    resolver: zodResolver(schema)
  });

  // ✅ Watch file changes for preview
  const avatarFile = watch('avatar');

  // ✅ Update preview when file changes
  React.useEffect(() => {
    if (avatarFile && avatarFile.length > 0) {
      const file = avatarFile[0];
      const objectUrl = URL.createObjectURL(file);
      setPreview(objectUrl);

      // ✅ Cleanup
      return () => URL.revokeObjectURL(objectUrl);
    }
  }, [avatarFile]);

  const onSubmit = async (data: FormData) => {
    const formData = new FormData();
    formData.append('name', data.name);
    formData.append('avatar', data.avatar[0]);

    // ✅ Upload
    const response = await fetch('/api/upload', {
      method: 'POST',
      body: formData
    });

    const result = await response.json();
    console.log('Upload result:', result);
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
      <div>
        <label htmlFor="name">Name</label>
        <input
          id="name"
          {...register('name')}
          className="border rounded px-3 py-2 w-full"
        />
        {errors.name && <p className="text-red-500">{errors.name.message}</p>}
      </div>

      <div>
        <label htmlFor="avatar">Avatar</label>
        <input
          id="avatar"
          type="file"
          accept="image/*"
          {...register('avatar')}
          className="border rounded px-3 py-2 w-full"
        />
        {errors.avatar && <p className="text-red-500">{errors.avatar.message as string}</p>}
      </div>

      {/* Preview */}
      {preview && (
        <div>
          <p className="text-sm text-gray-600 mb-2">Preview:</p>
          <img
            src={preview}
            alt="Preview"
            className="w-32 h-32 object-cover rounded"
          />
        </div>
      )}

      <button
        type="submit"
        className="bg-blue-600 text-white px-6 py-2 rounded hover:bg-blue-700"
      >
        Upload
      </button>
    </form>
  );
}
```

### 7.2. Multiple File Upload

```tsx
// ═══════════════════════════════════════════════════════════
// MULTIPLE FILE UPLOAD
// ═══════════════════════════════════════════════════════════

import { useForm } from 'react-hook-form';
import { z } from 'zod';
import { zodResolver } from '@hookform/resolvers/zod';
import { useState } from 'react';

const schema = z.object({
  title: z.string().min(2),
  images: z
    .instanceof(FileList)
    .refine(files => files.length > 0, 'At least one image is required')
    .refine(files => files.length <= 5, 'Maximum 5 images allowed')
    .refine(
      files => Array.from(files).every(file => file.size <= 5 * 1024 * 1024),
      'Each file must be less than 5MB'
    )
});

type FormData = z.infer<typeof schema>;

function MultipleFileUpload() {
  const [previews, setPreviews] = useState<string[]>([]);

  const {
    register,
    handleSubmit,
    formState: { errors },
    watch
  } = useForm<FormData>({
    resolver: zodResolver(schema)
  });

  const imagesFiles = watch('images');

  React.useEffect(() => {
    if (imagesFiles && imagesFiles.length > 0) {
      const files = Array.from(imagesFiles);
      const urls = files.map(file => URL.createObjectURL(file));
      setPreviews(urls);

      // Cleanup
      return () => {
        urls.forEach(url => URL.revokeObjectURL(url));
      };
    }
  }, [imagesFiles]);

  const onSubmit = async (data: FormData) => {
    const formData = new FormData();
    formData.append('title', data.title);

    Array.from(data.images).forEach((file, index) => {
      formData.append(`images[${index}]`, file);
    });

    const response = await fetch('/api/upload-multiple', {
      method: 'POST',
      body: formData
    });

    console.log('Uploaded:', await response.json());
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
      <div>
        <label>Title</label>
        <input {...register('title')} className="border rounded px-3 py-2 w-full" />
        {errors.title && <p className="text-red-500">{errors.title.message}</p>}
      </div>

      <div>
        <label>Images (max 5)</label>
        <input
          type="file"
          accept="image/*"
          multiple
          {...register('images')}
          className="border rounded px-3 py-2 w-full"
        />
        {errors.images && <p className="text-red-500">{errors.images.message as string}</p>}
      </div>

      {/* Previews */}
      {previews.length > 0 && (
        <div>
          <p className="text-sm text-gray-600 mb-2">Previews:</p>
          <div className="grid grid-cols-5 gap-2">
            {previews.map((preview, index) => (
              <img
                key={index}
                src={preview}
                alt={`Preview ${index + 1}`}
                className="w-full h-24 object-cover rounded"
              />
            ))}
          </div>
        </div>
      )}

      <button type="submit" className="bg-blue-600 text-white px-6 py-2 rounded">
        Upload All
      </button>
    </form>
  );
}
```

### 7.3. Drag & Drop Upload

```tsx
// ═══════════════════════════════════════════════════════════
// DRAG & DROP UPLOAD
// ═══════════════════════════════════════════════════════════

import { useForm } from 'react-hook-form';
import { useState, useCallback } from 'react';

interface FormData {
  files: FileList;
}

function DragDropUpload() {
  const [isDragging, setIsDragging] = useState(false);
  const [previews, setPreviews] = useState<string[]>([]);

  const { register, handleSubmit, setValue, watch } = useForm<FormData>();

  const files = watch('files');

  // ✅ Handle drag events
  const handleDragEnter = useCallback((e: React.DragEvent) => {
    e.preventDefault();
    e.stopPropagation();
    setIsDragging(true);
  }, []);

  const handleDragLeave = useCallback((e: React.DragEvent) => {
    e.preventDefault();
    e.stopPropagation();
    setIsDragging(false);
  }, []);

  const handleDragOver = useCallback((e: React.DragEvent) => {
    e.preventDefault();
    e.stopPropagation();
  }, []);

  const handleDrop = useCallback((e: React.DragEvent) => {
    e.preventDefault();
    e.stopPropagation();
    setIsDragging(false);

    const droppedFiles = e.dataTransfer.files;
    if (droppedFiles.length > 0) {
      setValue('files', droppedFiles);
      
      // Create previews
      const urls = Array.from(droppedFiles).map(file => URL.createObjectURL(file));
      setPreviews(urls);
    }
  }, [setValue]);

  React.useEffect(() => {
    if (files && files.length > 0) {
      const urls = Array.from(files).map(file => URL.createObjectURL(file));
      setPreviews(urls);

      return () => {
        urls.forEach(url => URL.revokeObjectURL(url));
      };
    }
  }, [files]);

  const onSubmit = (data: FormData) => {
    console.log('Files:', data.files);
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
      {/* Drop Zone */}
      <div
        onDragEnter={handleDragEnter}
        onDragLeave={handleDragLeave}
        onDragOver={handleDragOver}
        onDrop={handleDrop}
        className={`
          border-2 border-dashed rounded-lg p-8 text-center
          transition-colors cursor-pointer
          ${isDragging ? 'border-blue-500 bg-blue-50' : 'border-gray-300 hover:border-gray-400'}
        `}
      >
        <input
          type="file"
          multiple
          {...register('files')}
          className="hidden"
          id="file-input"
        />
        
        <label htmlFor="file-input" className="cursor-pointer">
          <div className="space-y-2">
            <svg
              className="mx-auto h-12 w-12 text-gray-400"
              stroke="currentColor"
              fill="none"
              viewBox="0 0 48 48"
            >
              <path
                d="M28 8H12a4 4 0 00-4 4v20m32-12v8m0 0v8a4 4 0 01-4 4H12a4 4 0 01-4-4v-4m32-4l-3.172-3.172a4 4 0 00-5.656 0L28 28M8 32l9.172-9.172a4 4 0 015.656 0L28 28m0 0l4 4m4-24h8m-4-4v8m-12 4h.02"
                strokeWidth={2}
                strokeLinecap="round"
                strokeLinejoin="round"
              />
            </svg>
            <div className="text-sm text-gray-600">
              <span className="font-semibold text-blue-600">Click to upload</span>
              {' '}or drag and drop
            </div>
            <p className="text-xs text-gray-500">PNG, JPG, GIF up to 10MB</p>
          </div>
        </label>
      </div>

      {/* Previews */}
      {previews.length > 0 && (
        <div>
          <p className="text-sm font-medium mb-2">Uploaded Files:</p>
          <div className="grid grid-cols-4 gap-4">
            {previews.map((preview, index) => (
              <div key={index} className="relative group">
                <img
                  src={preview}
                  alt={`Upload ${index + 1}`}
                  className="w-full h-32 object-cover rounded"
                />
                <button
                  type="button"
                  onClick={() => {
                    const newPreviews = previews.filter((_, i) => i !== index);
                    setPreviews(newPreviews);
                  }}
                  className="absolute top-2 right-2 bg-red-500 text-white rounded-full w-6 h-6 opacity-0 group-hover:opacity-100 transition-opacity"
                >
                  ×
                </button>
              </div>
            ))}
          </div>
        </div>
      )}

      <button
        type="submit"
        className="bg-blue-600 text-white px-6 py-2 rounded hover:bg-blue-700"
      >
        Upload Files
      </button>
    </form>
  );
}
```

### 7.4. File Upload with Progress

```tsx
// ═══════════════════════════════════════════════════════════
// FILE UPLOAD WITH PROGRESS
// ═══════════════════════════════════════════════════════════

import { useForm } from 'react-hook-form';
import { useState } from 'react';
import axios from 'axios';

interface FormData {
  file: FileList;
}

function FileUploadWithProgress() {
  const [uploadProgress, setUploadProgress] = useState(0);
  const [isUploading, setIsUploading] = useState(false);

  const { register, handleSubmit } = useForm<FormData>();

  const onSubmit = async (data: FormData) => {
    if (!data.file || data.file.length === 0) return;

    const formData = new FormData();
    formData.append('file', data.file[0]);

    setIsUploading(true);
    setUploadProgress(0);

    try {
      // ✅ Upload with progress tracking
      const response = await axios.post('/api/upload', formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        },
        onUploadProgress: (progressEvent) => {
          const percentCompleted = Math.round(
            (progressEvent.loaded * 100) / (progressEvent.total || 100)
          );
          setUploadProgress(percentCompleted);
        }
      });

      console.log('Upload complete:', response.data);
    } catch (error) {
      console.error('Upload failed:', error);
    } finally {
      setIsUploading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
      <div>
        <label>Select File</label>
        <input
          type="file"
          {...register('file')}
          className="border rounded px-3 py-2 w-full"
        />
      </div>

      {isUploading && (
        <div>
          <div className="flex justify-between text-sm mb-1">
            <span>Uploading...</span>
            <span>{uploadProgress}%</span>
          </div>
          <div className="w-full bg-gray-200 rounded-full h-2">
            <div
              className="bg-blue-600 h-2 rounded-full transition-all duration-300"
              style={{ width: `${uploadProgress}%` }}
            />
          </div>
        </div>
      )}

      <button
        type="submit"
        disabled={isUploading}
        className="bg-blue-600 text-white px-6 py-2 rounded hover:bg-blue-700 disabled:opacity-50"
      >
        {isUploading ? 'Uploading...' : 'Upload'}
      </button>
    </form>
  );
}
```

### 7.5. Base64 File Upload

```tsx
// ═══════════════════════════════════════════════════════════
// BASE64 FILE UPLOAD (No FormData)
// ═══════════════════════════════════════════════════════════

import { useForm } from 'react-hook-form';
import { useState } from 'react';

interface FormData {
  image: FileList;
  description: string;
}

interface SubmitData {
  imageBase64: string;
  description: string;
}

function Base64Upload() {
  const [preview, setPreview] = useState<string | null>(null);

  const { register, handleSubmit, watch } = useForm<FormData>();

  const imageFile = watch('image');

  React.useEffect(() => {
    if (imageFile && imageFile.length > 0) {
      const reader = new FileReader();
      reader.onloadend = () => {
        setPreview(reader.result as string);
      };
      reader.readAsDataURL(imageFile[0]);
    }
  }, [imageFile]);

  const onSubmit = async (data: FormData) => {
    if (!data.image || data.image.length === 0) return;

    // ✅ Convert to Base64
    const reader = new FileReader();
    reader.onloadend = async () => {
      const base64String = reader.result as string;

      const submitData: SubmitData = {
        imageBase64: base64String,
        description: data.description
      };

      // ✅ Send JSON (não FormData)
      const response = await fetch('/api/upload-base64', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(submitData)
      });

      console.log('Uploaded:', await response.json());
    };

    reader.readAsDataURL(data.image[0]);
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
      <div>
        <label>Image</label>
        <input
          type="file"
          accept="image/*"
          {...register('image')}
          className="border rounded px-3 py-2 w-full"
        />
      </div>

      {preview && (
        <div>
          <p className="text-sm text-gray-600 mb-2">Preview:</p>
          <img src={preview} alt="Preview" className="w-64 h-64 object-cover rounded" />
        </div>
      )}

      <div>
        <label>Description</label>
        <textarea
          {...register('description')}
          className="border rounded px-3 py-2 w-full"
          rows={3}
        />
      </div>

      <button type="submit" className="bg-blue-600 text-white px-6 py-2 rounded">
        Upload
      </button>
    </form>
  );
}
```

---

## 8. 🔄 **Dynamic Forms**

### 8.1. Field Arrays

```tsx
// ═══════════════════════════════════════════════════════════
// FIELD ARRAYS (useFieldArray)
// ═══════════════════════════════════════════════════════════

import { useForm, useFieldArray } from 'react-hook-form';
import { z } from 'zod';
import { zodResolver } from '@hookform/resolvers/zod';

const schema = z.object({
  name: z.string().min(2),
  emails: z.array(
    z.object({
      email: z.string().email('Invalid email')
    })
  ).min(1, 'At least one email is required')
});

type FormData = z.infer<typeof schema>;

function FieldArrayExample() {
  const {
    register,
    control,
    handleSubmit,
    formState: { errors }
  } = useForm<FormData>({
    resolver: zodResolver(schema),
    defaultValues: {
      name: '',
      emails: [{ email: '' }]
    }
  });

  const { fields, append, remove } = useFieldArray({
    control,
    name: 'emails'
  });

  const onSubmit = (data: FormData) => {
    console.log('Submit:', data);
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
      <div>
        <label>Name</label>
        <input
          {...register('name')}
          className="border rounded px-3 py-2 w-full"
        />
        {errors.name && <p className="text-red-500">{errors.name.message}</p>}
      </div>

      <div className="space-y-2">
        <label className="block font-medium">Emails</label>
        
        {fields.map((field, index) => (
          <div key={field.id} className="flex gap-2">
            <input
              {...register(`emails.${index}.email`)}
              className="border rounded px-3 py-2 flex-1"
              placeholder="email@example.com"
            />
            
            <button
              type="button"
              onClick={() => remove(index)}
              disabled={fields.length === 1}
              className="bg-red-500 text-white px-4 py-2 rounded disabled:opacity-50"
            >
              Remove
            </button>
          </div>
        ))}

        {errors.emails && (
          <p className="text-red-500">{errors.emails.message}</p>
        )}

        {errors.emails && errors.emails.map((error, index) => (
          error?.email && (
            <p key={index} className="text-red-500 text-sm">
              Email {index + 1}: {error.email.message}
            </p>
          )
        ))}
      </div>

      <button
        type="button"
        onClick={() => append({ email: '' })}
        className="bg-green-500 text-white px-4 py-2 rounded"
      >
        Add Email
      </button>

      <button type="submit" className="bg-blue-600 text-white px-6 py-2 rounded">
        Submit
      </button>
    </form>
  );
}
```

### 8.2. Nested Field Arrays

```tsx
// ═══════════════════════════════════════════════════════════
// NESTED FIELD ARRAYS (e.g., Todo list with subtasks)
// ═══════════════════════════════════════════════════════════

import { useForm, useFieldArray } from 'react-hook-form';

interface FormData {
  todos: {
    title: string;
    subtasks: {
      name: string;
      completed: boolean;
    }[];
  }[];
}

function NestedFieldArrays() {
  const { register, control, handleSubmit } = useForm<FormData>({
    defaultValues: {
      todos: [
        {
          title: '',
          subtasks: [{ name: '', completed: false }]
        }
      ]
    }
  });

  const { fields: todoFields, append: appendTodo, remove: removeTodo } = useFieldArray({
    control,
    name: 'todos'
  });

  const onSubmit = (data: FormData) => {
    console.log('Submit:', data);
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
      {todoFields.map((todoField, todoIndex) => (
        <div key={todoField.id} className="border p-4 rounded">
          <div className="flex gap-2 mb-4">
            <input
              {...register(`todos.${todoIndex}.title`)}
              placeholder="Todo title"
              className="border rounded px-3 py-2 flex-1"
            />
            <button
              type="button"
              onClick={() => removeTodo(todoIndex)}
              className="bg-red-500 text-white px-4 py-2 rounded"
            >
              Remove Todo
            </button>
          </div>

          <NestedSubtasks nestIndex={todoIndex} {...{ control, register }} />
        </div>
      ))}

      <button
        type="button"
        onClick={() => appendTodo({ title: '', subtasks: [{ name: '', completed: false }] })}
        className="bg-green-500 text-white px-4 py-2 rounded"
      >
        Add Todo
      </button>

      <button type="submit" className="bg-blue-600 text-white px-6 py-2 rounded ml-2">
        Submit
      </button>
    </form>
  );
}

// ✅ Nested component for subtasks
function NestedSubtasks({ nestIndex, control, register }: any) {
  const { fields, append, remove } = useFieldArray({
    control,
    name: `todos.${nestIndex}.subtasks`
  });

  return (
    <div className="ml-4 space-y-2">
      <p className="text-sm font-medium text-gray-600">Subtasks:</p>
      
      {fields.map((field, index) => (
        <div key={field.id} className="flex gap-2 items-center">
          <input
            type="checkbox"
            {...register(`todos.${nestIndex}.subtasks.${index}.completed`)}
          />
          
          <input
            {...register(`todos.${nestIndex}.subtasks.${index}.name`)}
            placeholder="Subtask name"
            className="border rounded px-3 py-2 flex-1 text-sm"
          />
          
          <button
            type="button"
            onClick={() => remove(index)}
            className="bg-red-400 text-white px-2 py-1 rounded text-sm"
          >
            ×
          </button>
        </div>
      ))}

      <button
        type="button"
        onClick={() => append({ name: '', completed: false })}
        className="bg-green-400 text-white px-3 py-1 rounded text-sm"
      >
        Add Subtask
      </button>
    </div>
  );
}
```

### 8.3. Dynamic Form Builder

```tsx
// ═══════════════════════════════════════════════════════════
// DYNAMIC FORM BUILDER (Create forms from config)
// ═══════════════════════════════════════════════════════════

import { useForm } from 'react-hook-form';

interface FieldConfig {
  name: string;
  label: string;
  type: 'text' | 'email' | 'number' | 'select' | 'textarea';
  options?: string[];
  validation?: any;
}

const formConfig: FieldConfig[] = [
  {
    name: 'firstName',
    label: 'First Name',
    type: 'text',
    validation: { required: 'First name is required' }
  },
  {
    name: 'email',
    label: 'Email',
    type: 'email',
    validation: {
      required: 'Email is required',
      pattern: {
        value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
        message: 'Invalid email'
      }
    }
  },
  {
    name: 'age',
    label: 'Age',
    type: 'number',
    validation: {
      required: 'Age is required',
      min: { value: 18, message: 'Must be 18+' }
    }
  },
  {
    name: 'country',
    label: 'Country',
    type: 'select',
    options: ['USA', 'Canada', 'UK', 'Brazil'],
    validation: { required: 'Country is required' }
  },
  {
    name: 'bio',
    label: 'Bio',
    type: 'textarea',
    validation: {
      minLength: { value: 10, message: 'Bio must be at least 10 characters' }
    }
  }
];

function DynamicFormBuilder() {
  const {
    register,
    handleSubmit,
    formState: { errors }
  } = useForm();

  const onSubmit = (data: any) => {
    console.log('Submit:', data);
  };

  const renderField = (field: FieldConfig) => {
    const error = errors[field.name];

    switch (field.type) {
      case 'select':
        return (
          <div key={field.name} className="space-y-1">
            <label className="block font-medium">{field.label}</label>
            <select
              {...register(field.name, field.validation)}
              className="border rounded px-3 py-2 w-full"
            >
              <option value="">Select...</option>
              {field.options?.map(option => (
                <option key={option} value={option}>{option}</option>
              ))}
            </select>
            {error && <p className="text-red-500 text-sm">{error.message as string}</p>}
          </div>
        );

      case 'textarea':
        return (
          <div key={field.name} className="space-y-1">
            <label className="block font-medium">{field.label}</label>
            <textarea
              {...register(field.name, field.validation)}
              className="border rounded px-3 py-2 w-full"
              rows={4}
            />
            {error && <p className="text-red-500 text-sm">{error.message as string}</p>}
          </div>
        );

      default:
        return (
          <div key={field.name} className="space-y-1">
            <label className="block font-medium">{field.label}</label>
            <input
              type={field.type}
              {...register(field.name, field.validation)}
              className="border rounded px-3 py-2 w-full"
            />
            {error && <p className="text-red-500 text-sm">{error.message as string}</p>}
          </div>
        );
    }
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
      {formConfig.map(renderField)}

      <button type="submit" className="bg-blue-600 text-white px-6 py-2 rounded">
        Submit
      </button>
    </form>
  );
}
```

### 8.4. Conditional Fields

```tsx
// ═══════════════════════════════════════════════════════════
// CONDITIONAL FIELDS (Show/hide based on values)
// ═══════════════════════════════════════════════════════════

import { useForm } from 'react-hook-form';

interface FormData {
  accountType: 'personal' | 'business';
  firstName: string;
  lastName: string;
  companyName?: string;
  taxId?: string;
  hasReferral: boolean;
  referralCode?: string;
}

function ConditionalFields() {
  const {
    register,
    handleSubmit,
    watch,
    formState: { errors }
  } = useForm<FormData>({
    defaultValues: {
      accountType: 'personal',
      hasReferral: false
    }
  });

  const accountType = watch('accountType');
  const hasReferral = watch('hasReferral');

  const onSubmit = (data: FormData) => {
    console.log('Submit:', data);
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
      {/* Account Type */}
      <div>
        <label className="block font-medium mb-2">Account Type</label>
        <div className="space-x-4">
          <label className="inline-flex items-center">
            <input
              type="radio"
              value="personal"
              {...register('accountType')}
              className="mr-2"
            />
            Personal
          </label>
          <label className="inline-flex items-center">
            <input
              type="radio"
              value="business"
              {...register('accountType')}
              className="mr-2"
            />
            Business
          </label>
        </div>
      </div>

      {/* Personal Fields */}
      {accountType === 'personal' && (
        <>
          <input
            {...register('firstName', { required: 'First name is required' })}
            placeholder="First Name"
            className="border rounded px-3 py-2 w-full"
          />
          {errors.firstName && <p className="text-red-500">{errors.firstName.message}</p>}

          <input
            {...register('lastName', { required: 'Last name is required' })}
            placeholder="Last Name"
            className="border rounded px-3 py-2 w-full"
          />
          {errors.lastName && <p className="text-red-500">{errors.lastName.message}</p>}
        </>
      )}

      {/* Business Fields */}
      {accountType === 'business' && (
        <>
          <input
            {...register('companyName', { required: 'Company name is required' })}
            placeholder="Company Name"
            className="border rounded px-3 py-2 w-full"
          />
          {errors.companyName && <p className="text-red-500">{errors.companyName.message}</p>}

          <input
            {...register('taxId', { required: 'Tax ID is required' })}
            placeholder="Tax ID"
            className="border rounded px-3 py-2 w-full"
          />
          {errors.taxId && <p className="text-red-500">{errors.taxId.message}</p>}
        </>
      )}

      {/* Referral Checkbox */}
      <label className="flex items-center">
        <input
          type="checkbox"
          {...register('hasReferral')}
          className="mr-2"
        />
        I have a referral code
      </label>

      {/* Conditional Referral Field */}
      {hasReferral && (
        <input
          {...register('referralCode', { required: 'Referral code is required' })}
          placeholder="Enter referral code"
          className="border rounded px-3 py-2 w-full"
        />
      )}
      {hasReferral && errors.referralCode && (
        <p className="text-red-500">{errors.referralCode.message}</p>
      )}

      <button type="submit" className="bg-blue-600 text-white px-6 py-2 rounded">
        Submit
      </button>
    </form>
  );
}
```

---

## 9. 🎨 **Formulários com UI Libraries**

### 9.1. shadcn/ui Integration

```tsx
// ═══════════════════════════════════════════════════════════
// SHADCN/UI + REACT HOOK FORM
// ═══════════════════════════════════════════════════════════

// npx shadcn-ui@latest init
// npx shadcn-ui@latest add form input label button

import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import {
  Form,
  FormControl,
  FormDescription,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from '@/components/ui/form';
import { Input } from '@/components/ui/input';
import { Button } from '@/components/ui/button';

const formSchema = z.object({
  username: z.string().min(2, 'Username must be at least 2 characters'),
  email: z.string().email('Invalid email address'),
  password: z.string().min(8, 'Password must be at least 8 characters')
});

type FormData = z.infer<typeof formSchema>;

function ShadcnForm() {
  const form = useForm<FormData>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      username: '',
      email: '',
      password: ''
    }
  });

  const onSubmit = (data: FormData) => {
    console.log('Submit:', data);
  };

  return (
    <Form {...form}>
      <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8">
        <FormField
          control={form.control}
          name="username"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Username</FormLabel>
              <FormControl>
                <Input placeholder="johndoe" {...field} />
              </FormControl>
              <FormDescription>
                This is your public display name.
              </FormDescription>
              <FormMessage />
            </FormItem>
          )}
        />

        <FormField
          control={form.control}
          name="email"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Email</FormLabel>
              <FormControl>
                <Input type="email" placeholder="john@example.com" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />

        <FormField
          control={form.control}
          name="password"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Password</FormLabel>
              <FormControl>
                <Input type="password" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />

        <Button type="submit">Submit</Button>
      </form>
    </Form>
  );
}
```

### 9.2. Radix UI Integration

```tsx
// ═══════════════════════════════════════════════════════════
// RADIX UI + REACT HOOK FORM
// ═══════════════════════════════════════════════════════════

// npm install @radix-ui/react-label @radix-ui/react-select

import { useForm, Controller } from 'react-hook-form';
import * as Label from '@radix-ui/react-label';
import * as Select from '@radix-ui/react-select';
import { CheckIcon, ChevronDownIcon } from '@radix-ui/react-icons';

interface FormData {
  name: string;
  country: string;
}

function RadixForm() {
  const {
    register,
    handleSubmit,
    control,
    formState: { errors }
  } = useForm<FormData>();

  const onSubmit = (data: FormData) => {
    console.log('Submit:', data);
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
      {/* Input with Radix Label */}
      <div className="space-y-2">
        <Label.Root htmlFor="name" className="text-sm font-medium">
          Name
        </Label.Root>
        <input
          id="name"
          {...register('name', { required: 'Name is required' })}
          className="border rounded px-3 py-2 w-full"
        />
        {errors.name && (
          <p className="text-red-500 text-sm">{errors.name.message}</p>
        )}
      </div>

      {/* Radix Select */}
      <div className="space-y-2">
        <Label.Root htmlFor="country" className="text-sm font-medium">
          Country
        </Label.Root>
        
        <Controller
          name="country"
          control={control}
          rules={{ required: 'Country is required' }}
          render={({ field }) => (
            <Select.Root value={field.value} onValueChange={field.onChange}>
              <Select.Trigger className="inline-flex items-center justify-between border rounded px-3 py-2 w-full">
                <Select.Value placeholder="Select a country" />
                <Select.Icon>
                  <ChevronDownIcon />
                </Select.Icon>
              </Select.Trigger>

              <Select.Portal>
                <Select.Content className="bg-white border rounded shadow-lg">
                  <Select.Viewport className="p-1">
                    <SelectItem value="us">United States</SelectItem>
                    <SelectItem value="ca">Canada</SelectItem>
                    <SelectItem value="uk">United Kingdom</SelectItem>
                    <SelectItem value="br">Brazil</SelectItem>
                  </Select.Viewport>
                </Select.Content>
              </Select.Portal>
            </Select.Root>
          )}
        />
        
        {errors.country && (
          <p className="text-red-500 text-sm">{errors.country.message}</p>
        )}
      </div>

      <button
        type="submit"
        className="bg-blue-600 text-white px-6 py-2 rounded hover:bg-blue-700"
      >
        Submit
      </button>
    </form>
  );
}

// ✅ Select Item component
function SelectItem({ children, ...props }: any) {
  return (
    <Select.Item
      className="relative flex items-center px-8 py-2 rounded hover:bg-gray-100 cursor-pointer"
      {...props}
    >
      <Select.ItemText>{children}</Select.ItemText>
      <Select.ItemIndicator className="absolute left-2">
        <CheckIcon />
      </Select.ItemIndicator>
    </Select.Item>
  );
}
```

### 9.3. Material-UI Integration

```tsx
// ═══════════════════════════════════════════════════════════
// MATERIAL-UI + REACT HOOK FORM
// ═══════════════════════════════════════════════════════════

// npm install @mui/material @emotion/react @emotion/styled

import { useForm, Controller } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import {
  TextField,
  Button,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  FormHelperText,
  Box
} from '@mui/material';

const schema = z.object({
  firstName: z.string().min(2, 'First name must be at least 2 characters'),
  email: z.string().email('Invalid email'),
  country: z.string().min(1, 'Country is required')
});

type FormData = z.infer<typeof schema>;

function MuiForm() {
  const {
    control,
    handleSubmit,
    formState: { errors }
  } = useForm<FormData>({
    resolver: zodResolver(schema),
    defaultValues: {
      firstName: '',
      email: '',
      country: ''
    }
  });

  const onSubmit = (data: FormData) => {
    console.log('Submit:', data);
  };

  return (
    <Box
      component="form"
      onSubmit={handleSubmit(onSubmit)}
      sx={{ display: 'flex', flexDirection: 'column', gap: 2, maxWidth: 400 }}
    >
      {/* TextField */}
      <Controller
        name="firstName"
        control={control}
        render={({ field }) => (
          <TextField
            {...field}
            label="First Name"
            variant="outlined"
            error={!!errors.firstName}
            helperText={errors.firstName?.message}
          />
        )}
      />

      {/* Email */}
      <Controller
        name="email"
        control={control}
        render={({ field }) => (
          <TextField
            {...field}
            label="Email"
            type="email"
            variant="outlined"
            error={!!errors.email}
            helperText={errors.email?.message}
          />
        )}
      />

      {/* Select */}
      <Controller
        name="country"
        control={control}
        render={({ field }) => (
          <FormControl error={!!errors.country}>
            <InputLabel>Country</InputLabel>
            <Select {...field} label="Country">
              <MenuItem value="us">United States</MenuItem>
              <MenuItem value="ca">Canada</MenuItem>
              <MenuItem value="uk">United Kingdom</MenuItem>
              <MenuItem value="br">Brazil</MenuItem>
            </Select>
            {errors.country && (
              <FormHelperText>{errors.country.message}</FormHelperText>
            )}
          </FormControl>
        )}
      />

      <Button type="submit" variant="contained">
        Submit
      </Button>
    </Box>
  );
}
```

---

## 10. 🌐 **Server-Side Validation**

### 10.1. Next.js Server Actions

```tsx
// ═══════════════════════════════════════════════════════════
// NEXT.JS SERVER ACTIONS
// ═══════════════════════════════════════════════════════════

// ────────────────────────────────────────────────────────────
// actions/user.ts
// ────────────────────────────────────────────────────────────

'use server';

import { z } from 'zod';

const userSchema = z.object({
  username: z.string().min(3).max(20),
  email: z.string().email(),
  password: z.string().min(8)
});

export async function createUser(formData: FormData) {
  // ✅ 1. Parse form data
  const rawData = {
    username: formData.get('username'),
    email: formData.get('email'),
    password: formData.get('password')
  };

  // ✅ 2. Validate with Zod
  const result = userSchema.safeParse(rawData);

  if (!result.success) {
    return {
      success: false,
      errors: result.error.flatten().fieldErrors
    };
  }

  // ✅ 3. Additional server-side checks
  const existingUser = await db.user.findUnique({
    where: { email: result.data.email }
  });

  if (existingUser) {
    return {
      success: false,
      errors: {
        email: ['Email is already registered']
      }
    };
  }

  // ✅ 4. Create user
  const user = await db.user.create({
    data: result.data
  });

  return {
    success: true,
    data: user
  };
}

// ────────────────────────────────────────────────────────────
// app/signup/page.tsx
// ────────────────────────────────────────────────────────────

'use client';

import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { createUser } from '@/actions/user';
import { useState } from 'react';

const schema = z.object({
  username: z.string().min(3).max(20),
  email: z.string().email(),
  password: z.string().min(8)
});

type FormData = z.infer<typeof schema>;

export default function SignupPage() {
  const [serverErrors, setServerErrors] = useState<Record<string, string[]>>({});

  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting }
  } = useForm<FormData>({
    resolver: zodResolver(schema)
  });

  const onSubmit = async (data: FormData) => {
    const formData = new FormData();
    formData.append('username', data.username);
    formData.append('email', data.email);
    formData.append('password', data.password);

    const result = await createUser(formData);

    if (!result.success) {
      setServerErrors(result.errors || {});
    } else {
      console.log('User created:', result.data);
      setServerErrors({});
    }
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
      <div>
        <input
          {...register('username')}
          placeholder="Username"
          className="border rounded px-3 py-2 w-full"
        />
        {errors.username && <p className="text-red-500">{errors.username.message}</p>}
        {serverErrors.username && (
          <p className="text-red-500">{serverErrors.username[0]}</p>
        )}
      </div>

      <div>
        <input
          type="email"
          {...register('email')}
          placeholder="Email"
          className="border rounded px-3 py-2 w-full"
        />
        {errors.email && <p className="text-red-500">{errors.email.message}</p>}
        {serverErrors.email && (
          <p className="text-red-500">{serverErrors.email[0]}</p>
        )}
      </div>

      <div>
        <input
          type="password"
          {...register('password')}
          placeholder="Password"
          className="border rounded px-3 py-2 w-full"
        />
        {errors.password && <p className="text-red-500">{errors.password.message}</p>}
        {serverErrors.password && (
          <p className="text-red-500">{serverErrors.password[0]}</p>
        )}
      </div>

      <button
        type="submit"
        disabled={isSubmitting}
        className="bg-blue-600 text-white px-6 py-2 rounded disabled:opacity-50"
      >
        {isSubmitting ? 'Creating...' : 'Sign Up'}
      </button>
    </form>
  );
}
```

### 10.2. useFormState (Next.js)

```tsx
// ═══════════════════════════════════════════════════════════
// USEFORMSTATE (Next.js 14+)
// ═══════════════════════════════════════════════════════════

// ────────────────────────────────────────────────────────────
// actions/contact.ts
// ────────────────────────────────────────────────────────────

'use server';

import { z } from 'zod';

const contactSchema = z.object({
  name: z.string().min(2, 'Name must be at least 2 characters'),
  email: z.string().email('Invalid email'),
  message: z.string().min(10, 'Message must be at least 10 characters')
});

export type ContactFormState = {
  success: boolean;
  errors?: {
    name?: string[];
    email?: string[];
    message?: string[];
  };
  message?: string;
};

export async function submitContact(
  prevState: ContactFormState,
  formData: FormData
): Promise<ContactFormState> {
  const rawData = {
    name: formData.get('name'),
    email: formData.get('email'),
    message: formData.get('message')
  };

  const result = contactSchema.safeParse(rawData);

  if (!result.success) {
    return {
      success: false,
      errors: result.error.flatten().fieldErrors
    };
  }

  // ✅ Send email, save to DB, etc
  try {
    await sendEmail(result.data);
    
    return {
      success: true,
      message: 'Message sent successfully!'
    };
  } catch (error) {
    return {
      success: false,
      message: 'Failed to send message. Please try again.'
    };
  }
}

// ────────────────────────────────────────────────────────────
// app/contact/page.tsx
// ────────────────────────────────────────────────────────────

'use client';

import { useFormState } from 'react-dom';
import { useFormStatus } from 'react-dom';
import { submitContact, type ContactFormState } from '@/actions/contact';

const initialState: ContactFormState = {
  success: false
};

export default function ContactPage() {
  const [state, formAction] = useFormState(submitContact, initialState);

  return (
    <form action={formAction} className="space-y-4">
      <div>
        <input
          name="name"
          placeholder="Name"
          className="border rounded px-3 py-2 w-full"
        />
        {state.errors?.name && (
          <p className="text-red-500">{state.errors.name[0]}</p>
        )}
      </div>

      <div>
        <input
          name="email"
          type="email"
          placeholder="Email"
          className="border rounded px-3 py-2 w-full"
        />
        {state.errors?.email && (
          <p className="text-red-500">{state.errors.email[0]}</p>
        )}
      </div>

      <div>
        <textarea
          name="message"
          placeholder="Message"
          rows={5}
          className="border rounded px-3 py-2 w-full"
        />
        {state.errors?.message && (
          <p className="text-red-500">{state.errors.message[0]}</p>
        )}
      </div>

      {state.message && (
        <p className={state.success ? 'text-green-500' : 'text-red-500'}>
          {state.message}
        </p>
      )}

      <SubmitButton />
    </form>
  );
}

function SubmitButton() {
  const { pending } = useFormStatus();

  return (
    <button
      type="submit"
      disabled={pending}
      className="bg-blue-600 text-white px-6 py-2 rounded disabled:opacity-50"
    >
      {pending ? 'Sending...' : 'Send Message'}
    </button>
  );
}
```

### 10.3. API Route Validation

```tsx
// ═══════════════════════════════════════════════════════════
// API ROUTE VALIDATION
// ═══════════════════════════════════════════════════════════

// ────────────────────────────────────────────────────────────
// app/api/users/route.ts
// ────────────────────────────────────────────────────────────

import { NextRequest, NextResponse } from 'next/server';
import { z } from 'zod';

const createUserSchema = z.object({
  email: z.string().email(),
  password: z.string().min(8),
  name: z.string().min(2)
});

export async function POST(request: NextRequest) {
  try {
    const body = await request.json();

    // ✅ Validate
    const result = createUserSchema.safeParse(body);

    if (!result.success) {
      return NextResponse.json(
        {
          success: false,
          errors: result.error.flatten().fieldErrors
        },
        { status: 400 }
      );
    }

    // ✅ Additional checks
    const existingUser = await db.user.findUnique({
      where: { email: result.data.email }
    });

    if (existingUser) {
      return NextResponse.json(
        {
          success: false,
          errors: {
            email: ['Email is already registered']
          }
        },
        { status: 400 }
      );
    }

    // ✅ Create user
    const user = await db.user.create({
      data: result.data
    });

    return NextResponse.json({
      success: true,
      data: user
    });
  } catch (error) {
    return NextResponse.json(
      { success: false, message: 'Internal server error' },
      { status: 500 }
    );
  }
}

// ────────────────────────────────────────────────────────────
// Client-side
// ────────────────────────────────────────────────────────────

'use client';

import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { useState } from 'react';

const schema = z.object({
  email: z.string().email(),
  password: z.string().min(8),
  name: z.string().min(2)
});

type FormData = z.infer<typeof schema>;

export default function SignupForm() {
  const [serverErrors, setServerErrors] = useState<Record<string, string[]>>({});

  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting }
  } = useForm<FormData>({
    resolver: zodResolver(schema)
  });

  const onSubmit = async (data: FormData) => {
    setServerErrors({});

    const response = await fetch('/api/users', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data)
    });

    const result = await response.json();

    if (!result.success) {
      setServerErrors(result.errors || {});
    } else {
      console.log('User created:', result.data);
    }
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
      <div>
        <input {...register('name')} placeholder="Name" />
        {errors.name && <p className="text-red-500">{errors.name.message}</p>}
        {serverErrors.name && <p className="text-red-500">{serverErrors.name[0]}</p>}
      </div>

      <div>
        <input type="email" {...register('email')} placeholder="Email" />
        {errors.email && <p className="text-red-500">{errors.email.message}</p>}
        {serverErrors.email && <p className="text-red-500">{serverErrors.email[0]}</p>}
      </div>

      <div>
        <input type="password" {...register('password')} placeholder="Password" />
        {errors.password && <p className="text-red-500">{errors.password.message}</p>}
        {serverErrors.password && <p className="text-red-500">{serverErrors.password[0]}</p>}
      </div>

      <button type="submit" disabled={isSubmitting}>
        {isSubmitting ? 'Creating...' : 'Sign Up'}
      </button>
    </form>
  );
}
```

### 10.4. tRPC Integration

```tsx
// ═══════════════════════════════════════════════════════════
// TRPC + REACT HOOK FORM
// ═══════════════════════════════════════════════════════════

// ────────────────────────────────────────────────────────────
// server/routers/user.ts
// ────────────────────────────────────────────────────────────

import { z } from 'zod';
import { router, publicProcedure } from '../trpc';

export const userRouter = router({
  create: publicProcedure
    .input(
      z.object({
        email: z.string().email(),
        name: z.string().min(2),
        password: z.string().min(8)
      })
    )
    .mutation(async ({ input, ctx }) => {
      // ✅ Check if email exists
      const existing = await ctx.db.user.findUnique({
        where: { email: input.email }
      });

      if (existing) {
        throw new Error('Email already registered');
      }

      // ✅ Create user
      const user = await ctx.db.user.create({
        data: input
      });

      return user;
    })
});

// ────────────────────────────────────────────────────────────
// Client component
// ────────────────────────────────────────────────────────────

'use client';

import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { trpc } from '@/utils/trpc';

const schema = z.object({
  email: z.string().email(),
  name: z.string().min(2),
  password: z.string().min(8)
});

type FormData = z.infer<typeof schema>;

export default function SignupForm() {
  const createUser = trpc.user.create.useMutation();

  const {
    register,
    handleSubmit,
    formState: { errors }
  } = useForm<FormData>({
    resolver: zodResolver(schema)
  });

  const onSubmit = async (data: FormData) => {
    try {
      const user = await createUser.mutateAsync(data);
      console.log('User created:', user);
    } catch (error) {
      console.error('Error:', error);
    }
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
      <div>
        <input {...register('name')} placeholder="Name" />
        {errors.name && <p className="text-red-500">{errors.name.message}</p>}
      </div>

      <div>
        <input type="email" {...register('email')} placeholder="Email" />
        {errors.email && <p className="text-red-500">{errors.email.message}</p>}
      </div>

      <div>
        <input type="password" {...register('password')} placeholder="Password" />
        {errors.password && <p className="text-red-500">{errors.password.message}</p>}
      </div>

      {createUser.error && (
        <p className="text-red-500">{createUser.error.message}</p>
      )}

      <button type="submit" disabled={createUser.isLoading}>
        {createUser.isLoading ? 'Creating...' : 'Sign Up'}
      </button>
    </form>
  );
}
```

---

## 11. ♿ **Formulários Acessíveis**

### 11.1. ARIA Labels & Descriptions

```tsx
// ═══════════════════════════════════════════════════════════
// ARIA ATTRIBUTES
// ═══════════════════════════════════════════════════════════

import { useForm } from 'react-hook-form';
import { useId } from 'react';

interface FormData {
  email: string;
  password: string;
}

function AccessibleForm() {
  const emailId = useId();
  const emailErrorId = useId();
  const emailDescId = useId();
  const passwordId = useId();
  const passwordErrorId = useId();

  const {
    register,
    handleSubmit,
    formState: { errors }
  } = useForm<FormData>();

  return (
    <form onSubmit={handleSubmit(console.log)}>
      {/* Email Field */}
      <div>
        <label htmlFor={emailId}>
          Email Address
          <span aria-label="required">*</span>
        </label>
        
        <p id={emailDescId} className="text-sm text-gray-600">
          We'll never share your email with anyone else.
        </p>
        
        <input
          id={emailId}
          type="email"
          {...register('email', { required: 'Email is required' })}
          aria-required="true"
          aria-invalid={!!errors.email}
          aria-describedby={`${emailDescId} ${errors.email ? emailErrorId : ''}`}
          className="border rounded px-3 py-2 w-full"
        />
        
        {errors.email && (
          <p
            id={emailErrorId}
            role="alert"
            aria-live="polite"
            className="text-red-500 text-sm mt-1"
          >
            {errors.email.message}
          </p>
        )}
      </div>

      {/* Password Field */}
      <div>
        <label htmlFor={passwordId}>
          Password
          <span aria-label="required">*</span>
        </label>
        
        <input
          id={passwordId}
          type="password"
          {...register('password', {
            required: 'Password is required',
            minLength: {
              value: 8,
              message: 'Password must be at least 8 characters'
            }
          })}
          aria-required="true"
          aria-invalid={!!errors.password}
          aria-describedby={errors.password ? passwordErrorId : undefined}
          className="border rounded px-3 py-2 w-full"
        />
        
        {errors.password && (
          <p
            id={passwordErrorId}
            role="alert"
            aria-live="polite"
            className="text-red-500 text-sm mt-1"
          >
            {errors.password.message}
          </p>
        )}
      </div>

      <button
        type="submit"
        aria-label="Submit login form"
        className="bg-blue-600 text-white px-6 py-2 rounded"
      >
        Login
      </button>
    </form>
  );
}
```

### 11.2. Keyboard Navigation

```tsx
// ═══════════════════════════════════════════════════════════
// KEYBOARD NAVIGATION
// ═══════════════════════════════════════════════════════════

import { useForm } from 'react-hook-form';
import { useRef, KeyboardEvent } from 'react';

interface FormData {
  field1: string;
  field2: string;
  field3: string;
}

function KeyboardNavigationForm() {
  const field1Ref = useRef<HTMLInputElement>(null);
  const field2Ref = useRef<HTMLInputElement>(null);
  const field3Ref = useRef<HTMLInputElement>(null);
  const submitRef = useRef<HTMLButtonElement>(null);

  const { register, handleSubmit } = useForm<FormData>();

  const handleKeyDown = (e: KeyboardEvent<HTMLInputElement>, nextRef: any) => {
    // ✅ Enter to move to next field
    if (e.key === 'Enter') {
      e.preventDefault();
      nextRef.current?.focus();
    }
  };

  return (
    <form onSubmit={handleSubmit(console.log)}>
      <div>
        <label htmlFor="field1">Field 1</label>
        <input
          id="field1"
          {...register('field1')}
          ref={field1Ref}
          onKeyDown={(e) => handleKeyDown(e, field2Ref)}
          className="border rounded px-3 py-2 w-full"
        />
      </div>

      <div>
        <label htmlFor="field2">Field 2</label>
        <input
          id="field2"
          {...register('field2')}
          ref={field2Ref}
          onKeyDown={(e) => handleKeyDown(e, field3Ref)}
          className="border rounded px-3 py-2 w-full"
        />
      </div>

      <div>
        <label htmlFor="field3">Field 3</label>
        <input
          id="field3"
          {...register('field3')}
          ref={field3Ref}
          onKeyDown={(e) => handleKeyDown(e, submitRef)}
          className="border rounded px-3 py-2 w-full"
        />
      </div>

      <button
        ref={submitRef}
        type="submit"
        className="bg-blue-600 text-white px-6 py-2 rounded"
      >
        Submit
      </button>
    </form>
  );
}
```

### 11.3. Screen Reader Support

```tsx
// ═══════════════════════════════════════════════════════════
// SCREEN READER FRIENDLY FORM
// ═══════════════════════════════════════════════════════════

import { useForm } from 'react-hook-form';
import { useState } from 'react';

interface FormData {
  email: string;
  password: string;
}

function ScreenReaderForm() {
  const [announcement, setAnnouncement] = useState('');

  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting }
  } = useForm<FormData>();

  const onSubmit = async (data: FormData) => {
    setAnnouncement('Submitting form...');
    
    await new Promise(resolve => setTimeout(resolve, 1000));
    
    setAnnouncement('Form submitted successfully!');
    console.log(data);
  };

  return (
    <>
      {/* ✅ Screen reader announcements */}
      <div
        role="status"
        aria-live="polite"
        aria-atomic="true"
        className="sr-only"
      >
        {announcement}
      </div>

      <form onSubmit={handleSubmit(onSubmit)} aria-label="Login form">
        <fieldset>
          <legend className="text-lg font-bold mb-4">Login Information</legend>

          <div className="space-y-4">
            <div>
              <label htmlFor="email" className="block mb-1">
                Email Address
              </label>
              <input
                id="email"
                type="email"
                {...register('email', { required: 'Email is required' })}
                aria-required="true"
                aria-invalid={!!errors.email}
                className="border rounded px-3 py-2 w-full"
              />
              {errors.email && (
                <p role="alert" className="text-red-500 text-sm mt-1">
                  <span className="sr-only">Error: </span>
                  {errors.email.message}
                </p>
              )}
            </div>

            <div>
              <label htmlFor="password" className="block mb-1">
                Password
              </label>
              <input
                id="password"
                type="password"
                {...register('password', { required: 'Password is required' })}
                aria-required="true"
                aria-invalid={!!errors.password}
                className="border rounded px-3 py-2 w-full"
              />
              {errors.password && (
                <p role="alert" className="text-red-500 text-sm mt-1">
                  <span className="sr-only">Error: </span>
                  {errors.password.message}
                </p>
              )}
            </div>
          </div>
        </fieldset>

        <button
          type="submit"
          disabled={isSubmitting}
          aria-disabled={isSubmitting}
          className="bg-blue-600 text-white px-6 py-2 rounded mt-4"
        >
          {isSubmitting ? (
            <>
              <span aria-hidden="true">...</span>
              <span className="sr-only">Submitting</span>
            </>
          ) : (
            'Login'
          )}
        </button>
      </form>
    </>
  );
}

// ✅ CSS for screen-reader-only content
const styles = `
.sr-only {
  position: absolute;
  width: 1px;
  height: 1px;
  padding: 0;
  margin: -1px;
  overflow: hidden;
  clip: rect(0, 0, 0, 0);
  white-space: nowrap;
  border-width: 0;
}
`;
```

### 11.4. Focus Management

```tsx
// ═══════════════════════════════════════════════════════════
// FOCUS MANAGEMENT
// ═══════════════════════════════════════════════════════════

import { useForm } from 'react-hook-form';
import { useEffect, useRef } from 'react';

interface FormData {
  name: string;
  email: string;
}

function FocusManagementForm() {
  const firstErrorRef = useRef<HTMLInputElement>(null);

  const {
    register,
    handleSubmit,
    setFocus,
    formState: { errors, isSubmitSuccessful }
  } = useForm<FormData>();

  // ✅ Focus first field on mount
  useEffect(() => {
    setFocus('name');
  }, [setFocus]);

  // ✅ Focus first error on submit
  useEffect(() => {
    if (Object.keys(errors).length > 0) {
      const firstErrorField = Object.keys(errors)[0] as keyof FormData;
      setFocus(firstErrorField);
    }
  }, [errors, setFocus]);

  // ✅ Focus success message after successful submit
  useEffect(() => {
    if (isSubmitSuccessful) {
      document.getElementById('success-message')?.focus();
    }
  }, [isSubmitSuccessful]);

  const onSubmit = (data: FormData) => {
    console.log('Submit:', data);
  };

  return (
    <div>
      <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
        <div>
          <label htmlFor="name">Name</label>
          <input
            id="name"
            {...register('name', { required: 'Name is required' })}
            className={`border rounded px-3 py-2 w-full ${
              errors.name ? 'border-red-500' : ''
            }`}
          />
          {errors.name && (
            <p className="text-red-500 text-sm mt-1">{errors.name.message}</p>
          )}
        </div>

        <div>
          <label htmlFor="email">Email</label>
          <input
            id="email"
            type="email"
            {...register('email', {
              required: 'Email is required',
              pattern: {
                value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
                message: 'Invalid email'
              }
            })}
            className={`border rounded px-3 py-2 w-full ${
              errors.email ? 'border-red-500' : ''
            }`}
          />
          {errors.email && (
            <p className="text-red-500 text-sm mt-1">{errors.email.message}</p>
          )}
        </div>

        <button
          type="submit"
          className="bg-blue-600 text-white px-6 py-2 rounded"
        >
          Submit
        </button>
      </form>

      {isSubmitSuccessful && (
        <div
          id="success-message"
          tabIndex={-1}
          role="status"
          className="mt-4 p-4 bg-green-100 text-green-800 rounded"
        >
          Form submitted successfully!
        </div>
      )}
    </div>
  );
}
```

---

## 12. ⚡ **Performance Optimization**

### 12.1. Debounced Validation

```tsx
// ═══════════════════════════════════════════════════════════
// DEBOUNCED VALIDATION
// ═══════════════════════════════════════════════════════════

import { useForm } from 'react-hook-form';
import { useState, useEffect } from 'react';
import { debounce } from 'lodash';

interface FormData {
  username: string;
  email: string;
}

function DebouncedForm() {
  const [usernameAvailable, setUsernameAvailable] = useState<boolean | null>(null);
  const [checking, setChecking] = useState(false);

  const { register, watch, handleSubmit } = useForm<FormData>();

  const username = watch('username');

  // ✅ Debounced username check
  useEffect(() => {
    if (!username || username.length < 3) {
      setUsernameAvailable(null);
      return;
    }

    const checkUsername = debounce(async (value: string) => {
      setChecking(true);
      
      // Simulate API call
      await new Promise(resolve => setTimeout(resolve, 500));
      
      const taken = ['admin', 'root', 'user'].includes(value.toLowerCase());
      setUsernameAvailable(!taken);
      setChecking(false);
    }, 500);

    checkUsername(username);

    return () => {
      checkUsername.cancel();
    };
  }, [username]);

  return (
    <form onSubmit={handleSubmit(console.log)} className="space-y-4">
      <div>
        <label>Username</label>
        <input
          {...register('username')}
          className="border rounded px-3 py-2 w-full"
        />
        
        {checking && <p className="text-gray-500 text-sm">Checking...</p>}
        
        {usernameAvailable === true && (
          <p className="text-green-500 text-sm">✓ Username available</p>
        )}
        
        {usernameAvailable === false && (
          <p className="text-red-500 text-sm">✗ Username taken</p>
        )}
      </div>

      <div>
        <label>Email</label>
        <input
          type="email"
          {...register('email')}
          className="border rounded px-3 py-2 w-full"
        />
      </div>

      <button type="submit" className="bg-blue-600 text-white px-6 py-2 rounded">
        Submit
      </button>
    </form>
  );
}
```

### 12.2. Lazy Validation

```tsx
// ═══════════════════════════════════════════════════════════
// LAZY VALIDATION (Only validate on blur/submit)
// ═══════════════════════════════════════════════════════════

import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';

const schema = z.object({
  email: z.string().email(),
  password: z.string().min(8)
});

type FormData = z.infer<typeof schema>;

function LazyValidationForm() {
  const {
    register,
    handleSubmit,
    formState: { errors, touchedFields }
  } = useForm<FormData>({
    resolver: zodResolver(schema),
    mode: 'onTouched',  // ✅ Only validate after blur
    reValidateMode: 'onBlur'  // ✅ Re-validate on blur (not onChange)
  });

  return (
    <form onSubmit={handleSubmit(console.log)} className="space-y-4">
      <div>
        <input
          type="email"
          {...register('email')}
          placeholder="Email"
          className="border rounded px-3 py-2 w-full"
        />
        {/* ✅ Only show error after field is touched */}
        {touchedFields.email && errors.email && (
          <p className="text-red-500 text-sm">{errors.email.message}</p>
        )}
      </div>

      <div>
        <input
          type="password"
          {...register('password')}
          placeholder="Password"
          className="border rounded px-3 py-2 w-full"
        />
        {touchedFields.password && errors.password && (
          <p className="text-red-500 text-sm">{errors.password.message}</p>
        )}
      </div>

      <button type="submit" className="bg-blue-600 text-white px-6 py-2 rounded">
        Submit
      </button>
    </form>
  );
}
```

### 12.3. Virtualized Large Forms

```tsx
// ═══════════════════════════════════════════════════════════
// VIRTUALIZED LARGE FORMS
// ═══════════════════════════════════════════════════════════

// npm install react-window

import { useForm, useFieldArray } from 'react-hook-form';
import { FixedSizeList } from 'react-window';

interface FormData {
  items: Array<{ name: string; value: string }>;
}

function VirtualizedForm() {
  const { register, control, handleSubmit } = useForm<FormData>({
    defaultValues: {
      items: Array.from({ length: 1000 }, (_, i) => ({
        name: `Item ${i + 1}`,
        value: ''
      }))
    }
  });

  const { fields } = useFieldArray({
    control,
    name: 'items'
  });

  const Row = ({ index, style }: any) => {
    const field = fields[index];
    
    return (
      <div style={style} className="flex gap-2 p-2 border-b">
        <input
          {...register(`items.${index}.name`)}
          className="border rounded px-2 py-1 flex-1"
          readOnly
        />
        <input
          {...register(`items.${index}.value`)}
          className="border rounded px-2 py-1 flex-1"
          placeholder="Enter value"
        />
      </div>
    );
  };

  return (
    <form onSubmit={handleSubmit(console.log)}>
      <h2 className="text-xl font-bold mb-4">1000 Items Form</h2>
      
      {/* ✅ Virtualized list (only renders visible items) */}
      <FixedSizeList
        height={600}
        itemCount={fields.length}
        itemSize={50}
        width="100%"
      >
        {Row}
      </FixedSizeList>

      <button
        type="submit"
        className="mt-4 bg-blue-600 text-white px-6 py-2 rounded"
      >
        Submit
      </button>
    </form>
  );
}
```

### 12.4. Memoization

```tsx
// ═══════════════════════════════════════════════════════════
// MEMOIZATION (Prevent unnecessary re-renders)
// ═══════════════════════════════════════════════════════════

import { useForm } from 'react-hook-form';
import { memo } from 'react';

interface FormData {
  firstName: string;
  lastName: string;
  email: string;
}

// ✅ Memoized input component
const MemoizedInput = memo(({ name, register, error }: any) => {
  console.log(`Rendering input: ${name}`);
  
  return (
    <div>
      <label>{name}</label>
      <input
        {...register(name)}
        className="border rounded px-3 py-2 w-full"
      />
      {error && <p className="text-red-500 text-sm">{error.message}</p>}
    </div>
  );
});

MemoizedInput.displayName = 'MemoizedInput';

function OptimizedForm() {
  const {
    register,
    handleSubmit,
    formState: { errors }
  } = useForm<FormData>();

  return (
    <form onSubmit={handleSubmit(console.log)} className="space-y-4">
      {/* ✅ Each input only re-renders when its own value/error changes */}
      <MemoizedInput
        name="firstName"
        register={register}
        error={errors.firstName}
      />
      
      <MemoizedInput
        name="lastName"
        register={register}
        error={errors.lastName}
      />
      
      <MemoizedInput
        name="email"
        register={register}
        error={errors.email}
      />

      <button type="submit" className="bg-blue-600 text-white px-6 py-2 rounded">
        Submit
      </button>
    </form>
  );
}
```

### 12.5. Code Splitting

```tsx
// ═══════════════════════════════════════════════════════════
// CODE SPLITTING (Lazy load heavy components)
// ═══════════════════════════════════════════════════════════

import { lazy, Suspense, useState } from 'react';
import { useForm } from 'react-hook-form';

// ✅ Lazy load heavy editor component
const RichTextEditor = lazy(() => import('@/components/RichTextEditor'));

interface FormData {
  title: string;
  useRichEditor: boolean;
  content: string;
}

function CodeSplitForm() {
  const { register, watch, handleSubmit } = useForm<FormData>();
  const useRichEditor = watch('useRichEditor');

  return (
    <form onSubmit={handleSubmit(console.log)} className="space-y-4">
      <div>
        <input
          {...register('title')}
          placeholder="Title"
          className="border rounded px-3 py-2 w-full"
        />
      </div>

      <label className="flex items-center gap-2">
        <input type="checkbox" {...register('useRichEditor')} />
        Use Rich Text Editor
      </label>

      {useRichEditor ? (
        <Suspense fallback={<div>Loading editor...</div>}>
          <RichTextEditor name="content" />
        </Suspense>
      ) : (
        <textarea
          {...register('content')}
          rows={10}
          className="border rounded px-3 py-2 w-full"
        />
      )}

      <button type="submit" className="bg-blue-600 text-white px-6 py-2 rounded">
        Submit
      </button>
    </form>
  );
}
```

---

## 13. 🧪 **Form Testing**

### 13.1. Vitest + Testing Library

```tsx
// ═══════════════════════════════════════════════════════════
// VITEST + REACT TESTING LIBRARY
// ═══════════════════════════════════════════════════════════

// npm install -D vitest @testing-library/react @testing-library/user-event @testing-library/jest-dom

// ────────────────────────────────────────────────────────────
// LoginForm.tsx
// ────────────────────────────────────────────────────────────

import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';

const schema = z.object({
  email: z.string().email('Invalid email'),
  password: z.string().min(8, 'Password must be at least 8 characters')
});

type FormData = z.infer<typeof schema>;

interface LoginFormProps {
  onSubmit: (data: FormData) => void;
}

export function LoginForm({ onSubmit }: LoginFormProps) {
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting }
  } = useForm<FormData>({
    resolver: zodResolver(schema)
  });

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <div>
        <label htmlFor="email">Email</label>
        <input
          id="email"
          type="email"
          {...register('email')}
          placeholder="email@example.com"
        />
        {errors.email && (
          <p role="alert" data-testid="email-error">
            {errors.email.message}
          </p>
        )}
      </div>

      <div>
        <label htmlFor="password">Password</label>
        <input
          id="password"
          type="password"
          {...register('password')}
          placeholder="Password"
        />
        {errors.password && (
          <p role="alert" data-testid="password-error">
            {errors.password.message}
          </p>
        )}
      </div>

      <button type="submit" disabled={isSubmitting}>
        {isSubmitting ? 'Logging in...' : 'Login'}
      </button>
    </form>
  );
}

// ────────────────────────────────────────────────────────────
// LoginForm.test.tsx
// ────────────────────────────────────────────────────────────

import { describe, it, expect, vi } from 'vitest';
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { LoginForm } from './LoginForm';

describe('LoginForm', () => {
  it('renders form fields', () => {
    render(<LoginForm onSubmit={() => {}} />);

    expect(screen.getByLabelText(/email/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/password/i)).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /login/i })).toBeInTheDocument();
  });

  it('shows validation errors for empty fields', async () => {
    const user = userEvent.setup();
    render(<LoginForm onSubmit={() => {}} />);

    const submitButton = screen.getByRole('button', { name: /login/i });
    await user.click(submitButton);

    expect(await screen.findByTestId('email-error')).toHaveTextContent('Invalid email');
    expect(await screen.findByTestId('password-error')).toHaveTextContent(
      'Password must be at least 8 characters'
    );
  });

  it('shows validation error for invalid email', async () => {
    const user = userEvent.setup();
    render(<LoginForm onSubmit={() => {}} />);

    const emailInput = screen.getByLabelText(/email/i);
    await user.type(emailInput, 'invalid-email');
    await user.tab();

    expect(await screen.findByTestId('email-error')).toHaveTextContent('Invalid email');
  });

  it('shows validation error for short password', async () => {
    const user = userEvent.setup();
    render(<LoginForm onSubmit={() => {}} />);

    const passwordInput = screen.getByLabelText(/password/i);
    await user.type(passwordInput, '123');
    await user.tab();

    expect(await screen.findByTestId('password-error')).toHaveTextContent(
      'Password must be at least 8 characters'
    );
  });

  it('submits form with valid data', async () => {
    const user = userEvent.setup();
    const handleSubmit = vi.fn();
    render(<LoginForm onSubmit={handleSubmit} />);

    const emailInput = screen.getByLabelText(/email/i);
    const passwordInput = screen.getByLabelText(/password/i);
    const submitButton = screen.getByRole('button', { name: /login/i });

    await user.type(emailInput, 'test@example.com');
    await user.type(passwordInput, 'password123');
    await user.click(submitButton);

    expect(handleSubmit).toHaveBeenCalledWith({
      email: 'test@example.com',
      password: 'password123'
    });
  });

  it('disables submit button while submitting', async () => {
    const user = userEvent.setup();
    const handleSubmit = vi.fn(() => new Promise(resolve => setTimeout(resolve, 100)));
    render(<LoginForm onSubmit={handleSubmit} />);

    const emailInput = screen.getByLabelText(/email/i);
    const passwordInput = screen.getByLabelText(/password/i);
    const submitButton = screen.getByRole('button', { name: /login/i });

    await user.type(emailInput, 'test@example.com');
    await user.type(passwordInput, 'password123');
    await user.click(submitButton);

    expect(submitButton).toBeDisabled();
    expect(submitButton).toHaveTextContent(/logging in/i);
  });
});
```

### 13.2. Testing Field Arrays

```tsx
// ═══════════════════════════════════════════════════════════
// TESTING FIELD ARRAYS
// ═══════════════════════════════════════════════════════════

// ────────────────────────────────────────────────────────────
// EmailListForm.tsx
// ────────────────────────────────────────────────────────────

import { useForm, useFieldArray } from 'react-hook-form';

interface FormData {
  emails: Array<{ email: string }>;
}

interface EmailListFormProps {
  onSubmit: (data: FormData) => void;
}

export function EmailListForm({ onSubmit }: EmailListFormProps) {
  const { register, control, handleSubmit } = useForm<FormData>({
    defaultValues: {
      emails: [{ email: '' }]
    }
  });

  const { fields, append, remove } = useFieldArray({
    control,
    name: 'emails'
  });

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      {fields.map((field, index) => (
        <div key={field.id} data-testid={`email-field-${index}`}>
          <input
            {...register(`emails.${index}.email`)}
            placeholder="Email"
            aria-label={`Email ${index + 1}`}
          />
          <button
            type="button"
            onClick={() => remove(index)}
            disabled={fields.length === 1}
            aria-label={`Remove email ${index + 1}`}
          >
            Remove
          </button>
        </div>
      ))}

      <button
        type="button"
        onClick={() => append({ email: '' })}
        aria-label="Add email"
      >
        Add Email
      </button>

      <button type="submit">Submit</button>
    </form>
  );
}

// ────────────────────────────────────────────────────────────
// EmailListForm.test.tsx
// ────────────────────────────────────────────────────────────

import { describe, it, expect, vi } from 'vitest';
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { EmailListForm } from './EmailListForm';

describe('EmailListForm', () => {
  it('renders one email field by default', () => {
    render(<EmailListForm onSubmit={() => {}} />);

    expect(screen.getByTestId('email-field-0')).toBeInTheDocument();
  });

  it('adds new email field when clicking add button', async () => {
    const user = userEvent.setup();
    render(<EmailListForm onSubmit={() => {}} />);

    const addButton = screen.getByRole('button', { name: /add email/i });
    await user.click(addButton);

    expect(screen.getByTestId('email-field-0')).toBeInTheDocument();
    expect(screen.getByTestId('email-field-1')).toBeInTheDocument();
  });

  it('removes email field when clicking remove button', async () => {
    const user = userEvent.setup();
    render(<EmailListForm onSubmit={() => {}} />);

    // Add second field
    const addButton = screen.getByRole('button', { name: /add email/i });
    await user.click(addButton);

    // Remove second field
    const removeButton = screen.getByRole('button', { name: /remove email 2/i });
    await user.click(removeButton);

    expect(screen.getByTestId('email-field-0')).toBeInTheDocument();
    expect(screen.queryByTestId('email-field-1')).not.toBeInTheDocument();
  });

  it('disables remove button when only one field exists', () => {
    render(<EmailListForm onSubmit={() => {}} />);

    const removeButton = screen.getByRole('button', { name: /remove email 1/i });
    expect(removeButton).toBeDisabled();
  });

  it('submits form with multiple emails', async () => {
    const user = userEvent.setup();
    const handleSubmit = vi.fn();
    render(<EmailListForm onSubmit={handleSubmit} />);

    // Add second email field
    const addButton = screen.getByRole('button', { name: /add email/i });
    await user.click(addButton);

    // Fill in emails
    const email1Input = screen.getByLabelText(/email 1/i);
    const email2Input = screen.getByLabelText(/email 2/i);
    await user.type(email1Input, 'test1@example.com');
    await user.type(email2Input, 'test2@example.com');

    // Submit
    const submitButton = screen.getByRole('button', { name: /submit/i });
    await user.click(submitButton);

    expect(handleSubmit).toHaveBeenCalledWith({
      emails: [
        { email: 'test1@example.com' },
        { email: 'test2@example.com' }
      ]
    });
  });
});
```

### 13.3. Testing Async Validation

```tsx
// ═══════════════════════════════════════════════════════════
// TESTING ASYNC VALIDATION
// ═══════════════════════════════════════════════════════════

// ────────────────────────────────────────────────────────────
// SignupForm.tsx
// ────────────────────────────────────────────────────────────

import { useForm } from 'react-hook-form';

interface FormData {
  username: string;
}

interface SignupFormProps {
  checkUsername: (username: string) => Promise<boolean>;
  onSubmit: (data: FormData) => void;
}

export function SignupForm({ checkUsername, onSubmit }: SignupFormProps) {
  const {
    register,
    handleSubmit,
    formState: { errors, isValidating }
  } = useForm<FormData>();

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <div>
        <label htmlFor="username">Username</label>
        <input
          id="username"
          {...register('username', {
            required: 'Username is required',
            validate: async (value) => {
              const available = await checkUsername(value);
              return available || 'Username is taken';
            }
          })}
        />
        {isValidating && <p data-testid="validating">Checking...</p>}
        {errors.username && (
          <p role="alert" data-testid="username-error">
            {errors.username.message}
          </p>
        )}
      </div>

      <button type="submit">Sign Up</button>
    </form>
  );
}

// ────────────────────────────────────────────────────────────
// SignupForm.test.tsx
// ────────────────────────────────────────────────────────────

import { describe, it, expect, vi } from 'vitest';
import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { SignupForm } from './SignupForm';

describe('SignupForm', () => {
  it('shows loading state during async validation', async () => {
    const user = userEvent.setup();
    const checkUsername = vi.fn(() => new Promise(resolve => setTimeout(() => resolve(true), 100)));

    render(<SignupForm checkUsername={checkUsername} onSubmit={() => {}} />);

    const usernameInput = screen.getByLabelText(/username/i);
    await user.type(usernameInput, 'testuser');
    await user.tab();

    expect(screen.getByTestId('validating')).toBeInTheDocument();
    
    await waitFor(() => {
      expect(screen.queryByTestId('validating')).not.toBeInTheDocument();
    });
  });

  it('shows error when username is taken', async () => {
    const user = userEvent.setup();
    const checkUsername = vi.fn(() => Promise.resolve(false));

    render(<SignupForm checkUsername={checkUsername} onSubmit={() => {}} />);

    const usernameInput = screen.getByLabelText(/username/i);
    await user.type(usernameInput, 'admin');
    await user.tab();

    expect(await screen.findByTestId('username-error')).toHaveTextContent('Username is taken');
  });

  it('submits when username is available', async () => {
    const user = userEvent.setup();
    const checkUsername = vi.fn(() => Promise.resolve(true));
    const handleSubmit = vi.fn();

    render(<SignupForm checkUsername={checkUsername} onSubmit={handleSubmit} />);

    const usernameInput = screen.getByLabelText(/username/i);
    const submitButton = screen.getByRole('button', { name: /sign up/i });

    await user.type(usernameInput, 'newuser');
    await user.click(submitButton);

    await waitFor(() => {
      expect(handleSubmit).toHaveBeenCalledWith({ username: 'newuser' });
    });
  });
});
```

### 13.4. E2E Testing (Playwright)

```tsx
// ═══════════════════════════════════════════════════════════
// E2E TESTING WITH PLAYWRIGHT
// ═══════════════════════════════════════════════════════════

// npm install -D @playwright/test

// ────────────────────────────────────────────────────────────
// e2e/signup.spec.ts
// ────────────────────────────────────────────────────────────

import { test, expect } from '@playwright/test';

test.describe('Signup Form', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('http://localhost:3000/signup');
  });

  test('shows validation errors for empty form', async ({ page }) => {
    await page.click('button[type="submit"]');

    await expect(page.locator('[data-testid="email-error"]')).toHaveText('Email is required');
    await expect(page.locator('[data-testid="password-error"]')).toHaveText(
      'Password is required'
    );
  });

  test('shows error for invalid email', async ({ page }) => {
    await page.fill('input[name="email"]', 'invalid-email');
    await page.fill('input[name="password"]', 'password123');
    await page.click('button[type="submit"]');

    await expect(page.locator('[data-testid="email-error"]')).toHaveText('Invalid email');
  });

  test('completes signup flow successfully', async ({ page }) => {
    // Fill form
    await page.fill('input[name="email"]', 'test@example.com');
    await page.fill('input[name="password"]', 'password123');
    await page.fill('input[name="confirmPassword"]', 'password123');

    // Submit
    await page.click('button[type="submit"]');

    // Wait for success message
    await expect(page.locator('[data-testid="success-message"]')).toBeVisible();
    await expect(page.locator('[data-testid="success-message"]')).toHaveText(
      'Account created successfully!'
    );

    // Check redirect
    await expect(page).toHaveURL('http://localhost:3000/dashboard');
  });

  test('handles server error gracefully', async ({ page }) => {
    // Mock API to return error
    await page.route('**/api/signup', route => {
      route.fulfill({
        status: 500,
        body: JSON.stringify({ error: 'Internal server error' })
      });
    });

    await page.fill('input[name="email"]', 'test@example.com');
    await page.fill('input[name="password"]', 'password123');
    await page.fill('input[name="confirmPassword"]', 'password123');
    await page.click('button[type="submit"]');

    await expect(page.locator('[data-testid="error-message"]')).toHaveText(
      'Something went wrong. Please try again.'
    );
  });

  test('preserves form data when navigating back', async ({ page }) => {
    await page.fill('input[name="email"]', 'test@example.com');
    await page.fill('input[name="password"]', 'password123');

    // Navigate away
    await page.goto('http://localhost:3000/');

    // Navigate back
    await page.goBack();

    // Check if form data is preserved (if using form persistence)
    await expect(page.locator('input[name="email"]')).toHaveValue('test@example.com');
  });
});
```

---

## 14. 🎯 **Patterns & Best Practices**

### 14.1. Reusable Form Components

```tsx
// ═══════════════════════════════════════════════════════════
// REUSABLE FORM COMPONENTS
// ═══════════════════════════════════════════════════════════

// ────────────────────────────────────────────────────────────
// components/FormField.tsx
// ────────────────────────────────────────────────────────────

import { useFormContext } from 'react-hook-form';
import { useId } from 'react';

interface FormFieldProps {
  name: string;
  label: string;
  type?: 'text' | 'email' | 'password' | 'number';
  placeholder?: string;
  required?: boolean;
  description?: string;
}

export function FormField({
  name,
  label,
  type = 'text',
  placeholder,
  required,
  description
}: FormFieldProps) {
  const {
    register,
    formState: { errors }
  } = useFormContext();

  const id = useId();
  const errorId = useId();
  const descId = useId();

  const error = errors[name];

  return (
    <div className="space-y-1">
      <label htmlFor={id} className="block font-medium">
        {label}
        {required && <span className="text-red-500 ml-1">*</span>}
      </label>

      {description && (
        <p id={descId} className="text-sm text-gray-600">
          {description}
        </p>
      )}

      <input
        id={id}
        type={type}
        {...register(name)}
        placeholder={placeholder}
        aria-required={required}
        aria-invalid={!!error}
        aria-describedby={description ? descId : undefined}
        className={`border rounded px-3 py-2 w-full ${
          error ? 'border-red-500' : 'border-gray-300'
        }`}
      />

      {error && (
        <p id={errorId} role="alert" className="text-red-500 text-sm">
          {error.message as string}
        </p>
      )}
    </div>
  );
}

// ────────────────────────────────────────────────────────────
// Usage
// ────────────────────────────────────────────────────────────

import { useForm, FormProvider } from 'react-hook-form';
import { FormField } from '@/components/FormField';

function MyForm() {
  const methods = useForm();

  return (
    <FormProvider {...methods}>
      <form onSubmit={methods.handleSubmit(console.log)} className="space-y-4">
        <FormField
          name="email"
          label="Email Address"
          type="email"
          required
          description="We'll never share your email"
        />

        <FormField
          name="password"
          label="Password"
          type="password"
          required
        />

        <button type="submit">Submit</button>
      </form>
    </FormProvider>
  );
}
```

### 14.2. Custom Form Hooks

```tsx
// ═══════════════════════════════════════════════════════════
// CUSTOM FORM HOOKS
// ═══════════════════════════════════════════════════════════

// ────────────────────────────────────────────────────────────
// hooks/useFormPersist.ts
// ────────────────────────────────────────────────────────────

import { useEffect } from 'react';
import { UseFormWatch } from 'react-hook-form';

export function useFormPersist(
  key: string,
  watch: UseFormWatch<any>,
  setValue: any,
  exclude: string[] = []
) {
  // ✅ Load from localStorage on mount
  useEffect(() => {
    const saved = localStorage.getItem(key);
    if (saved) {
      const data = JSON.parse(saved);
      Object.keys(data).forEach(fieldName => {
        if (!exclude.includes(fieldName)) {
          setValue(fieldName, data[fieldName]);
        }
      });
    }
  }, [key, setValue, exclude]);

  // ✅ Save to localStorage on change
  useEffect(() => {
    const subscription = watch((data) => {
      const filteredData = { ...data };
      exclude.forEach(field => delete filteredData[field]);
      localStorage.setItem(key, JSON.stringify(filteredData));
    });

    return () => subscription.unsubscribe();
  }, [key, watch, exclude]);
}

// ────────────────────────────────────────────────────────────
// hooks/useFormAnalytics.ts
// ────────────────────────────────────────────────────────────

import { useEffect } from 'react';
import { UseFormWatch } from 'react-hook-form';

export function useFormAnalytics(formName: string, watch: UseFormWatch<any>) {
  const startTime = useRef(Date.now());

  useEffect(() => {
    // Track form start
    analytics.track('Form Started', { form: formName });

    return () => {
      // Track form abandonment
      const timeSpent = Date.now() - startTime.current;
      analytics.track('Form Abandoned', {
        form: formName,
        timeSpent
      });
    };
  }, [formName]);

  // Track field interactions
  useEffect(() => {
    const subscription = watch((data, { name, type }) => {
      if (type === 'change' && name) {
        analytics.track('Form Field Changed', {
          form: formName,
          field: name
        });
      }
    });

    return () => subscription.unsubscribe();
  }, [formName, watch]);
}

// ────────────────────────────────────────────────────────────
// Usage
// ────────────────────────────────────────────────────────────

import { useForm } from 'react-hook-form';
import { useFormPersist } from '@/hooks/useFormPersist';
import { useFormAnalytics } from '@/hooks/useFormAnalytics';

function PersistentForm() {
  const { register, watch, setValue, handleSubmit } = useForm();

  // ✅ Persist form data
  useFormPersist('signup-form', watch, setValue, ['password']);

  // ✅ Track analytics
  useFormAnalytics('signup-form', watch);

  const onSubmit = (data: any) => {
    localStorage.removeItem('signup-form');
    console.log('Submit:', data);
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <input {...register('email')} placeholder="Email" />
      <input {...register('password')} type="password" placeholder="Password" />
      <button type="submit">Submit</button>
    </form>
  );
}
```

### 14.3. Error Handling Pattern

```tsx
// ═══════════════════════════════════════════════════════════
// ERROR HANDLING PATTERN
// ═══════════════════════════════════════════════════════════

import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { useState } from 'react';

const schema = z.object({
  email: z.string().email(),
  password: z.string().min(8)
});

type FormData = z.infer<typeof schema>;

type ServerError = {
  field?: keyof FormData;
  message: string;
};

function FormWithErrorHandling() {
  const [serverError, setServerError] = useState<string | null>(null);

  const {
    register,
    handleSubmit,
    setError,
    formState: { errors, isSubmitting }
  } = useForm<FormData>({
    resolver: zodResolver(schema)
  });

  const onSubmit = async (data: FormData) => {
    setServerError(null);

    try {
      const response = await fetch('/api/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
      });

      if (!response.ok) {
        const error: ServerError = await response.json();

        // ✅ Field-specific error
        if (error.field) {
          setError(error.field, {
            type: 'server',
            message: error.message
          });
        } else {
          // ✅ General error
          setServerError(error.message);
        }
        return;
      }

      const result = await response.json();
      console.log('Success:', result);
    } catch (error) {
      // ✅ Network error
      setServerError('Network error. Please check your connection and try again.');
    }
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
      {/* General error alert */}
      {serverError && (
        <div
          role="alert"
          className="bg-red-50 border border-red-200 text-red-800 px-4 py-3 rounded"
        >
          {serverError}
        </div>
      )}

      <div>
        <input
          type="email"
          {...register('email')}
          placeholder="Email"
          className="border rounded px-3 py-2 w-full"
        />
        {errors.email && (
          <p className="text-red-500 text-sm mt-1">{errors.email.message}</p>
        )}
      </div>

      <div>
        <input
          type="password"
          {...register('password')}
          placeholder="Password"
          className="border rounded px-3 py-2 w-full"
        />
        {errors.password && (
          <p className="text-red-500 text-sm mt-1">{errors.password.message}</p>
        )}
      </div>

      <button
        type="submit"
        disabled={isSubmitting}
        className="bg-blue-600 text-white px-6 py-2 rounded disabled:opacity-50"
      >
        {isSubmitting ? 'Logging in...' : 'Login'}
      </button>
    </form>
  );
}
```

### 14.4. Form State Management

```tsx
// ═══════════════════════════════════════════════════════════
// FORM STATE MANAGEMENT (Zustand)
// ═══════════════════════════════════════════════════════════

// npm install zustand

// ────────────────────────────────────────────────────────────
// store/formStore.ts
// ────────────────────────────────────────────────────────────

import { create } from 'zustand';
import { persist } from 'zustand/middleware';

interface FormState {
  currentStep: number;
  formData: Record<string, any>;
  setStep: (step: number) => void;
  updateFormData: (data: Record<string, any>) => void;
  resetForm: () => void;
}

export const useFormStore = create<FormState>()(
  persist(
    (set) => ({
      currentStep: 1,
      formData: {},
      setStep: (step) => set({ currentStep: step }),
      updateFormData: (data) =>
        set((state) => ({
          formData: { ...state.formData, ...data }
        })),
      resetForm: () => set({ currentStep: 1, formData: {} })
    }),
    {
      name: 'multi-step-form'
    }
  )
);

// ────────────────────────────────────────────────────────────
// Multi-step form with Zustand
// ────────────────────────────────────────────────────────────

import { useForm } from 'react-hook-form';
import { useFormStore } from '@/store/formStore';

function Step1() {
  const { formData, updateFormData, setStep } = useFormStore();
  const { register, handleSubmit } = useForm({
    defaultValues: formData
  });

  const onSubmit = (data: any) => {
    updateFormData(data);
    setStep(2);
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <input {...register('firstName')} placeholder="First Name" />
      <input {...register('lastName')} placeholder="Last Name" />
      <button type="submit">Next</button>
    </form>
  );
}

function Step2() {
  const { formData, updateFormData, setStep, resetForm } = useFormStore();
  const { register, handleSubmit } = useForm({
    defaultValues: formData
  });

  const onSubmit = async (data: any) => {
    updateFormData(data);
    
    // Submit complete form
    const completeData = { ...formData, ...data };
    await fetch('/api/submit', {
      method: 'POST',
      body: JSON.stringify(completeData)
    });
    
    resetForm();
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <input {...register('email')} placeholder="Email" />
      <input {...register('phone')} placeholder="Phone" />
      <button type="button" onClick={() => setStep(1)}>Back</button>
      <button type="submit">Submit</button>
    </form>
  );
}
```

### 14.5. Best Practices Checklist

```markdown
# ✅ React Forms Best Practices

## Validation
- [ ] Client-side validation with Zod/Yup
- [ ] Server-side validation (never trust client)
- [ ] Real-time validation for better UX (onBlur/onChange)
- [ ] Clear, actionable error messages
- [ ] Show errors near the relevant field

## Accessibility
- [ ] Proper label associations (htmlFor/id)
- [ ] ARIA attributes (aria-required, aria-invalid, aria-describedby)
- [ ] Keyboard navigation support
- [ ] Screen reader announcements for errors
- [ ] Focus management (first error on submit)

## Performance
- [ ] Debounce expensive validations
- [ ] Use uncontrolled components when possible (React Hook Form)
- [ ] Memoize components to prevent re-renders
- [ ] Lazy load heavy form components
- [ ] Virtualize large field arrays

## UX
- [ ] Loading states during submission
- [ ] Disable submit button while submitting
- [ ] Success/error feedback after submission
- [ ] Preserve form data on navigation (optional)
- [ ] Auto-save drafts for long forms
- [ ] Clear button for easy reset

## Security
- [ ] CSRF protection for mutations
- [ ] Input sanitization
- [ ] Rate limiting on submission
- [ ] Sensitive data encryption (passwords)
- [ ] Validate file uploads (type, size)

## Testing
- [ ] Unit tests for validation logic
- [ ] Integration tests for form submission
- [ ] E2E tests for critical flows
- [ ] Test accessibility with screen readers
- [ ] Test keyboard navigation

## Code Quality
- [ ] TypeScript for type safety
- [ ] Reusable form components
- [ ] Centralized validation schemas
- [ ] Consistent error handling
- [ ] Clean separation of concerns
```

---

## 15. 📚 **Recursos e Referências**

### 15.1. Documentação Oficial

**React Hook Form:**
- 📖 **Docs**: https://react-hook-form.com/
- 🎯 **API Reference**: https://react-hook-form.com/api
- 💡 **Examples**: https://github.com/react-hook-form/react-hook-form/tree/master/examples

**Zod:**
- 📖 **Docs**: https://zod.dev/
- 🔍 **Schema Validation**: https://zod.dev/?id=basic-usage
- 🔗 **Integration**: https://react-hook-form.com/get-started#SchemaValidation

**Bibliotecas Relacionadas:**
- ✅ **@hookform/resolvers**: https://github.com/react-hook-form/resolvers
- ✅ **@hookform/devtools**: https://react-hook-form.com/dev-tools

### 15.2. Ferramentas & Extensões

```bash
# ═══════════════════════════════════════════════════════════
# FERRAMENTAS ÚTEIS
# ═══════════════════════════════════════════════════════════

# Core
npm install react-hook-form zod @hookform/resolvers

# UI Libraries
npm install @radix-ui/react-label @radix-ui/react-select
npm install @mui/material @emotion/react @emotion/styled

# File Upload
npm install react-dropzone

# Rich Text
npm install @tiptap/react @tiptap/starter-kit

# Date Picker
npm install react-datepicker
npm install date-fns

# Phone Input
npm install react-phone-number-input

# Autocomplete
npm install react-select

# Testing
npm install -D vitest @testing-library/react @testing-library/user-event
npm install -D @playwright/test

# Dev Tools
npm install -D @hookform/devtools
```

### 15.3. Exemplo Completo: E-commerce Checkout

```tsx
// ═══════════════════════════════════════════════════════════
// COMPLETE E-COMMERCE CHECKOUT FORM
// ═══════════════════════════════════════════════════════════

import { useForm, FormProvider } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { useState } from 'react';

// ✅ Schema
const checkoutSchema = z.object({
  // Personal Info
  email: z.string().email('Invalid email'),
  firstName: z.string().min(2, 'First name is required'),
  lastName: z.string().min(2, 'Last name is required'),
  phone: z.string().regex(/^\+?[1-9]\d{1,14}$/, 'Invalid phone number'),

  // Shipping Address
  address: z.string().min(5, 'Address is required'),
  city: z.string().min(2, 'City is required'),
  state: z.string().min(2, 'State is required'),
  zipCode: z.string().regex(/^\d{5}(-\d{4})?$/, 'Invalid ZIP code'),
  country: z.string().min(2, 'Country is required'),

  // Billing (optional if same as shipping)
  sameAsShipping: z.boolean(),
  billingAddress: z.string().optional(),
  billingCity: z.string().optional(),
  billingState: z.string().optional(),
  billingZipCode: z.string().optional(),

  // Payment
  cardNumber: z.string().length(16, 'Card number must be 16 digits'),
  cardName: z.string().min(2, 'Cardholder name is required'),
  expiryDate: z.string().regex(/^\d{2}\/\d{2}$/, 'Format: MM/YY'),
  cvv: z.string().length(3, 'CVV must be 3 digits'),

  // Terms
  acceptTerms: z.boolean().refine(val => val === true, 'You must accept terms')
}).refine(data => {
  if (!data.sameAsShipping) {
    return (
      data.billingAddress &&
      data.billingCity &&
      data.billingState &&
      data.billingZipCode
    );
  }
  return true;
}, {
  message: 'Billing address is required',
  path: ['billingAddress']
});

type CheckoutFormData = z.infer<typeof checkoutSchema>;

export default function CheckoutForm() {
  const [step, setStep] = useState(1);

  const methods = useForm<CheckoutFormData>({
    resolver: zodResolver(checkoutSchema),
    defaultValues: {
      sameAsShipping: true,
      acceptTerms: false
    }
  });

  const sameAsShipping = methods.watch('sameAsShipping');

  const onSubmit = async (data: CheckoutFormData) => {
    console.log('Order submitted:', data);
    
    // Process payment
    const response = await fetch('/api/checkout', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data)
    });

    if (response.ok) {
      window.location.href = '/order-confirmation';
    }
  };

  const nextStep = async () => {
    let isValid = false;

    switch (step) {
      case 1:
        isValid = await methods.trigger(['email', 'firstName', 'lastName', 'phone']);
        break;
      case 2:
        isValid = await methods.trigger([
          'address',
          'city',
          'state',
          'zipCode',
          'country'
        ]);
        break;
    }

    if (isValid) setStep(prev => prev + 1);
  };

  return (
    <FormProvider {...methods}>
      <form onSubmit={methods.handleSubmit(onSubmit)} className="max-w-2xl mx-auto p-6">
        {/* Progress Indicator */}
        <div className="mb-8">
          <div className="flex justify-between">
            {['Contact', 'Shipping', 'Payment'].map((label, i) => (
              <div
                key={label}
                className={`flex-1 text-center ${
                  i + 1 <= step ? 'text-blue-600' : 'text-gray-400'
                }`}
              >
                <div className="font-medium">{label}</div>
              </div>
            ))}
          </div>
          <div className="mt-2 h-2 bg-gray-200 rounded">
            <div
              className="h-2 bg-blue-600 rounded transition-all"
              style={{ width: `${(step / 3) * 100}%` }}
            />
          </div>
        </div>

        {/* Step 1: Contact Info */}
        {step === 1 && (
          <div className="space-y-4">
            <h2 className="text-2xl font-bold mb-4">Contact Information</h2>

            <FormField name="email" label="Email" type="email" required />
            <div className="grid grid-cols-2 gap-4">
              <FormField name="firstName" label="First Name" required />
              <FormField name="lastName" label="Last Name" required />
            </div>
            <FormField name="phone" label="Phone Number" required />

            <button
              type="button"
              onClick={nextStep}
              className="w-full bg-blue-600 text-white py-3 rounded hover:bg-blue-700"
            >
              Continue to Shipping
            </button>
          </div>
        )}

        {/* Step 2: Shipping Address */}
        {step === 2 && (
          <div className="space-y-4">
            <h2 className="text-2xl font-bold mb-4">Shipping Address</h2>

            <FormField name="address" label="Street Address" required />
            <div className="grid grid-cols-2 gap-4">
              <FormField name="city" label="City" required />
              <FormField name="state" label="State" required />
            </div>
            <div className="grid grid-cols-2 gap-4">
              <FormField name="zipCode" label="ZIP Code" required />
              <FormField name="country" label="Country" required />
            </div>

            <label className="flex items-center gap-2">
              <input type="checkbox" {...methods.register('sameAsShipping')} />
              Billing address same as shipping
            </label>

            {!sameAsShipping && (
              <div className="border-t pt-4 space-y-4">
                <h3 className="font-bold">Billing Address</h3>
                <FormField name="billingAddress" label="Street Address" />
                <div className="grid grid-cols-2 gap-4">
                  <FormField name="billingCity" label="City" />
                  <FormField name="billingState" label="State" />
                </div>
                <FormField name="billingZipCode" label="ZIP Code" />
              </div>
            )}

            <div className="flex gap-4">
              <button
                type="button"
                onClick={() => setStep(1)}
                className="flex-1 border border-gray-300 py-3 rounded hover:bg-gray-50"
              >
                Back
              </button>
              <button
                type="button"
                onClick={nextStep}
                className="flex-1 bg-blue-600 text-white py-3 rounded hover:bg-blue-700"
              >
                Continue to Payment
              </button>
            </div>
          </div>
        )}

        {/* Step 3: Payment */}
        {step === 3 && (
          <div className="space-y-4">
            <h2 className="text-2xl font-bold mb-4">Payment Information</h2>

            <FormField name="cardNumber" label="Card Number" required />
            <FormField name="cardName" label="Cardholder Name" required />
            <div className="grid grid-cols-2 gap-4">
              <FormField name="expiryDate" label="Expiry (MM/YY)" required />
              <FormField name="cvv" label="CVV" required />
            </div>

            <label className="flex items-start gap-2">
              <input
                type="checkbox"
                {...methods.register('acceptTerms')}
                className="mt-1"
              />
              <span className="text-sm">
                I accept the{' '}
                <a href="/terms" className="text-blue-600 hover:underline">
                  Terms & Conditions
                </a>
              </span>
            </label>
            {methods.formState.errors.acceptTerms && (
              <p className="text-red-500 text-sm">
                {methods.formState.errors.acceptTerms.message}
              </p>
            )}

            <div className="flex gap-4">
              <button
                type="button"
                onClick={() => setStep(2)}
                className="flex-1 border border-gray-300 py-3 rounded hover:bg-gray-50"
              >
                Back
              </button>
              <button
                type="submit"
                disabled={methods.formState.isSubmitting}
                className="flex-1 bg-blue-600 text-white py-3 rounded hover:bg-blue-700 disabled:opacity-50"
              >
                {methods.formState.isSubmitting ? 'Processing...' : 'Place Order'}
              </button>
            </div>
          </div>
        )}
      </form>
    </FormProvider>
  );
}

// ✅ Reusable FormField component (from section 14.1)
function FormField({ name, label, type = 'text', required }: any) {
  const { register, formState: { errors } } = useFormContext();
  const error = errors[name];

  return (
    <div>
      <label className="block font-medium mb-1">
        {label}
        {required && <span className="text-red-500 ml-1">*</span>}
      </label>
      <input
        type={type}
        {...register(name)}
        className={`border rounded px-3 py-2 w-full ${
          error ? 'border-red-500' : 'border-gray-300'
        }`}
      />
      {error && (
        <p className="text-red-500 text-sm mt-1">{error.message as string}</p>
      )}
    </div>
  );
}
```

### 15.4. Resources

```markdown
# 📚 Additional Resources

## Articles & Tutorials
- [React Hook Form Tutorial](https://www.robinwieruch.de/react-hook-form/)
- [Advanced Form Validation](https://kentcdodds.com/blog/advanced-form-validation)
- [Accessible Forms](https://www.smashingmagazine.com/2021/11/guide-building-accessible-forms/)

## Videos
- [React Hook Form Crash Course](https://www.youtube.com/watch?v=RkXv4AXXC_4)
- [Building Production Forms](https://egghead.io/courses/react-hook-form)

## GitHub Examples
- [shadcn/ui Forms](https://ui.shadcn.com/docs/components/form)
- [React Hook Form Examples](https://github.com/react-hook-form/react-hook-form/tree/master/examples)

## Tools
- [Form Builder](https://formbuilder.online/)
- [Zod Schema Generator](https://transform.tools/json-to-zod)
```

---

## 🎯 **Conclusão**

### **React Forms** em resumo:

✅ **React Hook Form**: Performance + DX otimizados  
✅ **Zod**: Type-safe schema validation  
✅ **Validation**: Client + Server-side  
✅ **Multi-Step**: Wizard patterns  
✅ **File Upload**: Single, multiple, drag-drop  
✅ **Dynamic Forms**: Field arrays, conditional fields  
✅ **UI Libraries**: shadcn/ui, Radix, MUI  
✅ **Accessibility**: ARIA, keyboard, screen readers  
✅ **Performance**: Debounce, lazy, virtualization  
✅ **Testing**: Vitest, Playwright, E2E  
✅ **Patterns**: Reusable components, custom hooks  

**Happy form building!** 📝🚀

---

