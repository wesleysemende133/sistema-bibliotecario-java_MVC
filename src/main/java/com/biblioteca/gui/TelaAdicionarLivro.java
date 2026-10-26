package com.biblioteca.gui;

import com.biblioteca.dao.AutorDAO;
import com.biblioteca.dao.LivroDAO;
import com.biblioteca.model.Autor;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TelaAdicionarLivro extends JDialog {

    private JTextField txtTitulo, txtIsbn, txtAno, txtQtd, txtIdCategoria;
    private JComboBox<String> comboAutores;
    private JButton btnSalvar, btnCancelar, btnApagarAutor;
    private AutorDAO autorDAO = new AutorDAO();
    private LivroDAO livroDAO = new LivroDAO();

    public TelaAdicionarLivro(Frame parent) {
        super(parent, "Adicionar Novo Livro", true);
        setSize(400, 450);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(8, 2, 10, 10));

        // Componentes
        add(new JLabel(" Título:"));
        txtTitulo = new JTextField();
        add(txtTitulo);

        add(new JLabel(" ISBN:"));
        txtIsbn = new JTextField();
        add(txtIsbn);

        add(new JLabel(" Ano de Publicação:"));
        txtAno = new JTextField();
        add(txtAno);

        add(new JLabel(" Quantidade:"));
        txtQtd = new JTextField();
        add(txtQtd);

        add(new JLabel(" ID da Categoria (1 a 4):"));
        txtIdCategoria = new JTextField();
        add(txtIdCategoria);

        // Área do Autor
        add(new JLabel(" Autor:"));
        JPanel painelAutor = new JPanel(new BorderLayout(5, 0));
        comboAutores = new JComboBox<>();
        btnApagarAutor = new JButton("X");
        btnApagarAutor.setToolTipText("Apagar autor selecionado");
        painelAutor.add(comboAutores, BorderLayout.CENTER);
        painelAutor.add(btnApagarAutor, BorderLayout.EAST);
        add(painelAutor);

        add(new JLabel("")); // Espaço
        JButton btnNovoAutor = new JButton("Criar Novo Autor");
        add(btnNovoAutor);

        add(new JLabel("")); // Espaço
        JPanel painelBotoes = new JPanel(new FlowLayout());
        btnSalvar = new JButton("Salvar");
        btnCancelar = new JButton("Cancelar");
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnCancelar);
        add(painelBotoes);

        // Carregar autores iniciais
        carregarAutores();

        // Ações
        btnCancelar.addActionListener(e -> dispose());

        btnApagarAutor.addActionListener(e -> {
            String selecionado = (String) comboAutores.getSelectedItem();
            if (selecionado != null) {
                // Extrai o ID da string (ex: "1 - José Saramago")
                int idAutor = Integer.parseInt(selecionado.split(" - ")[0]);
                if (autorDAO.remover(idAutor)) {
                    JOptionPane.showMessageDialog(this, "Autor removido!");
                    carregarAutores();
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao remover. O autor pode ter livros associados.");
                }
            }
        });

        btnNovoAutor.addActionListener(e -> {
            String nome = JOptionPane.showInputDialog(this, "Nome do novo autor:");
            if (nome != null && !nome.trim().isEmpty()) {
                Autor novo = new Autor();
                novo.setNomeAutor(nome);
                if (autorDAO.inserir(novo)) {
                    JOptionPane.showMessageDialog(this, "Autor criado com sucesso!");
                    carregarAutores();
                    // Seleciona o novo autor na lista
                    comboAutores.setSelectedItem(novo.getIdAutor() + " - " + novo.getNomeAutor());
                }
            }
        });

        btnSalvar.addActionListener(e -> {
            try {
                String autorSelecionado = (String) comboAutores.getSelectedItem();
                if (autorSelecionado == null) {
                    JOptionPane.showMessageDialog(this, "Selecione um autor!");
                    return;
                }
                
                int idAutor = Integer.parseInt(autorSelecionado.split(" - ")[0]);
                int idCat = Integer.parseInt(txtIdCategoria.getText());
                
                boolean sucesso = livroDAO.inserir(
                    txtTitulo.getText(), txtIsbn.getText(),
                    Integer.parseInt(txtAno.getText()),
                    Integer.parseInt(txtQtd.getText()),
                    idAutor, idCat
                );

                if (sucesso) {
                    JOptionPane.showMessageDialog(this, "Livro adicionado!");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao guardar livro.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Ano, Quantidade e Categoria devem ser números.");
            }
        });
    }

    private void carregarAutores() {
        comboAutores.removeAllItems();
        List<Autor> autores = autorDAO.listarTodos();
        for (Autor a : autores) {
            comboAutores.addItem(a.getIdAutor() + " - " + a.getNomeAutor());
        }
    }
}