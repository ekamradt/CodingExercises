# Interview Code Request 

### Overview
When you run the application the Report prints to the console. The secret of the
report is a database view, in the file src/main/resources/db/migration/SQL_01.sql

The application stays active and allows REST queries to fetch the report and get and add cell 
phones. If this were a real app, we'd add CRUD for cell phones and their usage, as well as, search filters
and pagination.

### Stack
* Java 11
* Spring Boot/Data/Rest
* Spring Boot (see src/main/java/com/example/wcf/WcfApplication.java)
* Spring Rest (see src/main/java/com/example/wcf/controller/CellPhoneController.java)
* H2 memory database
* Database Setup by Liquibase (see src/main/resources/db/migration/SQL_01.sql)
* Lombok

### Running the Application
The below are just examples of what can be done. and are terribly incompete for a production application.  Also, I've
spent some hours on this and haven't written unit/mocked tests yet.  I will if asked.

* When the application run the report is printed to the console
* You can also get the report with the URL 
  * http://localhost:8080/api/report
  * http://localhost:8080/api/report/json
* Add Cell Phone Example : POST http://localhost:8080/api/cellPhone
``` 
curl --header "Content-Type: application/json" \
  --request POST \
  --data '{ "employeeId": 9999, "employeeName": "Elmo Washington", "model": "Pizza Brand", "purchaseDate": "20200101" }' \
  http://localhost:8080/api/cellPhone
```  
* Update Cell Phone Example : PUT http://localhost:8080/api/cellPhone/{ID}
```
curl --header "Content-Type: application/json" \
    --request PUT \
    --data '{ "employeeId": 9999, "employeeName": "Elmo Johnson Washington", "model": "Pizza Brand Phone", "purchaseDate": "20200101" }' \
    http://localhost:8080/api/cellPhone
```

* Fetch CellPhone Example : GET http://localhost:8080/api/cellPhone/{ID}
```
curl --header "Content-Type: application/json" \
  --request GET \
  http://localhost:8080/api/cellPhone/10
```

### Build & Run Jar

mvn clean package spring-boot:repackage

java -jar ./target/wcf-0.0.1-SNAPSHOT.jar



