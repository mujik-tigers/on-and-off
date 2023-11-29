package site.onandoff.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class LoginData {

	private String email;
	private String password;

}
