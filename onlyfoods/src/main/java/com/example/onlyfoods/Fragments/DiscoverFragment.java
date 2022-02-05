package com.example.onlyfoods.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlyfoods.Adapters.DiscoverRecyclerViewAdapter;
import com.example.onlyfoods.R;

public class DiscoverFragment extends Fragment {

    String[] names = {"Hello", "Gold", "Sakura", "Apatan"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_leyhang_actual_discover, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.CLDiscover);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(new DiscoverRecyclerViewAdapter(names));
        return view;
    }
}
