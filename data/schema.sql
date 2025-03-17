create database flight_planner;

use flight_planner;

create table user (
    email varchar(150) not null,
    username varchar(30) not null,
    first_name varchar(30) not null,
    last_name varchar(30) not null,
    password varchar(30),
    constraint pk_email primary key (email) 
);


select email, username, first_name, last_name, password from users
 where email = 'jenhanyen@gmail.com';

-- {
--   "iss": "https://accounts.google.com",
--   "azp": "175032234502-fq0i953n2hqqg4k2c83gae6a6ifa1bps.apps.googleusercontent.com",
--   "aud": "175032234502-fq0i953n2hqqg4k2c83gae6a6ifa1bps.apps.googleusercontent.com",
--   "sub": "108958605298128428060",
--   "email": "jenhanyen@gmail.com",
--   "email_verified": "true",
--   "nbf": "1741673136",
--   "name": "Hanyen Jen",
--   "picture": "https://lh3.googleusercontent.com/a/ACg8ocIbDFUBSzVSJKQG8D-153XBQHabxUEVrG1kzhn0VNwQXA3M5w=s96-c",
--   "given_name": "Hanyen",
--   "family_name": "Jen",
--   "iat": "1741673436",
--   "exp": "1741677036",
--   "jti": "49b8617f575db1da1ee9b8ff5281f6a3664e250e",
--   "alg": "RS256",
--   "kid": "914fb9b087180bc0303284500c5f540c6d4f5e2f",
--   "typ": "JWT"
-- }