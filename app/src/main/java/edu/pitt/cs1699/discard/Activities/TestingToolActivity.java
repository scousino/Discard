package edu.pitt.cs1699.discard.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import edu.pitt.cs1699.discard.R;

public class TestingToolActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing_tool);
    }

    private void messageTrigger(View v) {
        Intent intent = new Intent();
        intent.setAction("edu.pitt.cs1699.discard.NEW_MESSAGE");
        String triggerData = "";
        try {
            triggerData = new JSONObject()
                    .put("Time", new JSONObject()
                            .put("Posted Date", "2018-04-02")
                            .put("Posted Time", "15:45:00"))
                    .put("Details", new JSONObject()
                            .put("ChatID", "ABC")
                            .put("Message", "Remember to drive safe as you're leaving the game!")
                    ).toString();

        } catch (JSONException j) {
            j.printStackTrace();
        }
        intent.putExtra("data", triggerData);
        sendBroadcast(intent);
    }
}
