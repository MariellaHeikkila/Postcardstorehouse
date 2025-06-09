package com.maalelan.postcardstorehouse.models.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.maalelan.postcardstorehouse.models.database.entities.PostcardImageEntity;

import java.util.List;

@Dao
public interface PostcardImageDao {

    @Insert
    long insert(PostcardImageEntity image);

    @Update
    void update(PostcardImageEntity image);

    @Delete
    void delete(PostcardImageEntity image); //delete one specific image

    @Query("SELECT * FROM postcard_images ORDER BY id DESC")
    LiveData<List<PostcardImageEntity>> getAllImages();

    @Query("SELECT * FROM postcard_images WHERE postcard_id = :postcardId ORDER BY tag_name ASC")
    LiveData<List<PostcardImageEntity>> getImagesForPostcard(long postcardId);

    @Query("DELETE FROM postcard_images WHERE postcard_id = :postcardId") //delete all pc id related images
    void deleteImagesByPostcardId(long postcardId);

    /**
     * Get distinct tag names for filter dropdown
     * Excludes null and empty tag names for cleaner filer options
     * Ordered alphabetically for consistent UI experience.
     *
     * @return LiveData list of unique tag names used in postcard images
     */
    @Query("SELECT DISTINCT tag_name FROM postcard_images " +
            "WHERE tag_name IS NOT NULL AND tag_name != '' ORDER BY tag_name ASC")
    LiveData<List<String>> getDistinctTagNames();

}
