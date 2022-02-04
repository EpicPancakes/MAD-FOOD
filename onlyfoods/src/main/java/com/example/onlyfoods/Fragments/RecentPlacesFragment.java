package com.example.onlyfoods.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlyfoods.Adapters.MyRecentPlacesRecyclerViewAdapter;
import com.example.onlyfoods.Adapters.UserRecentPlacesRecyclerViewAdapter;
import com.example.onlyfoods.DAOs.DAORecentPlace;
import com.example.onlyfoods.DAOs.DAOUser;
import com.example.onlyfoods.Models.RecentPlace;
import com.example.onlyfoods.Models.User;
import com.example.onlyfoods.R;
import com.example.onlyfoods.placeholder.PlaceholderContent;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A fragment representing a list of Items.
 */
public class RecentPlacesFragment extends Fragment implements MyRecentPlacesRecyclerViewAdapter.OnItemClickListener, UserRecentPlacesRecyclerViewAdapter.OnItemClickListener  {

    private static final String VIEWED_USER_KEY = "viewedUserKey";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    DAORecentPlace daoRP;
    MyRecentPlacesRecyclerViewAdapter myRPAdapter;
    UserRecentPlacesRecyclerViewAdapter userRPAdapter;
    ArrayList<RecentPlace> rps = new ArrayList<>();
    private User user;
    private String viewedUserKey;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecentPlacesFragment() {
    }

    public static RecentPlacesFragment newInstance(String viewedUserKey) {
        RecentPlacesFragment fragment = new RecentPlacesFragment();
        Bundle args = new Bundle();
        args.putString(VIEWED_USER_KEY, viewedUserKey);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            viewedUserKey = getArguments().getString(VIEWED_USER_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recent_places_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            if(viewedUserKey!= null){
                userRPAdapter = new UserRecentPlacesRecyclerViewAdapter(getContext(), rps);
                recyclerView.setAdapter(userRPAdapter);
                userRPAdapter.setOnItemClickListener(RecentPlacesFragment.this);
            }else{
                myRPAdapter = new MyRecentPlacesRecyclerViewAdapter(getContext(), rps);
                recyclerView.setAdapter(myRPAdapter);
                myRPAdapter.setOnItemClickListener(RecentPlacesFragment.this);
            }
        }

        daoRP = new DAORecentPlace();
        loadData();

        return view;
    }

    private void loadData() {
        // TODO: Replace userKey with the session key obtained from User
        String rpUserKey;
        if(viewedUserKey != null){
            rpUserKey = viewedUserKey;
        }else{
            rpUserKey = "-MutmLS6FPIkhneAJSGT";
        }

        daoRP.getByUserKey(rpUserKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                rps.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    RecentPlace rp = data.getValue(RecentPlace.class);
                    rp.setRecentPlaceKey(data.getKey());
                    rps.add(rp);
                }
                if(viewedUserKey!=null){
                    userRPAdapter.notifyDataSetChanged();
                }else{
                    myRPAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(getContext(), "Navigating to restaurant" + position, Toast.LENGTH_SHORT).show();
    }

//    @Override
//    public void onEditClick(int position) {
//        Toast.makeText(getContext(), "Edit click at position: " + position, Toast.LENGTH_SHORT).show();
//    }

    @Override
    public void onDeleteClick(int position) {
        deleteFile(position);
    }

    private void deleteFile(int position) {
        RecentPlace selectedItem = rps.get(position);
        if (selectedItem != null) {
            final String selectedRecentPlaceKey = selectedItem.getRecentPlaceKey();
            daoRP.remove(selectedRecentPlaceKey).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    deleteRPinUser(selectedRecentPlaceKey);
                    Toast.makeText(getContext(), "Removed recent place", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void deleteRPinUser(String rpKey){
        // Include recent place key in user's recent places list by updating user
        DAOUser daoUser = new DAOUser();
        daoUser.getByUserKey("-MutmLS6FPIkhneAJSGT").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                user.setUserKey(snapshot.getKey());
                if (user != null) {
                    Map<String, Object> objectHM = new HashMap<>();
                    Map<String, Boolean> booleanHM;
                    if(user.getRecentPlaces()!=null){
                        booleanHM = user.getRecentPlaces();
                    }else{
                        booleanHM = new HashMap<>();
                    }
                    booleanHM.remove(rpKey);
                    objectHM.put("recentPlaces", booleanHM);
                    daoUser.update(user.getUserKey(), objectHM).addOnSuccessListener(suc -> {
                        Toast.makeText(getContext(), "Record is updated", Toast.LENGTH_SHORT).show();
                    }).addOnFailureListener(er ->
                    {
                        Toast.makeText(getContext(), "" + er.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}