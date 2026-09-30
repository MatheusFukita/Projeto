package com.restaurante.controller;

import com.restaurante.builder.PedidoBuilder;
import com.restaurante.dao.*;
import com.restaurante.factory.DAOFactory;
import com.restaurante.model.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * MVC - Controller: coordena Model (entidades/DAOs) e View (console),
 * concentrando as regras de negócio do sistema.
 */
public class RestauranteController {

    private final MesaDAO mesaDAO = DAOFactory.criarMesaDAO();
    private final ClienteDAO clienteDAO = DAOFactory.criarClienteDAO();
    private final ProdutoDAO produtoDAO = DAOFactory.criarProdutoDAO();
    private final PedidoDAO pedidoDAO = DAOFactory.criarPedidoDAO();

    // ---------- Mesa ----------
    public void cadastrarMesa(int numero, int capacidade, String localizacao) {
        Mesa mesa = new Mesa(null, numero, capacidade, localizacao, StatusMesa.LIVRE);
        mesaDAO.inserir(mesa);
    }

    public List<Mesa> listarMesas() {
        return mesaDAO.consultarTodos();
    }

    public void removerMesa(int id) {
        mesaDAO.deletar(id);
    }

    // ---------- Produto ----------
    public void cadastrarProduto(String nome, String categoria, BigDecimal preco, String descricao, int tempoPreparoMin) {
        Produto produto = new Produto(null, nome, categoria, preco, descricao, true, tempoPreparoMin);
        produtoDAO.inserir(produto);
    }

    public List<Produto> listarProdutos() {
        return produtoDAO.consultarTodos();
    }

    // ---------- Cliente ----------
    public void cadastrarCliente(String nome, String telefone, String cpf) {
        clienteDAO.inserir(new Cliente(null, nome, telefone, cpf));
    }

    public List<Cliente> listarClientes() {
        return clienteDAO.consultarTodos();
    }

    // ---------- Pedido (fluxo principal do sistema) ----------

    /** Abre um pedido para uma mesa livre (relacionamento 1:1 mesa-pedido aberto). */
    public Pedido abrirPedido(int mesaId, Integer clienteId, String garcom) {
        Mesa mesa = mesaDAO.consultarPorId(mesaId);
        if (mesa == null) throw new IllegalArgumentException("Mesa não encontrada");
        if (mesa.getStatus() == StatusMesa.OCUPADA) throw new IllegalStateException("Mesa já está ocupada");

        Cliente cliente = clienteId != null ? clienteDAO.consultarPorId(clienteId) : null;

        Pedido pedido = new PedidoBuilder()
                .paraMesa(mesa)
                .doCliente(cliente)
                .atendidoPor(garcom)
                .abrir();

        pedidoDAO.inserir(pedido);

        mesa.setStatus(StatusMesa.OCUPADA);
        mesaDAO.atualizar(mesa);
        return pedido;
    }

    /** Adiciona um item (produto + quantidade) a um pedido aberto. Relacionamento 1:n pedido-itens. */
    public void adicionarItem(int pedidoId, int produtoId, int quantidade, String observacao) {
        Pedido pedido = pedidoDAO.consultarPorId(pedidoId);
        if (pedido == null) throw new IllegalArgumentException("Pedido não encontrado");
        if (pedido.getStatus() != StatusPedido.ABERTO) throw new IllegalStateException("Pedido não está aberto");

        Produto produto = produtoDAO.consultarPorId(produtoId);
        if (produto == null || !produto.isDisponivel()) throw new IllegalArgumentException("Produto indisponível");

        ItemPedido item = new ItemPedido(null, pedidoId, produto, quantidade, produto.getPreco(), observacao);
        pedidoDAO.adicionarItem(pedido, item);
    }

    /**
     * FUNCIONALIDADE DE AUTOMAÇÃO DE PROCESSO DE NEGÓCIO:
     * fecha a comanda calculando automaticamente subtotal + 10% de taxa de
     * serviço, registra a forma de pagamento, e libera a mesa para novos
     * clientes — tudo em uma única operação, sem intervenção manual do garçom
     * em cada etapa.
     */
    public Pedido fecharPedido(int pedidoId, FormaPagamento formaPagamento) {
        Pedido pedido = pedidoDAO.consultarPorId(pedidoId);
        if (pedido == null) throw new IllegalArgumentException("Pedido não encontrado");
        if (pedido.getStatus() != StatusPedido.ABERTO) throw new IllegalStateException("Pedido já está fechado");
        if (pedido.getItens().isEmpty()) throw new IllegalStateException("Pedido sem itens não pode ser fechado");

        pedido.setStatus(StatusPedido.FECHADO);
        pedido.setDataFechamento(LocalDateTime.now());
        pedido.setFormaPagamento(formaPagamento);
        pedidoDAO.atualizar(pedido);

        Mesa mesa = pedido.getMesa();
        mesa.setStatus(StatusMesa.LIVRE);
        mesaDAO.atualizar(mesa);

        return pedido;
    }

    public Pedido consultarPedido(int id) {
        return pedidoDAO.consultarPorId(id);
    }

    public List<Pedido> listarPedidos() {
        return pedidoDAO.consultarTodos();
    }
}
