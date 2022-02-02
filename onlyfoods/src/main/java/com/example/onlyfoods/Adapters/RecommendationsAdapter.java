package com.example.onlyfoods.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.onlyfoods.DAOs.DAORestaurant;
import com.example.onlyfoods.Models.Recommendation;
import com.example.onlyfoods.Models.Restaurant;
import com.example.onlyfoods.R;

import java.text.SimpleDateFormat;
import java.util.List;

// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class RecommendationsAdapter extends
        RecyclerView.Adapter<RecommendationsAdapter.ViewHolder> {

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView nameTextView;
        public TextView messageButton;
        public TextView TVRecommendedRestaurantName;
        public TextView TVRecommendationLocation;
        public TextView TVRecommendedByUser;
        public TextView TVRecommendationDate;
        public TextView TVRecommendationMessage;


        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.item_number);
            messageButton = (TextView) itemView.findViewById(R.id.content);
            TVRecommendedRestaurantName = (TextView) itemView.findViewById(R.id.TVRecommendedRestaurantName);
            TVRecommendationLocation = (TextView) itemView.findViewById(R.id.TVRecommendationLocation);
            TVRecommendedByUser = (TextView) itemView.findViewById(R.id.TVRecommendedByUser);
            TVRecommendationDate = (TextView) itemView.findViewById(R.id.TVRecommendationDate);
            TVRecommendationMessage = (TextView) itemView.findViewById(R.id.TVRecommendationMessage);
        }
    }

    // Store a member variable for the recommendations
    private List<Recommendation> recs;

    // Pass in the recommendation array into the constructor
    public RecommendationsAdapter(List<Recommendation> recs) {
        this.recs = recs;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public RecommendationsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View recommendationView = inflater.inflate(R.layout.fragment_recommendations_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(recommendationView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(RecommendationsAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        Recommendation rec = recs.get(position);

        // Set item views based on your views and data model
        TextView textView = holder.nameTextView;
        textView.setText(rec.getRestaurantKey());
        TextView button = holder.messageButton;

        TextView TVRecommendedRestaurantName = holder.TVRecommendedRestaurantName;
        TextView TVRecommendationLocation = holder.TVRecommendationLocation;
        TextView TVRecommendedByUser = holder.TVRecommendedByUser;
        TextView TVRecommendationDate = holder.TVRecommendationDate;
        TextView TVRecommendationMessage = holder.TVRecommendationMessage;

        // TODO: Replace user key with actual user name
        TVRecommendedByUser.setText(rec.getRecommendedUserKey());
        TVRecommendationDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(rec.getDate()));
        TVRecommendationMessage.setText(rec.getRecommendationMsg());

        DAORestaurant daoRest = new DAORestaurant();
        daoRest.get(rec.getRestaurantKey()).addOnSuccessListener(suc ->{
            Restaurant restaurant = suc.getValue(Restaurant.class);
            assert restaurant != null;
            TVRecommendedRestaurantName.setText(restaurant.getRestaurantName());
            TVRecommendationLocation.setText(restaurant.getLocation());
        }).addOnFailureListener(er ->{
            Toast.makeText(TVRecommendedRestaurantName.getContext(), "" + er.getMessage(), Toast.LENGTH_SHORT).show();
        });


//        button.setText(rec.isOnline() ? "Message" : "Offline");
//        button.setEnabled(rec.isOnline());
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return recs.size();
    }

}