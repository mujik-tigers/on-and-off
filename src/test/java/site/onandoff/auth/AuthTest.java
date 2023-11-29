package site.onandoff.auth;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import site.onandoff.IntegrationTestSupport;
import site.onandoff.auth.application.TokenManager;
import site.onandoff.auth.dto.LoginData;
import site.onandoff.member.Member;
import site.onandoff.member.Provider;
import site.onandoff.util.PasswordEncoder;

class AuthTest extends IntegrationTestSupport {

	@Autowired
	EntityManager entityManager;

	@Autowired
	TokenManager tokenManager;

	@Test
	@DisplayName("일반 로그인 : 성공")
	@Transactional
	void loginSuccess() throws Exception {
		// given
		Member member = new Member(
			null,
			"yeon@email.com",
			"yeonise",
			PasswordEncoder.encrypt("test!1234"),
			Provider.LOCAL
		);
		entityManager.persist(member);
		entityManager.close();

		LoginData loginData = new LoginData("yeon@email.com", "test!1234");

		// when & then
		mockMvc.perform(post("/login")
				.content(objectMapper.writeValueAsString(loginData))
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
	@Transactional
	void loginFail() throws Exception {
		// given
		Member member = new Member(
			null,
			"yeon@email.com",
			"yeonise",
			PasswordEncoder.encrypt("test!1234"),
			Provider.LOCAL
		);
		entityManager.persist(member);
		entityManager.close();

		LoginData loginData = new LoginData("yeon@email.com", "test!123");

		// when & then
		mockMvc.perform(post("/login")
				.content(objectMapper.writeValueAsString(loginData))
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
		LoginData loginData = new LoginData("yeon@email.c", "test1234");

		// when & then
		mockMvc.perform(post("/login")
				.content(objectMapper.writeValueAsString(loginData))
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
		Long MEMBER_ID = 1L;
		String refreshToken = tokenManager.issueTokenPair(MEMBER_ID).getRefreshToken();

		// when & then
		mockMvc.perform(post("/reissue")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + refreshToken))
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
		Long MEMBER_ID = 1L;
		String accessToken = tokenManager.issueTokenPair(MEMBER_ID).getAccessToken();

		// when & then
		mockMvc.perform(post("/reissue")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
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
		String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwicmVmcmVzaCI6ZmFsc2UsImlzcyI6InRpZ2VycyIsImlhdCI6MTcwMTIzMzIzMSwiZXhwIjoxNzAxMjM1MDMxfQ.wtPrTsa9lwGxmAj0HhD-FZde9T-4Fpdyz28pE3kpUC8";

		// when & then
		mockMvc.perform(post("/reissue")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
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
}
