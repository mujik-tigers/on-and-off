package site.onandoff.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.onandoff.member.validator.EmailFormatCheck;
import site.onandoff.member.validator.PasswordFormatCheck;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class LoginForm {

	@EmailFormatCheck
	private String email;

	@PasswordFormatCheck
	private String password;

}
