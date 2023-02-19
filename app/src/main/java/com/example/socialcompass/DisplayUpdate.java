package com.example.socialcompass;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

public class DisplayUpdate {

    private Activity activity;

    public DisplayUpdate(Activity activity) {
        this.activity = activity;
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
}
