/***
 * Excerpted from "Hello, Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/eband4 for more book information.
***/
package edu.neu.madcourse.manusaxena.Assignment8;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.neu.madcourse.manusaxena.Assignment8.realtimedatabase.RealtimeDatabaseActivity;
import edu.neu.madcourse.manusaxena.R;

public class TPWGMainFragment extends Fragment {

   private AlertDialog mDialog;

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
      View rootView =
            inflater.inflate(R.layout.fragment_tpwg_main, container, false);
      View newMultiplayerButton = rootView.findViewById(R.id.two_p_word_game_new_multiplayer_button);
      View aboutButton = rootView.findViewById(R.id.two_p_word_game_about_button);
      View ackButton = rootView.findViewById(R.id.two_p_word_game_acknowledgement_button);


      newMultiplayerButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            startActivity(new Intent(getActivity(), RealtimeDatabaseActivity.class));
         }
      });

      aboutButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.about_test_code);
            builder.setCancelable(false);
            builder.setPositiveButton(R.string.ok_label,
                  new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialogInterface, int i) {
                        // nothing
                     }
                  });
            mDialog = builder.show();
         }
      });
      ackButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.text_acknowledgements);
            builder.setCancelable(false);
            builder.setPositiveButton(R.string.ok_label,
                    new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i) {
                          // nothing
                       }
                    });
            mDialog = builder.show();
         }
      });
      return rootView;
   }

   @Override
   public void onPause() {
      super.onPause();

      // Get rid of the about dialog if it's still up
      if (mDialog != null)
         mDialog.dismiss();
   }
}
