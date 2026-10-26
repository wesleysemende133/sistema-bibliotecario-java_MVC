package com.biblioteca.model;

import java.time.LocalDate;

public class Bibliotecario extends Pessoa {
    private String numeroFuncionario;
    private LocalDate dataAdmissao;

    public Bibliotecario() { super(); }
    public Bibliotecario(String nome, String email, String senha, String tel, String numFunc) {
        super(nome, email, senha, tel);
        this.numeroFuncionario = numFunc;
        this.dataAdmissao = LocalDate.now();
    }

    public String getNumeroFuncionario() { return numeroFuncionario; }
    public void setNumeroFuncionario(String numeroFuncionario) { this.numeroFuncionario = numeroFuncionario; }
    public LocalDate getDataAdmissao() { return dataAdmissao; }
    public void setDataAdmissao(LocalDate dataAdmissao) { this.dataAdmissao = dataAdmissao; }
}