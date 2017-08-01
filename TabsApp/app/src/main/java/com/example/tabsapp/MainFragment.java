package com.example.tabsapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.UUID;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class MainFragment extends Fragment implements SensorEventListener {
    Vibrator vibrate;
    SensorManager sensorManager;
    Sensor accelerometer;
    SharedPreferences preferences;
    GpsClass gpsClass;

    private static final String PREF = "PREFERENCE";
    private static final String SHAKING = "SHAKING";
    private static final String TRANSFER = "TRANSFER";
    private static final String LOG_TAG = "MainActivity";
    private static final String baseUrl = "https://crash-service.herokuapp.com";

    TextView coordinates, dateTime, predictText, latitude, longitude, speed;

    private boolean ColorBackground = true, sp = true, res = false;
    private String dt;
    private long date;
    private float accelerationRoot, x, y, z, kmSpd = (float) 3.6;
    private float[] values;
    private int VALUE = 15;
    private int NUM = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_own, container, false);

        sensorManager = (SensorManager) rootView.getContext().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        latitude = (TextView) rootView.findViewById(R.id.latitude);
        longitude = (TextView) rootView.findViewById(R.id.longitude);
        speed = (TextView) rootView.findViewById(R.id.speed);
        coordinates = (TextView) rootView.findViewById(R.id.coordinates);
        predictText = (TextView) rootView.findViewById(R.id.predictText);
        dateTime = (TextView) rootView.findViewById(R.id.dateTime);
        vibrate = (Vibrator) rootView.getContext().getSystemService(Context.VIBRATOR_SERVICE);
        preferences = getActivity().getSharedPreferences(PREF, getActivity().getApplicationContext().MODE_PRIVATE);

        gpsClass = new GpsClass(rootView.getContext());

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        sensorManager.registerListener(MainFragment.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            values = event.values;
            x = values[0];
            y = values[1];
            z = values[2];
            accelerationRoot = (x * x + y * y + z * z) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
            VALUE = preferences.getInt(SHAKING, 10);
            NUM = preferences.getInt(TRANSFER, 1);
            if (accelerationRoot > VALUE) {
                if (gpsClass.latitude != 0.0 && gpsClass.longitude != 0.0 /**&& gpsClass.speed != 0.0**/) {
                    new MyAsyncTask().start();
                    vibrate.vibrate(500);
                    if (NUM == 1) {
                        predictText.setText("Ваши координаты переданы по GPS!");
                    } else if (NUM == 2) {
                        predictText.setText("Ваши координаты переданы по INTERNET!");
                    }
                }
                getSpeed();
                setDate();
                latitude.setText("lat: " + gpsClass.latitude);
                longitude.setText("long: " + gpsClass.longitude);
                if (ColorBackground) {
                    coordinates.setBackgroundColor(Color.BLUE);
                    ColorBackground = false;
                } else {
                    coordinates.setBackgroundColor(Color.RED);
                    ColorBackground = true;
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public static String _getUniqueID(Activity activity) {
        final TelephonyManager tm = (TelephonyManager) activity.getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(activity.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String deviceId = deviceUuid.toString();
        return deviceId;
    }

    public void setDate() {
        date = System.currentTimeMillis();
        SimpleDateFormat dtFormat = new SimpleDateFormat("Время аварии: H:mm:ss, dd:MM:yyyy", new Locale("ru"));
        dt = dtFormat.format(date);
        dateTime.setText(dt);
    }

    class MyAsyncTask extends Thread {
        @Override
        public void run() {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            UserService service = retrofit.create(UserService.class);
            Crash crash = new Crash(gpsClass.latitude, gpsClass.longitude, gpsClass.speed, 0, System.currentTimeMillis(), _getUniqueID(getActivity()));
            Call<Boolean> call = service.fromCar(crash);
            try {
                Response<Boolean> coorResponse = call.execute();
                Boolean res = coorResponse.body();
                Log.d(LOG_TAG, "" + res);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void getSpeed() {
        speed.setText("speed: " + gpsClass.speed * kmSpd);
    }
}


