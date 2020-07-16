--
DROP TABLE IF EXISTS uv_campsite_user CASCADE;
DROP TABLE IF EXISTS uv_campsite CASCADE;
DROP TABLE IF EXISTS uv_user CASCADE;

DROP SEQUENCE IF EXISTS hibernate_sequence;
CREATE SEQUENCE hibernate_sequence START WITH 1000;

CREATE TABLE uv_campsite (
    campsite_id         SERIAL
,   campsite_name       VARCHAR(250) NOT NULL
,   created_dt          timestamptz NOT NULL default CURRENT_TIMESTAMP
,   updated_dt          timestamptz NOT NULL default CURRENT_TIMESTAMP
,   CONSTRAINT cell_phone_pk PRIMARY KEY( campsite_id )
);

INSERT INTO uv_campsite (campsite_name) VALUES ('Campsite # 1'); -- Id: 1

CREATE TABLE uv_user (
    user_id             SERIAL
,   user_email          varchar(256) NOT NULL
,   user_first_name     varchar(256)
,   user_middle_name    varchar(256)
,   user_last_name      varchar(256)
,   created_dt          timestamptz NOT NULL default CURRENT_TIMESTAMP
,   updated_dt          timestamptz NOT NULL default CURRENT_TIMESTAMP
--
,   CONSTRAINT uv_user_pk PRIMARY KEY( user_id )
);

INSERT INTO uv_user (user_email, user_first_name, user_last_name ) VALUES
 ( 'a@a.com', 'Afirst', 'Alast') -- Id: 1
,( 'b@b.com', 'Bfirst', 'Blast') -- Id: 2
,( 'c@c.com', 'Cfirst', 'Clast') -- Id: 3
,( 'd@d.com', 'Dfirst', 'Dlast') -- Id: 4
,( 'e@e.com', 'Efirst', 'Elast') -- Id: 5
,( 'f@f.com', 'Ffirst', 'Flast') -- Id: 6
;

CREATE TABLE uv_campsite_user (
    campsite_user_id    SERIAL
,   campsite_id         INT NOT NULL
,   user_id             INT NOT NULL
,   arrival_dt          DATE NOT NULL
,   depart_dt           DATE NOT NULL
,   created_dt          timestamptz NOT NULL default CURRENT_TIMESTAMP
,   updated_dt          timestamptz NOT NULL default CURRENT_TIMESTAMP
--
,   CONSTRAINT uv_campsite_user_pk PRIMARY KEY( campsite_user_id )
,   CONSTRAINT uv_campsite_user_user FOREIGN KEY (user_id) REFERENCES uv_user(user_id)
,   CONSTRAINT uv_campsite_user_campsite FOREIGN KEY (campsite_id) REFERENCES uv_campsite(campsite_id)
);

DROP VIEW IF EXISTS uv_campsite_available_v;
CREATE VIEW uv_campsite_available_v
AS
    SELECT  avail_date
    FROM (
        SELECT DATEADD(DAY, counter, CURRENT_DATE) AS avail_date
        ,      CU.user_id IS NOT NULL              AS is_booked
        FROM (
                 SELECT X AS counter
                 FROM   system_range(1,30)
             ) A
            LEFT OUTER JOIN uv_campsite_user CU
                ON A.avail_date BETWEEN CU.arrival_dt AND CU.depart_dt
    ) A
    WHERE   is_booked IS NULL
;

