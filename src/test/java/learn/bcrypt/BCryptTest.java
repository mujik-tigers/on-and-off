package learn.bcrypt;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

public class BCryptTest {

	@Test
	@DisplayName("단순 텍스트인 평문 패스워드를 입력받으면 암호화된 60자 길이의 해쉬 패스워드를 만들어낸다.")
	void encrypt() {
		String stringPassword = "hyeonise";

		String hashPassword = BCrypt.hashpw(stringPassword, BCrypt.gensalt());

		System.out.println(hashPassword);
		System.out.println(hashPassword.length());
	}

	@Test
	@DisplayName("같은 평문 패스워드를 입력하더라도, 해쉬 패스워드는 다르다.")
	void encryptSameStringPassword() {
		String stringPassword = "hyeonise";

		String hashPassword1 = BCrypt.hashpw(stringPassword, BCrypt.gensalt());
		String hashPassword2 = BCrypt.hashpw(stringPassword, BCrypt.gensalt());

		System.out.println(hashPassword1);
		System.out.println(hashPassword2);

		assertThat(hashPassword1).isNotEqualTo(hashPassword2);
	}

	@Test
	@DisplayName("만들어진 해쉬 패스워드가 주어진 평문 패스워드로 만들어졌는지 확인할 수 있다.")
	void checkPassword() {
		String stringPassword = "hyeonise";

		String hashPassword = BCrypt.hashpw(stringPassword, BCrypt.gensalt());

		String userPassword1 = "hyeonise";
		String userPassword2 = "yeonise";

		assertThat(BCrypt.checkpw(userPassword1, hashPassword)).isTrue();
		assertThat(BCrypt.checkpw(userPassword2, hashPassword)).isFalse();
	}
}
