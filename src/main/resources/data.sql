INSERT INTO todo(id, username, description, target_date, is_done)
VALUES (1, 'User1','Learn JPA', sysdate(), false);

INSERT INTO todo(id, username, description, target_date, is_done)
VALUES (2, 'User1','Learn DATA JPA', sysdate(), false);


INSERT INTO todo(id, username, description, target_date, is_done)
VALUES (3, 'User2','Learn Italian', sysdate(), false);

/*
$2a$10$xzjXcyAdaqrx6HwbIl.iV.2K2k5R8Q475kceZjSNIdw.OOX3r9k.W
user1Password
*/
INSERT INTO user(id, username, password)
VALUES (1, 'User1', '$2a$10$xzjXcyAdaqrx6HwbIl.iV.2K2k5R8Q475kceZjSNIdw.OOX3r9k.W');

/*
$2a$10$kQnRiMW28ebrj6QSqspnouxevkZH58DFVJlLgTAIXwuQ8GnR09EXK
user2Password
*/
INSERT INTO user(id, username, password)
VALUES (2, 'User2', '$2a$10$kQnRiMW28ebrj6QSqspnouxevkZH58DFVJlLgTAIXwuQ8GnR09EXK');