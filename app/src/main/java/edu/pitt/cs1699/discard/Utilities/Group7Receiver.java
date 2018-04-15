package edu.pitt.cs1699.discard.Utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import edu.pitt.cs1699.discard.Activities.ChatroomActivity;
import edu.pitt.cs1699.discard.Database.Chatroom;
import edu.pitt.cs1699.discard.Database.DiscardDatabase;
import edu.pitt.cs1699.discard.Database.Message;

import static edu.pitt.cs1699.discard.Enums.CHATROOM_ID;
import static edu.pitt.cs1699.discard.Enums.MESSAGE_TRIGGER_ACTION;

public class Group7Receiver extends BroadcastReceiver {
    private Context ctx;

    @Override
    public void onReceive(Context context, Intent intent) {
        ctx = context;
        // For receiving from Group 6
        if (MESSAGE_TRIGGER_ACTION.equals(intent.getAction())) {
            String json_string = intent.getStringExtra("data");

            try {
                // get JSONObject
                JSONObject json_6 = new JSONObject(json_string);

                //Add the new message received to the local database
                new AddNewMessageAsyncTask().execute(new AddMessageParam(json_6, DiscardDatabase.getDiscardDatabase(context)));

            } catch (JSONException j) {
                j.printStackTrace();
            }

        }

        // For sending to Group 8
        String temp = null;
        JSONObject trigger;

        Random rand = new Random();

        try {
            trigger = new JSONObject()
                    .put("Name", "Bread")
                    .put("Price", String.valueOf(rand.nextInt(100)))
                    .put("Quantity", String.valueOf(rand.nextInt(25)));


            Intent intent_8 = new Intent("edu.pitt.cs1699.team8.SINGLE");
            intent_8.putExtra("data", trigger.toString());
            intent_8.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            context.sendBroadcast(intent_8);
        } catch (JSONException j) {
            j.printStackTrace();
        }

    }

    private class AddNewMessageAsyncTask extends AsyncTask<AddMessageParam, Void, String> {

        @Override
        protected String doInBackground(AddMessageParam... addMsgParam) {
            JSONObject json_6;
            DiscardDatabase db;
            String posted_date, posted_time, chat_id = "", message;
            if(addMsgParam.length > 0) {
                json_6 = addMsgParam[0].jsonData;
                db = addMsgParam[0].db;
                try {
                    posted_date = json_6.getJSONObject("Time").getString("Posted Date");
                    posted_time = json_6.getJSONObject("Time").getString("Posted Time");
                    JSONObject details = json_6.getJSONObject("Details");
                    chat_id = details.getString("ChatID");
                    message = details.getString("Message");

                    Message newMessage = new Message(chat_id, message, posted_date, posted_time);
                    //Determine if chat room already exists, if not create a new one
                    if(db.getChatroomDao().getChatroomById(chat_id).getValue() == null) {
                        Chatroom newChatroom = new Chatroom(chat_id, "New Chatroom", "This is a brand new chatroom!",
                                40.444489,-79.953228);
                        db.getChatroomDao().addChatroom(newChatroom);
                    }

                    db.getMessageDao().addMessage(newMessage);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            return chat_id;
        }

        @Override
        protected void onPostExecute(String chat_id) {
            Intent chatroom = new Intent(ctx, ChatroomActivity.class);
            chatroom.putExtra(CHATROOM_ID, chat_id);
            ctx.startActivity(chatroom);
        }
    }

    private class AddMessageParam {
        JSONObject jsonData;
        DiscardDatabase db;

        AddMessageParam(JSONObject data, DiscardDatabase database) {
            this.jsonData = data;
            this.db = database;
        }
    }
}
