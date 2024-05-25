package hexlet.code.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class UrlCheck {
    @Setter
    private long id;
    private long urlId;
    private int statusCode;
    private String title;
    private String h1;
    private String description;
    @Setter
    private Timestamp createdAt;

    public UrlCheck(long urlId, int statusCode, String title, String h1, String description) {
        this.urlId = urlId;
        this.statusCode = statusCode;
        this.title = title;
        this.h1 = h1;
        this.description = description;
    }
}
