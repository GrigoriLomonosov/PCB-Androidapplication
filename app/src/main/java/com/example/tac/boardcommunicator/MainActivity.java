package com.example.tac.boardcommunicator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Opens new activity, that allows connection to BT-devices
     * @param view
     */
    public void goToBlueToothActivity(View view) {
        Log.d("test", "BT: pressed");
        Intent intent = new Intent(this, Devices.class);
        startActivity(intent);
    }

    /**
     * Opens new activity, that controls individual nodes.
     * @param view
     */
    public void goToNodeControlPanel(View view) {
        Log.d("test", "controlpanel: pressed");
        Intent intent = new Intent(this, NodeActivity.class);
        startActivity(intent);
    }

    /**
     * Opens new activity, that controls the settings of the application
     * @param view
     */
    public void goToSettings(View view){
        Log.d("test", "settings: pressed");
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    /**
     * Updates the system volume to a given volume
     * @param view
     */
    public void updateVolume(View view) {
        Log.d("test", "update volume: pressed");
    }

    /**
     * Updates the system time to the current time
     * @param view
     */
    public void updateTime(View view) {
        Log.d("test", "updateTime: pressed");
    }

    /**
     * Updates the system date to the current date
     * @param view
     */
    public void updateDate(View view) {
        Log.d("test", "updateDate: pressed");
    }

    /**
     * Updates the range IP of the system to a given IP
     * @param view
     */
    public void updateIP(View view) {
        Log.d("test", "updateIP: pressed");
    }

    /**
     * TODO
     * @param view
     */
    public void reset(View view) {
        Log.d("test", "reset: pressed");
    }

    /**
     * TODO
     * @param view
     */
    public void uploadFile(View view) {
        Log.d("test", "uploadFile: pressed");
    }

}