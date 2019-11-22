package com.example.trip;

import org.json.JSONException;
import org.json.JSONObject;

public class PayUtils {
    private static JSONObject getBaseRequest() throws JSONException {
        return new JSONObject().put("apiVersion", 2).put("apiVersionMinor", 0);
    }
}
