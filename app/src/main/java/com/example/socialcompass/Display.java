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

    //deprecated method, no reason to change the ID of textviews and imageviews
//    public void updateLabelPointer(int labelPointerID, int locationPointerID)
//    {
//        TextView label = activity.findViewById(labelPointerID);
//        ImageView marker = activity.findViewById(locationPointerID);
//        ConstraintLayout.LayoutParams layoutParamsMarker = (ConstraintLayout.LayoutParams) marker.getLayoutParams();
//        ConstraintLayout.LayoutParams layoutParamsLabel = (ConstraintLayout.LayoutParams) label.getLayoutParams();
//        layoutParamsLabel.circleAngle = layoutParamsMarker.circleAngle;
//        layoutParamsLabel.circleRadius = layoutParamsMarker.circleRadius + 100;
//        label.setLayoutParams(layoutParamsLabel);
//    }

    // update a marker imageview with a new angle.
    public void updatePointer(ImageView markerLocation, double angle){

        //this HAS TO BE RUN ON UI THREAD, this is because the origin of the call for this function
        // is in a listener, which runs in a background thread (originally called in device.notifyObserver())
        // so if we want the update to be in real time, it has to run on ui thread.
        this.activity.runOnUiThread(() -> {
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) markerLocation.getLayoutParams();
            layoutParams.circleAngle = (float) angle;
            markerLocation.setLayoutParams(layoutParams);
        });
    }


}
