package site.onandoff.util.encryption;

import org.mindrot.jbcrypt.BCrypt;

public class BcryptManager {

	public static String encrypt(String plainPassword) {
		return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
	}

	public static boolean isPasswordMatch(String plainPassword, String hashPassword) {
		return BCrypt.checkpw(plainPassword, hashPassword);
	}

}
