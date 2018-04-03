package edu.pitt.cs1699.discard.Activities;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

        mDb = DiscardDatabase.getDiscardDatabase(this);

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
        ChatroomDao chatDao = mDb.getChatroomDao();

        //TODO
        //List<Chatroom> nearbyRooms = chatDao.getChatroomsByLocation(latitude, longitude, _PROXIMITY);

        //TEST ONLY
        List<Chatroom> nearbyRooms = Utilities.getPeopleList(this);


        mAdapter = new ChatroomAdapter(nearbyRooms, this);
        mBinding.recyclerView.setAdapter(mAdapter);
    }

}
