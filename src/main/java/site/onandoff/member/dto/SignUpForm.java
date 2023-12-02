package site.onandoff.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.onandoff.validator.member.EmailFormatCheck;
import site.onandoff.validator.member.NicknameFormatCheck;
import site.onandoff.validator.member.PasswordFormatCheck;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SignUpForm {

	@EmailFormatCheck
	private String email;

	@NicknameFormatCheck
	private String nickname;

	@PasswordFormatCheck
	private String password;

	public UniqueSignUpForm toUnique() {
		return new UniqueSignUpForm(email, nickname, password);
	}

}
