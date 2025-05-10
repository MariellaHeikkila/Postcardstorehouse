package com.maalelan.postcardstorehouse.models;

public class PostcardImage {
    private long id;
    private long postcardId;
    private String tagName; //[front, back, postmark, stamp]
    private String imageUri; // uri path

    //empty constructor for serialization etc.
    public PostcardImage() {
    }

    public PostcardImage(long postcardId, String tagName, String imageUri) {
        this.postcardId = postcardId;
        this.tagName = tagName;
        this.imageUri = imageUri;
    }

    public PostcardImage(long id, long postcardId, String tagName, String imageUri) {
        this.id = id;
        this.postcardId = postcardId;
        this.tagName = tagName;
        this.imageUri = imageUri;
    }

    //getters and setters


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
