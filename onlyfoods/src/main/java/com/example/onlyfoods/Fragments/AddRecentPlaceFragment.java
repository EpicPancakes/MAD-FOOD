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
import com.google.firebase.auth.FirebaseAuth;
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
 * Use the {@link AddRecentPlaceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddRecentPlaceFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView TVSelectDateArrived;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private AutoCompleteTextView ACTVRestaurantRP;
    ArrayList<Restaurant> restaurants = new ArrayList<>();
    ArrayList<String> results = new ArrayList<>();
    private User user;
    private String sessionUserKey;
    private DAORestaurant daoRest;
    private DAORecentPlace daoRP;
    private DateFormat formatter;
    private View view;


    public AddRecentPlaceFragment() {
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
    public static AddRecentPlaceFragment newInstance(String param1, String param2) {
        AddRecentPlaceFragment fragment = new AddRecentPlaceFragment();
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
        return inflater.inflate(R.layout.fragment_add_recent_place, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        this.view = view;
        ACTVRestaurantRP = (AutoCompleteTextView) view.findViewById(R.id.ACTVRestaurantRP);
        Button BTNSubmitOnly = view.findViewById(R.id.BTNSubmitOnly);
        Button BTNSubmitAddReview = view.findViewById(R.id.BTNSubmitAddReview);

        TVSelectDateArrived = (TextView) view.findViewById(R.id.TVSelectDateArrived);
        TVSelectDateArrived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        getContext(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                Log.d("AddRecentPlace", "onDateSet: dd/mm/yyyy: " + dayOfMonth + "/" + month + "/" + year);
                String date = dayOfMonth + "/" + month + "/" + year;
                TVSelectDateArrived.setText(date);
            }
        };


        formatter = new SimpleDateFormat("dd/MM/yyyy"); // Make sure user insert date into edittext in this format.

        daoRest = new DAORestaurant();
        daoRP = new DAORecentPlace();

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

        ACTVRestaurantRP.setAdapter(autoComplete);
        ACTVRestaurantRP.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (results.size() == 0 ||
                            !results.contains(ACTVRestaurantRP.getText().toString())) {
                        ACTVRestaurantRP.setError("Invalid restaurant.");
                    }
                    ;
                }
            }
        });

        BTNSubmitOnly.setOnClickListener(v -> {
            submitRecentPlace(false);
        });

        BTNSubmitAddReview.setOnClickListener(v -> {
            submitRecentPlace(true);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void submitRecentPlace(boolean addReview){
        if (!hasErrors()) {
            Date dateObject = null;
            try {
                String dateArrived = (TVSelectDateArrived.getText().toString());
                dateObject = formatter.parse(dateArrived);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
                Log.i("Date Parsing Error: ", e.toString());
            }
            ACTVRestaurantRP.getText().toString();
            Restaurant selectedRestaurant = restaurants.stream().filter(restaurant -> (ACTVRestaurantRP.getText().toString()).equals(restaurant.getRestaurantName())).findFirst().orElse(null);
            Date finalDateObject = dateObject;
            RecentPlace rp = new RecentPlace(selectedRestaurant.getRestaurantKey(), sessionUserKey, finalDateObject);
            daoRP.add(rp).addOnSuccessListener(suc2 ->
            {
                Toast.makeText(getContext(), "Record is inserted", Toast.LENGTH_SHORT).show();
                if(addReview){
                    Bundle args = new Bundle();
                    args.putString("restaurantKey", selectedRestaurant.getRestaurantKey());
                    args.putBoolean("fromMyProfile", true);
                    Navigation.findNavController(view).navigate(R.id.RPNextToAddReview, args);
                }else{
                    getActivity().onBackPressed();
                }
            }).addOnFailureListener(er ->
            {
                Toast.makeText(getContext(), "" + er.getMessage(), Toast.LENGTH_SHORT).show();
            });

            // Include recent place key in user's recent places list by updating user
            DAOUser daoUser = new DAOUser();
            daoUser.getByUserKey(sessionUserKey).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    user = snapshot.getValue(User.class);
                    user.setUserKey(snapshot.getKey());
                    if (user != null) {
                        Map<String, Object> objectHM = new HashMap<>();
                        Map<String, Boolean> booleanHM;
                        if (user.getRecentPlaces() != null) {
                            booleanHM = user.getRecentPlaces();
                        } else {
                            booleanHM = new HashMap<>();
                        }
                        booleanHM.put(daoRP.getRecentPlaceKey(), true);
                        objectHM.put("recentPlaces", booleanHM);
                        daoUser.update(user.getUserKey(), objectHM).addOnSuccessListener(suc -> {
//                                Toast.makeText(view.getContext(), "Record is updated", Toast.LENGTH_SHORT).show();
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
        }
    }

    private boolean hasErrors() {

        if (ACTVRestaurantRP.getText().toString().isEmpty()) {
            ACTVRestaurantRP.setError("Please enter a restaurant name.");
            return true;
        } else if (results.size() == 0 || !results.contains(ACTVRestaurantRP.getText().toString())) {
            ACTVRestaurantRP.setError("Invalid restaurant.");
            return true;
        }

        return false;
    }
}