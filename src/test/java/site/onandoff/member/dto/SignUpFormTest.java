package site.onandoff.member.dto;

import static org.assertj.core.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class SignUpFormTest {

	private static ValidatorFactory validatorFactory;
	private static Validator validator;

	@BeforeAll
	public static void init() {
		validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}

	@AfterAll
	public static void close() {
		validatorFactory.close();
	}

	@Test
	@DisplayName("만약 올바르지 않은 이메일 형식을 입력하면 violation 을 반환한다.")
	void wrongInputForm() throws Exception {
		// given
		String invalidEmailInput = "it's fake email";
		SignUpForm signUpForm = new SignUpForm(invalidEmailInput, "hyun", "1234");

		// when
		Set<ConstraintViolation<SignUpForm>> violations = validator.validate(signUpForm);

		// then
		assertThat(violations.size()).isEqualTo(1);
		for (ConstraintViolation<SignUpForm> violation : violations) {
			assertThat(violation.getMessage()).isEqualTo("이메일이 양식에 맞지 않습니다.");
		}
	}

	@Test
	@DisplayName("올바른 이메일 형식을 입력하면 violation 을 반환하지 않는다.")
	void correctInputForm() throws Exception {
		// given
		String invalidEmailInput = "ghkdgus29@codesquand.ac.kr";
		SignUpForm signUpForm = new SignUpForm(invalidEmailInput, "hyun", "1234");

		// when
		Set<ConstraintViolation<SignUpForm>> violations = validator.validate(signUpForm);

		// then
		assertThat(violations).isEmpty();
	}

}
