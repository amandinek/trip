package com.example.trip;

import android.location.Location;

public interface LocationEngineListener {
    public void onConnected();
    public void onLocationChanged(Location location);
}
