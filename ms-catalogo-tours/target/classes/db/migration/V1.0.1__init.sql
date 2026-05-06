CREATE TABLE tour (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    destino VARCHAR(100) NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    cupos_disponibles INT NOT NULL,
    hora_embarque TIME NOT NULL,
    fecha_tour DATE NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (id)
);

INSERT INTO tour (nombre, descripcion, destino, precio, cupos_disponibles, hora_embarque, fecha_tour)
VALUES 
('Cristo Redentor + City Tour', 'Ingreso incluido. Almuerzo buffet incluido. Guia bilingüe + Maracana', 'Rio de Janeiro', 47827.00, 20, '06:00:00', '2026-06-01'),
('Acuario + Boulevard Olimpico', 'El acuario mas grande de Sudamerica: 10.000 animales, tunel', 'Rio de Janeiro', 44148.00, 15, '06:00:00', '2026-06-01'),
('Petropolis - La Ciudad Imperial', 'Palacios imperiales, jardines europeos y almuerzo buffet incluido', 'Petropolis', 38630.00, 20, '07:00:00', '2026-06-02'),
('Buzios en Catamaran Premium', 'Party boat con DJ y musica navegando por las playas', 'Buzios', 47827.00, 15, '07:00:00', '2026-06-02'),
('Ala Delta / Parapente', 'Vuelo sobre Rio de Janeiro', 'Rio de Janeiro', 51506.00, 10, '08:00:00', '2026-06-03'),
('Pedra do Telegrafo', 'La foto mas epica de Rio', 'Rio de Janeiro', 35000.00, 15, '06:30:00', '2026-06-03'),
('Favela Tavares Bastos', 'Juego en el Campo Pele, explora la favela', 'Rio de Janeiro', 33111.00, 12, '09:00:00', '2026-06-04'),
('Ensayo Escuela de Samba', 'Noche de carnaval autentico', 'Rio de Janeiro', 40000.00, 20, '20:00:00', '2026-06-04'),
('Stand Up Paddle Amanecer', 'Rema al amanecer en Copacabana. Maximo 15 personas', 'Rio de Janeiro', 36790.00, 15, '05:30:00', '2026-06-05'),
('Dia en Rio Tour Completo', 'El tour mas completo de Rio: Cristo, Pan de Azucar, playas, Maracana', 'Rio de Janeiro', 80939.00, 20, '06:00:00', '2026-06-05'),
('Arraial do Cabo', 'Las playas mas cristalinas de Brasil. Aguas turquesas, arena blanca', 'Arraial do Cabo', 42309.00, 15, '06:00:00', '2026-06-06'),
('Tour Favela Rocinha', 'La favela mas grande de America Latina: moto + caminata, capoeira', 'Rio de Janeiro', 29432.00, 12, '09:00:00', '2026-06-06'),
('Angra dos Reis e Ilha Grande', 'Dia completo en playas e islas', 'Angra dos Reis', 36790.00, 15, '06:00:00', '2026-06-07'),
('Futbol en Brasil Maracana', 'Experiencia de partido segun fecha', 'Rio de Janeiro', 45000.00, 20, '18:00:00', '2026-06-07'),
('Favela Santa Marta', 'Tranvia tradicional, Espacio Michael Jackson y Rapidosos Furiosos', 'Rio de Janeiro', 30000.00, 12, '09:00:00', '2026-06-08'),
('Ginga Tropical Show Brasileno', 'Show nocturno a las 20:30', 'Rio de Janeiro', 55000.00, 30, '20:00:00', '2026-06-08');
