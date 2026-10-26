package com.biblioteca.dao;

import com.biblioteca.model.EstadoEmprestimo;
import com.biblioteca.util.Conexao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmprestimoDAO {

        public boolean inserir(int idPessoa, int idLivro, int quantidade) {
        String sqlInsert = "INSERT INTO emprestimo (id_membro, id_livro, data_emprestimo, data_devolucao_prevista, estado) VALUES (?, ?, ?, ?, ?)";
        String sqlUpdateStock = "UPDATE livro SET exemplares_disponiveis = exemplares_disponiveis - ? WHERE id_livro = ? AND exemplares_disponiveis >= ?";
        
        try (Connection conn = Conexao.getConnection()) {
            conn.setAutoCommit(false); 
            
            try (PreparedStatement psStock = conn.prepareStatement(sqlUpdateStock)) {
                psStock.setInt(1, quantidade);
                psStock.setInt(2, idLivro);
                psStock.setInt(3, quantidade);
                if (psStock.executeUpdate() == 0) {
                    conn.rollback();
                    return false; // Não há stock suficiente
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(sqlInsert)) {
                LocalDate hoje = LocalDate.now();
                ps.setInt(1, idPessoa);
                ps.setInt(2, idLivro);
                ps.setDate(3, Date.valueOf(hoje));
                ps.setDate(4, Date.valueOf(hoje.plusDays(15)));
                ps.setString(5, EstadoEmprestimo.EMPRESTADO.name());
                ps.executeUpdate();
            }
            
            conn.commit();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir empréstimo: " + e.getMessage());
            return false;
        }
    }

    public boolean registarDevolucao(int idEmprestimo) {
        String sqlUpdateEmp = "UPDATE emprestimo SET data_devolucao_real = ?, estado = ? WHERE id_emprestimo = ? AND estado = 'EMPRESTADO'";
        String sqlUpdateStock = "UPDATE livro l JOIN emprestimo e ON l.id_livro = e.id_livro SET l.exemplares_disponiveis = l.exemplares_disponiveis + 1 WHERE e.id_emprestimo = ?";
        
        try (Connection conn = Conexao.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sqlUpdateEmp)) {
                ps.setDate(1, Date.valueOf(LocalDate.now()));
                ps.setString(2, EstadoEmprestimo.DEVOLVIDO.name());
                ps.setInt(3, idEmprestimo);
                if (ps.executeUpdate() == 0) {
                    conn.rollback();
                    return false; // Emprestimo nao encontrado ou ja devolvido
                }
            }

            try (PreparedStatement psStock = conn.prepareStatement(sqlUpdateStock)) {
                psStock.setInt(1, idEmprestimo);
                psStock.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao registar devolucao: " + e.getMessage());
            return false;
        }
    }

    public List<Object[]> listarAtivos() {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT e.id_emprestimo, u.nome, l.titulo, e.data_emprestimo, e.data_devolucao_prevista " +
                     "FROM emprestimo e " +
                     "JOIN usuario u ON e.id_membro = u.id_membro " +
                     "JOIN livro l ON e.id_livro = l.id_livro " +
                     "WHERE e.estado = 'EMPRESTADO' ORDER BY e.id_emprestimo";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getInt("id_emprestimo"),
                    rs.getString("nome"),
                    rs.getString("titulo"),
                    rs.getDate("data_emprestimo"),
                    rs.getDate("data_devolucao_prevista")
                });
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar emprestimos ativos: " + e.getMessage());
        }
        return lista;
    }
}