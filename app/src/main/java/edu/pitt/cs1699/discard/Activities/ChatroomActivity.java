package edu.pitt.cs1699.discard.Activities;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


import edu.pitt.cs1699.discard.Database.DiscardDatabase;
import edu.pitt.cs1699.discard.Database.Message;
import edu.pitt.cs1699.discard.Database.MessageDao;
import edu.pitt.cs1699.discard.R;
import edu.pitt.cs1699.discard.Utilities.MessageAdapter;
import edu.pitt.cs1699.discard.Utilities.Utilities;
import edu.pitt.cs1699.discard.databinding.ActivityChatroomBinding;

import static edu.pitt.cs1699.discard.Enums.CHATROOM_ID;
import static edu.pitt.cs1699.discard.Enums.CHATROOM_NAME;

public class ChatroomActivity extends AppCompatActivity {

    private DiscardDatabase mDb;
    RecyclerView messageList;

    private ActivityChatroomBinding mBinding;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private String chatroomID = "";

    private List<Message> chatroomMessages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String chatroomName = intent.getStringExtra(CHATROOM_NAME);
        chatroomID = intent.getStringExtra(CHATROOM_ID);
        setTitle(chatroomName);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_chatroom);

        mBinding.recyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mBinding.recyclerView.setLayoutManager(mLayoutManager);

        mDb = DiscardDatabase.getDiscardDatabase(this);
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

    public void keepMessage() {
        //Initiate next group's stuff
    }

    public void discardMessage(String messageID) {
        //Delete local message
        //Initiate next group's stuff
    }
}
