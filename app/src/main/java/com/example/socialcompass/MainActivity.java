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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("thisUserID", MODE_PRIVATE);
        String nameSelected = preferences.getString("Name", "Name not found");
        if (nameSelected.equals("Name not found"))
        {
            Intent intent = new Intent(this, NameInput.class);
            startActivity(intent);
        }
        else {
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200 );
            }
            TextView uidText = findViewById(R.id.uidDisplay);
            String uid = preferences.getString("UUID", "Error: UID not generated yet!");
            uidText.setText("Your UID is: " + uid);
        }
    }

    public void locationInputEnter(View view) {

        Intent intent = new Intent(this, LocationInput.class);
        startActivity(intent);

    }

    public void enterCompassActivity(View view) {

        //if there are no locations show alert
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("UIDs",MODE_PRIVATE);

        int numOfLocations = preferences.getAll().size();

        if(numOfLocations == 0){
            runOnUiThread(() -> {
                Utilities.showAlert(this, "Must have at least 1 location entered");
            });
        }
        else {
            Intent intent = new Intent(this, CompassActivity.class);
            startActivity(intent);
        }
    }

    public void resetButtonClicked(View view) {

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("UIDs",MODE_PRIVATE);
        preferences.edit().clear().commit();

        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);


    }
}