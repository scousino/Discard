package edu.pitt.cs1699.discard.Utilities;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.widget.Toast;

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
                    Toast.makeText(getApplicationContext(), "Location, Location, Location", Toast.LENGTH_SHORT).show();
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
