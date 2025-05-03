package com.maalelan.postcardstorehouse.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.maalelan.postcardstorehouse.models.Postcard;
import com.maalelan.postcardstorehouse.repositories.PostcardRepository;

import java.util.List;

/**
 * ViewModel for managing postcard relaet UI data in a lifecycle-conscious way.
 * Acts as a bridge between the UI and the repository.
 */
public class PostcardViewModel extends AndroidViewModel {

    private final PostcardRepository repository;
    private final LiveData<List<Postcard>> allPostcards;

    /**
     * Constructor initializes the repository and Livedata source.
     * @param application The Application instance used to create the ViewModel
     */
    public PostcardViewModel(@NonNull Application application) {
        super(application);
        repository = new PostcardRepository(application);
        allPostcards = repository.getAllPostcards();
    }

    /**
     * Returns all postcards as LiveData to be observed by the UI.
     * @return LiveData list of Postcard objects
     */
    public LiveData<List<Postcard>> getAllPostcards() {
        return allPostcards;
    }

    /**
     * Triggers the repository to add a new postcard to the database.
     * @param postcard The postcard to be added
     */
    public void addPostcard(Postcard postcard) {
        repository.addPostcard(postcard);
    }

    /**
     * Triggers the repository to update an existing postcard.
     * @param postcard The Postcard to be updated
     */
    public void updatePostcard(Postcard postcard) {
        repository.updatePostcard(postcard);
    }
}
