package br.com.bcp.currencyconversionservice;

//import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="currency-exchange-service", url="localhost:8000") // with service discovery you don't need to hardcode URL
//@FeignClient(name="currency-exchange-service")
//@FeignClient(name="netflix-zuul-api-gateway-server")
//@RibbonClient(name="currency-exchange-service")
public interface CurrencyExchangeServiceProxy {

    // Sem Zuul VVVVVV
    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    // Com Zuull VVVVV
    // @GetMapping("/currency-exchange-service/currency-exchange/from/{from}/to/{to}")
    public CurrencyConversionBean retrieveExchangeValue(@PathVariable("from") String from, @PathVariable("to") String to);

}