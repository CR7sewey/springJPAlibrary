## Diagrams

### Branches

- main: JPA concepts
- authorAPI: API devolopement from main
- 
# 📚 Registo de Autor

## 📖 Descrição
Sistema para gerenciamento de autores de livros, permitindo cadastro, atualização, consulta e exclusão.  
O sistema aplica regras de negócio para garantir integridade dos dados e respeitar os diferentes perfis de usuários.

---

## 👥 Atores
- **Gerente**: pode **cadastrar, atualizar e remover** autores.
- **Operador**: pode **consultar** os dados dos autores.

---

## 🗂️ Campos do Autor
### Campos obrigatórios:
- **Nome**
- **Data de Nascimento**
- **Nacionalidade**

### Campos de controle (aplicação/auditoria):
- **ID** (UUID)
- **Data de Registo**
- **Data da Última Atualização**
- **Usuário da Última Atualização**

---

## ⚖️ Regras de Negócio
- Não permitir **registo de autores duplicados** (Nome + Data de Nascimento + Nacionalidade).
- Não permitir **exclusão de autor vinculado a algum livro**.

---

## 🔗 Contrato da API

### ➕ Registo Autor
- **POST** `/autores`  
  **Body**:
```json
{
  "nome": "string",
  "dataNascimento": "date",
  "nacionalidade": "string"
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
"errors: [
{ "field": "nome", "error": "Nome é obrigatório" }
]
}
```
3. Autor Duplicado

Código: 409 - Conflict
Body:
```json
{
"status": 409,
"message": "Registro Duplicado",
"errors: []
}
```


### ➕ Visualizar Detalhes do Autor
- **GET** `/autores/{id}`

- Resposta
1. Sucesso

Código: 200 - Ok
Body:
```json
{
  "id": "uuid",
  "nome": "string",
  "dataNascimento": "date",
  "nacionalidade": "string;
}
```
2. Erro de Validação

Código: 404 - Erro

### ➕ Excluir Autor
- **Delete** `/autores/{id}`

- Resposta
1. Sucesso

Código: 204 - No Content

2. Erro

Código: 400 - Erro
Body:
```json
{
  "status": 400,
  "message": "Erro na exclusão: registro está sendo utilizado.",
  "errors: []
}
```

### ➕ Pesquisar Autor
- **GET** `/autores`  
- Query Params: nome, nacionalidade, birth date
  **Body**:

- Resposta
1. Sucesso

Código: 200 - Ok
Body:
```json
[{
  "id": "uuid",
  "nome": "string",
  "dataNascimento": "date",
  "nacionalidade": "string;
}]
```

### ➕ Update Autor
- **Put** `/autores/{id}`  
  **Body**:
```json
{
  "nome": "string",
  "dataNascimento": "date",
  "nacionalidade": "string"
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
{ "field": "nome", "error": "Nome é obrigatório" }
]
}
```
3. Autor Duplicado

Código: 409 - Conflict
Body:
```json
{
"status": 409,
"message": "Registro Duplicado",
"errors: []
}
```



<img size="170" alt="Screenshot 2025-09-14 145753" src="https://github.com/user-attachments/assets/a97bb5d4-94dc-43d2-b8f6-0aea888e32fb" />

<img size="170" alt="Screenshot 2025-09-14 145807" src="https://github.com/user-attachments/assets/c85af0e0-8f21-4333-b368-09165d5704d8" />
