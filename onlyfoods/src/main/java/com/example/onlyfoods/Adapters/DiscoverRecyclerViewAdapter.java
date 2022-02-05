package com.example.onlyfoods.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlyfoods.DAOs.DAORestaurant;
import com.example.onlyfoods.DAOs.DAOUser;
import com.example.onlyfoods.Models.ProfileImage;
import com.example.onlyfoods.Models.Restaurant;
import com.example.onlyfoods.Models.User;
import com.example.onlyfoods.databinding.LeyhangDiscoverItemBinding;
import com.example.onlyfoods.databinding.LeyhangDiscoverItemBinding;
import com.example.onlyfoods.placeholder.PlaceholderContent.PlaceholderItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class DiscoverRecyclerViewAdapter extends RecyclerView.Adapter<DiscoverRecyclerViewAdapter.ViewHolder> {

    private OnItemClickListener mListener;
    private final List<Restaurant> restaurants;

    public DiscoverRecyclerViewAdapter(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LeyhangDiscoverItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Restaurant discoverRestaurant = restaurants.get(position);
        holder.TVDiscoverName.setText(discoverRestaurant.getRestaurantName());
        holder.TVDiscoverCategory.setText(discoverRestaurant.getCategory());
        holder.TVDiscoverLocation.setText(discoverRestaurant.getLocation());

        if (discoverRestaurant.getRestaurantImageUrl() != null) {
            Picasso.get().load(discoverRestaurant.getRestaurantImageUrl()).fit().centerCrop().into(holder.IVDiscoverImage);
        }
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView TVDiscoverName;
        public final TextView TVDiscoverCategory;
        public final TextView TVDiscoverLocation;
        public final ImageView IVDiscoverImage;

        public ViewHolder(LeyhangDiscoverItemBinding binding) {
            super(binding.getRoot());
            TVDiscoverName = binding.TVDiscoverName;
            TVDiscoverCategory = binding.TVDiscoverCategory;
            TVDiscoverLocation = binding.TVDiscoverLocation;
            IVDiscoverImage = binding.IVDiscoverImage;

            itemView.setOnClickListener(this);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + "'";
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                int position = getAbsoluteAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);
                }
            }
        }
    }
}