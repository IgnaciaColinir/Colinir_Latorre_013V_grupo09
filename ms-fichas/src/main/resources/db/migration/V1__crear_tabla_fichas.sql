CREATE TABLE ficha (
    id_ficha BIGINT AUTO_INCREMENT PRIMARY KEY,
    rut_paciente VARCHAR(20) NOT NULL,
    tipo_sangre VARCHAR(10) NOT NULL,
    alergias VARCHAR(255) NOT NULL,
    antecedentes_familiares VARCHAR(500) NOT NULL
);