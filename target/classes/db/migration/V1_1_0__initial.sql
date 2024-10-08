create table IF NOT EXISTS tasks
(
id bigint generated by default as identity,
     title varchar(255),
     description varchar(255),
     priority varchar(255) check (priority in('HIGH','LOW','MEDIUM')),
     status varchar(255) check (status in ('CLOSED','DONE','IN_PROGRESS','TODO')),
     author_id bigint,
     executor_id bigint,
     primary key (id)
 );

create table IF NOT EXISTS users (
    id bigint generated by default as identity,
    email varchar(255) unique,
    password varchar(255) not null,
    role varchar(255) not null check (role in('ROLE_ADMIN','ROLE_USER')),
    primary key (id)
);
create table IF NOT EXISTS comments
(
    id bigint generated by default as identity,
    author_id bigint,
    description varchar(255) not null,
    task_id bigint REFERENCES tasks (id),
    primary key (id)
);

INSERT INTO users(id,email, password,role)
VALUES ('1','test1@gmail.com', '$2a$12$ZgpCUjeVPCqRjkRIvUMpceJW30gQlhzdxCj38e5dUVzx5sqxvKu82','ROLE_USER'),
       ('2','test2@gmail.com', '$2a$12$ZgpCUjeVPCqRjkRIvUMpceJW30gQlhzdxCj38e5dUVzx5sqxvKu82','ROLE_USER');
-- password = qwerty123

INSERT INTO tasks(title, description, priority, status, executor_id, author_id)
-- для первого пользователя
VALUES ('school', 'take a test','HIGH','TODO','1','1'),
('work', 'send a report','MEDIUM','IN_PROGRESS','1','1'),
('hospital', 'get a medicine','LOW','CLOSED','1','1'),
('routine', 'do chores','HIGH','DONE','1','1'),
('cooking', 'make lunch','MEDIUM','IN_PROGRESS','1','1'),
-- для второго пользователя
('school', 'take a test','HIGH','TODO','2','2'),
('work', 'send a report','MEDIUM','IN_PROGRESS','2','2'),
('hospital', 'get a medicine','LOW','CLOSED','2','2'),
('routine', 'do chores','HIGH','DONE','2','2'),
('cooking', 'make lunch','MEDIUM','IN_PROGRESS','2','2');



