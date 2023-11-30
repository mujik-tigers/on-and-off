package site.onandoff.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AuthenticationTokenPair {

	private String accessToken;
	private String refreshToken;

}
