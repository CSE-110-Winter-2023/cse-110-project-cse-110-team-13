package com.example.socialcompass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

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

    public float userAngle; // Current user angle, relative to North

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

        // Initialize userAngle to 0 (North)
        userAngle = 0;

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
            if (Objects.equals(locationsCoordinates[i], "default"))
            {
                findViewById(locationPointerIDs[i]).setVisibility(View.INVISIBLE);
                findViewById(labelPointerIDs[i]).setVisibility(View.INVISIBLE);
                continue;
            }
            //compute angle and update marker and label
            float angle = AngleUtil.compassCalculateAngle("0,0", locationsCoordinates[i]);
            updatePointer(locationPointerIDs[i], angle);
            updateLabelPointer(i);
        }
    }

    public void redraw(){

        // Redraw Compass face to new userAngle
        ImageView compassFace = findViewById(R.id.CompassFace);
        compassFace.setRotation(userAngle);

        // Redraw each location pointer to new userAngle
        for (int i = 0; i < numOfLocations; i++){
            float angle = AngleUtil.compassCalculateAngle("0,0", locationsCoordinates[i]);
            angle = angle + userAngle;
            updatePointer(locationPointerIDs[i], angle);
            updateLabelPointer(i);
        }
    }

    public void rotate90(View view){
        userAngle = (userAngle + 90 > 360) ? (userAngle + 90 - 360) : userAngle + 90;
        redraw();
    }

    public void rotate60(View view){
        userAngle = (userAngle + 60 > 360) ? (userAngle + 60 - 360) : userAngle + 60;
        redraw();
    }

    public void rotate30(View view){
        userAngle = (userAngle + 30 > 360) ? (userAngle + 30 - 360) : userAngle + 30;
        redraw();
    }

    //fills location array
    public void loadLocationCoordinates(){
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("locationLabels",MODE_PRIVATE);
        Map<String,?> locationLabels = preferences.getAll();

        /*
        for(var entry : locationLabels.entrySet()){



        }
        */


        String[] locationNames = {"myHomeLocation","familyLocation","friendLocation"};

        for(int i = 0; i < locationNames.length; i++){
            locationsCoordinates[i] = preferences.getString(locationNames[i], "default");
        }




    }

    //fill label array
    public void loadLocationLabels(){

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("locationLabels",MODE_PRIVATE);
        String[] locationNames = {"myHomeLabel","familyLabel","friendLabel"};

        for(int i = 0; i < locationNames.length; i++){
            locationsLabels[i] = preferences.getString(locationNames[i], "default" + locationNames[i]);
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
        ConstraintLayout.LayoutParams layoutParamsMarker = (ConstraintLayout.LayoutParams) marker.getLayoutParams();
        ConstraintLayout.LayoutParams layoutParamsLabel = (ConstraintLayout.LayoutParams) label.getLayoutParams();
        layoutParamsLabel.circleAngle = layoutParamsMarker.circleAngle;
        layoutParamsLabel.circleRadius = layoutParamsMarker.circleRadius + 100;
        label.setLayoutParams(layoutParamsLabel);
    }

    public void goHomeClicked(View view) {
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
