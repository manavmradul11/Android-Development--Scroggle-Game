package edu.neu.madcourse.manusaxena.Assignment7.realtimedatabase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;

import edu.neu.madcourse.manusaxena.Assignment7.realtimedatabase.models.User;
import edu.neu.madcourse.manusaxena.R;

public class RealtimeDatabaseActivity extends AppCompatActivity {

    private static final String TAG = RealtimeDatabaseActivity.class.getSimpleName();

    private static DatabaseReference mDatabase;
    private static String clientToken;

    private EditText name;
    private AdapterUser adapter;
    private ArrayList<String> myStringArray= new ArrayList<String>();
    private ArrayList<User> allUsers= new ArrayList<User>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realtime_database);
        adapter = new AdapterUser(this, R.layout.simple_list_item_1, allUsers);
        ListView listView = (ListView) findViewById(R.id.multiplayer_listView);
        listView.setAdapter(adapter);
        name = (EditText) findViewById(R.id.multiplayer_name);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        clientToken= FirebaseInstanceId.getInstance().getToken();

        /*mDatabase.child("users").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.getChildren()!=null)
                        {
                            for(DataSnapshot user:dataSnapshot.getChildren())
                            {
                                User tempuser = user.getValue(User.class);
                                myStringArray.add(tempuser.username);
                                allUsers.add(tempuser);
                                adapter.notifyDataSetChanged();

                            }

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });*/


        mDatabase.child("users").addChildEventListener(
                new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        User tempuser = dataSnapshot.getValue(User.class);
                        myStringArray.add(tempuser.username);
                        allUsers.add(tempuser);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        User tempuser = dataSnapshot.getValue(User.class);
                        for(User prevuser:allUsers)
                        {
                            if(tempuser.identity.equalsIgnoreCase(prevuser.identity))
                            {
                                allUsers.remove(prevuser);
                            }

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



    }

    public void writeNewUser(String userId, String name, int score) {
        User user = new User(userId,name, score);

        mDatabase.child("users").child(userId).setValue(user);
    }

    public void multiplayerOkButton(View view) {
        writeNewUser(clientToken,name.getText().toString(),0);
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
