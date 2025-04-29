package com.maalelan.postcardstorehouse.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.maalelan.postcardstorehouse.R;

/**
 * MainActivity acts as the main entry point of the application and sets up the navigation architecture.
 * This activity uses the Navigation Component and BottomNavigationView
 * to provide a smooth navigation experience for the user.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the user interface layout to activity_main.xml
        setContentView(R.layout.activity_main);

        // Find the BottomNavigationView in the layout by its ID
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav_view);

        // Retrieve the NavHostFragment, which serves as the container for the navigation graph.
        // This fragment hosts all the application's navigable fragments.
        NavHostFragment navHostFragment = (NavHostFragment)
                getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

        // Ensure that the NavHostFragment was successfully found before accessing its NavController
        if(navHostFragment != null && bottomNav != null) {
            // Obtain the NavController from the NavHostFragment.
            // NavController manages the app navigation.
            NavController navController = navHostFragment.getNavController();

                // Link the BottomNavigationView with the NavController.
                // This enables automated navigation handling when the user selects
                // an item from the bottom navigation bar.
                NavigationUI.setupWithNavController(bottomNav, navController);
        } else {
            Log.e("MainActivity", "NavHostFragment / bottomNav is null.");
        }
    }
}