REVOKE ALL ON SCHEMA cinep FROM PUBLIC;
ALTER DEFAULT PRIVILEGES IN SCHEMA cinep REVOKE ALL ON TABLES FROM PUBLIC;

CREATE TABLE IF NOT EXISTS cinep.movie (
    id SERIAL,
    original_title VARCHAR (255),
    estonian_title VARCHAR (255),
    russian_title VARCHAR (255),
    english_title VARCHAR (255),
    cinema VARCHAR (50),
    cinema_auditorium(100),
    duration SMALLINT,
    start_time TIME,
    start_date DATE,
    production_year VARCHAR (4),
    show_url VARCHAR (255),
    CONSTRAINT movie_id_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS cinep.genre (
    id SERIAL,
    genre_name VARCHAR (50),
    movie_id INTEGER,
    CONSTRAINT genre_id_pk PRIMARY KEY (id),
    CONSTRAINT movie_id_fk FOREIGN KEY REFERENCES cinep.movie (id)
);

CREATE TABLE IF NOT EXISTS cinep.user (
    id SERIAL,
    username VARCHAR (100),
    password VARCHAR (600),
    user_role VARCHAR (20),
    CONSTRAINT user_id_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS cinep.bookmark (
    id SERIAL,
    user_id INTEGER,
    movie_id INTEGER,
    CONSTRAINT bookmark_id_pk PRIMARY KEY (id),
    CONSTRAINT user_id_fk FOREIGN KEY REFERENCES cinep.user (id),
    CONSTRAINT movie_id_fk FOREIGN KEY REFERENCES cinep.movie (id)
);
