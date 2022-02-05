package com.example.onlyfoods.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlyfoods.DAOs.DAOProfileImage;
import com.example.onlyfoods.DAOs.DAORestaurant;
import com.example.onlyfoods.DAOs.DAOUser;
import com.example.onlyfoods.Models.ProfileImage;
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
    private final List<User> followers;
    private DAOUser daoUser;
    private String sessionUserKey;
    private DAORestaurant daoRest;

    public SavedRestaurantsRecyclerViewAdapter(List<User> followers) {
        this.followers = followers;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        daoUser = new DAOUser();
        daoRest = new DAORestaurant();
        sessionUserKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
        return new ViewHolder(LeyhangSavedItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
//        User followerUser = followers.get(position);
//        holder.TVSavedRestaurantName.setText(followerUser.getUsername());
//        holder.TVSavedRestaurantsNum.setText(String.valueOf(followerUser.getSavedRestaurantsCount()));
//        holder.TVReviewsNum.setText(String.valueOf(followerUser.getReviewsCount()));
//
//        daoRest.getByUserKey(followerUser.getUserKey()).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot data: snapshot.getChildren()) {
//                    if(data.exists()){
//                        ProfileImage profileImage = data.getValue(ProfileImage.class);
//                        if (profileImage.getProfileImageUrl() != null) {
//                            Picasso.get().load(profileImage.getProfileImageUrl()).fit().centerCrop().into(holder.IVSavedRestaurantImage);
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//        // check if the follower is the session user itself
//        if(sessionUserKey.equals(followerUser.getUserKey())){
//            holder.BTNSavedRestaurantFollowing.setVisibility(View.VISIBLE);
//            holder.BTNSavedRestaurantFollow.setVisibility(View.INVISIBLE);
//            holder.BTNSavedRestaurantFollowing.setEnabled(false);
//            holder.BTNSavedRestaurantFollowing.setText("Disabled");
//        }else {
//            // check if session user is currently following viewed user
//            daoUser.checkIfFollows(sessionUserKey, followerUser.getUserKey()).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if (snapshot.exists()) {
//                        holder.BTNSavedRestaurantFollowing.setVisibility(View.VISIBLE);
//                        holder.BTNSavedRestaurantFollow.setVisibility(View.INVISIBLE);
//                    } else {
//                        holder.BTNSavedRestaurantFollow.setVisibility(View.VISIBLE);
//                        holder.BTNSavedRestaurantFollowing.setVisibility(View.INVISIBLE);
//                    }
//                }
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//        }
//
//        // Follows the user upon clicking the button "Follow"
//        holder.BTNSavedRestaurantFollow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DAOUser daoUser = new DAOUser();
//                daoUser.getByUserKeyOnce(sessionUserKey).addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
//                    @Override
//                    public void onSuccess(DataSnapshot dataSnapshot) {
//                        User sessionUser = dataSnapshot.getValue(User.class);
//                        if (sessionUser != null && followerUser.getUserKey() != null) {
//                            sessionUser.setUserKey(dataSnapshot.getKey());
//
//                            Map<String, Object> objectHM2 = new HashMap<>();
//                            Map<String, Boolean> booleanHM2;
//                            if (followerUser.getSavedRestaurants() != null) {
//                                booleanHM2 = followerUser.getSavedRestaurants();
//                            } else {
//                                booleanHM2 = new HashMap<>();
//                            }
//                            booleanHM2.put(sessionUser.getUserKey(), true);
//                            objectHM2.put("followers", booleanHM2);
//                            daoUser.update(followerUser.getUserKey(), objectHM2).addOnSuccessListener(suc -> {
//
//                                Map<String, Object> objectHM = new HashMap<>();
//                                Map<String, Boolean> booleanHM;
//                                if (sessionUser.getFollowing() != null) {
//                                    booleanHM = sessionUser.getFollowing();
//                                } else {
//                                    booleanHM = new HashMap<>();
//                                }
//                                booleanHM.put(followerUser.getUserKey(), true);
//                                objectHM.put("following", booleanHM);
//
//                                daoUser.update(sessionUser.getUserKey(), objectHM).addOnSuccessListener(suc2 -> {
////                                    holder.BTNSavedRestaurantFollowing.setVisibility(View.VISIBLE);
////                                    holder.BTNSavedRestaurantFollow.setVisibility(View.INVISIBLE);
////                                    Toast.makeText(getContext(), "User followed", Toast.LENGTH_SHORT).show();
//                                });
//                            }).addOnFailureListener(er ->
//                            {
////                                Toast.makeText(view.getContext(), "" + er.getMessage(), Toast.LENGTH_SHORT).show();
//                            });
//                        }
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//
//                    }
//                });
//            }
//        });
//
//        // Unfollows the user upon clicking the button "Following"
//        holder.BTNSavedRestaurantFollowing.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DAOUser daoUser = new DAOUser();
//                daoUser.getByUserKeyOnce(sessionUserKey).addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
//                    @Override
//                    public void onSuccess(DataSnapshot dataSnapshot) {
//                        User sessionUser = dataSnapshot.getValue(User.class);
//                        if (sessionUser != null && followerUser.getUserKey() != null) {
//                            sessionUser.setUserKey(dataSnapshot.getKey());
//                            Map<String, Object> objectHM = new HashMap<>();
//                            Map<String, Boolean> booleanHM;
//                            if (sessionUser.getFollowing() != null) {
//                                booleanHM = sessionUser.getFollowing();
//                            } else {
//                                booleanHM = new HashMap<>();
//                            }
//                            booleanHM.remove(followerUser.getUserKey());
//                            objectHM.put("following", booleanHM);
//                            daoUser.update(sessionUser.getUserKey(), objectHM).addOnSuccessListener(suc -> {
//
//                                Map<String, Object> objectHM2 = new HashMap<>();
//                                Map<String, Boolean> booleanHM2;
//                                if (followerUser.getSavedRestaurants() != null) {
//                                    booleanHM2 = followerUser.getSavedRestaurants();
//                                } else {
//                                    booleanHM2 = new HashMap<>();
//                                }
//                                booleanHM2.remove(sessionUser.getUserKey());
//                                objectHM2.put("followers", booleanHM2);
//                                daoUser.update(followerUser.getUserKey(), objectHM2).addOnSuccessListener(suc2 -> {
////                                    holder.BTNSavedRestaurantFollowing.setVisibility(View.INVISIBLE);
////                                    holder.BTNSavedRestaurantFollow.setVisibility(View.VISIBLE);
////                                    Toast.makeText(view.getContext(), "User unfollowed", Toast.LENGTH_SHORT).show();
//                                });
//                            }).addOnFailureListener(er ->
//                            {
////                                Toast.makeText(view.getContext(), "" + er.getMessage(), Toast.LENGTH_SHORT).show();
//                            });
//                        }
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//
//                    }
//                });
//            }
//        });
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
//        public final TextView TVSavedRestaurantName;
//        public final Button BTNSavedRestaurantFollow;
//        public final Button BTNSavedRestaurantFollowing;
//        public final TextView TVSavedRestaurantsNum;
//        public final TextView TVReviewsNum;
//        public final ImageView IVSavedRestaurantImage;

        public ViewHolder(LeyhangSavedItemBinding binding) {
            super(binding.getRoot());
//            TVSavedRestaurantName = binding.TVSavedRestaurantName;
//            BTNSavedRestaurantFollow = binding.BTNSavedRestaurantFollow;
//            BTNSavedRestaurantFollowing = binding.BTNSavedRestaurantFollowing;
//            TVSavedRestaurantsNum = binding.TVSavedRestaurantSavedRestaurantsNum;
//            TVReviewsNum = binding.TVSavedRestaurantReviewsNum;
//            IVSavedRestaurantImage = binding.IVSavedRestaurantImage;

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