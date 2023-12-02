package site.onandoff.auth.dto;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import site.onandoff.IntegrationTestSupport;

class LoginFormTest extends IntegrationTestSupport {

	@Autowired
	private Validator validator;

	@Test
	@DisplayName("이메일과 비밀번호 형식이 올바르면 컬렉션에 아무것도 저장하지 않는다.")
	void validateEmailSuccess() {
		// given
		LoginForm loginForm = new LoginForm("yeon@email.com", "yeon!123");

		// when
		Set<ConstraintViolation<LoginForm>> violations = validator.validate(loginForm);

		// then
		assertThat(violations).isEmpty();
	}

	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = {" ", "email.com", "email#email.com", "email@email.c"})
	@DisplayName("이메일 형식이 올바르지 않다면 이메일 오류를 컬렉션에 저장한다.")
	void validateEmailFail(String email) {
		// given
		LoginForm loginForm = new LoginForm(email, "yeon!123");

		// when
		Set<ConstraintViolation<LoginForm>> violations = validator.validate(loginForm);

		// then
		assertAll(
			() -> assertThat(violations).hasSize(1),
			() -> assertThat(violations.iterator().next().getMessage()).isEqualTo("이메일이 양식에 맞지 않습니다.")
		);
	}

	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = {" ", "yeon", "yeonhyun", "yeon1234", "12345678", "1234567!"})
	@DisplayName("비밀번호 형식이 올바르지 않다면 비밀번호 오류를 컬렉션에 저장한다.")
	void validatePasswordFail(String password) {
		// given
		LoginForm loginForm = new LoginForm("yeon@email.com", password);

		// when
		Set<ConstraintViolation<LoginForm>> violations = validator.validate(loginForm);

		// then
		assertAll(
			() -> assertThat(violations).hasSize(1),
			() -> assertThat(violations.iterator().next().getMessage()).isEqualTo(
				"비밀번호는 영문, 숫자, 특수기호를 모두 포함하는 8-16자 문자열입니다.")
		);
	}

}
