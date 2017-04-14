/***
 * Excerpted from "Hello, Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/eband4 for more book information.
***/
package edu.neu.madcourse.manusaxena.Assignment8.wordgame;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import edu.neu.madcourse.manusaxena.Assignment8.fcm.FCMActivity;
import edu.neu.madcourse.manusaxena.Assignment8.realtimedatabase.models.Game;
import edu.neu.madcourse.manusaxena.Assignment8.realtimedatabase.models.User;
import edu.neu.madcourse.manusaxena.R;

public class WordGameActivity extends AppCompatActivity {



   public static final String KEY_RESTORE = "key_restore";
   public static final String PREF_RESTORE = "pref_restore";
   private MediaPlayer mMediaPlayer;
   private Handler mHandler = new Handler();
   private WordGameFragment mGameFragment;
   private static DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();
   private static String clientToken= FirebaseInstanceId.getInstance().getToken();
   private static String gameId=" some gameId in Word Game Activity";
   private int me;
   private static String otherplayer;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

       setContentView(R.layout.activity_tpwg_container);
       mGameFragment = (WordGameFragment) getFragmentManager().findFragmentById(R.id.fragment_tpwg);



       Bundle bundle=null;
       String gameInfo=null;


       bundle = getIntent().getExtras();
       gameInfo = getIntent().getStringExtra(KEY_RESTORE);





       for (String key : bundle.keySet()) {
           if (key.equals("gameId")) {
               Object value = bundle.get(key);
               gameId = ""+value;
           }

           if (key.equals("ctotherplayer")) {
               Object value = bundle.get(key);
               otherplayer = ""+value;
               mGameFragment.setMe(1);
               me=1;
           }
       }


       if(gameId.equals(" some gameId in Word Game Activity"))
       {
           gameId = gameInfo.split(" ")[0];
           otherplayer= gameInfo.split(" ")[1];
           mGameFragment.setMe(0);
           me=0;
       }

       mGameFragment.setGameId(gameId);


       mDatabase.child("game/"+gameId).addValueEventListener(
               new ValueEventListener() {
                   @Override
                   public void onDataChange(DataSnapshot dataSnapshot) {
                       if(dataSnapshot.exists())
                       {  Game game = dataSnapshot.getValue(Game.class);
                           mGameFragment.setPlayer1(game.player1);
                           mGameFragment.setPlayer2(game.player2);
                           mGameFragment.setCtplayer1(game.ctPlayer1);
                           mGameFragment.setCtplayer2(game.ctPlayer2);
                           if(game.gameState!=null)
                           {
                               mGameFragment.putState(game.gameState);
                           }


                       }

                   }
                   @Override
                   public void onCancelled(DatabaseError databaseError) {

                   }
               });

       mDatabase.child("users/"+otherplayer).addValueEventListener(
               new ValueEventListener() {
                   @Override
                   public void onDataChange(DataSnapshot dataSnapshot) {
                       if(dataSnapshot.exists())
                       {  User user = dataSnapshot.getValue(User.class);
                           if(user.active)
                           {
                               mGameFragment.runnable.run();
                               mGameFragment.mDialog.hide();
                               mGameFragment.mHandler.postDelayed(mGameFragment.runnable,3000);
                           }
                           else
                           {
                               final FCMActivity fcm = new FCMActivity();
                               new Thread(new Runnable() {
                                   @Override
                                   public void run() {
                                       fcm.pushNote(otherplayer,gameId,clientToken);
                                   }
                               }).start();
                               mGameFragment.mHandler.removeCallbacks(mGameFragment.runnable);
                               mGameFragment.mDialog.show();

                           }

                       }

                   }
                   @Override
                   public void onCancelled(DatabaseError databaseError) {

                   }
               });


       boolean restore = getIntent().getBooleanExtra(KEY_RESTORE, false);
       if (restore) {
           mDatabase.child("game/"+gameId).addListenerForSingleValueEvent(
                   new ValueEventListener() {
                       @Override
                       public void onDataChange(DataSnapshot dataSnapshot) {
                           if(dataSnapshot.exists())
                           {  Game game = dataSnapshot.getValue(Game.class);
                               mGameFragment.putState(game.gameState);
                           }

                       }
                       @Override
                       public void onCancelled(DatabaseError databaseError) {

                       }
                   });
       }



       Log.d("WGS", "restore = " + restore);

   }

    @Override
    protected void onStart() {
        super.onStart();
        String gameState = mGameFragment.getState();
        mDatabase.child("game/"+gameId+"/gameState").setValue(gameState);
        mDatabase.child("users/"+clientToken+"/active").setValue(true);
    }


   @Override
   protected void onResume() {
      super.onResume();

      mMediaPlayer = MediaPlayer.create(this, R.raw.wg1);
      mMediaPlayer.setLooping(true);
      mMediaPlayer.start();
   }

   @Override
   protected void onPause() {
      super.onPause();
      mHandler.removeCallbacks(mGameFragment.runnable);
      mMediaPlayer.stop();
      mMediaPlayer.reset();
      mMediaPlayer.release();
      String gameData = mGameFragment.getState();
      Log.d("WGS", "state = " + gameData);
   }
}
