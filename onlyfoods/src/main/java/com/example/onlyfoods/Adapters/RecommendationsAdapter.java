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
import com.example.onlyfoods.DAOs.DAOUser;
import com.example.onlyfoods.Models.Recommendation;
import com.example.onlyfoods.Models.Restaurant;
import com.example.onlyfoods.Models.User;
import com.example.onlyfoods.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;

// Create the basic adapter extending from RecyclerView.Adapter
public class RecommendationsAdapter extends
        RecyclerView.Adapter<RecommendationsAdapter.ViewHolder> {

    private OnItemClickListener mListener;
    private List<Recommendation> recs;
    private DAOUser daoUser;

    public RecommendationsAdapter(List<Recommendation> recs) {
        this.recs = recs;
    }

    @Override
    public RecommendationsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View recommendationView = inflater.inflate(R.layout.fragment_recommendations_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(recommendationView);
        return viewHolder;
    }

    // Populate data into the item through holder
    @Override
    public void onBindViewHolder(RecommendationsAdapter.ViewHolder holder, int position) {
        Recommendation rec = recs.get(position);
        TextView TVRecommendedRestaurantName = holder.TVRecommendedRestaurantName;
        TextView TVRecommendationLocation = holder.TVRecommendationLocation;
        TextView TVRecommendedByUser = holder.TVRecommendedByUser;
        TextView TVRecommendationDate = holder.TVRecommendationDate;
        TextView TVRecommendationMessage = holder.TVRecommendationMessage;

        daoUser = new DAOUser();
        daoUser.getByUserKeyOnce(rec.getRecommendedUserKey()).addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                User recommendedByUser = dataSnapshot.getValue(User.class);
                TVRecommendedByUser.setText(recommendedByUser.getUsername());
            }
        });

        TVRecommendationDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(rec.getDate()));
        TVRecommendationMessage.setText(rec.getRecommendationMsg());

        DAORestaurant daoRest = new DAORestaurant();
        daoRest.getRestaurantsByKey(rec.getRestaurantKey()).addOnSuccessListener(suc ->{
            Restaurant restaurant = suc.getValue(Restaurant.class);
            assert restaurant != null;
            TVRecommendedRestaurantName.setText(restaurant.getRestaurantName());
            TVRecommendationLocation.setText(restaurant.getLocation());
            if (restaurant.getRestaurantImageUrl() != null) {
                Picasso.get().load(restaurant.getRestaurantImageUrl()).fit().centerCrop().into(holder.IVRecommendationImage);
            }
        }).addOnFailureListener(er ->{
            Toast.makeText(TVRecommendedRestaurantName.getContext(), "" + er.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return recs.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView TVRecommendedRestaurantName;
        public TextView TVRecommendationLocation;
        public TextView TVRecommendedByUser;
        public TextView TVRecommendationDate;
        public TextView TVRecommendationMessage;
        public final ImageView IVRecommendationImage;


        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            TVRecommendedRestaurantName = (TextView) itemView.findViewById(R.id.TVRecommendedRestaurantName);
            TVRecommendationLocation = (TextView) itemView.findViewById(R.id.TVRecommendationLocation);
            TVRecommendedByUser = (TextView) itemView.findViewById(R.id.TVRecommendedByUser);
            TVRecommendationDate = (TextView) itemView.findViewById(R.id.TVRecommendationDate);
            TVRecommendationMessage = (TextView) itemView.findViewById(R.id.TVRecommendationMessage);
            IVRecommendationImage = (ImageView) itemView.findViewById(R.id.IVRecommendationImage);


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

    }
}