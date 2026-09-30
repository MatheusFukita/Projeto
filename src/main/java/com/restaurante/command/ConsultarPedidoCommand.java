package com.restaurante.command;

import com.restaurante.controller.RestauranteController;
import com.restaurante.model.Pedido;
import com.restaurante.view.ConsoleView;

public class ConsultarPedidoCommand implements Command {
    private final RestauranteController controller;
    private final ConsoleView view;

    public ConsultarPedidoCommand(RestauranteController controller, ConsoleView view) {
        this.controller = controller;
        this.view = view;
    }

    @Override
    public void executar() {
        int id = view.lerInt("ID do pedido");
        Pedido pedido = controller.consultarPedido(id);
        if (pedido == null) view.mostrarErro("Pedido não encontrado");
        else view.mostrarMensagem(pedido.toString());
    }
}
