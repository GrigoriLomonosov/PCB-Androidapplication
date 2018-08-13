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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Devices extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener{

    private String TAG = "test";
    private TextView text;
    private Button btn;

    String[] country = { "India", "USA", "China", "Japan", "Other"};

    private BluetoothAdapter myBluetooth = null;
    private Set<BluetoothDevice> pairedDevices;
    public static String EXTRA_ADDRESS = "device_address";

    private List<BlueToothDevice> devices = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO remove
        devices.add(new BlueToothDevice("name1","address1"));
        devices.add( new  BlueToothDevice("name2","address2"));

        //TextView text = (TextView) findViewById(R.id.textView);

        myBluetooth = BluetoothAdapter.getDefaultAdapter();
        if ( myBluetooth==null ) {
            //text.setText("adapter niet gevonden");

        } else if ( !myBluetooth.isEnabled() ) {
            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnBTon, 1);
            //text.setText(myBluetooth.toString());
        }

        setContentView(R.layout.activity_devices);

        text = (TextView) findViewById(R.id.devices_text_view);
        text.setText("test");

        // Spinner with devices
        String[] arraySpinner = new String[] {
                "1", "2", "3", "4", "5"
        };
        Spinner s = (Spinner) findViewById(R.id.BTSpinner);
        s.setOnItemSelectedListener(this);
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,country);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        s.setAdapter(aa);


        super.onCreate(savedInstanceState);

    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        Toast.makeText(getApplicationContext(),country[position] , Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}
