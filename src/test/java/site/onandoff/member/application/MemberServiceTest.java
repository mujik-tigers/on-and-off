package site.onandoff.member.application;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.validation.ConstraintViolationException;
import site.onandoff.IntegrationTestSupport;
import site.onandoff.member.Member;
import site.onandoff.member.Provider;
import site.onandoff.member.dto.SignUpForm;
import site.onandoff.member.dto.SignUpSuccessResponse;
import site.onandoff.member.dto.UniqueSignUpForm;
import site.onandoff.member.infrastructure.MemberRepository;

class MemberServiceTest extends IntegrationTestSupport {

	@Autowired
	private MemberService memberService;

	@Autowired
	private MemberRepository memberRepository;

	@Test
	@DisplayName("회원가입 성공 시, 저장된 멤버의 PK를 담는 SignUpSuccessResponse 를 반환한다.")
	void signUpSuccess() throws Exception {
		// given
		SignUpForm signUpForm = new SignUpForm("ghkdgus29@naver.com", "hyun", "1234567a!");
		UniqueSignUpForm uniqueSignUpForm = signUpForm.toUnique();

		// when
		SignUpSuccessResponse signUpSuccessResponse = memberService.signUp(uniqueSignUpForm);

		// then
		List<Member> members = memberRepository.findAll();
		Member savedMember = members.get(0);

		assertThat(members.size()).isEqualTo(1);
		assertThat(signUpSuccessResponse).usingRecursiveComparison()
			.isEqualTo(new SignUpSuccessResponse(savedMember.getId()));
	}

	@Test
	@DisplayName("중복되는 이메일이나 닉네임이 있는 경우, ConstraintViolationException 이 발생한다.")
	void signUpFail() throws Exception {
		// given
		memberRepository.save(new Member("ghkdgus28@naver.com", "hyun", "1234asd1!", Provider.LOCAL));
		SignUpForm signUpForm = new SignUpForm("ghkdgus29@naver.com", "hyun", "1234567a!");
		UniqueSignUpForm uniqueSignUpForm = signUpForm.toUnique();

		// when & then
		assertThatThrownBy(() -> memberService.signUp(uniqueSignUpForm))
			.isInstanceOf(ConstraintViolationException.class)
			.hasMessageContaining("이미 존재하는 닉네임입니다.");
	}

}
