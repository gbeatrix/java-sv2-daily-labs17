package day01;

import org.mariadb.jdbc.MariaDbDataSource;

import java.io.IOException;
import java.sql.SQLException;
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

        ActorsRepository actorsRepository = new ActorsRepository(dataSource);
        // actorsRepository.saveActor("Jack Doe");
        actorsRepository.findActorsWithPrefix("Jo").forEach(System.out::println);
    }
}
