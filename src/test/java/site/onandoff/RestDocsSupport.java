package site.onandoff;

import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import site.onandoff.exception.GlobalExceptionHandler;
import site.onandoff.interceptor.AccessTokenInterceptor;
import site.onandoff.interceptor.RefreshTokenInterceptor;

@ExtendWith(RestDocumentationExtension.class)
public abstract class RestDocsSupport {

	protected MockMvc mockMvc;
	protected ObjectMapper objectMapper = new ObjectMapper();
	private final AccessTokenInterceptor accessTokenInterceptor = mock(AccessTokenInterceptor.class);
	private final RefreshTokenInterceptor refreshTokenInterceptor = mock(RefreshTokenInterceptor.class);

	@BeforeEach
	void setUp(RestDocumentationContextProvider provider) throws Exception {
		this.mockMvc = MockMvcBuilders.standaloneSetup(initController())
			.setControllerAdvice(GlobalExceptionHandler.class)
			.addInterceptors(accessTokenInterceptor, refreshTokenInterceptor)
			.apply(MockMvcRestDocumentation.documentationConfiguration(provider))
			.build();

		given(accessTokenInterceptor.preHandle(any(), any(), any()))
			.willReturn(true);
		given(refreshTokenInterceptor.preHandle(any(), any(), any()))
			.willReturn(true);
	}

	protected abstract Object initController();

}
