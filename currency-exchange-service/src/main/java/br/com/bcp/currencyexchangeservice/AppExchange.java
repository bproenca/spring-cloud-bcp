package br.com.bcp.currencyexchangeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class AppExchange {

	public static void main(String[] args) {
		System.out.println("*** " + System.getProperty("java.version") + " ***");
		System.out.println("*** " + System.getProperty("java.vendor") + " ***");

		SpringApplication.run(AppExchange.class, args);
	}

}
