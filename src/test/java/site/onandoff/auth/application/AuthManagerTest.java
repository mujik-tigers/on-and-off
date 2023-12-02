package site.onandoff.auth.application;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import site.onandoff.IntegrationTestSupport;
import site.onandoff.exception.ErrorType;
import site.onandoff.exception.auth.AuthorizationHeaderException;

class AuthManagerTest extends IntegrationTestSupport {

	@Autowired
	AuthManager authManager;

	@Test
	@DisplayName("Authorization 헤더의 형식이 유효하면 헤더에 담긴 토큰을 반환한다.")
	void validateAuthorizationHeaderSuccess() {
		// given
		String token = "sample";
		String authorizationHeader = "Bearer " + token;

		// when
		String extractedToken = authManager.validateAuthorizationHeader(authorizationHeader);

		// then
		assertThat(extractedToken).isEqualTo(token);

	}

	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = {" ", "Bear "})
	@DisplayName("Authorization 헤더가 비어있거나 형식이 올바르지 않다면 예외가 발생한다.")
	void validateAuthorizationHeaderFail(String invalidAuthorizationHeader) {
		// when & then
		assertThatThrownBy(() -> authManager.validateAuthorizationHeader(invalidAuthorizationHeader))
			.isInstanceOf(AuthorizationHeaderException.class)
			.hasMessage(ErrorType.INVALID_AUTHORIZATION_HEADER.getMessage());
	}

}
