package site.onandoff.exception.auth;

import site.onandoff.exception.CustomException;
import site.onandoff.exception.ErrorType;

public class InvalidTokenException extends CustomException {

	public InvalidTokenException() {
		super(ErrorType.INVALID_TOKEN);
	}

}
