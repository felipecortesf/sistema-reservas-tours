CREATE TABLE mensaje_whatsapp (
    id BIGINT NOT NULL AUTO_INCREMENT,
    telefono_remitente VARCHAR(20) NOT NULL,
    telefono_destinatario VARCHAR(20),
    nombre_remitente VARCHAR(100),
    contenido TEXT NOT NULL,
    tipo_mensaje VARCHAR(50) DEFAULT 'TEXTO',
    direccion VARCHAR(20) NOT NULL,
    estado VARCHAR(50) DEFAULT 'RECIBIDO',
    procesado BOOLEAN DEFAULT FALSE,
    fecha_mensaje DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

INSERT INTO mensaje_whatsapp (telefono_remitente, nombre_remitente, contenido, tipo_mensaje, direccion, estado, procesado)
VALUES 
('+56912345678', 'Juan Perez', 'Donde esta la van?', 'TEXTO', 'ENTRANTE', 'PROCESADO', TRUE),
('+56987654321', 'Maria Garcia', 'A que hora es el tour?', 'TEXTO', 'ENTRANTE', 'PROCESADO', TRUE);
