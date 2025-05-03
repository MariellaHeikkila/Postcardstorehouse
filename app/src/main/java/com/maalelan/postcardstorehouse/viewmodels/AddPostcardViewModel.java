package com.maalelan.postcardstorehouse.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.maalelan.postcardstorehouse.models.Postcard;
import com.maalelan.postcardstorehouse.repositories.PostcardRepository;

/**
 * ViewModel for AddPostcardFragment.
 * Handles the logic of adding new postcards.
 */
public class AddPostcardViewModel extends AndroidViewModel {

    private final PostcardRepository repository;

    public AddPostcardViewModel(@NonNull Application application) {
        super(application);
        repository = new PostcardRepository(application);
    }

    /**
     * Adds a postcard to the repository.
     * @param postcard The postcard to add
     */
    public void addPostcard(Postcard postcard) {
        repository.addPostcard(postcard);
    }
}
