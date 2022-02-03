package com.example.onlyfoods.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.onlyfoods.DAOs.DAOProfileImage;
import com.example.onlyfoods.DAOs.DAOUser;
import com.example.onlyfoods.Models.ProfileImage;
import com.example.onlyfoods.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class EditNameDialog extends AppCompatDialogFragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText ETEditName;

    private DAOUser daoUser;
    private String username;
    private View view;

    public static EditNameDialog newInstance(int arg, String username) {
        EditNameDialog frag = new EditNameDialog();
        Bundle args = new Bundle();
        args.putInt("count", arg);
        frag.setArguments(args);
        frag.setUsername(username);
        return frag;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_edit_name, null);
        daoUser = new DAOUser();

        ETEditName = view.findViewById(R.id.ETEditName);

        builder.setView(view)
                .setTitle("Edit Username")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        editName();
                    }
                });


        return builder.create();
    }

    private void editName() {
        HashMap<String, Object> usernameHM = new HashMap<>();
        usernameHM.put("username", ETEditName.getText().toString());
        daoUser.update("-MutmLS6FPIkhneAJSGT", usernameHM).addOnSuccessListener(suc -> {
            Toast.makeText(view.getContext(), "Username successfully updated", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(er -> {
            Toast.makeText(view.getContext(), "" + er, Toast.LENGTH_SHORT).show();
        });
    }
}
