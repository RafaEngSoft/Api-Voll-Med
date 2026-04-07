# 🏥 Voll.Med API

API REST desenvolvida para a clínica médica fictícia **Voll.Med**. O projeto cobre o ciclo completo de desenvolvimento de uma API com Spring Boot, incluindo cadastro, listagem, atualização e exclusão lógica de médicos.

---

## 📋 Índice

- [Sobre o Projeto](#-sobre-o-projeto)
- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Funcionalidades](#-funcionalidades)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Endpoints da API](#-endpoints-da-api)
- [Configuração e Execução](#-configuração-e-execução)
- [Banco de Dados](#-banco-de-dados)
- [Validações](#-validações)

---

## 📌 Sobre o Projeto

A **Voll.Med API** é uma aplicação back-end RESTful que gerencia o cadastro de médicos para uma clínica médica. A API implementa boas práticas de desenvolvimento, como:

- Padrão MVC com Spring Boot
- Bean Validation para validação de dados de entrada
- Paginação e ordenação nos endpoints de listagem
- Exclusão lógica (soft delete) de registros
- Migrações controladas de banco de dados com Flyway
- Tratamento centralizado de erros

---

## 🚀 Tecnologias Utilizadas

| Tecnologia | Versão |
|---|---|
| Java | 17 |
| Spring Boot | 3.5.10 |
| Spring Data JPA | — |
| Spring Validation | — |
| MySQL Connector/J | — |
| Flyway | — |
| Lombok | — |
| Maven | — |

---

## ✅ Funcionalidades

- [x] Cadastro de médicos com validação de dados
- [x] Listagem paginada de médicos ativos
- [x] Atualização parcial de dados do médico (nome, telefone, endereço)
- [x] Exclusão lógica de médicos (inativação)
- [x] Tratamento de erros de duplicidade (e-mail e CRM únicos)
- [x] Migrações automáticas do banco de dados com Flyway

---

## 📁 Estrutura do Projeto

```
src/
└── main/
    ├── java/med/voll/api/
    │   ├── ApiApplication.java          # Classe principal (entry point)
    │   ├── controller/
    │   │   ├── MedicoController.java    # Controller REST dos médicos
    │   │   ├── HelloController.java     # Controller de health check
    │   │   └── TratadorDeErros.java     # Handler global de exceções
    │   ├── medico/
    │   │   ├── Medico.java              # Entidade JPA
    │   │   ├── MedicoRepository.java    # Repositório Spring Data
    │   │   ├── Especialidade.java       # Enum de especialidades
    │   │   ├── DadosCadastroMedico.java # DTO de cadastro
    │   │   ├── DadosAtualizacaoMedico.java # DTO de atualização
    │   │   └── DadosListagemMedicos.java   # DTO de listagem
    │   └── endereco/
    │       ├── Endereco.java            # Objeto embutido (Embeddable)
    │       └── DadosEndereco.java       # DTO de endereço
    └── resources/
        ├── application.properties       # Configurações da aplicação
        └── db/migration/
            ├── V1__create-table-medicos.sql
            ├── V2__table-medicos-add-colum-telefone.sql
            └── V3_create-table-medicos-add-colum-ativo.sql
```

---

## 🔗 Endpoints da API

### `POST /medicos` — Cadastrar médico

Cadastra um novo médico na base de dados.

**Request Body:**
```json
{
  "nome": "Dr. João Silva",
  "telefone": "11999998888",
  "email": "joao.silva@voll.med",
  "crm": "123456",
  "especialidade": "CARDIOLOGIA",
  "endereco": {
    "logradouro": "Rua das Flores",
    "bairro": "Centro",
    "cep": "12345678",
    "numero": "100",
    "complemento": "Sala 1",
    "cidade": "São Paulo",
    "uf": "SP"
  }
}
```

**Especialidades disponíveis:** `CARDIOLOGIA` | `DERMATOLOGIA` | `GINECOLOGIA` | `ORTOPEDIA`

**Response:** `200 OK` com os dados do médico cadastrado.

---

### `GET /medicos` — Listar médicos

Retorna uma página com os médicos ativos, ordenados por nome.

**Query Params (opcionais):**
| Parâmetro | Padrão | Descrição |
|---|---|---|
| `page` | `0` | Número da página |
| `size` | `10` | Tamanho da página |
| `sort` | `nome` | Campo de ordenação |

**Response:** `200 OK` com página de médicos.

---

### `PUT /medicos` — Atualizar médico

Atualiza parcialmente os dados de um médico existente. Apenas `nome`, `telefone` e `endereco` podem ser alterados.

**Request Body:**
```json
{
  "id": 1,
  "nome": "Dr. João Silva Atualizado",
  "telefone": "11988887777",
  "endereco": {
    "logradouro": "Av. Paulista",
    "numero": "1000"
  }
}
```

**Response:** `200 OK` com os dados atualizados.

---

### `DELETE /medicos/{id}` — Excluir médico

Realiza a **exclusão lógica** do médico (inativação). O registro permanece no banco de dados com o campo `ativo = false`.

**Response:** `200 OK`

---

## ⚙️ Configuração e Execução

### Pré-requisitos

- Java 17+
- Maven 3.8+
- MySQL 8+

### Passos para rodar

1. **Clone o repositório:**
   ```bash
   git clone https://github.com/RafaEngSoft/Api-Spring-Boot.git
   cd Api-Spring-Boot
   ```

2. **Configure o banco de dados** no arquivo `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/vollmed_api?createDatabaseIfNotExist=true
   spring.datasource.username=seu_usuario
   spring.datasource.password=sua_senha
   ```

3. **Execute a aplicação:**
   ```bash
   ./mvnw spring-boot:run
   ```
   Ou no Windows:
   ```bash
   mvnw.cmd spring-boot:run
   ```

4. A API estará disponível em: `http://localhost:8080`

> O Flyway criará automaticamente o banco `vollmed_api` e aplicará todas as migrations na primeira execução.

---

## 🗄️ Banco de Dados

As migrations são gerenciadas pelo **Flyway** e criam/evoluem a tabela `medicos`:

| Coluna | Tipo | Restrições |
|---|---|---|
| `id` | BIGINT | PK, AUTO_INCREMENT |
| `nome` | VARCHAR(100) | NOT NULL |
| `email` | VARCHAR(100) | NOT NULL, UNIQUE |
| `crm` | VARCHAR(6) | NOT NULL, UNIQUE |
| `especialidade` | VARCHAR(100) | NOT NULL |
| `telefone` | VARCHAR(20) | NOT NULL |
| `ativo` | TINYINT/BOOLEAN | NOT NULL |
| `logradouro` | VARCHAR(100) | NOT NULL |
| `bairro` | VARCHAR(100) | NOT NULL |
| `cep` | VARCHAR(9) | NOT NULL |
| `numero` | VARCHAR(20) | NOT NULL |
| `complemento` | VARCHAR(100) | — |
| `cidade` | VARCHAR(100) | NOT NULL |
| `uf` | CHAR(2) | NOT NULL |

---

## 🛡️ Validações

O projeto utiliza **Bean Validation (Jakarta)** para garantir a integridade dos dados:

| Campo | Regra |
|---|---|
| `nome` | Não pode ser vazio (`@NotBlank`) |
| `email` | Formato de e-mail válido (`@Email`) |
| `telefone` | Exatamente 11 dígitos numéricos (`@Pattern`) |
| `crm` | Entre 4 e 6 dígitos numéricos (`@Pattern`) |
| `especialidade` | Não nulo, deve ser um valor do enum (`@NotNull`) |
| `endereco` | Não nulo e válido (`@NotNull @Valid`) |

Erros de duplicidade (e-mail ou CRM já cadastrados) retornam `409 Conflict` com mensagem amigável, tratados pelo `TratadorDeErros`.


