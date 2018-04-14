package edu.pitt.cs1699.discard.Activities;
/**
 * Created by RJ on 4/14/2018.
 */
import android.app.Application;
import edu.pitt.cs1699.discard.Database.DiscardDatabase;

import edu.pitt.cs1699.discard.Utilities.Utilities;



public class Discard extends Application {
    private static DiscardDatabase mDb;

    // Called when the application is starting, before any other application objects have been created.
    // Overriding this method is totally optional!
    @Override
    public void onCreate() {
        super.onCreate();
        mDb = DiscardDatabase.getDiscardDatabase(this);
        Utilities util = new Utilities();
        util.insertDataBase(mDb);
    }


    // This is called when the overall system is running low on memory,
    // and would like actively running processes to tighten their belts.
    // Overriding this method is totally optional!
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}