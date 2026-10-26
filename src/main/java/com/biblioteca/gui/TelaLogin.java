package com.biblioteca.gui;

import com.biblioteca.dao.PessoaDAO;
import com.biblioteca.model.Bibliotecario;
import com.biblioteca.model.Pessoa;
import com.biblioteca.model.UsuarioComum;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class TelaLogin extends JFrame {

    private JTextField txtEmail;
    private JPasswordField txtSenha;

    public TelaLogin() {
        setTitle("Biblioteca - Login");
        setSize(350, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 2, 10, 10));

        // Componentes de Login
        add(new JLabel(" Email:"));
        txtEmail = new JTextField();
        add(txtEmail);

        add(new JLabel(" Senha:"));
        txtSenha = new JPasswordField();
        add(txtSenha);

        JButton btnEntrar = new JButton("Entrar");
        JButton btnCriarConta = new JButton("Criar Nova Conta"); // NOVO BOTÃO
        add(btnEntrar);
        add(btnCriarConta);

        // Ação do botão Entrar
        btnEntrar.addActionListener((ActionEvent e) -> {
            fazerLogin();
        });

        // Ação do botão Criar Conta
        btnCriarConta.addActionListener((ActionEvent e) -> {
            abrirTelaRegisto();
        });
    }

    private void fazerLogin() {
        String email = txtEmail.getText();
        String senha = new String(txtSenha.getPassword());

        PessoaDAO dao = new PessoaDAO();
        Pessoa logado = dao.autenticar(email, senha);

        if (logado == null) {
            JOptionPane.showMessageDialog(this, "Email ou senha incorretos!", "Erro", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Bem-vindo, " + logado.getNomeCompleto());
            this.dispose(); // Fecha a janela de login
            
            if (logado instanceof Bibliotecario) {
                new TelaBibliotecario((Bibliotecario) logado).setVisible(true);
            } else {
                new TelaUsuario((UsuarioComum) logado).setVisible(true);
            }
        }
    }

    // NOVO MÉTODO: Formulário de Registo
    private void abrirTelaRegisto() {
        JTextField regNome = new JTextField();
        JTextField regEmail = new JTextField();
        JPasswordField regSenha = new JPasswordField();
        JTextField regTelefone = new JTextField();
        JTextField regCartao = new JTextField();

        Object[] message = {
            "Nome Completo:", regNome,
            "Email:", regEmail,
            "Senha:", regSenha,
            "Telefone:", regTelefone,
            "Nº do Cartão de Biblioteca:", regCartao
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Criar Nova Conta", JOptionPane.OK_CANCEL_OPTION);
        
        if (option == JOptionPane.OK_OPTION) {
            if (regNome.getText().isEmpty() || regEmail.getText().isEmpty() || regSenha.getPassword().length == 0) {
                JOptionPane.showMessageDialog(this, "Nome, Email e Senha são obrigatórios!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            UsuarioComum novoUser = new UsuarioComum(
                regNome.getText(), 
                regEmail.getText(), 
                new String(regSenha.getPassword()), 
                regTelefone.getText(), 
                regCartao.getText()
            );

            PessoaDAO dao = new PessoaDAO();
            if (dao.inserirUsuarioComum(novoUser)) {
                JOptionPane.showMessageDialog(this, "Conta criada com sucesso! Já pode fazer login.");
                txtEmail.setText(regEmail.getText()); // Preenche o email para facilitar o login
                txtSenha.setText(""); // Limpa a senha por segurança
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao criar conta. O email pode já estar registado.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TelaLogin().setVisible(true);
        });
    }
}