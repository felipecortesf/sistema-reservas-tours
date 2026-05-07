CREATE TABLE embarque (
    id BIGINT NOT NULL AUTO_INCREMENT,
    tour_id BIGINT NOT NULL,
    tour_nombre VARCHAR(100) NOT NULL,
    fecha_embarque DATE NOT NULL,
    hora_embarque_programada TIME NOT NULL,
    hora_embarque_real TIME,
    punto_encuentro VARCHAR(200) NOT NULL,
    nombre_guia VARCHAR(100) NOT NULL,
    telefono_guia VARCHAR(20),
    estado VARCHAR(50) DEFAULT 'PROGRAMADO',
    observaciones TEXT,
    PRIMARY KEY (id)
);

INSERT INTO embarque (tour_id, tour_nombre, fecha_embarque, hora_embarque_programada, punto_encuentro, nombre_guia, telefono_guia, estado)
VALUES 
(1, 'Cristo Redentor + City Tour', '2026-06-01', '06:00:00', 'Hotel Copacabana Palace - Lobby', 'Carlos Silva', '+5521999999999', 'PROGRAMADO'),
(2, 'Acuario + Boulevard Olimpico', '2026-06-01', '06:00:00', 'Hotel Ipanema - Entrada principal', 'Ana Lima', '+5521888888888', 'PROGRAMADO');
