package com.example.onlyfoods.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.onlyfoods.DAOs.DAORestaurant;
import com.example.onlyfoods.Models.Review;
import com.example.onlyfoods.Models.Restaurant;
import com.example.onlyfoods.databinding.FragmentReviewsItemBinding;
import com.example.onlyfoods.placeholder.PlaceholderContent.PlaceholderItem;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class UserReviewsRecyclerViewAdapter extends RecyclerView.Adapter<UserReviewsRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Review> list = new ArrayList<>();
    Context context;
    private OnItemClickListener mListener;

    public UserReviewsRecyclerViewAdapter(Context ctx, ArrayList<Review> reviewsList) {
        context = ctx;
        list = reviewsList;
    }

    public void setItems(ArrayList<Review> rp){
        list.addAll(rp);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentReviewsItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Review review = list.get(position);

        DAORestaurant daoRest = new DAORestaurant();
        daoRest.get(review.getRestaurantKey()).addOnSuccessListener(suc ->{
            Restaurant restaurant = suc.getValue(Restaurant.class);
            assert restaurant != null;
            holder.TVRestaurantName.setText(restaurant.getRestaurantName());
            holder.TVCategory.setText(restaurant.getCategory());
            holder.TVDaysAgo.setText(new SimpleDateFormat("dd/MM/yyyy").format(review.getDate()));
            holder.TVReviewMessage.setText(review.getReviewMsg());

            if (restaurant.getRestaurantImageUrl() != null) {
                Picasso.get().load(restaurant.getRestaurantImageUrl()).fit().centerCrop().into(holder.IVReviewRestaurant);
            }

        }).addOnFailureListener(er ->{
            Toast.makeText(context, "" + er.getMessage(), Toast.LENGTH_SHORT).show();
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView TVRestaurantName;
        public final TextView TVDaysAgo;
        public final TextView TVCategory;
        public final TextView TVReviewMessage;
        public final ImageView IVReviewRestaurant;


        public ViewHolder(FragmentReviewsItemBinding binding) {
            super(binding.getRoot());
            TVRestaurantName = binding.TVReviewRestaurantName;
            TVDaysAgo = binding.TVReviewDaysAgo;
            TVCategory = binding.TVReviewCategory;
            TVReviewMessage = binding.TVReviewMessage;
            IVReviewRestaurant = binding.IVReviewRestaurant;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mListener != null) {
                int position = getAbsoluteAdapterPosition();
                if(position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);
                }
            }
        }

        @Override
        public String toString() {
            return super.toString() + " '" + "'";
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}