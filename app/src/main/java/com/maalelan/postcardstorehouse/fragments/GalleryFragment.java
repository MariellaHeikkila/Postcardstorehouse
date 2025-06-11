package com.maalelan.postcardstorehouse.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.maalelan.postcardstorehouse.R;
import com.maalelan.postcardstorehouse.adapters.PostcardAdapter;
import com.maalelan.postcardstorehouse.navigation.NavigationManager;
import com.maalelan.postcardstorehouse.viewmodels.PostcardViewModel;

public class GalleryFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView emptyGalleryView;
    private PostcardAdapter adapter;
    private PostcardViewModel postcardViewModel;

    // Filter UI components
    private View filterPanel;
    private Button toggleFilterButton, applyFiltersButton, clearFiltersButton;
    private Spinner spinnerCountry, spinnerTopic, spinnerTagName;
    private CheckBox checkBoxFavorite, checkBoxSentByUser;
    private boolean isFilterPanelVisible = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.gallery_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize RecyclerView and ViewModel
        initializeViews(view);
        setupRecyclerView();

        // Initialize ViewModel to observe the data
        postcardViewModel = new ViewModelProvider(this).get(PostcardViewModel.class);

        try {
            // Observe postcards list
            postcardViewModel.getAllPostcards().observe(getViewLifecycleOwner(), postcards -> {
                // Update adapter with new data
                adapter.setPostcards(postcards);

                // Show "No postcards" message if db is empty
                if (postcards == null || postcards.isEmpty()) {
                    emptyGalleryView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    emptyGalleryView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            });

            postcardViewModel.getPostcardThumbnails().observe(getViewLifecycleOwner(), thumbnails -> {
                adapter.setPostcardThumbnails(thumbnails);
            });
        } catch (Exception e) {
            Toast.makeText(getContext(), "An error occurred: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    private void initializeViews(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        emptyGalleryView = view.findViewById(R.id.empty_gallery_text);

        // Filter UI components
        filterPanel = view.findViewById(R.id.filter_panel);
        toggleFilterButton = view.findViewById(R.id.button_toggle_filter);
        spinnerCountry = view.findViewById(R.id.spinner_country);
        spinnerTopic = view.findViewById(R.id.spinner_topic);
        spinnerTagName = view.findViewById(R.id.spinner_tag_name);
        checkBoxFavorite = view.findViewById(R.id.checkbox_filter_favorite);
        checkBoxSentByUser = view.findViewById(R.id.checkbox_filter_sent_by_user);
        applyFiltersButton = view.findViewById(R.id.button_apply_filters);
        clearFiltersButton = view.findViewById(R.id.button_clear_filters);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // Adapter for displaying the postcards
        adapter = new PostcardAdapter();
        recyclerView.setAdapter(adapter);

        // Isfavorite listener
        adapter.setPostcardFavoriteToggleListener(((postcard, isFavorite) -> {
            postcard.setFavorite(isFavorite);
            postcardViewModel.updateFavoriteStatus(postcard, isFavorite);
            Toast.makeText(getContext(),
                    "Postikortti " + (isFavorite ? "on lemppari" : "ei oo lemppari"),
                    Toast.LENGTH_SHORT).show();
        }));

        // Delete listener
        adapter.setOnPostcardDeleteListener(postcard -> {
            new AlertDialog.Builder(getContext())
                    .setTitle("Vahvista poisto")
                    .setMessage("Haluatko varmasti poistaa postikortin \"" + postcard.getTopic() + "\"?")
                    .setPositiveButton("Poista", (dialog, which) -> {
                        postcardViewModel.deletePostcardCompletely(postcard);
                        adapter.removePostcard(postcard);
                        Toast.makeText(getContext(), "Postikortti poistettu", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Peruuta", null)
                    .show();
        });

        // Detail click listener
        adapter.setOnPostcardDetailsClickListener(postcard -> {
            NavDirections action =
                    GalleryFragmentDirections.actionNavigationGalleryToNavigationDetails(postcard.getId());
            NavigationManager.getInstance().navigate(this, action);
        });
    }
}