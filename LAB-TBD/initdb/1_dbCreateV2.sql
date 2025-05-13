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
