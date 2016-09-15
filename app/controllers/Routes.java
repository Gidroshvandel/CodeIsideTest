package controllers;

import models.Book;
import models.Library;
import models.User;
import play.mvc.Controller;

import java.util.List;

public class Routes extends Controller {
    public static void getBooks(String bookName, String author) {
        Book book = Book.findByNameAndAutor(bookName, author);
        if(book != null) {
            List<Library> list = Library.findByBook(book);
            renderJSON(list);
        }
        else {
            renderJSON("null");
        }
    }

    public static void getUsers(String userEmail) {
        User user = User.find("byEmail", userEmail).first();
        if(user != null) {
            List<Library> list = Library.findByUserOnUse(user, true);
            renderJSON(list);
        }
        else {
            renderJSON("null");
        }
    }
}
