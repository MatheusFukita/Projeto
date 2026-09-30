package com.restaurante.command;

import com.restaurante.controller.RestauranteController;
import com.restaurante.model.Pedido;
import com.restaurante.view.ConsoleView;

public class AbrirPedidoCommand implements Command {
    private final RestauranteController controller;
    private final ConsoleView view;

    public AbrirPedidoCommand(RestauranteController controller, ConsoleView view) {
        this.controller = controller;
        this.view = view;
    }

    @Override
    public void executar() {
        try {
            int mesaId = view.lerInt("ID da mesa");
            String clienteStr = view.lerTexto("ID do cliente (ou vazio para não informar)");
            Integer clienteId = clienteStr.isBlank() ? null : Integer.parseInt(clienteStr.trim());
            String garcom = view.lerTexto("Nome do garçom");
            Pedido pedido = controller.abrirPedido(mesaId, clienteId, garcom);
            view.mostrarMensagem("Pedido #" + pedido.getId() + " aberto para a mesa " + pedido.getMesa().getNumero());
        } catch (Exception e) {
            view.mostrarErro(e.getMessage());
        }
    }
}
