package net.softloaf.automatchic.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing
@EnableScheduling
@SpringBootApplication(scanBasePackages = {"net.softloaf.automatchic.app", "net.softloaf.automatchic.common"})
public class AutomatchicApplication {

	public static void main(String[] args) {
		SpringApplication.run(AutomatchicApplication.class, args);
	}

}
