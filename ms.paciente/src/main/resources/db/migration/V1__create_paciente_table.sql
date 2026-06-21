CREATE TABLE IF NOT EXISTS paciente (
    rut VARCHAR(13) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    apellidos VARCHAR(150) NOT NULL,
    direccion VARCHAR(200) NOT NULL,
    fecha_nacimiento DATE NULL,
    telefono VARCHAR(20) NOT NULL,
    email VARCHAR(150) NOT NULL,
    prevision VARCHAR(50) NOT NULL,
    
    PRIMARY KEY (rut)
    );