package com.example.onlyfoods.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlyfoods.Adapters.ResultsRecyclerViewAdapter;
import com.example.onlyfoods.Adapters.ResultsRecyclerViewAdapter;
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
 * Use the {@link leyhangResultsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class leyhangResultsFragment extends Fragment implements ResultsRecyclerViewAdapter.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ArrayList<Restaurant> restaurants = new ArrayList<>();
    private ResultsRecyclerViewAdapter adapter;
    private String sessionUserKey;
    private DAOUser daoUser;
    private DAORestaurant daoRest;
    private String categoryString;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public leyhangResultsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment leyhangResultsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static leyhangResultsFragment newInstance(String param1, String param2) {
        leyhangResultsFragment fragment = new leyhangResultsFragment();
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
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            categoryString = bundle.getString("categoryString");
        }

        sessionUserKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
        daoUser = new DAOUser();
        daoRest = new DAORestaurant();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leyhang_results, container, false);

        // Lookup the recyclerview in activity layout
        RecyclerView rvResultsRestaurants = (RecyclerView) view.findViewById(R.id.RVResultsRestaurants);
        adapter = new ResultsRecyclerViewAdapter(restaurants);
        rvResultsRestaurants.setAdapter(adapter);
        adapter.setOnItemClickListener(leyhangResultsFragment.this);
        if(categoryString != null){
            loadData();
        }
        rvResultsRestaurants.setLayoutManager(new LinearLayoutManager(this.getContext()));
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


        Button BtnFilterResults = view.findViewById(R.id.BtnFilterResults);
        View.OnClickListener OCLFilterResults = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.DestCategories);
            }
        };
        BtnFilterResults.setOnClickListener(OCLFilterResults);

        Button BtnRecommendResults = view.findViewById(R.id.BtnRecommendResults);
        View.OnClickListener OCLRecommendResults = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.DestDiscovery);
            }
        };
        BtnRecommendResults.setOnClickListener(OCLRecommendResults);

        Button BtnSavedResults = view.findViewById(R.id.BtnSavedResults);
        View.OnClickListener OCLSavedResults = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.DestSaved);
            }
        };
        BtnSavedResults.setOnClickListener(OCLSavedResults);
    }

    private void loadData() {

        daoRest.getRestaurantByCategory(categoryString).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                restaurants.clear();
                adapter.notifyDataSetChanged();
                for (DataSnapshot data: snapshot.getChildren()) {
                    Restaurant restaurant = data.getValue(Restaurant.class);
                    restaurant.setRestaurantKey(data.getKey());
                    restaurants.add(restaurant);
                    adapter.notifyItemInserted(restaurants.size() - 1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getView().getContext(), ""+error, Toast.LENGTH_SHORT).show();
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