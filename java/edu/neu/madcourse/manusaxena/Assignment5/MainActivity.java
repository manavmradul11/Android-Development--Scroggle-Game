package edu.neu.madcourse.manusaxena.Assignment5;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ProgressBar;

import edu.neu.madcourse.manusaxena.Assignment7.Communication;
import edu.neu.madcourse.manusaxena.Assignment8.TwoPlayerWordGame;
import edu.neu.madcourse.manusaxena.Project.manusaxena.TrickiestPart;
import edu.neu.madcourse.manusaxena.R;


public class MainActivity extends  AppCompatActivity {

    public final static String EXTRA_MESSAGE = "edu.neu.madcourse.manusaxena.IMEIID";
    private ProgressBar spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        spinner.setVisibility(View.GONE);
    }

    protected void onResume()
    {
        super.onResume();
        spinner.setVisibility(View.GONE);

    }

    public void ticTacToe(View view) {
        Intent intent = new Intent(this, Game.class);
        //EditText editText = (EditText) findViewById(R.id.edit_message);
        //String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void dictionary(View view) {

        spinner.setVisibility(View.VISIBLE);
        Intent intent = new Intent(this, TestDictionary.class);
        //EditText editText = (EditText) findViewById(R.id.edit_message);
        //String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);

        startActivity(intent);

    }
    public void aboutMe(View view) {
        Intent intent = new Intent(this, AboutMe.class);
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String imeiId= telephonyManager.getDeviceId();
        intent.putExtra(EXTRA_MESSAGE, imeiId);
        startActivity(intent);
    }

    public void wordGame(View view) {

        spinner.setVisibility(View.VISIBLE);
        Intent intent = new Intent(this, WordGame.class);
        startActivity(intent);

    }

    public void communication(View view) {

        spinner.setVisibility(View.VISIBLE);
        Intent intent = new Intent(this,Communication.class);
        startActivity(intent);

    }

    public void twoPlayerWordGame(View view) {

        spinner.setVisibility(View.VISIBLE);
        Intent intent = new Intent(this,TwoPlayerWordGame.class);
        startActivity(intent);


    }

    public void trickiestPart(View view) {

        spinner.setVisibility(View.VISIBLE);
        Intent intent = new Intent(this,TrickiestPart.class);
        startActivity(intent);

    }

    public void quit(View view) {
        finish();
    }
}
