package com.example.onlyfoods;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    ArrayList<Review> list;
//    ReviewAdapter reviewAdapter;

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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_gp_restaurant, container, false);
    }

//      Fitri's code
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_recommendations, container, false);
//        addFragment(view);
//        return view;
//    }
//
//    private void addFragment(View view){
//
//        // Lookup the recyclerview in activity layout
//        RecyclerView rvRecommendations = (RecyclerView) view.findViewById(R.id.RVRecommendations);
//
//        // Initialize recommendations
//        recommendations = Recommendation.createRecommendationsList(20);
//        // Create adapter passing in the sample user data
//        RecommendationsAdapter adapter = new RecommendationsAdapter(recommendations);
//        // Attach the adapter to the recyclerview to populate items
//        rvRecommendations.setAdapter(adapter);
//        // Set layout manager to position the items
//        rvRecommendations.setLayoutManager(new LinearLayoutManager(this.getContext()));
//
//    }

    //recommendations : list //Recommendation: Review
    private void addFragment(View view){

        // Lookup the recyclerview in activity layout
        RecyclerView rvReview = (RecyclerView) view.findViewById(R.id.RVRecommendations);
        populateList();

//        // Initialize recommendations
//        recommendations = Recommendation.createRecommendationsList(20);

        // Create adapter passing in the sample user data
        ReviewAdapter adapter = new ReviewAdapter(list);
        rvReview.setAdapter(adapter);
        rvReview.setItemAnimator(new DefaultItemAnimator());
        rvReview.setLayoutManager(new LinearLayoutManager(this.getContext(), RecyclerView.VERTICAL, false));


    }

    private void populateList() {
        list = new ArrayList<>();
        list.add(new Review("Fitri Koh", "Delicious Afffffffffff", R.drawable.my_profile_image));
        list.add(new Review("LeyHang Koh", "Trash", R.drawable.cat_img));
        list.add(new Review("Jason Koh", "STRESS LA BABI", R.drawable.dog_img));

    }


}