INSERT INTO MEMBER (name, email, password)
VALUES ('moda', 'moda@woowa.com', '1234');

INSERT INTO SEAT (grade, number)
VALUES ('VIP', 1),
       ('VIP', 2),
       ('VIP', 3),
       ('VIP', 4),
       ('VIP', 5),
       ('R', 6),
       ('R', 7),
       ('R', 8),
       ('R', 9),
       ('R', 10),
       ('S', 11),
       ('S', 12),
       ('S', 13),
       ('S', 14),
       ('S', 15),
       ('A', 16),
       ('A', 17),
       ('A', 18),
       ('A', 19),
       ('A', 20)
;

INSERT INTO MUSICAL (musical_month, title, description)
VALUES (6, 'memphis', '내가 제일 좋아하는 공연!!')
;

INSERT INTO RESERVATION (date, musical_time, musical_id, member_id, seat_id)
VALUES ('2025-06-30', 'EVENING', 1, 1, 5)
;
