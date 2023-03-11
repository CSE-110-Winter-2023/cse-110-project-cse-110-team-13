package com.example.socialcompass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;

import java.util.ArrayList;

public class CompassActivity extends AppCompatActivity {

    public ArrayList<Marker> friends = new ArrayList<>();

    private LocationService locationService;
    private OrientationService orientationService;
    private MarkerBuilder builder = new MarkerBuilder();
    private DisplayUpdate display = new DisplayUpdate(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);

        // fill arrays with data from intents
        loadFriendsFromUIDs();
        for (int i = 0; i < friends.size(); i++) {
            var currMarker = friends.get(i);
            // float angle = AngleUtil.compassCalculateAngle("0,0", currMarker.getCoordinate(), 0);

            builder = builder.addUIElements(i, currMarker, this);
        }
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

    public void goHomeClicked(View view) {
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}

