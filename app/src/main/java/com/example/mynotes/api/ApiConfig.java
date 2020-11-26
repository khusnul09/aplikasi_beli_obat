package com.example.mynotes.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiConfig {
    public static String BASE_URL = "https://obats.000webhostapp.com/api/";

    public Retrofit doRequest() {
        Retrofit.Builder builder = new Retrofit.Builder();
        return builder.baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public ApiInterface instance() {
        return doRequest().create(ApiInterface.class);
    }
}
