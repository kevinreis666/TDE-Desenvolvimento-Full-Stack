# TDE - Desenvolvimento Full Stack

Aplicação web desenvolvida com **Spring Boot**, **Thymeleaf** e **Java** para a interface do sistema de gestão de avaliações da UNIFAN. O frontend consome uma API REST externa e permite realizar login, visualizar o dashboard e gerenciar usuários, cursos e disciplinas.

## Funcionalidades

- Autenticação de usuários
- Dashboard do sistema
- Visualização e edição de perfil
- Cadastro, consulta, atualização e exclusão de usuários
- Cadastro, consulta, atualização e exclusão de cursos
- Cadastro, consulta, atualização e exclusão de disciplinas
- Navegação entre páginas com Thymeleaf

## Tecnologias

- Java 25
- Spring Boot 4.1.2
- Spring Web MVC
- Thymeleaf
- Maven
- HTML
- Lombok

## Pré-requisitos

- JDK 25
- Maven 3.9 ou superior
- Uma API REST disponível em `http://localhost:8080/api`

## Como executar

1. Clone o repositório:

```bash
git clone https://github.com/kevinreis666/TDE-Desenvolvimento-Full-Stack.git
cd TDE-Desenvolvimento-Full-Stack
```

2. Execute a aplicação:

```bash
./mvnw spring-boot:run
```

No Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

3. Acesse no navegador:

```text
http://localhost:8081
```

## Configuração da API

A URL da API pode ser alterada em `src/main/resources/application.properties`:

```properties
server.port=8081
api.base-url=http://localhost:8080/api
```

## Páginas disponíveis

| Página | Arquivo | Finalidade |
|---|---|---|
| Login | `login.html` | Autenticação |
| Dashboard | `dashboard.html` | Visão geral do sistema |
| Perfil | `perfil.html` | Dados do usuário autenticado |
| Usuários | `usuarios.html` | Gestão de usuários |
| Cursos | `cursos.html` | Gestão de cursos |
| Disciplinas | `disciplinas.html` | Gestão de disciplinas |

## Estrutura do projeto

```text
src/
├── main/
│   ├── java/com/unifan/gestaoavaliacoesfrontend/
│   │   ├── client/       # Clientes HTTP para a API REST
│   │   ├── controller/   # Controladores Spring MVC
│   │   ├── dto/          # Objetos de transferência de dados
│   │   └── GestaoAvaliacoesFrontendApplication.java
│   └── resources/
│       ├── templates/    # Páginas Thymeleaf
│       └── application.properties
└── test/
```

## Testes

Execute os testes com:

```bash
./mvnw test
```

## Observações

- Este projeto representa o frontend da aplicação.
- A API backend deve estar em execução na porta `8080` para que login e operações de CRUD funcionem corretamente.
- Há um arquivo `frontend_fullstack.rar` no repositório que parece conter uma versão compactada do frontend.

## Licença

Nenhuma licença foi informada para este projeto.
