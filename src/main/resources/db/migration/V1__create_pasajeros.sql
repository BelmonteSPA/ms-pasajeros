CREATE TABLE pasajeros (
    id_pasajero BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(150) NOT NULL,
    email       VARCHAR(150) NOT NULL UNIQUE,
    telefono    VARCHAR(20),
    empresa     VARCHAR(150),
    activo      BOOLEAN NOT NULL DEFAULT TRUE
);
