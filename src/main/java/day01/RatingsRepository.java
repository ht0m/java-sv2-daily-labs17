package day01;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class RatingsRepository {

    private DataSource dataSource;

    private static final int MIN_RATINGS = 1;
    private static final int MAX_RATINGS = 5;

    public RatingsRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insertRating(long movieId, List<Integer> rating) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            insertRatingWithConn(connection, movieId, rating);
            connection.setAutoCommit(true);
            calculateAverage(connection, movieId);
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot connect to ratings", sqle);
        }
    }

    private void calculateAverage(Connection connection, long movieId) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement("select AVG(rating) from ratings where movie_id = ?")) {
            stmt.setLong(1, movieId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
//                    double avg = Double.valueOf(rs.getString(1));
                    double avg = rs.getFloat(1);
                    insertAvgIntoMovie(connection, movieId, avg);
                }
            } catch (SQLException sqle) {
                throw new IllegalStateException("Cannot calculate average");
            }
        }
    }

    private void insertAvgIntoMovie(Connection connection, long movieId, double avg) {
        try (PreparedStatement stmt = connection.prepareStatement("UPDATE movies set average = ? WHERE id =?")) {
            stmt.setDouble(1,avg);
            stmt.setLong(2,movieId);
            stmt.executeUpdate();
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot insert average");
        }
    }

    private void insertRatingWithConn(Connection connection, long movieId, List<Integer> rating) throws
            SQLException {
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

        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot connect to ratings", sqle);
        }
    }
}

