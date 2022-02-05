package com.example.onlyfoods;

import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlyfoods.DAOs.DAORestaurant;
import com.example.onlyfoods.DAOs.DAOReview;
import com.example.onlyfoods.Models.Recommendation;
import com.example.onlyfoods.Models.Restaurant;
import com.example.onlyfoods.Models.Review;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link gpRestaurantFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class gpRestaurantFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

//    RecyclerView recyclerView;
    ArrayList<Review> lists = new ArrayList<>();
    private String restaurantKey;
    ReviewAdapter adapter;
    DAORestaurant daoRest;

    public gpRestaurantFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment gpRestaurantFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static gpRestaurantFragment newInstance(String param1, String param2) {
        gpRestaurantFragment fragment = new gpRestaurantFragment();
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
            restaurantKey = getArguments().getString("restaurantKey");
        }
        daoRest = new DAORestaurant();
        restaurantKey = "-MukspRMoaeV7w04xhk5";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

//        return inflater.inflate(R.layout.fragment_gp_restaurant, container, false);
        View view = inflater.inflate(R.layout.fragment_gp_restaurant, container, false);
        addFragment(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView restaurantName = (TextView) view.findViewById(R.id.restNameId);
        TextView restaurantCat = (TextView) view.findViewById(R.id.restCategoryId);
        ImageView restaurantImg = (ImageView) view.findViewById(R.id.restImageId);

        daoRest.get(restaurantKey).addOnSuccessListener(suc ->{
            Restaurant restaurant = suc.getValue(Restaurant.class);
            assert restaurant != null;
            restaurantName.setText(restaurant.getRestaurantName());
            restaurantCat.setText(restaurant.getCategory());
            if (restaurant.getRestaurantImageUrl() != null) {
                Picasso.get().load(restaurant.getRestaurantImageUrl()).fit().centerCrop().into(restaurantImg);
            }
        }).addOnFailureListener(er ->{
            Toast.makeText(restaurantName.getContext(), "" + er.getMessage(), Toast.LENGTH_SHORT).show();
        });

        Button add = (Button) view.findViewById(R.id.addButton);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("restaurantKey", restaurantKey);
                Navigation.findNavController(view).navigate(R.id.DestAddReview, args);
            }
        });

    }

    //recommendations : list //Recommendation: Review
    private void addFragment(View view){

        // Lookup the recyclerview in activity layout
        RecyclerView rvReview = (RecyclerView) view.findViewById(R.id.reviewRecycler);

//        // Initialize recommendations
//        recommendations = Recommendation.createRecommendationsList(20);

        // Create adapter passing in the sample user data
        adapter = new ReviewAdapter(lists);
        rvReview.setAdapter(adapter);
        rvReview.setItemAnimator(new DefaultItemAnimator());
        rvReview.setLayoutManager(new LinearLayoutManager(this.getContext(), RecyclerView.VERTICAL, false));
        if(restaurantKey != null){
            loadData();
        }

    }

    private void loadData() {
        DAOReview daoReview = new DAOReview();
        // TODO: Replace userKey with the session key obtained from User
        daoReview.getByRestaurantKey(restaurantKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lists.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Review list = data.getValue(Review.class);
                    list.setReviewKey(data.getKey());
                    lists.add(list);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



}