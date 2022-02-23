package day01;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class RatingsRepositoryTest {


    ActorsRepository actorsRepository;
    RatingsRepository ratingsRepository;
    MovieRepository movieRepository;
    ActorsMoviesRepository actorsMoviesRepository;
    ActorMoviesService actorMoviesService;
    MoviesRatingService moviesRatingService;

    Flyway flyway;

    @BeforeEach
    void init() {
        MariaDbDataSource dataSource = new MariaDbDataSource();
        try {
            dataSource.setUrl("jdbc:mariadb://localhost:3306/movies-actors-test?useUnicode=true");
            dataSource.setUser("root");
            dataSource.setPassword("klaradb");
        } catch (SQLException sqle) {
            throw new IllegalStateException("Can not reach data source", sqle);
        }

        flyway = Flyway.configure().dataSource(dataSource).load();
        flyway.clean();
        flyway.migrate();

        actorsRepository = new ActorsRepository(dataSource);
        ratingsRepository = new RatingsRepository(dataSource);
        movieRepository = new MovieRepository(dataSource);
        actorsMoviesRepository = new ActorsMoviesRepository(dataSource);
        moviesRatingService = new MoviesRatingService(movieRepository,ratingsRepository);
        actorMoviesService = new ActorMoviesService(actorsRepository,movieRepository,actorsMoviesRepository);
    }

    @Test
    void InsertTest() {

        actorMoviesService.insertMovieWithActors("Titanic", LocalDate.of(1998, 11, 16), Arrays.asList("Leonardo DiCaprio", "Kate Winslet"));
        actorMoviesService.insertMovieWithActors("Matrix", LocalDate.of(1999, 6, 8), Arrays.asList("Leonardo DiCaprio", "Marilin Monroe", "Kate Winslet"));
        actorMoviesService.insertMovieWithActors("Snatch", LocalDate.of(2000, 3, 21), Arrays.asList("Bradd Pitt", "Jason Statham", "Stephen Graham"));
        actorMoviesService.insertMovieWithActors("Student of the Year 2", LocalDate.of(2019, 1, 2), Arrays.asList("Tara Sutaria", "Tiger Shroff", "Leslie P. Mehlmann"));

        moviesRatingService.addRatings("Student of the Year 2",1);
        moviesRatingService.addRatings("Titanic", 5,4,2,2);
        moviesRatingService.addRatings("Matrix",1,2,3,4,5);
        moviesRatingService.addRatings("Snatch",4,5,4,4,5,4);
        moviesRatingService.addRatings("Student of the Year 2",5);


    }

}