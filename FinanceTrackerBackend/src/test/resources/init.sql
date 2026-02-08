create database financetracker;
create table if not exists accounts (account_id int primary key,account_name int);
insert into users (user_id,email, password, first_name, last_name) values (1,'lahcen','ahtat204','lahcen@asue24.org','lahce33');
insert into users (user_id,email, password, first_name, last_name) values (2,'mohamed','herewego','ahtmod@gmail','lah234gtgt3');
insert into accounts(account_id,account_name,balance,user_id) values (2,'testaccount',1346.22,1);
insert into accounts(account_id,account_name,balance,user_id) values (3,'testaccount2',1646.22,2);
insert into transactions(transaction_id,description, transaction_type, amount, date, account_id) VALUES(3,'test','EXPENSE',22.3,'2024-10-10',2);