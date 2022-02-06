package com.example.onlyfoods.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlyfoods.DAOs.DAORestaurant;
import com.example.onlyfoods.DAOs.DAOUser;
import com.example.onlyfoods.Models.Deal;
import com.example.onlyfoods.Models.Restaurant;
import com.example.onlyfoods.Models.User;
import com.example.onlyfoods.databinding.FragmentDealsItemBinding;
import com.example.onlyfoods.databinding.LeyhangResultsItemBinding;
import com.example.onlyfoods.placeholder.PlaceholderContent.PlaceholderItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class DealsRecyclerViewAdapter extends RecyclerView.Adapter<DealsRecyclerViewAdapter.ViewHolder> {

    private OnItemClickListener mListener;
    private final List<Deal> deals;
    private DAOUser daoUser;
    private String sessionUserKey;


    public DealsRecyclerViewAdapter(List<Deal> deals) {
        this.deals = deals;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        daoUser = new DAOUser();
        sessionUserKey = FirebaseAuth.getInstance().getCurrentUser().getUid();

        return new ViewHolder(FragmentDealsItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Deal deal = deals.get(position);
        holder.TVDealsTitle.setText(deal.getDealTitle());
        holder.BtnDealsPrice.setText(deal.getPrice());

        DAORestaurant daoRest = new DAORestaurant();
        daoRest.getRestaurantsByKey(deal.getRestaurantKey()).addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                Restaurant restaurant = dataSnapshot.getValue(Restaurant.class);
                holder.TVDealsRestaurantName.setText(restaurant.getRestaurantName());
                holder.TVDealsCategory.setText(restaurant.getCategory());
                Picasso.get().load(restaurant.getRestaurantImageUrl()).fit().centerCrop().into(holder.IVDealsRestaurantLogo);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });



    }

    @Override
    public int getItemCount() {
        return deals.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView IVDealsRestaurantLogo;
        public final TextView TVDealsRestaurantName;
        public final TextView TVDealsCategory;
        public final TextView TVDealsTitle;
        public final Button BtnDealsPrice;

        public ViewHolder(FragmentDealsItemBinding binding) {
            super(binding.getRoot());
            IVDealsRestaurantLogo = binding.IVDealsRestaurantLogo;
            TVDealsRestaurantName = binding.TVDealsRestaurantName;
            TVDealsCategory = binding.TVDealsCategory;
            TVDealsTitle = binding.TVDealsTitle;
            BtnDealsPrice = binding.BtnDealsPrice;

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
}