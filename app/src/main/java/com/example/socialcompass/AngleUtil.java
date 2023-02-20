package com.example.socialcompass;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

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
    public static float compassCalculateAngle(String userLocation, String objectLocation, float orientation)
    {
        //convert radian to angles
        orientation = orientation*180/((float) Math.PI);
        //split user coords into x and y
        String[] userCoords = userLocation.split(",");
        //split object coords into x and y
        String[] objectCoords = objectLocation.split(",");
        double lat1 = Double.parseDouble(objectCoords[0]);
        double lat2 = Double.parseDouble(objectCoords[1]);

        var markerCoord = new LatLng(lat1, lat2);

        double lat3 = Double.parseDouble(userCoords[0]);
        double lat4 =  Double.parseDouble(userCoords[1]);
        var userCoord = new LatLng(lat3, lat4);
        //get differences between both for angle compute
        float northRelative = (float) SphericalUtil.computeHeading(userCoord, markerCoord);
        northRelative = northRelative-orientation;

        northRelative = northRelative % 360;
        return northRelative;
    }
}
