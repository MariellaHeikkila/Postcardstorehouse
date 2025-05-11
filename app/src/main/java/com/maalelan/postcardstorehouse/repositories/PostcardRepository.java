package com.maalelan.postcardstorehouse.repositories;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.maalelan.postcardstorehouse.models.Postcard;
import com.maalelan.postcardstorehouse.models.PostcardImage;
import com.maalelan.postcardstorehouse.models.database.PostcardDatabase;
import com.maalelan.postcardstorehouse.models.database.dao.PostcardDao;
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
     * @param Listener 
     */
    public void addPostcard(Postcard postcard, final OnPostcardAddedListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // first create an instance of PostcardDao
                PostcardDao postcardDao = postcardDatabase.postcardDao();
                long id = postcardDao.insertPostcard(PostcardMapper.toEntity(postcard));

                // callback interface
                if (listener != null) {
                    final long finalPostcardId = id;
                    // Post back to main thread
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            listener.OnPostcardAdded(finalPostcardId);
                        }
                    });
                }
            }
        }).start();
    }

    public interface OnPostcardAddedListener {
        void OnPostcardAdded(long id);
    }
//    public void addPostcard(Postcard postcard) {
//        PostcardEntity entity = PostcardMapper.toEntity(postcard);
//        new Thread(() -> postcardDatabase.postcardDao().insertPostcard(entity)).start();
//    }

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
     * Inserts a new image linked to a postcard, into the database
     * on a background thread.
     * @param image The PostcardImage object to insert
     */
    public void addPostcardImage(PostcardImage image) {
        PostcardImageEntity entity = PostcardMapper.toEntity(image);
        new Thread(() -> postcardDatabase.postcardImageDao().insert(entity)).start();
    }

    /**
     * Deletes an image from the database on a background thread.
     * @param image The PostcardImage object to delete
     */
    public void deleteImage(PostcardImage image) {
        PostcardImageEntity entity = PostcardMapper.toEntity(image);
        new Thread(() -> postcardDatabase.postcardImageDao().delete(entity)).start();
    }

}
