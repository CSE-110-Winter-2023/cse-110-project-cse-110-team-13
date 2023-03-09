package com.example.socialcompass;

import android.app.Activity;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

interface ServerSubject {
    void update(String location);
}

interface ServerObserver {
    public ArrayList<Marker> markerList = new ArrayList<Marker>();
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

    ServerListener(Activity activity, ServerObserver obs, String privateUID) {
        this.activity = activity;
        this.api = new ServerAPI();
        this.oldLocation = "0,0";
        this.obs = obs;
        this.privateUID = privateUID;
    }

    public void notifyObserver() {
        //very bad implementation due to restriction of ScheduledFuture and Runnables.

        class OneShotTask implements Runnable {
            String uid;
            OneShotTask(int i) { this.uid = obs.markerList.get(i).getUID(); }
            public void run() {
                api.getFriendAsync(this.uid);
            }
        }

        for(int i = 0; i < obs.markerList.size(); i++) {
            int finalI = i;
            friendFuture =  scheduler.scheduleWithFixedDelay(new OneShotTask(finalI), 3000,
                    3000, TimeUnit.MILLISECONDS);

            Friend friend;
            try {
                friend = (Friend) friendFuture.get();
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            };

            this.obs.markerList.get(i).setCoordinate(String.valueOf(friend.latitude)+","+String.valueOf(friend.longitude));
        }

    }

    public void update(String location) {
        this.oldLocation = location;
        String[] latlon = this.oldLocation.split(",");
        float lat = Float.parseFloat(latlon[0]);
        float lon = Float.parseFloat(latlon[1]);
        api.patchFriendAsync(this.privateUID, lat , lon);
    }

}
