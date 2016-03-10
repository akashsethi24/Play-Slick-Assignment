# --- !Ups
create table "users" ("user_name" VARCHAR(20) NOT NULL,"user_email" VARCHAR(30) NOT NULL,"user_password" VARCHAR(20) NOT NULL,"user_mobile" VARCHAR(10) NOT NULL,"user_isadmin" BOOLEAN NOT NULL,"user_id" SERIAL PRIMARY KEY);
INSERT INTO "users" values ('Prabhat Kashyap','prabhatkashyap33@gmail.com','test123','8826518608',true,1);

create table "certificate" ("cer_id" SERIAL NOT NULL PRIMARY KEY,"user_id" INTEGER NOT NULL,"cer_email" VARCHAR(50) NOT NULL,"cer_desc" VARCHAR(200) NOT NULL,"cer_year" INTEGER NOT NULL);
INSERT INTO "certificate" values(1,1,'Test Certificate','Test Desc',2016);

create table "assignment" ("assign_id" SERIAL NOT NULL PRIMARY KEY,"user_id" INTEGER NOT NULL, "assign_name" VARCHAR(50) NOT NULL, "assign_date" VARCHAR(50) NOT NULL,"assign_remark" VARCHAR(50) NOT NULL,"assign_marks" INTEGER NOT NULL);
INSERT INTO "assignment" values(1,1,'Scala Assignment','32/13/2444', 'Average',5);

create table "language" ("lang_id" SERIAL NOT NULL PRIMARY KEY,"user_id" INTEGER NOT NULL, "language" VARCHAR(50) NOT NULL, "lang_fluency" VARCHAR(50) NOT NULL);
INSERT INTO "language" values(1,1,'Hindi','Excellent');

create table "programmingLang" ("id" SERIAL NOT NULL PRIMARY KEY,"user_id" INTEGER NOT NULL, "name" VARCHAR(50) NOT NULL, "skill_level" VARCHAR(50) NOT NULL);
INSERT INTO "programmingLang" values(1,1,'Scala','Good');

# --- !Downs
DROP table "users";
DROP table "certificate";
DROP table "assignment";
DROP table "language";
DROP table "programmingLang";

