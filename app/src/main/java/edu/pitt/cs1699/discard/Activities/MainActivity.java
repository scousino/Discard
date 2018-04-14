package edu.pitt.cs1699.discard.Activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import edu.pitt.cs1699.discard.Database.Chatroom;
import edu.pitt.cs1699.discard.Database.ChatroomDao;
import edu.pitt.cs1699.discard.Database.DiscardDatabase;
import edu.pitt.cs1699.discard.R;
import edu.pitt.cs1699.discard.Utilities.ChatroomAdapter;
import edu.pitt.cs1699.discard.Utilities.Utilities;
import edu.pitt.cs1699.discard.databinding.ActivityMainBinding;

import static edu.pitt.cs1699.discard.Enums._PROXIMITY;


public class MainActivity extends AppCompatActivity {


    private DiscardDatabase mDb;
    RecyclerView roomList;
    private Context mContext;
    private ActivityMainBinding mBinding;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private float latitiude = 0;
    private float longitude = 0;

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

        Intent intent = getIntent();
        if(intent != null){
            if(intent.hasExtra("lat")){
                String strLat = intent.getStringExtra("lat");
                latitiude = Float.valueOf(strLat);
            }
            if(intent.hasExtra("long")){
                String strLong = intent.getStringExtra("long");
                longitude = Float.valueOf(strLong);
            }
        }

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

    public void Group8LocationTrigger(){
        Intent intent = new Intent();
        intent.setAction("edu.pitt.cs1699.team8.StoreArrival");
        intent.putExtra("Longitude","LongitudeDouble");
        intent.putExtra("Latitude","LatitudeDouble");
        sendBroadcast(intent);
    }
}
