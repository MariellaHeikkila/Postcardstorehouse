package com.maalelan.postcardstorehouse.repositories;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.maalelan.postcardstorehouse.models.FilterCriteria;
import com.maalelan.postcardstorehouse.models.Postcard;
import com.maalelan.postcardstorehouse.models.PostcardImage;
import com.maalelan.postcardstorehouse.models.database.PostcardDatabase;
import com.maalelan.postcardstorehouse.models.database.dao.PostcardDao;
import com.maalelan.postcardstorehouse.models.database.entities.PostcardEntity;
import com.maalelan.postcardstorehouse.models.database.entities.PostcardImageEntity;
import com.maalelan.postcardstorehouse.utils.PostcardMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Repository class that handles data operations related to postcards and their images.
 * Acts as an abstraction layer between the UI and the Room db.
 *
 * Enhanced with filtering capabilities for postcard queries.
 */
public class PostcardRepository {

    private final PostcardDatabase postcardDatabase;

    /**
     * Initialize the repository and database instance.
     * @param context Application context
     */
    public PostcardRepository(Context context) {
        postcardDatabase = PostcardDatabase.getDatabase(context);
    }

    //== POSTCARD METHODS ==

    /**
     * Retrieves all postcards from the database as LiveData.
     * Converts database entities to POJO models using the mapper.
     *
     * @return LiveData containing a list of Postcard objects
     */
    public LiveData<List<Postcard>> getAllPostcards() {
        return Transformations.map(
                postcardDatabase.postcardDao().getAllPostcards(),
                entities -> {
                    List<Postcard> postcards = new ArrayList<>();
                    for (PostcardEntity entity : entities) {
                        postcards.add(PostcardMapper.fromEntity(entity));
                    }
                    return postcards;
                }
        );
    }

    /**
     * Retrieves filtered postcards based on the provided filter criteria.
     * Supports filtering by country, topic, favorite status, sent by user status and tag names.
     * All filters are optional - null values are ignored.
     *
     * @param filterCriteria The filter criteria containing all filter parameters
     * @return LiveData containing a list filtered Postcard objects
     */
    public LiveData<List<Postcard>> getFilteredPostcards(FilterCriteria filterCriteria) {
        return Transformations.map(
                postcardDatabase.postcardDao().getFilteredPostcards(
                        filterCriteria.getCountry(),
                        filterCriteria.getTopic(),
                        filterCriteria.getIsFavorite(),
                        filterCriteria.getIsSentByUser(),
                        filterCriteria.getTagName()
                ),
                entities -> {
                    List<Postcard> postcards = new ArrayList<>();
                    for (PostcardEntity entity : entities) {
                        postcards.add(PostcardMapper.fromEntity(entity));
                    }
                    return postcards;
                }
        );
    }

    /**
     * Inserts a new postcard and its related images into the database on a background thread.
     * If image list is null or empty, only the postcard will be inserted.
     *
     * @param postcard The Postcard object to insert
     * @param images The images associated with the postcard
     * @param listener Callback interface to return the inserted postcard´s ID
     */
    public void addPostcard(Postcard postcard, List<PostcardImage> images, final OnPostcardAddedListener listener) {
        new Thread(() -> {
                // Insert postcard and get ID
            PostcardEntity postcardEntity = PostcardMapper.toEntity(postcard);
            long postcardId = postcardDatabase.postcardDao().insertPostcard(postcardEntity);

            // insert related images with correct postcardId only if the list is not null and not empty
            if (images != null && !images.isEmpty()) {
                for (PostcardImage image : images) {
                    PostcardImageEntity imageEntity = PostcardMapper.toEntity(image);
                    imageEntity.setPostcardId(postcardId);
                    postcardDatabase.postcardImageDao().insert(imageEntity);
                }
            }

            // Notify via callback on the main thread
            if (listener != null) {
                new Handler(Looper.getMainLooper()).post(()->
                        listener.OnPostcardAdded(postcardId));
            }
        }).start();
    }

    /**
     * Callback interface for returning the ID of a newly inserted postcard.
     */
    public interface OnPostcardAddedListener {
        void OnPostcardAdded(long id);
    }

    /**
     * Updates an existing postcard in the database on a background thread.
     *
     * @param postcard The Postcard object with updated data
     */
    public void updatePostcard(Postcard postcard) {
        PostcardEntity entity = PostcardMapper.toEntity(postcard);
        new Thread(() -> postcardDatabase.postcardDao().updatePostcard(entity)).start();
    }

    //== POSTCARD IMAGES METHODS

    /**
     * Retrieves all images related to a specific postcard as a LiveData list.
     * Converts entities to model objects using the mapper
     *
     * @param postcardId The ID of the postcard whose images are to be fetched
     * @return Livedata containing a list of PostcardImage objects
     */
    public LiveData<List<PostcardImage>> getImagesForPostcard(long postcardId) {
        return Transformations.map(
                postcardDatabase.postcardImageDao().getImagesForPostcard(postcardId),
                entities -> {
                    List<PostcardImage> images = new ArrayList<>();
                    for (PostcardImageEntity entity : entities) {
                        images.add(PostcardMapper.fromEntity(entity));
                    }
                    return images;
                }
        );
    }

    /**
     * Get thumbnail image for recycler view
     */
    public LiveData<Map<Long, String>> getPostcardThumbnails() {
        return Transformations.map(
                postcardDatabase.postcardImageDao().getAllImages(),
                entities -> {
                    Map<Long, String> thumbnailMap = new HashMap<>();
                    for (PostcardImageEntity image : entities) {
                        // only store the first image per postcard
                        if (!thumbnailMap.containsKey(image.getPostcardId())) {
                            thumbnailMap.put(image.getPostcardId(), image.getImageUri());
                        }
                    }
                    return thumbnailMap;
                }
        );
    }

    /**
     * Inserts a new image linked to a postcard, into the database
     * on a background thread.
     *
     * @param image The PostcardImage object to insert
     */
    public void addPostcardImage(PostcardImage image) {
        PostcardImageEntity entity = PostcardMapper.toEntity(image);
        new Thread(() -> postcardDatabase.postcardImageDao().insert(entity)).start();
    }

    /**
     * Deletes a postcard and all its related images from the database.
     *
     * @param postcard The postcard to delete
     */
    public void deletePostcardWithImages(Postcard postcard) {
        new Thread(() -> {
            long postcardId = postcard.getId();
            // delete images
            postcardDatabase.postcardImageDao().deleteImagesByPostcardId(postcardId);
            // delete postcard
            postcardDatabase.postcardDao().deletePostcardById(postcardId);
        }).start();
    }

    /**
     * Deletes an image from the database on a background thread.
     *
     * @param image The PostcardImage object to delete
     */
    public void deleteImage(PostcardImage image) {
        PostcardImageEntity entity = PostcardMapper.toEntity(image);
        new Thread(() -> postcardDatabase.postcardImageDao().delete(entity)).start();
    }

    //== FILTER SUPPORT METHODS ==

    /**
     * Retrieves distinct countries from all postcards for filter options.
     * Used to populate country filter dropdown
     *
     * @return LiveData list of unique country names
     */
    public LiveData<List<String>> getDistinctCountries() {
        return postcardDatabase.postcardDao().getDistinctCountries();
    }

    /**
     * Retrieves distinct topics from all postcards for filter options.
     * used to populate topic filter dropdown.
     *
     * @return LiveData list of unique topic names
     */
    public LiveData<List<String>> getDistinctTopics() {
        return postcardDatabase.postcardDao().getDistinctTopics();
    }

    /**
     * Retrieves distinct tag names from all postcard images for filter options.
     * Used to populate tag filter dropdown.
     *
     * @return LiveData list of unique tag names
     */
    public LiveData<List<String>> getDistinctTagNames() {
        return postcardDatabase.postcardImageDao().getDistinctTagNames();
    }

}
