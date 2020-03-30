package br.com.bcp.currencyconversionservice;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CurrencyConversionController {

        private Logger logger = LoggerFactory.getLogger(CurrencyConversionController.class);

        @Autowired
        private CurrencyExchangeServiceProxy exchangeProxy;

        /*
         * call external service (exchange) usign RestTemplate
         */
        @GetMapping("/currency-converter/from/{from}/to/{to}/quantity/{quantity}")
        public CurrencyConversionBean convertCurrencyRestTemplate(@PathVariable String from, @PathVariable String to,
                        @PathVariable BigDecimal quantity) {

                Map<String, String> uriVariables = new HashMap<>();
                uriVariables.put("from", from);
                uriVariables.put("to", to);

                ResponseEntity<CurrencyConversionBean> responseEntity = new RestTemplate().getForEntity(
                                "http://localhost:8000/currency-exchange/from/{from}/to/{to}",
                                CurrencyConversionBean.class, uriVariables);

                CurrencyConversionBean response = responseEntity.getBody();
                logger.info("{}", response);

                CurrencyConversionBean currencyConversionBean = new CurrencyConversionBean(
                        response.getId(), from, to,
                        response.getConversionMultiple(), quantity,
                        quantity.multiply(response.getConversionMultiple()));

                currencyConversionBean.setPort(response.getPort());
                currencyConversionBean.setHostName(response.getHostName());
                currencyConversionBean.setHostAddress(response.getHostAddress());
                return currencyConversionBean;
        }

         // call external service (exchange) usign Feign
         @GetMapping("/currency-converter-feign/from/{from}/to/{to}/quantity/{quantity}")
         public CurrencyConversionBean convertCurrencyFeign(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity) {
                 
                 CurrencyConversionBean response = exchangeProxy.retrieveExchangeValue(from, to);
                 
                 CurrencyConversionBean currencyConversionBean = new CurrencyConversionBean(
                        response.getId(), from, to,
                        response.getConversionMultiple(), quantity,
                        quantity.multiply(response.getConversionMultiple()));
                 
                 currencyConversionBean.setPort(response.getPort());
                 currencyConversionBean.setHostName(response.getHostName());
                 currencyConversionBean.setHostAddress(response.getHostAddress());
                 
                 logger.info("{}", response);
                 return currencyConversionBean;
        }
        
}