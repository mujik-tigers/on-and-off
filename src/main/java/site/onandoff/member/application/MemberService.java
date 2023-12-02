package site.onandoff.member.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import site.onandoff.member.Member;
import site.onandoff.member.Provider;
import site.onandoff.member.dto.SignUpSuccessResponse;
import site.onandoff.member.dto.UniqueSignUpForm;
import site.onandoff.member.infrastructure.MemberRepository;
import site.onandoff.util.encryption.AES256Manager;
import site.onandoff.util.encryption.BCryptManager;

@Service
@Transactional(readOnly = true)
@Validated
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final AES256Manager aes256Manager;

	@Transactional
	public SignUpSuccessResponse signUp(@Valid UniqueSignUpForm signUpForm) {
		Member newMember = new Member(
			aes256Manager.encrypt(signUpForm.getEmail()),
			signUpForm.getNickname(),
			BCryptManager.encrypt(signUpForm.getPassword()),
			Provider.LOCAL
		);

		Member savedMember = memberRepository.save(newMember);

		return new SignUpSuccessResponse(savedMember.getId());
	}
}
