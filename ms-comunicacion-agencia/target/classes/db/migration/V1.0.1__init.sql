CREATE TABLE mensaje (
    id BIGINT NOT NULL AUTO_INCREMENT,
    origen VARCHAR(50) NOT NULL,
    destino VARCHAR(50) NOT NULL,
    telefono_origen VARCHAR(20),
    telefono_destino VARCHAR(20),
    contenido TEXT NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    estado VARCHAR(50) DEFAULT 'ENVIADO',
    reserva_id BIGINT,
    fecha_mensaje DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

INSERT INTO mensaje (origen, destino, telefono_origen, telefono_destino, contenido, tipo, estado, reserva_id)
VALUES 
('CLIENTE', 'KARY', '+56912345678', '+5521777777777', 'Donde esta la van?', 'CONSULTA', 'ENVIADO', 1),
('KARY', 'CLIENTE', '+5521777777777', '+56912345678', 'La van esta en camino, llega en 10 minutos', 'RESPUESTA', 'ENVIADO', 1),
('KARY', 'FELIPE', '+5521777777777', '+56999999999', 'Cliente Juan no aparece, llamarlo por favor', 'ALERTA', 'ENVIADO', 1);
