package controllers;


import controllers.logic.App;
import models.Book;
import models.Library;
import models.User;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@With(Secure.class)
public class Application extends Controller {

    private static Logger log = Logger.getLogger(Application.class.getName());

    @Before
    static void setConnectedUser() {

        if(Security.isConnected()) {
            try {
                User user = User.find("byEmail", Security.connected()).first();
                renderArgs.put("user", user.getFullname());
            }
            catch (Exception e){
                log.log(Level.SEVERE, "Exception: ", e);
                Security.redirect("/logout");
            }

        }
    }

    public static void index() {
        List<Library> list = Library.findByUserOnUse(getCurrentUser(), true);
        render(list);
    }

    public static void books() {
        List<Book> bookList = Book.findAll();
        render(bookList);
    }

    public static void deleteBook(Long id) {
        try{
            Library lib = Library.findById(id);
            lib.setOnUse(false);
            lib.save();
            String e = "Возврат прошёл успешно";
            render(e);
        }catch (Exception e){
            log.log(Level.SEVERE, "Exception: ", e);
            render(e);
        }
    }

    public static void addBook(String name, String author, String calendarPut, String calendarPush) {

        Book book = Book.findByNameAndAutor(name, author);
        String e = App.validateAddBook(book, getCurrentUser(), calendarPut, calendarPush);
        render(e);

    }

    private static User getCurrentUser(){
        User user = new User();
        if(Security.isConnected()) {
            user = User.find("byEmail", Security.connected()).first();
        }
        return user;
    }
}