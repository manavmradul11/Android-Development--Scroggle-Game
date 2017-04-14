package edu.neu.madcourse.manusaxena.Assignment8.realtimedatabase;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;

import edu.neu.madcourse.manusaxena.Assignment8.realtimedatabase.models.User;
import edu.neu.madcourse.manusaxena.R;



public class RealtimeDatabaseActivity extends AppCompatActivity
        implements
        ConnectionCallbacks, OnConnectionFailedListener{

    private static final String TAG = RealtimeDatabaseActivity.class.getSimpleName();

    private static DatabaseReference mDatabase;
    public static String clientToken;

    private EditText name;
    private AdapterUser adapter;
    private ArrayList<String> myStringArray= new ArrayList<String>();
    private ArrayList<User> allUsers= new ArrayList<User>();

    protected Location mLastLocation  ;
    protected GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tpwg_realtime_database);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        clientToken= FirebaseInstanceId.getInstance().getToken();

        final ListView listView = (ListView) findViewById(R.id.multiplayer_listView);

        name = (EditText) findViewById(R.id.multiplayer_name);


        final TextView tv = (TextView)findViewById(R.id.tpwg_username);
        final TextView tl = (TextView)findViewById(R.id.tpwg_test_location);
        final LinearLayout ll = (LinearLayout)findViewById(R.id.tpwg_details);

        adapter = new AdapterUser(this, R.layout.simple_list_item_1, allUsers);
        listView.setAdapter(adapter);

        mDatabase.child("users/"+clientToken).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {  User tempuser = dataSnapshot.getValue(User.class);
                            tv.setText(tempuser.username);
                            tv.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.VISIBLE);
                            adapter.setUser(tempuser.username);
                        }
                        else
                        {
                            ll.setVisibility(View.VISIBLE);
                        }

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


        mDatabase.child("users").addChildEventListener(
                new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {User tempuser = dataSnapshot.getValue(User.class);
                        myStringArray.add(tempuser.username);

                        Location nLastLocation= new Location("");
                        nLastLocation.setLatitude(tempuser.xcord);
                        nLastLocation.setLongitude(tempuser.ycord);

                        if(mLastLocation!=null){

                            if(mLastLocation.distanceTo(nLastLocation)<500)
                            {
                                allUsers.add(tempuser);
                                adapter.notifyDataSetChanged();
                            }
                        }


                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        User tempuser = dataSnapshot.getValue(User.class);
                        User removeUser= null;
                        for(User prevuser:allUsers)
                        {
                            if(tempuser.identity.equalsIgnoreCase(prevuser.identity))
                            {
                                removeUser= prevuser;
                            }

                        }

                        if(removeUser!=null)
                        {
                            allUsers.remove(removeUser);
                        }
                        allUsers.add(tempuser);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "onCancelled:" + databaseError);
                    }
                }
        );

        buildGoogleApiClient();

    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        // Provides a simple way of getting a device's location and is well suited for
        // applications that do not require a fine-grained location and that do not need location
        // updates. Gets the best and most recent location currently available, which may be null
        // in rare cases when a location is not available.

        try
        {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                mDatabase.child("users").child(clientToken).child("xcord").setValue(mLastLocation.getLatitude());
                mDatabase.child("users").child(clientToken).child("ycord").setValue(mLastLocation.getLongitude());
                mGoogleApiClient.disconnect();

            } else {
                Toast.makeText(this,"", Toast.LENGTH_LONG).show();
            }
        }
        catch (SecurityException ex)
        {
            System.out.println(ex+" exception for location code");
        }


    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }


    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }



    public void writeNewUser(String userId, String name,boolean active, int score) {
        User user = new User(userId,name,active, score);

        mDatabase.child("users").child(userId).setValue(user);
    }

    public void multiplayerOkButton(View view) {
        writeNewUser(clientToken,name.getText().toString(),false,0);
        adapter.notifyDataSetChanged();
    }

    public void onAddScore(DatabaseReference postRef, String user) {
        postRef
        .child("users")
        .child(user)
        .runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                User u = mutableData.getValue(User.class);
                if (u == null) {
                    return Transaction.success(mutableData);
                }

                u.score = u.score + 5;

                mutableData.setValue(u);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d(TAG, "postTransaction:onComplete:" + databaseError);
            }
        });
    }
}
