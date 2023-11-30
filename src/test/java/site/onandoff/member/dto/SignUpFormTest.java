package site.onandoff.member.dto;

import static org.assertj.core.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import site.onandoff.IntegrationTestSupport;
import site.onandoff.member.Member;
import site.onandoff.member.Provider;
import site.onandoff.member.infrastructure.MemberRepository;
import site.onandoff.util.encryption.AES256Manager;

class SignUpFormTest extends IntegrationTestSupport {

	@Autowired
	private Validator validator;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private AES256Manager aes256Manager;

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
	@DisplayName("중복되지 않은 이메일을 입력하면 violation 을 반환하지 않는다.")
	void uniqueEmailInput() throws Exception {
		// given
		String uniqueEmailInput = "ghkdgus29@codesquand.ac.kr";
		SignUpForm signUpForm = new SignUpForm(uniqueEmailInput, "hyun", "1234567a!");

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
		memberRepository.save(new Member(aes256Manager.encrypt(duplicateEmailInput), "hoon", "1234", Provider.LOCAL));
		SignUpForm signUpForm = new SignUpForm(duplicateEmailInput, "hyun", "1234567a!");

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
		SignUpForm signUpForm = new SignUpForm(longEmailInput, "hyun", "1234567a!");

		// when
		Set<ConstraintViolation<SignUpForm>> violations = validator.validate(signUpForm);

		// then
		assertThat(violations.size()).isEqualTo(1);
		assertThat(violations.iterator().next().getMessage()).isEqualTo("이메일 길이는 62자 이하입니다.");
	}

	@Test
	@DisplayName("DB를 사용하지 않는 검증 먼저 수행한 후, DB를 사용하는 검증을 수행하기 때문에 글자수 위반 violation과 중복 유저 violation 이 같이 발생하지 않는다.")
	void DBlessGroupFirstThenDBGroup() throws Exception {
		// given
		String longEmailInput = "hellohellohellohellohellohellohellohellohellohellohellohello@naver.com";
		memberRepository.save(new Member(longEmailInput, "hoon", "1234567a!", Provider.LOCAL));
		SignUpForm signUpForm = new SignUpForm(longEmailInput, "hyun", "1234567a!");

		// when
		Set<ConstraintViolation<SignUpForm>> violations = validator.validate(signUpForm);

		// then
		assertThat(violations.size()).isEqualTo(1);
		assertThat(violations.iterator().next().getMessage()).isEqualTo("이메일 길이는 62자 이하입니다.");
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

	@Test
	@DisplayName("중복된 닉네임을 입력하면 중복 관련 violation 을 반환한다.")
	void duplicateNicknameInput() throws Exception {
		// given
		String duplicateNicknameInput = "hyun";
		memberRepository.save(
			new Member("ghkdgus29@codesquad.kr", duplicateNicknameInput, "1234567a!", Provider.LOCAL));
		SignUpForm signUpForm = new SignUpForm("ghkdgus29@naver.com", duplicateNicknameInput, "1234567a!");

		// when
		Set<ConstraintViolation<SignUpForm>> violations = validator.validate(signUpForm);

		// then
		assertThat(violations.size()).isEqualTo(1);
		assertThat(violations.iterator().next().getMessage()).isEqualTo("이미 존재하는 닉네임입니다.");
	}

}
