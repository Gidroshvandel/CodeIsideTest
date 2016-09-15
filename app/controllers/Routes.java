package controllers;

import models.Book;
import play.mvc.Controller;

import java.util.List;

public class Routes extends Controller {
    public static void getBooks() {
        List bookList = Book.findAll();
        renderJSON(bookList);
    }
}
