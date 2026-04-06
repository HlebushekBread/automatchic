package net.softloaf.automatchic.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"net.softloaf.automatchic.app", "net.softloaf.automatchic.common"})
public class AutomatchicApplication {

	public static void main(String[] args) {
		SpringApplication.run(AutomatchicApplication.class, args);
	}

}
