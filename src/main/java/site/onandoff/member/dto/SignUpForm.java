package site.onandoff.member.dto;

import jakarta.validation.GroupSequence;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
