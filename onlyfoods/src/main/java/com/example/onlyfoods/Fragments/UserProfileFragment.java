package com.example.onlyfoods.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

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

        if(bundle != null){
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

    private void addFragment(View view){
        tabLayout = view.findViewById(R.id.TLUPFollows);
        viewPager = view.findViewById(R.id.VPUPFollows);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager(), getLifecycle());
        adapter.addFragment(new RecentPlacesFragment(), "Recent Places");
        adapter.addFragment(new ReviewsFragment(), "Reviews");
        viewPager.setAdapter(adapter);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if(position == 0)
                tab.setText("Recent Places");
            else
                tab.setText("Reviews");
        }).attach();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){

        IVUPBackdrop = view.findViewById(R.id.IVUPBackdrop);
        TVUPName = view.findViewById(R.id.TVUPName);
        TVUPFollowers = view.findViewById(R.id.TVUPFollowers);
        TVUPFollowing = view.findViewById(R.id.TVUPFollowing);
        if(userKey != null){
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
                    if(backdrop!=null){
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
                    if(profileImage!=null){
                        Picasso.get().load(profileImage.getProfileImageUrl()).fit().centerCrop().into(IVProfileImage);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        TextView TVFollowers = (TextView) view.findViewById(R.id.TVUPFollowers);
        View.OnClickListener OCLFollowers = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.UPNextToFollows);
            }
        };
        TVFollowers.setOnClickListener(OCLFollowers);

        TextView TVFollowing = (TextView) view.findViewById(R.id.TVUPFollowing);
        View.OnClickListener OCLFollowing = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.UPNextToFollows);
            }
        };
        TVFollowing.setOnClickListener(OCLFollowers);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        daoBD.removeListener(mDBListenerBD);
        daoPI.removeListener(mDBListenerPI);
    }

}