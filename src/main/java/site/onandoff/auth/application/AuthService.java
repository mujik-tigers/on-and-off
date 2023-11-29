package site.onandoff.auth.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import site.onandoff.auth.Account;
import site.onandoff.auth.dto.AuthenticationTokenPair;
import site.onandoff.auth.dto.LoginData;
import site.onandoff.auth.dto.ReissuedAccessToken;
import site.onandoff.member.Member;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

	private final AuthManager authManager;
	private final TokenManager tokenManager;

	@Transactional
	public AuthenticationTokenPair login(LoginData loginData) {
		Member member = authManager.authenticateLoginData(loginData);
		return tokenManager.issueTokenPair(member.getId());
	}

	@Transactional
	public ReissuedAccessToken reissueAccessToken(Account account) {
		return tokenManager.issueAccessToken(account.getId());
	}

}
