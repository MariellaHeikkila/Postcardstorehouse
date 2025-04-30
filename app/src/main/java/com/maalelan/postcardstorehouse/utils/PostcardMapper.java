package com.maalelan.postcardstorehouse.utils;

import com.maalelan.postcardstorehouse.models.Postcard;
import com.maalelan.postcardstorehouse.models.database.entities.PostcardEntity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/** A utility class responsible for mapping between postcard (POJO) and PostcardEntity (Room Entity).
 * This allows separation between the domain model used in business logic and the persistence model used with Room.
 */
public class PostcardMapper {

    // Shared date formatter to convert Date objects to and from String format ("dd.MM.yyyy").
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

    /**
     * Converts a Postcard object into a PostcardEntity suitable for Room database storage.
     *
     * @param postcard the domain model (POJO) representing a postcard
     * @return a PostcardEntity for Room persistence
     */
    public static PostcardEntity toEntity(Postcard postcard) {
        return new PostcardEntity(
                postcard.getCountry(),
                postcard.getTheme(),
                sdf.format(postcard.getSentDate()),
                sdf.format(postcard.getReceivedDate()),
                postcard.getNotes(),
                postcard.isFavorite(),
                postcard.isSentByUser()
        );
    }

    /**
     * Converts a PostcardEntity from the Room database into a Postcard (POJO) for use in app logic
     *
     * @param entity the Room entity representing a postcard record
     * @return a Postcard object for use in the application
     */
    public static Postcard fromEntity(PostcardEntity entity) {
        Postcard postcard = new Postcard();

        //Set basic attributes directly from the entity
        postcard.setId((entity.getId()));
        postcard.setCountry(entity.getCountry());
        postcard.setTheme(entity.getTheme());

        // Try parsing string-formatted dates into Date objects
        try {
            postcard.setSentDate(sdf.parse(entity.getSentDate()));
            postcard.setReceivedDate(sdf.parse(entity.getReceivedDate()));
        } catch (Exception e) {
            // If parsing fails, fallback to the current date to avoid crashes
            postcard.setSentDate(new Date());
            postcard.setReceivedDate(new Date());
        }

        // Set the remaining fields
        postcard.setNotes(entity.getNotes());
        postcard.setFavorite(entity.isFavorite());
        postcard.setSentByUser(entity.isSentByUser());

        return postcard;
    }

}

