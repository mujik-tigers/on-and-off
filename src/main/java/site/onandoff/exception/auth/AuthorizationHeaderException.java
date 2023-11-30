package site.onandoff.exception.auth;

import site.onandoff.exception.CustomException;
import site.onandoff.exception.ErrorType;

public class AuthorizationHeaderException extends CustomException {

	public AuthorizationHeaderException() {
		super(ErrorType.INVALID_AUTHORIZATION_HEADER);
	}

}
