CREATE TABLE pago (
    id BIGINT NOT NULL AUTO_INCREMENT,
    reserva_id BIGINT NOT NULL,
    cliente_nombre VARCHAR(100) NOT NULL,
    cliente_telefono VARCHAR(20) NOT NULL,
    tour_nombre VARCHAR(150) NOT NULL,
    monto DECIMAL(10,2) NOT NULL,
    metodo_pago VARCHAR(50) NOT NULL,
    estado VARCHAR(50) DEFAULT 'PENDIENTE',
    fecha_pago DATETIME,
    observaciones TEXT,
    PRIMARY KEY (id)
);

INSERT INTO pago (reserva_id, cliente_nombre, cliente_telefono, tour_nombre, monto, metodo_pago, estado, fecha_pago)
VALUES
(1, 'Juan Perez', '+56912345678', 'Cristo Redentor + City Tour', 47827.00, 'TRANSFERENCIA', 'PAGADO', NOW()),
(2, 'Maria Garcia', '+56987654321', 'Acuario + Boulevard Olimpico', 44148.00, 'EFECTIVO', 'PENDIENTE', NOW());
