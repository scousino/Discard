package edu.pitt.cs1699.discard.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Spencer Cousino on 3/30/2018.
 */

@Dao
public interface MessageDao {
    @Query("SELECT * FROM tblMessages")
    public LiveData<List<Message>> getAllMessages();

    @Query("SELECT * FROM tblMessages WHERE cr_id = :chatroomID ORDER BY msg_id")
    public LiveData<List<Message>> getAllChatroomMessages(String chatroomID);

    @Query("SELECT * FROM tblMessages WHERE msg_id = :msgID")
    public LiveData<Message> getMessageByID(int msgID);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void addMessage(Message msg);

    @Insert
    public void addMultipleMessages(Message... msgs);

    @Delete
    public void deleteMessage(Message msg);

    @Delete
    public void deleteMultipleMessages(Message... msgs);
}
