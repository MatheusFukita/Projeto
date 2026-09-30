package com.restaurante.command;

import com.restaurante.controller.RestauranteController;
import com.restaurante.view.ConsoleView;

public class RemoverMesaCommand implements Command {
    private final RestauranteController controller;
    private final ConsoleView view;

    public RemoverMesaCommand(RestauranteController controller, ConsoleView view) {
        this.controller = controller;
        this.view = view;
    }

    @Override
    public void executar() {
        try {
            int id = view.lerInt("ID da mesa a remover");
            controller.removerMesa(id);
            view.mostrarMensagem("Mesa removida.");
        } catch (Exception e) {
            view.mostrarErro(e.getMessage());
        }
    }
}
