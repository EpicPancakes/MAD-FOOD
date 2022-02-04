package com.example.onlyfoods;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

//    private Context mContext;
    private ArrayList<Review> list;
    View view;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context mContext = parent.getContext();
        view = LayoutInflater.from(mContext).inflate(R.layout.reviews_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.userName.setText(list.get(position).getName());
        holder.IVProfilePicture.setImageResource(list.get(position).getImage());
        holder.reviewMsg.setText(list.get(position).getReviewMsg());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public ReviewAdapter(ArrayList<Review> list){
        //REMOVED CONTEXT FROM THE PARAMETER OF THE CONSTRCUTOR ABV ^
//        this.mContext =mContext;
        this.list = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView IVProfilePicture, menuPopUp;
        TextView userName, reviewMsg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            IVProfilePicture = (ImageView) itemView.findViewById(R.id.IVProfilePicture);
            menuPopUp = (ImageView) itemView.findViewById(R.id.menuMore);
            userName = (TextView) itemView.findViewById(R.id.userName);
            reviewMsg = (TextView) itemView.findViewById(R.id.TVReview);
        }
    }
}
