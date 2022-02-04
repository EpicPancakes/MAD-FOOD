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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.onlyfoods.DAOs.DAOProfileImage;
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

public class EditProfileImageDialog extends AppCompatDialogFragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Button BTNChooseProfileImage;
    private ImageView IVEditProfileImage;
    private ProgressBar PBEditProfileImage;

    private Uri mProfileImageImageUri;

    private StorageReference mStorageRef;
    private FirebaseStorage mStorage;
    private DAOProfileImage daoBD;
    ActivityResultLauncher<String> mTakePhoto;

    private ProfileImage profileImage;
    private View view;

    public static EditProfileImageDialog newInstance(int arg, ProfileImage profileImage) {
        EditProfileImageDialog frag = new EditProfileImageDialog();
        Bundle args = new Bundle();
        args.putInt("count", arg);
        frag.setArguments(args);
        frag.setProfileImage(profileImage);
        return frag;
    }

    public void setProfileImage(ProfileImage profileImage) {
        this.profileImage = profileImage;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_edit_profile_image, null);

        BTNChooseProfileImage = view.findViewById(R.id.BTNChooseProfileImage);
        IVEditProfileImage = view.findViewById(R.id.IVUploadedProfileImage);
        PBEditProfileImage = view.findViewById(R.id.PBEditProfileImage);

        mStorage = FirebaseStorage.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("profileImage");
        daoBD = new DAOProfileImage();

        mTakePhoto = registerForActivityResult(
                new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {

                        if (result != null) {
                            mProfileImageImageUri = result;
                        }

                        Picasso.get().load(mProfileImageImageUri).into(IVEditProfileImage);

                    }
                }
        );

        BTNChooseProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });


        builder.setView(view)
                .setTitle("Edit Profile Image")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("upload", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        uploadFile();
                    }
                });


        return builder.create();
    }

    private void openFileChooser() {
        mTakePhoto.launch("image/*");
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = view.getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if (mProfileImageImageUri != null) {
            deleteFile();
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mProfileImageImageUri));

            fileReference.putFile(mProfileImageImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    PBEditProfileImage.setProgress(0);
                                }
                            }, 500);

                            Toast.makeText(view.getContext(), "Upload successful", Toast.LENGTH_LONG).show();
                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while(!urlTask.isSuccessful());
                            Uri downloadUrl = urlTask.getResult();

                            ProfileImage profileImage = new ProfileImage("-MutmLS6FPIkhneAJSGT", downloadUrl.toString());

                            daoBD.add(profileImage);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            PBEditProfileImage.setProgress((int) progress);
                        }
                    });

        } else {
            Toast.makeText(view.getContext(), "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteFile(){
        if(profileImage != null){
            String profileImageKey = profileImage.getProfileImageKey();
            StorageReference imageRef = mStorage.getReferenceFromUrl(profileImage.getProfileImageUrl());
            imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    daoBD.remove(profileImageKey);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Error while deleting previous file", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
