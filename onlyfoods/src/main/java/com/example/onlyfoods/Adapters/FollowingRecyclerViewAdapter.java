package com.example.onlyfoods.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlyfoods.DAOs.DAOUser;
import com.example.onlyfoods.Models.User;
import com.example.onlyfoods.databinding.FragmentFollowingItemBinding;
import com.example.onlyfoods.placeholder.PlaceholderContent.PlaceholderItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class FollowingRecyclerViewAdapter extends RecyclerView.Adapter<FollowingRecyclerViewAdapter.ViewHolder> {

    private OnItemClickListener mListener;
    private final List<User> following;
    private DAOUser daoUser;

    public FollowingRecyclerViewAdapter(List<User> following) {
        this.following = following;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        daoUser = new DAOUser();
        return new ViewHolder(FragmentFollowingItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        User followingUser = following.get(position);
        holder.TVFollowingName.setText(followingUser.getUsername());
        holder.TVFollowersNum.setText(String.valueOf(followingUser.getFollowersCount()));
        holder.TVReviewsNum.setText(String.valueOf(followingUser.getReviewsCount()));

        // check if session user is currently following viewed user
        daoUser.checkIfFollows("-MutmLS6FPIkhneAJSGT", followingUser.getUserKey()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    holder.BTNFollowingFollowing.setVisibility(View.VISIBLE);
                    holder.BTNFollowingFollow.setVisibility(View.INVISIBLE);
                } else {
                    holder.BTNFollowingFollow.setVisibility(View.VISIBLE);
                    holder.BTNFollowingFollowing.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Follows the user upon clicking the button "Follow"
        holder.BTNFollowingFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DAOUser daoUser = new DAOUser();
                daoUser.getByUserKeyOnce("-MutmLS6FPIkhneAJSGT").addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        User sessionUser = dataSnapshot.getValue(User.class);
                        if (sessionUser != null && followingUser.getUserKey() != null) {
                            sessionUser.setUserKey(dataSnapshot.getKey());

                            Map<String, Object> objectHM2 = new HashMap<>();
                            Map<String, Boolean> booleanHM2;
                            if (followingUser.getFollowers() != null) {
                                booleanHM2 = followingUser.getFollowers();
                            } else {
                                booleanHM2 = new HashMap<>();
                            }
                            booleanHM2.put(sessionUser.getUserKey(), true);
                            objectHM2.put("followers", booleanHM2);
                            daoUser.update(followingUser.getUserKey(), objectHM2).addOnSuccessListener(suc -> {

                                Map<String, Object> objectHM = new HashMap<>();
                                Map<String, Boolean> booleanHM;
                                if (sessionUser.getFollowing() != null) {
                                    booleanHM = sessionUser.getFollowing();
                                } else {
                                    booleanHM = new HashMap<>();
                                }
                                booleanHM.put(followingUser.getUserKey(), true);
                                objectHM.put("following", booleanHM);

                                daoUser.update(sessionUser.getUserKey(), objectHM).addOnSuccessListener(suc2 -> {

                                });
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

        // Unfollows the user upon clicking the button "Following"
        holder.BTNFollowingFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DAOUser daoUser = new DAOUser();
                daoUser.getByUserKeyOnce("-MutmLS6FPIkhneAJSGT").addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        User sessionUser = dataSnapshot.getValue(User.class);
                        if (sessionUser != null && followingUser.getUserKey() != null) {
                            sessionUser.setUserKey(dataSnapshot.getKey());
                            Map<String, Object> objectHM = new HashMap<>();
                            Map<String, Boolean> booleanHM;
                            if (sessionUser.getFollowing() != null) {
                                booleanHM = sessionUser.getFollowing();
                            } else {
                                booleanHM = new HashMap<>();
                            }
                            booleanHM.remove(followingUser.getUserKey());
                            objectHM.put("following", booleanHM);
                            daoUser.update(sessionUser.getUserKey(), objectHM).addOnSuccessListener(suc -> {

                                Map<String, Object> objectHM2 = new HashMap<>();
                                Map<String, Boolean> booleanHM2;
                                if (followingUser.getFollowers() != null) {
                                    booleanHM2 = followingUser.getFollowers();
                                } else {
                                    booleanHM2 = new HashMap<>();
                                }
                                booleanHM2.remove(sessionUser.getUserKey());
                                objectHM2.put("followers", booleanHM2);
                                daoUser.update(followingUser.getUserKey(), objectHM2).addOnSuccessListener(suc2 -> {
//                                    holder.BTNFollowingFollowing.setVisibility(View.INVISIBLE);
//                                    holder.BTNFollowingFollow.setVisibility(View.VISIBLE);
//                                    Toast.makeText(view.getContext(), "User unfollowed", Toast.LENGTH_SHORT).show();
                                });
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
        return following.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView TVFollowingName;
        public final Button BTNFollowingFollow;
        public final Button BTNFollowingFollowing;
        public final TextView TVFollowersNum;
        public final TextView TVReviewsNum;

        public ViewHolder(FragmentFollowingItemBinding binding) {
            super(binding.getRoot());
            TVFollowingName = binding.TVFollowingName;
            BTNFollowingFollow = binding.BTNFollowingFollow;
            BTNFollowingFollowing = binding.BTNFollowingFollowing;
            TVFollowersNum = binding.TVFollowingFollowersNum;
            TVReviewsNum = binding.TVFollowingReviewsNum;

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