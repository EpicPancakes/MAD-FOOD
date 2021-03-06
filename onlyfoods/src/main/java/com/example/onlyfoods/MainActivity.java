package com.example.onlyfoods;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.onlyfoods.Fragments.MyProfileFragment;
import com.example.onlyfoods.Fragments.SpinTheWheelFragment;
import com.example.onlyfoods.Fragments.leyhangDiscoverFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavHostFragment host =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.NHFMain);
        NavController navController = host.getNavController();


        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        setupBottomNavMenu(navController);
        String intentFragment = getIntent().getExtras().getString("Fragment");

        switch (intentFragment){
            case "Restaurant":
                // Load corresponding fragment
                navController.navigate(R.id.DestDiscovery);
                break;
            case "Spin the wheel":
                // Load corresponding fragment
                navController.navigate(R.id.DestSpinTheWheel);
                break;
            case "My profile":
                break;
            case "Deals":
                navController.navigate(R.id.DestDeals);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bottom, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            Navigation.findNavController(this, R.id.NHFMain).navigateUp();
            return true;
        }
        catch (Exception ex)
        {
            return super.onOptionsItemSelected(item);
        }
    }

    private void setupBottomNavMenu(NavController navController) {
        BottomNavigationView bottomNav = findViewById(R.id.BottomNavView);
        NavigationUI.setupWithNavController(bottomNav, navController);
    }

}