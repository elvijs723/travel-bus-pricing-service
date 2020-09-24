# travel-bus-pricing-service
Simple WEB Service that provides pricing for bus routes

### Required
Apache Maven 3.3 or above

### to package WAR file run
mvn clean package

### to run spring-boot war with in-built webserver locally
mvn spring-boot:run

### to run tests
mvn test

### the web service

One POST end-point for receiving pricings for given route:
```
http://localhost:8081/pricing/bus/draft/route
```
For formatted response:
```
http://localhost:8081/pricing/bus/draft/route?format=true
```
POST request BODY:
```
[
    {
        "isInfant": false,
        "luggageItems": [
            {
                "itemName" : "coffin"
            },
            {
                "itemName" : "backpack"
            }
        ]
    },
    {
        "isInfant": true,
        "luggageItems": [
            {
                "itemName" : "coffer"
            }
        ]
    }
]
```
