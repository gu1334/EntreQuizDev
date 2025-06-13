
# EntreQuizDev

-----

## Visão Geral do Projeto

O **EntreQuizDev** é uma plataforma robusta de quiz desenvolvida para testar e aprimorar conhecimentos em diversas áreas da programação, como Backend, Frontend e DevOps. Ideal para desenvolvedores que buscam se preparar para entrevistas técnicas, reforçar conceitos ou simplesmente desafiar seus conhecimentos de forma interativa. A plataforma oferece um ambiente dinâmico para a criação, gerenciamento e resolução de perguntas, com feedback instantâneo e registro de desempenho.

-----

## Funcionalidades

  * **Criação e Gerenciamento de Perguntas:** Adicione, edite e remova perguntas com suas respostas corretas e múltiplas respostas incorretas.
  * **Perguntas por Área:** Categorize e filtre perguntas por áreas específicas (Backend, Frontend, DevOps, Estágio, etc.).
  * **Seleção Aleatória de Perguntas:** Obtenha perguntas aleatórias para um fluxo de quiz dinâmico.
  * **Registro de Desempenho:** Armazenamento de todas as tentativas de resposta, indicando se foram corretas ou incorretas, para acompanhamento.
  * **Autenticação e Autorização:** Integração com um serviço de usuários (`user-service`) para controle de acesso e segurança via JWT.

-----

## Tecnologias Utilizadas

O projeto é construído com um stack moderno e robusto:

  * **Backend:**
      * **Java 17**
      * **Spring Boot 3.2.5** (incluindo Spring Web, Spring Data JPA, Spring Security)
      * **Hibernate** (implementação JPA)
      * **Maven** (gerenciador de dependências)
      * **Lombok** (para reduzir boilerplate code)
      * **Spring Cloud OpenFeign** (para comunicação com o `user-service`)
  * **Banco de Dados:**
      * **MySQL**
  * **Ferramentas:**
      * **Postman** (para testes de API)

-----

## Como Rodar o Projeto (Localmente)

Siga estas instruções para configurar e executar o EntreQuizDev em sua máquina local:

### 1\. Pré-requisitos

Certifique-se de ter instalado:

  * **Java Development Kit (JDK) 17** ou superior.
  * **Apache Maven 3.6+**.
  * Uma instância do **MySQL** configurada e rodando.
  * O **`user-service`** (que o `AuthClient` aponta para `http://localhost:8081`) precisa estar rodando para a autenticação funcionar. Caso não possua, a funcionalidade de segurança (`/CadastrarPergunta/**`) estará comprometida.

### 2\. Configuração do Banco de Dados

1.  Crie um banco de dados MySQL para o projeto (ex: `quizdb`).
2.  Atualize o arquivo `src/main/resources/application.properties` com as credenciais do seu banco de dados:
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/quizdb?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
    spring.datasource.username=seu_usuario
    spring.datasource.password=sua_senha
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    ```
    *Altere `seu_usuario` e `sua_senha` pelas suas credenciais.*

### 3\. Compilar e Executar

1.  Clone o repositório para sua máquina local:
    ```bash
    git clone https://github.com/gu1334/EntreQuizDev.git
    ```
2.  Navegue até o diretório raiz do projeto:
    ```bash
    cd EntreQuizDev
    ```
3.  Compile o projeto usando Maven:
    ```bash
    mvn clean install
    ```
4.  Execute a aplicação Spring Boot:
    ```bash
    mvn spring-boot:run
    ```
    A aplicação estará disponível em `http://localhost:8080`.

-----

## Endpoints da API

Aqui estão os principais endpoints disponíveis, ideais para testar com o Postman:

### Endpoints de Perguntas (`/perguntas`)

  * **`POST /perguntas`**

      * **Descrição:** Cria uma nova pergunta ou atualiza uma existente (se o `id` for fornecido).
      * **Body de Exemplo:**
        ```json
        {
            "pergunta": "Qual é o principal uso do Spring Boot?",
            "respostaCorreta": "Simplificar o desenvolvimento de aplicações Spring",
            "respostasIncorretas": ["Desenvolvimento de jogos", "Edição de vídeo", "Criação de websites estáticos"],
            "area": "BACKEND_JR"
        }
        ```

  * **`PATCH /perguntas/{id}`**

      * **Descrição:** Atualiza parcialmente uma pergunta existente pelo ID.
      * **Exemplo:** `PATCH /perguntas/1`
      * **Body de Exemplo (para atualizar apenas a pergunta e a área):**
        ```json
        {
            "pergunta": "Qual framework Java é popular para microsserviços?",
            "area": "BACKEND_PLENO"
        }
        ```

  * **`GET /perguntas`**

      * **Descrição:** Lista todas as perguntas cadastradas.

  * **`DELETE /perguntas/{id}`**

      * **Descrição:** Deleta uma pergunta específica pelo ID.
      * **Exemplo:** `DELETE /perguntas/1`
      * **Status de Resposta:** `204 No Content` em caso de sucesso.

### Endpoints de Respostas (`/resposta`)

  * **`GET /resposta/aleatoria/{area}`**

      * **Descrição:** Busca uma pergunta aleatória de uma área específica para o quiz.
      * **Exemplo:** `GET http://localhost:8080/resposta/aleatoria/PROGRAMACAO`
      * **Retorno de Exemplo:**
        ```json
        {
            "id": 123,
            "pergunta": "Qual a capital da França?",
            "respostas": ["Berlim", "Madrid", "Paris", "Roma"]
        }
        ```

  * **`POST /resposta/{id}/{numeroResposta}`**

      * **Descrição:** Envia a resposta do usuário para uma pergunta específica. O `numeroResposta` deve ser o índice da opção escolhida **começando de 1**.
      * **Exemplo:** Se a pergunta recebida no GET aleatório tinha "Paris" (resposta correta) na 3ª posição da lista de `respostas`, e seu ID é 123, a requisição seria:
        `POST http://localhost:8080/resposta/123/3`
      * **Retorno de Exemplo:** `Resposta Correta! Registro salvo.` ou `Resposta Incorreta. Registro salvo.`

-----

## Contribuição

Contribuições são bem-vindas\! Sinta-se à vontade para abrir issues, enviar pull requests ou sugerir melhorias.

-----

## Licença

Este projeto está licenciado sob a [MIT License](https://opensource.org/licenses/MIT).

-----

## Contato

Para dúvidas ou sugestões, entre em contato:

  * **LinkedIn:** [https://www.linkedin.com/in/gustavo-freire-bb56b6185/]

-----
