package com.example.socialcompass;


public class MarkerFactory {

    Marker currMarker;
    ServerAPI server = ServerAPI.provide();

    public MarkerFactory(){

    }

    public Marker createMarker(String UID){
        currMarker = new Marker(UID);
        var futureFriend = server.getFriendAsync(UID);
        Friend friend = null;
        try{
            friend  = futureFriend.get();
        }
        catch(Exception e){}

        currMarker.setLabel(friend.getLabel());
        currMarker.setCoordinate( String.valueOf(friend.getLatitude()) + String.valueOf(friend.getLongitude()));

        return currMarker;
    }



}
