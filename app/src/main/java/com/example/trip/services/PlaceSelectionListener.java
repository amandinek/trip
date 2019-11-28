package com.example.trip.services;

import android.location.Location;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;

public interface PlaceSelectionListener {
    void onLocationChanged(Location dropLocation);

    void onPlaceSelected(Place place);

    void onError(Status status);
}
