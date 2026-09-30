package com.restaurante.model;

public class Cliente {
    private Integer id;
    private String nome;
    private String telefone;
    private String cpf;

    public Cliente() {}

    public Cliente(Integer id, String nome, String telefone, String cpf) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.cpf = cpf;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    @Override
    public String toString() {
        return String.format("Cliente #%d | %s | tel=%s | cpf=%s", id, nome, telefone, cpf);
    }
}
