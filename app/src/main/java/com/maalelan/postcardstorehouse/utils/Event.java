package com.maalelan.postcardstorehouse.utils;

/**
 * A wrapper class for data that is exposed via LiveData that represents an event.
 * This is used to prevent events from being triggered multiple times on configuration changes.
 *
 * @param <T> The type of the event content
 */
public class Event<T> {
    private final T content;
    private boolean hasBeenHandled = false;

    /**
     * Create a new event with the given content
     * @param content The content of the event
     */
    public Event(T content) {
        this.content = content;
    }

    /**
     * Get the content of the event and mark it has handled.
     * Return null if the event has already been handled.
     *
     * @return The content of the event, or null if already handled
     */
    public T getContentIfNotHandled() {
        if (hasBeenHandled) {
            return null;
        } else {
            hasBeenHandled = true;
            return content;
        }
    }

    /**
     * Get the content of the event without marking it has handled
     * @return The content of the event
     */
    public T peekContent() {
        return content;
    }

    /**
     * Check if the event has been handled
     * @return True if event has been handled, false otherwise
     */
    public boolean isHandled() {
        return hasBeenHandled;
    }
}
