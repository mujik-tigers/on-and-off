package site.onandoff.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ReissuedAccessToken {

	private String accessToken;

}
