package controllers;


import models.Book;
import models.Library;
import models.User;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@With(Secure.class)
public class Application extends Controller {

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

    public static void index() {
        User user = new User();
        if(Security.isConnected()) {
            user = User.find("byEmail", Security.connected()).first();
        }
        List<Library> list = Library.findByUserOnUse(user, true);
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
//            lib.setEndReadDate(new java.util.Date ());
            lib.save();
            String e = "Возврат прошёл успешно";
            render(e);

        }catch (Exception e){
            render(e);
        }


    }

    public static void addBook(String name, String author, String calendarPut, String calendarPush) {
        try {
            User user = User.find("byEmail", Security.connected()).first();

            Book book = Book.findByNameAndAutor(name, author);

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date datePut = formatter.parse(calendarPut);
            Date datePush = formatter.parse(calendarPush);

            if (datePut.before(datePush)) {

                if(datePut.after(new java.util.Date ()) || datePush.after(new java.util.Date ())) {

                    List<Library> list = Library.findByBook(book);
                    if (list != null) {

                        Boolean bool = true;

                        for (Library library : list
                                ) {
                            if (!((datePut.before(library.getStartReadDate()) || datePut.after(library.getEndReadDate())) && (datePush.before(library.getStartReadDate()) || datePush.after(library.getEndReadDate())))) {
                                bool = false;
                            }
                        }
                        if (bool) {
                            new Library(book, user, datePut, datePush, true).save();
                            String e = "Добавление прошло успешно";
                            render(e);
                        } else {
                            String e = "Error. В выбранный период книга занята други пользователем";
                            render(e);
                        }
                    } else {
                        new Library(book, user, datePut, datePush, true).save();
                        String e = "Добавление прошло успешно";
                        render(e);
                    }
                }
                else {
                    String e = "Error. Дата получения или возврата раньше текущей даты";
                    render(e);
                }
            }
            else {
                String e = "Error. Дата возврата раньше даты получения";
                render(e);
            }

        }catch (Exception e){
            render(e);
        }
    }
}