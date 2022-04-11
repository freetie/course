CREATE TABLE account (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(30) UNIQUE NOT NULL,
    password VARCHAR(60) NOT NULL,
    role VARCHAR(10) NOT NULL DEFAULT 'STUDENT',
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now(),
    status VARCHAR(10) NOT NULL DEFAULT 'OK'
);

INSERT INTO account (id, username, password, role) VALUES (1, 'Admin1', '$2a$10$sMx.OzjwW6ZWMTmxQYtYkOZCx1nMUpDfUyRom1IdCAmdqLJDKQeaq', 'ADMIN');
INSERT INTO account (id, username, password, role) VALUES (2, 'Teacher1', '$2a$10$Y9EjLKR2XDC.GKF6zlGd8.XzENRI8qxXOwnDQFikBocftCN/jjw3O', 'TEACHER');
INSERT INTO account (id, username, password) VALUES (3, 'Student1', '$2a$10$KZL0BMu8/vG73kGd2cjF5exUqHWPYgO7RDGy.PYgQXTmji3eeMddW');

CREATE TABLE session (
    id INT PRIMARY KEY AUTO_INCREMENT,
    account_id INT NOT NULL,
    token VARCHAR(100) UNIQUE NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE course (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(30) UNIQUE NOT NULL,
    `desc` VARCHAR(200),
    picture VARCHAR(300),
    price INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE video (
    id INT PRIMARY KEY AUTO_INCREMENT,
    `index` INT NOT NULL,
    title VARCHAR(30) UNIQUE NOT NULL,
    url VARCHAR(300) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE course_order (
    id INT PRIMARY KEY AUTO_INCREMENT,
    account_id INT NOT NULL,
    course_id  INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now()
);