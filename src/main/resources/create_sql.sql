-- Table users
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255),
    ip_addr VARCHAR(255),
    token VARCHAR(255),
    created_at DATETIME,
    updated_at DATETIME
);

-- Table messages
CREATE TABLE messages (
    id INT AUTO_INCREMENT PRIMARY KEY,
    content VARCHAR(255),
    user_id INT,
    sended_at DATETIME,
    updated_at DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Trigger update_user_sended_at
DELIMITER //
CREATE OR REPLACE TRIGGER update_message_sended_at
    AFTER INSERT ON users
    FOR EACH ROW
BEGIN
    UPDATE users
    SET sended_at = NOW()
    WHERE id = NEW.id;
END;
//
DELIMITER ;

-- Trigger update_user_updated_at
DELIMITER //
CREATE OR REPLACE TRIGGER update_message_sended_at
    AFTER UPDATE ON users
    FOR EACH ROW
BEGIN
    UPDATE users
    SET updated_at = NOW()
    WHERE id = NEW.id;
END;
//
DELIMITER ;

-- Trigger update_message_sended_at
DELIMITER //
CREATE OR REPLACE TRIGGER update_message_sended_at
    AFTER INSERT ON messages
    FOR EACH ROW
BEGIN
    UPDATE messages
    SET sended_at = NOW()
    WHERE id = NEW.id;
END;
//
DELIMITER ;

-- Trigger update_message_updated_at
DELIMITER //
CREATE OR REPLACE TRIGGER update_message_sended_at
    AFTER UPDATE ON messages
    FOR EACH ROW
BEGIN
    UPDATE messages
    SET updated_at = NOW()
    WHERE id = NEW.id;
END;
//
DELIMITER ;




