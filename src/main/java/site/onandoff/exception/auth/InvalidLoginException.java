package site.onandoff.exception.auth;

import site.onandoff.exception.CustomException;
import site.onandoff.exception.ErrorType;

public class InvalidLoginException extends CustomException {

	public InvalidLoginException() {
		super(ErrorType.INVALID_LOGIN_REQUEST);
	}

}
