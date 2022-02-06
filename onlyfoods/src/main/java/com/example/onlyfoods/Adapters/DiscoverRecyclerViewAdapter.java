package com.example.onlyfoods.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlyfoods.DAOs.DAOUser;
import com.example.onlyfoods.Models.Restaurant;
import com.example.onlyfoods.Models.User;
import com.example.onlyfoods.databinding.LeyhangDiscoverItemBinding;
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

public class DiscoverRecyclerViewAdapter extends RecyclerView.Adapter<DiscoverRecyclerViewAdapter.ViewHolder> {

    private OnItemClickListener mListener;
    private final List<Restaurant> restaurants;
    private DAOUser daoUser;
    private String sessionUserKey;


    public DiscoverRecyclerViewAdapter(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        daoUser = new DAOUser();
        sessionUserKey = FirebaseAuth.getInstance().getCurrentUser().getUid();

        return new ViewHolder(LeyhangDiscoverItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Restaurant discoverRestaurant = restaurants.get(position);
        holder.TVDiscoverName.setText(discoverRestaurant.getRestaurantName());
        holder.TVDiscoverCategory.setText(discoverRestaurant.getCategory());
        holder.TVDiscoverLocation.setText(discoverRestaurant.getLocation());

        // check if session user is currently following viewed user
        daoUser.checkIfRestaurantSaved(sessionUserKey, discoverRestaurant.getRestaurantKey()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    holder.BTNUnheartD.setVisibility(View.VISIBLE);
                    holder.BTNHeartD.setVisibility(View.INVISIBLE);
                } else {
                    holder.BTNHeartD.setVisibility(View.VISIBLE);
                    holder.BTNUnheartD.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if (discoverRestaurant.getRestaurantImageUrl() != null) {
            Picasso.get().load(discoverRestaurant.getRestaurantImageUrl()).fit().centerCrop().into(holder.IVDiscoverImage);
        }

        // Saves the restaurant upon clicking the button "Save"
        holder.BTNHeartD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                daoUser.getByUserKeyOnce(sessionUserKey).addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        User sessionUser = dataSnapshot.getValue(User.class);
                        if (sessionUser != null && discoverRestaurant.getRestaurantKey() != null) {
                            sessionUser.setUserKey(dataSnapshot.getKey());
                            Map<String, Object> objectHM = new HashMap<>();
                            Map<String, Boolean> booleanHM;
                            if (sessionUser.getSavedRestaurants() != null) {
                                booleanHM = sessionUser.getSavedRestaurants();
                            } else {
                                booleanHM = new HashMap<>();
                            }
                            booleanHM.put(discoverRestaurant.getRestaurantKey(), true);
                            objectHM.put("savedRestaurants", booleanHM);
                            daoUser.update(sessionUserKey, objectHM).addOnSuccessListener(suc2 -> {
//                                    Toast.makeText(getContext(), "User followed", Toast.LENGTH_SHORT).show();
                            }).addOnFailureListener(er ->
                            {
//                                Toast.makeText(view.getContext(), "" + er.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        });

        // Unsaves the restaurant upon clicking the button "Unsave"
        holder.BTNUnheartD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DAOUser daoUser = new DAOUser();
                daoUser.getByUserKeyOnce(sessionUserKey).addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        User sessionUser = dataSnapshot.getValue(User.class);
                        if (sessionUser != null && discoverRestaurant.getRestaurantKey() != null) {
                            sessionUser.setUserKey(dataSnapshot.getKey());
                            Map<String, Object> objectHM = new HashMap<>();
                            Map<String, Boolean> booleanHM;
                            if (sessionUser.getSavedRestaurants() != null) {
                                booleanHM = sessionUser.getSavedRestaurants();
                            } else {
                                booleanHM = new HashMap<>();
                            }
                            booleanHM.remove(discoverRestaurant.getRestaurantKey());
                            objectHM.put("savedRestaurants", booleanHM);
                            daoUser.update(sessionUserKey, objectHM).addOnSuccessListener(suc2 -> {
//                                    Toast.makeText(getContext(), "User followed", Toast.LENGTH_SHORT).show();
                            }).addOnFailureListener(er ->
                            {
//                                Toast.makeText(view.getContext(), "" + er.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        });
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
        public final ImageButton BTNHeartD;
        public final ImageButton BTNUnheartD;

        public ViewHolder(LeyhangDiscoverItemBinding binding) {
            super(binding.getRoot());
            TVDiscoverName = binding.TVDiscoverName;
            TVDiscoverCategory = binding.TVDiscoverCategory;
            TVDiscoverLocation = binding.TVDiscoverLocation;
            IVDiscoverImage = binding.IVDiscoverImage;
            BTNHeartD = binding.BTNHeartD;
            BTNUnheartD = binding.BTNUnheartD;

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