package com.restaurante.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidade principal do domínio (13 atributos).
 * Relacionamento 1:1 com Mesa (uma mesa tem no máximo um pedido aberto por vez).
 * Relacionamento 1:n com ItemPedido (um pedido tem vários itens).
 * Relacionamento n:1 com Cliente (um cliente pode ter vários pedidos ao longo do tempo).
 */
public class Pedido {
    private Integer id;
    private Mesa mesa;                       // 1:1
    private Cliente cliente;                 // n:1
    private String garcom;
    private List<ItemPedido> itens = new ArrayList<>(); // 1:n
    private StatusPedido status;
    private LocalDateTime dataAbertura;
    private LocalDateTime dataFechamento;
    private BigDecimal subtotal = BigDecimal.ZERO;
    private BigDecimal taxaServico = BigDecimal.ZERO;
    private BigDecimal valorTotal = BigDecimal.ZERO;
    private FormaPagamento formaPagamento;
    private String observacao;

    public Pedido() {}

    public void adicionarItem(ItemPedido item) {
        this.itens.add(item);
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Mesa getMesa() { return mesa; }
    public void setMesa(Mesa mesa) { this.mesa = mesa; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public String getGarcom() { return garcom; }
    public void setGarcom(String garcom) { this.garcom = garcom; }
    public List<ItemPedido> getItens() { return itens; }
    public void setItens(List<ItemPedido> itens) { this.itens = itens; }
    public StatusPedido getStatus() { return status; }
    public void setStatus(StatusPedido status) { this.status = status; }
    public LocalDateTime getDataAbertura() { return dataAbertura; }
    public void setDataAbertura(LocalDateTime dataAbertura) { this.dataAbertura = dataAbertura; }
    public LocalDateTime getDataFechamento() { return dataFechamento; }
    public void setDataFechamento(LocalDateTime dataFechamento) { this.dataFechamento = dataFechamento; }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    public BigDecimal getTaxaServico() { return taxaServico; }
    public void setTaxaServico(BigDecimal taxaServico) { this.taxaServico = taxaServico; }
    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }
    public FormaPagamento getFormaPagamento() { return formaPagamento; }
    public void setFormaPagamento(FormaPagamento formaPagamento) { this.formaPagamento = formaPagamento; }
    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Pedido #%d | Mesa %d | status=%s | aberto=%s%n",
                id, mesa != null ? mesa.getNumero() : -1, status, dataAbertura));
        for (ItemPedido item : itens) sb.append(item).append(System.lineSeparator());
        sb.append(String.format("Subtotal=R$ %.2f | Taxa=R$ %.2f | Total=R$ %.2f",
                subtotal, taxaServico, valorTotal));
        return sb.toString();
    }
}
