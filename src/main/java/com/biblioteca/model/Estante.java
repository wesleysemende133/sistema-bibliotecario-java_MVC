package com.biblioteca.model;

public class Estante {
    private int idEstante;
    private String codigoEstante;
    private String localizacao;
    private int capacidadeMaxima;
    private int exemplaresOcupados;
    private Categoria categoria;

    public Estante() {}

    public boolean temEspacoDisponivel() {
        return exemplaresOcupados < capacidadeMaxima;
    }

    public double ocupacaoPercentual() {
        if (capacidadeMaxima == 0) return 0.0;
        return (double) exemplaresOcupados / capacidadeMaxima * 100.0;
    }

    // Getters e Setters...
    public int getIdEstante() { return idEstante; }
    public void setIdEstante(int idEstante) { this.idEstante = idEstante; }
    public String getCodigoEstante() { return codigoEstante; }
    public void setCodigoEstante(String codigoEstante) { this.codigoEstante = codigoEstante; }
    public String getLocalizacao() { return localizacao; }
    public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }
    public int getCapacidadeMaxima() { return capacidadeMaxima; }
    public void setCapacidadeMaxima(int capacidadeMaxima) { this.capacidadeMaxima = capacidadeMaxima; }
    public int getExemplaresOcupados() { return exemplaresOcupados; }
    public void setExemplaresOcupados(int exemplaresOcupados) { this.exemplaresOcupados = exemplaresOcupados; }
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
}