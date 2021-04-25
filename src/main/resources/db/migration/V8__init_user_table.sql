drop table if exists user;

create table USER (
    id Integer primary key auto_increment,
    email varchar(80) not null,
    password varchar(50) not null
);

alter table USER add column task_group_id int null;
alter table USER add foreign key (task_group_id) references TASK_GROUPS(id);

