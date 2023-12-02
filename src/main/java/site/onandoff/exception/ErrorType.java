package site.onandoff.exception;

import org.springframework.http.HttpStatus;

public enum ErrorType {

	// Auth
	INVALID_AUTHORIZATION_HEADER(HttpStatus.BAD_REQUEST, "Authorization 헤더를 찾을 수 없거나 형식이 올바르지 않습니다"),
	EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다"),
	INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다"),
	INVALID_LOGIN_REQUEST(HttpStatus.UNAUTHORIZED, "유효하지 않은 사용자 정보입니다"),

	// AES256
	AES256_SETTING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "AES256 관련 잘못된 설정을 하였습니다"),

	// Member
	MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 사용자입니다"),
	PASSWORD_NOT_MATCHED(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다");

	private final HttpStatus status;
	private final String message;

	ErrorType(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
	}

	public HttpStatus getStatus() {
		return this.status;
	}

	public String getMessage() {
		return this.message;
	}

}
