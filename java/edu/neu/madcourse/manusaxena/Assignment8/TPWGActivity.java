/***
 * Excerpted from "Hello, Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/eband4 for more book information.
***/
package edu.neu.madcourse.manusaxena.Assignment8;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import edu.neu.madcourse.manusaxena.Assignment8.realtimedatabase.RealtimeDatabaseActivity;
import edu.neu.madcourse.manusaxena.Assignment8.wordgame.WordGameActivity;
import edu.neu.madcourse.manusaxena.R;


public class TPWGActivity extends Activity {



   public static final String KEY_RESTORE = "key_restore";
   public static final String PREF_RESTORE = "pref_restore";
   private MediaPlayer mMediaPlayer;
   private Handler mHandler = new Handler();

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_tpwg);
   }


   @Override
   protected void onResume() {
      super.onResume();
      mMediaPlayer = MediaPlayer.create(this, R.raw.wg1);
      //mMediaPlayer.setLooping(true);
      //mMediaPlayer.start();
   }

   @Override
   protected void onPause() {
      super.onPause();
      mHandler.removeCallbacks(null);
      //mMediaPlayer.stop();
      //mMediaPlayer.reset();
      //mMediaPlayer.release();
   }

   public void resumeTPWGActivity(View view) {
      Intent intent = new Intent(this, WordGameActivity.class);
      intent.putExtra(WordGameActivity.KEY_RESTORE, true);
      this.startActivity(intent);
   }

   public void openTPWGActivity(View view) {
      startActivity(new Intent(TPWGActivity.this, WordGameActivity.class));
   }

   public void openDBActivity(View view) {
      startActivity(new Intent(TPWGActivity.this, RealtimeDatabaseActivity.class));
   }
}
