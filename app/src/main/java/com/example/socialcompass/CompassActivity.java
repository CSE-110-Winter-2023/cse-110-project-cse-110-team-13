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
    private MarkerBuilder builder;
    private Display display;
    private Device device;
    private ServerListener serverListener;
    private String privateUID = "team13testdummy";
    private CurrentState currentState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        this.builder = new MarkerBuilder(getApplicationContext());
        // fill arrays with data from intents
        loadFriendsFromUIDs();
        for (int i = 0; i < friends.size(); i++) {
            var currMarker = friends.get(i);
            float angle = 0;
            try {
                angle = AngleUtil.compassCalculateAngle("0,0", currMarker.getCoordinate(), 0);
            }
            catch(Exception e) {};

            builder = builder.addUIElements(i, currMarker, angle, this);
        }


        this.locationService = new LocationService(this);
        this.orientationService = new OrientationService(this);
        this.display = new Display(this);
        this.device = new Device(this, this.locationService, this.orientationService);
        this.serverListener = new ServerListener(this, this.privateUID);
        this.currentState = new CurrentState(this, this.serverListener, this.device, this.display, this.friends);

        this.serverListener.registerServerObserver(this.currentState);
        this.device.registerDeviceObserver(this.currentState);


        initialise();

    }
    //set up listeners in device and serverListener.
    public void initialise() {
        this.serverListener.notifyObserver();
        this.device.notifyObserver();
    }
    /*
    Loads friends UIDs from shared preferences into array
     */
    public void loadFriendsFromUIDs(){

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("UIDs",MODE_PRIVATE);
        var UIDs = preferences.getAll();

        //refactor for marker builder class
        for(String key: UIDs.keySet()){
            this.friends.add(builder.createMarker(key));
        }
    }

    public void goHomeClicked(View view) {
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}

