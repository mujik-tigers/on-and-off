package site.onandoff.member.dto;

import lombok.Getter;
import site.onandoff.member.Member;

@Getter
public class SignUpSuccessResponse {

	private final String redirectURL = "https://on-and-off.site";
	private final Long savedUserId;

	public SignUpSuccessResponse(Member savedMember) {
		this.savedUserId = savedMember.getId();
	}

}
