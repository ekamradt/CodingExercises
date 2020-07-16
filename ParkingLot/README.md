## Parking Lot

Challenge

Design a system to manage a parking lot business

##### Requirements:

Charge customers hourly or monthly for parking.

Provide options for covered or uncovered parking (at different prices)
Functions. 

* Ability for customers to check-in
* Ability for customers to check-out
* Provide a daily revenue report to the business owner


#### Database Outline Prototype

[DB.sql](DB.sql)

#### Rest APIs

###### POST .../api/v1/login
    * At first users must log in through Google, Yahoo, or Microsoft connected logins and their 
        successful login email is compaired to an emaii in our database.  Or a customer is created.

###### POST .../api/v1/lot/checkin/<lot_id>/<customer_id>
    * Starts the clock on a parking event.
    * Save a record in pl_customer_lot with the 
        * customer
        * which lot
        * start time
        * parking_type (HOURLY, MONTHLY)
    
###### POST .../api/v1/lot/checkout/<lot_id>/<customer_id>
    * Ends a parking event for a customer
    * Updates pl_customer_lot record with 
        * end_time
        * calculates price based on hours/monthly (price_paid)
    * Payments are made throu Google pay, PayPal, etc. We link user by email
        * More details are beyond the scope of this exercise.
        
###### GET  .../api/v1/report/daily
###### GET  .../api/v1/report/summary

###### POST .../api/v1/customer/create (w/ json body)
###### PUT .../api/v1/customer/update (w/ json body)
    * Update customer can make a customer inactive (we choose to save all data). So no delete.

###### POST .../api/v1/lot/create (w/ json body)
###### PUT .../api/v1/lot/update (w/ json body)
    * Update lot can make a lot inactive (we choose to save all data).  So no delete.


