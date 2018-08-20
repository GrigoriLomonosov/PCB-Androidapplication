package com.example.tac.boardcommunicator;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BluetoothFragment.OnBluetoothSelectedListener} interface
 * to handle interaction events.
 * Use the {@link BluetoothFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BluetoothFragment extends Fragment implements
        AdapterView.OnItemSelectedListener{

    // Layout fields
    private String TAG = "test";
    private TextView device_name;
    private TextView device_address;
    private TextView bt_result_text;
    private TextView result_text;
    private Button connectBtn;
    private Button disconnectBtn;
    private Button readIPBtn;
    private Button readSSIDBtn;
    private Button readPWBtn;
    private Button findFreeIpBtn;
    private Button setIPBtn;
    private Button testBtn;

    private final BluetoothService bluetoothService = BluetoothService.getInstance();

    // Listener to communicate with Activity
    private OnBluetoothSelectedListener mListener;

    public BluetoothFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment BluetoothFragment.
     */
    public static BluetoothFragment newInstance() {
        BluetoothFragment fragment = new BluetoothFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
           // Do nothing: no arguments
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bluetooth, container, false);

        // Set the layout
        device_name = (TextView) view.findViewById(R.id.device_name);
        device_address = (TextView) view.findViewById(R.id.device_address);
        result_text = (TextView) view.findViewById(R.id.resultText);
        bt_result_text = (TextView) view.findViewById(R.id.BT_result_text);
        Spinner s = (Spinner) view.findViewById(R.id.BTSpinner);
        s.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(getActivity().getApplicationContext(),android.R.layout.simple_spinner_item,bluetoothService.getDevices());
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(aa);

        //Buttons
        connectBtn = (Button) view.findViewById(R.id.connectBtn);
        disconnectBtn = (Button) view.findViewById(R.id.disconnectBtn);
        readIPBtn = (Button) view.findViewById(R.id.readIPBtn);
        readSSIDBtn = (Button) view.findViewById(R.id.readSSIDBtn);
        readPWBtn = (Button) view.findViewById(R.id.readPwBtn);
        findFreeIpBtn = (Button) view.findViewById(R.id.findFreeIP);
        setIPBtn = (Button) view.findViewById(R.id.setIP);
        testBtn = (Button) view.findViewById(R.id.testBtn);

        // Set the listeners for the buttons
        connectBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                connectToBT(v);
            }
        });

        disconnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disconnectFromBT(v);
            }
        });

        readIPBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                readIP(v);
            }
        });

        readSSIDBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readSSID(v);
            }
        });

        readPWBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readPassword(v);
            }
        });

        findFreeIpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findFreeIP(v);
            }
        });

        setIPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIP(v);
            }
        });

        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testDevice(v);
            }
        });

        super.onCreate(savedInstanceState);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnBluetoothSelectedListener) {
            mListener = (OnBluetoothSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Sets the textfields in the application based on the selected value from the spinner
     * @param arg0
     * @param arg1
     * @param position The position of the selected item in the list
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        Log.d(TAG, "onItemSelected: in selecter");
        bluetoothService.setDevice(bluetoothService.getDevices()[position]);
        if(bluetoothService.getDevice() != null){
            device_address.setText(bluetoothService.getDevice().getAddress());
            device_name.setText(bluetoothService.getDevice().getName());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        device_address.setText("");
        device_name.setText("");
    }

    /**
     * Connects the selected device through Bluetooth
     * @param view
     */
    private void connectToBT(View view){
        Log.d(TAG, "connectToBT: pressed");
        if(bluetoothService.connectToBT()){
            bt_result_text.setText("Connection successfull");
        }
        else{
            if(bluetoothService.getDevice() == null){
                bt_result_text.setText("Please select a device before connecting");
            }
            else {
                bt_result_text.setText("Connection failed: try disconnecting before connecting again");
            }
        }
    }

    /**
     * Disconnects the currently connected device. If no connection is present, nothing happens
     * @param view
     */
    private void disconnectFromBT(View view){
        Log.d(TAG, "disconnectFromBT: pressed");
        if(bluetoothService.disconnectFromBT()){
            bt_result_text.setText("No more connections: disconnection successfull");
        }
        else{
            bt_result_text.setText("Disconnection failed");
        }
    }

    /**
     * Reads the current ip-address from the device
     * @param view
     */
    private void readIP(View view){
        Log.d(TAG, "readIP: pressed");
        result_text.setText(bluetoothService.readIP(getResources().getString(R.string.cmd_readip)));
    }

    /**
     * Reads the current SSID from the device
     * @param view
     */
    private void readSSID(View view){
        Log.d(TAG, "readSSID: pressed");
        result_text.setText(bluetoothService.readSSID(getResources().getString(R.string.cmd_readssid)));
    }

    /**
     * Reads the current password
     * @param view
     */
    private void readPassword(View view){
        Log.d(TAG, "readPassword: pressed");
        result_text.setText(bluetoothService.readPW(getResources().getString(R.string.cmd_readpasword)));
    }

    /**
     * Finds a free ip-address in the network
     * @param view
     */
    private void findFreeIP(View view){
        Log.d(TAG, "findFreeIP: pressed");
        result_text.setText(bluetoothService.findFreeIP());
    }

    private void testDevice(View view){
        Log.d(TAG, "testDevice: pressed");
        result_text.setText(bluetoothService.test(getResources().getString(R.string.cmd_test)));
    }

    /**
     * Sets the ip-address to the one currently in a textfield. When format is not correct, nothing happens
     * @param view
     */
    private void setIP(View view){
        Log.d(TAG, "setIP: pressed");
        String ip = result_text.getText().toString().trim();
        result_text.setText(bluetoothService.setIP("123.123.123.132", getResources().getString(R.string.cmd_changeip)));
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnBluetoothSelectedListener {
        void onBluetoothSelected();
    }
}
