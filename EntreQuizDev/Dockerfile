# Usa a imagem base do OpenJDK 17
FROM openjdk:17-jdk-slim AS build

# Definição de argumentos para configuração dinâmica
ARG PROFILE
ARG ADDITIONAL_OPTS

# Define variáveis de ambiente
ENV PROFILE=${PROFILE}
ENV ADDITIONAL_OPTS=${ADDITIONAL_OPTS}

# Define o diretório de trabalho
WORKDIR /opt/entrequizdev

# Copia o JAR gerado pelo Maven/Gradle para dentro do container
COPY target/EntreQuizDev-0.0.1-SNAPSHOT.jar EntreQuizDev.jar

# Expõe as portas necessárias
EXPOSE 5005
EXPOSE 8082

# Comando de execução
CMD ["sh", "-c", "java $ADDITIONAL_OPTS -jar EntreQuizDev.jar --spring.profiles.active=$PROFILE"]
