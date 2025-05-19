package org.springframework.samples.gitvision;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.samples.gitvision.configuration.DotenvInitializer;

@SpringBootApplication(scanBasePackages={"org.springframework.samples.gitvision"})
public class GitVisionApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(GitVisionApplication.class);
        app.addInitializers(new DotenvInitializer());
        app.run(args);
	}

}
