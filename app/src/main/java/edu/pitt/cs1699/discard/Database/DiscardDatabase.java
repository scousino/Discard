package edu.pitt.cs1699.discard.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by Spencer Cousino on 3/30/2018.
 */

@Database(entities = {Chatroom.class, Message.class}, version = 2)
public abstract class DiscardDatabase extends RoomDatabase {
    public abstract ChatroomDao getChatroomDao();
    public abstract MessageDao getMessageDao();

    private static DiscardDatabase INSTANCE;

    public static DiscardDatabase getDiscardDatabase(Context ctx) {
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(ctx, DiscardDatabase.class, "discard-database")
                    .fallbackToDestructiveMigration().build();
        }

        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
