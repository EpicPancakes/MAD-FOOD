package com.example.onlyfoods.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlyfoods.R;

public class DiscoverRecyclerViewAdapter extends RecyclerView.Adapter<DiscoverRecyclerViewAdapter.MyViewHolder>{

    // Testing Purpose
    String[] list;

    public DiscoverRecyclerViewAdapter(String[] list){
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.leyhang_discover_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.TVDiscoverName.setText(list[position]);
    }

    @Override
    public int getItemCount() {
        return list.length;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView TVDiscoverName, TVDiscoverAddress, TVDiscoverOpening;
        Button BtnDiscoverSaved;
        ImageView IVFilterPic;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            TVDiscoverName = itemView.findViewById(R.id.TVDiscoverName);
            TVDiscoverAddress = itemView.findViewById(R.id.TVDiscoverAddress);
            TVDiscoverOpening = itemView.findViewById(R.id.TVDiscoverOpening);
            BtnDiscoverSaved = itemView.findViewById(R.id.BtnDiscoverSaved);
            IVFilterPic = itemView.findViewById(R.id.IVFilterPic);
        }
    }
}
