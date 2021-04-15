drop table if exists task_events;
create table task_events (
                       id Integer primary key auto_increment,
                       task_id int,
                       occurrence datetime,
                       name varchar(30)
)
