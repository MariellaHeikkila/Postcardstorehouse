package com.maalelan.postcardstorehouse.models;

public class PostcardImage {
    private int id;
    private int postcardId;
    private String tagName; //[front, back, postmark, stamp]
    private String imageUri; // uri path

    //empty constructor for serialization etc.
    public PostcardImage() {
    }

    public PostcardImage(int id, int postcardId, String tagName, String imageUri) {
        this.id = id;
        this.postcardId = postcardId;
        this.tagName = tagName;
        this.imageUri = imageUri;
    }

    //getters and setters


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getPostcardId() {
        return postcardId;
    }
    public void setPostcardId(int postcardId) {
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
