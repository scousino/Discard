package edu.pitt.cs1699.discard.Activities;

import android.arch.lifecycle.Observer;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.IBinder;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


import edu.pitt.cs1699.discard.Database.Chatroom;
import edu.pitt.cs1699.discard.Database.ChatroomDao;
import edu.pitt.cs1699.discard.Database.DiscardDatabase;
import edu.pitt.cs1699.discard.Database.Message;
import edu.pitt.cs1699.discard.Database.MessageDao;
import edu.pitt.cs1699.discard.R;
import edu.pitt.cs1699.discard.Utilities.ChatroomAdapter;
import edu.pitt.cs1699.discard.Utilities.MessageAdapter;
import edu.pitt.cs1699.discard.Utilities.Utilities;
import edu.pitt.cs1699.discard.databinding.ActivityChatroomBinding;

import static edu.pitt.cs1699.discard.Enums.CHATROOM_ID;
import static edu.pitt.cs1699.discard.Enums._PROXIMITY;

public class ChatroomActivity extends AppCompatActivity {

    static private DiscardDatabase mDb;
    static private Context mContext;
    RecyclerView messageList;

    private ActivityChatroomBinding mBinding;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    private Button keepButton;
    private Button discardButton;

    static private String chatroomID = "";
    static private String chatroomName = "";
    private List<Message> chatroomMessages = new ArrayList<>();

    private static Messenger mService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        chatroomID = intent.getStringExtra(CHATROOM_ID);

        mContext = this;

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_chatroom);
        mBinding.recyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mBinding.recyclerView.setLayoutManager(mLayoutManager);

        mDb = DiscardDatabase.getDiscardDatabase(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDb.getChatroomDao().getChatroomById(chatroomID).observe(this, new Observer<Chatroom>() {
            @Override
            public void onChanged(@Nullable Chatroom chatroom) {
                if(chatroom != null) {
                    setTitle(chatroom.name);
                }
            }
        });
    }

    protected void onResume(){
        super.onResume();

        getMessages(chatroomID);
    }

    private void getMessages(String chat_id){
        MessageDao messageDao = mDb.getMessageDao();

        //Get messages from local db for the chatroom
        messageDao.getAllChatroomMessages(chat_id).observe(this, new Observer<List<Message>>() {
            @Override
            public void onChanged(@Nullable List<Message> messages) {
                if (messages != null) {
                    chatroomMessages.addAll(messages);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
        mAdapter = new MessageAdapter(chatroomMessages, ChatroomActivity.this);
        mBinding.recyclerView.setAdapter(mAdapter);

    }

    static public void keepMessage(Context context) {
        //Initiate next group's stuff
        Intent group8 = new Intent("edu.pitt.cs1699.team8.SINGLE");
        Random rand = new Random();

        Bundle dataBundle = new Bundle();

        //Create random data to send to Group 8
        try {
            JSONObject itemData = new JSONObject();
            itemData.put("Name", "6 pack of Coca-Cola");
            itemData.put("Price", rand.nextInt(500));
            itemData.put("Quantity", rand.nextInt(5));

            dataBundle.putString("singleItemData", itemData.toString());
            group8.putExtra("singleItemData", dataBundle);
            //Send intent to Group 8's activity
            mContext.startActivity(group8);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Toast.makeText(context, "Message Kept", Toast.LENGTH_SHORT).show();
    }

    static public void discardMessage(Message message, Context context) {
        //Delete local message
        new deleteMessage().execute(mDb, message);


        //Initiate next group's stuff
        Intent intent = new Intent("edu.pitt.cs1699.team8.ClearService.class");
        intent.setPackage("edu.pitt.cs1699.team8");
        mContext.bindService(intent, clearConnection, Context.BIND_AUTO_CREATE);

    }

    private static class deleteMessage extends AsyncTask<Object, Void, Void> {

        @Override
        protected Void doInBackground(Object... args) {
            DiscardDatabase mDb = (DiscardDatabase) args[0];
            Message message = (Message) args[1];
            MessageDao messageDao = mDb.getMessageDao();
            messageDao.deleteMessage(message);
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {

        }
    }

    private static ServiceConnection clearConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mService = new Messenger(service);
            android.os.Message message = new android.os.Message();
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

    @Override
    public void onBackPressed() {
        Intent main = new Intent(this, MainActivity.class);
        startActivity(main);
    }
}
