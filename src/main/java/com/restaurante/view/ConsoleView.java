package com.restaurante.view;

import java.math.BigDecimal;
import java.util.Scanner;

/**
 * MVC - View: única responsável por interagir com o usuário (ler/mostrar dados).
 * Não contém regra de negócio nenhuma.
 */
public class ConsoleView {

    private final Scanner sc = new Scanner(System.in);

    public void mostrarMensagem(String msg) {
        System.out.println(msg);
    }

    public void mostrarErro(String msg) {
        System.out.println("[ERRO] " + msg);
    }

    public String lerTexto(String rotulo) {
        System.out.print(rotulo + ": ");
        return sc.nextLine();
    }

    public int lerInt(String rotulo) {
        System.out.print(rotulo + ": ");
        return Integer.parseInt(sc.nextLine().trim());
    }

    public BigDecimal lerDecimal(String rotulo) {
        System.out.print(rotulo + ": ");
        return new BigDecimal(sc.nextLine().trim().replace(",", "."));
    }

    public String lerOpcao() {
        System.out.print("\n> Escolha uma opção: ");
        return sc.nextLine().trim();
    }
}
