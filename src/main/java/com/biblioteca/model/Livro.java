package com.biblioteca.model;

public class Livro {
    private int idLivro;
    private String titulo;
    private String isbn;
    private int anoPublicacao;
    private int exemplaresDisponiveis;
    private Autor autor;
    private Categoria categoria;
    private Estante estante;

    public Livro() {}

    public boolean estaDisponivel() {
        return exemplaresDisponiveis > 0;
    }

    // Getters e Setters...
    public int getIdLivro() { return idLivro; }
    public void setIdLivro(int idLivro) { this.idLivro = idLivro; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public int getAnoPublicacao() { return anoPublicacao; }
    public void setAnoPublicacao(int anoPublicacao) { this.anoPublicacao = anoPublicacao; }
    public int getExemplaresDisponiveis() { return exemplaresDisponiveis; }
    public void setExemplaresDisponiveis(int exemplaresDisponiveis) { this.exemplaresDisponiveis = exemplaresDisponiveis; }
    public Autor getAutor() { return autor; }
    public void setAutor(Autor autor) { this.autor = autor; }
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
    public Estante getEstante() { return estante; }
    public void setEstante(Estante estante) { this.estante = estante; }
}