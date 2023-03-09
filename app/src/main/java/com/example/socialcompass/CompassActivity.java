package com.example.socialcompass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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


    public ArrayList<Marker> friends = new ArrayList<>();
    public RecyclerView recyclerView;

    private LocationService locationService;
    private OrientationService orientationService;
    public CurrentState currentState;

    public String publicCodeUID = "team13testdummy";
    public String privateCodeUID = "team13testdummy";
    //The number of locations that can be shown on the compass
    public int numOfLocations = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);


        //fill arrays with data from intents
        loadFriends();

        CurrentState currentState = new CurrentState(this, friends, )
        /*

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
        */




    }

    /*
    Loads friends UIDs from shared preferences into array
     */
    public void loadFriends(){

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("UIDs",MODE_PRIVATE);
        var UIDs = preferences.getAll();

        MarkerFactory factory = new MarkerFactory();

        //refactor for marker builder class
        for(String key: UIDs.keySet()){
            friends.add(factory.createMarker(key));
        }

        Log.d("test1", "size of friends: " + String.valueOf(friends.size()));
        fillFriends();
    }

    /*
    Fills out information about the markers
     */
    public void fillFriends(){

        MarkerAdapter adapter = new MarkerAdapter();
        adapter.setHasStableIds(true);
        adapter.setMarkers(friends);

        recyclerView = findViewById(R.id.markersList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }


    public void goHomeClicked(View view) {
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}

