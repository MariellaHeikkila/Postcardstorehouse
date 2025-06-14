package com.maalelan.postcardstorehouse.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.maalelan.postcardstorehouse.models.FilterCriteria;
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

    // Filter-related LiveData
    private final MutableLiveData<FilterCriteria> currentFilterCriteria;
    private final MediatorLiveData<List<Postcard>> displayedPostcards;

    // Filter options for UI
    private final LiveData<List<String>> availableCountries;
    private final LiveData<List<String>> availableTopics;
    private final LiveData<List<String>> availableTagNames;

    /**
     * Constructor initializes the repository and Livedata source.
     * Sets up filtering mechanism with MediatorLiveData.
     *
     * @param application The Application instance used to create the ViewModel
     */
    public PostcardViewModel(@NonNull Application application) {
        super(application);
        repository = new PostcardRepository(application);
        allPostcards = repository.getAllPostcards();
        postcardThumbnails = repository.getPostcardThumbnails();

        // filter-related LiveData
        currentFilterCriteria = new MutableLiveData<>(FilterCriteria.empty());
        displayedPostcards = new MediatorLiveData<>();
        // Filter options
        availableCountries = repository.getDistinctCountries();
        availableTopics = repository.getDistinctTopics();
        availableTagNames = repository.getDistinctTagNames();

        setupFilterMediatorLiveData();

    }

    /**
     * Sets up MediatorLiveData to automatically switch between filtered and unfiltered results.
     * When filter criteria changes, it either shows all postcards or applies filters.
     */
    private void setupFilterMediatorLiveData() {
        // add source for all postcards (default view)
        displayedPostcards.addSource(allPostcards, postcards -> {
            FilterCriteria criteria = currentFilterCriteria.getValue();
            if (criteria == null || !criteria.hasActiveFilters()) {
                displayedPostcards.setValue(postcards);
            }
        });

        // add source for filter criteria changes
        displayedPostcards.addSource(currentFilterCriteria, criteria -> {
            if (criteria == null || !criteria.hasActiveFilters()) {
                // No filters active - show all postcards
                List<Postcard> allCards = allPostcards.getValue();
                displayedPostcards.setValue(allCards);
            } else {
                // filters active - need to switch to filtered source
                updateFilteredSource(criteria);
            }
        });
    }

    /**
     * Updates the filtered data source when filter criteria changes.
     * Removes old filtered source and adds new one.
     *
     * @param criteria The new filter criteria to apply
     */
    private void updateFilteredSource(FilterCriteria criteria) {
        //remove any existing filtered sources to prevent memory leaks
        displayedPostcards.removeSource(allPostcards);

        // add new filtered source
        LiveData<List<Postcard>> filteredSource = repository.getFilteredPostcards(criteria);
        displayedPostcards.addSource(filteredSource, filteredPostcards -> {
            displayedPostcards.setValue(filteredPostcards);
        });

        // Re-add all postcards source for when filters are cleared
        displayedPostcards.addSource(allPostcards, postcards -> {
            FilterCriteria currentCriteria = currentFilterCriteria.getValue();
            if (currentCriteria == null || !currentCriteria.hasActiveFilters()) {
                displayedPostcards.setValue(postcards);
            }
        });
    }

    //== PUBLIC METHODS FOR UI ==

    /**
     * Returns postcards to be displayed (either all or filtered) as LiveData.
     * This automatically switches between filtered and unfiltered results based on active filters.
     *
     * @return LiveData list of Postcard objects to display
     */
    public LiveData<List<Postcard>> getDisplayedPostcards() {
        return displayedPostcards;
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

    //== FILTER METHODS ==

    // getters for filter options
    public LiveData<List<String>> getAvailableCountries() {
        return availableCountries;
    }
    public LiveData<List<String>> getAvailableTopics() {
        return availableTopics;
    }
    public LiveData<List<String>> getAvailableTagNames() {
        return availableTagNames;
    }

    /**
     * Applies new filter criteria to the postcard list.
     * This will trigger the MediatorLiveData to update the displayed postcards.
     *
     * @param filterCriteria The new filter criteria to apply
     */
    public void applyFilters(FilterCriteria filterCriteria) {
        currentFilterCriteria.setValue(filterCriteria);
    }

    /**
     * Clears all active filters and shows all postcards
     */
    public void clearFilters() {
        currentFilterCriteria.setValue(FilterCriteria.empty());
    }

    /**
     * Returns the current filter criteria being applied.
     *
     * @return LiveData containing the current FilterCriteria
     */
    public LiveData<FilterCriteria> getCurrentFilterCriteria() {
        return currentFilterCriteria;
    }

    public boolean hasActiveFilters() {
        FilterCriteria criteria = currentFilterCriteria.getValue();
        return criteria != null && criteria.hasActiveFilters();
    }

}
