/*Inserting users into the database*/
INSERT INTO users(id, password, username) VALUES (1001, '$2a$10$iC70FZ8fTk.yvsm.xPSmK.c7iaJGQQ.7CTFwG.m5jRQr9E5H9fiWq', 'Breeder01');
INSERT INTO users(id, password, username) VALUES (1002, '$2a$10$hT9Emljbs0176Yd5bES/YuH2j2/4HdC./sq4oqbBmSEte9pAQUzw2', 'User01');

/*Inserting roles into the database*/
INSERT INTO authorities(id, authority) VALUES (1001, 'ROLE_ADMIN');
INSERT INTO authorities(id, authority) VALUES (1002, 'ROLE_USER');

/*Inserting authorisation id and user id to JOIN table*/
INSERT INTO users_authorities(users_id, authorities_id)	VALUES (1001, 1001);
INSERT INTO users_authorities(users_id, authorities_id)	VALUES (1002, 1002);

/* Inserting dogs into the database */
INSERT INTO dogs (id, animal_type, birth_method, blood_temperature, hair_color, date_of_birth, date_of_death, food, sex, weight_in_grams, kind_of_hair, number_of_teeth, breed, breed_group, chip_number, dog_years, name, can_see, can_hear) VALUES (1001, 'carnivore', 'livebirth', 'warmblooded', 'brown', '2017-1-13', null, 'dogchow', 'female', 10.0, 'long haired', 42, 'Dachschund', 'Hound', 111111111111111, 5, 'Saartje', true, true);
INSERT INTO dogs (id, animal_type, birth_method, blood_temperature, hair_color, date_of_birth, date_of_death, food, sex, weight_in_grams, kind_of_hair, number_of_teeth, breed, breed_group, chip_number, dog_years, name, can_see, can_hear) VALUES (1002, 'carnivore', 'livebirth', 'warmblooded', 'brown', '2017-1-13', null, 'dogchow', 'female', 10.0, 'long haired', 42, 'Dachschund', 'Hound', 222222222222222, 2, 'Lotje', true, true);
INSERT INTO dogs (id, animal_type, birth_method, blood_temperature, hair_color, date_of_birth, date_of_death, food, sex, weight_in_grams, kind_of_hair, number_of_teeth, breed, breed_group, chip_number, dog_years, name, can_see, can_hear) VALUES (1003, 'carnivore', 'livebirth', 'warmblooded', 'brown', '2017-1-13', null, 'dogchow', 'female', 10.0, 'long haired', 42, 'Dachschund', 'Hound', 333333333333333, 0, 'Pip',true, true);
INSERT INTO dogs (id, animal_type, birth_method, blood_temperature, hair_color, date_of_birth, date_of_death, food, sex, weight_in_grams, kind_of_hair, number_of_teeth, breed, breed_group, chip_number, dog_years, name, can_see, can_hear) VALUES (1004, 'carnivore', 'livebirth', 'warmblooded', 'brown', '2017-1-13', null, 'dogchow', 'female', 10.0, 'long haired', 42, 'Dachschund', 'Hound', 444444444444444, 3, 'Hondje', true, true);
INSERT INTO dogs (id, animal_type, birth_method, blood_temperature, hair_color, date_of_birth, date_of_death, food, sex, weight_in_grams, kind_of_hair, number_of_teeth, breed, breed_group, chip_number, dog_years, name, can_see, can_hear) VALUES (1005, 'carnivore', 'livebirth', 'warmblooded', 'brown', '2017-1-13', null, 'dogchow', 'female', 10.0, 'long haired', 42, 'Dachschund', 'Hound', 555555555555555, 2, 'Takkie', true, true);

/* Inserting persons into the database */
INSERT INTO persons(id, animal_type, birth_method, blood_temperature, date_of_birth, date_of_death, food, hair_color, sex, weight_in_grams, kind_of_hair, number_of_teeth, city, country, first_name, house_number, house_number_extension, last_name, street, zip_code) VALUES (2001, 'omnivore','livebirth', 'warmblooded', '1986-01-02', null, 'beef', 'brown', 'female', 6300.00, 'long hair', 32, 'Kaatsheuvel', 'the Netherlands', 'Eva', 31, null, 'van de Merwe', 'Maas', '5172 CN');
INSERT INTO persons(id, animal_type, birth_method, blood_temperature, date_of_birth, date_of_death, food, hair_color, sex, weight_in_grams, kind_of_hair, number_of_teeth, city, country, first_name, house_number, house_number_extension, last_name, street, zip_code) VALUES (2002, 'omnivore','livebirth', 'warmblooded', '1982-02-21', null, 'beef', 'brown', 'male', 8000.00, 'long hair', 32, 'Kaatsheuvel', 'the Netherlands', 'Teun', 31, null, 'van de Merwe', 'Maas', '5172 CN');
INSERT INTO persons(id, animal_type, birth_method, blood_temperature, date_of_birth, date_of_death, food, hair_color, sex, weight_in_grams, kind_of_hair, number_of_teeth, city, country, first_name, house_number, house_number_extension, last_name, street, zip_code) VALUES (2003, 'omnivore','livebirth', 'warmblooded', '2011-02-04', null, 'beef', 'brown', 'female', 4500.00, 'long hair', 32, 'Kaatsheuvel', 'the Netherlands', 'Demi', 31, null, 'Hamers', 'Maas', '5172 CN');
INSERT INTO persons(id, animal_type, birth_method, blood_temperature, date_of_birth, date_of_death, food, hair_color, sex, weight_in_grams, kind_of_hair, number_of_teeth, city, country, first_name, house_number, house_number_extension, last_name, street, zip_code) VALUES (2004, 'omnivore','livebirth', 'warmblooded', '2013-12-05', null, 'beef', 'blond', 'female', 3500.00, 'long hair', 32, 'Kaatsheuvel', 'the Netherlands', 'Lara', 31, null, 'Hamers', 'Maas', '5172 CN');
INSERT INTO persons(id, animal_type, birth_method, blood_temperature, date_of_birth, date_of_death, food, hair_color, sex, weight_in_grams, kind_of_hair, number_of_teeth, city, country, first_name, house_number, house_number_extension, last_name, street, zip_code) VALUES (2005, 'omnivore','livebirth', 'warmblooded', '2014-12-27', null, 'beef', 'brown', 'male', 3500.00, 'short hair', 32, 'Kaatsheuvel', 'the Netherlands', 'Tijl', 31, null, 'van de Merwe', 'Maas', '5172 CN');
INSERT INTO persons(id, animal_type, birth_method, blood_temperature, date_of_birth, date_of_death, food, hair_color, sex, weight_in_grams, kind_of_hair, number_of_teeth, city, country, first_name, house_number, house_number_extension, last_name, street, zip_code) VALUES (2006, 'omnivore','livebirth', 'warmblooded', '2018-12-23', null, 'beef', 'blond', 'female', 2300.00, 'medium long hair', 32, 'Kaatsheuvel', 'the Netherlands', 'Janina', 31, null, 'van de Merwe', 'Maas', '5172 CN');

/* Update my two dogs, set myself as owner */
UPDATE dogs
SET person_id = 2001
    WHERE id = 1001;

UPDATE dogs
SET person_id = 2001
    WHERE id = 1002;
