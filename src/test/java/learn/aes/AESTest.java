package learn.aes;

import static org.assertj.core.api.Assertions.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AESTest {

	private final String algorithm = "AES/CBC/PKCS5Padding";
	private final int KEY_LENGTH = 32;
	private final String key = "h".repeat(KEY_LENGTH);
	private final int BLOCK_SIZE = 16;
	private final String iv = "i".repeat(BLOCK_SIZE);

	@Test
	@DisplayName("이메일과 같은 사용자 개인정보를 암호화하여 저장한다.")
	void encrypt() throws Exception {
		Cipher cipher = Cipher.getInstance(algorithm);
		SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
		IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes());
		cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec);

		String userEmail1 = "ghkdgus29@naver.com";                                                // -> 44 or tinyblob?
		String userEmail2 = "hellohellohellohellohellohello@hellohellohellohellohello.hello";    // -> 88 or tinyblob?

		byte[] encryptedUserEmail1 = cipher.doFinal(userEmail1.getBytes(StandardCharsets.UTF_8));
		byte[] encryptedUserEmail2 = cipher.doFinal(userEmail2.getBytes(StandardCharsets.UTF_8));

		String encryptedUserEmail1String = Base64.getEncoder().encodeToString(encryptedUserEmail1);
		System.out.println(encryptedUserEmail1String);
		System.out.println(encryptedUserEmail1String.length());

		String encryptedUserEmail2String = Base64.getEncoder().encodeToString(encryptedUserEmail2);
		System.out.println(encryptedUserEmail2String);
		System.out.println(encryptedUserEmail2String.length());
	}

	@Test
	@DisplayName("암호화된 개인정보를 복호화할 수 있다.")
	void decrypt() throws Exception {
		Cipher cipher = Cipher.getInstance(algorithm);
		SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
		IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes());
		cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec);

		String userEmail1 = "ghkdgus29@naver.com";
		String userEmail2 = "hellohellohellohellohellohello@hellohellohellohellohello.hello";

		byte[] encryptedUserEmail1 = cipher.doFinal(userEmail1.getBytes(StandardCharsets.UTF_8));
		byte[] encryptedUserEmail2 = cipher.doFinal(userEmail2.getBytes(StandardCharsets.UTF_8));

		cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec);
		byte[] decryptedUserEmail1 = cipher.doFinal(encryptedUserEmail1);
		byte[] decryptedUserEmail2 = cipher.doFinal(encryptedUserEmail2);

		String decryptedUserEmailString1 = new String(decryptedUserEmail1, StandardCharsets.UTF_8);
		System.out.println(decryptedUserEmailString1);
		assertThat(decryptedUserEmailString1).isEqualTo(userEmail1);

		String decryptedUserEmailString2 = new String(decryptedUserEmail2, StandardCharsets.UTF_8);
		System.out.println(decryptedUserEmailString2);
		assertThat(decryptedUserEmailString2).isEqualTo(userEmail2);
	}
}
