package com.restaurante.command;

import com.restaurante.controller.RestauranteController;
import com.restaurante.view.ConsoleView;

public class AdicionarItemPedidoCommand implements Command {
    private final RestauranteController controller;
    private final ConsoleView view;

    public AdicionarItemPedidoCommand(RestauranteController controller, ConsoleView view) {
        this.controller = controller;
        this.view = view;
    }

    @Override
    public void executar() {
        try {
            int pedidoId = view.lerInt("ID do pedido");
            int produtoId = view.lerInt("ID do produto");
            int quantidade = view.lerInt("Quantidade");
            String obs = view.lerTexto("Observação (ex: sem cebola) ou vazio");
            controller.adicionarItem(pedidoId, produtoId, quantidade, obs.isBlank() ? null : obs);
            view.mostrarMensagem("Item adicionado ao pedido!");
        } catch (Exception e) {
            view.mostrarErro(e.getMessage());
        }
    }
}
