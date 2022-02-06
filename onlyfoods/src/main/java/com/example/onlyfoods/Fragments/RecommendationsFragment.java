package com.example.onlyfoods.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.onlyfoods.Adapters.RecommendationsAdapter;
import com.example.onlyfoods.DAOs.DAORecommendation;
import com.example.onlyfoods.Models.Recommendation;
import com.example.onlyfoods.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecommendationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecommendationsFragment extends Fragment implements RecommendationsAdapter.OnItemClickListener {

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
    DAORecommendation daoRec;
    RecommendationsAdapter adapter;
    ArrayList<Recommendation> recs = new ArrayList<>();
    private String sessionUserKey;


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
        sessionUserKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
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

        // Create adapter passing in the recommendation data
        adapter = new RecommendationsAdapter(recs);
        // Attach the adapter to the recyclerview to populate items
        rvRecommendations.setAdapter(adapter);
        adapter.setOnItemClickListener(RecommendationsFragment.this);

        daoRec = new DAORecommendation();
        loadData();

        // Set layout manager to position the items
        rvRecommendations.setLayoutManager(new LinearLayoutManager(this.getContext()));

    }

    private void loadData() {
        // TODO: Replace userKey with the session key obtained from User
        daoRec.getByUserKey(sessionUserKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recs.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Recommendation rec = data.getValue(Recommendation.class);
                    rec.setRecommendationKey(data.getKey());
                    recs.add(rec);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){

        FloatingActionButton BtnAddRecommendation = view.findViewById(R.id.FABAddRecommendation);
        View.OnClickListener OCLAddRecommendation = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.NextToAddRecommendation);
            }
        };
        BtnAddRecommendation.setOnClickListener(OCLAddRecommendation);

    }

    @Override
    public void onItemClick(int position) {
        Bundle args = new Bundle();
        args.putString("restaurantKey", recs.get(position).getRestaurantKey());
        Navigation.findNavController(getView()).navigate(R.id.DestRestaurant, args);
    }
}