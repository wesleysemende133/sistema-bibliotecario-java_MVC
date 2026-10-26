package com.biblioteca;

import com.biblioteca.dao.PessoaDAO;
import com.biblioteca.model.Bibliotecario;
import com.biblioteca.model.Pessoa;
import com.biblioteca.model.UsuarioComum;
import com.biblioteca.view.DashboardBibliotecario;
import com.biblioteca.view.DashboardUsuario;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner ler = new Scanner(System.in);
        
        System.out.println("=================================");
        System.out.println("     BIBLIOTECA - LOGIN");
        System.out.println("=================================");
        
        System.out.print("Email: ");
        String email = ler.nextLine();
        System.out.print("Senha: ");
        String senha = ler.nextLine();

        PessoaDAO dao = new PessoaDAO();
        Pessoa utilizador = dao.autenticar(email, senha);

        if (utilizador == null) {
            System.out.println("\n[ERRO] Email ou senha incorretos.");
        } else {
            System.out.println("\nBem-vindo(a), " + utilizador.getNomeCompleto());
            
            if (utilizador instanceof Bibliotecario) {
                menuBibliotecario((Bibliotecario) utilizador, ler);
            } else {
                menuUtilizador((UsuarioComum) utilizador, ler);
            }
        }
        ler.close();
    }

    // O menu do Bibliotecário aqui mesmo
    private static void menuBibliotecario(Bibliotecario b, Scanner ler) {
        int opcao = -1;
        do {
            System.out.println("\n--- PAINEL DO BIBLIOTECÁRIO ---");
            System.out.println("1 - Gerir Livros");
            System.out.println("2 - Novo Empréstimo");
            System.out.println("3 - Devolução");
            System.out.println("0 - Sair");
            System.out.print("Escolha: ");
            
            try {
                opcao = Integer.parseInt(ler.nextLine());
                if (opcao == 1) {
                    // A chamada para a View
                    new DashboardBibliotecario(b).menuLivros(ler);
                } else if (opcao == 2) {
                    // ... Lógica de empréstimo
                } else if (opcao == 0) {
                    b.logout();
                }
            } catch (Exception e) {
                System.out.println("Erro: Digite um número.");
            }
        } while (opcao != 0);
    }

    // O menu do Usuário Comum aqui mesmo
    private static void menuUtilizador(UsuarioComum u, Scanner ler) {
        int opcao = -1;
        do {
            System.out.println("\n--- PAINEL DO USUÁRIO ---");
            System.out.println("1 - Ver Catálogo");
            System.out.println("0 - Sair");
            System.out.print("Escolha: ");
            
            try {
                opcao = Integer.parseInt(ler.nextLine());
                if (opcao == 1) {
                    new DashboardUsuario(u).mostrarCatalogo();
                } else if (opcao == 0) {
                    u.logout();
                }
            } catch (Exception e) {
                System.out.println("Digite um número.");
            }
        } while (opcao != 0);
    }
}