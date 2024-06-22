-- CREATE TABLE IF NOT EXISTS users (
--     id INT AUTO_INCREMENT PRIMARY KEY,
--     name VARCHAR(255) NOT NULL,
--     phone VARCHAR(20),
--     account VARCHAR(255) UNIQUE NOT NULL,
--     password VARCHAR(255) NOT NULL,
--     role VARCHAR(50),
--     register BOOLEAN,
--     create_time TIMESTAMP,
--     update_time TIMESTAMP
-- );

-- INSERT INTO users (name, phone, account, password, role, register, create_time, update_time)
-- SELECT 'valueA', '123456789', 'user@gmail.com', 'password', 'user', 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
-- WHERE NOT EXISTS (SELECT 1 FROM users WHERE account = 'user@gmail.com');



-- INSERT
--     IGNORE INTO user (
--         id,
--         name,
--         phone,
--         account,
--         password,
--         role,
--         register,
--         create_time,
--         update_time
--     )
-- VALUES
--     (
--         1,
--         'valueA',
--         '123456789',
--         'admin',
--         'password',
--         'admin',
--         0,
--         CURRENT_TIMESTAMP,
--         CURRENT_TIMESTAMP
--     ),
--     (
--         2,
--         'valueB',
--         '123456789',
--         'user',
--         'password',
--         'user',
--         0,
--         CURRENT_TIMESTAMP,
--         CURRENT_TIMESTAMP
--     );


--     MERGE INTO "users" (id, name, phone, account, password, role, register, create_time, update_time)
-- KEY (account)
-- VALUES (1, 'valueA', '123456789', 'admin', 'password', 'admin', 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
--        (2, 'valueB', '123456789', 'user', 'password', 'user', 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);


-- INSERT
--     IGNORE INTO file_data (
--         id,
--         content_type,
--         file_name,
--         original_file_name,
--         suffix
--     )
-- VALUES
--     (
--         1,
--         "image/jpeg",
--         "16923603086287289.jpg",
--         "images.jpeg",
--         ".jpg"
--     );

    INSERT IGNORE INTO file_data (id, content_type, file_name, original_file_name, suffix) VALUES 
(1, 'image/jpeg', '16923603086287289.jpg', 'images.jpeg', '.jpg');