package com.biblioteca.gui;

import com.biblioteca.dao.LivroDAO;
import com.biblioteca.dao.PessoaDAO;
import com.biblioteca.dao.RequisicaoDAO;
import com.biblioteca.model.Livro;
import com.biblioteca.model.UsuarioComum;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TelaUsuario extends JFrame {

    private UsuarioComum usuario;
    private JTable tabela;
    private DefaultTableModel modeloTabela;

    public TelaUsuario(UsuarioComum u) {
        this.usuario = u;
        setTitle("Área do Utilizador - " + u.getNomeCompleto());
        setSize(850, 450); // Aumentei para caber a nova coluna de quantidade
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Topo: Botões do Utilizador
        JPanel painelTopo = new JPanel();
        JButton btnAtualizar = new JButton("Atualizar Catálogo");
        JButton btnEditarPerfil = new JButton("Editar Perfil"); 
        JButton btnPedirLivro = new JButton("Pedir Livro"); 
        JButton btnMeusPedidos = new JButton("Meus Pedidos"); 
        JButton btnSair = new JButton("Sair"); 
        
        painelTopo.add(new JLabel("Olá, " + u.getNomeCompleto() + " |"));
        painelTopo.add(btnAtualizar);
        painelTopo.add(btnEditarPerfil);
        painelTopo.add(btnPedirLivro);
        painelTopo.add(btnMeusPedidos);
        painelTopo.add(btnSair);
        add(painelTopo, BorderLayout.NORTH);

        // Tabela Principal (Catálogo)
        String[] colunas = {"ID", "Título", "Autor", "Categoria", "Disponíveis"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabela = new JTable(modeloTabela);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        carregarLivros();

        // Ações
        btnAtualizar.addActionListener(e -> carregarLivros());

        btnSair.addActionListener(e -> {
            dispose();
            new TelaLogin().setVisible(true);
        });

        btnPedirLivro.addActionListener(e -> {
            String inputId = JOptionPane.showInputDialog(this, "Digite o ID do livro que deseja requisitar (ver coluna 'ID' da tabela):");
            if (inputId != null && !inputId.trim().isEmpty()) {
                try {
                    int idLivro = Integer.parseInt(inputId);
                    
                    String inputQtd = JOptionPane.showInputDialog(this, "Quantos exemplares deste livro deseja?");
                    if (inputQtd != null && !inputQtd.trim().isEmpty()) {
                        int quantidade = Integer.parseInt(inputQtd);
                        
                        RequisicaoDAO dao = new RequisicaoDAO();
                        if (dao.pedirLivro(usuario.getIdPessoa(), idLivro, quantidade)) {
                            JOptionPane.showMessageDialog(this, "Pedido de " + quantidade + " exemplar(es) enviado! Aguarde aprovação.");
                        } else {
                            JOptionPane.showMessageDialog(this, "Erro ao enviar o pedido.");
                        }
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Digite números válidos.");
                }
            }
        });

        btnMeusPedidos.addActionListener(e -> abrirMeusPedidos());
        btnEditarPerfil.addActionListener(e -> abrirEdicaoPerfil());
    }

    private void carregarLivros() {
        modeloTabela.setRowCount(0);
        List<Livro> livros = new LivroDAO().listarTodos();
        for (Livro l : livros) {
            Object[] linha = {
                l.getIdLivro(),
                l.getTitulo(),
                l.getAutor().getNomeAutor(),
                l.getCategoria().getNomeCategoria(),
                l.getExemplaresDisponiveis() > 0 ? "Sim (" + l.getExemplaresDisponiveis() + ")" : "Esgotado"
            };
            modeloTabela.addRow(linha);
        }
    }

    // Método atualizado: com a coluna de quantidade
    private void abrirMeusPedidos() {
        JDialog dialog = new JDialog(this, "Meus Pedidos e Empréstimos", true);
        dialog.setSize(750, 300); 
        dialog.setLocationRelativeTo(this);
        
        // Adicionada a coluna "Qtd"
        String[] colunas = {"ID Pedido", "Livro", "Qtd", "Data Pedido", "Estado", "Data Empréstimo", "Devolver Até"};
        DefaultTableModel modelo = new DefaultTableModel(colunas, 0);
        JTable tabelaPedidos = new JTable(modelo);
        dialog.add(new JScrollPane(tabelaPedidos));
        
        List<Object[]> meusPedidos = new RequisicaoDAO().listarMeusPedidos(usuario.getIdPessoa());
        for (Object[] p : meusPedidos) {
            modelo.addRow(p);
        }
        
        dialog.setVisible(true);
    }

    // Janela para o utilizador editar os seus dados
    private void abrirEdicaoPerfil() {
        JTextField txtNome = new JTextField(usuario.getNomeCompleto());
        JTextField txtEmail = new JTextField(usuario.getEmail());
        JPasswordField txtSenha = new JPasswordField(usuario.getSenhaAcesso());
        JTextField txtTelefone = new JTextField(usuario.getTelefone());

        Object[] message = {
            "Nome Completo:", txtNome,
            "Email:", txtEmail,
            "Senha:", txtSenha,
            "Telefone:", txtTelefone
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Editar Perfil", JOptionPane.OK_CANCEL_OPTION);
        
        if (option == JOptionPane.OK_OPTION) {
            usuario.setNomeCompleto(txtNome.getText());
            usuario.setEmail(txtEmail.getText());
            usuario.setSenhaAcesso(new String(txtSenha.getPassword()));
            usuario.setTelefone(txtTelefone.getText());

            PessoaDAO dao = new PessoaDAO();
            if (dao.atualizar(usuario)) {
                JOptionPane.showMessageDialog(this, "Perfil atualizado com sucesso!");
                setTitle("Área do Utilizador - " + usuario.getNomeCompleto()); 
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao atualizar perfil. O email pode já estar em uso.");
            }
        }
    }
}