package site.onandoff.auth.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import io.jsonwebtoken.Claims;
import site.onandoff.IntegrationTestSupport;
import site.onandoff.auth.dto.AuthenticationTokenPair;
import site.onandoff.exception.ErrorType;
import site.onandoff.exception.auth.ExpiredTokenException;
import site.onandoff.exception.auth.InvalidTokenException;

class TokenManagerTest extends IntegrationTestSupport {

	@Autowired
	private TokenManager tokenManager;

	@Test
	@DisplayName("사용자 고유 식별 아이디 정보를 갖는 access token과 refresh token을 발급한다.")
	void issueTokenPairSuccess() {
		// given
		Long MEMBER_ID = 1L;

		// when
		AuthenticationTokenPair tokenPair = tokenManager.issueTokenPair(MEMBER_ID);
		Claims accessClaims = tokenManager.validateAccessToken(tokenPair.getAccessToken());
		Claims refreshClaims = tokenManager.validateRefreshToken(tokenPair.getRefreshToken());

		// then
		assertAll(
			() -> assertThat(accessClaims.get("id", Long.class)).isEqualTo(MEMBER_ID),
			() -> assertThat(accessClaims.get("refresh", Boolean.class)).isFalse(),
			() -> assertThat(refreshClaims.get("id", Long.class)).isEqualTo(MEMBER_ID),
			() -> assertThat(refreshClaims.get("refresh", Boolean.class)).isTrue()
		);
	}

	@Test
	@DisplayName("토큰이 만료되었다면 예외가 발생한다.")
	void parseWithExpiredToken() {
		// given
		String expiredToken = "eyJhbGciOiJIUzI1NiJ9.eyJyZWZyZXNoIjpmYWxzZSwiaWQiOjEsImlzcyI6InRpZ2VycyIsImlhdCI6MTcwMTUwNTM3NiwiZXhwIjoxNzAxNTA1Mzc2fQ.iFeZA-pHW02FvrbNQKCXNkDHbc2ON6olZBJdOAa7Lyk";

		// when & then
		assertThatThrownBy(() -> tokenManager.validateAccessToken(expiredToken))
			.isInstanceOf(ExpiredTokenException.class)
			.hasMessage(ErrorType.EXPIRED_TOKEN.getMessage());
	}

	@Test
	@DisplayName("토큰의 형식이 올바르지 않거나 내용이 위조되었다면 예외가 발생한다.")
	void parseWithInvalidToken() {
		// given
		String invalidToken = "eyJhbGciOiJIUzI1NiJ9.eyJyZWZyZXNoIjp0cnVlLCJpZCI6MSwiaXNzIjoidGlnZXJzIiwiaWF0IjoxNzAxMjI5OTE3LCJleHAiOjE3MDM4MjE5MTd9.4RjOoRX-bHRfUncQVJi7W4FMaNaHvA1xFnS2F-1XpG";

		// when & then
		assertThatThrownBy(() -> tokenManager.validateAccessToken(invalidToken))
			.isInstanceOf(InvalidTokenException.class)
			.hasMessage(ErrorType.INVALID_TOKEN.getMessage());
	}

	@Test
	@DisplayName("토큰의 타입이 refresh가 아니라면 예외가 발생한다.")
	void validateRefreshToken() {
		// given
		Long MEMBER_ID = 1L;
		String accessToken = tokenManager.issueAccessToken(MEMBER_ID).getAccessToken();

		// when & then
		assertThatThrownBy(() -> tokenManager.validateRefreshToken(accessToken))
			.isInstanceOf(InvalidTokenException.class)
			.hasMessage(ErrorType.INVALID_TOKEN.getMessage());

	}

}
