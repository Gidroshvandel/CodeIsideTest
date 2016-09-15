package models;

import java.util.*;
import javax.persistence.*;

import groovy.transform.PackageScope;
import play.data.validation.Email;
import play.data.validation.Password;
import play.data.validation.Required;
import play.data.validation.Unique;
import play.db.jpa.*;
import play.libs.Crypto;

@Entity
public class User extends Model {

    @Email
    @Unique
    @Required
    private String email;
    @Password
    @Required
    private String  password;
    @Required
    private String fullname;
    public boolean isAdmin;

    public User() {
    }

    public User(String email, String password, String fullname) {
        this.email = email;
        this.password = Crypto.passwordHash(email + password);
        this.fullname = fullname;
    }

    public User(String email, String password, String fullname, boolean isAdmin) {
        this.email = email;
        this.password = Crypto.passwordHash(email + password);
        this.fullname = fullname;
        this.isAdmin = isAdmin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public static User findByEmail(String email) {
        User user = User.find("byEmail", email).first();
        return user;
    }

    public static Object connect(String email, String password) {
        User user = User.find("SELECT user FROM models.User user WHERE user.email=:email and user.password=:password").bind("email", email).bind("password", Crypto.passwordHash(email + password)).first();
        return user;

    }
}
