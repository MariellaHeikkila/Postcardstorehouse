package com.maalelan.postcardstorehouse.navigation;

import androidx.navigation.NavDirections;

/**
 *  Base class for navigation events in MVVM architecture.
 *  Used to encapsulate different navigation actions that can be triggered from ViewModel.
 */
public abstract class NavigationEvent {

    /**
     * Navigate to a specific destination by ID
     */
    public static class ToDestination extends NavigationEvent {
        private final int destinationId;

        public ToDestination(int destinationId) {
            this.destinationId = destinationId;
        }

        public int getDestinationId() {
            return destinationId;
        }
    }

    /**
     * Navigate using NavDirections (Safe Args)
     */
    public static class ToDirection extends NavigationEvent {
        private final NavDirections directions;

        public ToDirection(NavDirections directions) {
            this.directions = directions;
        }

        public NavDirections getDirections() {
            return directions;
        }
    }

    /**
     * Navigate back (equivalent to pressing the back button)
     */
    public static class Back extends NavigationEvent {
    }

    /**
     * Navigate back to a specific destination in the back stack
     */
    public static class BackToDestination extends NavigationEvent {
        private final int destinationId;

        public BackToDestination(int destinationId) {
            this.destinationId = destinationId;
        }

        public int getDestinationId() {
            return destinationId;
        }
    }
}
