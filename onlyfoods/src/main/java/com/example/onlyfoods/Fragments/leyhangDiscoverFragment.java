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

import com.example.onlyfoods.Adapters.DiscoverRecyclerViewAdapter;
import com.example.onlyfoods.DAOs.DAORestaurant;
import com.example.onlyfoods.DAOs.DAOUser;
import com.example.onlyfoods.Models.Restaurant;
import com.example.onlyfoods.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link leyhangDiscoverFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class leyhangDiscoverFragment extends Fragment implements DiscoverRecyclerViewAdapter.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ArrayList<Restaurant> restaurants = new ArrayList<>();
    private DiscoverRecyclerViewAdapter adapter;
    private String sessionUserKey;
    private DAOUser daoUser;
    private DAORestaurant daoRest;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public leyhangDiscoverFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment leyhangDiscoverFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static leyhangDiscoverFragment newInstance(String param1, String param2) {
        leyhangDiscoverFragment fragment = new leyhangDiscoverFragment();
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
        View view = inflater.inflate(R.layout.fragment_leyhang_discover, container, false);

        // Lookup the recyclerview in activity layout
        RecyclerView rvDiscoverRestaurants = (RecyclerView) view.findViewById(R.id.RVDiscoverRestaurants);
        adapter = new DiscoverRecyclerViewAdapter(restaurants);
        rvDiscoverRestaurants.setAdapter(adapter);
        adapter.setOnItemClickListener(leyhangDiscoverFragment.this);
        loadData();
        rvDiscoverRestaurants.setLayoutManager(new LinearLayoutManager(this.getContext()));
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


        Button BtnFilterDiscover = view.findViewById(R.id.BtnFilterDiscover);
        View.OnClickListener OCLFilterDiscover = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.DestCategories);
            }
        };
        BtnFilterDiscover.setOnClickListener(OCLFilterDiscover);

        Button BtnRecommendDiscover = view.findViewById(R.id.BtnRecommendDiscover);
        View.OnClickListener OCLRecommendDiscover = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.DestDiscovery);
            }
        };
        BtnRecommendDiscover.setOnClickListener(OCLRecommendDiscover);

        Button BtnDiscoverDiscover = view.findViewById(R.id.BtnSavedDiscover);
        View.OnClickListener OCLSavedDiscover = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.DestSaved);
            }
        };
        BtnDiscoverDiscover.setOnClickListener(OCLSavedDiscover);
    }

    private void loadData() {

        daoRest.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                for (DataSnapshot data: dataSnapshot.getChildren()) {
                    Restaurant restaurant = data.getValue(Restaurant.class);
                    restaurant.setRestaurantKey(data.getKey());
                    restaurants.add(restaurant);
                    adapter.notifyItemInserted(restaurants.size() - 1);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    @Override
    public void onItemClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("restaurantKey", restaurants.get(position).getRestaurantKey());
//        Navigation.findNavController(getView()).navigate(R.id.NextToUserProfile, bundle);
        Toast.makeText(getContext(), "Viewing " + restaurants.get(position).getRestaurantName(), Toast.LENGTH_SHORT).show();
    }
}