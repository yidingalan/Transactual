CREATE DATABASE finhacks;
use finhacks;

show tables;
drop table fh_users;
drop table fh_records;

CREATE TABLE fh_users (user_id INT(6), first_name VARCHAR(20), last_name VARCHAR(20));
CREATE TABLE fh_records (user_id INT(6), amount DECIMAL(7,2), payment_date TIMESTAMP);

insert into fh_users (user_id, first_name, last_name) VALUES(1, "Marko", "Fresh");
insert into fh_users (user_id, first_name, last_name) VALUES(2, "Alan", "Wang");
insert into fh_records (user_id, amount, payment_date) VALUES(1, 12.65, NOW());
insert into fh_records (user_id, amount, payment_date) VALUES(1, 79.99, NOW());
insert into fh_records (user_id, amount, payment_date) VALUES(1, 927.66, NOW());
insert into fh_records (user_id, amount, payment_date) VALUES(2, 9.99, NOW());
insert into fh_records (user_id, amount, payment_date) VALUES(2, 66.89, NOW());

select * from fh_users;
select * from fh_records;


