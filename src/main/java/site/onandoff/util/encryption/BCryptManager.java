package site.onandoff.util.encryption;

import org.mindrot.jbcrypt.BCrypt;

public class BCryptManager {

	public static String encrypt(String plainData) {
		return BCrypt.hashpw(plainData, BCrypt.gensalt());
	}

	public static boolean isMatch(String plainData, String hashData) {
		return BCrypt.checkpw(plainData, hashData);
	}

}
