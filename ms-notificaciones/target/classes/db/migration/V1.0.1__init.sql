CREATE TABLE notificacion (
    id BIGINT NOT NULL AUTO_INCREMENT,
    destinatario_telefono VARCHAR(20) NOT NULL,
    destinatario_nombre VARCHAR(100) NOT NULL,
    mensaje TEXT NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    estado VARCHAR(50) DEFAULT 'PENDIENTE',
    fecha_envio DATETIME,
    reserva_id BIGINT,
    PRIMARY KEY (id)
);

INSERT INTO notificacion (destinatario_telefono, destinatario_nombre, mensaje, tipo, estado, fecha_envio, reserva_id)
VALUES 
('+56912345678', 'Juan Perez', 'Recordatorio tour mañana', 'RECORDATORIO', 'ENVIADO', NOW(), 1),
('+56987654321', 'Maria Garcia', 'Recordatorio tour mañana', 'RECORDATORIO', 'ENVIADO', NOW(), 2);
