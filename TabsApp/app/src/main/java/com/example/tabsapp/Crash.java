package com.example.tabsapp;

public class Crash {
    public int id;
    public double latitude;
    public double longitude;
    public float speed;
    public long ts;
    public String uid;

    public Crash(double latitude, double longitude, float speed,int id, long ts, String uid) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.speed = speed;
        this.id = id;
        this.ts = ts;
        this.uid = uid;
    }
}
