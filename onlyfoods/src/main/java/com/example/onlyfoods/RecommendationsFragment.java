package com.example.onlyfoods;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecommendationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecommendationsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    // ViewPager implementation to slide between recent places and reviews
    TabLayout tabLayout;
    ViewPager2 viewPager;
    ArrayList<Recommendation> recommendations;

    public RecommendationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecommendationsFragment newInstance(String param1, String param2) {
        RecommendationsFragment fragment = new RecommendationsFragment();
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
        View view = inflater.inflate(R.layout.fragment_recommendations, container, false);
        addFragment(view);
        return view;
    }

    private void addFragment(View view){

        // Lookup the recyclerview in activity layout
        RecyclerView rvRecommendations = (RecyclerView) view.findViewById(R.id.RVRecommendations);

        // Initialize recommendations
        recommendations = Recommendation.createRecommendationsList(20);
        // Create adapter passing in the sample user data
        RecommendationsAdapter adapter = new RecommendationsAdapter(recommendations);
        // Attach the adapter to the recyclerview to populate items
        rvRecommendations.setAdapter(adapter);
        // Set layout manager to position the items
        rvRecommendations.setLayoutManager(new LinearLayoutManager(this.getContext()));
        // That's all!
//
//        tabLayout = view.findViewById(R.id.TLFollows);
//        viewPager = view.findViewById(R.id.VPFollows);
//        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager(), getLifecycle());
//        adapter.addFragment(new FollowersFragment(), "Followers");
//        adapter.addFragment(new FollowingFragment(), "Following");
//        viewPager.setAdapter(adapter);
//        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
//            if(position == 0)
//                tab.setText("Followers");
//            else
//                tab.setText("Following");
//        }).attach();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){

//        Button BtnCat = view.findViewById(R.id.BtnCat);
//        View.OnClickListener OCLCat = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Navigation.findNavController(view).navigate(R.id.DestCat);
//            }
//        };
//        BtnCat.setOnClickListener(OCLCat);
//
//        Button BtnDog = view.findViewById(R.id.BtnDog);
//        View.OnClickListener OCLDog = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Navigation.findNavController(view).navigate(R.id.NextToDog);
//            }
//        };
//        BtnDog.setOnClickListener(OCLDog);
//
//        Button BtnHedgehog = view.findViewById(R.id.BtnHedgehog);
//        View.OnClickListener OCLHedgehog = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Navigation.findNavController(view).navigate(R.id.NextToHedgehog);
//            }
//        };
//        BtnHedgehog.setOnClickListener(OCLHedgehog);
    }

}