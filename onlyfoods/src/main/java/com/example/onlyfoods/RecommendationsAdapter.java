package com.example.onlyfoods;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.onlyfoods.R;
import com.example.onlyfoods.Recommendation;

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

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.item_number);
            messageButton = (TextView) itemView.findViewById(R.id.content);
        }
    }

    // Store a member variable for the recommendations
    private List<Recommendation> mRecommendations;

    // Pass in the recommendation array into the constructor
    public RecommendationsAdapter(List<Recommendation> recommendations) {
        mRecommendations = recommendations;
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
        Recommendation recommendation = mRecommendations.get(position);

        // Set item views based on your views and data model
        TextView textView = holder.nameTextView;
        textView.setText(recommendation.getName());
        TextView button = holder.messageButton;
        button.setText(recommendation.isOnline() ? "Message" : "Offline");
        button.setEnabled(recommendation.isOnline());
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mRecommendations.size();
    }

}