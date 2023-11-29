package site.onandoff.member.dto;

import static org.assertj.core.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import site.onandoff.member.Member;
import site.onandoff.member.Provider;
import site.onandoff.member.infrastructure.MemberRepository;

@SpringBootTest
class SignUpFormTest {

	@Autowired
	private Validator validator;

	@Autowired
	private MemberRepository memberRepository;

	@Test
	@DisplayName("만약 올바르지 않은 이메일 형식을 입력하면 이메일 형식 관련 violation 을 반환한다.")
	void wrongInputForm() throws Exception {
		// given
		String invalidEmailInput = "it's fake email";
		SignUpForm signUpForm = new SignUpForm(invalidEmailInput, "hyun", "1234");

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
		SignUpForm signUpForm = new SignUpForm(invalidEmailInput, "hyun", "1234");

		// when
		Set<ConstraintViolation<SignUpForm>> violations = validator.validate(signUpForm);

		// then
		assertThat(violations).isEmpty();
	}

	@Test
	@DisplayName("중복되지 않은 이메일을 입력하면 violation 을 반환하지 않는다.")
	void uniqueEmailInput() throws Exception {
		// given
		String uniqueEmailInput = "ghkdgus29@codesquand.ac.kr";
		SignUpForm signUpForm = new SignUpForm(uniqueEmailInput, "hyun", "1234");

		// when
		Set<ConstraintViolation<SignUpForm>> violations = validator.validate(signUpForm);

		// then
		assertThat(violations).isEmpty();
	}

	@Test
	@DisplayName("중복된 이메일을 입력하면 중복 관련 violation 을 반환한다.")
	void duplicateEmailInput() throws Exception {
		// given
		String duplicateEmailInput = "ghkdgus29@codesquand.ac.kr";
		memberRepository.save(new Member(duplicateEmailInput, "hoon", "1234", Provider.LOCAL));
		SignUpForm signUpForm = new SignUpForm(duplicateEmailInput, "hyun", "1234");

		// when
		Set<ConstraintViolation<SignUpForm>> violations = validator.validate(signUpForm);

		// then
		assertThat(violations.size()).isEqualTo(1);
		assertThat(violations.iterator().next().getMessage()).isEqualTo("이미 존재하는 이메일입니다.");
	}

	@Test
	@DisplayName("62자 초과의 이메일을 입력하면 글자수 관련 violation 을 반환한다.")
	void shortEmailInput() throws Exception {
		// given
		String longEmailInput = "hellohellohellohellohellohellohellohellohellohellohellohello@naver.com";
		SignUpForm signUpForm = new SignUpForm(longEmailInput, "hyun", "1234");

		// when
		Set<ConstraintViolation<SignUpForm>> violations = validator.validate(signUpForm);

		// then
		assertThat(violations.size()).isEqualTo(1);
		assertThat(violations.iterator().next().getMessage()).isEqualTo("이메일 길이는 62자 이하입니다.");
	}
}
