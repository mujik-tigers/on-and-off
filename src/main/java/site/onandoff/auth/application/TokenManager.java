package site.onandoff.auth.application;

import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import site.onandoff.auth.dto.AuthenticationTokenPair;
import site.onandoff.auth.dto.ReissuedAccessToken;
import site.onandoff.exception.auth.ExpiredTokenException;
import site.onandoff.exception.auth.InvalidTokenException;

@Component
@RequiredArgsConstructor
public class TokenManager {

	private final TokenProperties tokenProperties;

	public AuthenticationTokenPair issueTokenPair(Long id) {
		String accessToken = generateToken(Map.of("id", id, "refresh", false),
			tokenProperties.getAccessTokenDuration());
		String refreshToken = generateToken(Map.of("id", id, "refresh", true),
			tokenProperties.getRefreshTokenDuration());
		return new AuthenticationTokenPair(accessToken, refreshToken);
	}

	public ReissuedAccessToken issueAccessToken(Long id) {
		String accessToken = generateToken(Map.of("id", id, "refresh", false),
			tokenProperties.getAccessTokenDuration());
		return new ReissuedAccessToken(accessToken);
	}

	private String generateToken(Map<String, Object> claims, long duration) {
		Date now = new Date();
		Date expiry = new Date(now.getTime() + duration);

		return Jwts.builder()
			.claims(claims)
			.issuer(tokenProperties.getIssuer())
			.issuedAt(now)
			.expiration(expiry)
			.signWith(Keys.hmacShaKeyFor(tokenProperties.getSecret().getBytes()))
			.compact();
	}

	public Claims validateAccessToken(String accessToken) {
		Claims claims = parse(accessToken);
		if (claims.get("refresh", Boolean.class)) {
			throw new InvalidTokenException();
		}

		return parse(accessToken);
	}

	public Claims validateRefreshToken(String refreshToken) {
		Claims claims = parse(refreshToken);
		if (claims.get("refresh", Boolean.class)) {
			return claims;
		}

		throw new InvalidTokenException();
	}

	private Claims parse(String token) {
		try {
			return Jwts.parser()
				.verifyWith(Keys.hmacShaKeyFor(tokenProperties.getSecret().getBytes()))
				.build()
				.parseSignedClaims(token)
				.getPayload();
		} catch (
			ExpiredJwtException e) {
			throw new ExpiredTokenException();
		} catch (UnsupportedJwtException | MalformedJwtException | SignatureException e) {
			throw new InvalidTokenException();
		}
	}

}
