package com.restaurante.command;

import com.restaurante.controller.RestauranteController;
import com.restaurante.view.ConsoleView;

public class CadastrarMesaCommand implements Command {
    private final RestauranteController controller;
    private final ConsoleView view;

    public CadastrarMesaCommand(RestauranteController controller, ConsoleView view) {
        this.controller = controller;
        this.view = view;
    }

    @Override
    public void executar() {
        try {
            int numero = view.lerInt("Número da mesa");
            int capacidade = view.lerInt("Capacidade (lugares)");
            String local = view.lerTexto("Localização (ex: Salão, Varanda)");
            controller.cadastrarMesa(numero, capacidade, local);
            view.mostrarMensagem("Mesa cadastrada com sucesso!");
        } catch (Exception e) {
            view.mostrarErro(e.getMessage());
        }
    }
}
