package com.example.onlyfoods.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.onlyfoods.DAOs.DAORecentPlace;
import com.example.onlyfoods.Models.RecentPlace;
import com.example.onlyfoods.Adapters.MyRecentPlacesRecyclerViewAdapter;
import com.example.onlyfoods.R;
import com.example.onlyfoods.placeholder.PlaceholderContent;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 */
public class RecentPlacesFragment extends Fragment implements MyRecentPlacesRecyclerViewAdapter.OnItemClickListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    DAORecentPlace daoRP;
    MyRecentPlacesRecyclerViewAdapter adapter;
    ArrayList<RecentPlace> rps = new ArrayList<>();


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecentPlacesFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static RecentPlacesFragment newInstance(int columnCount) {
        RecentPlacesFragment fragment = new RecentPlacesFragment();
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
            adapter = new MyRecentPlacesRecyclerViewAdapter(getContext(), PlaceholderContent.ITEMS, rps);
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(RecentPlacesFragment.this);
        }

        daoRP = new DAORecentPlace();
        loadData();

        return view;
    }

    private void loadData() {
        // TODO: Replace testUser with the userKey obtained from User
        daoRP.getByUserKey("testUser").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                rps.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    RecentPlace rp = data.getValue(RecentPlace.class);
                    rp.setRecentPlaceKey(data.getKey());
                    rps.add(rp);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onItemClick(int position) {
//        Toast.makeText(getContext(), "Normal click at position: " + position, Toast.LENGTH_SHORT).show();
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
}