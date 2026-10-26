package com.biblioteca.model;

import java.time.LocalDate;

public class Emprestimo {
    private int idEmprestimo;
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucaoPrevista;
    private LocalDate dataDevolucaoReal;
    private EstadoEmprestimo estadoEmprestimo;
    private Pessoa pessoaRequisitante;
    private Livro livroRequisitado;

    public Emprestimo() {}

    public boolean estaAtrasado() {
        return estadoEmprestimo != EstadoEmprestimo.DEVOLVIDO 
            && LocalDate.now().isAfter(dataDevolucaoPrevista);
    }

    public void devolver() {
        this.dataDevolucaoReal = LocalDate.now();
        this.estadoEmprestimo = EstadoEmprestimo.DEVOLVIDO;
    }

    // Getters e Setters...
    public int getIdEmprestimo() { return idEmprestimo; }
    public void setIdEmprestimo(int idEmprestimo) { this.idEmprestimo = idEmprestimo; }
    public LocalDate getDataEmprestimo() { return dataEmprestimo; }
    public void setDataEmprestimo(LocalDate dataEmprestimo) { this.dataEmprestimo = dataEmprestimo; }
    public LocalDate getDataDevolucaoPrevista() { return dataDevolucaoPrevista; }
    public void setDataDevolucaoPrevista(LocalDate d) { this.dataDevolucaoPrevista = d; }
    public LocalDate getDataDevolucaoReal() { return dataDevolucaoReal; }
    public void setDataDevolucaoReal(LocalDate d) { this.dataDevolucaoReal = d; }
    public EstadoEmprestimo getEstadoEmprestimo() { return estadoEmprestimo; }
    public void setEstadoEmprestimo(EstadoEmprestimo e) { this.estadoEmprestimo = e; }
    public Pessoa getPessoaRequisitante() { return pessoaRequisitante; }
    public void setPessoaRequisitante(Pessoa p) { this.pessoaRequisitante = p; }
    public Livro getLivroRequisitado() { return livroRequisitado; }
    public void setLivroRequisitado(Livro l) { this.livroRequisitado = l; }
}