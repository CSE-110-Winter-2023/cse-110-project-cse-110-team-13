package com.example.socialcompass;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

interface ServerSubject {
    void updateLocationOnServer(String location);
    void registerServerObserver(ServerObserver obs);
}

interface ServerObserver {
    void serverUpdate();
}

public class ServerListener implements ServerSubject {
    private Activity activity;
    private ServerAPI api;
    private String privateUID;
    private String oldLocation = "0,0";
    private ServerObserver obs;
    private ScheduledFuture<?> friendFuture;
    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);
    public boolean isMocking = false;

    ServerListener(Activity activity, String privateUID) {
        this.activity = activity;
        this.api = ServerAPI.provide();
        this.oldLocation = "0,0";
        this.privateUID = privateUID;
    }

    public void registerServerObserver(ServerObserver obs) {
        this.obs = obs;
    }

    public void notifyObserver() {

        class OneShotTask implements Runnable {
            ServerObserver obs;
            OneShotTask(ServerObserver obs) { this.obs = obs; }
            public void run() {
                obs.serverUpdate();
            }
        }

        friendFuture =  scheduler.scheduleWithFixedDelay(new OneShotTask(this.obs), 3000,
                3000, TimeUnit.MILLISECONDS);

        }

    public void updateLocationOnServer(String location) {
        this.oldLocation = location;
        String[] latlon = this.oldLocation.split(",");
        float lat = Float.parseFloat(latlon[0]);
        float lon = Float.parseFloat(latlon[1]);

        Log.d("test6","patching location");

        Future<Integer> patch = api.patchFriendAsync(this.privateUID, lat , lon);

        while(!patch.isDone());

        try {
            Log.d("test6",String.valueOf(patch.get()));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Log.d("test6", "done patching location");

//        class OneShotTask implements Runnable {
//            String privateUID;
//            float lat;
//            float lon;
//            OneShotTask(String privateUID, float lat, float lon) {
//                this.privateUID = privateUID;
//                this.lat = lat;
//                this.lon = lon;
//            }
//            public void run() {
//
//            }
//        }
//        friendFuture =  scheduler.scheduleWithFixedDelay(new OneShotTask(this.privateUID, lat, lon),
//                3000,
//                3000, TimeUnit.MILLISECONDS);
    }

}