package org.njctl.courseapp.model;

import org.json.JSONObject;

public interface AsyncJsonResponse {
    void processJson(JSONObject output);
}