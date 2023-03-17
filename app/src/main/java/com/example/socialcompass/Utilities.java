package com.example.socialcompass;

import android.app.Activity;
import android.app.AlertDialog;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.util.Locale;

public class Utilities {
    public static void showAlert(Activity activity, String message) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity);
        alertBuilder
                .setTitle("Alert!")
                .setMessage(message)
                .setPositiveButton("I understand", (dialog, i) -> {dialog.cancel();})
                .setCancelable(true);
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    public static String formatTime(long time) {
       long seconds = (time /1000) % 60;
       long minutes = (time / (1000 * 60)) % 60;
       long hours = (time / (1000 * 60 * 6)) % 24;

        if (hours >= 1) {
            return String.format("%d hr", hours);
        }
        if (minutes > 0) {
            return String.format("%d m", minutes);
        }
        if (seconds > 0) {
            return String.format("%d s", seconds);
        }
        return "0";

    }

    //compute angle at which marker and TextView must be placed
    // userLocation: String - the string of the user's current coordinate in the format "<latitude>,<longitude>"
    // objectLocation:  String - the string of the marker's current coordinate in the format "<latitude>,<longitude>"
    // orientation: float - the current orientation of the phone wrt North. In radian.
    public static float compassCalculateAngle(String userLocation, String objectLocation, float orientation) throws Exception {
        //convert radian to angles
        float currOrientation = orientation * 180 / ((float) Math.PI);
        //split object coords into x and y
        String[] objectCoords = objectLocation.split(",");
        double lat1 = 0;
        double lon1 = 0;
        try {
            lat1 = Double.parseDouble(objectCoords[0]);
        } catch (Exception e) {
            throw new Exception(e);
        }
        try {
            lon1 = Double.parseDouble(objectCoords[1]);

        } catch (Exception e) {
            throw new Exception(e);

        }
        var markerCoord = new LatLng(lat1, lon1);

        String[] location = userLocation.split(",");
        double lat2 = 0;
        try {
            lat2 = Double.parseDouble(location[0]);

        } catch (Exception e) {
            throw new Exception(e);

        }
        double lon2 = 0;
        try {
            lon2 = Double.parseDouble(location[1]);
        } catch (Exception e) {
            throw new Exception(e);

        }

        var userCoord = new LatLng(lat2, lon2);
        //get differences between both for angle compute
        float northRelative = (float) SphericalUtil.computeHeading(userCoord, markerCoord);
        northRelative = northRelative - currOrientation;

        northRelative = northRelative % 360;
        return northRelative;
    }

    //Calculate the distance between the user's location and a marker's location, return
    // the distance in miles
    public static float markerCalculateDistance(String userLocation, String markerLocation) throws Exception {
        String[] objectCoords = userLocation.split(",");
        double lat1 = 0;
        double lon1 = 0;
        try {
            lat1 = Double.parseDouble(objectCoords[0]);
        } catch (Exception e) {
            throw new Exception(e);
        }
        try {
            lon1 = Double.parseDouble(objectCoords[1]);

        } catch (Exception e) {
            throw new Exception(e);

        }
        var userCoord = new LatLng(lat1, lon1);

        String[] location = markerLocation.split(",");
        double lat2 = 0;
        try {
            lat2 = Double.parseDouble(location[0]);

        } catch (Exception e) {
            throw new Exception(e);

        }
        double lon2 = 0;
        try {
            lon2 = Double.parseDouble(location[1]);
        } catch (Exception e) {
            throw new Exception(e);

        }

        var markerCoord = new LatLng(lat2, lon2);

        float relativeDistance = (float) SphericalUtil.computeDistanceBetween(userCoord, markerCoord);
        // convert meters to miles because #America #landoftheFree
        relativeDistance = relativeDistance * 0.000621f;
        return relativeDistance;
    }
}
