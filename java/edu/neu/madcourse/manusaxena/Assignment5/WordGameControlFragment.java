/***
 * Excerpted from "Hello, Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/eband4 for more book information.
***/
package edu.neu.madcourse.manusaxena.Assignment5;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;

import edu.neu.madcourse.manusaxena.R;

public class WordGameControlFragment extends Fragment {

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
      View rootView =
            inflater.inflate(R.layout.fragment_control_word_game, container, false);
      View quit= rootView.findViewById(R.id.button_quit);
      View main = rootView.findViewById(R.id.button_pause);

       quit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent homeIntent=new Intent(getActivity().getApplicationContext(), MainActivity.class);

               homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

               startActivity(homeIntent);
           }
       });

      main.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            getActivity().finish();
         }
      });

      return rootView;
   }

}
