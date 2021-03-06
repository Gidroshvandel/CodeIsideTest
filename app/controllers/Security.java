package controllers;


import models.Book;
import models.Library;
import models.User;
import play.libs.Crypto;

import java.text.SimpleDateFormat;

public class Security extends Secure.Security {



    static boolean authenticate(String username, String password) {
        return User.connect(username, password) != null;
    }

    static boolean check(String profile) {
        if("admin".equals(profile)) {
            return User.find("byEmail", connected()).<User>first().isAdmin;
        }
        return false;
    }

}
