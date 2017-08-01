package com.example.tabsapp;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.POST;

public interface UserService {
    @POST("/fromCar")
    Call<Boolean> fromCar(@Body Crash crash);
}