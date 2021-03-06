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
import com.example.onlyfoods.DAOs.DAOUser;
import com.example.onlyfoods.Models.ProfileImage;
import com.example.onlyfoods.Models.User;
import com.example.onlyfoods.databinding.FragmentFollowersItemBinding;
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

public class FollowersRecyclerViewAdapter extends RecyclerView.Adapter<FollowersRecyclerViewAdapter.ViewHolder> {

    private OnItemClickListener mListener;
    private final List<User> followers;
    private DAOUser daoUser;
    private String sessionUserKey;
    private DAOProfileImage daoPI;

    public FollowersRecyclerViewAdapter(List<User> followers) {
        this.followers = followers;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        daoUser = new DAOUser();
        daoPI = new DAOProfileImage();
        sessionUserKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
        return new ViewHolder(FragmentFollowersItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        User followerUser = followers.get(position);
        holder.TVFollowerName.setText(followerUser.getUsername());
        holder.TVFollowersNum.setText(String.valueOf(followerUser.getFollowersCount()));
        holder.TVReviewsNum.setText(String.valueOf(followerUser.getReviewsCount()));

        daoPI.getByUserKey(followerUser.getUserKey()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data: snapshot.getChildren()) {
                    if(data.exists()){
                        ProfileImage profileImage = data.getValue(ProfileImage.class);
                        if (profileImage.getProfileImageUrl() != null) {
                            Picasso.get().load(profileImage.getProfileImageUrl()).fit().centerCrop().into(holder.IVFollowerImage);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // check if the follower is the session user itself
        if(sessionUserKey.equals(followerUser.getUserKey())){
            holder.BTNFollowerFollowing.setVisibility(View.VISIBLE);
            holder.BTNFollowerFollow.setVisibility(View.INVISIBLE);
            holder.BTNFollowerFollowing.setEnabled(false);
            holder.BTNFollowerFollowing.setText("You");
        }else {
            // check if session user is currently following viewed user
            daoUser.checkIfFollows(sessionUserKey, followerUser.getUserKey()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        holder.BTNFollowerFollowing.setVisibility(View.VISIBLE);
                        holder.BTNFollowerFollow.setVisibility(View.INVISIBLE);
                    } else {
                        holder.BTNFollowerFollow.setVisibility(View.VISIBLE);
                        holder.BTNFollowerFollowing.setVisibility(View.INVISIBLE);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        // Follows the user upon clicking the button "Follow"
        holder.BTNFollowerFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DAOUser daoUser = new DAOUser();
                daoUser.getByUserKeyOnce(sessionUserKey).addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        User sessionUser = dataSnapshot.getValue(User.class);
                        if (sessionUser != null && followerUser.getUserKey() != null) {
                            sessionUser.setUserKey(dataSnapshot.getKey());

                            Map<String, Object> objectHM2 = new HashMap<>();
                            Map<String, Boolean> booleanHM2;
                            if (followerUser.getFollowers() != null) {
                                booleanHM2 = followerUser.getFollowers();
                            } else {
                                booleanHM2 = new HashMap<>();
                            }
                            booleanHM2.put(sessionUser.getUserKey(), true);
                            objectHM2.put("followers", booleanHM2);
                            daoUser.update(followerUser.getUserKey(), objectHM2).addOnSuccessListener(suc -> {

                                Map<String, Object> objectHM = new HashMap<>();
                                Map<String, Boolean> booleanHM;
                                if (sessionUser.getFollowing() != null) {
                                    booleanHM = sessionUser.getFollowing();
                                } else {
                                    booleanHM = new HashMap<>();
                                }
                                booleanHM.put(followerUser.getUserKey(), true);
                                objectHM.put("following", booleanHM);

                                daoUser.update(sessionUser.getUserKey(), objectHM).addOnSuccessListener(suc2 -> {
                                });
                            }).addOnFailureListener(er ->
                            {
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

        // Unfollows the user upon clicking the button "Following"
        holder.BTNFollowerFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DAOUser daoUser = new DAOUser();
                daoUser.getByUserKeyOnce(sessionUserKey).addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        User sessionUser = dataSnapshot.getValue(User.class);
                        if (sessionUser != null && followerUser.getUserKey() != null) {
                            sessionUser.setUserKey(dataSnapshot.getKey());
                            Map<String, Object> objectHM = new HashMap<>();
                            Map<String, Boolean> booleanHM;
                            if (sessionUser.getFollowing() != null) {
                                booleanHM = sessionUser.getFollowing();
                            } else {
                                booleanHM = new HashMap<>();
                            }
                            booleanHM.remove(followerUser.getUserKey());
                            objectHM.put("following", booleanHM);
                            daoUser.update(sessionUser.getUserKey(), objectHM).addOnSuccessListener(suc -> {

                                Map<String, Object> objectHM2 = new HashMap<>();
                                Map<String, Boolean> booleanHM2;
                                if (followerUser.getFollowers() != null) {
                                    booleanHM2 = followerUser.getFollowers();
                                } else {
                                    booleanHM2 = new HashMap<>();
                                }
                                booleanHM2.remove(sessionUser.getUserKey());
                                objectHM2.put("followers", booleanHM2);
                                daoUser.update(followerUser.getUserKey(), objectHM2).addOnSuccessListener(suc2 -> {
                                });
                            }).addOnFailureListener(er ->
                            {
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
        public final Button BTNFollowerFollowing;
        public final TextView TVFollowersNum;
        public final TextView TVReviewsNum;
        public final ImageView IVFollowerImage;

        public ViewHolder(FragmentFollowersItemBinding binding) {
            super(binding.getRoot());
            TVFollowerName = binding.TVFollowerName;
            BTNFollowerFollow = binding.BTNFollowerFollow;
            BTNFollowerFollowing = binding.BTNFollowerFollowing;
            TVFollowersNum = binding.TVFollowerFollowersNum;
            TVReviewsNum = binding.TVFollowerReviewsNum;
            IVFollowerImage = binding.IVFollowerImage;

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