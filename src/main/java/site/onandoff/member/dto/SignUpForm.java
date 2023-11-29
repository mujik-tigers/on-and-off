package site.onandoff.member.dto;

import static site.onandoff.member.dto.ValidationGroups.*;

import jakarta.validation.GroupSequence;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@GroupSequence({SignUpForm.class, DBGroup.class})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SignUpForm {

	@EmailForm
	@EmailSize
	@EmailDuplicateCheck(groups = DBGroup.class)
	private String email;

	private String nickname;

	@PasswordForm
	private String password;

}
