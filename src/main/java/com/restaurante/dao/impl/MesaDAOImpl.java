package com.restaurante.dao.impl;

import com.restaurante.dao.MesaDAO;
import com.restaurante.factory.ConnectionFactory;
import com.restaurante.model.Mesa;
import com.restaurante.model.StatusMesa;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MesaDAOImpl implements MesaDAO {

    @Override
    public void inserir(Mesa mesa) {
        String sql = "INSERT INTO mesa (numero, capacidade, localizacao, status) VALUES (?,?,?,?)";
        try (Connection con = ConnectionFactory.criarConexao();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, mesa.getNumero());
            ps.setInt(2, mesa.getCapacidade());
            ps.setString(3, mesa.getLocalizacao());
            ps.setString(4, mesa.getStatus().name());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) mesa.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir mesa", e);
        }
    }

    @Override
    public void atualizar(Mesa mesa) {
        String sql = "UPDATE mesa SET numero=?, capacidade=?, localizacao=?, status=? WHERE id=?";
        try (Connection con = ConnectionFactory.criarConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, mesa.getNumero());
            ps.setInt(2, mesa.getCapacidade());
            ps.setString(3, mesa.getLocalizacao());
            ps.setString(4, mesa.getStatus().name());
            ps.setInt(5, mesa.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar mesa", e);
        }
    }

    @Override
    public void deletar(Integer id) {
        String sql = "DELETE FROM mesa WHERE id=?";
        try (Connection con = ConnectionFactory.criarConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar mesa", e);
        }
    }

    @Override
    public Mesa consultarPorId(Integer id) {
        String sql = "SELECT * FROM mesa WHERE id=?";
        try (Connection con = ConnectionFactory.criarConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao consultar mesa", e);
        }
        return null;
    }

    @Override
    public List<Mesa> consultarTodos() {
        List<Mesa> lista = new ArrayList<>();
        String sql = "SELECT * FROM mesa ORDER BY numero";
        try (Connection con = ConnectionFactory.criarConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar mesas", e);
        }
        return lista;
    }

    private Mesa map(ResultSet rs) throws SQLException {
        return new Mesa(
                rs.getInt("id"),
                rs.getInt("numero"),
                rs.getInt("capacidade"),
                rs.getString("localizacao"),
                StatusMesa.valueOf(rs.getString("status"))
        );
    }
}
