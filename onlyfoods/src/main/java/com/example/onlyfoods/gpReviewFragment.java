package com.example.onlyfoods;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.onlyfoods.DAOs.DAOReview;
import com.example.onlyfoods.DAOs.DAOUser;
import com.example.onlyfoods.Models.Review;
import com.example.onlyfoods.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link gpReviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class gpReviewFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_RESTAURANT_KEY = "restaurantKey";
    private static final String ARG_FROM_MY_PROFILE = "fromMyProfile";

    // TODO: Rename and change types of parameters
    private String restaurantKey;
    private boolean fromMyProfile = false;
    private String sessionUserKey;
    private EditText ETReviewMessage;
    private Button BTNSubmitReview;

    private DAOReview daoRev;
    private User user;

    public gpReviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param restaurantKey Restaurant Key.
     * @return A new instance of fragment gpReviewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static gpReviewFragment newInstance(String restaurantKey) {
        gpReviewFragment fragment = new gpReviewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_RESTAURANT_KEY, restaurantKey);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            restaurantKey = getArguments().getString(ARG_RESTAURANT_KEY);
            fromMyProfile = getArguments().getBoolean(ARG_FROM_MY_PROFILE);
        }
        sessionUserKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gp_review, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        ETReviewMessage = (EditText) view.findViewById(R.id.ETReviewMessage);
        BTNSubmitReview  = (Button) view.findViewById(R.id.BTNSubmitReview);

        daoRev = new DAOReview();

        BTNSubmitReview.setOnClickListener(v -> {
            if (!hasErrors()) {
                Review review = new Review(sessionUserKey, restaurantKey, new Date(), ETReviewMessage.getText().toString());
                daoRev.add(review).addOnSuccessListener(suc2 ->
                {
                    Toast.makeText(getContext(), "Review added", Toast.LENGTH_SHORT).show();
                    if(fromMyProfile){
                        Navigation.findNavController(view).navigate(R.id.DestMyProfile);
                    }else{
                        getParentFragmentManager().popBackStackImmediate();
                    }
                }).addOnFailureListener(er ->
                {
                    Toast.makeText(getContext(), "" + er.getMessage(), Toast.LENGTH_SHORT).show();
                });

                // Include review key in user's review list by updating user
                DAOUser daoUser = new DAOUser();
                daoUser.getByUserKey(sessionUserKey).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        user = snapshot.getValue(User.class);
                        user.setUserKey(snapshot.getKey());
                        if (user != null) {
                            Map<String, Object> objectHM = new HashMap<>();
                            Map<String, Boolean> booleanHM;
                            if (user.getReviews() != null) {
                                booleanHM = user.getReviews();
                            } else {
                                booleanHM = new HashMap<>();
                            }
                            booleanHM.put(daoRev.getReviewKey(), true);
                            objectHM.put("reviews", booleanHM);
                            daoUser.update(user.getUserKey(), objectHM).addOnSuccessListener(suc -> {

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
        });

    }


    private boolean hasErrors() {
        if (ETReviewMessage.getText().toString().isEmpty()) {
            ETReviewMessage.setError("Please enter a review message.");
            return true;
        }
        return false;
    }
}
