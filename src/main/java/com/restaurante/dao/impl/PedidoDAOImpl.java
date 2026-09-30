package com.restaurante.dao.impl;

import com.restaurante.dao.PedidoDAO;
import com.restaurante.factory.ConnectionFactory;
import com.restaurante.model.*;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAOImpl implements PedidoDAO {

    private final MesaDAOImpl mesaDAO = new MesaDAOImpl();
    private final ClienteDAOImpl clienteDAO = new ClienteDAOImpl();
    private final ProdutoDAOImpl produtoDAO = new ProdutoDAOImpl();

    @Override
    public void inserir(Pedido pedido) {
        String sql = "INSERT INTO pedido (mesa_id, cliente_id, garcom, status, data_abertura, subtotal, taxa_servico, valor_total, forma_pagamento, observacao) " +
                     "VALUES (?,?,?,?,?,?,?,?,?,?)";
        try (Connection con = ConnectionFactory.criarConexao();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, pedido.getMesa().getId());
            if (pedido.getCliente() != null) ps.setInt(2, pedido.getCliente().getId());
            else ps.setNull(2, Types.INTEGER);
            ps.setString(3, pedido.getGarcom());
            ps.setString(4, pedido.getStatus().name());
            ps.setTimestamp(5, Timestamp.valueOf(pedido.getDataAbertura()));
            ps.setBigDecimal(6, pedido.getSubtotal());
            ps.setBigDecimal(7, pedido.getTaxaServico());
            ps.setBigDecimal(8, pedido.getValorTotal());
            ps.setString(9, pedido.getFormaPagamento() != null ? pedido.getFormaPagamento().name() : null);
            ps.setString(10, pedido.getObservacao());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) pedido.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir pedido", e);
        }
    }

    @Override
    public void atualizar(Pedido pedido) {
        String sql = "UPDATE pedido SET cliente_id=?, garcom=?, status=?, data_fechamento=?, subtotal=?, taxa_servico=?, valor_total=?, forma_pagamento=?, observacao=? WHERE id=?";
        try (Connection con = ConnectionFactory.criarConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (pedido.getCliente() != null) ps.setInt(1, pedido.getCliente().getId());
            else ps.setNull(1, Types.INTEGER);
            ps.setString(2, pedido.getGarcom());
            ps.setString(3, pedido.getStatus().name());
            ps.setTimestamp(4, pedido.getDataFechamento() != null ? Timestamp.valueOf(pedido.getDataFechamento()) : null);
            ps.setBigDecimal(5, pedido.getSubtotal());
            ps.setBigDecimal(6, pedido.getTaxaServico());
            ps.setBigDecimal(7, pedido.getValorTotal());
            ps.setString(8, pedido.getFormaPagamento() != null ? pedido.getFormaPagamento().name() : null);
            ps.setString(9, pedido.getObservacao());
            ps.setInt(10, pedido.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar pedido", e);
        }
    }

    @Override
    public void deletar(Integer id) {
        String sql = "DELETE FROM pedido WHERE id=?";
        try (Connection con = ConnectionFactory.criarConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar pedido", e);
        }
    }

    @Override
    public Pedido consultarPorId(Integer id) {
        String sql = "SELECT * FROM pedido WHERE id=?";
        try (Connection con = ConnectionFactory.criarConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao consultar pedido", e);
        }
        return null;
    }

    @Override
    public List<Pedido> consultarTodos() {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT * FROM pedido ORDER BY data_abertura DESC";
        try (Connection con = ConnectionFactory.criarConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar pedidos", e);
        }
        return lista;
    }

    @Override
    public Pedido consultarPedidoAbertoPorMesa(int mesaId) {
        String sql = "SELECT * FROM pedido WHERE mesa_id=? AND status='ABERTO'";
        try (Connection con = ConnectionFactory.criarConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, mesaId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao consultar pedido aberto da mesa", e);
        }
        return null;
    }

    @Override
    public void adicionarItem(Pedido pedido, ItemPedido item) {
        String sql = "INSERT INTO item_pedido (pedido_id, produto_id, quantidade, preco_unitario, observacao) VALUES (?,?,?,?,?)";
        try (Connection con = ConnectionFactory.criarConexao();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, pedido.getId());
            ps.setInt(2, item.getProduto().getId());
            ps.setInt(3, item.getQuantidade());
            ps.setBigDecimal(4, item.getPrecoUnitario());
            ps.setString(5, item.getObservacao());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) item.setId(rs.getInt(1));
            }
            pedido.adicionarItem(item);
            recalcularTotais(con, pedido);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao adicionar item ao pedido", e);
        }
    }

    private void recalcularTotais(Connection con, Pedido pedido) throws SQLException {
        BigDecimal subtotal = BigDecimal.ZERO;
        for (ItemPedido item : buscarItens(con, pedido.getId())) {
            subtotal = subtotal.add(item.getSubtotal());
        }
        BigDecimal taxa = subtotal.multiply(BigDecimal.valueOf(0.10));
        BigDecimal total = subtotal.add(taxa);
        pedido.setSubtotal(subtotal);
        pedido.setTaxaServico(taxa);
        pedido.setValorTotal(total);
        String sql = "UPDATE pedido SET subtotal=?, taxa_servico=?, valor_total=? WHERE id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBigDecimal(1, subtotal);
            ps.setBigDecimal(2, taxa);
            ps.setBigDecimal(3, total);
            ps.setInt(4, pedido.getId());
            ps.executeUpdate();
        }
    }

    private List<ItemPedido> buscarItens(Connection con, int pedidoId) throws SQLException {
        List<ItemPedido> itens = new ArrayList<>();
        String sql = "SELECT * FROM item_pedido WHERE pedido_id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, pedidoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Produto produto = produtoDAO.consultarPorId(rs.getInt("produto_id"));
                    itens.add(new ItemPedido(rs.getInt("id"), pedidoId, produto,
                            rs.getInt("quantidade"), rs.getBigDecimal("preco_unitario"), rs.getString("observacao")));
                }
            }
        }
        return itens;
    }

    private Pedido map(ResultSet rs) throws SQLException {
        Pedido pedido = new Pedido();
        pedido.setId(rs.getInt("id"));
        pedido.setMesa(mesaDAO.consultarPorId(rs.getInt("mesa_id")));
        int clienteId = rs.getInt("cliente_id");
        if (!rs.wasNull()) pedido.setCliente(clienteDAO.consultarPorId(clienteId));
        pedido.setGarcom(rs.getString("garcom"));
        pedido.setStatus(StatusPedido.valueOf(rs.getString("status")));
        pedido.setDataAbertura(rs.getTimestamp("data_abertura").toLocalDateTime());
        Timestamp fechamento = rs.getTimestamp("data_fechamento");
        if (fechamento != null) pedido.setDataFechamento(fechamento.toLocalDateTime());
        pedido.setSubtotal(rs.getBigDecimal("subtotal"));
        pedido.setTaxaServico(rs.getBigDecimal("taxa_servico"));
        pedido.setValorTotal(rs.getBigDecimal("valor_total"));
        String forma = rs.getString("forma_pagamento");
        if (forma != null) pedido.setFormaPagamento(FormaPagamento.valueOf(forma));
        pedido.setObservacao(rs.getString("observacao"));
        try (Connection con2 = ConnectionFactory.criarConexao()) {
            pedido.setItens(buscarItens(con2, pedido.getId()));
        }
        return pedido;
    }
}
