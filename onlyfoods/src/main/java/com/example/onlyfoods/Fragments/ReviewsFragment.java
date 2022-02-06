package com.example.onlyfoods.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlyfoods.Adapters.MyReviewsRecyclerViewAdapter;
import com.example.onlyfoods.Adapters.UserReviewsRecyclerViewAdapter;
import com.example.onlyfoods.DAOs.DAOReview;
import com.example.onlyfoods.DAOs.DAOUser;
import com.example.onlyfoods.Models.Review;
import com.example.onlyfoods.Models.User;
import com.example.onlyfoods.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A fragment representing a list of Items.
 */
public class ReviewsFragment extends Fragment implements MyReviewsRecyclerViewAdapter.OnItemClickListener, UserReviewsRecyclerViewAdapter.OnItemClickListener  {

    private static final String VIEWED_USER_KEY = "viewedUserKey";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    DAOReview daoRev;
    MyReviewsRecyclerViewAdapter myRevAdapter;
    UserReviewsRecyclerViewAdapter userRevAdapter;
    ArrayList<Review> reviews = new ArrayList<>();
    private User user;
    private String viewedUserKey;
    private String sessionUserKey;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ReviewsFragment() {
    }

    public static ReviewsFragment newInstance(String viewedUserKey) {
        ReviewsFragment fragment = new ReviewsFragment();
        Bundle args = new Bundle();
        args.putString(VIEWED_USER_KEY, viewedUserKey);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            viewedUserKey = getArguments().getString(VIEWED_USER_KEY);
        }
        sessionUserKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reviews_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            if(viewedUserKey!= null){
                userRevAdapter = new UserReviewsRecyclerViewAdapter(getContext(), reviews);
                recyclerView.setAdapter(userRevAdapter);
                userRevAdapter.setOnItemClickListener(ReviewsFragment.this);
            }else{
                myRevAdapter = new MyReviewsRecyclerViewAdapter(getContext(), reviews);
                recyclerView.setAdapter(myRevAdapter);
                myRevAdapter.setOnItemClickListener(ReviewsFragment.this);
            }
        }

        daoRev = new DAOReview();
        loadData();

        return view;
    }

    private void loadData() {
        // TODO: Replace userKey with the session key obtained from User
        String revUserKey;
        if(viewedUserKey != null){
            revUserKey = viewedUserKey;
        }else{
            revUserKey = sessionUserKey;
        }

        daoRev.getByUserKey(revUserKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reviews.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Review rev = data.getValue(Review.class);
                    rev.setReviewKey(data.getKey());
                    reviews.add(rev);
                }
                if(viewedUserKey!=null){
                    userRevAdapter.notifyDataSetChanged();
                }else{
                    myRevAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Bundle args = new Bundle();
        args.putString("restaurantKey", reviews.get(position).getRestaurantKey());
        Navigation.findNavController(getView()).navigate(R.id.DestRestaurant, args);
    }

//    @Override
//    public void onEditClick(int position) {
//        Toast.makeText(getContext(), "Edit click at position: " + position, Toast.LENGTH_SHORT).show();
//    }

    @Override
    public void onDeleteClick(int position) {
        deleteFile(position);
    }

    private void deleteFile(int position) {
        Review selectedItem = reviews.get(position);
        if (selectedItem != null) {
            final String selectedReviewKey = selectedItem.getReviewKey();
            daoRev.remove(selectedReviewKey).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    deleteReviewinUser(selectedReviewKey);
                    Toast.makeText(getContext(), "Removed review", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void deleteReviewinUser(String revKey){
        // Include review key in user's reviews list by updating user
        DAOUser daoUser = new DAOUser();
        daoUser.getByUserKey(sessionUserKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                user.setUserKey(snapshot.getKey());
                if (user != null) {
                    Map<String, Object> objectHM = new HashMap<>();
                    Map<String, Boolean> booleanHM;
                    if(user.getReviews()!=null){
                        booleanHM = user.getReviews();
                    }else{
                        booleanHM = new HashMap<>();
                    }
                    booleanHM.remove(revKey);
                    objectHM.put("reviews", booleanHM);
                    daoUser.update(user.getUserKey(), objectHM).addOnSuccessListener(suc -> {
                        Toast.makeText(getContext(), "Record is updated", Toast.LENGTH_SHORT).show();
                    }).addOnFailureListener(er ->
                    {
                        Toast.makeText(getContext(), "" + er.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}