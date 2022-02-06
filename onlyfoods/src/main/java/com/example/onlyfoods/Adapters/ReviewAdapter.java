package com.example.onlyfoods.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlyfoods.DAOs.DAOProfileImage;
import com.example.onlyfoods.DAOs.DAOReview;
import com.example.onlyfoods.DAOs.DAOUser;
import com.example.onlyfoods.Models.ProfileImage;
import com.example.onlyfoods.Models.Review;
import com.example.onlyfoods.Models.User;
import com.example.onlyfoods.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReviewAdapter extends
        RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private List<Review> lists;
    private DAOProfileImage daoPI;
    private DAOUser daoUser;
    private DAOReview daoRev;
    private String sessionUserKey;
    private Context mContext;
    private User user;
    View view;
    private OnItemClickListener mListener;


    public ReviewAdapter(List<Review> lists) {
        this.lists = lists;
    }


    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View reviewView = LayoutInflater.from(mContext).inflate(R.layout.reviews_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(reviewView);
        sessionUserKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.ViewHolder holder, int position) {
        Review list = lists.get(position);
        TextView textView = holder.nameTextView;
        ImageView IVProfilePicture = holder.IVProfilePicture;


        TextView TVReviewMsg = holder.TVReviewMsg;
        TextView TVReviewDate = holder.TVReviewDate;

        daoUser = new DAOUser();
        daoPI = new DAOProfileImage();
        daoRev = new DAOReview();

        daoUser.getByUserKeyOnce(list.getUserKey()).addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                User reviewByUser = dataSnapshot.getValue(User.class);
                textView.setText(reviewByUser.getUsername());

            }
        });



        if(list.getUserKey().equals(sessionUserKey)){
            holder.menuPopUp.setVisibility(View.VISIBLE);
            holder.menuPopUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Might need to call the context
                    PopupMenu popupMenu = new PopupMenu(mContext,v);
                    popupMenu.inflate(R.menu.review_menu);
                    popupMenu.show();
                    popupMenu.setOnMenuItemClickListener(item -> {
                        switch (item.getItemId()){
                            case R.id.menu_edit:
                                Bundle args = new Bundle();
                                args.putString("reviewKey", list.getReviewKey());
                                args.putString("restaurantKey", list.getRestaurantKey());
                                Navigation.findNavController(v).navigate(R.id.DestEditReview, args);
                                break;
                            case R.id.menu_delete:
                                new AlertDialog.Builder(mContext)
                                    .setTitle("Delete entry")
                                    .setMessage("Are you sure you want to delete this entry?")

                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            deleteReview(list.getReviewKey());
                                        }
                                     })

                                    // A null listener allows the button to dismiss the dialog and take no further action.
                                    .setNegativeButton(android.R.string.no, null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
//                                Toast.makeText(mContext, "Delete Clicked", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return true;
                    }

                    );



                }
            });
        }


        daoPI.getByUserKey(list.getUserKey()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data: snapshot.getChildren()) {
                    if(data.exists()){
                        ProfileImage profileImage = data.getValue(ProfileImage.class);
                        if (profileImage.getProfileImageUrl() != null) {
                            Picasso.get().load(profileImage.getProfileImageUrl()).fit().centerCrop().into(IVProfilePicture);
                        }
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        TVReviewDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(list.getDate()));
        TVReviewMsg.setText(list.getReviewMsg());


    }

    private void deleteReview(String revKey){
        daoRev.remove(revKey).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
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

                            }).addOnFailureListener(er ->
                            {

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

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public ReviewAdapter(ArrayList<Review> list){
        this.lists = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView IVProfilePicture, menuPopUp;
        TextView nameTextView, TVReviewMsg, TVReviewDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            IVProfilePicture = (ImageView) itemView.findViewById(R.id.IVProfilePicture);
            menuPopUp = (ImageView) itemView.findViewById(R.id.menuMore);
            nameTextView = (TextView) itemView.findViewById(R.id.userName);
            TVReviewMsg = (TextView) itemView.findViewById(R.id.TVReview);
            TVReviewDate = (TextView) itemView.findViewById(R.id.rvDate);

            itemView.setOnClickListener(this);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + "'";
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                int position = getAbsoluteAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);
                }
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}
