package hexlet.code.repository;

import hexlet.code.model.UrlCheck;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UrlCheckRepository extends BaseRepository {
    public static void save(UrlCheck urlCheck) {
        String sql = "INSERT INTO url_checks (url_id, status_code, title, h1, description, created_at) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, urlCheck.getUrlId());
            stmt.setInt(2, urlCheck.getStatusCode());
            stmt.setString(3, urlCheck.getTitle());
            stmt.setString(4, urlCheck.getH1());
            stmt.setString(5, urlCheck.getDescription());
            var createdAt = Timestamp.valueOf(LocalDateTime.now());
            stmt.setTimestamp(6, createdAt);

            stmt.executeUpdate();

            var generatedKey = stmt.getGeneratedKeys();

            if (generatedKey.next()) {
                urlCheck.setId(generatedKey.getLong(1));
                urlCheck.setCreatedAt(createdAt);
            } else {
                throw new SQLException("DB have not returned an id after saving an check");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Optional<UrlCheck> find(long id) {
        String sql = "SELECT * FROM url_checks WHERE url_id = ?";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            var resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                var urlId = resultSet.getLong("url_id");
                var statusCode = resultSet.getInt("status_code");
                var title = resultSet.getString("title");
                var h1 = resultSet.getString("h1");
                var description = resultSet.getString("description");
                var createdAt = resultSet.getTimestamp("created_at");

                var urlCheck = new UrlCheck(urlId, statusCode, title, h1, description);
                urlCheck.setId(id);
                urlCheck.setCreatedAt(createdAt);

                return Optional.of(urlCheck);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<UrlCheck> getChecksById(long urlId) {
        String sql = "SELECT * FROm url_checks WHERE url_id = ?";
        try (var conn = dataSource.getConnection();
            var stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, urlId);
            var resultSet = stmt.executeQuery();
            List<UrlCheck> result = new ArrayList<>();

            while (resultSet.next()) {
                var id = resultSet.getLong("id");
                var statusCode = resultSet.getInt("status_code");
                var title = resultSet.getString("title");
                var h1 = resultSet.getString("title");
                var description = resultSet.getString("description");
                var createdAt = resultSet.getTimestamp("created_at");
                var urlCheck = new UrlCheck(urlId, statusCode, title, h1, description);
                urlCheck.setId(id);
                urlCheck.setCreatedAt(createdAt);
                result.add(urlCheck);
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
