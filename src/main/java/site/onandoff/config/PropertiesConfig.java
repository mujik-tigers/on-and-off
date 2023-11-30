package site.onandoff.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import site.onandoff.auth.application.TokenProperties;
import site.onandoff.util.encryption.AES256Properties;

@Configuration
@EnableConfigurationProperties(value = {AES256Properties.class, TokenProperties.class})
public class PropertiesConfig {
	
}
