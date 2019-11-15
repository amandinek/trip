package com.example.trip;

import android.location.Location;

import androidx.annotation.NonNull;

import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineResult;

import java.lang.ref.WeakReference;

public class LocationListeningCallback implements LocationEngineCallback<LocationEngineResult> {

    private final WeakReference<ConfrimRiderActivity> activityWeakReference;

    LocationListeningCallback(ConfrimRiderActivity activity) {
        this.activityWeakReference = new WeakReference<>(activity);
    }

    @Override
    public void onSuccess(LocationEngineResult result) {

// The LocationEngineCallback interface's method which fires when the device's location has changed.


        Location lastLocation = result.getLastLocation();


    }

    @Override
    public void onFailure(@NonNull Exception exception) {

// The LocationEngineCallback interface's method which fires when the device's location can not be captured



    }
}
