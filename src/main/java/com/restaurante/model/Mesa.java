package com.restaurante.model;

public class Mesa {
    private Integer id;
    private int numero;
    private int capacidade;
    private String localizacao;
    private StatusMesa status;

    public Mesa() {}

    public Mesa(Integer id, int numero, int capacidade, String localizacao, StatusMesa status) {
        this.id = id;
        this.numero = numero;
        this.capacidade = capacidade;
        this.localizacao = localizacao;
        this.status = status;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }
    public int getCapacidade() { return capacidade; }
    public void setCapacidade(int capacidade) { this.capacidade = capacidade; }
    public String getLocalizacao() { return localizacao; }
    public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }
    public StatusMesa getStatus() { return status; }
    public void setStatus(StatusMesa status) { this.status = status; }

    @Override
    public String toString() {
        return String.format("Mesa #%d | numero=%d | capacidade=%d | local=%s | status=%s",
                id, numero, capacidade, localizacao, status);
    }
}
