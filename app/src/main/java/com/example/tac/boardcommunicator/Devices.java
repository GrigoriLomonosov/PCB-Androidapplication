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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Devices extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener{

    // Layout fields
    private String TAG = "test";
    private TextView device_name;
    private TextView device_address;
    private Button connectBtn;

    // Bluetooth fields
    private Set<BluetoothDevice> devices = new HashSet<>();
    private BluetoothDevice[] deviceArr;

    String[] country = { "India", "USA", "China", "Japan", "Other"};

    private BluetoothAdapter myBluetooth = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Make the Bluetooth connection and create the paired devices
        myBluetooth = BluetoothAdapter.getDefaultAdapter();
        if ( myBluetooth==null ) {
            Log.d(TAG, "onCreate: BTAdapter niet gevonden");

        } else if ( !myBluetooth.isEnabled() ) {
            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnBTon, 1);
            Log.d(TAG, "onCreate: BTAdapter gevonden");
        }
        //TODO fill the devices array
        devices = myBluetooth.getBondedDevices();
        deviceArr = setToArr(devices);

        setContentView(R.layout.activity_devices);

        device_name = (TextView) findViewById(R.id.device_name);
        device_address = (TextView) findViewById(R.id.device_address);

        // Spinner with devices
        Spinner s = (Spinner) findViewById(R.id.BTSpinner);
        s.setOnItemSelectedListener(this);
        //Creating the ArrayAdapter instance having the devices list

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,deviceArr);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(aa);

        super.onCreate(savedInstanceState);

    }

    private BluetoothDevice[] setToArr(Set<BluetoothDevice> input){
        BluetoothDevice[] result = new BluetoothDevice[input.size()];
        int i = 0;
        for(BluetoothDevice dev: input){
            result[i] = dev;
            i++;
        }
        return result;
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        //Toast.makeText(getApplicationContext(),country[position] , Toast.LENGTH_LONG).show();
        device_address.setText(deviceArr[position].getAddress());
        device_name.setText(deviceArr[position].getName());
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}
