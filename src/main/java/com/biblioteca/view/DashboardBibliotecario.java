package com.biblioteca.view;

import com.biblioteca.dao.LivroDAO;
import com.biblioteca.model.Bibliotecario;
import com.biblioteca.model.Livro;
import java.util.List;
import java.util.Scanner;

public class DashboardBibliotecario {
    
    private Bibliotecario bibliotecario;
    
    public DashboardBibliotecario(Bibliotecario b) {
        this.bibliotecario = b;
    }

    public void menuLivros(Scanner ler) {
        int op = -1;
        while (op != 0) {
            System.out.println("\n--- GERIR LIVROS ---");
            System.out.println("1 - Listar Livros");
            System.out.println("2 - Adicionar Livro");
            System.out.println("3 - Remover Livro");
            System.out.println("0 - Voltar");
            System.out.print("Opção: ");
            
            try {
                op = Integer.parseInt(ler.nextLine());
            } catch (Exception e) {
                System.out.println("Inválido.");
                continue;
            }
            
            LivroDAO dao = new LivroDAO();
            
            if (op == 1) {
                List<Livro> lista = dao.listarTodos();
                if (lista.size() == 0) {
                    System.out.println("Nenhum livro cadastrado.");
                } else {
                    for (Livro l : lista) {
                        System.out.println(l.getIdLivro() + " | " + l.getTitulo() + 
                                           " | Autor: " + l.getAutor().getNomeAutor() + 
                                           " | Stock: " + l.getExemplaresDisponiveis());
                    }
                }
            } 
            else if (op == 2) {
                try {
                    System.out.print("Titulo: ");
                    String titulo = ler.nextLine();
                    System.out.print("ISBN: ");
                    String isbn = ler.nextLine();
                    System.out.print("Ano de Publicacao: ");
                    int ano = Integer.parseInt(ler.nextLine());
                    System.out.print("Quantidade: ");
                    int qtd = Integer.parseInt(ler.nextLine());
                    System.out.print("ID do Autor: ");
                    int idAutor = Integer.parseInt(ler.nextLine());
                    System.out.print("ID da Categoria: ");
                    int idCat = Integer.parseInt(ler.nextLine());
                    
                    dao.inserir(titulo, isbn, ano, qtd, idAutor, idCat);
                    System.out.println("Livro gravado!");
                } catch (Exception e) {
                    System.out.println("Erro nos dados inseridos. Tente de novo.");
                }
            } 
            else if (op == 3) {
                System.out.print("Digite o ID do livro que quer apagar: ");
                int id = Integer.parseInt(ler.nextLine());
                
                boolean apagou = dao.remover(id);
                if (apagou) {
                    System.out.println("Livro apagado do sistema.");
                } else {
                    System.out.println("Nao foi possivel apagar. O livro pode ter emprestimos ativos.");
                }
            }
        }
    }
}