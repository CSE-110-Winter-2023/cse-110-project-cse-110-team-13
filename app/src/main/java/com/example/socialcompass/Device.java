package com.example.socialcompass;

import android.app.Activity;
import android.view.View;

import androidx.lifecycle.LifecycleOwner;

import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

interface DeviceSubject {
    public void notifyObserver();
}

interface DeviceObserver {
    void deviceUpdate(String location, float orientation);
}

public class Device implements  DeviceSubject {
    private Activity activity;
    private LocationService locationService;
    private OrientationService orientationService;
    private String oldLocation = "0,0";
    private float oldOrientation = 0.0F;
    private DeviceObserver obs;
    private ScheduledFuture<?> Future;
    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);

    Device(Activity activity, LocationService locationService, OrientationService orientationService) {
        this.activity = activity;
        this.locationService = locationService;
        this.orientationService = orientationService;
    }

    public void registerDeviceObserver(DeviceObserver obs) {
        this.obs = obs;
    }

    public void notifyObserver() {
        this.locationService.getLocation().observe((LifecycleOwner) activity, loc -> {
            // since there are two listeners, you cannot update 2 changing values at the same time
            //this is why we have oldLocation and oldOrientation
            // when this listener is detecting a changing location, it will use an unupdated but latest version
            // of orientation to pass in this callback. Then it will update the new changing location into oldLocation
            String newLocation = Double.toString(loc.first) + "," + Double.toString(loc.second);
            try {
                if (AngleUtil.markerCalculateDistance(this.oldLocation, newLocation) > 0.0024) {
                    this.oldLocation = newLocation;
                    obs.deviceUpdate(this.oldLocation, this.oldOrientation);
                }
            } catch (Exception e) {

            }
        });

        this.orientationService.getOrientation().observe((LifecycleOwner) activity, ori -> {
            if(Math.abs(ori-this.oldOrientation) > 1) {
                this.oldOrientation = ori;
                obs.deviceUpdate(this.oldLocation, this.oldOrientation);
            }
            });

    }
}