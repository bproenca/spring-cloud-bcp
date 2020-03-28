package br.com.bcp.currencyexchangeservice;

import java.net.InetAddress;
import java.net.UnknownHostException;

//import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CurrencyExchangeController {

    private Logger logger = LoggerFactory.getLogger(CurrencyExchangeController.class);

    @Autowired
    private Environment env;

    @Autowired
    private ExchangeValueRepository rep;

    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    public ExchangeValue retrieveExchangeValue(@PathVariable String from, @PathVariable String to) {

        ExchangeValue exchangeValue = rep.findByFromAndTo(from, to);
        //ExchangeValue exchangeValue = new ExchangeValue(100L, from, to, BigDecimal.valueOf(123));
        exchangeValue.setPort(Integer.parseInt(env.getProperty("local.server.port")));

        try {
            InetAddress localHost = InetAddress.getLocalHost();
            exchangeValue.setHostName(localHost.getHostName());
            exchangeValue.setHostAddress(localHost.getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        logger.info("{}", exchangeValue);
        return exchangeValue;
    }

}