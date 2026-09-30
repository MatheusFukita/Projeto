package com.restaurante.dao.impl;

import com.restaurante.dao.ProdutoDAO;
import com.restaurante.factory.ConnectionFactory;
import com.restaurante.model.Produto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAOImpl implements ProdutoDAO {

    @Override
    public void inserir(Produto p) {
        String sql = "INSERT INTO produto (nome, categoria, preco, descricao, disponivel, tempo_preparo_min) VALUES (?,?,?,?,?,?)";
        try (Connection con = ConnectionFactory.criarConexao();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getNome());
            ps.setString(2, p.getCategoria());
            ps.setBigDecimal(3, p.getPreco());
            ps.setString(4, p.getDescricao());
            ps.setBoolean(5, p.isDisponivel());
            ps.setInt(6, p.getTempoPreparoMin());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) p.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir produto", e);
        }
    }

    @Override
    public void atualizar(Produto p) {
        String sql = "UPDATE produto SET nome=?, categoria=?, preco=?, descricao=?, disponivel=?, tempo_preparo_min=? WHERE id=?";
        try (Connection con = ConnectionFactory.criarConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getNome());
            ps.setString(2, p.getCategoria());
            ps.setBigDecimal(3, p.getPreco());
            ps.setString(4, p.getDescricao());
            ps.setBoolean(5, p.isDisponivel());
            ps.setInt(6, p.getTempoPreparoMin());
            ps.setInt(7, p.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar produto", e);
        }
    }

    @Override
    public void deletar(Integer id) {
        String sql = "DELETE FROM produto WHERE id=?";
        try (Connection con = ConnectionFactory.criarConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar produto", e);
        }
    }

    @Override
    public Produto consultarPorId(Integer id) {
        String sql = "SELECT * FROM produto WHERE id=?";
        try (Connection con = ConnectionFactory.criarConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao consultar produto", e);
        }
        return null;
    }

    @Override
    public List<Produto> consultarTodos() {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT * FROM produto ORDER BY nome";
        try (Connection con = ConnectionFactory.criarConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar produtos", e);
        }
        return lista;
    }

    private Produto map(ResultSet rs) throws SQLException {
        return new Produto(
                rs.getInt("id"), rs.getString("nome"), rs.getString("categoria"),
                rs.getBigDecimal("preco"), rs.getString("descricao"),
                rs.getBoolean("disponivel"), rs.getInt("tempo_preparo_min")
        );
    }
}
