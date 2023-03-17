package com.example.socialcompass;

import android.util.Log;

public class MockDeviceObserver implements DeviceObserver {

    private Boolean hasGPSSignal;

    @Override
    public void deviceUpdate(String location, float orientation) {
    }

    @Override
    public void signalUpdate(long time) {
        this.hasGPSSignal = false;
    }

    @Override
    public void hasSignal() {
        this.hasGPSSignal = true;
    }

    public Boolean isGPSEnabled(){
        return this.hasGPSSignal;
    }
}
