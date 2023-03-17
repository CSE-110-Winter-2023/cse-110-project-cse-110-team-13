package com.example.socialcompass;


import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MockServerAPI implements Server {
    final String SERVERURL = "https://socialcompass.goto.ucsd.edu/location/";
    public ArrayList<Friend> mockFriendServer;
    public MockServerAPI() {
        mockFriendServer = new ArrayList<>();
        mockFriendServer.add(createVirtualFriend("0"));
        //add pointnemo
        mockFriendServer.add(new Friend("16724533","Point Nemo", -48.87666f,-123.39333f, true,
                "2023-02-18T12:00:00Z", "2023-02-18T18:30:00Z"));
    }
    public Friend createVirtualFriend(@NonNull String uuid) {
        return new Friend(uuid, "Mock friend", -15, 15, true, "1", "1");
    }
    public void pushVirtualFriend(@NonNull String uuid) {
        mockFriendServer.add(createVirtualFriend(uuid));
    }
    private Friend getFriend(@NonNull String uuid) {
        // URLs cannot contain spaces, so we replace them with %20.
        for(int i = 0; i < mockFriendServer.size(); i++) {
            if ( mockFriendServer.get(i).getUUIDAsString().equals(uuid))
                return mockFriendServer.get(i);
        }
        return null;
    }
    public Future<Friend> getFriendAsync(String uuid) {

        var executor = Executors.newSingleThreadExecutor();
        var future = executor.submit(() -> getFriend(uuid));

        return future;
    }

    private int putFriend(@NonNull Friend friend) {
        mockFriendServer.add(friend);
        return 200;
    }

    //asyncly puts friend into server
    public Future<Integer> putFriendAsync(Friend friend)
    {
        var executor = Executors.newSingleThreadExecutor();
        var future = executor.submit(() -> putFriend(friend));

        return future;
    }

    //deletes friend from server
    //should only be called asynchronously
    private int deleteFriend(@NonNull Friend friend)
    {
        for (int i = 0; i < mockFriendServer.size(); i++) {
            if( friend.getUUIDAsString().equals(mockFriendServer.get(i).getUUIDAsString())) {
                mockFriendServer.remove(i);
                return 200;
            }
        }
        return 440;
    }

    //asyncly deletes friend
    public Future<Integer> deleteFriendAsync(Friend friend)
    {
        var executor = Executors.newSingleThreadExecutor();
        var future = executor.submit(() -> deleteFriend(friend));

        return future;
    }

    public int patchFriend(@NonNull String privateCode, float lat, float lon)
    {
        for (int i = 0; i < mockFriendServer.size(); i++) {
            if( mockFriendServer.get(i).getUUIDAsString().equals(privateCode)) {
                mockFriendServer.get(i).latitude = lat;
                mockFriendServer.get(i).longitude = lon;
                return 200;
            }
        }
        return 440;
    }

    //asyncly patches updated friend coordinates to server
    public Future<Integer> patchFriendAsync(String privateCode, float lat, float lon)
    {
        Log.d("test6","in patch async");
        var executor = Executors.newSingleThreadExecutor();
        var future = executor.submit(() -> patchFriend(privateCode, lat, lon));

        return future;
    }

    private int patchFriend(@NonNull String privateCode, boolean isPublic)
    {
        for (int i = 0; i < mockFriendServer.size(); i++) {
            if( mockFriendServer.get(i).getUUIDAsString().equals(privateCode)) {
                mockFriendServer.get(i).publicFriend = isPublic;
                return 200;
            }
        }
        return 440;
    }

    //asyncly patches updated friend is public to server
    public Future<Integer> patchFriendAsync(String privateCode, boolean isPublic)
    {
        var executor = Executors.newSingleThreadExecutor();
        var future = executor.submit(() -> patchFriend(privateCode, isPublic));

        return future;
    }

}
