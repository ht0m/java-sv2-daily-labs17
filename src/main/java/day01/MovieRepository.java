package day01;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MovieRepository {

    private DataSource dataSource;

    public MovieRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void saveMovie(String title, LocalDate release) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement("insert into movies (title, release_date) values (?,?)")) {
            stmt.setString(1, title);
            stmt.setDate(2, Date.valueOf(release));
            stmt.executeQuery();

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

    private List<Movie> processResultSet(ResultSet rs) throws SQLException{
        List<Movie> result = new ArrayList<>();
        while (rs.next()) {
            Long id = rs.getLong("id");
            String title = rs.getString("title");
            LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
            result.add(new Movie(id, title, releaseDate));
        }
        return result;
    }
}
