package com.biblioteca.dao;

import com.biblioteca.model.*;
import com.biblioteca.util.Conexao;

import java.sql.*;

public class PessoaDAO {

    // Autentica e devolve o tipo correto de utilizador (Polimorfismo)
    public Pessoa autenticar(String email, String senhaAcesso) {
        String sql = "SELECT * FROM usuario WHERE email = ? AND senha = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, email);
            ps.setString(2, senhaAcesso);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String tipo = rs.getString("tipo");
                    Pessoa p;
                    
                    if ("BIBLIOTECARIO".equals(tipo)) {
                        Bibliotecario b = new Bibliotecario();
                        b.setNumeroFuncionario(rs.getString("codigo_funcionario"));
                        Date d = rs.getDate("data_admissao");
                        if (d != null) b.setDataAdmissao(d.toLocalDate());
                        p = b;
                    } else {
                        UsuarioComum u = new UsuarioComum();
                        u.setNumeroCartaoBiblioteca(rs.getString("num_cartao"));
                        p = u;
                    }
                    
                    p.setIdPessoa(rs.getInt("id_membro"));
                    p.setNomeCompleto(rs.getString("nome"));
                    p.setEmail(rs.getString("email"));
                    p.setSenhaAcesso(rs.getString("senha"));
                    p.setTelefone(rs.getString("telefone"));
                    Date dr = rs.getDate("data_registo");
                    if (dr != null) p.setDataCriacaoConta(dr.toLocalDate());
                    
                    return p;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro na autenticação: " + e.getMessage());
        }
        return null;
    }

    public boolean inserirUsuarioComum(UsuarioComum u) {
        String sql = "INSERT INTO usuario (nome, email, tipo, senha, telefone, num_cartao) VALUES (?, ?, 'COMUM', ?, ?, ?)";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getNomeCompleto());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getSenhaAcesso());
            ps.setString(4, u.getTelefone());
            ps.setString(5, u.getNumeroCartaoBiblioteca());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao registar utilizador: " + e.getMessage());
            return false;
        }
    }

    public boolean atualizar(Pessoa p) {
        String sql = "UPDATE usuario SET nome = ?, email = ?, senha = ?, telefone = ? WHERE id_membro = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, p.getNomeCompleto());
            ps.setString(2, p.getEmail());
            ps.setString(3, p.getSenhaAcesso());
            ps.setString(4, p.getTelefone());
            ps.setInt(5, p.getIdPessoa());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar utilizador: " + e.getMessage());
            return false;
        }
    }
}