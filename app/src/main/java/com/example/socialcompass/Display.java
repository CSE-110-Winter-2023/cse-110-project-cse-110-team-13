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

    private ImageView compassViewO;
    private ImageView compassView43;
    private ImageView compassView42;
    private ImageView compassView41;
    private ImageView compassView32;
    private ImageView compassView31;
    private TextView dist44;
    private TextView dist43;
    private TextView dist42;
    private TextView dist41;
    private TextView dist33;
    private TextView dist32;
    private TextView dist31;
    private TextView dist22;
    private TextView dist21;
    private TextView dist1;
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
        compassViewO = activity.findViewById(R.id.CompassFaceO);
        compassView43 = activity.findViewById(R.id.CompassFace4_3);
        compassView42 = activity.findViewById(R.id.CompassFace4_2);
        compassView41 = activity.findViewById(R.id.CompassFace4_1);
        compassView32 = activity.findViewById(R.id.CompassFace3_2);
        compassView31 = activity.findViewById(R.id.CompassFace3_1);
        dist44 = activity.findViewById(R.id.dist44);
        dist43 = activity.findViewById(R.id.dist43);
        dist42 = activity.findViewById(R.id.dist42);
        dist41 = activity.findViewById(R.id.dist41);
        dist33 = activity.findViewById(R.id.dist33);
        dist32 = activity.findViewById(R.id.dist32);
        dist31 = activity.findViewById(R.id.dist31);
        dist22 = activity.findViewById(R.id.dist22);
        dist21 = activity.findViewById(R.id.dist21);
        dist1 = activity.findViewById(R.id.dist1);
    }

    public float clamp(float value, float min, float max)
    {
        if (value < min)
            return min;
        return Math.min(value, max);
    }

    public int getZoomSetting()
    {
        return zoomSetting;
    }

    public void setZoomSetting(int setting)
    {
        zoomSetting = setting;
    }

    public int convertDpToPixel(float dp) {
        float pixels =  dp * this.context.getResources().getDisplayMetrics().density;
        return (int) pixels;
    }
    // update a marker imageview with a new angle and distance
    public void updatePointer(ImageView markerLocation, TextView markerLabel, double angle, float distance){

        //this HAS TO BE RUN ON UI THREAD, this is because the origin of the call for this function
        // is in a listener, which runs in a background thread (originally called in device.notifyObserver())
        // so if we want the update to be in real time, it has to run on ui thread.
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) markerLocation.getLayoutParams();
        layoutParams.circleAngle = (float) angle;

        if(zoomSetting == 1) {
            compassViewO.setVisibility(View.VISIBLE);
            compassView43.setVisibility(View.INVISIBLE);
            compassView42.setVisibility(View.INVISIBLE);
            compassView41.setVisibility(View.INVISIBLE);
            compassView32.setVisibility(View.INVISIBLE);
            compassView31.setVisibility(View.INVISIBLE);
            dist44.setVisibility(View.INVISIBLE);
            dist43.setVisibility(View.INVISIBLE);
            dist42.setVisibility(View.INVISIBLE);
            dist41.setVisibility(View.INVISIBLE);
            dist33.setVisibility(View.INVISIBLE);
            dist32.setVisibility(View.INVISIBLE);
            dist31.setVisibility(View.INVISIBLE);
            dist22.setVisibility(View.INVISIBLE);
            dist21.setVisibility(View.INVISIBLE);
            dist1.setVisibility(View.VISIBLE);
            if(distance > 1) {
                this.activity.runOnUiThread(() -> {
                    markerLocation.setVisibility(View.VISIBLE);
                    markerLabel.setVisibility(View.INVISIBLE);
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
            compassViewO.setVisibility(View.VISIBLE);
            compassView43.setVisibility(View.INVISIBLE);
            compassView42.setVisibility(View.VISIBLE);
            compassView41.setVisibility(View.INVISIBLE);
            compassView32.setVisibility(View.INVISIBLE);
            compassView31.setVisibility(View.INVISIBLE);
            dist44.setVisibility(View.INVISIBLE);
            dist43.setVisibility(View.INVISIBLE);
            dist42.setVisibility(View.INVISIBLE);
            dist41.setVisibility(View.INVISIBLE);
            dist33.setVisibility(View.INVISIBLE);
            dist32.setVisibility(View.INVISIBLE);
            dist31.setVisibility(View.INVISIBLE);
            dist22.setVisibility(View.VISIBLE);
            dist21.setVisibility(View.VISIBLE);
            dist1.setVisibility(View.INVISIBLE);
            if(distance > 10) {
                this.activity.runOnUiThread(() -> {
                    markerLocation.setVisibility(View.VISIBLE);
                    markerLabel.setVisibility(View.INVISIBLE);
                });
            }
            else if (distance <= 10 && distance > 1) {
                this.activity.runOnUiThread(() -> {
                    markerLocation.setVisibility(View.VISIBLE);
                    markerLabel.setVisibility(View.VISIBLE);
                });

                //interpolation: get dp distance on compass using interpolation
                layoutParams.circleRadius = MAX_RADIUS_IN_DP;
            }
            else {
                this.activity.runOnUiThread(() -> {
                    markerLocation.setVisibility(View.VISIBLE);
                    markerLabel.setVisibility(View.VISIBLE);
                });

                //interpolation: get dp distance on compass using interpolation
                float radiusOnCompass = distance*(MAX_RADIUS_IN_DP/2);
                int radius = convertDpToPixel(radiusOnCompass);
                layoutParams.circleRadius = radius;
            }
        }

        else if (zoomSetting == 3) {
            compassViewO.setVisibility(View.VISIBLE);
            compassView43.setVisibility(View.INVISIBLE);
            compassView42.setVisibility(View.INVISIBLE);
            compassView41.setVisibility(View.INVISIBLE);
            compassView32.setVisibility(View.VISIBLE);
            compassView31.setVisibility(View.VISIBLE);
            dist44.setVisibility(View.INVISIBLE);
            dist43.setVisibility(View.INVISIBLE);
            dist42.setVisibility(View.INVISIBLE);
            dist41.setVisibility(View.INVISIBLE);
            dist33.setVisibility(View.VISIBLE);
            dist32.setVisibility(View.VISIBLE);
            dist31.setVisibility(View.VISIBLE);
            dist22.setVisibility(View.INVISIBLE);
            dist21.setVisibility(View.INVISIBLE);
            dist1.setVisibility(View.INVISIBLE);
            if(distance > 500) {
                this.activity.runOnUiThread(() -> {
                    markerLocation.setVisibility(View.VISIBLE);
                    markerLabel.setVisibility(View.INVISIBLE);
                });
            }
            else if (distance <= 500 && distance > 10) {
                this.activity.runOnUiThread(() -> {
                    markerLocation.setVisibility(View.VISIBLE);
                    markerLabel.setVisibility(View.VISIBLE);
                });

                //interpolation: get dp distance on compass using interpolation
                layoutParams.circleRadius = MAX_RADIUS_IN_DP;
            }
            else if (distance <= 10 && distance > 1) {
                this.activity.runOnUiThread(() -> {
                    markerLocation.setVisibility(View.VISIBLE);
                    markerLabel.setVisibility(View.VISIBLE);
                });

                //interpolation: get dp distance on compass using interpolation
                float radiusOnCompass = ((distance - 1)/9) * (MAX_RADIUS_IN_DP/3);
                int radius = convertDpToPixel(radiusOnCompass) + (int) 2 * MAX_RADIUS_IN_DP/3;
                layoutParams.circleRadius = radius;
            }
            else {
                this.activity.runOnUiThread(() -> {
                    markerLocation.setVisibility(View.VISIBLE);
                    markerLabel.setVisibility(View.VISIBLE);
                });

                //interpolation: get dp distance on compass using interpolation
                float radiusOnCompass = distance*MAX_RADIUS_IN_DP/3;
                int radius = convertDpToPixel(radiusOnCompass);
                layoutParams.circleRadius = radius;
            }
        }

        else {
            compassViewO.setVisibility(View.VISIBLE);
            compassView43.setVisibility(View.VISIBLE);
            compassView42.setVisibility(View.VISIBLE);
            compassView41.setVisibility(View.VISIBLE);
            compassView32.setVisibility(View.INVISIBLE);
            compassView31.setVisibility(View.INVISIBLE);
            dist44.setVisibility(View.VISIBLE);
            dist43.setVisibility(View.VISIBLE);
            dist42.setVisibility(View.VISIBLE);
            dist41.setVisibility(View.VISIBLE);
            dist33.setVisibility(View.INVISIBLE);
            dist32.setVisibility(View.INVISIBLE);
            dist31.setVisibility(View.INVISIBLE);
            dist22.setVisibility(View.INVISIBLE);
            dist21.setVisibility(View.INVISIBLE);
            dist1.setVisibility(View.INVISIBLE);
            if(distance > 500) {
                this.activity.runOnUiThread(() -> {
                    markerLocation.setVisibility(View.VISIBLE);
                    markerLabel.setVisibility(View.VISIBLE);
                });
                layoutParams.circleRadius = MAX_RADIUS_IN_DP;
            }
            else if (distance <= 500 && distance > 10) {
                this.activity.runOnUiThread(() -> {
                    markerLocation.setVisibility(View.VISIBLE);
                    markerLabel.setVisibility(View.VISIBLE);
                });

                //interpolation: get dp distance on compass using interpolation
                float radiusOnCompass = ((distance - 10)/490) * (MAX_RADIUS_IN_DP/4);
                int radius = convertDpToPixel(radiusOnCompass) + (int) (3*MAX_RADIUS_IN_DP/4);

                if(radius > MAX_RADIUS_IN_DP) {
                    radius = MAX_RADIUS_IN_DP;
                }

                layoutParams.circleRadius = radius;
            }
            else if (distance <= 10 && distance > 1) {
                this.activity.runOnUiThread(() -> {
                    markerLocation.setVisibility(View.VISIBLE);
                    markerLabel.setVisibility(View.VISIBLE);
                });

                //interpolation: get dp distance on compass using interpolation
                float radiusOnCompass = ((distance - 1)/9) * (MAX_RADIUS_IN_DP/4);
                int radius = convertDpToPixel(radiusOnCompass) + (int) 2*MAX_RADIUS_IN_DP/4;
                layoutParams.circleRadius = radius;
            }
            else {
                this.activity.runOnUiThread(() -> {
                    markerLocation.setVisibility(View.VISIBLE);
                    markerLabel.setVisibility(View.VISIBLE);
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
