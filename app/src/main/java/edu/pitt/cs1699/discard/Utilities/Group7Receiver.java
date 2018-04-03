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
import edu.pitt.cs1699.discard.Database.DiscardDatabase;
import edu.pitt.cs1699.discard.Database.Message;

import static edu.pitt.cs1699.discard.Enums.CHATROOM_ID;
import static edu.pitt.cs1699.discard.Enums.CHATROOM_NAME;

public class Group7Receiver extends BroadcastReceiver {
    private Context ctx;

    @Override
    public void onReceive(Context context, Intent intent) {
        ctx = context;
        // For receiving from Group 6
        if (intent.getAction().equals("edu.pitt.cs1699.discard.NEW_MESSAGE")) {
            String json_string = intent.getStringExtra("data");

            try {
                // get JSONObject
                JSONObject json_6 = new JSONObject(json_string);

                //Add the new message received to the local database
                new AddNewMessageAsyncTask().execute(new AddMessageParam(json_6, DiscardDatabase.getDiscardDatabase(context)));

            } catch (JSONException j) {
                j.printStackTrace();
            }
            /*
            Right now I'm not sure what we want to do with the info Group 6
            gave us... I also have a feeling that there needs to be more done
            to intercept their broadcast (right now I have the getIntent() stmt,
            Receiver tag in the manifest, and the Receiver class under Utilties
            */
        } else {
            Toast toast = Toast.makeText(context, "Intent does not come from the correct app",
                    Toast.LENGTH_LONG);
            toast.show();
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
        /*
        I believe this part is correct. Only thing I can think of is to
        move it somewhere else or to put something different into the JSONObject
        */

    }

    private class AddNewMessageAsyncTask extends AsyncTask<AddMessageParam, Void, String> {

        @Override
        protected String doInBackground(AddMessageParam... addMsgParam) {
            JSONObject json_6;
            DiscardDatabase db;
            String time, posted_date, posted_time, chat_id = "", message;
            if(addMsgParam.length > 0) {
                json_6 = addMsgParam[0].jsonData;
                db = addMsgParam[0].db;
                try {
                    time = json_6.getString("time");
                    posted_date = json_6.getString("Posted Date");
                    posted_time = json_6.getString("Posted Time");
                    JSONObject details = json_6.getJSONObject("Details");
                    chat_id = json_6.getString("ChatID");
                    message = json_6.getString("Message");

                    Message newMessage = new Message(chat_id, message, posted_date, posted_time);
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
            chatroom.putExtra(CHATROOM_NAME, "Testing");
            chatroom.putExtra(CHATROOM_ID, chat_id);
            ctx.startActivity(new Intent(ctx, ChatroomActivity.class));
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
