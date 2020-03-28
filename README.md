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
2. Eureka Naming Server
    * http://localhost:8761/
3. Limits Service
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

