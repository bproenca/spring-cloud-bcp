# spring-cloud-bcp
Spring Cloud Example

Command to run
```
mvn spring-boot:run 
mvn spring-boot:run -Dserver.port=8001
```

## Start-up Order (and tests)

1. Spring Cloud Config Server
    * http://localhost:8888/limits-service/default
    * http://localhost:8888/limits-service/qa
    * Config Storage
      * uri: https://github.com/bproenca/spring-cloud-bcp-config-repo.git
      * Files in this Repo:
        * limits-service.properties
        * limits-service-qa.properties
        * *qa inherits default properties (eg. maximum property):*
        ```json
        {
          "name": "limits-service",
          "profiles": [
            "qa"
          ],
          "label": null,
          "version": "550508df700d3e0729f25f3a4de49dabc34277d9",
          "state": null,
          "propertySources": [
            {
              "name": "https://github.com/bproenca/spring-cloud-bcp-config-repo.git/limits-service-qa.properties",
              "source": {
                "limits-service.minimum": "4321",
                "message": "Ola Mundo"
              }
            },
            {
              "name": "https://github.com/bproenca/spring-cloud-bcp-config-repo.git/limits-service.properties",
              "source": {
                "limits-service.minimum": "11",
                "limits-service.maximum": "1111",
                "message": "Hello World"
              }
            }
          ]
        }
        ```
2. Eureka Naming Server (service registration / service discovery)
    * http://localhost:8761/
3. Limits Service
    * This service loads it's configuration from Config Server
    * http://localhost:8080/limits
    * http://localhost:8080/message [profile = qa]
    * http://localhost:8080/actuator/health
    * change and commit file 
      * https://github.com/bproenca/spring-cloud-bcp-config-repo/blob/master/limits-service-qa.properties
    * Refresh config
      * [POST] http://localhost:8080/actuator/refresh
    * Check again
      * http://localhost:8080/message
4. Exchange Service
    * Run 2 instances
    * mvn spring-boot:run (port 8000)
    * mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8001
    * http://localhost:8000/h2-console
      * JDBC URL: jdbc:h2:mem:testdb
      * Query: `select * from exchange_value`
    * http://localhost:8000/currency-exchange/from/EUR/to/INR
5. Conversion (calculation) Service
    * http://localhost:8100/currency-converter/from/EUR/to/INR/quantity/10 [Without Feign]
    * http://localhost:8100/currency-converter-feign/from/EUR/to/INR/quantity/10


## Conversion >> Exchange:

Conversion Service consumes an API from Exchange Service.  
**Feign** is used for API communication (make this call simple).  
Exchange can have many instances running, add **Ribbon** to Load Balance between all instances (hardcoded list of servers).  
*The problem is when new instances are added/removed.*  
**Eureka** (together with Feign) acts as a service discovery (+load balance). So when new instances of Exchange go Up/Down, Conversion service is not affected (only healthy instances are routed)

### Implementation step-by-step  (CurrencyExchangeServiceProxy.java)
1. Add Feign (Java to HTTP client binder):  
    ```java
    @FeignClient(name="currency-exchange-service", url="localhost:8000") // without service discovery (eureka) you have to hardcode URL
    ```
2. Add Ribbon (Client Side Load Balancer):  
    ```java
    @FeignClient(name="currency-exchange-service")
    @RibbonClient(name="currency-exchange-service")
    ```
    applicatin.properties
    ```properties
    currency-exchange-service.ribbon.listOfServers=http://localhost:8000,http://localhost:8001
    ```
3. Add Eureka (Service Registration / Service Discovery)
    * Proxy - doen't change
    * application.properties
      ```properties
      currency-exchange-service.ribbon.listOfServers=http://localhost:8000,http://localhost:8001
      eureka.client.service-url.default-zone=http://localhost:8761/eureka
      ```
    * Add this annotation to the main class: `@EnableDiscoveryClient`
    * Now if you add a new service (exchange), it will automatically be routed once registered in Eureka 
4. *Optional*
    * Since Eureka client also has a built-in load balancer that does basic round-robin load balancing.
    * You can remove Ribbon from the project

## Config Server (refresh all instances)

Scenario: run several instances of Limits Service  
Event: a configuration (from Config Server) is changed  
Solution: you have to POST **all** instances `/actuator/refresh` in order to retrieve the current value

Workaround *limits-service projetct*:  
* Run RabbitMQ server
* Add **spring-cloud-starter-bus** dependency
* Add `management.endpoints.web.exposure.include=*` (or refresh)
* Postman [POST] http://localhost:8080/actuator/bus-refresh

## Fault Tolerance with Hystrix

* Add dependency: `spring-cloud-starter-hystrix` 
* `@EnableHystrix` at startup class
* Implement fallback:
```java
    ...
    @GetMapping("/fault-tolerance-example")
    @HystrixCommand(fallbackMethod = "fallbackRetrieveConfiguration")
    public LimitConfiguration retrieveConfiguration() {
        String errorMessage = "## Error to test fallback ##";
        logger.info("Not Available {}", errorMessage);
        throw new RuntimeException(errorMessage);
    }

    public LimitConfiguration fallbackRetrieveConfiguration() {
        return new LimitConfiguration(101, 202);
    }
```

## Ports

|     Application       |     Port          |
| ------------- | ------------- |
| Limits Service | 8080, 8081, ... |
| Spring Cloud Config Server | 8888 |
|  |  |
| Currency Exchange Service | 8000, 8001, 8002, ..  |
| Currency Conversion Service | 8100, 8101, 8102, ... |
| Netflix Eureka Naming Server | 8761 |
| Netflix Zuul API Gateway Server | 8765 |
| Zipkin Distributed Tracing Server | 9411 |


## URLs

|     Application       |     URL          |
| ------------- | ------------- |
| Limits Service | http://localhost:8080/limits POST -> http://localhost:8080/actuator/refresh|
|Spring Cloud Config Server| http://localhost:8888/limits-service/default http://localhost:8888/limits-service/dev |
|  Currency Converter Service - Direct Call| http://localhost:8100/currency-converter/from/USD/to/INR/quantity/10|
|  Currency Converter Service - Feign| http://localhost:8100/currency-converter-feign/from/EUR/to/INR/quantity/10000|
| Currency Exchange Service | http://localhost:8000/currency-exchange/from/EUR/to/INR http://localhost:8001/currency-exchange/from/USD/to/INR|
| Eureka | http://localhost:8761/|
| Zuul - Currency Exchange & Exchange Services | http://localhost:8765/currency-exchange-service/currency-exchange/from/EUR/to/INR http://localhost:8765/currency-conversion-service/currency-converter-feign/from/USD/to/INR/quantity/10|
| Zipkin | http://localhost:9411/zipkin/ |
| Spring Cloud Bus Refresh | http://localhost:8080/bus/refresh |

## VM Argument

-Dserver.port=8001

