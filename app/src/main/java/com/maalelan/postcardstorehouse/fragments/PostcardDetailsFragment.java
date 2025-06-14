package com.maalelan.postcardstorehouse.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.maalelan.postcardstorehouse.R;
import com.maalelan.postcardstorehouse.utils.DateUtils;
import com.maalelan.postcardstorehouse.viewmodels.PostcardViewModel;

public class PostcardDetailsFragment extends Fragment {

    private long postcardId;
    private PostcardViewModel viewModel;

    TextView textCountry, textTopic, textSentDate, textReceivedDate, textNotes;

    CheckBox checkFavorite, checkSentByUser;

    ImageView imageThumbnail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.postcard_details_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Retrieve postcard ID from arguments

        postcardId = PostcardDetailsFragmentArgs.fromBundle(getArguments()).getPostcardId();


        textCountry = view.findViewById(R.id.text_detail_country);
        textTopic = view.findViewById(R.id.text_detail_topic);
        textSentDate = view.findViewById(R.id.text_detail_sent_date);
        textReceivedDate = view.findViewById(R.id.text_detail_received_date);
        textNotes = view.findViewById(R.id.text_detail_notes);
        checkFavorite = view.findViewById(R.id.checkbox_detail_favorite);
        checkSentByUser = view.findViewById(R.id.checkbox_detail_sent_by_user);
        imageThumbnail = view.findViewById(R.id.image_detail_thumbnail);

        viewModel = new ViewModelProvider(this).get(PostcardViewModel.class);

        // Observe the postcard data by ID
        viewModel.getPostcardById(postcardId).observe(getViewLifecycleOwner(), postcard -> {
            if (postcard != null) {
                // Update fields with postcard details
                textCountry.setText("Maa: " + postcard.getCountry());
                textTopic.setText("Aihe: " + postcard.getTopic());
                textSentDate.setText("Lähetyspäivä: " + DateUtils.format(postcard.getSentDate()));
                textReceivedDate.setText("Vastaanotettu: "+ DateUtils.format(postcard.getReceivedDate()));
                textNotes.setText("Muistiinpanot: " + postcard.getNotes());
                // remove previous listener to avoid triggering on setChecked
                checkFavorite.setOnCheckedChangeListener(null);
                checkFavorite.setChecked(postcard.isFavorite());
                checkSentByUser.setChecked(postcard.isSentByUser());

                // set listener for user interaction
                checkFavorite.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (postcard.isFavorite() != isChecked) {
                        // Update the favorite status in the ViewModel/db
                        postcard.setFavorite(isChecked);
                        viewModel.updateFavoriteStatus(postcard, isChecked);
                    }
                });


                // observe the thumbnail URI and load it into the imageview using Glide
                viewModel.getThumbnailByPostcardId(postcardId).observe(getViewLifecycleOwner(), uri -> {
                    Glide.with(requireContext())
                            .load(uri)
                            .placeholder(R.drawable.ic_camera_placeholder)
                            .into(imageThumbnail);
                });
            }
        });
    }
}