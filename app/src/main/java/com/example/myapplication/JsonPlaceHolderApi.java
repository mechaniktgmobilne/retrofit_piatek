package com.example.myapplication;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonPlaceHolderApi {
    @GET("pytania")
    public Call<List<Pytanie>> getPytania();
//https://developer.android.com/codelabs/basic-android-kotlin-compose-getting-data-internet#0

}
