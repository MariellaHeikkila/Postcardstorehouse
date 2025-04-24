package com.maalelan.postcardstorehouse.models.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import com.maalelan.postcardstorehouse.models.database.entities.PostcardEntity;


@Dao
public interface PostcardDao {
    @Insert
    void insertPostcard(PostcardEntity... postcards); //same as: public abstract void insertPostcard(PostcardEntity... postcards);
}
