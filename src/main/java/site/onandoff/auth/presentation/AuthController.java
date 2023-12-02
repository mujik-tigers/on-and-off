package site.onandoff.auth.presentation;

import static site.onandoff.util.api.ResponseMessage.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import site.onandoff.auth.Account;
import site.onandoff.auth.application.AuthService;
import site.onandoff.auth.dto.AuthenticationTokenPair;
import site.onandoff.auth.dto.LoginData;
import site.onandoff.auth.dto.ReissuedAccessToken;
import site.onandoff.util.api.ApiResponse;
import site.onandoff.util.resolver.Login;

@RestController
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/login")
	public ApiResponse<AuthenticationTokenPair> login(@RequestBody @Valid LoginData loginData) {
		return ApiResponse.ok(LOGIN_SUCCESS.getMessage(), authService.login(loginData));
	}

	@PostMapping("/reissue")
	public ApiResponse<ReissuedAccessToken> reissueAccessToken(@Login Account account) {
		return ApiResponse.ok(REISSUE_TOKENS_SUCCESS.getMessage(), authService.reissueAccessToken(account));
	}

}
