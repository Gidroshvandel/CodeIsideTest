package controllers.logic;


import models.Book;
import models.Library;
import models.User;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class App {

    private static Logger log = Logger.getLogger(App.class.getName());

    private static Boolean isUniqueRequestToPutBook(List<Library> list, Date datePut, Date datePush){
        Boolean uniqueRequestToPutBook = true;
        for (Library library : list
                ) {
            if (!((datePut.before(library.getStartReadDate()) || datePut.after(library.getEndReadDate())) && (datePush.before(library.getStartReadDate()) || datePush.after(library.getEndReadDate())))) {
                uniqueRequestToPutBook = false;
            }
        }
        return  uniqueRequestToPutBook;
    }

    public static String validateAddBook(Book book, User user, String calendarPut, String calendarPush) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date datePut = formatter.parse(calendarPut);
            Date datePush = formatter.parse(calendarPush);

            if (datePut.before(datePush)) {

                if (datePut.after(new java.util.Date()) || datePush.after(new java.util.Date())) {

                    List<Library> list = Library.findByBook(book);
                    if (list != null) {
                        if (isUniqueRequestToPutBook(list, datePut, datePush)) {
                            new Library(book, user, datePut, datePush, true).save();
                            return "Добавление прошло успешно";
                        } else {
                            return "Error. В выбранный период книга занята други пользователем";
                        }
                    } else {
                        new Library(book, user, datePut, datePush, true).save();
                        return "Добавление прошло успешно";
                    }
                } else {
                    return "Error. Дата получения или возврата раньше текущей даты";
                }
            } else {
                return "Error. Дата возврата раньше даты получения";
            }
        }catch (ParseException e){
            log.log(Level.SEVERE, "Exception: ", e);
            return "Неверный формат даты";
        }
    }

}
