package controllers.logic;


import models.Book;
import models.Library;
import models.User;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class App {

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
                String e;
                if (datePut.before(datePush)) {

                    if (datePut.after(new java.util.Date()) || datePush.after(new java.util.Date())) {

                        List<Library> list = Library.findByBook(book);
                        if (list != null) {
                            if (isUniqueRequestToPutBook(list, datePut, datePush)) {
                                new Library(book, user, datePut, datePush, true).save();
                                e = "Добавление прошло успешно";
                                return e;
                            } else {
                                e = "Error. В выбранный период книга занята други пользователем";
                                return e;
                            }
                        } else {
                            new Library(book, user, datePut, datePush, true).save();
                            e = "Добавление прошло успешно";
                            return e;
                        }
                    } else {
                        e = "Error. Дата получения или возврата раньше текущей даты";
                        return e;
                    }
                } else {
                    e = "Error. Дата возврата раньше даты получения";
                    return e;
                }
            }catch (Exception e){
                return e.toString();
            }
    }

}
