# Microservices k8s (google cloud platform)
This example shows how to run microservices on k8s (GCP).  
URL's (localhost or GCP-IP):
* http://localhost:8000/currency-exchange/from/EUR/to/INR
* http://localhost:8100/currency-conversion/from/EUR/to/INR/quantity/10

## Running local (without Docker)
On both projects:
```
mvn spring-boot:run 
```

## Running local (with Docker)
On both projects:
```
mvn clean install
```
and then ...
```
docker run --rm -p 8000:8000 bproenca/currency-exchange-service-k8s-gcp:1.0
docker run --rm -p 8100:8100 -e CURRENCY_EXCHANGE_URI=`hostname -I | awk '{print $1}'` bproenca/currency-conversion-service-k8s-gcp:1.0
```

## Running on GCP
Now we are going to deploy two instances of currency-exchange:
```
docker push bproenca/currency-exchange-service-k8s-gcp:1.0
docker push bproenca/currency-conversion-service-k8s-gcp:1.0

gcloud container clusters resize --zone us-central1-a cluster-bcp --num-nodes=3
kubectl apply -f deployment.yaml

kubectl delete -f deployment.yaml
gcloud container clusters resize --zone us-central1-a cluster-bcp --num-nodes=0
```
