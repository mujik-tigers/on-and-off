package site.onandoff.exception;

import org.springframework.http.HttpStatus;

public enum ErrorType {

	// Auth
	INVALID_AUTHORIZATION_HEADER(HttpStatus.BAD_REQUEST, "Authorization 헤더를 찾을 수 없거나 형식이 올바르지 않습니다"),
	EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다"),
	INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다"),
	INVALID_LOGIN_REQUEST(HttpStatus.UNAUTHORIZED, "유효하지 않은 사용자 정보입니다");

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
