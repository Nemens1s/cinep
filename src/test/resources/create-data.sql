DROP TABLE IF EXISTS USER_BOOKMARKS;
DROP TABLE IF EXISTS GENRES;
DROP TABLE IF EXISTS MOVIES;
DROP TABLE IF EXISTS USERS;

CREATE TABLE USERS
(
    id       LONG AUTO_INCREMENT PRIMARY KEY,
    password varchar(250) not null,
    role     varchar(250) not null,
    username VARCHAR(250) not null

);

CREATE TABLE MOVIES
(
    id                  LONG         NOT NULL,
    ORIGINAL_TITLE      varchar(250) not null,
    ESTONIAN_TITLE      varchar(250) not null,
    RUSSIAN_TITLE       varchar(250) not null,
    ENGLISH_TITLE       varchar(250) not null,
    THEATRE             varchar(250) not null,
    THEATRE_AUDITORIUM  varchar(250) not null,
    DURATION_IN_MINUTES INTEGER      not null,
    USER_RATING         VARCHAR(250),
    START_DATE          date,
    START_TIME          time,
    SHOW_URL            VARCHAR(250),
    PRODUCTION_YEAR     VARCHAR(4)
);
CREATE TABLE USER_BOOKMARKS
(
    id       LONG AUTO_INCREMENT PRIMARY KEY,
    user_id  LONG DEFAULT not null,
    movie_id LONG DEFAULT not null
);

CREATE TABLE GENRES
(
    id          LONG AUTO_INCREMENT PRIMARY KEY,
    description VARCHAR(50),
    movie_id    LONG DEFAULT NOT NULL
);

ALTER TABLE USER_BOOKMARKS
    ADD CONSTRAINT FK_USER_ID FOREIGN KEY (user_id) REFERENCES USERS (id) ON DELETE CASCADE;
ALTER TABLE USER_BOOKMARKS
    ADD CONSTRAINT FK_MOVIE_ID_BOOKMARK FOREIGN KEY (movie_id) REFERENCES MOVIES (id) ON DELETE CASCADE;

ALTER TABLE GENRES
    ADD CONSTRAINT FK_MOVIE_ID FOREIGN KEY (movie_id) REFERENCES MOVIES (id) ON DELETE CASCADE;


INSERT INTO USERS(id, password, role, username)
VALUES (1, 'qwerty', 'USER', 'First User'),
       (2, 'qwerty', 'USER', 'Second User'),
       (3, 'qwerty', 'USER', 'Third User'),
       (4, 'qwerty', 'USER', 'Fourth User'),
       (5, 'qwerty', 'USER', 'Fifth User'),
       (6, 'qwerty', 'USER', 'Sixth User'),
       (7, 'qwerty', 'USER', 'Seventh User');

INSERT into MOVIES(id, ORIGINAL_TITLE, ESTONIAN_TITLE, RUSSIAN_TITLE, ENGLISH_TITLE, THEATRE, THEATRE_AUDITORIUM,
                   DURATION_IN_MINUTES, PRODUCTION_YEAR, START_DATE, START_TIME)
VALUES (1, 'Star Wars', 'Star Wars', 'Звёздные Войны', 'Star Wars', 'Coca-cola-plaza', '1', 90, '1970', '2020-07-20', '11:00:00'),
       (2, 'Star Wars', 'Star Wars', 'Звёздные Войны', 'Star Wars', 'Kosmos', '1', 90, '1970', '2020-07-20', '21:00:00'),
       (3, 'Star Wars', 'Star Wars', 'Звёздные Войны', 'Star Wars', 'T1', '1', 290, '1970', '2020-07-20', '15:45:00'),
       (4, 'Hobbit', 'Kääbik', 'Хоббит', 'Hobbit', 'Apollo', '1', 190, '1970', '2020-07-20', '13:30:00'),
       (5, 'Star Wars', 'Star Wars', 'Звёздные Войны', 'Star Wars', 'Viimsi', '1', 90, '1970', '2020-07-21', '17:15:00'),
       (6, 'Lord of the Rings', 'Sõrmuste Isand', '', 'Lord of the Rings', 'Coca-cola-plaza', '1', 90, '1970', '2020-07-21', '22:00:00'),
       (7, '300 spartans', '300', '300 спартанцев', '300 spartans', 'Coca-cola-plaza', '1', 90, '1970', '2020-07-21', '12:00:00'),
       (8, 'Saw', 'Saag', 'Пила', 'Saw', 'Kosmos', '1', 910, '1970', '2020-07-21', '16:00:00'),
       (9, 'Predator', 'Kiskja', 'Хищник', 'Predator', 'Kosmos', '1', 90, '1970', '2020-07-23', '18:30:00'),
       (10, 'Avengers', 'Tasujad', 'Мстители', 'Avengers', 'Coca-cola-plaza', '1', 90, '1970', '2020-07-23', '18:30:00'),
       (11, 'Alien', 'Tulnukas', 'Чужой', 'Alien', 'T1', '1', 90, '1970', '2020-07-23', '19:00:00'),
       (12, 'Alien', 'Tulnukas', 'Чужой', 'Alien', 'Artis', '1', 90, '1970', '2020-07-23', '23:50:00');



INSERT into USER_BOOKMARKS(user_id, movie_id)
VALUES (1, 1),
       (1, 10),
       (3, 7),
       (3, 3),
       (3, 10),
       (6, 1),
       (4, 9),
       (4, 9),
       (6, 11),
       (5, 3),
       (4, 1);

INSERT INTO GENRES(description, movie_id)
values ('Horror', 11),
       ('Sci-fi', 11),
       ('Horror', 12),
       ('Sci-fi', 12),
       ('Ulme', 11),
       ('Õudus', 11),
       ('Фантастика', 11),
       ('Ужасы', 11),
       ('Ulme', 12),
       ('Õudus', 12),
       ('Фантастика', 12),
       ('Action', 10),
       ('Экшн', 10),
       ('Märul', 10);




