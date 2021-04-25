alter table USERS drop constraint CONSTRAINT_2;

alter table TASK_GROUPS add column user_id int null;
alter table TASK_GROUPS add foreign key  (user_id) references USERS(id);
