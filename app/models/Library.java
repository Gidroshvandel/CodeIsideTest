package models;

import java.util.*;
import javax.persistence.*;

import com.sun.org.apache.xpath.internal.operations.Bool;
import play.data.validation.Required;
import play.db.jpa.*;

@Entity
public class Library extends Model {

    @Required
    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "bookId", nullable = false)
    private Book book;
    @Required
    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "userId", nullable = false)
    private User user;
    @Required
    private Date startReadDate;
    @Required
    private Date endReadDate;
    @Required
    private Boolean onUse;

    public Library() {

    }

    public Library(Book book, User user, Date startReadDate, Date endReadDate, Boolean onUse) {
        this.book = book;
        this.user = user;
        this.startReadDate = startReadDate;
        this.endReadDate = endReadDate;
        this.onUse = onUse;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getStartReadDate() {
        return startReadDate;
    }

    public void setStartReadDate(Date startReadDate) {
        this.startReadDate = startReadDate;
    }

    public Date getEndReadDate() {
        return endReadDate;
    }

    public void setEndReadDate(Date endReadDate) {
        this.endReadDate = endReadDate;
    }

    public Boolean getOnUse() {
        return onUse;
    }

    public void setOnUse(Boolean onUse) {
        this.onUse = onUse;
    }

//    public static List findByUser(User user) {
//        List userList = Library.find("byUser", user).fetch();
//        return userList;
//    }

    public static List findByUserOnUse(User user, Boolean onUse) {
        List userList = Library.find("SELECT userList FROM models.Library userList WHERE userList.user=:user and userList.onUse=:onUse").bind("user", user).bind("onUse", onUse).fetch();
        return userList;

    }

    public static List findByBook(Book book) {
        List bookList = Library.find("byBook", book).fetch();
        return bookList;
    }
}
