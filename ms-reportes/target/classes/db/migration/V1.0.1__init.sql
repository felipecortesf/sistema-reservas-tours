CREATE TABLE reporte (
    id BIGINT NOT NULL AUTO_INCREMENT,
    tipo VARCHAR(50) NOT NULL,
    fecha_reporte DATE NOT NULL,
    total_reservas INT DEFAULT 0,
    total_embarques INT DEFAULT 0,
    total_retrasos INT DEFAULT 0,
    total_clientes_no_encontrados INT DEFAULT 0,
    resumen TEXT,
    enviado_whatsapp BOOLEAN DEFAULT FALSE,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

INSERT INTO reporte (tipo, fecha_reporte, total_reservas, total_embarques, total_retrasos, resumen)
VALUES ('DIARIO', CURDATE(), 2, 2, 0, 'Reporte del dia: 2 reservas, 2 embarques, sin retrasos');
