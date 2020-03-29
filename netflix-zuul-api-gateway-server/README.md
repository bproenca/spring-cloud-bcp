
## how to make a request go through Zuul


1- original
http://localhost:8000/currency-exchange/from/EUR/to/INR

2- template
http://localhost:8765/{application-name}/{uri}

3- parsed
http://localhost:8765/currency-exchange-service/currency-exchange/from/EUR/to/INR