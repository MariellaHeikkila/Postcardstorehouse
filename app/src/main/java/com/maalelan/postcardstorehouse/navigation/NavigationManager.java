package com.maalelan.postcardstorehouse.navigation;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.maalelan.postcardstorehouse.utils.Event;

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

    /**
     * Navigare to a destination by resource ID
     * @param fragment The current fragment
     * @param destinationId The destination resource ID
     */
    public void navigate(Fragment fragment, int destinationId) {
        NavController navController = NavHostFragment.findNavController(fragment);
        navController.navigate(destinationId);
    }

    /**
     * Navigate back
     * @param fragment The current fragment
     */
    public void navigateBack(Fragment fragment) {
        NavController navController = NavHostFragment.findNavController(fragment);
        navController.navigateUp();
    }

    /**
     * Set up navigation events observer for a fragment
     * @param fragment The fragment to observe navigation events
     * @param lifecycleOwner the lifecycle owner ( the fragments viewlifecycleOwner)
     * @param navigationEvents The navigation events LiveData from ViewModel
     */
    public void setupNavigationObserver(
            Fragment fragment,
            LifecycleOwner lifecycleOwner,
            LiveData<Event<NavigationEvent>> navigationEvents) {

        navigationEvents.observe(lifecycleOwner, event -> {
            if (event != null) {
                // Handle the navigation event only once
                NavigationEvent navigationEvent = event.getContentIfNotHandled();
                if (navigationEvent != null) {
                    handleNavigationEvent(fragment, navigationEvent);
                }
            }
        });
    }

    /**
     * Handle a navigation event
     * @param fragment The fragment to navigate from
     * @param navigationEvent The navigation event to handle
     */
    private void handleNavigationEvent(Fragment fragment, NavigationEvent navigationEvent) {
        NavController navController = NavHostFragment.findNavController(fragment);

        if (navigationEvent instanceof NavigationEvent.ToDestination) {
            navController.navigate(((NavigationEvent.ToDestination) navigationEvent).getDestinationId());
        }
        if (navigationEvent instanceof NavigationEvent.ToDirection) {
            navController.navigate(((NavigationEvent.ToDirection) navigationEvent).getDirections());
        }
        if (navigationEvent instanceof NavigationEvent.Back) {
            navController.navigateUp();
        }
        if (navigationEvent instanceof NavigationEvent.BackToDestination) {
            navController.popBackStack(
                    ((NavigationEvent.BackToDestination) navigationEvent).getDestinationId(), false);
        }

    }
}
