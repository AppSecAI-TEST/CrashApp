package com.example.tabsapp;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

public interface CrashService {
    @GET("/listCrashes")
    Call<List<Crash>> getNearestCrashes(@Query("lat") Double lat, @Query("lon") Double lon);
}
