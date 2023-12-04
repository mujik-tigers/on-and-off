package site.onandoff.interceptor;

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
	private static final String ACCOUNT_ID = "id";
	private static final WhiteList whiteList = WhiteList.create()
		.addPathAndMethod("/members", HttpMethod.POST);

	private final AuthManager authManager;
	private final TokenManager tokenManager;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
		Exception {
		if (CorsUtils.isPreFlightRequest(request))
			return true;

		if (whiteList.contains(request.getPathInfo(), request.getMethod())) {
			return true;
		}

		String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
		String token = authManager.validateAuthorizationHeader(authorizationHeader);

		Claims claims = tokenManager.validateAccessToken(token);

		request.setAttribute(ACCOUNT_ID, claims.get(ACCOUNT_ID, Long.class));
		return true;
	}

}
