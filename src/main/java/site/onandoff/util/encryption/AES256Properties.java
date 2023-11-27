package site.onandoff.util.encryption;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@ConfigurationProperties("aes256")
@RequiredArgsConstructor
@Getter
public class AES256Properties {

	private final String algorithm;
	private final String key;
	private final String initialVector;

}
