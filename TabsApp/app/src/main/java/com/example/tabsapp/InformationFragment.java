package com.example.tabsapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class InformationFragment extends Fragment {
    GpsClass gpsClass;
    TextView textView;
    Handler handler;
    SharedPreferences preferences;
    String getCoordinates;

    private static final String PREF = "PREFERENCE";
    private static final String COORD = "COORDINATES";
    private static final String LOG_TAG = "MainActivity";
    private static final String baseUrl = "https://crash-service.herokuapp.com";
    private static boolean doIt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_information, container, false);
        preferences = getActivity().getSharedPreferences(PREF, getActivity().getApplicationContext().MODE_PRIVATE);
        textView = (TextView) rootView.findViewById(R.id.text);
        handler = new Handler();
        doIt = true;
        Thread thread = new infUpdate();
        thread.start();
        return rootView;
    }

    @Override
    public void onDestroy() {
        doIt = false;
        super.onDestroy();
    }

    class infUpdate extends Thread {
        public void run() {
            getCoordinates = preferences.getString(COORD, "null");
            while (doIt) {
                try {
                    sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!textView.getText().equals(getCoordinates)) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("Information", preferences.getString(COORD, "null"));
                            textView.setText(getCoordinates);
                        }
                    });
                }
            }
        }
    }
}
/**
 * class MyAsyncTask extends Thread {
 *
 * @Override public void run() {
 * GsonBuilder builder = new GsonBuilder();
 * Gson gson = builder.create();
 * Retrofit retrofit = new Retrofit.Builder()
 * .baseUrl(baseUrl)
 * .addConverterFactory(GsonConverterFactory.create(gson))
 * .build();
 * CrashService service = retrofit.create(CrashService.class);
 * while (doingThis) {
 * Log.d("COORS", gpsClass.latitude + " " + gpsClass.longitude);
 * Call<List<Crash>> result = service.getNearestCrashes(gpsClass.latitude, gpsClass.longitude);
 * result.enqueue(new Callback<List<Crash>>() {
 * @Override public void onResponse(Response<List<Crash>> response, Retrofit retrofit) {
 * Log.d("CRASHES", "onResponse here " + response.body().size());
 * String str = "";
 * for (Crash crash : response.body()) {
 * str += "latitude: " + crash.latitude + "\n" + "longitude: " + crash.longitude +
 * ";\n" + "-------------------" + "\n";
 * }
 * final String viewMsg = str;
 * for (int i = 0; i < response.body().size(); i++) {
 * Log.d("CRASHES", "into body");
 * handler.post(new Runnable() {
 * @Override public void run() {
 * textView.setText(viewMsg);
 * }
 * });
 * }
 * }
 * @Override public void onFailure(Throwable t) {
 * }
 * });
 * try {
 * Thread.sleep(PAUSE);
 * } catch (InterruptedException e) {
 * e.printStackTrace();
 * }
 * }
 * }
 * }
 **/