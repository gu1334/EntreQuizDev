<<<<<<< HEAD
# 🧠 EntreQuizDev

[![GitHub license](https://img.shields.io/github/license/gu1334/EntreQuizDev)](https://github.com/gu1334/EntreQuizDev/blob/main/LICENSE)  
![GitHub last commit](https://img.shields.io/github/last-commit/gu1334/EntreQuizDev)  
![GitHub issues](https://img.shields.io/github/issues/gu1334/EntreQuizDev)  
![GitHub pull requests](https://img.shields.io/github/issues-pr/gu1334/EntreQuizDev)

**EntreQuizDev** é uma plataforma gamificada de perguntas técnicas voltada para entrevistas, com suporte a partidas multiplayer e sistema de ranking.  
O principal objetivo é proporcionar uma forma interativa para desenvolvedores praticarem conhecimentos técnicos, com perguntas categorizadas por área de atuação (Backend, DevOps, etc.).

---


## 🎯 Objetivos do Projeto

- Aplicar conceitos de **microsserviços** com **Clean Architecture**
- Utilizar **Apache Kafka** para comunicação assíncrona entre serviços
- Implementar **autenticação e autorização** com OAuth2 e JWT
- Realizar **containerização com Docker** e orquestração com Kubernetes
- Provisionar infraestrutura com **Terraform** na **AWS**
- Automatizar processos com **GitLab CI/CD**
- Monitorar aplicações com **Datadog** ou **New Relic**

---

## 🧱 Arquitetura do Projeto

O sistema é baseado em uma arquitetura de microsserviços com responsabilidades bem definidas, seguindo os princípios da Clean Architecture.

```

entrequizdev/
├── gateway/                 # API Gateway com autenticação e roteamento
├── user-service/           # Serviço de usuários com OAuth2
├── quiz-service/           # Gerenciamento de partidas e sessões
├── matchmaking-service/    # Lógica para encontrar adversários
├── score-service/          # Cálculo de pontuação e rankings
├── question-bank-service/  # Banco de perguntas técnicas por área
├── notification-service/   # Envio de notificações (Kafka, e-mail)
├── shared-libs/            # Bibliotecas compartilhadas (DTOs, auth, exceptions)
├── docker/                 # Arquivos Docker e docker-compose
├── infra/                  # Configurações Terraform e Kubernetes
├── .gitlab-ci.yml          # Pipeline de CI/CD
└── README.md

````

---

## 🚀 Tecnologias Utilizadas

- **Java 17 + Spring Boot**
- **Spring Security + OAuth2 + JWT**
- **Apache Kafka**
- **PostgreSQL**
- **Redis**
- **Docker & Docker Compose**
- **Kubernetes**
- **Terraform (AWS)**
- **Swagger / OpenAPI**
- **GitLab CI/CD**
- **Datadog / New Relic** (monitoramento)

---

## 🛠️ Como Rodar Localmente

> ⚠️ Pré-requisitos: [Docker](https://docs.docker.com/get-docker/) e [Docker Compose](https://docs.docker.com/compose/install/) instalados.

```bash
# Clone o repositório
git clone https://github.com/gu1334/EntreQuizDev.git
cd EntreQuizDev

# Acesse a pasta docker e suba os serviços
cd docker
docker-compose up --build
````

* O **API Gateway** estará disponível em: `http://localhost:8080`
* A documentação Swagger de cada serviço pode ser acessada nas portas individuais configuradas.

---

## 🗺️ Roadmap

* ✅ Estrutura inicial dos microsserviços
* ✅ Setup com Docker, Kafka e PostgreSQL
* ✅ Autenticação com OAuth2 no `user-service`
* 🔄 Implementação do fluxo de partidas e matchmaking
* 🔄 Integração com Redis para cache e matchmaking eficiente
* 🔜 Deploy automatizado na AWS com Terraform + Kubernetes
* 🔜 Painel de monitoramento com Datadog ou New Relic

---

## 📚 Aprendizados Esperados

* Projetar sistemas distribuídos escaláveis e resilientes
* Trabalhar com microsserviços, mensageria e arquitetura limpa
* Automatizar deploys e provisionar infraestrutura como código
* Monitorar aplicações em produção e aplicar boas práticas de DevOps

---

## 👨‍💻 Autor

**Gustavo Freire**
Estudante de Análise e Desenvolvimento de Sistemas
Apaixonado por tecnologia, automação e desafios técnicos!

🔗 [GitHub - @gu1334](https://github.com/gu1334)

---

## 📝 Licença

Este projeto está licenciado sob a licença MIT. Consulte o arquivo [LICENSE](https://github.com/gu1334/EntreQuizDev/blob/main/LICENSE) para mais detalhes.


=======
# user service


tudo referente a autenticação de usuario e cadastramento de usuario
>>>>>>> dev
