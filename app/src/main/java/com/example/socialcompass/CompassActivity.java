package com.example.socialcompass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CompassActivity extends AppCompatActivity {


    public ArrayList<Marker> friends = new ArrayList<>();

    private LocationService locationService;
    private OrientationService orientationService;
    private MarkerBuilder builder = new MarkerBuilder();
    public CurrentState currentState;


    //The number of locations that can be shown on the compass
    public int numOfLocations = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);


        //fill arrays with data from intents
        loadFriendsFromUIDs();
        fillFriends();


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
    public void loadFriendsFromUIDs(){

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("UIDs",MODE_PRIVATE);
        var UIDs = preferences.getAll();

        //refactor for marker builder class
        for(String key: UIDs.keySet()){
            friends.add(builder.createMarker(key));
        }

    }



    public void fillFriends(){
        int index = 0;
        for(var marker:friends){
            builder.addUIElements(index,marker,this);
            index++;
        }
    }


    public void goHomeClicked(View view) {
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}

