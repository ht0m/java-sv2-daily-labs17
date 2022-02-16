package day01;

import org.flywaydb.core.Flyway;
import org.mariadb.jdbc.MariaDbDataSource;
import java.sql.SQLException;
import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {
        MariaDbDataSource dataSource = new MariaDbDataSource();
        try {
            dataSource.setUrl("jdbc:mariadb://localhost:3306/movies-actors?useUnicode=true");
            dataSource.setUser("root");
            dataSource.setPassword("klaradb");
        } catch (SQLException sqle) {
            throw new IllegalStateException("Can not reach data source", sqle);
        }

        Flyway flyway = Flyway.configure().dataSource(dataSource).load();
        flyway.clean();
        flyway.migrate();

        ActorsRepository actorsRepository = new ActorsRepository(dataSource);
        MovieRepository movieRepository = new MovieRepository(dataSource);
        movieRepository.saveMovie("Titanic", LocalDate.of(1998,01,22));
        movieRepository.saveMovie("Madmax", LocalDate.of(1985,05,12));

        System.out.println(movieRepository.findAllMovies());

//        actorsRepository.saveActor("Jake Doe");
//        System.out.println(actorsRepository.findActorsWithPrefix("Ja"));

    }
}
