package controllers;

import models.Book;
import models.Library;
import models.User;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;

import java.util.List;

@With(Secure.class)
public class Admin extends Controller {
    @Before
    static void setConnectedUser() {
        if(Security.isConnected()) {
            try {
            User user = User.find("byEmail", Security.connected()).first();
            renderArgs.put("user", user.getFullname());
        }
        catch (Exception e){
            Security.redirect("/logout");
        }
        }
    }

    public static void usersPage() {
        List userList = User.findAll();

        render(userList);
    }

    public static void userBooksList(String email) {
        User user = User.find("byEmail", email).first();
        List<Library> list = Library.findByUserOnUse(user, true);
        String username = user.getFullname();
        render(list, username);
    }

    public static void whoUseBook(String name, String author) {
        Book book = Book.findByNameAndAutor(name, author);
        List<Library> list = Library.findByBook(book);
        String bookname = name;

        render(list, bookname);
    }

    public static void index() {
        render();
    }
}
