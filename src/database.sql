create table member(
    user_id varchar(255) not null unique primary key,
    username varchar(255) not null,
    nickname varchar(255) not null,
    password varchar(255) not null,
    birthday date not null,
    email varchar(255) not null,
    created_at datetime not null default current_timestamp
);