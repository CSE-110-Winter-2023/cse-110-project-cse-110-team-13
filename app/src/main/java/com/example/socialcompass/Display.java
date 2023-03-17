package com.example.socialcompass;

import android.app.Activity;
import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.concurrent.Future;

import okhttp3.internal.Util;

public class Display {

    private Activity activity;
    private Context context;

    private ImageView[] compassViews;
    private TextView[] textViews;

    //the zoom setting for the display
    // zoomSetting == 1: only 1 circle from 0 - 1 mile
    // zoomSetting == 2: 2 circle, first one from 0 - 1 mile, the other from 1 - 10 mile
    // zoomSetting == 3: 3 circle, the third one from 10 - 500 mile
    // zoomSetting == 4 : 4 circle, fourth one is just a perimeter with anything outside of the 500 mile
    // range will lie on the perimeter
    private int zoomSetting = 2;
    private int MAX_RADIUS_IN_DP = 160;
    private boolean[][] spots;

    public Display(Activity activity, Context context) {

        //the spaces that a marker can be in
        spots = new boolean[36][11];

        this.activity = activity;
        this.context = context;

        loadUIElements();
    }


    public void loadUIElements(){

        compassViews = new ImageView[]{
                activity.findViewById(R.id.CompassFaceO),
                activity.findViewById(R.id.CompassFace4_3),
                activity.findViewById(R.id.CompassFace4_2),
                activity.findViewById(R.id.CompassFace4_1),
                activity.findViewById(R.id.CompassFace3_2),
                activity.findViewById(R.id.CompassFace3_1)
        };

        textViews = new TextView[]{
                activity.findViewById(R.id.dist44),
                activity.findViewById(R.id.dist43),
                activity.findViewById(R.id.dist42),
                activity.findViewById(R.id.dist41),
                activity.findViewById(R.id.dist33),
                activity.findViewById(R.id.dist32),
                activity.findViewById(R.id.dist31),
                activity.findViewById(R.id.dist22),
                activity.findViewById(R.id.dist21),
                activity.findViewById(R.id.dist1)
        };
    }

    public void allViewInvis(){
        for(ImageView i: compassViews){ i.setVisibility(View.INVISIBLE);
        }
        for(TextView t: textViews){ t.setVisibility(View.INVISIBLE);
        }
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

    public void updateNoSignal(ImageView greenDot, ImageView redDot, TextView timeSinceLastUpdate, long time) {
        this.activity.runOnUiThread(() -> {
            greenDot.setVisibility(View.INVISIBLE);
            redDot.setVisibility(View.VISIBLE);
            timeSinceLastUpdate.setVisibility(View.VISIBLE);
            timeSinceLastUpdate.setText(Utilities.formatTime(time));
        });

    }

    public void updateHasSignal(ImageView greenDot, ImageView redDot, TextView timeSinceLastUpdate) {
        this.activity.runOnUiThread(() -> {
            greenDot.setVisibility(View.VISIBLE);
            redDot.setVisibility(View.INVISIBLE);
            timeSinceLastUpdate.setVisibility(View.INVISIBLE);
        });

    }
    // update a marker imageview with a new angle and distance
    public void updatePointer(ImageView markerLocation, TextView markerLabel, double angle, float distance, String ogLabel){

        //resets label incase it has been truncated
        markerLabel.setText(ogLabel);

        //marks old spot as unused
        ConstraintLayout.LayoutParams layoutParamsOld = (ConstraintLayout.LayoutParams) markerLocation.getLayoutParams();
        int oldIndexAngle = getAngleIndex(layoutParamsOld.circleAngle);
        int oldIndexRadius = getRadiusIndex(layoutParamsOld.circleRadius);
        spots[oldIndexAngle][oldIndexRadius] = false;

        int radiusToBe = calculateRadius(distance);
        //check its new spot to see if it overlapps
        int indexAngle = getAngleIndex(angle);
        int indexRadius = getRadiusIndex(radiusToBe);

        //check if next to anyone and should truncate label
        if(truncateCheck(indexAngle,indexRadius)) truncate(markerLabel);

        //If its already at the center can't push it down any farther
        if(spots[indexAngle][indexRadius] && indexRadius != 0){
            updatePointer(markerLocation,markerLabel,angle,(float)(distance * 0.5),ogLabel);
        }
        else{

            spots[indexAngle][indexRadius] = true;
            updateCompass(markerLocation,markerLabel,distance);
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) markerLocation.getLayoutParams();
            layoutParams.circleAngle = (float) angle;
            layoutParams.circleRadius = radiusToBe;

            //this HAS TO BE RUN ON UI THREAD, this is because the origin of the call for this function
            // is in a listener, which runs in a background thread (originally called in device.notifyObserver())
            // so if we want the update to be in real time, it has to run on ui thread.
            this.activity.runOnUiThread(() -> {
                markerLocation.setLayoutParams(layoutParams);
            });

            }
    }

    public boolean truncateCheck(int indexAngle, int indexRadius){
        int truncateCheck;
        if(indexAngle < 35)  truncateCheck = indexAngle + 1;
        else  truncateCheck = 0;

        return spots[truncateCheck][indexRadius];
    }

    public void truncate(TextView markerLabel){
        String label = (String) markerLabel.getText();
        if(label.length() > 10){
            label = label.substring(0,5) + "...";
        }
        markerLabel.setText(label);

    }

    public int getRadiusIndex(float radius){
        int index = (int)((radius ) /48);

        if(index > 10){index = 10;}
        return index;
    }

    public int getAngleIndex(double angle){
        int index = (int)((angle + 360)%360 /10);

        if(index > 35){index = 35;}
        return index;
    }

    public void updateCompass(ImageView markerLocation, TextView markerLabel, float distance){
        switch (zoomSetting) {
            case 1:
                this.activity.runOnUiThread(() -> {
                    allViewInvis();
                    compassViews[0].setVisibility(View.VISIBLE);
                    textViews[9].setVisibility(View.VISIBLE);
                });

                if (distance > 1) {
                    this.activity.runOnUiThread(() -> {
                        markerLocation.setVisibility(View.VISIBLE);
                        markerLabel.setVisibility(View.INVISIBLE);
                    });
                } else {
                    this.activity.runOnUiThread(() -> {
                        markerLocation.setVisibility(View.VISIBLE);
                    });
                }
                break;
            case 2:
                this.activity.runOnUiThread(() -> {
                    allViewInvis();
                    compassViews[0].setVisibility(View.VISIBLE);
                    compassViews[2].setVisibility(View.VISIBLE);
                    textViews[7].setVisibility(View.VISIBLE);
                    textViews[8].setVisibility(View.VISIBLE);
                });

                if (distance > 10) {
                    this.activity.runOnUiThread(() -> {
                        markerLocation.setVisibility(View.VISIBLE);
                        markerLabel.setVisibility(View.INVISIBLE);
                    });
                } else if (distance <= 10 && distance > 1) {
                    this.activity.runOnUiThread(() -> {
                        markerLocation.setVisibility(View.VISIBLE);
                        markerLabel.setVisibility(View.VISIBLE);
                    });


                } else {
                    this.activity.runOnUiThread(() -> {
                        markerLocation.setVisibility(View.VISIBLE);
                        markerLabel.setVisibility(View.VISIBLE);
                    });

                }
            break;
            case 3:
                this.activity.runOnUiThread(() -> {
                    allViewInvis();
                    compassViews[0].setVisibility(View.VISIBLE);
                    compassViews[4].setVisibility(View.VISIBLE);
                    compassViews[5].setVisibility(View.VISIBLE);
                    textViews[4].setVisibility(View.VISIBLE);
                    textViews[5].setVisibility(View.VISIBLE);
                    textViews[6].setVisibility(View.VISIBLE);
                });


                if (distance > 500) {
                    this.activity.runOnUiThread(() -> {
                        markerLocation.setVisibility(View.VISIBLE);
                        markerLabel.setVisibility(View.INVISIBLE);
                    });
                } else if (distance <= 500 && distance > 10) {
                    this.activity.runOnUiThread(() -> {
                        markerLocation.setVisibility(View.VISIBLE);
                        markerLabel.setVisibility(View.VISIBLE);
                    });


                } else if (distance <= 10 && distance > 1) {
                    this.activity.runOnUiThread(() -> {
                        markerLocation.setVisibility(View.VISIBLE);
                        markerLabel.setVisibility(View.VISIBLE);
                    });

                } else {
                    this.activity.runOnUiThread(() -> {
                        markerLocation.setVisibility(View.VISIBLE);
                        markerLabel.setVisibility(View.VISIBLE);
                    });

                }
            break;
            default:
                this.activity.runOnUiThread(() -> {
                    allViewInvis();
                    compassViews[0].setVisibility(View.VISIBLE);
                    compassViews[1].setVisibility(View.VISIBLE);
                    compassViews[2].setVisibility(View.VISIBLE);
                    compassViews[3].setVisibility(View.VISIBLE);
                    textViews[0].setVisibility(View.VISIBLE);
                    textViews[1].setVisibility(View.VISIBLE);
                    textViews[2].setVisibility(View.VISIBLE);
                    textViews[3].setVisibility(View.VISIBLE);
                });

                if (distance > 500) {
                    this.activity.runOnUiThread(() -> {
                        markerLocation.setVisibility(View.VISIBLE);
                        markerLabel.setVisibility(View.VISIBLE);
                    });

                } else if (distance <= 500 && distance > 10) {
                    this.activity.runOnUiThread(() -> {
                        markerLocation.setVisibility(View.VISIBLE);
                        markerLabel.setVisibility(View.VISIBLE);
                    });

                } else if (distance <= 10 && distance > 1) {
                    this.activity.runOnUiThread(() -> {
                        markerLocation.setVisibility(View.VISIBLE);
                        markerLabel.setVisibility(View.VISIBLE);
                    });

                } else {
                    this.activity.runOnUiThread(() -> {
                        markerLocation.setVisibility(View.VISIBLE);
                        markerLabel.setVisibility(View.VISIBLE);
                    });

                }
            }
        }




    }

    public int calculateRadius(float distance){

        int radiusToBe = 0;
        if(zoomSetting == 1) {

            if(distance > 1) {
                radiusToBe = convertDpToPixel(MAX_RADIUS_IN_DP);
                Log.d("test8","At zoom level 1 the radis to be is " + radiusToBe);


            }
            else {

                //interpolation: get dp distance on compass using interpolation
                float radiusOnCompass = distance*(MAX_RADIUS_IN_DP/2.0f);
                int radius = convertDpToPixel(radiusOnCompass);
                if(radius > convertDpToPixel(MAX_RADIUS_IN_DP)) {
                    radius = convertDpToPixel(MAX_RADIUS_IN_DP);
                }
                radiusToBe = radius;
                Log.d("test8","At zoom level 1 the radis to be is BAD " + radiusToBe);
            }
        }

        else if (zoomSetting == 2) {

            if(distance > 10) {

                radiusToBe = convertDpToPixel(MAX_RADIUS_IN_DP);

            }
            else if (distance <= 10 && distance > 1) {

                Log.d("test12",""+distance);

                //interpolation: get dp distance on compass using interpolation
                float radiusOnCompass = ((distance - 1)/9) * (MAX_RADIUS_IN_DP/2);
                int radius = convertDpToPixel(radiusOnCompass) + convertDpToPixel(MAX_RADIUS_IN_DP/2);
                radiusToBe = radius;
            }
            else {

                //interpolation: get dp distance on compass using interpolation
                float radiusOnCompass = distance*(MAX_RADIUS_IN_DP/4.0f);
                int radius = convertDpToPixel(radiusOnCompass);
                radiusToBe = radius;
            }
        }

        else if (zoomSetting == 3) {

            if(distance > 500) {
                radiusToBe = convertDpToPixel(MAX_RADIUS_IN_DP);

            }
            else if (distance <= 500 && distance > 10) {

                //interpolation: get dp distance on compass using interpolation
                float radiusOnCompass = ((distance - 10)/490) * (MAX_RADIUS_IN_DP/3);
                int radius = convertDpToPixel(radiusOnCompass) + convertDpToPixel( 2 * MAX_RADIUS_IN_DP/3);
                radiusToBe = radius;

            }
            else if (distance <= 10 && distance > 1) {

                //interpolation: get dp distance on compass using interpolation
                float radiusOnCompass = ((distance - 1)/9) * (MAX_RADIUS_IN_DP/3);
                int radius = convertDpToPixel(radiusOnCompass) + convertDpToPixel(MAX_RADIUS_IN_DP/3);
                radiusToBe = radius;
            }
            else {

                //interpolation: get dp distance on compass using interpolation
                float radiusOnCompass = distance*MAX_RADIUS_IN_DP/6.0f;
                int radius = convertDpToPixel(radiusOnCompass);
                radiusToBe = radius;
            }
        }

        else {


            if(distance > 500) {
                radiusToBe = convertDpToPixel(MAX_RADIUS_IN_DP);
            }
            else if (distance <= 500 && distance > 10) {


                //interpolation: get dp distance on compass using interpolation
                float radiusOnCompass = ((distance - 10)/490) * (MAX_RADIUS_IN_DP/4);
                int radius = convertDpToPixel(radiusOnCompass) + convertDpToPixel(2*MAX_RADIUS_IN_DP/4);

                if(radius > convertDpToPixel(MAX_RADIUS_IN_DP)) {
                    radius = convertDpToPixel(MAX_RADIUS_IN_DP);
                }

                radiusToBe = radius;
            }
            else if (distance <= 10 && distance > 1) {

                //interpolation: get dp distance on compass using interpolation
                float radiusOnCompass = ((distance - 1)/9) * (MAX_RADIUS_IN_DP/4);
                int radius = convertDpToPixel(radiusOnCompass) + convertDpToPixel(MAX_RADIUS_IN_DP/4);
                radiusToBe = radius;
            }
            else {


                //interpolation: get dp distance on compass using interpolation
                float radiusOnCompass = distance * (MAX_RADIUS_IN_DP/10.0f);
                int radius = convertDpToPixel(radiusOnCompass);
                radiusToBe = radius;
            }
        }


        Log.d("test8","The returned radius to be is "+ radiusToBe);
        return radiusToBe;
    }


}