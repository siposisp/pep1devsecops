-- Crea la extension PostGis en la base de datos si no existe
CREATE EXTENSION IF NOT EXISTS postgis;

CREATE TABLE points (
    point_id SERIAL PRIMARY KEY,
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    geom GEOMETRY(Point, 4326)
);

CREATE TABLE users (
    rut VARCHAR(20) PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    lastName VARCHAR(255) NOT NULL,
    birthDate DATE NOT NULL,
    sex VARCHAR(1) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    availability BOOLEAN NOT NULL,
    location BIGINT,
    FOREIGN KEY (location) REFERENCES points(point_id)
);

CREATE TABLE attributes (
    attribute_id BIGSERIAL PRIMARY KEY,
    Attribute VARCHAR(255) NOT NULL
);

CREATE TABLE user_attribute (
    user_attribute_id BIGSERIAL PRIMARY KEY,
    rut VARCHAR(20),
    attribute_id BIGINT,
    FOREIGN KEY (rut) REFERENCES users(rut),
    FOREIGN KEY (attribute_id) REFERENCES attributes(attribute_id)
);

CREATE TABLE institutions (
    institution_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE user_institution (
    user_institution_id BIGSERIAL PRIMARY KEY,
    rut VARCHAR(20),
    institution BIGINT,
    FOREIGN KEY (rut) REFERENCES users(rut),
    FOREIGN KEY (institution) REFERENCES institutions(institution_id)
);

CREATE TABLE emergencies (
    emergency_id BIGSERIAL PRIMARY KEY,
    status BOOLEAN NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    coordinator VARCHAR(20),
    FOREIGN KEY (coordinator) REFERENCES users(rut),
    location BIGINT,
    FOREIGN KEY (location) REFERENCES points(point_id)
);

CREATE TABLE emergency_attribute (
    emergency_attribute_id BIGSERIAL PRIMARY KEY,
    emergency BIGINT,
    attribute BIGINT,
    compatibility BOOLEAN NOT NULL,
    FOREIGN KEY (emergency) REFERENCES emergencies(emergency_id),
    FOREIGN KEY (attribute) REFERENCES attributes(attribute_id)
);

CREATE TABLE tasks (
    task_id BIGSERIAL PRIMARY KEY,
    emergency BIGINT,
    type VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    state BOOLEAN NOT NULL,
    FOREIGN KEY (emergency) REFERENCES emergencies(emergency_id)
);

CREATE TABLE task_user (
    task_user_id BIGSERIAL PRIMARY KEY,
    task BIGINT,
    rut VARCHAR(20),
    FOREIGN KEY (task) REFERENCES tasks(task_id),
    FOREIGN KEY (rut) REFERENCES users(rut)
);

CREATE TABLE rankings (
    ranking_id BIGSERIAL PRIMARY KEY,
    rut VARCHAR(20),
    task_id BIGINT,
    value INT,
    FOREIGN KEY (rut) REFERENCES users(rut),
    FOREIGN KEY (task_id) REFERENCES tasks(task_id)
);

--Indexes

-- User
CREATE INDEX idx_user_rut ON users (rut);
CREATE INDEX idx_user_email ON users (email);
CREATE INDEX idx_user_disponibilidad ON users (availability);

-- Attribute
CREATE INDEX idx_attribute_id ON attributes (attribute_id);

-- user_attribute
CREATE INDEX idx_user_attribute_rut ON user_attribute (rut);
CREATE INDEX idx_user_attribute_idAttribute ON user_attribute (attribute_id);

-- Institution
CREATE INDEX idx_institution_id ON institutions (institution_id);

-- user_institution
CREATE INDEX idx_user_institution_rut ON user_institution (rut);
CREATE INDEX idx_user_institution_idInstitution ON user_institution (institution);

-- Emergency
CREATE INDEX idx_emergency_rut ON emergencies (coordinator);

-- emergency_attribute
CREATE INDEX idx_emergency_attribute_emergency_id ON emergency_attribute (emergency);
CREATE INDEX idx_emergency_attribute_idAttribute ON emergency_attribute (attribute);

-- Task
CREATE INDEX idx_task_emergency_id ON tasks (emergency);

-- Ranking
CREATE INDEX idx_ranking_rut ON rankings (rut);
CREATE INDEX idx_ranking_idTask ON rankings (task_id);

-- Points
CREATE INDEX idx_points_id ON points (point_id);
CREATE INDEX idx_points_lat ON points (latitude);
CREATE INDEX idx_points_lon ON points (longitude);





-- Point Trigger
CREATE OR REPLACE FUNCTION generate_point_from_lat_lng()
RETURNS TRIGGER AS $$
BEGIN
    NEW.geom := ST_SetSRID(ST_MakePoint(NEW.latitude, NEW.longitude), 4326);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER insert_point_trigger
BEFORE INSERT ON points
FOR EACH ROW
EXECUTE FUNCTION generate_point_from_lat_lng();

CREATE OR REPLACE TRIGGER update_point_trigger
BEFORE UPDATE ON points
FOR EACH ROW
WHEN (NEW.latitude <> OLD.latitude OR NEW.longitude <> OLD.longitude)
EXECUTE FUNCTION generate_point_from_lat_lng();
-----------------------------------------------------------------------------------------------------------------

-- User triggers

-- Audit trigger
CREATE TABLE user_audit_trigger (
    trigger_id SERIAL PRIMARY KEY,
    rut VARCHAR(20),
    name VARCHAR(255),
    lastname VARCHAR(255),
    email VARCHAR(255),
    password VARCHAR(255),
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    role VARCHAR(255),
    operation TEXT
);

-- Modify user_audit_trigger_function
CREATE OR REPLACE FUNCTION user_audit_trigger_function()
RETURNS TRIGGER AS $$
BEGIN
    -- Insert into user_audit_trigger
    INSERT INTO user_audit_trigger (rut, name, email, lastname, password, role, date, operation)
    VALUES (NEW.rut, NEW.name, NEW.lastname, NEW.email, NEW.password, NEW.role, CURRENT_TIMESTAMP, TG_OP);

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER user_audit_trigger
BEFORE INSERT OR UPDATE OR DELETE ON Users
FOR EACH ROW
EXECUTE FUNCTION user_audit_trigger_function();

-- Trigger to prevent users from having duplicated attributes
CREATE OR REPLACE FUNCTION prevent_user_duplicate_attributes_func()
RETURNS TRIGGER AS $$
BEGIN
    IF (SELECT COUNT(*) FROM user_attribute WHERE rut = NEW.rut AND attribute_id = NEW.attribute_id) > 0 THEN
        RAISE EXCEPTION 'Un usuario no puede tener atributos duplicados.';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER prevent_user_duplicate_attributes
BEFORE INSERT ON user_attribute
FOR EACH ROW
EXECUTE FUNCTION prevent_user_duplicate_attributes_func();

-- Trigger to prevent users from having duplicated institutions
CREATE OR REPLACE FUNCTION prevent_user_duplicate_institutions_func()
RETURNS TRIGGER AS $$
BEGIN
    IF (SELECT COUNT(*) FROM user_institution WHERE rut = NEW.rut AND institution = NEW.institution) > 0 THEN
        RAISE EXCEPTION 'Un usuario no puede tener instituciones duplicadas.';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER prevent_user_duplicate_institutions
BEFORE INSERT ON user_institution
FOR EACH ROW
EXECUTE FUNCTION prevent_user_duplicate_institutions_func();
-----------------------------------------------------------------------------------------------------------------

-- Emergency triggers

-- Audit trigger
CREATE TABLE emergency_audit_trigger (
    trigger_id SERIAL PRIMARY KEY,
    emergency_id BIGINT,
    status BOOLEAN,
    title VARCHAR(255),
    description TEXT,
    coordinator VARCHAR(20),
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    operation TEXT
);

CREATE OR REPLACE FUNCTION emergency_audit_trigger_function()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO emergency_audit_trigger (emergency_id, status, title, description, coordinator, date, operation)
    VALUES (NEW.emergency_id, NEW.status, NEW.title, NEW.description, NEW.coordinator, CURRENT_TIMESTAMP, TG_OP);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER emergency_audit_trigger
BEFORE INSERT OR UPDATE OR DELETE ON Emergencies
FOR EACH ROW
EXECUTE FUNCTION emergency_audit_trigger_function();

-- Trigger to prevent emergencies from having duplicated attributes
CREATE OR REPLACE FUNCTION prevent_emergency_duplicate_attributes_func()
RETURNS TRIGGER AS $$
BEGIN
    IF (SELECT COUNT(*) FROM emergency_attribute WHERE emergency = NEW.emergency AND attribute = NEW.attribute) > 0 THEN
        RAISE EXCEPTION 'Una emergencia no puede tener atributos duplicados.';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER prevent_emergency_duplicate_attributes
BEFORE INSERT ON emergency_attribute
FOR EACH ROW
EXECUTE FUNCTION prevent_emergency_duplicate_attributes_func();









-- Reporte de cantidad de operaciones de inserción, actualización y eliminación de usuarios
-- Genera el reporte a partir del voluntario y coordinator que que tenga mas de cada tipo de operation.


CREATE OR REPLACE PROCEDURE get_user_operations_report()
AS $$

DECLARE
    user_volunteer RECORD;
    user_coordinator RECORD;
BEGIN
    -- Get the volunteer user with the most insert operations
    RAISE NOTICE 'Voluntario con más operationes de inserción:';
    FOR user_volunteer IN
        SELECT rut, name, lastName, COUNT(*)
        FROM User_auditTrigger
        WHERE operation = 'INSERT'
        AND role = 'VOLUNTEER'
        GROUP BY rut
        ORDER BY COUNT(*) DESC
        LIMIT 1
    LOOP
        RAISE NOTICE 'Rut: %, Nombre: %, Apellido: %, Cantidad: %', user_volunteer.rut, user_volunteer.name, user_volunteer.lastName, user_volunteer.count;
    END LOOP;

    -- Get the volunteer user with the most update operations
    RAISE NOTICE 'Voluntario con más operationes de actualización:';
    FOR user_volunteer IN
        SELECT rut, name, lastName, COUNT(*)
        FROM User_auditTrigger
        WHERE operation = 'UPDATE'
        AND role = 'VOLUNTEER'
        GROUP BY rut
        ORDER BY COUNT(*) DESC
        LIMIT 1
    LOOP
        RAISE NOTICE 'Rut: %, Nombre: %, Apellido: %, Cantidad: %', user_volunteer.rut, user_volunteer.name, user_volunteer.lastName, user_volunteer.count;
    END LOOP;

    -- Get the volunteer user with the most delete operations
    RAISE NOTICE 'Voluntario con más operationes de eliminación:';
    FOR user_volunteer IN
        SELECT rut, name, lastName, COUNT(*)
        FROM User_auditTrigger
        WHERE operation = 'DELETE'
        AND role = 'VOLUNTEER'
        GROUP BY rut
        ORDER BY COUNT(*) DESC
        LIMIT 1
    LOOP
        RAISE NOTICE 'Rut: %, Nombre: %, Apellido: %, Cantidad: %', user_volunteer.rut, user_volunteer.name, user_volunteer.lastName, user_volunteer.count;
    END LOOP;

    -- Get the coordinator user with the most insert operations
    RAISE NOTICE 'Coordinator con más operationes de inserción:';
    FOR user_coordinator IN
        SELECT rut, name, lastName, COUNT(*)
        FROM User_auditTrigger
        WHERE operation = 'INSERT'
        AND role = 'COORDINATOR'
        GROUP BY rut
        ORDER BY COUNT(*) DESC
        LIMIT 1
    LOOP
        RAISE NOTICE 'Rut: %, Nombre: %, Apellido: %, Cantidad: %', user_coordinator.rut, user_coordinator.name, user_coordinator.lastName, user_coordinator.count;
    END LOOP;

    -- Get the coordinator user with the most update operations
    RAISE NOTICE 'Coordinator con más operationes de actualización:';
    FOR user_coordinator IN
        SELECT rut, name, lastName, COUNT(*)
        FROM User_auditTrigger
        WHERE operation = 'UPDATE'
        AND role = 'COORDINATOR'
        GROUP BY rut
        ORDER BY COUNT(*) DESC
        LIMIT 1
    LOOP
        RAISE NOTICE 'Rut: %, Nombre: %, Apellido: %, Cantidad: %', user_coordinator.rut, user_coordinator.name, user_coordinator.lastName, user_coordinator.count;
    END LOOP;

    -- Get the coordinator user with the most delete operations
    RAISE NOTICE 'Coordinator con más operationes de eliminación:';
    FOR user_coordinator IN
        SELECT rut, name, lastName, COUNT(*)
        FROM User_auditTrigger
        WHERE operation = 'DELETE'
        AND role = 'COORDINATOR'
        GROUP BY rut
        ORDER BY COUNT(*) DESC
        LIMIT 1
    LOOP
        RAISE NOTICE 'Rut: %, Nombre: %, Apellido: %, Cantidad: %', user_coordinator.rut, user_coordinator.name, user_coordinator.lastName, user_coordinator.count;
    END LOOP;

END;
$$ LANGUAGE plpgsql;

-- Para llamarlo dentro directamente en la base de dato ejecute: CALL get_user_operations_report();


-- Procedimiento almacenado para el reporte de cantidad e actulaizaciones, inserciones y eliminaciones de emergencia 
-- Genera el reporte a partir del coordinador que que tenga mas de cada tipo de operation.
CREATE OR REPLACE PROCEDURE get_coordinatorUser_operations()
AS $$

DECLARE
    coordinator_maxCount_insert INT;
    coordinator_maxCount_insert_id VARCHAR(20);
    coordinator_maxCount_update INT;
    coordinator_maxCount_update_id VARCHAR(20);
    coordinator_maxCount_delete INT;
    coordinator_maxCount_delete_id VARCHAR(20);
BEGIN
    -- Contar operationes de inserción por coordinatores
    SELECT COUNT(*), coordinator INTO coordinator_maxCount_insert, coordinator_maxCount_insert_id
    FROM Emergency_auditTrigger
    WHERE operation = 'INSERT'
    GROUP BY coordinator
    ORDER BY COUNT(*) DESC
    LIMIT 1;

   -- Count update operations by coordinators
    SELECT COUNT(*), coordinator INTO coordinator_maxCount_update, coordinator_maxCount_update_id
    FROM Emergency_auditTrigger
    WHERE operation = 'UPDATE'
    GROUP BY coordinator
    ORDER BY COUNT(*) DESC
    LIMIT 1;

    -- Contar operationes de eliminación por coordinatores
    SELECT COUNT(*), coordinator INTO coordinator_maxCount_delete, coordinator_maxCount_delete_id
    FROM Emergency_auditTrigger
    WHERE operation = 'DELETE'
    GROUP BY coordinator
    ORDER BY COUNT(*) DESC
    LIMIT 1;

    -- Mostrar resultados
    RAISE NOTICE 'Coordinador con más operationes de inserción de emergencias: ID = %, Cantidad = %', coordinator_maxCount_insert_id, coordinator_maxCount_insert;
    RAISE NOTICE 'Coordinador con más operationes de actualización de emergencias: ID = %, Cantidad = %', coordinator_maxCount_update_id, coordinator_maxCount_update;
    RAISE NOTICE 'Coordinador con más operationes de eliminación de emergencias: ID = %, Cantidad = %', coordinator_maxCount_delete_id, coordinator_maxCount_delete;

END;
$$ LANGUAGE plpgsql;


--Para llamar al procedimiento: CALL get_coordinatorUser_operations();














-- Poblacion de la tabla Voluntarios
-- Las contraseñas son el primer nombre en 'minuscula'+'primeros 3 digitos del rut'
INSERT INTO users (rut, email, name, lastname, birthdate, sex, password, role, availability)
VALUES
    ('123271472-1', 'correo.ejemplo@gmail.com', 'Elsa', 'Capuntas', '1997-06-22', 'F', '$2a$10$JWCTjsZvnbhrh6Y99lSJuubMITj1ykX.0Rn6oysAApoteY20fbqC.', 'VOLUNTEER', True),
    ('176271472-1', 'correo.ejemplo@gmail.com', 'Elivs', 'Tec', '1997-12-22', 'M', '$2a$10$32.WMcUybLU1ykwDaGmJZ.dAR6pkafizvkbl0wSP/VZ7wEqPFUOGS', 'VOLUNTEER', True),
    ('182473567-4', 'correo.ejemplo@gmail.com', 'Pedro', 'Perez', '1999-10-31', 'M', '$2a$10$YsmKIfYGUgTmknrsEbJOz.5acNKnWmVM2NDbhuC7mizwANcmMUoLC', 'VOLUNTEER', True),
    ('2421283874-4', 'maria.ejemplo@gmail.com', 'Maria', 'Perez', '1978-11-30', 'F', '$2a$10$B0xQF1r1iCK0wnnlulKTI.q7qgBYYXABtJ8spQhACj2fehRLRJE76', 'VOLUNTEER', True),
    ('3641746726-4', 'correo.ejemplo@gmail.com', 'Juan', 'SuperApellido', '2000-12-22', 'M', '$2a$10$0XtHEGIFsrRvSz5YMQsi8ORaoBS4jVHprwNwOWNKeKeONeAQB7VG2', 'VOLUNTEER', True),
    ('4152351623-5', 'correo.ejemplo@gmail.com', 'Aitor', 'Tilla', '2000-03-14', 'M', '$2a$10$lUQZmfWqqMrMIvigmrUNhuumirqf7CC4VNNbSAI/rAT4QLjuXAchC', 'VOLUNTEER', True),
    ('42163612342-5', 'correo.ejemplo@gmail.com', 'Elena', 'Nito', '2001-06-11', 'F', '$2a$10$HWFPIWz0aAauCewQoIHxw.hXEXZLvxb60FTzp7qlIcaEc.AmWjiu6', 'VOLUNTEER', True),
    ('4412317123-k', 'correo.ejemplo@gmail.com', 'Armando', 'Casas', '2002-07-12', 'M', '$2a$10$cxY0Zed1Fwpoc3E/UDjtt.LP9QVIvcuPgt9L6j3Xnyd5BtJovwSMi', 'VOLUNTEER', True),
    ('5872873212-4', 'correo.ejemplo@gmail.com', 'Susana', 'Oria', '2000-11-26', 'F', '$2a$10$6n5aRdvxOl3HXAig7L6bBeYUVxkpjfBVRUCvmG55OjlCz3/0o/Ila', 'VOLUNTEER', True),
    ('61523512412-5', 'correo.ejemplo@gmail.com', 'Helen', 'Chufe', '2000-02-16', 'F', '$2a$10$Ak8qBb.FCXhfOE/7RMx.eeRJlDRjEuauwt2MldRoI2VpSHLeJ/jQG', 'VOLUNTEER', False),
    ('65265412312-k', 'correo.ejemplo@gmail.com', 'Elmer', 'Curio', '2001-10-12', 'M', '$2a$10$zIal5qkGG2U.Qx8Idaahwe4OC9lDdsOJ52J3Z3eOqFcyYzE5dTJBC', 'VOLUNTEER', False),
    ('6527442312-k', 'correo.ejemplo@gmail.com', 'Elsa', 'Pallo', '2002-07-12', 'F', '$2a$10$1vqWoEX6ErVvBHW6.06eAOEjzvJP2GqLU3cfge1To/S5giNEyDYwy', 'VOLUNTEER', False),
    ('6717263715312-k', 'correo.ejemplo@gmail.com', 'Alan', 'Brito Delgado', '2001-02-22', 'M', '$2a$10$ZT.iqWL.n6yfJPz1d/N4LO3I5KXfwZZoDTrc.9LCPS2qPENZY5T16', 'VOLUNTEER', False),
    ('7123674212-5', 'correo.ejemplo@gmail.com', 'Armando', 'Sillas', '2000-04-19', 'M', '$2a$10$5DEJ9nasG5MnUUlgPda6Uuy3hyg1vtD8XC/ytGzOkMrB7EvIcnbdi', 'VOLUNTEER', True),
    ('7126371263-k', 'correo.ejemplo@gmail.com', 'Mario', 'Neta', '2000-05-20', 'M', '$2a$10$zWYg2zgjSDE/zYkuWt35eOrSSyiAtCheIEREdkWt72T8JIHp4J9du', 'VOLUNTEER', True),
    ('71264646674-4', 'correo.ejemplo@gmail.com', 'Aquiles', 'Baeza', '2001-11-13', 'M', '$2a$10$cVP6o0eDpTRhElUMTbJrKeR9AFAM3vtk.xogRrSn1BVHd0Wn/rf/C', 'VOLUNTEER', True),
    ('7126476122-4', 'correo.ejemplo@gmail.com', 'Eustacio', 'Perez', '2001-11-13', 'M', '$2a$10$mIkd1hzL4rGbfyoyByaVDuUYNYBPPxZzPrwlGT7EqPui8ZWA7WjZW', 'VOLUNTEER', True),
    ('7146176122-4', 'correo.ejemplo@gmail.com', 'Clementia', 'Del Rosario', '2000-11-16', 'F', '$2a$10$dUJK89Pao.vGR7hjPezggOapdLwaFqpxnvB8VGO3za3bu4mUIXcHG', 'VOLUNTEER', True),
    ('7723674212-5', 'correo.ejemplo@gmail.com', 'Lola', 'Mento', '2001-03-09', 'F', '$2a$10$0v1Rv1KQRZ4OHKSLSy4JUuYNfMA1/yAvtBndD4JlPBkwhseoKd/Ua', 'VOLUNTEER', True),
    ('81723817246-1', 'correo.ejemplo@gmail.com', 'Zacaria', 'Flores del Campo', '2002-11-26', 'F', '$2a$10$n47WKpnIBCC7zoYVAnDviOaO5MUQiUifwqoaXThAO3/F8a.sIFiFy', 'VOLUNTEER', True),
    ('876327463-4', 'correo.ejemplo@gmail.com', 'Diego', 'SuperApellido', '2000-12-22', 'M', '$2a$10$xYPusc0RS.yb56gjNZeLSOEKbisdFD4uoU8cuWemtFzk5iPHkyPha', 'COORDINATOR', False),
    ('881263612-k', 'correo.ejemplo@gmail.com', 'Esteban', 'Dido', '1999-05-20', 'M', '$2a$10$2xuw5OvwVsQ3gaDihcsfyOivRa2X1dALtlKv6pi90mfMQwnSqHlb2', 'COORDINATOR', True),
    ('948128124-1', 'correo.ejemplo@gmail.com', 'Aquiles', 'Brinco', '2000-11-26', 'F', '$2a$10$oh4r.6xrChGbToX2B3yS6OwkYdSbAdP7IPnuo2PMmzfhlq0UYQ5cG', 'COORDINATOR', True),
    ('98482714-4', 'correo.ejemplo@gmail.com', 'Elba', 'Calao', '1994-04-20', 'F', '$2a$10$Y0ntA2TXhIKuTIcrtTWOm.uyC7dvYjiNLL5cJsBkMWb1clYQnbVgS', 'COORDINATOR', True);

--Poblacion de la tabla Atributo
INSERT INTO attributes (attribute)
VALUES 
    ('Fuerza fisica'),
    ('Rescate en Terreno'),
    ('Apoyo psicologico'),
    ('Manejo de herramientas de rescate'),
    ('Conocimientos en primeros auxilios'),
    ('Manejo de equipos de comunicacion'),
    ('Capacidad para la búsqueda y localización de personas perdidas');

--Poblacion de la tabla Voluntario_Atributo
INSERT INTO user_attribute (rut, attribute_id)
VALUES 
    ('3641746726-4', '1'),
    ('3641746726-4', '2'),
    ('176271472-1', '3'),
    ('176271472-1', '4'),
    ('182473567-4', '5'),
    ('182473567-4', '6'),
    ('948128124-1', '7'),
    ('948128124-1', '1'),
    ('81723817246-1', '4'),
    ('81723817246-1', '1'),
    ('71264646674-4', '6'),
    ('71264646674-4', '7'),
    ('5872873212-4', '1'),
    ('5872873212-4', '2'),
    ('123271472-1', '7'),
    ('123271472-1', '3'),
    ('876327463-4', '1'),
    ('876327463-4', '2'),
    ('7126476122-4', '7'),
    ('7126476122-4', '3');

--Poblacion de la tabla Institucion
INSERT INTO institutions (name) 
VALUES 
    ('Cruz Roja de Chile'),
    ('Bomberos Municipales'),
    ('Equipo de rescate de desastres naturales');

-- Poblacion de la tabla user_institution
INSERT INTO user_institution (rut, institution)
VALUES 
    ('881263612-k', '1'),
    ('948128124-1', '2'),
    ('98482714-4', '3');


--Poblacion de la tabla Emergencia
INSERT INTO emergencies (status, title, description, coordinator)
VALUES 
    (true, 'Incendio forestal en zona rural', 'Se ha reportado un incendio forestal en la zona de la Reserva Nacional. Se necesita asistencia inmediata.', '881263612-k'),
    (false, 'Evacuación por fuga de gas', 'Se ha detectado una fuga de gas en un edificio residencial. Se requiere evacuación de los residentes.', '881263612-k'),
    (true, 'Inundación en área urbana', 'Las fuertes lluvias han provocado inundaciones en varios sectores de la ciudad. Se necesitan equipos de rescate.', '948128124-1'),
    (true, 'Accidente de tráfico múltiple', 'Se ha producido un accidente de tráfico en la autopista principal. Varios vehículos están involucrados.', '948128124-1'),
    (false, 'Deslizamiento de tierra en carretera', 'Un deslizamiento de tierra ha bloqueado una carretera importante. Se necesita ayuda para despejar la vía.', '98482714-4');

--Poblacion de tabla
-- Población de la tabla Emergencia_Atributo
INSERT INTO emergency_attribute (emergency, attribute, compatibility)
VALUES 
    (1, 1, true),   -- La emergencia 1 tiene compatibilidad con el atributo 1 (Fuerza física)
    (1, 2, true),   -- La emergencia 1 tiene compatibilidad con el atributo 2 (Rescate en Terreno)
    (1, 3, false),  -- La emergencia 1 no tiene compatibilidad con el atributo 3 (Apoyo psicológico)
    (2, 4, false),  -- La emergencia 2 no tiene compatibilidad con el atributo 4 (Manejo de herramientas de rescate)
    (2, 5, true),   -- La emergencia 2 tiene compatibilidad con el atributo 5 (Conocimientos en primeros auxilios)
    (3, 6, true),   -- La emergencia 3 tiene compatibilidad con el atributo 6 (Manejo de equipos de comunicación)
    (3, 7, true),   -- La emergencia 3 tiene compatibilidad con el atributo 7 (Capacidad para la búsqueda y localización de personas perdidas)
    (4, 1, true),   -- La emergencia 4 tiene compatibilidad con el atributo 1 (Fuerza física)
    (4, 2, false),  -- La emergencia 4 no tiene compatibilidad con el atributo 2 (Rescate en Terreno)
    (5, 6, false), -- La emergencia 5 no tiene compatibilidad con el atributo 6 (Manejo de equipos de comunicación)
    (5, 7, true);  -- La emergencia 5 tiene compatibilidad con el atributo 7 (Capacidad para la búsqueda y localización de personas perdidas)

-- Población de la tabla Tarea
INSERT INTO tasks (emergency, type, description, state)
VALUES 
    (1, 'Evacuación de residentes', 'Coordinar y ejecutar la evacuación de los residentes afectados por el incendio forestal.', true),
    (2, 'Control de fuga de gas', 'Gestionar la contención y control de la fuga de gas en el edificio residencial.', false),
    (3, 'Rescate de personas atrapadas', 'Realizar operaciones de rescate para ayudar a las personas atrapadas por la inundación.', true),
    (4, 'Atención médica a heridos', 'Brindar atención médica a los heridos en el accidente de tráfico múltiple.', true),
    (5, 'Despeje de la carretera', 'Organizar y llevar a cabo el despeje de la carretera bloqueada por el deslizamiento de tierra.', false),
    (1, 'Suministro de alimentos', 'Distribuir alimentos y agua potable a los evacuados.', true),
    (2, 'Apoyo psicológico a afectados', 'Brindar apoyo emocional y asistencia psicológica a las personas afectadas por la fuga de gas.', false);

INSERT INTO task_user (task, rut)
VALUES 
    (1, '123271472-1'),
    (2, '176271472-1'),
    (3, '182473567-4'),
    (4, '2421283874-4'),
    (5, '3641746726-4'),
    (1, '4152351623-5'),
    (2, '42163612342-5'),
    (3, '4412317123-k'),
    (4, '5872873212-4'),
    (5, '61523512412-5');

-- Población de la tabla Ranking
INSERT INTO rankings (rut, task_id, value)
VALUES 
    ('123271472-1', 1, 4),   -- Juan participó en la evacuación de residentes del incendio forestal y recibió un valor de ranking de 4.
    ('176271472-1', 2, 3),   -- María estuvo a cargo del control de la fuga de gas y recibió un valor de ranking de 3.
    ('182473567-4', 3, 5),   -- Carlos participó en el rescate de personas atrapadas por la inundación y recibió un valor de ranking de 5.
    ('2421283874-4', 4, 4),   -- Ana brindó atención médica a los heridos en el accidente de tráfico múltiple y recibió un valor de ranking de 4.
    ('3641746726-4', 5, 2),   -- Pedro ayudó en el despeje de la carretera bloqueada por el deslizamiento de tierra y recibió un valor de ranking de 2.
    ('4152351623-5', 1, 3),   -- Laura participó en la evacuación de residentes del incendio forestal y recibió un valor de ranking de 3.
    ('42163612342-5', 2, 4),   -- Diego estuvo a cargo del control de la fuga de gas y recibió un valor de ranking de 4.
    ('4412317123-k', 3, 5),   -- Sofía participó en el rescate de personas atrapadas por la inundación y recibió un valor de ranking de 5.
    ('5872873212-4', 4, 4),   -- Miguel brindó atención médica a los heridos en el accidente de tráfico múltiple y recibió un valor de ranking de 4.
    ('61523512412-5', 5, 3);-- Paula ayudó en el despeje de la carretera bloqueada por el deslizamiento de tierra y recibió un valor de ranking de 3.


--Poblacion de la tabla Punto
INSERT INTO points (longitude, latitude)
VALUES 

-- Sueltas por Chile (principalmente La Serena)

    (-33.51783, -70.68525),
    (-33.00139, -71.42378),
    (-31.85485, -71.15550),
    (-29.867673, -71.241257),
    (-29.91469, -71.24675),
    (-29.921754, -71.212340),
    (-29.838239, -71.270828),
    (-43.551452, -72.336967),

-- Osorno
    (-40.583746, -73.116731),
    (-40.579751, -73.133199),
    (-40.578882, -73.127024),
    (-40.571412, -73.121077),
    (-40.574713, -73.174370),
    (-40.587741, -73.145779),
    (-40.592431, -73.174370),
    (-40.570543, -73.124736),
    (-40.565330, -73.111699),
    (-40.597468, -73.108497),

-- Concepción
    (-36.824599, -73.045838), 
    (-36.805786, -73.050785),
    (-36.796732, -73.090361),
    (-36.791214, -73.050608),
    (-36.823892, -73.021632),
    (-36.781875, -73.099549),
    (-36.823043, -72.994070),
    (-36.817810, -73.050431),
    (-36.826437, -73.049195),
    (-36.829549, -73.050431),
    (-36.779420, -73.034353),
    (-36.833650, -73.056792),
    (-36.833084, -73.052022),
    (-36.831104, -73.047958),
    (-36.782724, -73.034177),

-- Talcahuano
    (-36.730018, -73.116741),
    (-36.720588, -73.121099),
    (-36.723430, -73.113296),
    (-36.722524, -73.113296),
    (-36.712703, -73.134971),
    (-36.721390, -73.125830),
    (-36.722524, -73.119704),
    (-36.723657, -73.110799),
    (-36.722863, -73.113107),
    (-36.719351, -73.121589),
    (-36.712665, -73.120835);

-- Agregar ubicaciones a los usuarios
UPDATE users SET location = 1 WHERE rut = '123271472-1';
UPDATE users SET location = 2 WHERE rut = '176271472-1';
UPDATE users SET location = 3 WHERE rut = '182473567-4';
UPDATE users SET location = 4 WHERE rut = '2421283874-4';
UPDATE users SET location = 30 WHERE rut = '3641746726-4';
UPDATE users SET location = 5 WHERE rut = '4152351623-5';
UPDATE users SET location = 7 WHERE rut = '42163612342-5';
UPDATE users SET location = 8 WHERE rut = '4412317123-k';
UPDATE users SET location = 9 WHERE rut = '5872873212-4';
UPDATE users SET location = 10 WHERE rut = '61523512412-5';
UPDATE users SET location = 11 WHERE rut = '65265412312-k';
UPDATE users SET location = 12 WHERE rut = '6527442312-k';
UPDATE users SET location = 13 WHERE rut = '6717263715312-k';
UPDATE users SET location = 27 WHERE rut = '7123674212-5';
UPDATE users SET location = 15 WHERE rut = '7126371263-k';
UPDATE users SET location = 16 WHERE rut = '71264646674-4';
UPDATE users SET location = 17 WHERE rut = '7126476122-4';
UPDATE users SET location = 18 WHERE rut = '7146176122-4';
UPDATE users SET location = 19 WHERE rut = '7723674212-5';
UPDATE users SET location = 20 WHERE rut = '81723817246-1';
UPDATE users SET location = 21 WHERE rut = '876327463-4';
UPDATE users SET location = 22 WHERE rut = '881263612-k';
UPDATE users SET location = 23 WHERE rut = '948128124-1';
UPDATE users SET location = 24 WHERE rut = '98482714-4';

-- Agregar ubicaciones a las emergencias
UPDATE Emergencies SET location = 6 WHERE emergency_id = 1;
UPDATE Emergencies SET location = 26 WHERE emergency_id = 2;
UPDATE Emergencies SET location = 14 WHERE emergency_id = 3;
UPDATE Emergencies SET location = 28 WHERE emergency_id = 4;
UPDATE Emergencies SET location = 29 WHERE emergency_id = 5;














