package com.example.onlyfoods.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlyfoods.DAOs.DAOUser;
import com.example.onlyfoods.Models.User;
import com.example.onlyfoods.databinding.FragmentFollowersItemBinding;
import com.example.onlyfoods.placeholder.PlaceholderContent.PlaceholderItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class FollowersRecyclerViewAdapter extends RecyclerView.Adapter<FollowersRecyclerViewAdapter.ViewHolder> {

    private OnItemClickListener mListener;
    private final List<User> followers;
    private DAOUser daoUser;

    public FollowersRecyclerViewAdapter(List<User> followers) {
        this.followers = followers;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        daoUser = new DAOUser();
        return new ViewHolder(FragmentFollowersItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        User follower = followers.get(position);
        holder.TVFollowerName.setText(follower.getUsername());
        holder.TVFollowersNum.setText(String.valueOf(follower.getFollowersCount()));
        holder.TVReviewsNum.setText(String.valueOf(follower.getReviewsCount()));

        // check if session user is currently following viewed user
        daoUser.checkIfFollows("-MutmLS6FPIkhneAJSGT", follower.getUserKey()).addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    holder.BTNFollowerFollowing.setVisibility(View.VISIBLE);
                } else {
                    holder.BTNFollowerFollow.setVisibility(View.VISIBLE);
                }
            }
        });

        // Follows the user upon clicking the button "Follow"
        holder.BTNFollowerFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DAOUser daoUser = new DAOUser();
                daoUser.getByUserKeyOnce("-MutmLS6FPIkhneAJSGT").addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        User sessionUser = dataSnapshot.getValue(User.class);
                        if (sessionUser != null && follower.getUserKey() != null) {
                            sessionUser.setUserKey(dataSnapshot.getKey());
                            Map<String, Object> objectHM = new HashMap<>();
                            Map<String, Boolean> booleanHM;
                            if (sessionUser.getFollowing() != null) {
                                booleanHM = sessionUser.getFollowing();
                            } else {
                                booleanHM = new HashMap<>();
                            }
                            booleanHM.put(follower.getUserKey(), true);
                            objectHM.put("following", booleanHM);
                            daoUser.update(sessionUser.getUserKey(), objectHM).addOnSuccessListener(suc -> {

                                Map<String, Object> objectHM2 = new HashMap<>();
                                Map<String, Boolean> booleanHM2;
                                if (follower.getFollowers() != null) {
                                    booleanHM2 = follower.getFollowers();
                                } else {
                                    booleanHM2 = new HashMap<>();
                                }
                                booleanHM2.put(sessionUser.getUserKey(), true);
                                objectHM2.put("followers", booleanHM2);
                                daoUser.update(follower.getUserKey(), objectHM2).addOnSuccessListener(suc2 -> {
                                    holder.BTNFollowerFollowing.setVisibility(View.VISIBLE);
                                    holder.BTNFollowerFollow.setVisibility(View.INVISIBLE);
//                                    Toast.makeText(getContext(), "User followed", Toast.LENGTH_SHORT).show();
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
        holder.BTNFollowerFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DAOUser daoUser = new DAOUser();
                daoUser.getByUserKeyOnce("-MutmLS6FPIkhneAJSGT").addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        User sessionUser = dataSnapshot.getValue(User.class);
                        if (sessionUser != null && follower.getUserKey() != null) {
                            sessionUser.setUserKey(dataSnapshot.getKey());
                            Map<String, Object> objectHM = new HashMap<>();
                            Map<String, Boolean> booleanHM;
                            if (sessionUser.getFollowing() != null) {
                                booleanHM = sessionUser.getFollowing();
                            } else {
                                booleanHM = new HashMap<>();
                            }
                            booleanHM.remove(follower.getUserKey());
                            objectHM.put("following", booleanHM);
                            daoUser.update(sessionUser.getUserKey(), objectHM).addOnSuccessListener(suc -> {

                                Map<String, Object> objectHM2 = new HashMap<>();
                                Map<String, Boolean> booleanHM2;
                                if (follower.getFollowers() != null) {
                                    booleanHM2 = follower.getFollowers();
                                } else {
                                    booleanHM2 = new HashMap<>();
                                }
                                booleanHM2.remove(sessionUser.getUserKey());
                                objectHM2.put("followers", booleanHM2);
                                daoUser.update(follower.getUserKey(), objectHM2).addOnSuccessListener(suc2 -> {
                                    holder.BTNFollowerFollowing.setVisibility(View.INVISIBLE);
                                    holder.BTNFollowerFollow.setVisibility(View.VISIBLE);
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

        public ViewHolder(FragmentFollowersItemBinding binding) {
            super(binding.getRoot());
            TVFollowerName = binding.TVFollowerName;
            BTNFollowerFollow = binding.BTNFollowerFollow;
            BTNFollowerFollowing = binding.BTNFollowerFollowing;
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
            if (mListener != null) {
                int position = getAbsoluteAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);
                }
            }
        }
    }
}