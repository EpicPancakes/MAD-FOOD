package com.example.onlyfoods.Fragments;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.onlyfoods.DAOs.DAORecentPlace;
import com.example.onlyfoods.DAOs.DAORestaurant;
import com.example.onlyfoods.DAOs.DAOUser;
import com.example.onlyfoods.Models.RecentPlace;
import com.example.onlyfoods.Models.Restaurant;
import com.example.onlyfoods.Models.User;
import com.example.onlyfoods.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SelectRestaurantFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectRestaurantFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private AutoCompleteTextView ACTVRestaurantSR;
    ArrayList<Restaurant> restaurants = new ArrayList<>();
    ArrayList<String> results = new ArrayList<>();
    private User user;


    public SelectRestaurantFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddRecentPlace.
     */
    // TODO: Rename and change types and number of parameters
    public static SelectRestaurantFragment newInstance(String param1, String param2) {
        SelectRestaurantFragment fragment = new SelectRestaurantFragment();
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
        return inflater.inflate(R.layout.fragment_select_restaurant, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        ACTVRestaurantSR = (AutoCompleteTextView) view.findViewById(R.id.ACTVRestaurantSR);
        Button BTNContinueSR = view.findViewById(R.id.BTNContinueSR);

        DAORestaurant daoRest = new DAORestaurant();

        final ArrayAdapter<String> autoComplete = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);

        daoRest.getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                restaurants.clear();
                for (DataSnapshot suggestionSnapshot : snapshot.getChildren()) {
                    Restaurant rest = suggestionSnapshot.getValue(Restaurant.class);
                    rest.setRestaurantKey(suggestionSnapshot.getKey());
                    restaurants.add(rest);
                    String suggestion = suggestionSnapshot.child("restaurantName").getValue(String.class);
                    autoComplete.add(suggestion);
                    results.add(suggestion);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ACTVRestaurantSR.setAdapter(autoComplete);
        ACTVRestaurantSR.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (results.size() == 0 ||
                            !results.contains(ACTVRestaurantSR.getText().toString())) {
                        ACTVRestaurantSR.setError("Invalid restaurant.");
                    }
                }
            }
        });

        BTNContinueSR.setOnClickListener(v -> {
            if (!hasErrors()) {
                ACTVRestaurantSR.getText().toString();
                Restaurant selectedRestaurant = restaurants.stream().filter(restaurant -> (ACTVRestaurantSR.getText().toString()).equals(restaurant.getRestaurantName())).findFirst().orElse(null);
                Bundle args = new Bundle();
                args.putString("restaurantKey", selectedRestaurant.getRestaurantKey());
                args.putBoolean("fromMyProfile", true);
                Navigation.findNavController(view).navigate(R.id.NextToAddReview, args);
            }
        });
    }

    private boolean hasErrors() {

        if (ACTVRestaurantSR.getText().toString().isEmpty()) {
            ACTVRestaurantSR.setError("Please enter a restaurant name.");
            return true;
        } else if (results.size() == 0 || !results.contains(ACTVRestaurantSR.getText().toString())) {
            ACTVRestaurantSR.setError("Invalid restaurant.");
            return true;
        }

        return false;
    }
}