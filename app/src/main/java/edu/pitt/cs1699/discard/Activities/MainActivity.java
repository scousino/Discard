package edu.pitt.cs1699.discard.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import edu.pitt.cs1699.discard.Database.Chatroom;
import edu.pitt.cs1699.discard.Database.ChatroomDao;
import edu.pitt.cs1699.discard.Database.DiscardDatabase;
import edu.pitt.cs1699.discard.R;
import edu.pitt.cs1699.discard.Utilities.ChatroomAdapter;
import edu.pitt.cs1699.discard.Utilities.EventAdditionTrigger;
import edu.pitt.cs1699.discard.databinding.ActivityMainBinding;

import static edu.pitt.cs1699.discard.Enums._PROXIMITY;


public class MainActivity extends AppCompatActivity {


    private static DiscardDatabase mDb;
    RecyclerView roomList;
    private static Context mContext;
    private ActivityMainBinding mBinding;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private float latitiude = 0;
    private float longitude = 0;
    //private String

    private final String standStartDate = "2017-01-01";
    private final String standEndDate   = "2020-12-31";
    private final String standStartTime = "01:00:00";
    private final String standEndTime   = "23:00:00";

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
    }

    @Override
    protected void onResume(){
        super.onResume();

        Intent intent = getIntent();
        if(intent.getAction() != null) {
            if (intent.getAction().equals("edu.pitt.cs1699.discard.EVENT")) {
                String json_string = intent.getStringExtra("data");
                EventAdditionTrigger EAT = new EventAdditionTrigger();
                EAT.addEvent(this, json_string);
            }
        }
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

    static public void keepChatroom(Chatroom room, Context context) {
        /*Send info to group 8
         *Group 8 Trigger - Adding a recipe to the shopping list
         *If chatroom is kept, suggest a recipe related to the event/chatroom
         *Example: Recipe for hot wings if user keeps a chatroom for a football game
         */

        final Context tContext = context;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Relevant Recipe")
                .setMessage("Members of \"" + room.name + "\" recommend Hot Wings for this event! Would you like to add Hot Wings to your shopping list?");

        builder.setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent();
                intent.setAction("edu.pitt.cs1699.team8.MULTI");
                String triggerData = "";
                try {
                    JSONObject item1 = new JSONObject()
                            .put("Name", "Chicken Wings")
                            .put("Price", "5")
                            .put("Quantity", "12");

                    JSONObject item2 = new JSONObject()
                            .put("Name", "Hot Pepper Sauce")
                            .put("Price", "3")
                            .put("Quantity", "10");

                    JSONObject item3 = new JSONObject()
                            .put("Name", "Butter")
                            .put("Price", "2")
                            .put("Quantity", "10");

                    JSONArray items = new JSONArray();
                    items.put(item1);
                    items.put(item2);
                    items.put(item3);

                    JSONObject root = new JSONObject().put("Items", items);

                    triggerData = root.toString();

                } catch (JSONException je) {
                    je.printStackTrace();
                }
                intent.putExtra("multipleItemData", triggerData);
                tContext.startActivity(intent);
            }
        });

        builder.setNegativeButton("No!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    static public void discardChatroom(Chatroom room, Context context) {
        ChatroomDao roomDao = mDb.getChatroomDao();
        new deleteRoom().execute(mDb, room);

        //TODO
        //SOME TRIGGER GOES HERE


        Intent intent = new Intent(mContext, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        mContext.startActivity(intent);
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

            //List<Chatroom> nearbyRooms = chatDao.getChatroomsByLocation(lat, lon, _PROXIMITY);
            List<Chatroom> nearbyRooms = chatDao.byLocationAndTime(lat, lon, _PROXIMITY, standStartDate,standStartTime,standEndDate,standEndTime);
            return nearbyRooms;
        }

        @Override
        protected void onPostExecute(List<Chatroom> rooms) {
            mAdapter = new ChatroomAdapter(rooms, mContext);
            mBinding.recyclerView.setAdapter(mAdapter);
        }
    }

    public void Group8LocationTrigger(){
        Intent intent = new Intent("edu.pitt.cs1699.team8.StoreArrival");
        intent.putExtra("Longitude", 2.123);
        intent.putExtra("Latitude", 1.234);
        sendBroadcast(intent);
    }

    private static class deleteRoom extends AsyncTask<Object, Void, Void> {

        @Override
        protected Void doInBackground(Object... args) {
            DiscardDatabase mDb = (DiscardDatabase) args[0];
            Chatroom room = (Chatroom) args[1];
            ChatroomDao roomDao = mDb.getChatroomDao();
            roomDao.closeChatroom(room);
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {

        }
    }


    private Boolean firstTime = null;
    /**
     * Checks if the user is opening the app for the first time.
     * Note that this method should be placed inside an activity and it can be called multiple times.
     * @return boolean
     */
    private boolean isFirstTime() {
        if (firstTime == null) {
            SharedPreferences mPreferences = this.getSharedPreferences("first_time", Context.MODE_PRIVATE);
            firstTime = mPreferences.getBoolean("firstTime", true);
            if (firstTime) {
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean("firstTime", false);
                editor.commit();
            }
        }
        return firstTime;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
