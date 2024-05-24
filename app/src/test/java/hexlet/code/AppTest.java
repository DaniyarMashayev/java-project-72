package hexlet.code;

import static hexlet.code.App.readResourceFile;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

import hexlet.code.model.Url;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlsRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.Javalin;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.javalin.testtools.JavalinTest;

public class AppTest {
    private static Javalin app;
    private static String baseUrl;

    @BeforeAll
    public static void beforeAll() throws IOException {
        MockWebServer mockServer = new MockWebServer();
        baseUrl = mockServer.url("/").toString();
        MockResponse mockResponse = new MockResponse().setBody(readResourceFile("fixtures/test.html"));
        mockServer.enqueue(mockResponse);
    }

    @BeforeEach
    public final void setUp() throws IOException, SQLException {
        app = App.getApp();
    }

    @Test
    public void testMainPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.rootPath());
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("Анализатор страниц");
        });
    }

    @Test
    public void testUrl1() {
        JavalinTest.test(app, (server, client) -> {
            var request = "url=https://test-domain.com";
            var response = client.post(NamedRoutes.urlsPath(), request);
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("https://test-domain.com");
            assertThat(UrlsRepository.getEntities()).hasSize(1);
        });
    }

    @Test
    public void testUrl2() {
        JavalinTest.test(app, (server, client) -> {
            var request = "url=https://test-domain.com:8080/example/test";
            var response = client.post(NamedRoutes.urlsPath(), request);
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("https://test-domain.com:8080");
            assertThat(UrlsRepository.getEntities()).hasSize(1);
        });
    }

    @Test
    public void testUrlNotFound() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.urlPath(99999L));
            assertThat(response.code()).isEqualTo(404);
        });
    }

    @Test
    public void testUrlsPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.urlsPath());
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    public void testUrlPage() {
        String input = "https://www.mail.ru";
        var url = new Url(input);
        url.setCreatedAt(new Timestamp(new Date().getTime()));
        UrlsRepository.save(url);

        JavalinTest.test(app, (server, client) -> {
            assertTrue(UrlsRepository.find(url.getId()).isPresent());

            var response = client.get(NamedRoutes.urlPath(url.getId()));

            assertThat(response.code()).isEqualTo(200);
            assertThat(Objects.requireNonNull(response.body()).string()).contains(input);
            assertEquals(UrlsRepository.find(url.getId()).orElseThrow().getName(), input);
            assertEquals(UrlsRepository.find(input).orElseThrow().getName(), input);
        });
    }

    @Test
    public void testUrlCheck() {
        Url url = new Url(baseUrl);
        UrlsRepository.save(url);
        JavalinTest.test(app, (server, client) -> {
            try (var response = client.post(NamedRoutes.urlChecksPath(url.getId()))) {
                var check = UrlCheckRepository.find(url.getId()).orElseThrow();
                assertThat(response.code()).isEqualTo(200);
                assertThat(check.getTitle()).isEqualTo("Test Title");
                assertThat(check.getH1()).isEqualTo("Test Page Analyzer");
                assertThat(check.getDescription()).isEqualTo("");
            } catch (final Exception th) {
                System.out.println(th.getMessage());
            }
        });
    }
}
