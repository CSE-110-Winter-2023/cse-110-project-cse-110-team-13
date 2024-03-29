package com.example.socialcompass;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Pair;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class LocationService implements LocationListener {
    private static LocationService instance;
    private static final int MAX_TIME_SINCE_UPDATE = 1000;
    private Activity activity;
    private long lastLocationUpdateTime = 0;


    private MutableLiveData<Pair<Double,Double>> locationValue;

    private final LocationManager locationManager;

    public static LocationService singleton(Activity activity) {
        if(instance == null) {
            instance = new LocationService(activity);

        }
        return instance;

    }

    protected LocationService(Activity activity) {
        this.locationValue = new MutableLiveData<>();
        this.activity = activity;
        this.locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        this.registerLocationListener();
    }

    private void registerLocationListener() {
        if(ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            throw new IllegalStateException("App needs location permission to get latest location");
        }

        this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        this.locationValue.postValue(new Pair<Double,Double>(location.getLatitude(), location.getLongitude()));
        lastLocationUpdateTime = System.currentTimeMillis();
    }

    public boolean isGPSDisabled() {
        long timeSinceUpdate = System.currentTimeMillis() - lastLocationUpdateTime;
        return timeSinceUpdate > MAX_TIME_SINCE_UPDATE;
    }

    private void unregisterLocationListener() {locationManager.removeUpdates(this);}

    public LiveData<Pair<Double, Double>> getLocation() {return this.locationValue;}

    public void setMockOrientationSource(MutableLiveData<Pair<Double, Double>> mockDataSource) {
        unregisterLocationListener();
        this.locationValue = mockDataSource;

    }
}
