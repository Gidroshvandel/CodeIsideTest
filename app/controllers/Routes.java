package controllers;

import models.Book;
import models.Library;
import models.User;
import play.mvc.Controller;

import java.util.List;

public class Routes extends Controller {
    public static void getBooks(String bookName, String author) {
        Book book = Book.findByNameAndAutor(bookName, author);
        List<Library> list = Library.findByBook(book);
        renderJSON(list);
    }

    public static void getUsers(String userEmail) {
        User user = User.find("byEmail", userEmail).first();
        List<Library> list = Library.findByUserOnUse(user, true);
        renderJSON(list);
    }
}
