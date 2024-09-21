CREATE EXTENSION IF NOT EXISTS pgcrypto;

INSERT INTO users (id, nickname, email, password)
VALUES (1, 'alice', 'alice@example.com', crypt('password1', gen_salt('bf'))),
       (2, 'bob', 'bob@example.com', crypt('password2', gen_salt('bf')));