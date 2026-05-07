CREATE TABLE notificacion_push (
    id BIGINT NOT NULL AUTO_INCREMENT,
    titulo VARCHAR(100) NOT NULL,
    mensaje TEXT NOT NULL,
    destinatario_id BIGINT,
    destinatario_telefono VARCHAR(20),
    tipo VARCHAR(50) NOT NULL,
    estado VARCHAR(50) DEFAULT 'PENDIENTE',
    fecha_envio DATETIME,
    leida BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (id)
);

INSERT INTO notificacion_push (titulo, mensaje, destinatario_telefono, tipo, estado, fecha_envio)
VALUES
('Tour mañana', 'Recordatorio: Su tour es mañana a las 06:00', '+56912345678', 'RECORDATORIO', 'ENVIADO', NOW()),
('Embarque listo', 'Su transporte está listo en el lobby', '+56987654321', 'EMBARQUE', 'ENVIADO', NOW());
