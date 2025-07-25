package com.maalelan.postcardstorehouse.models.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;


@Entity(
        tableName = "postcard_images",
        foreignKeys = @ForeignKey(
                entity = PostcardEntity.class, // reference to postcardEntity
                parentColumns = "id", // reference to postcardEntity id field
                childColumns = "postcard_id", // This field holds this reference key
                onDelete = ForeignKey.CASCADE // if postcard is deleted, images are deleted also
        )
)
public class PostcardImageEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long id;

    @ColumnInfo(name = "postcard_id", index = true)
    public long postcardId;

    @ColumnInfo(name = "tag_name")
    public String tagName;

    @ColumnInfo(name = "image_uri")
    public String imageUri;

    public  PostcardImageEntity() {}
    public PostcardImageEntity(long postcardId, String tagName, String imageUri) {
        this.postcardId = postcardId;
        this.tagName = tagName;
        this.imageUri = imageUri;
    }

    // Getters and Setters


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPostcardId() {
        return postcardId;
    }

    public void setPostcardId(long postcardId) {
        this.postcardId = postcardId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
