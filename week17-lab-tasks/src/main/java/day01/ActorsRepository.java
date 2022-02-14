package day01;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActorsRepository {
    private DataSource dataSource;

    public ActorsRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void saveActor(String name) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement("INSERT INTO actors (actor_name) VALUES(?)")
        ) {
            stmt.setString(1, name);
            stmt.executeUpdate();
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot update: " + name, sqle);
        }
    }

    public List<String> findActorsWithPrefix(String prefix) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement("SELECT actor_name FROM actors WHERE actor_name LIKE ?;")
        ) {
            stmt.setString(1, prefix + "%");
            return getResultsByStatement(stmt);
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot query: " + prefix, sqle);
        }
    }

    private List<String> getResultsByStatement(PreparedStatement stmt) throws SQLException {
        List<String> result = new ArrayList<>();
        try (ResultSet resultSet = stmt.executeQuery()) {
            while (resultSet.next()) {
                result.add(resultSet.getString(1));
            }
        }
        return result;
    }
}
