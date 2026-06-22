CREATE TABLE insumo (
    id_insumo BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    categoria VARCHAR(100) NOT NULL,
    stock_actual INT NOT NULL,
    stock_minimo INT NOT NULL
);