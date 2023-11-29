package site.onandoff.member.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SignUpForm {

	@EmailForm
	@Size(max = 62, message = "이메일 길이는 62자 이하입니다.")
	@EmailDuplicateCheck
	private String email;
	private String nickname;
	private String password;

}
