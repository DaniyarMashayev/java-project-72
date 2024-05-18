package hexlet.code.contoller;

import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlsRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

public class UrlCheckController {
    public static void createCheck(Context ctx) throws SQLException {
        int urlId = ctx.formParamAsClass("id", Integer.class).get();
        var url = UrlsRepository.find((long) urlId).orElseThrow(() -> new NotFoundResponse("url not found"));

        try {
            HttpResponse<String> response = Unirest.get(url.getName()).asString();
            Document doc = Jsoup.parse(response.getBody());
            int statusCode = response.getStatus();
            String title = doc.title();
            String h1 = doc.select("h1").text();
            String description = doc.select("meta[name=description]").attr("content");
            Timestamp createdAt = new Timestamp(new Date().getTime());
            var urlCheck = new UrlCheck(urlId, statusCode, title, h1, description);
            UrlCheckRepository.save(urlCheck);
            ctx.sessionAttribute("flash", "Страница успешно проверена");
            ctx.sessionAttribute("flashInfo", "success");
            ctx.redirect(NamedRoutes.urlPath(urlId));
        } catch (Exception e) {
            ctx.sessionAttribute("flash", "Некорректный адрес");
            ctx.sessionAttribute("flashInfo", "danger");
            ctx.redirect(NamedRoutes.urlPath(urlId));
        }
    }
}
