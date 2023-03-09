package com.example.socialcompass;

import android.app.Activity;
import android.view.View;

import androidx.lifecycle.LifecycleOwner;

import java.util.Objects;

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

    Device(Activity activity, LocationService locationService, OrientationService orientationService,
           DeviceObserver obs) {
        this.activity = activity;
        this.locationService = locationService;
        this.orientationService = orientationService;
        this.obs = obs;
    }

    public void notifyObserver() {
        this.locationService.getLocation().observe((LifecycleOwner) activity, loc -> {
            // since there are two listeners, you cannot update 2 changing values at the same time
            //this is why we have oldLocation and oldOrientation
            // when this listener is detecting a changing location, it will use an unupdated but latest version
            // of orientation to pass in this callback. Then it will update the new changing location into oldLocation
            this.oldLocation = Double.toString(loc.first) + "," + Double.toString(loc.second);
            obs.deviceUpdate(this.oldLocation, this.oldOrientation);

        });

        this.orientationService.getOrientation().observe((LifecycleOwner) activity, ori -> {
            this.oldOrientation = ori;
            obs.deviceUpdate(this.oldLocation, this.oldOrientation);
        });
    }
}
