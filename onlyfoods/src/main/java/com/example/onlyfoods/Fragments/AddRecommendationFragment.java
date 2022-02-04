package com.example.onlyfoods.Fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.onlyfoods.DAOs.DAORecommendation;
import com.example.onlyfoods.DAOs.DAORestaurant;
import com.example.onlyfoods.DAOs.DAOUser;
import com.example.onlyfoods.Models.Recommendation;
import com.example.onlyfoods.Models.Restaurant;
import com.example.onlyfoods.Models.User;
import com.example.onlyfoods.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddRecommendationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddRecommendationFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private AutoCompleteTextView ACTVRestaurantRec;
    private AutoCompleteTextView ACTVRecommendTo;
    private EditText ETRecommendMessage;
    ArrayList<Restaurant> restaurants = new ArrayList<>();
    ArrayList<String> restaurantResults = new ArrayList<>();
    ArrayList<User> followers = new ArrayList<>();
    ArrayList<String> followerResults = new ArrayList<>();
    private User user;

    private DAOUser daoUser;
    private DAORestaurant daoRest;
    private DAORecommendation daoRec;

    private String sessionUserKey;

    public AddRecommendationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddRecommendation.
     */
    // TODO: Rename and change types and number of parameters
    public static AddRecommendationFragment newInstance(String param1, String param2) {
        AddRecommendationFragment fragment = new AddRecommendationFragment();
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

        sessionUserKey = "-MutmLS6FPIkhneAJSGT";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_recommendation, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        ACTVRestaurantRec = (AutoCompleteTextView) view.findViewById(R.id.ACTVRestaurantRec);
        ACTVRecommendTo = (AutoCompleteTextView) view.findViewById(R.id.ACTVRecommendTo);
        ETRecommendMessage = (EditText) view.findViewById(R.id.ETRecommendMessage);
        daoRest = new DAORestaurant();
        daoUser = new DAOUser();
        daoRec = new DAORecommendation();
        Button BTNRecommendPlace = view.findViewById(R.id.BTNRecommendPlace);


        final ArrayAdapter<String> autoCompleteRestaurant = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);
        daoRest.getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                restaurants.clear();
                for (DataSnapshot suggestionSnapshot : snapshot.getChildren()) {
                    Restaurant rest = suggestionSnapshot.getValue(Restaurant.class);
                    rest.setRestaurantKey(suggestionSnapshot.getKey());
                    restaurants.add(rest);
                    String suggestion = suggestionSnapshot.child("restaurantName").getValue(String.class);
                    autoCompleteRestaurant.add(suggestion);
                    restaurantResults.add(suggestion);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        final ArrayAdapter<String> autoCompleteFollower = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);
        daoUser.getFollowersByUserKey(sessionUserKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                followers.clear();
                for (DataSnapshot suggestionSnapshot : snapshot.getChildren()) {

                    daoUser.getByUserKey(suggestionSnapshot.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                            User follower = userSnapshot.getValue(User.class);
                            follower.setUserKey(userSnapshot.getKey());
                            followers.add(follower);
                            String suggestion = userSnapshot.child("username").getValue(String.class);
                            autoCompleteFollower.add(suggestion);
                            followerResults.add(suggestion);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ACTVRecommendTo.setAdapter(autoCompleteFollower);
        ACTVRecommendTo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (followerResults.size() == 0 ||
                            !followerResults.contains(ACTVRecommendTo.getText().toString())) {
                        ACTVRecommendTo.setError("Invalid follower.");
                    }
                }
            }
        });

        ACTVRestaurantRec.setAdapter(autoCompleteRestaurant);
        ACTVRestaurantRec.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (restaurantResults.size() == 0 ||
                            !restaurantResults.contains(ACTVRestaurantRec.getText().toString())) {
                        ACTVRestaurantRec.setError("Invalid restaurant.");
                    }
                }
            }
        });

        BTNRecommendPlace.setOnClickListener(v -> {
            if (!(hasRestaurantErrors() || hasFollowerErrors())) {

                Restaurant selectedRestaurant = restaurants.stream().filter(restaurant -> (ACTVRestaurantRec.getText().toString()).equals(restaurant.getRestaurantName())).findFirst().orElse(null);
                User selectedUser = followers.stream().filter(follower -> (ACTVRecommendTo.getText().toString()).equals(follower.getUsername())).findFirst().orElse(null);

                Calendar calendar = Calendar.getInstance();
                Recommendation rec = new Recommendation(selectedUser.getUserKey(), selectedRestaurant.getRestaurantKey(),sessionUserKey, new Date(), ETRecommendMessage.getText().toString());
                daoRec.add(rec).addOnSuccessListener(suc2 ->
                {

                    // Include recent place key in user's recent places list by updating user
                    DAOUser daoUser = new DAOUser();
                    daoUser.getByUserKey(selectedUser.getUserKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            user = snapshot.getValue(User.class);
                            user.setUserKey(snapshot.getKey());
                            if (user != null) {
                                Map<String, Object> objectHM = new HashMap<>();
                                Map<String, Boolean> booleanHM;
                                if(user.getRecommendations()!=null){
                                    booleanHM = user.getRecommendations();
                                }else{
                                    booleanHM = new HashMap<>();
                                }
                                booleanHM.put(daoRec.getRecommendationKey(), true);
                                objectHM.put("recommendations", booleanHM);

                                daoUser.update(user.getUserKey(), objectHM).addOnSuccessListener(suc -> {
                                    Toast.makeText(view.getContext(), "Recommendation successfully sent!", Toast.LENGTH_SHORT).show();
                                }).addOnFailureListener(er ->
                                {
                                    Toast.makeText(view.getContext(), "" + er.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    Toast.makeText(getContext(), "Record is inserted", Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                }).addOnFailureListener(er ->
                {
                    Toast.makeText(getContext(), "" + er.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }

        });

    }

    private boolean hasRestaurantErrors() {

        if (ACTVRestaurantRec.getText().toString().isEmpty()) {
            ACTVRestaurantRec.setError("Please enter a restaurant name.");
            return true;
        } else if (restaurantResults.size() == 0 || !restaurantResults.contains(ACTVRestaurantRec.getText().toString())) {
            ACTVRestaurantRec.setError("Invalid restaurant.");
            return true;
        }

        return false;
    }

    private boolean hasFollowerErrors() {

        if (ACTVRecommendTo.getText().toString().isEmpty()) {
            ACTVRecommendTo.setError("Please enter a follower name.");
            return true;
        } else if (followerResults.size() == 0 || !followerResults.contains(ACTVRecommendTo.getText().toString())) {
            ACTVRecommendTo.setError("Invalid follower.");
            return true;
        }

        return false;
    }
}