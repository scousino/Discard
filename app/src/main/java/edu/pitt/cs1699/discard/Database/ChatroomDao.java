package edu.pitt.cs1699.discard.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

/**
 * Created by Spencer Cousino on 3/30/2018.
 */

@Dao
public interface ChatroomDao {
    @Query("SELECT * FROM tblChatroom")
    public Chatroom[] getAllChatrooms();

    @Insert
    public void addChatroom(Chatroom newChatroom);

    @Insert
    public void addMultipleChatrooms(Chatroom... newChatrooms);

    //This method will also delete all the messages associated with it.
    @Delete
    public void closeChatroom(Chatroom chatroomToDelete);
}
