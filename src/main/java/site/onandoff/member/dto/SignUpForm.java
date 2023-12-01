package site.onandoff.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import site.onandoff.member.validator.EmailForm;
import site.onandoff.member.validator.EmailSize;
import site.onandoff.member.validator.NicknameForm;
import site.onandoff.member.validator.PasswordForm;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SignUpForm {

	@EmailForm
	@EmailSize
	private String email;

	@NicknameForm
	private String nickname;

	@PasswordForm
	private String password;

	public UniqueSignUpForm toUnique() {
		return new UniqueSignUpForm(email, nickname, password);
	}

}
