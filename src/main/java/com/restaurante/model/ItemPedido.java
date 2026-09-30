package com.restaurante.model;

import java.math.BigDecimal;

public class ItemPedido {
    private Integer id;
    private Integer pedidoId;
    private Produto produto;
    private int quantidade;
    private BigDecimal precoUnitario;
    private String observacao;

    public ItemPedido() {}

    public ItemPedido(Integer id, Integer pedidoId, Produto produto, int quantidade,
                       BigDecimal precoUnitario, String observacao) {
        this.id = id;
        this.pedidoId = pedidoId;
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.observacao = observacao;
    }

    public BigDecimal getSubtotal() {
        return precoUnitario.multiply(BigDecimal.valueOf(quantidade));
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getPedidoId() { return pedidoId; }
    public void setPedidoId(Integer pedidoId) { this.pedidoId = pedidoId; }
    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }
    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
    public BigDecimal getPrecoUnitario() { return precoUnitario; }
    public void setPrecoUnitario(BigDecimal precoUnitario) { this.precoUnitario = precoUnitario; }
    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }

    @Override
    public String toString() {
        return String.format("  - %dx %s (R$ %.2f cada) = R$ %.2f %s",
                quantidade, produto.getNome(), precoUnitario, getSubtotal(),
                observacao != null ? "[" + observacao + "]" : "");
    }
}
