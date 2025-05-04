package com.maalelan.postcardstorehouse.models;

import java.util.Date;

public class Postcard {

    private int id;
    private String country;
    private String topic;
    private Date sentDate;
    private Date receivedDate;
    private String notes;
    private boolean isFavorite;
    private boolean isSentByUser;

    //Empty Constructor
    public Postcard() {
    }

    //Constructor with params
    public Postcard(int id, String country, String topic, Date sentDate, Date receivedDate,
                    String notes, boolean isFavorite, boolean isSentByUser) {
        this.id = id;
        this.country = country;
        this.topic = topic;
        this.sentDate = sentDate;
        this.receivedDate = receivedDate;
        this.notes = notes;
        this.isFavorite = isFavorite;
        this.isSentByUser = isSentByUser;
    }

    // Constructor without id, used in AddPostcardFragment for adding new postcard
    public Postcard(String country, String topic, Date sentDate, Date receivedDate,
                    String notes, boolean isFavorite, boolean isSentByUser) {
        this(-1, country,topic,sentDate, receivedDate, notes, isFavorite, isSentByUser);
    }

    //Getters and setters


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }

    public String getTopic() {
        return topic;
    }
    public void setTopic(String theme) {
        this.topic = topic;
    }

    public Date getSentDate() {
        return sentDate;
    }
    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }
    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isFavorite() {
        return isFavorite;
    }
    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public boolean isSentByUser() {
        return isSentByUser;
    }
    public void setSentByUser(boolean sentByUser) {
        isSentByUser = sentByUser;
    }
}
