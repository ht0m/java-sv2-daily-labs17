package day01;

import java.time.LocalDate;

public class Movie {

    private  Long id;
    private String title;
    private LocalDate release;

    public Movie(Long id, String title, LocalDate release) {
        this.id = id;
        this.title = title;
        this.release = release;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", release=" + release +
                '}';
    }
}
