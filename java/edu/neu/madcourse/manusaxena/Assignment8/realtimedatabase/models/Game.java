package edu.neu.madcourse.manusaxena.Assignment8.realtimedatabase.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Game {


    public   String identity;
    public  String player1;
    public   String player2;
    public  int scorePlayer1;
    public  int scorePlayer2;
    public  String gameState;
    public  String ctPlayer1;
    public  String ctPlayer2;



    public Game(String identity, String player1, String player2,String ctPlayer1, String ctPlayer2, int scorePlayer1, int scorePlayer2, String gameState) {
        this.identity = identity;
        this.player1 = player1;
        this.player2 = player2;
        this.ctPlayer1 = ctPlayer1;
        this.ctPlayer2 = ctPlayer2;
        this.scorePlayer1 = scorePlayer1;
        this.scorePlayer2 = scorePlayer2;
        this.gameState = gameState;
    }

    public Game(){
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
}
