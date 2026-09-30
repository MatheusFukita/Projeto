package com.restaurante.command;

import com.restaurante.controller.RestauranteController;
import com.restaurante.view.ConsoleView;

import java.math.BigDecimal;

public class CadastrarProdutoCommand implements Command {
    private final RestauranteController controller;
    private final ConsoleView view;

    public CadastrarProdutoCommand(RestauranteController controller, ConsoleView view) {
        this.controller = controller;
        this.view = view;
    }

    @Override
    public void executar() {
        try {
            String nome = view.lerTexto("Nome do produto");
            String categoria = view.lerTexto("Categoria (ex: Prato, Bebida, Sobremesa)");
            BigDecimal preco = view.lerDecimal("Preço");
            String descricao = view.lerTexto("Descrição");
            int tempoPreparo = view.lerInt("Tempo de preparo (minutos)");
            controller.cadastrarProduto(nome, categoria, preco, descricao, tempoPreparo);
            view.mostrarMensagem("Produto cadastrado com sucesso!");
        } catch (Exception e) {
            view.mostrarErro(e.getMessage());
        }
    }
}
