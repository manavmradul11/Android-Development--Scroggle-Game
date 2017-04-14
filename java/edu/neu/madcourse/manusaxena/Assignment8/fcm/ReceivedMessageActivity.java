package edu.neu.madcourse.manusaxena.Assignment8.fcm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import edu.neu.madcourse.manusaxena.R;

public class ReceivedMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_message);

        TextView txt = (TextView) findViewById(R.id.testing);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            for (String key : bundle.keySet()) {
                Object value = bundle.get(key);
                txt.append(key + ": " + value + "\n\n");
            }
        }
    }
}
