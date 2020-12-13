# Projeto D-Banco

Esse projeto é destinado a resolver desafio [donus-code-challenge](https://github.com/ztech-company/donus-code-challenge/blob/master/backend.md)

## Tecnologias Utilizadas
- Spring Boot
- Apache Kafka
- Postgresql 
- MongoDB 
- Swagger UI
- Docker

Para atender o desafio foi criado projeto dividido em 3 modulos (d-banco-conta, d-banco-transacao e d-banco-balanco) provendo uma solução em micro serviços. Outro principio utilizado é a separação das base em leitura e escrita aplicando uma base do padrão CQRS. Essa abordagem via propiciar maior performance e estabilidade para aplicação ao utilizar um banco de dados documental para acesso a informações disponíveis apenas para leitura (serviços de consulta/listar conta e extrato). Para realizar o intercambio de informações entre os serviços foi utilizado o Broker Kafka com publicação de mensagens no cadastro de conta e realização de transações (deposito, saque e transferência). Abaixo segue imagem ilustrativa da arquitetura proposta.

![Project architecture](https://github.com/nelsonjunior/d-banco/blob/master/documentacao/Projeto.png)

## Configuração

Para criação e execução dos container com bancos de dados e kafka com docker

```bash
docker-compose up
```


Para iniciar os projetos deve ser executado o comando abaixo via prompt de comando nas suas respectivas pastas **d-banco-conta**, **d-banco-transacao** e **d-banco-balanco**.

```bash
mvn spring-boot:run
```

## Endereço da documentação das APIs (Swagger)
- Projeto **d-banco-conta** 
```url
http://localhost:8081/swagger-ui.html
```
- Projeto **d-banco-transacao**
```url
http://localhost:8082/swagger-ui.html
```
- Projeto **d-banco-balanco**
```url
http://localhost:8083/swagger-ui.html
```

## Funcionalidades modulo d-banco-conta

### Realizar cadastro de conta corrente

Via comando
```curl
curl -X POST "http://localhost:8081/v1/contas" -H "accept: application/json" -H "Content-Type: application/json" -d "{ \"cpf\": \"1234567890\", \"nomeCompleto\": \"Nelson Rodrigues\"}"
```
Request URL via GET
```url
http://localhost:8081/v1/contas
```
Response body
```json
{
  "nomeCompleto": "Nelson Rodrigues",
  "cpf": "1234567890"
}
```

### Consultar conta corrente

Via comando
```curl
curl -X GET "http://localhost:8081/v1/contas/1234567890" -H "accept: application/json"
```
Request URL via GET
```url
http://localhost:8081/v1/contas/1234567890
```

### Listar contas correntes

Via comando
```curl
curl -X GET "http://localhost:8081/v1/contas" -H "accept: application/json"
```
Request URL via GET
```url
http://localhost:8081/v1/contas
```

## Funcionalidades modulo d-banco-transacao

### Realizar deposito em conta corrente

Via comando
```curl
curl -X POST "http://localhost:8082/v1/contas/depositar" -H "accept: application/json" -H "Content-Type: application/json" -d "{ \"cpf\": \"1234567890\", \"valor\": 100.00}"
```
Request URL via GET
```url
http://localhost:8082/v1/contas/depositar
```
Response body
```json
{
  "cpf": "1234567890",
  "valor": 100.00
}
```

### Realizar saque em conta corrente

Via comando
```curl
curl -X POST "http://localhost:8082/v1/contas/sacar" -H "accept: application/json" -H "Content-Type: application/json" -d "{ \"cpf\": \"1234567890\", \"valor\": 15.40}"
```
Request URL via GET
```url
http://localhost:8082/v1/contas/sacar
```
Response body
```json
{
  "cpf": "1234567890",
  "valor": 15.40
}
```

### Realizar transferência entre contas corrente

Via comando
```curl
curl -X POST "http://localhost:8082/v1/contas/transferir" -H "accept: application/json" -H "Content-Type: application/json" -d "{ \"cpfDestino\": \"9876543210\", \"cpfOrigem\": \"1234567890\", \"valor\": 55.00}"
```
Request URL via GET
```url
http://localhost:8082/v1/contas/transferir
```
Response body
```json
{
  "cpfDestino": "9876543210",
  "cpfOrigem": "1234567890",
  "valor": 55.00
}
```


## Funcionalidades modulo d-banco-balanco

### Obter saldo de conta corrente

Via comando
```curl
curl -X GET "http://localhost:8083/v1/contas/1234567890/saldo" -H "accept: application/json"
```
Request URL via GET
```url
http://localhost:8083/v1/contas/1234567890/saldo
```

### Obter extrato de conta corrente

Via comando
```curl
curl -X GET "http://localhost:8083/v1/contas/1234567890/extrato" -H "accept: application/json"
```
Request URL via GET
```url
http://localhost:8083/v1/contas/1234567890/extrato
```

## Observações
1. A fim de facilitar teste de massa de dados não implementei a validação do número do CPF.
2. Utilizei o número do cpf como identificado da conta corrente entre os serviços

## Testes de Performance
Para validar a performance da aplicação e simulação de stress criei alguns cenários de teste que podem ser executados com a ferramenta k6. 

### Instalação e configuração do k6
Instruções para instalação podem ser vista no [link](https://k6.io/docs/getting-started/installation).

### Execução
Os cenários de teste estão presentes na pasta **d-banco-k6**

Simulação de cadastro de contas:
```command
k6 run --vus 50 --duration 5s stress-cadastrar-test.js
```

Simulação de deposito de contas:
```command
k6 run --vus 10 --duration 5s stress-depositar-test.js
```
Simulação de obter saldo de contas:
```command
k6 run --vus 10 --duration 5s stress-obter-saldo-test.js
```