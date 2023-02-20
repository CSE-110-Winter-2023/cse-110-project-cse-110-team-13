package com.example.socialcompass;

import android.app.Activity;
import android.view.View;

import androidx.lifecycle.LifecycleOwner;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

interface Subject {
    //register observer
    void addMarker(String coordinate, String label, Integer locationID, Integer labelID);

    //remove observer, will be implemented
    //void removeObserver();

    // update observer
    void notifyObserver();
}

//CurrentState will be a subject class, keeping track of all subscribed values and set their values
// respectively if needed. It also employs location and orientation service to update its observer
// (in this case the observers are Markers in its markerList)
// it will also employ a DisplayUpdate which will update the UI accordingly.
public class CurrentState implements Subject {
    private LocationService locationService; //location service
    private OrientationService orientationService; //orientation service
    private String oldLocation; //oldLocation so that the listeners can use a placeholder value
    private float oldOrientation; // oldOrientation so that the listeners can use a placeholder value
    public List<Marker> markerList; // list of observers, in this case, Markers
    public int numOfLocations; // number of observers
    private Activity activity; // activity
    private DisplayUpdate updater; // UI updater

    // constructor
    public CurrentState(int numberOfLocations, LocationService locationService, OrientationService orientationService, Activity activity) {
        this.numOfLocations = numberOfLocations;
        this.markerList = new ArrayList<Marker>();
        for (int i = 0; i < numberOfLocations; i++) {
            this.markerList.add(new Marker("","",0,0));
        }
        this.locationService = locationService;
        this.orientationService = orientationService;
        this.oldLocation = "0,0";
        this.oldOrientation = 0;
        this.activity = activity;
        this.updater = new DisplayUpdate(activity);
    }
    //add a new marker into currentState, aka registering a new observer. This will be needed when
    // we have an add new location feature in the compass
    public void addMarker(String coordinate, String label, Integer locationID, Integer labelID) {
        markerList.add(new Marker(coordinate, label, locationID, labelID));
        this.numOfLocations+=1;
    }

    // set the coordinate, label, location id and label id (for textview) for a marker.
    public void setMarkerInfo(int i, String coordinate, String label, Integer locationID, Integer labelID) {
        setMarkerCoordinate(i, coordinate);
        setMarkerLabel(i, label);
        setLabelID(i, labelID);
        setLocationID(i, locationID);
    }
    // setters
    public void setMarkerCoordinate(int i, String coordinate) {
        this.markerList.get(i).setCoordinate(coordinate);
    }
    public void setMarkerLabel(int i, String label) {
        this.markerList.get(i).setLabel(label);
    }
    public void setLocationID(int i, Integer locationID) {
        this.markerList.get(i).setLocationID(locationID);
    }
    public void setLabelID(int i, Integer labelID) {
        this.markerList.get(i).setLabelID(labelID);
    }
    // notify observer method.
    // this will employ two listeners, one for the location service, the other for the orientation.

    public void notifyObserver() {
        this.locationService.getLocation().observe((LifecycleOwner) activity, loc -> {
            // since there are two listeners, you cannot update 2 changing values at the same time
            //this is why we have oldLocation and oldOrientation
            // when this listener is detecting a changing location, it will use an unupdated but latest version
            // of orientation to pass in this callback. Then it will update the new changing location into oldLocation
            this.oldLocation = Double.toString(loc.first) + "," + Double.toString(loc.second);
            for (int i = 0; i < numOfLocations; i++)
            {
                //if no coordinates are sent, don't draw the marker or its label
                if (Objects.equals(markerList.get(i).getCoordinate(), "default"))
                {
                    activity.findViewById(markerList.get(i).getLocationID()).setVisibility(View.INVISIBLE);
                    activity.findViewById(markerList.get(i).getLabelID()).setVisibility(View.INVISIBLE);
                    continue;
                }
                //compute angle and update marker and label
                double angle = AngleUtil.compassCalculateAngle(oldLocation,
                        markerList.get(i).getCoordinate(), oldOrientation);

                updater.updatePointer(markerList.get(i).getLocationID(), angle);
                updater.updateLabelPointer(markerList.get(i).getLabelID(), markerList.get(i).getLocationID());
            }
        });

        this.orientationService.getOrientation().observe((LifecycleOwner) activity, ori -> {
            this.oldOrientation = ori;
            for (int i = 0; i < numOfLocations; i++)
            {
                //if no coordinates are sent, don't draw the marker or its label
                if (Objects.equals(markerList.get(i).getCoordinate(), "default"))
                {
                    activity.findViewById(markerList.get(i).getLocationID()).setVisibility(View.INVISIBLE);
                    activity.findViewById(markerList.get(i).getLabelID()).setVisibility(View.INVISIBLE);
                    continue;
                }
                //compute angle and update marker and label
                double angle = AngleUtil.compassCalculateAngle(oldLocation,
                        markerList.get(i).getCoordinate(), oldOrientation);

                updater.updatePointer(markerList.get(i).getLocationID(), angle);
                updater.updateLabelPointer(markerList.get(i).getLabelID(), markerList.get(i).getLocationID());
            }
        });
    }
}
