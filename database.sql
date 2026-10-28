-- Script tạo database APIDB (MySQL)
CREATE DATABASE IF NOT EXISTS APIDB CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE APIDB;

CREATE TABLE IF NOT EXISTS student (
    id      INT AUTO_INCREMENT PRIMARY KEY,
    name    VARCHAR(255) NOT NULL,
    address VARCHAR(255),
    phone   VARCHAR(20),
    dob     DATE
);

CREATE TABLE IF NOT EXISTS course (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    score      DOUBLE NOT NULL,
    student_id INT NOT NULL,
    CONSTRAINT fk_course_student FOREIGN KEY (student_id) REFERENCES student (id) ON DELETE CASCADE
);

-- Dữ liệu mẫu
INSERT INTO student (name, address, phone, dob) VALUES
('Nguyen Van An',  'Ha Noi',      '0901234567', '2003-05-12'),
('Tran Thi Binh',  'Ho Chi Minh', '0912345678', '2002-11-20'),
('Le Van Cuong',   'Da Nang',     '0987654321', '2003-02-08');

INSERT INTO course (name, score, student_id) VALUES
('Java Core', 8.5, 1), ('Spring Boot', 9.0, 1), ('SQL', 7.5, 1),
('Java Core', 6.0, 2), ('HTML/CSS', 7.0, 2),
('Java Core', 4.5, 3), ('SQL', 5.0, 3);
