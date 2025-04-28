package com.maalelan.postcardstorehouse.controllers;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.maalelan.postcardstorehouse.R;

/**
 * Centralized controller for handling all navigation in the application.
 * This singleton class manages navigation actions across different screens.
 */
public class NavigationController {

    // Singleton instance
    private static NavigationController instance;

    // Private constructor to prevent direct instantiation
    private NavigationController() {}

    /**
     * Get the singleton instance of NavigationController
     * @return The NavigationController instance
     */
    public static NavigationController getInstance() {
        if (instance == null) {
            instance = new NavigationController();
        }
        return instance;
    }

    /**
     * Navigate to Add screen
     * @param fragment The current fragment
     */
    public void navigateToAdd(Fragment fragment) {
        NavController navController = NavHostFragment.findNavController(fragment);
        navController.navigate(R.id.navigation_add);
    }

    /**
     * Navigate to Gallery screen
     * @param fragment The current fragment
     */
    public void navigateToGallery(Fragment fragment) {
        NavController navController = NavHostFragment.findNavController(fragment);
        navController.navigate(R.id.navigation_gallery);
    }
}
