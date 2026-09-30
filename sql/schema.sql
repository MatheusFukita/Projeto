-- Sistema de Pedidos e Mesas para Restaurante
CREATE DATABASE IF NOT EXISTS restaurante;
USE restaurante;

CREATE TABLE IF NOT EXISTS mesa (
    id INT AUTO_INCREMENT PRIMARY KEY,
    numero INT NOT NULL UNIQUE,
    capacidade INT NOT NULL,
    localizacao VARCHAR(50),
    status VARCHAR(20) NOT NULL DEFAULT 'LIVRE'
);

CREATE TABLE IF NOT EXISTS cliente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    telefone VARCHAR(20),
    cpf VARCHAR(14)
);

CREATE TABLE IF NOT EXISTS produto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    categoria VARCHAR(50),
    preco DECIMAL(10,2) NOT NULL,
    descricao VARCHAR(255),
    disponivel BOOLEAN DEFAULT TRUE,
    tempo_preparo_min INT
);

CREATE TABLE IF NOT EXISTS pedido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    mesa_id INT NOT NULL UNIQUE,               -- 1:1 pedido aberto <-> mesa
    cliente_id INT,                             -- 1:n cliente -> pedidos
    garcom VARCHAR(100),
    status VARCHAR(20) NOT NULL DEFAULT 'ABERTO',
    data_abertura DATETIME NOT NULL,
    data_fechamento DATETIME NULL,
    subtotal DECIMAL(10,2) DEFAULT 0,
    taxa_servico DECIMAL(10,2) DEFAULT 0,
    valor_total DECIMAL(10,2) DEFAULT 0,
    forma_pagamento VARCHAR(20),
    observacao VARCHAR(255),
    FOREIGN KEY (mesa_id) REFERENCES mesa(id),
    FOREIGN KEY (cliente_id) REFERENCES cliente(id)
);

CREATE TABLE IF NOT EXISTS item_pedido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    pedido_id INT NOT NULL,                     -- 1:n pedido -> itens
    produto_id INT NOT NULL,
    quantidade INT NOT NULL,
    preco_unitario DECIMAL(10,2) NOT NULL,
    observacao VARCHAR(255),
    FOREIGN KEY (pedido_id) REFERENCES pedido(id) ON DELETE CASCADE,
    FOREIGN KEY (produto_id) REFERENCES produto(id)
);
