package site.onandoff.exception;

import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {

	private final HttpStatus status;

	public CustomException(ErrorType errorType) {
		super(errorType.getMessage());
		this.status = errorType.getStatus();
	}

	public HttpStatus getStatus() {
		return status;
	}

}
