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
    private Display display;
    private Device device;
    private ServerListener serverListener;
    private String privateUID = "team13testdummy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);

        // fill arrays with data from intents
        loadFriendsFromUIDs();
        for (int i = 0; i < friends.size(); i++) {
            var currMarker = friends.get(i);
            AngleUtil util = new AngleUtil();
            try {
                float angle = util.compassCalculateAngle("0,0", currMarker.getCoordinate(), 0);
            }
            catch(Exception e) {};
            builder = builder.addUIElements(i, currMarker, this);
        }


        LocationService locationService = new LocationService(this);
        OrientationService orientationService = new OrientationService(this);
        Display display = new Display(this, this.friends);
        Device device = new Device(this, locationService, orientationService);
        ServerListener serverListener = new ServerListener(this, this.privateUID);
        CurrentState currentState = new CurrentState(this, serverListener, device, display);

        serverListener.registerServerObserver(currentState);
        device.registerDeviceObserver(currentState);


        initialise();

    }
    //set up listeners in device and serverListener.
    public void initialise() {
        device.notifyObserver();
        serverListener.notifyObserver();
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

