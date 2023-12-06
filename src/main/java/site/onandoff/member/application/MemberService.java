package site.onandoff.member.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import site.onandoff.exception.member.MemberNotFoundException;
import site.onandoff.member.Member;
import site.onandoff.member.Provider;
import site.onandoff.member.dto.MemberProfile;
import site.onandoff.member.dto.ModifiedMember;
import site.onandoff.member.dto.SignUpSuccessResponse;
import site.onandoff.member.dto.UniqueNicknameChangeForm;
import site.onandoff.member.dto.UniqueSignUpForm;
import site.onandoff.member.dto.ValidPasswordChangeForm;
import site.onandoff.member.infrastructure.MemberRepository;
import site.onandoff.util.encryption.AES256Manager;
import site.onandoff.util.encryption.BCryptManager;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final AES256Manager aes256Manager;

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

	public ModifiedMember modifyNickname(@Valid UniqueNicknameChangeForm nicknameChangeForm) {
		Member member = findMemberBy(nicknameChangeForm.getId());
		member.modifyNickname(nicknameChangeForm.getNickname());

		return new ModifiedMember(member.getId(), member.getNickname());
	}

	public ModifiedMember modifyPassword(@Valid ValidPasswordChangeForm passwordChangeForm) {
		Member member = findMemberBy(passwordChangeForm.getId());
		member.modifyPassword(BCryptManager.encrypt(passwordChangeForm.getNewPassword()));

		return new ModifiedMember(member.getId(), member.getNickname());
	}

	public void deleteMember(Long memberId) {
		Member member = findMemberBy(memberId);
		memberRepository.delete(member);
	}

	@Transactional(readOnly = true)
	public MemberProfile fetchMemberInformation(Long memberId) {
		Member member = findMemberBy(memberId);
		String decryptedMemberEmail = aes256Manager.decrypt(member.getEmail());
		return new MemberProfile(decryptedMemberEmail, member.getNickname());
	}

	private Member findMemberBy(Long id) {
		return memberRepository.findById(id)
			.orElseThrow(MemberNotFoundException::new);
	}

}
