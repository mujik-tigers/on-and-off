package site.onandoff.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.onandoff.validator.member.PasswordFormatCheck;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PasswordChangeForm {

	private String password;

	@PasswordFormatCheck
	private String newPassword;

	public ValidPasswordChangeForm toValid(Long memberId) {
		return new ValidPasswordChangeForm(memberId, password, newPassword);
	}

}
