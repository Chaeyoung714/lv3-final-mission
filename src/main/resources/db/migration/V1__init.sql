CREATE TABLE member
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    email           VARCHAR(255) NOT NULL UNIQUE,
    name           VARCHAR(255) NOT NULL,
    password       VARCHAR(255) NOT NULL
);

CREATE TABLE musical
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    musical_month   INT NOT NULL,
    title           VARCHAR(255) NOT NULL,
    description     VARCHAR(255)
);

CREATE TABLE seat
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    grade           VARCHAR(255) NOT NULL,
    number          INT NOT NULL
);

CREATE TABLE reservation
(
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    date                DATE NOT NULL,
    musical_time        VARCHAR(255) NOT NULL,
    musical_id          BIGINT NOT NULL,
    member_id           BIGINT NOT NULL,
    seat_id             BIGINT NOT NULL,
    FOREIGN KEY (musical_id) REFERENCES musical (id),
    FOREIGN KEY (member_id) REFERENCES member (id),
    FOREIGN KEY (seat_id) REFERENCES seat (id)
);
