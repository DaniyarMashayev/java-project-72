package gg.jte.generated.ondemand.layout;
import gg.jte.Content;
import hexlet.code.util.NamedRoutes;
import hexlet.code.dto.BasePage;
public final class JtepageGenerated {
	public static final String JTE_NAME = "layout/page.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,3,3,3,19,19,19,19,19,19,19,19,19,19,25,25,25,25,25,25,25,25,25,26,26,26,26,26,26,26,26,26,32,32,33,33,33,33,34,34,34,37,37,38,38,38,40,40,49,49,51,51,51,3,4,5,5,5,5};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, BasePage page, Content content, Content footer) {
		jteOutput.writeContent("\n<!DOCTYPE html>\n<html lang=\"en\">\n    <head>\n        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n        <title>Анализатор страниц</title>\n        <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65\" crossorigin=\"anonymous\">\n        <script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js\" integrity=\"sha384-kenU1KFdBIe4zVF0s0G1M5b4hcpxyD9F7jL+jjXkk+Q2h455rYXK/7HAuoJl+0I4\" crossorigin=\"anonymous\"></script>\n    </head>\n    <body class=\"d-flex flex-column min-vh-100\">\n        <nav class=\"navbar navbar-expand-lg navbar-dark bg-dark\">\n            <div class=\"container-fluid\">\n                <a class=\"navbar-brand\"");
		var __jte_html_attribute_0 = NamedRoutes.rootPath();
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
			jteOutput.writeContent(" href=\"");
			jteOutput.setContext("a", "href");
			jteOutput.writeUserContent(__jte_html_attribute_0);
			jteOutput.setContext("a", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent(">Анализатор страниц</a>\n                <button class=\"navbar-toggler\" type=\"button\" data-bs-toggle=\"collapse\" data-bs-target=\"#navbarNav\" aria-controls=\"navbarNav\" aria-expanded=\"false\" aria-label=\"Toggle navigation\">\n                    <span class=\"navbar-toggler-icon\"></span>\n                </button>\n                <div class=\"collapse navbar-collapse\" id=\"navbarNav\">\n                    <div class=\"navbar-nav\">\n                        <a class=\"nav-link\"");
		var __jte_html_attribute_1 = NamedRoutes.rootPath();
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_1)) {
			jteOutput.writeContent(" href=\"");
			jteOutput.setContext("a", "href");
			jteOutput.writeUserContent(__jte_html_attribute_1);
			jteOutput.setContext("a", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent(">Главная</a>\n                        <a class=\"nav-link\"");
		var __jte_html_attribute_2 = NamedRoutes.urlsPath();
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_2)) {
			jteOutput.writeContent(" href=\"");
			jteOutput.setContext("a", "href");
			jteOutput.writeUserContent(__jte_html_attribute_2);
			jteOutput.setContext("a", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent(">Сайты</a>\n                    </div>\n                </div>\n            </div>\n        </nav>\n        <main class=\"flex-grow-1\">\n            ");
		if (page != null && page.getFlash() != null) {
			jteOutput.writeContent("\n                <div class=\"rounded-0 m-0 alert alert-dismissible fade show alert-");
			jteOutput.setContext("div", "class");
			jteOutput.writeUserContent(page.getFlashType());
			jteOutput.setContext("div", null);
			jteOutput.writeContent("\" role=\"alert\">\n                    <p class=\"m-0\">");
			jteOutput.setContext("p", null);
			jteOutput.writeUserContent(page.getFlash());
			jteOutput.writeContent("</p>\n                    <button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"alert\" aria-label=\"Close\"></button>\n                </div>\n            ");
		}
		jteOutput.writeContent("\n            ");
		jteOutput.setContext("main", null);
		jteOutput.writeUserContent(content);
		jteOutput.writeContent("\n        </main>\n        ");
		if (footer != null) {
			jteOutput.writeContent("\n            <footer class=\"footer border-top py-3 mt-5 bg-light\">\n                <div class=\"container-xl\">\n                    <div class=\"text-center\">\n                        created by\n                        <a href=\"https://github.com/DaniyarMashayev\" target=\"_blank\">Daniyar Mashayev</a>\n                    </div>\n                </div>\n            </footer>\n        ");
		}
		jteOutput.writeContent("\n    </body>\n</html>");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		BasePage page = (BasePage)params.get("page");
		Content content = (Content)params.get("content");
		Content footer = (Content)params.getOrDefault("footer", null);
		render(jteOutput, jteHtmlInterceptor, page, content, footer);
	}
}
