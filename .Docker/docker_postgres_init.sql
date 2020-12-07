CREATE USER conta1 WITH PASSWORD 'conta1' CREATEDB;
CREATE DATABASE conta1
    WITH
    OWNER = conta1
    ENCODING = 'UTF8'
    LC_COLLATE = 'en_US.utf8'
    LC_CTYPE = 'en_US.utf8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;