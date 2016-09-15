import models.Book;
import models.Library;
import models.User;
import play.jobs.Job;
import play.jobs.OnApplicationStart;

import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

@OnApplicationStart
public class Bootstrap extends Job {

    private static Logger log = Logger.getLogger(Bootstrap.class.getName());

    public void doJob() {
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
                log.log(Level.SEVERE, "Exception: ", e);
            }
        }
    }

}
