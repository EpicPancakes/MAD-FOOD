package com.example.onlyfoods.Fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.onlyfoods.Adapters.DiscoverRecyclerViewAdapter;
import com.example.onlyfoods.DAOs.DAORestaurant;
import com.example.onlyfoods.DAOs.DAOUser;
import com.example.onlyfoods.Models.Restaurant;
import com.example.onlyfoods.Models.User;
import com.example.onlyfoods.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    private User user;
    private String recentCategory;

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

        daoUser.getByUserKeyOnce(sessionUserKey).addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                if(user.getRecentCategory() != null){
                    recentCategory = user.getRecentCategory();
                }
                if(recentCategory!=null){
                    loadDataByRecentCategory();
                }else{
                    loadData();
                }
            }
        }).addOnFailureListener(er->{
            Toast.makeText(getView().getContext(), "Unable to retrieve user data.", Toast.LENGTH_SHORT).show();
        });
        rvDiscoverRestaurants.setLayoutManager(new LinearLayoutManager(this.getContext()));
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        SearchView SVDiscover = (SearchView) view.findViewById(R.id.SVDiscover);
        SVDiscover.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {
//                loadDataFromQuery(newText);
                //Log.e("onQueryTextChange", "==called");
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                loadDataFromQuery(query);
                // Do something
                return false;
            }

        });

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
                restaurants.clear();
                adapter.notifyDataSetChanged();
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
                Toast.makeText(getView().getContext(), "Database unavailable at the moment, please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadDataByRecentCategory() {
        daoRest.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                restaurants.clear();
                adapter.notifyDataSetChanged();
                for (DataSnapshot data: dataSnapshot.getChildren()) {
                    Restaurant restaurant = data.getValue(Restaurant.class);
                    restaurant.setRestaurantKey(data.getKey());
                    restaurants.add(restaurant);
                }
                ArrayList<Restaurant> toMove = new ArrayList<>();
                restaurants.stream().filter(r -> r.getCategory().equals(recentCategory)).forEach(
                        o -> {
//                            int position = restaurants.indexOf(o);
                            toMove.add(o);
//                            restaurants.remove(position);
//                            restaurants.add(0, o);
                        }
                );
                restaurants.removeAll(toMove);
                restaurants.addAll(0, toMove);
                adapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getView().getContext(), "Database unavailable at the moment, please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadDataFromQuery(String query) {

        daoRest.getRestaurantsByQuery(query).addValueEventListener(new ValueEventListener() {
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
                Toast.makeText(getView().getContext(), "Database unavailable at the moment, please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("restaurantKey", restaurants.get(position).getRestaurantKey());
//        Navigation.findNavController(getView()).navigate(R.id.NextToUserProfile, bundle);
        String category = restaurants.get(position).getCategory();
        Map<String, Object> stringHM = new HashMap<>();
        stringHM.put("recentCategory", category);
        daoUser.update(sessionUserKey, stringHM).addOnSuccessListener(suc->{

        }).addOnFailureListener(er->{

        });

    }
}