package edu.pitt.cs1699.discard;

/**
 * Created by Spencer Cousino on 3/28/2018.
 */

public final class Enums {

    // Enum for proximity of latitude and longitude.
    // i.e. how close a user has to be to a chatroom to see it
    public static final double _PROXIMITY = .01;

    // Enum for message sent through the chatroom adapter's intent for chatroom activity
    public static final String CHATROOM_ID = "chatroom:ID";

    public static final String MESSAGE_TRIGGER_ACTION = "edu.pitt.cs1699.discard.NEW_MESSAGE";
}
