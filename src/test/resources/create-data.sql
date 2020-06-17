DROP TABLE IF EXISTS USERS;

CREATE TABLE USERS(
    id LONG AUTO_INCREMENT PRIMARY KEY,
    password varchar(250) not null,
    role varchar(250) not null,
    username VARCHAR(250) not null

);

DROP TABLE IF EXISTS USER_BOOKMARKS;

CREATE TABLE USER_BOOKMARKS(
    user_id LONG DEFAULT not null,
    movie_id LONG DEFAULT not null
);

DROP TABLE IF EXISTS MOVIES;

CREATE TABLE MOVIES(
    id LONG NOT NULL,
    ORIGINAL_TITLE varchar(250) not null,
    THEATRE varchar(250) not null,
    THEATRE_AUDITORIUM varchar(250) not null,
    DURATION_IN_MINUTES INTEGER not null,
    USER_RATING VARCHAR(250),
    START_DATE date,
    START_TIME time,
    SHOW_URL VARCHAR(250)
);

INSERT INTO USERS(id, password , role, username) VALUES
    (1 , 'qwerty', 'USER', 'First User'),
    (2 , 'qwerty', 'USER', 'Second User'),
    (3 , 'qwerty', 'USER', 'Third User'),
    (4 , 'qwerty', 'USER', 'Fourth User'),
    (5 , 'qwerty', 'USER', 'Fifth User'),
    (6 , 'qwerty', 'USER', 'Sixth User'),
    (7 , 'qwerty', 'USER', 'Seventh User');

INSERT into MOVIES(id, ORIGINAL_TITLE, THEATRE, THEATRE_AUDITORIUM, DURATION_IN_MINUTES) VALUES
    (1, 'Star Wars', 'Coca-cola-plaza', '1', 90),
    (2, 'Star Wars', 'Kosmos', '1', 90),
    (3, 'Star Wars', 'T1', '1', 290),
    (4, 'Hobbit', 'Apollo', '1', 190),
    (5, 'Star Wars', 'Viimsi', '1', 90),
    (6, 'Lord of the Rings', 'Coca-cola-plaza', '1', 90),
    (7, '300 spartans', 'Coca-cola-plaza', '1', 90),
    (8, 'Saw', 'Kosmos', '1', 910),
    (9, 'Predator', 'Kosmos', '1', 90),
    (10, 'Avengers', 'Coca-cola-plaza', '1',90),
    (11, 'Alien', 'T1', '1', 90),
    (12, 'Alien', 'Artis', '1', 90);


INSERT into USER_BOOKMARKS(user_id, movie_id) VALUES
    (1, 1),
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
