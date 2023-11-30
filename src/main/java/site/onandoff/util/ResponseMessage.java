package site.onandoff.util;

public enum ResponseMessage {

	// Auth
	LOGIN_SUCCESS("로그인에 성공했습니다"),
	REISSUE_TOKENS_SUCCESS("토큰 재발급에 성공했습니다");

	private final String message;

	ResponseMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}

}
