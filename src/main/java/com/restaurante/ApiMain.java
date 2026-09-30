package com.restaurante;

import com.restaurante.api.ClienteApiHandler;
import com.restaurante.api.MesaApiHandler;
import com.restaurante.api.PedidoApiHandler;
import com.restaurante.api.ProdutoApiHandler;
import com.restaurante.controller.RestauranteController;
import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * Sobe uma API REST simples (com a HttpServer nativa do JDK — sem
 * dependências extras de framework web) para que a tela HTML
 * (restaurante-app-integrado.html) consiga ler e gravar no MESMO banco
 * MySQL usado pelo console, reaproveitando o RestauranteController.
 *
 * Executar (após "mvn clean package"):
 *   java -cp target/restaurante-app.jar com.restaurante.ApiMain
 */
public class ApiMain {

    public static void main(String[] args) throws Exception {
        int porta = 8080;
        RestauranteController controller = new RestauranteController();

        HttpServer server = HttpServer.create(new InetSocketAddress(porta), 0);
        server.createContext("/api/mesas", new MesaApiHandler(controller));
        server.createContext("/api/produtos", new ProdutoApiHandler(controller));
        server.createContext("/api/clientes", new ClienteApiHandler(controller));
        server.createContext("/api/pedidos", new PedidoApiHandler(controller));
        server.setExecutor(Executors.newFixedThreadPool(8));
        server.start();

        System.out.println("==================================================");
        System.out.println(" API do Sistema de Pedidos e Mesas no ar!");
        System.out.println(" http://localhost:" + porta + "/api/mesas");
        System.out.println(" Abra o arquivo restaurante-app-integrado.html no");
        System.out.println(" navegador para usar a tela conectada a esta API.");
        System.out.println("==================================================");
    }
}
