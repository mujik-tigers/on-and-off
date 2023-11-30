package site.onandoff.util.encryption;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import site.onandoff.IntegrationTestSupport;

class AES256ManagerTest extends IntegrationTestSupport {

	@Autowired
	private AES256Manager aes256Manager;

	@Test
	@DisplayName("같은 평문을 입력하면 똑같은 암호문을 반환한다.")
	void encrypt() throws Exception {
		// given
		String plain = "ghkdgus29@naver.com";

		// when
		String encrypt1 = aes256Manager.encrypt(plain);
		String encrypt2 = aes256Manager.encrypt(plain);

		// then
		assertThat(encrypt2).isEqualTo(encrypt2);
	}

	@Test
	@DisplayName("서로 다른 평문은 서로 다른 암호문을 반환한다.")
	void differentPlainDifferentEncrypt() throws Exception {
		// given
		String plain1 = "ghkdgus29@naver.com";
		String plain2 = "ghkdgus28@naver.com";

		// when
		String encrypt1 = aes256Manager.encrypt(plain1);
		String encrypt2 = aes256Manager.encrypt(plain2);

		// then
		assertThat(encrypt1).isNotEqualTo(encrypt2);
	}

	@Test
	@DisplayName("암호화한 평문을 그대로 복호화 할 수 있다.")
	void decrypt() throws Exception {
		// given
		String plain = "ghkdgus29@naver.com";
		String encrypt = aes256Manager.encrypt(plain);

		// when
		String decrypt = aes256Manager.decrypt(encrypt);

		// then
		assertThat(decrypt).isEqualTo(plain);
	}
}


