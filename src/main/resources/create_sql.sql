-- Table users
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) UNIQUE,
    password VARCHAR(255),
    ip_addr VARCHAR(255),
    token VARCHAR(255),
    created_at DATETIME,
    updated_at DATETIME,
    deleted_at DATETIME
);

-- Table messages
CREATE TABLE messages (
    id INT AUTO_INCREMENT PRIMARY KEY,
    content VARCHAR(255),
    user_id INT,
    created_at DATETIME,
    updated_at DATETIME,
    deleted_at DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Trigger update_user_created_at
DELIMITER //
CREATE OR REPLACE TRIGGER update_user_created_at
    BEFORE INSERT ON users
    FOR EACH ROW
BEGIN
    UPDATE users
    SET NEW.created_at = NOW()
    WHERE id = NEW.id;
END
//
DELIMITER ;

-- Trigger update_user_updated_at
DELIMITER //
CREATE OR REPLACE TRIGGER update_user_updated_at
    BEFORE UPDATE ON users
    FOR EACH ROW
BEGIN
    UPDATE users
    SET NEW.updated_at = NOW()
    WHERE id = NEW.id;
END
//
DELIMITER ;

-- Trigger update_message_sended_at
DELIMITER //
CREATE OR REPLACE TRIGGER update_message_sended_at
    BEFORE INSERT ON messages
    FOR EACH ROW
BEGIN
    UPDATE messages
    SET NEW.created_at = NOW()
    WHERE id = NEW.id;
END
//
DELIMITER ;

-- Trigger update_message_updated_at
DELIMITER //
CREATE OR REPLACE TRIGGER update_message_updated_at
    BEFORE UPDATE ON messages
    FOR EACH ROW
BEGIN
    UPDATE messages
    SET NEW.updated_at = NOW()
    WHERE id = NEW.id;
END
//
DELIMITER ;

-- Procedure to delete a user
DELIMITER //
CREATE OR REPLACE PROCEDURE delete_user(
    IN target_username VARCHAR(255)
)
BEGIN
    DECLARE target_id INT;

    SELECT id INTO target_id FROM users WHERE username=target_username;

    IF target_id IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'User not found';
    END IF;

    UPDATE users
    SET deleted_at=NOW()
    WHERE id=target_id;

    UPDATE messages
    SET deleted_at=NOW()
    WHERE user_id=target_id;
END
//
DELIMITER ;

-- Procedure to insert a userDELIMITER //
CREATE PROCEDURE InsertUser(IN p_username VARCHAR(255), IN p_addr VARCHAR(255), IN p_token VARCHAR(255), OUT p_generated_id INT)
BEGIN
    INSERT INTO users (username, ip_addr, token) VALUES (p_username, p_addr, p_token);
    SET p_generated_id = LAST_INSERT_ID();
END //
DELIMITER ;
