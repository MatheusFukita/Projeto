package com.restaurante.model;

import java.math.BigDecimal;

public class Produto {
    private Integer id;
    private String nome;
    private String categoria;
    private BigDecimal preco;
    private String descricao;
    private boolean disponivel;
    private int tempoPreparoMin;

    public Produto() {}

    public Produto(Integer id, String nome, String categoria, BigDecimal preco,
                    String descricao, boolean disponivel, int tempoPreparoMin) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
        this.preco = preco;
        this.descricao = descricao;
        this.disponivel = disponivel;
        this.tempoPreparoMin = tempoPreparoMin;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public boolean isDisponivel() { return disponivel; }
    public void setDisponivel(boolean disponivel) { this.disponivel = disponivel; }
    public int getTempoPreparoMin() { return tempoPreparoMin; }
    public void setTempoPreparoMin(int tempoPreparoMin) { this.tempoPreparoMin = tempoPreparoMin; }

    @Override
    public String toString() {
        return String.format("Produto #%d | %s | %s | R$ %.2f | disponivel=%s",
                id, nome, categoria, preco, disponivel);
    }
}
