package com.example.socialley;

import java.net.InterfaceAddress;
import java.util.HashMap;
import java.util.Hashtable;


public class User {
    String username;
    String email;
    String password;
    String userId;
    String profilePic;
    String lastMessage,status;

    public User(){

    }


    public User(String username, String email, String password, String userId, String profilePic) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.userId = userId;
        this.profilePic = profilePic;
    }

    public User(String username, String email, String password, String userId, String profilePic, String lastMessage) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.userId = userId;
        this.profilePic = profilePic;
        this.lastMessage = lastMessage;
    }

    public User(String username, String email, String password, String userId, String profilePic, String lastMessage, String status) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.userId = userId;
        this.profilePic = profilePic;
        this.lastMessage = lastMessage;
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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




    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.profilePic = "";
    }
}
