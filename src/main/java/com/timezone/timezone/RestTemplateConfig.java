package com.timezone.timezone;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


/**
 * added template config
 * @author yadla
 *
 */
@Configuration
public class RestTemplateConfig {
	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

}
