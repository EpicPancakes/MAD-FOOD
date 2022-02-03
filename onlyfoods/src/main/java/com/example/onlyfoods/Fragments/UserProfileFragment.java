package com.example.onlyfoods.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import com.example.onlyfoods.Adapters.ViewPagerAdapter;
import com.example.onlyfoods.DAOs.DAOBackdrop;
import com.example.onlyfoods.DAOs.DAOProfileImage;
import com.example.onlyfoods.DAOs.DAOUser;
import com.example.onlyfoods.Models.Backdrop;
import com.example.onlyfoods.Models.ProfileImage;
import com.example.onlyfoods.Models.User;
import com.example.onlyfoods.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String userKey;
    private TextView TVUPName;
    private TextView TVUPFollowers;
    private TextView TVUPFollowing;
    private Button BTNUPFollow;
    private Button BTNUPFollowing;

    private User user;
    private DAOUser daoUser;

    private DAOBackdrop daoBD;
    private ValueEventListener mDBListenerBD;

    private DAOProfileImage daoPI;
    private ValueEventListener mDBListenerPI;

    ImageView IVUPBackdrop;
    Backdrop backdrop;

    ImageView IVProfileImage;
    ProfileImage profileImage;

    // ViewPager implementation to slide between recent places and reviews
    TabLayout tabLayout;
    ViewPager2 viewPager;

    public UserProfileFragment() {
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
    public static UserProfileFragment newInstance(String param1, String param2) {
        UserProfileFragment fragment = new UserProfileFragment();
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
        daoUser = new DAOUser();
        daoBD = new DAOBackdrop();
        daoPI = new DAOProfileImage();
        Bundle bundle = this.getArguments();

        if (bundle != null) {
            userKey = bundle.getString("userKey");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        addFragment(view);
        return view;
    }

    private void addFragment(View view) {
        tabLayout = view.findViewById(R.id.TLUPFollows);
        viewPager = view.findViewById(R.id.VPUPFollows);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager(), getLifecycle());
        adapter.addFragment(new RecentPlacesFragment(), "Recent Places");
        adapter.addFragment(new ReviewsFragment(), "Reviews");
        viewPager.setAdapter(adapter);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0)
                tab.setText("Recent Places");
            else
                tab.setText("Reviews");
        }).attach();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        IVUPBackdrop = view.findViewById(R.id.IVUPBackdrop);
        TVUPName = view.findViewById(R.id.TVUPName);
        TVUPFollowers = view.findViewById(R.id.TVUPFollowers);
        TVUPFollowing = view.findViewById(R.id.TVUPFollowing);
        BTNUPFollow = view.findViewById(R.id.BTNUPFollow);
        BTNUPFollowing = view.findViewById(R.id.BTNUPFollowing);


        if (userKey != null) {

            // check if session user is currently following viewed user
            daoUser.checkIfFollows("-MutmLS6FPIkhneAJSGT", userKey).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        BTNUPFollowing.setVisibility(View.VISIBLE);
                        BTNUPFollow.setVisibility(View.INVISIBLE);
                    } else {
                        BTNUPFollow.setVisibility(View.VISIBLE);
                        BTNUPFollowing.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            daoUser.getByUserKey(userKey).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    user = snapshot.getValue(User.class);
                    TVUPName.setText(user.getUsername());
                    String followersCountText = String.valueOf(user.getFollowersCount()) + " Followers";
                    TVUPFollowers.setText(followersCountText);
                    String followingCountText = String.valueOf(user.getFollowingCount()) + " Following";
                    TVUPFollowing.setText(followingCountText);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            mDBListenerBD = daoBD.getByUserKey(userKey).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        backdrop = data.getValue(Backdrop.class);
                        backdrop.setBackdropKey(data.getKey());
                    }
                    if (backdrop != null) {
                        Picasso.get().load(backdrop.getBackdropUrl()).fit().centerCrop().into(IVUPBackdrop);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            IVProfileImage = view.findViewById(R.id.IVUPProfileImage);
            mDBListenerPI = daoPI.getByUserKey(userKey).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        profileImage = data.getValue(ProfileImage.class);
                        profileImage.setProfileImageKey(data.getKey());
                    }
                    if (profileImage != null) {
                        Picasso.get().load(profileImage.getProfileImageUrl()).fit().centerCrop().into(IVProfileImage);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        View.OnClickListener OCLFollowers = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.UPNextToFollows);
            }
        };
        TVUPFollowers.setOnClickListener(OCLFollowers);

        View.OnClickListener OCLFollowing = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.UPNextToFollows);
            }
        };
        TVUPFollowing.setOnClickListener(OCLFollowers);

        // Follows the user upon clicking the button "Follow"
        BTNUPFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DAOUser daoUser = new DAOUser();
                daoUser.getByUserKeyOnce("-MutmLS6FPIkhneAJSGT").addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        User sessionUser = dataSnapshot.getValue(User.class);
                        if (sessionUser != null && userKey != null) {
                            sessionUser.setUserKey(dataSnapshot.getKey());
                            Map<String, Object> objectHM = new HashMap<>();
                            Map<String, Boolean> booleanHM;
                            if (sessionUser.getFollowing() != null) {
                                booleanHM = sessionUser.getFollowing();
                            } else {
                                booleanHM = new HashMap<>();
                            }
                            booleanHM.put(userKey, true);
                            objectHM.put("following", booleanHM);
                            daoUser.update(sessionUser.getUserKey(), objectHM).addOnSuccessListener(suc -> {

                                Map<String, Object> objectHM2 = new HashMap<>();
                                Map<String, Boolean> booleanHM2;
                                if (user.getFollowers() != null) {
                                    booleanHM2 = user.getFollowers();
                                } else {
                                    booleanHM2 = new HashMap<>();
                                }
                                booleanHM2.put(sessionUser.getUserKey(), true);
                                objectHM2.put("followers", booleanHM2);
                                daoUser.update(userKey, objectHM2).addOnSuccessListener(suc2 -> {
//                                    BTNUPFollowing.setVisibility(View.VISIBLE);
//                                    BTNUPFollow.setVisibility(View.INVISIBLE);
                                    Toast.makeText(view.getContext(), "User followed", Toast.LENGTH_SHORT).show();
                                });

                            }).addOnFailureListener(er ->
                            {
                                Toast.makeText(view.getContext(), "" + er.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        });

        // Unfollows the user upon clicking the button "Following"
        BTNUPFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DAOUser daoUser = new DAOUser();
                daoUser.getByUserKeyOnce("-MutmLS6FPIkhneAJSGT").addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        User sessionUser = dataSnapshot.getValue(User.class);
                        if (sessionUser != null && userKey != null) {
                            sessionUser.setUserKey(dataSnapshot.getKey());
                            Map<String, Object> objectHM = new HashMap<>();
                            Map<String, Boolean> booleanHM;
                            if (sessionUser.getFollowing() != null) {
                                booleanHM = sessionUser.getFollowing();
                            } else {
                                booleanHM = new HashMap<>();
                            }
                            booleanHM.remove(userKey);
                            objectHM.put("following", booleanHM);
                            daoUser.update(sessionUser.getUserKey(), objectHM).addOnSuccessListener(suc -> {

                                Map<String, Object> objectHM2 = new HashMap<>();
                                Map<String, Boolean> booleanHM2;
                                if (user.getFollowers() != null) {
                                    booleanHM2 = user.getFollowers();
                                } else {
                                    booleanHM2 = new HashMap<>();
                                }
                                booleanHM2.remove(sessionUser.getUserKey());
                                objectHM2.put("followers", booleanHM2);
                                daoUser.update(userKey, objectHM2).addOnSuccessListener(suc2 -> {
//                                    BTNUPFollowing.setVisibility(View.INVISIBLE);
//                                    BTNUPFollow.setVisibility(View.VISIBLE);
                                    Toast.makeText(view.getContext(), "User unfollowed", Toast.LENGTH_SHORT).show();
                                });

                            }).addOnFailureListener(er ->
                            {
                                Toast.makeText(view.getContext(), "" + er.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        daoBD.removeListener(mDBListenerBD);
        daoPI.removeListener(mDBListenerPI);
    }

}