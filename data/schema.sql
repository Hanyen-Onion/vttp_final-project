create database flight_planner;

use flight_planner;

-- drop table users;

create table users (
    email varchar(150) not null,
    username varchar(30) not null,
    password varchar(30) not null,
	country varchar (30) not null,
    city varchar(30) not null,
    timezone varchar(40) not null,
    currency char(3) not null,
    constraint pk_email primary key (email) 
);

select * from users;

select email, username, password from users
 where email = 'jenhanyen@gmail.com';
