package week18.day02;

public class Book {

    private long id;
    private String writer;
    private String title;
    private int price;
    private int pieces;

    public Book(String writer, String title, int price, int pieces) {
        this.writer = writer;
        this.title = title;
        this.price = price;
        this.pieces = pieces;
    }

    public long getId() {
        return id;
    }

    public String getWriter() {
        return writer;
    }

    public String getTitle() {
        return title;
    }

    public int getPrice() {
        return price;
    }

    public int getPieces() {
        return pieces;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", writer='" + writer + '\'' +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", pieces=" + pieces +
                '}';
    }
}
