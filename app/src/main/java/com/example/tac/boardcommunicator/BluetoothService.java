package com.example.tac.boardcommunicator;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class BluetoothService {

    //TODO adjust with start value
    //TODO Remove: testfield for logging
    private String TAG = "test";

    // This string is at the start of every command. It is the hex-string representation of START
    private final String start = "5354415254";

    private final byte[] startValue = new byte[]{'S','T','A','R','T'};

    private final byte[] dummyBytes = new byte[]{3,3};

    // String representation of 2 dummy bytes
    private final String dummies = "0000";

    // The encoding used in the writing
    private final String encoding = "ASCII";

    // The maximum delay before the device has to answer
    private final int maxSleep = 150;

    // The size of the buffer who receives messages from the board
    private final int bufferSize = 1024;

    // Standard UUID
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // Static field to allow for the SINGLETON design pattern
    private static BluetoothService INSTANCE = null;

    // Handles processing of different types of data
    private final DataProcessor dataProcessor = new DataProcessor();

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
            // Test if connection is working
            try {
                socket.getOutputStream().write(12);
            }
            catch (IOException e){
                return false;
            }
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

    /**
     * Returns true if the bluetoothservice is capable of reading and writing to a certain device
     * @return
     */
    public boolean isConnected(){
        return connectedThreadRead != null && connectedThreadWrite != null;
    }


    //###########################################################################################
    //################## Write your commands to communicate below ###############################


    /**
     * Returns a free ip-address in the current network
     * @return an available ip-address
     */
    public String findFreeIP(){
        return "just a testvalue" + dataProcessor.findFreeIp();
    }

    /**
     * Sets the ip of the device to the given ip
     * @param ip to new ip-address
     * @param cmd the basic command to send to the device
     * @return a string holding a response from the device
     */
    public String setIP(String ip, String cmd){
        //TODO check
        String reply = "Set IP failed: nothing happened";
        if(isConnected()){
            if (dataProcessor.checkIPFormat(ip.trim())){
                String processedCmd = start + cmd.replaceAll(" ", "") + dataProcessor.ip2Hex(ip) + dummies;
                Log.d(TAG, "setIP: " + processedCmd);
                try{
                    Log.d(TAG, "setIP: here");
                    write(processedCmd.getBytes(encoding));
                    byte[] temp = read();
                    if(new String(temp,encoding).contains("OKEND")){
                        return "IP set correctly to: " + ip;
                    }
                    else{
                        return "Please try again, not able to show password";
                    }
                }
                catch (Exception e){
                    reply = "Could not write: " + e.getMessage();
                    Log.d(TAG, "setIP: " + e.getMessage());
                }
                reply = "Set IP succeeded";
            }
            else{
                reply = "IP is not in the correct format";
            }
        }
        else{
            reply = "Connect a device before changing the IP";
        }
        return reply;
    }

    /**
     * Returns the password
     * @return
     */
    public String readPW(String cmd){
        byte[] cmdData = createByteArrayWithCmdData(cmd);
        try{
            byte[] finalCmd = dataProcessor.concatByteArrays(startValue, cmdData, dummyBytes);
            write(finalCmd);
            byte[] received = read();
            return new String(received);
        }
        catch(Exception e){
            Log.d(TAG, "test: " + e.getMessage());
        }
        return "Nothing received within designated time";
    }

    /**
     * Returns the current SSID
     * @return
     */
    public String readSSID(String cmd){
        byte[] cmdData = createByteArrayWithCmdData(cmd);
        try{
            byte[] finalCmd = dataProcessor.concatByteArrays(startValue, cmdData, dummyBytes);
            write(finalCmd);
            byte[] received = read();
            return new String(received);
        }
        catch(Exception e){
            Log.d(TAG, "test: " + e.getMessage());
        }
        return "Nothing received within designated time";
    }

    /**
     * Reads the current ip of the device
     * @return
     */
    public String readIP(String cmd){
        byte[] cmdData = createByteArrayWithCmdData(cmd);
        try{
            byte[] finalCmd = dataProcessor.concatByteArrays(startValue, cmdData, dummyBytes);
            write(finalCmd);
            byte[] received = read();
            return dataProcessor.byteArrayToStringIP(received);
        }
        catch(Exception e){
            Log.d(TAG, "test: " + e.getMessage());
        }
        return "Nothing received within designated time";
    }

    public String test(String cmd){
        byte[] cmdData = createByteArrayWithCmdData(cmd);
        try{
            byte[] finalCmd = dataProcessor.concatByteArrays(startValue, cmdData, dummyBytes);
            write(finalCmd);
            byte[] received = read();
            return new String(received);
        }
        catch(Exception e){
            Log.d(TAG, "test: " + e.getMessage());
        }
        return "Nothing received within designated time";
    }

    public String setMinTime(){
        //TODO implement
        return "";
    }

    public String readMinTime(){
        //TODO implement
        return "";
    }

    public String setMinweight(){
        //TODO implement
        return "";
    }

    public String readMinWeight(){
        //TODO implement
        return "";
    }

    public String setMinCompareTime(){
        //TODO implement
        return "";
    }

    public String readMinCompareTime(){
        //TODO implement
        return "";
    }

    public String setTimeStep(){
        //TODO implement
        return "";
    }

    public String readTimeStep(){
        //TODO implement
        return "";
    }

    public String setWeightStep(){
        //TODO implement
        return "";
    }

    public String readWeightStep(){
        //TODO implement
        return "";
    }

    public String updateDate(){
        //TODO implement
        return "";
    }

    public String updateTime(){
        //TODO implement
        return "";
    }

    public String updateVolume(){
        //TODO implement
        return "";
    }

    public String uploadSoundFile(){
        //TODO implement
        return "";
    }

    //#############################################################################
    //#############################################################################

    private byte[] createByteArrayWithCmdData(String cmd){
        String[] processedCmd = cmd.split(" ");
        int cmdNumber = Integer.valueOf(processedCmd[0].trim());
        int lengthOfCmd = Integer.valueOf(processedCmd[1].trim());
        byte[] cmdData = new byte[]{(byte)cmdNumber,(byte)lengthOfCmd};
        return cmdData;
    }

    private void write(byte[] byteArr){
        connectedThreadWrite.write(byteArr);
    }

    private byte[] read() {
        byte[] temp = new byte[bufferSize];
        try{
            Thread.sleep(maxSleep);
            return connectedThreadRead.read();
        }
        catch(InterruptedException e){
            Log.d(TAG, "read: " + e.getMessage());
        }
        return temp;
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
        private byte[] mmBuffer = new byte[bufferSize]; // mmBuffer store for the stream

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

       /* public byte[] read2(int numberOfBytes){
            byte[] tempBuffer = new byte[numberOfBytes];
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
        }*/

        public byte[] read(){
            int treshHold = 0;
            try{
                while (mmInStream.available()==0 && treshHold<3000)
                {
                    Thread.sleep(1);
                    treshHold++;
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                baos.reset();

                while (mmInStream.available() > 0)
                {
                    baos.write(mmInStream.read());
                    Thread.sleep(1);
                }

                return baos.toByteArray();
            }
            catch (Exception e){
                Log.d(TAG, "read2: " + e.getMessage());
            }
            return new byte[1];
        }

        public int readOneByte(){
            try{
                return mmInStream.read();
            }
            catch(IOException e){
                Log.d(TAG, "readOneByte: " + e.getMessage());
            }
            return -1;
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
                } catch (IOException e) {
                    Log.d(TAG, "Input stream was disconnected", e);
                    break;
                }
            }
        }

        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
                Log.d(TAG, "write: written to temp output");
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when sending data", e);
            }
        }

        // Call this method to shut down the connection.
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
