package edu.neu.madcourse.manusaxena.Assignment5;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import edu.neu.madcourse.manusaxena.R;


public class AboutMe extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        Intent intent = getIntent();
        String imeiId = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);


        TextView textView = (TextView) findViewById(R.id.phoneIMEIID);
        textView.setText("IMEI ID: "+imeiId);
    }
}
