package edu.pitt.cs1699.discard.Activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    Messenger mService = null;

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
                Intent intent = new Intent("edu.pitt.cs1699.team8.MULTI");
                String triggerData = "";
                try {
                    JSONObject item1 = new JSONObject()
                            .put("Name", "Chicken Wings")
                            .put("Price", "500")
                            .put("Quantity", "12");

                    JSONObject item2 = new JSONObject()
                            .put("Name", "Hot Pepper Sauce")
                            .put("Price", "300")
                            .put("Quantity", "10");

                    JSONObject item3 = new JSONObject()
                            .put("Name", "Butter")
                            .put("Price", "200")
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


        // Group 8 Location Trigger
        Intent intent = new Intent("edu.pitt.cs1699.team8.StoreArrival");
        intent.putExtra("Longitude", 2.123);
        intent.putExtra("Latitude", 1.234);
        mContext.sendBroadcast(intent);

        // Reset Chatrooms to not display deleted one
        Intent intent2 = new Intent(mContext, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        mContext.startActivity(intent2);
    }


        private void getRooms(float latitude, float longitude){
            new getRooms().execute(mDb, latitude, longitude);
        }

        private class getRooms extends AsyncTask<Object, Void, List<Chatroom>> {

            @Override
            protected List<Chatroom> doInBackground(Object... args) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                DateFormat tf = new SimpleDateFormat("HH:mm:ss", Locale.US);
                DiscardDatabase mDb = (DiscardDatabase) args[0];
                float lat = (float) args[1];
                float lon = (float) args[2];
                Date sDate = (Date) args[3];
                Date eDate = (Date) args[4];
                Date sTime = (Date) args[5];
                Date eTime = (Date) args[6];
                ChatroomDao chatDao = mDb.getChatroomDao();

                // get chatrooms by location
                List<Chatroom> nearbyRooms = chatDao.getChatroomsByLocation(lat, lon, _PROXIMITY);

                // Go through each element in nearbyRooms, Convert date and time, and check the
                // date and time against the time we need
                List<Chatroom> nearbyTimeRooms = null;
                for (int i = 0; i < nearbyRooms.size(); i++) {
                    try {
                        if (df.parse( nearbyRooms.get(i).startDate).compareTo(sDate) >= 0) {
                            if (df.parse( nearbyRooms.get(i).endDate).compareTo(eDate) <= 0) {
                                if (tf.parse( nearbyRooms.get(i).startTime).compareTo(sTime) >= 0 ) {
                                    if (tf.parse( nearbyRooms.get(i).endTime).compareTo(eTime) <= 0) {
                                        // add this to the new list
                                        nearbyTimeRooms.add(nearbyRooms.get(i));
                                    } else { continue; }
                                } else { continue; }
                            } else { continue; }
                        } else { continue; }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                //List<Chatroom> nearbyRooms = chatDao.byLocationAndTime(lat, lon, _PROXIMITY, standStartDate,standStartTime,standEndDate,standEndTime);

                //return nearbyRooms;
                return nearbyTimeRooms;
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    // Group 8's Clear List Trigger
    // send the value 56 via an IPC message to clear a list
    public void Group8ClearTrigger(View v) throws RemoteException {
        Intent intent = new Intent("edu.pitt.cs1699.team8.ClearService.class");
        intent.setPackage("edu.pitt.cs1699.team8");
        this.bindService(intent, clearConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection clearConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mService = new Messenger(service);
            Message message = new Message();
            message.what = 56;      // need Group 8's response
            String triggerData = "";

            try {
                triggerData = new JSONObject()
                        .put("Value", new JSONObject().put("value", "56")).toString();
                        // need Group 8's response
            } catch (JSONException j) {
                j.printStackTrace();
            }

            Bundle bundle = new Bundle();
            bundle.putString("Value", triggerData);

            message.obj = bundle;
            try {
                mService.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mService = null;
        }

        public void onServiceDisconnected(ComponentName className) {
            mService = null;
        }
    };
}
