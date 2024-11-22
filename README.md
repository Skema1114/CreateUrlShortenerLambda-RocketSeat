# Url Shortener AWS - RocketSeat

## Descrição

**Url Shortener AWS** é um projeto desenvolvido em **Java**, que utiliza tecnologias da **AWS** para implementar um encurtador de URLs funcional. Este sistema permite criar e gerenciar URLs encurtadas, armazenando-as em um bucket **S3**, e redirecionar para a URL original através de um código único gerado pelo sistema.

---

## Funcionalidades

### 1. **Criação de URL Encurtada**
- **POST:** `https://vamcs3awhi.execute-api.eu-central-1.amazonaws.com/create` (url com autenticação)
- **Parâmetros no corpo da requisição:**
  - `originalUrl`: A URL que será encurtada.
  - `expirationTime`: Tempo em segundos para a expiração do encurtamento.
- **Descrição:** Este endpoint aciona uma função **Lambda** que: Gera um código único para a URL, salva as informações no bucket **S3** da AWS e retorna um código (`code`) para acesso à URL encurtada.

### 2. **Redirecionamento pela URL Encurtada**
- **GET:** `https://vamcs3awhi.execute-api.eu-central-1.amazonaws.com/{code}` (url com autenticação)
- **Descrição:** Este endpoint redireciona o usuário para a URL original associada ao código fornecido e se a URL expirar, o redirecionamento não será efetuado.

---

## Tecnologias Utilizadas

### Linguagem
- **Java** (desenvolvimento realizado no **IntelliJ IDEA**).

### AWS Services
- **AWS Lambda**: Execução das funções de backend sem necessidade de provisionamento de servidores.
- **S3 Bucket**: Armazenamento de registros das URLs encurtadas.
- **IAM**: Controle de acesso e permissões para os serviços AWS utilizados.
- **CloudWatch**: Monitoramento e logs das funções Lambda e do sistema.
- **API Gateway**: Exposição dos endpoints para acesso público.

### Ferramentas
- **Insomnia**: Testes das requisições HTTP.

---

## Requisitos para Configuração

1. **Java** (versão 17 ou superior) instalado no sistema.
2. Uma conta ativa na **AWS** com permissões para configurar os seguintes serviços:
   - Lambda
   - S3
   - API Gateway
   - IAM
3. Ferramentas para teste de API, como **Insomnia** ou **Postman**.
4. IntelliJ IDEA ou outro editor de sua preferência para o desenvolvimento em Java.

---

## Como Executar

### 1. Configurar a AWS
- Crie um bucket no S3 para armazenar os registros.
- Configure as permissões no IAM para que a Lambda possa acessar o S3 e outros serviços necessários.
- Configure o API Gateway para expor os endpoints.

### 2. Implementar as Funções Lambda
- Implemente as funções Lambda em Java e configure-as para executar as operações de encurtamento e redirecionamento.
- Faça o deploy das funções Lambda na AWS.

### 3. Testar os Endpoints
- Utilize o Insomnia ou outra ferramenta de sua preferência para testar os endpoints:
  - **Criação de URL encurtada:** Certifique-se de enviar os parâmetros `originalUrl` e `expirationTime`.
  - **Redirecionamento:** Teste acessando o endpoint com o código retornado.

---

## Logs e Monitoramento

- Utilize o **CloudWatch** para:
  - Visualizar logs das funções Lambda.
  - Monitorar possíveis erros ou exceções.
