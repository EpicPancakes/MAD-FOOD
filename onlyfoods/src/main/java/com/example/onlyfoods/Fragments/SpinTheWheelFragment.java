package com.example.onlyfoods.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.onlyfoods.DAOs.DAORestaurant;
import com.example.onlyfoods.DAOs.DAOUser;
import com.example.onlyfoods.Models.Restaurant;
import com.example.onlyfoods.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

import rubikstudio.library.LuckyWheelView;
import rubikstudio.library.model.LuckyItem;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SpinTheWheelFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SpinTheWheelFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private LuckyWheelView luckyWheelView;
    private ArrayList<LuckyItem> arrayListItems;
    private DAOUser daoUser;
    private DAORestaurant daoRest;
    private String sessionUserKey;
    private int counter = 0;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SpinTheWheelFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AboutAppFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SpinTheWheelFragment newInstance(String param1, String param2) {
        SpinTheWheelFragment fragment = new SpinTheWheelFragment();
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

        daoRest = new DAORestaurant();
        daoUser = new DAOUser();
        sessionUserKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_spin_the_wheel, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        luckyWheelView = (LuckyWheelView) view.findViewById(R.id.luckyWheel);
        arrayListItems = new ArrayList<>();

        daoUser.getSavedRestaurantsByUserKey(sessionUserKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayListItems.clear();
                counter  = 0;
                for (DataSnapshot data : snapshot.getChildren()) {
                    daoRest.getRestaurantsByKey(data.getKey()).addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            Restaurant restaurant = dataSnapshot.getValue(Restaurant.class);
                            restaurant.setRestaurantKey(dataSnapshot.getKey());
                            LuckyItem luckyItem = new LuckyItem();
                            luckyItem.topText = restaurant.getRestaurantName();
                            if(counter%2==0){
                                luckyItem.color = Color.parseColor("#FFF3AD");
                            }else{
                                luckyItem.color = Color.parseColor("#E57685");
                            }
                            counter++;
                            arrayListItems.add(luckyItem);
                            luckyWheelView.setData(arrayListItems);
                            luckyWheelView.setRound(getRandomRound());

                            luckyWheelView.setLuckyRoundItemSelectedListener(new LuckyWheelView.LuckyRoundItemSelectedListener() {
                                @Override
                                public void LuckyRoundItemSelected(int index) {
                                    Toast.makeText(view.getContext(), "Let's dine at "+arrayListItems.get(index).topText+"!", Toast.LENGTH_SHORT).show();
                                }
                            });

                            Button BTNSpinTheWheel = view.findViewById(R.id.BTNSpinTheWheel);
                            BTNSpinTheWheel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
//                        int index = getRandomIndex();
                                    luckyWheelView.startLuckyWheelWithRandomTarget();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


//        LuckyItem luckyItem1 = new LuckyItem();
//        luckyItem1.topText = "Eyuzu Cuisine";
//        luckyItem1.color = Color.parseColor("#8574F1");
//        arrayListItems.add(luckyItem1);
//
//        LuckyItem luckyItem2 = new LuckyItem();
//        luckyItem2.topText = "Eyuzu Cuisine 0";
//        luckyItem2.color = Color.parseColor("#8574F1");
//        arrayListItems.add(luckyItem2);
//
//        LuckyItem luckyItem3 = new LuckyItem();
//        luckyItem3.topText = "Eyuzu Cuisine 1";
//        luckyItem3.color = Color.parseColor("#8574F1");
//        arrayListItems.add(luckyItem3);
//
//        LuckyItem luckyItem4 = new LuckyItem();
//        luckyItem4.topText = "Eyuzu Cuisine";
//        luckyItem4.color = Color.parseColor("#8574F1");
//        arrayListItems.add(luckyItem4);
//
//        LuckyItem luckyItem5 = new LuckyItem();
//        luckyItem5.topText = "Eyuzu Cuisine";
//        luckyItem5.color = Color.parseColor("#8574F1");
//        arrayListItems.add(luckyItem5);
    }

    public int getRandomRound(){
        Random rand = new Random();
        return rand.nextInt(10) + 5;
    }

//    public int getRandomIndex(){
//        int rand = new Random().nextInt(arrayListItems.size());
//        return rand;
//    }
}