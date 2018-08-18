package com.example.tac.boardcommunicator;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class BluetoothService {

    //TODO Remove: testfield for logging
    private String TAG = "test";

    // The encoding used in the writing
    private final String encoding = "UTF-8";

    // The size of the buffer who receives messages from the board
    private final int bufferSize = 1024;

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

    // Variable for activities to know if the application is already connected through bluetooth TODO checken of het nodig is
    private boolean isConnected = false;

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

    /**
     * Returns the singleton running all the code handling the Bluetooth connection
     * @return The singleton handling the Bluetooth connection
     */
    public static BluetoothService getInstance(){
        if (INSTANCE == null) {
            INSTANCE = new BluetoothService();
        }
        return(INSTANCE);
    }

    /**
     * An array holding all the devices recognized by this device
     * @return array holding the recognized devices
     */
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
     * @return true if the connection succeeded, false otherwise
     */
    public boolean connectToBT(){
        if (device != null && connectThread == null){
            connectThread = new ConnectThread(device);
            connectThread.run();
            connectedThreadRead = new ConnectedThread();
            connectedThreadWrite = new ConnectedThread();
            //TODO deze weer inschakelen om te readen
            /*//Start listening in the background to the connected device
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    connectedThreadRead.run();
                }
            });*/
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Disconnects the application from the current Bluetooth device.
     * @return True if the bluetooth connection was stopped, false otherwise
     */
    public boolean disconnectFromBT(){
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
            return true;
        }
        return false;
    }

    public byte[] read(){
        return connectedThreadRead.read();
    }

    public boolean write(byte[] byteArr){
        String text = "START";
        try{
            byte[] bytes = text.getBytes("UTF-8");
            connectedThreadRead.write(bytes);
        }
        catch (UnsupportedEncodingException e){
            Log.d(TAG, "write: " + e.getMessage());
            return false;
        }
        return true;
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
        
        public byte[] read(){
            byte[] buffer = new byte[bufferSize];
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    try{
                        mmInStream.read(mmBuffer);
                    }
                    catch (IOException e) {
                        Log.d(TAG, "read: failed in ConnectedThread");
                    }

                }
            });
            return mmBuffer;
        }

        /**
         * Listens to the connected Bluetooth device for incoming messages.
         */
        public void run() {
            mmBuffer = new byte[bufferSize];

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
            Log.d(TAG, "write: ");
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
