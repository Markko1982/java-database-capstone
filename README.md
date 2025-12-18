# Smart Clinic Managemenrt System

## Perfis de Ambiente (Spring Profiles)

Este projeto utiliza perfis do Spring para separar configurações de ambiente:

### default
- Não conecta a nenhum banco de dados
- Contém apenas configurações gerais da aplicação
- Usado quando nenhum profile é informado

### local
- Usado para desenvolvimento local
- MySQL e MongoDB via Docker Compose
- Arquivo: application-local.properties

Rodar localmente:
```bash
SPRING_PROFILES_ACTIVE=local ./mvnw spring-boot:run
