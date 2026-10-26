Aqui está o teu `README.md` **atualizado e polido**. Adicionei uma secção dedicada à **Resolução de Problemas (Troubleshooting)** para resolver o erro do `HeadlessException`/`UnsatisfiedLinkError`, que é a "dor de cabeça" número 1 de quem corre Swing no Linux, e melhorei a formatação geral.

Copia e substitui todo o conteúdo do teu `README.md` por este:

```markdown
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
```

## ⚙️ Como Executar o Projeto

### Pré-requisitos
*   Java JDK 17+ instalado.
*   MySQL Server instalado e a correr.
*   Maven instalado (ou usar a IDE que já tenha o Maven embutido).
*   Uma IDE (recomendado: IntelliJ IDEA, Eclipse ou VS Code).

### Passo 1: Configurar a Base de Dados
1. Abre o MySQL Workbench (ou o terminal do MySQL).
2. Executa o script `scrip_bd.sql` localizado na raiz do projeto. Este script irá:
   * Criar a base de dados `trabalho_final`.
   * Criar todas as tabelas necessárias.
   * Inserir dados de teste (autores, categorias, livros e utilizadores).

### Passo 2: Configurar as Credenciais
1. Na pasta `src/main/resources/`, cria um ficheiro chamado `database.properties` (se não existir).
2. Adiciona as tuas credenciais locais do MySQL:
   ```properties
   db.url=jdbc:mysql://localhost:3306/trabalho_final?serverTimezone=UTC&useSSL=false
   db.user=teu_utilizador
   db.password=tua_senha
   ```

### Passo 3: Compilar e Executar

**Opção A: Pelo terminal (Recomendado para Linux)**
Na raiz do projeto, executa:
```bash
# Limpar e compilar o projeto
mvn clean compile

# Executar a interface gráfica (forçando o modo gráfico)
export MAVEN_OPTS="-Djava.awt.headless=false"
mvn exec:java -Dexec.mainClass="com.biblioteca.Main"
```

**Opção B: Pela IDE (VS Code)**
1. Abre o ficheiro `TelaLogin.java` ou `Main.java`.
2. Clica no botão **Run** (▶️) por cima do método `main`.
*(Se aparecer um erro relacionado com "HeadlessException", consulta a secção de Resolução de Problemas abaixo).*

---

## 🔑 Credenciais de Teste

O script SQL insere dois utilizadores para facilitar os testes:

| Tipo | Email | Senha |
| :--- | :--- | :--- |
| **Bibliotecário** | `wesley@exemplo.com` | `hercio.123` |
| **Utilizador Comum** | `ana@exemplo.com` | `ana123` |

---

## 🚨 Resolução de Problemas (Troubleshooting)

### Erro: `HeadlessException` ou `UnsatisfiedLinkError` no Linux
Se tentares correr o projeto no VS Code ou no terminal e aparecer um erro a dizer que não encontra o `libawt_xawt.so` ou que a JVM está em modo *headless*, significa que o teu ambiente Java não está a conseguir comunicar com o servidor gráfico do Linux.

**Solução 1 (Terminal):** Define a variável de ambiente antes de correr o Maven:
```bash
export MAVEN_OPTS="-Djava.awt.headless=false"
mvn exec:java -Dexec.mainClass="com.biblioteca.Main"
```

**Solução 2 (VS Code):** O VS Code usa um Java interno (headless). Para o forçar a usar o Java do sistema (que tem suporte gráfico):
1. Abre as definições do VS Code (`Ctrl + Shift + P` > `Preferences: Open Workspace Settings (JSON)`).
2. Adiciona as seguintes linhas:
   ```json
   {
       "java.configuration.runtimes": [
           {
               "name": "JavaSE-21",
               "path": "/usr/lib/jvm/java-21-openjdk-amd64",
               "default": true
           }
       ],
       "java.debug.settings.vmArgs": "-Djava.awt.headless=false"
   }
   ```
   *(Nota: Altera o caminho `/usr/lib/jvm/...` para a versão exata do Java que tens instalada no teu sistema).*

### Erro: `Access denied for user` no MySQL
Se o Java não conseguir ligar-se à base de dados, verifica se:
1. O MySQL está a correr (`sudo systemctl status mysql`).
2. O utilizador no ficheiro `database.properties` tem permissões para a base de dados `trabalho_final`.
   Para corrigir, entra no MySQL como root e corre:
   ```sql
   GRANT ALL PRIVILEGES ON trabalho_final.* TO 'teu_utilizador'@'localhost';
   FLUSH PRIVILEGES;
   ```

---

## 👨‍💻 Autor

*   **Wesley** - *Desenvolvimento e Modelação* - [O teu perfil do GitHub](https://github.com/wesleysemende133)

