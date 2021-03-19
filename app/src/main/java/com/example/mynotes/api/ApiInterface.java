package com.example.mynotes.api;

import com.example.mynotes.Model;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET("Obat")
    Call<ArrayList<Model>> getListObat();
}
