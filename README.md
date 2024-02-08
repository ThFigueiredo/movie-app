# Projeto Pior Filme do Golden Raspberry Awards

## Sobre

### Requisitos do Sistema
1. O sistema consiste em carregar os dados de um arquivo .csv e gravar na base de dados.
2. Além disso, é disponibilizada uma API para obter o produtor com o maior intervalo entre dois prêmios consecutivos e o que obteve dois prêmios mais rapidamente.

## Tecnologias

- **Java 17**
- **Spring Boot v3.2.2**
- **Banco de dados H2**
- **Maven**

## Executando a Aplicação

### API de Todos os Filmes:
[http://localhost:8081/api/movie](http://localhost:8081/api/movie)

### API para Obter Intervalo Mínimo e Máximo de Vitórias:
[http://localhost:8081/api/movie/interval](http://localhost:8081/api/movie/interval)

Para executar a aplicação siga estas etapas:

1. Clone o repositório:
   ```bash
   git clone https://github.com/ThFigueiredo/movie-app.git
Acesse o diretório do projeto:

bash
Copy code
cd movie-app
Execute a aplicação:

bash
Copy code
mvn spring-boot:run
Testes de Integração
Para executar os testes de integração, utilize o seguinte comando:

bash
Copy code
mvn test -Pintegration

Em caso de dúvidas ou problemas, entre em contato pelo e-mail: thfigueiredo.developer@gmail.com.

Obrigado por explorar o projeto! 😊

