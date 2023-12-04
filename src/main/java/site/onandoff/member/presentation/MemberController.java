package site.onandoff.member.presentation;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import site.onandoff.auth.Account;
import site.onandoff.member.application.MemberService;
import site.onandoff.member.dto.ModifiedMember;
import site.onandoff.member.dto.NicknameChangeForm;
import site.onandoff.member.dto.PasswordChangeForm;
import site.onandoff.member.dto.SignUpForm;
import site.onandoff.member.dto.SignUpSuccessResponse;
import site.onandoff.util.api.ApiResponse;
import site.onandoff.util.api.ResponseMessage;
import site.onandoff.util.resolver.Login;

@RestController
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	@PostMapping("/members")
	public ApiResponse<SignUpSuccessResponse> signUp(@RequestBody @Valid SignUpForm signUpForm) {
		SignUpSuccessResponse signUpSuccessResponse = memberService.signUp(signUpForm.toUnique());

		return ApiResponse.ok(ResponseMessage.SIGNUP_SUCCESS.getMessage(), signUpSuccessResponse);
	}

	@PatchMapping("/members/nickname")
	public ApiResponse<ModifiedMember> modifyNickname(@Login Account account,
		@RequestBody @Valid NicknameChangeForm nicknameChangeForm) {
		ModifiedMember modifiedMember = memberService.modifyNickname(nicknameChangeForm.toUnique(account.getId()));

		return ApiResponse.ok(ResponseMessage.NICKNAME_MODIFICATION_SUCCESS.getMessage(), modifiedMember);
	}

	@PatchMapping("/members/password")
	public ApiResponse<ModifiedMember> modifyPassword(@Login Account account,
		@RequestBody @Valid PasswordChangeForm passwordChangeForm) {
		ModifiedMember modifiedMember = memberService.modifyPassword(passwordChangeForm.toValid(account.getId()));

		return ApiResponse.ok(ResponseMessage.PASSWORD_MODIFICATION_SUCCESS.getMessage(), modifiedMember);
	}

	@DeleteMapping("/members")
	public ApiResponse<Void> deleteMember(@Login Account account) {
		memberService.deleteMember(account.getId());
		return ApiResponse.ok(ResponseMessage.MEMBER_DELETE_SUCCESS.getMessage(), null);
	}

}
