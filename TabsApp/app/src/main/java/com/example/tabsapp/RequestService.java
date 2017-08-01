package com.example.tabsapp;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class RequestService extends Service {
    GpsClass gpsClass;
    InformationFragment informationFragment;
    SharedPreferences sharedPreferences;

    public static boolean status;

    private static final String PREF = "PREFERENCE";
    private static final String COORD = "COORDINATES";
    private static final String SHAKING = "SHAKING";
    private static final String TRANSFER = "TRANSFER";
    private static final String baseUrl = "https://crash-service.herokuapp.com";

    @Override
    public void onCreate() {
        super.onCreate();
        gpsClass = new GpsClass(getApplicationContext());
        informationFragment = new InformationFragment();
        sharedPreferences = getSharedPreferences(PREF, getApplicationContext().MODE_PRIVATE);
        status = true;
        Thread thread = new working();
        thread.start();
        Log.d("BROADCASTRECIVER", "Service start oncreate");
    }

    @Override
    public void onDestroy() {
        status = false;
        Log.d("BROADCASTRECIVER", "Service ondestroy");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class working extends Thread {
        public void run() {
            Log.d("BROADCASTRECIVER", "Service start run");
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            CrashService service = retrofit.create(CrashService.class);
            while (status) {
                Log.d("BROADCASTRECIVER", "Service start while");
                Log.d("COORS", gpsClass.latitude + " " + gpsClass.longitude);
                Call<List<Crash>> result = service.getNearestCrashes(gpsClass.latitude, gpsClass.longitude);
                result.enqueue(new Callback<List<Crash>>() {
                    @Override
                    public void onResponse(Response<List<Crash>> response, Retrofit retrofit) {
                        Log.d("BROADCASTRECIVER", "Service start onResponse");
                        Log.d("CRASHES", "onResponse here " + response.body().size());
                        String str = "";
                        for (Crash crash : response.body()) {
                            Log.d("BROADCASTRECIVER", "Service start response.body()");
                            str += "latitude: " + crash.latitude + "\n" + "longitude: " + crash.longitude +
                                    ";\n" + "-------------------" + "\n";
                        }
                        final String viewMsg = str;
                        for (int i = 0; i < response.body().size(); i++) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(COORD, viewMsg);
                            editor.apply();
                            Log.d("BROADCASTRECIVER", "Service start SharedPreference");
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                    }
                });
                try {
                    Thread.sleep(2500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
