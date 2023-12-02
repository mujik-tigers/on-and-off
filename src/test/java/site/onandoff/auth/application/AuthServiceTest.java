package site.onandoff.auth.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import site.onandoff.IntegrationTestSupport;
import site.onandoff.auth.dto.AuthenticationTokenPair;
import site.onandoff.auth.dto.LoginData;
import site.onandoff.exception.ErrorType;
import site.onandoff.exception.auth.InvalidLoginException;
import site.onandoff.member.Member;
import site.onandoff.member.Provider;
import site.onandoff.member.infrastructure.MemberRepository;
import site.onandoff.util.encryption.AES256Manager;
import site.onandoff.util.encryption.BCryptManager;

class AuthServiceTest extends IntegrationTestSupport {

	@Autowired
	private AuthService authService;

	@Autowired
	private AES256Manager aes256Manager;

	@Autowired
	private MemberRepository memberRepository;

	@Test
	@DisplayName("로그인 요청 데이터가 유효하면 토큰을 발행한다.")
	void loginSuccess() {
		// given
		String EMAIL = "yeon@email.com";
		String PASSWORD = "yeon!123";

		Member member = new Member(
			aes256Manager.encrypt(EMAIL),
			"yeonise",
			BCryptManager.encrypt(PASSWORD),
			Provider.LOCAL
		);
		memberRepository.save(member);

		LoginData loginData = new LoginData(EMAIL, PASSWORD);

		// when
		AuthenticationTokenPair tokenPair = authService.login(loginData);

		// then
		assertAll(
			() -> assertThat(tokenPair.getAccessToken()).isNotBlank(),
			() -> assertThat(tokenPair.getRefreshToken()).isNotBlank()
		);

	}

	@ParameterizedTest
	@CsvSource({"yeon@email.com,yeon123!", "yeom@email.com,yeon!123"})
	@DisplayName("로그인 요청 데이터가 유효하지 않다면 예외가 발생한다.")
	void loginFail(String email, String password) {
		// given
		Member member = new Member(
			aes256Manager.encrypt("yeon@email.com"),
			"yeonise",
			BCryptManager.encrypt("yeon!123"),
			Provider.LOCAL
		);
		memberRepository.save(member);

		LoginData loginData = new LoginData(email, password);

		// when & then
		assertThatThrownBy(() -> authService.login(loginData))
			.isInstanceOf(InvalidLoginException.class)
			.hasMessage(ErrorType.INVALID_LOGIN_REQUEST.getMessage());
	}

}
