package edu.pitt.cs1699.discard.Utilities;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import edu.pitt.cs1699.discard.Activities.MainActivity;

public class LocationTimeService extends Service {
   static final int MSG_LOCATION = 1;
   static final int MSG_TIME = 2;

    final Messenger mMessenger = new Messenger(new IncomingHandler());


    @Override
    public IBinder onBind(Intent intent){
        return mMessenger.getBinder();
    }

    private class IncomingHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what) {
                case MSG_LOCATION:
                    Bundle bundle = new Bundle();
                    bundle = (Bundle)msg.obj;
                    JSONObject jsonObj = null;
                    JSONObject jsonLocation = null;
                    String lat = "";
                    String lon = "";
                    try {
                        jsonObj = new JSONObject(bundle.getString("location"));
                        jsonLocation = jsonObj.getJSONObject("Location");
                        lat = jsonLocation.getString("Lat");
                        lon = jsonLocation.getString("Long");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("lat",lat);
                    intent.putExtra("long",lon);
                    startActivity(intent);


                    Toast.makeText(getApplicationContext(), "Lat: " + lat + ", Long: " + lon, Toast.LENGTH_SHORT).show();
                    break;
                case MSG_TIME:
                    Bundle timeBundle = new Bundle();
                    timeBundle = (Bundle)msg.obj;
                    JSONObject jsonTimeObj = null;
                    JSONObject jsonTime = null;
                    String date = "";
                    String time = "";

                    try {
                        jsonTimeObj = new JSONObject(timeBundle.getString("time"));
                        jsonTime = jsonTimeObj.getJSONObject("Time");
                        date = jsonTime.getString("Date");
                        time = jsonTime.getString("Time");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Intent intentTime = new Intent(getApplicationContext(), MainActivity.class);
                    intentTime.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intentTime.putExtra("date", date);
                    intentTime.putExtra("time", time);
                    startActivity(intentTime);

                    Toast.makeText(getApplicationContext(), "Time, Time, Time", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }
}
