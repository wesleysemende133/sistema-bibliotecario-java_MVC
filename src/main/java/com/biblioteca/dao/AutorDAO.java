package com.biblioteca.dao;

import com.biblioteca.model.Autor;
import com.biblioteca.util.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AutorDAO {

    public List<Autor> listarTodos() {
        List<Autor> lista = new ArrayList<>();
        String sql = "SELECT * FROM autor ORDER BY nome_autor";
        
        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Autor a = new Autor();
                a.setIdAutor(rs.getInt("id_autor"));
                a.setNomeAutor(rs.getString("nome_autor"));
                a.setNacionalidade(rs.getString("nacionalidade"));
                Date d = rs.getDate("data_nascimento");
                if (d != null) a.setDataNascimento(d.toLocalDate());
                lista.add(a);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar autores: " + e.getMessage());
        }
        return lista;
    }

    public boolean inserir(Autor autor) {
        String sql = "INSERT INTO autor (nome_autor, nacionalidade, data_nascimento) VALUES (?, ?, ?)";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, autor.getNomeAutor());
            ps.setString(2, autor.getNacionalidade());
            if (autor.getDataNascimento() != null) {
                ps.setDate(3, Date.valueOf(autor.getDataNascimento()));
            } else {
                ps.setNull(3, Types.DATE);
            }
            
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        autor.setIdAutor(rs.getInt(1)); // Atualiza o ID do objeto
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao inserir autor: " + e.getMessage());
        }
        return false;
    }

    public boolean remover(int idAutor) {
        String sql = "DELETE FROM autor WHERE id_autor = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idAutor);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            // Se o autor tiver livros associados, o MySQL não vai deixar apagar (restrição de FK)
            System.err.println("Erro ao remover autor: " + e.getMessage());
            return false;
        }
    }
}