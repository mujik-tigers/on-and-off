package site.onandoff.auth.presentation;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import site.onandoff.RestDocsSupport;
import site.onandoff.auth.Account;
import site.onandoff.auth.application.AuthService;
import site.onandoff.auth.dto.AuthenticationTokenPair;
import site.onandoff.auth.dto.LoginForm;
import site.onandoff.auth.dto.ReissuedAccessToken;
import site.onandoff.exception.auth.AuthorizationHeaderException;
import site.onandoff.exception.auth.ExpiredTokenException;
import site.onandoff.exception.auth.InvalidLoginException;
import site.onandoff.exception.auth.InvalidTokenException;

class AuthControllerTest extends RestDocsSupport {

	private final AuthService authService = mock(AuthService.class);

	@Override
	protected Object initController() {
		return new AuthController(authService);
	}

	@Test
	@DisplayName("일반 로그인 : 성공")
	void loginSuccess() throws Exception {
		// given
		LoginForm loginForm = new LoginForm("yeon@email.com", "test!1234");

		given(authService.login(any(LoginForm.class)))
			.willReturn(new AuthenticationTokenPair("accessToken", "refreshToken"));

		// when & then
		mockMvc.perform(post("/login")
				.content(objectMapper.writeValueAsString(loginForm))
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andDo(document("login-success",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
					fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
				),
				responseFields(
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("코드"),
					fieldWithPath("status").type(JsonFieldType.STRING).description("상태"),
					fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
					fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
					fieldWithPath("data.accessToken").type(JsonFieldType.STRING).description("Access Token"),
					fieldWithPath("data.refreshToken").type(JsonFieldType.STRING).description("Refresh Token")
				)
			));
	}

	@Test
	@DisplayName("일반 로그인 : 실패")
	void loginFail() throws Exception {
		// given
		LoginForm loginForm = new LoginForm("yeon@email.com", "test!123");

		given(authService.login(any(LoginForm.class)))
			.willThrow(new InvalidLoginException());

		// when & then
		mockMvc.perform(post("/login")
				.content(objectMapper.writeValueAsString(loginForm))
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isUnauthorized())
			.andDo(document("login-fail",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
					fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
				),
				responseFields(
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("코드"),
					fieldWithPath("status").type(JsonFieldType.STRING).description("상태"),
					fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
					fieldWithPath("data").type(JsonFieldType.NULL).description("응답 데이터")
				)
			));
	}

	@Test
	@DisplayName("일반 로그인 : 입력 형식 오류")
	void loginValidFail() throws Exception {
		// given
		LoginForm loginForm = new LoginForm("yeon@email.c", "test1234");

		// when & then
		mockMvc.perform(post("/login")
				.content(objectMapper.writeValueAsString(loginForm))
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andDo(document("login-valid-fail",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
					fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
				),
				responseFields(
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("코드"),
					fieldWithPath("status").type(JsonFieldType.STRING).description("상태"),
					fieldWithPath("message").type(JsonFieldType.NULL).description("메시지"),
					fieldWithPath("data").type(JsonFieldType.ARRAY).description("응답 데이터"),
					fieldWithPath("data[].field").type(JsonFieldType.STRING).description("필드"),
					fieldWithPath("data[].message").type(JsonFieldType.STRING).description("에러 메시지")
				)
			));
	}

	@Test
	@DisplayName("토큰 재발급 : 성공")
	void reissueSuccess() throws Exception {
		// given
		given(authService.reissueAccessToken(any(Account.class)))
			.willReturn(new ReissuedAccessToken("accessToken"));

		// when & then
		mockMvc.perform(post("/reissue")
				.header(HttpHeaders.AUTHORIZATION, "Bearer refreshToken"))
			.andDo(print())
			.andExpect(status().isOk())
			.andDo(document("reissue-success",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				responseFields(
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("코드"),
					fieldWithPath("status").type(JsonFieldType.STRING).description("상태"),
					fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
					fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
					fieldWithPath("data.accessToken").type(JsonFieldType.STRING).description("Access Token")
				)
			));
	}

	@Test
	@DisplayName("토큰 재발급 : 실패")
	void reissueFail() throws Exception {
		// given
		given(authService.reissueAccessToken(any(Account.class)))
			.willThrow(new InvalidTokenException());

		// when & then
		mockMvc.perform(post("/reissue")
				.header(HttpHeaders.AUTHORIZATION, "Bearer refreshToken"))
			.andDo(print())
			.andExpect(status().isUnauthorized())
			.andDo(document("reissue-fail",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				responseFields(
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("코드"),
					fieldWithPath("status").type(JsonFieldType.STRING).description("상태"),
					fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
					fieldWithPath("data").type(JsonFieldType.NULL).description("응답 데이터")
				)
			));
	}

	@Test
	@DisplayName("인증 실패 : 만료된 토큰")
	void requestWithExpiredToken() throws Exception {
		// given
		given(authService.reissueAccessToken(any(Account.class)))
			.willThrow(new ExpiredTokenException());

		// when & then
		mockMvc.perform(post("/reissue")
				.header(HttpHeaders.AUTHORIZATION, "Bearer [accessToken or refreshToken]"))
			.andDo(print())
			.andExpect(status().isUnauthorized())
			.andDo(document("expired-token",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				responseFields(
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("코드"),
					fieldWithPath("status").type(JsonFieldType.STRING).description("상태"),
					fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
					fieldWithPath("data").type(JsonFieldType.NULL).description("응답 데이터")
				)
			));
	}

	@Test
	@DisplayName("인증 실패 : Authorization Header 오류")
	void requestWithInvalidAuthorizationHeader() throws Exception {
		// given
		given(authService.reissueAccessToken(any(Account.class)))
			.willThrow(new AuthorizationHeaderException());

		// when & then
		mockMvc.perform(post("/reissue")
				.header(HttpHeaders.AUTHORIZATION, "Bear "))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andDo(document("invalid-authorization-header",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				responseFields(
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("코드"),
					fieldWithPath("status").type(JsonFieldType.STRING).description("상태"),
					fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
					fieldWithPath("data").type(JsonFieldType.NULL).description("응답 데이터")
				)
			));
	}

}
