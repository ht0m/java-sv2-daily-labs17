package day01;

import org.flywaydb.core.Flyway;
import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;

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
//        actorsRepository.saveActor("Jake Doe");
//        System.out.println(actorsRepository.findActorsWithPrefix("Ja"));

        MovieRepository movieRepository = new MovieRepository(dataSource);
        ActorsRepository actorsRepository1 = new ActorsRepository(dataSource);
        ActorsMoviesRepository actorsMoviesRepository = new ActorsMoviesRepository(dataSource);
        ActorMoviesService actorMoviesService = new ActorMoviesService(actorsRepository, movieRepository, actorsMoviesRepository);

        actorMoviesService.insertMovieWithActors("Titanic", LocalDate.of(1998, 11, 16), Arrays.asList("Leonardo DiCaprio", "Kate Winslet"));
        actorMoviesService.insertMovieWithActors("Matrix", LocalDate.of(1999, 6, 8), Arrays.asList("Leonardo DiCaprio", "Marilin Monroe", "Kate Winslet"));


//        movieRepository.saveMovie("Titanic", LocalDate.of(1998, 01, 22));
//        movieRepository.saveMovie("Madmax-2", LocalDate.of(1985, 05, 12));
//        System.out.println(movieRepository.findAllMovies());


    }
}
