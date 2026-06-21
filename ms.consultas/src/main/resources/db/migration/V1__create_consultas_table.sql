CREATE TABLE IF NOT EXISTS consultas (
    id INT NOT NULL AUTO_INCREMENT,
    id_paciente VARCHAR(13) NOT NULL,
    nom_paciente VARCHAR(150),
    id_medico VARCHAR(50),
    nom_medico VARCHAR(150) NOT NULL,
    fecha_consulta DATE NOT NULL,
    hora_consulta TIME NOT NULL,
    diagnostico TEXT NOT NULL,
    valor_consulta DECIMAL(10, 2) NOT NULL DEFAULT 0.00, 
    valor_tratamiento DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    estado VARCHAR (50) NOT NULL,
    
    PRIMARY KEY (id),
    CONSTRAINT uq_consulta_medico_paciente UNIQUE (fecha_consulta,hora_consulta, id_medico)

);