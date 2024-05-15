@import hexlet.code.dto.urls.UrlPage
@import java.time.format.DateTimeFormatter
@import hexlet.code.util.NamedRoutes
@param UrlPage page

@template.layout.page(
content = @`
    <section>
        <div class="container-lg mt-5">
            <h1>Сайт: ${page.getUrl().getName()}</h1>

            <table class="table table-bordered table-hover mt-3">
                <tbody>
                <tr>
                    <td>ID</td>
                    <td>${page.getUrl().getId()}</td>
                </tr>
                <tr>
                    <td>Имя</td>
                    <td>${page.getUrl().getName()}</td>
                </tr>
                <tr>
                    <td>Дата создания</td>
                    <td>19/02/2024 16:29</td>
                    <td>${page.getUrl().getCreatedAt().toLocalDateTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))}</td>
                </tr>
                </tbody>
            </table>




            <h2 class="mt-5">Проверки</h2>
            <form method="post" action="/urls/1/checks">
                <button type="submit" class="btn btn-primary">Запустить проверку</button>
            </form>

            <table class="table table-bordered table-hover mt-3">
                <thead>
                <tr><th class="col-1">ID</th>
                    <th class="col-1">Код ответа</th>
                    <th>Заголовок</th>
                    <th>Заголовок первого уровня</th>
                    <th>Описание</th>
                    <th class="col-2">Дата проверки</th>
                </tr></thead>
                <tbody>



                </tbody>
            </table>
        </div>

    </section>
`
)