package day01;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MoviesRepository {
    private DataSource dataSource;

    public MoviesRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void saveMovie(String title, LocalDate releaseDate) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement("INSERT INTO movies VALUE (NULL, ?, ?)")
        ) {
            stmt.setString(1, title);
            stmt.setDate(2, Date.valueOf(releaseDate));
            stmt.executeUpdate();
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot insert: " + title, sqle);
        }
    }

    public List<Movie> findAllMovies() {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement("SELECT * FROM movies");
                ResultSet resultSet = stmt.executeQuery()
        ) {
            List<Movie> result = new ArrayList<>();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String title = resultSet.getString(2);
                LocalDate date = resultSet.getDate(3).toLocalDate();
                result.add(new Movie(id, title, date));
            }
            return result;
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot get movies", sqle);
        }
    }

    public Optional<Movie> findMovieByTitle(String title){
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement("SELECT * FROM movies WHERE title=?")
        ) {
            stmt.setString(1, title);
            try (ResultSet rs = stmt.executeQuery()){
                if(rs.next()) {
                    return Optional.of(new Movie(
                            rs.getLong("id"),
                            rs.getString("title"),
                            rs.getDate("release_date").toLocalDate()
                    ));
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot get movie: " + title, sqle);
        }
    }
}
