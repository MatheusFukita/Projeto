package com.restaurante.dao.impl;

import com.restaurante.dao.ClienteDAO;
import com.restaurante.factory.ConnectionFactory;
import com.restaurante.model.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAOImpl implements ClienteDAO {

    @Override
    public void inserir(Cliente c) {
        String sql = "INSERT INTO cliente (nome, telefone, cpf) VALUES (?,?,?)";
        try (Connection con = ConnectionFactory.criarConexao();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getNome());
            ps.setString(2, c.getTelefone());
            ps.setString(3, c.getCpf());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) c.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir cliente", e);
        }
    }

    @Override
    public void atualizar(Cliente c) {
        String sql = "UPDATE cliente SET nome=?, telefone=?, cpf=? WHERE id=?";
        try (Connection con = ConnectionFactory.criarConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getNome());
            ps.setString(2, c.getTelefone());
            ps.setString(3, c.getCpf());
            ps.setInt(4, c.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar cliente", e);
        }
    }

    @Override
    public void deletar(Integer id) {
        String sql = "DELETE FROM cliente WHERE id=?";
        try (Connection con = ConnectionFactory.criarConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar cliente", e);
        }
    }

    @Override
    public Cliente consultarPorId(Integer id) {
        String sql = "SELECT * FROM cliente WHERE id=?";
        try (Connection con = ConnectionFactory.criarConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao consultar cliente", e);
        }
        return null;
    }

    @Override
    public List<Cliente> consultarTodos() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM cliente ORDER BY nome";
        try (Connection con = ConnectionFactory.criarConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar clientes", e);
        }
        return lista;
    }

    private Cliente map(ResultSet rs) throws SQLException {
        return new Cliente(rs.getInt("id"), rs.getString("nome"), rs.getString("telefone"), rs.getString("cpf"));
    }
}
