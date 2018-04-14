package edu.pitt.cs1699.discard.Utilities;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import edu.pitt.cs1699.discard.Database.Chatroom;
import edu.pitt.cs1699.discard.Database.ChatroomDao;
import edu.pitt.cs1699.discard.Database.DiscardDatabase;
import edu.pitt.cs1699.discard.Database.Message;
import edu.pitt.cs1699.discard.Database.MessageDao;

public class Utilities {

    public static List<Chatroom> getRoomList() {
        List<Chatroom> rooms = new ArrayList<Chatroom>();
        rooms.add(new Chatroom("Test1", "A Chat Room", "This is a chat room description", 0, 0));
        rooms.add(new Chatroom("Test2", "Another Chat Room", "This is a chat room description", 0, 0));
        rooms.add(new Chatroom("Test3", "A Third Chat Room", "This is a chat room description", 0, 0));
        rooms.add(new Chatroom("Test4", "AnotherOne", "This is a chat room description", 0, 0));
        rooms.add(new Chatroom("Test5","Location Room 1", "This is location 1", 40.4406,-79.9958));
        rooms.add(new Chatroom("Test6","Location Room 2", "This is location 2", 40.4506,-79.9958));
        rooms.add(new Chatroom("Test7","Location Room 3", "This is location 3", 40.4306,-79.9958));
        rooms.add(new Chatroom("Test8","Location Room 4", "This is location 4", 40.4606,-79.9958));
        rooms.add(new Chatroom("Test9","Location Room 5", "This is location 5", 40.4206,-79.9958));
        rooms.add(new Chatroom("Test10","Location Room 6", "This is location 6", 40.4406,-79.9858));
        rooms.add(new Chatroom("Test11","Location Room 7", "This is location 7", 40.4406,-79.9758));
        rooms.add(new Chatroom("Test12","Location Room 8", "This is location 8", 40.4406,-80.0058));
        rooms.add(new Chatroom("Test13","Location Room 9", "This is location 9", 40.4406,-80.0158));
        rooms.add(new Chatroom("Test14","Location Room 10", "This is location 10", 40.4406,-80.0158, "2018-04-14", "10:35:00", "2018-04-14", "14:35:00"));
        rooms.add(new Chatroom("Test15","Location Room 11", "This is location 11", 40.4406,-80.0158, "2018-04-15", "22:00:00", "2018-04-15", "23:59:59"));

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
