package site.onandoff.util;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoder {

	public String encrypt(String password) {
		return BCrypt.hashpw(password, BCrypt.gensalt());
	}

	public boolean isMatch(String password, String hashed) {
		return BCrypt.checkpw(password, hashed);
	}

}
