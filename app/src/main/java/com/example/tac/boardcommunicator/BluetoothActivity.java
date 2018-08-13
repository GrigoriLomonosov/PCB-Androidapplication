package com.example.tac.boardcommunicator;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BluetoothActivity extends AppCompatActivity {

    private String TAG = "test";
    private RecyclerView mRecyclerView;
    private BTRecyclerViewAdapter adapter;

    private BluetoothAdapter myBluetooth = null;
    private Set<BluetoothDevice> pairedDevices;
    public static String EXTRA_ADDRESS = "device_address";

    private List<BlueToothDevice> devices = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO remove
        devices.add(new BlueToothDevice("name1","address1"));
        devices.add( new  BlueToothDevice("name2","address2"));

        super.onCreate(savedInstanceState);

        //TextView text = (TextView) findViewById(R.id.textView);

        myBluetooth = BluetoothAdapter.getDefaultAdapter();
        if ( myBluetooth==null ) {
            //text.setText("adapter niet gevonden");

        } else if ( !myBluetooth.isEnabled() ) {
            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnBTon, 1);
            //text.setText(myBluetooth.toString());
        }

        setContentView(R.layout.activity_bluetooth);

        mRecyclerView = (RecyclerView) findViewById(R.id.BT_devices);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BTRecyclerViewAdapter(BluetoothActivity.this, devices);
        mRecyclerView.setAdapter(adapter);

    }

    public void open(View view) {
        Log.d(TAG, "open: pressed ");
    }

    public void close(View view) {
        Log.d(TAG, "close: pressed");
    }

    public void send(View view) {
        Log.d(TAG, "send: pressed");
    }
}
