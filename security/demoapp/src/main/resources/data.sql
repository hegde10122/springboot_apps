INSERT INTO users(username,password,enabled)
VALUES ('vaibhav','dongare',true);

INSERT INTO users(username,password,enabled)
VALUES ('ganesh','hegde',true);

INSERT INTO authorities(username,authority)
VALUES('vaibhav','ROLE_USER');

INSERT INTO authorities(username,authority)
VALUES('ganesh','ROLE_ADMIN');
