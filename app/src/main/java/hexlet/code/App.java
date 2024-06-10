//package hexlet.code;
//
//import gg.jte.ContentType;
//import gg.jte.TemplateEngine;
//import gg.jte.resolve.ResourceCodeResolver;
//import hexlet.code.controller.RootController;
//import hexlet.code.controller.UrlCheckController;
//import hexlet.code.controller.UrlsController;
//import hexlet.code.repository.BaseRepository;
//import hexlet.code.util.NamedRoutes;
//import io.javalin.Javalin;
//
//import com.zaxxer.hikari.HikariConfig;
//import com.zaxxer.hikari.HikariDataSource;
//import io.javalin.rendering.template.JavalinJte;
//import lombok.extern.slf4j.Slf4j;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.nio.charset.StandardCharsets;
//import java.sql.SQLException;
//import java.util.Objects;
//import java.util.stream.Collectors;
//
//@Slf4j
//public class App {
//
//    private static int getPort() {
//        String port = System.getenv().getOrDefault("PORT", "7070");
//        return Integer.parseInt(port);
//    }
//
//    private static String getDatabaseUrl() {
//        return System.getenv().getOrDefault("JDBC_DATABASE_URL", "jdbc:h2:mem:project;DB_CLOSE_DELAY=-1;");
//    }
//
//    private static TemplateEngine createTemplateEngine() {
//        ClassLoader classLoader = App.class.getClassLoader();
//        ResourceCodeResolver codeResolver = new ResourceCodeResolver("templates", classLoader);
//        return TemplateEngine.create(codeResolver, ContentType.Html);
//    }
//
//    static String readResourceFile(String fileName) throws IOException {
//        var inputStream = App.class.getClassLoader().getResourceAsStream(fileName);
//        try (BufferedReader reader = new BufferedReader(
//                new InputStreamReader(Objects.requireNonNull(inputStream), StandardCharsets.UTF_8))) {
//            return reader.lines().collect(Collectors.joining("\n"));
//        }
//    }
//
//    public static void main(String[] args) throws IOException, SQLException {
//        Javalin app = getApp();
//        app.start(getPort());
//    }
//
//    public static Javalin getApp() throws IOException, SQLException {
//
//        var hikariConfig = new HikariConfig();
//        hikariConfig.setJdbcUrl(getDatabaseUrl());
//
//        HikariDataSource dataSource = new HikariDataSource(hikariConfig);
//        var sql = readResourceFile("schema.sql");
//
//        log.info(sql);
//        try (var connection = dataSource.getConnection();
//             var statement = connection.createStatement()) {
//            statement.execute(sql);
//        }
//
//        BaseRepository.dataSource = dataSource;
//
//        Javalin app = Javalin.create(config -> {
//            config.bundledPlugins.enableDevLogging();
//            config.fileRenderer(new JavalinJte(createTemplateEngine()));
//        });
//
//        app.get(NamedRoutes.rootPath(), RootController::index);
//        app.get(NamedRoutes.urlsPath(), UrlsController::index);
//        app.post(NamedRoutes.urlsPath(), UrlsController::create);
//        app.get(NamedRoutes.urlPath("{id}"), UrlsController::show);
//        app.post(NamedRoutes.urlChecksPath("{id}"), UrlCheckController::createCheck);
//        app.get(NamedRoutes.urlChecksPath("{id}"), UrlsController::show);
//        app.delete(NamedRoutes.urlPath("{id}"), UrlsController::deleteById);
//
//        app.exception(Exception.class, (endpoint, ctx) -> {
//            ctx.status(404);
//        });
//
//        return app;
//    }
//}

package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.ResourceCodeResolver;
import hexlet.code.controller.RootController;
import hexlet.code.controller.UrlCheckController;
import hexlet.code.controller.UrlsController;
import hexlet.code.repository.BaseRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class App {

    private static int getPort() {
        String port = System.getenv().getOrDefault("PORT", "7070");
        return Integer.parseInt(port);
    }

    public static String readResourceFile(String file) throws IOException {
        try (var inputStream = App.class.getClassLoader().getResourceAsStream(file)) {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(Objects.requireNonNull(inputStream), StandardCharsets.UTF_8))) {
                return reader.lines().collect(Collectors.joining("\n"));
            }
        }
    }

    private static HikariDataSource getDataBase() throws SQLException, IOException  {
        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(System.getenv()
                .getOrDefault("JDBC_DATABASE_URL", "jdbc:h2:mem:project;DB_CLOSE_DELAY=-1;"));

        var dataSource = new HikariDataSource(hikariConfig);

        String sql = readResourceFile("schema.sql");

        log.info(sql);
        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(sql);
        }
        return dataSource;
    }

    private static TemplateEngine createTemplateEngine() {
        ClassLoader classLoader = App.class.getClassLoader();
        ResourceCodeResolver codeResolver = new ResourceCodeResolver("templates", classLoader);
        return TemplateEngine.create(codeResolver, ContentType.Html);
    }

    public static Javalin getApp() throws IOException, SQLException {

        BaseRepository.dataSource = getDataBase();

        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte(createTemplateEngine()));
        });

        app.get(NamedRoutes.rootPath(), RootController::index);
        app.get(NamedRoutes.urlsPath(), UrlsController::index);
        app.post(NamedRoutes.urlsPath(), UrlsController::create);
        app.get(NamedRoutes.urlPath("{id}"), UrlsController::show);
        app.post(NamedRoutes.urlChecksPath("{id}"), UrlCheckController::createCheck);
        app.get(NamedRoutes.urlChecksPath("{id}"), UrlsController::show);
        app.delete(NamedRoutes.urlPath("{id}"), UrlsController::deleteById);

        app.exception(Exception.class, (endpoint, ctx) -> {
            ctx.status(404);
        });

        return app;
    }

    public static void main(String[] args) throws IOException, SQLException {
        var app = getApp();
        app.start(getPort());
    }
}
