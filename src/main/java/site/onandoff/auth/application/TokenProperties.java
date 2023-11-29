package site.onandoff.auth.application;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@ConfigurationProperties("jwt")
@RequiredArgsConstructor
@Getter
public class TokenProperties {

	private final long accessTokenDuration;
	private final long refreshTokenDuration;
	private final String issuer;
	private final String secret;

}
