CREATE TABLE IF NOT EXISTS pago (
    id BIGINT NOT NULL AUTO_INCREMENT,
    idConsulta int NOT NULL,
    idPaciente VARCHAR(100) NOT NULL,
    valorConsulta DECIMAL(10, 2) NOT NULL,
    valorTratamiento DECIMAL(10, 2) NOT NULL,
    montoTotal DECIMAL(10, 2) NOT NULL,
    metodoPago VARCHAR(150) NOT NULL,
    estadoPago VARCHAR(50) NOT NULL,
    
    PRIMARY KEY (id)
);