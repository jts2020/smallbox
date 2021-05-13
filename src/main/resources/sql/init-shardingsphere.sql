DROP TABLE IF EXISTS TBL_USER_0;
DROP TABLE IF EXISTS TBL_USER_1;

CREATE TABLE TBL_USER_0
(
    id BIGINT(20) NOT NULL COMMENT '主键ID',
    name VARCHAR(30) NULL DEFAULT NULL COMMENT '姓名',
    age INT(11) NULL DEFAULT NULL COMMENT '年龄',
    email VARCHAR(50) NULL DEFAULT NULL COMMENT '邮箱',
    PRIMARY KEY (id)
);

CREATE TABLE TBL_USER_1
(
    id BIGINT(20) NOT NULL COMMENT '主键ID',
    name VARCHAR(30) NULL DEFAULT NULL COMMENT '姓名',
    age INT(11) NULL DEFAULT NULL COMMENT '年龄',
    email VARCHAR(50) NULL DEFAULT NULL COMMENT '邮箱',
    PRIMARY KEY (id)
);

DELETE FROM TBL_USER_0;

INSERT INTO TBL_USER_0 (id, name, age, email) VALUES
(2, 'Jack', 20, 'test2@baomidou.com'),
(4, 'Sandy', 21, 'test4@baomidou.com'),
(6, 'Billie', 24, 'test5@baomidou.com');

DELETE FROM TBL_USER_1;

INSERT INTO TBL_USER_1 (id, name, age, email) VALUES
(1, 'Lili', 18, 'test6@baomidou.com'),
(3, 'Tom', 28, 'test3@baomidou.com'),
(7, 'Lala', 20, 'test7@baomidou.com');