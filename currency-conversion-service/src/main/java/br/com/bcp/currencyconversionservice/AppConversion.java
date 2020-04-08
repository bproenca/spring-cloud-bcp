package br.com.bcp.currencyconversionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

//import brave.sampler.Sampler;

@SpringBootApplication
@EnableFeignClients("br.com.bcp.currencyconversionservice")
//@EnableDiscoveryClient
public class AppConversion {

	public static void main(String[] args) {
		System.out.println("*** " + System.getProperty("java.version") + " ***");
		System.out.println("*** " + System.getProperty("java.vendor") + " ***");

		SpringApplication.run(AppConversion.class, args);
	}

	/*
	@Bean
	public Sampler defaultSampler() {
		return Sampler.ALWAYS_SAMPLE;
	}
*/
}
