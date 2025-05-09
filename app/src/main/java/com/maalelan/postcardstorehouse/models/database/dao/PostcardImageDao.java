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
    LiveData<List<PostcardImageEntity>> getImagesForPostcard(int postcardId);

    @Query("DELETE FROM postcard_images WHERE postcard_id = :postcardId") //delete all pc id related images
    void deleteImagesByPostcardId(int postcardId);


}
