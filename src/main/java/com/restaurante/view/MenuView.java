package com.restaurante.view;

import com.restaurante.command.*;
import com.restaurante.controller.RestauranteController;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * MVC - View de mais alto nível: monta o menu e delega cada opção a um
 * Command (padrão COMMAND), que por sua vez aciona o Controller.
 */
public class MenuView {

    private final ConsoleView view = new ConsoleView();
    private final RestauranteController controller = new RestauranteController();
    private final Map<String, Command> comandos = new LinkedHashMap<>();
    private final Map<String, String> descricoes = new LinkedHashMap<>();

    public MenuView() {
        registrar("1", "Cadastrar mesa", new CadastrarMesaCommand(controller, view));
        registrar("2", "Listar mesas", new ListarMesasCommand(controller, view));
        registrar("3", "Remover mesa", new RemoverMesaCommand(controller, view));
        registrar("4", "Cadastrar produto", new CadastrarProdutoCommand(controller, view));
        registrar("5", "Listar produtos", new ListarProdutosCommand(controller, view));
        registrar("6", "Cadastrar cliente", new CadastrarClienteCommand(controller, view));
        registrar("7", "Abrir pedido (ocupar mesa)", new AbrirPedidoCommand(controller, view));
        registrar("8", "Adicionar item ao pedido", new AdicionarItemPedidoCommand(controller, view));
        registrar("9", "Fechar comanda (calcula total + libera mesa)", new FecharPedidoCommand(controller, view));
        registrar("10", "Consultar pedido por ID", new ConsultarPedidoCommand(controller, view));
        registrar("11", "Listar todos os pedidos", new ListarPedidosCommand(controller, view));
    }

    private void registrar(String opcao, String descricao, Command command) {
        comandos.put(opcao, command);
        descricoes.put(opcao, descricao);
    }

    public void iniciar() {
        String opcao;
        do {
            System.out.println("\n===== Sistema de Pedidos e Mesas para Restaurante =====");
            descricoes.forEach((k, v) -> System.out.println(k + " - " + v));
            System.out.println("0 - Sair");
            opcao = view.lerOpcao();

            if (opcao.equals("0")) {
                view.mostrarMensagem("Encerrando...");
                break;
            }
            Command comando = comandos.get(opcao);
            if (comando != null) {
                comando.executar();
            } else {
                view.mostrarErro("Opção inválida");
            }
        } while (true);
    }
}
