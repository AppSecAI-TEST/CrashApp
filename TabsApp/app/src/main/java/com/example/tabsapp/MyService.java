package com.example.tabsapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class MyService extends Service {
    public static boolean status;

    @Override
    public void onCreate() {
        super.onCreate();
        status = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        status = false;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
