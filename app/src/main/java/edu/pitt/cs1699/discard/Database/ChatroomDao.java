package edu.pitt.cs1699.discard.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ChatroomDao {
    @Query("SELECT * FROM tblChatroom")
    public Chatroom[] getAllChatrooms();

    @Query("SELECT * FROM tblChatroom WHERE cr_id = :chatroomID")
    public LiveData<Chatroom> getChatroomById(String chatroomID);

    @Query("SELECT * FROM tblChatroom WHERE (longitude <= (:longitude + :proximity) AND longitude >= (:longitude - :proximity)) AND (latitude <= (:latitude + :proximity) AND latitude >= (:latitude - :proximity))")
    public List<Chatroom> getChatroomsByLocation( float latitude, float longitude,double proximity);

    @Query("SELECT * FROM tblChatroom WHERE (startDate >= (:startDate)) AND (startTime == (:startTime) AND endDate <= (:endDate) AND endTime == (:endTime))")
    public List<Chatroom> getChatroomsByTime( String startDate, String startTime, String endDate, String endTime);

    @Query("SELECT * FROM tblChatroom WHERE (longitude <= (:longitude + :proximity) AND longitude >= (:longitude - :proximity)) AND (latitude <= (:latitude + :proximity)) AND (latitude >= (:latitude - :proximity))  AND startDate >= (:startDate) AND (startTime == (:startTime) AND endDate <= (:endDate) AND endTime == (:endTime))")
    public List<Chatroom> byLocationAndTime(float latitude, float longitude, double proximity, String startDate, String startTime, String endDate, String endTime);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void addChatroom(Chatroom newChatroom);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void addMultipleChatrooms(Chatroom... newChatrooms);

    //This method will also delete all the messages associated with it.
    @Delete
    public void closeChatroom(Chatroom chatroomToDelete);
}
