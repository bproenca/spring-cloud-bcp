# spring-cloud-bcp
Distributed Tracing

**Sleuth**: assigns an unique ID to the request (through all microservices).  
**Zipkin**: middleware (*see above how to install*) aggregates logs from all microservices (group by sleuth ID). Zipkin consumes logs from a Queue.  
**sleuth-zipkin**: creates messages (from logs) compatible with Zipkin.  
**bus-amqp**: sends message to **amqp** (default = RabbitMQ)

>>As long as Spring Cloud Bus AMQP and RabbitMQ are on the classpath any Spring Boot application will try to contact a RabbitMQ server on localhost:5672 (the default value of spring.rabbitmq.addresses)

Sleuth and Zipkin added to the projects: *Zuul, Conversion and Exchange*.  

1. Add dependency to pom.xml:  
  ```xml
      <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-zuul</artifactId>
      </dependency>
      <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-sleuth</artifactId>
      </dependency>
      <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-sleuth-zipkin</artifactId>
      </dependency>
      <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-bus-amqp</artifactId>
      </dependency>
  ```
2. Add Sampler:

```java
...
import brave.sampler.Sampler;

@EnableZuulProxy
@EnableDiscoveryClient
@SpringBootApplication
public class AppNetflixZuulApiGatewayServer {
  ...
  
	@Bean
	public Sampler defaultSampler() {
		return Sampler.ALWAYS_SAMPLE;
	}
}
```

**Command to run:**
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
## Install RabbitMQ and Zipkin

```docker
version: "3"
services:
  rabbitMq:
    image: rabbitmq
    hostname: mqbcp
    ports:
      - "5672:5672"
      - "15672:15672"
    networks:
      - bcpnet

  zipkin:
    image: openzipkin/zipkin
    depends_on:
      - rabbitMq
    ports:
      - "9411:9411"
    environment:
      - "RABBIT_URI=amqp://mqbcp"
    networks:
      - bcpnet

networks:
  bcpnet:

```