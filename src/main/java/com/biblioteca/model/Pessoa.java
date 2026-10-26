package com.biblioteca.model;

import java.time.LocalDate;

public abstract class Pessoa {
    protected int idPessoa;
    protected String nomeCompleto;
    protected String email;
    protected String senhaAcesso;
    protected String telefone;
    protected LocalDate dataCriacaoConta;

    public Pessoa() { this.dataCriacaoConta = LocalDate.now(); }

    public Pessoa(String nomeCompleto, String email, String senhaAcesso, String telefone) {
        this.nomeCompleto = nomeCompleto;
        this.email = email;
        this.senhaAcesso = senhaAcesso;
        this.telefone = telefone;
        this.dataCriacaoConta = LocalDate.now();
    }

    public boolean login(String email, String senhaAcesso) {
        return this.email.equals(email) && this.senhaAcesso.equals(senhaAcesso);
    }

    public void logout() {
        System.out.println("Sessão terminada para: " + nomeCompleto);
    }

    // Getters e Setters omitidos para poupar espaco
    public int getIdPessoa() { return idPessoa; }
    public void setIdPessoa(int idPessoa) { this.idPessoa = idPessoa; }
    public String getNomeCompleto() { return nomeCompleto; }
    public void setNomeCompleto(String nomeCompleto) { this.nomeCompleto = nomeCompleto; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSenhaAcesso() { return senhaAcesso; }
    public void setSenhaAcesso(String senhaAcesso) { this.senhaAcesso = senhaAcesso; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public LocalDate getDataCriacaoConta() { return dataCriacaoConta; }
    public void setDataCriacaoConta(LocalDate dataCriacaoConta) { this.dataCriacaoConta = dataCriacaoConta; }
}