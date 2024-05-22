package com.example.queueeat;

public class userClass {
    String useremail, password, type, username;

    public userClass(String useremail, String password, String type, String username) {
        this.useremail = useremail;
        this.password = password;
        this.type = type;
        this.username = username;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
