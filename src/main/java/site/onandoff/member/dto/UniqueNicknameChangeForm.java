package site.onandoff.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import site.onandoff.validator.member.NicknameDuplicateCheck;

@AllArgsConstructor
@Getter
public class UniqueNicknameChangeForm {

	private Long id;

	@NicknameDuplicateCheck
	private String nickname;

}
