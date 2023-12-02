package site.onandoff.auth.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import site.onandoff.auth.Account;
import site.onandoff.auth.dto.AuthenticationTokenPair;
import site.onandoff.auth.dto.LoginForm;
import site.onandoff.auth.dto.ReissuedAccessToken;
import site.onandoff.member.Member;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

	private final AuthManager authManager;
	private final TokenManager tokenManager;

	public AuthenticationTokenPair login(LoginForm loginForm) {
		Member member = authManager.authenticateLoginData(loginForm);
		return tokenManager.issueTokenPair(member.getId());
	}

	public ReissuedAccessToken reissueAccessToken(Account account) {
		return tokenManager.issueAccessToken(account.getId());
	}

}
