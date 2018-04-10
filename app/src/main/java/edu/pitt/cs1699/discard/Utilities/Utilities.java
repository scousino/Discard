package edu.pitt.cs1699.discard.Utilities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import edu.pitt.cs1699.discard.Activities.ChatroomActivity;
import edu.pitt.cs1699.discard.Database.Chatroom;
import edu.pitt.cs1699.discard.Database.ChatroomDao;
import edu.pitt.cs1699.discard.Database.DiscardDatabase;
import edu.pitt.cs1699.discard.Database.Message;
import edu.pitt.cs1699.discard.Database.MessageDao;

import java.util.List;
import java.util.ArrayList;

import static edu.pitt.cs1699.discard.Enums.CHATROOM_ID;
import static edu.pitt.cs1699.discard.Enums.CHATROOM_NAME;


public class Utilities {

    public static List<Chatroom> getRoomList() {
        List<Chatroom> rooms = new ArrayList<Chatroom>();
        rooms.add(new Chatroom("Test1", "A Chat Room", "This is a chat room description", 0, 0));
        rooms.add(new Chatroom("Test2", "Another Chat Room", "This is a chat room description", 0, 0));
        rooms.add(new Chatroom("Test3", "A Third Chat Room", "This is a chat room description", 0, 0));
        rooms.add(new Chatroom("Test4", "AnotherOne", "This is a chat room description", 0, 0));

        return rooms;
    }

    public static List<Message> getMessageList(){
        List<Message> messages = new ArrayList<>();
        messages.add(new Message("Test1", "A message has appeared", "Today", "11:11:11"));
        messages.add(new Message("Test2", "A 2nd message has appeared", "Yesterday", "6:06:06"));
        messages.add(new Message("Test3", "A 3rd message has appeared", "Today", "11:11:11"));

        return messages;
    }

    private static void insertMessages(DiscardDatabase mDb){
        MessageDao messageDao = mDb.getMessageDao();

        List<Message> messages = getMessageList();
        for(int i = 0; i < messages.size(); i++){
            messageDao.addMessage(messages.get(i));
        }
    }
    private static void insertRooms(DiscardDatabase mDb){
        ChatroomDao chatroomDao = mDb.getChatroomDao();

        List<Chatroom> chatrooms = getRoomList();
        for(int i = 0; i < chatrooms.size(); i++){
            chatroomDao.addChatroom(chatrooms.get(i));
        }
    }

    public void insertDataBase(DiscardDatabase mDb){
        new insertMessagesTask().execute(mDb);

    }

    private class insertMessagesTask extends AsyncTask<DiscardDatabase, Void, String> {

        @Override
        protected String doInBackground(DiscardDatabase... mDb) {
            insertRooms(mDb[0]);
            insertMessages(mDb[0]);
            return "";
        }

        @Override
        protected void onPostExecute(String id) {

        }
    }
}
