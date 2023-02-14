package com.example.socialcompass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class LocationInput extends AppCompatActivity {
    private TextView myHomeLocation;
    private TextView myHomeLabel;
    private TextView familyLocation;
    private TextView familyLabel;
    private TextView friendLocation;
    private TextView friendLabel;
    private ExecutorService backgroundThreadExecutor = Executors.newSingleThreadExecutor();
    private Future<Integer> future;
    private Future<Void> voidFuture;
    private Future<Boolean> boolFuture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_input);
        // Get plain text ids for the fields
        myHomeLocation = findViewById(R.id.MyLocation);
        myHomeLabel = findViewById(R.id.MyLabel);
        familyLocation = findViewById(R.id.FamilyLocation);
        familyLabel = findViewById(R.id.FamilyLabel);
        friendLocation = findViewById(R.id.FriendLocation);
        friendLabel = findViewById(R.id.FriendLabel);


    }
    private boolean emptyInputCheck() {
        int finalInt = 0;
        //run in background thread or else it will have async issues.
        this.future = backgroundThreadExecutor.submit(() -> {
            int numEmpty = 0;
            //check each field if it is empty
            if (myHomeLocation.getText().toString().equals("")) {
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
        //try catch phrase to get java squiggly lines to stfu
        try {
            finalInt = future.get();
        }
        catch (Exception e) {
        }
        //there are 3 empty fields
        if (finalInt == 3) {
            Utilities.showAlert(this, "Please input at least one location!");
            return false;
        }
        //u're good
        return true;
    }
    //check if the type is actually float or not and if the user use too many arguments
    public boolean typeCheck(String inputCoordinateString) {
        //since the string is empty, the user didn't input anything in for this field
        // split the string using the delimiter ","
        String[] coordinateStringArr = inputCoordinateString.split(",");
        // check if there are exactly 2 arguments.
        if(coordinateStringArr.length != 2) {
            runOnUiThread(() -> {
                Utilities.showAlert(this, "One or more of your coordinates input are invalid");
            });
            return false;
        }

        float latitude = 0;
        float longitude = 0;
        // check if the string is actually a float
        try {
            latitude = Float.parseFloat(coordinateStringArr[0]);
            longitude = Float.parseFloat(coordinateStringArr[1]);
        }
        catch(Exception e) {
            runOnUiThread(() -> {
                Utilities.showAlert(this, "One or more of your coordinates input are invalid");
            });

            return false;
        }
        //pass it onto validCoordinateCheck to see if the coordinate is an actual existing coordinate.
        return validCoordinateCheck(latitude, longitude);
    }

    public boolean validCoordinateCheck(float latitude, float longitude) {
        //implement api to check if coordinate is a-ok (valid)!!!
        //there's no api yet so we'll use latitude in the range of -90 to 90 and longitude in the range of -180 to 180, (0,0) is probably GMT at the equator.
        if(latitude <= -90 || latitude >= 90) {
            runOnUiThread(() -> {
                Utilities.showAlert(this, "One or more of your coordinates input are invalid");
            });
            return false;
        }
        if(longitude <= -180 || longitude >= 180) {
            runOnUiThread(() -> {
                Utilities.showAlert(this, "One or more of your coordinates input are invalid");
            });
            return false;
        }

        return true;
    }

    public boolean faultyInputCheck() {
        //async--alert issues dealing
        this.boolFuture = backgroundThreadExecutor.submit(() -> {
            //check type and validity for each field

            String myHomeLocationString = myHomeLocation.getText().toString();
            if(!myHomeLocationString.equals(""))
                if(!typeCheck(myHomeLocationString)) return false;

            String familyLocationString = familyLocation.getText().toString();
            if(!familyLocationString.equals(""))
                if(!typeCheck(familyLocationString)) return false;

            String friendLocationString = friendLocation.getText().toString();
            if(!friendLocationString.equals(""))
                if(!typeCheck(friendLocationString)) return false;

            return true;
        });

        boolean finalCheck = false;
        //again -- try catch phrase to tell java to stfu
        try{
            finalCheck = this.boolFuture.get();
        }
        catch(Exception e) {

        }

        return finalCheck;
    }

    public boolean locationLabelPairCheck() {
        this.boolFuture = backgroundThreadExecutor.submit(() -> {
            String myHomeLocationString = myHomeLocation.getText().toString();
            String myHomeLabelString = myHomeLabel.getText().toString();
            String familyLocationString = familyLocation.getText().toString();
            String familyLabelString = familyLabel.getText().toString();
            String friendLocationString = friendLocation.getText().toString();
            String friendLabelString = friendLabel.getText().toString();

            if(myHomeLocationString.equals("") && !myHomeLabelString.equals("")) {
                runOnUiThread(() -> {
                    Utilities.showAlert(this, "One of your label doesn't have a location assigned to it!");
                });
                return false;
            }
            if(familyLocationString.equals("") && !familyLabelString.equals("")) {
                runOnUiThread(() -> {
                    Utilities.showAlert(this, "One of your label doesn't have a location assigned to it!");
                });
                return false;
            }
            if(friendLocationString.equals("") && !friendLabelString.equals("")) {
                runOnUiThread(() -> {
                    Utilities.showAlert(this, "One of your label doesn't have a location assigned to it!");
                });
                return false;
            }

            return true;
        });
        boolean finalBool = false;
        try {
            finalBool = boolFuture.get();
        }
        catch(Exception e) {

        }
        return finalBool;
    }

    public void onSubmitButton(View view) {
        //check if input query is empty
        if(!emptyInputCheck()) {
            return;
        }
        // check if label is assigned an empty coordinates
        if(!locationLabelPairCheck()) {
            return;
        }

        // check if coordinates is float and if valid
        // if all is valid, continue to pass it through to the other activity.
        if(!faultyInputCheck()) {
            return;
        }

        //bad implementation below because we're getting the floats from the fields again, possible refactoring needed in the future
        //will use the code below as placeholder for now

        String myHomeLocationString = myHomeLocation.getText().toString();
        String myHomeLabelString = myHomeLabel.getText().toString();
        String familyLocationString = familyLocation.getText().toString();
        String familyLabelString = familyLabel.getText().toString();
        String friendLocationString = friendLocation.getText().toString();
        String friendLabelString = friendLabel.getText().toString();

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("locationLabels",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Intent intent = new Intent(this, CompassActivity.class);

        if(!myHomeLocationString.equals("")) {
            editor.putString("myHomeLocation", myHomeLocationString);
            editor.putString("myHomeLabel", myHomeLabelString);
            intent.putExtra("myHomeLocation", myHomeLocationString);
            intent.putExtra("myHomeLabel", myHomeLabelString);
        }

        if(!familyLocationString.equals("")) {
            editor.putString("familyLocation", familyLocationString);
            editor.putString("familyLabel", familyLabelString);
            intent.putExtra("familyLocation", familyLocationString);
            intent.putExtra("familyLabel", familyLabelString);
        }

        if(!friendLocationString.equals("")) {
            editor.putString("friendLocation", friendLocationString);
            editor.putString("friendLabel", friendLabelString);
            intent.putExtra("friendLocation", friendLocationString);
            intent.putExtra("friendLabel", friendLabelString);
        }
        editor.apply();



        startActivity(intent);

    }
}
