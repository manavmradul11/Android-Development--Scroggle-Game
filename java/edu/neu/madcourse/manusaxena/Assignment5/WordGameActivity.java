/***
 * Excerpted from "Hello, Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/eband4 for more book information.
***/
package edu.neu.madcourse.manusaxena.Assignment5;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import java.util.ArrayList;


import edu.neu.madcourse.manusaxena.R;

public class WordGameActivity extends Activity {



   public static final String KEY_RESTORE = "key_restore";
   public static final String PREF_RESTORE = "pref_restore";
   private MediaPlayer mMediaPlayer;
   private Handler mHandler = new Handler();
   private WordGameFragment mGameFragment;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_word_game);
      mGameFragment = (WordGameFragment) getFragmentManager()
            .findFragmentById(R.id.fragment_word_game);
      boolean restore = getIntent().getBooleanExtra(KEY_RESTORE, false);
      if (restore) {
         String gameData = getPreferences(MODE_PRIVATE)
               .getString(PREF_RESTORE, null);
         if (gameData != null) {
            mGameFragment.putState(gameData);
         }
      }
      Log.d("WGS", "restore = " + restore);



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
      mHandler.removeCallbacks(null);
      mMediaPlayer.stop();
      mMediaPlayer.reset();
      mMediaPlayer.release();
      String gameData = mGameFragment.getState();
      getPreferences(MODE_PRIVATE).edit()
            .putString(PREF_RESTORE, gameData)
            .commit();
      Log.d("WGS", "state = " + gameData);
   }
}
