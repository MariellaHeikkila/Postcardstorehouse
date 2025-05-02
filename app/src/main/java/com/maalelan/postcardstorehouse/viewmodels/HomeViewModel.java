package com.maalelan.postcardstorehouse.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.maalelan.postcardstorehouse.R;
import com.maalelan.postcardstorehouse.navigation.NavigationEvent;
import com.maalelan.postcardstorehouse.utils.Event;

/**
 * ViewModel for the  HomeFragment.
 * Manages navigation to Add postcard and gallery view events.
 */
public class HomeViewModel extends ViewModel {
    private final MutableLiveData<Event<NavigationEvent>> navigationEvent = new MutableLiveData<>();

    public LiveData<Event<NavigationEvent>> getNavigationEvent() {
        return navigationEvent;
    }

    public void navigateToAdd() {
        navigationEvent.setValue(new Event<>(new NavigationEvent.ToDestination(R.id.navigation_add)));
    }

    public void navigateToGallery() {
        navigationEvent.setValue(new Event<>(new NavigationEvent.ToDestination(R.id.navigation_gallery)));
    }
}
