package com.example.onlyfoods;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.onlyfoods.Adapters.FiltersRecyclerViewAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link leyhangCategories#newInstance} factory method to
 * create an instance of this fragment.
 */
public class leyhangCategories extends Fragment implements FiltersRecyclerViewAdapter.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ArrayList<String> categories = new ArrayList<>();
    private FiltersRecyclerViewAdapter adapter;



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public leyhangCategories() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment leyhangFilter.
     */
    // TODO: Rename and change types and number of parameters
    public static leyhangCategories newInstance(String param1, String param2) {
        leyhangCategories fragment = new leyhangCategories();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leyhang_filter, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            adapter = new FiltersRecyclerViewAdapter(categories);
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(leyhangCategories.this);

        }
        loadData();
        return view;
    }

    private void loadData() {
        categories.add("Local");
        categories.add("Western");
        categories.add("Japanese");
        adapter.notifyItemRangeInserted(0,3);
    }

    @Override
    public void onItemClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("categoryString", categories.get(position));
//        Navigation.findNavController(getView()).navigate(R.id.NextToUserProfile, bundle);
        Toast.makeText(getContext(), "Viewing "+categories.get(position)+" restaurants", Toast.LENGTH_SHORT).show();
    }
}