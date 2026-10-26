package com.biblioteca.dao;

import com.biblioteca.model.Autor;
import com.biblioteca.model.Categoria;
import com.biblioteca.model.Livro;
import com.biblioteca.util.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivroDAO {

    // 1. Listar todos os livros (com JOIN para trazer o nome do autor e categoria)
    public List<Livro> listarTodos() {
        List<Livro> lista = new ArrayList<>();
        String sql = "SELECT l.*, a.nome_autor, c.nome_categoria FROM livro l " +
                     "JOIN autor a ON l.id_autor = a.id_autor " +
                     "JOIN categoria c ON l.id_categoria = c.id_categoria " +
                     "ORDER BY l.titulo";
        
        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Livro l = new Livro();
                l.setIdLivro(rs.getInt("id_livro"));
                l.setTitulo(rs.getString("titulo"));
                l.setIsbn(rs.getString("isbn"));
                l.setAnoPublicacao(rs.getInt("ano_publicacao"));
                l.setExemplaresDisponiveis(rs.getInt("exemplares_disponiveis"));
                
                Autor a = new Autor();
                a.setNomeAutor(rs.getString("nome_autor"));
                l.setAutor(a);
                
                Categoria c = new Categoria();
                c.setNomeCategoria(rs.getString("nome_categoria"));
                l.setCategoria(c);
                
                lista.add(l);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar livros: " + e.getMessage());
        }
        return lista;
    }

    // 2. Inserir um novo livro
    public boolean inserir(String titulo, String isbn, int ano, int qtd, int idAutor, int idCategoria) {
        String sql = "INSERT INTO livro (titulo, isbn, ano_publicacao, exemplares_disponiveis, id_autor, id_categoria) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, titulo);
            ps.setString(2, isbn);
            ps.setInt(3, ano);
            ps.setInt(4, qtd);
            ps.setInt(5, idAutor);
            ps.setInt(6, idCategoria);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir livro: " + e.getMessage());
            return false;
        }
    }

    // 3. Remover um livro pelo ID
    public boolean remover(int idLivro) {
        String sql = "DELETE FROM livro WHERE id_livro = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idLivro);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao remover livro: " + e.getMessage());
            return false;
        }
    }
}