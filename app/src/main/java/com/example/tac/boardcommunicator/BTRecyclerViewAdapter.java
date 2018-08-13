package com.example.tac.boardcommunicator;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class BTRecyclerViewAdapter extends RecyclerView.Adapter<BTRecyclerViewAdapter.CustomViewHolder> {

        private List<BlueToothDevice> devicesList;
        private Context mContext;

        public BTRecyclerViewAdapter(Context context, List<BlueToothDevice> devicesList) {
            this.devicesList = devicesList;
            this.mContext = context;
        }

        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row, null);
            CustomViewHolder viewHolder = new CustomViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
            BlueToothDevice device = devicesList.get(i);
            customViewHolder.name.setText(device.getName());
            customViewHolder.address.setText(device.getAddress());
        }

        @Override
        public int getItemCount() {
            return (null != devicesList ? devicesList.size() : 0);
        }

        class CustomViewHolder extends RecyclerView.ViewHolder {
            protected TextView name;
            protected TextView address;

            public CustomViewHolder(View view) {
                super(view);
                this.name = (TextView) view.findViewById(R.id.name);
                this.address = (TextView) view.findViewById(R.id.title);
            }
        }
    }
