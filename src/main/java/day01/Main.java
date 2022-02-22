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
        RatingsRepository ratingsRepository = new RatingsRepository(dataSource);
        ActorMoviesService actorMoviesService = new ActorMoviesService(actorsRepository, movieRepository, actorsMoviesRepository);
        MoviesRatingService moviesRatingService = new MoviesRatingService(movieRepository, ratingsRepository);

        actorMoviesService.insertMovieWithActors("Titanic", LocalDate.of(1998, 11, 16), Arrays.asList("Leonardo DiCaprio", "Kate Winslet"));
        actorMoviesService.insertMovieWithActors("Matrix", LocalDate.of(1999, 6, 8), Arrays.asList("Leonardo DiCaprio", "Marilin Monroe", "Kate Winslet"));
        actorMoviesService.insertMovieWithActors("Snatch", LocalDate.of(2000, 3, 21), Arrays.asList("Bradd Pitt", "Jason Statham", "Stephen Graham"));
        actorMoviesService.insertMovieWithActors("Student of the Year 2", LocalDate.of(2019, 1, 2), Arrays.asList("Tara Sutaria", "Tiger Shroff", "Leslie P. Mehlmann"));

        moviesRatingService.addRatings("Student of the Year 2",1);
        moviesRatingService.addRatings("Titanic", 5,4,2,2);
        moviesRatingService.addRatings("Matrix",1,2,3,4,5);
        moviesRatingService.addRatings("Snatch",4,5,4,4,5,4);
        moviesRatingService.addRatings("Student of the Year 2",5);


//        movieRepository.saveMovie("Titanic", LocalDate.of(1998, 01, 22));
//        movieRepository.saveMovie("Madmax-2", LocalDate.of(1985, 05, 12));
//        System.out.println(movieRepository.findAllMovies());


    }
}
