# spring-cloud-bcp
Adapting the project to use an API gateway (Zuul)

Command to run
```
mvn spring-boot:run 
mvn spring-boot:run -Dserver.port=8001
```

## Start-up Order (and tests)

1. Spring Cloud Config Server **(optional)**
1. Eureka Naming Server (service registration / service discovery)
    * http://localhost:8761/
1. Exchange Service
    * Run 2 instances
    * mvn spring-boot:run (port 8000)
    * mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8001
1. Zuul Api Gateway
    * Direct Call: http://localhost:8765/currency-exchange-service/currency-exchange/from/EUR/to/INR 
    * Open Feign: http://localhost:8765/currency-conversion-service/currency-converter-feign/from/USD/to/INR/quantity/10|
    * Check available routes:
      * Add this property: `management.endpoints.web.exposure.include=health,env,routes`
      * http://localhost:8765/actuator/routes
1. Conversion (calculation) Service
    * http://localhost:8100/currency-converter/from/EUR/to/INR/quantity/10 [Without Feign]
    * http://localhost:8100/currency-converter-feign/from/EUR/to/INR/quantity/10 [With Feign + Zuul]


## Conversion >> Exchange:

New flow: Conversion >> Zull Api Gateway >> Exchange

### CurrencyExchangeServiceProxy.java
```java
//@FeignClient(name="currency-exchange-service")
@FeignClient(name="netflix-zuul-api-gateway-server")
@RibbonClient(name="currency-exchange-service")
public interface CurrencyExchangeServiceProxy {

    //@GetMapping("/currency-exchange/from/{from}/to/{to}")
    @GetMapping("/currency-exchange-service/currency-exchange/from/{from}/to/{to}")
    public CurrencyConversionBean retrieveExchangeValue(@PathVariable("from") String from, @PathVariable("to") String to);

}
```
