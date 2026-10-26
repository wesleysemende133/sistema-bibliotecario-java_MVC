package com.biblioteca.gui;

import com.biblioteca.dao.EmprestimoDAO;
import com.biblioteca.dao.LivroDAO;
import com.biblioteca.dao.RequisicaoDAO;
import com.biblioteca.model.Bibliotecario;
import com.biblioteca.model.Livro;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TelaBibliotecario extends JFrame {

    private Bibliotecario bibliotecario;
    private JTable tabela;
    private DefaultTableModel modeloTabela;

    public TelaBibliotecario(Bibliotecario b) {
        this.bibliotecario = b;
        setTitle("Painel do Bibliotecário - " + b.getNomeCompleto());
        setSize(1000, 500); // Aumentei para caber todos os botões
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Painel de topo (Botões)
        JPanel painelTopo = new JPanel();
        JButton btnAdicionarLivro = new JButton("Adicionar Livro");
        JButton btnRemoverLivro = new JButton("Remover Livro");
        JButton btnGerirAutores = new JButton("Gerir Autores");
        JButton btnVerPedidos = new JButton("Ver Pedidos Pendentes");
        JButton btnVerEmprestimos = new JButton("Ver Empréstimos Ativos");
        JButton btnAtualizar = new JButton("Atualizar Tabela");
        JButton btnSair = new JButton("Sair");
        
        painelTopo.add(btnAdicionarLivro);
        painelTopo.add(btnRemoverLivro);
        painelTopo.add(btnGerirAutores);
        painelTopo.add(btnVerPedidos);
        painelTopo.add(btnVerEmprestimos);
        painelTopo.add(btnAtualizar);
        painelTopo.add(btnSair);
        add(painelTopo, BorderLayout.NORTH);

        // Tabela de Livros
        String[] colunas = {"ID", "Título", "Autor", "Stock"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabela = new JTable(modeloTabela);
        JScrollPane scroll = new JScrollPane(tabela);
        add(scroll, BorderLayout.CENTER);

        carregarLivros();

        // Ações dos botões
        btnAtualizar.addActionListener(e -> carregarLivros());

        btnSair.addActionListener(e -> {
            dispose();
            new TelaLogin().setVisible(true);
        });

        btnGerirAutores.addActionListener(e -> {
            TelaAutores telaAutores = new TelaAutores(this);
            telaAutores.setVisible(true);
        });

        btnVerPedidos.addActionListener(e -> abrirTelaPedidosPendentes());
        btnVerEmprestimos.addActionListener(e -> abrirTelaEmprestimosAtivos());

        btnRemoverLivro.addActionListener(e -> {
            int linhaSelecionada = tabela.getSelectedRow();
            if (linhaSelecionada >= 0) {
                int idLivro = (int) modeloTabela.getValueAt(linhaSelecionada, 0);
                int confirmacao = JOptionPane.showConfirmDialog(this, "Remover este livro?", "Confirmar", JOptionPane.YES_NO_OPTION);
                
                if (confirmacao == JOptionPane.YES_OPTION) {
                    boolean sucesso = new LivroDAO().remover(idLivro);
                    if (sucesso) {
                        JOptionPane.showMessageDialog(this, "Livro removido!");
                        carregarLivros();
                    } else {
                        JOptionPane.showMessageDialog(this, "Erro ao remover. Pode ter empréstimos ativos.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um livro na tabela primeiro.");
            }
        });

        btnAdicionarLivro.addActionListener(e -> {
            TelaAdicionarLivro dialog = new TelaAdicionarLivro(this);
            dialog.setVisible(true);
            carregarLivros();
        });
    }

    private void carregarLivros() {
        modeloTabela.setRowCount(0); 
        List<Livro> livros = new LivroDAO().listarTodos();
        for (Livro l : livros) {
            Object[] linha = {
                l.getIdLivro(),
                l.getTitulo(),
                l.getAutor().getNomeAutor(),
                l.getExemplaresDisponiveis()
            };
            modeloTabela.addRow(linha);
        }
    }

    // --- JANELA 1: PEDIDOS PENDENTES ---
    private void abrirTelaPedidosPendentes() {
        JDialog dialog = new JDialog(this, "Pedidos de Requisição Pendentes", true);
        dialog.setSize(800, 400); // Aumentei para caber a coluna Qtd
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        // NOVA COLUNA "Qtd" NO ÍNDICE 3
        String[] colunas = {"ID Pedido", "Nome do Utilizador", "Livro", "Qtd", "Data", "ID User", "ID Livro"};
        DefaultTableModel modelo = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable tabelaPedidos = new JTable(modelo);
        
        // Ocultar as colunas de ID (agora são os índices 5 e 6)
        tabelaPedidos.getColumnModel().getColumn(5).setMinWidth(0);
        tabelaPedidos.getColumnModel().getColumn(5).setMaxWidth(0);
        tabelaPedidos.getColumnModel().getColumn(6).setMinWidth(0);
        tabelaPedidos.getColumnModel().getColumn(6).setMaxWidth(0);

        dialog.add(new JScrollPane(tabelaPedidos), BorderLayout.CENTER);

        JButton btnAprovar = new JButton("Aprovar Pedido");
        JButton btnRecusar = new JButton("Recusar Pedido");
        JButton btnAtualizar = new JButton("Atualizar Lista");
        
        JPanel painelSul = new JPanel();
        painelSul.add(btnAprovar);
        painelSul.add(btnRecusar);
        painelSul.add(btnAtualizar);
        dialog.add(painelSul, BorderLayout.SOUTH);

        Runnable carregarDados = () -> {
            modelo.setRowCount(0);
            List<Object[]> pedidos = new RequisicaoDAO().listarPendentes();
            for (Object[] p : pedidos) modelo.addRow(p);
        };

        carregarDados.run();
        btnAtualizar.addActionListener(e -> carregarDados.run());

        btnAprovar.addActionListener(e -> {
            int linha = tabelaPedidos.getSelectedRow();
            if (linha >= 0) {
                int idReq = (int) modelo.getValueAt(linha, 0);
                int idUser = (int) modelo.getValueAt(linha, 5); // Índice atualizado
                int idLivro = (int) modelo.getValueAt(linha, 6); // Índice atualizado
                int qtd = (int) modelo.getValueAt(linha, 3); // NOVO: Ler a quantidade

                if (new RequisicaoDAO().aprovar(idReq, idUser, idLivro, qtd)) {
                    JOptionPane.showMessageDialog(dialog, "Pedido de " + qtd + " exemplar(es) Aprovado!");
                    carregarDados.run();
                    carregarLivros();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Erro ao aprovar (Verifique se há stock suficiente).");
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "Selecione um pedido na tabela.");
            }
        });

        btnRecusar.addActionListener(e -> {
            int linha = tabelaPedidos.getSelectedRow();
            if (linha >= 0) {
                int idReq = (int) modelo.getValueAt(linha, 0);
                if (new RequisicaoDAO().recusar(idReq)) {
                    JOptionPane.showMessageDialog(dialog, "Pedido Recusado.");
                    carregarDados.run();
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "Selecione um pedido na tabela.");
            }
        });

        dialog.setVisible(true);
    }

    // --- JANELA 2: EMPRÉSTIMOS ATIVOS E DEVOLUÇÕES ---
    private void abrirTelaEmprestimosAtivos() {
        JDialog dialog = new JDialog(this, "Empréstimos Ativos (Devoluções)", true);
        dialog.setSize(800, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        String[] colunas = {"ID Empréstimo", "Nome do Utilizador", "Livro", "Data Empréstimo", "Devolver Até"};
        DefaultTableModel modelo = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable tabelaEmprestimos = new JTable(modelo);
        dialog.add(new JScrollPane(tabelaEmprestimos), BorderLayout.CENTER);

        JButton btnDevolver = new JButton("Registar Devolução Selecionada");
        JButton btnAtualizar = new JButton("Atualizar Lista");
        
        JPanel painelSul = new JPanel();
        painelSul.add(btnDevolver);
        painelSul.add(btnAtualizar);
        dialog.add(painelSul, BorderLayout.SOUTH);

        Runnable carregarDados = () -> {
            modelo.setRowCount(0);
            List<Object[]> ativos = new EmprestimoDAO().listarAtivos();
            for (Object[] e : ativos) modelo.addRow(e);
        };

        carregarDados.run();
        btnAtualizar.addActionListener(e -> carregarDados.run());

        btnDevolver.addActionListener(e -> {
            int linha = tabelaEmprestimos.getSelectedRow();
            if (linha >= 0) {
                int idEmprestimo = (int) modelo.getValueAt(linha, 0);
                String nomeUser = (String) modelo.getValueAt(linha, 1);
                String livro = (String) modelo.getValueAt(linha, 2);

                int confirmacao = JOptionPane.showConfirmDialog(dialog, 
                    "Confirmar devolução do livro '" + livro + "' por " + nomeUser + "?", 
                    "Confirmar Devolução", JOptionPane.YES_NO_OPTION);

                if (confirmacao == JOptionPane.YES_OPTION) {
                    if (new EmprestimoDAO().registarDevolucao(idEmprestimo)) {
                        JOptionPane.showMessageDialog(dialog, "Devolução registada com sucesso!");
                        carregarDados.run();
                        carregarLivros();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Erro ao registar devolução.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "Selecione um empréstimo na tabela para devolver.");
            }
        });

        dialog.setVisible(true);
    }
}