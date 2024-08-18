package org.springframework.samples.gitvision;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages={"org.springframework.samples.gitvision"})
public class GitVisionApplication {

	public static void main(String[] args) {
		SpringApplication.run(GitVisionApplication.class, args);
	}

}
