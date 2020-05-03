## Running local (without Docker)
On both projects:
```
mvn spring-boot:run 
```

## Running local (with Docker)
On both projects:
```
mvn clean install
docker run --rm -p 8000:8000 bproenca/currency-exchange-service-k8s-gcp:1.0

docker run --rm -p 8100:8100 -e SPRING_OPTS='--CURRENCY_EXCHANGE_URI=192.168.0.20' bproenca/currency-conversion-service-k8s-gcp:1.0
docker run --rm -p 8100:8100 -e SPRING_OPTS='--CURRENCY_EXCHANGE_URI='`hostname -I | awk '{print $1}'` bproenca/currency-conversion-service-k8s-gcp:1.0
```

## Running on GCP
On both projects:
```
mvn clean install
docker push 
```
