package day01;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MovieRepository {

    private DataSource dataSource;

    public MovieRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public long saveMovie(String title, LocalDate release) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement("insert into movies (title, release_date,average) values (?,?,?)",
                     Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, title);
            stmt.setDate(2, Date.valueOf(release));
            stmt.setDouble(3, 0.0);
            stmt.executeUpdate();
            return getKeyFromMovieByStatement(stmt);

        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot reach database", sqle);
        }
    }

    public List<Movie> findAllMovies() {
        List<Movie> result = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement("select * from movies");
             ResultSet rs = stmt.executeQuery()) {

            return processResultSet(rs);

        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot query!", sqle);
        }
    }

    public double getMovieAverageRating(String title) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement("select Round(Avg(rating),2) as movie_avg from ratings join movies on movies.id =ratings.movie_id where movies.title =?")
        ) {
            stmt.setString(1, title);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("movie_avg");
                }
                throw new IllegalArgumentException("No movie find with this id");
            }
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot get Avg", sqle);
        }
    }

    public void updateMovieAverageByTitle(String title, double avg) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement("update movies set average =? where title=?")
        ) {
            stmt.setString(2, title);
            stmt.setDouble(1, avg);
            stmt.executeUpdate();
        } catch (SQLException sqlException) {
            throw new IllegalStateException("Cannot query!", sqlException);
        }


    }

    private List<Movie> processResultSet(ResultSet rs) throws SQLException {
        List<Movie> result = new ArrayList<>();
        while (rs.next()) {
            Long id = rs.getLong("id");
            String title = rs.getString("title");
            LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
            result.add(new Movie(id, title, releaseDate));
        }
        return result;
    }

    public Optional<Movie> findMovieByTitle(String title) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM movies WHERE title =?")) {
            stmt.setString(1, title);

            return processSelectStatement(stmt);

        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot query!", sqle);
        }
    }

    private long getKeyFromMovieByStatement(PreparedStatement stmt) {
        try (ResultSet rs = stmt.getGeneratedKeys()) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot query!");
        }
        throw new IllegalStateException("Cannot get key");
    }

    private Optional<Movie> processSelectStatement(PreparedStatement stmt) throws SQLException {
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                long id = rs.getLong("id");
                String title = rs.getString("title");
                LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
                Optional<Movie> result = Optional.of(new Movie(id, title, releaseDate));
                return result;
            }
        }
        return Optional.empty();
    }
}
