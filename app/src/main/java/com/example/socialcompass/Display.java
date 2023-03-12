package com.example.socialcompass;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.concurrent.Future;

public class Display {

    private Activity activity;
    private ServerAPI server;

    public Display(Activity activity) {
        this.activity = activity;
        this.server = new ServerAPI();
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

    public void updatePointer(ImageView markerLocation, double angle){
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) markerLocation.getLayoutParams();
        layoutParams.circleAngle = (float) angle;
        markerLocation.setLayoutParams(layoutParams);
    }


}
