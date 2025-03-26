create table if not exists recipe (
    id int not null auto_increment primary key,
    created timestamp(6),
    updated timestamp(6),
    name varchar(20),
    description text
);
