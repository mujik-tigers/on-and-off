package site.onandoff.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import site.onandoff.member.validator.EmailDuplicateCheck;
import site.onandoff.member.validator.NicknameDuplicateCheck;

@AllArgsConstructor
@Getter
public class UniqueSignUpForm {

	@EmailDuplicateCheck
	private String email;

	@NicknameDuplicateCheck
	private String nickname;

	private String password;

}
