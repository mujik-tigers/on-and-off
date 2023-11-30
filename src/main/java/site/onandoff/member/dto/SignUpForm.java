package site.onandoff.member.dto;

import jakarta.validation.GroupSequence;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import site.onandoff.member.validator.DBUsing;
import site.onandoff.member.validator.EmailDuplicateCheck;
import site.onandoff.member.validator.EmailForm;
import site.onandoff.member.validator.EmailSize;
import site.onandoff.member.validator.NicknameDuplicateCheck;
import site.onandoff.member.validator.NicknameForm;
import site.onandoff.member.validator.PasswordForm;

@GroupSequence({SignUpForm.class, DBUsing.class})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SignUpForm {

	@EmailForm
	@EmailSize
	@EmailDuplicateCheck(groups = DBUsing.class)
	private String email;

	@NicknameForm
	@NicknameDuplicateCheck(groups = DBUsing.class)
	private String nickname;

	@PasswordForm
	private String password;

}
