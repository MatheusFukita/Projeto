package com.restaurante.dao;

import java.util.List;

public interface GenericDAO<T, ID> {
    void inserir(T entidade);
    void atualizar(T entidade);
    void deletar(ID id);
    T consultarPorId(ID id);
    List<T> consultarTodos();
}
