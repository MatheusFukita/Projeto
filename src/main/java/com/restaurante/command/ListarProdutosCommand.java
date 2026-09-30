package com.restaurante.command;

import com.restaurante.controller.RestauranteController;
import com.restaurante.model.Produto;
import com.restaurante.view.ConsoleView;

public class ListarProdutosCommand implements Command {
    private final RestauranteController controller;
    private final ConsoleView view;

    public ListarProdutosCommand(RestauranteController controller, ConsoleView view) {
        this.controller = controller;
        this.view = view;
    }

    @Override
    public void executar() {
        for (Produto p : controller.listarProdutos()) {
            view.mostrarMensagem(p.toString());
        }
    }
}
