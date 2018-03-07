BEGIN TRANSACTION;

  DROP TABLE IF EXISTS "rigs" CASCADE;
  DROP TABLE IF EXISTS "users" CASCADE;
  DROP TABLE IF EXISTS "user_profiles" CASCADE;
  DROP TABLE IF EXISTS "user_and_profile" CASCADE;

  DROP SEQUENCE IF EXISTS "rig_seq" CASCADE;
  DROP SEQUENCE IF EXISTS "user_seq" CASCADE;
  DROP SEQUENCE IF EXISTS "profile_seq" CASCADE;

  CREATE SEQUENCE "rig_seq" START 1;
  CREATE SEQUENCE "user_seq" START 1;
  CREATE SEQUENCE "profile_seq" START 1;

  CREATE TABLE "rigs" (
    "id"   BIGINT PRIMARY KEY DEFAULT "nextval"('"rig_seq"'),
    "name" VARCHAR(100) NOT NULL,
    "user_id" BIGINT NOT NULL,
    "password" VARCHAR(100) NOT NULL
  );

  CREATE TABLE "users" (
    "id"       BIGINT PRIMARY KEY DEFAULT "nextval"('"user_seq"'),
    "username" VARCHAR(100) NOT NULL,
    "password" VARCHAR(100) NOT NULL,
    "email" VARCHAR(100) NOT NULL,
    "account_non_expired" BOOLEAN NOT NULL,
    "account_non_locked" BOOLEAN NOT NULL,
    "credentials_non_expired" BOOLEAN NOT NULL,
    "enabled" BOOLEAN NOT NULL
  );

  CREATE TABLE "user_profiles" (
    "id"    INTEGER PRIMARY KEY DEFAULT "nextval"('"profile_seq"'),
    "type"  VARCHAR(100) UNIQUE
  );
  INSERT INTO "user_profiles" VALUES (1,'ROLE_ADMIN'),(2,'ROLE_USER');

  CREATE TABLE "user_and_profile" (
    "user_id"    BIGINT,
    "profile_id" INTEGER,
    PRIMARY KEY ("user_id", profile_id),
    FOREIGN KEY ("user_id") REFERENCES "users" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION,
    FOREIGN KEY ("profile_id") REFERENCES "user_profiles" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION
  );

END TRANSACTION;