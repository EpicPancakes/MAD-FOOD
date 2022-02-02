package com.example.onlyfoods.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.onlyfoods.Models.User;
import com.example.onlyfoods.databinding.FragmentFollowersItemBinding;
import com.example.onlyfoods.placeholder.PlaceholderContent.PlaceholderItem;

import org.w3c.dom.Text;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class FollowersRecyclerViewAdapter extends RecyclerView.Adapter<FollowersRecyclerViewAdapter.ViewHolder> {

    private OnItemClickListener mListener;
    private final List<User> followers;

    public FollowersRecyclerViewAdapter(List<User> followers) {
        this.followers = followers;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentFollowersItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        User follower = followers.get(position);
        holder.TVFollowerName.setText(follower.getUsername());
        holder.TVFollowersNum.setText(String.valueOf(follower.getFollowersCount()));
        holder.TVReviewsNum.setText(String.valueOf(follower.getReviewsCount()));
    }

    @Override
    public int getItemCount() {
        return followers.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView TVFollowerName;
        public final Button BTNFollowerFollow;
        public final TextView TVFollowersNum;
        public final TextView TVReviewsNum;

        public ViewHolder(FragmentFollowersItemBinding binding) {
            super(binding.getRoot());
            TVFollowerName = binding.TVFollowerName;
            BTNFollowerFollow = binding.BtnFollowerFollow;
            TVFollowersNum = binding.TVFollowersNum;
            TVReviewsNum = binding.TVReviewsNum;

            itemView.setOnClickListener(this);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + "'";
        }

        @Override
        public void onClick(View v) {
            if(mListener != null) {
                int position = getAbsoluteAdapterPosition();
                if(position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);
                }
            }
        }
    }
}