package com.example.onlyfoods.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.onlyfoods.databinding.LeyhangFilterItemBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;


public class FiltersRecyclerViewAdapter extends RecyclerView.Adapter<FiltersRecyclerViewAdapter.ViewHolder> {

    private OnItemClickListener mListener;
    private final List<String> categories;
    private String sessionUserKey;

    public FiltersRecyclerViewAdapter(List<String> categories) {
        this.categories = categories;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        sessionUserKey = FirebaseAuth.getInstance().getCurrentUser().getUid();
        return new ViewHolder(LeyhangFilterItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String category = categories.get(position);
        holder.TVFilterCategory.setText(category);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView TVFilterCategory;

        public ViewHolder(LeyhangFilterItemBinding binding) {
            super(binding.getRoot());
            TVFilterCategory = binding.TVFilterCategory;
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