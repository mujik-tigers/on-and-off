package site.onandoff.exception.auth;

import site.onandoff.exception.CustomException;
import site.onandoff.exception.ErrorType;

public class ExpiredTokenException extends CustomException {

	public ExpiredTokenException() {
		super(ErrorType.EXPIRED_TOKEN);
	}

}
