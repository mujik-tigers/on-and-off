package site.onandoff.member.presentation;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import site.onandoff.RestDocsSupport;
import site.onandoff.member.application.MemberService;
import site.onandoff.member.dto.SignUpForm;
import site.onandoff.member.dto.SignUpSuccessResponse;
import site.onandoff.member.dto.UniqueSignUpForm;

class MemberControllerTest extends RestDocsSupport {

	private final MemberService memberService = mock(MemberService.class);

	@Override
	protected Object initController() {
		return new MemberController(memberService);
	}

	@Test
	@DisplayName("회원가입 성공 시, Redirect URL 과 회원이 DB에 저장될때의 pk값을 반환한다.")
	void signUpSuccess() throws Exception {
		// given
		SignUpForm signUpForm = new SignUpForm("ghkdgus29@naver.com", "hyun", "1234567a!");

		given(memberService.signUp(any(UniqueSignUpForm.class)))
			.willReturn(new SignUpSuccessResponse(1L));

		// when & then
		mockMvc.perform(post("/members")
				.content(objectMapper.writeValueAsString(signUpForm))
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.redirectURL").value("https://on-and-off.site"))
			.andExpect(jsonPath("$.data.savedMemberId").value(1))
			.andDo(document("signup-success",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
					fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
					fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
				),
				responseFields(
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("코드"),
					fieldWithPath("status").type(JsonFieldType.STRING).description("상태"),
					fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
					fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
					fieldWithPath("data.redirectURL").type(JsonFieldType.STRING).description("리디렉션 URL"),
					fieldWithPath("data.savedMemberId").type(JsonFieldType.NUMBER).description("유저 PK")
				)
			));
	}

	@Test
	@DisplayName("회원가입 실패 시, 400 Bad Request를 반환하며, data엔 어떤 필드에서 실패하였는지 에러 메시지와 함께 응답한다.")
	void signUpFail() throws Exception {
		// given
		SignUpForm signUpForm = new SignUpForm("ghkdgus29@", "hyunnnnnnnn", "123");

		// when & then
		mockMvc.perform(post("/members")
				.content(objectMapper.writeValueAsString(signUpForm))
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andDo(document("signup-fail",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
					fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
					fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
				),
				responseFields(
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("코드"),
					fieldWithPath("status").type(JsonFieldType.STRING).description("상태"),
					fieldWithPath("message").type(JsonFieldType.NULL).description("메시지"),
					fieldWithPath("data[]").type(JsonFieldType.ARRAY).description("응답 데이터"),
					fieldWithPath("data[].field").type(JsonFieldType.STRING).description("잘못 입력한 필드"),
					fieldWithPath("data[].message").type(JsonFieldType.STRING).description("에러 발생 이유")
				)
			));
	}

}
