package site.onandoff.member.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SignUpForm {

	@EmailForm
	private String email;
	private String nickname;
	private String password;

}
