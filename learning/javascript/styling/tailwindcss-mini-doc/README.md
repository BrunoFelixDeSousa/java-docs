# 🎨 Tailwind CSS - Utility-First Framework

**Tailwind CSS** é o framework CSS **utility-first** mais popular para criar designs modernos sem sair do HTML.

> "Rapidly build modern websites without ever leaving your HTML"

[![Tailwind CSS](https://img.shields.io/badge/Tailwind_CSS-v3+-06B6D4?style=flat&logo=tailwindcss&logoColor=white)](https://tailwindcss.com)
[![PostCSS](https://img.shields.io/badge/PostCSS-Ready-DD3A0A?style=flat&logo=postcss&logoColor=white)](https://postcss.org/)

---

## 📋 Índice

- [Por que Tailwind?](#-por-que-tailwind)
- [Instalação](#-instalação)
- [Setup com Frameworks](#️-setup-com-frameworks)
- [Conceitos Fundamentais](#-conceitos-fundamentais)
- [Layout & Spacing](#-layout--spacing)
- [Typography](#-typography)
- [Colors & Backgrounds](#-colors--backgrounds)
- [Borders & Effects](#-borders--effects)
- [Responsive Design](#-responsive-design)
- [States & Variants](#-states--variants)
- [Customização](#-customização)
- [Componentes Reutilizáveis](#-componentes-reutilizáveis)
- [Best Practices](#-best-practices)

---

## 🎯 Por que Tailwind?

### ✅ Comparação com Outras Abordagens

```html
<!-- ❌ CSS tradicional: Arquivos separados -->
<div class="card">
  <h2 class="card-title">Título</h2>
  <p class="card-description">Descrição</p>
</div>

<style>
  .card {
    padding: 1rem;
    background: white;
    border-radius: 0.5rem;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  }
  .card-title {
    font-size: 1.5rem;
    font-weight: bold;
    margin-bottom: 0.5rem;
  }
  /* ... mais CSS */
</style>

<!-- ❌ Bootstrap: Componentes prontos (pouca flexibilidade) -->
<div class="card">
  <div class="card-body">
    <h5 class="card-title">Título</h5>
    <p class="card-text">Descrição</p>
  </div>
</div>

<!-- ✅ Tailwind: Utility classes (máxima flexibilidade) -->
<div class="p-4 bg-white rounded-lg shadow-md">
  <h2 class="text-2xl font-bold mb-2">Título</h2>
  <p class="text-gray-600">Descrição</p>
</div>
```

### 🚀 Vantagens do Tailwind

| Vantagem | Descrição |
|----------|-----------|
| **Velocidade** | Construa UI sem sair do HTML |
| **Flexibilidade** | Sem componentes pré-definidos - você decide |
| **Performance** | PurgeCSS remove classes não usadas (KB finais) |
| **Consistência** | Design system built-in (cores, espaçamentos) |
| **Responsive** | Mobile-first com prefixos intuitivos |
| **Dark Mode** | Suporte nativo com `dark:` variant |

---

## 📦 Instalação

### Vite + React/Vue

```bash
# Instalar Tailwind
npm install -D tailwindcss postcss autoprefixer

# Inicializar config
npx tailwindcss init -p
```

```javascript
// tailwind.config.js
/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {},
  },
  plugins: [],
}
```

```css
/* src/index.css */
@tailwind base;
@tailwind components;
@tailwind utilities;
```

```typescript
// main.tsx
import './index.css'
```

### Next.js (App Router)

```bash
npx create-next-app@latest my-app --typescript --tailwind
```

Ou instalar manualmente:

```bash
npm install -D tailwindcss postcss autoprefixer
npx tailwindcss init -p
```

```javascript
// tailwind.config.js
/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    './app/**/*.{js,ts,jsx,tsx,mdx}',
    './pages/**/*.{js,ts,jsx,tsx,mdx}',
    './components/**/*.{js,ts,jsx,tsx,mdx}',
  ],
  theme: {
    extend: {},
  },
  plugins: [],
}
```

```css
/* app/globals.css */
@tailwind base;
@tailwind components;
@tailwind utilities;
```

---

## ⚙️ Setup com Frameworks

### React + TypeScript + Vite

```typescript
// src/App.tsx
export function App() {
  return (
    <div className="min-h-screen bg-gray-100 py-8">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <h1 className="text-4xl font-bold text-gray-900">
          Hello Tailwind!
        </h1>
      </div>
    </div>
  );
}
```

### Next.js 14+ (App Router)

```tsx
// app/page.tsx
export default function HomePage() {
  return (
    <main className="flex min-h-screen flex-col items-center justify-center p-24">
      <h1 className="text-6xl font-bold">
        Welcome to{' '}
        <span className="text-blue-600">Next.js</span>
      </h1>
    </main>
  );
}
```

---

## 💡 Conceitos Fundamentais

### Utility-First Mindset

```html
<!-- Cada classe = 1 propriedade CSS -->
<div class="
  p-4          → padding: 1rem
  bg-blue-500  → background-color: rgb(59 130 246)
  text-white   → color: rgb(255 255 255)
  rounded-lg   → border-radius: 0.5rem
  shadow-md    → box-shadow: ...
">
  Conteúdo
</div>
```

### Sistema de Espaçamento

Tailwind usa escala de `0` a `96` (baseado em `0.25rem = 4px`):

```html
<!-- Padding -->
<div class="p-0">   <!-- 0px -->
<div class="p-1">   <!-- 4px -->
<div class="p-2">   <!-- 8px -->
<div class="p-4">   <!-- 16px -->
<div class="p-8">   <!-- 32px -->
<div class="p-16">  <!-- 64px -->

<!-- Margin -->
<div class="m-4">   <!-- margin: 1rem -->
<div class="mt-4">  <!-- margin-top: 1rem -->
<div class="mx-4">  <!-- margin-left/right: 1rem -->
<div class="my-4">  <!-- margin-top/bottom: 1rem -->

<!-- Margin negativo -->
<div class="-mt-4"> <!-- margin-top: -1rem -->

<!-- Margin auto -->
<div class="mx-auto"> <!-- margin-left/right: auto -->
```

### Sistema de Cores

```html
<!-- Cores com intensidades (50-950) -->
<div class="bg-gray-50">    <!-- Cinza muito claro -->
<div class="bg-gray-100">
<div class="bg-gray-500">   <!-- Cinza médio -->
<div class="bg-gray-900">   <!-- Cinza muito escuro -->

<!-- Cores principais -->
<div class="bg-blue-500">   <!-- Azul -->
<div class="bg-red-500">    <!-- Vermelho -->
<div class="bg-green-500">  <!-- Verde -->
<div class="bg-yellow-500"> <!-- Amarelo -->
<div class="bg-purple-500"> <!-- Roxo -->
<div class="bg-pink-500">   <!-- Rosa -->
<div class="bg-indigo-500"> <!-- Índigo -->

<!-- Texto com cores -->
<p class="text-gray-600">Texto cinza</p>
<p class="text-blue-500">Texto azul</p>
```

---

## 📐 Layout & Spacing

### Flexbox

```html
<!-- Flex container -->
<div class="flex">
  <div>Item 1</div>
  <div>Item 2</div>
</div>

<!-- Direção -->
<div class="flex flex-row">      <!-- horizontal (padrão) -->
<div class="flex flex-col">      <!-- vertical -->
<div class="flex flex-row-reverse"> <!-- horizontal invertido -->

<!-- Justify (eixo principal) -->
<div class="flex justify-start">    <!-- início -->
<div class="flex justify-center">   <!-- centro -->
<div class="flex justify-end">      <!-- fim -->
<div class="flex justify-between">  <!-- espaço entre -->
<div class="flex justify-around">   <!-- espaço ao redor -->

<!-- Align (eixo transversal) -->
<div class="flex items-start">      <!-- início -->
<div class="flex items-center">     <!-- centro -->
<div class="flex items-end">        <!-- fim -->
<div class="flex items-stretch">    <!-- esticar -->

<!-- Gap (espaço entre itens) -->
<div class="flex gap-4">            <!-- 1rem -->
<div class="flex gap-x-4 gap-y-2">  <!-- horizontal 1rem, vertical 0.5rem -->

<!-- Flex grow/shrink -->
<div class="flex">
  <div class="flex-1">Expande</div>
  <div class="flex-none">Fixo</div>
</div>

<!-- Exemplo completo: Header -->
<header class="flex items-center justify-between p-4 bg-white shadow">
  <div class="text-xl font-bold">Logo</div>
  <nav class="flex gap-6">
    <a href="#">Home</a>
    <a href="#">About</a>
    <a href="#">Contact</a>
  </nav>
</header>
```

### Grid

```html
<!-- Grid básico -->
<div class="grid grid-cols-3 gap-4">
  <div>1</div>
  <div>2</div>
  <div>3</div>
</div>

<!-- Colunas responsivas -->
<div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
  <!-- 1 coluna mobile, 2 tablet, 4 desktop -->
</div>

<!-- Grid auto-fit (responsivo automático) -->
<div class="grid grid-cols-[repeat(auto-fit,minmax(250px,1fr))] gap-4">
  <!-- Ajusta automaticamente colunas baseado no espaço -->
</div>

<!-- Span columns -->
<div class="grid grid-cols-4 gap-4">
  <div class="col-span-2">Ocupa 2 colunas</div>
  <div>1 coluna</div>
  <div>1 coluna</div>
</div>

<!-- Exemplo: Card Grid -->
<div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
  <div class="bg-white p-6 rounded-lg shadow">Card 1</div>
  <div class="bg-white p-6 rounded-lg shadow">Card 2</div>
  <div class="bg-white p-6 rounded-lg shadow">Card 3</div>
</div>
```

### Width & Height

```html
<!-- Width (largura) -->
<div class="w-full">      <!-- 100% -->
<div class="w-1/2">       <!-- 50% -->
<div class="w-1/3">       <!-- 33.333% -->
<div class="w-64">        <!-- 16rem = 256px -->
<div class="w-screen">    <!-- 100vw -->

<!-- Min/Max width -->
<div class="min-w-0 max-w-md">   <!-- max-width: 28rem -->
<div class="max-w-screen-xl">    <!-- max-width: 1280px -->

<!-- Height (altura) -->
<div class="h-full">      <!-- 100% -->
<div class="h-screen">    <!-- 100vh -->
<div class="h-64">        <!-- 16rem = 256px -->

<!-- Min/Max height -->
<div class="min-h-screen">  <!-- min-height: 100vh -->
<div class="max-h-96">      <!-- max-height: 24rem -->

<!-- Container centralizado -->
<div class="max-w-7xl mx-auto px-4">
  <!-- Container responsivo com padding lateral -->
</div>
```

---

## 📝 Typography

### Font Size

```html
<p class="text-xs">    <!-- 0.75rem = 12px -->
<p class="text-sm">    <!-- 0.875rem = 14px -->
<p class="text-base">  <!-- 1rem = 16px (padrão) -->
<p class="text-lg">    <!-- 1.125rem = 18px -->
<p class="text-xl">    <!-- 1.25rem = 20px -->
<p class="text-2xl">   <!-- 1.5rem = 24px -->
<p class="text-3xl">   <!-- 1.875rem = 30px -->
<p class="text-4xl">   <!-- 2.25rem = 36px -->
<p class="text-5xl">   <!-- 3rem = 48px -->
<p class="text-6xl">   <!-- 3.75rem = 60px -->
```

### Font Weight

```html
<p class="font-thin">       <!-- 100 -->
<p class="font-light">      <!-- 300 -->
<p class="font-normal">     <!-- 400 (padrão) -->
<p class="font-medium">     <!-- 500 -->
<p class="font-semibold">   <!-- 600 -->
<p class="font-bold">       <!-- 700 -->
<p class="font-extrabold">  <!-- 800 -->
<p class="font-black">      <!-- 900 -->
```

### Text Alignment

```html
<p class="text-left">      <!-- Esquerda -->
<p class="text-center">    <!-- Centro -->
<p class="text-right">     <!-- Direita -->
<p class="text-justify">   <!-- Justificado -->
```

### Text Transform & Decoration

```html
<!-- Transform -->
<p class="uppercase">      <!-- MAIÚSCULAS -->
<p class="lowercase">      <!-- minúsculas -->
<p class="capitalize">     <!-- Primeira Letra -->

<!-- Decoration -->
<p class="underline">      <!-- Sublinhado -->
<p class="line-through">   <!-- Riscado -->
<p class="no-underline">   <!-- Remove sublinhado -->

<!-- Line height -->
<p class="leading-tight">  <!-- line-height: 1.25 -->
<p class="leading-normal"> <!-- line-height: 1.5 -->
<p class="leading-loose">  <!-- line-height: 2 -->

<!-- Letter spacing -->
<p class="tracking-tight"> <!-- Menor espaçamento -->
<p class="tracking-normal"> <!-- Padrão -->
<p class="tracking-wide">  <!-- Maior espaçamento -->
```

### Typography Exemplo

```html
<article class="max-w-prose mx-auto">
  <h1 class="text-4xl font-bold text-gray-900 mb-4">
    Título Principal
  </h1>
  
  <h2 class="text-2xl font-semibold text-gray-700 mb-3">
    Subtítulo
  </h2>
  
  <p class="text-base text-gray-600 leading-relaxed mb-4">
    Lorem ipsum dolor sit amet, consectetur adipiscing elit.
    Sed do eiusmod tempor incididunt ut labore.
  </p>
  
  <blockquote class="border-l-4 border-blue-500 pl-4 italic text-gray-500">
    "Esta é uma citação importante."
  </blockquote>
</article>
```

---

## 🎨 Colors & Backgrounds

### Background Colors

```html
<!-- Cores sólidas -->
<div class="bg-white">
<div class="bg-gray-100">
<div class="bg-blue-500">
<div class="bg-red-600">

<!-- Transparência (com opacity) -->
<div class="bg-blue-500 bg-opacity-50">   <!-- 50% opaco -->
<div class="bg-blue-500/50">              <!-- Sintaxe moderna (v3.0+) -->

<!-- Gradientes -->
<div class="bg-gradient-to-r from-blue-500 to-purple-600">
  <!-- Gradiente da esquerda (blue) para direita (purple) -->
</div>

<div class="bg-gradient-to-br from-pink-500 via-red-500 to-yellow-500">
  <!-- Gradiente com 3 cores (via = intermediária) -->
</div>

<!-- Direções de gradiente -->
bg-gradient-to-t     <!-- para cima -->
bg-gradient-to-tr    <!-- diagonal superior direita -->
bg-gradient-to-r     <!-- para direita -->
bg-gradient-to-br    <!-- diagonal inferior direita -->
bg-gradient-to-b     <!-- para baixo -->
bg-gradient-to-bl    <!-- diagonal inferior esquerda -->
bg-gradient-to-l     <!-- para esquerda -->
bg-gradient-to-tl    <!-- diagonal superior esquerda -->
```

### Text Colors

```html
<p class="text-gray-900">      <!-- Texto escuro -->
<p class="text-blue-500">      <!-- Texto azul -->
<p class="text-red-600">       <!-- Texto vermelho -->

<!-- Com opacity -->
<p class="text-gray-900/75">   <!-- 75% opaco -->
```

### Background Images

```html
<!-- Background size -->
<div class="bg-cover">         <!-- Cobrir todo container -->
<div class="bg-contain">       <!-- Conter sem cortar -->

<!-- Background position -->
<div class="bg-center">        <!-- Centralizado -->
<div class="bg-top">           <!-- Topo -->
<div class="bg-bottom">        <!-- Fundo -->

<!-- Background repeat -->
<div class="bg-no-repeat">     <!-- Não repetir -->
<div class="bg-repeat">        <!-- Repetir -->

<!-- Exemplo completo: Hero section -->
<div 
  class="h-screen bg-cover bg-center bg-no-repeat" 
  style="background-image: url('/hero.jpg')"
>
  <div class="h-full bg-black/50 flex items-center justify-center">
    <h1 class="text-white text-6xl font-bold">Hero Title</h1>
  </div>
</div>
```

---

## 🔲 Borders & Effects

### Borders

```html
<!-- Border width -->
<div class="border">        <!-- 1px todas -->
<div class="border-2">      <!-- 2px todas -->
<div class="border-t-2">    <!-- 2px apenas top -->
<div class="border-x-4">    <!-- 4px left/right -->

<!-- Border color -->
<div class="border border-gray-300">
<div class="border-2 border-blue-500">

<!-- Border radius -->
<div class="rounded">       <!-- 0.25rem -->
<div class="rounded-md">    <!-- 0.375rem -->
<div class="rounded-lg">    <!-- 0.5rem -->
<div class="rounded-xl">    <!-- 0.75rem -->
<div class="rounded-full">  <!-- 9999px (círculo) -->

<!-- Radius específico -->
<div class="rounded-t-lg">  <!-- Apenas topo -->
<div class="rounded-l-lg">  <!-- Apenas esquerda -->
```

### Shadows

```html
<!-- Box shadow -->
<div class="shadow-sm">     <!-- Sombra pequena -->
<div class="shadow">        <!-- Sombra padrão -->
<div class="shadow-md">     <!-- Sombra média -->
<div class="shadow-lg">     <!-- Sombra grande -->
<div class="shadow-xl">     <!-- Sombra extra grande -->
<div class="shadow-2xl">    <!-- Sombra gigante -->
<div class="shadow-none">   <!-- Sem sombra -->

<!-- Inner shadow -->
<div class="shadow-inner">

<!-- Drop shadow (para SVG/imagens) -->
<img class="drop-shadow-lg" src="..." />
```

### Opacity

```html
<div class="opacity-0">     <!-- Invisível -->
<div class="opacity-25">    <!-- 25% visível -->
<div class="opacity-50">    <!-- 50% visível -->
<div class="opacity-75">    <!-- 75% visível -->
<div class="opacity-100">   <!-- 100% visível (padrão) -->
```

### Effects

```html
<!-- Blur -->
<div class="blur-sm">       <!-- Desfoque pequeno -->
<div class="blur-lg">       <!-- Desfoque grande -->

<!-- Brightness -->
<div class="brightness-50"> <!-- 50% brilho -->
<div class="brightness-125"> <!-- 125% brilho -->

<!-- Contrast -->
<div class="contrast-125">

<!-- Grayscale -->
<div class="grayscale">     <!-- Preto e branco -->
```

---

## 📱 Responsive Design

### Breakpoints

```html
<!-- Mobile-first: Classes sem prefixo = mobile -->
<div class="w-full sm:w-1/2 md:w-1/3 lg:w-1/4">
  <!-- 
    Mobile: 100% largura
    SM (≥640px): 50% largura
    MD (≥768px): 33% largura
    LG (≥1024px): 25% largura
  -->
</div>

<!-- Breakpoints disponíveis -->
sm: 640px   <!-- Small devices -->
md: 768px   <!-- Medium devices -->
lg: 1024px  <!-- Large devices -->
xl: 1280px  <!-- Extra large -->
2xl: 1536px <!-- 2x Extra large -->
```

### Responsive Patterns

```html
<!-- Stack mobile, row desktop -->
<div class="flex flex-col lg:flex-row gap-4">
  <div>Item 1</div>
  <div>Item 2</div>
</div>

<!-- Hide/Show baseado em tela -->
<div class="hidden lg:block">
  <!-- Visível apenas em desktop (lg+) -->
</div>

<div class="block lg:hidden">
  <!-- Visível apenas em mobile (< lg) -->
</div>

<!-- Text size responsivo -->
<h1 class="text-2xl md:text-4xl lg:text-6xl">
  Título Responsivo
</h1>

<!-- Padding responsivo -->
<div class="p-4 md:p-8 lg:p-12">
  Padding aumenta com tela
</div>

<!-- Grid responsivo -->
<div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4">
  <!-- 1 col mobile, 2 sm, 3 lg, 4 xl -->
</div>
```

### Container Responsivo

```html
<!-- Container que se adapta -->
<div class="container mx-auto px-4">
  <!-- 
    Container com max-width baseado no breakpoint
    + margin horizontal auto (centralizado)
    + padding lateral 1rem
  -->
</div>

<!-- Container customizado -->
<div class="max-w-screen-xl mx-auto px-4 sm:px-6 lg:px-8">
  <!-- Padding lateral responsivo -->
</div>
```

---

## 🎭 States & Variants

### Hover

```html
<!-- Hover em cores -->
<button class="bg-blue-500 hover:bg-blue-700">
  Hover muda cor
</button>

<!-- Hover em texto -->
<a href="#" class="text-blue-500 hover:text-blue-700 hover:underline">
  Link com hover
</a>

<!-- Hover em scale -->
<div class="transform hover:scale-105 transition">
  Aumenta 5% no hover
</div>
```

### Focus

```html
<!-- Focus em inputs -->
<input 
  class="border border-gray-300 focus:border-blue-500 focus:ring-2 focus:ring-blue-200"
  type="text" 
/>

<!-- Focus em botões -->
<button class="bg-blue-500 focus:outline-none focus:ring-4 focus:ring-blue-300">
  Botão
</button>
```

### Active

```html
<button class="bg-blue-500 active:bg-blue-800">
  Clique aqui
</button>
```

### Disabled

```html
<button 
  class="bg-blue-500 disabled:bg-gray-300 disabled:cursor-not-allowed" 
  disabled
>
  Desabilitado
</button>

<input 
  class="border disabled:bg-gray-100 disabled:text-gray-400" 
  disabled 
/>
```

### Group Hover

```html
<!-- Hover no pai afeta filho -->
<div class="group">
  <img src="..." class="group-hover:scale-110 transition" />
  <p class="group-hover:text-blue-500">Texto muda ao hover no container</p>
</div>

<!-- Exemplo: Card com hover -->
<div class="group bg-white rounded-lg shadow hover:shadow-xl transition">
  <img src="..." class="group-hover:scale-105 transition" />
  <h3 class="group-hover:text-blue-600 transition">Título</h3>
</div>
```

### Dark Mode

```html
<!-- Habilitar dark mode no config -->
// tailwind.config.js
module.exports = {
  darkMode: 'class', // ou 'media'
  // ...
}

<!-- Usar dark: variant -->
<div class="bg-white dark:bg-gray-900 text-black dark:text-white">
  Tema adaptável
</div>

<!-- Toggle dark mode -->
<html class="dark">
  <!-- Adicione class="dark" no <html> para ativar -->
</html>

<!-- Exemplo completo -->
<div class="min-h-screen bg-white dark:bg-gray-900">
  <h1 class="text-gray-900 dark:text-white">Título</h1>
  <p class="text-gray-600 dark:text-gray-400">Parágrafo</p>
</div>
```

---

## 🎨 Customização

### tailwind.config.js

```javascript
/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ['./src/**/*.{js,jsx,ts,tsx}'],
  
  theme: {
    // Sobrescrever valores padrão
    screens: {
      sm: '480px',
      md: '768px',
      lg: '1024px',
      xl: '1280px',
    },
    
    // Extend: adicionar SEM remover padrões
    extend: {
      // Cores customizadas
      colors: {
        primary: {
          50: '#eff6ff',
          100: '#dbeafe',
          500: '#3b82f6',
          900: '#1e3a8a',
        },
        secondary: '#64748b',
      },
      
      // Font family
      fontFamily: {
        sans: ['Inter', 'sans-serif'],
        mono: ['Fira Code', 'monospace'],
      },
      
      // Espaçamento customizado
      spacing: {
        '72': '18rem',
        '84': '21rem',
        '96': '24rem',
      },
      
      // Border radius
      borderRadius: {
        '4xl': '2rem',
      },
      
      // Box shadow
      boxShadow: {
        'custom': '0 4px 14px 0 rgba(0, 0, 0, 0.1)',
      },
      
      // Animations
      keyframes: {
        wiggle: {
          '0%, 100%': { transform: 'rotate(-3deg)' },
          '50%': { transform: 'rotate(3deg)' },
        },
        fadeIn: {
          '0%': { opacity: '0' },
          '100%': { opacity: '1' },
        },
      },
      animation: {
        wiggle: 'wiggle 1s ease-in-out infinite',
        fadeIn: 'fadeIn 0.5s ease-in',
      },
    },
  },
  
  plugins: [
    require('@tailwindcss/forms'),
    require('@tailwindcss/typography'),
    require('@tailwindcss/aspect-ratio'),
  ],
}
```

### Usando Cores Customizadas

```html
<div class="bg-primary-500 text-white">
  Usando cor customizada
</div>

<button class="bg-primary-500 hover:bg-primary-700">
  Botão
</button>
```

### Arbitrary Values

```html
<!-- Valores específicos quando necessário -->
<div class="w-[137px]">           <!-- width: 137px -->
<div class="h-[calc(100vh-4rem)]"> <!-- height: calc(100vh - 4rem) -->
<div class="bg-[#1da1f2]">        <!-- background: #1da1f2 (Twitter blue) -->
<div class="text-[14px]">         <!-- font-size: 14px -->
<div class="top-[117px]">         <!-- top: 117px -->

<!-- Valores dinâmicos com CSS variables -->
<div class="bg-[var(--my-color)]">
```

---

## 🧩 Componentes Reutilizáveis

### @apply Directive

```css
/* src/styles/components.css */
@layer components {
  /* Botão primário */
  .btn-primary {
    @apply bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded;
  }
  
  /* Card */
  .card {
    @apply bg-white rounded-lg shadow-md p-6;
  }
  
  /* Input */
  .input {
    @apply border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500;
  }
}
```

```html
<!-- Usar classes customizadas -->
<button class="btn-primary">Clique aqui</button>
<div class="card">Conteúdo do card</div>
<input class="input" type="text" />
```

### React Components

```tsx
// components/Button.tsx
interface ButtonProps {
  variant?: 'primary' | 'secondary' | 'danger';
  children: React.ReactNode;
  onClick?: () => void;
}

export function Button({ variant = 'primary', children, onClick }: ButtonProps) {
  const baseClasses = 'px-4 py-2 rounded font-semibold transition';
  
  const variantClasses = {
    primary: 'bg-blue-500 hover:bg-blue-700 text-white',
    secondary: 'bg-gray-500 hover:bg-gray-700 text-white',
    danger: 'bg-red-500 hover:bg-red-700 text-white',
  };
  
  return (
    <button 
      className={`${baseClasses} ${variantClasses[variant]}`}
      onClick={onClick}
    >
      {children}
    </button>
  );
}

// Uso
<Button variant="primary">Salvar</Button>
<Button variant="danger">Deletar</Button>
```

### Class Variance Authority (CVA)

```bash
npm install class-variance-authority clsx
```

```tsx
// components/Button.tsx
import { cva, type VariantProps } from 'class-variance-authority';
import { clsx } from 'clsx';

const buttonVariants = cva(
  // Base classes
  'px-4 py-2 rounded font-semibold transition',
  {
    variants: {
      variant: {
        primary: 'bg-blue-500 hover:bg-blue-700 text-white',
        secondary: 'bg-gray-500 hover:bg-gray-700 text-white',
        danger: 'bg-red-500 hover:bg-red-700 text-white',
        outline: 'border-2 border-blue-500 text-blue-500 hover:bg-blue-50',
      },
      size: {
        sm: 'text-sm px-3 py-1',
        md: 'text-base px-4 py-2',
        lg: 'text-lg px-6 py-3',
      },
    },
    defaultVariants: {
      variant: 'primary',
      size: 'md',
    },
  }
);

interface ButtonProps extends VariantProps<typeof buttonVariants> {
  children: React.ReactNode;
  onClick?: () => void;
}

export function Button({ variant, size, children, onClick }: ButtonProps) {
  return (
    <button className={buttonVariants({ variant, size })} onClick={onClick}>
      {children}
    </button>
  );
}

// Uso
<Button variant="primary" size="lg">Grande</Button>
<Button variant="outline" size="sm">Pequeno</Button>
```

---

## ✅ Best Practices

### 1. Mobile-First Approach

```html
<!-- ✅ Bom: Começar mobile, adicionar desktop -->
<div class="text-sm md:text-base lg:text-lg">
  Texto responsivo
</div>

<!-- ❌ Ruim: Desktop-first requer mais overrides -->
<div class="text-lg lg:text-base md:text-sm">
  Menos eficiente
</div>
```

### 2. Use @apply com Moderação

```css
/* ✅ Bom: Componentes verdadeiramente reutilizados */
.btn-primary {
  @apply bg-blue-500 hover:bg-blue-700 text-white px-4 py-2 rounded;
}

/* ❌ Ruim: @apply para tudo (perde vantagens do Tailwind) */
.my-div {
  @apply flex items-center justify-center p-4 bg-white rounded;
}
/* Melhor usar classes direto no HTML */
```

### 3. Organize Classes Logicamente

```html
<!-- ✅ Bom: Agrupado por função -->
<div class="
  flex items-center justify-between    <!-- Layout -->
  w-full max-w-4xl mx-auto             <!-- Size & Position -->
  p-4 md:p-6                           <!-- Spacing -->
  bg-white rounded-lg shadow-md        <!-- Appearance -->
  hover:shadow-lg transition           <!-- Interaction -->
">
```

### 4. Extraia Componentes Repetitivos

```tsx
// ❌ Ruim: Repetir classes em vários lugares
<button class="bg-blue-500 hover:bg-blue-700 text-white px-4 py-2 rounded">
<button class="bg-blue-500 hover:bg-blue-700 text-white px-4 py-2 rounded">
<button class="bg-blue-500 hover:bg-blue-700 text-white px-4 py-2 rounded">

// ✅ Bom: Componente React
function PrimaryButton({ children }) {
  return (
    <button className="bg-blue-500 hover:bg-blue-700 text-white px-4 py-2 rounded">
      {children}
    </button>
  );
}
```

### 5. Use Plugins Oficiais

```javascript
// tailwind.config.js
module.exports = {
  plugins: [
    require('@tailwindcss/forms'),        // Estilos melhores para forms
    require('@tailwindcss/typography'),   // Prose classes para conteúdo
    require('@tailwindcss/aspect-ratio'), // Aspect ratio utilities
    require('@tailwindcss/line-clamp'),   // Truncar texto
  ],
}
```

```html
<!-- @tailwindcss/typography -->
<article class="prose lg:prose-xl">
  <h1>Título</h1>
  <p>Parágrafo com estilos automáticos</p>
</article>

<!-- @tailwindcss/line-clamp -->
<p class="line-clamp-3">
  Texto longo que será truncado após 3 linhas...
</p>
```

### 6. PurgeCSS em Produção

```javascript
// tailwind.config.js
module.exports = {
  content: [
    './src/**/*.{js,jsx,ts,tsx}',
    './public/index.html',
  ],
  // PurgeCSS remove classes não usadas automaticamente
}
```

### 7. Use clsx/classnames para Conditional Classes

```bash
npm install clsx
```

```tsx
import clsx from 'clsx';

function Button({ primary, disabled, children }) {
  return (
    <button
      className={clsx(
        'px-4 py-2 rounded font-semibold',
        primary ? 'bg-blue-500 text-white' : 'bg-gray-300 text-black',
        disabled && 'opacity-50 cursor-not-allowed'
      )}
      disabled={disabled}
    >
      {children}
    </button>
  );
}
```

### 8. Dark Mode System

```typescript
// hooks/useDarkMode.ts
import { useEffect, useState } from 'react';

export function useDarkMode() {
  const [isDark, setIsDark] = useState(false);

  useEffect(() => {
    // Check local storage or system preference
    const isDarkMode = 
      localStorage.theme === 'dark' ||
      (!('theme' in localStorage) && 
       window.matchMedia('(prefers-color-scheme: dark)').matches);
    
    setIsDark(isDarkMode);
    
    if (isDarkMode) {
      document.documentElement.classList.add('dark');
    } else {
      document.documentElement.classList.remove('dark');
    }
  }, []);

  const toggleDarkMode = () => {
    if (isDark) {
      document.documentElement.classList.remove('dark');
      localStorage.theme = 'light';
    } else {
      document.documentElement.classList.add('dark');
      localStorage.theme = 'dark';
    }
    setIsDark(!isDark);
  };

  return { isDark, toggleDarkMode };
}

// Uso
function App() {
  const { isDark, toggleDarkMode } = useDarkMode();

  return (
    <div className="min-h-screen bg-white dark:bg-gray-900">
      <button onClick={toggleDarkMode}>
        {isDark ? '🌞' : '🌙'}
      </button>
    </div>
  );
}
```

---

## 🎯 Exemplos Completos

### Card Component

```html
<div class="max-w-sm rounded-lg overflow-hidden shadow-lg hover:shadow-xl transition">
  <img class="w-full h-48 object-cover" src="/image.jpg" alt="Card" />
  
  <div class="p-6">
    <div class="flex items-center gap-2 mb-2">
      <span class="bg-blue-100 text-blue-800 text-xs px-2 py-1 rounded">Tag</span>
    </div>
    
    <h2 class="text-xl font-bold text-gray-900 mb-2">
      Título do Card
    </h2>
    
    <p class="text-gray-600 text-sm mb-4">
      Descrição do card com informações relevantes sobre o conteúdo.
    </p>
    
    <div class="flex items-center justify-between">
      <div class="flex items-center gap-2">
        <img class="w-8 h-8 rounded-full" src="/avatar.jpg" alt="Avatar" />
        <span class="text-sm text-gray-700">João Silva</span>
      </div>
      
      <button class="bg-blue-500 hover:bg-blue-700 text-white px-4 py-2 rounded text-sm">
        Ver mais
      </button>
    </div>
  </div>
</div>
```

### Navigation Bar

```html
<nav class="bg-white shadow-lg">
  <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
    <div class="flex justify-between h-16">
      <!-- Logo -->
      <div class="flex items-center">
        <img class="h-8 w-auto" src="/logo.svg" alt="Logo" />
        <span class="ml-2 text-xl font-bold text-gray-900">Brand</span>
      </div>
      
      <!-- Desktop Menu -->
      <div class="hidden md:flex items-center space-x-8">
        <a href="#" class="text-gray-700 hover:text-blue-600 transition">Home</a>
        <a href="#" class="text-gray-700 hover:text-blue-600 transition">About</a>
        <a href="#" class="text-gray-700 hover:text-blue-600 transition">Services</a>
        <a href="#" class="text-gray-700 hover:text-blue-600 transition">Contact</a>
        
        <button class="bg-blue-500 hover:bg-blue-700 text-white px-4 py-2 rounded">
          Login
        </button>
      </div>
      
      <!-- Mobile Menu Button -->
      <div class="md:hidden flex items-center">
        <button class="text-gray-700 hover:text-blue-600">
          <svg class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16" />
          </svg>
        </button>
      </div>
    </div>
  </div>
</nav>
```

### Form

```html
<form class="max-w-md mx-auto bg-white p-8 rounded-lg shadow-md">
  <h2 class="text-2xl font-bold text-gray-900 mb-6">Cadastro</h2>
  
  <!-- Nome -->
  <div class="mb-4">
    <label class="block text-gray-700 text-sm font-semibold mb-2" for="name">
      Nome completo
    </label>
    <input 
      class="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent" 
      id="name" 
      type="text" 
      placeholder="João Silva"
    />
  </div>
  
  <!-- Email -->
  <div class="mb-4">
    <label class="block text-gray-700 text-sm font-semibold mb-2" for="email">
      Email
    </label>
    <input 
      class="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500" 
      id="email" 
      type="email" 
      placeholder="joao@example.com"
    />
  </div>
  
  <!-- Senha -->
  <div class="mb-6">
    <label class="block text-gray-700 text-sm font-semibold mb-2" for="password">
      Senha
    </label>
    <input 
      class="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500" 
      id="password" 
      type="password"
    />
  </div>
  
  <!-- Checkbox -->
  <div class="mb-6">
    <label class="flex items-center">
      <input type="checkbox" class="mr-2" />
      <span class="text-sm text-gray-700">
        Aceito os <a href="#" class="text-blue-500 hover:underline">termos de uso</a>
      </span>
    </label>
  </div>
  
  <!-- Submit -->
  <button 
    class="w-full bg-blue-500 hover:bg-blue-700 text-white font-bold py-3 px-4 rounded transition"
    type="submit"
  >
    Cadastrar
  </button>
</form>
```

---

## 📚 Recursos Adicionais

### Documentação
- [Tailwind CSS Docs](https://tailwindcss.com/docs)
- [Tailwind Play](https://play.tailwindcss.com) - Playground online
- [Tailwind UI](https://tailwindui.com) - Componentes premium

### Ferramentas
- **Tailwind CSS IntelliSense** (VS Code) - Autocomplete
- **Headless UI** - Componentes acessíveis sem estilo
- **daisyUI** - Biblioteca de componentes para Tailwind
- **Flowbite** - Componentes open-source

### Plugins Essenciais
```bash
npm install -D @tailwindcss/forms
npm install -D @tailwindcss/typography
npm install -D @tailwindcss/aspect-ratio
npm install -D @tailwindcss/container-queries
```

---

**Voltar para**: [📁 Styling](../../../README.md) | [📁 JavaScript](../../../../README.md) | [📁 Learning](../../../../../README.md) | [📁 Repositório Principal](../../../../../../README.md)
