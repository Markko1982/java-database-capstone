# Etapa 1: Build do projeto
FROM maven:3.9.4-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
COPY app ./app
RUN mvn -f app/pom.xml clean package -DskipTests

# Etapa 2: Imagem final
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=builder /app/app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
