package br.com.bcp.currencyconversionservice;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//  Plain Project:
//  1- Add Feign (pom):  
//    @FeignClient(name="currency-exchange-service", url="localhost:8000") // without service discovery (eureka) you have to hardcode URL
//  2- Add Ribbon (client side load labancer):  
//    @FeignClient(name="currency-exchange-service")
//    @RibbonClient(name="currency-exchange-service")
//    applicatin.properties
//      currency-exchange-service.ribbon.listOfServers=http://localhost:8000,http://localhost:8001
//  3- Add Eureka (service registration / service discovery)
//    Here (Proxy) - doen't change anything
//    application.properties
//        Remove  currency-exchange-service.ribbon.listOfServers=http://localhost:8000,http://localhost:8001
//        Add     eureka.client.service-url.default-zone=http://localhost:8761/eureka
//    Add this annotation to the main class: @EnableDiscoveryClient
//    Now if you add a new service (exchange), it will automatically be routed once registered in Eureka 
//  4- <<Optional>>
//    Since Eureka client also has a built-in load balancer that does basic round-robin load balancing.
//    You can remove Ribbon from the project

//@FeignClient(name="currency-exchange-service", url="localhost:8000") // without service discovery (eureka) you have to hardcode URL
@FeignClient(name="currency-exchange-service")
@RibbonClient(name="currency-exchange-service")
public interface CurrencyExchangeServiceProxy {

    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    public CurrencyConversionBean retrieveExchangeValue(@PathVariable("from") String from, @PathVariable("to") String to);

}