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
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.onlyfoods.DAOs.DAOBackdrop;
import com.example.onlyfoods.Models.Backdrop;
import com.example.onlyfoods.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class EditBackdropDialog extends AppCompatDialogFragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Button BTNChooseBackdrop;
    private EditText ETBackdropFileName;
    private ImageView IVEditBackdrop;
    private ProgressBar PBEditBackdrop;

    private Uri mBackdropImageUri;

    private StorageReference mStorageRef;
    private FirebaseStorage mStorage;
    private DAOBackdrop daoBD;
    ActivityResultLauncher<String> mTakePhoto;

    private Backdrop backdrop;
    private View view;
    private String sessionUserKey;

    public static EditBackdropDialog newInstance(int arg, Backdrop backdrop) {
        EditBackdropDialog frag = new EditBackdropDialog();
        Bundle args = new Bundle();
        args.putInt("count", arg);
        frag.setArguments(args);
        frag.setBackdrop(backdrop);
        return frag;
    }

    public void setBackdrop(Backdrop backdrop) {
        this.backdrop = backdrop;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_edit_backdrop, null);
        
        sessionUserKey = FirebaseAuth.getInstance().getCurrentUser().getUid();

        BTNChooseBackdrop = view.findViewById(R.id.BTNChooseBackdrop);
        ETBackdropFileName = view.findViewById(R.id.ETBackdropFileName);
        IVEditBackdrop = view.findViewById(R.id.IVUploadedBackdrop);
        PBEditBackdrop = view.findViewById(R.id.PBEditBackdrop);

        mStorage = FirebaseStorage.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("backdrop");
        daoBD = new DAOBackdrop();

        mTakePhoto = registerForActivityResult(
                new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {

                        if (result != null) {
                            mBackdropImageUri = result;
                        }

                        Picasso.get().load(mBackdropImageUri).into(IVEditBackdrop);

                    }
                }
        );

        BTNChooseBackdrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });


        builder.setView(view)
                .setTitle("Edit Backdrop")
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
        if (mBackdropImageUri != null) {
            deleteFile();
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mBackdropImageUri));

            fileReference.putFile(mBackdropImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    PBEditBackdrop.setProgress(0);
                                }
                            }, 500);

                            Toast.makeText(view.getContext(), "Upload successful", Toast.LENGTH_LONG).show();
                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while(!urlTask.isSuccessful());
                            Uri downloadUrl = urlTask.getResult();

                            Backdrop backdrop = new Backdrop(sessionUserKey, ETBackdropFileName.getText().toString().trim(), downloadUrl.toString());

                            daoBD.add(backdrop);
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
                            PBEditBackdrop.setProgress((int) progress);
                        }
                    });

        } else {
            Toast.makeText(view.getContext(), "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteFile(){
        if(backdrop != null){
            String backdropKey = backdrop.getBackdropKey();
            StorageReference imageRef = mStorage.getReferenceFromUrl(backdrop.getBackdropUrl());
            imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    daoBD.remove(backdropKey);
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
