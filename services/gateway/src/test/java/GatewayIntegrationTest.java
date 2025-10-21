import com.klasix12.GatewayApplication;
import com.klasix12.config.routes.AuthRoutesConfig;
import com.klasix12.redis.RedisService;
import com.klasix12.security.service.TokenManager;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest(
        classes = GatewayApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureWebTestClient
@Import(AuthRoutesConfig.class)
public class GatewayIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Mock
    private TokenManager tokenManager;

    @Mock
    private RedisService redisService;

    private static MockWebServer mockAuthService;

    @BeforeAll
    static void setupServer() throws IOException {
        mockAuthService = new MockWebServer();
        mockAuthService.start(8082);
    }

    @AfterAll
    static void shutdownServer() throws IOException {
        mockAuthService.shutdown();
    }

    // TODO: поменять на приватные эндпоинты
    @Test
    void shouldReturnUnauthorizedWhenNoToken() {
        webTestClient.post().uri("/auth/logout")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void shouldRejectIfTokenBlacklisted() {
        when(redisService.exists(anyString())).thenReturn(Mono.just(true));

        webTestClient.post().uri("/auth/logout")
                .header(HttpHeaders.AUTHORIZATION, "Bearer fake.jwt")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void shouldPassIfValidTokenAndProxyRequest() {
        String token = "valid.jwt";

        when(redisService.exists(anyString())).thenReturn(Mono.just(false));
        when(tokenManager.isAccessTokenValid(token)).thenReturn(Mono.just(true));
        when(tokenManager.extractId(token)).thenReturn(Mono.just("1"));
        when(tokenManager.extractUsername(token)).thenReturn(Mono.just("test_user"));
        when(tokenManager.extractRoles(token)).thenReturn(Mono.just(List.of("ROLE_USER")));

        mockAuthService.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"status\":\"ok\"}")
                .addHeader("Content-Type", "application/json"));

        webTestClient.post().uri("/auth/login")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.status").isEqualTo("ok");
    }
}