--
DROP TABLE IF EXISTS cell_phone;
DROP TABLE IF EXISTS cell_usage_month;

CREATE SEQUENCE hibernate_sequence START WITH 1000;

CREATE TABLE cell_phone (
    cell_phone_id       SERIAL
,   employee_id         INT             NOT NULL
,   employee_name       VARCHAR(250)    NOT NULL
,   purchase_date       DATE            NOT NULL
,   model               VARCHAR(250)    NOT NULL
--
,   CONSTRAINT cell_phone_pk PRIMARY KEY( cell_phone_id )
);

CREATE TABLE cell_usage_month (
    cell_usage_month_id         SERIAL
,   employee_id                 INT    NOT NULL
,   usage_date                  DATE   NOT NULL
,   total_minutes               INT    NOT NULL
,   total_data                  FLOAT  NOT NULL
--
,   CONSTRAINT cell_usage_month_pk PRIMARY KEY( cell_usage_month_id )
);

/*
Details Section : For each company cell phone provide the following information
    Employee Id
    Employee Name
    Model
    Purchase Date
    Minutes Usage : one column for each month
    Data Usage    : one column for each month
*/
DROP VIEW IF EXISTS cell_phone_summary_v;
CREATE VIEW cell_phone_summary_v
AS
    SELECT  row_number() over() AS id
    ,       P.employee_id
    ,       P.employee_name
    ,       P.model
    ,       P.purchase_date
    ,       SUM(COALESCE(M.total_minutes, 0))  AS minutes_usage
    ,       SUM(COALESCE(M.total_data, 0))     AS data_usage
    --
    FROM    cell_phone P
            LEFT OUTER JOIN cell_usage_month M
                ON M.employee_id = P.employee_id
    GROUP BY P.employee_id, P.employee_name, P.model, P.purchase_date
;

SELECT * FROM cell_phone_summary_v;

/*
Header Section
    Report Run Date
    Number of Phones
    Total Minutes
    Total Data
    Average Minutes
    Average Data
 */
DROP VIEW IF EXISTS cell_phone_usage_summary_v;
CREATE VIEW cell_phone_usage_summary_v
AS
    SELECT  row_number() over() AS id
    ,       report_date
    ,       number_of_phones
    ,       total_minutes
    ,       total_data
    ,       total_minutes / number_of_phones    AS average_minutes
    ,       total_data / number_of_phones       AS average_data
    --
    FROM    (
        SELECT  CURRENT_DATE        AS report_date
        ,       count(*)            AS number_of_phones
        ,       SUM(minutes_usage)  AS total_minutes
        ,       SUM(data_usage)     AS total_data
        --
        FROM    cell_phone_summary_v
    ) A
;

SELECT * FROM cell_phone_usage_summary_v;
