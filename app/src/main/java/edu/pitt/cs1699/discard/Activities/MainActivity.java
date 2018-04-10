package edu.pitt.cs1699.discard.Activities;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ScrollView;
import android.databinding.DataBindingUtil;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.pitt.cs1699.discard.Database.Chatroom;
import edu.pitt.cs1699.discard.Database.ChatroomDao;
import edu.pitt.cs1699.discard.Database.DiscardDatabase;
import edu.pitt.cs1699.discard.*;

import edu.pitt.cs1699.discard.R;


import static edu.pitt.cs1699.discard.Enums._PROXIMITY;
import edu.pitt.cs1699.discard.Utilities.ChatroomAdapter;
import edu.pitt.cs1699.discard.Utilities.Utilities;
import edu.pitt.cs1699.discard.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {


    private DiscardDatabase mDb;
    RecyclerView roomList;
    private Context mContext;
    private ActivityMainBinding mBinding;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Nearby Chat Rooms");

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mBinding.recyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mBinding.recyclerView.setLayoutManager(mLayoutManager);

        mContext = this;

        mDb = DiscardDatabase.getDiscardDatabase(this);
        Utilities util = new Utilities();
        util.insertDataBase(mDb);

        // For receiving from Group 6
        Intent intent_6 = getIntent();
        if (intent_6.getAction().equals("edu.pitt.cs1699.discard.NEW_MESSAGE")) {
            Bundle extras = intent_6.getExtras();
            String json_string = extras.getString("NEW_MESSAGE");

            String time, posted_date, posted_time, chat_id, message;

            try {
                // get JSONObject
                JSONObject json_6 = new JSONObject(json_string);
                time = json_6.getString("time");
                posted_date = json_6.getString("Posted Date");
                posted_time = json_6.getString("Posted Time");
                JSONObject details = json_6.getJSONObject("Details");
                chat_id = json_6.getString("ChatID");
                message = json_6.getString("Message");
            } catch (JSONException j) {
                j.printStackTrace();
            }
            /*
            Right now I'm not sure what we want to do with the info Group 6
            gave us... I also have a feeling that there needs to be more done
            to intercept their broadcast (right now I have the getIntent() stmt,
            Receiver tag in the manifest, and the Receiver class under Utilties
            */
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Intent does not come from the correct app",
                    Toast.LENGTH_LONG);
            toast.show();
        }

        // For sending to Group 8
        String temp = null;
        JSONObject trigger;

        try {
            trigger = new JSONObject();
            trigger.put("Name", "Bread")
                    .put("Price", "79")
                    .put("Quantity", "1")
                    .toString();


            Intent intent_8 = new Intent("edu.pitt.cs1699.team8.SINGLE");
            intent_8.putExtra("data", trigger.toString());
            intent_8.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            sendBroadcast(intent_8);
        } catch (JSONException j) {
            j.printStackTrace();
        }


    }

    @Override
    protected void onResume(){
        super.onResume();

        //TODO
        //LOCATION TRIGGER
        float latitiude = 0;
        float longitude = 0;

        getRooms(latitiude, longitude);
    }
    private void getRooms(float latitude, float longitude){
        new getRooms().execute(mDb, latitude, longitude);
    }

    private class getRooms extends AsyncTask<Object, Void, List<Chatroom>> {

        @Override
        protected List<Chatroom> doInBackground(Object... args) {
            DiscardDatabase mDb = (DiscardDatabase) args[0];
            float lat = (float) args[1];
            float lon = (float) args[2];
            ChatroomDao chatDao = mDb.getChatroomDao();
            List<Chatroom> nearbyRooms = chatDao.getChatroomsByLocation(lat, lon, _PROXIMITY);
            return nearbyRooms;
        }

        @Override
        protected void onPostExecute(List<Chatroom> rooms) {
            mAdapter = new ChatroomAdapter(rooms, mContext);
            mBinding.recyclerView.setAdapter(mAdapter);
        }
    }
}
