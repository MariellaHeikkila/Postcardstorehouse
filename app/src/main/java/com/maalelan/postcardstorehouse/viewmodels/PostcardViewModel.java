package com.maalelan.postcardstorehouse.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.maalelan.postcardstorehouse.models.Postcard;
import com.maalelan.postcardstorehouse.models.PostcardImage;
import com.maalelan.postcardstorehouse.repositories.PostcardRepository;

import java.util.List;
import java.util.Map;

/**
 * ViewModel for managing postcard relate UI data in a lifecycle-conscious way.
 * Acts as a bridge between the UI and the repository.
 */
public class PostcardViewModel extends AndroidViewModel {

    private final PostcardRepository repository;
    private final LiveData<List<Postcard>> allPostcards;

    private final LiveData<Map<Long, String>> postcardThumbnails;

    /**
     * Constructor initializes the repository and Livedata source.
     * @param application The Application instance used to create the ViewModel
     */
    public PostcardViewModel(@NonNull Application application) {
        super(application);
        repository = new PostcardRepository(application);
        allPostcards = repository.getAllPostcards();
        postcardThumbnails = repository.getPostcardThumbnails();
    }

    /**
     * Returns all postcards as LiveData to be observed by the UI.
     * @return LiveData list of Postcard objects
     */
    public LiveData<List<Postcard>> getAllPostcards() {
        return allPostcards;
    }

    /**
     * Returns Livedata object containing the Postcard with the specified ID.
     *
     * @param id The unique identifier of the postcard to retrieve
     * @return Livedata containing the Postcard with the matching ID, or null.
     */
    public LiveData<Postcard> getPostcardById(long id) {
        return Transformations.map(allPostcards, postcards -> {
            if (postcards == null) return null;
            for (Postcard postcard : postcards) {
                if (postcard.getId() == id) {
                    return postcard;
                }
            }
            return null;
        });
    }

    /**
     * Triggers the repository to add a new postcard to the database.
     * @param postcard The postcard to be added
     * @param images List of associated postcard images (can be null or empty)
     * @param listener Callback to notify when insertion is complete
     */
    public void addPostcard(Postcard postcard, List<PostcardImage> images, PostcardRepository.OnPostcardAddedListener listener) {
        repository.addPostcard(postcard, images, listener);
    }

    /**
     * Triggers the repository to update an existing postcard.
     * @param postcard The Postcard to be updated
     */
    public void updatePostcard(Postcard postcard) {
        repository.updatePostcard(postcard);
    }

    /**
     * Update isFavorite
     *
     * @param postcard
     * @param isFavorite
     */
    public void updateFavoriteStatus(Postcard postcard, boolean isFavorite) {
        postcard.setFavorite(isFavorite);
        repository.updatePostcard(postcard);
    }

    /**
     * Deletes a postcard an all its related images from the database.
     *
     * @param postcard The postcard to delete
     */
    public void deletePostcardCompletely(Postcard postcard) {
        repository.deletePostcardWithImages(postcard);
    }

    // == IMAGE related methods

    /**
     * Gets all images related to a specific postcard ID.
     * @param postcardId The postcard ID
     * @return LiveData list of related images
     */
    public LiveData<List<PostcardImage>> getImagesForPostcard(long postcardId) {
        return repository.getImagesForPostcard(postcardId);
    }

    /**
     *
     */
    public LiveData<Map<Long, String>> getPostcardThumbnails() {
        return postcardThumbnails;
    }

    /**
     * Returns a Livedata object containing the thumbnail URI of the specified postcard.
     * The URI is extracted from the map of all loaded thumbnails.
     *
     * @param postcardId The iD of the postcard whose thumbnail URi is requested
     * @return Livedata containing the thumbnail URI as a String, or null if not found
     */
    public LiveData<String> getThumbnailByPostcardId(long postcardId) {
        return Transformations.map(postcardThumbnails, map -> {
            if (map == null) return null;
            return map.get(postcardId);
        });
    }

    /**
     * Saves a postcard image to the database
     * @param postcardId The ID of the related postcard
     * @param tagName Tag describing the image type
     * @param imageUri The URI of the image
     */
    public void addPostcardImage(long postcardId, String tagName, String imageUri) {
        PostcardImage image = new PostcardImage(postcardId, tagName, imageUri);
        repository.addPostcardImage(image);
    }

    /**
     * Delete a specific postcard image from the db
     * @param image The image to be deleted
     */
    public void deletePostcardImage(PostcardImage image) {
        repository.deleteImage(image);
    }

}
