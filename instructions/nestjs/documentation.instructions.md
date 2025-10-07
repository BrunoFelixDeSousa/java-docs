---
applyTo: "**/README.md,**/CHANGELOG.md,**/*.md,**/docs/**/*"
description: "Padrões para documentação técnica, ADRs e documentação de APIs"
---

# Instruções de Documentação

## Objetivo
Estabelecer padrões consistentes para documentação técnica, garantindo clareza, acessibilidade e manutenibilidade da documentação do projeto.

## Escopo
- README.md principal e por módulo
- Documentação de APIs (OpenAPI/Swagger)
- Architecture Decision Records (ADRs)
- Changelog e release notes
- Documentação de contribuição
- Guias de setup e troubleshooting

## Pessoal vs Enterprise

### Projetos Pessoais (Foco: Essencial)
- README básico com setup e uso
- Comentários inline em código complexo
- Changelog simples
- Documentação de API auto-gerada

### Projetos Enterprise (Foco: Completa)
- Documentação completa multi-nível
- ADRs obrigatórios para decisões arquiteturais
- Documentação de APIs completa
- Guias de contribuição detalhados
- Documentação de troubleshooting
- Métricas de qualidade da documentação

## Templates de Documentação

### README.md Principal
```markdown
# [Nome do Projeto]

## 📋 Sobre o Projeto
Breve descrição do projeto, seu propósito e valor de negócio.

## 🚀 Tecnologias
- **NestJS** - Framework backend
- **TypeScript** - Linguagem principal
- **PostgreSQL** - Banco de dados
- **Redis** - Cache e sessions
- **Docker** - Containerização

## 📦 Pré-requisitos
- Node.js 20+ 
- Docker & Docker Compose
- PostgreSQL 15+
- Redis 7+

## 🛠️ Instalação e Setup

### Desenvolvimento Local
```bash
# Clone o repositório
git clone https://github.com/username/project-name.git
cd project-name

# Instale as dependências
npm install

# Configure as variáveis de ambiente
cp .env.example .env

# Execute as migrações
npm run migration:run

# Inicie o projeto em desenvolvimento
npm run start:dev
```

### Docker (Recomendado)
```bash
# Suba todos os serviços
docker-compose up -d

# Acesse a aplicação
http://localhost:3000
```

## 📚 Documentação da API
- **Swagger UI**: [http://localhost:3000/api](http://localhost:3000/api)
- **OpenAPI Spec**: [http://localhost:3000/api-json](http://localhost:3000/api-json)

## 🏗️ Arquitetura
```
src/
├── domain/          # Entidades e regras de negócio
├── application/     # Use cases e serviços de aplicação
├── infrastructure/  # Implementações externas
└── presentation/    # Controllers e DTOs
```

## 🧪 Testes
```bash
# Testes unitários
npm run test

# Testes e2e
npm run test:e2e

# Coverage
npm run test:cov
```

## 📈 Métricas e Monitoring
- **Health Check**: `/health`
- **Metrics**: `/metrics`
- **Logs**: Structured logging com correlationId

## 🤝 Contribuindo
Consulte [CONTRIBUTING.md](CONTRIBUTING.md) para detalhes sobre o processo de contribuição.

## 📄 Changelog
Veja [CHANGELOG.md](CHANGELOG.md) para as mudanças recentes.

## 📝 Licença
Este projeto está licenciado sob a licença MIT - veja [LICENSE](LICENSE) para detalhes.
```

### CONTRIBUTING.md Template
```markdown
# Guia de Contribuição

## Código de Conduta
Este projeto segue o [Código de Conduta](CODE_OF_CONDUCT.md). Ao participar, você concorda em seguir estes termos.

## Como Contribuir

### Reportando Bugs
- Use a [busca de issues](../../issues) para verificar se o bug já foi reportado
- Se não encontrar, [crie uma nova issue](../../issues/new) usando o template de bug

### Sugerindo Melhorias
- Verifique se a sugestão já não existe nas [issues](../../issues)
- Crie uma nova issue usando o template de feature request

### Processo de Pull Request
1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## Padrões de Desenvolvimento

### Convenções de Commit
Seguimos [Conventional Commits](https://conventionalcommits.org/):
```
feat: nova funcionalidade
fix: correção de bug
docs: mudanças na documentação
style: formatação, sem mudanças de código
refactor: refatoração sem mudança de funcionalidade
test: adição ou correção de testes
chore: mudanças em build, configs, etc
```

### Padrões de Código
- ESLint sem warnings
- Prettier formatação
- Testes para novas funcionalidades
- Cobertura mínima de 85%

### Branch Naming
- `feature/` - novas funcionalidades
- `bugfix/` - correção de bugs
- `hotfix/` - correções críticas
- `docs/` - mudanças na documentação
```

### CHANGELOG.md Template
```markdown
# Changelog
Todas as mudanças notáveis deste projeto serão documentadas neste arquivo.

O formato é baseado em [Keep a Changelog](https://keepachangelog.com/pt-BR/1.0.0/),
e este projeto segue [Semantic Versioning](https://semver.org/lang/pt-BR/).

## [Não Lançado]
### Adicionado
- Nova funcionalidade X
### Alterado
- Melhoria na performance Y
### Removido
- API deprecated Z

## [1.2.0] - 2024-03-15
### Adicionado
- Sistema de autenticação JWT
- Middleware de rate limiting
- Validação de DTOs com class-validator

### Alterado
- Atualização do NestJS para v10
- Melhoria na documentação da API

### Corrigido
- Bug na paginação de usuários
- Problema de memory leak no cache

## [1.1.0] - 2024-02-28
### Adicionado
- CRUD completo de usuários
- Testes e2e
- Docker compose para desenvolvimento

## [1.0.0] - 2024-02-15
### Adicionado
- Versão inicial do projeto
- Estrutura base do NestJS
- Configuração inicial do banco de dados
```

## Documentação de APIs

### OpenAPI/Swagger Configuration
```typescript
// main.ts
async function bootstrap() {
  const app = await NestFactory.create(AppModule);
  
  const config = new DocumentationBuilder()
    .setTitle('API Name')
    .setDescription('API para gerenciamento de [recurso]')
    .setVersion('1.0')
    .addBearerAuth()
    .addTag('users', 'Operações relacionadas a usuários')
    .addTag('auth', 'Autenticação e autorização')
    .build();
    
  const document = SwaggerModule.createDocument(app, config);
  SwaggerModule.setup('api', app, document, {
    swaggerOptions: {
      persistAuthorization: true,
    },
  });
  
  await app.listen(3000);
}
```

### Controller Documentation
```typescript
@ApiTags('users')
@Controller('users')
@UseGuards(JwtAuthGuard)
@ApiBearerAuth()
export class UsersController {
  @Post()
  @ApiOperation({ 
    summary: 'Criar usuário',
    description: 'Cria um novo usuário no sistema com os dados fornecidos'
  })
  @ApiCreatedResponse({
    description: 'Usuário criado com sucesso',
    type: UserResponseDto,
  })
  @ApiBadRequestResponse({
    description: 'Dados inválidos fornecidos',
    schema: {
      type: 'object',
      properties: {
        statusCode: { type: 'number', example: 400 },
        message: { type: 'array', items: { type: 'string' } },
        error: { type: 'string', example: 'Bad Request' }
      }
    }
  })
  @ApiConflictResponse({
    description: 'Email já está em uso'
  })
  async create(
    @Body() createUserDto: CreateUserDto
  ): Promise<UserResponseDto> {
    return this.usersService.create(createUserDto);
  }
}
```

### DTO Documentation
```typescript
export class CreateUserDto {
  @ApiProperty({
    description: 'Nome completo do usuário',
    example: 'João Silva',
    minLength: 2,
    maxLength: 100
  })
  @IsString()
  @MinLength(2)
  @MaxLength(100)
  name: string;

  @ApiProperty({
    description: 'Email único do usuário',
    example: 'joao.silva@exemplo.com',
    format: 'email'
  })
  @IsEmail()
  email: string;

  @ApiProperty({
    description: 'Senha do usuário (mínimo 8 caracteres)',
    example: 'minhasenha123',
    minLength: 8
  })
  @IsString()
  @MinLength(8)
  password: string;
}
```

## Architecture Decision Records (ADRs)

### Template de ADR
```markdown
# ADR-001: Escolha do Framework de Backend

## Status
Aceito

## Contexto
Precisamos escolher um framework para desenvolvimento da API REST do projeto. 
As opções consideradas foram Express.js puro, Fastify e NestJS.

## Decisão
Escolhemos NestJS como framework principal para desenvolvimento da API.

## Consequências

### Positivas
- Estrutura organizada e opinativa
- Injeção de dependência nativa
- Decorators para facilitar desenvolvimento
- Ecosystem maduro com muitas integrações
- TypeScript first-class support
- Documentação automática com Swagger

### Negativas
- Curva de aprendizado inicial
- Overhead comparado ao Express puro
- Lock-in com ecosystem específico

## Alternativas Consideradas
- **Express.js**: Mais flexível, mas requer mais configuração manual
- **Fastify**: Performance superior, mas ecosystem menor

## Referências
- [NestJS Documentation](https://docs.nestjs.com/)
- [Performance Benchmark](link-para-benchmark)
```

## Documentação por Módulo

### README.md por Módulo
```markdown
# Módulo de Usuários

## Responsabilidades
- Gerenciamento de usuários (CRUD)
- Autenticação e autorização
- Perfis de usuário

## Estrutura
```
users/
├── dto/              # DTOs de entrada e saída
├── entities/         # Entidades do domínio
├── controllers/      # Controllers REST
├── services/         # Serviços de aplicação
├── repositories/     # Repositórios de dados
└── tests/           # Testes específicos do módulo
```

## APIs Principais
- `GET /users` - Listar usuários
- `POST /users` - Criar usuário
- `GET /users/:id` - Buscar usuário por ID
- `PUT /users/:id` - Atualizar usuário
- `DELETE /users/:id` - Deletar usuário

## Eventos Emitidos
- `user.created` - Usuário criado
- `user.updated` - Usuário atualizado
- `user.deleted` - Usuário deletado

## Dependências
- `AuthModule` - Para autenticação
- `DatabaseModule` - Para persistência
```

## Documentação de Troubleshooting

### TROUBLESHOOTING.md
```markdown
# Troubleshooting Guide

## Problemas Comuns

### Erro de Conexão com Banco
**Sintoma**: `Connection refused` ou `timeout`
**Possíveis Causas**:
- Banco não está executando
- Configurações de conexão incorretas
- Firewall bloqueando conexão

**Solução**:
```bash
# Verificar se o banco está executando
docker-compose ps

# Verificar logs do banco
docker-compose logs postgres

# Recriar containers
docker-compose down && docker-compose up -d
```

### Problemas de Performance
**Sintoma**: Resposta lenta da API
**Investigação**:
```bash
# Verificar métricas
curl http://localhost:3000/metrics

# Verificar logs de performance
docker-compose logs api | grep "slow query"
```

### Problemas de Autenticação
**Sintoma**: Token JWT inválido
**Verificações**:
- Verificar se o token não expirou
- Verificar se o JWT_SECRET está configurado
- Verificar formato do header Authorization

## Logs e Debugging
```bash
# Logs em tempo real
docker-compose logs -f api

# Logs com nível de debug
NODE_ENV=development npm run start:debug
```
```

## Métricas de Qualidade da Documentação

### KPIs para Enterprise
- **Cobertura**: 100% das APIs documentadas
- **Atualização**: Docs atualizadas em <24h após mudanças
- **Feedback**: <5% de issues relacionadas a documentação
- **Onboarding Time**: <2h para novo dev fazer primeiro deploy

### Checklist de Documentação

#### ✅ Para cada Feature/Module
- [ ] README específico do módulo
- [ ] APIs documentadas no Swagger
- [ ] Exemplos de uso incluídos
- [ ] Casos de erro documentados
- [ ] ADR criado se foi decisão arquitetural

#### ✅ Para cada Release
- [ ] CHANGELOG atualizado
- [ ] Breaking changes destacados
- [ ] Migration guide se necessário
- [ ] Documentação de deploy atualizada

## Ferramentas Recomendadas

### Geração de Documentação
- **@nestjs/swagger** - Auto-geração de OpenAPI
- **compodoc** - Documentação de código TypeScript
- **doctoc** - Auto-geração de TOCs para markdown

### Validação de Documentação
- **markdownlint** - Linting de arquivos markdown
- **alex** - Linguagem inclusiva
- **textlint** - Grammar e style checking

## Referências
- [Architectural Decision Records](https://adr.github.io/)
- [Keep a Changelog](https://keepachangelog.com/)
- [Conventional Commits](https://conventionalcommits.org/)
- [OpenAPI Specification](https://swagger.io/specification/)
