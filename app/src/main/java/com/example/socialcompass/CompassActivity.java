package com.example.socialcompass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CompassActivity extends AppCompatActivity {


    public ArrayList<String> friends = new ArrayList<>();
    public ArrayList<Integer> markerIDs = new ArrayList<>();


    //arrays holding:
    //-coordinates passed from intent
    //-labels passed from intent
    //-IDs for the markers
    //-IDs for the marker label TextViews
    public String[] locationsCoordinates;
    public String[] locationsLabels;
    public int[] locationPointerIDs;
    public int[] labelPointerIDs;
    private LocationService locationService;
    private OrientationService orientationService;
    public CurrentState currentState;


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
        loadFriends();
        createMarkers(this);

        findViewById(markerIDs.get(0)).setVisibility(View.VISIBLE);

        loadLocationCoordinates();
        loadLocationLabels();
        loadLocationPointerIDs();
        loadLabelPointerIDs();




        //set up CurrentState
        locationService = LocationService.singleton(this);
        orientationService = OrientationService.singleton(this);
        currentState = new CurrentState(numOfLocations, locationService, orientationService, this);

        //initiate currentState and set TextViews to label text for all markers
        for (int i = 0; i < numOfLocations; i++) {
            currentState.setMarkerInfo(i, locationsCoordinates[i], locationsLabels[i],
                    locationPointerIDs[i], labelPointerIDs[i]);
            ((TextView) findViewById(labelPointerIDs[i])).setText(locationsLabels[i]);
        }
        //set up listener in currentState
        currentState.notifyObserver();



    }

    public void loadFriends(){

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("UIDs",MODE_PRIVATE);
        var UIDs = preferences.getAll();

        for(String s: UIDs.keySet()){
            friends.add(s);
        }

    }

    public void createMarkers(Context context){

        for(String s: friends){
            createMarker(context);
        }

    }

    public void createMarker(Context context){


    }


    //fills location array
    public void loadLocationCoordinates(){
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("locationLabels",MODE_PRIVATE);
        Map<String,?> locationLabels = preferences.getAll();

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

    public void goHomeClicked(View view) {
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}

