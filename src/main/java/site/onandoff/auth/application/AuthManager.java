package site.onandoff.auth.application;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import site.onandoff.auth.dto.LoginData;
import site.onandoff.exception.auth.AuthorizationHeaderException;
import site.onandoff.exception.auth.InvalidLoginException;
import site.onandoff.member.Member;
import site.onandoff.member.MemberRepository;
import site.onandoff.member.Provider;
import site.onandoff.util.PasswordEncoder;

@Component
@RequiredArgsConstructor
public class AuthManager {

	private static final String AUTHORIZATION_PREFIX = "Bearer ";

	private final MemberRepository memberRepository;

	public String validateAuthorizationHeader(String authorizationHeader) {
		if (authorizationHeader == null || !authorizationHeader.startsWith(AUTHORIZATION_PREFIX)) {
			throw new AuthorizationHeaderException();
		}

		return authorizationHeader.substring(AUTHORIZATION_PREFIX.length());
	}

	public Member authenticateLoginData(LoginData loginData) {
		Member member = memberRepository.findByEmailAndProvider(loginData.getEmail(), Provider.LOCAL)
			.orElseThrow(InvalidLoginException::new);

		if (PasswordEncoder.isMatch(loginData.getPassword(), member.getPassword())) {
			return member;
		}

		throw new InvalidLoginException();
	}

}
