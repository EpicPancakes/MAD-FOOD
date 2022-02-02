package com.example.onlyfoods.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlyfoods.Adapters.FollowersRecyclerViewAdapter;
import com.example.onlyfoods.DAOs.DAOUser;
import com.example.onlyfoods.Models.RecentPlace;
import com.example.onlyfoods.Models.User;
import com.example.onlyfoods.R;
import com.example.onlyfoods.placeholder.PlaceholderContent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 */
public class FollowersFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    private User user;
    private DAOUser daoUser;
    ArrayList<User> followers = new ArrayList<>();
    private FollowersRecyclerViewAdapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FollowersFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static FollowersFragment newInstance(int columnCount) {
        FollowersFragment fragment = new FollowersFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_followers_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            adapter = new FollowersRecyclerViewAdapter(followers);
            recyclerView.setAdapter(adapter);
        }

        daoUser = new DAOUser();
        loadData();
        return view;
    }

    private void loadData() {
        // TODO: Replace userKey with the current user in session
        daoUser.getFollowersByUserKey("-MutmLS6FPIkhneAJSGT").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                followers.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    daoUser.getByUserKey(data.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                            User follower = userSnapshot.getValue(User.class);
                            follower.setUserKey(userSnapshot.getKey());
                            followers.add(follower);
                            adapter.notifyItemInserted(followers.size()-1);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}