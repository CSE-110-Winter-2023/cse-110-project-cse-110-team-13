package com.example.socialcompass;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

public class Display {

    private Activity activity;
    public ArrayList<Marker> markerList; // list of observers, in this case, Markers


    public Display(Activity activity, ArrayList<Marker> markerList) {
        this.activity = activity;
        this.markerList = markerList;
    }
    //this method updates the location of the marker
    public void updateLabelPointer(int labelPointerID, int locationPointerID)
    {
        TextView label = activity.findViewById(labelPointerID);
        ImageView marker = activity.findViewById(locationPointerID);
        ConstraintLayout.LayoutParams layoutParamsMarker = (ConstraintLayout.LayoutParams) marker.getLayoutParams();
        ConstraintLayout.LayoutParams layoutParamsLabel = (ConstraintLayout.LayoutParams) label.getLayoutParams();
        layoutParamsLabel.circleAngle = layoutParamsMarker.circleAngle;
        layoutParamsLabel.circleRadius = layoutParamsMarker.circleRadius + 100;
        label.setLayoutParams(layoutParamsLabel);
    }

    public void updatePointer(int markerId, double angle){
        ImageView marker = activity.findViewById(markerId);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) marker.getLayoutParams();
        layoutParams.circleAngle = (float) angle;
        marker.setLayoutParams(layoutParams);
    }

    public void updateMarkerWhenDeviceChange(String location, float orientation) {
        for(int i = 0; i < markerList.size(); i++) {
            //TODO: for each marker update the new orientation based on user's new locaiton/orientation

        }
    }

    public void updateMarkerWhenServerChange() {
        for(int i = 0; i < markerList.size(); i++) {
            //TODO: for each marker get new location, then update the marker, then update display

        }
    }



}
