package models;

import java.util.*;
import javax.persistence.*;

import play.data.validation.Required;
import play.db.jpa.*;

@Entity
public class Book extends Model {

    @Required
    private String name;
    @Required
    private String author;

    public Book() {
    }

    public Book(String name, String author) {
        this.name = name;
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public static Book findByNameAndAutor(String name, String author) {
        Book book = Book.find("SELECT book FROM models.Book book WHERE book.name=:name and book.author=:author").bind("name", name).bind("author", author).first();
        return book;
    }
}
