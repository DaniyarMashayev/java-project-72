package hexlet.code.model;


import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Setter;


@Setter
@AllArgsConstructor
public class Url {
    private Long id;
    private String name;
    private Timestamp createdAt;
}
