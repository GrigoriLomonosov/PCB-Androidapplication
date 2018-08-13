package com.example.tac.boardcommunicator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

/**
 * Responsible for managing a given node
 */
public class NodeActivity extends AppCompatActivity {
    
    private String TAG = "test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_node);
    }
    
    public void updateTime(View view) {
        Log.d(TAG, "updateTime: pressed");
    }
    
    public void updateDate(View view) {
        Log.d(TAG, "updateDate: pressed");
    }
    
    public void findFirstFreeIP (View view) {
        Log.d(TAG, "findFirstFreeIP: pressed");
    }
    
    public void updateIP(View view) {
        Log.d(TAG, "updateIP: pressed");
    }
    
    public void setMinCapVolume(View view) {
        Log.d(TAG, "setMinCapVolume: pressed");
    }
    
    public void setMinTimeForFinish(View view) {
        Log.d(TAG, "setMinTimeForFinish: pressed");
    }
    
    public void setMinTimeForCompare(View view) {
        Log.d(TAG, "setMinTimeForCompare: pressed");
    }

    public void reset(View view) {
        Log.d(TAG, "reset: pressed");
    }
}
