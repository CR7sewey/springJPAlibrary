## Diagrams

### Branches

- main: JPA concepts
- authorAPI: API devolopement from main
- bookAPI: API development from authorAPI

# 📚 Registo de Livros

## 📖 Descrição
Deseja-se registar os livros, bem como realizar suas atualizações, consultas e permitir sua exclusão. Ao consultar um livro, deverá ser disponibilizado alguns filtros de pesquisa para busca paginada, campos de busca: titulo, genero, isbn, nome do autor e ano de publicação.

---

## 👥 Atores
- **Gerente e Operador**: pode **cadastrar, atualizar e remover** autores.
  
---

## 🗂️ Campos do Livro
### Campos obrigatórios:
- **ISBN**
- **Titulo**
- **Data de Publicacao**
- **Genero** (não obrigatorio)
- **Preco** (nao obrigatorio)
- **Autor**

### Campos de controle (aplicação/auditoria):
- **ID** (UUID)
- **Data de Registo**
- **Data da Última Atualização**
- **Usuário da Última Atualização**

---

## ⚖️ Regras de Negócio
- Não permitir cadastrar um Livro com mesmo ISBN que outro.
- Se a data de publicação for a partir de 2020, deverá ter o preço informado obrigatoriamente.
- Data de publicação não pode ser uma data futura.

---

## 🔗 Contrato da API

### ➕ Registo Livro
- **POST** `/books`  
  **Body**:
```json
{
  "isbn": "string",
  "titulo": "string",
  "dataPublicacao": "date",
  "genero": "enum",
  "preco": number,
  "id_autor": "uuid"
}
```
- Resposta
1. Sucesso

Código: 201 - Created
Header: Location - URI do recurso criado

2. Erro de Validação

Código: 422 - Unprocessable Entity
Body:
```json
{
"status": 422,
"message": "Erro de Validação",
"errors": [
      { "field": "titulo", "error": "Campo obrigatório" }
    ]
}
```
3. ISBN Duplicado

Código: 409 - Conflict
Body:
```json
{
"status": 409,
"message": "ISBN Duplicado",
"errors": []
}
```


### ➕ Visualizar Detalhes do Autor
- **GET** `/books/{id}`

- Resposta
1. Sucesso

Código: 200 - Ok
Body:
```json
{
  "id": "uuid",
  "isbn": "string",
  "titulo": "string",
  "dataPublicacao": "date",
  "genero": "enum",
  "preco": number,
  "autor": {
    "nome": "string",
    "dataNascimento": "date",
    "nacionalidade": "string;
  }
}
```
2. Erro de Validação

Código: 404 - Erro

### ➕ Excluir Livro
- **Delete** `/books/{id}`

- Resposta
1. Sucesso

Código: 204 - No Content

2. Erro

Código: 404 - Erro

### ➕ Pesquisar Autor
- **GET** `/books`  
- Query Params: isbn, titulo, nome autor, genero, ano de publicação
  **Body**:

- Resposta
1. Sucesso

Código: 200 - Ok
Body:
```json
[{
  "id": "uuid",
  "isbn": "string",
  "titulo": "string",
  "dataPublicacao": "date",
  "genero": "enum",
  "preco": number,
  "autor": {
    "nome": "string",
    "dataNascimento": "date",
    "nacionalidade": "string;
  }
}]
```

### ➕ Update Book
- **Put** `/books/{id}`  
  **Body**:
```json
{
  "isbn": "string",
  "titulo": "string",
  "dataPublicacao": "date",
  "genero": "enum",
  "preco": number,
  "id_autor": "uuid"
}
```
- Resposta
1. Sucesso

Código: 204 - No Content

2. Erro de Validação

Código: 422 - Unprocessable Entity
Body:
```json
{
"status": 422,
"message": "Erro de Validação",
"errors: [
      { "field": titulo, "error": "Campo obrigatório" }
    ]
}
```
3. ISBN Duplicado

Código: 409 - Conflict
Body:
```json
{
"status": 409,
"message": "ISBN Duplicado",
"errors: []
}
```



<img size="170" alt="Screenshot 2025-09-14 145753" src="https://github.com/user-attachments/assets/a97bb5d4-94dc-43d2-b8f6-0aea888e32fb" />

<img size="170" alt="Screenshot 2025-09-14 145807" src="https://github.com/user-attachments/assets/c85af0e0-8f21-4333-b368-09165d5704d8" />
