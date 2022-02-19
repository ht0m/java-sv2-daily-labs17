package day01;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class RatingsRepository {

    private DataSource dataSource;

    private static final int MIN_RATINGS=1;
    private static final int MAX_RATINGS=5;

    public RatingsRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insertRating(long movieId, List<Integer> rating) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement stmt = connection.prepareStatement("insert into ratings (movie_id, rating)  values(?,?)")) {
                for (Integer actual : rating) {
                    if (actual < MIN_RATINGS || actual > MAX_RATINGS) {
                        throw new IllegalArgumentException("Invalid rating!");
                    }
                    stmt.setLong(1, movieId);
                    stmt.setLong(2, actual);
                    stmt.executeUpdate();
                }
                connection.commit();
            } catch (IllegalArgumentException e) {
                connection.rollback();
            }
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot connect to ratings", sqle);
        }
    }
}

