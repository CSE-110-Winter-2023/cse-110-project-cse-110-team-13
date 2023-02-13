package com.example.socialcompass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CompassActivity extends AppCompatActivity {
    //hashmap storing the names and coordinates of locations
    public String[] locationsCoordinates;
    public String[] locationsLabels;
    public int[] locationPointerIDs;

    public int[] labelPointerIDs;

    public int numOfLocations = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        locationsCoordinates = new String[numOfLocations];
        locationsLabels = new String[numOfLocations];
        locationPointerIDs = new int[numOfLocations];
        labelPointerIDs = new int[numOfLocations];
        loadLocationCoordinates();
        loadLocationLabels();
        loadLocationPointerIDs();
        loadLabelPointerIDs();

        for (int i = 0; i < numOfLocations; i++)
            ((TextView) findViewById(labelPointerIDs[i])).setText(locationsLabels[i]);

        for (int i = 0; i < numOfLocations; i++)
        {
            float angle = compassCalculateAngle("0,0", locationsCoordinates[i]);
            updatePointer(locationPointerIDs[i], angle);
            updateLabelPointer(i);
        }
    }

    public void loadLocationCoordinates(){

        //SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        Bundle extra = getIntent().getExtras();
        String[] locationNames = {"myHomeLocation","familyLocation","friendLocation"};

        for(int i = 0; i < locationNames.length; i++){
            locationsCoordinates[i] = extra.getString(locationNames[i], "default" + locationNames[i]);
        }


    }

    public void loadLocationLabels(){

        //SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        Bundle extra = getIntent().getExtras();
        String[] locationNames = {"myHomeLabel","familyLabel","friendLabel"};

        for(int i = 0; i < locationNames.length; i++){
            locationsLabels[i] = extra.getString(locationNames[i], "default" + locationNames[i]);
        }

    }

    public void loadLocationPointerIDs()
    {
        locationPointerIDs[0] = R.id.Marker1;
        locationPointerIDs[1] = R.id.Marker2;
        locationPointerIDs[2] = R.id.Marker3;
    }

    public void loadLabelPointerIDs()
    {
        labelPointerIDs[0] = R.id.Label1;
        labelPointerIDs[1] = R.id.Label2;
        labelPointerIDs[2] = R.id.Label3;
    }
    public float compassCalculateAngle(String userLocation, String objectLocation)
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

    public void updatePointer(int markerId, float angle){
        ImageView marker = findViewById(markerId);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) marker.getLayoutParams();
        layoutParams.circleAngle = angle;
        marker.setLayoutParams(layoutParams);
    }

    public void updateLabelPointer(int index)
    {
        TextView label = findViewById(labelPointerIDs[index]);
        ImageView marker = findViewById(locationPointerIDs[index]);
        float xCoord = marker.getX();
        float yCoord = marker.getY() + 50;
        yCoord = (yCoord > 0) ? yCoord : -yCoord;
        label.setX(xCoord);
        label.setY(yCoord);
    }

    public void goBackClicked(View view) {
        finish();
    }

    public void buttonPressed(View view) {
        ImageView marker = findViewById(locationPointerIDs[0]);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) marker.getLayoutParams();
        layoutParams.circleAngle += 10;
        marker.setLayoutParams(layoutParams);


    }
}