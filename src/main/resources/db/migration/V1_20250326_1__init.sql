create table if not exists recipe (
    id int not null auto_increment primary key,
    version int not null default 0,
    created timestamp(6) not null default now(6),
    updated timestamp(6) not null default now(6),
    category varchar(20),
    name varchar(50) not null,
    description text not null
);
create unique index recipe_name_idx on recipe(name);

create table if not exists instruction (
    id int not null auto_increment primary key,
    version int not null default 0,
    created timestamp(6) not null default now(6),
    updated timestamp(6) not null default now(6),
    recipe_id int not null,
    value text not null,
    foreign key inst_recipe_id_fk (recipe_id) references recipe(id)
);
create index inst_recipe_id_idx on instruction(recipe_id);

create table if not exists ingredient (
    id int not null auto_increment primary key,
    version int not null default 0,
    created timestamp(6) not null default now(6),
    updated timestamp(6) not null default now(6),
    name varchar(50) not null
);
create unique index ingredient_name_idx on ingredient(name);

create table if not exists measured_ingredient (
    id int not null auto_increment primary key,
    version int not null default 0,
    created timestamp(6) not null default now(6),
    updated timestamp(6) not null default now(6),
    recipe_id int not null,
    ingredient_id int not null,
    quantity varchar(20) not null,
    foreign key mi_recipe_id_fk (recipe_id) references recipe(id),
    foreign key mi_ingr_id_fk (ingredient_id) references ingredient(id)
);
create index mi_recipe_id_idx on measured_ingredient(recipe_id);

create table if not exists link (
    id int not null auto_increment primary key,
    version int not null default 0,
    created timestamp(6) not null default now(6),
    updated timestamp(6) not null default now(6),
    recipe_id int not null,
    type varchar(20) not null,
    url varchar(4000) not null,
    foreign key link_recipe_id_fk (recipe_id) references recipe(id)
);
create index link_recipe_id_idx on link(recipe_id);
