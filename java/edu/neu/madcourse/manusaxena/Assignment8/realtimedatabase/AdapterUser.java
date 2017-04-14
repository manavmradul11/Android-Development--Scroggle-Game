package edu.neu.madcourse.manusaxena.Assignment8.realtimedatabase;

/**
 * Created by manusaxena on 3/4/17.
 *
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.UUID;

import edu.neu.madcourse.manusaxena.Assignment8.realtimedatabase.models.Game;
import edu.neu.madcourse.manusaxena.Assignment8.realtimedatabase.models.User;
import edu.neu.madcourse.manusaxena.Assignment8.wordgame.WordGameActivity;
import edu.neu.madcourse.manusaxena.R;


public class AdapterUser extends ArrayAdapter<User> {


    private ArrayList<User> lUser;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    private String user;
    private static LayoutInflater inflater = null;
    private static DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();
    private static String clientToken= FirebaseInstanceId.getInstance().getToken();




    public AdapterUser (Activity activity, int textViewResourceId,ArrayList<User> _lUser) {
        super(activity, textViewResourceId, _lUser);
        try {
            this.lUser = _lUser;
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        } catch (Exception e) {

        }
    }


    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        public Button user;

    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;
        try {
            if (convertView == null) {
                vi = inflater.inflate(R.layout.simple_list_item_1, null);
                holder = new ViewHolder();

                holder.user = (Button) vi.findViewById(R.id.onlineUser);
                vi.setTag(holder);
            } else {
                holder = (ViewHolder) vi.getTag();
            }



            holder.user.setText("Click to Notify "+lUser.get(position).username);

            holder.user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String gameIdentity= UUID.randomUUID().toString();
                    final String player1 = clientToken;
                    final String player2= lUser.get(position).identity;

                    Game game = new Game(gameIdentity,user,lUser.get(position).username,player1,player2,0,0,"");
                    mDatabase.child("game").child(gameIdentity).setValue(game);
                    final Intent intent = new Intent(getContext(), WordGameActivity.class);
                    intent.putExtra(WordGameActivity.KEY_RESTORE,gameIdentity+" "+player2);
                    getContext().startActivity(intent);
                }
            });


        } catch (Exception e) {


        }
        return vi;
    }
}
