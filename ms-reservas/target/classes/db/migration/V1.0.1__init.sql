CREATE TABLE reserva (
    id BIGINT NOT NULL AUTO_INCREMENT,
    cliente_nombre VARCHAR(100) NOT NULL,
    cliente_telefono VARCHAR(20) NOT NULL,
    cliente_email VARCHAR(100),
    tour_id BIGINT NOT NULL,
    tour_nombre VARCHAR(100) NOT NULL,
    fecha_tour DATE NOT NULL,
    hora_embarque TIME NOT NULL,
    punto_encuentro VARCHAR(200),
    nombre_guia VARCHAR(100),
    estado VARCHAR(50) DEFAULT 'CONFIRMADA',
    notificacion_enviada BOOLEAN DEFAULT FALSE,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

INSERT INTO reserva (cliente_nombre, cliente_telefono, cliente_email, tour_id, tour_nombre, fecha_tour, hora_embarque, punto_encuentro, nombre_guia, estado)
VALUES 
('Juan Perez', '+56912345678', 'juan@email.com', 1, 'Cristo Redentor + City Tour', '2026-06-01', '06:00:00', 'Hotel Copacabana Palace', 'Carlos Silva', 'CONFIRMADA'),
('Maria Garcia', '+56987654321', 'maria@email.com', 2, 'Acuario + Boulevard Olimpico', '2026-06-01', '06:00:00', 'Hotel Ipanema', 'Ana Lima', 'CONFIRMADA');
