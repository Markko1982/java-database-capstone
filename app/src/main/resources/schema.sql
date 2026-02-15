-- ======================================================================
-- TABELAS PRINCIPAIS
-- ======================================================================

DROP TABLE IF EXISTS doctor_available_times;
DROP TABLE IF EXISTS appointment;
DROP TABLE IF EXISTS patient;
DROP TABLE IF EXISTS admin;
DROP TABLE IF EXISTS doctor;

CREATE TABLE doctor (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    specialty VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL
);

CREATE TABLE doctor_available_times (
  doctor_id BIGINT NOT NULL,
  available_time VARCHAR(11) NOT NULL,
  CONSTRAINT fk_doctor_available_times_doctor
    FOREIGN KEY (doctor_id) REFERENCES doctor(id)
);

DROP TABLE IF EXISTS appointments;
DROP TABLE IF EXISTS patient;
DROP TABLE IF EXISTS admin;

CREATE TABLE patient (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    address VARCHAR(255) NOT NULL
);

CREATE TABLE admin (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  email VARCHAR(100) NOT NULL,
  password VARCHAR(255) NOT NULL
);

CREATE TABLE appointment (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  appointment_time DATETIME NOT NULL,
  status INT NOT NULL,
  doctor_id BIGINT NOT NULL,
  patient_id BIGINT NOT NULL,
  CONSTRAINT fk_appointments_doctor FOREIGN KEY (doctor_id) REFERENCES doctor(id),
  CONSTRAINT fk_appointments_patient FOREIGN KEY (patient_id) REFERENCES patient(id)
);


-- (aqui continuam os PROCEDURES que você já tinha)

-- Procedimento 1: Relatório Diário de Consultas por Médico
DELIMITER $$
CREATE PROCEDURE GetDailyAppointmentReportByDoctor(
    IN report_date DATE
)
BEGIN
    SELECT
        d.name AS doctor_name,
        a.appointment_time,
        a.status,
        p.name AS patient_name,
        p.phone AS patient_phone
    FROM
        appointment a
    JOIN
        doctor d ON a.doctor_id = d.id
    JOIN
        patient p ON a.patient_id = p.id
    WHERE
        DATE(a.appointment_time) = report_date
    ORDER BY
        d.name, a.appointment_time;
END$$
DELIMITER ;

-- Procedimento 2: Médico com Mais Pacientes por Mês
DELIMITER $$
CREATE PROCEDURE GetDoctorWithMostPatientsByMonth(
    IN input_month INT,
    IN input_year INT
)
BEGIN
    SELECT
        doctor_id,
        COUNT(patient_id) AS patients_seen
    FROM
        appointment
    WHERE
        MONTH(appointment_time) = input_month
        AND YEAR(appointment_time) = input_year
    GROUP BY
        doctor_id
    ORDER BY
        patients_seen DESC
    LIMIT 1;
END$$
DELIMITER ;

-- Procedimento 3: Médico com Mais Pacientes por Ano
DELIMITER $$
CREATE PROCEDURE GetDoctorWithMostPatientsByYear(
    IN input_year INT
)
BEGIN
    SELECT
        doctor_id,
        COUNT(patient_id) AS patients_seen
    FROM
        appointment
    WHERE
        YEAR(appointment_time) = input_year
    GROUP BY
        doctor_id
    ORDER BY
        patients_seen DESC
    LIMIT 1;
END$$
DELIMITER ;