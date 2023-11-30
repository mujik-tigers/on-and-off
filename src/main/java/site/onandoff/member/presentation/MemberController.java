package site.onandoff.member.presentation;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import site.onandoff.member.application.MemberService;
import site.onandoff.member.dto.SignUpForm;
import site.onandoff.member.dto.SignUpSuccessResponse;
import site.onandoff.util.ApiResponse;
import site.onandoff.util.ResponseMessage;

@RestController
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	@PostMapping("/members")
	public ApiResponse<SignUpSuccessResponse> signUp(@RequestBody @Validated SignUpForm signUpForm) {
		SignUpSuccessResponse signUpSuccessResponse = memberService.signUp(signUpForm);

		return ApiResponse.ok(ResponseMessage.SIGNUP_SUCCESS.getMessage(), signUpSuccessResponse);
	}

}
