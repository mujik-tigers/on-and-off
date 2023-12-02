package site.onandoff.util.encryption;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BCryptManagerTest {

	@Test
	@DisplayName("평문을 60자의 해쉬 암호로 만들어 반환한다.")
	void encrypt() {
		// given
		String plainPassword1 = "hi";
		String plainPassword2 = "hello";
		String plainPassword3 = "안녕";

		// when
		String hashPassword1 = BCryptManager.encrypt(plainPassword1);
		String hashPassword2 = BCryptManager.encrypt(plainPassword2);
		String hashPassword3 = BCryptManager.encrypt(plainPassword3);

		// then
		assertThat(hashPassword1.length()).isEqualTo(60);
		assertThat(hashPassword2.length()).isEqualTo(60);
		assertThat(hashPassword3.length()).isEqualTo(60);
	}

	@Test
	@DisplayName("같은 평문을 암호화하더라도 결과는 항상 다르다.")
	void sameInputDifferentOutput() {
		// given
		String plainPassword1 = "hi";
		String plainPassword2 = "hi";
		String plainPassword3 = "hi";

		// when
		String hashPassword1 = BCryptManager.encrypt(plainPassword1);
		String hashPassword2 = BCryptManager.encrypt(plainPassword2);
		String hashPassword3 = BCryptManager.encrypt(plainPassword3);

		// then
		assertThat(hashPassword1).isNotEqualTo(hashPassword2);
		assertThat(hashPassword2).isNotEqualTo(hashPassword3);
		assertThat(hashPassword3).isNotEqualTo(hashPassword1);
	}

	@Test
	@DisplayName("입력한 평문으로 만든 해쉬암호가 맞다면 참을 반환한다.")
	void isPasswordMatch() {
		// given
		String plainPassword = "hi";
		String hashPassword = BCryptManager.encrypt(plainPassword);

		// when
		boolean passwordMatch = BCryptManager.isPasswordMatch(plainPassword, hashPassword);

		// then
		assertThat(passwordMatch).isTrue();
	}

	@Test
	@DisplayName("입력한 평문으로 만든 해쉬암호가 아니라면 거짓을 반환한다.")
	void isNotPasswordMatch() {
		// given
		String hashPassword = BCryptManager.encrypt("hi");
		String plainPassword = "hello";

		// when
		boolean passwordMatch = BCryptManager.isPasswordMatch(plainPassword, hashPassword);

		// then
		assertThat(passwordMatch).isFalse();
	}
}
