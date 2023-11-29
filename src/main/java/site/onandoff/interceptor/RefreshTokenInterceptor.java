package site.onandoff.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import site.onandoff.auth.application.AuthManager;
import site.onandoff.auth.application.TokenManager;

@Component
@RequiredArgsConstructor
public class RefreshTokenInterceptor implements HandlerInterceptor {

	private static final String AUTHORIZATION_HEADER = "Authorization";
	public static final String ACCOUNT_ID = "id";
	public static final String ACCOUNT_TOKEN = "token";

	private final AuthManager authManager;
	private final TokenManager tokenManager;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
		Exception {
		if (CorsUtils.isPreFlightRequest(request))
			return true;

		String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
		String token = authManager.validateAuthorizationHeader(authorizationHeader);

		Claims claims = tokenManager.validateRefreshToken(token);

		request.setAttribute(ACCOUNT_ID, claims.get(ACCOUNT_ID, Long.class));
		request.setAttribute(ACCOUNT_TOKEN, token);
		return true;
	}

}
