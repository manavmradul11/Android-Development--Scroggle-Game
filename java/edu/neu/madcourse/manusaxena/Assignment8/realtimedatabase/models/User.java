package edu.neu.madcourse.manusaxena.Assignment8.realtimedatabase.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    public String identity;
    public String username;
    public boolean active;
    public int score;
    public int xcord;
    public int ycord;


    public User(){
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String identity,String username,boolean active, int score){
        this.identity= identity;
        this.username = username;
        this.active = active;
        this.score = score;
    }

}
