# 📚 Docs para Estudo - Mini Documentações

Central de **mini-documentações** e resumos de conceitos, bibliotecas e frameworks para múltiplas tecnologias.

> Documentações concisas, práticas e com exemplos de código

---

## 📋 Índice

- [Conceitos Gerais](#-conceitos-gerais)
- [JavaScript/TypeScript](#-javascripttypescript)
- [Python](#-python)
- [Estatísticas](#-estatísticas)

---

## 🧠 Conceitos Gerais

### Arquiteturas
- 🏗️ **[Clean Architecture](Conceitos-Gerais/clea-arch.md)** - Arquitetura limpa
- 🏗️ **[Hexagonal Architecture](Conceitos-Gerais/arch-hexagonal.md)** - Ports & Adapters

### Compiladores
- ⚙️ **[Compiladores](Conceitos-Gerais/compiladores.md)** - Teoria de compiladores

### Estruturas de Dados
📁 **[estrutura-de-dados-mini-doc/](Conceitos-Gerais/estrutura-de-dados-mini-doc/)**

- Arrays, Listas, Pilhas, Filas
- Árvores (BST, AVL, Red-Black)
- Grafos e algoritmos
- Tabelas Hash
- Complexidade (Big O)

```python
# Exemplo: Binary Search Tree
class Node:
    def __init__(self, value):
        self.value = value
        self.left = None
        self.right = None

class BST:
    def insert(self, root, value):
        if not root:
            return Node(value)
        if value < root.value:
            root.left = self.insert(root.left, value)
        else:
            root.right = self.insert(root.right, value)
        return root
```

### Three.js
📁 **[threejs-mini-doc/](Conceitos-Gerais/threejs-mini-doc/)**

Biblioteca 3D para navegadores.

```javascript
const scene = new THREE.Scene();
const camera = new THREE.PerspectiveCamera(75, window.innerWidth / window.innerHeight, 0.1, 1000);
const renderer = new THREE.WebGLRenderer();

const geometry = new THREE.BoxGeometry();
const material = new THREE.MeshBasicMaterial({ color: 0x00ff00 });
const cube = new THREE.Mesh(geometry, material);
scene.add(cube);

function animate() {
    requestAnimationFrame(animate);
    cube.rotation.x += 0.01;
    renderer.render(scene, camera);
}
```

### TypeScript
📁 **[typescript-mini-doc/](Conceitos-Gerais/typescript-mini-doc/)**

Superset tipado de JavaScript.

```typescript
// Tipos básicos
let nome: string = "João";
let idade: number = 25;
let ativo: boolean = true;

// Interfaces
interface Usuario {
  id: number;
  nome: string;
  email: string;
}

// Generics
function primeiroElemento<T>(arr: T[]): T | undefined {
  return arr[0];
}

// Type Guards
function isString(value: unknown): value is string {
  return typeof value === 'string';
}
```

---

## 🟨 JavaScript/TypeScript

### Bibliotecas

#### React Query
📁 **[JavaScript/Bibliotecas/react-query-mini-doc/](JavaScript/Bibliotecas/react-query-mini-doc/)**

Gerenciamento de estado server-side.

```typescript
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';

// Query
const { data, isLoading } = useQuery({
  queryKey: ['produtos'],
  queryFn: () => fetch('/api/produtos').then(res => res.json())
});

// Mutation
const queryClient = useQueryClient();
const mutation = useMutation({
  mutationFn: (produto) => fetch('/api/produtos', {
    method: 'POST',
    body: JSON.stringify(produto)
  }),
  onSuccess: () => {
    queryClient.invalidateQueries({ queryKey: ['produtos'] });
  }
});
```

---

#### Zod
📁 **[JavaScript/Bibliotecas/zod-mini-doc/](JavaScript/Bibliotecas/zod-mini-doc/)**

Validação de schemas com TypeScript.

```typescript
import { z } from 'zod';

// Schema
const usuarioSchema = z.object({
  nome: z.string().min(3),
  email: z.string().email(),
  idade: z.number().min(18).max(120),
});

type Usuario = z.infer<typeof usuarioSchema>;

// Validar
const resultado = usuarioSchema.safeParse(dados);
if (resultado.success) {
  console.log(resultado.data);
} else {
  console.error(resultado.error.issues);
}
```

---

### Estilo

#### TailwindCSS
📁 **[JavaScript/Estilo/tailwindcss-mini-doc/](JavaScript/Estilo/tailwindcss-mini-doc/)**

Framework CSS utility-first.

```tsx
<div className="flex items-center justify-between p-4 bg-white rounded-lg shadow-md hover:shadow-lg transition-shadow">
  <h2 className="text-2xl font-bold text-gray-800">Título</h2>
  <button className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600">
    Clique
  </button>
</div>
```

---

### Frameworks

#### React Hook Form
📁 **[JavaScript/Frameworks/react-hook-form-mini-doc/](JavaScript/Frameworks/react-hook-form-mini-doc/)**

Formulários performáticos com React.

```typescript
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';

const { register, handleSubmit, formState: { errors } } = useForm({
  resolver: zodResolver(schema)
});

<form onSubmit={handleSubmit(onSubmit)}>
  <input {...register("email")} />
  {errors.email && <span>{errors.email.message}</span>}
  <button type="submit">Enviar</button>
</form>
```

---

#### React Router
📁 **[JavaScript/Frameworks/react-router-mini-doc/](JavaScript/Frameworks/react-router-mini-doc/)**

Roteamento para React.

```typescript
import { createBrowserRouter, RouterProvider } from 'react-router-dom';

const router = createBrowserRouter([
  {
    path: "/",
    element: <Home />,
    errorElement: <ErrorPage />,
  },
  {
    path: "/produtos/:id",
    element: <Produto />,
    loader: ({ params }) => fetch(`/api/produtos/${params.id}`)
  }
]);

<RouterProvider router={router} />
```

---

#### React + TypeScript
📁 **[JavaScript/Frameworks/react-typescript-mini-doc/](JavaScript/Frameworks/react-typescript-mini-doc/)**

React com TypeScript completo.

```typescript
interface ButtonProps {
  children: React.ReactNode;
  onClick: () => void;
  variant?: 'primary' | 'secondary';
}

const Button: React.FC<ButtonProps> = ({ 
  children, 
  onClick, 
  variant = 'primary' 
}) => {
  return (
    <button onClick={onClick} className={variant}>
      {children}
    </button>
  );
};
```

---

### Testes

#### Jest
📁 **[JavaScript/Testes/jest-mini-doc/](JavaScript/Testes/jest-mini-doc/)**

Framework de testes para JavaScript.

```typescript
describe('Calculator', () => {
  it('deve somar dois números', () => {
    expect(sum(1, 2)).toBe(3);
  });

  it('deve lançar erro para entrada inválida', () => {
    expect(() => sum('a', 'b')).toThrow();
  });
});

// Mocks
jest.mock('./api');
const mockFetch = jest.fn().mockResolvedValue({ data: 'test' });
```

---

## 🐍 Python

📁 **[Python/README.md](Python/README.md)** - **Documentação Completa**

### Linguagem
📁 **[Python/linguagem/](Python/linguagem/)**
- Sintaxe básica
- POO
- Decorators
- Type Hints
- Async/await

### Machine Learning & Computer Vision
📄 **[Python/ML-visao-computacional/ML.md](Python/ML-visao-computacional/ML.md)**

#### Bibliotecas Essenciais

| Biblioteca | Descrição | Exemplo |
|------------|-----------|---------|
| **NumPy** | Arrays e computação numérica | `np.array([1,2,3])` |
| **Pandas** | Análise de dados tabulares | `df.groupby('col').mean()` |
| **Matplotlib** | Visualização | `plt.plot(x, y)` |
| **Scikit-learn** | ML clássico | `RandomForestClassifier()` |
| **YOLO** | Detecção de objetos | `model.predict('img.jpg')` |

#### Frameworks

**OpenCV** 📁 [opencv-mini-doc/](Python/ML-visao-computacional/opencv-mini-doc/)
```python
import cv2
img = cv2.imread('image.jpg')
gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
edges = cv2.Canny(gray, 100, 200)
```

**PyTorch** 📁 [pytorch-mini-doc/](Python/ML-visao-computacional/pytorch-mini-doc/)
```python
import torch
import torch.nn as nn

class Net(nn.Module):
    def __init__(self):
        super().__init__()
        self.fc1 = nn.Linear(784, 128)
        self.fc2 = nn.Linear(128, 10)
    
    def forward(self, x):
        x = torch.relu(self.fc1(x))
        return self.fc2(x)
```

📓 **[Iris PyTorch Notebook](Python/ML-visao-computacional/pytorch-mini-doc/Iris_PyTorch.ipynb)**

**TensorFlow** 📁 [tensorflow-mini-doc/](Python/ML-visao-computacional/tensorflow-mini-doc/)
```python
import tensorflow as tf

model = tf.keras.Sequential([
    tf.keras.layers.Dense(128, activation='relu'),
    tf.keras.layers.Dense(10, activation='softmax')
])
```

---

## 📊 Estatísticas

| Categoria | Tecnologias | Mini-Docs |
|-----------|-------------|-----------|
| **Conceitos Gerais** | Arquiteturas, Compiladores, Estruturas de Dados, Three.js, TypeScript | 6 |
| **JavaScript** | React Query, Zod, TailwindCSS, React Hook Form, React Router, Jest | 7 |
| **Python** | NumPy, Pandas, Matplotlib, Scikit-learn, YOLO, OpenCV, PyTorch, TensorFlow | 10+ |
| **Total** | 20+ tecnologias | **25+ documentações** |

---

## 🎯 Como Usar

1. **Escolha a tecnologia** que deseja estudar no índice acima
2. **Acesse a pasta** correspondente
3. **Leia o README.md** para visão geral
4. **Pratique com exemplos** de código fornecidos
5. **Consulte recursos** adicionais nos links

---

## 🤝 Contribuindo

Para adicionar uma nova mini-documentação:

1. Crie pasta seguindo padrão: `nome-tecnologia-mini-doc/`
2. Adicione `README.md` com estrutura:
   - Introdução
   - Exemplos práticos
   - Recursos adicionais
3. Atualize este índice principal

---

**Voltar para**: [📁 Repositório Principal](../README.md)
- [Machine Learning](Python/ML-visao-computacional/ML.md)
