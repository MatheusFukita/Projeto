package com.restaurante.command;

import com.restaurante.controller.RestauranteController;
import com.restaurante.model.Mesa;
import com.restaurante.view.ConsoleView;

public class ListarMesasCommand implements Command {
    private final RestauranteController controller;
    private final ConsoleView view;

    public ListarMesasCommand(RestauranteController controller, ConsoleView view) {
        this.controller = controller;
        this.view = view;
    }

    @Override
    public void executar() {
        for (Mesa m : controller.listarMesas()) {
            view.mostrarMensagem(m.toString());
        }
    }
}
