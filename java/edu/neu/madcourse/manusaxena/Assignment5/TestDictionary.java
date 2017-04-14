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
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import edu.neu.madcourse.manusaxena.R;

public class TestDictionary extends Activity {

    ArrayList<String> myStringArray;
    ArrayAdapter<String> adapter;
    private MediaPlayer mMediaPlayer;


   @Override
   protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_dictionary);
       myStringArray=new ArrayList<String>();
       adapter = new ArrayAdapter<String>(this, R.layout.simple_list_item_1, myStringArray);
       mMediaPlayer = MediaPlayer.create(this, R.raw.sound7);

       int size=432335;
       final long[] wordValues=new long[size];
       try
       {
           String line = null;
           BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getAssets().open("wordlist.txt")));
           char[] letters;



           int counter=0;
           while ((line = bufferedReader.readLine()) != null)
           {
               counter=counter+1;

               letters=line.toLowerCase().toCharArray();

               wordValues[counter]=encode(letters);

           }
           bufferedReader.close();
           Arrays.sort(wordValues);



       }
       catch (Exception ex) {
           // reading error
       }

       TextWatcher watcher= new TextWatcher() {
           public void afterTextChanged(Editable s) {
               if(s.length()>3)
               {
                   long key = encode(s.toString().toLowerCase().toCharArray());

                   if(key==0)
                   {}
                   else  if(Arrays.binarySearch(wordValues,key)>0)
                   {
                       if(!myStringArray.contains(s.toString().toLowerCase()))
                       {
                           mMediaPlayer.start();
                           myStringArray.add(s.toString());
                           adapter.notifyDataSetChanged();


                       }
                   }

               }

           }
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {
               //Do something or nothing.
           }
           public void onTextChanged(CharSequence s, int start, int before, int count) {


           }

       };

       EditText editBox = (EditText) findViewById(R.id.editText);
       editBox.addTextChangedListener(watcher);
   }

    protected void onResume()
    {
        super.onResume();
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

    }

    public void returnToMenu(View view) {
        onBackPressed();
    }

    public void clearText(View view) {
        EditText editBox = (EditText) findViewById(R.id.editText);
        editBox.setText("");
        myStringArray.clear();
        adapter.notifyDataSetChanged();
    }
    public void acknowledgements(View view) {
        Intent intent = new Intent(this, Acknowledgements.class);
        startActivity(intent);
    }

    public long encode(char[] letters)
    {
        long value=0;
        for(char letter:letters)
        {
            switch(letter)
            {
                case 'a': value= value*10+1;break;
                case 'b': value= value*10+2;break;
                case 'c': value= value*10+3;break;
                case 'd': value= value*10+4;break;
                case 'e': value= value*10+5;break;
                case 'f': value= value*10+6;break;
                case 'g': value= value*10+7;break;
                case 'h': value= value*10+8;break;
                case 'i': value= value*10+9;break;
                case 'j': value= value*100+10;break;
                case 'k': value= value*100+11;break;
                case 'l': value= value*100+12;break;
                case 'm': value= value*100+13;break;
                case 'n': value= value*100+14;break;
                case 'o': value= value*100+15;break;
                case 'p': value= value*100+16;break;
                case 'q': value= value*100+17;break;
                case 'r': value= value*100+18;break;
                case 's': value= value*100+19;break;
                case 't': value= value*100+20;break;
                case 'u': value= value*100+21;break;
                case 'v': value= value*100+22;break;
                case 'w': value= value*100+23;break;
                case 'x': value= value*100+24;break;
                case 'y': value= value*100+25;break;
                case 'z': value= value*100+26;break;


            }



        }
        return value;
    }
}
