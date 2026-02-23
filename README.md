# Smart Clinic Management System

Capstone project para praticar desenvolvimento com **Java + Spring Boot**, integrando **MySQL (relacional)** e **MongoDB (documentos)**, com **Docker Compose** para ambiente local.

> Status: em evolução. O escopo funcional está descrito em `user_stories.md`.

## Stack

- Java 17
- Spring Boot 3 (Web, Validation, Data JPA, Data MongoDB, Thymeleaf)
- MySQL 8 + MongoDB 6 (via Docker Compose)
- OpenAPI/Swagger UI (springdoc)
- JWT (jjwt)

## Estrutura do repositório

- `app/` — aplicação Spring Boot (Maven)
- `docker-compose.yml` — MySQL + MongoDB para desenvolvimento local
- `.env.example` — variáveis de ambiente para rodar localmente
- `schema-design.md` — rascunho de modelagem (MySQL + MongoDB)
- `user_stories.md` — histórias de usuário / backlog

## Perfis de ambiente (Spring Profiles)

Este projeto utiliza perfis do Spring para separar configurações de ambiente:

### `default`
- Não conecta automaticamente em banco de dados
- Contém configurações gerais da aplicação
- Usado quando nenhum profile é informado

### `local`
- Desenvolvimento local com bancos via Docker Compose
- Configurações em: `app/src/main/resources/application-local.properties`

## Rodando localmente (com MySQL + MongoDB via Docker)

### 1) Preparar variáveis de ambiente

Na raiz do repo:

```bash
cp .env.example .env
```

> Ajuste valores se necessário (ex.: `DB_URL`, `DB_USER`, `DB_PASS`, `MONGO_URI`, `JWT_SECRET`).

### 2) Subir bancos (Docker Compose)

Na raiz do repo:

```bash
docker compose up -d
```

> O MySQL inicializa com `schema.sql` e `data.sql` montados a partir de `app/src/main/resources/`.

### 3) Subir a aplicação

```bash
cd app
SPRING_PROFILES_ACTIVE=local ./mvnw spring-boot:run
```

Aplicação: `http://localhost:8080`

## OpenAPI / Swagger

Com a aplicação rodando:

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Testes

```bash
cd app
./mvnw test
```

## Variáveis de ambiente

Arquivo base: `.env.example`

- `DB_URL` — JDBC URL do MySQL
- `DB_USER` — usuário do MySQL
- `DB_PASS` — senha do MySQL
- `MONGO_URI` — URI do MongoDB
- `JWT_SECRET` — segredo para tokens JWT (local/dev)

## Documentação do domínio

- `schema-design.md` — visão geral da modelagem (MySQL e MongoDB)
- `user_stories.md` — backlog e critérios de aceitação

## Licença

Este projeto está sob a licença **Apache 2.0** (ver `LICENSE`).