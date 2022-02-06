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
import com.example.onlyfoods.databinding.LeyhangResultsItemBinding;
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
public class ResultsRecyclerViewAdapter extends RecyclerView.Adapter<ResultsRecyclerViewAdapter.ViewHolder> {

    private OnItemClickListener mListener;
    private final List<Restaurant> restaurants;
    private DAOUser daoUser;
    private String sessionUserKey;


    public ResultsRecyclerViewAdapter(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        daoUser = new DAOUser();
        sessionUserKey = FirebaseAuth.getInstance().getCurrentUser().getUid();

        return new ViewHolder(LeyhangResultsItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Restaurant resultsRestaurant = restaurants.get(position);
        holder.TVResultsName.setText(resultsRestaurant.getRestaurantName());
        holder.TVResultsCategory.setText(resultsRestaurant.getCategory());
        holder.TVResultsLocation.setText(resultsRestaurant.getLocation());

        // check if session user is currently following viewed user
        daoUser.checkIfRestaurantSaved(sessionUserKey, resultsRestaurant.getRestaurantKey()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    holder.IBUnheartR.setVisibility(View.VISIBLE);
                    holder.IBHeartR.setVisibility(View.INVISIBLE);
                } else {
                    holder.IBHeartR.setVisibility(View.VISIBLE);
                    holder.IBUnheartR.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if (resultsRestaurant.getRestaurantImageUrl() != null) {
            Picasso.get().load(resultsRestaurant.getRestaurantImageUrl()).fit().centerCrop().into(holder.IVResultsImage);
        }

        // Saves the restaurant upon clicking the button "Save"
        holder.IBHeartR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                daoUser.getByUserKeyOnce(sessionUserKey).addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        User sessionUser = dataSnapshot.getValue(User.class);
                        if (sessionUser != null && resultsRestaurant.getRestaurantKey() != null) {
                            sessionUser.setUserKey(dataSnapshot.getKey());
                            Map<String, Object> objectHM = new HashMap<>();
                            Map<String, Boolean> booleanHM;
                            if (sessionUser.getSavedRestaurants() != null) {
                                booleanHM = sessionUser.getSavedRestaurants();
                            } else {
                                booleanHM = new HashMap<>();
                            }
                            booleanHM.put(resultsRestaurant.getRestaurantKey(), true);
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
        holder.IBUnheartR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DAOUser daoUser = new DAOUser();
                daoUser.getByUserKeyOnce(sessionUserKey).addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        User sessionUser = dataSnapshot.getValue(User.class);
                        if (sessionUser != null && resultsRestaurant.getRestaurantKey() != null) {
                            sessionUser.setUserKey(dataSnapshot.getKey());
                            Map<String, Object> objectHM = new HashMap<>();
                            Map<String, Boolean> booleanHM;
                            if (sessionUser.getSavedRestaurants() != null) {
                                booleanHM = sessionUser.getSavedRestaurants();
                            } else {
                                booleanHM = new HashMap<>();
                            }
                            booleanHM.remove(resultsRestaurant.getRestaurantKey());
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
        public final TextView TVResultsName;
        public final TextView TVResultsCategory;
        public final TextView TVResultsLocation;
        public final ImageView IVResultsImage;
        public final ImageButton IBHeartR;
        public final ImageButton IBUnheartR;

        public ViewHolder(LeyhangResultsItemBinding binding) {
            super(binding.getRoot());
            TVResultsName = binding.TVResultsName;
            TVResultsCategory = binding.TVResultsCategory;
            TVResultsLocation = binding.TVResultsLocation;
            IVResultsImage = binding.IVResultsImage;
            IBHeartR = binding.BTNHeartR;
            IBUnheartR = binding.BTNUnheartR;

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