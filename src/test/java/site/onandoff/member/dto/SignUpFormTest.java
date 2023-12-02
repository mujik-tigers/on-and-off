package site.onandoff.member.dto;

import static org.assertj.core.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import site.onandoff.IntegrationTestSupport;

class SignUpFormTest extends IntegrationTestSupport {

	@Autowired
	private Validator validator;

	@Test
	@DisplayName("만약 올바르지 않은 이메일 형식을 입력하면 이메일 형식 관련 violation 을 반환한다.")
	void wrongInputForm() throws Exception {
		// given
		String invalidEmailInput = "it's fake email";
		SignUpForm signUpForm = new SignUpForm(invalidEmailInput, "hyun", "1234567a!");

		// when
		Set<ConstraintViolation<SignUpForm>> violations = validator.validate(signUpForm);

		// then
		assertThat(violations.size()).isEqualTo(1);
		assertThat(violations.iterator().next().getMessage()).isEqualTo("이메일이 양식에 맞지 않습니다.");
	}

	@Test
	@DisplayName("올바른 이메일 형식을 입력하면 violation 을 반환하지 않는다.")
	void correctInputForm() throws Exception {
		// given
		String invalidEmailInput = "ghkdgus29@codesquand.ac.kr";
		SignUpForm signUpForm = new SignUpForm(invalidEmailInput, "hyun", "1234567a!");

		// when
		Set<ConstraintViolation<SignUpForm>> violations = validator.validate(signUpForm);

		// then
		assertThat(violations).isEmpty();
	}

	@Test
	@DisplayName("62자 초과의 이메일을 입력하면 이메일 형식 관련 violation 을 반환한다.")
	void shortEmailInput() throws Exception {
		// given
		String longEmailInput = "hellohellohellohellohellohellohellohellohellohellohellohello@naver.com";
		SignUpForm signUpForm = new SignUpForm(longEmailInput, "hyun", "1234567a!");

		// when
		Set<ConstraintViolation<SignUpForm>> violations = validator.validate(signUpForm);

		// then
		assertThat(violations.size()).isEqualTo(1);
		assertThat(violations.iterator().next().getMessage()).isEqualTo("이메일이 양식에 맞지 않습니다.");
	}

	@Test
	@DisplayName("비밀번호는 영문자, 숫자, 특수문자를 포함하는 8-16자 문자열이어야 한다.")
	void inputPassword() throws Exception {
		// given
		String shortPassword = "123a!";
		SignUpForm shortPasswordForm = new SignUpForm("ghkdgus29@naver.com", "hyun", shortPassword);

		String longPassword = "1235678901234567890a!";
		SignUpForm longPasswordForm = new SignUpForm("ghkdgus29@naver.com", "hyun", longPassword);

		String invalidPassword = "1234567890";
		SignUpForm invalidPasswordForm = new SignUpForm("ghkdgus29@naver.com", "hyun", invalidPassword);

		String validPassword = "1234567a!";
		SignUpForm validPasswordForm = new SignUpForm("ghkdgus29@naver.com", "hyun", validPassword);

		// when
		Set<ConstraintViolation<SignUpForm>> shortPasswordViolations = validator.validate(shortPasswordForm);
		Set<ConstraintViolation<SignUpForm>> longPasswordViolations = validator.validate(longPasswordForm);
		Set<ConstraintViolation<SignUpForm>> invalidPasswordViolations = validator.validate(invalidPasswordForm);
		Set<ConstraintViolation<SignUpForm>> validPasswordViolations = validator.validate(validPasswordForm);

		// then
		assertThat(shortPasswordViolations.iterator().next().getMessage())
			.isEqualTo(longPasswordViolations.iterator().next().getMessage())
			.isEqualTo(invalidPasswordViolations.iterator().next().getMessage());

		assertThat(shortPasswordViolations.size())
			.isEqualTo(longPasswordViolations.size())
			.isEqualTo(invalidPasswordViolations.size())
			.isEqualTo(1);

		assertThat(validPasswordViolations).isEmpty();
	}

	@Test
	@DisplayName("닉네임은 영문자, 숫자, 한글로 이루어진 1-10자 문자열이어야 한다.")
	void inputNickname() throws Exception {
		// given
		String blankNickname = "";
		SignUpForm blankNicknameForm = new SignUpForm("ghkdgus29@naver.com", blankNickname, "1234567a!");

		String longNickname = "1235678901234567890";
		SignUpForm longNicknameForm = new SignUpForm("ghkdgus29@naver.com", longNickname, "1234567a!");

		String invalidNickname = "황현!!";
		SignUpForm invalidNicknameForm = new SignUpForm("ghkdgus29@naver.com", invalidNickname, "1234567a!");

		String validNickname = "황현";
		SignUpForm validNicknameForm = new SignUpForm("ghkdgus29@naver.com", validNickname, "1234567a!");

		// when
		Set<ConstraintViolation<SignUpForm>> blankNicknameViolations = validator.validate(blankNicknameForm);
		Set<ConstraintViolation<SignUpForm>> longNicknameViolations = validator.validate(longNicknameForm);
		Set<ConstraintViolation<SignUpForm>> invalidNicknameViolations = validator.validate(invalidNicknameForm);
		Set<ConstraintViolation<SignUpForm>> validNicknameViolations = validator.validate(validNicknameForm);

		// then
		assertThat(blankNicknameViolations.iterator().next().getMessage())
			.isEqualTo(longNicknameViolations.iterator().next().getMessage())
			.isEqualTo(invalidNicknameViolations.iterator().next().getMessage());

		assertThat(blankNicknameViolations.size())
			.isEqualTo(longNicknameViolations.size())
			.isEqualTo(invalidNicknameViolations.size())
			.isEqualTo(1);

		assertThat(validNicknameViolations).isEmpty();
	}

}
