package com.restaurante.command;

import com.restaurante.controller.RestauranteController;
import com.restaurante.model.FormaPagamento;
import com.restaurante.model.Pedido;
import com.restaurante.view.ConsoleView;

public class FecharPedidoCommand implements Command {
    private final RestauranteController controller;
    private final ConsoleView view;

    public FecharPedidoCommand(RestauranteController controller, ConsoleView view) {
        this.controller = controller;
        this.view = view;
    }

    @Override
    public void executar() {
        try {
            int pedidoId = view.lerInt("ID do pedido a fechar");
            view.mostrarMensagem("Formas de pagamento: DINHEIRO, CARTAO_CREDITO, CARTAO_DEBITO, PIX");
            String formaStr = view.lerTexto("Forma de pagamento");
            FormaPagamento forma = FormaPagamento.valueOf(formaStr.trim().toUpperCase());
            Pedido pedido = controller.fecharPedido(pedidoId, forma);
            view.mostrarMensagem("Comanda fechada!\n" + pedido);
        } catch (Exception e) {
            view.mostrarErro(e.getMessage());
        }
    }
}
