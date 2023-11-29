package site.onandoff.interceptor;

import java.util.Arrays;

import org.springframework.http.HttpMethod;
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
public class AccessTokenInterceptor implements HandlerInterceptor {

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

		if (Whitelist.contains(request))
			return true;

		String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
		String token = authManager.validateAuthorizationHeader(authorizationHeader);

		Claims claims = tokenManager.validateAccessToken(token);

		request.setAttribute(ACCOUNT_ID, claims.get(ACCOUNT_ID, Long.class));
		request.setAttribute(ACCOUNT_TOKEN, token);
		return true;
	}

	enum Whitelist {

		LOGIN(HttpMethod.POST, "/login");

		private final HttpMethod httpMethod;
		private final String url;

		Whitelist(HttpMethod httpMethod, String url) {
			this.httpMethod = httpMethod;
			this.url = url;
		}

		public static boolean contains(HttpServletRequest request) {
			return Arrays.stream(values())
				.anyMatch(whitelist -> whitelist.httpMethod.name().equals(request.getMethod()) &&
					whitelist.url.equals(request.getRequestURI()));
		}

	}

}
