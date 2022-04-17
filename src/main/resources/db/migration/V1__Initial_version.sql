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

INSERT INTO course (id, title, `desc`, picture, price) VALUES (1, 'HTML & CSS', 'Webページの見た目をつくる言語', 'https://ddb6ltykpq547.cloudfront.net/language/1/icon_for_web/2157d1e7aa48b6d370b46ad6c2c71732', 78);
INSERT INTO course (id, title, `desc`, picture, price) VALUES (2, 'JavaScript', '多様な可能性を秘めたフロントエンド言語', 'https://ddb6ltykpq547.cloudfront.net/language/26/icon_for_web/b91f687669df25708bdd41dc1ebc0082', 120);
INSERT INTO course (id, title, `desc`, picture, price) VALUES (3, 'Java', '大規模開発からモバイルアプリまで、汎用的なプログラミング言語', 'https://ddb6ltykpq547.cloudfront.net/language/7/icon_for_web/215b012918da862620380f1388e813a0', 400);

CREATE TABLE video (
    id INT PRIMARY KEY AUTO_INCREMENT,
    course_id INT NOT NULL,
    `index` INT NOT NULL,
    title VARCHAR(30) UNIQUE NOT NULL,
    path VARCHAR(300) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE course_order (
    id INT PRIMARY KEY AUTO_INCREMENT,
    account_id INT NOT NULL,
    course_id  INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now()
);