package day01;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ActorMoviesService {

    private ActorsRepository actorsRepository;
    public MovieRepository movieRepository;
    private ActorsMoviesRepository actorsMoviesRepository;

    public ActorMoviesService(ActorsRepository actorsRepository, MovieRepository movieRepository, ActorsMoviesRepository actorsMoviesRepository) {
        this.actorsRepository = actorsRepository;
        this.movieRepository = movieRepository;
        this.actorsMoviesRepository = actorsMoviesRepository;
    }

    public void insertMovieWithActors(String title, LocalDate release, List<String> actorNames) {
        long movieId = movieRepository.saveMovie(title, release);
        for (String actual: actorNames) {
            long actorId;
            Optional<Actor> found = actorsRepository.findActorByName(actual);
            if (found.isPresent()) {
                actorId = found.get().getId();
            } else {
                actorId = actorsRepository.saveActor(actual);
            }
            actorsMoviesRepository.insertActorAndMovieID(actorId, movieId);
        }
    }




}
