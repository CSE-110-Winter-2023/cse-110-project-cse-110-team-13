package com.example.socialcompass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class LocationInput extends AppCompatActivity {

    //List of UIds that will be passed to compassActivity to create markers for each

    private TextView inputView;

    private final int UIDLENGTH = 32;

    private ExecutorService backgroundThreadExecutor = Executors.newSingleThreadExecutor();
    private Future<Boolean> future;
    private Future<Boolean> boolFuture;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_input);

        inputView = findViewById(R.id.InputUId);

    }

    //Checks if empty
    protected boolean empty(String input) {
        boolean empty = false;
        //run in background thread or else it will have async issues.
        this.future = backgroundThreadExecutor.submit(() -> input.equals(""));

        //try catch phrase to get java squiggly lines to stfu
        try {
            empty = future.get();
        }
        catch (Exception e) {
        }
        //there are 3 empty fields
        if (empty) {
            //Utilities.showAlert(this, "Please input UID");
            return true;
        }

        return false;
    }

    //checks if valid UID
    public boolean validUId(String input) {
        //32 characters, 10 digits A-F

        //check that input is made of correct characters
        if(!input.matches("-?[0-9a-fA-F]+")){
            runOnUiThread(() -> {
                Utilities.showAlert(this, "Your UID should be made of characters A-F, 0-9");
            });
            return false;
        }
        return true;
    }

    public boolean inputCheck(String input){

        if(empty(input)) return false;

        return asyncValidUId(input);
    }

    public boolean asyncValidUId(String input) {

        //async--alert issues dealing
        this.boolFuture = backgroundThreadExecutor.submit(() -> {
            //check type and validity for each field
            if(!validUId(input)) return false;

            return true;
        });

        boolean checkValid = false;
        //again -- try catch phrase to tell java to stfu
        try{
            checkValid = this.boolFuture.get();
        }
        catch(Exception e) {}

        return checkValid;
    }

    public void onSubmitButton(View view) {

        String input = inputView.getText().toString();

        if(!inputCheck(input)) return;

        //Temp stores each UID to UID pair

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("UIDs",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(input,input);
        editor.apply();

        inputView.setText("");

    }

    public void onExitClicked(View view) {

        finish();

    }
}
