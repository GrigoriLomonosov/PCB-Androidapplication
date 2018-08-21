package com.example.tac.boardcommunicator;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainControlFragment.OnMainSelectedListener} interface
 * to handle interaction events.
 * Use the {@link MainControlFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainControlFragment extends Fragment {

    //TODO remove testvalue
    private final String TAG = "main control";

    private Button updateDateBtn;
    private Button updateTimeBtn;
    private Button uploadBtn;
    private Button updateVolumeBtn;
    private SeekBar currentVolumeBar;
    private TextView currentVolumeText;

    private final BluetoothService bluetoothService = BluetoothService.getInstance();

    private OnMainSelectedListener mListener;

    public MainControlFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment MainControlFragment.
     */
    public static MainControlFragment newInstance() {
        MainControlFragment fragment = new MainControlFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Do nothing, no args
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_control, container, false);

        // Set the layout
        currentVolumeText = (TextView) view.findViewById(R.id.volTextView);
        currentVolumeBar = (SeekBar) view.findViewById(R.id.volSeekBar);

        // Buttons
        updateDateBtn = (Button) view.findViewById(R.id.dateBtn);
        updateTimeBtn = (Button) view.findViewById(R.id.timeBtn);
        uploadBtn = (Button) view.findViewById(R.id.uploadBtn);
        updateVolumeBtn =(Button) view.findViewById(R.id.volBtn);

        // Listeners for Buttons
        updateDateBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                updateDate(v);
            }
        });
        updateTimeBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                updateTime(v);
            }
        });
        uploadBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                uploadSoundFile(v);
            }
        });
        updateVolumeBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                updateVolume(v);
            }
        });

        super.onCreate(savedInstanceState);

        return view;
    }

    private void updateDate(View view){
        bluetoothService.updateDate(getResources().getString(R.string.cmd_update_date));
        Log.d(TAG, "updateDate: pressed");
    }

    private void updateTime(View view){
        bluetoothService.updateTime(getResources().getString(R.string.cmd_update_time));
        Log.d(TAG, "updateTime: pressed");
    }

    private void updateVolume(View view){
        bluetoothService.updateVolume(getResources().getString(R.string.cmd_update_volume));
        Log.d(TAG, "updateVolume: pressed");
    }

    private void uploadSoundFile(View view){
        bluetoothService.uploadSoundFile(getResources().getString(R.string.cmd_upload_file));
        Log.d(TAG, "uploadSoundFile: pressed");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMainSelectedListener) {
            mListener = (OnMainSelectedListener) context;
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
    public interface OnMainSelectedListener {
        void onMainSelected();
    }
}
