package hexlet.code.model;


import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@ToString
public final class Url {
    @Setter
    private long id;

    @ToString.Include
    private String name;

    @Setter
    private Timestamp createdAt;

    public Url(String name) {
        this.name = name;
    }
}
