package site.onandoff;

import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import site.onandoff.auth.application.AuthManager;
import site.onandoff.auth.application.AuthService;
import site.onandoff.auth.application.TokenManager;
import site.onandoff.auth.presentation.AuthController;
import site.onandoff.member.application.MemberService;
import site.onandoff.member.presentation.MemberController;
import site.onandoff.util.encryption.AES256Manager;

@WebMvcTest(controllers = {AuthController.class, MemberController.class})
@AutoConfigureRestDocs
public abstract class RestDocsSupport {

	@Autowired
	protected MockMvc mockMvc;

	@Autowired
	protected ObjectMapper objectMapper;

	@MockBean
	protected AuthService authService;

	@MockBean
	protected AuthManager authManager;

	@MockBean
	protected TokenManager tokenManager;

	@MockBean
	protected MemberService memberService;

	@MockBean
	protected AES256Manager aes256Manager;

	@BeforeEach
	void setUp() {
		given(tokenManager.validateRefreshToken(any()))
			.willReturn(Jwts.claims().add("id", 1L).build());
		given(tokenManager.validateAccessToken(any()))
			.willReturn(Jwts.claims().add("id", 1L).build());
	}

}
