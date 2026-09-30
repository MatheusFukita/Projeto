package com.restaurante.api.dto;

import java.math.BigDecimal;

public class CriarProdutoRequest {
    public String nome;
    public String categoria;
    public BigDecimal preco;
    public String descricao;
    public int tempoPreparoMin;
}
