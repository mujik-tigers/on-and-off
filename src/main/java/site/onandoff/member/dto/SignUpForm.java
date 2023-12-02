package site.onandoff.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.onandoff.member.validator.EmailFormatCheck;
import site.onandoff.member.validator.NicknameFormatCheck;
import site.onandoff.member.validator.PasswordForm;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SignUpForm {

	@EmailFormatCheck
	private String email;

	@NicknameFormatCheck
	private String nickname;

	@PasswordForm
	private String password;

	public UniqueSignUpForm toUnique() {
		return new UniqueSignUpForm(email, nickname, password);
	}

}
