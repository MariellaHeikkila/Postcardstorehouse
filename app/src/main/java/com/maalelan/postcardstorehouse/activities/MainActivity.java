package com.maalelan.postcardstorehouse.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.maalelan.postcardstorehouse.R;
import com.maalelan.postcardstorehouse.navigation.NavigationManager;

/**
 * MainActivity acts as the main entry point of the application and sets up the navigation architecture.
 * This activity uses the Navigation Component and BottomNavigationView.
 */
public class MainActivity extends AppCompatActivity {

    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the user interface layout to activity_main.xml
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        setupNavigation();
    }

    /**
     * Set up navigation components including NavController and BottomNavigationView
     */
    private void setupNavigation() {

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
            navController = navHostFragment.getNavController();

            // Set up top level destinations for proper Up button behavior
            AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.navigation_home,
                    R.id.navigation_add,
                    R.id.navigation_gallery
            ).build();

            // Connect action bar with nav controller for title updates
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

            // Connect the BottomNavigationView with the NavController.
            NavigationUI.setupWithNavController(bottomNav, navController);
            // Initialize navigationManager singleton
            NavigationManager.getInstance();
        } else {
            Log.e("MainActivity", "NavHostFragment / bottomNav is null.");
        }
    }
}
