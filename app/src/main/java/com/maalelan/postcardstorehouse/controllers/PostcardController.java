package com.maalelan.postcardstorehouse.controllers;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.maalelan.postcardstorehouse.models.Postcard;
import com.maalelan.postcardstorehouse.models.database.PostcardDatabase;
import com.maalelan.postcardstorehouse.models.database.entities.PostcardEntity;
import com.maalelan.postcardstorehouse.utils.PostcardMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * PostcardController handles business logic for postcards.
 * It interacts with the database for fetching, adding and updating postcards.
 * It provides Livedata to the View so it can react to data changes.
 */
public class PostcardController {

    private final PostcardDatabase postcardDatabase;
    private final MutableLiveData<List<Postcard>> allPostcards;

    // Constructor initializes the database and Livedata
    public PostcardController(Context context) {
        postcardDatabase = PostcardDatabase.getDatabase(context);
        allPostcards = new MutableLiveData<>();
        fetchAllPostcards();
    }

    /**
     * Fetch all postcards from the database and update to LiveData
     */
    public void fetchAllPostcards() {
        postcardDatabase.postcardDao().getAllPostcards().observeForever(postcardEntities -> {
            List<Postcard> postcards = new ArrayList<>();

            // Convert from Entity to POJO using the Mapper
            for (PostcardEntity entity : postcardEntities) {
                postcards.add(PostcardMapper.fromEntity(entity));
            }
            // Post updated list to LiveData
            allPostcards.postValue(postcards);
        });
    }

    /**
     * Get all postcards as LiveData to be observed by the view
     * @return LiveData<List<Postcard>> containing all postcards
     */
    public LiveData<List<Postcard>> getAllPostcards() {
        return allPostcards;
    }

    /**
     * Add a new postcard to the database.
     * @param postcard The postcard to be added
     */
    public void addPostcard(Postcard postcard) {
        PostcardEntity entity = PostcardMapper.toEntity(postcard);
        new Thread(() -> postcardDatabase.postcardDao().insertPostcard(entity)).start();
    }

    /**
     * Update an existing postcard in the database.
     * @param postcard The postcard with updated data
     */
    public void updatePostcard(Postcard postcard) {
        PostcardEntity entity = PostcardMapper.toEntity(postcard);
        new Thread(() -> postcardDatabase.postcardDao().updatePostcard(entity)).start();
    }



}
