package hexlet.code;

import static hexlet.code.App.readResourceFile;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlsRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.Javalin;
import io.javalin.http.NotFoundResponse;
import okhttp3.Response;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.javalin.testtools.JavalinTest;

public class AppTest {
    private static Javalin app;
    private static String baseUrl;
    private static MockWebServer mockServer;

    @BeforeAll
    public static void beforeAll() throws IOException {
        mockServer = new MockWebServer();
        baseUrl = mockServer.url("/").toString();
        MockResponse mockResponse = new MockResponse().setBody(readResourceFile("fixtures/test.html"));
        mockServer.enqueue(mockResponse);
    }

    @BeforeEach
    public final void setUp() throws IOException, SQLException {
        app = App.getApp();
    }

    @AfterAll
    public static void afterAll() throws IOException {
        mockServer.shutdown();
    }

    @Test
    public void testMainPage() {
        JavalinTest.test(app, (server, client) -> {
            Response response = client.get(NamedRoutes.rootPath());
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("Анализатор страниц");
        });
    }

    @Test
    public void testUrlsPage() {
        JavalinTest.test(app, (server, client) -> {
            Response response = client.get(NamedRoutes.urlsPath());
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    public void testUrlPage() {
        String input = "https://www.mail.ru";
        Url url = new Url(input);
        url.setCreatedAt(new Timestamp(new Date().getTime()));
        UrlsRepository.save(url);

        JavalinTest.test(app, (server, client) -> {
            assertTrue(UrlsRepository.find(url.getId()).isPresent());

            Response response = client.get(NamedRoutes.urlPath(url.getId()));

            assertThat(response.code()).isEqualTo(200);
            assertThat(Objects.requireNonNull(response.body()).string()).contains(input);
            assertEquals(UrlsRepository.find(url.getId()).orElseThrow().getName(), input);
            assertEquals(UrlsRepository.find(input).orElseThrow().getName(), input);
        });
    }

    @Test
    public void testRegisterNewUrl() {
        String input = "url=https://www.mail.ru";
        JavalinTest.test(app, (server, client) -> {
            client.post(NamedRoutes.urlsPath(), input);
            Response response = client.get(NamedRoutes.urlsPath());
            String bodyString = Objects.requireNonNull(response.body()).string();
            assertThat(response.code()).isEqualTo(200);
            assertThat(UrlsRepository.getEntities()).hasSize(1);
            Url url = UrlsRepository.find("https://www.mail.ru")
                    .orElseThrow(() -> new NotFoundResponse("Url = https://www.mail.ru not found"));
            assertThat(bodyString).contains("https://www.mail.ru");
            assertFalse(url.toString().isEmpty());
            assertEquals("https://www.mail.ru", url.getName());
        });
    }

    @Test
    public void testDoubleUrlPage() {
        String input = "url=https://www.mail.ru";

        JavalinTest.test(app, (server, client) -> {
            client.post(NamedRoutes.urlsPath(), input);
            client.post(NamedRoutes.urlsPath(), input);
            assertThat(UrlsRepository.getEntities()).hasSize(1);
        });
    }

    @Test
    public void testInvalidUrl() {
        String input = "url=test.com";
        JavalinTest.test(app, (server, client) -> {
            try (var response = client.post(NamedRoutes.urlsPath(), input)) {
                assertThat(response.code()).isEqualTo(400);
                assertThat(response.body() != null ? response.body().string() : null).contains("Некорректный URL");
            }
        });
    }

    @Test
    public void testUrlNotFound() {
        long id = 9999;
        UrlsRepository.delete(id);
        JavalinTest.test(app, (server, client) -> {
            Response response = client.get(NamedRoutes.urlPath(id));
            assertThat(response.code()).isEqualTo(404);
        });
    }

    @Test
    public void testUrlCheck() {
        Url url = new Url(baseUrl);
        UrlsRepository.save(url);
        JavalinTest.test(app, (server, client) -> {
            try (var response = client.post(NamedRoutes.urlChecksPath(url.getId()))) {
                UrlCheck check = UrlCheckRepository.find(url.getId()).orElseThrow();
                assertThat(response.code()).isEqualTo(200);
                assertThat(check.getUrlId()).isNotNull();
                assertThat(check.getTitle()).isEqualTo("Test Title");
                assertThat(check.getH1()).isEqualTo("Test Page Analyzer");
                assertThat(check.getDescription()).isEqualTo("");
            } catch (final Exception th) {
                System.out.println(th.getMessage());
            }
        });
    }
}
