CREATE TABLE role (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(30) UNIQUE NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    status VARCHAR(10) NOT NULL DEFAULT 'OK'
);

INSERT INTO role (id, name) VALUES (1, 'Admin');
INSERT INTO role (id, name) VALUES (2, 'Teacher');
INSERT INTO role (id, name) VALUES (3, 'Student');

CREATE TABLE permission (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(30) UNIQUE NOT NULL,
    role_id INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now(),
    status VARCHAR(10) NOT NULL DEFAULT 'OK'
);

INSERT INTO permission (name, role_id) VALUES ('MANAGE_ACCOUNT', 1);
INSERT INTO permission (name, role_id) VALUES ('UPLOAD_COURSE', 2);
INSERT INTO permission (name, role_id) VALUES ('LOGIN', 3);

CREATE TABLE account (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(30) UNIQUE NOT NULL,
    password VARCHAR(60) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now(),
    status VARCHAR(10) NOT NULL DEFAULT 'OK'
);

INSERT INTO account (id, name, password) VALUES (1, 'Admin1', '');
INSERT INTO account (id, name, password) VALUES (2, 'Teacher1', '');
INSERT INTO account (id, name, password) VALUES (3, 'Student1', '');

CREATE TABLE account_role (
    id INT PRIMARY KEY AUTO_INCREMENT,
    account_id INT NOT NULL,
    role_id INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now(),
    status VARCHAR(10) NOT NULL DEFAULT 'OK'
);

INSERT INTO account_role (account_id, role_id) VALUES (1, 1);
INSERT INTO account_role (account_id, role_id) VALUES (2, 2);
INSERT INTO account_role (account_id, role_id) VALUES (3, 3);

CREATE TABLE session (
    id INT PRIMARY KEY AUTO_INCREMENT,
    account_id INT NOT NULL,
    token VARCHAR(100) UNIQUE NOT NULL,
);