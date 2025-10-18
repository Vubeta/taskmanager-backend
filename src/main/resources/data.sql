INSERT INTO users (username, password) VALUES ('admin', '$2a$10$AFF8b2ICU.FHYiU5C/cLKuZLsXoxI4uW/QPrDVu5.Sh5wF9E3pOIa'); -- bcrypt hashed
INSERT INTO tasks (title, description, completed, created_at, updated_at) VALUES
    ('Welcome', 'This is your first task', false, NOW(), NOW());

