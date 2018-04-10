package edu.pitt.cs1699.discard.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import java.util.List;

import edu.pitt.cs1699.discard.Enums;

/**
 * Created by Spencer Cousino on 3/30/2018.
 */

@Dao
public interface ChatroomDao {
    @Query("SELECT * FROM tblChatroom")
    public Chatroom[] getAllChatrooms();

    @Query("SELECT * FROM tblChatroom WHERE cr_id = :chatroomID")
    public LiveData<Chatroom> getChatroomById(String chatroomID);

    @Query("SELECT * FROM tblChatroom WHERE (longitude <= (:longitude + :proximity) AND longitude >= (:longitude - :proximity)) AND (latitude <= (:latitude + :proximity) AND latitude >= (:latitude - :proximity))")
    public List<Chatroom> getChatroomsByLocation(float longitude, float latitude, double proximity);

    @Insert
    public void addChatroom(Chatroom newChatroom);

    @Insert
    public void addMultipleChatrooms(Chatroom... newChatrooms);

    //This method will also delete all the messages associated with it.
    @Delete
    public void closeChatroom(Chatroom chatroomToDelete);
}
