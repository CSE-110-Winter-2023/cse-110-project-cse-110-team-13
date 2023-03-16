package com.example.socialcompass;

import androidx.lifecycle.MutableLiveData;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class TimeService {

    private MutableLiveData<Long> timeValue;
    private ScheduledFuture<?> clockFuture;
    private static TimeService instance;

    protected TimeService() {
        this.timeValue = new MutableLiveData<>();
        this.registerTimeListener();
    }

    public void registerTimeListener() {
        var executor = Executors.newSingleThreadScheduledExecutor();
        this.clockFuture = executor.scheduleAtFixedRate(() -> {
            this.timeValue.postValue(System.currentTimeMillis());
        }, 0, 1000, TimeUnit.MILLISECONDS);
    }

    public MutableLiveData<Long> getTime() {
        return this.timeValue;
    }

}
