package edu.neu.madcourse.manusaxena.Assignment7.realtimedatabase.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    public String identity;
    public String username;
    public int score;


    public User(){
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String identity,String username, int score){
        this.identity= identity;
        this.username = username;
        this.score = score;
    }

}
