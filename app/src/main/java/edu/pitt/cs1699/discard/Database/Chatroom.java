package edu.pitt.cs1699.discard.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by Spencer Cousino on 3/28/2018.
 */

@Entity(tableName = "tblChatroom")
public class Chatroom {

    public Chatroom() {
        this.cr_id = "";
        this.name = "Chatroom";
    }

    public Chatroom(String cr_id, String name, String description, double lat, double longitude){
        this.cr_id = cr_id;
        this.name = name;
        this.description = description;
        this.latitude = lat;
        this.longitude = longitude;
    }

    public Chatroom(String cr_id, String name, String description, double lat, double longitude,
                    String sDate, String sTime, String eDate, String eTime){
        this.cr_id = cr_id;
        this.name = name;
        this.description = description;
        this.latitude = lat;
        this.longitude = longitude;
        this.startDate = sDate;
        this.startTime = sTime;
        this.endDate = eDate;
        this.endTime = eTime;
    }


    @PrimaryKey
    @NonNull
    public String cr_id;

    @NonNull
    public String name;

    public String description;

    public double latitude;
    public double longitude;

    public String startDate;
    public String startTime;

    public String endDate;
    public String endTime;


}
