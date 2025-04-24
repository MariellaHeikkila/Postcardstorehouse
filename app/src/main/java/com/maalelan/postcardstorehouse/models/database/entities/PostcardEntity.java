package com.maalelan.postcardstorehouse.models.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "postcards")
public class PostcardEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "country")
    private String country;

    @ColumnInfo(name = "theme")
    private String theme;

    @ColumnInfo(name = "sent_date")
    private String sentDate;

    @ColumnInfo(name = "received_date")
    private String receivedDate;

    @ColumnInfo(name = "notes")
    private String notes;

    @ColumnInfo(name = "is_favorite")
    private boolean isFavorite;

    @ColumnInfo(name = "is_sent_by_user")
    private boolean isSentByUser;

    // -- constructors --

    public PostcardEntity(){
    }
    public PostcardEntity(String country, String theme, String sentDate, String receivedDate,
                          String notes, boolean isFavorite, boolean isSentByUser) {
        this.country = country;
        this.theme = theme;
        this.sentDate = sentDate;
        this.receivedDate = receivedDate;
        this.notes = notes;
        this.isFavorite = isFavorite;
        this.isSentByUser = isSentByUser;
    }

    // -- Getters & Setters --


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

    public String getSentDate() {
        return sentDate;
    }
    public void setSentDate(String sentDate) {
        this.sentDate = sentDate;
    }

    public String getReceivedDate() {
        return receivedDate;
    }
    public void setReceivedDate(String receivedDate) {
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
