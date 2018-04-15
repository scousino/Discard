package edu.pitt.cs1699.discard.Utilities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import edu.pitt.cs1699.discard.Activities.ChatroomActivity;
import edu.pitt.cs1699.discard.Database.Chatroom;
import edu.pitt.cs1699.discard.Database.DiscardDatabase;
import edu.pitt.cs1699.discard.Database.Message;

import static edu.pitt.cs1699.discard.Enums.CHATROOM_ID;

public class EventAdditionTrigger {
    private Context ctx;

    public void addEvent( Context context, String data) {
        ctx = context;
        JSONObject event;

        try {
            event = new JSONObject(data);

            new AddNewEventAsyncTask().execute(new AddEventParam(event, DiscardDatabase.getDiscardDatabase(context)));

        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    private class AddNewEventAsyncTask extends AsyncTask<AddEventParam, Void, String> {

        @Override
        protected String doInBackground(AddEventParam... addEventParams) {
            JSONObject data;
            DiscardDatabase db;
            String chat_id = "", name, desc, startDate, startTime, endDate, endTime;
            double lat, lon;

            if(addEventParams.length > 0) {
                data = addEventParams[0].jsonData;
                db = addEventParams[0].db;

                try {
                    chat_id = data.getJSONObject("Details").getString("ChatID");
                    name = data.getJSONObject("Details").getString("Name");
                    desc = data.getJSONObject("Details").getString("Description");
                    startDate = data.getJSONObject("Time").getString("Start Date");
                    startTime = data.getJSONObject("Time").getString("Start Time");
                    endDate = data.getJSONObject("Time").getString("End Date");
                    endTime = data.getJSONObject("Time").getString("End Time");
                    lat = Double.parseDouble(data.getJSONObject("Location").getString("Lat"));
                    lon = Double.parseDouble(data.getJSONObject("Location").getString("Long"));

                    Chatroom newChatroom = new Chatroom(chat_id, name, desc, lat, lon,
                            startDate, startTime, endDate, endTime);

                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                    DateFormat tf = new SimpleDateFormat("HH:mm:ss", Locale.US);
                    Date current = new Date();
                    Message newMessage = new Message(chat_id, "Event Created!", df.format(current), tf.format(current));

                    db.getChatroomDao().addChatroom(newChatroom);
                    db.getMessageDao().addMessage(newMessage);
                    Intent chatroom = new Intent(ctx, ChatroomActivity.class);
                    chatroom.putExtra(CHATROOM_ID, chat_id);
                    ctx.startActivity(chatroom);

                } catch(JSONException je) {
                    je.printStackTrace();
                }
            }
            return chat_id;
        }
    }

    private class AddEventParam {
        JSONObject jsonData;
        DiscardDatabase db;

        AddEventParam(JSONObject data, DiscardDatabase database) {
            this.jsonData = data;
            this.db = database;
        }
    }
}
