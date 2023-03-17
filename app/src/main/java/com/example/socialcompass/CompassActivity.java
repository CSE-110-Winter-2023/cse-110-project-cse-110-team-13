package com.example.socialcompass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import java.util.ArrayList;

public class CompassActivity extends AppCompatActivity {

    public ArrayList<Marker> friends = new ArrayList<>();

    private MarkerBuilder builder;
    private Display display;
    private Device device;
    private ServerListener serverListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        this.builder = new MarkerBuilder();
        // fill markers with data from shred preferences
        loadFriendsFromUIDs();
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("thisUserID", MODE_PRIVATE);
        String privateUID = prefs.getString("UUID", "qwerty");

        //calculate intial angle to give to marker builder
        for (int i = 0; i < friends.size(); i++) {
            var currMarker = friends.get(i);
            float angle = 0;
            try {
                angle = Utilities.compassCalculateAngle("0,0", currMarker.getCoordinate(), 0);
            }
            catch(Exception ignored) {};

            //create ui element for markers
            builder = builder.addUIElements(i, currMarker, angle, this);
        }


        LocationService locationService = new LocationService(this);
        OrientationService orientationService = new OrientationService(this);
        TimeService timeService = new TimeService();
        this.display = new Display(this, getApplicationContext());
        this.device = new Device(this, locationService, orientationService, timeService);
        this.serverListener = new ServerListener(this, privateUID);
        CurrentState currentState = new CurrentState(this, this.serverListener, this.device, this.display, this.friends);

        this.serverListener.registerServerObserver(currentState);
        this.device.registerDeviceObserver(currentState);








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

    public void onZoomInClicked(View view)
    {
        Button btn = findViewById(R.id.zoomIn);
        Button zo = findViewById(R.id.zoomOut);
        int setting = display.getZoomSetting();
        if (setting > 1)
        {
            display.setZoomSetting(setting - 1);
            //emulator purple
            btn.setBackgroundColor(0xFF6200EE);
            zo.setBackgroundColor(0xFF6200EE);
            if (display.getZoomSetting() == 1)
                //medium gray
                btn.setBackgroundColor(0xFF7F7F7F);
        }
        else
            btn.setBackgroundColor(0xFF7F7F7F);
    }

    public void onZoomOutClicked(View view)
    {
        Button btn = findViewById(R.id.zoomOut);
        Button zi = findViewById(R.id.zoomIn);
        int setting = display.getZoomSetting();
        if (setting < 4)
        {
            display.setZoomSetting(setting + 1);
            btn.setBackgroundColor(0xFF6200EE);
            zi.setBackgroundColor(0xFF6200EE);
            if (display.getZoomSetting() == 4)
                btn.setBackgroundColor(0xFF7F7F7F);
        }
        else
            btn.setBackgroundColor(0xFF7F7F7F);
    }

    public void goHomeClicked(View view) {
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}

