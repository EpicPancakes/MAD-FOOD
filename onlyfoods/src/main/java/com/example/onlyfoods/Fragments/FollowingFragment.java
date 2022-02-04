package com.example.onlyfoods.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlyfoods.Adapters.FollowingRecyclerViewAdapter;
import com.example.onlyfoods.DAOs.DAOUser;
import com.example.onlyfoods.Models.User;
import com.example.onlyfoods.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 */
public class FollowingFragment extends Fragment implements FollowingRecyclerViewAdapter.OnItemClickListener {

    // TODO: Customize parameter argument names
    private static final String USER_KEY = "userKey";
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    private User user;
    private DAOUser daoUser;
    ArrayList<User> following = new ArrayList<>();
    private FollowingRecyclerViewAdapter adapter;
    private String viewedUserKey;
    private String sessionUserKey;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FollowingFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static FollowingFragment newInstance(String userKey) {
        FollowingFragment fragment = new FollowingFragment();
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
        View view = inflater.inflate(R.layout.fragment_following_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            adapter = new FollowingRecyclerViewAdapter(following);
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(FollowingFragment.this);
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
        daoUser.getFollowingByUserKey(userKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                following.clear();
                adapter.notifyDataSetChanged();
                for (DataSnapshot data : snapshot.getChildren()) {
                    daoUser.getByUserKeyOnce(data.getKey()).addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot userSnapshot) {
                            User followingUser = userSnapshot.getValue(User.class);
                            followingUser.setUserKey(userSnapshot.getKey());
                            following.add(followingUser);
                            adapter.notifyItemInserted(following.size()-1);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

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
        bundle.putString("userKey", following.get(position).getUserKey());
        Navigation.findNavController(getView()).navigate(R.id.NextToUserProfile, bundle);
        Toast.makeText(getContext(), "Navigating to user", Toast.LENGTH_SHORT).show();
    }
}