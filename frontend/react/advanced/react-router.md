# 🛣️ **React Router v6 - Guia Completo**

> **React Router v6, protected routes, layouts, nested routes, loaders, actions e navigation**

---

## 📑 **Índice**

1. [Introdução ao React Router v6](#1-introdução-ao-react-router-v6)
2. [Setup e Configuração](#2-setup-e-configuração)
3. [Rotas Básicas](#3-rotas-básicas)
4. [Navegação](#4-navegação)
5. [Nested Routes e Layouts](#5-nested-routes-e-layouts)
6. [Dynamic Routes e Params](#6-dynamic-routes-e-params)
7. [Protected Routes](#7-protected-routes)
8. [Loaders e Actions](#8-loaders-e-actions)
9. [Error Handling](#9-error-handling)
10. [Search Params](#10-search-params)
11. [Programmatic Navigation](#11-programmatic-navigation)
12. [Route Guards](#12-route-guards)
13. [Code Splitting com Routes](#13-code-splitting-com-routes)
14. [Advanced Patterns](#14-advanced-patterns)
15. [Recursos e Referências](#15-recursos-e-referências)

---

## 1. 🎯 **Introdução ao React Router v6**

### 1.1. O que é React Router?

**React Router** é a biblioteca padrão para **routing** em aplicações React.

**Principais conceitos:**

```
┌─────────────────────────────────────────────────────────┐
│ ROUTING NO REACT ROUTER                                 │
├─────────────────────────────────────────────────────────┤
│                                                         │
│  URL: /dashboard/profile/123                            │
│                                                         │
│  ┌─────────────┐                                        │
│  │   Router    │  Sincroniza URL com UI                 │
│  └─────────────┘                                        │
│         │                                               │
│         ├─ Route: /dashboard                            │
│         │    └─ Component: DashboardLayout              │
│         │                                               │
│         ├─ Route: /dashboard/profile                    │
│         │    └─ Component: ProfilePage                  │
│         │                                               │
│         └─ Route: /dashboard/profile/:id                │
│              └─ Component: ProfileDetails               │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

### 1.2. React Router v6 vs v5

**Mudanças principais:**

```tsx
// ═══════════════════════════════════════════════════════════
// V5 (OLD)
// ═══════════════════════════════════════════════════════════

import { BrowserRouter, Switch, Route } from 'react-router-dom';

<BrowserRouter>
  <Switch>
    <Route exact path="/" component={Home} />
    <Route path="/about" component={About} />
  </Switch>
</BrowserRouter>

// ═══════════════════════════════════════════════════════════
// V6 (NEW)
// ═══════════════════════════════════════════════════════════

import { BrowserRouter, Routes, Route } from 'react-router-dom';

<BrowserRouter>
  <Routes>
    <Route path="/" element={<Home />} />
    <Route path="/about" element={<About />} />
  </Routes>
</BrowserRouter>

// ✅ DIFERENÇAS:
// - Switch → Routes
// - component → element (JSX direto)
// - Sem "exact" (comportamento padrão)
// - Nested routes mais simples
// - Hooks melhores (useNavigate, useParams)
```

**Tabela de mudanças:**

| v5 | v6 | Mudança |
|----|----|----|
| `<Switch>` | `<Routes>` | Novo nome + comportamento |
| `component={Home}` | `element={<Home />}` | JSX direto |
| `exact path="/"` | `path="/"` | Exact é padrão |
| `<Redirect>` | `<Navigate>` | Novo componente |
| `useHistory()` | `useNavigate()` | Nova API |
| `useRouteMatch()` | `useMatch()` | Simplificado |

### 1.3. Quando Usar React Router

**✅ USE quando:**
- SPA (Single Page Application)
- Múltiplas páginas/views
- Navegação sem reload
- URLs amigáveis (/user/123)
- Histórico de navegação

**❌ NÃO use quando:**
- App de uma página única (sem navegação)
- Usar Next.js (tem routing próprio)
- Aplicação muito simples

---

## 2. ⚙️ **Setup e Configuração**

### 2.1. Instalação

```bash
# React Router v6
npm install react-router-dom

# TypeScript types (incluídos por padrão v6.4+)
npm install --save-dev @types/react-router-dom
```

### 2.2. Setup Básico

```tsx
// ═══════════════════════════════════════════════════════════
// SETUP BÁSICO
// ═══════════════════════════════════════════════════════════

// main.tsx ou index.tsx
import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import { BrowserRouter } from 'react-router-dom';
import App from './App';

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <BrowserRouter>
      <App />
    </BrowserRouter>
  </StrictMode>
);

// App.tsx
import { Routes, Route } from 'react-router-dom';
import Home from './pages/Home';
import About from './pages/About';
import NotFound from './pages/NotFound';

function App() {
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/about" element={<About />} />
      <Route path="*" element={<NotFound />} />
    </Routes>
  );
}

export default App;
```

### 2.3. Router Types

```tsx
// ═══════════════════════════════════════════════════════════
// TIPOS DE ROUTER
// ═══════════════════════════════════════════════════════════

// ✅ BrowserRouter: Usa HTML5 history API
// URLs: /home, /about
import { BrowserRouter } from 'react-router-dom';

<BrowserRouter>
  <App />
</BrowserRouter>

// ✅ HashRouter: Usa hash (#) na URL
// URLs: /#/home, /#/about
// Use apenas se não puder configurar servidor
import { HashRouter } from 'react-router-dom';

<HashRouter>
  <App />
</HashRouter>

// ✅ MemoryRouter: Não usa URL (testing)
import { MemoryRouter } from 'react-router-dom';

<MemoryRouter initialEntries={['/home']}>
  <App />
</MemoryRouter>

// ✅ StaticRouter: Server-side rendering
import { StaticRouter } from 'react-router-dom/server';

<StaticRouter location="/home">
  <App />
</StaticRouter>
```

### 2.4. Configuração do Servidor (SPA)

**Vite (vite.config.ts):**

```typescript
// ═══════════════════════════════════════════════════════════
// VITE CONFIG para SPA
// ═══════════════════════════════════════════════════════════

import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig({
  plugins: [react()],
  server: {
    // ✅ Redireciona todas rotas para index.html (SPA)
    historyApiFallback: true
  }
});
```

**Nginx:**

```nginx
# ═══════════════════════════════════════════════════════════
# NGINX CONFIG para SPA
# ═══════════════════════════════════════════════════════════

server {
  listen 80;
  server_name example.com;
  root /var/www/html;
  index index.html;

  # Redireciona todas rotas para index.html
  location / {
    try_files $uri $uri/ /index.html;
  }
}
```

**Apache (.htaccess):**

```apache
# ═══════════════════════════════════════════════════════════
# APACHE CONFIG para SPA
# ═══════════════════════════════════════════════════════════

<IfModule mod_rewrite.c>
  RewriteEngine On
  RewriteBase /
  RewriteRule ^index\.html$ - [L]
  RewriteCond %{REQUEST_FILENAME} !-f
  RewriteCond %{REQUEST_FILENAME} !-d
  RewriteRule . /index.html [L]
</IfModule>
```

---

## 3. 🛤️ **Rotas Básicas**

### 3.1. Definindo Rotas

```tsx
// ═══════════════════════════════════════════════════════════
// ROTAS BÁSICAS
// ═══════════════════════════════════════════════════════════

import { Routes, Route } from 'react-router-dom';

function App() {
  return (
    <Routes>
      {/* Rota raiz */}
      <Route path="/" element={<Home />} />

      {/* Rotas estáticas */}
      <Route path="/about" element={<About />} />
      <Route path="/contact" element={<Contact />} />
      <Route path="/pricing" element={<Pricing />} />

      {/* Rota 404 (catch-all) */}
      <Route path="*" element={<NotFound />} />
    </Routes>
  );
}
```

### 3.2. Index Routes

```tsx
// ═══════════════════════════════════════════════════════════
// INDEX ROUTES
// ═══════════════════════════════════════════════════════════

<Routes>
  <Route path="/dashboard" element={<DashboardLayout />}>
    {/* ✅ Renderiza em /dashboard (sem path adicional) */}
    <Route index element={<DashboardHome />} />
    
    <Route path="profile" element={<Profile />} />
    <Route path="settings" element={<Settings />} />
  </Route>
</Routes>

// URLs:
// /dashboard          → DashboardLayout + DashboardHome (index)
// /dashboard/profile  → DashboardLayout + Profile
// /dashboard/settings → DashboardLayout + Settings
```

### 3.3. Route Props

```tsx
// ═══════════════════════════════════════════════════════════
// ROUTE PROPS
// ═══════════════════════════════════════════════════════════

<Route
  path="/users"
  element={<Users />}
  // ✅ caseSensitive: URLs case-sensitive (default: false)
  caseSensitive={true}
/>

// caseSensitive={false}: /users = /Users = /USERS
// caseSensitive={true}:  /users ≠ /Users
```

---

## 4. 🧭 **Navegação**

### 4.1. Link Component

```tsx
// ═══════════════════════════════════════════════════════════
// LINK: Navegação declarativa
// ═══════════════════════════════════════════════════════════

import { Link } from 'react-router-dom';

function Navigation() {
  return (
    <nav>
      {/* ✅ Link básico */}
      <Link to="/">Home</Link>
      <Link to="/about">About</Link>

      {/* ✅ Link absoluto */}
      <Link to="/contact">Contact</Link>

      {/* ✅ Link relativo */}
      <Link to="settings">Settings</Link>  {/* Relativo à rota atual */}
      <Link to="../back">Back</Link>

      {/* ✅ Link com state */}
      <Link to="/login" state={{ from: '/dashboard' }}>
        Login
      </Link>

      {/* ✅ Replace (não adiciona ao histórico) */}
      <Link to="/login" replace>
        Login
      </Link>

      {/* ✅ Link com props HTML */}
      <Link to="/external" target="_blank" rel="noopener">
        External
      </Link>
    </nav>
  );
}
```

### 4.2. NavLink Component

```tsx
// ═══════════════════════════════════════════════════════════
// NAVLINK: Link com active state
// ═══════════════════════════════════════════════════════════

import { NavLink } from 'react-router-dom';

function Navigation() {
  return (
    <nav>
      {/* ✅ className como função */}
      <NavLink
        to="/"
        className={({ isActive }) => (isActive ? 'active' : '')}
      >
        Home
      </NavLink>

      {/* ✅ style como função */}
      <NavLink
        to="/about"
        style={({ isActive }) => ({
          color: isActive ? 'red' : 'black',
          fontWeight: isActive ? 'bold' : 'normal'
        })}
      >
        About
      </NavLink>

      {/* ✅ Render props */}
      <NavLink to="/contact">
        {({ isActive }) => (
          <span>
            {isActive && '👉 '}
            Contact
          </span>
        )}
      </NavLink>

      {/* ✅ end prop: Ativa apenas em match exato */}
      <NavLink to="/" end>
        Home
      </NavLink>
      {/* Sem end: ativa em /, /about, /contact */}
      {/* Com end: ativa apenas em / */}
    </nav>
  );
}

// CSS
/*
.active {
  color: blue;
  font-weight: bold;
  border-bottom: 2px solid blue;
}
*/
```

### 4.3. Navigate Component

```tsx
// ═══════════════════════════════════════════════════════════
// NAVIGATE: Redirecionamento declarativo
// ═══════════════════════════════════════════════════════════

import { Navigate } from 'react-router-dom';

// ✅ Redirect simples
function OldAboutPage() {
  return <Navigate to="/about" />;
}

// ✅ Redirect com replace (não adiciona ao histórico)
function RedirectToHome() {
  return <Navigate to="/" replace />;
}

// ✅ Redirect condicional
function ProtectedPage() {
  const isAuthenticated = useAuth();

  if (!isAuthenticated) {
    return <Navigate to="/login" state={{ from: location }} />;
  }

  return <div>Protected Content</div>;
}

// ✅ Redirect com state
<Navigate to="/dashboard" state={{ message: 'Login successful' }} />
```

---

## 5. 🏗️ **Nested Routes e Layouts**

### 5.1. Nested Routes Básico

```tsx
// ═══════════════════════════════════════════════════════════
// NESTED ROUTES
// ═══════════════════════════════════════════════════════════

import { Routes, Route, Outlet } from 'react-router-dom';

// Layout component
function DashboardLayout() {
  return (
    <div>
      <header>Dashboard Header</header>
      <nav>
        <Link to="/dashboard">Home</Link>
        <Link to="/dashboard/profile">Profile</Link>
        <Link to="/dashboard/settings">Settings</Link>
      </nav>
      
      {/* ✅ Outlet: Renderiza rota filha */}
      <main>
        <Outlet />
      </main>
      
      <footer>Dashboard Footer</footer>
    </div>
  );
}

// App
function App() {
  return (
    <Routes>
      <Route path="/dashboard" element={<DashboardLayout />}>
        <Route index element={<DashboardHome />} />
        <Route path="profile" element={<Profile />} />
        <Route path="settings" element={<Settings />} />
      </Route>
    </Routes>
  );
}

// URLs e Renderização:
// /dashboard          → DashboardLayout + DashboardHome
// /dashboard/profile  → DashboardLayout + Profile
// /dashboard/settings → DashboardLayout + Settings
```

### 5.2. Multiple Layouts

```tsx
// ═══════════════════════════════════════════════════════════
// MÚLTIPLOS LAYOUTS
// ═══════════════════════════════════════════════════════════

// Public Layout (Header + Footer)
function PublicLayout() {
  return (
    <div>
      <Header />
      <Outlet />
      <Footer />
    </div>
  );
}

// Dashboard Layout (Sidebar + Content)
function DashboardLayout() {
  return (
    <div className="dashboard">
      <Sidebar />
      <main>
        <Outlet />
      </main>
    </div>
  );
}

// Auth Layout (Centered)
function AuthLayout() {
  return (
    <div className="auth-container">
      <Outlet />
    </div>
  );
}

// App
function App() {
  return (
    <Routes>
      {/* Public routes */}
      <Route element={<PublicLayout />}>
        <Route path="/" element={<Home />} />
        <Route path="/about" element={<About />} />
        <Route path="/pricing" element={<Pricing />} />
      </Route>

      {/* Auth routes */}
      <Route element={<AuthLayout />}>
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/forgot-password" element={<ForgotPassword />} />
      </Route>

      {/* Dashboard routes */}
      <Route element={<DashboardLayout />}>
        <Route path="/dashboard" element={<DashboardHome />} />
        <Route path="/dashboard/profile" element={<Profile />} />
        <Route path="/dashboard/settings" element={<Settings />} />
      </Route>
    </Routes>
  );
}
```

### 5.3. Deeply Nested Routes

```tsx
// ═══════════════════════════════════════════════════════════
// ROTAS PROFUNDAMENTE ANINHADAS
// ═══════════════════════════════════════════════════════════

function App() {
  return (
    <Routes>
      {/* 3 níveis de nesting */}
      <Route path="/app" element={<AppLayout />}>
        <Route path="dashboard" element={<DashboardLayout />}>
          <Route index element={<DashboardHome />} />
          
          <Route path="analytics" element={<AnalyticsLayout />}>
            <Route index element={<AnalyticsOverview />} />
            <Route path="reports" element={<Reports />} />
            <Route path="charts" element={<Charts />} />
          </Route>
          
          <Route path="settings" element={<SettingsLayout />}>
            <Route index element={<SettingsGeneral />} />
            <Route path="profile" element={<SettingsProfile />} />
            <Route path="security" element={<SettingsSecurity />} />
          </Route>
        </Route>
      </Route>
    </Routes>
  );
}

// URLs:
// /app/dashboard                           → AppLayout + DashboardLayout + DashboardHome
// /app/dashboard/analytics                 → AppLayout + DashboardLayout + AnalyticsLayout + AnalyticsOverview
// /app/dashboard/analytics/reports         → AppLayout + DashboardLayout + AnalyticsLayout + Reports
// /app/dashboard/settings/profile          → AppLayout + DashboardLayout + SettingsLayout + SettingsProfile
```

### 5.4. Outlet Context

```tsx
// ═══════════════════════════════════════════════════════════
// OUTLET CONTEXT: Passar dados para rotas filhas
// ═══════════════════════════════════════════════════════════

import { Outlet, useOutletContext } from 'react-router-dom';

// Layout com context
interface DashboardContextType {
  user: User;
  notifications: number;
}

function DashboardLayout() {
  const [user, setUser] = useState<User | null>(null);
  const [notifications, setNotifications] = useState(0);

  const contextValue: DashboardContextType = {
    user,
    notifications
  };

  return (
    <div>
      <Sidebar />
      <main>
        {/* ✅ Passa context para rotas filhas */}
        <Outlet context={contextValue} />
      </main>
    </div>
  );
}

// Rota filha usa context
function Profile() {
  const { user, notifications } = useOutletContext<DashboardContextType>();

  return (
    <div>
      <h1>{user?.name}</h1>
      <p>Notifications: {notifications}</p>
    </div>
  );
}
```

---

## 6. 🔀 **Dynamic Routes e Params**

### 6.1. URL Parameters

```tsx
// ═══════════════════════════════════════════════════════════
// URL PARAMS
// ═══════════════════════════════════════════════════════════

import { useParams } from 'react-router-dom';

// App
<Routes>
  <Route path="/users/:userId" element={<UserProfile />} />
  <Route path="/posts/:postId/comments/:commentId" element={<Comment />} />
</Routes>

// Component
function UserProfile() {
  const { userId } = useParams();  // ✅ string | undefined

  return <div>User ID: {userId}</div>;
}

// TypeScript
function UserProfile() {
  const { userId } = useParams<{ userId: string }>();

  // userId é string (pode ser undefined se rota não match)
  
  return <div>User ID: {userId}</div>;
}
```

### 6.2. Optional Parameters

```tsx
// ═══════════════════════════════════════════════════════════
// PARÂMETROS OPCIONAIS
// ═══════════════════════════════════════════════════════════

// ✅ OPÇÃO 1: Duas rotas
<Routes>
  <Route path="/users" element={<UserList />} />
  <Route path="/users/:userId" element={<UserProfile />} />
</Routes>

// ✅ OPÇÃO 2: Query params (melhor para opcionais)
<Routes>
  <Route path="/users" element={<UserList />} />
</Routes>

// Usar: /users?id=123
function UserList() {
  const [searchParams] = useSearchParams();
  const userId = searchParams.get('id');

  if (userId) {
    return <UserProfile userId={userId} />;
  }

  return <div>All users</div>;
}
```

### 6.3. Wildcard Routes

```tsx
// ═══════════════════════════════════════════════════════════
// WILDCARD ROUTES
// ═══════════════════════════════════════════════════════════

<Routes>
  {/* Captura tudo depois de /files/ */}
  <Route path="/files/*" element={<FileExplorer />} />
</Routes>

function FileExplorer() {
  const location = useLocation();
  
  // location.pathname: /files/documents/report.pdf
  const filePath = location.pathname.replace('/files/', '');
  // filePath: documents/report.pdf

  return <div>File: {filePath}</div>;
}

// ✅ Alternativa: Usar splat
<Routes>
  <Route path="/files/:path*" element={<FileExplorer />} />
</Routes>

function FileExplorer() {
  const { path } = useParams();  // documents/report.pdf
  
  return <div>File: {path}</div>;
}
```

### 6.4. Multiple Params

```tsx
// ═══════════════════════════════════════════════════════════
// MÚLTIPLOS PARAMS
// ═══════════════════════════════════════════════════════════

<Routes>
  <Route
    path="/users/:userId/posts/:postId"
    element={<PostDetail />}
  />
</Routes>

function PostDetail() {
  const { userId, postId } = useParams<{
    userId: string;
    postId: string;
  }>();

  return (
    <div>
      <p>User: {userId}</p>
      <p>Post: {postId}</p>
    </div>
  );
}

// URL: /users/123/posts/456
// userId: "123"
// postId: "456"
```

---

## 7. 🔒 **Protected Routes**

### 7.1. Protected Route Component

```tsx
// ═══════════════════════════════════════════════════════════
// PROTECTED ROUTE COMPONENT
// ═══════════════════════════════════════════════════════════

import { Navigate, useLocation } from 'react-router-dom';

interface ProtectedRouteProps {
  children: React.ReactNode;
}

function ProtectedRoute({ children }: ProtectedRouteProps) {
  const { isAuthenticated } = useAuth();
  const location = useLocation();

  if (!isAuthenticated) {
    // ✅ Redireciona para login, salvando URL tentada
    return <Navigate to="/login" state={{ from: location }} replace />;
  }

  return <>{children}</>;
}

// Uso
<Routes>
  <Route path="/login" element={<Login />} />
  
  <Route
    path="/dashboard"
    element={
      <ProtectedRoute>
        <Dashboard />
      </ProtectedRoute>
    }
  />
</Routes>
```

### 7.2. Protected Route com Layout

```tsx
// ═══════════════════════════════════════════════════════════
// PROTECTED LAYOUT
// ═══════════════════════════════════════════════════════════

function ProtectedLayout() {
  const { isAuthenticated, loading } = useAuth();
  const location = useLocation();

  if (loading) {
    return <LoadingSpinner />;
  }

  if (!isAuthenticated) {
    return <Navigate to="/login" state={{ from: location }} replace />;
  }

  return (
    <div>
      <Sidebar />
      <main>
        <Outlet />
      </main>
    </div>
  );
}

// Uso
<Routes>
  {/* Public routes */}
  <Route path="/login" element={<Login />} />
  
  {/* Protected routes */}
  <Route element={<ProtectedLayout />}>
    <Route path="/dashboard" element={<Dashboard />} />
    <Route path="/profile" element={<Profile />} />
    <Route path="/settings" element={<Settings />} />
  </Route>
</Routes>
```

### 7.3. Role-Based Protection

```tsx
// ═══════════════════════════════════════════════════════════
// ROLE-BASED PROTECTED ROUTES
// ═══════════════════════════════════════════════════════════

interface RequireRoleProps {
  allowedRoles: string[];
  children: React.ReactNode;
}

function RequireRole({ allowedRoles, children }: RequireRoleProps) {
  const { user, isAuthenticated } = useAuth();
  const location = useLocation();

  if (!isAuthenticated) {
    return <Navigate to="/login" state={{ from: location }} replace />;
  }

  const hasRole = user?.roles?.some(role => allowedRoles.includes(role));

  if (!hasRole) {
    return <Navigate to="/unauthorized" replace />;
  }

  return <>{children}</>;
}

// Uso
<Routes>
  <Route
    path="/admin"
    element={
      <RequireRole allowedRoles={['admin']}>
        <AdminPanel />
      </RequireRole>
    }
  />

  <Route
    path="/moderator"
    element={
      <RequireRole allowedRoles={['admin', 'moderator']}>
        <ModeratorPanel />
      </RequireRole>
    }
  />
</Routes>
```

### 7.4. Permission-Based Protection

```tsx
// ═══════════════════════════════════════════════════════════
// PERMISSION-BASED PROTECTION
// ═══════════════════════════════════════════════════════════

type Permission = 'read' | 'write' | 'delete' | 'admin';

interface RequirePermissionProps {
  permission: Permission;
  children: React.ReactNode;
}

function RequirePermission({ permission, children }: RequirePermissionProps) {
  const { user, isAuthenticated } = useAuth();

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  const hasPermission = user?.permissions?.includes(permission);

  if (!hasPermission) {
    return (
      <div>
        <h1>Access Denied</h1>
        <p>You don't have permission: {permission}</p>
      </div>
    );
  }

  return <>{children}</>;
}

// Uso
<Route
  path="/users/delete"
  element={
    <RequirePermission permission="delete">
      <DeleteUsers />
    </RequirePermission>
  }
/>
```

---

## 8. 📦 **Loaders e Actions**

### 8.1. Data Loader Pattern

```tsx
// ═══════════════════════════════════════════════════════════
// DATA LOADERS (React Router v6.4+)
// ═══════════════════════════════════════════════════════════

import { createBrowserRouter, useLoaderData } from 'react-router-dom';

// ✅ Loader function
async function userLoader({ params }: { params: { userId: string } }) {
  const response = await fetch(`/api/users/${params.userId}`);
  
  if (!response.ok) {
    throw new Response('User not found', { status: 404 });
  }
  
  return response.json();
}

// Component
function UserProfile() {
  // ✅ Data já carregada antes do render
  const user = useLoaderData() as User;

  return (
    <div>
      <h1>{user.name}</h1>
      <p>{user.email}</p>
    </div>
  );
}

// Router config
const router = createBrowserRouter([
  {
    path: '/users/:userId',
    element: <UserProfile />,
    loader: userLoader
  }
]);
```

### 8.2. Loader com TypeScript

```tsx
// ═══════════════════════════════════════════════════════════
// TYPED LOADERS
// ═══════════════════════════════════════════════════════════

import type { LoaderFunctionArgs } from 'react-router-dom';

interface User {
  id: number;
  name: string;
  email: string;
}

// ✅ Typed loader
async function userLoader({ params }: LoaderFunctionArgs): Promise<User> {
  const response = await fetch(`/api/users/${params.userId}`);
  return response.json();
}

// Component com type inference
function UserProfile() {
  const user = useLoaderData() as Awaited<ReturnType<typeof userLoader>>;
  //    ✅ Type: User

  return <div>{user.name}</div>;
}
```

### 8.3. Multiple Loaders (Parallel)

```tsx
// ═══════════════════════════════════════════════════════════
// MÚLTIPLOS LOADERS (Paralelo)
// ═══════════════════════════════════════════════════════════

async function dashboardLoader() {
  // ✅ Carrega dados em paralelo
  const [user, stats, notifications] = await Promise.all([
    fetch('/api/user').then(r => r.json()),
    fetch('/api/stats').then(r => r.json()),
    fetch('/api/notifications').then(r => r.json())
  ]);

  return { user, stats, notifications };
}

function Dashboard() {
  const { user, stats, notifications } = useLoaderData() as Awaited<
    ReturnType<typeof dashboardLoader>
  >;

  return (
    <div>
      <h1>Welcome, {user.name}</h1>
      <p>Stats: {stats.total}</p>
      <p>Notifications: {notifications.length}</p>
    </div>
  );
}
```

### 8.4. Loader with Dependencies

```tsx
// ═══════════════════════════════════════════════════════════
// LOADER COM DEPENDÊNCIAS
// ═══════════════════════════════════════════════════════════

async function postLoader({ params }: LoaderFunctionArgs) {
  // 1. Carrega post
  const post = await fetch(`/api/posts/${params.postId}`).then(r => r.json());

  // 2. Carrega comments (depende do post)
  const comments = await fetch(`/api/posts/${post.id}/comments`).then(r => r.json());

  return { post, comments };
}

function PostDetail() {
  const { post, comments } = useLoaderData() as Awaited<
    ReturnType<typeof postLoader>
  >;

  return (
    <div>
      <h1>{post.title}</h1>
      <p>{post.content}</p>
      
      <h2>Comments ({comments.length})</h2>
      {comments.map(comment => (
        <div key={comment.id}>{comment.text}</div>
      ))}
    </div>
  );
}
```

### 8.5. Actions (Form Submissions)

```tsx
// ═══════════════════════════════════════════════════════════
// ACTIONS: Form submissions
// ═══════════════════════════════════════════════════════════

import { Form, redirect, useActionData } from 'react-router-dom';
import type { ActionFunctionArgs } from 'react-router-dom';

// ✅ Action function
async function createUserAction({ request }: ActionFunctionArgs) {
  const formData = await request.formData();
  
  const user = {
    name: formData.get('name') as string,
    email: formData.get('email') as string
  };

  try {
    const response = await fetch('/api/users', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(user)
    });

    if (!response.ok) {
      return { error: 'Failed to create user' };
    }

    // ✅ Redirect após sucesso
    return redirect('/users');
  } catch (error) {
    return { error: 'Network error' };
  }
}

// Component
function CreateUser() {
  const actionData = useActionData() as { error?: string } | undefined;

  return (
    <div>
      <h1>Create User</h1>
      
      {/* ✅ Form component (não <form>) */}
      <Form method="post">
        <input name="name" placeholder="Name" required />
        <input name="email" type="email" placeholder="Email" required />
        <button type="submit">Create</button>
      </Form>

      {actionData?.error && (
        <p style={{ color: 'red' }}>{actionData.error}</p>
      )}
    </div>
  );
}

// Router config
const router = createBrowserRouter([
  {
    path: '/users/create',
    element: <CreateUser />,
    action: createUserAction
  }
]);
```

### 8.6. Optimistic UI com Actions

```tsx
// ═══════════════════════════════════════════════════════════
// OPTIMISTIC UI
// ═══════════════════════════════════════════════════════════

import { useFetcher } from 'react-router-dom';

function TodoItem({ todo }: { todo: Todo }) {
  const fetcher = useFetcher();

  // ✅ Optimistic state
  const isCompleted = fetcher.formData
    ? fetcher.formData.get('completed') === 'true'
    : todo.completed;

  return (
    <fetcher.Form method="post" action={`/todos/${todo.id}/toggle`}>
      <input
        type="checkbox"
        name="completed"
        value="true"
        checked={isCompleted}
        onChange={(e) => fetcher.submit(e.currentTarget.form)}
      />
      <span style={{ textDecoration: isCompleted ? 'line-through' : 'none' }}>
        {todo.text}
      </span>
    </fetcher.Form>
  );
}

// Action
async function toggleTodoAction({ params, request }: ActionFunctionArgs) {
  const formData = await request.formData();
  const completed = formData.get('completed') === 'true';

  await fetch(`/api/todos/${params.todoId}`, {
    method: 'PATCH',
    body: JSON.stringify({ completed })
  });

  return null;
}
```

### 8.7. Revalidation

```tsx
// ═══════════════════════════════════════════════════════════
// REVALIDATION: Recarregar dados
// ═══════════════════════════════════════════════════════════

import { useRevalidator } from 'react-router-dom';

function UserProfile() {
  const user = useLoaderData() as User;
  const revalidator = useRevalidator();

  const handleRefresh = () => {
    // ✅ Revalida loader (recarrega dados)
    revalidator.revalidate();
  };

  return (
    <div>
      <h1>{user.name}</h1>
      <button onClick={handleRefresh}>
        Refresh {revalidator.state === 'loading' && '...'}
      </button>
    </div>
  );
}
```

---

## 9. 🚨 **Error Handling**

### 9.1. Error Boundary

```tsx
// ═══════════════════════════════════════════════════════════
// ERROR BOUNDARY
// ═══════════════════════════════════════════════════════════

import { useRouteError, isRouteErrorResponse } from 'react-router-dom';

function ErrorBoundary() {
  const error = useRouteError();

  if (isRouteErrorResponse(error)) {
    // ✅ Erro de Response (404, 500, etc)
    return (
      <div>
        <h1>{error.status} {error.statusText}</h1>
        <p>{error.data}</p>
      </div>
    );
  }

  if (error instanceof Error) {
    // ✅ Erro JS normal
    return (
      <div>
        <h1>Error</h1>
        <p>{error.message}</p>
        <pre>{error.stack}</pre>
      </div>
    );
  }

  return <div>Unknown error</div>;
}

// Router config
const router = createBrowserRouter([
  {
    path: '/users/:userId',
    element: <UserProfile />,
    errorElement: <ErrorBoundary />,
    loader: userLoader
  }
]);
```

### 9.2. Nested Error Boundaries

```tsx
// ═══════════════════════════════════════════════════════════
// NESTED ERROR BOUNDARIES
// ═══════════════════════════════════════════════════════════

const router = createBrowserRouter([
  {
    path: '/',
    element: <Root />,
    errorElement: <RootErrorBoundary />,  // ✅ Erro na raiz
    children: [
      {
        path: 'dashboard',
        element: <Dashboard />,
        errorElement: <DashboardErrorBoundary />,  // ✅ Erro no dashboard
        children: [
          {
            path: 'profile',
            element: <Profile />,
            errorElement: <ProfileErrorBoundary />  // ✅ Erro no profile
          }
        ]
      }
    ]
  }
]);

// Erro em Profile → ProfileErrorBoundary
// Erro em Dashboard → DashboardErrorBoundary
// Erro em Root → RootErrorBoundary
```

### 9.3. Custom Error Types

```tsx
// ═══════════════════════════════════════════════════════════
// CUSTOM ERROR TYPES
// ═══════════════════════════════════════════════════════════

class NotFoundError extends Error {
  constructor(resource: string) {
    super(`${resource} not found`);
    this.name = 'NotFoundError';
  }
}

class UnauthorizedError extends Error {
  constructor() {
    super('Unauthorized');
    this.name = 'UnauthorizedError';
  }
}

// Loader
async function userLoader({ params }: LoaderFunctionArgs) {
  const response = await fetch(`/api/users/${params.userId}`);

  if (response.status === 404) {
    throw new NotFoundError('User');
  }

  if (response.status === 401) {
    throw new UnauthorizedError();
  }

  return response.json();
}

// Error Boundary
function ErrorBoundary() {
  const error = useRouteError();

  if (error instanceof NotFoundError) {
    return (
      <div>
        <h1>404</h1>
        <p>{error.message}</p>
        <Link to="/">Go Home</Link>
      </div>
    );
  }

  if (error instanceof UnauthorizedError) {
    return <Navigate to="/login" />;
  }

  return <div>Error: {error.message}</div>;
}
```

### 9.4. Error Recovery

```tsx
// ═══════════════════════════════════════════════════════════
// ERROR RECOVERY
// ═══════════════════════════════════════════════════════════

import { useNavigate, useRevalidator } from 'react-router-dom';

function ErrorBoundary() {
  const error = useRouteError();
  const navigate = useNavigate();
  const revalidator = useRevalidator();

  const handleRetry = () => {
    // ✅ Tenta recarregar dados
    revalidator.revalidate();
  };

  const handleGoBack = () => {
    navigate(-1);
  };

  return (
    <div>
      <h1>Something went wrong</h1>
      <p>{error.message}</p>
      
      <button onClick={handleRetry}>Try Again</button>
      <button onClick={handleGoBack}>Go Back</button>
      <button onClick={() => navigate('/')}>Go Home</button>
    </div>
  );
}
```

---

## 10. 🔍 **Search Params**

### 10.1. useSearchParams Hook

```tsx
// ═══════════════════════════════════════════════════════════
// SEARCH PARAMS (Query String)
// ═══════════════════════════════════════════════════════════

import { useSearchParams } from 'react-router-dom';

function SearchPage() {
  const [searchParams, setSearchParams] = useSearchParams();

  // ✅ Ler params
  const query = searchParams.get('q');         // /search?q=react
  const page = searchParams.get('page');       // /search?page=2
  const filter = searchParams.get('filter');   // /search?filter=recent

  // ✅ Setar params
  const handleSearch = (value: string) => {
    setSearchParams({ q: value });
    // URL: /search?q=value
  };

  // ✅ Adicionar param (mantém existentes)
  const handlePageChange = (page: number) => {
    setSearchParams(prev => {
      prev.set('page', page.toString());
      return prev;
    });
  };

  // ✅ Remover param
  const handleClearFilter = () => {
    setSearchParams(prev => {
      prev.delete('filter');
      return prev;
    });
  };

  return (
    <div>
      <input
        value={query || ''}
        onChange={(e) => handleSearch(e.target.value)}
        placeholder="Search..."
      />

      <p>Page: {page || 1}</p>
      <button onClick={() => handlePageChange(Number(page || 1) + 1)}>
        Next Page
      </button>
    </div>
  );
}
```

### 10.2. Multiple Search Params

```tsx
// ═══════════════════════════════════════════════════════════
// MÚLTIPLOS SEARCH PARAMS
// ═══════════════════════════════════════════════════════════

function ProductList() {
  const [searchParams, setSearchParams] = useSearchParams();

  const filters = {
    category: searchParams.get('category'),
    minPrice: searchParams.get('minPrice'),
    maxPrice: searchParams.get('maxPrice'),
    sort: searchParams.get('sort')
  };

  const handleFilterChange = (key: string, value: string) => {
    setSearchParams(prev => {
      if (value) {
        prev.set(key, value);
      } else {
        prev.delete(key);
      }
      return prev;
    });
  };

  return (
    <div>
      {/* Category filter */}
      <select
        value={filters.category || ''}
        onChange={(e) => handleFilterChange('category', e.target.value)}
      >
        <option value="">All Categories</option>
        <option value="electronics">Electronics</option>
        <option value="books">Books</option>
      </select>

      {/* Price filter */}
      <input
        type="number"
        value={filters.minPrice || ''}
        onChange={(e) => handleFilterChange('minPrice', e.target.value)}
        placeholder="Min Price"
      />
      <input
        type="number"
        value={filters.maxPrice || ''}
        onChange={(e) => handleFilterChange('maxPrice', e.target.value)}
        placeholder="Max Price"
      />

      {/* Sort */}
      <select
        value={filters.sort || ''}
        onChange={(e) => handleFilterChange('sort', e.target.value)}
      >
        <option value="">Default</option>
        <option value="price-asc">Price: Low to High</option>
        <option value="price-desc">Price: High to Low</option>
      </select>
    </div>
  );
}

// URL: /products?category=electronics&minPrice=100&maxPrice=1000&sort=price-asc
```

### 10.3. Search Params com Loader

```tsx
// ═══════════════════════════════════════════════════════════
// LOADER com SEARCH PARAMS
// ═══════════════════════════════════════════════════════════

async function productsLoader({ request }: LoaderFunctionArgs) {
  const url = new URL(request.url);
  const searchParams = url.searchParams;

  const category = searchParams.get('category');
  const minPrice = searchParams.get('minPrice');
  const sort = searchParams.get('sort');

  // Construir query para API
  const apiParams = new URLSearchParams();
  if (category) apiParams.set('category', category);
  if (minPrice) apiParams.set('minPrice', minPrice);
  if (sort) apiParams.set('sort', sort);

  const response = await fetch(`/api/products?${apiParams}`);
  return response.json();
}

function ProductList() {
  const products = useLoaderData() as Product[];
  const [searchParams, setSearchParams] = useSearchParams();

  return (
    <div>
      <div>
        {/* Filtros (mudam search params → revalida loader) */}
        <select
          value={searchParams.get('category') || ''}
          onChange={(e) => {
            setSearchParams(prev => {
              if (e.target.value) {
                prev.set('category', e.target.value);
              } else {
                prev.delete('category');
              }
              return prev;
            });
          }}
        >
          <option value="">All</option>
          <option value="electronics">Electronics</option>
        </select>
      </div>

      <div>
        {products.map(product => (
          <div key={product.id}>{product.name}</div>
        ))}
      </div>
    </div>
  );
}
```

### 10.4. Type-Safe Search Params

```tsx
// ═══════════════════════════════════════════════════════════
// TYPE-SAFE SEARCH PARAMS
// ═══════════════════════════════════════════════════════════

import { z } from 'zod';

// Schema de validação
const searchParamsSchema = z.object({
  q: z.string().optional(),
  page: z.coerce.number().min(1).default(1),
  limit: z.coerce.number().min(10).max(100).default(20),
  sort: z.enum(['date', 'title', 'relevance']).default('relevance')
});

function useTypedSearchParams() {
  const [searchParams, setSearchParams] = useSearchParams();

  // ✅ Parse e valida
  const params = searchParamsSchema.parse({
    q: searchParams.get('q'),
    page: searchParams.get('page'),
    limit: searchParams.get('limit'),
    sort: searchParams.get('sort')
  });

  return [params, setSearchParams] as const;
}

// Uso
function SearchResults() {
  const [params, setSearchParams] = useTypedSearchParams();

  // ✅ params.page é number (garantido)
  // ✅ params.sort é 'date' | 'title' | 'relevance'

  return (
    <div>
      <p>Page: {params.page}</p>
      <p>Limit: {params.limit}</p>
      <p>Sort: {params.sort}</p>
    </div>
  );
}
```

---

## 11. 🧭 **Programmatic Navigation**

### 11.1. useNavigate Hook

```tsx
// ═══════════════════════════════════════════════════════════
// useNavigate: Navegação programática
// ═══════════════════════════════════════════════════════════

import { useNavigate } from 'react-router-dom';

function LoginForm() {
  const navigate = useNavigate();

  const handleLogin = async (credentials: Credentials) => {
    const success = await login(credentials);

    if (success) {
      // ✅ Navega para dashboard
      navigate('/dashboard');
    }
  };

  return (
    <form onSubmit={(e) => {
      e.preventDefault();
      handleLogin(credentials);
    }}>
      {/* ... */}
    </form>
  );
}
```

### 11.2. Navigate Options

```tsx
// ═══════════════════════════════════════════════════════════
// NAVIGATE OPTIONS
// ═══════════════════════════════════════════════════════════

function Examples() {
  const navigate = useNavigate();

  // ✅ Navegação básica
  navigate('/about');

  // ✅ Replace (não adiciona ao histórico)
  navigate('/login', { replace: true });

  // ✅ Com state
  navigate('/dashboard', {
    state: { message: 'Login successful' }
  });

  // ✅ Navegação relativa
  navigate('..'); // Sobe um nível
  navigate('../sibling'); // Rota irmã

  // ✅ Navegação numérica (histórico)
  navigate(-1);  // Volta
  navigate(-2);  // Volta 2 páginas
  navigate(1);   // Avança

  // ✅ Com hash
  navigate('/docs#section-1');

  // ✅ Com search params
  navigate('/search?q=react');
}
```

### 11.3. Navigate com State

```tsx
// ═══════════════════════════════════════════════════════════
// STATE em navegação
// ═══════════════════════════════════════════════════════════

import { useLocation, useNavigate } from 'react-router-dom';

// Página que navega
function UserList() {
  const navigate = useNavigate();

  const handleUserClick = (user: User) => {
    navigate(`/users/${user.id}`, {
      state: { user }  // ✅ Passa dados via state
    });
  };

  return (
    <div>
      {users.map(user => (
        <button key={user.id} onClick={() => handleUserClick(user)}>
          {user.name}
        </button>
      ))}
    </div>
  );
}

// Página de destino
function UserProfile() {
  const location = useLocation();
  const { user } = location.state as { user: User };

  // ✅ Pode usar user imediatamente (não precisa fetch)
  return <div>{user?.name}</div>;
}
```

### 11.4. Redirect After Action

```tsx
// ═══════════════════════════════════════════════════════════
// REDIRECT APÓS AÇÃO
// ═══════════════════════════════════════════════════════════

function CreatePost() {
  const navigate = useNavigate();

  const handleSubmit = async (data: PostData) => {
    const newPost = await fetch('/api/posts', {
      method: 'POST',
      body: JSON.stringify(data)
    }).then(r => r.json());

    // ✅ Redireciona para post criado
    navigate(`/posts/${newPost.id}`, {
      state: { message: 'Post created successfully!' }
    });
  };

  return <PostForm onSubmit={handleSubmit} />;
}
```

### 11.5. Conditional Navigation

```tsx
// ═══════════════════════════════════════════════════════════
// NAVEGAÇÃO CONDICIONAL
// ═══════════════════════════════════════════════════════════

function useConditionalNavigate() {
  const navigate = useNavigate();
  const location = useLocation();

  const navigateWithFallback = (to: string) => {
    // ✅ Volta se veio de dentro do app, senão vai para fallback
    if (location.key !== 'default') {
      navigate(-1);
    } else {
      navigate(to);
    }
  };

  return navigateWithFallback;
}

// Uso
function DetailPage() {
  const navigateBack = useConditionalNavigate();

  return (
    <div>
      <button onClick={() => navigateBack('/')}>
        Back
      </button>
    </div>
  );
}
```

---

## 12. 🛡️ **Route Guards**

### 12.1. Auth Guard

```tsx
// ═══════════════════════════════════════════════════════════
// AUTH GUARD
// ═══════════════════════════════════════════════════════════

function AuthGuard({ children }: { children: React.ReactNode }) {
  const { isAuthenticated, loading } = useAuth();
  const location = useLocation();

  if (loading) {
    return <LoadingSpinner />;
  }

  if (!isAuthenticated) {
    // ✅ Salva URL tentada para redirect após login
    return <Navigate to="/login" state={{ from: location.pathname }} replace />;
  }

  return <>{children}</>;
}

// Uso
<Route
  path="/dashboard"
  element={
    <AuthGuard>
      <Dashboard />
    </AuthGuard>
  }
/>
```

### 12.2. Role Guard

```tsx
// ═══════════════════════════════════════════════════════════
// ROLE GUARD
// ═══════════════════════════════════════════════════════════

interface RoleGuardProps {
  allowedRoles: string[];
  children: React.ReactNode;
  fallback?: React.ReactNode;
}

function RoleGuard({ allowedRoles, children, fallback }: RoleGuardProps) {
  const { user } = useAuth();

  const hasRole = user?.roles.some(role => allowedRoles.includes(role));

  if (!hasRole) {
    return fallback || <Navigate to="/unauthorized" replace />;
  }

  return <>{children}</>;
}

// Uso
<Route
  path="/admin"
  element={
    <RoleGuard allowedRoles={['admin']}>
      <AdminPanel />
    </RoleGuard>
  }
/>
```

### 12.3. Subscription Guard

```tsx
// ═══════════════════════════════════════════════════════════
// SUBSCRIPTION GUARD
// ═══════════════════════════════════════════════════════════

function SubscriptionGuard({ children }: { children: React.ReactNode }) {
  const { user } = useAuth();

  const hasActiveSubscription = user?.subscription?.status === 'active';

  if (!hasActiveSubscription) {
    return <Navigate to="/pricing" replace />;
  }

  return <>{children}</>;
}

// Uso
<Route
  path="/premium"
  element={
    <SubscriptionGuard>
      <PremiumFeatures />
    </SubscriptionGuard>
  }
/>
```

### 12.4. Composable Guards

```tsx
// ═══════════════════════════════════════════════════════════
// GUARDS COMPOSTOS
// ═══════════════════════════════════════════════════════════

function composeGuards(...guards: React.FC<{ children: React.ReactNode }>[]) {
  return ({ children }: { children: React.ReactNode }) =>
    guards.reduceRight(
      (acc, Guard) => <Guard>{acc}</Guard>,
      children
    );
}

// Uso: Múltiplos guards
const ProtectedAdminRoute = composeGuards(
  AuthGuard,
  ({ children }) => <RoleGuard allowedRoles={['admin']}>{children}</RoleGuard>,
  SubscriptionGuard
);

<Route
  path="/admin/premium"
  element={
    <ProtectedAdminRoute>
      <AdminPremium />
    </ProtectedAdminRoute>
  }
/>
```

---

## 13. ⚡ **Code Splitting com Routes**

### 13.1. Lazy Loading Routes

```tsx
// ═══════════════════════════════════════════════════════════
// LAZY LOADING: Carrega rotas sob demanda
// ═══════════════════════════════════════════════════════════

import { lazy, Suspense } from 'react';
import { createBrowserRouter } from 'react-router-dom';

// ✅ Lazy import
const Dashboard = lazy(() => import('./pages/Dashboard'));
const Profile = lazy(() => import('./pages/Profile'));
const Settings = lazy(() => import('./pages/Settings'));

const router = createBrowserRouter([
  {
    path: '/',
    element: <Root />,
    children: [
      {
        path: 'dashboard',
        element: (
          <Suspense fallback={<LoadingSpinner />}>
            <Dashboard />
          </Suspense>
        )
      },
      {
        path: 'profile',
        element: (
          <Suspense fallback={<LoadingSpinner />}>
            <Profile />
          </Suspense>
        )
      }
    ]
  }
]);
```

### 13.2. Suspense Boundary no Layout

```tsx
// ═══════════════════════════════════════════════════════════
// SUSPENSE no LAYOUT (evita repetição)
// ═══════════════════════════════════════════════════════════

function AppLayout() {
  return (
    <div>
      <Header />
      
      <Suspense fallback={<PageLoader />}>
        {/* ✅ Todas rotas lazy usam esse Suspense */}
        <Outlet />
      </Suspense>
      
      <Footer />
    </div>
  );
}

const router = createBrowserRouter([
  {
    element: <AppLayout />,
    children: [
      {
        path: 'dashboard',
        element: <Dashboard />  // ✅ Não precisa Suspense aqui
      },
      {
        path: 'profile',
        element: <Profile />  // ✅ Nem aqui
      }
    ]
  }
]);
```

### 13.3. Preloading Routes

```tsx
// ═══════════════════════════════════════════════════════════
// PRELOAD: Carrega rota antes do clique
// ═══════════════════════════════════════════════════════════

const DashboardLazy = lazy(() => import('./pages/Dashboard'));

function NavLink() {
  const handleMouseEnter = () => {
    // ✅ Preload on hover
    import('./pages/Dashboard');
  };

  return (
    <Link to="/dashboard" onMouseEnter={handleMouseEnter}>
      Dashboard
    </Link>
  );
}
```

### 13.4. Named Exports com Lazy

```tsx
// ═══════════════════════════════════════════════════════════
// NAMED EXPORTS
// ═══════════════════════════════════════════════════════════

// pages/Dashboard.tsx
export function Dashboard() {
  return <div>Dashboard</div>;
}

// Router
const Dashboard = lazy(() =>
  import('./pages/Dashboard').then(module => ({
    default: module.Dashboard  // ✅ Wrapper para named export
  }))
);
```

### 13.5. Route-Based Splitting

```tsx
// ═══════════════════════════════════════════════════════════
// SPLITTING POR ROTA (Vite bundle analysis)
// ═══════════════════════════════════════════════════════════

// vite.config.ts
export default defineConfig({
  build: {
    rollupOptions: {
      output: {
        manualChunks: {
          // ✅ Vendor chunk
          vendor: ['react', 'react-dom', 'react-router-dom'],
          
          // ✅ Auth chunk (Login + Register)
          auth: [
            './src/pages/Login',
            './src/pages/Register'
          ],
          
          // ✅ Dashboard chunk
          dashboard: [
            './src/pages/Dashboard',
            './src/pages/Analytics'
          ]
        }
      }
    }
  }
});
```

---

## 14. 🚀 **Advanced Patterns**

### 14.1. Route Configuration Object

```tsx
// ═══════════════════════════════════════════════════════════
// ROUTE CONFIG OBJECT
// ═══════════════════════════════════════════════════════════

import { createBrowserRouter, RouterProvider } from 'react-router-dom';

const routes = [
  {
    path: '/',
    element: <Root />,
    errorElement: <ErrorBoundary />,
    loader: rootLoader,
    children: [
      {
        index: true,
        element: <Home />
      },
      {
        path: 'dashboard',
        element: <Dashboard />,
        loader: dashboardLoader,
        children: [
          {
            path: 'analytics',
            element: <Analytics />,
            loader: analyticsLoader
          }
        ]
      },
      {
        path: 'users/:userId',
        element: <UserProfile />,
        loader: userLoader,
        action: updateUserAction
      }
    ]
  }
];

const router = createBrowserRouter(routes);

function App() {
  return <RouterProvider router={router} />;
}
```

### 14.2. Basename Configuration

```tsx
// ═══════════════════════════════════════════════════════════
// BASENAME: App em subpath
// ═══════════════════════════════════════════════════════════

// App hospedado em: https://example.com/my-app/

const router = createBrowserRouter(routes, {
  basename: '/my-app'
});

// Rotas:
// /           → https://example.com/my-app/
// /dashboard  → https://example.com/my-app/dashboard
// /users/123  → https://example.com/my-app/users/123

// Links funcionam normalmente:
<Link to="/dashboard">Dashboard</Link>  // ✅ Vai para /my-app/dashboard
```

### 14.3. Future Flags

```tsx
// ═══════════════════════════════════════════════════════════
// FUTURE FLAGS: Opt-in para novos comportamentos
// ═══════════════════════════════════════════════════════════

const router = createBrowserRouter(routes, {
  future: {
    // ✅ v7 behavior (preparação para v7)
    v7_startTransition: true,
    v7_normalizeFormMethod: true,
    v7_relativeSplatPath: true
  }
});
```

### 14.4. Custom Router

```tsx
// ═══════════════════════════════════════════════════════════
// CUSTOM ROUTER (casos especiais)
// ═══════════════════════════════════════════════════════════

import { unstable_HistoryRouter as HistoryRouter } from 'react-router-dom';
import { createBrowserHistory } from 'history';

// ✅ Acesso ao history fora do React
export const history = createBrowserHistory();

function App() {
  return (
    <HistoryRouter history={history}>
      <Routes>
        <Route path="/" element={<Home />} />
      </Routes>
    </HistoryRouter>
  );
}

// Uso fora do componente
history.push('/login');  // Navega de qualquer lugar
```

### 14.5. Route Matching

```tsx
// ═══════════════════════════════════════════════════════════
// ROUTE MATCHING: matchPath e matchRoutes
// ═══════════════════════════════════════════════════════════

import { matchPath, matchRoutes } from 'react-router-dom';

// ✅ Testa se pathname combina com pattern
const match = matchPath(
  { path: '/users/:userId' },
  '/users/123'
);

if (match) {
  console.log(match.params);  // { userId: '123' }
}

// ✅ Encontra rotas que combinam
const matches = matchRoutes(routes, '/dashboard/analytics');

matches?.forEach(match => {
  console.log(match.route.path);  // 'dashboard', 'analytics'
});
```

### 14.6. Data Router Hooks

```tsx
// ═══════════════════════════════════════════════════════════
// DATA ROUTER HOOKS
// ═══════════════════════════════════════════════════════════

import {
  useNavigation,
  useMatches,
  useRevalidator
} from 'react-router-dom';

function App() {
  // ✅ Estado de navegação (loading, submitting)
  const navigation = useNavigation();

  // ✅ Todas as rotas que combinaram
  const matches = useMatches();

  // ✅ Revalidar dados
  const revalidator = useRevalidator();

  return (
    <div>
      {/* Loading global */}
      {navigation.state === 'loading' && <GlobalLoader />}
      
      {/* Breadcrumbs */}
      <nav>
        {matches.map(match => (
          <Link key={match.pathname} to={match.pathname}>
            {match.pathname}
          </Link>
        ))}
      </nav>
      
      <Outlet />
    </div>
  );
}
```

### 14.7. Scroll Restoration

```tsx
// ═══════════════════════════════════════════════════════════
// SCROLL RESTORATION
// ═══════════════════════════════════════════════════════════

import { useEffect } from 'react';
import { useLocation } from 'react-router-dom';

function ScrollToTop() {
  const { pathname } = useLocation();

  useEffect(() => {
    // ✅ Scroll to top em cada navegação
    window.scrollTo(0, 0);
  }, [pathname]);

  return null;
}

// Uso
function App() {
  return (
    <>
      <ScrollToTop />
      <Routes>
        {/* ... */}
      </Routes>
    </>
  );
}
```

---

## 15. 📚 **Recursos e Referências**

### 15.1. Documentação Oficial

**React Router v6:**
- 📖 **Docs**: https://reactrouter.com
- 🎯 **API Reference**: https://reactrouter.com/en/main/start/concepts
- 🔄 **Migração v5 → v6**: https://reactrouter.com/en/main/upgrading/v5

**Tutoriais:**
- ✅ Tutorial oficial: https://reactrouter.com/en/main/start/tutorial
- ✅ Data loading: https://reactrouter.com/en/main/guides/data-libs
- ✅ Deferred data: https://reactrouter.com/en/main/guides/deferred

### 15.2. Ferramentas

**DevTools:**
```bash
# React Router DevTools (browser extension)
# Chrome: https://chrome.google.com/webstore
# Firefox: https://addons.mozilla.org
```

**Type Safety:**
```bash
npm install -D @types/react-router-dom

# Zod para validação de params
npm install zod
```

**Testing:**
```bash
# React Router Testing Library
npm install -D @testing-library/react

# Memory Router para testes
import { MemoryRouter } from 'react-router-dom';
```

### 15.3. Best Practices Checklist

```markdown
✅ **Routing:**
- [ ] Use createBrowserRouter (não BrowserRouter)
- [ ] Configure errorElement em todas rotas principais
- [ ] Use Suspense com lazy routes
- [ ] Configure basename se app não está na raiz

✅ **Data Loading:**
- [ ] Use loaders para fetch de dados
- [ ] Use actions para mutations
- [ ] Prefira loaders paralelos (Promise.all)
- [ ] Valide search params com schema

✅ **Navigation:**
- [ ] Use Link (não <a>)
- [ ] Use NavLink para active states
- [ ] Configure Navigate com replace quando apropriado
- [ ] Salve location state para redirects pós-login

✅ **Protection:**
- [ ] Implemente protected routes
- [ ] Configure role-based guards
- [ ] Salve URL tentada para redirect
- [ ] Mostre loading durante auth check

✅ **Performance:**
- [ ] Code splitting por rota
- [ ] Preload rotas em hover
- [ ] Configure Suspense boundaries
- [ ] Use React.memo em componentes grandes

✅ **Error Handling:**
- [ ] Error boundaries em layouts
- [ ] Custom error pages (404, 500)
- [ ] Recovery actions (retry, go back)
- [ ] Log de erros (Sentry, etc)

✅ **TypeScript:**
- [ ] Type params com useParams<{ id: string }>
- [ ] Type loader return values
- [ ] Type location state
- [ ] Valide search params com schema
```

### 15.4. Common Patterns

#### 📁 **Estrutura de Pastas**

```
src/
├── routes/
│   ├── root.tsx              # Layout raiz
│   ├── error-boundary.tsx    # Error boundary
│   └── protected.tsx         # Protected route wrapper
├── pages/
│   ├── Home.tsx
│   ├── Dashboard/
│   │   ├── index.tsx         # Dashboard main
│   │   ├── loader.ts         # Dashboard loader
│   │   └── Analytics.tsx
│   └── Users/
│       ├── List.tsx
│       ├── Detail.tsx
│       └── loaders.ts
├── router.tsx                # Router configuration
└── main.tsx
```

#### 🔧 **router.tsx**

```tsx
// ═══════════════════════════════════════════════════════════
// ROUTER CONFIGURATION
// ═══════════════════════════════════════════════════════════

import { createBrowserRouter } from 'react-router-dom';
import Root from './routes/root';
import ErrorBoundary from './routes/error-boundary';
import ProtectedRoute from './routes/protected';

// Lazy imports
const Home = lazy(() => import('./pages/Home'));
const Dashboard = lazy(() => import('./pages/Dashboard'));
const UserList = lazy(() => import('./pages/Users/List'));
const UserDetail = lazy(() => import('./pages/Users/Detail'));

export const router = createBrowserRouter([
  {
    path: '/',
    element: <Root />,
    errorElement: <ErrorBoundary />,
    children: [
      {
        index: true,
        element: <Home />
      },
      {
        path: 'dashboard',
        element: (
          <ProtectedRoute>
            <Dashboard />
          </ProtectedRoute>
        ),
        loader: dashboardLoader
      },
      {
        path: 'users',
        children: [
          {
            index: true,
            element: <UserList />,
            loader: usersLoader
          },
          {
            path: ':userId',
            element: <UserDetail />,
            loader: userLoader
          }
        ]
      }
    ]
  }
]);
```

### 15.5. Performance Tips

```tsx
// ═══════════════════════════════════════════════════════════
// PERFORMANCE OPTIMIZATION
// ═══════════════════════════════════════════════════════════

// ✅ 1. Code splitting por rota
const Dashboard = lazy(() => import('./Dashboard'));

// ✅ 2. Preload em hover
<Link
  to="/dashboard"
  onMouseEnter={() => import('./Dashboard')}
>
  Dashboard
</Link>

// ✅ 3. Parallel data loading
async function loader() {
  const [user, posts, comments] = await Promise.all([
    fetch('/api/user'),
    fetch('/api/posts'),
    fetch('/api/comments')
  ]);
  
  return { user, posts, comments };
}

// ✅ 4. Defer para dados não críticos
import { defer } from 'react-router-dom';

async function loader() {
  const critical = await fetch('/api/critical');
  const deferred = fetch('/api/non-critical');  // Não await
  
  return defer({
    critical,
    deferred  // ✅ Carrega em paralelo com render
  });
}

// ✅ 5. Revalidate apenas quando necessário
<fetcher.Form reloadDocument={false}>
  {/* Não revalida toda página */}
</fetcher.Form>
```

### 15.6. Exemplo Completo: Blog com Auth

```tsx
// ═══════════════════════════════════════════════════════════
// EXEMPLO COMPLETO: BLOG COM AUTH, CRUD, PROTECTED ROUTES
// ═══════════════════════════════════════════════════════════

// ────────────────────────────────────────────────────────────
// types.ts
// ────────────────────────────────────────────────────────────

interface User {
  id: number;
  name: string;
  email: string;
  roles: string[];
}

interface Post {
  id: number;
  title: string;
  content: string;
  authorId: number;
  createdAt: string;
}

// ────────────────────────────────────────────────────────────
// auth.ts
// ────────────────────────────────────────────────────────────

let currentUser: User | null = null;

export const authService = {
  async login(email: string, password: string): Promise<User> {
    const response = await fetch('/api/login', {
      method: 'POST',
      body: JSON.stringify({ email, password })
    });
    
    currentUser = await response.json();
    return currentUser;
  },

  async logout() {
    await fetch('/api/logout', { method: 'POST' });
    currentUser = null;
  },

  getCurrentUser(): User | null {
    return currentUser;
  },

  isAuthenticated(): boolean {
    return currentUser !== null;
  }
};

// ────────────────────────────────────────────────────────────
// loaders.ts
// ────────────────────────────────────────────────────────────

import type { LoaderFunctionArgs } from 'react-router-dom';

// Posts list loader
export async function postsLoader() {
  const response = await fetch('/api/posts');
  return response.json();
}

// Single post loader
export async function postLoader({ params }: LoaderFunctionArgs) {
  const response = await fetch(`/api/posts/${params.postId}`);
  
  if (!response.ok) {
    throw new Response('Post not found', { status: 404 });
  }
  
  return response.json();
}

// Dashboard loader (parallel data)
export async function dashboardLoader() {
  const [user, stats, recentPosts] = await Promise.all([
    fetch('/api/user').then(r => r.json()),
    fetch('/api/stats').then(r => r.json()),
    fetch('/api/posts/recent').then(r => r.json())
  ]);

  return { user, stats, recentPosts };
}

// ────────────────────────────────────────────────────────────
// actions.ts
// ────────────────────────────────────────────────────────────

import { redirect } from 'react-router-dom';
import type { ActionFunctionArgs } from 'react-router-dom';

// Create post action
export async function createPostAction({ request }: ActionFunctionArgs) {
  const formData = await request.formData();
  
  const post = {
    title: formData.get('title'),
    content: formData.get('content')
  };

  const response = await fetch('/api/posts', {
    method: 'POST',
    body: JSON.stringify(post)
  });

  const newPost = await response.json();
  
  return redirect(`/posts/${newPost.id}`);
}

// Update post action
export async function updatePostAction({ params, request }: ActionFunctionArgs) {
  const formData = await request.formData();
  
  const post = {
    title: formData.get('title'),
    content: formData.get('content')
  };

  await fetch(`/api/posts/${params.postId}`, {
    method: 'PUT',
    body: JSON.stringify(post)
  });

  return redirect(`/posts/${params.postId}`);
}

// Delete post action
export async function deletePostAction({ params }: ActionFunctionArgs) {
  await fetch(`/api/posts/${params.postId}`, {
    method: 'DELETE'
  });

  return redirect('/posts');
}

// ────────────────────────────────────────────────────────────
// components/ProtectedRoute.tsx
// ────────────────────────────────────────────────────────────

import { Navigate, useLocation } from 'react-router-dom';
import { authService } from '../auth';

export function ProtectedRoute({ children }: { children: React.ReactNode }) {
  const location = useLocation();

  if (!authService.isAuthenticated()) {
    return <Navigate to="/login" state={{ from: location.pathname }} replace />;
  }

  return <>{children}</>;
}

// ────────────────────────────────────────────────────────────
// pages/Login.tsx
// ────────────────────────────────────────────────────────────

import { Form, useNavigate, useLocation, useActionData } from 'react-router-dom';
import { authService } from '../auth';

export async function loginAction({ request }: ActionFunctionArgs) {
  const formData = await request.formData();
  
  try {
    await authService.login(
      formData.get('email') as string,
      formData.get('password') as string
    );
    
    return { success: true };
  } catch (error) {
    return { error: 'Invalid credentials' };
  }
}

export function Login() {
  const navigate = useNavigate();
  const location = useLocation();
  const actionData = useActionData() as { success?: boolean; error?: string };

  useEffect(() => {
    if (actionData?.success) {
      const from = (location.state as any)?.from || '/dashboard';
      navigate(from, { replace: true });
    }
  }, [actionData]);

  return (
    <div>
      <h1>Login</h1>
      
      <Form method="post">
        <input name="email" type="email" placeholder="Email" required />
        <input name="password" type="password" placeholder="Password" required />
        <button type="submit">Login</button>
      </Form>

      {actionData?.error && <p>{actionData.error}</p>}
    </div>
  );
}

// ────────────────────────────────────────────────────────────
// pages/Dashboard.tsx
// ────────────────────────────────────────────────────────────

import { useLoaderData, Link } from 'react-router-dom';

export function Dashboard() {
  const { user, stats, recentPosts } = useLoaderData() as Awaited<
    ReturnType<typeof dashboardLoader>
  >;

  return (
    <div>
      <h1>Welcome, {user.name}!</h1>
      
      <div>
        <h2>Stats</h2>
        <p>Total Posts: {stats.totalPosts}</p>
        <p>Total Views: {stats.totalViews}</p>
      </div>

      <div>
        <h2>Recent Posts</h2>
        {recentPosts.map(post => (
          <div key={post.id}>
            <Link to={`/posts/${post.id}`}>{post.title}</Link>
          </div>
        ))}
      </div>
    </div>
  );
}

// ────────────────────────────────────────────────────────────
// pages/Posts/List.tsx
// ────────────────────────────────────────────────────────────

import { useLoaderData, Link, useSearchParams } from 'react-router-dom';

export function PostsList() {
  const posts = useLoaderData() as Post[];
  const [searchParams, setSearchParams] = useSearchParams();

  const query = searchParams.get('q') || '';

  const filteredPosts = posts.filter(post =>
    post.title.toLowerCase().includes(query.toLowerCase())
  );

  return (
    <div>
      <h1>Posts</h1>
      
      <input
        value={query}
        onChange={(e) => setSearchParams({ q: e.target.value })}
        placeholder="Search posts..."
      />

      <Link to="/posts/create">Create Post</Link>

      <div>
        {filteredPosts.map(post => (
          <div key={post.id}>
            <Link to={`/posts/${post.id}`}>
              <h2>{post.title}</h2>
            </Link>
            <p>{post.content.substring(0, 100)}...</p>
          </div>
        ))}
      </div>
    </div>
  );
}

// ────────────────────────────────────────────────────────────
// pages/Posts/Detail.tsx
// ────────────────────────────────────────────────────────────

import { useLoaderData, Link, useFetcher } from 'react-router-dom';
import { authService } from '../../auth';

export function PostDetail() {
  const post = useLoaderData() as Post;
  const fetcher = useFetcher();
  const currentUser = authService.getCurrentUser();

  const canEdit = currentUser?.id === post.authorId;

  return (
    <div>
      <h1>{post.title}</h1>
      <p>{post.content}</p>
      <p>Created: {new Date(post.createdAt).toLocaleDateString()}</p>

      {canEdit && (
        <div>
          <Link to={`/posts/${post.id}/edit`}>Edit</Link>
          
          <fetcher.Form method="post" action={`/posts/${post.id}/delete`}>
            <button type="submit">Delete</button>
          </fetcher.Form>
        </div>
      )}

      <Link to="/posts">Back to Posts</Link>
    </div>
  );
}

// ────────────────────────────────────────────────────────────
// pages/Posts/Create.tsx
// ────────────────────────────────────────────────────────────

import { Form } from 'react-router-dom';

export function CreatePost() {
  return (
    <div>
      <h1>Create Post</h1>
      
      <Form method="post">
        <input name="title" placeholder="Title" required />
        <textarea name="content" placeholder="Content" required />
        <button type="submit">Create</button>
      </Form>
    </div>
  );
}

// ────────────────────────────────────────────────────────────
// pages/Posts/Edit.tsx
// ────────────────────────────────────────────────────────────

import { Form, useLoaderData } from 'react-router-dom';

export function EditPost() {
  const post = useLoaderData() as Post;

  return (
    <div>
      <h1>Edit Post</h1>
      
      <Form method="post">
        <input name="title" defaultValue={post.title} required />
        <textarea name="content" defaultValue={post.content} required />
        <button type="submit">Update</button>
      </Form>
    </div>
  );
}

// ────────────────────────────────────────────────────────────
// router.tsx
// ────────────────────────────────────────────────────────────

import { createBrowserRouter } from 'react-router-dom';
import { lazy, Suspense } from 'react';

const Dashboard = lazy(() => import('./pages/Dashboard'));
const PostsList = lazy(() => import('./pages/Posts/List'));
const PostDetail = lazy(() => import('./pages/Posts/Detail'));
const CreatePost = lazy(() => import('./pages/Posts/Create'));
const EditPost = lazy(() => import('./pages/Posts/Edit'));

export const router = createBrowserRouter([
  {
    path: '/',
    element: <Root />,
    errorElement: <ErrorBoundary />,
    children: [
      {
        index: true,
        element: <Home />
      },
      {
        path: 'login',
        element: <Login />,
        action: loginAction
      },
      {
        path: 'dashboard',
        element: (
          <ProtectedRoute>
            <Suspense fallback={<Loader />}>
              <Dashboard />
            </Suspense>
          </ProtectedRoute>
        ),
        loader: dashboardLoader
      },
      {
        path: 'posts',
        children: [
          {
            index: true,
            element: (
              <Suspense fallback={<Loader />}>
                <PostsList />
              </Suspense>
            ),
            loader: postsLoader
          },
          {
            path: 'create',
            element: (
              <ProtectedRoute>
                <Suspense fallback={<Loader />}>
                  <CreatePost />
                </Suspense>
              </ProtectedRoute>
            ),
            action: createPostAction
          },
          {
            path: ':postId',
            element: (
              <Suspense fallback={<Loader />}>
                <PostDetail />
              </Suspense>
            ),
            loader: postLoader
          },
          {
            path: ':postId/edit',
            element: (
              <ProtectedRoute>
                <Suspense fallback={<Loader />}>
                  <EditPost />
                </Suspense>
              </ProtectedRoute>
            ),
            loader: postLoader,
            action: updatePostAction
          },
          {
            path: ':postId/delete',
            action: deletePostAction
          }
        ]
      }
    ]
  }
]);
```

### 15.7. Migration Guide: v5 → v6

```tsx
// ═══════════════════════════════════════════════════════════
// MIGRAÇÃO v5 → v6
// ═══════════════════════════════════════════════════════════

// ❌ v5
<Switch>
  <Route exact path="/" component={Home} />
  <Route path="/users/:id" component={User} />
</Switch>

// ✅ v6
<Routes>
  <Route path="/" element={<Home />} />
  <Route path="/users/:id" element={<User />} />
</Routes>

// ────────────────────────────────────────────────────────────

// ❌ v5
import { useHistory } from 'react-router-dom';

const history = useHistory();
history.push('/dashboard');

// ✅ v6
import { useNavigate } from 'react-router-dom';

const navigate = useNavigate();
navigate('/dashboard');

// ────────────────────────────────────────────────────────────

// ❌ v5
<Redirect to="/login" />

// ✅ v6
<Navigate to="/login" />

// ────────────────────────────────────────────────────────────

// ❌ v5
import { useRouteMatch } from 'react-router-dom';

const match = useRouteMatch('/users/:id');

// ✅ v6
import { matchPath } from 'react-router-dom';

const match = matchPath({ path: '/users/:id' }, location.pathname);
```

### 15.8. Testing

```tsx
// ═══════════════════════════════════════════════════════════
// TESTING COM MEMORY ROUTER
// ═══════════════════════════════════════════════════════════

import { render, screen } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import { App } from './App';

describe('App routing', () => {
  it('renders home page', () => {
    render(
      <MemoryRouter initialEntries={['/']}>
        <App />
      </MemoryRouter>
    );

    expect(screen.getByText('Home')).toBeInTheDocument();
  });

  it('renders user profile', () => {
    render(
      <MemoryRouter initialEntries={['/users/123']}>
        <App />
      </MemoryRouter>
    );

    expect(screen.getByText('User Profile')).toBeInTheDocument();
  });
});
```

---

## 🎯 **Conclusão**

### **React Router v6** em resumo:

✅ **Routing moderno**: Routes + element prop  
✅ **Data loading**: Loaders + Actions (v6.4+)  
✅ **Nested routes**: Outlet para layouts  
✅ **Type-safe**: TypeScript first-class  
✅ **Performance**: Code splitting + lazy loading  
✅ **Error handling**: Error boundaries por rota  
✅ **Protected routes**: Auth guards + role-based  
✅ **Search params**: useSearchParams hook  
✅ **Navigation**: useNavigate + Link + NavLink  

**Happy routing!** 🛣️🚀

---

Continuo com os tópicos finais (Code Splitting, Advanced Patterns e Recursos)! Prosseguir?

