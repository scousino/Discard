package edu.pitt.cs1699.discard.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by Spencer Cousino on 3/30/2018.
 */

@Entity(tableName = "tblMessages", foreignKeys = @ForeignKey(entity = Chatroom.class,
                                                            parentColumns = "cr_id",
                                                            childColumns = "cr_id",
                                                            onDelete = CASCADE))
public class Message {

    public Message(String cr_id, String message, String posted_date, String posted_time){
        this.cr_id = cr_id;
        this.message = message;
        this.posted_date = posted_date;
        this.posted_time = posted_time;
    }

    @PrimaryKey(autoGenerate = true)
    public int msg_id;

    //Foreign key that ties a message to a specific Chatroom
    public String cr_id;

    public String message = "";

    public String posted_date;
    public String posted_time;
}
