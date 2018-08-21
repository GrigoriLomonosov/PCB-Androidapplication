package com.example.tac.boardcommunicator;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NodeControlFragment.OnNodeSelectedListener} interface
 * to handle interaction events.
 * Use the {@link NodeControlFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NodeControlFragment extends Fragment {

    private final String TAG = "in nodeControlFragment";

    private Button setMinTime;
    private Button readMinTime;
    private Button setMinWeight;
    private Button readMinWeight;
    private Button setMinCompareTime;
    private Button readMinCompareTime;
    private Button setTimeStep;
    private Button readTimeStep;
    private Button setWeightStep;
    private Button readWeightStep;
    private TextView outputText;
    private TextView inputText;

    private final BluetoothService bluetoothService = BluetoothService.getInstance();

    // Listener to communicate with Activity
    private OnNodeSelectedListener mListener;

    public NodeControlFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     */
    public static NodeControlFragment newInstance() {
        NodeControlFragment fragment = new NodeControlFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Do nothing: no parameters
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_node_control, container, false);

        // Set the layout
        outputText = (TextView) view.findViewById(R.id.outputText);
        inputText = (TextView) view.findViewById(R.id.inputText);

        // Buttons
        setMinTime = (Button) view.findViewById(R.id.setMinTimeBtn);
        readMinTime = (Button) view.findViewById(R.id.readMinTimeBtn);
        setMinWeight = (Button) view.findViewById(R.id.setMinWeightBtn);
        readMinWeight = (Button) view.findViewById(R.id.readMinWeightBtn);
        setMinCompareTime = (Button) view.findViewById(R.id.setMinCompareTimeBtn);
        readMinCompareTime = (Button) view.findViewById(R.id.readMinCompareTimeBtn);
        setTimeStep = (Button) view.findViewById(R.id.setTimeStepBtn);
        readTimeStep = (Button) view.findViewById(R.id.readTimeStepBtn);
        setWeightStep = (Button) view.findViewById(R.id.setWeightStepBtn);
        readWeightStep = (Button) view.findViewById(R.id.readWeightStepBtn);

        // Listeners for Buttons
        setMinTime.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                setMinTime(v);
            }
        });
        readMinTime.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                readMinTime(v);
            }
        });
        setMinWeight.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                setMinweight(v);
            }
        });
        readMinWeight.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                readMinWeight(v);
            }
        });
        setMinCompareTime.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                setMinCompareTime(v);
            }
        });
        readMinCompareTime.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                readMinCompareTime(v);
            }
        });
        setTimeStep.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                setTimeStep(v);
            }
        });
        readTimeStep.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                readTimeStep(v);
            }
        });
        setWeightStep.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                setWeightStep(v);
            }
        });
        readWeightStep.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                readMinTime(v);
            }
        });

        super.onCreate(savedInstanceState);

        return view;
    }

    private void setMinTime(View v){
        bluetoothService.setMinTime(getResources().getString(R.string.cmd_setminweighttime));
        Log.d(TAG, "setMinTime: pressed");
    }

    private void readMinTime(View v){
        bluetoothService.readMinTime(getResources().getString(R.string.cmd_readminweighttime));
        Log.d(TAG, "readMinTime: pressed");
    }

    private void setMinweight(View v){
        bluetoothService.setMinweight(getResources().getString(R.string.cmd_setminweight));
        Log.d(TAG, "setMinweight: pressed");
    }

    private void readMinWeight(View v){
        bluetoothService.readMinWeight(getResources().getString(R.string.cmd_readminweight));
        Log.d(TAG, "readMinWeight: pressed");
    }

    private void setMinCompareTime(View v){
        bluetoothService.setMinCompareTime(getResources().getString(R.string.cmd_setmincomparetime));
        Log.d(TAG, "setMinCompareTime: pressed");
    }

    private void readMinCompareTime(View v){
        bluetoothService.readMinCompareTime(getResources().getString(R.string.cmd_readmincomparetime));
        Log.d(TAG, "readMinCompareTime: pressed");
    }

    private void setTimeStep(View v){
        bluetoothService.setTimeStep(getResources().getString(R.string.cmd_settimesteps));
        Log.d(TAG, "setTimeStep: pressed");
    }

    private void readTimeStep(View v){
        bluetoothService.readTimeStep(getResources().getString(R.string.cmd_readtimesteps));
        Log.d(TAG, "readTimeStep: pressed");
    }

    private void setWeightStep(View v){
        bluetoothService.setWeightStep(getResources().getString(R.string.cmd_setweightsteps));
        Log.d(TAG, "setWeightStep: pressed");
    }

    private void readWeightStep(View v){
        bluetoothService.readMinWeight(getResources().getString(R.string.cmd_readweightsteps));
        Log.d(TAG, "readWeightStep: presssed");
    }
    
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNodeSelectedListener) {
            mListener = (OnNodeSelectedListener) context;
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
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnNodeSelectedListener {
        void onNodeSelected();
    }
}
