# 📚 Sistema de Gestão de Biblioteca

Trabalho final da disciplina de Programação em Java. O sistema é uma aplicação desktop (Desktop Application) que permite gerir o acervo de uma biblioteca, os seus utilizadores e os empréstimos de livros, com base numa arquitetura em camadas e ligação a uma base de dados relacional.

## 🚀 Funcionalidades Principais

O sistema divide-se em dois perfis de acesso com permissões distintas:

### 👤 Utilizador Comum
*   **Login e Registo:** Criação de conta e autenticação segura.
*   **Catálogo:** Consulta de todos os livros disponíveis, autores e categorias.
*   **Requisição:** Pedido de empréstimo de livros, especificando a quantidade de exemplares.
*   **Meus Pedidos:** Acompanhamento do estado dos pedidos (Pendente, Aprovado, Recusado) e visualização das datas de empréstimo e devolução.
*   **Gestão de Perfil:** Edição dos dados pessoais (nome, email, telefone e senha).

### 👑 Bibliotecário (Administrador)
*   **Gestão de Livros:** Adicionar, listar e remover livros do acervo.
*   **Gestão de Autores:** Criar e remover autores.
*   **Gestão de Utilizadores:** Cadastrar novos utilizadores e remover contas existentes.
*   **Gestão de Requisições:** Visualizar pedidos pendentes, aprovar (dando baixa no stock) ou recusar.
*   **Gestão de Empréstimos:** Visualizar empréstimos ativos e registar devoluções (repondo o stock do livro).

## 🛠️ Tecnologias Utilizadas

*   **Linguagem:** Java (JDK 17 ou superior)
*   **Interface Gráfica:** Java Swing
*   **Acesso a Dados:** JDBC (Java Database Connectivity)
*   **Base de Dados:** MySQL
*   **Gestão de Dependências:** Apache Maven
*   **Modelação:** UML (StarUML)

## 🏗️ Arquitetura do Projeto

O projeto segue o padrão **MVC (Model-View-Controller)** de forma simplificada, organizado nos seguintes pacotes:

```text
com.biblioteca
├── model/      # Classes de domínio (Pessoa, UsuarioComum, Bibliotecario, Livro, etc.)
├── dao/        # Data Access Objects (Conexão e operações SQL)
├── gui/        # Interfaces Gráficas (Telas Swing)
├── util/       # Utilitários (Classe de Conexão)
├── view/       # Interfaces de consola (legado)
└── Main.java   # Ponto de entrada do sistema

⚙️ Como Executar o Projeto
Pré-requisitos

    Java JDK 17+ instalado.

    MySQL Server instalado e a correr.

    Maven instalado (ou usar a IDE que já tenha o Maven embutido).

    Uma IDE (recomendado: IntelliJ IDEA, Eclipse ou VS Code).

Passo 1: Configurar a Base de Dados

    Abre o MySQL Workbench (ou o terminal do MySQL).

    Executa o script scrip_bd.sql localizado na raiz do projeto. Este script irá:

        Criar a base de dados trabalho_final.

        Criar todas as tabelas necessárias.

        Inserir dados de teste (autores, categorias, livros e utilizadores).

Passo 2: Configurar as Credenciais

    Na pasta src/main/resources/, cria um ficheiro chamado database.properties (se não existir).

    Adiciona as tuas credenciais locais do MySQL:
    properties

    db.url=jdbc:mysql://localhost:3306/trabalho_final?serverTimezone=UTC&useSSL=false
    db.user=teu_utilizador
    db.password=tua_senha

Passo 3: Compilar e Executar

Pelo terminal, na raiz do projeto:
bash

# Compilar o projeto
mvn clean compile

# Executar a interface gráfica
mvn exec:java -Dexec.mainClass="com.biblioteca.gui.TelaLogin" -Djava.awt.headless=false

(Nota: A flag -Djava.awt.headless=false é necessária em ambientes Linux para permitir a abertura de janelas gráficas).
🔑 Credenciais de Teste

O script SQL insere dois utilizadores para facilitar os testes:
Tipo	Email	Senha
Bibliotecário	wesley@exemplo.com	hercio.123
Utilizador Comum	ana@exemplo.com	ana123
👨‍💻 Autor

    Wesley - Desenvolvimento e Modelação - O teu perfil do GitHub

Projeto desenvolvido para fins académicos.
text
