import org.junit.*;
import java.util.*;
import play.test.*;
import models.*;

public class BasicTest extends UnitTest {

    @Test
    public void aVeryImportantThingToTest() {
        assertEquals(2, 1 + 1);
    }

    @Test
    public void testTags() {
        // Create a new user and save it
        User bob = new User("bob@gmail.com", "secret", "Bob").save();

        Book book = new Book("testBook","testAuthor").save();

        // Create a new post

//        Library library = new Library(book, bob, 1.1,2.2);


        // Check
        assertEquals(1, Library.find("testBook"));
    }

}
