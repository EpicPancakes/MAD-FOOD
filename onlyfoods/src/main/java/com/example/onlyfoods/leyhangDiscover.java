package com.example.onlyfoods;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.onlyfoods.Fragments.DiscoverFragment;

public class leyhangDiscover extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leyhang_discover);

        FrameLayout layout = new FrameLayout(this);
        layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        layout.setId(R.id.layout);
        setContentView(layout);

        getSupportFragmentManager().beginTransaction().add(R.id.layout, new DiscoverFragment()).commit();
    }
}