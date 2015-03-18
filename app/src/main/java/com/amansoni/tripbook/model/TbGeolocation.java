package com.amansoni.tripbook.model;

import android.util.Log;

/**
 * Created by Aman on 12/12/2014.
 */
public class TbGeolocation {
    private static final String TAG = "TbGeolocation";
    private double longitude;
    private double latitude;

    public TbGeolocation(double longitude, double latitude){
        this.longitude = longitude;
        this.latitude = latitude;
        Log.d(TAG, "New location " + this.toString());
    }

    public double getLongitude() {
        return longitude;
    }
//
//    public void setLongitude(double longitude) {
//        this.longitude = longitude;
//    }

    public double getLatitude() {
        return latitude;
    }

//    public void setLatitude(double latitude) {
//        this.latitude = latitude;
//    }

    @Override
    public String toString(){
        return "Longitude:" + longitude + " Latitude:" + latitude;
    }
}
