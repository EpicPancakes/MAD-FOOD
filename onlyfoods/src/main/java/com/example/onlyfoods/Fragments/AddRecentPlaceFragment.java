package com.example.onlyfoods.Fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.onlyfoods.DAOs.DAORecentPlace;
import com.example.onlyfoods.DAOs.DAORestaurant;
import com.example.onlyfoods.Models.RecentPlace;
import com.example.onlyfoods.Models.Restaurant;
import com.example.onlyfoods.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

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

    private AutoCompleteTextView ACTVRestaurantRP;
    ArrayList<Restaurant> restaurants = new ArrayList<>();
    ArrayList<String> results = new ArrayList<>();


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

        ACTVRestaurantRP = (AutoCompleteTextView) view.findViewById(R.id.ACTVRestaurantRP);
        final EditText ETDateArrived = (EditText) view.findViewById(R.id.ETDateArrived);
        Button BTNSubmitOnly = view.findViewById(R.id.BTNSubmitOnly);
        Button BTNSubmitAddReview = view.findViewById(R.id.BTNSubmitAddReview);


        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy"); // Make sure user insert date into edittext in this format.

        DAORestaurant daoRest = new DAORestaurant();
        DAORecentPlace daoRP = new DAORecentPlace();

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
                        ACTVRestaurantRP.setError("Invalid username.");
                    }
                    ;
                }
            }
        });

        BTNSubmitOnly.setOnClickListener(v -> {
            if (!hasErrors()) {
                Date dateObject = null;
                try {
                    String dateArrived = (ETDateArrived.getText().toString());
                    dateObject = formatter.parse(dateArrived);
//            date = new SimpleDateFormat("dd/MM/yyyy").format(dateObject);
//            time = new SimpleDateFormat("h:mmaa").format(dateObject);
                } catch (java.text.ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Log.i("Date Parsing Error: ", e.toString());
                }

//            Restaurant rest = new Restaurant(ACTVRestaurantRP.getText().toString(), "Western", "Petaling Jaya, Selangor");

                ACTVRestaurantRP.getText().toString();
                Restaurant selectedRestaurant = restaurants.stream().filter(restaurant -> (ACTVRestaurantRP.getText().toString()).equals(restaurant.getRestaurantName())).findFirst().orElse(null);
                Date finalDateObject = dateObject;
                RecentPlace rp = new RecentPlace(selectedRestaurant.getRestaurantKey(), "testUser", finalDateObject);
                daoRP.add(rp).addOnSuccessListener(suc2 ->
                {
                    Toast.makeText(getContext(), "Record is inserted", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(er ->
                {
                    Toast.makeText(getContext(), "" + er.getMessage(), Toast.LENGTH_SHORT).show();
                });
//            daoRest.add(rest).addOnSuccessListener(suc -> {
//
//                Toast.makeText(getContext(), "Restaurant is inserted", Toast.LENGTH_SHORT).show();
//                RecentPlace rp = new RecentPlace(daoRest.getRestaurantKey(), "testUser", finalDateObject);
//
//                daoRP.add(rp).addOnSuccessListener(suc2 ->
//                {
//                    Toast.makeText(getContext(), "Record is inserted", Toast.LENGTH_SHORT).show();
//                }).addOnFailureListener(er ->
//                {
//                    Toast.makeText(getContext(), "" + er.getMessage(), Toast.LENGTH_SHORT).show();
//                });
//
//            }).addOnFailureListener(er -> {
//                Toast.makeText(getContext(), "" + er.getMessage(), Toast.LENGTH_SHORT).show();
//            });
            }

        });

        BTNSubmitAddReview.setOnClickListener(v -> {

            // UPDATE

//            Date dateObject = null;
//            try{
//
//                String dateArrived=(ETDateArrived.getText().toString());
//
//                dateObject = formatter.parse(dateArrived);
//
////            date = new SimpleDateFormat("dd/MM/yyyy").format(dateObject);
////            time = new SimpleDateFormat("h:mmaa").format(dateObject);
//            }
//
//            catch (java.text.ParseException e)
//            {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//                Log.i("Date Parsing Error: ", e.toString());
//            }
//
//            HashMap<String, Object> hashMap = new HashMap<>();
//            hashMap.put("restaurant", ETSelectRestaurantRP.getText().toString());
//            hashMap.put("date", dateObject);
//
////            RecentPlace rp = new RecentPlace(ETSelectRestaurantRP.getText().toString(), dateObject);
//            dao.update("-MuiaPbcwyBgNac6nSG3", hashMap).addOnSuccessListener(suc ->
//            {
//                Toast.makeText(getContext(), "Record is updated", Toast.LENGTH_SHORT).show();
//            }).addOnFailureListener(er ->
//            {
//                Toast.makeText(getContext(), "" + er.getMessage(), Toast.LENGTH_SHORT).show();
//            });

            // REMOVE

//            dao.remove("-MuiaPbcwyBgNac6nSG3").addOnSuccessListener(suc ->
//            {
//                Toast.makeText(getContext(), "Record is deleted", Toast.LENGTH_SHORT).show();
//            }).addOnFailureListener(er ->
//            {
//                Toast.makeText(getContext(), "" + er.getMessage(), Toast.LENGTH_SHORT).show();
//            });
        });
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