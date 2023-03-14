package com.example.socialcompass.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.socialcompass.State.CurrentState;
import com.example.socialcompass.State.Device;
import com.example.socialcompass.UI.Display;
import com.example.socialcompass.State.LocationService;
import com.example.socialcompass.UI.Marker;
import com.example.socialcompass.UI.MarkerBuilder;
import com.example.socialcompass.State.OrientationService;
import com.example.socialcompass.R;
import com.example.socialcompass.Server.ServerListener;
import com.example.socialcompass.Utilities.AngleUtil;

import java.util.ArrayList;

public class CompassActivity extends AppCompatActivity {

    public ArrayList<Marker> friends = new ArrayList<>();

    private LocationService locationService;
    private OrientationService orientationService;
    private MarkerBuilder builder;
    private Display display;
    private Device device;
    private ServerListener serverListener;
    private String privateUID;
    private CurrentState currentState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        this.builder = new MarkerBuilder();
        // fill arrays with data from intents
        loadFriendsFromUIDs();
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("thisUserID", MODE_PRIVATE);
        privateUID = prefs.getString("UUID", "qwerty");
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

