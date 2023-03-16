package com.example.socialcompass;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ServerAPI {

    //API instance which other classes may access
    private volatile static ServerAPI instance = null;

    //API client which does all the heavy lifing
    private final OkHttpClient client;

    private String SERVERURL;

    //constructor
    public ServerAPI() {
        SERVERURL = "https://socialcompass.goto.ucsd.edu/location/";
        this.client = new OkHttpClient();
    }

    //singleton provide method
    public static ServerAPI provide()
    {

        if (instance == null)
            instance = new ServerAPI();

        return instance;
    }

    public static void mockServerUrl(String serverUrl){
        instance.SERVERURL = serverUrl;
    }
    public String getSERVERURL() {
        return SERVERURL;
    }

    //URL at which locations are stored


    //gets friend from server
    //should only be called asynchronously
    @Nullable
    private Friend getFriend(@NonNull String uuid) {
        // URLs cannot contain spaces, so we replace them with %20.
        String uuidPath = uuid.replace(" ", "%20");

        Request request = new Request.Builder()
                .url(SERVERURL + uuidPath)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return Friend.fromJSON(response.body().string());
        }
        catch(Exception e){
            Log.e("Error getting note", e.toString());
        }
        return null;
    }

    //asyncly gets friend
    public Future<Friend> getFriendAsync(String uuid) {

        var executor = Executors.newSingleThreadExecutor();
        var future = executor.submit(() -> getFriend(uuid));

        return future;
    }

    //checks if UID is in the server
    //should only be called asynchronously
    @Nullable
    private boolean checkUID(@NonNull String uuid) {
        // URLs cannot contain spaces, so we replace them with %20.
        String uuidPath = uuid.replace(" ", "%20");

        Request request = new Request.Builder()
                .url(SERVERURL + uuidPath)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return true;
        }
        catch(Exception e){
            Log.e("Error getting note", e.toString());
        }
        return false;
    }

    //asyncly gets friend
    public Future<Boolean> checkUIDAsync(String uuid) {

        var executor = Executors.newSingleThreadExecutor();
        var future = executor.submit(() -> checkUID(uuid));

        return future;
    }




    //defines JSON MediaType for use by other methods
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    //puts friend into server
    //should only be called asynchronously
    private int putFriend(@NonNull Friend friend) {
        PutFriend pf = PutFriend.createNewFromFriend(friend);
        RequestBody body = RequestBody.create(pf.toJSON(), JSON);
        Request request = new Request.Builder()
                .url(SERVERURL + (pf.getPrivateCode().replace(" ", "%20")))
                .put(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.code();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return 0;
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
        RequestBody body = RequestBody.create("{\n\"private_code\": " + friend.getUUIDAsString() + "\n}", JSON);
        Request request = new Request.Builder()
                .url(SERVERURL + (friend.getUUIDAsString().replace(" ", "%20")))
                .delete(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.code();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return 0;
    }

    //asyncly deletes friend
    public Future<Integer> deleteFriendAsync(Friend friend)
    {
        var executor = Executors.newSingleThreadExecutor();
        var future = executor.submit(() -> deleteFriend(friend));

        return future;
    }

    //patches updated friend coordinates to server
    //should only be called asynchronously
    public int patchFriend(@NonNull String privateCode, float lat, float lon)
    {
        RequestBody body = RequestBody.create("{\n\"private_code\": \"" + privateCode + "\",\n"
                + "\"latitude\": " + lat + ",\n"
                + "\"longitude\": " + lon + "\n}", JSON);
        Request request = new Request.Builder()
                .url(SERVERURL + (privateCode.replace(" ", "%20")))
                .patch(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.code();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return 0;
    }

    //asyncly patches updated friend coordinates to server
    public Future<Integer> patchFriendAsync(String privateCode, float lat, float lon)
    {
        Log.d("test6","in patch async");
        var executor = Executors.newSingleThreadExecutor();
        var future = executor.submit(() -> patchFriend(privateCode, lat, lon));

        return future;
    }



    //patches updated friend label to server
    //should only be called asynchronously
    private int patchFriend(@NonNull String privateCode, String label)
    {
        RequestBody body = RequestBody.create("{\n\"private_code\": \"" + privateCode + "\",\n"
                + "\"label\": \"" + label + "\"\n}", JSON);
        Request request = new Request.Builder()
                .url(SERVERURL + (privateCode.replace(" ", "%20")))
                .patch(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.code();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return 0;
    }

    //asyncly patches updated friend label to server
    public Future<Integer> patchFriendAsync(String privateCode, String label)
    {
        var executor = Executors.newSingleThreadExecutor();
        var future = executor.submit(() -> patchFriend(privateCode, label));

        return future;
    }

    //patches updated whether friend is public to server
    //should only be called asynchronously
    private int patchFriend(@NonNull String privateCode, boolean isPublic)
    {
        RequestBody body = RequestBody.create("{\n\"private_code\": " + privateCode + ",\n"
                + "\"is_listed_publicly\": \"" + isPublic + "\"\n}", JSON);
        Request request = new Request.Builder()
                .url(SERVERURL + (privateCode.replace(" ", "%20")))
                .patch(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.code();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return 0;
    }

    //asyncly patches updated friend is public to server
    public Future<Integer> patchFriendAsync(String privateCode, boolean isPublic)
    {
        var executor = Executors.newSingleThreadExecutor();
        var future = executor.submit(() -> patchFriend(privateCode, isPublic));

        return future;
    }
}
