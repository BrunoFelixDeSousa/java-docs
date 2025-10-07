# 🔄 TanStack Query (React Query) - Guia Completo

**TanStack Query** (anteriormente React Query) é a biblioteca mais poderosa para gerenciamento de **estado assíncrono** em React.

> "Gerenciamento de estado do servidor, sem dor de cabeça"

[![TanStack Query](https://img.shields.io/badge/TanStack_Query-v5+-FF4154?style=flat&logo=react-query&logoColor=white)](https://tanstack.com/query)
[![TypeScript](https://img.shields.io/badge/TypeScript-Ready-3178C6?style=flat&logo=typescript&logoColor=white)](https://www.typescriptlang.org/)

---

## 📋 Índice

- [Por que TanStack Query?](#-por-que-tanstack-query)
- [Instalação](#-instalação)
- [Setup Inicial](#️-setup-inicial)
- [Conceitos Fundamentais](#-conceitos-fundamentais)
- [Queries (Leitura)](#-queries-leitura)
- [Mutations (Escrita)](#️-mutations-escrita)
- [Cache & Invalidation](#-cache--invalidation)
- [Infinite Queries](#-infinite-queries)
- [Prefetching](#-prefetching)
- [Optimistic Updates](#-optimistic-updates)
- [Estrutura de Projeto](#-estrutura-de-projeto)
- [Best Practices](#-best-practices)

---

## 🎯 Por que TanStack Query?

### ✅ Problemas que Resolve

```typescript
// ❌ Sem React Query: Complexo e propenso a bugs
function UserList() {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    setLoading(true);
    fetch('/api/users')
      .then(res => res.json())
      .then(data => {
        setUsers(data);
        setLoading(false);
      })
      .catch(err => {
        setError(err);
        setLoading(false);
      });
  }, []); // E se precisar refetch? E cache? E deduplicação?

  // ... mais lógica complexa
}

// ✅ Com React Query: Simples e robusto
function UserList() {
  const { data: users, isLoading, error } = useQuery({
    queryKey: ['users'],
    queryFn: () => fetch('/api/users').then(res => res.json())
  });

  // Cache automático, refetch, deduplicação, tudo resolvido! 🎉
}
```

### 🚀 Recursos Principais

| Recurso | Descrição |
|---------|-----------|
| **Cache Automático** | Dados em cache inteligente com invalidação |
| **Deduplicação** | Múltiplas requisições = 1 chamada de API |
| **Background Updates** | Atualiza dados sem loading states |
| **Stale-While-Revalidate** | Mostra cache enquanto revalida |
| **Infinite Scroll** | Paginação infinita built-in |
| **Prefetching** | Carrega dados antes de serem necessários |
| **Optimistic Updates** | UI atualiza antes da resposta da API |
| **DevTools** | Debug visual do estado das queries |

---

## 📦 Instalação

### Vite + React + TypeScript

```bash
npm install @tanstack/react-query
npm install @tanstack/react-query-devtools --save-dev
```

### Next.js

```bash
npm install @tanstack/react-query @tanstack/react-query-devtools
```

---

## ⚙️ Setup Inicial

### Vite/React Setup

```tsx
// src/main.tsx
import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { ReactQueryDevtools } from '@tanstack/react-query-devtools';

// 1. Criar QueryClient com configurações globais
const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      staleTime: 60 * 1000,              // 1min - Dados "frescos"
      gcTime: 5 * 60 * 1000,             // 5min - Garbage collection (antigo cacheTime)
      refetchOnWindowFocus: false,       // Não refetch ao focar janela
      retry: 1,                          // Tentar 1x em caso de erro
    },
  },
});

// 2. Envolver app com Provider
ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <QueryClientProvider client={queryClient}>
      <App />
      {/* DevTools - apenas em desenvolvimento */}
      <ReactQueryDevtools initialIsOpen={false} />
    </QueryClientProvider>
  </React.StrictMode>
);
```

### Next.js Setup (App Router)

```tsx
// app/providers.tsx
'use client';

import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { ReactQueryDevtools } from '@tanstack/react-query-devtools';
import { useState } from 'react';

export function Providers({ children }: { children: React.ReactNode }) {
  // IMPORTANTE: Criar novo QueryClient por sessão no Next.js
  const [queryClient] = useState(() => new QueryClient({
    defaultOptions: {
      queries: {
        staleTime: 60 * 1000,
        gcTime: 5 * 60 * 1000,
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

// app/layout.tsx
import { Providers } from './providers';

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="pt-BR">
      <body>
        <Providers>{children}</Providers>
      </body>
    </html>
  );
}
```

---

## 💡 Conceitos Fundamentais

### 1. Query Key (Chave da Query)

**Query Keys** identificam e organizam o cache.

```typescript
// ✅ BOAS PRÁTICAS: Query Keys Hierárquicas

// Simples
['todos']                           // Todos os TODOs
['todos', 5]                        // TODO específico

// Com filtros
['todos', { status: 'done' }]      // TODOs filtrados
['todos', { userId: 1, page: 2 }]  // TODOs paginados

// Hierárquica (recomendado)
['users']                           // Lista de usuários
['users', 'list', { page: 1 }]     // Usuários paginados
['users', 'detail', 5]              // Detalhes do usuário 5

// ✅ FACTORY PATTERN: Organize em um objeto
export const todoKeys = {
  all: ['todos'] as const,
  lists: () => [...todoKeys.all, 'list'] as const,
  list: (filters: object) => [...todoKeys.lists(), filters] as const,
  details: () => [...todoKeys.all, 'detail'] as const,
  detail: (id: number) => [...todoKeys.details(), id] as const,
};

// Uso
useQuery({ queryKey: todoKeys.list({ status: 'done' }), ... });
useQuery({ queryKey: todoKeys.detail(5), ... });
```

### 2. staleTime vs gcTime

```typescript
// staleTime: Quanto tempo os dados são "frescos"
// - Dados frescos NÃO disparam refetch automático
// - Padrão: 0 (sempre stale)

// gcTime (antigo cacheTime): Quanto tempo dados ficam em memória
// - Após esse tempo SEM uso, dados são removidos
// - Padrão: 5 minutos

// Exemplo visual
const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      staleTime: 30 * 1000,      // 30s - Dados considerados frescos
      gcTime: 5 * 60 * 1000,     // 5min - Mantém em cache por 5min
    },
  },
});

// Timeline de uma query:
// t=0s   → Fetch inicial (fresh)
// t=30s  → Fica stale (mas ainda em cache)
// t=31s  → Novo acesso = refetch automático (pois está stale)
// t=5min → Se não for usada, é removida do cache (GC)
```

### 3. Estados de uma Query

```typescript
const { data, error, status, fetchStatus } = useQuery({ ... });

// status: Estado dos DADOS
// - 'pending'  → Ainda não tem dados (primeira vez)
// - 'error'    → Erro ao buscar dados
// - 'success'  → Dados carregados com sucesso

// fetchStatus: Estado da REQUISIÇÃO
// - 'fetching' → Buscando dados (pode ter cache)
// - 'paused'   → Query pausada (sem rede)
// - 'idle'     → Query não está buscando

// Helpers
const { isLoading, isFetching, isError, isSuccess } = useQuery({ ... });

// isLoading = status === 'pending'   (sem dados E buscando)
// isFetching = fetchStatus === 'fetching' (buscando, pode ter cache)
```

---

## 📖 Queries (Leitura)

### Query Básica

```typescript
// services/userService.ts
export interface User {
  id: number;
  name: string;
  email: string;
}

export const userService = {
  getUsers: async (): Promise<User[]> => {
    const res = await fetch('/api/users');
    if (!res.ok) throw new Error('Falha ao buscar usuários');
    return res.json();
  },
};

// hooks/useUsers.ts
import { useQuery } from '@tanstack/react-query';
import { userService } from '../services/userService';

export const useUsers = () => {
  return useQuery({
    queryKey: ['users'],
    queryFn: userService.getUsers,
    
    // Opções adicionais
    staleTime: 60 * 1000,         // 1min
    gcTime: 5 * 60 * 1000,        // 5min
    retry: 3,                     // Tentar 3x
    retryDelay: attemptIndex =>   // Delay exponencial
      Math.min(1000 * 2 ** attemptIndex, 30000),
  });
};

// components/UserList.tsx
import { useUsers } from '../hooks/useUsers';

export function UserList() {
  const { data: users, isLoading, error } = useUsers();

  if (isLoading) return <div>Carregando usuários...</div>;
  if (error) return <div>Erro: {error.message}</div>;

  return (
    <ul>
      {users?.map(user => (
        <li key={user.id}>{user.name} - {user.email}</li>
      ))}
    </ul>
  );
}
```

### Query com Parâmetros

```typescript
// services/userService.ts
export interface UserFilters {
  page?: number;
  limit?: number;
  search?: string;
}

export const userService = {
  getUsers: async (filters?: UserFilters): Promise<User[]> => {
    const params = new URLSearchParams(filters as any);
    const res = await fetch(`/api/users?${params}`);
    if (!res.ok) throw new Error('Erro ao buscar');
    return res.json();
  },
};

// hooks/useUsers.ts
export const useUsers = (filters?: UserFilters) => {
  return useQuery({
    // IMPORTANTE: Query key DEVE incluir os filtros
    // Assim, cada combinação de filtros tem seu próprio cache
    queryKey: ['users', filters],
    queryFn: () => userService.getUsers(filters),
  });
};

// components/UserList.tsx
export function UserList() {
  const [page, setPage] = useState(1);
  const { data: users, isLoading } = useUsers({ page, limit: 10 });

  // Ao mudar page, nova query é disparada automaticamente
  return (
    <div>
      <ul>
        {users?.map(user => <li key={user.id}>{user.name}</li>)}
      </ul>
      <button onClick={() => setPage(p => p - 1)} disabled={page === 1}>
        Anterior
      </button>
      <button onClick={() => setPage(p => p + 1)}>
        Próxima
      </button>
    </div>
  );
}
```

### Query Condicional (Enabled)

```typescript
// Hook para detalhes de usuário
export const useUserDetails = (userId?: number) => {
  return useQuery({
    queryKey: ['users', 'detail', userId],
    queryFn: () => fetch(`/api/users/${userId}`).then(res => res.json()),
    
    // ENABLED: Só executa query se userId existir
    enabled: !!userId,
  });
};

// Uso
function UserProfile({ userId }: { userId?: number }) {
  // Se userId for undefined, query não executa
  const { data: user } = useUserDetails(userId);

  if (!userId) return <div>Selecione um usuário</div>;
  if (!user) return <div>Carregando...</div>;

  return <div>{user.name}</div>;
}
```

### Queries Dependentes

```typescript
// Query 1: Buscar detalhes do usuário
const { data: user } = useQuery({
  queryKey: ['user', userId],
  queryFn: () => fetch(`/api/users/${userId}`).then(res => res.json()),
  enabled: !!userId,
});

// Query 2: Buscar posts do usuário (depende da Query 1)
const { data: posts } = useQuery({
  queryKey: ['posts', user?.id],
  queryFn: () => fetch(`/api/posts?userId=${user.id}`).then(res => res.json()),
  
  // Só executa SE user.id existir (Query 1 teve sucesso)
  enabled: !!user?.id,
});
```

---

## ✏️ Mutations (Escrita)

Mutations são para **criar, atualizar ou deletar** dados.

### Mutation Básica

```typescript
// services/userService.ts
export interface CreateUserDto {
  name: string;
  email: string;
  password: string;
}

export const userService = {
  createUser: async (data: CreateUserDto): Promise<User> => {
    const res = await fetch('/api/users', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
    });
    if (!res.ok) throw new Error('Erro ao criar usuário');
    return res.json();
  },
};

// hooks/useCreateUser.ts
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { userService } from '../services/userService';

export const useCreateUser = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: userService.createUser,
    
    // onSuccess: Executado após sucesso
    onSuccess: (newUser) => {
      // INVALIDAR cache: força refetch das queries de usuários
      queryClient.invalidateQueries({ queryKey: ['users'] });
      
      // Ou ATUALIZAR cache diretamente (sem refetch)
      // queryClient.setQueryData<User[]>(['users'], (old) => 
      //   old ? [...old, newUser] : [newUser]
      // );
    },
    
    // onError: Executado após erro
    onError: (error) => {
      console.error('Erro ao criar usuário:', error);
    },
  });
};

// components/CreateUserForm.tsx
import { useCreateUser } from '../hooks/useCreateUser';

export function CreateUserForm() {
  const createUser = useCreateUser();
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    
    createUser.mutate(
      { name, email, password: '123456' },
      {
        // Callbacks locais (sobrescrevem os globais)
        onSuccess: () => {
          setName('');
          setEmail('');
          alert('Usuário criado!');
        },
        onError: (error) => {
          alert(`Erro: ${error.message}`);
        },
      }
    );
  };

  return (
    <form onSubmit={handleSubmit}>
      <input 
        value={name} 
        onChange={e => setName(e.target.value)} 
        placeholder="Nome" 
      />
      <input 
        value={email} 
        onChange={e => setEmail(e.target.value)} 
        placeholder="Email" 
      />
      <button type="submit" disabled={createUser.isPending}>
        {createUser.isPending ? 'Criando...' : 'Criar Usuário'}
      </button>
      {createUser.isError && (
        <div className="error">{createUser.error.message}</div>
      )}
    </form>
  );
}
```

### Mutation de Update

```typescript
// hooks/useUpdateUser.ts
interface UpdateUserDto {
  id: number;
  data: Partial<User>;
}

export const useUpdateUser = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ id, data }: UpdateUserDto) =>
      fetch(`/api/users/${id}`, {
        method: 'PATCH',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data),
      }).then(res => res.json()),

    onSuccess: (updatedUser) => {
      // Invalidar lista
      queryClient.invalidateQueries({ queryKey: ['users'] });
      
      // Invalidar detalhes do usuário específico
      queryClient.invalidateQueries({ 
        queryKey: ['users', 'detail', updatedUser.id] 
      });
    },
  });
};

// Uso
const updateUser = useUpdateUser();

updateUser.mutate({
  id: 5,
  data: { name: 'Novo Nome' }
});
```

### Mutation de Delete

```typescript
// hooks/useDeleteUser.ts
export const useDeleteUser = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (userId: number) =>
      fetch(`/api/users/${userId}`, { method: 'DELETE' }),

    onSuccess: (_, deletedUserId) => {
      // _ = resposta (não usamos)
      // deletedUserId = variável passada para mutate()
      
      queryClient.invalidateQueries({ queryKey: ['users'] });
      
      // Remover do cache
      queryClient.removeQueries({ 
        queryKey: ['users', 'detail', deletedUserId] 
      });
    },
  });
};

// Uso
const deleteUser = useDeleteUser();

deleteUser.mutate(5); // Deleta usuário ID 5
```

---

## 🗄️ Cache & Invalidation

### Invalidar Queries

```typescript
const queryClient = useQueryClient();

// Invalidar TODAS queries de usuários
queryClient.invalidateQueries({ queryKey: ['users'] });

// Invalidar apenas query específica
queryClient.invalidateQueries({ queryKey: ['users', 'detail', 5] });

// Invalidar com filtro
queryClient.invalidateQueries({
  queryKey: ['users'],
  predicate: (query) => 
    query.queryKey[1] === 'list', // Só listas
});
```

### Atualizar Cache Manualmente

```typescript
// Ler do cache
const users = queryClient.getQueryData<User[]>(['users']);

// Escrever no cache
queryClient.setQueryData<User[]>(['users'], (oldUsers) => {
  if (!oldUsers) return [];
  return [...oldUsers, newUser];
});

// Atualizar parcialmente
queryClient.setQueryData<User>(['users', 'detail', 5], (oldUser) => {
  if (!oldUser) return oldUser;
  return { ...oldUser, name: 'Novo Nome' };
});
```

### Remover do Cache

```typescript
// Remover query específica
queryClient.removeQueries({ queryKey: ['users', 'detail', 5] });

// Remover todas queries de usuários
queryClient.removeQueries({ queryKey: ['users'] });

// Limpar TODO o cache
queryClient.clear();
```

---

## ♾️ Infinite Queries

Para **paginação infinita** (scroll infinito).

```typescript
// services/postService.ts
export interface PostsResponse {
  data: Post[];
  meta: {
    currentPage: number;
    totalPages: number;
  };
}

export const postService = {
  getPosts: async (page = 1): Promise<PostsResponse> => {
    const res = await fetch(`/api/posts?page=${page}&limit=10`);
    return res.json();
  },
};

// hooks/usePosts.ts
import { useInfiniteQuery } from '@tanstack/react-query';

export const usePosts = () => {
  return useInfiniteQuery({
    queryKey: ['posts'],
    
    // pageParam: número da página (inicial = 1)
    queryFn: ({ pageParam }) => postService.getPosts(pageParam),
    
    initialPageParam: 1, // Página inicial
    
    // Retorna próxima página OU undefined (fim)
    getNextPageParam: (lastPage) => {
      const { currentPage, totalPages } = lastPage.meta;
      return currentPage < totalPages ? currentPage + 1 : undefined;
    },
    
    // Opcional: página anterior
    getPreviousPageParam: (firstPage) => {
      const { currentPage } = firstPage.meta;
      return currentPage > 1 ? currentPage - 1 : undefined;
    },
  });
};

// components/InfinitePostList.tsx
import { usePosts } from '../hooks/usePosts';

export function InfinitePostList() {
  const {
    data,              // { pages: [...], pageParams: [...] }
    fetchNextPage,     // Função para carregar próxima página
    hasNextPage,       // Boolean: tem próxima página?
    isFetchingNextPage, // Boolean: carregando próxima?
    status,
  } = usePosts();

  if (status === 'pending') return <div>Carregando...</div>;
  if (status === 'error') return <div>Erro</div>;

  return (
    <div>
      {/* data.pages = array de páginas */}
      {data.pages.map((page, i) => (
        <div key={i}>
          {page.data.map(post => (
            <div key={post.id}>
              <h3>{post.title}</h3>
              <p>{post.body}</p>
            </div>
          ))}
        </div>
      ))}

      <button
        onClick={() => fetchNextPage()}
        disabled={!hasNextPage || isFetchingNextPage}
      >
        {isFetchingNextPage
          ? 'Carregando...'
          : hasNextPage
          ? 'Carregar Mais'
          : 'Fim'}
      </button>
    </div>
  );
}
```

---

## ⚡ Prefetching

**Prefetch**: Carregar dados **antes** de serem necessários.

```typescript
import { useQueryClient } from '@tanstack/react-query';

function UserList() {
  const queryClient = useQueryClient();
  const [page, setPage] = useState(1);

  // Prefetch ao passar mouse no botão "Próxima"
  const prefetchNextPage = () => {
    queryClient.prefetchQuery({
      queryKey: ['users', { page: page + 1 }],
      queryFn: () => userService.getUsers({ page: page + 1 }),
    });
  };

  return (
    <div>
      {/* Lista de usuários */}
      
      <button
        onClick={() => setPage(p => p + 1)}
        onMouseEnter={prefetchNextPage} // Prefetch no hover
      >
        Próxima
      </button>
    </div>
  );
}
```

### Prefetch no Next.js (SSR)

```tsx
// app/users/page.tsx
import { dehydrate, HydrationBoundary, QueryClient } from '@tanstack/react-query';
import { UserList } from './UserList';
import { userService } from '@/services/userService';

export default async function UsersPage() {
  const queryClient = new QueryClient();

  // Prefetch no servidor
  await queryClient.prefetchQuery({
    queryKey: ['users'],
    queryFn: userService.getUsers,
  });

  return (
    <HydrationBoundary state={dehydrate(queryClient)}>
      <UserList />
    </HydrationBoundary>
  );
}

// UserList.tsx (Client Component)
'use client';

export function UserList() {
  // Dados já vêm do servidor (sem loading inicial)
  const { data: users } = useQuery({
    queryKey: ['users'],
    queryFn: userService.getUsers,
  });

  return <ul>{users?.map(u => <li key={u.id}>{u.name}</li>)}</ul>;
}
```

---

## 🚀 Optimistic Updates

Atualizar UI **antes** da resposta da API (UX instantânea).

```typescript
// hooks/useUpdateTodo.ts
interface Todo {
  id: number;
  title: string;
  completed: boolean;
}

export const useUpdateTodo = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (updatedTodo: Todo) =>
      fetch(`/api/todos/${updatedTodo.id}`, {
        method: 'PATCH',
        body: JSON.stringify(updatedTodo),
      }).then(res => res.json()),

    // onMutate: ANTES da requisição
    onMutate: async (updatedTodo) => {
      // 1. Cancelar refetches em andamento
      await queryClient.cancelQueries({ queryKey: ['todos'] });

      // 2. Snapshot do estado anterior (para rollback)
      const previousTodos = queryClient.getQueryData<Todo[]>(['todos']);

      // 3. Atualizar cache otimisticamente
      queryClient.setQueryData<Todo[]>(['todos'], (old) =>
        old?.map(todo =>
          todo.id === updatedTodo.id ? updatedTodo : todo
        )
      );

      // 4. Retornar contexto para usar no onError
      return { previousTodos };
    },

    // onError: Reverter se der erro
    onError: (err, newTodo, context) => {
      // Restaurar snapshot
      queryClient.setQueryData(['todos'], context?.previousTodos);
      console.error('Erro ao atualizar:', err);
    },

    // onSettled: Sempre executado (sucesso ou erro)
    onSettled: () => {
      // Refetch para garantir consistência
      queryClient.invalidateQueries({ queryKey: ['todos'] });
    },
  });
};

// Uso
const updateTodo = useUpdateTodo();

// UI atualiza instantaneamente, depois sincroniza com API
updateTodo.mutate({ 
  id: 1, 
  title: 'Updated', 
  completed: true 
});
```

---

## 📁 Estrutura de Projeto

### Organização Recomendada

```
src/
├── api/
│   ├── client.ts              # Axios/Fetch configurado
│   └── endpoints.ts           # Constantes de URLs
│
├── services/                  # Lógica de API
│   ├── userService.ts
│   ├── postService.ts
│   └── types.ts               # TypeScript types
│
├── hooks/                     # React Query hooks
│   ├── queries/
│   │   ├── useUsers.ts
│   │   ├── usePosts.ts
│   │   └── keys.ts            # Query key factory
│   └── mutations/
│       ├── useCreateUser.ts
│       └── useUpdatePost.ts
│
└── components/
    ├── UserList.tsx
    └── CreateUserForm.tsx
```

### Query Key Factory

```typescript
// hooks/queries/keys.ts

// ✅ Centralizar todas as query keys
export const queryKeys = {
  users: {
    all: ['users'] as const,
    lists: () => [...queryKeys.users.all, 'list'] as const,
    list: (filters: object) => [...queryKeys.users.lists(), filters] as const,
    details: () => [...queryKeys.users.all, 'detail'] as const,
    detail: (id: number) => [...queryKeys.users.details(), id] as const,
  },
  posts: {
    all: ['posts'] as const,
    lists: () => [...queryKeys.posts.all, 'list'] as const,
    list: (filters: object) => [...queryKeys.posts.lists(), filters] as const,
    details: () => [...queryKeys.posts.all, 'detail'] as const,
    detail: (id: number) => [...queryKeys.posts.details(), id] as const,
  },
};

// Uso
useQuery({ queryKey: queryKeys.users.list({ page: 1 }), ... });
useQuery({ queryKey: queryKeys.users.detail(5), ... });

// Invalidar todos os usuários
queryClient.invalidateQueries({ queryKey: queryKeys.users.all });

// Invalidar apenas listas
queryClient.invalidateQueries({ queryKey: queryKeys.users.lists() });
```

### API Client (Axios)

```typescript
// api/client.ts
import axios from 'axios';

export const apiClient = axios.create({
  baseURL: import.meta.env.VITE_API_URL || '/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

// Interceptor: Adicionar token
apiClient.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Interceptor: Tratamento de erros
apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      // Redirecionar para login
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);
```

---

## ✅ Best Practices

### 1. Configure staleTime Apropriadamente

```typescript
// ❌ Ruim: staleTime padrão (0) = sempre refetch
useQuery({ queryKey: ['users'], queryFn: fetchUsers });

// ✅ Bom: Define staleTime baseado na volatilidade dos dados
const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      // Dados estáticos (países, categorias)
      staleTime: 24 * 60 * 60 * 1000, // 24h

      // Dados voláteis (notificações, cotações)
      staleTime: 5 * 1000, // 5s
    },
  },
});
```

### 2. Use Query Keys Estruturadas

```typescript
// ❌ Ruim: Flat keys
['users']
['userDetails']
['userPosts']

// ✅ Bom: Hierárquicas
['users', 'list']
['users', 'detail', 5]
['users', 'posts', 5]

// Permite invalidar com precisão
queryClient.invalidateQueries({ queryKey: ['users'] }); // Todas
queryClient.invalidateQueries({ queryKey: ['users', 'list'] }); // Só listas
```

### 3. Evite Over-fetching com Enabled

```typescript
// ❌ Ruim: Query sempre executa
const { data } = useUserDetails(userId);

// ✅ Bom: Query só executa quando necessário
const { data } = useQuery({
  queryKey: ['users', userId],
  queryFn: () => fetchUser(userId!),
  enabled: !!userId, // Só se userId existir
});
```

### 4. Use Optimistic Updates com Cuidado

```typescript
// ✅ Bom para: Toggle de checkbox, like, etc.
const toggleTodo = useMutation({
  mutationFn: updateTodo,
  onMutate: async (newTodo) => {
    // Atualizar cache otimisticamente
    queryClient.setQueryData(['todos'], (old) => ...);
  },
  onError: (err, newTodo, context) => {
    // Rollback em caso de erro
    queryClient.setQueryData(['todos'], context?.previousTodos);
  },
});

// ⚠️ Evitar para: Operações críticas (pagamentos, etc.)
```

### 5. Prefetch Dados Previsíveis

```typescript
// ✅ Prefetch ao hover (UX instantânea)
<Link
  to="/user/5"
  onMouseEnter={() =>
    queryClient.prefetchQuery({
      queryKey: ['users', 5],
      queryFn: () => fetchUser(5),
    })
  }
>
  Ver Perfil
</Link>
```

### 6. Configure Retry Inteligente

```typescript
// ✅ Não retry erros 4xx (cliente)
const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: (failureCount, error) => {
        // Não retry para erros 4xx
        if (error.response?.status >= 400 && error.response?.status < 500) {
          return false;
        }
        // Retry até 3x para erros 5xx
        return failureCount < 3;
      },
    },
  },
});
```

### 7. Use DevTools em Desenvolvimento

```tsx
import { ReactQueryDevtools } from '@tanstack/react-query-devtools';

<QueryClientProvider client={queryClient}>
  <App />
  {/* Ctrl+Shift+D para abrir */}
  {process.env.NODE_ENV === 'development' && (
    <ReactQueryDevtools initialIsOpen={false} />
  )}
</QueryClientProvider>
```

### 8. Type Safety com TypeScript

```typescript
// ✅ Sempre tipar queries e mutations
interface User {
  id: number;
  name: string;
}

const useUsers = () => {
  return useQuery<User[], Error>({
    //            ^data  ^error
    queryKey: ['users'],
    queryFn: fetchUsers,
  });
};

const useCreateUser = () => {
  return useMutation<User, Error, CreateUserDto>({
    //               ^data ^error ^variables
    mutationFn: createUser,
  });
};
```

---

## 🎯 Exemplo Completo: CRUD de TODOs

```typescript
// types/todo.ts
export interface Todo {
  id: number;
  title: string;
  completed: boolean;
}

export interface CreateTodoDto {
  title: string;
}

// services/todoService.ts
import { apiClient } from '../api/client';
import { Todo, CreateTodoDto } from '../types/todo';

export const todoService = {
  getTodos: async (): Promise<Todo[]> => {
    const { data } = await apiClient.get('/todos');
    return data;
  },

  createTodo: async (dto: CreateTodoDto): Promise<Todo> => {
    const { data } = await apiClient.post('/todos', dto);
    return data;
  },

  updateTodo: async (todo: Todo): Promise<Todo> => {
    const { data } = await apiClient.put(`/todos/${todo.id}`, todo);
    return data;
  },

  deleteTodo: async (id: number): Promise<void> => {
    await apiClient.delete(`/todos/${id}`);
  },
};

// hooks/queries/useTodos.ts
import { useQuery } from '@tanstack/react-query';
import { todoService } from '../../services/todoService';

export const useTodos = () => {
  return useQuery({
    queryKey: ['todos'],
    queryFn: todoService.getTodos,
    staleTime: 30 * 1000, // 30s
  });
};

// hooks/mutations/useCreateTodo.ts
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { todoService } from '../../services/todoService';

export const useCreateTodo = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: todoService.createTodo,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['todos'] });
    },
  });
};

// hooks/mutations/useUpdateTodo.ts
export const useUpdateTodo = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: todoService.updateTodo,
    
    // Optimistic update
    onMutate: async (updatedTodo) => {
      await queryClient.cancelQueries({ queryKey: ['todos'] });
      
      const previousTodos = queryClient.getQueryData<Todo[]>(['todos']);
      
      queryClient.setQueryData<Todo[]>(['todos'], (old) =>
        old?.map(todo => todo.id === updatedTodo.id ? updatedTodo : todo)
      );
      
      return { previousTodos };
    },
    
    onError: (err, newTodo, context) => {
      queryClient.setQueryData(['todos'], context?.previousTodos);
    },
    
    onSettled: () => {
      queryClient.invalidateQueries({ queryKey: ['todos'] });
    },
  });
};

// components/TodoList.tsx
import { useTodos } from '../hooks/queries/useTodos';
import { useUpdateTodo } from '../hooks/mutations/useUpdateTodo';

export function TodoList() {
  const { data: todos, isLoading } = useTodos();
  const updateTodo = useUpdateTodo();

  const toggleTodo = (todo: Todo) => {
    updateTodo.mutate({ ...todo, completed: !todo.completed });
  };

  if (isLoading) return <div>Carregando...</div>;

  return (
    <ul>
      {todos?.map(todo => (
        <li key={todo.id}>
          <input
            type="checkbox"
            checked={todo.completed}
            onChange={() => toggleTodo(todo)}
          />
          <span style={{ textDecoration: todo.completed ? 'line-through' : 'none' }}>
            {todo.title}
          </span>
        </li>
      ))}
    </ul>
  );
}
```

---

## 📚 Recursos Adicionais

### Documentação
- [TanStack Query Docs](https://tanstack.com/query/latest/docs/react/overview)
- [React Query v5 Migration Guide](https://tanstack.com/query/latest/docs/react/guides/migrating-to-v5)

### Ferramentas
- **React Query DevTools** - Debug visual
- **ESLint Plugin** - `@tanstack/eslint-plugin-query`

### Comunidade
- [GitHub Discussions](https://github.com/TanStack/query/discussions)
- [Discord](https://discord.com/invite/WrRKjPJ)

---

**Voltar para**: [📁 Libraries](../../../README.md) | [📁 JavaScript](../../../../README.md) | [📁 Learning](../../../../../README.md) | [📁 Repositório Principal](../../../../../../README.md)
