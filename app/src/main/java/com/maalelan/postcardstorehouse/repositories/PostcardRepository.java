package com.maalelan.postcardstorehouse.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.maalelan.postcardstorehouse.models.Postcard;
import com.maalelan.postcardstorehouse.models.PostcardImage;
import com.maalelan.postcardstorehouse.models.database.PostcardDatabase;
import com.maalelan.postcardstorehouse.models.database.entities.PostcardEntity;
import com.maalelan.postcardstorehouse.models.database.entities.PostcardImageEntity;
import com.maalelan.postcardstorehouse.utils.PostcardMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository class that handles data operations for postcards.
 * Acts as a single source of truth and abstracts access to the database.
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
     * Inserts a new postcard into the database on a background thread.
     * @param postcard The Postcard object to insert
     */
    public void addPostcard(Postcard postcard) {
        PostcardEntity entity = PostcardMapper.toEntity(postcard);
        new Thread(() -> postcardDatabase.postcardDao().insertPostcard(entity)).start();
    }

    /**
     * Updates an existing postcard in the database on a background thread.
     * @param postcard The Postcard object with updated data
     */
    public void updatePostcard(Postcard postcard) {
        PostcardEntity entity = PostcardMapper.toEntity(postcard);
        new Thread(() -> postcardDatabase.postcardDao().updatePostcard(entity)).start();
    }

    //== POSTCARD IMAGES METHODS

    /**
     * Retrieves all images related to a specific postcard as a LiveData list.
     * @param postcardId The ID of the postcard whose images are to be fetched
     * @return Livedata containing a list of PostcardImage objects
     */
    public LiveData<List<PostcardImage>> getImagesForPostcard(int postcardId) {
        return Transformations.map(
                postcardDatabase.postcardImageDao().getImagesForPostcard(postcardId),
                entities -> {
                    List<PostcardImage> images = new ArrayList<>();
                    for (PostcardImageEntity entity : entities) {
                        // images.add(Continue Mapperstuff here);
                    }
                    return images;
                }
        );
    }

}
