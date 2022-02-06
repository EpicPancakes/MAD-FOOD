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

import com.example.onlyfoods.Adapters.DealsRecyclerViewAdapter;
import com.example.onlyfoods.Adapters.ResultsRecyclerViewAdapter;
import com.example.onlyfoods.DAOs.DAODeals;
import com.example.onlyfoods.DAOs.DAORestaurant;
import com.example.onlyfoods.DAOs.DAOUser;
import com.example.onlyfoods.Models.Deal;
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
 * Use the {@link DealsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DealsFragment extends Fragment implements DealsRecyclerViewAdapter.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ArrayList<Deal> deals = new ArrayList<>();
    private DealsRecyclerViewAdapter adapter;
    private DAODeals daoDeal;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DealsFragment() {
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
    public static DealsFragment newInstance(String param1, String param2) {
        DealsFragment fragment = new DealsFragment();
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

        daoDeal = new DAODeals();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_deals_list, container, false);

        // Lookup the recyclerview in activity layout
        RecyclerView rvDeals = (RecyclerView) view.findViewById(R.id.RVDeals);
        adapter = new DealsRecyclerViewAdapter(deals);
        rvDeals.setAdapter(adapter);
        adapter.setOnItemClickListener(DealsFragment.this);
        loadData();
        rvDeals.setLayoutManager(new LinearLayoutManager(this.getContext()));
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

    }

    private void loadData() {

        daoDeal.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                deals.clear();
                adapter.notifyDataSetChanged();
                for (DataSnapshot data: dataSnapshot.getChildren()) {
                    Deal deal = data.getValue(Deal.class);
                    deal.setDealKey(data.getKey());
                    deals.add(deal);
                    adapter.notifyItemInserted(deals.size() - 1);
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
        bundle.putString("dealKey", deals.get(position).getDealKey());
        Navigation.findNavController(getView()).navigate(R.id.DestDealDetails, bundle);
//        Toast.makeText(getContext(), "Viewing " + deals.get(position).getDealTitle(), Toast.LENGTH_SHORT).show();
    }
}