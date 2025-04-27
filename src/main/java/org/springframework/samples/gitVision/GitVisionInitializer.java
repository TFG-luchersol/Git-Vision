package org.springframework.samples.gitvision;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.samples.gitvision.configuration.DotenvInitializer;

public class GitVisionInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application
            .initializers(new DotenvInitializer())
            .sources(GitVisionApplication.class);
	}

}
