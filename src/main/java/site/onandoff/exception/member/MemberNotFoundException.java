package site.onandoff.exception.member;

import site.onandoff.exception.CustomException;
import site.onandoff.exception.ErrorType;

public class MemberNotFoundException extends CustomException {

	public MemberNotFoundException() {
		super(ErrorType.MEMBER_NOT_FOUND);
	}

}
