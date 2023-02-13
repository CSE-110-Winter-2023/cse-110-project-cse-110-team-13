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
    //arrays holding:
    //-coordinates passed from intent
    //-labels passed from intent
    //-IDs for the markers
    //-IDs for the marker label TextViews
    public String[] locationsCoordinates;
    public String[] locationsLabels;
    public int[] locationPointerIDs;

    public int[] labelPointerIDs;
    //The number of locations that can be shown on the compass
    public int numOfLocations = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        //initialize arrays
        locationsCoordinates = new String[numOfLocations];
        locationsLabels = new String[numOfLocations];
        locationPointerIDs = new int[numOfLocations];
        labelPointerIDs = new int[numOfLocations];
        //fill arrays with data from intents
        loadLocationCoordinates();
        loadLocationLabels();
        loadLocationPointerIDs();
        loadLabelPointerIDs();
        //set TextViews to label text
        for (int i = 0; i < numOfLocations; i++)
            ((TextView) findViewById(labelPointerIDs[i])).setText(locationsLabels[i]);

        //main loop:
        //initializes the locations of the markers and labels
        //when we add rotation, this will be done continuously
        for (int i = 0; i < numOfLocations; i++)
        {
            //if no coordinates are sent, don't draw the marker or its label
            if (locationsCoordinates[i] == "default")
            {
                findViewById(locationPointerIDs[i]).setVisibility(View.INVISIBLE);
                findViewById(labelPointerIDs[i]).setVisibility(View.INVISIBLE);
                continue;
            }
            //compute angle and update marker and label
            float angle = compassCalculateAngle("0,0", locationsCoordinates[i]);
            updatePointer(locationPointerIDs[i], angle);
            updateLabelPointer(i);
        }
    }

    //fills location array
    public void loadLocationCoordinates(){
        Bundle extra = getIntent().getExtras();
        String[] locationNames = {"myHomeLocation","familyLocation","friendLocation"};

        for(int i = 0; i < locationNames.length; i++){
            locationsCoordinates[i] = extra.getString(locationNames[i], "default");
        }


    }

    //fill label array
    public void loadLocationLabels(){

        //SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        Bundle extra = getIntent().getExtras();
        String[] locationNames = {"myHomeLabel","familyLabel","friendLabel"};

        for(int i = 0; i < locationNames.length; i++){
            locationsLabels[i] = extra.getString(locationNames[i], "default" + locationNames[i]);
        }

    }

    //fill marker ID array
    public void loadLocationPointerIDs()
    {
        locationPointerIDs[0] = R.id.Marker1;
        locationPointerIDs[1] = R.id.Marker2;
        locationPointerIDs[2] = R.id.Marker3;
    }

    //fill TextView ID array
    public void loadLabelPointerIDs()
    {
        labelPointerIDs[0] = R.id.Label1;
        labelPointerIDs[1] = R.id.Label2;
        labelPointerIDs[2] = R.id.Label3;
    }

    //compute angle at which marker and TextView must be placed
    //takes user location and object location as input
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

    //updates marker pointer with required angle
    public void updatePointer(int markerId, float angle){
        ImageView marker = findViewById(markerId);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) marker.getLayoutParams();
        layoutParams.circleAngle = angle;
        marker.setLayoutParams(layoutParams);
    }

    //updates TextView label pointer with required angle
    public void updateLabelPointer(int index)
    {
        TextView label = findViewById(labelPointerIDs[index]);
        ImageView marker = findViewById(locationPointerIDs[index]);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) marker.getLayoutParams();
        layoutParams.circleAngle -= 5;
        label.setLayoutParams(layoutParams);
    }

    public void goBackClicked(View view) {
        finish();
    }
}