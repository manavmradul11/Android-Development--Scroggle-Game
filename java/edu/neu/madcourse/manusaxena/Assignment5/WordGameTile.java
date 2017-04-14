/***
 * Excerpted from "Hello, Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/eband4 for more book information.
***/
package edu.neu.madcourse.manusaxena.Assignment5;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageButton;
import android.graphics.PorterDuff.Mode;


import edu.neu.madcourse.manusaxena.R;

public class WordGameTile {




   private int charlevel;



   private int colorMode;

    public int getLarge() {
        return large;
    }

    public void setLarge(int large) {
        this.large = large;
    }

    private int large;

    public int getSmall() {
        return small;
    }

    public void setSmall(int small) {
        this.small = small;
    }

    private int small;
   private final WordGameFragment mGame;
   private View mView;
   private WordGameTile mSubTiles[];


    public int getColorMode() {
        return colorMode;
    }

    public void setColorMode(int colorMode) {
        this.colorMode = colorMode;
    }
   public int getCharlevel() {
        return charlevel;
   }

   public void setCharlevel(int charlevel) {
        this.charlevel = charlevel;
   }

   public WordGameTile(WordGameFragment game) {
      this.mGame = game;
   }


   public View getView() {
      return mView;
   }

   public void setView(View view) {
      this.mView = view;
   }

   public WordGameTile[] getSubTiles() {
      return mSubTiles;
   }

   public void setSubTiles(WordGameTile[] subTiles) {
      this.mSubTiles = subTiles;
   }

   public void updateDrawableState() {
      if (mView == null) return;
      int level = getCharlevel();
      if (mView.getBackground() != null) {
         mView.getBackground().setLevel(level);
      }
      if (mView instanceof ImageButton) {
         Drawable drawable = ((ImageButton) mView).getDrawable();
          Mode mMode = Mode.OVERLAY;
          mView.setBackgroundColor(colorMode);
         drawable.setLevel(level);
      }
   }




   public void animate() {
      Animator anim = AnimatorInflater.loadAnimator(mGame.getActivity(),
            R.animator.tictactoe);
      if (getView() != null) {
         anim.setTarget(getView());
         anim.start();
      }
   }
}
