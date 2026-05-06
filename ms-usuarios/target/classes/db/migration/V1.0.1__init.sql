CREATE TABLE rol (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE usuario (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    telefono VARCHAR(20),
    rol_id BIGINT NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE usuario
    ADD CONSTRAINT fk_rol
        FOREIGN KEY (rol_id) REFERENCES rol (id);

INSERT INTO rol (nombre) VALUES ('ADMIN');
INSERT INTO rol (nombre) VALUES ('AGENTE');
INSERT INTO rol (nombre) VALUES ('CLIENTE');

INSERT INTO usuario (nombre, apellido, email, password, telefono, rol_id)
VALUES ('Admin', 'Sistema', 'admin@reservatours.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '999999999', 1);
