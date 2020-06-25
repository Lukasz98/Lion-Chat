CREATE DATABASE Lion_chat;
USE Lion_chat;

CREATE TABLE users ( 
	id INTEGER PRIMARY KEY AUTO_INCREMENT, 
	login VARCHAR(20) NOT NULL, 
	passwd BINARY(16) NOT NULL 
);

CREATE TABLE groups ( 
	id INTEGER PRIMARY KEY AUTO_INCREMENT 
);

CREATE TABLE contacts ( 
	user_id1 INTEGER NOT NULL,
	user_id2 INTEGER NOT NULL,
	PRIMARY KEY (user_id1, user_id2),
	FOREIGN KEY (user_id1) REFERENCES users(id),
	FOREIGN KEY (user_id2) REFERENCES users(id)
);

CREATE TABLE group_messages ( 
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    group_id INTEGER NOT NULL,
    author_id INTEGER NOT NULL,
    text VARCHAR(200) NOT NULL,
	FOREIGN KEY (group_id) REFERENCES groups(id),
    FOREIGN KEY (author_id) REFERENCES users(id)
);

CREATE TABLE group_members ( 
    group_id INTEGER NOT NULL,
    member_id INTEGER NOT NULL,
    PRIMARY KEY (group_id, member_id),
    FOREIGN KEY (group_id) REFERENCES groups(id),
    FOREIGN KEY (member_id) REFERENCES users(id)
);

CREATE TABLE group_msg_views ( 
    group_id INTEGER NOT NULL,
    msg_id INTEGER NOT NULL,
    viewer_id INTEGER NOT NULL,
    viewed BOOLEAN NOT NULL,
    PRIMARY KEY (group_id, msg_id, viewer_id),
    FOREIGN KEY (group_id) REFERENCES groups(id),
    FOREIGN KEY (msg_id) REFERENCES group_messages(id),
    FOREIGN KEY (viewer_id) REFERENCES users(id)
);

CREATE TABLE priv_messages (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    sender_id INTEGER NOT NULL,
    receiver_id INTEGER NOT NULL,
    text VARCHAR(200) NOT NULL,
    viewed BOOLEAN NOT NULL,
    FOREIGN KEY (sender_id) REFERENCES users(id),
    FOREIGN KEY (receiver_id) REFERENCES users(id)
);

DELIMITER$$
CREATE TRIGGER after_grp_msg_insert
AFTER INSERT
ON group_messages FOR EACH ROW
BEGIN
	insert into group_msg_views (msg_id, group_id, viewer_id, viewed)
	SELECT NEW.id, NEW.group_id, member_id, false as 'abnc'
    FROM group_members WHERE
    group_members.group_id=NEW.group_id;
END$$
DELIMITER ;

CREATE USER 'server_user'@'localhost' IDENTIFIED BY '123';

GRANT INSERT ON Lion_chat.groups TO 'server_user'@'localhost';
GRANT INSERT ON Lion_chat.contacts TO 'server_user'@'localhost';
GRANT INSERT ON Lion_chat.group_messages TO 'server_user'@'localhost';
GRANT INSERT ON Lion_chat.group_members TO 'server_user'@'localhost';
GRANT INSERT ON Lion_chat.group_msg_views TO 'server_user'@'localhost';
GRANT INSERT ON Lion_chat.priv_messages TO 'server_user'@'localhost';

GRANT SELECT ON Lion_chat.groups TO 'server_user'@'localhost';
GRANT SELECT ON Lion_chat.contacts TO 'server_user'@'localhost';
GRANT SELECT ON Lion_chat.group_messages TO 'server_user'@'localhost';
GRANT SELECT ON Lion_chat.group_members TO 'server_user'@'localhost';
GRANT SELECT ON Lion_chat.group_msg_views TO 'server_user'@'localhost';
GRANT SELECT ON Lion_chat.priv_messages TO 'server_user'@'localhost';
GRANT SELECT ON Lion_chat.users TO 'server_user'@'localhost';

GRANT UPDATE ON Lion_chat.group_msg_views TO 'server_user'@'localhost';
GRANT UPDATE ON Lion_chat.priv_messages TO 'server_user'@'localhost';


INSERT INTO users (login, passwd) VALUES ('lukasz', unhex(md5('123')));
INSERT INTO users (login, passwd) VALUES ('lucja', unhex(md5('123')));
INSERT INTO users (login, passwd) VALUES ('adam', unhex(md5('123')));


