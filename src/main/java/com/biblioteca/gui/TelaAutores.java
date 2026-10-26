package com.biblioteca.gui;

import com.biblioteca.dao.AutorDAO;
import com.biblioteca.model.Autor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TelaAutores extends JDialog {

    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private AutorDAO dao = new AutorDAO();

    public TelaAutores(Frame parent) {
        super(parent, "Gestão de Autores", true);
        setSize(500, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // Topo: Botões de Ação
        JPanel painelTopo = new JPanel();
        JButton btnAdicionar = new JButton("Adicionar Autor");
        JButton btnRemover = new JButton("Remover Selecionado");
        
        painelTopo.add(btnAdicionar);
        painelTopo.add(btnRemover);
        add(painelTopo, BorderLayout.NORTH);

        // Centro: Tabela
        String[] colunas = {"ID", "Nome", "Nacionalidade"};
        modeloTabela = new DefaultTableModel(colunas, 0);
        tabela = new JTable(modeloTabela);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        carregarAutores();

        // Ação: Adicionar Autor
        btnAdicionar.addActionListener(e -> {
            String nome = JOptionPane.showInputDialog(this, "Nome do Autor:");
            if (nome != null && !nome.trim().isEmpty()) {
                String nacionalidade = JOptionPane.showInputDialog(this, "Nacionalidade (opcional):");
                
                Autor novo = new Autor();
                novo.setNomeAutor(nome);
                novo.setNacionalidade(nacionalidade);

                if (dao.inserir(novo)) {
                    JOptionPane.showMessageDialog(this, "Autor adicionado!");
                    carregarAutores();
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao adicionar autor.");
                }
            }
        });

        // Ação: Remover Autor
        btnRemover.addActionListener(e -> {
            int linha = tabela.getSelectedRow();
            if (linha >= 0) {
                int idAutor = (int) modeloTabela.getValueAt(linha, 0);
                String nomeAutor = (String) modeloTabela.getValueAt(linha, 1);
                
                int confirmacao = JOptionPane.showConfirmDialog(this, 
                        "Tem a certeza que quer remover o autor '" + nomeAutor + "'?", 
                        "Confirmar", JOptionPane.YES_NO_OPTION);
                
                if (confirmacao == JOptionPane.YES_OPTION) {
                    if (dao.remover(idAutor)) {
                        JOptionPane.showMessageDialog(this, "Autor removido!");
                        carregarAutores();
                    } else {
                        JOptionPane.showMessageDialog(this, "Não foi possível remover. O autor tem livros associados.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um autor na tabela primeiro.");
            }
        });
    }

    private void carregarAutores() {
        modeloTabela.setRowCount(0); // Limpa
        List<Autor> autores = dao.listarTodos();
        for (Autor a : autores) {
            Object[] linha = { a.getIdAutor(), a.getNomeAutor(), a.getNacionalidade() != null ? a.getNacionalidade() : "N/A" };
            modeloTabela.addRow(linha);
        }
    }
}