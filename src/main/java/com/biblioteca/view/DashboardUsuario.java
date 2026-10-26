package com.biblioteca.view;

import com.biblioteca.dao.LivroDAO;
import com.biblioteca.model.Livro;
import com.biblioteca.model.UsuarioComum;
import java.util.List;

public class DashboardUsuario {
    
    private UsuarioComum user;
    
    public DashboardUsuario(UsuarioComum u) {
        this.user = u;
    }

    public void mostrarCatalogo() {
        LivroDAO dao = new LivroDAO();
        List<Livro> livros = dao.listarTodos();
        
        System.out.println("\n--- CATALOGO DA BIBLIOTECA ---");
        System.out.println("Titulo / Autor / Disponiveis");
        System.out.println("---------------------------------");
        
        for (int i = 0; i < livros.size(); i++) {
            Livro l = livros.get(i);
            System.out.println(l.getTitulo() + " - " + l.getAutor().getNomeAutor() + " (" + l.getExemplaresDisponiveis() + " disp.)");
        }
        
        if (livros.isEmpty()) {
            System.out.println("A biblioteca esta vazia de momento.");
        }
    }
}