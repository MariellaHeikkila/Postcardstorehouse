package com.maalelan.postcardstorehouse.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.maalelan.postcardstorehouse.R;
import com.maalelan.postcardstorehouse.models.Postcard;
import com.maalelan.postcardstorehouse.utils.DateUtils;
import com.maalelan.postcardstorehouse.viewmodels.PostcardViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Fragment for adding a new postcard.
 * Gathers user input and delegates saving to ViewModel.
 */
public class AddPostcardFragment extends Fragment {

    private PostcardViewModel viewModel;

    // UI components
    private EditText editSentDate, editReceivedDate, editCountry, editTopic, editNotes;
    private CheckBox checkboxFavorite, checkboxIsSentByUser;
    private Button buttonSave, buttonAddPhoto;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 2;
    private ImageView imagePreview;

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
        viewModel = new ViewModelProvider(this).get(PostcardViewModel.class);

        // Bind views
        editSentDate = view.findViewById(R.id.edit_sent_date);
        editReceivedDate = view.findViewById(R.id.edit_received_date);
        editCountry = view.findViewById(R.id.edit_country);
        editTopic = view.findViewById(R.id.edit_topic);
        editNotes = view.findViewById(R.id.edit_notes);
        checkboxFavorite = view.findViewById(R.id.checkbox_favorite);
        checkboxIsSentByUser = view.findViewById(R.id.checkbox_is_sent_by_user);
        buttonSave = view.findViewById(R.id.button_save);
        buttonAddPhoto = view.findViewById(R.id.button_add_photo);
        imagePreview = view.findViewById(R.id.image_preview);

        buttonAddPhoto.setOnClickListener(v -> askCameraPermission());

        editSentDate.setOnClickListener(v -> showDatePicker(editSentDate));
        editReceivedDate.setOnClickListener(v -> showDatePicker(editReceivedDate));

        // Handle save button click
        buttonSave.setOnClickListener(v -> savePostcard());

    }

    private void askCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            openCamera();
        }
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getContext(), "Kameraa ei löydy", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                          @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(getContext(), "Kameran käyttöoikeus tarvitaan kuvan ottamiseen", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK && data != null) {
            Toast.makeText(getContext(), "Kuva otettu!", Toast.LENGTH_SHORT).show();
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            if (imageBitmap != null) {
                imagePreview.setImageBitmap(imageBitmap);
            }
        }
    }

    /**
     * Shows a date picker dialog and sets the selected date to the given EditText.
     * @param targetField The EditText to update with the selected date
     */
    private void showDatePicker(EditText targetField) {
        final Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                    targetField.setText(dateFormat.format(calendar.getTime()));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    /**
     * Collect user input and pass it to the ViewModel to save
     */
    private void savePostcard() {
        String sentText = editSentDate.getText().toString().trim();
        String receivedText = editReceivedDate.getText().toString().trim();
        Date sentDate = DateUtils.parse(sentText);
        Date receivedDate = DateUtils.parse(receivedText);
        String country = editCountry.getText().toString().trim();
        String topic = editTopic.getText().toString().trim();
        String notes = editNotes.getText().toString().trim();
        boolean isFavorite = checkboxFavorite.isChecked();
        boolean isSentByUser = checkboxIsSentByUser.isChecked();

        // Validation maybe here

        // Build postcard object
        Postcard postcard = new Postcard(
                -1,
                country,
                topic,
                sentDate,
                receivedDate,
                notes,
                isFavorite,
                isSentByUser
        );

        // Save via ViewModel
        viewModel.addPostcard(postcard);

        Toast.makeText(requireContext(), "Postcard saved", Toast.LENGTH_SHORT).show();

        // After this maybe resetting form, navigate somewhere?

    }
}