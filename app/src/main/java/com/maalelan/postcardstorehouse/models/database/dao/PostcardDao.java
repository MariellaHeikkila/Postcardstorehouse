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

    @Query("DELETE FROM postcards WHERE id = :postcardId")
    void deletePostcardById(long postcardId);

    @Query("SELECT * FROM postcards")
    LiveData<List<PostcardEntity>> getAllPostcards();

    /**
     *Filtered query for postcards based on multiple criteria.
     * Uses LIKE for partial matching on text and exact matching for boolean fields.
     * NULL parameters are ignored in the filtering.
     *
     * @param country Country filter ( partial match, case-insensitive)
     * @param topic Topic filter (partial match, case-insensitive)
     * @param isFavorite Favorite status filter (exact match, null = ignore)
     * @param isSentByUser Sent by user status filter (exact match, null = ignore)
     * @param tagName Image tagName filter, dropdown choice
     * @return LiveData list of filtered PostcardEntity objects
     */
    @Query("SELECT DISTINCT p.* FROM postcards p " +
            "LEFT JOIN postcard_images pi ON p.id = pi.postcard_id " +
            "WHERE (:country IS NULL OR LOWER(p.country) LIKE LOWER('%' || :country || '%')) " +
            "AND (:topic IS NULL OR LOWER(p.topic) LIKE LOWER('%' || :topic || '%')) " +
            "AND (:isFavorite IS NULL OR p.is_favorite = :isFavorite) " +
            "AND (:isSentByUser IS NULL OR p.is_sent_by_user = :isSentByUser) " +
            "AND (:tagName IS NULL OR LOWER(pi.tag_name) LIKE LOWER('%' || :tagName || '%')) " +
            "ORDER BY p.sent_date DESC")
    LiveData<List<PostcardEntity>> getFilteredPostcards(String country, String topic,
                                                        Boolean isFavorite, Boolean isSentByUser,
                                                        String tagName);


}
