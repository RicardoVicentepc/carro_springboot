
### 🚗 Desenvolvimento de API RESTful para Gerenciamento de Carros 🚀

## Contexto:
Uma empresa de desenvolvimento de software decidiu criar uma solução para gerenciar um inventário de carros de forma eficiente e moderna. Utilizando Spring Boot, a meta é desenvolver uma API RESTful que permita operações básicas de CRUD sobre os carros, além de funcionalidades adicionais como atualização de placa e cálculo do valor médio dos carros por marca.

## Desafio:
Implementar a API seguindo os requisitos funcionais abaixo, com ênfase na aplicação de validações específicas para o cadastro de novos carros, garantindo a unicidade e o formato correto da placa.

## Requisitos Funcionais e Exemplos de URL/URI:

### 🆕 Cadastro de Carros:
- Método HTTP: POST
- URL Completa: http://localhost:8080/carros
- Validações Importantes:
    - A placa do carro deve ser única no sistema. 🚫🔁
    - A placa deve seguir o formato especificado pelo regex "^[A-Z]{3}[0-9][A-Z][0-9]{2}$". ✔️📝
- Descrição: Cadastro de um novo carro, retorne o carro recém cadastrado com ID gerado.

<hr>

### 📋 Listagem de Todos os Carros:
- Método HTTP: GET
- URL Completa: http://localhost:8080/carros
- Descrição: Retorna todos os carros cadastrados.

<hr>

### 🔍 Consulta de Carro por ID:
- Método HTTP: GET
- URL Completa: http://localhost:8080/carros/1
- Descrição: Consulta de um carro específico pelo seu ID.

<hr>

### 🔄 Atualização de Carro:
- Método HTTP: PUT
- URL Completa: http://localhost:8080/carros/1
- Descrição: Atualiza informações de um carro existente, com revalidação da placa, retornando o mesmo caso encontre.

<hr>

### ❌ Exclusão de Carro:
- Método HTTP: DELETE
- URL Completa: http://localhost:8080/carros/2
- Descrição: Remove um carro pelo seu ID, sem necessidade de retornar o objeto removido.

<hr>

### 🆔 Atualização da Placa do Carro:
- Método HTTP: PATCH
- URL Completa: http://localhost:8080/carros/1/emplacamento/DEF4G56
- Descrição: Atualiza a placa de um carro, considerando validações de placa já citadas anteriormente,
  retorna o carro no caso de sucesso.

<hr>

### 💰 Cálculo do Valor Médio dos Carros por Marca:
- Método HTTP: GET
- URL Completa: http://localhost:8080/carros/valor-medio?marca=Toyota
- Descrição: Calcula o valor médio dos carros de uma marca específica.

### Lembrete! 🚨⚠️😱
- Para este exercício, é necessário desenvolver uma lógica que simule o comportamento do autoincremento de um banco de dados relacional, atribuindo identificadores únicos do tipo inteiro sem sinal. Importante ressaltar que um identificador não deve ser reutilizado sob nenhuma circunstância.
  Por exemplo, se um carro com o identificador <b>42</b> for excluído, esse número específico não deve ser atribuído a nenhum outro carro posteriormente;
- Não esqueça de devolver os códigos adequados para cada caso nos endpoints (sucesso, erro e etc);

## Exemplos para teste:

### Fusca 🤜
```json
{
  "marca": "Volkswagen",
  "modelo": "Fusca",
  "cor": "Azul",
  "placa": "ANO4S56",
  "preco": 30000.00,
  "ano": 1972
}
```
### Gurgel 🇧🇷
```json
{
  "marca": "Gurgel",
  "modelo": "X12",
  "cor": "Branco",
  "placa": "GUR7X12",
  "preco": 15000.00,
  "ano": 1980
}
```

### Marea 🧯🔥
```json
{
  "marca": "Fiat",
  "modelo": "Marea",
  "cor": "Vermelho incêndio",
  "placa": "MRE5T98",
  "preco": 20000.00,
  "ano": 1999
}
```

### Escort 🏎️💨
```json
{
  "marca": "Ford",
  "modelo": "Escort XR3",
  "cor": "Azul Cobalto",
  "placa": "FSC0X93",
  "preco": 25000.00,
  "ano": 1989
}
```