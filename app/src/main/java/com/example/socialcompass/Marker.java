package com.example.socialcompass;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

interface Location {
    String getCoordinate();

    String getMarkerLabel();

    Integer getLabelID();

    Integer getLocationID();
}

public class Marker implements Location {
    private String markerCoordinate;
    private String markerLabel;
    private Integer markerLocationID;
    private Integer markerLabelID;

    private TextView label;
    private ImageView location;


    private String UID;
    public Marker(){
    }

    public Marker(String UID){
        this.UID = UID;

    }

    public Marker(String coordinate, String label, Integer locationID, Integer labelID) {
        this.markerCoordinate = coordinate;
        this.markerLabel = label;
        this.markerLocationID = locationID;
        this.markerLabelID = labelID;
    }
    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getCoordinate() {
        return this.markerCoordinate;
    }

    public String getMarkerLabel() {
        return this.markerLabel;
    }

    public Integer getLocationID() {
        return this.markerLocationID;
    }

    public Integer getLabelID() {
        return this.markerLabelID;
    }

    public void setCoordinate(String coordinate) {
        this.markerCoordinate = coordinate;
    }

    public void setLabel(String label) {
        this.markerLabel = label;
    }

    public void setLocationID(Integer ID) {
        this.markerLocationID = ID;
    }

    public void setLabelID(Integer ID) {
        this.markerLabelID = ID;
    }

    public void setLabel(TextView label) {
        this.label = label;
    }

    public TextView getLabel(){
        return this.label;
    }

    public ImageView getLocation() {
        return location;
    }

    public void setLocation(ImageView location) {
        this.location = location;
    }


}
