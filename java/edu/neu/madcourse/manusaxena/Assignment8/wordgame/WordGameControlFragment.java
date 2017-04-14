/***
 * Excerpted from "Hello, Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/eband4 for more book information.
***/
package edu.neu.madcourse.manusaxena.Assignment8.wordgame;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import edu.neu.madcourse.manusaxena.Assignment5.MainActivity;
import edu.neu.madcourse.manusaxena.R;

public class WordGameControlFragment extends Fragment {

    private static DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();
    private static String clientToken= FirebaseInstanceId.getInstance().getToken();
    private AlertDialog mDialog;

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
      View rootView =
            inflater.inflate(R.layout.fragment_control_tpwg, container, false);
      View quit= rootView.findViewById(R.id.button_quit);

       quit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent homeIntent=new Intent(getActivity().getApplicationContext(), MainActivity.class);

               homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
               mDatabase.child("users/"+clientToken+"/active").setValue(false);
               startActivity(homeIntent);
           }
       });




      return rootView;
   }

    @Override
    public void onPause() {
        super.onPause();

        // Get rid of the about dialog if it's still up

    }
}
