# 🐍 Python - Machine Learning & Computer Vision

Documentação completa de Python com foco em Machine Learning e Visão Computacional.

---

## 📋 Índice

- [Linguagem Python](#-linguagem-python)
- [Machine Learning](#-machine-learning)
- [Computer Vision](#-computer-vision)
- [Bibliotecas Essenciais](#-bibliotecas-essenciais)
- [Frameworks](#️-frameworks)
- [Guia de Estudo](#-guia-de-estudo)

---

## 🐍 Linguagem Python

📁 **[linguagem/](./linguagem/)**

Fundamentos da linguagem Python.

### Principais Tópicos
- Sintaxe básica e estruturas de dados
- POO (Programação Orientada a Objetos)
- List comprehensions, generators
- Decorators e context managers
- Type hints (Python 3.10+)
- Async/await

```python
# Type Hints (Python 3.10+)
def calcular_media(numeros: list[float]) -> float:
    return sum(numeros) / len(numeros)

# Dataclasses
from dataclasses import dataclass

@dataclass
class Pessoa:
    nome: str
    idade: int
    email: str | None = None

# Pattern Matching (Python 3.10+)
def classificar(valor):
    match valor:
        case int(x) if x > 0:
            return "Positivo"
        case int(x) if x < 0:
            return "Negativo"
        case 0:
            return "Zero"
        case _:
            return "Não é número"
```

---

## 🤖 Machine Learning

📄 **[ML-visao-computacional/ML.md](./ML-visao-computacional/ML.md)**

Conceitos fundamentais de Machine Learning.

### Principais Conceitos

#### 1. **Supervised Learning**
- Classification
- Regression
- Decision Trees
- Random Forest
- Neural Networks

#### 2. **Unsupervised Learning**
- Clustering (K-Means, DBSCAN)
- Dimensionality Reduction (PCA, t-SNE)

#### 3. **Deep Learning**
- Neural Networks
- CNNs (Convolutional Neural Networks)
- RNNs (Recurrent Neural Networks)
- Transformers

---

## 👁️ Computer Vision

Processamento e análise de imagens.

### Técnicas Principais
- Detecção de objetos
- Segmentação de imagens
- Reconhecimento facial
- Tracking de objetos
- OCR (Optical Character Recognition)

---

## 📚 Bibliotecas Essenciais

### NumPy
📁 **[ML-visao-computacional/libs/numpy-mini-doc/](./ML-visao-computacional/libs/numpy-mini-doc/)**

Computação numérica e arrays multidimensionais.

```python
import numpy as np

# Criar arrays
arr = np.array([1, 2, 3, 4, 5])
matriz = np.array([[1, 2], [3, 4]])

# Operações vetorizadas
arr * 2  # [2, 4, 6, 8, 10]
arr + 10  # [11, 12, 13, 14, 15]

# Broadcasting
matriz + arr  # Adiciona arr a cada linha

# Funções matemáticas
np.mean(arr)  # 3.0
np.std(arr)   # 1.414
np.sum(arr)   # 15

# Indexing avançado
arr[arr > 3]  # [4, 5]
```

---

### Pandas
📁 **[ML-visao-computacional/libs/pandas-mini-doc/](./ML-visao-computacional/libs/pandas-mini-doc/)**

Manipulação e análise de dados tabulares.

```python
import pandas as pd

# Criar DataFrame
df = pd.DataFrame({
    'nome': ['João', 'Maria', 'Pedro'],
    'idade': [25, 30, 35],
    'cidade': ['SP', 'RJ', 'MG']
})

# Filtrar
df[df['idade'] > 25]

# Agrupar
df.groupby('cidade')['idade'].mean()

# Ler CSV
df = pd.read_csv('dados.csv')

# Estatísticas
df.describe()

# Manipulação
df['idade_dobro'] = df['idade'] * 2
df.drop('cidade', axis=1, inplace=True)
```

---

### Matplotlib
📁 **[ML-visao-computacional/libs/matplotlib-mini-doc/](./ML-visao-computacional/libs/matplotlib-mini-doc/)**

Visualização de dados.

```python
import matplotlib.pyplot as plt

# Gráfico de linha
plt.plot([1, 2, 3, 4], [1, 4, 2, 3])
plt.xlabel('X')
plt.ylabel('Y')
plt.title('Meu Gráfico')
plt.show()

# Histograma
plt.hist(dados, bins=30)
plt.show()

# Scatter plot
plt.scatter(x, y, c=cores, s=tamanhos)
plt.show()
```

---

### Scikit-learn
📁 **[ML-visao-computacional/libs/scikit-learn-mini-doc/](./ML-visao-computacional/libs/scikit-learn-mini-doc/)**

Machine Learning clássico.

```python
from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import accuracy_score

# Dividir dados
X_train, X_test, y_train, y_test = train_test_split(
    X, y, test_size=0.2, random_state=42
)

# Treinar modelo
model = RandomForestClassifier(n_estimators=100)
model.fit(X_train, y_train)

# Avaliar
y_pred = model.predict(X_test)
accuracy = accuracy_score(y_test, y_pred)
print(f'Accuracy: {accuracy:.2f}')

# Feature importance
importances = model.feature_importances_
```

---

### YOLO (You Only Look Once)
📁 **[ML-visao-computacional/libs/yolo-mini-doc/](./ML-visao-computacional/libs/yolo-mini-doc/)**

Detecção de objetos em tempo real.

```python
from ultralytics import YOLO

# Carregar modelo
model = YOLO('yolov8n.pt')

# Detectar objetos em imagem
results = model('imagem.jpg')

# Processar resultados
for r in results:
    boxes = r.boxes
    for box in boxes:
        cls = int(box.cls[0])
        conf = float(box.conf[0])
        print(f'Classe: {cls}, Confiança: {conf:.2f}')

# Detectar em vídeo
results = model('video.mp4', stream=True)
```

---

## 🏗️ Frameworks

### OpenCV
📁 **[ML-visao-computacional/opencv-mini-doc/](./ML-visao-computacional/opencv-mini-doc/)**

Processamento de imagens e vídeo.

```python
import cv2

# Ler imagem
img = cv2.imread('imagem.jpg')

# Converter para escala de cinza
gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)

# Detectar bordas
edges = cv2.Canny(gray, 100, 200)

# Detectar faces
face_cascade = cv2.CascadeClassifier('haarcascade_frontalface_default.xml')
faces = face_cascade.detectMultiScale(gray, 1.1, 4)

# Desenhar retângulos
for (x, y, w, h) in faces:
    cv2.rectangle(img, (x, y), (x+w, y+h), (255, 0, 0), 2)

# Salvar resultado
cv2.imwrite('resultado.jpg', img)

# Captura de vídeo
cap = cv2.VideoCapture(0)  # Webcam
while True:
    ret, frame = cap.read()
    cv2.imshow('Frame', frame)
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break
cap.release()
cv2.destroyAllWindows()
```

---

### PyTorch
📁 **[ML-visao-computacional/pytorch-mini-doc/](./ML-visao-computacional/pytorch-mini-doc/)**

Deep Learning framework.

```python
import torch
import torch.nn as nn
import torch.optim as optim

# Definir modelo
class Net(nn.Module):
    def __init__(self):
        super(Net, self).__init__()
        self.fc1 = nn.Linear(784, 128)
        self.fc2 = nn.Linear(128, 10)
    
    def forward(self, x):
        x = torch.relu(self.fc1(x))
        x = self.fc2(x)
        return x

# Criar modelo
model = Net()

# Loss e optimizer
criterion = nn.CrossEntropyLoss()
optimizer = optim.Adam(model.parameters(), lr=0.001)

# Treinar
for epoch in range(10):
    for inputs, labels in train_loader:
        optimizer.zero_grad()
        outputs = model(inputs)
        loss = criterion(outputs, labels)
        loss.backward()
        optimizer.step()
    print(f'Epoch {epoch+1}, Loss: {loss.item():.4f}')
```

**Notebooks**:
- 📓 **[iris-pytorch.ipynb](./ML-visao-computacional/pytorch-mini-doc/iris-pytorch.ipynb)** - Classificação Iris com PyTorch

---

### TensorFlow
📁 **[ML-visao-computacional/tensorflow-mini-doc/](./ML-visao-computacional/tensorflow-mini-doc/)**

Deep Learning framework do Google.

```python
import tensorflow as tf
from tensorflow import keras

# Criar modelo
model = keras.Sequential([
    keras.layers.Dense(128, activation='relu', input_shape=(784,)),
    keras.layers.Dropout(0.2),
    keras.layers.Dense(10, activation='softmax')
])

# Compilar
model.compile(
    optimizer='adam',
    loss='sparse_categorical_crossentropy',
    metrics=['accuracy']
)

# Treinar
model.fit(X_train, y_train, epochs=10, validation_split=0.2)

# Avaliar
test_loss, test_acc = model.evaluate(X_test, y_test)
print(f'Test accuracy: {test_acc:.4f}')

# Predizer
predictions = model.predict(X_new)
```

---

## 🎯 Guia de Estudo

### Iniciante (4-6 semanas)

**Semana 1-2: Fundamentos Python**
- Sintaxe básica
- Estruturas de dados (list, dict, set)
- Funções e módulos
- POO básica

**Semana 3-4: NumPy e Pandas**
- Arrays NumPy
- DataFrames Pandas
- Manipulação de dados
- Visualização com Matplotlib

**Semana 5-6: ML Básico**
- Scikit-learn
- Regressão linear
- Classificação (KNN, Decision Tree)
- Avaliação de modelos

---

### Intermediário (6-8 semanas)

**Semana 1-2: Computer Vision Básica**
- OpenCV fundamentals
- Processamento de imagens
- Filtros e transformações

**Semana 3-4: Deep Learning Intro**
- PyTorch ou TensorFlow
- Redes neurais básicas
- CNNs para imagens

**Semana 5-6: YOLO e Detecção**
- YOLOv8
- Detecção de objetos
- Tracking

**Semana 7-8: Projeto Prático**
- Sistema de detecção customizado
- Pipeline completo
- Deploy

---

### Avançado (8-12 semanas)

**Tópicos Avançados**:
- Transfer Learning
- GANs (Generative Adversarial Networks)
- Object Segmentation
- Action Recognition
- Real-time Systems
- Model Optimization
- Edge AI (Raspberry Pi, Jetson)

---

## 🛠️ Ferramentas Recomendadas

- **IDE**: VSCode, PyCharm, Jupyter Lab
- **Ambiente**: Anaconda, venv, Poetry
- **GPU**: CUDA, cuDNN (NVIDIA)
- **Cloud**: Google Colab, Kaggle Notebooks
- **Version Control**: Git, DVC (Data Version Control)

---

## 📦 Instalação

```bash
# Criar ambiente virtual
python -m venv venv
source venv/bin/activate  # Linux/Mac
venv\Scripts\activate     # Windows

# Instalar pacotes essenciais
pip install numpy pandas matplotlib scikit-learn

# Machine Learning / Deep Learning
pip install torch torchvision  # PyTorch
pip install tensorflow         # TensorFlow

# Computer Vision
pip install opencv-python
pip install ultralytics  # YOLO

# Jupyter
pip install jupyter jupyterlab
```

---

## 🔗 Recursos

- [Python Documentation](https://docs.python.org/3/)
- [NumPy Docs](https://numpy.org/doc/)
- [Pandas Docs](https://pandas.pydata.org/docs/)
- [Scikit-learn](https://scikit-learn.org/)
- [PyTorch Tutorials](https://pytorch.org/tutorials/)
- [TensorFlow Guide](https://www.tensorflow.org/guide)
- [OpenCV Docs](https://docs.opencv.org/)
- [YOLOv8 Docs](https://docs.ultralytics.com/)

---

**Voltar para**: [📁 Repositório Principal](../../README.md) | [📁 Docs para Estudo](../README.md)
