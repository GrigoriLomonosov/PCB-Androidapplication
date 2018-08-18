package com.example.tac.boardcommunicator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "test";

    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    // Service necessary to handle bluetooth communication
    private final BluetoothService bluetoothService = BluetoothService.getInstance();

    // Handles data processing in application
    private final DataProcessor dataProcessor = new DataProcessor();

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
        //Intent intent = new Intent(this, Devices.class);
        //startActivity(intent);
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
        //bluetoothService.write(null);
        Log.d(TAG, "updateVolume1: " + dataProcessor.toRead());
        Log.d(TAG, "updateVolume2: " + dataProcessor.findFreeIp());
        try {
            Log.d(TAG, "updateVolume3: " + dataProcessor.createArpMap().size());
        }
        catch (Exception e){
            Log.d(TAG, "updateVolume: fuk");
        }
        
    }

    /**
     * Updates the system time to the current time
     * @param view
     */
    public void updateTime(View view) {
        //TODO hier is iets mis: null pointer Log.d(TAG, "updateTime: " +  bluetoothService.read().length);
    }

    /**
     * Updates the system date to the current date
     * @param view
     */
    public void updateDate(View view) {
        Log.d("test", "updateDate: pressed");
        Intent intent = new Intent(this, TabbedActivity.class);
        startActivity(intent);
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