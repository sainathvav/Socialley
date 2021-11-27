package com.example.socialley;

import java.net.InterfaceAddress;
import java.util.HashMap;
import java.util.Hashtable;


public class User {
    String username;
    String email;
    String password;
    String id;
    String profilePic;
    public User(){

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public void setProfilePic(String profilepic) {
        this.profilePic = profilepic;
    }

    public String getProfilePic() {
        return this.profilePic;
    }


    public void setID(String id) {
        this.id = id;
    }

    public String getID() {
        return this.id;
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.profilePic = "";
    }
}
