package com.example.onlyfoods.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.onlyfoods.DAOs.DAOBackdrop;
import com.example.onlyfoods.EditBackdropDialog;
import com.example.onlyfoods.Models.Backdrop;
import com.example.onlyfoods.Models.RecentPlace;
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

    private DAOBackdrop daoBD;
    private Backdrop backdrop;
    private ImageView IVEditBackdrop;

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

        daoBD = new DAOBackdrop();
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

        daoBD.getByUserKey("testUser").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    backdrop = data.getValue(Backdrop.class);
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

        BTNEditBackdrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
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

    public void openDialog() {
        EditBackdropDialog editBackdropDialog = new EditBackdropDialog();
        editBackdropDialog.show(getParentFragmentManager(), "Edit Backdrop");
    }
}