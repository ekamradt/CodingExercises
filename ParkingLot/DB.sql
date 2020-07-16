

/***********************************
Challenge

Design a system to manage a parking lot business

##### Requirements:

Charge customers hourly or monthly for parking.

Provide options for covered or uncovered parking (at different prices)
Functions.

* Ability for customers to check-in
* Ability for customers to check-out
* Provide a daily revenue report to the business owner
 */

DROP VIEW IF EXISTS v_daily_detail_report;
DROP VIEW IF EXISTS v_daily_summary_report;
DROP TABLE IF EXISTS  pl_customer CASCADE;
DROP TABLE IF EXISTS  pl_lot CASCADE;
DROP TABLE IF EXISTS  pl_customer_lot CASCADE;
DROP TYPE IF EXISTS e_parking_type;
DROP TYPE IF EXISTS e_parking_cover_type;

CREATE TYPE e_parking_type AS ENUM ('HOURLY','MONTHLY');
CREATE TYPE e_parking_cover_type AS ENUM ('OPEN','COVERED');

CREATE TABLE pl_customer (
    customer_id     serial      PRIMARY KEY
,   first_name      varchar(64)
,   last_name       varchar(64)
,   email           varchar(512)
);

CREATE TABLE pl_lot (
    lot_id              serial  PRIMARY KEY
,   address             text
,   parking_cover_type  e_parking_cover_type
,   price_per_hour      float
,   price_per_month     float
);

CREATE TABLE pl_customer_lot (
    customer_lot_id     serial PRIMARY KEY
,   customer_id         int
,   lot_id              int
,   parking_type        e_parking_type -- HOURLY, MONTHLY
,   price_paid          float -- assume everyone pays full cost at the time of parking
,   start_time          timestamp   -- Date/Time
,   end_time            timestamp   -- Date/Time
);

-- ==============================
-- Report is based on date, cars parked and cars paid that day.
-- ==============================
CREATE VIEW v_daily_detail_report
AS
    SELECT  L.lot_id
    ,       L.address
    ,       L.parking_cover_type
    ,       CL.parking_type
    ,       CL.start_time
    ,       CL.end_time
    ,       CL.price_paid
    FROM    pl_lot  L
            INNER JOIN pl_customer_lot CL
                       ON  CL.lot_id = L.lot_id
                       AND CURRENT_DATE BETWEEN CL.start_time AND CL.end_time
;

CREATE VIEW v_daily_summary_report
AS
    SELECT  lot_id
    ,       address
    ,       parking_cover_type
    ,       SUM(CASE WHEN end_time IS NULL THEN 1 ELSE 0 END)   AS number_parked
    ,       SUM(CASE WHEN end_time IS NULL THEN 0 ELSE 1 END)   AS number_left_and_paid
    ,       SUM(price_paid)                                     AS daily_Total
    FROM    v_daily_detail_report
    GROUP BY lot_id, address, parking_cover_type
;
