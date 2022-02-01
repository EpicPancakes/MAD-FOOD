package com.example.onlyfoods;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlyfoods.DAOs.DAORestaurant;
import com.example.onlyfoods.Models.RecentPlace;
import com.example.onlyfoods.Models.Restaurant;
import com.example.onlyfoods.placeholder.PlaceholderContent.PlaceholderItem;
import com.example.onlyfoods.databinding.FragmentRecentPlacesItemBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyRecentPlacesRecyclerViewAdapter extends RecyclerView.Adapter<MyRecentPlacesRecyclerViewAdapter.ViewHolder> {

    private ArrayList<RecentPlace> list = new ArrayList<>();
    private final List<PlaceholderItem> mValues;
    Context context;
    private OnItemClickListener mListener;

    public MyRecentPlacesRecyclerViewAdapter(Context ctx, List<PlaceholderItem> items, ArrayList<RecentPlace> recentPlacesList) {
        context = ctx;
        mValues = items;
        list = recentPlacesList;
    }

    public void setItems(ArrayList<RecentPlace> rp){
        list.addAll(rp);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentRecentPlacesItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        RecentPlace rp = list.get(position);

        DAORestaurant daoRest = new DAORestaurant();
        daoRest.get(rp.getRestaurantKey()).addOnSuccessListener(suc ->{
            Restaurant restaurant = suc.getValue(Restaurant.class);
            assert restaurant != null;
            holder.TVRestaurantName.setText(restaurant.getRestaurantName());
            holder.TVCategory.setText(restaurant.getCategory());
            holder.TVDaysAgo.setText(new SimpleDateFormat("dd/MM/yyyy").format(rp.getDate()));
            holder.TVLocation.setText(restaurant.getLocation());
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).id);
            holder.mContentView.setText(mValues.get(position).content);
        }).addOnFailureListener(er ->{
            Toast.makeText(context, "" + er.getMessage(), Toast.LENGTH_SHORT).show();
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
    View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        public final TextView mIdView;
        public final TextView mContentView;
        public final TextView TVRestaurantName;
        public final TextView TVDaysAgo;
        public final TextView TVCategory;
        public final TextView TVLocation;
        public PlaceholderItem mItem;

        public ViewHolder(FragmentRecentPlacesItemBinding binding) {
            super(binding.getRoot());
            mIdView = binding.itemNumber;
            mContentView = binding.content;
            TVRestaurantName = binding.TVRPRestaurant;
            TVDaysAgo = binding.TVRPDaysAgo;
            TVCategory = binding.TVRPCategory;
            TVLocation = binding.TVRPCity;

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
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
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select Action");
            MenuItem edit = menu.add(Menu.NONE, 1, 1, "Edit");
            MenuItem delete = menu.add(Menu.NONE, 2, 2, "Delete");

            edit.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if(mListener != null) {
                int position = getAbsoluteAdapterPosition();
                if(position != RecyclerView.NO_POSITION) {
                    switch (item.getItemId()){
                        case 1:
                            mListener.onEditClick(position);
                            return true;
                        case 2:
                            mListener.onDeleteClick(position);
                            return true;
                    }
                }
            }
            return false;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onEditClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}