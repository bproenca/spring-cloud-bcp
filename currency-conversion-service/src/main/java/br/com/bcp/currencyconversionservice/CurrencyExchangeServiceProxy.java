package br.com.bcp.currencyconversionservice;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//  Plain Project:
//  1- Add Feign (pom):  
//    @FeignClient(name="currency-exchange-service", url="localhost:8000") // without service discovery (eureka) you have to hardcode URL
//  2- Add Ribbon:  
//    @FeignClient(name="currency-exchange-service")
//    @RibbonClient(name="currency-exchange-service") [seens to be optional]
//    applicatin.properties
//      currency-exchange-service.ribbon.listOfServers=http://localhost:8000,http://localhost:8001

//@FeignClient(name="currency-exchange-service", url="localhost:8000") // without service discovery (eureka) you have to hardcode URL
@FeignClient(name="currency-exchange-service")
@RibbonClient(name="currency-exchange-service")
//@FeignClient(name="netflix-zuul-api-gateway-server")
public interface CurrencyExchangeServiceProxy {

    // Sem Zuul VVVVVV
    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    // Com Zuull VVVVV
    // @GetMapping("/currency-exchange-service/currency-exchange/from/{from}/to/{to}")
    public CurrencyConversionBean retrieveExchangeValue(@PathVariable("from") String from, @PathVariable("to") String to);

}