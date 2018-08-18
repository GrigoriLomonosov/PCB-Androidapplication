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
    private TextView result_text;

    // Currently connected device TODO dit kan hier wsh nog properder
    private BluetoothDevice device;

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
        result_text = (TextView) view.findViewById(R.id.BT_result_text);
        Spinner s = (Spinner) view.findViewById(R.id.BTSpinner);
        s.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(getActivity().getApplicationContext(),android.R.layout.simple_spinner_item,bluetoothService.getDevices());
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(aa);
        final Button connectBtn = (Button) view.findViewById(R.id.connectBtn);
        final Button disconnectBtn = (Button) view.findViewById(R.id.disconnectBtn);

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
        device = bluetoothService.getDevices()[position];
        bluetoothService.setDevice(device);
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

    /**
     * Connects the selected device through Bluetooth
     * @param view
     */
    private void connectToBT(View view){
        Log.d(TAG, "connectToBT: pressed");
        if(bluetoothService.connectToBT()){
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

    /**
     * Disconnects the currently connected device. If no connection is present, nothing happens
     * @param view
     */
    private void disconnectFromBT(View view){
        Log.d(TAG, "disconnectFromBT: pressed");
        if(bluetoothService.disconnectFromBT()){
            result_text.setText("No more connections: disconnection successfull");
        }
        else{
            result_text.setText("Disconnection failed");
        }
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
