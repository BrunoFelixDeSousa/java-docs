# DeepLearning4J - Guia Completo de Deep Learning em Java

[![Maven Central](https://img.shields.io/maven-central/v/org.deeplearning4j/deeplearning4j-core.svg?label=Maven%20Central)](https://search.maven.org/artifact/org.deeplearning4j/deeplearning4j-core)
[![Java](https://img.shields.io/badge/Java-8%2B-blue.svg)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/License-Apache%202.0-green.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![CUDA](https://img.shields.io/badge/CUDA-11.0%2B-76B900.svg)](https://developer.nvidia.com/cuda-downloads)

## 📋 Índice

1. [Introdução](#-introdução)
2. [Instalação e Setup](#-instalação-e-setup)
3. [ND4J - Arrays Multidimensionais](#-nd4j---arrays-multidimensionais)
4. [DataVec - Processamento de Dados](#-datavec---processamento-de-dados)
5. [Redes Neurais Feedforward](#-redes-neurais-feedforward)
6. [Redes Convolucionais (CNN)](#-redes-convolucionais-cnn)
7. [Redes Recorrentes (RNN/LSTM)](#-redes-recorrentes-rnnlstm)
8. [Transfer Learning](#-transfer-learning)
9. [Otimização e Hiperparâmetros](#-otimização-e-hiperparâmetros)
10. [Avaliação de Modelos](#-avaliação-de-modelos)
11. [GPU e Performance](#-gpu-e-performance)
12. [Produção e Deploy](#-produção-e-deploy)
13. [Testes](#-testes)
14. [Best Practices](#-best-practices)
15. [Referência Rápida](#-referência-rápida)
16. [Recursos](#-recursos)

---

## 🎯 Introdução

### O que é DeepLearning4J?

DeepLearning4J (DL4J) é um **framework de deep learning open-source** escrito em Java e Scala, projetado especificamente para ambientes empresariais e aplicações distribuídas na JVM.

### Por que usar DeepLearning4J?

| Aspecto | DeepLearning4J | TensorFlow/PyTorch |
|---------|----------------|---------------------|
| **Linguagem** | Java/Scala (JVM) | Python |
| **Integração Enterprise** | ✅ Nativa (Spring, Spark, Hadoop) | ❌ Requer bridges |
| **Computação Distribuída** | ✅ Spark nativo | ⚠️ Requer configuração |
| **GPU Support** | ✅ CUDA nativo | ✅ CUDA nativo |
| **Produção** | ✅ JVM ecosystem | ⚠️ Requer containerização |
| **Comunidade** | ⚠️ Menor | ✅ Muito grande |

### Arquitetura do Ecossistema

```
┌─────────────────────────────────────────────────┐
│           Aplicação Java/Scala                  │
├─────────────────────────────────────────────────┤
│  DL4J           RL4J          Arbiter           │
│ (Neural Nets) (Reinforcement) (HyperParam Opt)  │
├─────────────────────────────────────────────────┤
│              DataVec (ETL/Preprocessing)        │
├─────────────────────────────────────────────────┤
│              ND4J (N-Dimensional Arrays)        │
├─────────────────────────────────────────────────┤
│         Native Backend (CPU/CUDA/OpenCL)        │
└─────────────────────────────────────────────────┘
```

### Componentes Principais

**1. ND4J (N-Dimensional Arrays for Java)**:
- Equivalente ao NumPy em Java
- Suporte para operações matriciais otimizadas
- Backends: CPU, CUDA (GPU), OpenCL

**2. DataVec**:
- ETL para machine learning
- Transformação de dados (CSV, imagens, vídeo, áudio)
- Pré-processamento e normalização

**3. DL4J Core**:
- Redes neurais (Feedforward, CNN, RNN, LSTM, GAN)
- Treinamento e otimização
- Importação de modelos (Keras, TensorFlow)

**4. Arbiter**:
- Otimização de hiperparâmetros
- Grid search e random search

**5. RL4J**:
- Reinforcement Learning
- Deep Q-Networks (DQN)

---

## ⚙️ Instalação e Setup

### Pré-requisitos

- **Java**: 8+ (recomendado Java 11 ou 17)
- **Maven/Gradle**: Para gerenciamento de dependências
- **RAM**: 4GB mínimo (8GB+ recomendado)
- **GPU** (opcional): NVIDIA com CUDA 11.0+

### Maven Dependencies

#### Backend CPU (Padrão)

```xml
<properties>
    <dl4j.version>1.0.0-M2.1</dl4j.version>
    <nd4j.version>1.0.0-M2.1</nd4j.version>
</properties>

<dependencies>
    <!-- DL4J Core -->
    <dependency>
        <groupId>org.deeplearning4j</groupId>
        <artifactId>deeplearning4j-core</artifactId>
        <version>${dl4j.version}</version>
    </dependency>
    
    <!-- ND4J Backend CPU -->
    <dependency>
        <groupId>org.nd4j</groupId>
        <artifactId>nd4j-native-platform</artifactId>
        <version>${nd4j.version}</version>
    </dependency>
    
    <!-- DataVec -->
    <dependency>
        <groupId>org.datavec</groupId>
        <artifactId>datavec-api</artifactId>
        <version>${dl4j.version}</version>
    </dependency>
    
    <!-- DataVec Data Formats -->
    <dependency>
        <groupId>org.datavec</groupId>
        <artifactId>datavec-data-image</artifactId>
        <version>${dl4j.version}</version>
    </dependency>
    
    <!-- Logging -->
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-simple</artifactId>
        <version>1.7.36</version>
    </dependency>
</dependencies>
```

#### Backend GPU (CUDA)

```xml
<dependency>
    <groupId>org.nd4j</groupId>
    <artifactId>nd4j-cuda-11.6-platform</artifactId>
    <version>${nd4j.version}</version>
</dependency>
```

> **Nota**: Troque `nd4j-native-platform` por `nd4j-cuda-*` para usar GPU. Apenas uma dependência de backend pode estar no classpath.

### Gradle Dependencies

```gradle
def dl4jVersion = '1.0.0-M2.1'

dependencies {
    implementation "org.deeplearning4j:deeplearning4j-core:${dl4jVersion}"
    implementation "org.nd4j:nd4j-native-platform:${dl4jVersion}"
    implementation "org.datavec:datavec-api:${dl4jVersion}"
    implementation "org.datavec:datavec-data-image:${dl4jVersion}"
    implementation "org.slf4j:slf4j-simple:1.7.36"
}
```

### Verificação da Instalação

```java
package com.example.dl4j.setup;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

/**
 * Verifica instalação do DeepLearning4J e ND4J.
 */
public class SetupVerification {
    
    /**
     * Método main para verificação.
     *
     * @param args argumentos não utilizados
     */
    public static void main(String[] args) {
        System.out.println("=== Verificação DeepLearning4J ===\n");
        
        // 1. Verificar ND4J
        INDArray array = Nd4j.ones(3, 3);
        System.out.println("ND4J Array (3x3 de 1s):");
        System.out.println(array);
        
        // 2. Verificar backend
        String backend = Nd4j.getBackend().getClass().getSimpleName();
        System.out.println("\nBackend ativo: " + backend);
        
        // 3. Verificar GPU (se disponível)
        if (backend.contains("Cuda")) {
            System.out.println("✅ GPU CUDA detectada!");
            System.out.println("Dispositivos: " + Nd4j.getAffinityManager().getNumberOfDevices());
        } else {
            System.out.println("⚠️  Usando CPU (backend nativo)");
        }
        
        // 4. Operação matemática básica
        INDArray a = Nd4j.create(new double[][]{{1, 2}, {3, 4}});
        INDArray b = Nd4j.create(new double[][]{{5, 6}, {7, 8}});
        INDArray result = a.mmul(b); // Multiplicação matricial
        
        System.out.println("\nMultiplicação Matricial:");
        System.out.println("A =\n" + a);
        System.out.println("B =\n" + b);
        System.out.println("A × B =\n" + result);
        
        System.out.println("\n✅ Setup completo e funcionando!");
    }
}
```

**Output esperado**:
```
=== Verificação DeepLearning4J ===

ND4J Array (3x3 de 1s):
[[1.00, 1.00, 1.00],
 [1.00, 1.00, 1.00],
 [1.00, 1.00, 1.00]]

Backend ativo: CpuNDArrayFactory

⚠️  Usando CPU (backend nativo)

Multiplicação Matricial:
A =
[[1.00, 2.00],
 [3.00, 4.00]]
B =
[[5.00, 6.00],
 [7.00, 8.00]]
A × B =
[[19.00, 22.00],
 [43.00, 50.00]]

✅ Setup completo e funcionando!
```

---

## 🔢 ND4J - Arrays Multidimensionais

### Conceito Fundamental

**ND4J** (N-Dimensional Arrays for Java) é a biblioteca de álgebra linear que sustenta todo o DL4J. É o equivalente ao **NumPy** em Python.

### Por que ND4J é importante?

| Operação | Java Puro | ND4J |
|----------|-----------|------|
| Multiplicação de matrizes 1000×1000 | ~2000ms | ~20ms |
| Operações vetorizadas | ❌ Loops manuais | ✅ Nativo otimizado |
| GPU acceleration | ❌ N/A | ✅ CUDA automático |

### Criação de Arrays

```java
package com.example.dl4j.nd4j;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

/**
 * Demonstração de criação e manipulação de INDArrays.
 */
public class ND4JBasics {
    
    /**
     * Cria arrays de diferentes formas.
     *
     * @return array demonstrativo
     */
    public static INDArray createArrays() {
        // Vetor (1D)
        INDArray vetor = Nd4j.create(new double[]{1, 2, 3, 4, 5});
        System.out.println("Vetor (1D):");
        System.out.println(vetor);
        
        // Matriz (2D)
        INDArray matriz = Nd4j.create(new double[][]{
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        });
        System.out.println("\nMatriz (2D):");
        System.out.println(matriz);
        
        // Tensor 3D (batch × linhas × colunas)
        INDArray tensor3D = Nd4j.zeros(2, 3, 4); // 2 matrizes 3×4
        System.out.println("\nTensor 3D (2×3×4):");
        System.out.println(tensor3D);
        
        // Arrays especiais
        INDArray zeros = Nd4j.zeros(3, 3);
        INDArray ones = Nd4j.ones(3, 3);
        INDArray eye = Nd4j.eye(3); // Matriz identidade
        INDArray random = Nd4j.rand(3, 3); // Valores aleatórios [0, 1)
        
        System.out.println("\nMatriz Identidade:");
        System.out.println(eye);
        
        return matriz;
    }
    
    /**
     * Demonstra operações matemáticas básicas.
     */
    public static void mathematicalOperations() {
        System.out.println("\n=== Operações Matemáticas ===\n");
        
        INDArray a = Nd4j.create(new double[][]{{1, 2}, {3, 4}});
        INDArray b = Nd4j.create(new double[][]{{5, 6}, {7, 8}});
        
        // Operações elemento por elemento
        INDArray soma = a.add(b);
        INDArray subtracao = a.sub(b);
        INDArray multiplicacao = a.mul(b); // Element-wise
        INDArray divisao = a.div(b);
        
        System.out.println("A + B (element-wise):\n" + soma);
        System.out.println("\nA × B (element-wise):\n" + multiplicacao);
        
        // Multiplicação matricial
        INDArray matmul = a.mmul(b);
        System.out.println("\nA × B (multiplicação matricial):\n" + matmul);
        
        // Operações com escalares
        INDArray scalar = a.mul(2); // Multiplica todos por 2
        INDArray power = a.pow(2);  // Eleva ao quadrado
        
        System.out.println("\nA × 2:\n" + scalar);
        System.out.println("\nA²:\n" + power);
    }
    
    /**
     * Demonstra operações de agregação.
     */
    public static void aggregationOperations() {
        System.out.println("\n=== Operações de Agregação ===\n");
        
        INDArray matriz = Nd4j.create(new double[][]{
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        });
        
        System.out.println("Matriz original:\n" + matriz);
        
        // Agregações globais
        double sum = matriz.sumNumber().doubleValue();
        double mean = matriz.meanNumber().doubleValue();
        double max = matriz.maxNumber().doubleValue();
        double min = matriz.minNumber().doubleValue();
        
        System.out.println("\nSoma total: " + sum);
        System.out.println("Média: " + mean);
        System.out.println("Máximo: " + max);
        System.out.println("Mínimo: " + min);
        
        // Agregações por eixo
        INDArray sumColunas = matriz.sum(0); // Soma cada coluna
        INDArray sumLinhas = matriz.sum(1);  // Soma cada linha
        
        System.out.println("\nSoma por coluna:\n" + sumColunas);
        System.out.println("Soma por linha:\n" + sumLinhas);
    }
    
    /**
     * Demonstra indexação e slicing.
     */
    public static void indexingAndSlicing() {
        System.out.println("\n=== Indexação e Slicing ===\n");
        
        INDArray matriz = Nd4j.create(new double[][]{
            {1,  2,  3,  4},
            {5,  6,  7,  8},
            {9, 10, 11, 12}
        });
        
        System.out.println("Matriz original:\n" + matriz);
        
        // Acessar elemento específico
        double elemento = matriz.getDouble(1, 2); // Linha 1, Coluna 2
        System.out.println("\nElemento [1,2]: " + elemento);
        
        // Slice de linha
        INDArray linha1 = matriz.getRow(1);
        System.out.println("\nLinha 1:\n" + linha1);
        
        // Slice de coluna
        INDArray coluna2 = matriz.getColumn(2);
        System.out.println("\nColuna 2:\n" + coluna2);
        
        // Slice range (linhas 0-1, colunas 1-2)
        INDArray submatriz = matriz.get(
            Nd4j.createFromArray(0, 1), 
            Nd4j.createFromArray(1, 2)
        );
        System.out.println("\nSubmatriz [0:2, 1:3]:\n" + submatriz);
    }
    
    /**
     * Demonstra broadcasting.
     */
    public static void broadcasting() {
        System.out.println("\n=== Broadcasting ===\n");
        
        INDArray matriz = Nd4j.create(new double[][]{
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        });
        
        INDArray vetor = Nd4j.create(new double[]{10, 20, 30});
        
        System.out.println("Matriz (3×3):\n" + matriz);
        System.out.println("\nVetor (1×3):\n" + vetor);
        
        // Broadcasting: vetor é "expandido" para cada linha
        INDArray resultado = matriz.addRowVector(vetor);
        
        System.out.println("\nMatriz + Vetor (broadcast):\n" + resultado);
    }
    
    /**
     * Método main para demonstração.
     *
     * @param args argumentos não utilizados
     */
    public static void main(String[] args) {
        System.out.println("=== ND4J - N-Dimensional Arrays ===\n");
        
        createArrays();
        mathematicalOperations();
        aggregationOperations();
        indexingAndSlicing();
        broadcasting();
        
        System.out.println("\n✅ ND4J demonstrado com sucesso!");
    }
}
```

### Operações Avançadas

```java
package com.example.dl4j.nd4j;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.ops.transforms.Transforms;

/**
 * Operações avançadas com ND4J.
 */
public class ND4JAdvanced {
    
    /**
     * Álgebra linear avançada.
     */
    public static void linearAlgebra() {
        System.out.println("=== Álgebra Linear Avançada ===\n");
        
        INDArray A = Nd4j.create(new double[][]{
            {4, 2},
            {3, 1}
        });
        
        System.out.println("Matriz A:\n" + A);
        
        // Transposta
        INDArray AT = A.transpose();
        System.out.println("\nTransposta A^T:\n" + AT);
        
        // Inversa
        INDArray Ainv = Nd4j.linalg().solve(A, Nd4j.eye(2));
        System.out.println("\nInversa A^-1:\n" + Ainv);
        
        // Verificação: A × A^-1 = I
        INDArray identity = A.mmul(Ainv);
        System.out.println("\nA × A^-1 (deve ser identidade):\n" + identity);
        
        // Determinante
        double det = Nd4j.linalg().det(A).getDouble(0);
        System.out.println("\nDeterminante: " + det);
    }
    
    /**
     * Transformações matemáticas.
     */
    public static void transformations() {
        System.out.println("\n=== Transformações ===\n");
        
        INDArray x = Nd4j.create(new double[]{-2, -1, 0, 1, 2});
        
        System.out.println("X = " + x);
        
        // Funções de ativação comuns
        INDArray sigmoid = Transforms.sigmoid(x.dup());
        INDArray tanh = Transforms.tanh(x.dup());
        INDArray relu = Transforms.relu(x.dup());
        
        System.out.println("\nSigmoid(X) = " + sigmoid);
        System.out.println("Tanh(X) = " + tanh);
        System.out.println("ReLU(X) = " + relu);
        
        // Outras transformações
        INDArray exp = Transforms.exp(x.dup());
        INDArray log = Transforms.log(Nd4j.abs(x).add(1)); // log(|x| + 1)
        INDArray sqrt = Transforms.sqrt(Nd4j.abs(x));
        
        System.out.println("\nExp(X) = " + exp);
        System.out.println("Log(|X| + 1) = " + log);
        System.out.println("Sqrt(|X|) = " + sqrt);
    }
    
    /**
     * Normalização de dados.
     */
    public static void normalization() {
        System.out.println("\n=== Normalização ===\n");
        
        INDArray data = Nd4j.create(new double[][]{
            {100, 200, 300},
            {10, 20, 30},
            {1, 2, 3}
        });
        
        System.out.println("Dados originais:\n" + data);
        
        // Min-Max Normalization [0, 1]
        double min = data.minNumber().doubleValue();
        double max = data.maxNumber().doubleValue();
        INDArray minMaxNorm = data.sub(min).div(max - min);
        
        System.out.println("\nMin-Max Normalização [0, 1]:\n" + minMaxNorm);
        
        // Z-Score Normalization (mean=0, std=1)
        double mean = data.meanNumber().doubleValue();
        double std = data.stdNumber().doubleValue();
        INDArray zScore = data.sub(mean).div(std);
        
        System.out.println("\nZ-Score Normalização:\n" + zScore);
        System.out.printf("Média: %.2f, Desvio: %.2f%n", 
            zScore.meanNumber().doubleValue(), 
            zScore.stdNumber().doubleValue());
    }
    
    /**
     * Método main para demonstração.
     *
     * @param args argumentos não utilizados
     */
    public static void main(String[] args) {
        linearAlgebra();
        transformations();
        normalization();
        
        System.out.println("\n✅ Operações avançadas demonstradas!");
    }
}
```

### API Reference - ND4J

| Método | Descrição | Exemplo |
|--------|-----------|---------|
| `Nd4j.create(double[])` | Cria vetor 1D | `Nd4j.create(new double[]{1,2,3})` |
| `Nd4j.create(double[][])` | Cria matriz 2D | `Nd4j.create(new double[][]{{1,2},{3,4}})` |
| `Nd4j.zeros(int...)` | Array de zeros | `Nd4j.zeros(3, 3)` |
| `Nd4j.ones(int...)` | Array de uns | `Nd4j.ones(2, 4)` |
| `Nd4j.eye(int)` | Matriz identidade | `Nd4j.eye(3)` |
| `Nd4j.rand(int...)` | Valores aleatórios [0,1) | `Nd4j.rand(3, 3)` |
| `array.add(other)` | Soma element-wise | `a.add(b)` |
| `array.mmul(other)` | Multiplicação matricial | `a.mmul(b)` |
| `array.transpose()` | Transposta | `a.transpose()` |
| `array.sum(int)` | Soma por eixo | `a.sum(0)` |
| `Transforms.sigmoid(array)` | Função sigmoid | `Transforms.sigmoid(x)` |

---

## 📊 DataVec - Processamento de Dados

### Conceito

**DataVec** é o módulo de ETL (Extract, Transform, Load) do DL4J. Converte dados brutos (CSV, imagens, texto, áudio) em formato que redes neurais entendem (**INDArray**).

### Por que DataVec?

```java
// ❌ Sem DataVec (manual, propenso a erros)
BufferedReader reader = new BufferedReader(new FileReader("data.csv"));
String line;
List<double[]> data = new ArrayList<>();
while ((line = reader.readLine()) != null) {
    String[] values = line.split(",");
    double[] row = new double[values.length];
    for (int i = 0; i < values.length; i++) {
        row[i] = Double.parseDouble(values[i]);
    }
    data.add(row);
}
// Converter para INDArray, normalizar, criar batches...

// ✅ Com DataVec (uma linha)
RecordReader recordReader = new CSVRecordReader(0, ',');
recordReader.initialize(new FileSplit(new File("data.csv")));
DataSetIterator iterator = new RecordReaderDataSetIterator(recordReader, 32, 4, 3);
```

### Exemplo Completo: Iris Dataset

```java
package com.example.dl4j.datavec;

import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerStandardize;

import java.io.File;
import java.io.IOException;

/**
 * Demonstração de DataVec com Iris Dataset.
 * 
 * Dataset: 150 amostras de flores Iris com 4 features:
 * - Sepal length, Sepal width, Petal length, Petal width
 * - 3 classes: Setosa, Versicolor, Virginica
 */
public class IrisDataLoader {
    
    /**
     * Carrega e preprocessa Iris dataset.
     *
     * @param filePath caminho para arquivo CSV
     * @param batchSize tamanho do batch
     * @return iterator de datasets
     * @throws IOException se erro ao ler arquivo
     * @throws InterruptedException se interrompido
     */
    public static DataSetIterator loadIrisData(String filePath, int batchSize) 
            throws IOException, InterruptedException {
        
        // 1. Criar RecordReader para CSV
        int numLinesToSkip = 0;  // Sem cabeçalho
        char delimiter = ',';
        RecordReader recordReader = new CSVRecordReader(numLinesToSkip, delimiter);
        
        // 2. Inicializar com arquivo
        recordReader.initialize(new FileSplit(new File(filePath)));
        
        // 3. Criar DataSetIterator
        int labelIndex = 4;      // Coluna 4 é o label (0-indexed)
        int numClasses = 3;      // 3 tipos de flores
        
        DataSetIterator iterator = new RecordReaderDataSetIterator(
            recordReader,
            batchSize,
            labelIndex,
            numClasses
        );
        
        return iterator;
    }
    
    /**
     * Normaliza dados (Z-score: mean=0, std=1).
     *
     * @param iterator iterator de dados
     * @return normalizer configurado
     */
    public static DataNormalization normalizeData(DataSetIterator iterator) {
        // Criar normalizer
        DataNormalization normalizer = new NormalizerStandardize();
        
        // Calcular estatísticas (mean, std) dos dados
        normalizer.fit(iterator);
        
        // Reset iterator
        iterator.reset();
        
        // Aplicar normalização
        iterator.setPreProcessor(normalizer);
        
        return normalizer;
    }
    
    /**
     * Demonstra uso do DataLoader.
     *
     * @param args argumentos não utilizados
     */
    public static void main(String[] args) {
        try {
            System.out.println("=== DataVec - Iris Dataset ===\n");
            
            // Caminho do dataset (baixar de UCI ML Repository)
            String filePath = "data/iris.csv";
            int batchSize = 50;
            
            // 1. Carregar dados
            DataSetIterator iterator = loadIrisData(filePath, batchSize);
            System.out.println("✅ Dados carregados");
            System.out.println("Total de batches: " + iterator.totalExamples() / batchSize);
            
            // 2. Normalizar
            DataNormalization normalizer = normalizeData(iterator);
            System.out.println("✅ Normalização configurada");
            
            // 3. Iterar sobre batches
            System.out.println("\n=== Primeiros 3 batches ===");
            int batchNum = 0;
            while (iterator.hasNext() && batchNum < 3) {
                DataSet batch = iterator.next();
                
                System.out.println("\nBatch " + (batchNum + 1) + ":");
                System.out.println("  Features shape: " + batch.getFeatures().shapeInfoToString());
                System.out.println("  Labels shape: " + batch.getLabels().shapeInfoToString());
                System.out.println("  Num examples: " + batch.numExamples());
                
                // Mostrar primeira amostra
                System.out.println("\n  Primeira amostra:");
                System.out.println("    Features: " + batch.getFeatures().getRow(0));
                System.out.println("    Label: " + batch.getLabels().getRow(0));
                
                batchNum++;
            }
            
            System.out.println("\n✅ DataVec demonstrado com sucesso!");
            
        } catch (Exception e) {
            System.err.println("❌ Erro: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
```

### Transformações de Dados

```java
package com.example.dl4j.datavec;

import org.datavec.api.transform.TransformProcess;
import org.datavec.api.transform.schema.Schema;
import org.datavec.api.transform.transform.normalize.Normalize;
import org.datavec.api.transform.filter.ConditionFilter;
import org.datavec.api.transform.condition.column.DoubleColumnCondition;
import org.datavec.api.transform.condition.ConditionOp;

/**
 * Transformações e preprocessamento com DataVec.
 */
public class DataTransformations {
    
    /**
     * Cria pipeline de transformação.
     *
     * @return processo de transformação
     */
    public static TransformProcess createTransformPipeline() {
        // 1. Definir schema dos dados
        Schema inputSchema = new Schema.Builder()
            .addColumnDouble("feature1")
            .addColumnDouble("feature2")
            .addColumnDouble("feature3")
            .addColumnInteger("label")
            .build();
        
        // 2. Criar pipeline de transformações
        TransformProcess process = new TransformProcess.Builder(inputSchema)
            // Remover linhas com valores faltantes
            .removeColumnsWithMissingValues()
            
            // Filtrar valores fora do range
            .filter(new ConditionFilter(
                new DoubleColumnCondition("feature1", ConditionOp.LessThan, 0.0)
            ))
            
            // Normalizar features
            .normalize("feature1", Normalize.MinMax, 0, 100)
            .normalize("feature2", Normalize.MinMax, 0, 100)
            
            // Remover coluna desnecessária
            .removeColumns("feature3")
            
            .build();
        
        return process;
    }
    
    /**
     * Método main para demonstração.
     *
     * @param args argumentos não utilizados
     */
    public static void main(String[] args) {
        TransformProcess process = createTransformPipeline();
        
        System.out.println("=== Pipeline de Transformação ===\n");
        System.out.println("Schema inicial:");
        System.out.println(process.getInitialSchema());
        
        System.out.println("\nSchema final:");
        System.out.println(process.getFinalSchema());
        
        System.out.println("\nTransformações aplicadas:");
        process.getActionList().forEach(action -> 
            System.out.println("  - " + action.toString())
        );
    }
}
```

### Carregamento de Imagens

```java
package com.example.dl4j.datavec;

import org.datavec.api.split.FileSplit;
import org.datavec.image.loader.NativeImageLoader;
import org.datavec.image.recordreader.ImageRecordReader;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;

import java.io.File;
import java.util.Random;

/**
 * Carregamento e preprocessamento de imagens.
 */
public class ImageDataLoader {
    
    /**
     * Carrega dataset de imagens.
     *
     * @param dataPath diretório com imagens
     * @param height altura da imagem
     * @param width largura da imagem
     * @param channels canais (1=grayscale, 3=RGB)
     * @param numLabels número de classes
     * @param batchSize tamanho do batch
     * @return iterator de imagens
     * @throws Exception se erro ao carregar
     */
    public static DataSetIterator loadImages(
            String dataPath,
            int height,
            int width,
            int channels,
            int numLabels,
            int batchSize) throws Exception {
        
        // 1. Criar ImageRecordReader
        ImageRecordReader recordReader = new ImageRecordReader(
            height, width, channels
        );
        
        // 2. Inicializar com diretório de imagens
        File dataDir = new File(dataPath);
        FileSplit split = new FileSplit(dataDir, 
            NativeImageLoader.ALLOWED_FORMATS, 
            new Random(123));
        
        recordReader.initialize(split);
        
        // 3. Criar DataSetIterator
        DataSetIterator iterator = new RecordReaderDataSetIterator(
            recordReader,
            batchSize,
            1,          // Label index (baseado em nome do arquivo)
            numLabels
        );
        
        return iterator;
    }
    
    /**
     * Método main para demonstração.
     *
     * @param args argumentos não utilizados
     */
    public static void main(String[] args) {
        try {
            System.out.println("=== Image Data Loader ===\n");
            
            // Configuração MNIST-like (28x28 grayscale)
            String dataPath = "data/images/";
            int height = 28;
            int width = 28;
            int channels = 1;  // Grayscale
            int numLabels = 10; // 0-9 dígitos
            int batchSize = 32;
            
            DataSetIterator iterator = loadImages(
                dataPath, height, width, channels, numLabels, batchSize
            );
            
            System.out.println("✅ Image loader configurado");
            System.out.println("Batch size: " + batchSize);
            System.out.println("Image dimensions: " + height + "×" + width + "×" + channels);
            System.out.println("Num classes: " + numLabels);
            
        } catch (Exception e) {
            System.err.println("❌ Erro: " + e.getMessage());
        }
    }
}
```

---

## 🧠 Redes Neurais Feedforward

### Conceito

**Redes Feedforward** (ou Multi-Layer Perceptron - MLP) são o tipo mais simples de rede neural artificial. Os dados fluem em uma única direção: entrada → camadas ocultas → saída.

### Anatomia de uma Rede Neural

```
Input Layer    Hidden Layer(s)    Output Layer
    ●                ●                  ●
    ●──────────────→ ●──────────────→  ●
    ●                ●                  ●
    ●                ●
```

### Exemplo Completo: XOR Problem

O problema XOR é um clássico em redes neurais porque não é **linearmente separável** (impossível resolver com apenas uma camada).

```java
package com.example.dl4j.feedforward;

import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;

/**
 * Rede Neural para resolver o problema XOR.
 * 
 * XOR Truth Table:
 * Input  | Output
 * 0 0    | 0
 * 0 1    | 1
 * 1 0    | 1
 * 1 1    | 0
 */
public class XORNeuralNetwork {
    
    /**
     * Cria configuração da rede neural.
     *
     * @return configuração da rede
     */
    public static MultiLayerConfiguration createNetworkConfiguration() {
        int seed = 123;
        double learningRate = 0.1;
        int numInputs = 2;    // [x1, x2]
        int numHidden = 4;    // 4 neurônios na camada oculta
        int numOutputs = 1;   // XOR result
        
        return new NeuralNetConfiguration.Builder()
            .seed(seed)
            .weightInit(WeightInit.XAVIER)
            .updater(new Adam(learningRate))
            .list()
            
            // Camada Oculta: 2 inputs → 4 neurons (ReLU activation)
            .layer(0, new DenseLayer.Builder()
                .nIn(numInputs)
                .nOut(numHidden)
                .activation(Activation.RELU)
                .build())
            
            // Camada de Saída: 4 neurons → 1 output (Sigmoid activation)
            .layer(1, new OutputLayer.Builder(LossFunctions.LossFunction.MSE)
                .nIn(numHidden)
                .nOut(numOutputs)
                .activation(Activation.SIGMOID)
                .build())
            
            .build();
    }
    
    /**
     * Cria dataset XOR.
     *
     * @return dataset de treinamento
     */
    public static DataSet createXORDataset() {
        // Inputs: todas as combinações de 0 e 1
        INDArray input = Nd4j.create(new double[][] {
            {0, 0},
            {0, 1},
            {1, 0},
            {1, 1}
        });
        
        // Outputs esperados (XOR)
        INDArray output = Nd4j.create(new double[][] {
            {0},
            {1},
            {1},
            {0}
        });
        
        return new DataSet(input, output);
    }
    
    /**
     * Treina a rede neural.
     *
     * @param model rede neural
     * @param dataSet dados de treinamento
     * @param numEpochs número de épocas
     */
    public static void trainNetwork(MultiLayerNetwork model, DataSet dataSet, int numEpochs) {
        // Listener para mostrar progresso
        model.setListeners(new ScoreIterationListener(100));
        
        System.out.println("=== Iniciando Treinamento ===\n");
        
        for (int i = 0; i < numEpochs; i++) {
            model.fit(dataSet);
            
            if (i % 500 == 0) {
                double score = model.score();
                System.out.printf("Época %d: Loss = %.6f%n", i, score);
            }
        }
        
        System.out.println("\n✅ Treinamento concluído!");
    }
    
    /**
     * Avalia predições da rede.
     *
     * @param model rede neural treinada
     * @param dataSet dados de teste
     */
    public static void evaluatePredictions(MultiLayerNetwork model, DataSet dataSet) {
        System.out.println("\n=== Predições ===\n");
        
        INDArray inputs = dataSet.getFeatures();
        INDArray expected = dataSet.getLabels();
        INDArray predicted = model.output(inputs);
        
        System.out.println("Input  | Expected | Predicted | Rounded");
        System.out.println("-------+----------+-----------+--------");
        
        for (int i = 0; i < inputs.rows(); i++) {
            double[] input = inputs.getRow(i).toDoubleVector();
            double exp = expected.getDouble(i, 0);
            double pred = predicted.getDouble(i, 0);
            int rounded = pred > 0.5 ? 1 : 0;
            
            System.out.printf("[%.0f,%.0f] | %.0f        | %.4f    | %d%n",
                input[0], input[1], exp, pred, rounded);
        }
    }
    
    /**
     * Método main para demonstração.
     *
     * @param args argumentos não utilizados
     */
    public static void main(String[] args) {
        System.out.println("=== XOR Neural Network ===\n");
        
        // 1. Criar configuração da rede
        MultiLayerConfiguration config = createNetworkConfiguration();
        MultiLayerNetwork model = new MultiLayerNetwork(config);
        model.init();
        
        System.out.println("Arquitetura da Rede:");
        System.out.println(model.summary());
        
        // 2. Criar dataset
        DataSet trainingData = createXORDataset();
        
        // 3. Treinar
        int numEpochs = 2000;
        trainNetwork(model, trainingData, numEpochs);
        
        // 4. Avaliar
        evaluatePredictions(model, trainingData);
        
        System.out.println("\n✅ XOR Neural Network concluído!");
    }
}
```

**Output esperado**:
```
=== XOR Neural Network ===

Arquitetura da Rede:
=================================================================
LayerName (LayerType)        nIn,nOut    TotalParams  ParamsShape
=================================================================
layer0 (DenseLayer)          2,4         12           W:{2,4}, b:{1,4}
layer1 (OutputLayer)         4,1         5            W:{4,1}, b:{1,1}
-----------------------------------------------------------------
Total Parameters:  17
Trainable Parameters:  17

=== Iniciando Treinamento ===

Época 0: Loss = 0.253421
Época 500: Loss = 0.002156
Época 1000: Loss = 0.000721
Época 1500: Loss = 0.000421

✅ Treinamento concluído!

=== Predições ===

Input  | Expected | Predicted | Rounded
-------+----------+-----------+--------
[0,0] | 0        | 0.0023    | 0
[0,1] | 1        | 0.9987    | 1
[1,0] | 1        | 0.9989    | 1
[1,1] | 0        | 0.0019    | 0

✅ XOR Neural Network concluído!
```

### Exemplo Avançado: Iris Classification

```java
package com.example.dl4j.feedforward;

import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerStandardize;
import org.nd4j.linalg.learning.config.Nesterovs;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.io.File;

/**
 * Classificação de flores Iris usando MLP.
 * 
 * Dataset: 150 amostras, 4 features, 3 classes
 * - Features: sepal length, sepal width, petal length, petal width
 * - Classes: Setosa, Versicolor, Virginica
 */
public class IrisClassifier {
    
    /**
     * Cria configuração da rede neural.
     *
     * @return configuração da rede
     */
    public static MultiLayerConfiguration createConfiguration() {
        int seed = 123;
        double learningRate = 0.01;
        
        return new NeuralNetConfiguration.Builder()
            .seed(seed)
            .weightInit(WeightInit.XAVIER)
            .updater(new Nesterovs(learningRate, 0.9)) // Momentum
            .list()
            
            // Camada 1: 4 inputs → 10 neurons
            .layer(0, new DenseLayer.Builder()
                .nIn(4)
                .nOut(10)
                .activation(Activation.RELU)
                .build())
            
            // Camada 2: 10 neurons → 10 neurons
            .layer(1, new DenseLayer.Builder()
                .nIn(10)
                .nOut(10)
                .activation(Activation.RELU)
                .build())
            
            // Camada de Saída: 10 neurons → 3 classes
            .layer(2, new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                .nIn(10)
                .nOut(3)
                .activation(Activation.SOFTMAX) // Para classificação multi-classe
                .build())
            
            .build();
    }
    
    /**
     * Carrega dataset Iris.
     *
     * @param filePath caminho do arquivo CSV
     * @param batchSize tamanho do batch
     * @return iterator de dados
     * @throws Exception se erro ao carregar
     */
    public static DataSetIterator loadData(String filePath, int batchSize) throws Exception {
        RecordReader recordReader = new CSVRecordReader(0, ',');
        recordReader.initialize(new FileSplit(new File(filePath)));
        
        int labelIndex = 4;  // Coluna 4 é o label
        int numClasses = 3;  // 3 tipos de flores
        
        return new RecordReaderDataSetIterator(
            recordReader, batchSize, labelIndex, numClasses
        );
    }
    
    /**
     * Método main para demonstração.
     *
     * @param args argumentos não utilizados
     */
    public static void main(String[] args) {
        try {
            System.out.println("=== Iris Classifier ===\n");
            
            // 1. Carregar dados
            String trainFile = "data/iris_train.csv";
            String testFile = "data/iris_test.csv";
            int batchSize = 150;
            
            DataSetIterator trainData = loadData(trainFile, batchSize);
            DataSetIterator testData = loadData(testFile, batchSize);
            
            // 2. Normalizar
            DataNormalization normalizer = new NormalizerStandardize();
            normalizer.fit(trainData);
            trainData.setPreProcessor(normalizer);
            testData.setPreProcessor(normalizer);
            
            System.out.println("✅ Dados carregados e normalizados");
            
            // 3. Criar e inicializar rede
            MultiLayerConfiguration config = createConfiguration();
            MultiLayerNetwork model = new MultiLayerNetwork(config);
            model.init();
            model.setListeners(new ScoreIterationListener(10));
            
            System.out.println("\nArquitetura:");
            System.out.println(model.summary());
            
            // 4. Treinar
            System.out.println("\n=== Treinamento ===");
            int numEpochs = 100;
            
            for (int i = 0; i < numEpochs; i++) {
                model.fit(trainData);
                trainData.reset();
                
                if (i % 10 == 0) {
                    System.out.printf("Época %d concluída%n", i);
                }
            }
            
            System.out.println("✅ Treinamento concluído!");
            
            // 5. Avaliar
            System.out.println("\n=== Avaliação no Test Set ===");
            
            Evaluation eval = model.evaluate(testData);
            System.out.println(eval.stats());
            
            System.out.println("\nMatriz de Confusão:");
            System.out.println(eval.confusionToString());
            
            // Métricas
            System.out.println("\n=== Métricas ===");
            System.out.printf("Accuracy: %.2f%%%n", eval.accuracy() * 100);
            System.out.printf("Precision: %.4f%n", eval.precision());
            System.out.printf("Recall: %.4f%n", eval.recall());
            System.out.printf("F1 Score: %.4f%n", eval.f1());
            
        } catch (Exception e) {
            System.err.println("❌ Erro: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
```

---

## 🖼️ Redes Convolucionais (CNN)

### Conceito

**Redes Convolucionais** (CNN - Convolutional Neural Networks) são especializadas em processar dados com **estrutura de grade**, como imagens. Ao invés de processar cada pixel individualmente, CNNs aprendem **filtros** (kernels) que detectam padrões locais.

### Arquitetura CNN

```
Input Image (28×28×1)
        ↓
Conv Layer (filters=32, kernel=5×5)  →  Feature Maps (24×24×32)
        ↓
MaxPooling (2×2)                     →  (12×12×32)
        ↓
Conv Layer (filters=64, kernel=5×5)  →  Feature Maps (8×8×64)
        ↓
MaxPooling (2×2)                     →  (4×4×64)
        ↓
Flatten                              →  (1024)
        ↓
Dense Layer (128 neurons)            →  (128)
        ↓
Output Layer (10 classes)            →  (10)
```

### Exemplo Completo: MNIST Digit Recognition

```java
package com.example.dl4j.cnn;

import org.deeplearning4j.datasets.iterator.impl.MnistDataSetIterator;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.conf.layers.*;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.io.IOException;

/**
 * CNN para reconhecimento de dígitos manuscritos (MNIST).
 * 
 * MNIST Dataset: 60,000 imagens 28×28 em grayscale (0-9)
 */
public class MNISTConvNet {
    
    private static final int HEIGHT = 28;
    private static final int WIDTH = 28;
    private static final int CHANNELS = 1; // Grayscale
    private static final int NUM_CLASSES = 10; // 0-9
    
    /**
     * Cria arquitetura CNN.
     *
     * @return configuração da rede
     */
    public static MultiLayerConfiguration createCNNConfiguration() {
        int seed = 123;
        double learningRate = 0.001;
        
        return new NeuralNetConfiguration.Builder()
            .seed(seed)
            .weightInit(WeightInit.XAVIER)
            .updater(new Adam(learningRate))
            .list()
            
            // Layer 0: Convolutional Layer
            .layer(0, new ConvolutionLayer.Builder(5, 5)
                .nIn(CHANNELS)
                .stride(1, 1)
                .nOut(32) // 32 filtros
                .activation(Activation.RELU)
                .build())
            
            // Layer 1: Max Pooling (reduz dimensionalidade)
            .layer(1, new SubsamplingLayer.Builder(SubsamplingLayer.PoolingType.MAX)
                .kernelSize(2, 2)
                .stride(2, 2)
                .build())
            
            // Layer 2: Convolutional Layer
            .layer(2, new ConvolutionLayer.Builder(5, 5)
                .stride(1, 1)
                .nOut(64) // 64 filtros
                .activation(Activation.RELU)
                .build())
            
            // Layer 3: Max Pooling
            .layer(3, new SubsamplingLayer.Builder(SubsamplingLayer.PoolingType.MAX)
                .kernelSize(2, 2)
                .stride(2, 2)
                .build())
            
            // Layer 4: Dense Layer (Fully Connected)
            .layer(4, new DenseLayer.Builder()
                .activation(Activation.RELU)
                .nOut(128)
                .build())
            
            // Layer 5: Dropout (regularização - previne overfitting)
            .layer(5, new DropoutLayer.Builder(0.5).build())
            
            // Layer 6: Output Layer
            .layer(6, new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                .nOut(NUM_CLASSES)
                .activation(Activation.SOFTMAX)
                .build())
            
            .setInputType(InputType.convolutionalFlat(HEIGHT, WIDTH, CHANNELS))
            .build();
    }
    
    /**
     * Treina a CNN no MNIST.
     *
     * @param model rede neural
     * @param trainData dados de treinamento
     * @param numEpochs número de épocas
     */
    public static void trainModel(MultiLayerNetwork model, 
                                   DataSetIterator trainData, 
                                   int numEpochs) {
        System.out.println("=== Iniciando Treinamento ===\n");
        
        for (int i = 0; i < numEpochs; i++) {
            model.fit(trainData);
            trainData.reset();
            
            System.out.printf("Época %d/%d concluída%n", i + 1, numEpochs);
        }
        
        System.out.println("\n✅ Treinamento concluído!");
    }
    
    /**
     * Avalia modelo no test set.
     *
     * @param model rede treinada
     * @param testData dados de teste
     */
    public static void evaluateModel(MultiLayerNetwork model, 
                                      DataSetIterator testData) {
        System.out.println("\n=== Avaliação ===\n");
        
        Evaluation eval = model.evaluate(testData);
        
        System.out.println(eval.stats());
        
        System.out.println("\n=== Métricas por Classe ===");
        for (int i = 0; i < NUM_CLASSES; i++) {
            System.out.printf("Dígito %d: Precision=%.4f, Recall=%.4f, F1=%.4f%n",
                i,
                eval.precision(i),
                eval.recall(i),
                eval.f1(i)
            );
        }
    }
    
    /**
     * Método main para demonstração.
     *
     * @param args argumentos não utilizados
     */
    public static void main(String[] args) {
        try {
            System.out.println("=== MNIST CNN Classifier ===\n");
            
            // 1. Carregar dados MNIST
            int batchSize = 64;
            int rngSeed = 123;
            
            DataSetIterator trainData = new MnistDataSetIterator(batchSize, true, rngSeed);
            DataSetIterator testData = new MnistDataSetIterator(batchSize, false, rngSeed);
            
            System.out.println("✅ MNIST dataset carregado");
            System.out.println("Training samples: " + trainData.totalExamples());
            System.out.println("Test samples: " + testData.totalExamples());
            
            // 2. Criar e inicializar rede
            MultiLayerConfiguration config = createCNNConfiguration();
            MultiLayerNetwork model = new MultiLayerNetwork(config);
            model.init();
            model.setListeners(new ScoreIterationListener(100));
            
            System.out.println("\nArquitetura CNN:");
            System.out.println(model.summary());
            
            // 3. Treinar
            int numEpochs = 5;
            trainModel(model, trainData, numEpochs);
            
            // 4. Avaliar
            evaluateModel(model, testData);
            
            System.out.println("\n✅ MNIST CNN concluído!");
            
        } catch (IOException e) {
            System.err.println("❌ Erro ao carregar MNIST: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
```

**Output esperado**:
```
=== MNIST CNN Classifier ===

✅ MNIST dataset carregado
Training samples: 60000
Test samples: 10000

Arquitetura CNN:
=================================================================
LayerName (LayerType)          nIn,nOut    TotalParams  ParamsShape
=================================================================
layer0 (ConvolutionLayer)      1,32        832          W:{32,1,5,5}, b:{1,32}
layer1 (SubsamplingLayer)      -,-         0            -
layer2 (ConvolutionLayer)      32,64       51264        W:{64,32,5,5}, b:{1,64}
layer3 (SubsamplingLayer)      -,-         0            -
layer4 (DenseLayer)            1024,128    131200       W:{1024,128}, b:{1,128}
layer5 (DropoutLayer)          -,-         0            -
layer6 (OutputLayer)           128,10      1290         W:{128,10}, b:{1,10}
-----------------------------------------------------------------
Total Parameters:  184,586
Trainable Parameters:  184,586

=== Iniciando Treinamento ===

Época 1/5 concluída
Época 2/5 concluída
Época 3/5 concluída
Época 4/5 concluída
Época 5/5 concluída

✅ Treinamento concluído!

=== Avaliação ===

Accuracy: 98.75%
Precision: 0.9876
Recall: 0.9875
F1 Score: 0.9875

=== Métricas por Classe ===
Dígito 0: Precision=0.9912, Recall=0.9923, F1=0.9917
Dígito 1: Precision=0.9934, Recall=0.9947, F1=0.9940
Dígito 2: Precision=0.9865, Recall=0.9856, F1=0.9860
Dígito 3: Precision=0.9832, Recall=0.9843, F1=0.9837
Dígito 4: Precision=0.9878, Recall=0.9867, F1=0.9872
Dígito 5: Precision=0.9845, Recall=0.9834, F1=0.9839
Dígito 6: Precision=0.9889, Recall=0.9901, F1=0.9895
Dígito 7: Precision=0.9867, Recall=0.9876, F1=0.9871
Dígito 8: Precision=0.9823, Recall=0.9812, F1=0.9817
Dígito 9: Precision=0.9821, Recall=0.9834, F1=0.9827

✅ MNIST CNN concluído!
```

### Camadas CNN - Referência

| Camada | Função | Parâmetros Principais |
|--------|--------|----------------------|
| **ConvolutionLayer** | Aplica filtros para detectar features | `kernelSize`, `stride`, `nOut` (# filtros) |
| **SubsamplingLayer** | Reduz dimensionalidade (pooling) | `kernelSize`, `stride`, `poolingType` (MAX/AVG) |
| **DenseLayer** | Fully connected layer | `nIn`, `nOut`, `activation` |
| **DropoutLayer** | Regularização (desativa neurônios) | `dropOut` (probabilidade 0-1) |
| **BatchNormalization** | Normaliza ativações por batch | `decay`, `epsilon` |

---

## 🔁 Redes Recorrentes (RNN/LSTM)

### Conceito

**Redes Recorrentes** (RNN) processam **sequências** de dados, mantendo um **estado interno** (memória) que captura informação de inputs anteriores. São ideais para:

- Séries temporais (previsão de ações, clima)
- Texto (geração, tradução, sentiment analysis)
- Áudio (reconhecimento de fala, música)

**LSTM** (Long Short-Term Memory) é um tipo de RNN que resolve o problema do **vanishing gradient**, permitindo aprender dependências de longo prazo.

### Arquitetura RNN

```
Input Sequence: [x₁, x₂, x₃, x₄, x₅]
                 ↓   ↓   ↓   ↓   ↓
Hidden State:   h₁→ h₂→ h₃→ h₄→ h₅
                 ↓   ↓   ↓   ↓   ↓
Outputs:        y₁  y₂  y₃  y₄  y₅
```

### Exemplo: Time Series Prediction

```java
package com.example.dl4j.rnn;

import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.LSTM;
import org.deeplearning4j.nn.conf.layers.RnnOutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;

/**
 * LSTM para predição de séries temporais.
 * 
 * Exemplo: Predizer próximo valor baseado em sequência anterior
 */
public class TimeSeriesLSTM {
    
    /**
     * Cria configuração LSTM.
     *
     * @param numInput número de features de entrada
     * @param numHidden neurônios na camada LSTM
     * @param numOutput número de valores de saída
     * @return configuração da rede
     */
    public static MultiLayerConfiguration createLSTMConfiguration(
            int numInput, int numHidden, int numOutput) {
        
        int seed = 123;
        double learningRate = 0.005;
        
        return new NeuralNetConfiguration.Builder()
            .seed(seed)
            .weightInit(WeightInit.XAVIER)
            .updater(new Adam(learningRate))
            .list()
            
            // LSTM Layer
            .layer(0, new LSTM.Builder()
                .nIn(numInput)
                .nOut(numHidden)
                .activation(Activation.TANH)
                .build())
            
            // Output Layer (RNN)
            .layer(1, new RnnOutputLayer.Builder(LossFunctions.LossFunction.MSE)
                .nIn(numHidden)
                .nOut(numOutput)
                .activation(Activation.IDENTITY) // Para regressão
                .build())
            
            .build();
    }
    
    /**
     * Gera dados sintéticos de série temporal (seno).
     *
     * @param numSamples número de sequências
     * @param seqLength comprimento de cada sequência
     * @return dataset de treinamento
     */
    public static DataSet generateSineWaveData(int numSamples, int seqLength) {
        // Features: [numSamples, 1, seqLength]
        // Labels: [numSamples, 1, seqLength] (próximo valor)
        
        INDArray features = Nd4j.create(numSamples, 1, seqLength);
        INDArray labels = Nd4j.create(numSamples, 1, seqLength);
        
        for (int i = 0; i < numSamples; i++) {
            double offset = i * 0.1;
            
            for (int j = 0; j < seqLength; j++) {
                double value = Math.sin(offset + j * 0.1);
                features.putScalar(new int[]{i, 0, j}, value);
                
                // Label é o próximo valor
                double nextValue = Math.sin(offset + (j + 1) * 0.1);
                labels.putScalar(new int[]{i, 0, j}, nextValue);
            }
        }
        
        return new DataSet(features, labels);
    }
    
    /**
     * Método main para demonstração.
     *
     * @param args argumentos não utilizados
     */
    public static void main(String[] args) {
        System.out.println("=== Time Series LSTM ===\n");
        
        // 1. Configuração
        int numInput = 1;      // 1 feature (valor da série)
        int numHidden = 10;    // 10 neurônios LSTM
        int numOutput = 1;     // Predizer 1 valor
        int numSamples = 100;  // 100 sequências
        int seqLength = 50;    // Cada sequência tem 50 pontos
        
        // 2. Gerar dados
        DataSet trainingData = generateSineWaveData(numSamples, seqLength);
        System.out.println("✅ Dados gerados (sine wave)");
        System.out.println("Features shape: " + trainingData.getFeatures().shapeInfoToString());
        System.out.println("Labels shape: " + trainingData.getLabels().shapeInfoToString());
        
        // 3. Criar e inicializar rede
        MultiLayerConfiguration config = createLSTMConfiguration(
            numInput, numHidden, numOutput
        );
        MultiLayerNetwork model = new MultiLayerNetwork(config);
        model.init();
        model.setListeners(new ScoreIterationListener(10));
        
        System.out.println("\nArquitetura LSTM:");
        System.out.println(model.summary());
        
        // 4. Treinar
        System.out.println("\n=== Treinamento ===");
        int numEpochs = 50;
        
        for (int i = 0; i < numEpochs; i++) {
            model.fit(trainingData);
            
            if (i % 10 == 0) {
                double score = model.score();
                System.out.printf("Época %d: Loss = %.6f%n", i, score);
            }
        }
        
        System.out.println("\n✅ Treinamento concluído!");
        
        // 5. Testar predição
        System.out.println("\n=== Teste de Predição ===");
        
        INDArray testInput = trainingData.getFeatures().get(
            Nd4j.createFromArray(0), // Primeira amostra
            Nd4j.createFromArray(0), 
            Nd4j.createFromArray(0, 1, 2, 3, 4, 5, 6, 7, 8, 9) // Primeiros 10 pontos
        );
        
        INDArray prediction = model.rnnTimeStep(testInput);
        
        System.out.println("\nInput (primeiros 10 pontos):");
        System.out.println(testInput);
        
        System.out.println("\nPredição (próximos valores):");
        System.out.println(prediction);
        
        System.out.println("\n✅ Time Series LSTM concluído!");
    }
}
```

---

## 🔄 Transfer Learning

### Conceito

**Transfer Learning** reutiliza modelos pré-treinados em grandes datasets (ImageNet, etc.) para resolver novos problemas com menos dados e tempo de treinamento.

### Quando Usar Transfer Learning?

| Situação | Use Transfer Learning? |
|----------|------------------------|
| Poucos dados (<1000 imagens) | ✅ Sim - Essencial |
| Dados similares ao pré-treinamento | ✅ Sim - Altamente eficaz |
| Problema totalmente diferente | ⚠️  Talvez - Pode funcionar |
| Recursos computacionais limitados | ✅ Sim - Treina mais rápido |

### Exemplo: VGG16 para Classificação Customizada

```java
package com.example.dl4j.transfer;

import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.transferlearning.FineTuneConfiguration;
import org.deeplearning4j.nn.transferlearning.TransferLearning;
import org.deeplearning4j.zoo.ZooModel;
import org.deeplearning4j.zoo.model.VGG16;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;

/**
 * Transfer Learning com VGG16 pré-treinado.
 * 
 * VGG16: Modelo treinado em ImageNet (1.2M imagens, 1000 classes)
 * Adaptando para classificação customizada (ex: 5 classes)
 */
public class VGG16TransferLearning {
    
    /**
     * Carrega VGG16 e adapta para classificação customizada.
     *
     * @param numClasses número de classes do novo problema
     * @return modelo adaptado
     * @throws Exception se erro ao carregar
     */
    public static ComputationGraph createTransferModel(int numClasses) throws Exception {
        // 1. Carregar VGG16 pré-treinado
        ZooModel zooModel = VGG16.builder().build();
        ComputationGraph vgg16 = (ComputationGraph) zooModel.initPretrained();
        
        System.out.println("✅ VGG16 pré-treinado carregado");
        System.out.println("Camadas originais: " + vgg16.getLayers().length);
        
        // 2. Configuração de Fine-Tuning
        FineTuneConfiguration fineTuneConf = new FineTuneConfiguration.Builder()
            .updater(new Adam(1e-4)) // Learning rate menor para fine-tuning
            .seed(123)
            .build();
        
        // 3. Transfer Learning: Remover últimas camadas e adicionar novas
        ComputationGraph transferModel = new TransferLearning.GraphBuilder(vgg16)
            .fineTuneConfiguration(fineTuneConf)
            
            // Congelar camadas iniciais (features já aprendidas)
            .setFeatureExtractor("fc2") // Congelar até layer "fc2"
            
            // Remover última camada (1000 classes → não queremos)
            .removeVertexKeepConnections("predictions")
            
            // Adicionar nova camada de saída customizada
            .addLayer("predictions",
                new org.deeplearning4j.nn.conf.layers.OutputLayer.Builder()
                    .nIn(4096)
                    .nOut(numClasses) // Nosso número de classes
                    .activation(Activation.SOFTMAX)
                    .lossFunction(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                    .build(),
                "fc2")
            
            .build();
        
        System.out.println("✅ Modelo adaptado para " + numClasses + " classes");
        System.out.println("Camadas congeladas: " + transferModel.getConfiguration().getVertices().size());
        
        return transferModel;
    }
    
    /**
     * Método main para demonstração.
     *
     * @param args argumentos não utilizados
     */
    public static void main(String[] args) {
        try {
            System.out.println("=== VGG16 Transfer Learning ===\n");
            
            // Adaptar para 5 classes (ex: cachorro, gato, pássaro, cavalo, porco)
            int numClasses = 5;
            
            ComputationGraph model = createTransferModel(numClasses);
            
            System.out.println("\nArquitetura Adaptada:");
            System.out.println(model.summary());
            
            System.out.println("\n=== Estratégia ===");
            System.out.println("1. Camadas convolucionais CONGELADAS (feature extraction)");
            System.out.println("2. Apenas últimas camadas TREINADAS (classificação)");
            System.out.println("3. Learning rate MENOR (fine-tuning)");
            
            System.out.println("\n✅ Transfer Learning configurado!");
            
        } catch (Exception e) {
            System.err.println("❌ Erro: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
```

---

## ⚡ Otimização e Hiperparâmetros

### Otimizadores

| Otimizador | Descrição | Quando Usar |
|------------|-----------|-------------|
| **SGD** | Stochastic Gradient Descent básico | Baseline, problemas simples |
| **Momentum** | SGD com inércia | Acelera convergência |
| **Nesterovs** | Momentum com look-ahead | Melhora Momentum |
| **AdaGrad** | Learning rate adaptativo | Features esparsas |
| **RMSProp** | AdaGrad melhorado | RNNs, problemas não-convexos |
| **Adam** | RMSProp + Momentum | **Escolha padrão** (funciona bem na maioria dos casos) |

### Exemplo Completo: Hyperparameter Tuning com Arbiter

```java
package com.example.dl4j.optimization;

import org.deeplearning4j.arbiter.ComputationGraphSpace;
import org.deeplearning4j.arbiter.conf.updater.AdamSpace;
import org.deeplearning4j.arbiter.layers.DenseLayerSpace;
import org.deeplearning4j.arbiter.layers.OutputLayerSpace;
import org.deeplearning4j.arbiter.optimize.api.CandidateGenerator;
import org.deeplearning4j.arbiter.optimize.api.data.DataProvider;
import org.deeplearning4j.arbiter.optimize.api.data.DataSource;
import org.deeplearning4j.arbiter.optimize.api.score.ScoreFunction;
import org.deeplearning4j.arbiter.optimize.api.termination.MaxCandidatesCondition;
import org.deeplearning4j.arbiter.optimize.api.termination.MaxTimeCondition;
import org.deeplearning4j.arbiter.optimize.config.OptimizationConfiguration;
import org.deeplearning4j.arbiter.optimize.generator.RandomSearchGenerator;
import org.deeplearning4j.arbiter.optimize.parameter.continuous.ContinuousParameterSpace;
import org.deeplearning4j.arbiter.optimize.parameter.integer.IntegerParameterSpace;
import org.deeplearning4j.arbiter.optimize.runner.IOptimizationRunner;
import org.deeplearning4j.arbiter.optimize.runner.LocalOptimizationRunner;
import org.deeplearning4j.arbiter.saver.local.FileModelSaver;
import org.deeplearning4j.arbiter.scoring.impl.EvaluationScoreFunction;
import org.deeplearning4j.arbiter.task.ComputationGraphTaskCreator;
import org.deeplearning4j.earlystopping.EarlyStoppingConfiguration;
import org.deeplearning4j.earlystopping.saver.InMemoryModelSaver;
import org.deeplearning4j.earlystopping.scorecalc.DataSetLossCalculator;
import org.deeplearning4j.earlystopping.termination.MaxEpochsTerminationCondition;
import org.deeplearning4j.earlystopping.termination.MaxTimeIterationTerminationCondition;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * Otimização automática de hiperparâmetros com Arbiter.
 */
public class HyperparameterOptimization {
    
    /**
     * Cria espaço de busca de hiperparâmetros.
     *
     * @return espaço de configuração
     */
    public static ComputationGraphSpace createHyperparameterSpace() {
        // Definir ranges de hiperparâmetros para testar
        
        // Learning rate: [0.0001, 0.01]
        ContinuousParameterSpace learningRateSpace = 
            new ContinuousParameterSpace(0.0001, 0.01);
        
        // Número de neurônios na camada oculta: [50, 200]
        IntegerParameterSpace layerSizeSpace = 
            new IntegerParameterSpace(50, 200);
        
        // Dropout rate: [0.2, 0.7]
        ContinuousParameterSpace dropoutSpace = 
            new ContinuousParameterSpace(0.2, 0.7);
        
        // Criar espaço de configuração
        return new ComputationGraphSpace.Builder()
            .updater(new AdamSpace(learningRateSpace))
            .addInputs("input")
            
            // Camada oculta com tamanho variável
            .addLayer("dense1",
                new DenseLayerSpace.Builder()
                    .nIn(784) // MNIST: 28×28 = 784
                    .nOut(layerSizeSpace)
                    .activation(Activation.RELU)
                    .dropOut(dropoutSpace)
                    .build(),
                "input")
            
            // Camada de saída
            .addLayer("output",
                new OutputLayerSpace.Builder()
                    .nIn(layerSizeSpace)
                    .nOut(10) // 10 dígitos
                    .activation(Activation.SOFTMAX)
                    .lossFunction(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                    .build(),
                "dense1")
            
            .setOutputs("output")
            .setInputTypes(InputType.feedForward(784))
            .build();
    }
    
    /**
     * Exemplo de configuração de otimização.
     *
     * @param hyperparameterSpace espaço de busca
     * @param dataProvider provedor de dados
     * @return configuração de otimização
     */
    public static OptimizationConfiguration createOptimizationConfig(
            ComputationGraphSpace hyperparameterSpace,
            DataProvider dataProvider) {
        
        // Estratégia de busca: Random Search
        CandidateGenerator candidateGenerator = 
            new RandomSearchGenerator(hyperparameterSpace);
        
        // Função de score: Accuracy
        ScoreFunction scoreFunction = new EvaluationScoreFunction(
            Evaluation.Metric.ACCURACY
        );
        
        // Critérios de terminação
        return new OptimizationConfiguration.Builder()
            .candidateGenerator(candidateGenerator)
            .dataProvider(dataProvider)
            .modelSaver(new FileModelSaver("models/"))
            .scoreFunction(scoreFunction)
            .terminationConditions(
                new MaxTimeCondition(2, TimeUnit.HOURS),   // Máximo 2 horas
                new MaxCandidatesCondition(20)              // Testar 20 configurações
            )
            .build();
    }
    
    /**
     * Método main para demonstração.
     *
     * @param args argumentos não utilizados
     */
    public static void main(String[] args) {
        System.out.println("=== Hyperparameter Optimization ===\n");
        
        // Criar espaço de busca
        ComputationGraphSpace hyperparameterSpace = createHyperparameterSpace();
        
        System.out.println("✅ Espaço de hiperparâmetros criado");
        System.out.println("\n=== Parâmetros a otimizar ===");
        System.out.println("- Learning rate: [0.0001, 0.01]");
        System.out.println("- Layer size: [50, 200]");
        System.out.println("- Dropout: [0.2, 0.7]");
        System.out.println("\n=== Estratégia ===");
        System.out.println("- Método: Random Search");
        System.out.println("- Max configurações: 20");
        System.out.println("- Max tempo: 2 horas");
        System.out.println("- Métrica: Accuracy");
        
        System.out.println("\n✅ Configuração de otimização pronta!");
        System.out.println("💡 Execute IOptimizationRunner.execute() para iniciar busca");
    }
}
```

### Early Stopping

```java
package com.example.dl4j.optimization;

import org.deeplearning4j.earlystopping.EarlyStoppingConfiguration;
import org.deeplearning4j.earlystopping.EarlyStoppingResult;
import org.deeplearning4j.earlystopping.saver.InMemoryModelSaver;
import org.deeplearning4j.earlystopping.scorecalc.DataSetLossCalculator;
import org.deeplearning4j.earlystopping.termination.MaxEpochsTerminationCondition;
import org.deeplearning4j.earlystopping.termination.ScoreImprovementEpochTerminationCondition;
import org.deeplearning4j.earlystopping.trainer.EarlyStoppingGraphTrainer;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;

/**
 * Early Stopping para prevenir overfitting.
 */
public class EarlyStoppingExample {
    
    /**
     * Cria configuração de Early Stopping.
     *
     * @param validationData dados de validação
     * @return configuração de early stopping
     */
    public static EarlyStoppingConfiguration<ComputationGraph> createEarlyStoppingConfig(
            DataSetIterator validationData) {
        
        return new EarlyStoppingConfiguration.Builder<ComputationGraph>()
            // Calcular loss no validation set a cada época
            .epochTerminationConditions(
                // Parar se não houver melhoria em 5 épocas
                new ScoreImprovementEpochTerminationCondition(5)
            )
            
            // Critério de parada global
            .iterationTerminationConditions(
                // Máximo 100 épocas
                new MaxEpochsTerminationCondition(100)
            )
            
            // Como calcular score
            .scoreCalculator(new DataSetLossCalculator(validationData, true))
            
            // Salvar melhor modelo
            .modelSaver(new InMemoryModelSaver<>())
            
            // Avaliar a cada 1 época
            .evaluateEveryNEpochs(1)
            
            .build();
    }
    
    /**
     * Treina com Early Stopping.
     *
     * @param model modelo a treinar
     * @param trainData dados de treinamento
     * @param validationData dados de validação
     * @return resultado do treinamento
     */
    public static EarlyStoppingResult<ComputationGraph> trainWithEarlyStopping(
            ComputationGraph model,
            DataSetIterator trainData,
            DataSetIterator validationData) {
        
        EarlyStoppingConfiguration<ComputationGraph> esConfig = 
            createEarlyStoppingConfig(validationData);
        
        EarlyStoppingGraphTrainer trainer = new EarlyStoppingGraphTrainer(
            esConfig,
            model,
            trainData
        );
        
        // Executar treinamento
        EarlyStoppingResult<ComputationGraph> result = trainer.fit();
        
        // Informações sobre o treinamento
        System.out.println("=== Early Stopping Result ===");
        System.out.println("Termination reason: " + result.getTerminationReason());
        System.out.println("Total epochs: " + result.getTotalEpochs());
        System.out.println("Best epoch: " + result.getBestModelEpoch());
        System.out.println("Best score: " + result.getBestModelScore());
        
        return result;
    }
    
    /**
     * Método main para demonstração.
     *
     * @param args argumentos não utilizados
     */
    public static void main(String[] args) {
        System.out.println("=== Early Stopping ===\n");
        
        System.out.println("Configuração:");
        System.out.println("- Para se não houver melhoria em 5 épocas");
        System.out.println("- Máximo 100 épocas");
        System.out.println("- Salva melhor modelo automaticamente");
        System.out.println("- Previne overfitting");
        
        System.out.println("\n✅ Early Stopping configurado!");
    }
}
```

---

## 📈 Avaliação de Modelos

### Métricas de Classificação

```java
package com.example.dl4j.evaluation;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.evaluation.classification.ROC;
import org.nd4j.evaluation.classification.ROCMultiClass;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;

/**
 * Avaliação detalhada de modelos de classificação.
 */
public class ClassificationEvaluation {
    
    /**
     * Avalia modelo com métricas completas.
     *
     * @param model modelo treinado
     * @param testData dados de teste
     */
    public static void evaluateClassification(MultiLayerNetwork model, 
                                               DataSetIterator testData) {
        System.out.println("=== Evaluation Metrics ===\n");
        
        // 1. Evaluation padrão
        Evaluation eval = model.evaluate(testData);
        
        System.out.println(eval.stats());
        
        // 2. Métricas por classe
        System.out.println("\n=== Per-Class Metrics ===");
        int numClasses = eval.getNumRowCounter();
        
        System.out.printf("%-10s %-10s %-10s %-10s %-10s%n",
            "Class", "Precision", "Recall", "F1-Score", "Support");
        System.out.println("-".repeat(60));
        
        for (int i = 0; i < numClasses; i++) {
            System.out.printf("%-10d %-10.4f %-10.4f %-10.4f %-10d%n",
                i,
                eval.precision(i),
                eval.recall(i),
                eval.f1(i),
                (int) eval.truePositives().get(i) + (int) eval.falseNegatives().get(i)
            );
        }
        
        // 3. Matriz de Confusão
        System.out.println("\n=== Confusion Matrix ===");
        System.out.println(eval.confusionToString());
        
        // 4. Métricas globais
        System.out.println("\n=== Global Metrics ===");
        System.out.printf("Accuracy:  %.4f%n", eval.accuracy());
        System.out.printf("Precision: %.4f (macro avg)%n", eval.precision());
        System.out.printf("Recall:    %.4f (macro avg)%n", eval.recall());
        System.out.printf("F1-Score:  %.4f (macro avg)%n", eval.f1());
    }
    
    /**
     * Calcula ROC e AUC.
     *
     * @param model modelo treinado
     * @param testData dados de teste
     * @param numClasses número de classes
     */
    public static void evaluateROC(MultiLayerNetwork model, 
                                    DataSetIterator testData,
                                    int numClasses) {
        System.out.println("\n=== ROC and AUC ===\n");
        
        ROCMultiClass roc = new ROCMultiClass();
        
        testData.reset();
        while (testData.hasNext()) {
            var ds = testData.next();
            INDArray predicted = model.output(ds.getFeatures());
            roc.eval(ds.getLabels(), predicted);
        }
        
        // AUC por classe
        System.out.println("AUC Scores:");
        for (int i = 0; i < numClasses; i++) {
            double auc = roc.calculateAUC(i);
            System.out.printf("Class %d: %.4f%n", i, auc);
        }
        
        // AUC médio
        double avgAUC = roc.calculateAverageAUC();
        System.out.printf("\nAverage AUC: %.4f%n", avgAUC);
    }
    
    /**
     * Método main para demonstração.
     *
     * @param args argumentos não utilizados
     */
    public static void main(String[] args) {
        System.out.println("=== Classification Evaluation ===\n");
        
        System.out.println("Métricas disponíveis:");
        System.out.println("- Accuracy: Proporção de acertos");
        System.out.println("- Precision: TP / (TP + FP)");
        System.out.println("- Recall: TP / (TP + FN)");
        System.out.println("- F1-Score: 2 × (Precision × Recall) / (Precision + Recall)");
        System.out.println("- Confusion Matrix: Predições vs Reais");
        System.out.println("- ROC/AUC: Curva característica e área");
        
        System.out.println("\n✅ Evaluation configurado!");
    }
}
```

### Métricas de Regressão

```java
package com.example.dl4j.evaluation;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.evaluation.regression.RegressionEvaluation;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;

/**
 * Avaliação de modelos de regressão.
 */
public class RegressionEvaluationExample {
    
    /**
     * Avalia modelo de regressão.
     *
     * @param model modelo treinado
     * @param testData dados de teste
     */
    public static void evaluateRegression(MultiLayerNetwork model, 
                                           DataSetIterator testData) {
        System.out.println("=== Regression Evaluation ===\n");
        
        RegressionEvaluation eval = model.evaluateRegression(testData);
        
        System.out.println(eval.stats());
        
        System.out.println("\n=== Metrics Explained ===");
        System.out.println("MSE: Mean Squared Error");
        System.out.println("RMSE: Root Mean Squared Error");
        System.out.println("MAE: Mean Absolute Error");
        System.out.println("R²: Coefficient of Determination");
    }
    
    /**
     * Método main para demonstração.
     *
     * @param args argumentos não utilizados
     */
    public static void main(String[] args) {
        System.out.println("=== Regression Metrics ===\n");
        
        System.out.println("Métricas:");
        System.out.println("- MSE: Média dos erros ao quadrado");
        System.out.println("- RMSE: Raiz quadrada do MSE");
        System.out.println("- MAE: Média dos erros absolutos");
        System.out.println("- R²: Quão bem o modelo explica a variância");
        
        System.out.println("\n✅ Regression evaluation pronto!");
    }
}
```

---

## 🚀 GPU e Performance

### Configuração GPU (CUDA)

```xml
<!-- pom.xml - Trocar backend para GPU -->
<dependency>
    <groupId>org.nd4j</groupId>
    <artifactId>nd4j-cuda-11.6-platform</artifactId>
    <version>1.0.0-M2.1</version>
</dependency>
```

### Otimizações de Performance

```java
package com.example.dl4j.performance;

import org.nd4j.linalg.api.memory.MemoryWorkspace;
import org.nd4j.linalg.api.memory.conf.WorkspaceConfiguration;
import org.nd4j.linalg.api.memory.enums.AllocationPolicy;
import org.nd4j.linalg.api.memory.enums.LearningPolicy;
import org.nd4j.linalg.factory.Nd4j;

/**
 * Otimizações de performance com ND4J.
 */
public class PerformanceOptimizations {
    
    /**
     * Configura workspace para gerenciamento eficiente de memória.
     *
     * @return configuração de workspace
     */
    public static WorkspaceConfiguration createWorkspaceConfig() {
        return WorkspaceConfiguration.builder()
            .initialSize(10 * 1024 * 1024)  // 10MB inicial
            .policyAllocation(AllocationPolicy.STRICT)
            .policyLearning(LearningPolicy.FIRST_LOOP)
            .build();
    }
    
    /**
     * Usa workspace para operações.
     */
    public static void useWorkspace() {
        WorkspaceConfiguration config = createWorkspaceConfig();
        
        try (MemoryWorkspace ws = Nd4j.getWorkspaceManager().getAndActivateWorkspace(config, "demo")) {
            // Operações dentro do workspace são mais eficientes
            var array = Nd4j.create(1000, 1000);
            var result = array.mmul(array.transpose());
            
            System.out.println("✅ Operação com workspace concluída");
        }
    }
    
    /**
     * Método main para demonstração.
     *
     * @param args argumentos não utilizados
     */
    public static void main(String[] args) {
        System.out.println("=== Performance Optimizations ===\n");
        
        System.out.println("Dicas de Performance:");
        System.out.println("1. Use GPU (CUDA) para redes grandes");
        System.out.println("2. Configure workspaces para menos GC");
        System.out.println("3. Use batch size adequado (32-256)");
        System.out.println("4. Habilite cuDNN para CNNs");
        System.out.println("5. Use DataSetIterator ao invés de carregar tudo na memória");
        
        useWorkspace();
    }
}
```

---

## 📦 Produção e Deploy

### Salvando e Carregando Modelos

```java
package com.example.dl4j.deployment;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;

import java.io.File;
import java.io.IOException;

/**
 * Salvar e carregar modelos treinados.
 */
public class ModelPersistence {
    
    /**
     * Salva modelo treinado.
     *
     * @param model modelo a salvar
     * @param filepath caminho do arquivo
     * @throws IOException se erro ao salvar
     */
    public static void saveModel(MultiLayerNetwork model, String filepath) throws IOException {
        File file = new File(filepath);
        
        // Salva modelo completo (configuração + pesos)
        ModelSerializer.writeModel(model, file, true);
        
        System.out.println("✅ Modelo salvo em: " + filepath);
    }
    
    /**
     * Carrega modelo salvo.
     *
     * @param filepath caminho do arquivo
     * @return modelo carregado
     * @throws IOException se erro ao carregar
     */
    public static MultiLayerNetwork loadModel(String filepath) throws IOException {
        File file = new File(filepath);
        
        MultiLayerNetwork model = ModelSerializer.restoreMultiLayerNetwork(file);
        
        System.out.println("✅ Modelo carregado de: " + filepath);
        
        return model;
    }
    
    /**
     * Método main para demonstração.
     *
     * @param args argumentos não utilizados
     */
    public static void main(String[] args) {
        System.out.println("=== Model Persistence ===\n");
        
        System.out.println("Uso:");
        System.out.println("- ModelSerializer.writeModel() para salvar");
        System.out.println("- ModelSerializer.restoreMultiLayerNetwork() para carregar");
        System.out.println("- Formato: arquivo .zip com config e pesos");
        
        System.out.println("\n✅ Model persistence configurado!");
    }
}
```

### Serving com Spring Boot

```java
package com.example.dl4j.deployment;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.springframework.stereotype.Service;

/**
 * Serviço de predição para API REST.
 */
@Service
public class PredictionService {
    
    private final MultiLayerNetwork model;
    
    /**
     * Construtor carrega modelo.
     *
     * @throws Exception se erro ao carregar
     */
    public PredictionService() throws Exception {
        this.model = ModelPersistence.loadModel("models/trained_model.zip");
        System.out.println("✅ Modelo carregado no serviço");
    }
    
    /**
     * Realiza predição.
     *
     * @param inputData dados de entrada
     * @return predição
     */
    public double[] predict(double[] inputData) {
        // Converter para INDArray
        INDArray input = Nd4j.create(inputData).reshape(1, inputData.length);
        
        // Predição
        INDArray output = model.output(input);
        
        // Converter para array Java
        return output.toDoubleVector();
    }
    
    /**
     * Predição com classe (classificação).
     *
     * @param inputData dados de entrada
     * @return índice da classe predita
     */
    public int predictClass(double[] inputData) {
        double[] probabilities = predict(inputData);
        
        // Encontrar índice com maior probabilidade
        int predictedClass = 0;
        double maxProb = probabilities[0];
        
        for (int i = 1; i < probabilities.length; i++) {
            if (probabilities[i] > maxProb) {
                maxProb = probabilities[i];
                predictedClass = i;
            }
        }
        
        return predictedClass;
    }
}
```

---

## 🧪 Testes

### Suite de Testes Completa

```java
package com.example.dl4j;

import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.junit.jupiter.api.*;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes para modelos DeepLearning4J.
 */
class DeepLearning4JTest {
    
    private MultiLayerNetwork model;
    private DataSet xorDataset;
    
    @BeforeEach
    void setUp() {
        // Criar modelo XOR
        MultiLayerConfiguration config = new NeuralNetConfiguration.Builder()
            .seed(123)
            .weightInit(WeightInit.XAVIER)
            .updater(new Adam(0.1))
            .list()
            .layer(0, new DenseLayer.Builder()
                .nIn(2)
                .nOut(4)
                .activation(Activation.RELU)
                .build())
            .layer(1, new OutputLayer.Builder(LossFunctions.LossFunction.MSE)
                .nIn(4)
                .nOut(1)
                .activation(Activation.SIGMOID)
                .build())
            .build();
        
        model = new MultiLayerNetwork(config);
        model.init();
        
        // Criar dataset XOR
        INDArray input = Nd4j.create(new double[][]{
            {0, 0}, {0, 1}, {1, 0}, {1, 1}
        });
        INDArray output = Nd4j.create(new double[][]{
            {0}, {1}, {1}, {0}
        });
        
        xorDataset = new DataSet(input, output);
    }
    
    @Test
    @DisplayName("Modelo deve ser criado corretamente")
    void testModelCreation() {
        assertNotNull(model);
        assertEquals(2, model.getnLayers());
    }
    
    @Test
    @DisplayName("Modelo deve aceitar input correto")
    void testModelInput() {
        INDArray input = Nd4j.create(new double[][]{{0, 1}});
        INDArray output = model.output(input);
        
        assertNotNull(output);
        assertEquals(1, output.columns());
    }
    
    @Test
    @DisplayName("Treinamento deve reduzir loss")
    void testTraining() {
        double initialScore = model.score();
        
        // Treinar por 500 épocas
        for (int i = 0; i < 500; i++) {
            model.fit(xorDataset);
        }
        
        double finalScore = model.score();
        
        assertTrue(finalScore < initialScore, 
            "Loss deve diminuir após treinamento");
    }
    
    @Test
    @DisplayName("Predições devem ser corretas após treinamento")
    void testPredictions() {
        // Treinar
        for (int i = 0; i < 2000; i++) {
            model.fit(xorDataset);
        }
        
        // Testar XOR
        assertXORPrediction(0, 0, 0);
        assertXORPrediction(0, 1, 1);
        assertXORPrediction(1, 0, 1);
        assertXORPrediction(1, 1, 0);
    }
    
    @Test
    @DisplayName("Modelo deve ser serializável")
    void testSerialization() throws Exception {
        String filepath = "test_model.zip";
        
        // Salvar
        ModelPersistence.saveModel(model, filepath);
        
        // Carregar
        MultiLayerNetwork loadedModel = ModelPersistence.loadModel(filepath);
        
        assertNotNull(loadedModel);
        assertEquals(model.getnLayers(), loadedModel.getnLayers());
        
        // Cleanup
        new java.io.File(filepath).delete();
    }
    
    /**
     * Helper para testar predição XOR.
     */
    private void assertXORPrediction(double x1, double x2, double expected) {
        INDArray input = Nd4j.create(new double[][]{{x1, x2}});
        INDArray output = model.output(input);
        double prediction = output.getDouble(0);
        int rounded = prediction > 0.5 ? 1 : 0;
        
        assertEquals(expected, rounded, 
            String.format("XOR(%s, %s) deve ser %s", x1, x2, expected));
    }
}
```

---

## ✅ Best Practices

### 1. Use Learning Rate Adequado

```java
// ❌ Learning rate muito alto (não converge)
.updater(new Adam(1.0))

// ❌ Learning rate muito baixo (treina muito devagar)
.updater(new Adam(0.0000001))

// ✅ Learning rate balanceado
.updater(new Adam(0.001)) // Padrão
.updater(new Adam(0.01))  // Para problemas simples
```

### 2. Normalize Dados

```java
// ✅ Sempre normalize features
DataNormalization normalizer = new NormalizerStandardize();
normalizer.fit(trainData);
trainData.setPreProcessor(normalizer);
testData.setPreProcessor(normalizer);
```

### 3. Use Dropout para Prevenir Overfitting

```java
.layer(new DenseLayer.Builder()
    .nOut(128)
    .activation(Activation.RELU)
    .dropOut(0.5) // ✅ Desativa 50% dos neurônios aleatoriamente
    .build())
```

### 4. Monitore Loss Durante Treinamento

```java
// ✅ Use listeners para acompanhar progresso
model.setListeners(new ScoreIterationListener(100));
```

### 5. Use Early Stopping

```java
// ✅ Para automaticamente se não houver melhoria
EarlyStoppingConfiguration<MultiLayerNetwork> esConfig = 
    new EarlyStoppingConfiguration.Builder<>()
        .epochTerminationConditions(
            new ScoreImprovementEpochTerminationCondition(5)
        )
        .build();
```

---

## 📊 Referência Rápida

### Funções de Ativação

| Ativação | Uso | Range |
|----------|-----|-------|
| **ReLU** | Camadas ocultas (padrão) | [0, ∞) |
| **LeakyReLU** | Variação de ReLU | (-∞, ∞) |
| **Sigmoid** | Output binário (0 ou 1) | (0, 1) |
| **Tanh** | Camadas ocultas | (-1, 1) |
| **Softmax** | Output multi-classe | Σ = 1 |
| **Identity** | Regressão | (-∞, ∞) |

### Loss Functions

| Loss | Problema | Descrição |
|------|----------|-----------|
| **MSE** | Regressão | Mean Squared Error |
| **MAE** | Regressão | Mean Absolute Error |
| **NEGATIVELOGLIKELIHOOD** | Classificação multi-classe | Cross-entropy |
| **XENT** | Classificação binária | Binary cross-entropy |

### Otimizadores

| Otimizador | Learning Rate Típico | Uso |
|------------|----------------------|-----|
| **Adam** | 0.001 | Escolha padrão |
| **SGD** | 0.01 | Baseline |
| **RMSProp** | 0.001 | RNNs |
| **Nesterovs** | 0.01 | Com momentum |

---

## 📚 Recursos

### Documentação Oficial
- [DeepLearning4J Home](https://deeplearning4j.konduit.ai/)
- [API JavaDoc](https://deeplearning4j.konduit.ai/api/latest/)
- [Examples Repository](https://github.com/eclipse/deeplearning4j-examples)

### Repositório
- [GitHub DL4J](https://github.com/eclipse/deeplearning4j)
- [GitHub ND4J](https://github.com/eclipse/deeplearning4j/tree/master/nd4j)

### Maven Dependencies

```xml
<properties>
    <dl4j.version>1.0.0-M2.1</dl4j.version>
</properties>

<dependencies>
    <!-- Core -->
    <dependency>
        <groupId>org.deeplearning4j</groupId>
        <artifactId>deeplearning4j-core</artifactId>
        <version>${dl4j.version}</version>
    </dependency>
    
    <!-- Backend CPU -->
    <dependency>
        <groupId>org.nd4j</groupId>
        <artifactId>nd4j-native-platform</artifactId>
        <version>${dl4j.version}</version>
    </dependency>
    
    <!-- Backend GPU (alternativa) -->
    <dependency>
        <groupId>org.nd4j</groupId>
        <artifactId>nd4j-cuda-11.6-platform</artifactId>
        <version>${dl4j.version}</version>
    </dependency>
</dependencies>
```

---

## 🎯 Resumo

### Principais Pontos

1. **ND4J**: Biblioteca de álgebra linear (equivalente NumPy)
2. **DataVec**: ETL para machine learning (CSV, imagens, texto)
3. **Feedforward**: Redes simples para classificação/regressão
4. **CNN**: Especializadas em imagens (filtros convolucionais)
5. **RNN/LSTM**: Sequências e séries temporais (memória)
6. **Transfer Learning**: Reutilizar modelos pré-treinados
7. **Arbiter**: Otimização automática de hiperparâmetros
8. **GPU Support**: CUDA nativo para aceleração
9. **Production Ready**: Integração Spring Boot, serialização
10. **Evaluation**: Métricas completas (Accuracy, F1, ROC, AUC)

### Quando Usar DeepLearning4J

✅ **Use DL4J quando**:
- Aplicação Java/JVM existente
- Ambiente enterprise (Spring, Spark, Hadoop)
- Computação distribuída necessária
- Deploy em produção JVM

❌ **Considere alternativas quando**:
- Prototipagem rápida (Python é mais rápido)
- Comunidade maior necessária (TensorFlow/PyTorch)
- Estado da arte em research (papers usam Python)

---

**Voltar para**: [📁 Libraries](../README.md) | [📁 Repositório Principal](../../README.md)
