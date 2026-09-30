package com.restaurante.command;

import com.restaurante.controller.RestauranteController;
import com.restaurante.view.ConsoleView;

public class CadastrarClienteCommand implements Command {
    private final RestauranteController controller;
    private final ConsoleView view;

    public CadastrarClienteCommand(RestauranteController controller, ConsoleView view) {
        this.controller = controller;
        this.view = view;
    }

    @Override
    public void executar() {
        try {
            String nome = view.lerTexto("Nome do cliente");
            String telefone = view.lerTexto("Telefone");
            String cpf = view.lerTexto("CPF");
            controller.cadastrarCliente(nome, telefone, cpf);
            view.mostrarMensagem("Cliente cadastrado com sucesso!");
        } catch (Exception e) {
            view.mostrarErro(e.getMessage());
        }
    }
}
