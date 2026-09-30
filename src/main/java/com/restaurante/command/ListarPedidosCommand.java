package com.restaurante.command;

import com.restaurante.controller.RestauranteController;
import com.restaurante.model.Pedido;
import com.restaurante.view.ConsoleView;

public class ListarPedidosCommand implements Command {
    private final RestauranteController controller;
    private final ConsoleView view;

    public ListarPedidosCommand(RestauranteController controller, ConsoleView view) {
        this.controller = controller;
        this.view = view;
    }

    @Override
    public void executar() {
        for (Pedido p : controller.listarPedidos()) {
            view.mostrarMensagem(p.toString());
            view.mostrarMensagem("----------------------------------------");
        }
    }
}
