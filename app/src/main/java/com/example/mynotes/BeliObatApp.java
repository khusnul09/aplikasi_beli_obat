package com.example.mynotes;

import android.app.Application;

import com.cloudinary.android.MediaManager;

import java.util.HashMap;
import java.util.Map;

public class BeliObatApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Map config = new HashMap();

        config.put("cloud_name", "beliobatid");
        config.put("api_key", "832196155542743");
        config.put("api_secret", "bwnHoGmtO2Li9tq42rDckhd_5BE");
        MediaManager.init(getApplicationContext(), config);
    }
}
