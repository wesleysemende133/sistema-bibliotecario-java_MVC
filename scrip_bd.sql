-- ==========================================================
-- SCRIPT DE CRIAÇÃO DA BASE DE DADOS - SISTEMA DE BIBLIOTECA
-- ==========================================================

DROP DATABASE IF EXISTS trabalho_final;
CREATE DATABASE trabalho_final CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE trabalho_final;

-- ==========================================
-- 1. CRIAÇÃO DAS TABELAS
-- ==========================================

CREATE TABLE autor (
    id_autor INT AUTO_INCREMENT PRIMARY KEY,
    nome_autor VARCHAR(100) NOT NULL,
    nacionalidade VARCHAR(50),
    data_nascimento DATE
);

CREATE TABLE categoria (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    nome_categoria VARCHAR(50) NOT NULL UNIQUE,
    descricao VARCHAR(255)
);

CREATE TABLE estante (
    id_estante INT AUTO_INCREMENT PRIMARY KEY,
    codigo_estante VARCHAR(20) NOT NULL UNIQUE,
    localizacao VARCHAR(100) NOT NULL,
    capacidade_maxima INT NOT NULL,
    exemplares_ocupados INT NOT NULL DEFAULT 0,
    id_categoria INT,
    CONSTRAINT fk_estante_categoria FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria) ON DELETE SET NULL
);

-- Tabela de Utilizadores (Usa o padrão de Herança numa única tabela)
CREATE TABLE usuario (
    id_membro INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    tipo ENUM('COMUM', 'BIBLIOTECARIO') NOT NULL DEFAULT 'COMUM',
    senha VARCHAR(255) NOT NULL,
    telefone VARCHAR(20),
    data_registo DATE NOT NULL DEFAULT (CURRENT_DATE),
    -- Campos específicos de UsuarioComum
    num_cartao VARCHAR(20) NULL,
    -- Campos específicos de Bibliotecario
    codigo_funcionario VARCHAR(20) NULL,
    data_admissao DATE NULL
);

CREATE TABLE livro (
    id_livro INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL,
    isbn VARCHAR(20) UNIQUE,
    ano_publicacao INT,
    exemplares_disponiveis INT NOT NULL DEFAULT 1,
    id_autor INT NOT NULL,
    id_categoria INT NOT NULL,
    id_estante INT,
    CONSTRAINT fk_livro_autor FOREIGN KEY (id_autor) REFERENCES autor(id_autor) ON DELETE RESTRICT,
    CONSTRAINT fk_livro_categoria FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria) ON DELETE RESTRICT,
    CONSTRAINT fk_livro_estante FOREIGN KEY (id_estante) REFERENCES estante(id_estante) ON DELETE SET NULL
);

-- Tabela de Requisições (Pedidos feitos pelos utilizadores)
CREATE TABLE requisicao (
    id_requisicao INT AUTO_INCREMENT PRIMARY KEY,
    id_membro INT NOT NULL,
    id_livro INT NOT NULL,
    quantidade INT NOT NULL DEFAULT 1,
    data_pedido DATE NOT NULL DEFAULT (CURRENT_DATE),
    estado ENUM('PENDENTE', 'APROVADO', 'RECUSADO') NOT NULL DEFAULT 'PENDENTE',
    CONSTRAINT fk_req_usuario FOREIGN KEY (id_membro) REFERENCES usuario(id_membro) ON DELETE CASCADE,
    CONSTRAINT fk_req_livro FOREIGN KEY (id_livro) REFERENCES livro(id_livro) ON DELETE CASCADE
);

-- Tabela de Empréstimos (Gerados após aprovação das requisições)
CREATE TABLE emprestimo (
    id_emprestimo INT AUTO_INCREMENT PRIMARY KEY,
    id_membro INT NOT NULL,
    id_livro INT NOT NULL,
    data_emprestimo DATE NOT NULL DEFAULT (CURRENT_DATE),
    data_devolucao_prevista DATE NOT NULL,
    data_devolucao_real DATE,
    estado ENUM('EMPRESTADO', 'DEVOLVIDO', 'ATRASADO') NOT NULL DEFAULT 'EMPRESTADO',
    CONSTRAINT fk_emprestimo_usuario FOREIGN KEY (id_membro) REFERENCES usuario(id_membro) ON DELETE RESTRICT,
    CONSTRAINT fk_emprestimo_livro FOREIGN KEY (id_livro) REFERENCES livro(id_livro) ON DELETE RESTRICT
);

-- ==========================================
-- 2. INSERÇÃO DE DADOS DE TESTE
-- ==========================================

-- Autores
INSERT INTO autor (nome_autor, nacionalidade, data_nascimento) VALUES
('José Saramago', 'Portuguesa', '1922-11-16'),
('Mia Couto', 'Moçambicana', '1955-07-05'),
('Machado de Assis', 'Brasileira', '1839-06-21');

-- Categorias
INSERT INTO categoria (nome_categoria, descricao) VALUES
('Romance', 'Obras de ficção romântica e literária'),
('História', 'Livros de História e ensaios históricos'),
('Tecnologia', 'Programação, redes e informática');

-- Estantes
INSERT INTO estante (codigo_estante, localizacao, capacidade_maxima, exemplares_ocupados, id_categoria) VALUES
('A-01', 'Corredor Norte', 50, 2, 1),
('B-01', 'Corredor Sul', 40, 1, 3);

-- Utilizadores (Admin e Comum)
-- Senha do Admin: hercio.123 | Senha da Ana: ana123
INSERT INTO usuario (nome, email, tipo, senha, telefone, codigo_funcionario, data_admissao) VALUES
('Wesley Admin', 'wesley@exemplo.com', 'BIBLIOTECARIO', 'hercio.123', '840000000', 'BIB-001', '2024-01-15');

INSERT INTO usuario (nome, email, tipo, senha, telefone, num_cartao) VALUES
('Ana Silva', 'ana@exemplo.com', 'COMUM', 'ana123', '840000001', 'CART-001');

-- Livros
INSERT INTO livro (titulo, isbn, ano_publicacao, exemplares_disponiveis, id_autor, id_categoria, id_estante) VALUES
('Ensaio sobre a Cegueira', '978-972-21-1050-1', 1995, 3, 1, 1, 1),
('Terra Sonâmbula', '978-972-21-0987-4', 1992, 2, 2, 1, 1),
('Clean Code', '978-01-323-5088-4', 2008, 1, 3, 3, 2);