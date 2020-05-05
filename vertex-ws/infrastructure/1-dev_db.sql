CREATE TABLE user_account(
	id serial PRIMARY KEY,
	name VARCHAR (50) NOT NULL,
	password VARCHAR (50) NOT NULL,
	email VARCHAR (355) NOT NULL
);