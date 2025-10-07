# ▲ **Next.js 14/15 - App Router & Server Components**

## 📋 **Índice**

1. [Introdução ao Next.js](#1-introdução-ao-nextjs)
2. [App Router vs Pages Router](#2-app-router-vs-pages-router)
3. [File-based Routing](#3-file-based-routing)
4. [Server Components](#4-server-components)
5. [Client Components](#5-client-components)
6. [Server Actions](#6-server-actions)
7. [Data Fetching](#7-data-fetching)
8. [Caching](#8-caching)
9. [Streaming & Suspense](#9-streaming--suspense)
10. [Layouts & Templates](#10-layouts--templates)
11. [Route Handlers (API Routes)](#11-route-handlers-api-routes)
12. [Metadata & SEO](#12-metadata--seo)
13. [Middleware](#13-middleware)
14. [Deployment](#14-deployment)
15. [Recursos e Referências](#15-recursos-e-referências)

---

## 1. 🎯 **Introdução ao Next.js**

### 1.1. O que é Next.js?

**Next.js** é um framework React com recursos full-stack:

```tsx
// ═══════════════════════════════════════════════════════════
// NEXT.JS FEATURES
// ═══════════════════════════════════════════════════════════

/**
 * ✅ Server-Side Rendering (SSR)
 * ✅ Static Site Generation (SSG)
 * ✅ Incremental Static Regeneration (ISR)
 * ✅ API Routes (Backend)
 * ✅ File-based Routing
 * ✅ Image Optimization
 * ✅ Font Optimization
 * ✅ TypeScript Support
 * ✅ CSS Modules / Tailwind
 * ✅ React Server Components
 * ✅ Server Actions
 * ✅ Streaming SSR
 */
```

### 1.2. Next.js 14/15 Features

```tsx
// ═══════════════════════════════════════════════════════════
// NEXT.JS 14/15 NEW FEATURES
// ═══════════════════════════════════════════════════════════

/**
 * Next.js 14 (Oct 2023):
 * ✅ Turbopack (dev server rápido)
 * ✅ Server Actions (stable)
 * ✅ Partial Prerendering (preview)
 * ✅ Metadata improvements
 * 
 * Next.js 15 (Oct 2024):
 * ✅ React 19 support
 * ✅ Partial Prerendering (stable)
 * ✅ Enhanced caching
 * ✅ Improved error handling
 * ✅ Better TypeScript support
 */
```

### 1.3. Instalação

```bash
# ═══════════════════════════════════════════════════════════
# CRIAR PROJETO NEXT.JS
# ═══════════════════════════════════════════════════════════

# Create Next.js app (latest)
npx create-next-app@latest my-app

# Opções:
# ✅ TypeScript? Yes
# ✅ ESLint? Yes
# ✅ Tailwind CSS? Yes
# ✅ src/ directory? Yes (recomendado)
# ✅ App Router? Yes
# ✅ Import alias? @/* (default)

cd my-app
npm run dev
```

### 1.4. Estrutura de Pastas

```
my-app/
├── src/
│   ├── app/                    # ✅ App Router (Next.js 13+)
│   │   ├── layout.tsx          # Root layout
│   │   ├── page.tsx            # Home page (/)
│   │   ├── loading.tsx         # Loading UI
│   │   ├── error.tsx           # Error UI
│   │   ├── not-found.tsx       # 404 page
│   │   ├── global.css          # Global styles
│   │   │
│   │   ├── about/
│   │   │   └── page.tsx        # /about
│   │   │
│   │   ├── blog/
│   │   │   ├── page.tsx        # /blog
│   │   │   └── [slug]/
│   │   │       └── page.tsx    # /blog/[slug]
│   │   │
│   │   └── api/                # API Routes
│   │       └── users/
│   │           └── route.ts    # /api/users
│   │
│   ├── components/             # React components
│   │   ├── ui/
│   │   └── features/
│   │
│   ├── lib/                    # Utilities
│   │   ├── db.ts
│   │   └── utils.ts
│   │
│   └── types/                  # TypeScript types
│       └── index.ts
│
├── public/                     # Static assets
│   ├── images/
│   └── fonts/
│
├── next.config.js              # Next.js config
├── tailwind.config.ts          # Tailwind config
├── tsconfig.json               # TypeScript config
└── package.json
```

### 1.5. next.config.js

```js
// ═══════════════════════════════════════════════════════════
// NEXT.JS CONFIGURATION
// ═══════════════════════════════════════════════════════════

/** @type {import('next').NextConfig} */
const nextConfig = {
  // ✅ Experimental features
  experimental: {
    // Server Actions
    serverActions: {
      bodySizeLimit: '2mb'
    },
    
    // Partial Prerendering
    ppr: true,
    
    // Typed routes (Next.js 15)
    typedRoutes: true
  },

  // ✅ Image optimization
  images: {
    remotePatterns: [
      {
        protocol: 'https',
        hostname: 'images.unsplash.com'
      }
    ]
  },

  // ✅ Redirects
  async redirects() {
    return [
      {
        source: '/old-blog/:slug',
        destination: '/blog/:slug',
        permanent: true
      }
    ];
  },

  // ✅ Rewrites
  async rewrites() {
    return [
      {
        source: '/api/v1/:path*',
        destination: 'https://api.example.com/:path*'
      }
    ];
  }
};

module.exports = nextConfig;
```

---

## 2. 🔄 **App Router vs Pages Router**

### 2.1. Comparação

```tsx
// ═══════════════════════════════════════════════════════════
// PAGES ROUTER (Next.js ≤12) - Legacy
// ═══════════════════════════════════════════════════════════

// pages/index.tsx
export default function Home() {
  return <div>Home</div>;
}

// pages/blog/[slug].tsx
export async function getServerSideProps({ params }) {
  const post = await fetchPost(params.slug);
  return { props: { post } };
}

export default function BlogPost({ post }) {
  return <div>{post.title}</div>;
}

// ❌ Problemas:
// - getServerSideProps/getStaticProps confusos
// - Cliente + servidor no mesmo arquivo
// - Sem streaming
// - Sem React Server Components

// ═══════════════════════════════════════════════════════════
// APP ROUTER (Next.js 13+) - Moderno
// ═══════════════════════════════════════════════════════════

// app/page.tsx
export default function Home() {
  return <div>Home</div>;
}

// app/blog/[slug]/page.tsx
async function BlogPost({ params }: { params: { slug: string } }) {
  // ✅ Fetch direto no componente
  const post = await fetchPost(params.slug);
  
  return <div>{post.title}</div>;
}

// ✅ Benefícios:
// - Server Components (default)
// - Streaming + Suspense
// - Layouts compartilhados
// - Loading/Error states
// - Parallel routes
// - Intercepting routes
```

### 2.2. Migration Guide

```tsx
// ═══════════════════════════════════════════════════════════
// MIGRAÇÃO: Pages → App Router
// ═══════════════════════════════════════════════════════════

// ANTES (Pages Router)
// pages/blog/[slug].tsx

import { GetServerSideProps } from 'next';

interface Props {
  post: Post;
}

export const getServerSideProps: GetServerSideProps<Props> = async ({ params }) => {
  const post = await fetchPost(params!.slug as string);
  return { props: { post } };
};

export default function BlogPost({ post }: Props) {
  return <div>{post.title}</div>;
}

// DEPOIS (App Router)
// app/blog/[slug]/page.tsx

interface Props {
  params: { slug: string };
}

export default async function BlogPost({ params }: Props) {
  // ✅ Fetch direto
  const post = await fetchPost(params.slug);
  
  return <div>{post.title}</div>;
}

// ────────────────────────────────────────────────────────────

// ANTES: getStaticProps
export const getStaticProps = async () => {
  const posts = await fetchPosts();
  return { props: { posts }, revalidate: 60 };
};

// DEPOIS: Server Component + revalidate
export const revalidate = 60;

export default async function BlogList() {
  const posts = await fetchPosts();
  return <div>...</div>;
}

// ────────────────────────────────────────────────────────────

// ANTES: API Route (pages/api/users.ts)
export default async function handler(req, res) {
  const users = await db.users.findMany();
  res.json(users);
}

// DEPOIS: Route Handler (app/api/users/route.ts)
export async function GET() {
  const users = await db.users.findMany();
  return Response.json(users);
}
```

---

## 3. 🗂️ **File-based Routing**

### 3.1. Convenções de Arquivos

```tsx
// ═══════════════════════════════════════════════════════════
// FILE CONVENTIONS
// ═══════════════════════════════════════════════════════════

/**
 * page.tsx         → Rota pública (/page)
 * layout.tsx       → Layout compartilhado
 * loading.tsx      → Loading UI (Suspense)
 * error.tsx        → Error UI (Error Boundary)
 * not-found.tsx    → 404 page
 * template.tsx     → Re-render em navegação
 * route.ts         → API endpoint
 * default.tsx      → Parallel routes fallback
 */

// Exemplos:

// app/page.tsx              → /
// app/about/page.tsx        → /about
// app/blog/page.tsx         → /blog
// app/blog/[slug]/page.tsx  → /blog/:slug
```

### 3.2. Dynamic Routes

```tsx
// ═══════════════════════════════════════════════════════════
// DYNAMIC ROUTES
// ═══════════════════════════════════════════════════════════

// ✅ [slug] - Single dynamic segment
// app/blog/[slug]/page.tsx → /blog/hello-world

interface Props {
  params: { slug: string };
}

export default async function BlogPost({ params }: Props) {
  const post = await fetchPost(params.slug);
  return <div>{post.title}</div>;
}

// ────────────────────────────────────────────────────────────

// ✅ [...slug] - Catch-all segments
// app/shop/[...slug]/page.tsx

// Matches:
// /shop/clothes
// /shop/clothes/tops
// /shop/clothes/tops/t-shirts

interface Props {
  params: { slug: string[] };
}

export default function Shop({ params }: Props) {
  const path = params.slug.join('/');
  return <div>Category: {path}</div>;
}

// ────────────────────────────────────────────────────────────

// ✅ [[...slug]] - Optional catch-all
// app/docs/[[...slug]]/page.tsx

// Matches:
// /docs
// /docs/getting-started
// /docs/api/reference

export default function Docs({ params }: Props) {
  const sections = params.slug || [];
  return <div>Docs: {sections.join(' > ')}</div>;
}
```

### 3.3. Route Groups

```tsx
// ═══════════════════════════════════════════════════════════
// ROUTE GROUPS: (folder) não aparece na URL
// ═══════════════════════════════════════════════════════════

/**
 * app/
 * ├── (marketing)/          ← Grupo (não afeta URL)
 * │   ├── layout.tsx        ← Layout para marketing
 * │   ├── page.tsx          → /
 * │   ├── about/page.tsx    → /about
 * │   └── pricing/page.tsx  → /pricing
 * │
 * ├── (shop)/
 * │   ├── layout.tsx        ← Layout para shop
 * │   ├── products/page.tsx → /products
 * │   └── cart/page.tsx     → /cart
 * │
 * └── (dashboard)/
 *     ├── layout.tsx        ← Layout para dashboard
 *     └── settings/page.tsx → /settings
 */

// ✅ Benefícios:
// - Múltiplos layouts sem afetar URLs
// - Organização lógica
// - Shared layouts por seção
```

### 3.4. Parallel Routes

```tsx
// ═══════════════════════════════════════════════════════════
// PARALLEL ROUTES: @folder
// ═══════════════════════════════════════════════════════════

/**
 * app/
 * ├── layout.tsx
 * ├── @team/
 * │   └── page.tsx
 * └── @analytics/
 *     └── page.tsx
 */

// app/layout.tsx
export default function Layout({
  children,
  team,
  analytics
}: {
  children: React.ReactNode;
  team: React.ReactNode;
  analytics: React.ReactNode;
}) {
  return (
    <div>
      <div>{children}</div>
      <div className="grid grid-cols-2">
        <div>{team}</div>
        <div>{analytics}</div>
      </div>
    </div>
  );
}

// ✅ Múltiplas rotas renderizadas simultaneamente
```

### 3.5. Intercepting Routes

```tsx
// ═══════════════════════════════════════════════════════════
// INTERCEPTING ROUTES: (.)folder
// ═══════════════════════════════════════════════════════════

/**
 * app/
 * ├── feed/
 * │   └── page.tsx           → /feed
 * └── photo/
 *     ├── [id]/
 *     │   └── page.tsx       → /photo/123
 *     └── (.)photo/
 *         └── [id]/
 *             └── page.tsx   → Intercepts /photo/123 from /feed
 */

/**
 * Modifiers:
 * (.)   → Same level
 * (..)  → One level up
 * (..)(..) → Two levels up
 * (...)    → Root
 */

// Uso: Modal com URL própria (Instagram-like)
```

---

## 4. 🖥️ **Server Components**

### 4.1. Server Component Básico

```tsx
// ═══════════════════════════════════════════════════════════
// SERVER COMPONENT (default)
// ═══════════════════════════════════════════════════════════

// app/page.tsx

async function HomePage() {
  // ✅ Fetch no servidor
  const posts = await fetch('https://api.example.com/posts').then(r => r.json());

  // ✅ Acesso direto ao DB
  const users = await db.users.findMany();

  // ✅ Acesso a filesystem
  const content = await fs.readFile('content.md', 'utf-8');

  return (
    <div>
      <h1>Posts</h1>
      {posts.map(post => (
        <article key={post.id}>
          <h2>{post.title}</h2>
        </article>
      ))}
    </div>
  );
}

export default HomePage;

/**
 * ✅ Benefícios:
 * - Zero JavaScript no cliente
 * - Acesso direto ao backend
 * - Secrets seguros (API keys, DB credentials)
 * - SEO perfeito
 * - Streaming + Suspense
 */
```

### 4.2. Server Component com Prisma

```tsx
// ═══════════════════════════════════════════════════════════
// SERVER COMPONENT + PRISMA
// ═══════════════════════════════════════════════════════════

// lib/db.ts
import { PrismaClient } from '@prisma/client';

const globalForPrisma = global as unknown as { prisma: PrismaClient };

export const prisma = globalForPrisma.prisma || new PrismaClient();

if (process.env.NODE_ENV !== 'production') globalForPrisma.prisma = prisma;

// app/posts/page.tsx
import { prisma } from '@/lib/db';

async function PostsPage() {
  // ✅ Query direto no componente
  const posts = await prisma.post.findMany({
    include: {
      author: true,
      comments: {
        take: 5
      }
    },
    orderBy: {
      createdAt: 'desc'
    }
  });

  return (
    <div>
      {posts.map(post => (
        <article key={post.id}>
          <h2>{post.title}</h2>
          <p>By {post.author.name}</p>
          <p>{post.comments.length} comments</p>
        </article>
      ))}
    </div>
  );
}

export default PostsPage;
```

### 4.3. Streaming com Suspense

```tsx
// ═══════════════════════════════════════════════════════════
// STREAMING: Progressive rendering
// ═══════════════════════════════════════════════════════════

import { Suspense } from 'react';

async function DashboardPage() {
  return (
    <div>
      <h1>Dashboard</h1>

      {/* ✅ Shell imediato */}
      <QuickStats />

      {/* ✅ Streamed (query lenta) */}
      <Suspense fallback={<RecentActivitySkeleton />}>
        <RecentActivity />
      </Suspense>

      {/* ✅ Streamed (query muito lenta) */}
      <Suspense fallback={<AnalyticsSkeleton />}>
        <Analytics />
      </Suspense>
    </div>
  );
}

// Componente lento
async function RecentActivity() {
  // ✅ Query lenta (5s)
  await new Promise(resolve => setTimeout(resolve, 5000));
  const activities = await prisma.activity.findMany({ take: 10 });

  return (
    <div>
      {activities.map(activity => (
        <div key={activity.id}>{activity.description}</div>
      ))}
    </div>
  );
}

/**
 * Timeline:
 * t=0ms:   Cliente vê shell + skeletons
 * t=5s:    RecentActivity aparece
 * t=10s:   Analytics aparece
 * 
 * ✅ Melhor que esperar 10s para TUDO!
 */
```

### 4.4. Server Component Composition

```tsx
// ═══════════════════════════════════════════════════════════
// COMPOSITION: Server + Client Components
// ═══════════════════════════════════════════════════════════

// app/page.tsx (Server Component)
import ClientButton from '@/components/ClientButton';

async function HomePage() {
  const user = await fetchUser();

  return (
    <div>
      <h1>Welcome, {user.name}</h1>
      
      {/* ✅ Client Component dentro de Server */}
      <ClientButton userId={user.id} />
      
      {/* ✅ Server Component */}
      <RecentPosts userId={user.id} />
    </div>
  );
}

// components/ClientButton.tsx (Client Component)
'use client';

export default function ClientButton({ userId }: { userId: number }) {
  const [likes, setLikes] = useState(0);

  return (
    <button onClick={() => setLikes(likes + 1)}>
      ❤️ {likes}
    </button>
  );
}
```

### 4.5. Data Fetching Patterns

```tsx
// ═══════════════════════════════════════════════════════════
// DATA FETCHING PATTERNS
// ═══════════════════════════════════════════════════════════

// ❌ BAD: Waterfall (sequencial)
async function BadPage() {
  const user = await fetchUser();
  const posts = await fetchPosts(user.id);  // Espera user
  const comments = await fetchComments();    // Espera posts
  
  return <div>...</div>;
}

// ✅ GOOD: Parallel fetching
async function GoodPage() {
  // ✅ Inicia todos fetches juntos
  const [user, posts, comments] = await Promise.all([
    fetchUser(),
    fetchPosts(),
    fetchComments()
  ]);

  return <div>...</div>;
}

// ✅ BEST: Streaming (não bloqueia)
async function BestPage() {
  return (
    <div>
      {/* ✅ Carrega imediatamente */}
      <Suspense fallback={<UserSkeleton />}>
        <User />
      </Suspense>

      {/* ✅ Carrega em paralelo */}
      <Suspense fallback={<PostsSkeleton />}>
        <Posts />
      </Suspense>

      <Suspense fallback={<CommentsSkeleton />}>
        <Comments />
      </Suspense>
    </div>
  );
}
```

---

## 5. 💻 **Client Components**

### 5.1. 'use client' Directive

```tsx
// ═══════════════════════════════════════════════════════════
// CLIENT COMPONENT
// ═══════════════════════════════════════════════════════════

'use client';  // ✅ Marca como Client Component

import { useState, useEffect } from 'react';

export default function Counter() {
  const [count, setCount] = useState(0);

  useEffect(() => {
    console.log('Count:', count);
  }, [count]);

  return (
    <button onClick={() => setCount(count + 1)}>
      Count: {count}
    </button>
  );
}

/**
 * Use Client Components para:
 * ✅ useState, useEffect, hooks
 * ✅ Event handlers (onClick, onChange)
 * ✅ Browser APIs (localStorage, window)
 * ✅ Interatividade
 * ✅ Animações
 * ✅ Third-party libraries (React-based)
 */
```

### 5.2. Quando usar Client Components

```tsx
// ═══════════════════════════════════════════════════════════
// DECISION TREE
// ═══════════════════════════════════════════════════════════

/**
 * Precisa de interatividade? (clicks, state, effects)
 *   ↓ SIM
 *   CLIENT COMPONENT ('use client')
 * 
 * Precisa de dados do servidor? (DB, API, filesystem)
 *   ↓ SIM
 *   SERVER COMPONENT (default)
 * 
 * Precisa de ambos?
 *   ↓ SIM
 *   SERVER COMPONENT (parent) + CLIENT COMPONENT (child)
 */

// ✅ Exemplo: Composição ideal

// app/posts/page.tsx (Server Component)
async function PostsPage() {
  const posts = await prisma.post.findMany();  // ✅ Server

  return (
    <div>
      {posts.map(post => (
        <PostCard key={post.id} post={post} />  {/* ✅ Client */}
      ))}
    </div>
  );
}

// components/PostCard.tsx (Client Component)
'use client';

export function PostCard({ post }: { post: Post }) {
  const [liked, setLiked] = useState(false);  // ✅ Interatividade

  return (
    <article>
      <h2>{post.title}</h2>
      <button onClick={() => setLiked(!liked)}>
        {liked ? '❤️' : '🤍'}
      </button>
    </article>
  );
}
```

### 5.3. Client Boundary

```tsx
// ═══════════════════════════════════════════════════════════
// CLIENT BOUNDARY
// ═══════════════════════════════════════════════════════════

// ❌ BAD: Tudo é Client Component
'use client';

export default function Page() {
  const [count, setCount] = useState(0);

  return (
    <div>
      <Header />                    {/* ❌ Client (desnecessário) */}
      <StaticContent />             {/* ❌ Client (desnecessário) */}
      <button onClick={...}>        {/* ✅ Precisa Client */}
        {count}
      </button>
    </div>
  );
}

// ✅ GOOD: Client boundary mínimo

// app/page.tsx (Server Component)
export default function Page() {
  return (
    <div>
      <Header />                    {/* ✅ Server */}
      <StaticContent />             {/* ✅ Server */}
      <Counter />                   {/* ✅ Client (isolado) */}
    </div>
  );
}

// components/Counter.tsx (Client Component)
'use client';

export function Counter() {
  const [count, setCount] = useState(0);
  
  return (
    <button onClick={() => setCount(count + 1)}>
      {count}
    </button>
  );
}
```

### 5.4. Passing Server Components to Client

```tsx
// ═══════════════════════════════════════════════════════════
// SERVER COMPONENT como CHILDREN de CLIENT
// ═══════════════════════════════════════════════════════════

// ❌ NÃO FUNCIONA
'use client';

export function ClientComponent() {
  return (
    <div>
      <ServerComponent />  {/* ❌ Erro! */}
    </div>
  );
}

// ✅ FUNCIONA: Passar como children/props
'use client';

export function ClientWrapper({ children }: { children: React.ReactNode }) {
  const [isOpen, setIsOpen] = useState(false);

  return (
    <div>
      <button onClick={() => setIsOpen(!isOpen)}>Toggle</button>
      {isOpen && children}  {/* ✅ Server Component via children */}
    </div>
  );
}

// app/page.tsx (Server Component)
export default function Page() {
  return (
    <ClientWrapper>
      {/* ✅ Server Component passado como children */}
      <ServerContent />
    </ClientWrapper>
  );
}

async function ServerContent() {
  const data = await fetchData();
  return <div>{data}</div>;
}
```

### 5.5. Third-Party Libraries

```tsx
// ═══════════════════════════════════════════════════════════
// THIRD-PARTY LIBRARIES em Client Components
// ═══════════════════════════════════════════════════════════

// components/MapClient.tsx
'use client';

import { MapContainer, TileLayer } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';

export function Map({ center }: { center: [number, number] }) {
  return (
    <MapContainer center={center} zoom={13}>
      <TileLayer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />
    </MapContainer>
  );
}

// app/location/page.tsx (Server Component)
import { Map } from '@/components/MapClient';

export default async function LocationPage() {
  const location = await fetchLocation();

  return (
    <div>
      <h1>Location</h1>
      {/* ✅ Client Component com biblioteca */}
      <Map center={[location.lat, location.lng]} />
    </div>
  );
}
```

---

## 6. 🎬 **Server Actions**

### 6.1. Server Action Básico

```tsx
// ═══════════════════════════════════════════════════════════
// SERVER ACTIONS
// ═══════════════════════════════════════════════════════════

// app/actions.ts
'use server';

import { revalidatePath } from 'next/cache';
import { redirect } from 'next/navigation';
import { prisma } from '@/lib/db';

export async function createPost(formData: FormData) {
  // ✅ Validação
  const title = formData.get('title') as string;
  const content = formData.get('content') as string;

  if (!title || !content) {
    return { error: 'Title and content are required' };
  }

  // ✅ Criar post
  const post = await prisma.post.create({
    data: { title, content }
  });

  // ✅ Revalidar cache
  revalidatePath('/posts');

  // ✅ Redirect
  redirect(`/posts/${post.id}`);
}

// app/posts/create/page.tsx
import { createPost } from '@/app/actions';

export default function CreatePostPage() {
  return (
    <form action={createPost}>
      <input name="title" placeholder="Title" required />
      <textarea name="content" placeholder="Content" required />
      <button type="submit">Create Post</button>
    </form>
  );
}

/**
 * ✅ Benefícios:
 * - Progressive enhancement (funciona sem JS)
 * - Type-safe
 * - Automatic serialization
 * - Revalidation integrada
 */
```

### 6.2. Server Action com Validação (Zod)

```tsx
// ═══════════════════════════════════════════════════════════
// SERVER ACTION + ZOD VALIDATION
// ═══════════════════════════════════════════════════════════

'use server';

import { z } from 'zod';
import { revalidatePath } from 'next/cache';

// ✅ Schema de validação
const createPostSchema = z.object({
  title: z.string().min(3).max(100),
  content: z.string().min(10),
  published: z.boolean().default(false)
});

export async function createPost(formData: FormData) {
  // ✅ Parse e valida
  const result = createPostSchema.safeParse({
    title: formData.get('title'),
    content: formData.get('content'),
    published: formData.get('published') === 'on'
  });

  if (!result.success) {
    return {
      error: 'Validation failed',
      issues: result.error.flatten().fieldErrors
    };
  }

  // ✅ Tipo garantido
  const { title, content, published } = result.data;

  const post = await prisma.post.create({
    data: { title, content, published }
  });

  revalidatePath('/posts');

  return { success: true, post };
}
```

### 6.3. useFormStatus Hook

```tsx
// ═══════════════════════════════════════════════════════════
// useFormStatus: Loading state em forms
// ═══════════════════════════════════════════════════════════

'use client';

import { useFormStatus } from 'react-dom';

export function SubmitButton() {
  const { pending } = useFormStatus();

  return (
    <button type="submit" disabled={pending}>
      {pending ? 'Creating...' : 'Create Post'}
    </button>
  );
}

// app/posts/create/page.tsx
import { createPost } from '@/app/actions';
import { SubmitButton } from '@/components/SubmitButton';

export default function CreatePostPage() {
  return (
    <form action={createPost}>
      <input name="title" />
      <textarea name="content" />
      {/* ✅ Button mostra loading state */}
      <SubmitButton />
    </form>
  );
}
```

### 6.4. useFormState Hook

```tsx
// ═══════════════════════════════════════════════════════════
// useFormState: Form state management
// ═══════════════════════════════════════════════════════════

'use server';

// Action com state
export async function createPost(prevState: any, formData: FormData) {
  const title = formData.get('title') as string;

  if (!title) {
    return { error: 'Title is required' };
  }

  const post = await prisma.post.create({
    data: { title, content: formData.get('content') as string }
  });

  return { success: true, post };
}

// ────────────────────────────────────────────────────────────

'use client';

import { useFormState } from 'react-dom';
import { createPost } from '@/app/actions';

export function CreatePostForm() {
  const [state, formAction] = useFormState(createPost, { error: null });

  return (
    <form action={formAction}>
      <input name="title" />
      <textarea name="content" />
      <button type="submit">Create</button>

      {/* ✅ Mostra erro */}
      {state?.error && <p className="error">{state.error}</p>}

      {/* ✅ Mostra sucesso */}
      {state?.success && <p className="success">Post created!</p>}
    </form>
  );
}
```

### 6.5. Server Action Patterns

```tsx
// ═══════════════════════════════════════════════════════════
// SERVER ACTION PATTERNS
// ═══════════════════════════════════════════════════════════

// ✅ 1. Inline Server Action
export default function Page() {
  async function handleSubmit(formData: FormData) {
    'use server';
    
    // Action code here
  }

  return <form action={handleSubmit}>...</form>;
}

// ✅ 2. Separate file
// app/actions.ts
'use server';

export async function createUser(formData: FormData) {
  // ...
}

// ✅ 3. Inside Server Component
async function UserForm() {
  async function handleSubmit(formData: FormData) {
    'use server';
    // ...
  }

  return <form action={handleSubmit}>...</form>;
}

// ✅ 4. With event handler (Client Component)
'use client';

import { createUser } from '@/app/actions';

export function UserForm() {
  const handleSubmit = async (formData: FormData) => {
    const result = await createUser(formData);
    
    if (result.error) {
      toast.error(result.error);
    } else {
      toast.success('User created!');
    }
  };

  return (
    <form action={handleSubmit}>
      {/* ... */}
    </form>
  );
}
```

---

## 7. 🌐 **Data Fetching**

### 7.1. Fetch API (Extended)

```tsx
// ═══════════════════════════════════════════════════════════
// NEXT.JS FETCH (Extended)
// ═══════════════════════════════════════════════════════════

// ✅ 1. Default: Cache indefinidamente
async function Page() {
  const data = await fetch('https://api.example.com/data');
  // ✅ Cached indefinidamente (até revalidate)
  
  return <div>{data.title}</div>;
}

// ✅ 2. Revalidate: ISR (Incremental Static Regeneration)
async function Page() {
  const data = await fetch('https://api.example.com/data', {
    next: { revalidate: 60 }  // ✅ Revalida a cada 60s
  });
  
  return <div>{data.title}</div>;
}

// ✅ 3. No cache: SSR (Server-Side Rendering)
async function Page() {
  const data = await fetch('https://api.example.com/data', {
    cache: 'no-store'  // ✅ Sempre fresh (SSR)
  });
  
  return <div>{data.title}</div>;
}

// ✅ 4. Force cache
async function Page() {
  const data = await fetch('https://api.example.com/data', {
    cache: 'force-cache'  // ✅ Cache permanente
  });
  
  return <div>{data.title}</div>;
}

// ✅ 5. Tags para revalidação
async function Page() {
  const data = await fetch('https://api.example.com/data', {
    next: { tags: ['posts'] }  // ✅ Tag para revalidar depois
  });
  
  return <div>{data.title}</div>;
}

// Revalidar por tag:
import { revalidateTag } from 'next/cache';

revalidateTag('posts');  // ✅ Revalida todos fetches com tag 'posts'
```

### 7.2. generateStaticParams (SSG)

```tsx
// ═══════════════════════════════════════════════════════════
// STATIC SITE GENERATION
// ═══════════════════════════════════════════════════════════

// app/blog/[slug]/page.tsx

interface Props {
  params: { slug: string };
}

// ✅ Gera páginas estáticas em build time
export async function generateStaticParams() {
  const posts = await prisma.post.findMany({
    select: { slug: true }
  });

  return posts.map(post => ({
    slug: post.slug
  }));
}

export default async function BlogPost({ params }: Props) {
  const post = await prisma.post.findUnique({
    where: { slug: params.slug }
  });

  if (!post) {
    notFound();
  }

  return (
    <article>
      <h1>{post.title}</h1>
      <div>{post.content}</div>
    </article>
  );
}

/**
 * Build time:
 * - Gera /blog/hello-world.html
 * - Gera /blog/nextjs-guide.html
 * - etc (uma página para cada slug)
 * 
 * ✅ Páginas estáticas (super rápidas!)
 */
```

### 7.3. Dynamic Params

```tsx
// ═══════════════════════════════════════════════════════════
// DYNAMIC PARAMS CONFIG
// ═══════════════════════════════════════════════════════════

// ✅ 1. dynamicParams = true (default)
export const dynamicParams = true;

// Comportamento:
// - Slugs em generateStaticParams → SSG
// - Slugs NÃO em generateStaticParams → SSR (on-demand)

// ✅ 2. dynamicParams = false
export const dynamicParams = false;

// Comportamento:
// - Slugs em generateStaticParams → SSG
// - Slugs NÃO em generateStaticParams → 404

// ────────────────────────────────────────────────────────────

// Exemplo completo
export const dynamicParams = false;

export async function generateStaticParams() {
  // ✅ Só gera essas páginas
  return [
    { slug: 'hello-world' },
    { slug: 'nextjs-guide' }
  ];
}

// Acesso:
// /blog/hello-world → ✅ Funciona (SSG)
// /blog/nextjs-guide → ✅ Funciona (SSG)
// /blog/outro-post → ❌ 404 (não gerado)
```

### 7.4. Revalidation

```tsx
// ═══════════════════════════════════════════════════════════
// REVALIDATION STRATEGIES
// ═══════════════════════════════════════════════════════════

// ✅ 1. Time-based revalidation
export const revalidate = 60;  // Revalida a cada 60s

async function Page() {
  const data = await fetchData();
  return <div>{data}</div>;
}

// ✅ 2. On-demand revalidation (Server Action)
'use server';

import { revalidatePath, revalidateTag } from 'next/cache';

export async function createPost(formData: FormData) {
  const post = await prisma.post.create({
    data: { title: formData.get('title') as string }
  });

  // ✅ Revalida path específico
  revalidatePath('/posts');
  
  // ✅ Revalida por tag
  revalidateTag('posts');
  
  return { success: true };
}

// ✅ 3. Route Handler revalidation
// app/api/revalidate/route.ts
import { revalidatePath } from 'next/cache';

export async function POST(request: Request) {
  const { path } = await request.json();
  
  revalidatePath(path);
  
  return Response.json({ revalidated: true });
}

// Webhook do CMS:
// POST /api/revalidate { "path": "/posts/my-post" }
```

### 7.5. Parallel Data Fetching

```tsx
// ═══════════════════════════════════════════════════════════
// PARALLEL FETCHING
// ═══════════════════════════════════════════════════════════

// ❌ BAD: Sequential (waterfall)
async function Page() {
  const user = await fetchUser();           // 1s
  const posts = await fetchPosts(user.id);  // 1s (espera user)
  const comments = await fetchComments();   // 1s (espera posts)
  
  // Total: 3s
  
  return <div>...</div>;
}

// ✅ GOOD: Parallel
async function Page() {
  const [user, posts, comments] = await Promise.all([
    fetchUser(),      // ↓
    fetchPosts(),     // ↓ Paralelo
    fetchComments()   // ↓
  ]);
  
  // Total: 1s (max dos 3)
  
  return <div>...</div>;
}

// ✅ BEST: Streaming (não bloqueia)
async function Page() {
  return (
    <div>
      <Suspense fallback={<UserSkeleton />}>
        <User />
      </Suspense>
      
      <Suspense fallback={<PostsSkeleton />}>
        <Posts />
      </Suspense>
      
      <Suspense fallback={<CommentsSkeleton />}>
        <Comments />
      </Suspense>
    </div>
  );
}
```

---

## 8. 💾 **Caching**

### 8.1. Cache Layers

```tsx
// ═══════════════════════════════════════════════════════════
// NEXT.JS CACHE LAYERS
// ═══════════════════════════════════════════════════════════

/**
 * 1. Request Memoization (durante request)
 *    - Same request → cached
 *    - Automatic deduplication
 * 
 * 2. Data Cache (persistente)
 *    - fetch() responses
 *    - Revalidation configurável
 * 
 * 3. Full Route Cache (build time)
 *    - Páginas estáticas geradas
 * 
 * 4. Router Cache (cliente)
 *    - Client-side navigation cache
 */
```

### 8.2. Request Memoization

```tsx
// ═══════════════════════════════════════════════════════════
// REQUEST MEMOIZATION
// ═══════════════════════════════════════════════════════════

// lib/data.ts
export async function getUser(id: number) {
  return fetch(`/api/users/${id}`).then(r => r.json());
}

// app/page.tsx
async function Page() {
  const user1 = await getUser(1);  // ✅ Faz request
  const user2 = await getUser(1);  // ✅ Cached (mesmo request)
  const user3 = await getUser(1);  // ✅ Cached
  
  // ✅ Só 1 request real!
  
  return <div>{user1.name}</div>;
}

// ────────────────────────────────────────────────────────────

// Múltiplos componentes
async function Header() {
  const user = await getUser(1);  // Request 1
  return <div>{user.name}</div>;
}

async function Sidebar() {
  const user = await getUser(1);  // ✅ Cached (mesmo request)
  return <div>{user.email}</div>;
}

async function Page() {
  return (
    <div>
      <Header />
      <Sidebar />
    </div>
  );
}

// ✅ Só 1 request, mesmo em componentes diferentes!
```

### 8.3. Data Cache

```tsx
// ═══════════════════════════════════════════════════════════
// DATA CACHE
// ═══════════════════════════════════════════════════════════

// ✅ 1. Cache permanente (default)
const data = await fetch('https://api.example.com/data');
// Cached indefinidamente

// ✅ 2. Revalidate (ISR)
const data = await fetch('https://api.example.com/data', {
  next: { revalidate: 3600 }  // 1 hora
});

// ✅ 3. No cache (sempre fresh)
const data = await fetch('https://api.example.com/data', {
  cache: 'no-store'
});

// ✅ 4. Force cache
const data = await fetch('https://api.example.com/data', {
  cache: 'force-cache'
});

// ✅ 5. Opt-out cache (route segment)
export const dynamic = 'force-dynamic';  // Toda rota sem cache

async function Page() {
  const data = await fetch('...');  // ✅ Sem cache
  return <div>{data}</div>;
}
```

### 8.4. Opting Out of Caching

```tsx
// ═══════════════════════════════════════════════════════════
// OPT-OUT CACHING
// ═══════════════════════════════════════════════════════════

// ✅ 1. Route Segment Config
export const dynamic = 'force-dynamic';  // SSR sempre
export const revalidate = 0;             // Nunca cache

// ✅ 2. Fetch no-store
const data = await fetch('...', { cache: 'no-store' });

// ✅ 3. Cookies/Headers (automático)
import { cookies, headers } from 'next/headers';

async function Page() {
  const cookieStore = cookies();
  const token = cookieStore.get('token');
  
  // ✅ Uso de cookies → SSR automático (sem cache)
  
  return <div>...</div>;
}

// ✅ 4. searchParams (automático)
async function Page({ searchParams }: { searchParams: { q: string } }) {
  // ✅ Uso de searchParams → SSR (sem cache)
  
  return <div>Search: {searchParams.q}</div>;
}
```

### 8.5. Route Segment Config

```tsx
// ═══════════════════════════════════════════════════════════
// ROUTE SEGMENT CONFIG
// ═══════════════════════════════════════════════════════════

// app/dashboard/page.tsx

// ✅ Dynamic rendering
export const dynamic = 'auto' | 'force-dynamic' | 'error' | 'force-static';

// ✅ Revalidation
export const revalidate = false | 0 | number;  // segundos

// ✅ Runtime
export const runtime = 'nodejs' | 'edge';

// ✅ Fetch cache
export const fetchCache = 'auto' | 'default-cache' | 'only-cache' | 'force-cache' | 'default-no-store' | 'only-no-store' | 'force-no-store';

// ✅ Dynamic params
export const dynamicParams = true | false;

// Exemplo completo
export const dynamic = 'force-dynamic';  // SSR
export const revalidate = 60;            // Revalida a cada 60s
export const runtime = 'edge';           // Edge runtime

async function DashboardPage() {
  const data = await fetchDashboardData();
  return <div>{data}</div>;
}

export default DashboardPage;
```

---

## 9. 🌊 **Streaming & Suspense**

### 9.1. Loading UI

```tsx
// ═══════════════════════════════════════════════════════════
// LOADING.TSX
// ═══════════════════════════════════════════════════════════

// app/dashboard/loading.tsx
export default function Loading() {
  return (
    <div className="animate-pulse">
      <div className="h-8 bg-gray-200 rounded w-1/4 mb-4" />
      <div className="h-64 bg-gray-200 rounded" />
    </div>
  );
}

// app/dashboard/page.tsx
async function DashboardPage() {
  // ✅ Enquanto carrega → mostra loading.tsx
  const data = await fetchData();
  
  return <div>{data}</div>;
}

/**
 * Equivalente a:
 * 
 * <Suspense fallback={<Loading />}>
 *   <DashboardPage />
 * </Suspense>
 */
```

### 9.2. Streaming com Suspense

```tsx
// ═══════════════════════════════════════════════════════════
// STREAMING
// ═══════════════════════════════════════════════════════════

import { Suspense } from 'react';

async function DashboardPage() {
  return (
    <div>
      <h1>Dashboard</h1>

      {/* ✅ Shell (imediato) */}
      <QuickStats />

      {/* ✅ Streamed (componentes lentos) */}
      <Suspense fallback={<ChartSkeleton />}>
        <RevenueChart />
      </Suspense>

      <Suspense fallback={<TableSkeleton />}>
        <RecentOrders />
      </Suspense>

      <Suspense fallback={<ActivitySkeleton />}>
        <UserActivity />
      </Suspense>
    </div>
  );
}

// Componente lento
async function RevenueChart() {
  // ✅ Query complexa (3s)
  const data = await prisma.$queryRaw`
    SELECT date, SUM(revenue) as total
    FROM orders
    GROUP BY date
  `;

  return <Chart data={data} />;
}

/**
 * Timeline:
 * t=0ms:   Cliente vê: <h1> + <QuickStats> + Skeletons
 * t=1s:    <RecentOrders> aparece
 * t=2s:    <UserActivity> aparece
 * t=3s:    <RevenueChart> aparece
 * 
 * ✅ Progressive rendering!
 */
```

### 9.3. Parallel Suspense

```tsx
// ═══════════════════════════════════════════════════════════
// PARALLEL SUSPENSE
// ═══════════════════════════════════════════════════════════

// ✅ Carregam em paralelo, mostram juntos
async function Page() {
  return (
    <Suspense fallback={<DashboardSkeleton />}>
      {/* ✅ Todos carregam em paralelo */}
      <UserInfo />
      <Stats />
      <Activity />
    </Suspense>
  );
}

// vs

// ✅ Carregam em paralelo, mostram individualmente
async function Page() {
  return (
    <div>
      <Suspense fallback={<UserInfoSkeleton />}>
        <UserInfo />
      </Suspense>
      
      <Suspense fallback={<StatsSkeleton />}>
        <Stats />
      </Suspense>
      
      <Suspense fallback={<ActivitySkeleton />}>
        <Activity />
      </Suspense>
    </div>
  );
}
```

### 9.4. Preloading

```tsx
// ═══════════════════════════════════════════════════════════
// PRELOADING
// ═══════════════════════════════════════════════════════════

// lib/data.ts
import { cache } from 'react';

export const getUser = cache(async (id: number) => {
  return prisma.user.findUnique({ where: { id } });
});

export function preloadUser(id: number) {
  void getUser(id);  // ✅ Inicia fetch (não aguarda)
}

// ────────────────────────────────────────────────────────────

// app/users/[id]/page.tsx
import { getUser, preloadUser } from '@/lib/data';

async function UserPage({ params }: { params: { id: string } }) {
  const userId = parseInt(params.id);
  
  // ✅ Preload posts (paralelo)
  preloadPosts(userId);
  
  // ✅ Aguarda user
  const user = await getUser(userId);

  return (
    <div>
      <h1>{user.name}</h1>
      
      {/* ✅ Posts já estão carregando */}
      <Suspense fallback={<PostsSkeleton />}>
        <UserPosts userId={userId} />
      </Suspense>
    </div>
  );
}
```

---

## 10. 📐 **Layouts & Templates**

### 10.1. Root Layout

```tsx
// ═══════════════════════════════════════════════════════════
// ROOT LAYOUT (obrigatório)
// ═══════════════════════════════════════════════════════════

// app/layout.tsx
import './globals.css';
import { Inter } from 'next/font/google';

const inter = Inter({ subsets: ['latin'] });

export const metadata = {
  title: 'My App',
  description: 'My awesome app'
};

export default function RootLayout({
  children
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en">
      <body className={inter.className}>
        {/* ✅ Persiste entre rotas */}
        <Header />
        
        <main>{children}</main>
        
        <Footer />
      </body>
    </html>
  );
}

/**
 * ✅ Root layout:
 * - Obrigatório
 * - Deve ter <html> e <body>
 * - Persiste entre navegações
 * - Não re-renderiza
 */
```

### 10.2. Nested Layouts

```tsx
// ═══════════════════════════════════════════════════════════
// NESTED LAYOUTS
// ═══════════════════════════════════════════════════════════

/**
 * app/
 * ├── layout.tsx              # Root layout
 * ├── page.tsx                # /
 * │
 * └── dashboard/
 *     ├── layout.tsx          # Dashboard layout
 *     ├── page.tsx            # /dashboard
 *     └── settings/
 *         └── page.tsx        # /dashboard/settings
 */

// app/dashboard/layout.tsx
export default function DashboardLayout({
  children
}: {
  children: React.ReactNode;
}) {
  return (
    <div className="dashboard">
      <Sidebar />
      
      <div className="content">
        {children}
      </div>
    </div>
  );
}

/**
 * Renderização em /dashboard/settings:
 * 
 * <RootLayout>
 *   <DashboardLayout>
 *     <SettingsPage />
 *   </DashboardLayout>
 * </RootLayout>
 */
```

### 10.3. Route Groups Layouts

```tsx
// ═══════════════════════════════════════════════════════════
// ROUTE GROUPS: Múltiplos layouts
// ═══════════════════════════════════════════════════════════

/**
 * app/
 * ├── (marketing)/
 * │   ├── layout.tsx          # Marketing layout
 * │   ├── page.tsx            → /
 * │   ├── about/page.tsx      → /about
 * │   └── pricing/page.tsx    → /pricing
 * │
 * └── (shop)/
 *     ├── layout.tsx          # Shop layout
 *     ├── products/page.tsx   → /products
 *     └── cart/page.tsx       → /cart
 */

// app/(marketing)/layout.tsx
export default function MarketingLayout({ children }: Props) {
  return (
    <div>
      <MarketingHeader />
      {children}
      <MarketingFooter />
    </div>
  );
}

// app/(shop)/layout.tsx
export default function ShopLayout({ children }: Props) {
  return (
    <div>
      <ShopHeader />
      <ShopSidebar />
      {children}
    </div>
  );
}
```

### 10.4. Templates

```tsx
// ═══════════════════════════════════════════════════════════
// TEMPLATES: Re-render em navegação
// ═══════════════════════════════════════════════════════════

// app/template.tsx
export default function Template({ children }: { children: React.ReactNode }) {
  return (
    <div className="animate-fade-in">
      {children}
    </div>
  );
}

/**
 * Layout vs Template:
 * 
 * Layout:
 * - Persiste entre navegações
 * - Não re-renderiza
 * - State preservado
 * 
 * Template:
 * - Re-renderiza em cada navegação
 * - State resetado
 * - Útil para animações
 */
```

### 10.5. Dynamic Layouts

```tsx
// ═══════════════════════════════════════════════════════════
// DYNAMIC LAYOUTS
// ═══════════════════════════════════════════════════════════

// app/[tenant]/layout.tsx
async function TenantLayout({
  children,
  params
}: {
  children: React.ReactNode;
  params: { tenant: string };
}) {
  // ✅ Fetch tenant config
  const tenant = await fetchTenant(params.tenant);

  return (
    <div style={{ '--primary-color': tenant.color } as any}>
      <TenantHeader tenant={tenant} />
      {children}
    </div>
  );
}

export default TenantLayout;

/**
 * URLs:
 * /acme → Layout com config da Acme
 * /globex → Layout com config da Globex
 */
```

---

## 11. 🔌 **Route Handlers (API Routes)**

### 11.1. Basic Route Handler

```tsx
// ═══════════════════════════════════════════════════════════
// ROUTE HANDLERS
// ═══════════════════════════════════════════════════════════

// app/api/users/route.ts

export async function GET() {
  const users = await prisma.user.findMany();
  
  return Response.json(users);
}

export async function POST(request: Request) {
  const body = await request.json();
  
  const user = await prisma.user.create({
    data: body
  });
  
  return Response.json(user, { status: 201 });
}

/**
 * HTTP Methods:
 * - GET
 * - POST
 * - PUT
 * - PATCH
 * - DELETE
 * - HEAD
 * - OPTIONS
 */
```

### 11.2. Dynamic Route Handlers

```tsx
// ═══════════════════════════════════════════════════════════
// DYNAMIC ROUTE HANDLERS
// ═══════════════════════════════════════════════════════════

// app/api/users/[id]/route.ts

interface Context {
  params: { id: string };
}

export async function GET(request: Request, context: Context) {
  const userId = parseInt(context.params.id);
  
  const user = await prisma.user.findUnique({
    where: { id: userId }
  });

  if (!user) {
    return Response.json({ error: 'User not found' }, { status: 404 });
  }

  return Response.json(user);
}

export async function PATCH(request: Request, context: Context) {
  const userId = parseInt(context.params.id);
  const body = await request.json();

  const user = await prisma.user.update({
    where: { id: userId },
    data: body
  });

  return Response.json(user);
}

export async function DELETE(request: Request, context: Context) {
  const userId = parseInt(context.params.id);

  await prisma.user.delete({
    where: { id: userId }
  });

  return Response.json({ success: true });
}
```

### 11.3. Request Data

```tsx
// ═══════════════════════════════════════════════════════════
// REQUEST DATA
// ═══════════════════════════════════════════════════════════

export async function POST(request: Request) {
  // ✅ JSON body
  const body = await request.json();

  // ✅ Headers
  const contentType = request.headers.get('content-type');
  const auth = request.headers.get('authorization');

  // ✅ Cookies
  const { cookies } = await import('next/headers');
  const cookieStore = cookies();
  const token = cookieStore.get('token')?.value;

  // ✅ Search params
  const { searchParams } = new URL(request.url);
  const query = searchParams.get('q');
  const page = searchParams.get('page');

  // ✅ FormData
  const formData = await request.formData();
  const name = formData.get('name');
  const file = formData.get('file') as File;

  return Response.json({ success: true });
}
```

### 11.4. Response Types

```tsx
// ═══════════════════════════════════════════════════════════
// RESPONSE TYPES
// ═══════════════════════════════════════════════════════════

// ✅ 1. JSON
export async function GET() {
  return Response.json({ message: 'Hello' });
}

// ✅ 2. Text
export async function GET() {
  return new Response('Hello World');
}

// ✅ 3. HTML
export async function GET() {
  return new Response('<h1>Hello</h1>', {
    headers: { 'Content-Type': 'text/html' }
  });
}

// ✅ 4. Redirect
export async function GET() {
  return Response.redirect('https://example.com');
}

// ✅ 5. Stream
export async function GET() {
  const stream = new ReadableStream({
    start(controller) {
      controller.enqueue('chunk 1');
      controller.enqueue('chunk 2');
      controller.close();
    }
  });

  return new Response(stream);
}

// ✅ 6. File
export async function GET() {
  const file = await fs.readFile('file.pdf');
  
  return new Response(file, {
    headers: {
      'Content-Type': 'application/pdf',
      'Content-Disposition': 'attachment; filename="file.pdf"'
    }
  });
}
```

### 11.5. Middleware-like Patterns

```tsx
// ═══════════════════════════════════════════════════════════
// AUTH MIDDLEWARE PATTERN
// ═══════════════════════════════════════════════════════════

// lib/auth.ts
export async function withAuth(
  handler: (request: Request, context: any) => Promise<Response>
) {
  return async (request: Request, context: any) => {
    const { cookies } = await import('next/headers');
    const token = cookies().get('token')?.value;

    if (!token) {
      return Response.json({ error: 'Unauthorized' }, { status: 401 });
    }

    // ✅ Verify token
    const user = await verifyToken(token);

    if (!user) {
      return Response.json({ error: 'Invalid token' }, { status: 401 });
    }

    // ✅ Inject user no context
    return handler(request, { ...context, user });
  };
}

// app/api/protected/route.ts
export const GET = withAuth(async (request, { user }) => {
  return Response.json({ message: `Hello ${user.name}` });
});
```

---

## 12. 🏷️ **Metadata & SEO**

### 12.1. Static Metadata

```tsx
// ═══════════════════════════════════════════════════════════
// STATIC METADATA
// ═══════════════════════════════════════════════════════════

// app/page.tsx
import { Metadata } from 'next';

export const metadata: Metadata = {
  title: 'Home - My App',
  description: 'Welcome to my awesome app',
  
  keywords: ['nextjs', 'react', 'typescript'],
  
  authors: [{ name: 'John Doe', url: 'https://johndoe.com' }],
  
  openGraph: {
    title: 'Home - My App',
    description: 'Welcome to my awesome app',
    url: 'https://myapp.com',
    siteName: 'My App',
    images: [
      {
        url: 'https://myapp.com/og-image.jpg',
        width: 1200,
        height: 630
      }
    ],
    locale: 'en_US',
    type: 'website'
  },
  
  twitter: {
    card: 'summary_large_image',
    title: 'Home - My App',
    description: 'Welcome to my awesome app',
    images: ['https://myapp.com/twitter-image.jpg']
  },
  
  robots: {
    index: true,
    follow: true,
    googleBot: {
      index: true,
      follow: true,
      'max-video-preview': -1,
      'max-image-preview': 'large',
      'max-snippet': -1
    }
  }
};

export default function HomePage() {
  return <div>Home</div>;
}
```

### 12.2. Dynamic Metadata

```tsx
// ═══════════════════════════════════════════════════════════
// DYNAMIC METADATA
// ═══════════════════════════════════════════════════════════

// app/blog/[slug]/page.tsx

interface Props {
  params: { slug: string };
}

export async function generateMetadata({ params }: Props): Promise<Metadata> {
  // ✅ Fetch data
  const post = await prisma.post.findUnique({
    where: { slug: params.slug }
  });

  if (!post) {
    return {
      title: 'Post Not Found'
    };
  }

  return {
    title: post.title,
    description: post.excerpt,
    
    openGraph: {
      title: post.title,
      description: post.excerpt,
      images: [post.coverImage],
      type: 'article',
      publishedTime: post.publishedAt,
      authors: [post.author.name]
    },
    
    twitter: {
      card: 'summary_large_image',
      title: post.title,
      description: post.excerpt,
      images: [post.coverImage]
    }
  };
}

export default async function BlogPost({ params }: Props) {
  const post = await prisma.post.findUnique({
    where: { slug: params.slug }
  });

  return <article>{post.content}</article>;
}
```

### 12.3. Metadata Inheritance

```tsx
// ═══════════════════════════════════════════════════════════
// METADATA INHERITANCE
// ═══════════════════════════════════════════════════════════

// app/layout.tsx (root)
export const metadata = {
  title: {
    default: 'My App',
    template: '%s | My App'  // ✅ Template para child pages
  },
  description: 'My awesome app'
};

// app/blog/layout.tsx
export const metadata = {
  title: 'Blog'  // → "Blog | My App"
};

// app/blog/[slug]/page.tsx
export async function generateMetadata({ params }: Props) {
  const post = await fetchPost(params.slug);
  
  return {
    title: post.title  // → "Post Title | My App"
  };
}
```

### 12.4. JSON-LD Structured Data

```tsx
// ═══════════════════════════════════════════════════════════
// JSON-LD STRUCTURED DATA
// ═══════════════════════════════════════════════════════════

// app/blog/[slug]/page.tsx

export default async function BlogPost({ params }: Props) {
  const post = await fetchPost(params.slug);

  const jsonLd = {
    '@context': 'https://schema.org',
    '@type': 'BlogPosting',
    headline: post.title,
    description: post.excerpt,
    image: post.coverImage,
    datePublished: post.publishedAt,
    dateModified: post.updatedAt,
    author: {
      '@type': 'Person',
      name: post.author.name,
      url: post.author.url
    }
  };

  return (
    <>
      <script
        type="application/ld+json"
        dangerouslySetInnerHTML={{ __html: JSON.stringify(jsonLd) }}
      />
      
      <article>
        <h1>{post.title}</h1>
        <div>{post.content}</div>
      </article>
    </>
  );
}
```

### 12.5. Sitemap & Robots.txt

```tsx
// ═══════════════════════════════════════════════════════════
// SITEMAP
// ═══════════════════════════════════════════════════════════

// app/sitemap.ts
import { MetadataRoute } from 'next';

export default async function sitemap(): Promise<MetadataRoute.Sitemap> {
  const posts = await prisma.post.findMany({
    select: { slug: true, updatedAt: true }
  });

  const postUrls = posts.map(post => ({
    url: `https://myapp.com/blog/${post.slug}`,
    lastModified: post.updatedAt,
    changeFrequency: 'weekly' as const,
    priority: 0.8
  }));

  return [
    {
      url: 'https://myapp.com',
      lastModified: new Date(),
      changeFrequency: 'daily',
      priority: 1
    },
    {
      url: 'https://myapp.com/about',
      lastModified: new Date(),
      changeFrequency: 'monthly',
      priority: 0.5
    },
    ...postUrls
  ];
}

// ────────────────────────────────────────────────────────────
// ROBOTS.TXT
// ────────────────────────────────────────────────────────────

// app/robots.ts
import { MetadataRoute } from 'next';

export default function robots(): MetadataRoute.Robots {
  return {
    rules: {
      userAgent: '*',
      allow: '/',
      disallow: ['/admin/', '/api/']
    },
    sitemap: 'https://myapp.com/sitemap.xml'
  };
}
```

---

## 13. 🛡️ **Middleware**

### 13.1. Basic Middleware

```tsx
// ═══════════════════════════════════════════════════════════
// MIDDLEWARE
// ═══════════════════════════════════════════════════════════

// middleware.ts (raiz do projeto)
import { NextResponse } from 'next/server';
import type { NextRequest } from 'next/server';

export function middleware(request: NextRequest) {
  console.log('Middleware:', request.nextUrl.pathname);
  
  // ✅ Continua request
  return NextResponse.next();
}

/**
 * ✅ Middleware executa:
 * - ANTES de cada request
 * - No Edge Runtime (super rápido)
 * - Para todas rotas (ou filtradas)
 */
```

### 13.2. Auth Middleware

```tsx
// ═══════════════════════════════════════════════════════════
// AUTH MIDDLEWARE
// ═══════════════════════════════════════════════════════════

import { NextResponse } from 'next/server';
import type { NextRequest } from 'next/server';

export function middleware(request: NextRequest) {
  const token = request.cookies.get('token')?.value;

  // ✅ Rotas protegidas
  const protectedPaths = ['/dashboard', '/profile', '/settings'];
  const isProtected = protectedPaths.some(path =>
    request.nextUrl.pathname.startsWith(path)
  );

  if (isProtected && !token) {
    // ✅ Redirect to login
    const loginUrl = new URL('/login', request.url);
    loginUrl.searchParams.set('from', request.nextUrl.pathname);
    
    return NextResponse.redirect(loginUrl);
  }

  return NextResponse.next();
}

// ✅ Matcher: Define em quais rotas executar
export const config = {
  matcher: [
    '/dashboard/:path*',
    '/profile/:path*',
    '/settings/:path*'
  ]
};
```

### 13.3. Rewrite & Redirect

```tsx
// ═══════════════════════════════════════════════════════════
// REWRITE & REDIRECT
// ═══════════════════════════════════════════════════════════

export function middleware(request: NextRequest) {
  // ✅ 1. Redirect
  if (request.nextUrl.pathname === '/old-path') {
    return NextResponse.redirect(new URL('/new-path', request.url));
  }

  // ✅ 2. Rewrite (proxy)
  if (request.nextUrl.pathname.startsWith('/api/v1')) {
    return NextResponse.rewrite(
      new URL(request.nextUrl.pathname.replace('/api/v1', '/api/v2'), request.url)
    );
  }

  // ✅ 3. Rewrite para external API
  if (request.nextUrl.pathname.startsWith('/api/external')) {
    return NextResponse.rewrite(
      new URL(
        request.nextUrl.pathname.replace('/api/external', ''),
        'https://api.example.com'
      )
    );
  }

  return NextResponse.next();
}
```

### 13.4. Headers & Cookies

```tsx
// ═══════════════════════════════════════════════════════════
// HEADERS & COOKIES
// ═══════════════════════════════════════════════════════════

export function middleware(request: NextRequest) {
  const response = NextResponse.next();

  // ✅ Set headers
  response.headers.set('X-Custom-Header', 'value');
  response.headers.set('X-Request-ID', crypto.randomUUID());

  // ✅ Set cookies
  response.cookies.set('session-id', 'abc123', {
    httpOnly: true,
    secure: true,
    sameSite: 'strict',
    maxAge: 60 * 60 * 24 * 7  // 7 dias
  });

  // ✅ Delete cookies
  response.cookies.delete('old-cookie');

  // ✅ CORS headers
  response.headers.set('Access-Control-Allow-Origin', '*');
  response.headers.set('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE');

  return response;
}
```

### 13.5. A/B Testing

```tsx
// ═══════════════════════════════════════════════════════════
// A/B TESTING MIDDLEWARE
// ═══════════════════════════════════════════════════════════

export function middleware(request: NextRequest) {
  // ✅ Check existing variant
  let variant = request.cookies.get('variant')?.value;

  if (!variant) {
    // ✅ Assign random variant
    variant = Math.random() < 0.5 ? 'a' : 'b';
  }

  const response = NextResponse.next();

  // ✅ Set variant cookie
  response.cookies.set('variant', variant, {
    maxAge: 60 * 60 * 24 * 30  // 30 dias
  });

  // ✅ Add header para components
  response.headers.set('X-Variant', variant);

  return response;
}

// app/page.tsx
import { headers } from 'next/headers';

export default function HomePage() {
  const variant = headers().get('x-variant');

  if (variant === 'a') {
    return <HomePageA />;
  }

  return <HomePageB />;
}
```

### 13.6. Rate Limiting

```tsx
// ═══════════════════════════════════════════════════════════
// RATE LIMITING (Edge KV example)
// ═══════════════════════════════════════════════════════════

import { NextResponse } from 'next/server';
import type { NextRequest } from 'next/server';

const RATE_LIMIT = 10;  // requests per minute
const WINDOW = 60 * 1000;  // 1 minute

const requestCounts = new Map<string, { count: number; resetTime: number }>();

export function middleware(request: NextRequest) {
  const ip = request.ip || 'anonymous';
  const now = Date.now();

  const record = requestCounts.get(ip);

  if (!record || now > record.resetTime) {
    // ✅ Reset window
    requestCounts.set(ip, {
      count: 1,
      resetTime: now + WINDOW
    });
  } else {
    record.count++;

    if (record.count > RATE_LIMIT) {
      // ✅ Rate limited
      return NextResponse.json(
        { error: 'Too many requests' },
        { status: 429 }
      );
    }
  }

  return NextResponse.next();
}

export const config = {
  matcher: '/api/:path*'
};
```

### 13.7. Geolocation

```tsx
// ═══════════════════════════════════════════════════════════
// GEOLOCATION (Vercel/Cloudflare)
// ═══════════════════════════════════════════════════════════

export function middleware(request: NextRequest) {
  // ✅ Vercel geolocation headers
  const country = request.geo?.country || 'US';
  const city = request.geo?.city || 'Unknown';

  // ✅ Redirect based on country
  if (country === 'BR' && !request.nextUrl.pathname.startsWith('/pt')) {
    return NextResponse.redirect(new URL('/pt', request.url));
  }

  // ✅ Add geo headers
  const response = NextResponse.next();
  response.headers.set('X-Country', country);
  response.headers.set('X-City', city);

  return response;
}

// app/page.tsx
import { headers } from 'next/headers';

export default function HomePage() {
  const country = headers().get('x-country');

  return (
    <div>
      <h1>Welcome from {country}!</h1>
    </div>
  );
}
```

---

## 14. 🚀 **Deployment**

### 14.1. Vercel Deployment

```bash
# ═══════════════════════════════════════════════════════════
# VERCEL (Recomendado)
# ═══════════════════════════════════════════════════════════

# 1. Install Vercel CLI
npm i -g vercel

# 2. Deploy
vercel

# 3. Production deploy
vercel --prod

# Ou via GitHub:
# 1. Push to GitHub
# 2. Import no Vercel (vercel.com)
# 3. Auto-deploy on push

# ✅ Benefícios Vercel:
# - Zero config
# - Edge Functions
# - Incremental Static Regeneration
# - Image Optimization
# - Analytics
# - Preview deployments
```

### 14.2. Docker Deployment

```dockerfile
# ═══════════════════════════════════════════════════════════
# DOCKERFILE
# ═══════════════════════════════════════════════════════════

FROM node:18-alpine AS base

# Dependencies
FROM base AS deps
RUN apk add --no-cache libc6-compat
WORKDIR /app

COPY package*.json ./
RUN npm ci

# Builder
FROM base AS builder
WORKDIR /app
COPY --from=deps /app/node_modules ./node_modules
COPY . .

# Build
RUN npm run build

# Production
FROM base AS runner
WORKDIR /app

ENV NODE_ENV production

RUN addgroup --system --gid 1001 nodejs
RUN adduser --system --uid 1001 nextjs

COPY --from=builder /app/public ./public

# Standalone build
COPY --from=builder --chown=nextjs:nodejs /app/.next/standalone ./
COPY --from=builder --chown=nextjs:nodejs /app/.next/static ./.next/static

USER nextjs

EXPOSE 3000

ENV PORT 3000

CMD ["node", "server.js"]
```

```js
// ═══════════════════════════════════════════════════════════
// next.config.js (Standalone)
// ═══════════════════════════════════════════════════════════

/** @type {import('next').NextConfig} */
const nextConfig = {
  output: 'standalone'  // ✅ Cria build standalone para Docker
};

module.exports = nextConfig;
```

```yaml
# ═══════════════════════════════════════════════════════════
# docker-compose.yml
# ═══════════════════════════════════════════════════════════

version: '3.8'

services:
  app:
    build: .
    ports:
      - "3000:3000"
    environment:
      - DATABASE_URL=postgresql://user:pass@db:5432/mydb
      - NEXTAUTH_URL=http://localhost:3000
    depends_on:
      - db

  db:
    image: postgres:15
    environment:
      POSTGRES_USER: user
      POSTGRES_PASSWORD: pass
      POSTGRES_DB: mydb
    volumes:
      - postgres_data:/var/lib/postgresql/data

volumes:
  postgres_data:
```

### 14.3. Environment Variables

```bash
# ═══════════════════════════════════════════════════════════
# ENVIRONMENT VARIABLES
# ═══════════════════════════════════════════════════════════

# .env.local (desenvolvimento)
DATABASE_URL="postgresql://localhost:5432/mydb"
NEXTAUTH_SECRET="super-secret-key"
STRIPE_SECRET_KEY="sk_test_..."

# ✅ Público (exposto ao cliente)
NEXT_PUBLIC_API_URL="http://localhost:3000/api"
NEXT_PUBLIC_STRIPE_KEY="pk_test_..."
```

```tsx
// ═══════════════════════════════════════════════════════════
// USO
// ═══════════════════════════════════════════════════════════

// ✅ Server Component (acesso direto)
async function ServerComponent() {
  const dbUrl = process.env.DATABASE_URL;  // ✅ Seguro
  
  return <div>...</div>;
}

// ✅ Client Component (só NEXT_PUBLIC_*)
'use client';

function ClientComponent() {
  const apiUrl = process.env.NEXT_PUBLIC_API_URL;  // ✅ OK
  const secret = process.env.NEXTAUTH_SECRET;      // ❌ undefined
  
  return <div>API: {apiUrl}</div>;
}

// ✅ Route Handler
export async function GET() {
  const apiKey = process.env.STRIPE_SECRET_KEY;  // ✅ Seguro
  
  return Response.json({ ok: true });
}
```

### 14.4. Build Optimization

```js
// ═══════════════════════════════════════════════════════════
// BUILD OPTIMIZATION
// ═══════════════════════════════════════════════════════════

/** @type {import('next').NextConfig} */
const nextConfig = {
  // ✅ 1. Compiler options
  compiler: {
    removeConsole: process.env.NODE_ENV === 'production'
  },

  // ✅ 2. Minification
  swcMinify: true,

  // ✅ 3. Output
  output: 'standalone',  // Smaller Docker builds

  // ✅ 4. Compression
  compress: true,

  // ✅ 5. React Strict Mode
  reactStrictMode: true,

  // ✅ 6. Analyze bundle
  webpack: (config, { isServer }) => {
    if (!isServer) {
      config.optimization.splitChunks = {
        chunks: 'all',
        cacheGroups: {
          default: false,
          vendors: false,
          framework: {
            name: 'framework',
            chunks: 'all',
            test: /[\\/]node_modules[\\/](react|react-dom|scheduler|use-sync-external-store)[\\/]/,
            priority: 40,
            enforce: true
          },
          commons: {
            name: 'commons',
            minChunks: 2,
            priority: 20
          }
        }
      };
    }
    
    return config;
  }
};

module.exports = nextConfig;
```

### 14.5. Performance Monitoring

```tsx
// ═══════════════════════════════════════════════════════════
// WEB VITALS
// ═══════════════════════════════════════════════════════════

// app/layout.tsx
export function reportWebVitals(metric: any) {
  console.log(metric);
  
  // ✅ Send to analytics
  if (metric.name === 'FCP') {
    // First Contentful Paint
  }
  
  if (metric.name === 'LCP') {
    // Largest Contentful Paint
  }
  
  if (metric.name === 'CLS') {
    // Cumulative Layout Shift
  }
  
  if (metric.name === 'FID') {
    // First Input Delay
  }
  
  if (metric.name === 'TTFB') {
    // Time to First Byte
  }
  
  // Send to Vercel Analytics, Google Analytics, etc
  window.gtag?.('event', metric.name, {
    value: Math.round(metric.value),
    metric_id: metric.id,
    metric_value: metric.value,
    metric_delta: metric.delta
  });
}
```

### 14.6. Static Export

```js
// ═══════════════════════════════════════════════════════════
// STATIC EXPORT (HTML estático)
// ═══════════════════════════════════════════════════════════

/** @type {import('next').NextConfig} */
const nextConfig = {
  output: 'export'  // ✅ Gera HTML estático
};

module.exports = nextConfig;

/**
 * Build:
 * npm run build
 * 
 * Output:
 * /out
 *   ├── index.html
 *   ├── about.html
 *   └── blog/
 *       ├── post-1.html
 *       └── post-2.html
 * 
 * ✅ Deploy em:
 * - GitHub Pages
 * - Netlify
 * - AWS S3
 * - Qualquer CDN
 * 
 * ❌ Limitações:
 * - Sem Server Components
 * - Sem API Routes
 * - Sem ISR
 * - Sem Image Optimization (automatic)
 */
```

---

## 15. 📚 **Recursos e Referências**

### 15.1. Documentação Oficial

**Next.js Docs:**
- 📖 **Docs**: https://nextjs.org/docs
- 🎯 **App Router**: https://nextjs.org/docs/app
- 🖥️ **Server Components**: https://nextjs.org/docs/app/building-your-application/rendering/server-components
- 🎬 **Server Actions**: https://nextjs.org/docs/app/building-your-application/data-fetching/server-actions-and-mutations

**Learn Next.js:**
- ✅ Tutorial oficial: https://nextjs.org/learn
- ✅ Examples: https://github.com/vercel/next.js/tree/canary/examples

**React Docs:**
- 📖 Server Components: https://react.dev/blog/2023/03/22/react-labs-what-we-have-been-working-on-march-2023

### 15.2. Bibliotecas Essenciais

```bash
# ═══════════════════════════════════════════════════════════
# ESSENTIALS
# ═══════════════════════════════════════════════════════════

# Authentication
npm install next-auth

# Database
npm install prisma @prisma/client
npm install drizzle-orm

# Validation
npm install zod

# Forms
npm install react-hook-form

# UI
npm install @radix-ui/react-*
npm install @headlessui/react

# Styling
npm install tailwindcss
npm install clsx tailwind-merge

# Data Fetching
npm install @tanstack/react-query
npm install swr

# State Management
npm install zustand
npm install jotai

# Date/Time
npm install date-fns

# Icons
npm install lucide-react
```

### 15.3. Best Practices

```markdown
✅ **Architecture:**
- [ ] Use App Router (não Pages Router)
- [ ] Server Components por default
- [ ] Client Components só quando necessário
- [ ] Organize com Route Groups
- [ ] Use layouts para código compartilhado

✅ **Data Fetching:**
- [ ] Fetch em Server Components
- [ ] Parallel fetching (Promise.all)
- [ ] Cache com next: { revalidate }
- [ ] Tag-based revalidation
- [ ] Streaming com Suspense

✅ **Performance:**
- [ ] Code splitting automático
- [ ] Image Optimization (<Image>)
- [ ] Font Optimization (next/font)
- [ ] Static generation quando possível
- [ ] ISR para conteúdo dinâmico

✅ **SEO:**
- [ ] generateMetadata em todas páginas
- [ ] Structured data (JSON-LD)
- [ ] Sitemap.xml
- [ ] Robots.txt
- [ ] Open Graph tags

✅ **Security:**
- [ ] Environment variables
- [ ] CSRF protection (Server Actions)
- [ ] Rate limiting (Middleware)
- [ ] Input validation (Zod)
- [ ] SQL injection prevention (Prisma)

✅ **Developer Experience:**
- [ ] TypeScript strict mode
- [ ] ESLint + Prettier
- [ ] Git hooks (Husky)
- [ ] Commitlint
- [ ] Testing (Vitest, Playwright)
```

### 15.4. Exemplo Completo: Blog

```tsx
// ═══════════════════════════════════════════════════════════
// BLOG COMPLETO
// ═══════════════════════════════════════════════════════════

// ────────────────────────────────────────────────────────────
// prisma/schema.prisma
// ────────────────────────────────────────────────────────────

model User {
  id        Int      @id @default(autoincrement())
  email     String   @unique
  name      String
  posts     Post[]
  createdAt DateTime @default(now())
}

model Post {
  id          Int      @id @default(autoincrement())
  title       String
  slug        String   @unique
  content     String
  excerpt     String
  coverImage  String?
  published   Boolean  @default(false)
  author      User     @relation(fields: [authorId], references: [id])
  authorId    Int
  createdAt   DateTime @default(now())
  updatedAt   DateTime @updatedAt
}

// ────────────────────────────────────────────────────────────
// lib/db.ts
// ────────────────────────────────────────────────────────────

import { PrismaClient } from '@prisma/client';

const globalForPrisma = global as unknown as { prisma: PrismaClient };

export const prisma = globalForPrisma.prisma || new PrismaClient();

if (process.env.NODE_ENV !== 'production') globalForPrisma.prisma = prisma;

// ────────────────────────────────────────────────────────────
// app/blog/page.tsx
// ────────────────────────────────────────────────────────────

import { prisma } from '@/lib/db';
import { PostCard } from '@/components/PostCard';

export const revalidate = 60;  // ISR: 60 segundos

export const metadata = {
  title: 'Blog',
  description: 'Latest posts from our blog'
};

export default async function BlogPage() {
  const posts = await prisma.post.findMany({
    where: { published: true },
    include: { author: true },
    orderBy: { createdAt: 'desc' }
  });

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-4xl font-bold mb-8">Blog</h1>
      
      <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
        {posts.map(post => (
          <PostCard key={post.id} post={post} />
        ))}
      </div>
    </div>
  );
}

// ────────────────────────────────────────────────────────────
// app/blog/[slug]/page.tsx
// ────────────────────────────────────────────────────────────

import { notFound } from 'next/navigation';
import { prisma } from '@/lib/db';
import type { Metadata } from 'next';

interface Props {
  params: { slug: string };
}

// ✅ Static generation
export async function generateStaticParams() {
  const posts = await prisma.post.findMany({
    where: { published: true },
    select: { slug: true }
  });

  return posts.map(post => ({
    slug: post.slug
  }));
}

// ✅ Dynamic metadata
export async function generateMetadata({ params }: Props): Promise<Metadata> {
  const post = await prisma.post.findUnique({
    where: { slug: params.slug }
  });

  if (!post) return { title: 'Post Not Found' };

  return {
    title: post.title,
    description: post.excerpt,
    openGraph: {
      title: post.title,
      description: post.excerpt,
      images: post.coverImage ? [post.coverImage] : [],
      type: 'article'
    }
  };
}

export default async function BlogPost({ params }: Props) {
  const post = await prisma.post.findUnique({
    where: { slug: params.slug },
    include: { author: true }
  });

  if (!post) {
    notFound();
  }

  const jsonLd = {
    '@context': 'https://schema.org',
    '@type': 'BlogPosting',
    headline: post.title,
    description: post.excerpt,
    image: post.coverImage,
    datePublished: post.createdAt,
    author: {
      '@type': 'Person',
      name: post.author.name
    }
  };

  return (
    <>
      <script
        type="application/ld+json"
        dangerouslySetInnerHTML={{ __html: JSON.stringify(jsonLd) }}
      />
      
      <article className="container mx-auto px-4 py-8 max-w-3xl">
        {post.coverImage && (
          <img
            src={post.coverImage}
            alt={post.title}
            className="w-full h-64 object-cover rounded-lg mb-8"
          />
        )}
        
        <h1 className="text-4xl font-bold mb-4">{post.title}</h1>
        
        <div className="flex items-center gap-4 text-gray-600 mb-8">
          <span>By {post.author.name}</span>
          <span>•</span>
          <time>{new Date(post.createdAt).toLocaleDateString()}</time>
        </div>
        
        <div className="prose prose-lg">
          {post.content}
        </div>
      </article>
    </>
  );
}

// ────────────────────────────────────────────────────────────
// components/PostCard.tsx
// ────────────────────────────────────────────────────────────

import Link from 'next/link';
import type { Post, User } from '@prisma/client';

interface Props {
  post: Post & { author: User };
}

export function PostCard({ post }: Props) {
  return (
    <Link
      href={`/blog/${post.slug}`}
      className="block border rounded-lg overflow-hidden hover:shadow-lg transition"
    >
      {post.coverImage && (
        <img
          src={post.coverImage}
          alt={post.title}
          className="w-full h-48 object-cover"
        />
      )}
      
      <div className="p-4">
        <h2 className="text-xl font-bold mb-2">{post.title}</h2>
        <p className="text-gray-600 mb-4">{post.excerpt}</p>
        
        <div className="flex items-center justify-between text-sm text-gray-500">
          <span>{post.author.name}</span>
          <time>{new Date(post.createdAt).toLocaleDateString()}</time>
        </div>
      </div>
    </Link>
  );
}

// ────────────────────────────────────────────────────────────
// app/admin/posts/create/page.tsx
// ────────────────────────────────────────────────────────────

import { redirect } from 'next/navigation';
import { revalidatePath } from 'next/cache';
import { prisma } from '@/lib/db';

async function createPost(formData: FormData) {
  'use server';

  const title = formData.get('title') as string;
  const content = formData.get('content') as string;
  const excerpt = formData.get('excerpt') as string;
  
  // ✅ Generate slug
  const slug = title
    .toLowerCase()
    .replace(/[^a-z0-9]+/g, '-')
    .replace(/(^-|-$)/g, '');

  // ✅ Create post
  const post = await prisma.post.create({
    data: {
      title,
      slug,
      content,
      excerpt,
      authorId: 1  // TODO: Get from session
    }
  });

  // ✅ Revalidate
  revalidatePath('/blog');

  // ✅ Redirect
  redirect(`/blog/${post.slug}`);
}

export default function CreatePostPage() {
  return (
    <div className="container mx-auto px-4 py-8 max-w-2xl">
      <h1 className="text-3xl font-bold mb-8">Create Post</h1>
      
      <form action={createPost} className="space-y-6">
        <div>
          <label htmlFor="title" className="block font-medium mb-2">
            Title
          </label>
          <input
            id="title"
            name="title"
            type="text"
            required
            className="w-full border rounded px-3 py-2"
          />
        </div>

        <div>
          <label htmlFor="excerpt" className="block font-medium mb-2">
            Excerpt
          </label>
          <textarea
            id="excerpt"
            name="excerpt"
            required
            rows={3}
            className="w-full border rounded px-3 py-2"
          />
        </div>

        <div>
          <label htmlFor="content" className="block font-medium mb-2">
            Content
          </label>
          <textarea
            id="content"
            name="content"
            required
            rows={10}
            className="w-full border rounded px-3 py-2"
          />
        </div>

        <button
          type="submit"
          className="bg-blue-600 text-white px-6 py-2 rounded hover:bg-blue-700"
        >
          Create Post
        </button>
      </form>
    </div>
  );
}

// ────────────────────────────────────────────────────────────
// app/sitemap.ts
// ────────────────────────────────────────────────────────────

import { prisma } from '@/lib/db';
import type { MetadataRoute } from 'next';

export default async function sitemap(): Promise<MetadataRoute.Sitemap> {
  const posts = await prisma.post.findMany({
    where: { published: true },
    select: { slug: true, updatedAt: true }
  });

  const postUrls = posts.map(post => ({
    url: `https://myblog.com/blog/${post.slug}`,
    lastModified: post.updatedAt,
    changeFrequency: 'weekly' as const,
    priority: 0.8
  }));

  return [
    {
      url: 'https://myblog.com',
      lastModified: new Date(),
      changeFrequency: 'daily',
      priority: 1
    },
    {
      url: 'https://myblog.com/blog',
      lastModified: new Date(),
      changeFrequency: 'daily',
      priority: 0.9
    },
    ...postUrls
  ];
}
```

### 15.5. Troubleshooting

```markdown
## Common Issues

### ❌ "Error: Cannot find module 'next'"
✅ npm install next react react-dom

### ❌ "Error: Invalid next.config.js"
✅ Use module.exports (não export default)

### ❌ "Error: Text content does not match"
✅ Hydration mismatch (check client/server differences)

### ❌ "Error: Cannot read properties of undefined"
✅ Check async/await in Server Components

### ❌ "Error: Cookies can only be modified in Server Actions"
✅ Move cookie logic para Server Action

### ❌ Build muito lento
✅ next.config.js → swcMinify: true
✅ Remova dependências não usadas
✅ Use output: 'standalone' para Docker

### ❌ Bundle muito grande
✅ Análise: npm install @next/bundle-analyzer
✅ Code splitting (dynamic imports)
✅ Remove unused imports

### ❌ ISR não funciona
✅ Check revalidate config
✅ Verifique cache headers
✅ Use tags para on-demand revalidation
```

---

## 🎯 **Conclusão**

### **Next.js 14/15** em resumo:

✅ **App Router**: File-based routing moderno  
✅ **Server Components**: Zero JS no cliente  
✅ **Server Actions**: Backend integrado  
✅ **Data Fetching**: Fetch estendido com cache  
✅ **Caching**: Múltiplas camadas (Request, Data, Route)  
✅ **Streaming**: Progressive rendering com Suspense  
✅ **Layouts**: Compartilhamento de UI  
✅ **Route Handlers**: API Routes tipadas  
✅ **Metadata**: SEO first-class  
✅ **Middleware**: Edge runtime  
✅ **Deployment**: Vercel, Docker, Static Export  

**Happy Next.js coding!** ▲🚀

---

