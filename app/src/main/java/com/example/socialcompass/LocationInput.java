package com.example.socialcompass;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class LocationInput extends AppCompatActivity {
    private TextView myLocation;
    private TextView myLabel;
    private TextView familyLocation;
    private TextView familyLabel;
    private TextView friendLocation;
    private TextView friendLabel;
    private ExecutorService backgroundThreadExecutor = Executors.newSingleThreadExecutor();
    private Future<Integer> future;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_input);
        // Get plain text ids for the fields
        myLocation = findViewById(R.id.MyLocation);
        myLabel = findViewById(R.id.MyLabel);
        familyLocation = findViewById(R.id.FamilyLocation);
        familyLabel = findViewById(R.id.FamilyLabel);
        friendLocation = findViewById(R.id.FriendLocation);
        friendLabel = findViewById(R.id.FriendLabel);


    }
    private boolean inputCheck() {
        int finalInt = 0;

        this.future = backgroundThreadExecutor.submit(() -> {
            int numEmpty = 0;

            if (myLocation.getText().toString().equals("")) {
                numEmpty++;
            }
            if (familyLocation.getText().toString().equals("")) {
                numEmpty++;
            }
            if (friendLocation.getText().toString().equals("")) {
                numEmpty++;
            }
            return numEmpty;
        });
        try {
            finalInt = future.get();
        }
        catch (Exception e) {
        }
        System.out.println(finalInt);
        if (finalInt == 3) {
            return false;
        }
        else {
            return true;
        }
    }
    public void onSubmitButton(View view) {
        if(!inputCheck()) {
            // alert
            Utilities.showAlert(this, "Please input at least one location!");
            return;
        }



    }
}