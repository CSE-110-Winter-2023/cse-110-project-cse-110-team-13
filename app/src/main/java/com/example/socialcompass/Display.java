package com.example.socialcompass;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.concurrent.Future;

public class Display {

    private Activity activity;
    private ServerAPI server;
    private Context context;
    //the zoom setting for the display
    // zoomSetting == 1: only 1 circle from 0 - 1 mile
    // zoomSetting == 2: 2 circle, first one from 0 - 1 mile, the other from 1 - 10 mile
    // zoomSetting == 3: 3 circle, the third one from 10 - 500 mile
    // zoomSetting == 4 : 4 circle, fourth one is just a perimeter with anything outside of the 500 mile
    // range will lie on the perimeter
    private int zoomSetting = 2;
    private int MAX_RADIUS_IN_DP = 480;
    public Display(Activity activity, Context context) {
        this.activity = activity;
        this.server = new ServerAPI();
        this.context = context;
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
    public int convertDpToPixel(float dp) {
        float pixels =  dp * this.context.getResources().getDisplayMetrics().density;
        return (int) pixels;
    }
    // update a marker imageview with a new angle and distance
    public void updatePointer(ImageView markerLocation, double angle, float distance){

        //this HAS TO BE RUN ON UI THREAD, this is because the origin of the call for this function
        // is in a listener, which runs in a background thread (originally called in device.notifyObserver())
        // so if we want the update to be in real time, it has to run on ui thread.
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) markerLocation.getLayoutParams();
        layoutParams.circleAngle = (float) angle;
//            layoutParams.circleRadius = MAX_RADIUS_IN_DP;
        if(zoomSetting == 1) {
            if(distance > 1) {
                this.activity.runOnUiThread(() -> {
                    markerLocation.setVisibility(View.INVISIBLE);
                });
            }
            else {
                this.activity.runOnUiThread(() -> {
                    markerLocation.setVisibility(View.VISIBLE);
                });

                //interpolation: get dp distance on compass using interpolation
                float radiusOnCompass = distance*MAX_RADIUS_IN_DP;
                int radius = convertDpToPixel(radiusOnCompass);
                if(radius > MAX_RADIUS_IN_DP) {
                    radius = MAX_RADIUS_IN_DP;
                }
                layoutParams.circleRadius = radius;
            }
        }

        else if (zoomSetting == 2) {
            if(distance > 10) {
                this.activity.runOnUiThread(() -> {
                    markerLocation.setVisibility(View.INVISIBLE);
                });
            }
            else if (distance <= 10 && distance > 1) {
                this.activity.runOnUiThread(() -> {
                    markerLocation.setVisibility(View.VISIBLE);
                });

                //interpolation: get dp distance on compass using interpolation
                float radiusOnCompass = ((distance - 1)/9)*(MAX_RADIUS_IN_DP/2);
                int radius = convertDpToPixel(radiusOnCompass) + (int) (MAX_RADIUS_IN_DP/2);
                if(radius > MAX_RADIUS_IN_DP) {
                    radius = MAX_RADIUS_IN_DP;
                }
                layoutParams.circleRadius = radius;
            }
            else {
                this.activity.runOnUiThread(() -> {
                    markerLocation.setVisibility(View.VISIBLE);
                });

                //interpolation: get dp distance on compass using interpolation
                float radiusOnCompass = distance*(MAX_RADIUS_IN_DP/2);
                int radius = convertDpToPixel(radiusOnCompass);
                layoutParams.circleRadius = radius;
            }
        }

        else if (zoomSetting == 3) {
            if(distance > 500) {
                this.activity.runOnUiThread(() -> {
                    markerLocation.setVisibility(View.INVISIBLE);
                });
            }
            else if (distance <= 500 && distance > 10) {
                this.activity.runOnUiThread(() -> {
                    markerLocation.setVisibility(View.VISIBLE);
                });

                //interpolation: get dp distance on compass using interpolation
                float radiusOnCompass = ((distance - 10)/490) * (MAX_RADIUS_IN_DP/3);
                int radius = convertDpToPixel(radiusOnCompass) + (int) (2*MAX_RADIUS_IN_DP/3);

                if(radius > MAX_RADIUS_IN_DP) {
                    radius = MAX_RADIUS_IN_DP;
                }

                layoutParams.circleRadius = radius;
            }
            else if (distance <= 10 && distance > 1) {
                this.activity.runOnUiThread(() -> {
                    markerLocation.setVisibility(View.VISIBLE);
                });

                //interpolation: get dp distance on compass using interpolation
                float radiusOnCompass = ((distance - 1)/9) * (MAX_RADIUS_IN_DP/3);
                int radius = convertDpToPixel(radiusOnCompass) + (int) MAX_RADIUS_IN_DP/3;
                layoutParams.circleRadius = radius;
            }
            else {
                this.activity.runOnUiThread(() -> {
                    markerLocation.setVisibility(View.VISIBLE);
                });

                //interpolation: get dp distance on compass using interpolation
                float radiusOnCompass = distance*MAX_RADIUS_IN_DP/3;
                int radius = convertDpToPixel(radiusOnCompass);
                layoutParams.circleRadius = radius;
            }
        }

        else {
            if(distance > 500) {
                this.activity.runOnUiThread(() -> {
                    markerLocation.setVisibility(View.VISIBLE);
                });
                layoutParams.circleRadius = MAX_RADIUS_IN_DP;
            }
            else if (distance <= 500 && distance > 10) {
                this.activity.runOnUiThread(() -> {
                    markerLocation.setVisibility(View.VISIBLE);
                });

                //interpolation: get dp distance on compass using interpolation
                float radiusOnCompass = ((distance - 10)/490) * (MAX_RADIUS_IN_DP/4);
                int radius = convertDpToPixel(radiusOnCompass) + (int) (2*MAX_RADIUS_IN_DP/4);

                if(radius > MAX_RADIUS_IN_DP) {
                    radius = MAX_RADIUS_IN_DP;
                }

                layoutParams.circleRadius = radius;
            }
            else if (distance <= 10 && distance > 1) {
                this.activity.runOnUiThread(() -> {
                    markerLocation.setVisibility(View.VISIBLE);
                });

                //interpolation: get dp distance on compass using interpolation
                float radiusOnCompass = ((distance - 1)/9) * (MAX_RADIUS_IN_DP/4);
                int radius = convertDpToPixel(radiusOnCompass) + (int) MAX_RADIUS_IN_DP/4;
                layoutParams.circleRadius = radius;
            }
            else {
                this.activity.runOnUiThread(() -> {
                    markerLocation.setVisibility(View.VISIBLE);
                });

                //interpolation: get dp distance on compass using interpolation
                float radiusOnCompass = distance * (MAX_RADIUS_IN_DP/4);
                int radius = convertDpToPixel(radiusOnCompass);
                layoutParams.circleRadius = radius;
            }
        }

        this.activity.runOnUiThread(() -> {

            markerLocation.setLayoutParams(layoutParams);
        });
    }


}
