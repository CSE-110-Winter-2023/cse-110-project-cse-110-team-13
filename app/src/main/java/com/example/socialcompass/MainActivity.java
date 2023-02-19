package com.example.socialcompass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.Manifest;
import android.widget.TextView;
public class MainActivity extends AppCompatActivity {
    //private OrientationService orientationService;
    private int numOfLocations;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200 );
        }
        /*
        TextView locationText = findViewById(R.id.mainMenu);
        orientationService = OrientationService.singleton(this);
        orientationService.getOrientation().observe(this, ori -> {
            locationText.setText(Float.toString(ori));
        });
        */
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("numOfLocations",MODE_PRIVATE);
        numOfLocations = preferences.getInt("numOfLocations",0);

        /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);*/

    }

    public void locationInputEnter(View view) {

        Intent intent = new Intent(this, LocationInput.class);
        startActivity(intent);


    }

    public void enterCompassActivity(View view) {

        //if there are no locations entered then go to location entering screen
        if(numOfLocations == 0){

            Utilities.showAlert(this, "Must enter at least 1 location");
        }
        else{
            Intent intent = new Intent(this, CompassActivity.class);
            startActivity(intent);
        }


    }

    public void resetButtonClicked(View view) {

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("numOfLocations",MODE_PRIVATE);
        preferences.edit().clear().commit();

        preferences = getApplicationContext().getSharedPreferences("locationLabels",MODE_PRIVATE);
        preferences.edit().clear().commit();

        finish();


    }
}