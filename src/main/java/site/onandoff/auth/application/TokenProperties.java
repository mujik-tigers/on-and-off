package site.onandoff.auth.application;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Component
@ConfigurationProperties("jwt")
@RequiredArgsConstructor
@Getter
public class TokenProperties {

	private final long accessTokenDuration;
	private final long refreshTokenDuration;
	private final String issuer;
	private final String secret;

}
