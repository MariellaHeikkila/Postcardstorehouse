package com.maalelan.postcardstorehouse.utils;

import com.maalelan.postcardstorehouse.models.Postcard;
import com.maalelan.postcardstorehouse.models.PostcardImage;
import com.maalelan.postcardstorehouse.models.database.entities.PostcardEntity;
import com.maalelan.postcardstorehouse.models.database.entities.PostcardImageEntity;

import java.util.Date;

/** A utility class responsible for mapping between postcard (POJO) and PostcardEntity (Room Entity).
 * This allows separation between the domain model used in business logic and the persistence model used with Room.
 */
public class PostcardMapper {

    /**
     * Converts a Postcard object into a PostcardEntity suitable for Room database storage.
     *
     * @param postcard the domain model (POJO) representing a postcard
     * @return a PostcardEntity for Room persistence
     */
    public static PostcardEntity toEntity(Postcard postcard) {
        return new PostcardEntity(
                postcard.getCountry(),
                postcard.getTopic(),
                DateUtils.format(postcard.getSentDate()),
                DateUtils.format(postcard.getReceivedDate()),
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
        postcard.setTopic(entity.getTopic());

        // Try parsing string-formatted dates into Date objects
        try {
            postcard.setSentDate(DateUtils.parse(entity.getSentDate()));
            postcard.setReceivedDate(DateUtils.parse(entity.getReceivedDate()));
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

    /**
     * Converts a PostcardImage POJO into a PostcardImageEntity
     * suitable for Room database storage
     * @param image the PostcardImage object from the app logic
     * @return a PostcardImageEntity for Room persistence
     */
    public static PostcardImageEntity toEntity(PostcardImage image) {
        PostcardImageEntity entity = new PostcardImageEntity();
        entity.setId(image.getId());
        entity.setPostcardId(image.getPostcardId());
        entity.setTagName(image.getTagName());
        entity.setImageUri(image.getImageUri());
        return entity;
    }

    /**
     * Converts a PostcardImageEntity from the Room db into a
     * PostcardImage POJO
     * @param entity the Room entity representing a postcard img
     * @return a PostcardImage object for use in the app
     */
    public static PostcardImage fromEntity(PostcardImageEntity entity) {
        PostcardImage image = new PostcardImage();
        image.setId(entity.getId());
        image.setPostcardId(entity.getPostcardId());
        image.setTagName(entity.getTagName());
        image.setImageUri(entity.getImageUri());
        return image;
    }


}

