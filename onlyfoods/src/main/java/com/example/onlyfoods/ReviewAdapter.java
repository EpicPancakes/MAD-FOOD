package com.example.onlyfoods;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlyfoods.DAOs.DAOProfileImage;
import com.example.onlyfoods.DAOs.DAOReview;
import com.example.onlyfoods.DAOs.DAOUser;
import com.example.onlyfoods.Models.ProfileImage;
import com.example.onlyfoods.Models.Recommendation;
import com.example.onlyfoods.Models.Review;
import com.example.onlyfoods.Models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends
        RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private List<Review> lists;
    private DAOProfileImage daoPI;
    private DAOUser daoUser;
    private DAOReview daoRev;
    private String sessionUserKey;
    private Context mContext;
    View view;

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
                                //TODO DELETE PART
                                Toast.makeText(mContext, "Delete Clicked", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return true;
                    }

                    );



                }
            });
        }

            //Working
//        if(list.getUserKey().equals(sessionUserKey)){
//            holder.menuPopUp.setVisibility(View.VISIBLE);
//            holder.menuPopUp.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Bundle args = new Bundle();
//                    args.putString("reviewKey", list.getReviewKey());
//                    args.putString("restaurantKey", list.getRestaurantKey());
//                    Navigation.findNavController(v).navigate(R.id.DestEditReview, args);
//                }
//            });
//        }

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

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public ReviewAdapter(ArrayList<Review> list){
        //REMOVED CONTEXT FROM THE PARAMETER OF THE CONSTRCUTOR ABV ^
//        this.mContext =mContext;
        this.lists = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView IVProfilePicture, menuPopUp;
        TextView nameTextView, TVReviewMsg, TVReviewDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            IVProfilePicture = (ImageView) itemView.findViewById(R.id.IVProfilePicture);
            menuPopUp = (ImageView) itemView.findViewById(R.id.menuMore);
            nameTextView = (TextView) itemView.findViewById(R.id.userName);
            TVReviewMsg = (TextView) itemView.findViewById(R.id.TVReview);
            TVReviewDate = (TextView) itemView.findViewById(R.id.rvDate);
        }
    }
}
