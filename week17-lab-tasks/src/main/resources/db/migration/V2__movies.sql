CREATE TABLE movies (
    id BIGINT AUTO_INCREMENT,
    title VARCHAR(255),
    release_date DATE,
    CONSTRAINT pk_movies PRIMARY KEY (id)
);
