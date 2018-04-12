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



                    Toast.makeText(getApplicationContext(), "Lat: " + lat + ", Long: " + lon, Toast.LENGTH_SHORT).show();
                    break;
                case MSG_TIME:
                    Toast.makeText(getApplicationContext(), "Time, Time, Time", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }
}
