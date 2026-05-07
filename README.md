-- ===============================================================
-- BASE DE DATOS: AdoptaUnCompañero
-- Proyecto CFGS DAW 2025/2026
-- Diego Daniel Larrazábal Mendoza e Iván Sánchez Domínguez
-- ===============================================================
CREATE DATABASE IF NOT EXISTS adopta_un_companero;
USE adopta_un_companero;


-- Tabla de noticias
CREATE TABLE noticias (
    id_noticia       INT AUTO_INCREMENT PRIMARY KEY,
    titulo           VARCHAR(200) NOT NULL,
    subtitulo        VARCHAR(300),
    contenido        TEXT NOT NULL,
    imagen           VARCHAR(255),
    autor            VARCHAR(100),
    fecha_publicacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    publicada        BOOLEAN DEFAULT TRUE,
    INDEX idx_fecha (fecha_publicacion DESC),
    INDEX idx_publicada (publicada)
);

-- Datos de prueba de ejemplo
INSERT INTO noticias (titulo, subtitulo, contenido, autor, publicada) VALUES
('Nueva campaña de adopción de primavera',
 '¡10 nuevos animales esperan tu hogar!',
 'Esta semana han llegado al centro 10 nuevos compañeros buscando un hogar amoroso. Entre ellos hay perros, gatos y conejos de todas las edades. Todos están vacunados, desparasitados y con su microchip. Si estás pensando en adoptar, este es un momento perfecto. Acércate al centro en horario de 10:00 a 18:00 de lunes a sábado y conoce a estos peludos esperando una familia.',
 'Centro AdoptaUnCompañero', TRUE),

('Jornada solidaria de puertas abiertas',
 'Te esperamos el próximo sábado',
 'El próximo sábado celebramos una jornada de puertas abiertas en el centro. Habrá actividades para toda la familia: paseos con perros, charlas sobre cuidado animal, demostraciones de adiestramiento positivo y un photocall con los animales más fotogénicos. La entrada es libre y los donativos se destinarán íntegramente a mejorar las instalaciones del centro.',
 'Equipo de voluntarios', TRUE),

('Consejos para nuevos adoptantes',
 'Lo que debes saber antes de adoptar',
 'Adoptar un animal es una decisión que cambia tu vida y la del animal. Antes de dar el paso, es importante reflexionar sobre el tiempo que puedes dedicarle, el espacio del que dispones, los recursos económicos necesarios y el compromiso a largo plazo. Un perro vive entre 10 y 15 años, un gato puede llegar a 20 y un conejo unos 8-10 años. La adopción es para toda la vida.',
 'Veterinaria del centro', TRUE);
-- ===============================================================
-- TABLA ROLES (PDF 5 - Seguridad)
-- Define los roles del sistema: CLIENTE y ADMIN (descripción 2.2)
-- ===============================================================
CREATE TABLE roles (
    id_rol INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

-- ===============================================================
-- TABLA USUARIOS (2.2.3 cliente registrado / 2.2.4 admin)
-- NOTA: Quitamos id_rol directo (estaba en tu SQL original)
-- y usamos la tabla intermedia usuario_roles para que Spring
-- Security pueda asignar varios roles si fuese necesario,
-- siguiendo el patrón del PDF 5 (relación @OneToMany).
-- ===============================================================
CREATE TABLE usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    telefono VARCHAR(20),
    direccion VARCHAR(255),
    fecha_registro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_usuarios_email ON usuarios(email);

-- ===============================================================
-- TABLA INTERMEDIA USUARIO-ROLES (PDF 5 - Spring Security)
-- Permite asociar uno o varios roles a un mismo usuario.
-- ===============================================================
CREATE TABLE usuario_roles (
    id_usuario_rol INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    id_rol INT NOT NULL,

    CONSTRAINT fk_ur_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
        ON UPDATE CASCADE
        ON DELETE CASCADE,

    CONSTRAINT fk_ur_rol
        FOREIGN KEY (id_rol) REFERENCES roles(id_rol)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    -- Evita asignar dos veces el mismo rol al mismo usuario
    CONSTRAINT uq_usuario_rol UNIQUE (id_usuario, id_rol)
);

-- ===============================================================
-- TABLA ANIMALES (2.3.1 catálogo / 2.3.2 ficha / 2.2.4.1 CRUD)
-- ===============================================================
CREATE TABLE animales (
    id_animal INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    especie VARCHAR(50) NOT NULL,
    edad INT,
    tamano VARCHAR(50),
    personalidad TEXT,
    necesidades_especiales TEXT,
    estado_sanitario TEXT,
    estado ENUM('DISPONIBLE', 'RESERVADO', 'ADOPTADO') NOT NULL DEFAULT 'DISPONIBLE',
    fecha_alta TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_animales_especie ON animales(especie);
CREATE INDEX idx_animales_estado ON animales(estado);

-- ===============================================================
-- TABLA IMÁGENES DE ANIMALES (2.3.2.1 - PDF 6.2 archivos)
-- url_imagen guarda el nombre único (UUID) generado por
-- FileStorageService. Los archivos se sirven desde /uploads/**
-- ===============================================================
CREATE TABLE imagenes_animales (
    id_imagen INT AUTO_INCREMENT PRIMARY KEY,
    url_imagen VARCHAR(255) NOT NULL,
    id_animal INT NOT NULL,

    CONSTRAINT fk_imagenes_animales
        FOREIGN KEY (id_animal) REFERENCES animales(id_animal)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE INDEX idx_imagenes_animal ON imagenes_animales(id_animal);

-- ===============================================================
-- TABLA ESTADOS DE SOLICITUD (2.3.4.4)
-- ===============================================================
CREATE TABLE estados_solicitud (
    id_estado INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

-- ===============================================================
-- TABLA SOLICITUDES DE ADOPCIÓN (2.3.4 Proceso de adopción)
-- ===============================================================
CREATE TABLE solicitudes_adopcion (
    id_solicitud INT AUTO_INCREMENT PRIMARY KEY,
    fecha_solicitud TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    comentarios TEXT,
    id_usuario INT NOT NULL,
    id_animal INT NOT NULL,
    id_estado INT NOT NULL,

    CONSTRAINT fk_solicitudes_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT fk_solicitudes_animal
        FOREIGN KEY (id_animal) REFERENCES animales(id_animal)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT fk_solicitudes_estado
        FOREIGN KEY (id_estado) REFERENCES estados_solicitud(id_estado)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE INDEX idx_solicitudes_usuario ON solicitudes_adopcion(id_usuario);
CREATE INDEX idx_solicitudes_animal ON solicitudes_adopcion(id_animal);
CREATE INDEX idx_solicitudes_estado ON solicitudes_adopcion(id_estado);

-- Evita múltiples solicitudes del mismo usuario para el mismo animal
CREATE UNIQUE INDEX uq_usuario_animal ON solicitudes_adopcion(id_usuario, id_animal);

-- ===============================================================
-- TABLA HISTORIAL DE ESTADOS (2.2.5 - Lógica de negocio)
-- Registra cada cambio de estado de una solicitud (2.4.7 / 2.4.8).
-- ===============================================================
CREATE TABLE historial_estados_solicitud (
    id_historial INT AUTO_INCREMENT PRIMARY KEY,
    id_solicitud INT NOT NULL,
    estado_anterior INT,
    estado_nuevo INT NOT NULL,
    fecha_cambio TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    comentario_admin VARCHAR(255),

    CONSTRAINT fk_historial_solicitud
        FOREIGN KEY (id_solicitud) REFERENCES solicitudes_adopcion(id_solicitud)
        ON UPDATE CASCADE
        ON DELETE CASCADE,

    CONSTRAINT fk_historial_estado_anterior
        FOREIGN KEY (estado_anterior) REFERENCES estados_solicitud(id_estado)
        ON UPDATE CASCADE
        ON DELETE SET NULL,

    CONSTRAINT fk_historial_estado_nuevo
        FOREIGN KEY (estado_nuevo) REFERENCES estados_solicitud(id_estado)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE INDEX idx_historial_solicitud ON historial_estados_solicitud(id_solicitud);

-- ===============================================================
-- TABLA FAVORITOS (2.2.3.3 / 2.3.3.1)
-- ===============================================================
CREATE TABLE favoritos (
    id_usuario INT NOT NULL,
    id_animal INT NOT NULL,
    fecha_favorito TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_usuario, id_animal),

    CONSTRAINT fk_favoritos_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
        ON UPDATE CASCADE
        ON DELETE CASCADE,

    CONSTRAINT fk_favoritos_animal
        FOREIGN KEY (id_animal) REFERENCES animales(id_animal)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

-- ===============================================================
-- TABLA CITAS DE ADOPCIÓN (2.2.4.4)
-- ===============================================================
CREATE TABLE citas_adopcion (
    id_cita INT AUTO_INCREMENT PRIMARY KEY,
    fecha_cita DATETIME NOT NULL,
    observaciones TEXT,
    id_solicitud INT NOT NULL,

    CONSTRAINT fk_citas_solicitud
        FOREIGN KEY (id_solicitud) REFERENCES solicitudes_adopcion(id_solicitud)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE INDEX idx_citas_fecha ON citas_adopcion(fecha_cita);

-- ===============================================================
-- TABLA NOTIFICACIONES (2.2.5 - PDF 6.4 emails)
-- ===============================================================
CREATE TABLE notificaciones (
    id_notificacion INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    id_solicitud INT,
    tipo ENUM('EMAIL') NOT NULL DEFAULT 'EMAIL',
    asunto VARCHAR(150) NOT NULL,
    mensaje TEXT NOT NULL,
    enviado TINYINT(1) NOT NULL DEFAULT 0,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_envio TIMESTAMP NULL,

    CONSTRAINT fk_notificaciones_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
        ON UPDATE CASCADE
        ON DELETE CASCADE,

    CONSTRAINT fk_notificaciones_solicitud
        FOREIGN KEY (id_solicitud) REFERENCES solicitudes_adopcion(id_solicitud)
        ON UPDATE CASCADE
        ON DELETE SET NULL
);

CREATE INDEX idx_notificaciones_usuario ON notificaciones(id_usuario);
CREATE INDEX idx_notificaciones_enviado ON notificaciones(enviado);


-- ===============================================================
-- ===============================================================
-- DATOS INICIALES
-- ===============================================================
-- ===============================================================

-- ROLES (CLIENTE = id 1, ADMIN = id 2)
INSERT INTO roles (nombre) VALUES
('CLIENTE'),
('ADMIN');

-- ESTADOS DE SOLICITUD (PDF descripción 2.3.4.4)
INSERT INTO estados_solicitud (nombre) VALUES
('EN_REVISION'),
('APROBADA'),
('RECHAZADA'),
('EN_PROCESO'),
('FINALIZADA');

-- ===============================================================
-- USUARIOS DE PRUEBA
-- IMPORTANTE: la contraseña de TODOS es '1234' encriptada con
-- BCrypt (mismo hash que usa el profe en el PDF 5).
-- ===============================================================
INSERT INTO usuarios (nombre, email, password, telefono, direccion) VALUES
('Admin Centro',
 'admin@adopta.com',
 '$2a$12$ADsrcfWYwiZnhSE8fvf9ZeoqOUiYAivBNLN4pP6u/p9ZQnb37rsa6',
 '923111222',
 'Paseo de Canalejas 139, Salamanca'),

('Diego Larrazábal',
 'diego@adopta.com',
 '$2a$12$ADsrcfWYwiZnhSE8fvf9ZeoqOUiYAivBNLN4pP6u/p9ZQnb37rsa6',
 '600111222',
 'C/ Toro 25, Salamanca'),

('Iván Sánchez',
 'ivan@adopta.com',
 '$2a$12$ADsrcfWYwiZnhSE8fvf9ZeoqOUiYAivBNLN4pP6u/p9ZQnb37rsa6',
 '600333444',
 'Plaza Mayor 1, Salamanca'),

('María García',
 'maria@adopta.com',
 '$2a$12$ADsrcfWYwiZnhSE8fvf9ZeoqOUiYAivBNLN4pP6u/p9ZQnb37rsa6',
 '600555666',
 'C/ Zamora 12, Salamanca');

-- Asignación de roles
-- Admin (id 1) → ADMIN
-- Resto → CLIENTE
INSERT INTO usuario_roles (id_usuario, id_rol) VALUES
(1, 2),  -- Admin → ADMIN
(2, 1),  -- Diego → CLIENTE
(3, 1),  -- Iván  → CLIENTE
(4, 1);  -- María → CLIENTE

-- ===============================================================
-- ANIMALES DE PRUEBA
-- ===============================================================
INSERT INTO animales (nombre, especie, edad, tamano, personalidad, necesidades_especiales, estado_sanitario, estado) VALUES
('Luna',     'Perro', 3, 'Mediano', 'Cariñosa, sociable y juguetona. Le encantan los niños.',  'Ninguna',                              'Vacunada y desparasitada',         'DISPONIBLE'),
('Simba',    'Gato',  2, 'Pequeño', 'Tranquilo y muy mimoso. Se lleva bien con otros gatos.',  'Dieta especial bajo en grasas',        'Vacunado y esterilizado',          'DISPONIBLE'),
('Rocky',    'Perro', 5, 'Grande',  'Muy enérgico, necesita ejercicio diario.',                'Necesita un jardín o paseos largos',   'Sano. Castrado.',                  'DISPONIBLE'),
('Mía',      'Gato',  1, 'Pequeño', 'Curiosa, juguetona y muy cariñosa.',                       'Ninguna',                              'Vacunada',                          'DISPONIBLE'),
('Toby',     'Perro', 7, 'Pequeño', 'Adulto tranquilo, ideal para personas mayores.',           'Revisión cardíaca cada 6 meses',       'Tratamiento crónico controlado',   'RESERVADO'),
('Coco',     'Conejo',2, 'Pequeño', 'Tímido al principio, después muy cariñoso.',               'Heno fresco a diario',                 'Sano',                              'DISPONIBLE'),
('Bella',    'Perro', 4, 'Mediano', 'Obediente, ideal para familias.',                          'Ninguna',                              'Vacunada y desparasitada',         'ADOPTADO'),
('Whiskers', 'Gato',  6, 'Mediano', 'Independiente pero cariñoso con quien le gana confianza.', 'Ninguna',                              'Vacunado',                          'DISPONIBLE');

-- ===============================================================
-- SOLICITUDES DE ADOPCIÓN DE PRUEBA
-- ===============================================================
-- Diego solicita Toby (en estado EN_REVISION inicial)
INSERT INTO solicitudes_adopcion (comentarios, id_usuario, id_animal, id_estado) VALUES
('Tengo experiencia con perros mayores. Vivo en un piso tranquilo.', 2, 5, 1);

-- Iván solicita Mía y la solicitud ya está APROBADA
INSERT INTO solicitudes_adopcion (comentarios, id_usuario, id_animal, id_estado) VALUES
('Me encantaría tener una gata pequeña en casa.', 3, 4, 2);

-- María solicita Bella, ya finalizada (animal ADOPTADO)
INSERT INTO solicitudes_adopcion (comentarios, id_usuario, id_animal, id_estado) VALUES
('Familia con dos niños y jardín, busco perro tranquilo.', 4, 7, 5);

-- ===============================================================
-- HISTORIAL DE ESTADOS DE PRUEBA (2.2.5)
-- ===============================================================
-- Solicitud 1 (Diego/Toby): solo creación
INSERT INTO historial_estados_solicitud (id_solicitud, estado_anterior, estado_nuevo, comentario_admin) VALUES
(1, NULL, 1, 'Solicitud creada');

-- Solicitud 2 (Iván/Mía): EN_REVISION → APROBADA
INSERT INTO historial_estados_solicitud (id_solicitud, estado_anterior, estado_nuevo, comentario_admin) VALUES
(2, NULL, 1, 'Solicitud creada'),
(2, 1, 2,    'Aprobada tras revisión del centro');

-- Solicitud 3 (María/Bella): EN_REVISION → APROBADA → EN_PROCESO → FINALIZADA
INSERT INTO historial_estados_solicitud (id_solicitud, estado_anterior, estado_nuevo, comentario_admin) VALUES
(3, NULL, 1, 'Solicitud creada'),
(3, 1, 2,    'Aprobada por el centro'),
(3, 2, 4,    'Cita programada para entrega'),
(3, 4, 5,    'Adopción completada con éxito');

-- ===============================================================
-- FAVORITOS DE PRUEBA (2.2.3.3)
-- ===============================================================
INSERT INTO favoritos (id_usuario, id_animal) VALUES
(2, 1),  -- Diego: Luna
(2, 4),  -- Diego: Mía
(3, 2),  -- Iván:  Simba
(4, 3);  -- María: Rocky

-- ===============================================================
-- CITAS DE PRUEBA (2.2.4.4)
-- ===============================================================
INSERT INTO citas_adopcion (fecha_cita, observaciones, id_solicitud) VALUES
('2026-05-15 10:30:00', 'Primera visita al centro para conocer a Mía.', 2),
('2026-05-20 17:00:00', 'Entrega definitiva de Bella a la familia.',     3);

-- ===============================================================
-- NOTIFICACIONES DE PRUEBA (2.2.5)
-- ===============================================================
INSERT INTO notificaciones (id_usuario, id_solicitud, asunto, mensaje, enviado, fecha_envio) VALUES
(3, 2,
 'Tu solicitud ha sido aprobada',
 'Hola Iván, tu solicitud para adoptar a Mía ha sido APROBADA. Pronto te contactaremos para la cita.',
 1, NOW()),

(4, 3,
 'Adopción finalizada',
 'Hola María, la adopción de Bella se ha completado con éxito. ¡Gracias por confiar en nosotros!',
 1, NOW());
