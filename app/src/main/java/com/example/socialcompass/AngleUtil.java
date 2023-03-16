package com.example.socialcompass;


import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import android.os.Bundle;
import android.util.Pair;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;

public class AngleUtil {
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
