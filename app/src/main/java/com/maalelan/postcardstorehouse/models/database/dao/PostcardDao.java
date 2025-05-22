package com.maalelan.postcardstorehouse.models.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.maalelan.postcardstorehouse.models.database.entities.PostcardEntity;

import java.util.List;

@Dao
public interface PostcardDao {
    @Insert
    long insertPostcard(PostcardEntity postcard); //same as: public abstract void insertPostcard(PostcardEntity... postcards);

    @Update
    void updatePostcard(PostcardEntity postcardEntity);

    @Query("SELECT * FROM postcards")
    LiveData<List<PostcardEntity>> getAllPostcards();
}
