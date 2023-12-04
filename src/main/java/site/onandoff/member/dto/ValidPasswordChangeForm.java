package site.onandoff.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import site.onandoff.validator.member.PasswordMatchCheck;

@PasswordMatchCheck
@AllArgsConstructor
@Getter
public class ValidPasswordChangeForm {

	private Long id;
	private String password;
	private String newPassword;

}
