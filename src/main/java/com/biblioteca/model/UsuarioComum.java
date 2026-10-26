package com.biblioteca.model;

public class UsuarioComum extends Pessoa {
    private String numeroCartaoBiblioteca;

    public UsuarioComum() { super(); }
    public UsuarioComum(String nome, String email, String senha, String tel, String cartao) {
        super(nome, email, senha, tel);
        this.numeroCartaoBiblioteca = cartao;
    }

    public String getNumeroCartaoBiblioteca() { return numeroCartaoBiblioteca; }
    public void setNumeroCartaoBiblioteca(String numeroCartaoBiblioteca) { this.numeroCartaoBiblioteca = numeroCartaoBiblioteca; }
}