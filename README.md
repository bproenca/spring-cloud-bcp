# spring-cloud-bcp
Dockerizing the project

## Steps

1. Create a Dockerfile in each service
    ```docker
    FROM openjdk:13-alpine
    VOLUME /tmp
    EXPOSE 8100
    ADD target/*.jar app.jar
    ENV JAVA_OPTS=""
    ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
    ```
1. Add a maven plugin **dockerfile-maven-plugin** in each project
1. Create **docker-compose.yml**
1. Modify *application.properties* to reference Naming Service by **docker service name**
    * `eureka.client.service-url.defaultZone=http://naming-server:8761/eureka/`
1. Conversion calls Exchange using Feign (host is resolved in Eureka)
    ```java
    //find URL in the naming service (eureka)
    @FeignClient(name = "currency-exchange-service")
    public interface CurrencyExchangeServiceProxy {
        @GetMapping("/currency-exchange/from/{from}/to/{to}")
        public CurrencyConversionBean retrieveExchangeValue(@PathVariable("from") String from, @PathVariable("to") String to);
    }
    ```

## Start-up Order (and tests)

```
mvn clean package
```

1. Eureka Naming Server (service registration / service discovery)
    * http://localhost:8761/
1. Exchange Service
    * http://localhost:8000/currency-exchange/from/EUR/to/INR
1. Conversion (calculation) Service
    * http://localhost:8100/currency-converter-feign/from/EUR/to/INR/quantity/10


```
docker-compose up
```

