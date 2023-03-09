package com.example.socialcompass;

import android.app.Activity;
import android.view.View;

import androidx.lifecycle.LifecycleOwner;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;



//CurrentState will be a subject class, keeping track of all subscribed values and set their values
// respectively if needed. It also employs location and orientation service to update its observer
// (in this case the observers are Markers in its markerList)
// it will also employ a DisplayUpdate which will update the UI accordingly.
public class CurrentState implements DeviceObserver, ServerObserver {

    private String oldLocation; //oldLocation so that the listeners can use a placeholder value
    private float oldOrientation; // oldOrientation so that the listeners can use a placeholder value
    public ArrayList<Marker> markerList; // list of observers, in this case, Markers
    private ServerSubject server;
    private Activity activity; // activity
    private DisplayUpdate updater; // UI updater

    // constructor
    public CurrentState(Activity activity, ArrayList<Marker> list, ServerSubject server) {
        this.oldLocation = "0,0";
        this.oldOrientation = 0;
        this.activity = activity;
        this.updater = new DisplayUpdate(activity);
        this.markerList = list;
        this.server = server;
    }

    public void deviceUpdate(String location, float orientation) {
        this.oldLocation = location;
        this.oldOrientation = orientation;

        //update the new location onto the server
        this.server.update(location);

        // TODO: set the marker appropriately in the display class
    }

}
