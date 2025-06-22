# Hotel Reservation Service

## Perform the following to run the application

Run the following in the command prompt/powershell in the root of the application:
1. mvn clean install
2. java -jar .\target\hotel-reservation-service-0.0.1-SNAPSHOT.jar --spring.profiles.active=local

Or via docker cmd:
1. docker build . --tag=reservation-service:latest
2. docker run reservation-service:latest

FYI: Server runs on port 8080

### RE Question: An agency might have several hundred bookings how would you solve this issue through API
Can include pagination in the API, i.e. add the following query string parameters in the url: page=0&size=5
where page is which page to return, i.e. first page is 0, size is the number of rows to return per page.

## Design Implementation
- Contract/Swagger first. The contract as the source of truth and is what is supplied to the consumers of the service. Mvn generates the API from the contract using the openapitools plugin.
- Database timezone UTC
- Pre-loaded Hotels and Customers in the DB on startup

## Outstanding tasks and further recommendations for this project

### Architecture and non-functionals:
Backend should be hosted behind a gateway and WAF server (this is to help with throttling, ddos attacks and monitor for blacklisted ips)
Could consider a Backend For Front End service for authentication and orchestration.
Config Server to store configurations and a vault to securely store passwords
Backend secured preferably JWT with Oauth2 to an Identity Provider

### Missing functionality and improvements:
- Register and maintain Agents
- Register and maintain Hotels
- Register and maintain Customers
- Audit trail of what was changed, by whom and when.
- Improve unit testing. Just included the basic happy flow.
###  Missing integration functionality
- Integration into a reservation system to check availability, discounts and loyalty systems, cancellation policies etc.
  Must also be able to make changes to a reservation.
  I assume there must be some global system same as airlines. But I don't know.

