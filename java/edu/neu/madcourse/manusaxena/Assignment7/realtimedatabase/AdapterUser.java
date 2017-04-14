package edu.neu.madcourse.manusaxena.Assignment7.realtimedatabase;

/**
 * Created by manusaxena on 3/4/17.
 *
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.ArrayList;

import edu.neu.madcourse.manusaxena.Assignment7.fcm.FCMActivity;
import edu.neu.madcourse.manusaxena.Assignment7.realtimedatabase.models.User;
import edu.neu.madcourse.manusaxena.R;


public class AdapterUser extends ArrayAdapter<User> {
    private Activity activity;
    private ArrayList<User> lUser;
    private static LayoutInflater inflater = null;

    public AdapterUser (Activity activity, int textViewResourceId,ArrayList<User> _lUser) {
        super(activity, textViewResourceId, _lUser);
        try {
            this.activity = activity;
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
                    final FCMActivity fcm = new FCMActivity();
                    // Get token
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            fcm.pushNote(lUser.get(position).identity);
                        }
                    }).start();

                }
            });


        } catch (Exception e) {


        }
        return vi;
    }
}
