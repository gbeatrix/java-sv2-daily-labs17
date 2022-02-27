package day01;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mariadb.jdbc.MariaDbDataSource;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class ActorsRepositoryTest {
    ActorsRepository actorsRepository;
    Flyway flyway;

    @BeforeEach
    void init(){
        Properties appdata = new Properties();
        try {
            appdata.load(Main.class.getResourceAsStream("/application.properties"));
        } catch (IOException e) {
            throw new IllegalStateException("Cannot load properties.", e);
        }

        MariaDbDataSource dataSource = new MariaDbDataSource();
        try {
            dataSource.setUrl("jdbc:mariadb://localhost:3306/movies-actors-test?useUnicode=true");
            dataSource.setUser(appdata.getProperty("username"));
            dataSource.setPassword(appdata.getProperty("password"));
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot reach DataBase!",sqle);
        }

        flyway = Flyway.configure().dataSource(dataSource).load();
        flyway.clean();
        flyway.migrate();

        actorsRepository = new ActorsRepository(dataSource);
    }

    @Test
    void testInsert(){
        actorsRepository.saveActor("Jane Doe");
        actorsRepository.saveActor("Joe Doe");
        actorsRepository.saveActor("Jack Doe");
    }

    @Test
    void testFindByPrefix(){
        actorsRepository.saveActor("Jane Doe");
        actorsRepository.saveActor("Joe Doe");
        actorsRepository.saveActor("Jack Doe");
        assertEquals(List.of("Joe Doe"), actorsRepository.findActorsWithPrefix("Jo"));
    }
}