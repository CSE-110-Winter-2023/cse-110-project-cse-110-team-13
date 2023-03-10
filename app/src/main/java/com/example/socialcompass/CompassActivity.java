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


        // fill arrays with data from intents
        loadFriendsFromUIDs();
        fillFriends();

        locationService = LocationService.singleton(this);
        orientationService = OrientationService.singleton(this);
        for (int i = 0; i < friends.size(); i++) {
            builder.addUIElements(i, friends.get(i), this);
        }
        //currentState.notifyObserver();
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

    /*
    Creates the UIElements for markers
     */
    public void fillFriends(){
        int numLocations = friends.size();
        currentState = new CurrentState(locationService, orientationService, this, friends);
    }

    public void goHomeClicked(View view) {
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}

