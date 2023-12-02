package site.onandoff.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.onandoff.validator.member.NicknameFormatCheck;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class NicknameChangeForm {

	@NicknameFormatCheck
	private String nickname;

	public UniqueNicknameChangeForm toUnique(Long memberId) {
		return new UniqueNicknameChangeForm(memberId, nickname);
	}

}
