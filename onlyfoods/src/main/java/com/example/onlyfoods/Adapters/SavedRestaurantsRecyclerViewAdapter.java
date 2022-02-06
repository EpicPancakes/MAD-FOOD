package com.example.onlyfoods.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlyfoods.DAOs.DAORestaurant;
import com.example.onlyfoods.DAOs.DAOUser;
import com.example.onlyfoods.Models.ProfileImage;
import com.example.onlyfoods.Models.Restaurant;
import com.example.onlyfoods.Models.User;
import com.example.onlyfoods.databinding.LeyhangSavedItemBinding;
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
public class SavedRestaurantsRecyclerViewAdapter extends RecyclerView.Adapter<SavedRestaurantsRecyclerViewAdapter.ViewHolder> {

    private OnItemClickListener mListener;
    private final List<Restaurant> restaurants;
    private String sessionUserKey;


    public SavedRestaurantsRecyclerViewAdapter(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        sessionUserKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
        return new ViewHolder(LeyhangSavedItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Restaurant savedRestaurant = restaurants.get(position);
        holder.TVSavedName.setText(savedRestaurant.getRestaurantName());
        holder.TVSavedCategory.setText(savedRestaurant.getCategory());
        holder.TVSavedLocation.setText(savedRestaurant.getLocation());

        if (savedRestaurant.getRestaurantImageUrl() != null) {
            Picasso.get().load(savedRestaurant.getRestaurantImageUrl()).fit().centerCrop().into(holder.IVSavedImage);
        }

        // Unsaves the restaurant upon clicking the button "Unsave"
        holder.IBUnheartS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DAOUser daoUser = new DAOUser();
                daoUser.getByUserKeyOnce(sessionUserKey).addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        User sessionUser = dataSnapshot.getValue(User.class);
                        if (sessionUser != null && savedRestaurant.getRestaurantKey() != null) {
                            sessionUser.setUserKey(dataSnapshot.getKey());
                            Map<String, Object> objectHM = new HashMap<>();
                            Map<String, Boolean> booleanHM;
                            if (sessionUser.getSavedRestaurants() != null) {
                                booleanHM = sessionUser.getSavedRestaurants();
                            } else {
                                booleanHM = new HashMap<>();
                            }
                            booleanHM.remove(savedRestaurant.getRestaurantKey());
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
        public final TextView TVSavedName;
        public final TextView TVSavedCategory;
        public final TextView TVSavedLocation;
        public final ImageView IVSavedImage;
        public final ImageButton IBUnheartS;

        public ViewHolder(LeyhangSavedItemBinding binding) {
            super(binding.getRoot());
            TVSavedName = binding.TVSavedName;
            TVSavedCategory = binding.TVSavedCatagory;
            TVSavedLocation = binding.TVSavedLocation;
            IVSavedImage = binding.IVSavedImage;
            IBUnheartS = binding.IBUnheartS;
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