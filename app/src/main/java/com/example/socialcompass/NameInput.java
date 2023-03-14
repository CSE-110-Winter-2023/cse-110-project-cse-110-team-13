package com.example.socialcompass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class NameInput extends AppCompatActivity {

    private final ExecutorService backgroundThreadExecutor = Executors.newSingleThreadExecutor();

    EditText inputView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_input);
        inputView = findViewById(R.id.NameInputField);
    }

    public static boolean staticEmpty(String str)
    {
        return str.equals("");
    }

    public static String NameMock(String str)
    {
        if (staticEmpty(str)) return "EMPTY STRING >:(";
        String myUUID = UUID.randomUUID().toString();
        //create friend
        boolean publicFriend = false;
        String createdAt = Instant.now().toString();
        String updatedAt = createdAt;

        Friend friend = new Friend(myUUID, str, -15,
                15, publicFriend, createdAt, updatedAt);
        ServerAPI api = ServerAPI.provide();
        Future<Integer> f = api.putFriendAsync(friend);
        while(!f.isDone());
        return myUUID;
    }

    public void onNameSubmit(View view)
    {
        String name = inputView.getText().toString();
        Log.d("Received name", name);
        if (empty(name)) return;
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("thisUserID",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String myUUID = UUID.randomUUID().toString();
        editor.putString("Name", name);
        editor.putString("UUID", myUUID);
        editor.apply();
        //create friend
        boolean publicFriend = false;
        String createdAt = Instant.now().toString();
        String updatedAt = createdAt;

        Friend friend = new Friend(myUUID, name, -15,
                15, publicFriend, createdAt, updatedAt);
        Log.d("UID is", myUUID);
        ServerAPI api = ServerAPI.provide();
        api.putFriendAsync(friend);
        finish();
    }

    protected boolean empty(String input) {
        boolean e = input.equals("");
        //there are 3 empty fields
        if (e)
            Utilities.showAlert(this, "Username cannot be empty!");
        return e;
    }
}