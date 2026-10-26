package com.biblioteca.dao;

import com.biblioteca.util.Conexao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RequisicaoDAO {

    // 1. Utilizador pede um livro (agora com quantidade)
    public boolean pedirLivro(int idMembro, int idLivro, int quantidade) {
        String sql = "INSERT INTO requisicao (id_membro, id_livro, quantidade) VALUES (?, ?, ?)";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idMembro);
            ps.setInt(2, idLivro);
            ps.setInt(3, quantidade);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao pedir livro: " + e.getMessage());
            return false;
        }
    }

    // 2. Bibliotecário aprova o pedido (Cria o empréstimo, atualiza o estado e o stock)
    public boolean aprovar(int idRequisicao, int idMembro, int idLivro, int quantidade) {
        String sqlReq = "UPDATE requisicao SET estado = 'APROVADO' WHERE id_requisicao = ?";
        try (Connection conn = Conexao.getConnection()) {
            conn.setAutoCommit(false); // Transação
            
            try (PreparedStatement ps = conn.prepareStatement(sqlReq)) {
                ps.setInt(1, idRequisicao);
                ps.executeUpdate();
            }
            
            EmprestimoDAO empDAO = new EmprestimoDAO();
            if (!empDAO.inserir(idMembro, idLivro, quantidade)) {
                conn.rollback();
                return false; // Falhou porque não há stock, por exemplo
            }
            
            conn.commit();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao aprovar requisição: " + e.getMessage());
            return false;
        }
    }

    // 3. Bibliotecário recusa o pedido
    public boolean recusar(int idRequisicao) {
        String sql = "UPDATE requisicao SET estado = 'RECUSADO' WHERE id_requisicao = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idRequisicao);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    // 4. Listar pedidos PENDENTES (para o Bibliotecário)
    public List<Object[]> listarPendentes() {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT r.id_requisicao, u.nome, l.titulo, r.quantidade, r.data_pedido, r.id_membro, r.id_livro " +
                     "FROM requisicao r " +
                     "JOIN usuario u ON r.id_membro = u.id_membro " +
                     "JOIN livro l ON r.id_livro = l.id_livro " +
                     "WHERE r.estado = 'PENDENTE'";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getInt("id_requisicao"),
                    rs.getString("nome"),
                    rs.getString("titulo"),
                    rs.getInt("quantidade"), // Nova coluna no índice 3
                    rs.getDate("data_pedido"),
                    rs.getInt("id_membro"),
                    rs.getInt("id_livro")
                });
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar pendentes: " + e.getMessage());
        }
        return lista;
    }

    // 5. Listar pedidos de um Utilizador COM AS DATAS DO EMPRÉSTIMO
    public List<Object[]> listarMeusPedidos(int idMembro) {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT r.id_requisicao, l.titulo, r.quantidade, r.data_pedido, r.estado, " +
                     "e.data_emprestimo, e.data_devolucao_prevista " +
                     "FROM requisicao r " +
                     "JOIN livro l ON r.id_livro = l.id_livro " +
                     "LEFT JOIN emprestimo e ON e.id_membro = r.id_membro AND e.id_livro = r.id_livro " +
                     "WHERE r.id_membro = ? ORDER BY r.id_requisicao DESC";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idMembro);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String estado = rs.getString("estado");
                    String dataEmp = "-";
                    String dataDev = "-";
                    
                    if ("APROVADO".equals(estado) && rs.getDate("data_emprestimo") != null) {
                        dataEmp = rs.getDate("data_emprestimo").toString();
                        dataDev = rs.getDate("data_devolucao_prevista").toString();
                    }
                    
                    lista.add(new Object[]{
                        rs.getInt("id_requisicao"),
                        rs.getString("titulo"),
                        rs.getInt("quantidade"), // Quantidade no índice 2
                        rs.getDate("data_pedido"),
                        estado,
                        dataEmp,
                        dataDev
                    });
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar meus pedidos: " + e.getMessage());
        }
        return lista;
    }
}