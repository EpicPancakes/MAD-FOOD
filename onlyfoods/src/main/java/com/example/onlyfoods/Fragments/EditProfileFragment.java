package com.example.onlyfoods.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.onlyfoods.DAOs.DAOBackdrop;
import com.example.onlyfoods.DAOs.DAOProfileImage;
import com.example.onlyfoods.DAOs.DAOUser;
import com.example.onlyfoods.Models.Backdrop;
import com.example.onlyfoods.Models.ProfileImage;
import com.example.onlyfoods.Models.User;
import com.example.onlyfoods.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button BTNEditBackdrop;
    private ImageButton IBEditProfileImage;
    private ImageButton IBEditName;
    private User user;

    private DAOUser daoUser;
    private DAOBackdrop daoBD;
    private DAOProfileImage daoPI;
    private ValueEventListener mDBListenerBD;
    private ValueEventListener mDBListenerPI;

    private Backdrop backdrop;
    private ProfileImage profileImage;
    private ImageView IVEditBackdrop;
    private ImageView IVEditProfileImage;
    private TextView TVEditName;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditProfileFragment newInstance(String param1, String param2) {
        EditProfileFragment fragment = new EditProfileFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


        BTNEditBackdrop = view.findViewById(R.id.BTNEditBackdrop);
        IVEditBackdrop = view.findViewById(R.id.IVEditBackdrop);
        IBEditProfileImage = view.findViewById(R.id.IBEditProfileImage);
        IVEditProfileImage = view.findViewById(R.id.IVEditProfileImage);
        TVEditName = view.findViewById(R.id.TVEditName);
        IBEditName = view.findViewById(R.id.IBEditName);

        daoUser.getByUserKey("-MutmLS6FPIkhneAJSGT").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                TVEditName.setText(user.getUsername());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        mDBListenerBD = daoBD.getByUserKey("-MutmLS6FPIkhneAJSGT").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    backdrop = data.getValue(Backdrop.class);
                    backdrop.setBackdropKey(data.getKey());
                }
                if(backdrop!=null){
                    Picasso.get().load(backdrop.getBackdropUrl()).fit().centerCrop().into(IVEditBackdrop);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        mDBListenerPI = daoPI.getByUserKey("-MutmLS6FPIkhneAJSGT").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    profileImage = data.getValue(ProfileImage.class);
                    profileImage.setProfileImageKey(data.getKey());
                }
                if(profileImage!=null){
                    Picasso.get().load(profileImage.getProfileImageUrl()).fit().centerCrop().into(IVEditProfileImage);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        BTNEditBackdrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBDDialog();
            }
        });

        IBEditProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPIDialog();
            }
        });

        IBEditName.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                openENDialog();
            }
        });


        Button BtnAddRecentPlace = view.findViewById(R.id.BTNAddRecentPlace);
        View.OnClickListener OCLAddRecentPlace = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.NextToAddRecentPlace);
            }
        };
        BtnAddRecentPlace.setOnClickListener(OCLAddRecentPlace);

        IVEditBackdrop.setClipToOutline(true);
    }

    public void openBDDialog() {
        EditBackdropDialog editBackdropDialog = EditBackdropDialog.newInstance(1, backdrop);
        editBackdropDialog.show(getParentFragmentManager(), "Edit Backdrop");
    }

    public void openPIDialog() {
        EditProfileImageDialog editProfileImageDialog = EditProfileImageDialog.newInstance(1, profileImage);
        editProfileImageDialog.show(getParentFragmentManager(), "Edit Profile Image");
    }

    public void openENDialog() {
        EditNameDialog editNameDialog = EditNameDialog.newInstance(1, TVEditName.getText().toString());
        editNameDialog.show(getParentFragmentManager(), "Edit Username");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        daoBD.removeListener(mDBListenerBD);
        daoPI.removeListener(mDBListenerPI);
    }
}