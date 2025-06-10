package com.maalelan.postcardstorehouse.models;

import java.util.Objects;

/**
 * Model class representing filter criteria for postcard filtering.
 * Contains all possible filter parameters that can be applied to postcard queries.
 * Implements builder pattern for easy filter construction and immutability.
 */
public class FilterCriteria {
    private final String country;
    private final String topic;
    private final Boolean isFavorite;
    private final Boolean isSentByUser;
    private final String tagName;

    /**
     * Private constructor - use Builder to create instances.
     */
    private FilterCriteria(Builder builder){
        this.country = builder.country;
        this.topic = builder.topic;
        this.isFavorite = builder.isFavorite;
        this.isSentByUser = builder.isSentByUser;
        this.tagName = builder.tagName;
    }

    // Getters
    public String getCountry() {
        return country;
    }
    public String getTopic() {
        return topic;
    }
    public Boolean getIsFavorite() {
        return isFavorite;
    }
    public Boolean getIsSentByUser() {
        return isSentByUser;
    }
    public String getTagName() {
        return tagName;
    }

    /**
     * Checks if any filter criteria is set (not null).
     * Used to determine if filters are active.
     *
     * @return true if at least one filter criterion is set
     */
    public boolean hasActiveFilters() {
        return country != null || topic != null || isFavorite != null ||
                isSentByUser != null || tagName != null;
    }

    /**
     * Creates a new FilterCriteria with all filters cleared.
     *
     * @return Empty FilterCriteria instance
     */
    public static FilterCriteria empty() {
        return new Builder().build();
    }

    /**
     * Builder class for creating FilterCriteria instances.
     * Provides fluent API for setting filter parameters.
     */
    public static class Builder {
        private String country; // Temporary values while building
        private String topic;
        private Boolean isFavorite; // null = no filter, true/false = filter
        private Boolean isSentByUser;
        private String tagName;

        private String clean(String value) {
            return (value != null && !value.trim().isEmpty()) ? value.trim() : null;
        }

        public Builder setCountry(String country) {
            this.country = clean(country);
            return this;
        }
        public Builder setTopic(String topic) {
            this.topic = clean(topic);
            return this;
        }
        public Builder setIsFavorite(Boolean isFavorite) {
            this.isFavorite = isFavorite;
            return this;
        }
        public Builder setIsSentByUser(Boolean isSentByUser) {
            this.isSentByUser = isSentByUser;
            return this;
        }
        public Builder setTagName(String tagName) {
            this.tagName = clean(tagName);
            return this;
        }
        public FilterCriteria build() {
            return new FilterCriteria(this);
        }
    }

    @Override
    /**
     * Indicates whether other object is "equal to" this one.
     * Compares all filter fields for equality.
     *
     * @param obj the reference object with which to compare
     * @return true if this object is the same as the obj argument; false otherwise
     */
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        FilterCriteria that = (FilterCriteria) obj;
        return  Objects.equals(country, that.country) &&
                Objects.equals(topic,that.topic) &&
                Objects.equals(isFavorite, that.isFavorite) &&
                Objects.equals(isSentByUser, that.isSentByUser) &&
                Objects.equals(tagName, that.tagName);
    }

    @Override
    /**
     * Returns a hash code value for the object.
     * The hash code is based on all filter fields.
     *
     * @return a hash code value for this object
     */
    public int hashCode() {
        return Objects.hash(country, topic, isFavorite, isSentByUser, tagName);
    }

    @Override
    /**
     * Returns a string representation of the FilterCriteria.
     * Includes all filter fields and their values.
     *
     * @return a string representation of the object
     */
    public String toString() {
        return "FilterCriteria{" +
                "country='" + country + '\''+
                ", topic='" + topic + '\''+
                ", isFavorite=" + isFavorite +
                ", isSentByUser=" + isSentByUser +
                ", tagName='" + tagName + '\''+
                '}';
    }

}


