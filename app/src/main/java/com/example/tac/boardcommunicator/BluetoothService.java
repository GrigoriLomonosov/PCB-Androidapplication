package com.example.tac.boardcommunicator;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class BluetoothService {

    //TODO TESTfield
    private String TAG = "test";

    // Standard UUID
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // Static field to allow for the SINGLETON design pattern
    private static BluetoothService INSTANCE = null;

    // Bluetooth fields
    private Set<BluetoothDevice> devices = new HashSet<>();
    private BluetoothDevice[] deviceArr;
    private BluetoothDevice device;
    private BluetoothSocket socket;
    private BluetoothAdapter myBluetooth = null;

    // Keeps track of currently running threads
    private ConnectThread connectThread;
    private ConnectedThread connectedThreadRead;
    private ConnectedThread connectedThreadWrite;

    private BluetoothService() {
        myBluetooth = BluetoothAdapter.getDefaultAdapter();
        if ( myBluetooth==null ) {
            Log.d(TAG, "onCreate: BTAdapter niet gevonden");

        } else if ( !myBluetooth.isEnabled() ) {
            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        }
        //TODO fill the devices array
        devices = myBluetooth.getBondedDevices();
        deviceArr = setToArr(devices);
    }

    public static BluetoothService getInstance(){
        if (INSTANCE == null) {
            INSTANCE = new BluetoothService();
        }
        return(INSTANCE);
    }

    public BluetoothDevice[] getDevices(){
        return deviceArr;
    }

    public void setDevice(BluetoothDevice device){
        this.device = device;
    }

    public BluetoothDevice getDevice(){
        return device;
    }


    /**
     * Connects the application to the selected Bluetooth device. Connection is only possible when there is not yet a connection established and a device selected
     */
    public void connectToBT(){
        if (device != null && connectThread == null){
            connectThread = new ConnectThread(device);
            connectThread.run();
            connectedThreadRead = new ConnectedThread();
            connectedThreadWrite = new ConnectedThread();
            //result_text.setText("Connection successfull");
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
                //result_text.setText("Already connected to a device. Disconnect before trying to connect again.");
            }
            else if(device == null){
                //result_text.setText("Please select a device before trying to connect.");
            }
            //TODO write alert to tell users to select a device
        }
    }

    /**
     * Disconnects the application from the current Bluetooth device.
     */
    public void disconnectFromBT(){
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
            //result_text.setText("No more connections: disconnection successfull");
        }
    }

    public String read(){
        //TODO Hoe gaan we dat oplossen
        String result = "";
        if(connectedThreadRead == null){
            //result_text.setText("Please connect before trying to read");
        }
        else{
            //result = new String(connectedThreadRead.read());
            //result_text.setText(result);
        }
        return result;
    }

    public void write(String message){

    }


    //##############################################################################################
    //##############################################################################################

    /**
     * Handles the connection to a bluetooth device
     */
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

        /**
         * Tries to setup a Bluetooth connection with a device
         */
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

    /**
     * Handles the sending and receiving of data to/from the Bluetooth device
     */
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

        /**
         * Listens to the connected Bluetooth device for incoming messages.
         */
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
    }

    /**
     * Alters set of Bluetoothdevices to an array of Bluetoothdevices
     * @param input
     * @return
     */
    private BluetoothDevice[] setToArr(Set<BluetoothDevice> input){
        BluetoothDevice[] result = new BluetoothDevice[input.size()];
        int i = 0;
        for(BluetoothDevice dev: input){
            result[i] = dev;
            i++;
        }
        return result;
    }
}
