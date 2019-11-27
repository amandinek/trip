package com.example.trip.Common;

import com.example.trip.Remote.IGoogleAPI;
import com.example.trip.Remote.RetrofitClient;

public class Common {

    public static final String baseUrl ="https://maps.googleapi.com";
    public static IGoogleAPI getGoogleAPI(){
        return RetrofitClient.getClient(baseUrl).create(IGoogleAPI.class);
    }
}
