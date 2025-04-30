package com.maalelan.postcardstorehouse.navigation;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

/**
 * Navigation manager for the MVVM architecture.
 * This class handles navigation actions while maintaining separation
 * between ViewModel and View layers.
 */
public class NavigationManager {

    // Singleton instance
    private static NavigationManager instance;

    // Private constructor to prevent direct instantiation
    private NavigationManager() {}

    /**
     * Get the singleton instance of NavigationManager
     * @return The NavigationManager instance
     */
    public static NavigationManager getInstance() {
        if (instance == null) {
            instance = new NavigationManager();
        }
        return instance;
    }

    /**
     *  navigate using NavDirections
     * @param fragment The current fragment
     * @param directions The navigation directions
     */
    public void navigate(Fragment fragment, NavDirections directions) {
        NavController navController = NavHostFragment.findNavController(fragment);
        navController.navigate(directions);
    }


}
