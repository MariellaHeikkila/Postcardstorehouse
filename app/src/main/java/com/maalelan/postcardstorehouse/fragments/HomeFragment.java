package com.maalelan.postcardstorehouse.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.maalelan.postcardstorehouse.R;

/**
 * HomeFragment serves as the initial screen of the application.
 * It provides navigation buttons to other main sections of the app.
 */
public class HomeFragment extends Fragment {


    /**
     * Inflates the layout for this fragment.
     * This method only handles the layout inflation and returns the root view.
     *
     * @param inflater The LayoutInflater object to inflate views
     * @param container The parent view that the fragment's UI should be attached to
     * @param savedInstanceState The previously saved state of the fragment
     * @return The root View of the inflated layout
     */
@Nullable
@Override
public View onCreateView(@NonNull LayoutInflater inflater,
                         @Nullable ViewGroup container,
                         @Nullable Bundle savedInstanceState) {
    // Inflate the layout for this fragment and return the root view
    return inflater.inflate(R.layout.home_fragment, container, false);
}

    /**
     * Called immediately after onCreateView() has returned, but before any saved state
     * has been restored into the view. This is where view initialization, finding views,
     * and setting up listeners should happen.
     *
     * @param view The View returned by onCreateView()
     * @param savedInstanceState The previously saved state of the fragment
     */
@Override
public void onViewCreated(@NonNull View view,
                          @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    // Find navigation buttons from the layout
    Button addButton = view.findViewById(R.id.button_add);
    Button galleryButton = view.findViewById(R.id.button_gallery);

    // Set click listener for the Add button to navigate to the Add screen
    addButton.setOnClickListener(v ->
            NavHostFragment.findNavController(this).navigate(R.id.navigation_add));
    // Set click listener for the Gallery button to navigate to the Gallery screen
    galleryButton.setOnClickListener(v ->
            NavHostFragment.findNavController(this).navigate(R.id.navigation_gallery));
}
}