package site.onandoff.member.presentation;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.validator.internal.engine.path.PathImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import site.onandoff.RestDocsSupport;
import site.onandoff.member.application.MemberService;
import site.onandoff.member.dto.ModifiedMember;
import site.onandoff.member.dto.NicknameChangeForm;
import site.onandoff.member.dto.PasswordChangeForm;
import site.onandoff.member.dto.SignUpForm;
import site.onandoff.member.dto.SignUpSuccessResponse;
import site.onandoff.member.dto.UniqueNicknameChangeForm;
import site.onandoff.member.dto.UniqueSignUpForm;
import site.onandoff.member.dto.ValidPasswordChangeForm;

class MemberControllerTest extends RestDocsSupport {

	private static final long MEMBER_ID = 1L;
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
			.willReturn(new SignUpSuccessResponse(MEMBER_ID));

		// when & then
		mockMvc.perform(post("/members")
				.content(objectMapper.writeValueAsString(signUpForm))
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.redirectUrl").value("https://on-and-off.site"))
			.andExpect(jsonPath("$.data.savedMemberId").value(MEMBER_ID))
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
					fieldWithPath("data.redirectUrl").type(JsonFieldType.STRING).description("리디렉션 URL"),
					fieldWithPath("data.savedMemberId").type(JsonFieldType.NUMBER).description("유저 PK")
				)
			));
	}

	@Test
	@DisplayName("회원가입 입력폼이 잘못되어 회원가입 실패 시, 400 Bad Request를 반환하며, data엔 어떤 필드에서 실패하였는지 에러 메시지와 함께 응답한다.")
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

	@Test
	@DisplayName("회원가입 입력폼의 닉네임이나 이메일이 중복되어 회원가입 실패 시, 400 Bad Request를 반환하며, data엔 어떤 필드에서 실패하였는지 에러 메시지와 함께 응답한다.")
	void uniqueSignUpFail() throws Exception {
		// given
		SignUpForm signUpForm = new SignUpForm("ghkdgus29@naver.com", "hyun", "1234567a!");

		ConstraintViolationException exception = mock(ConstraintViolationException.class);
		Set<ConstraintViolation<?>> violations = new HashSet<>();
		ConstraintViolation<?> mockedViolation = mock(ConstraintViolation.class);
		violations.add(mockedViolation);

		given(mockedViolation.getPropertyPath()).willReturn(PathImpl.createPathFromString("signUp.signUpForm.email"));
		given(mockedViolation.getMessage()).willReturn("이미 존재하는 이메일입니다.");
		given(exception.getConstraintViolations()).willReturn(violations);
		given(memberService.signUp(any(UniqueSignUpForm.class)))
			.willThrow(exception);

		// when & then
		mockMvc.perform(post("/members")
				.content(objectMapper.writeValueAsString(signUpForm))
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andDo(document("unique-signup-fail",
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

	@Test
	@DisplayName("닉네임 변경 : 성공")
	void modifyNicknameSuccess() throws Exception {
		// given
		Long MEMBER_ID = 1L;
		String NEW_NICKNAME = "yeonise";

		NicknameChangeForm nicknameChangeForm = new NicknameChangeForm(NEW_NICKNAME);

		given(memberService.modifyNickname(any(UniqueNicknameChangeForm.class)))
			.willReturn(new ModifiedMember(MEMBER_ID, NEW_NICKNAME));

		// when & then
		mockMvc.perform(patch("/members/nickname")
				.header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
				.content(objectMapper.writeValueAsString(nicknameChangeForm))
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.id").value(MEMBER_ID))
			.andExpect(jsonPath("$.data.nickname").value(NEW_NICKNAME))
			.andDo(document("modify-nickname-success",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임")
				),
				responseFields(
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("코드"),
					fieldWithPath("status").type(JsonFieldType.STRING).description("상태"),
					fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
					fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
					fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("사용자 고유 식별 아이디"),
					fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("변경된 닉네임")
				)
			));
	}

	@Test
	@DisplayName("닉네임 변경 : 형식 오류")
	void modifyNicknameFail() throws Exception {
		// given
		String INVALID_NICKNAME = "thisIsTooLong";
		NicknameChangeForm nicknameChangeForm = new NicknameChangeForm(INVALID_NICKNAME);

		// when & then
		mockMvc.perform(patch("/members/nickname")
				.header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
				.content(objectMapper.writeValueAsString(nicknameChangeForm))
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andDo(document("modify-nickname-fail",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임")
				),
				responseFields(
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("코드"),
					fieldWithPath("status").type(JsonFieldType.STRING).description("상태"),
					fieldWithPath("message").type(JsonFieldType.NULL).description("메시지"),
					fieldWithPath("data[]").type(JsonFieldType.ARRAY).description("응답 데이터"),
					fieldWithPath("data[].field").type(JsonFieldType.STRING).description("필드"),
					fieldWithPath("data[].message").type(JsonFieldType.STRING).description("에러 메시지")
				)
			));
	}

	@Test
	@DisplayName("닉네임 변경 : 중복 오류")
	void modifyUniqueNicknameFail() throws Exception {
		// given
		String DUPLICATED_NICKNAME = "yeonise";
		NicknameChangeForm nicknameChangeForm = new NicknameChangeForm(DUPLICATED_NICKNAME);

		ConstraintViolationException exception = mock(ConstraintViolationException.class);
		Set<ConstraintViolation<?>> violations = new HashSet<>();
		ConstraintViolation<?> mockedViolation = mock(ConstraintViolation.class);
		violations.add(mockedViolation);

		given(mockedViolation.getPropertyPath()).willReturn(
			PathImpl.createPathFromString("modifyNickname.NicknameChangeForm.nickname"));
		given(mockedViolation.getMessage()).willReturn("이미 존재하는 닉네임입니다.");
		given(exception.getConstraintViolations()).willReturn(violations);
		given(memberService.modifyNickname(any(UniqueNicknameChangeForm.class)))
			.willThrow(exception);

		// when & then
		mockMvc.perform(patch("/members/nickname")
				.header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
				.content(objectMapper.writeValueAsString(nicknameChangeForm))
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andDo(document("unique-modify-nickname-fail",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임")
				),
				responseFields(
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("코드"),
					fieldWithPath("status").type(JsonFieldType.STRING).description("상태"),
					fieldWithPath("message").type(JsonFieldType.NULL).description("메시지"),
					fieldWithPath("data[]").type(JsonFieldType.ARRAY).description("응답 데이터"),
					fieldWithPath("data[].field").type(JsonFieldType.STRING).description("필드"),
					fieldWithPath("data[].message").type(JsonFieldType.STRING).description("에러 메시지")
				)
			));
	}

	@Test
	@DisplayName("비밀번호 변경 : 성공")
	void modifyPasswordSuccess() throws Exception {
		// given
		Long MEMBER_ID = 1L;
		String NICKNAME = "yeonise";
		String PASSWORD = "yeon!123";
		String NEW_PASSWORD = "hyeon!123";

		PasswordChangeForm passwordChangeForm = new PasswordChangeForm(PASSWORD, NEW_PASSWORD);

		given(memberService.modifyPassword(any(ValidPasswordChangeForm.class)))
			.willReturn(new ModifiedMember(MEMBER_ID, NICKNAME));

		// when & then
		mockMvc.perform(patch("/members/password")
				.header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
				.content(objectMapper.writeValueAsString(passwordChangeForm))
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.id").value(MEMBER_ID))
			.andExpect(jsonPath("$.data.nickname").value(NICKNAME))
			.andDo(document("modify-password-success",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("password").type(JsonFieldType.STRING).description("기존 비밀번호"),
					fieldWithPath("newPassword").type(JsonFieldType.STRING).description("새로운 비밀번호")
				),
				responseFields(
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("코드"),
					fieldWithPath("status").type(JsonFieldType.STRING).description("상태"),
					fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
					fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
					fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("사용자 고유 식별 아이디"),
					fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("사용자 닉네임")
				)
			));
	}

	@Test
	@DisplayName("비밀번호 변경 : 비밀번호 불일치 오류")
	void modifyValidPasswordFail() throws Exception {
		// given
		String NOT_MATCHED_PASSWORD = "yeon!123";
		String NEW_PASSWORD = "hyeon!123";
		PasswordChangeForm passwordChangeForm = new PasswordChangeForm(NOT_MATCHED_PASSWORD, NEW_PASSWORD);

		ConstraintViolationException exception = mock(ConstraintViolationException.class);
		Set<ConstraintViolation<?>> violations = new HashSet<>();
		ConstraintViolation<?> mockedViolation = mock(ConstraintViolation.class);
		violations.add(mockedViolation);

		given(mockedViolation.getPropertyPath()).willReturn(
			PathImpl.createPathFromString("modifyPassword.PasswordChangeForm.password"));
		given(mockedViolation.getMessage()).willReturn("비밀번호가 일치하지 않습니다");
		given(exception.getConstraintViolations()).willReturn(violations);
		given(memberService.modifyPassword(any(ValidPasswordChangeForm.class)))
			.willThrow(exception);

		// when & then
		mockMvc.perform(patch("/members/password")
				.header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
				.content(objectMapper.writeValueAsString(passwordChangeForm))
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andDo(document("modify-valid-password-fail",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("password").type(JsonFieldType.STRING).description("기존 비밀번호"),
					fieldWithPath("newPassword").type(JsonFieldType.STRING).description("새로운 비밀번호")
				),
				responseFields(
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("코드"),
					fieldWithPath("status").type(JsonFieldType.STRING).description("상태"),
					fieldWithPath("message").type(JsonFieldType.NULL).description("메시지"),
					fieldWithPath("data[]").type(JsonFieldType.ARRAY).description("응답 데이터"),
					fieldWithPath("data[].field").type(JsonFieldType.STRING).description("필드"),
					fieldWithPath("data[].message").type(JsonFieldType.STRING).description("에러 메시지")
				)
			));
	}

	@Test
	@DisplayName("비밀번호 변경 : 형식 오류")
	void modifyPasswordFail() throws Exception {
		// given
		String PASSWORD = "yeon!123";
		String INVALID_NEW_PASSWORD = "thisIsWrong";
		PasswordChangeForm passwordChangeForm = new PasswordChangeForm(PASSWORD, INVALID_NEW_PASSWORD);

		// when & then
		mockMvc.perform(patch("/members/password")
				.header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
				.content(objectMapper.writeValueAsString(passwordChangeForm))
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andDo(document("modify-password-fail",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestFields(
					fieldWithPath("password").type(JsonFieldType.STRING).description("기존 비밀번호"),
					fieldWithPath("newPassword").type(JsonFieldType.STRING).description("새로운 비밀번호")
				),
				responseFields(
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("코드"),
					fieldWithPath("status").type(JsonFieldType.STRING).description("상태"),
					fieldWithPath("message").type(JsonFieldType.NULL).description("메시지"),
					fieldWithPath("data[]").type(JsonFieldType.ARRAY).description("응답 데이터"),
					fieldWithPath("data[].field").type(JsonFieldType.STRING).description("필드"),
					fieldWithPath("data[].message").type(JsonFieldType.STRING).description("에러 메시지")
				)
			));
	}

}
