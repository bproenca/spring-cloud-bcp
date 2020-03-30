package br.com.bcp.limitsservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.bcp.limitsservice.bean.LimitConfiguration;

@RestController
@RefreshScope
//Allow to refresh config properties
//Need to enable "actuator/refresh" endpoint (bootstrap.properties)
//management.endpoints.web.exposure.include=health,env,refresh
public class LimitsConfigurationController {

    private Logger logger = LoggerFactory.getLogger(LimitsConfigurationController.class);

    @Autowired
    private Configuration configuration;

    @GetMapping("/limits")
    public LimitConfiguration retrieveLimitisFromConfiguration() {
        return new LimitConfiguration(configuration.getMinimum(), configuration.getMaximum());
    }

    @GetMapping("/fault-tolerance-example")
    @HystrixCommand(fallbackMethod = "fallbackRetrieveConfiguration")
    public LimitConfiguration retrieveConfiguration() {
        String errorMessage = "## Error to test fallback ##";
        logger.info("Not Available {}", errorMessage);
        throw new RuntimeException(errorMessage);
    }

    public LimitConfiguration fallbackRetrieveConfiguration() {
        return new LimitConfiguration(101, 202);
    }


    @Value("${message:Hello default}")
    private String message;

    @RequestMapping("/message")
    String getMessage() {
        return this.message;
    }
}
