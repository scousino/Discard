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
