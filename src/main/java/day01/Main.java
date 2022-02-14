package day01;

import org.mariadb.jdbc.MariaDbDataSource;
import java.sql.SQLException;

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

        ActorsRepository actorsRepository = new ActorsRepository(dataSource);
        
        actorsRepository.saveActor("Jane Doe");
        System.out.println(actorsRepository.findActorsWithPrefix("Ja"));

    }
}
