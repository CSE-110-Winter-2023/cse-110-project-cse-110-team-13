package com.example.socialcompass;
interface Location {
    String getCoordinate();
    String getLabel();
    Integer getLabelID();
    Integer getLocationID();
}
interface Observer {
    void update();
}

public class Marker implements Location, Observer {
    private String markerCoordinate;
    private String markerLabel;
    private Integer markerLocationID;
    private Integer markerLabelID;

    private String UID;

    public Marker(String UID){
        this.UID = UID;

    }

    public Marker(String coordinate, String label, Integer locationID, Integer labelID) {
        this.markerCoordinate = coordinate;
        this.markerLabel = label;
        this.markerLocationID = locationID;
        this.markerLabelID = labelID;
    }

    public String getCoordinate() {
        return this.markerCoordinate;
    }

    public String getLabel() {
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
    public void update() {

    }
}
