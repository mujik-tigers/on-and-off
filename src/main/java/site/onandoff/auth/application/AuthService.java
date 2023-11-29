package site.onandoff.auth.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import site.onandoff.auth.Account;
import site.onandoff.auth.dto.AuthenticationTokens;
import site.onandoff.auth.dto.LoginData;
import site.onandoff.member.Member;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

	private final AuthManager authManager;
	private final TokenManager tokenManager;

	@Transactional
	public AuthenticationTokens login(LoginData loginData) {
		Member member = authManager.authenticateLoginData(loginData);
		return tokenManager.issueTokens(member.getId());
	}

	@Transactional
	public AuthenticationTokens reissueTokens(Account account) {
		return tokenManager.issueTokens(account.getId());
	}

}
