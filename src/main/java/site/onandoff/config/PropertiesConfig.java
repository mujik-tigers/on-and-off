package site.onandoff.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import site.onandoff.auth.application.TokenProperties;

@Configuration
@EnableConfigurationProperties(value = {TokenProperties.class})
public class PropertiesConfig {
	
}
