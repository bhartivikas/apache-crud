package com.apacheapi.cms.config;

import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.configuration.server.ServerRuntime;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfig {
	
	@Bean
	ObjectContext objectContext() {
		
		ServerRuntime runtime = ServerRuntime.builder()
                .addConfig("cayenne-project-user.xml")
                .build();
		
		ObjectContext context = runtime.newContext();
		return context;

		
	}

}
