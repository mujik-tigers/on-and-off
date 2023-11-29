package site.onandoff.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AuthenticationTokens {

	private String accessToken;
	private String refreshToken;

}
