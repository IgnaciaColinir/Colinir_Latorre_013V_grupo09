CREATE TABLE servicio (
    id_servicio BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    descripcion VARCHAR(500) NOT NULL,
    precio INT NOT NULL,
    requiere_ayuno BOOLEAN NOT NULL
);