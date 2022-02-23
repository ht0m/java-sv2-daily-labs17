package day01;

import java.util.Arrays;
import java.util.Optional;

public class MoviesRatingService {

    private MovieRepository movieRepository;
    private RatingsRepository ratingsRepository;

    public MoviesRatingService(MovieRepository movieRepository, RatingsRepository ratingsRepository) {
        this.movieRepository = movieRepository;
        this.ratingsRepository = ratingsRepository;
    }

    public void addRatings(String title, Integer...ratings) {
        Optional<Movie> actual = movieRepository.findMovieByTitle(title);
        if (actual.isPresent()) {
            ratingsRepository.insertRating(actual.get().getId(), Arrays.asList(ratings));
            updateAverageByTitle(title);
        } else {
            throw new IllegalArgumentException("Cannot find movie" + title);
        }
    }

    public void updateAverageByTitle(String title) {
        double avg = movieRepository.getMovieAverageRating(title);
        movieRepository.updateMovieAverageByTitle(title,avg);
    }
}
