package hexlet.code.controller;

import hexlet.code.dto.BasePage;
import hexlet.code.dto.urls.UrlPage;
import hexlet.code.dto.urls.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlsRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class UrlsController {
    public static void create(Context ctx) throws URISyntaxException {
        var inputUrl = ctx.formParamAsClass("url", String.class).get();
        URI parsedUrl;
        try {
            parsedUrl = new URI(inputUrl);
            if (Objects.equals(parsedUrl.getScheme(), null) || Objects.equals(parsedUrl.getAuthority(), null)) {
                throw new URISyntaxException(parsedUrl.toString(), "Некорректный URL");
            }
        } catch (URISyntaxException e) {
            var page = new BasePage("Некорректный URL", "danger");
            ctx.status(400);
            ctx.render("index.jte", Collections.singletonMap("page", page));
        }
        parsedUrl = new URI(inputUrl);
        var name = parsedUrl.getScheme() + "://" + parsedUrl.getAuthority();
        Url newUrl = new Url(name);

        if (UrlsRepository.find(newUrl.getName()).isPresent()) {
            ctx.sessionAttribute("flash", "Страница уже существует");
            ctx.sessionAttribute("flashType", "warning");
        } else {
            UrlsRepository.save(newUrl);
            ctx.sessionAttribute("flash", "Страница успешно добавлена");
            ctx.sessionAttribute("flashType", "success");
        }
        ctx.redirect(NamedRoutes.urlsPath());
    }

    public static void index(Context ctx) {
        var flash = ctx.consumeSessionAttribute("flash");
        var flashType = ctx.consumeSessionAttribute("flashType");
        var page = new UrlsPage(UrlsRepository.getEntities());
        page.setFlash((String) flash);
        page.setFlashType((String) flashType);
        ctx.render("urls/index.jte", Collections.singletonMap("page", page));
    }

    public static void show(Context ctx) {
        long id = ctx.pathParamAsClass("id", long.class).get();

        var url = UrlsRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("URL not found"));
        List<UrlCheck> checks = UrlCheckRepository.getChecksById(id);
        var page = new UrlPage(url, checks);
        var flash = ctx.consumeSessionAttribute("flash");
        var flashType = ctx.consumeSessionAttribute("flashType");
        page.setFlash((String) flash);
        page.setFlashType((String) flashType);
        ctx.render("urls/show.jte", Collections.singletonMap("page", page));
    }

    public static void deleteById(Context ctx) {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        UrlsRepository.delete(id);
        ctx.redirect(NamedRoutes.urlsPath());
    }
}
