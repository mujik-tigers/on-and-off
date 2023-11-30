package site.onandoff.member.application;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import site.onandoff.member.Member;
import site.onandoff.member.Provider;
import site.onandoff.member.dto.SignUpForm;
import site.onandoff.member.dto.SignUpSuccessResponse;
import site.onandoff.member.infrastructure.MemberRepository;
import site.onandoff.util.encryption.AES256Manager;
import site.onandoff.util.encryption.BcryptManager;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final AES256Manager aes256Manager;

	public SignUpSuccessResponse signUp(SignUpForm signUpForm) {
		Member newMember = new Member(
			aes256Manager.encrypt(signUpForm.getEmail()),
			signUpForm.getNickname(),
			BcryptManager.encrypt(signUpForm.getPassword()),
			Provider.LOCAL
		);

		Member savedMember = memberRepository.save(newMember);

		return new SignUpSuccessResponse(savedMember);
	}
}
