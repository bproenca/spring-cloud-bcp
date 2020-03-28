package br.com.bcp.springcloudconfigserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class AppConfigServer {

	public static void main(String[] args) {
		System.out.println("*** " + System.getProperty("java.version") + " ***");
		System.out.println("*** " + System.getProperty("java.vendor") + " ***");

		SpringApplication.run(AppConfigServer.class, args);
	}
}
