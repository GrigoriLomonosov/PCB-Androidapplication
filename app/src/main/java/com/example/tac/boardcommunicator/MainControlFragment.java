package com.example.tac.boardcommunicator;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    private String title;
    private int page;

    private OnMainSelectedListener mListener;

    public MainControlFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainControlFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainControlFragment newInstance(String param1, String param2) {
        MainControlFragment fragment = new MainControlFragment();
        Bundle args = new Bundle();
        args.putString("someInt", param1);
        args.putString("someTitle", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            page = getArguments().getInt("someInt");
            title = getArguments().getString("someTitle");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_control, container, false);
        TextView label = (TextView)view.findViewById(R.id.testjes);
        label.setText("eindelijk gelukt");
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onMainSelected();
        }
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
        // TODO: Update argument type and name
        void onMainSelected();
    }
}
