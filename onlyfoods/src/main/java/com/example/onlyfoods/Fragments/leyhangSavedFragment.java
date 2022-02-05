package com.example.onlyfoods.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.onlyfoods.Adapters.SavedRestaurantsRecyclerViewAdapter;
import com.example.onlyfoods.DAOs.DAORestaurant;
import com.example.onlyfoods.DAOs.DAOUser;
import com.example.onlyfoods.Models.Restaurant;
import com.example.onlyfoods.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link leyhangSavedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class leyhangSavedFragment extends Fragment implements SavedRestaurantsRecyclerViewAdapter.OnItemClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ArrayList<Restaurant> restaurants = new ArrayList<>();
    private SavedRestaurantsRecyclerViewAdapter adapter;
    private String sessionUserKey;
    private DAOUser daoUser;
    private DAORestaurant daoRest;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public leyhangSavedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment leyhangSavedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static leyhangSavedFragment newInstance(String param1, String param2) {
        leyhangSavedFragment fragment = new leyhangSavedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        sessionUserKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
        daoUser = new DAOUser();
        daoRest = new DAORestaurant();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leyhang_saved, container, false);

        // Lookup the recyclerview in activity layout
        RecyclerView rvSavedRestaurants = (RecyclerView) view.findViewById(R.id.RVSavedRestaurants);
        adapter = new SavedRestaurantsRecyclerViewAdapter(restaurants);
        rvSavedRestaurants.setAdapter(adapter);
        adapter.setOnItemClickListener(leyhangSavedFragment.this);
        loadData();
        rvSavedRestaurants.setLayoutManager(new LinearLayoutManager(this.getContext()));
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


        Button BtnFilterSaved = view.findViewById(R.id.BtnFilterSaved);
        View.OnClickListener OCLFilterSaved = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.DestCategories);
            }
        };
        BtnFilterSaved.setOnClickListener(OCLFilterSaved);

        Button BtnRecommendSaved = view.findViewById(R.id.BtnRecommendSaved);
        View.OnClickListener OCLRecommendSaved = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.DestDiscovery);
            }
        };
        BtnRecommendSaved.setOnClickListener(OCLRecommendSaved);

        Button BtnSavedSaved = view.findViewById(R.id.BtnSavedSaved);
        View.OnClickListener OCLSavedSaved = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.DestSaved);
            }
        };
        BtnSavedSaved.setOnClickListener(OCLSavedSaved);
    }

    private void loadData() {

        daoUser.getSavedRestaurantsByUserKey(sessionUserKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                restaurants.clear();
                adapter.notifyDataSetChanged();
                for (DataSnapshot data : snapshot.getChildren()) {
                    daoRest.getRestaurantsByKey(data.getKey()).addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            Restaurant restaurant = dataSnapshot.getValue(Restaurant.class);
                            restaurant.setRestaurantKey(dataSnapshot.getKey());
                                restaurants.add(restaurant);
                                adapter.notifyItemInserted(restaurants.size()-1);
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
        bundle.putString("restaurantKey", restaurants.get(position).getRestaurantKey());
//        Navigation.findNavController(getView()).navigate(R.id.NextToUserProfile, bundle);
        Toast.makeText(getContext(), "Viewing "+restaurants.get(position).getRestaurantName(), Toast.LENGTH_SHORT).show();
    }
}