package com.maalelan.postcardstorehouse.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.maalelan.postcardstorehouse.R;
import com.maalelan.postcardstorehouse.adapters.PostcardAdapter;
import com.maalelan.postcardstorehouse.viewmodels.PostcardViewModel;

public class GalleryFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView emptyGalleryView;
    private PostcardAdapter adapter;
    private PostcardViewModel postcardViewModel;

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
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        emptyGalleryView = view.findViewById(R.id.empty_gallery_text);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Adapter for displaying the postcards
        adapter = new PostcardAdapter();
        recyclerView.setAdapter(adapter);

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
        } catch (Exception e) {
            Toast.makeText(getContext(), "An error occurred: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}