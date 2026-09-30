package com.restaurante.command;

/**
 * COMMAND: cada ação do menu vira um objeto Command, desacoplando o
 * "invocador" (menu do console) de quem efetivamente executa a ação
 * (Controller). Facilita adicionar/remover opções de menu sem alterar
 * a estrutura de decisão (sem if/else ou switch gigante).
 */
public interface Command {
    void executar();
}
