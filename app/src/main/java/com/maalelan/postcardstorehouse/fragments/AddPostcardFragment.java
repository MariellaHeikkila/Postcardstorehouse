package com.maalelan.postcardstorehouse.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.maalelan.postcardstorehouse.R;
import com.maalelan.postcardstorehouse.models.Postcard;
import com.maalelan.postcardstorehouse.viewmodels.AddPostcardViewModel;

/**
 * Fragment for adding a new postcard.
 * Gathers user input and delegates saving to ViewModel.
 */
public class AddPostcardFragment extends Fragment {

    private AddPostcardViewModel viewModel;

    // UI components
    private EditText editSentDate, editReceivedDate, editCountry, editTopic, editNotes;
    private CheckBox checkboxFavorite, checkboxIsSentByUser;
    private Button buttonSave;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment and return the root view
        return inflater.inflate(R.layout.add_postcard_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(AddPostcardViewModel.class);

        // Bind views
        editSentDate = view.findViewById(R.id.edit_sent_date);
        editReceivedDate = view.findViewById(R.id.edit_received_date);
        editCountry = view.findViewById(R.id.edit_country);
        editTopic = view.findViewById(R.id.edit_topic);
        editNotes = view.findViewById(R.id.edit_notes);
        checkboxFavorite = view.findViewById(R.id.checkbox_favorite);
        checkboxIsSentByUser = view.findViewById(R.id.checkbox_is_sent_by_user);
        buttonSave = view.findViewById(R.id.button_save);

        // Handle save button click

    }

    /**
     * Collect user input and pass it to the ViewModel to save
     */
    private void savePostcard() {

    }
}