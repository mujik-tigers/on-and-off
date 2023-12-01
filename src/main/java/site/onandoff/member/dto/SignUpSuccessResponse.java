package site.onandoff.member.dto;

import lombok.Getter;

@Getter
public class SignUpSuccessResponse {

	private final String redirectURL = "https://on-and-off.site";
	private final Long savedMemberId;

	public SignUpSuccessResponse(Long saveMemberId) {
		this.savedMemberId = saveMemberId;
	}

}
