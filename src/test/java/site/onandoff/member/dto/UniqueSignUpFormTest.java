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

class UniqueSignUpFormTest extends IntegrationTestSupport {

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private AES256Manager aes256Manager;

	@Autowired
	private Validator validator;

	@Test
	@DisplayName("중복되지 않은 이메일을 입력하면 violation 을 반환하지 않는다.")
	void uniqueEmailInput() throws Exception {
		// given
		String uniqueEmailInput = "ghkdgus29@codesquand.ac.kr";
		UniqueSignUpForm signUpForm = new UniqueSignUpForm(uniqueEmailInput, "hyun", "1234567a!");

		// when
		Set<ConstraintViolation<UniqueSignUpForm>> violations = validator.validate(signUpForm);

		// then
		assertThat(violations).isEmpty();
	}

	@Test
	@DisplayName("중복된 이메일을 입력하면 중복 관련 violation 을 반환한다.")
	void duplicateEmailInput() throws Exception {
		// given
		String duplicateEmailInput = "ghkdgus29@codesquand.ac.kr";
		memberRepository.save(new Member(aes256Manager.encrypt(duplicateEmailInput), "hoon", "1234", Provider.LOCAL));
		UniqueSignUpForm signUpForm = new UniqueSignUpForm(duplicateEmailInput, "hyun", "1234567a!");

		// when
		Set<ConstraintViolation<UniqueSignUpForm>> violations = validator.validate(signUpForm);

		// then
		assertThat(violations.size()).isEqualTo(1);
	}

	@Test
	@DisplayName("중복된 닉네임을 입력하면 중복 관련 violation 을 반환한다.")
	void duplicateNicknameInput() throws Exception {
		// given
		String duplicateNicknameInput = "hyun";
		memberRepository.save(
			new Member("ghkdgus29@codesquad.kr", duplicateNicknameInput, "1234567a!", Provider.LOCAL));
		UniqueSignUpForm signUpForm = new UniqueSignUpForm("ghkdgus29@naver.com", duplicateNicknameInput, "1234567a!");

		// when
		Set<ConstraintViolation<UniqueSignUpForm>> violations = validator.validate(signUpForm);

		// then
		assertThat(violations.size()).isEqualTo(1);
	}

}
