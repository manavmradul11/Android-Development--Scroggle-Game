package edu.neu.madcourse.manusaxena.Assignment8.fcm;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import edu.neu.madcourse.manusaxena.R;

public class FCMActivity extends AppCompatActivity {

    private static final String TAG = FCMActivity.class.getSimpleName();

    // Please add the server key from your firebase console in the follwoing format "key=<serverKey>"
    private static final String SERVER_KEY = "key=AAAAh7CmJNk:APA91bEoMk2RmUMOzy1V92H7_qyGRuh7zfBLudqQzLlJCBI_CQpFHjsB5tfU22-7CarQoykVURtBhXn90mWWCI_UWk_KXr_eJSpaztwiq_Al_NLer_EwB3LJXx7c4V-sHUy22_TP5R6p";

    // This is the client registration token
    private static final String CLIENT_REGISTRATION_TOKEN = "dBw-ZE7yMKU:APA91bFn5MdQB9VdARVGgSPWHx9AeUHpd1Q3tdZAt9mTac_TfQnRg83SslbBXNUwzUW4GjIeqck2beRCa0fgct3SglnSxpBuq2badCJlK_E3KzcmLr-ykmGOVTVdyi4XoJ_raoAeKEBK";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fcm);


        Button logTokenButton = (Button) findViewById(R.id.logTokenButton);
        logTokenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get token
                String token = FirebaseInstanceId.getInstance().getToken();

                // Log and toast
                String msg = getString(R.string.msg_token_fmt, token);
                Log.d(TAG, msg);
                Toast.makeText(FCMActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void pushNotification(View type) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                pushNote(CLIENT_REGISTRATION_TOKEN, "","");
            }
        }).start();
    }

    public void pushNote(String clientToken, String gameId, String otherPlayer) {
        JSONObject jPayload = new JSONObject();
        JSONObject jNotification = new JSONObject();
        JSONObject jData = new JSONObject();
        try {
            jNotification.put("title", "Scroggle-Two Player");
            jNotification.put("body", "Accept Game Request/ Your Turn");
            jNotification.put("sound", "default");
            jNotification.put("badge", "1");
            jNotification.put("click_action", "OPEN_ACTIVITY_1");
            jData.put("gameId",gameId);
            jData.put("ctotherplayer",otherPlayer);

            // If sending to a single client
            jPayload.put("to", clientToken);
            jPayload.put("data",jData);

            /*
            // If sending to multiple clients (must be more than 1 and less than 1000)
            JSONArray ja = new JSONArray();
            ja.put(CLIENT_REGISTRATION_TOKEN);
            // Add Other client tokens
            ja.put(FirebaseInstanceId.getInstance().getToken());
            jPayload.put("registration_ids", ja);
            */

            jPayload.put("priority", "high");
            jPayload.put("notification", jNotification);

            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", SERVER_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Send FCM message content.
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(jPayload.toString().getBytes());
            outputStream.close();

            // Read FCM response.
            InputStream inputStream = conn.getInputStream();
            final String resp = convertStreamToString(inputStream);

            Handler h = new Handler(Looper.getMainLooper());
            h.post(new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG, "run: " + resp);
                }
            });
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next().replace(",", ",\n") : "";
    }
}
