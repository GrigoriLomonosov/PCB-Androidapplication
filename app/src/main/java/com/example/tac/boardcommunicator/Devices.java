package com.example.tac.boardcommunicator;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Searches for Bluetooth devices and allows connection and disconnection of a selected device
 */
public class Devices extends AppCompatActivity{// implements
        //AdapterView.OnItemSelectedListener{

    // Standard UUID
    //static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

/*    // Layout fields
    private String TAG = "test";
    private TextView device_name;
    private TextView device_address;
    private TextView result_text;

    // Currently connected device TODO dit kan hier wsh nog properder
    private BluetoothDevice device;

    // The Bluetooth service
    private final BluetoothService service = BluetoothService.getInstance();

*//*    // Bluetooth fields
    private Set<BluetoothDevice> devices = new HashSet<>();
    private BluetoothDevice[] deviceArr;
    private BluetoothDevice device;
    private BluetoothSocket socket;
    private BluetoothAdapter myBluetooth = null;

    // Keeps track of currently running threads
    private ConnectThread connectThread;
    private ConnectedThread connectedThreadRead;
    private ConnectedThread connectedThreadWrite;*//*

    // Create a BroadcastReceiver for ACTION_FOUND of new devices.
    *//*private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //TODO handle discovery of new objects
            }
        }
    };*//*

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Make objects necessary for BT-connection and fill devices array
       *//* myBluetooth = BluetoothAdapter.getDefaultAdapter();
        if ( myBluetooth==null ) {
            Log.d(TAG, "onCreate: BTAdapter niet gevonden");

        } else if ( !myBluetooth.isEnabled() ) {
            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnBTon, 1);
            Log.d(TAG, "onCreate: BTAdapter gevonden");
        }
        //TODO fill the devices array
        devices = myBluetooth.getBondedDevices();
        deviceArr = setToArr(devices);*//*

        // Register for broadcasts when a device is discovered. TODO
        // Register for broadcasts when a device is discovered.
       *//* IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);*//*

        setContentView(R.layout.activity_devices);

        // Set the layout
        device_name = (TextView) findViewById(R.id.device_name);
        device_address = (TextView) findViewById(R.id.device_address);
        result_text = (TextView) findViewById(R.id.BT_result_text);
        Spinner s = (Spinner) findViewById(R.id.BTSpinner);
        s.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,service.getDevices());
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(aa);

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        Log.d(TAG, "onItemSelected: in selecter");
        device = service.getDevices()[position];
        service.setDevice(device);
        if(device != null){
            device_address.setText(device.getAddress());
            device_name.setText(device.getName());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        device_address.setText("");
        device_name.setText("");
    }

    public void connectToBT(View view){
        Log.d(TAG, "connectToBT: pressed");
        if(service.connectToBT()){
            result_text.setText("Connection successfull");
        }
        else{
            if(device == null){
                result_text.setText("Please select a device before connecting");
            }
            else {
                result_text.setText("Connection failed: try disconnecting before connecting again");
            }
        }
    }

    public void disconnectFromBT(View view){
        Log.d(TAG, "disconnectFromBT: pressed");
        if(service.disconnectFromBT()){
            result_text.setText("No more connections: disconnection successfull");
        }
        else{
            result_text.setText("Disconnection failed");
        }
    }*/
/*
    *//**
     * Connects the application to the selected Bluetooth device. Connection is only possible when there is not yet a connection established and a device selected
     * @param view
     *//*
    public void connectToBT(View view){
        if (device != null && connectThread == null){
            connectThread = new ConnectThread(device);
            connectThread.run();
            connectedThreadRead = new ConnectedThread();
            connectedThreadWrite = new ConnectedThread();
            result_text.setText("Connection successfull");
            //Start listening in the background to the connected device
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    connectedThreadRead.run();
                }
            });
        }
        else{
            Log.d(TAG, "connectToBT: Connect to BT failed");
            if (connectThread != null) {
                result_text.setText("Already connected to a device. Disconnect before trying to connect again.");
            }
            else if(device == null){
                result_text.setText("Please select a device before trying to connect.");
            }
            //TODO write alert to tell users to select a device
        }
    }

    *//**
     * Disconnects the application from the current Bluetooth device.
     * @param view
     *//*
    public void disconnectFromBT(View view){
        //Stop connection thread
        if(connectThread != null){
            connectThread.cancel();
        }
        connectThread = null;
        //Stop reading thread
        if(connectedThreadRead != null){
            connectedThreadRead.cancel();
        }
        connectedThreadRead = null;
        //Stop writing thread
        if(connectedThreadWrite != null){
            connectedThreadWrite.cancel();
        }
        connectedThreadWrite = null;
        if(connectThread == null && connectedThreadRead == null && connectedThreadWrite == null){
            result_text.setText("No more connections: disconnection successfull");
        }
    }

    public String read(){
        //TODO Hoe gaan we dat oplossen
        String result = "";
        if(connectedThreadRead == null){
            result_text.setText("Please connect before trying to read");
        }
        else{
            //result = new String(connectedThreadRead.read());
            //result_text.setText(result);
        }
        return result;
    }

    public void write(String message){

    }*/
/*
    //##############################################################################################
    //##############################################################################################

    *//**
     * Handles the connection to a bluetooth device
     *//*
    private class ConnectThread extends Thread {
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device){
            BluetoothSocket temp = null;
            mmDevice = device;
            try{
                temp = device.createInsecureRfcommSocketToServiceRecord(myUUID);
            }
            catch (IOException e){
                Log.e(TAG, "ConnectThread: socket create failed", e);
            }
            socket = temp;
        }

        *//**
         * Tries to setup a Bluetooth connection with a device
         *//*
        public void run() {
            // Cancel discovery because it otherwise slows down the connection.
            myBluetooth.cancelDiscovery();

            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                socket.connect();
                Log.d(TAG, "run: connection succeeded");
            } catch (IOException connectException) {
                // Unable to connect; close the socket and return.
                try {
                    Log.d(TAG, "run: Unable to connect");
                    socket.close();
                } catch (IOException closeException) {
                    Log.e(TAG, "Could not close the client socket", closeException);
                }
                return;
            }
        }

        // Closes the client socket and causes the thread to finish.
        public void cancel() {
            try {
                socket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the client socket", e);
            }
        }
    }

    //##############################################################################################
    //##############################################################################################

    *//**
     * Handles the sending and receiving of data to/from the Bluetooth device
     *//*
    private class ConnectedThread extends Thread {
        private final int buffersize = 256;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private byte[] mmBuffer; // mmBuffer store for the stream

        public ConnectedThread() {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams; using temp objects because
            // member streams are final.
            try {
                tmpIn = socket.getInputStream();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when creating input stream", e);
            }
            try {
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when creating output stream", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        *//**
         * Listens to the connected Bluetooth device for incoming messages.
         *//*
        public void run() {
            mmBuffer = new byte[buffersize];

            // Keep listening to the InputStream until an exception occurs.
            while (true) {
                String s = "";
                try {
                    mmInStream.read(mmBuffer);
                    //TODO remove this testcode
                    String result = new String(mmBuffer);
                    Log.d(TAG, "run: " + result);
                } catch (IOException e) {
                    Log.d(TAG, "Input stream was disconnected", e);
                    break;
                }
            }
        }

        // Call this from the main activity to send data to the remote device.
        public void write(byte[] bytes) {
            Log.d(TAG, "write: in write111111111111111111111111111111111");
            try {
                mmOutStream.write(bytes);
                Log.d(TAG, "write: written to temp output");
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when sending data", e);
            }
        }

        // Call this method from the main activity to shut down the connection.
        public void cancel() {
            try {
                socket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the connect socket", e);
            }
        }
    }*/

/*    *//**
     * Alters set of Bluetoothdevices to an array of Bluetoothdevices
     * @param input
     * @return
     *//*
    private BluetoothDevice[] setToArr(Set<BluetoothDevice> input){
        BluetoothDevice[] result = new BluetoothDevice[input.size()];
        int i = 0;
        for(BluetoothDevice dev: input){
            result[i] = dev;
            i++;
        }
        return result;
    }*/
}
