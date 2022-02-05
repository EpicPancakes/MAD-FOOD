package com.example.onlyfoods.Fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlyfoods.Adapters.FollowersRecyclerViewAdapter;
import com.example.onlyfoods.DAOs.DAOUser;
import com.example.onlyfoods.Models.User;
import com.example.onlyfoods.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 */
public class FollowersFragment extends Fragment implements FollowersRecyclerViewAdapter.OnItemClickListener {

    // TODO: Customize parameter argument names
    private static final String USER_KEY = "userKey";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    private User user;
    private DAOUser daoUser;
    ArrayList<User> followers = new ArrayList<>();
    private FollowersRecyclerViewAdapter adapter;
    private String viewedUserKey;
    private String sessionUserKey;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FollowersFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static FollowersFragment newInstance(String userKey) {
        FollowersFragment fragment = new FollowersFragment();
        Bundle args = new Bundle();
        args.putString(USER_KEY, userKey);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            viewedUserKey = getArguments().getString(USER_KEY);
        }
        sessionUserKey = FirebaseAuth.getInstance().getCurrentUser().getUid();

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
            adapter.setOnItemClickListener(FollowersFragment.this);
        }

        daoUser = new DAOUser();
        loadData();
        return view;
    }

    private void loadData() {
        // TODO: Replace userKey with the current user in session
        String userKey;
        if(viewedUserKey != null){
            userKey = viewedUserKey;
        }else{
            userKey = sessionUserKey;
        }
        daoUser.getFollowersByUserKey(userKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                followers.clear();
                adapter.notifyDataSetChanged();
                for (DataSnapshot data : snapshot.getChildren()) {
                    daoUser.getByUserKey(data.getKey()).addValueEventListener(new ValueEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                            User follower = userSnapshot.getValue(User.class);
                            follower.setUserKey(userSnapshot.getKey());
                            if(followers.stream().noneMatch(o -> o.getUserKey().equals(follower.getUserKey()))){
                                followers.add(follower);
                                adapter.notifyItemInserted(followers.size()-1);
                            }else{
                                followers.stream().filter(o -> o.getUserKey().equals(follower.getUserKey())).forEach(
                                        o -> {
                                            int position = followers.indexOf(o);
                                            followers.set(position, follower);
                                            adapter.notifyItemChanged(position);
                                        }
                                );
                            }
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

    @Override
    public void onItemClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("userKey", followers.get(position).getUserKey());
        Navigation.findNavController(getView()).navigate(R.id.NextToUserProfile, bundle);
        Toast.makeText(getContext(), "Navigating to user", Toast.LENGTH_SHORT).show();
    }
}