package edu.pitt.cs1699.discard.Utilities;

import android.content.Context;

import edu.pitt.cs1699.discard.Database.Chatroom;
import edu.pitt.cs1699.discard.Database.Message;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by Spencer Cousino on 3/28/2018.
 */

public class Utilities {

    public static List<Chatroom> getPeopleList(Context context) {
        List<Chatroom> rooms = new ArrayList<Chatroom>();
        rooms.add(new Chatroom("Test1", "A Chat Room", "This is a chat room description", 0, 0));
        rooms.add(new Chatroom("Test2", "Another Chat Room", "This is a chat room description", 0, 0));
        rooms.add(new Chatroom("Test3", "A Third Chat Room", "This is a chat room description", 0, 0));
        rooms.add(new Chatroom("Test4", "AnotherOne", "This is a chat room description", 0, 0));

        return rooms;
    }

    public static List<Message> getMessageList(String cr_id){
        List<Message> messages = new ArrayList<>();
        messages.add(new Message("Test1", "A message has appeared", "Today", "11:11:11"));
        messages.add(new Message("Test2", "A 2nd message has appeared", "Yesterday", "6:06:06"));
        messages.add(new Message("Test3", "A 3rd message has appeared", "Today", "11:11:11"));

        return messages;
    }
}
