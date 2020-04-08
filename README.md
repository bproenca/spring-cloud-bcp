# spring-cloud-bcp
Dockerizing the project (stack)

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
1. Conversion calls Exchange using Feign (host is resolved in Eureka)
    ```java
    @FeignClient(name = "currency-exchange-service", url = "${CURRENCY_EXCHANGE_URI}")
    public interface CurrencyExchangeServiceProxy {
        @GetMapping("/currency-exchange/from/{from}/to/{to}")
        public CurrencyConversionBean retrieveExchangeValue(@PathVariable("from") String from, @PathVariable("to") String to);
    }
    ```

## Start-up Order (and tests)

```
mvn clean package
```

1. Exchange Service
    * 2 replicas are running (see docker-compose)
    * Load balance is done by swarm
    * http://localhost:8000/currency-exchange/from/EUR/to/INR
1. Conversion (calculation) Service
    * http://localhost:8100/currency-converter-feign/from/EUR/to/INR/quantity/10


```
docker swarm init
docker stack deploy -c docker-compose.yml bcp-currency
docker stack ps bcp-currency
docker stack rm bcp-currency
```

