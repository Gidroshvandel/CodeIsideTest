package controllers;

import models.Book;
import play.mvc.Controller;

import java.util.List;

public class Routs extends Controller {
    public static void getBooks() {
        List bookList = Book.findAll();
        renderJSON(bookList);
    }
}
