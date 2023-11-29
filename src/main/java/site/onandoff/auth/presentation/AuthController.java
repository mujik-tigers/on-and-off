package site.onandoff.auth.presentation;

import static site.onandoff.util.ResponseMessage.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import site.onandoff.auth.Account;
import site.onandoff.auth.application.AuthService;
import site.onandoff.auth.dto.AuthenticationTokens;
import site.onandoff.auth.dto.LoginData;
import site.onandoff.util.ApiResponse;
import site.onandoff.util.Login;

@RestController
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/login")
	public ApiResponse<AuthenticationTokens> login(@RequestBody LoginData loginData) {
		return ApiResponse.ok(LOGIN_SUCCESS.getMessage(), authService.login(loginData));
	}

	@PostMapping("/reissue")
	public ApiResponse<AuthenticationTokens> reissueTokens(@Login Account account) {
		return ApiResponse.ok(REISSUE_TOKENS_SUCCESS.getMessage(),
			authService.reissueTokens(account));
	}

}
