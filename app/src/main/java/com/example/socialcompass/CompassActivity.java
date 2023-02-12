package com.example.socialcompass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class CompassActivity extends AppCompatActivity {
    private TextView myHomeLocation;
    private TextView myHomeLabel;
    private TextView familyLocation;
    private TextView familyLabel;
    private TextView friendLocation;
    private TextView friendLabel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
//        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
        Bundle extras = getIntent().getExtras();

        myHomeLocation = findViewById(R.id.myHomeLocation);
        myHomeLabel = findViewById(R.id.myHomeLabel);
        familyLocation = findViewById(R.id.familyLocation);
        familyLabel = findViewById(R.id.familyLabel);
        friendLocation = findViewById(R.id.friendLocation);
        friendLabel = findViewById(R.id.friendLabel);

        myHomeLocation.setText(extras.getString("myHomeLocation", "defaultMyHomeLocation"));
        myHomeLabel.setText(extras.getString("myHomeLabel", "defaultMyHomeLabel"));

        familyLocation.setText(extras.getString("familyLocation", "defaultFamilyLocation"));
        familyLabel.setText(extras.getString("familyLabel", "defaultFamilyLabel"));

        friendLocation.setText(extras.getString("friendLocation", "defaultFriendLocation"));
        friendLabel.setText(extras.getString("friendLabel", "defaultFriendLabel"));


    }

    public void goBackClicked(View view) {
        finish();
    }
}