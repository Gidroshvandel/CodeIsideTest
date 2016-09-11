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
            if(User.findByEmail("admin@mail.ru")==null){
                try {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    new User("admin@mail.ru","root","admin",true).save();
                    User user = new User("user@mail.ru","123456","Иванов Иван Иванович",false).save();
                    User user2 = new User("user2@mail.ru","123456","Простакова Людмила Сергеевна",false).save();
                    Book book = new Book("Война и мир","Лев Николаевич Толстой").save();
                    Book book2 = new Book("Степной Волк","Герман Гёссе").save();
                    Book book3 = new Book("Фауст","Иоганн Вольфганг фон Гёте").save();
                    new Book("Процесс","Франц Кафка").save();
                    new Book("Цифровой","Сергей и Мария Дяченко").save();
                    new Library(book, user, formatter.parse("2016-09-9"),formatter.parse("2016-09-11"),false).save();
                    new Library(book2, user, formatter.parse("2016-09-1"),formatter.parse("2016-09-9"),false).save();
                    new Library(book3, user, formatter.parse("2016-09-11"),formatter.parse("2016-09-16"),true).save();
                    new Library(book, user2, formatter.parse("2016-09-2"),formatter.parse("2016-09-11"),false).save();
                    new Library(book2, user2, formatter.parse("2016-09-5"),formatter.parse("2016-09-15"),true).save();
                    new Library(book3, user2, formatter.parse("2016-09-11"),formatter.parse("2016-09-22"),true).save();
                }
                catch (Exception e){
                    System.out.println(e);
                }

            }
            User user = User.find("byEmail", Security.connected()).first();
            renderArgs.put("user", user.getFullname());
        }
    }

    public static void index() {
        User user = new User();
        if(Security.isConnected()) {
            user = User.find("byEmail", Security.connected()).first();
        }
        List<Library> list = Library.findByUserOnUse(user, true);
//        for (Library lib: list
//             ) {
//            if(lib.getOnUse() == true){
//            }
//        }
        render(list);
    }

    public static void books() {
        List bookList = Book.findAll();
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

                List<Library> list = Library.findByBook(book);
                if(list != null) {

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
                }
                else{
                    new Library(book, user, datePut, datePush, true).save();
                    String e = "Добавление прошло успешно";
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