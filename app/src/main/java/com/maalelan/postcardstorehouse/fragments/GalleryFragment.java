package com.maalelan.postcardstorehouse.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import com.maalelan.postcardstorehouse.models.FilterCriteria;
import com.maalelan.postcardstorehouse.navigation.NavigationManager;
import com.maalelan.postcardstorehouse.viewmodels.PostcardViewModel;

import java.util.ArrayList;
import java.util.List;

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
        setupViewModel();
        setupFilterUI();
        observeData();
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

    private void setupViewModel() {
        // Initialize ViewModel to observe the data
        postcardViewModel = new ViewModelProvider(this).get(PostcardViewModel.class);
    }

    private void setupFilterUI() {
        // Toggle filter panel visibility
        toggleFilterButton.setOnClickListener(v -> toggleFilterPanel());

        // Apply filters button
        applyFiltersButton.setOnClickListener(v -> applyFilters());

        //clear filters button
        clearFiltersButton.setOnClickListener(v -> clearFilters());

        // Initially hide filter panel
        filterPanel.setVisibility(View.GONE);
    }

    private void observeData() {
        try {
            // Observe postcards list
            postcardViewModel.getDisplayedPostcards().observe(getViewLifecycleOwner(), postcards -> {
                // Update adapter with new data
                adapter.setPostcards(postcards);
                updateEmptyView(postcards);
            });

            // Observe postcard thumbnails
            postcardViewModel.getPostcardThumbnails().observe(getViewLifecycleOwner(), thumbnails -> {
                adapter.setPostcardThumbnails(thumbnails);
            });

            // Observe filter options and populate spinners
            observeFilterOptions();

            // Observe current filter criteria to update UI
            postcardViewModel.getCurrentFilterCriteria().observe(getViewLifecycleOwner(), criteria -> {
                updateFilterButtonText();
            });

        } catch (Exception e) {
            Toast.makeText(getContext(), "An error occurred: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void observeFilterOptions() {
        Context context = requireContext();

        postcardViewModel.getAvailableCountries().observe(getViewLifecycleOwner(), countries -> {
            populateSpinner(spinnerCountry, countries, context.getString(R.string.show_countries_filter));
        });

        postcardViewModel.getAvailableTopics().observe(getViewLifecycleOwner(), topics -> {
            populateSpinner(spinnerTopic, topics, context.getString(R.string.show_topics_filter));
        });

        postcardViewModel.getAvailableTagNames().observe(getViewLifecycleOwner(), tagnames -> {
            populateSpinner(spinnerTagName, tagnames, context.getString(R.string.show_tags_filter));
        });
    }

    private void populateSpinner(Spinner spinner, List<String> items, String defaultOption) {
        if (items == null) items = new ArrayList<>();

        List<String> spinnerItems = new ArrayList<>();
        spinnerItems.add(defaultOption); // Add default "all" option
        spinnerItems.addAll(items);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                R.layout.spinner_selected_item,
                spinnerItems
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void toggleFilterPanel() {
        isFilterPanelVisible = !isFilterPanelVisible;
        filterPanel.setVisibility(isFilterPanelVisible ? View.VISIBLE : View.GONE);
        updateFilterButtonText();
    }

    private void updateFilterButtonText() {
        boolean hasActiveFilters = postcardViewModel.hasActiveFilters();
        String buttonText;

        Context context = requireContext();
        String hideFiltersText = context.getString(R.string.hide_filters_button);
        String showActiveFiltersText = context.getString(R.string.show_active_filters_button);
        String showFiltersText = context.getString(R.string.show_filters_button);

        if (isFilterPanelVisible) {
            buttonText = hideFiltersText;
        } else if (hasActiveFilters) {
            buttonText = showActiveFiltersText;
        } else {
            buttonText = showFiltersText;
        }

        toggleFilterButton.setText(buttonText);
    }

    private void applyFilters() {
        FilterCriteria.Builder builder = new FilterCriteria.Builder();

        if (spinnerCountry.getSelectedItemPosition() > 0) {
            builder.setCountry((String) spinnerCountry.getSelectedItem());
        }

        if (spinnerTopic.getSelectedItemPosition() > 0) {
            builder.setTopic((String) spinnerTopic.getSelectedItem());
        }

        if (spinnerTagName.getSelectedItemPosition() > 0) {
            builder.setTagName((String) spinnerTagName.getSelectedItem());
        }

        if (checkBoxFavorite.isChecked()) {
            builder.setIsFavorite(true);
        }

        if (checkBoxSentByUser.isChecked()) {
            builder.setIsSentByUser(true);
        }

        FilterCriteria criteria = builder.build();
        postcardViewModel.applyFilters(criteria);

        Toast.makeText(getContext(), "Haku suoritettu", Toast.LENGTH_SHORT).show();
    }

    private void clearFilters() {
        //reset UI components
        spinnerCountry.setSelection(0);
        spinnerTopic.setSelection(0);
        spinnerTagName.setSelection(0);
        checkBoxFavorite.setChecked(false);
        checkBoxSentByUser.setChecked(false);

        // clear filter in viewModel
        postcardViewModel.clearFilters();

        Toast.makeText(getContext(), "Suodattimet tyhjennetty", Toast.LENGTH_SHORT).show();
    }

    private void updateEmptyView(List<?> postcards) {
        if (postcards == null || postcards.isEmpty()) {
            emptyGalleryView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);

            // Update empty message based on whether filters are active
            if (postcardViewModel.hasActiveFilters()) {
                emptyGalleryView.setText(R.string.no_postcards_to_display_with_filters);
            } else {
                emptyGalleryView.setText(R.string.no_postcards_to_display);
            }
        } else {
            emptyGalleryView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}