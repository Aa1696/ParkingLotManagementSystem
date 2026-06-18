CREATE TABLE parking_lot (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    total_floors INT,
    total_entry_gates INT,
    total_exit_gates INT
);

CREATE TABLE floor (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    floor_number INT NOT NULL,
    floor_name VARCHAR(255),
    parking_lot_id BIGINT NOT NULL,
    FOREIGN KEY (parking_lot_id) REFERENCES parking_lot(id)
);

CREATE TABLE parking_spot (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    spot_number VARCHAR(255) NOT NULL,
    spot_type VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    vehicle_type VARCHAR(50),
    assigned_vehicle_plate VARCHAR(255),
    floor_id BIGINT NOT NULL,
    FOREIGN KEY (floor_id) REFERENCES floor(id)
);

CREATE TABLE vehicle (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    vehicle_class VARCHAR(50) NOT NULL,
    license_plate VARCHAR(255) NOT NULL UNIQUE,
    vehicle_type VARCHAR(50) NOT NULL,
    owner_name VARCHAR(255)
);

CREATE TABLE ticket (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    license_plate VARCHAR(255) NOT NULL,
    vehicle_type VARCHAR(50) NOT NULL,
    owner_name VARCHAR(255),
    spot_number VARCHAR(255) NOT NULL,
    floor_number INT NOT NULL,
    entry_gate INT NOT NULL,
    entry_time TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL
);

CREATE TABLE invoice (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ticket_id BIGINT NOT NULL UNIQUE,
    license_plate VARCHAR(255) NOT NULL,
    entry_time TIMESTAMP NOT NULL,
    exit_time TIMESTAMP NOT NULL,
    duration_minutes BIGINT NOT NULL,
    parking_charge DECIMAL(10, 2) NOT NULL,
    service_charge DECIMAL(10, 2) DEFAULT 0,
    total_amount DECIMAL(10, 2) NOT NULL,
    paid BOOLEAN DEFAULT FALSE,
    exit_gate INT,
    FOREIGN KEY (ticket_id) REFERENCES ticket(id)
);

CREATE TABLE additional_service (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    service_type VARCHAR(50) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    ticket_id BIGINT NOT NULL,
    FOREIGN KEY (ticket_id) REFERENCES ticket(id)
);

CREATE TABLE app_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    email VARCHAR(255)
);

-- Seed Initial Data
INSERT INTO app_user (username, password, role, email) VALUES 
('admin', '$2a$10$7qN/x/fWf4Qf6kM.m2L1f.7fV.9T0.J.X.X.X.X.X.X.X.X.X.X', 'ADMIN', 'admin@parking.com');

INSERT INTO parking_lot (name, address, total_floors, total_entry_gates, total_exit_gates) VALUES 
('Central Park Mall Parking', '123 Main St, Cityville', 3, 2, 2);

-- Insert Floor 1
INSERT INTO floor (floor_number, floor_name, parking_lot_id) VALUES (1, 'Ground Floor', 1);

-- Insert Spots for Floor 1 (2 Bikes, 3 Cars, 1 Truck, 1 EV)
INSERT INTO parking_spot (spot_number, spot_type, status, floor_id) VALUES 
('F1-C1', 'COMPACT', 'AVAILABLE', 1),
('F1-C2', 'COMPACT', 'AVAILABLE', 1),
('F1-R1', 'REGULAR', 'AVAILABLE', 1),
('F1-R2', 'REGULAR', 'AVAILABLE', 1),
('F1-R3', 'REGULAR', 'AVAILABLE', 1),
('F1-L1', 'LARGE', 'AVAILABLE', 1),
('F1-EV1', 'EV_CHARGING', 'AVAILABLE', 1);

-- Insert Floor 2
INSERT INTO floor (floor_number, floor_name, parking_lot_id) VALUES (2, 'First Floor', 1);

-- Insert Spots for Floor 2
INSERT INTO parking_spot (spot_number, spot_type, status, floor_id) VALUES 
('F2-C1', 'COMPACT', 'AVAILABLE', 2),
('F2-C2', 'COMPACT', 'AVAILABLE', 2),
('F2-R1', 'REGULAR', 'AVAILABLE', 2),
('F2-R2', 'REGULAR', 'AVAILABLE', 2),
('F2-EV1', 'EV_CHARGING', 'AVAILABLE', 2);
