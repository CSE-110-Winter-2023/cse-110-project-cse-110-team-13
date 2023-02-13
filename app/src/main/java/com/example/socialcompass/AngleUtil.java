package com.example.socialcompass;

public class AngleUtil {
    //compute angle at which marker and TextView must be placed
    //takes user location and object location as input
    public static float compassCalculateAngle(String userLocation, String objectLocation)
    {
        //split user coords into x and y
        String[] userCoords = userLocation.split(",");
        //split object coords into x and y
        String[] objectCoords = objectLocation.split(",");
        //get differences between both for angle compute
        float xDiff = Float.parseFloat(objectCoords[0]) - Float.parseFloat(userCoords[0]);
        float yDiff = Float.parseFloat(objectCoords[1]) - Float.parseFloat(userCoords[1]);
        //return arctan(y/x), and account for arctan's principal values
        return -((float) Math.toDegrees((float) Math.atan(yDiff/xDiff) + (float) ((xDiff < 0) ? Math.PI : 0)) - 90);
    }
}
