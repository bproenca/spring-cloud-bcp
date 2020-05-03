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
On both projects:
```
docker push bproenca/currency-exchange-service-k8s-gcp:1.0
docker push bproenca/currency-conversion-service-k8s-gcp:1.0

gcloud container clusters resize --zone us-central1-a cluster-bcp --num-nodes=3
kubectl apply -f deployment.yaml

kubectl delete -f deployment.yaml
gcloud container clusters resize --zone us-central1-a cluster-bcp --num-nodes=0
```
