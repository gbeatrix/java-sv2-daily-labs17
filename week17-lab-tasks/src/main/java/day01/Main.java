package day01;

import org.flywaydb.core.Flyway;
import org.mariadb.jdbc.MariaDbDataSource;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        MariaDbDataSource dataSource = new MariaDbDataSource();
        Properties appdata = new Properties();
        try {
            appdata.load(Main.class.getResourceAsStream("/application.properties"));
        } catch (IOException e) {
            throw new IllegalStateException("Cannot load properties.", e);
        }

        try {
            dataSource.setUrl("jdbc:mariadb://localhost:3306/movies-actors?useUnicode=true");
            dataSource.setUser(appdata.getProperty("username"));
            dataSource.setPassword(appdata.getProperty("password"));
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot reach DataBase", sqle);
        }

        Flyway flyway = Flyway.configure().dataSource(dataSource).load();
        flyway.migrate();

        ActorsRepository actorsRepository = new ActorsRepository(dataSource);
        actorsRepository.saveActor("Jane Doe");
        actorsRepository.saveActor("Joe Doe");
        actorsRepository.saveActor("Jack Doe");
        actorsRepository.findActorsWithPrefix("Jo").forEach(System.out::println);

        MoviesRepository moviesRepository = new MoviesRepository(dataSource);
        moviesRepository.saveMovie("Titanic",LocalDate.of(1997,12,11));
        moviesRepository.saveMovie("Lord Of The Rings",LocalDate.of(2000,12,23));

        moviesRepository.findAllMovies().forEach(System.out::println);
    }
}
