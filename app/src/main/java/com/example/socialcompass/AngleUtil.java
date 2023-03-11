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
        double lat2 = 0;
        try {
            lat1 = Double.parseDouble(objectCoords[0]);
        } catch (Exception e) {
            throw new Exception(e);

        }
        try {
            lat2 = Double.parseDouble(objectCoords[1]);

        } catch (Exception e) {
            throw new Exception(e);

        }
        var markerCoord = new LatLng(lat1, lat2);

        String[] location = userLocation.split(",");
        double lat3 = 0;
        try {
            lat3 = Double.parseDouble(location[0]);

        } catch (Exception e) {
            throw new Exception(e);

        }
        double lat4 = 0;
        try {
            lat4 = Double.parseDouble(location[1]);
        } catch (Exception e) {
            throw new Exception(e);

        }

        var userCoord = new LatLng(lat3, lat4);
        //get differences between both for angle compute
        float northRelative = (float) SphericalUtil.computeHeading(userCoord, markerCoord);
        northRelative = northRelative - currOrientation;

        northRelative = northRelative % 360;
        return northRelative;
    }
}
