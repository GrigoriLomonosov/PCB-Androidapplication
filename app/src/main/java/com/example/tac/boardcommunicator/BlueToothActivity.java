package com.example.tac.boardcommunicator;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.Set;
import java.util.UUID;

/**
 * Responsible for finding BT devices and connecting to a selected BT device
 */
public class BlueToothActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 1;

    // The UUID for this device
    private static final String MY_UUID = UUID.randomUUID().toString();

    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    // Set of devices that share a link-key that can be used for authentication, and are capable of establishing an encrypted connection with each other.
    private Set<BluetoothDevice> pairedDevices;

    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Register for broadcasts when a device is discovered.
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue_tooth);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(mReceiver);
    }

    /**
     * Enables BT if it is supported by the device
     */
    public void enableBluetooth() {
        if (bluetoothAdapter == null) {
            Log.d("test", "BTCommunicator: device does not support bluetooth");
        }
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    /**
     * Returns the set of currently paired devices
     * @return The set of paired devices
     */
    public Set<BluetoothDevice> queryPairedDevices() {
        pairedDevices = bluetoothAdapter.getBondedDevices();
        if(pairedDevices.isEmpty()) {
            Log.d("test", "queryPairedDevices: no paired devices found");
        }
/*
        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
            }
        }*/
        return pairedDevices;
    }


    public void discoverDevices() {
        //TODO dit is waarschijnlijk niet nodig, zie oncreate
    }

    public void connectToDevice() {

    }
}
